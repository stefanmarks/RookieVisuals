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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import modifier.ConstantChange;
import modifier.AudioSignal_FrequencyIntensity;
import modifier.Oscillator;
import modifier.RandomGlitch;
import modifier.SetTranslation;
import modifier.SetScale;
import modifier.SetValue;
import processing.core.PApplet;
import timing.TimeBase;
import visual.SplitImage;
import visual.Stripes;
import visual.Visual;

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
        visuals  = new LinkedList<>();
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
            final int WINDOW_HEIGHT = WINDOW_WIDTH * 9 / 16; // 16:9 ratio
            size(WINDOW_WIDTH, WINDOW_HEIGHT, P3D);
        }
        
        frameRate(timeBase.frameRate);
        noCursor();

        // find inputs
        audioManager = new AudioManager();
        List<AudioInput> inputs = audioManager.getInputs();
//        System.out.println("Audio Inputs: ");
//        for ( int i = 0 ; i < inputs.size() ; i++ )
//        {
//            System.out.println(i + ":\t" + inputs.get(i));
//        }
        AudioInput input = inputs.get(0);
        minim = new Minim(this);
        minim.setInputMixer(input.getMixer());
        // create audio analyser
        audioAnalyser = new SpectrumAnalyser(25, 100, 1, 1);
        // attach input to audio analyser
        audioAnalyser.attachToAudio(minim.getLineIn());
        
        setupVisuals();
        setupOSC();
    }
    
    
    /**
     * Sets up the visuals.
     */
    private void setupVisuals()
    {
        Visual v = new SplitImage("RookieLogo", loadImage("rookie_logo_black.png"));
        v.addModifier(new SetTranslation(width / 2, height / 2));
        v.addModifier(new SetScale(300)); 
        v.addModifier(new RandomGlitch("split", 0.01f, -0.1f, 0.1f));
        v.addModifier(new SetValue("angle", 0));
        v.addModifier(new Oscillator("angle", 0.01f, 0, 10));
        visuals.add(v);

        Stripes s1 = new Stripes(width, height, -40, 40, 4.0f);
        s1.addModifier(new SetTranslation(-(width - height) / 2, 0)); 
        s1.addModifier(new ConstantChange("offset", 0.5f));
        s1.addModifier(new AudioSignal_FrequencyIntensity("stroke", audioAnalyser, 2, 4, 20)); // react to low frequencies
        visuals.add(s1);
        
        Stripes s2 = new Stripes(width, height, -40, 40, 4.0f);
        s2.addModifier(new SetTranslation(width - (width - height) / 2, 0)); 
        s2.addModifier(new ConstantChange("offset", -0.5f));
        s2.addModifier(new AudioSignal_FrequencyIntensity("stroke", audioAnalyser, 5, 4, 20)); // react to high frequencies
        visuals.add(s2);
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
    
    
    /**
     * Draws a single frame.
     */
    @Override
    public synchronized void draw()
    {
        timeBase.tick();
        
        // animate visuals
        for ( Visual visual : visuals )
        {
              visual.update(timeBase);
        }

        // render visuals
        background(255);
        for ( Visual visual : visuals )
        {
            pushMatrix();
            pushStyle();
            visual.render(this.g);
            popStyle();
            popMatrix();
        }
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
    private final List<Visual>  visuals;
} 

