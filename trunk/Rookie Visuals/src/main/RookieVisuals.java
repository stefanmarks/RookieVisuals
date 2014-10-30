package main;

import analyser.AudioInput;
import analyser.AudioManager;
import analyser.SpectrumAnalyser;
import com.illposed.osc.OSCParameter;
import com.illposed.osc.OSCParameterListener;
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
import modifier.OSCVariable;
import modifier.OffsetValue;
import modifier.RandomGlitch;
import modifier.SetRotation;
import modifier.SetTranslation;
import modifier.SetScale;
import modifier.TimedOffset;
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
    public static final String VERSION = "1.1";
    
    public static final String CONFIG_FILE = "./config.txt";
    public static final String NAME_FILE   = "./names.txt";
    
    
    /**
     * Creates an instance of the Rookie Visuals program.
     * 
     * @param usesAudio  <code>true</code> if program does sound analysis,
     *                   <code>false</code> if program relies on OSC sound analysis variables
     * @param fullscreen <code>true</code> if program should run in fullscreen mode,
     *                   <code>false</code> if program should run in window
     * @param mirrored   <code>true</code> if program should run in mirror mode,
     *                   <code>false</code> if not
     */
    public RookieVisuals(boolean usesAudio, boolean fullscreen, boolean mirrored)
    {
        this.usesAudio       = usesAudio;
        this.runInFullscreen = fullscreen;
        this.mirrored        = mirrored;
        
        timeBase      = new TimeBase(60);
        visualManager = new VisualManager();
        vars          = new RookieVisualsVariables();
        nameRowCount  = 0;
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
        
        if ( usesAudio )
        {
            // find inputs
            audioManager = new AudioManager();
            List<AudioInput> inputs = audioManager.getInputs();
            AudioInput input = inputs.get(0);
            minim = new Minim(null);
            minim.setInputMixer(input.getMixer());

            // create audio analyser
            audioAnalyser = new SpectrumAnalyser(25, 20, 3, 1);
            // attach input to audio analyser
            audioAnalyser.attachToAudio(minim.getLineIn());
        }
                
        loadNames();
        setupVisuals();
        setupOSC();
        
        vars.currentName.registerListener(new NameChangeListener());
        vars.logoVisible.registerListener(new LogoChangeListener());
    }

    
    /**
     * Loads and prepares the name strings
     */
    private void loadNames()
    {
        names = loadStrings(NAME_FILE);
        nameIdx = 0;
    }
    
    
    /**
     * Sets up the visuals.
     */
    private void setupVisuals()
    {
        visLogo = new SplitImage("RookieLogo", loadImage("rookie_logo_black.png"), mirrored ? -45 : 45);
        visLogo.addModifier(new SetScale(0.5f)); 
        visLogo.addModifier(new RandomGlitch("split", 0.01f, -0.1f, 0.1f));
        visLogo.setEnabled(vars.logoVisible.get());
        visualManager.add(visLogo);

        visName = getNameVisual(vars.currentName.get());
        visualManager.add(visName);

        visCurtain1 = new Stripes("StripesL", 5, 2.0f, 1.2f, mirrored ? 0.1f : -0.1f, 0.1f, 0.01f);
        visCurtain1.addModifier(new SetTranslation(mirrored ? -0.6f : -1.4f, -0.6f)); 
        visCurtain1.addModifier(new ConstantChange("offset", 0.5f));
        if ( usesAudio )
        {
            visCurtain1.addModifier(new AudioSignal_FrequencyIntensity("stroke[0]", audioAnalyser,  2, 0.01f, 0.1f)); // react to low frequencies
            visCurtain1.addModifier(new AudioSignal_FrequencyIntensity("stroke[1]", audioAnalyser,  5, 0.01f, 0.2f));
            visCurtain1.addModifier(new AudioSignal_FrequencyIntensity("stroke[2]", audioAnalyser,  8, 0.01f, 0.3f));
            visCurtain1.addModifier(new AudioSignal_FrequencyIntensity("stroke[3]", audioAnalyser, 12, 0.01f, 0.2f));
            visCurtain1.addModifier(new AudioSignal_FrequencyIntensity("stroke[4]", audioAnalyser, 17, 0.01f, 0.3f));
        }
        else
        {
            visCurtain1.addModifier(new OSCVariable("stroke[0]", vars.spectrum[0], 0.01f, 0.1f)); // react to low frequencies
            visCurtain1.addModifier(new OSCVariable("stroke[1]", vars.spectrum[2], 0.01f, 0.2f));
            visCurtain1.addModifier(new OSCVariable("stroke[2]", vars.spectrum[4], 0.01f, 0.3f));
            visCurtain1.addModifier(new OSCVariable("stroke[3]", vars.spectrum[6], 0.01f, 0.2f));
            visCurtain1.addModifier(new OSCVariable("stroke[4]", vars.spectrum[8], 0.01f, 0.3f)); // react to high frequencies
        }
        visualManager.add(visCurtain1);
        
        visCurtain2 = new Stripes("StripesR", 5, 2.0f, 1.2f, mirrored ? 0.1f : -0.1f, 0.1f, 0.01f);
        visCurtain2.addModifier(new SetTranslation(mirrored ? -2.6f : 0.6f, -0.6f)); 
        visCurtain2.addModifier(new ConstantChange("offset", -0.5f));
        if ( usesAudio )
        {        
            visCurtain2.addModifier(new AudioSignal_FrequencyIntensity("stroke[0]", audioAnalyser,  3, 0.01f, 0.1f)); // react to high frequencies
            visCurtain2.addModifier(new AudioSignal_FrequencyIntensity("stroke[1]", audioAnalyser,  7, 0.01f, 0.2f));
            visCurtain2.addModifier(new AudioSignal_FrequencyIntensity("stroke[2]", audioAnalyser, 10, 0.01f, 0.3f));
            visCurtain2.addModifier(new AudioSignal_FrequencyIntensity("stroke[3]", audioAnalyser, 15, 0.01f, 0.4f));
            visCurtain2.addModifier(new AudioSignal_FrequencyIntensity("stroke[4]", audioAnalyser, 20, 0.01f, 0.3f));
        }
        else
        {
            visCurtain2.addModifier(new OSCVariable("stroke[0]", vars.spectrum[1], 0.01f, 0.1f)); // react to low frequencies
            visCurtain2.addModifier(new OSCVariable("stroke[1]", vars.spectrum[3], 0.01f, 0.2f));
            visCurtain2.addModifier(new OSCVariable("stroke[2]", vars.spectrum[5], 0.01f, 0.3f));
            visCurtain2.addModifier(new OSCVariable("stroke[3]", vars.spectrum[7], 0.01f, 0.4f));
            visCurtain2.addModifier(new OSCVariable("stroke[4]", vars.spectrum[9], 0.01f, 0.3f)); // react to high frequencies
        }
        visualManager.add(visCurtain2);
        
        curtainOffset    = new OffsetValue[2];
        curtainOffset[0] = new OffsetValue("tX", 0);
        visCurtain1.addModifier(curtainOffset[0]);
        curtainOffset[1] = new OffsetValue("tX", 0);
        visCurtain2.addModifier(curtainOffset[1]);
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
            vars.registerWithInputPort(oscReceiver);
            // and ready to receive
            oscReceiver.startListening();
        }
        catch (SocketException e)
        {
            System.err.println("Could not start OSC receiver (" + e + ")");
        }
    }
    
    
    private Visual getNameVisual(String name)
    {
        nameRowCount = 0; 
                
        if ( !name.isEmpty() )
        {
            // split into parts between spaces
            String[]   nameParts = name.trim().split(" ");
            // prepare name matrix
            String[][] partMatrix = new String[2][nameParts.length];
            int col = 0; // column index
            int row = 0; // row index
            
            while ( row < nameParts.length )
            {
                String  part    = nameParts[row];
                boolean newName = part.equals("&");
                if ( newName )
                {
                    col = 0; // start first column again
                }
                partMatrix[col][row] = part;
                if ( !newName )
                {   
                    // first part of name has been done, next column
                    col = 1;
                }
                row++;
            }
            
            // add aligning spaces
            final String spaces = "                                                                                                                                                          ";
            for ( int r = 0 ; r < row ; r++ )
            {
                if ( partMatrix[0][r] == null )
                {
                    partMatrix[0][r] = spaces.substring(0, partMatrix[1][r].length() * 3);
                }
                else
                {
                    partMatrix[1][r] = spaces.substring(0, partMatrix[0][r].length() * 3);
                }
            }
            // assemble final string
            name = "";
            for ( int r = 0 ; r < row ; r++ )
            {
                for ( int c = 0 ; c < 2 ; c++ )
                { 
                    name += partMatrix[c][r];
                }
                if ( r < row - 1 ) // don't add at end
                {
                    nameRowCount++;
                    name += "\n";
                } 
            }
        }
        
        PGraphics i = createGraphics(1000, 400);
        PFont     f = createFont("TheFont.otf", 1, true);
        i.textFont(f);
        i.textSize(65);
        i.textAlign(mirrored ? LEFT : RIGHT);
        i.textLeading(45);
        i.fill(color(0));
        i.text(name.toUpperCase(), mirrored ? 10 : 990, 222 - 45 * nameRowCount / 2);
        Visual v = new SplitImage("Name", i, mirrored ? -45 : 45);
        v.addModifier(new SetScale(0.25f)); 
        v.addModifier(new SetRotation(mirrored ? 45 : -45));
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
        
        checkCurtain();
        
        background(color(255));
        
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
                        vars.currentName.set(names[nameIdx]);
                    }
                    break;
                }
                
                case 'n' : // enable/disable name
                {
                    vars.logoVisible.set(false);
                    vars.currentName.set(vars.currentName.get().isEmpty() ? names[nameIdx] : "");
                    break;
                }
                
                case 'l' :
                {
                    vars.logoVisible.set(!vars.logoVisible.get());
                    vars.currentName.set("");
                    break;
                }
                
                case 'c' :
                {
                    vars.curtainOpen.set(!vars.curtainOpen.get());
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
        if ( audioAnalyser != null )
        {
            audioAnalyser.detachFromAudio();
            if ( minim != null )
            {
                minim.stop();
            }
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
        Boolean runRemoteControl = null;
        Boolean usesAudio  = null;
        Boolean fullscreen = null;
        Boolean mirrored   = null;
        for ( String arg : args )
        {
            arg = arg.toLowerCase();
            if      ( arg.equals("-r" ) ) { runRemoteControl = true; }
            else if ( arg.equals("-a" ) ) { usesAudio  = true;  }
            else if ( arg.equals("-na") ) { usesAudio  = false; }
            else if ( arg.equals("-f" ) ) { fullscreen = true;  }
            else if ( arg.equals("-w" ) ) { fullscreen = false; }
            else if ( arg.equals("-m" ) ) { mirrored   = true;  }
            else if ( arg.equals("-nm") ) { mirrored   = false; }
            else
            {
                System.err.println("Unknown command line option " + arg);
            }
        }
        
        if ( runRemoteControl == null )
        {
            runRemoteControl = checkOption(
                    "Do you want to run the Remote Control?",
                    "Run Remote Control?");
        }
        
        if ( runRemoteControl )
        {
            RookieVisualsRemoteController.main(args);
            System.exit(0);
        }
        
        if ( usesAudio == null )
        {
            // no command line option given -> ask user
            usesAudio = checkOption(
                    "Do you want the the program to use the audio input?",
                    "Use Audio Input?"); 
        }
        
        if ( fullscreen == null )
        {
            // no command line option given -> ask user
            fullscreen = checkOption(
                    "Do you want to run the program in fullscreen mode?",
                    "Run in Fullscreen Mode?"); 
        }
        
        if ( mirrored == null )
        {
            // no command line option given -> ask user
            mirrored = checkOption(
                    "Do you want to run the program mirrored?",
                    "Run in Mirror Mode?"); 
        }

        // create program instance
        final RookieVisuals p = new RookieVisuals(usesAudio, fullscreen, mirrored);

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
    
    
    private void checkCurtain()
    {
        float targetValue = 0;
        if ( vars.curtainOpen.get() && visName.isEnabled() )
        {
            targetValue = (nameRowCount + 1) * 0.04f * (mirrored ? -1 : 1);
        }
        
        curtainOffset[0].lerpOffset(-targetValue, 2 * timeBase.frameTime);
        curtainOffset[1].lerpOffset( targetValue, 2 * timeBase.frameTime);
    }
    
    
    private static boolean checkOption(String message, String title)
    {
        int choice = JOptionPane.showConfirmDialog(null, message, title,
                        JOptionPane.YES_NO_CANCEL_OPTION);
        if ( (choice == JOptionPane.CLOSED_OPTION) ||
             (choice == JOptionPane.CANCEL_OPTION) ) 
        {
            // User doesn't want to run this at all
            System.exit(0); 
        }
        
        return choice == JOptionPane.YES_OPTION;
    }     
       
    
    /**
     * Listener for changes in the current name.
     */
    private class NameChangeListener implements OSCParameterListener<String>
    {
        @Override
        public void valueChanged(OSCParameter<String> param)
        {
            if ( param.get().isEmpty() )
            {
                visName.setEnabled(false);
            }
            else
            {
                Visual vNew = getNameVisual(param.get());
                visualManager.replace(visName, vNew);
                visName = vNew;
                vars.logoVisible.set(false);
            }
        }
    }
    
    
    /**
     * Listener for changes in the curtain opening.
     */
    private class LogoChangeListener implements OSCParameterListener<Boolean>
    {
        @Override
        public void valueChanged(OSCParameter<Boolean> param)
        {
            if ( param.get() != visLogo.isEnabled() )
            {
                visLogo.setEnabled(param.get());
            }
        }
    }

    
    private final boolean  usesAudio;
    private final boolean  runInFullscreen;
    private final boolean  mirrored;
    
    private final RookieVisualsVariables vars;
    private       OSCPortIn              oscReceiver;
    
    // live audio input
    private AudioManager      audioManager;
    private Minim             minim;
    private SpectrumAnalyser  audioAnalyser;
    
    // Visuals
    private final TimeBase      timeBase;
    private final VisualManager visualManager;
    
    private Visual        visLogo, visName, visCurtain1, visCurtain2;
    private int           nameRowCount;
    private OffsetValue[] curtainOffset;
    
    private String[] names;
    private int      nameIdx;
} 

