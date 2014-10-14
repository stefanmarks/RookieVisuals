package main;

import analyser.AudioInput;
import analyser.AudioManager;
import analyser.SpectrumAnalyser;
import com.illposed.osc.OSCPort;
import com.illposed.osc.OSCPortIn;
import ddf.minim.Minim;
import java.awt.Dimension;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.SocketException;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import modifier.ConstantChange;
import modifier.AudioSignal_FrequencyIntensity;
import modifier.RandomGlitch;
import modifier.SetRotation;
import modifier.SetTranslation;
import modifier.SetScale;
import modifier.TimedChange;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import timing.TimeBase;
import visual.SplitImage;
import visual.Stripes;
import visual.Visual;
import visual.VisualManager;

/**
 * Program for the background visuals of the Rookie 2014 show.
 * 
 * @author Stefan Marks
 * @author Dave Brown
 * @author Gerbrand van Melle
 */
public class RookieVisuals extends PApplet
{
    public static final String VERSION = "1.0";
    
    public static final String CONFIG_FILE = "./config.txt";
    public static final String NAME_FILE   = "./names.txt";
    
    
    /**
     * Creates an instance of the Rookie Visuals program.
     * 
     * @param fullscreen <code>true</code> if program should run in fullscreen mode,
     *                   <code>false</code> if program should run in window
     */
    public RookieVisuals(boolean fullscreen)
    {
        this.runInFullscreen = fullscreen;
        
        timeBase = new TimeBase(60);
        visualManager = new VisualManager();
    }
    
         
    /**
     * Sets up the program.
     */
    @Override
    public void setup()
    {
        if ( runInFullscreen )
        {
            size(displayWidth, displayHeight, OPENGL);
        }
        else
        {
            final int WINDOW_WIDTH  = 1200;
            final int WINDOW_HEIGHT = WINDOW_WIDTH * 9 / 16; // 4:3 ratio
            size(WINDOW_WIDTH, WINDOW_HEIGHT, P3D);
        }
        
        frameRate(timeBase.frameRate);
        noCursor();

        // find inputs
        audioManager = new AudioManager();
        List<AudioInput> inputs = audioManager.getInputs();
        AudioInput input = inputs.get(0);
        minim = new Minim(this);
        minim.setInputMixer(input.getMixer());
        
        // create audio analyser
        audioAnalyser = new SpectrumAnalyser(25, 20, 3, 1);
        // attach input to audio analyser
        audioAnalyser.attachToAudio(minim.getLineIn());
        
        loadNames();
        setupVisuals();
        setupOSC();
    }

    
    /**
     * Loads and prepares the name strings
     */
    private void loadNames()
    {
        final String spaces = "                                              ";
        
        names = loadStrings(NAME_FILE);
        
        for (int i = 0; i < names.length; i++)
        {
            String name = names[i].trim();
            name     = name.replaceFirst(" ", "\n");
            names[i] = name + spaces.substring(0, name.indexOf('\n') * 4);
        }
        
        nameIdx = 0;
    }
    
    
    /**
     * Sets up the visuals.
     */
    private void setupVisuals()
    {
        visLogo = new SplitImage("RookieLogo", loadImage("rookie_logo_black.png"));
        visLogo.addModifier(new SetScale(0.5f)); 
        visLogo.addModifier(new RandomGlitch("split", 0.01f, -0.1f, 0.1f));
        visualManager.add(visLogo);

        visName = getNameVisual("");
        visualManager.add(visName);

        PGraphics i2 = createGraphics(100, 100);
        i2.fill(color(255));
        i2.rect(0, 0, 100, 100);
        Visual v = new SplitImage("Blind", i2);
        v.addModifier(new ConstantChange("tY", -0.2f));
        //visualManager.add(v);

        visCurtain1 = new Stripes("StripesL", 5, 2.0f, 1.0f, -0.1f, 0.1f, 0.01f);
        visCurtain1.addModifier(new SetTranslation(-1.5f, -0.5f)); 
        visCurtain1.addModifier(new ConstantChange("offset", 0.5f));
        visCurtain1.addModifier(new AudioSignal_FrequencyIntensity("stroke[0]", audioAnalyser,  2, 0.01f, 0.1f)); // react to low frequencies
        visCurtain1.addModifier(new AudioSignal_FrequencyIntensity("stroke[1]", audioAnalyser,  5, 0.01f, 0.2f));
        visCurtain1.addModifier(new AudioSignal_FrequencyIntensity("stroke[2]", audioAnalyser,  8, 0.01f, 0.3f));
        visCurtain1.addModifier(new AudioSignal_FrequencyIntensity("stroke[3]", audioAnalyser, 12, 0.01f, 0.2f));
        visCurtain1.addModifier(new AudioSignal_FrequencyIntensity("stroke[4]", audioAnalyser, 17, 0.01f, 0.3f));
        visualManager.add(visCurtain1);
        
        visCurtain2 = new Stripes("StripesR", 5, 2.0f, 1.0f, -0.1f, 0.1f, 0.01f);
        visCurtain2.addModifier(new SetTranslation(0.5f, -0.5f)); 
        visCurtain2.addModifier(new ConstantChange("offset", -0.5f));
        visCurtain2.addModifier(new AudioSignal_FrequencyIntensity("stroke[0]", audioAnalyser,  3, 0.01f, 0.1f)); // react to high frequencies
        visCurtain2.addModifier(new AudioSignal_FrequencyIntensity("stroke[1]", audioAnalyser,  7, 0.01f, 0.2f));
        visCurtain2.addModifier(new AudioSignal_FrequencyIntensity("stroke[2]", audioAnalyser, 10, 0.01f, 0.3f));
        visCurtain2.addModifier(new AudioSignal_FrequencyIntensity("stroke[3]", audioAnalyser, 15, 0.01f, 0.4f));
        visCurtain2.addModifier(new AudioSignal_FrequencyIntensity("stroke[4]", audioAnalyser, 20, 0.01f, 0.3f));
        visualManager.add(visCurtain2);
        
        curtainOpen = false;
    }
    
    /**
     * Sets up the OSC receiver.
     */
    private void setupOSC()
    {
        try
        {
            // open port
            oscReceiver = new OSCPortIn(OSCPort.defaultSCOSCPort());
            // register listener (reacts to every incoming message)
            oscReceiver.startListening();
        }
        catch (SocketException e)
        {
            System.err.println("Could not start OSC receiver (" + e + ")");
        }
    }
    
    
    private Visual getNameVisual(String name)
    {
        PGraphics i = createGraphics(1000, 400);
        PFont     f = createFont("TheFont.otf", 1, true);
        i.textFont(f);
        i.textSize(65);
        i.textAlign(RIGHT);
        i.textLeading(45);
        i.fill(color(0));
        i.text(name.toUpperCase(), 990, 220);
        Visual v = new SplitImage("Name", i);
        v.addModifier(new SetTranslation(-0.035f, 0)); 
        v.addModifier(new SetScale(0.25f)); 
        v.addModifier(new SetRotation(-45));
        v.addModifier(new RandomGlitch("tX", 0.02f, -0.2f, 0.1f));
        return v;
    }
    
    /**
     * Draws a single frame.
     */
    @Override
    public synchronized void draw()
    {
        timeBase.tick();
        visualManager.update(timeBase);
        
        background(color(255));
        
                //fill(color(255, 20));
        //rect(0, 0, width, height);
        
        translate(width / 2, height / 2); // screen centre is 0/0
        scale(height);                    // Screen height rangs from -0.5 to 0.5
        visualManager.render(this.g);
    }

    
    /**
     * Called when a key is pressed
     */
    @Override
    public void keyPressed()
    {
        if ( key != CODED )
        {
            // plain keypress
            switch ( key )
            {
                case ' ' : // next name
                {
                    if ( nameIdx < names.length - 1 )
                    {
                        nameIdx++;
                        Visual vNew = getNameVisual(names[nameIdx]);
                        visualManager.replace(visName, vNew);
                        visName = vNew;
                        visLogo.setEnabled(false);
                    }
                    break;
                }
                
                case 'n' : // enable/disable name
                {
                    visLogo.setEnabled(false);
                    visName.setEnabled(!visName.isEnabled());
                    break;
                }
                
                case 'l' :
                {
                    visLogo.setEnabled(!visLogo.isEnabled());
                    visName.setEnabled(false);
                    break;
                }
                
                case 't' :
                {
                    float dX = curtainOpen ? -0.1f : 0.1f;
                    visCurtain1.addModifier(new TimedChange("tX", -dX, 1));
                    visCurtain2.addModifier(new TimedChange("tX",  dX, 1));
                    visLogo.addModifier(new TimedChange("split", 20 * dX, 1));
                    curtainOpen = !curtainOpen;
                    break;
                }
            }
        }
        else
        {
            // special key
            switch ( keyCode )
            {
            }
        }
    }
    
    
    /**
     * Checks if the applet should run fullscreen.
     * 
     * @return <code>true</code> if applet should run fullscreen,
     *         <code>false</code> if not
     */
    @Override
    public boolean sketchFullScreen()
    {
        return runInFullscreen;
    }

    
    /**
     * Disposes of the applet.
     */
    @Override
    public void dispose()
    {        
        audioAnalyser.detachFromAudio();
        if ( minim != null )
        {
            minim.stop();
        }
        
        super.dispose();
    }
    
    
    /**
     * Main method for the program.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args)
    {
        // Prepare a better logging output
        // get toplevel logger
        Logger logger = Logger.getLogger("");
        // get rid of standard handler
        logger.removeHandler(logger.getHandlers()[0]);
        // add custom handler
        Handler handler = new CompactLogHandler();
        logger.addHandler(handler);     

        // check command line parameters
        Boolean fullscreen = null;
        for ( String arg : args )
        {
            if ( arg.equalsIgnoreCase("-f") ) 
            {
                fullscreen = true; 
            }
            else if ( arg.equalsIgnoreCase("-w") ) 
            {
                fullscreen = false; 
            }
            else
            {
                System.err.println("Unknown command line option " + arg);
            }
        }
        
        if ( fullscreen == null )
        {
            // no command line option given -> ask user
            fullscreen = checkFullscreen(); 
        }
        
        // create program instance
        final RookieVisuals p = new RookieVisuals(fullscreen);

        // in case of an exception, show a swing dialog box
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                StringWriter sw = new StringWriter();
                PrintWriter  pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                JTextArea txt = new JTextArea(sw.getBuffer().toString());
                txt.setEditable(false);
                JScrollPane scrl = new JScrollPane(txt);
                scrl.setPreferredSize(new Dimension(700, 400));
                JOptionPane.showMessageDialog(
                        p.frame, 
                        scrl,
                        "Sorry, but there was a problem...", 
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });
        
        runSketch(new String[]
        {
            "Rookie Visuals v" + VERSION
        }, p);
    }
    
    
    private static boolean checkFullscreen()
    {
        // check if fullscreen mode is desired
        int choice = JOptionPane.showConfirmDialog(null,
                        "Do you want to run the program in fullscreen mode?",
                        "Run in Fullscreen Mode?",
                        JOptionPane.YES_NO_CANCEL_OPTION);
        if ( (choice == JOptionPane.CLOSED_OPTION) ||
             (choice == JOptionPane.CANCEL_OPTION) ) 
        {
            // User doesn't want to run this at all
            System.exit(0); 
        }
        
        return choice == JOptionPane.YES_OPTION;
    }
    
    
    private final boolean  runInFullscreen;
    private OSCPortIn      oscReceiver;
    
    // live audio input
    private AudioManager      audioManager;
    private Minim             minim;
    private SpectrumAnalyser  audioAnalyser;
    
    // Visuals
    private final TimeBase      timeBase;
    private final VisualManager visualManager;
    
    private Visual   visLogo, visName, visCurtain1, visCurtain2;
    private boolean  curtainOpen;
    
    private String[] names;
    private int      nameIdx;
} 

