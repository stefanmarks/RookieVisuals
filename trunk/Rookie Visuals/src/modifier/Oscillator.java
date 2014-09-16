package modifier;

import timing.TimeBase;

/**
 * Modifier that constantly changes a parameter.
 * 
 * @author  Stefan Marks
 */
public class Oscillator extends AbstractSingleModifier
{
    /**
     * Creates a new oscillator modifier.
     * 
     * @param paramName  the name of the parameter to modify
     * @param frequency  the frequency of the oscillator in Hz
     * @param phase      the start phase in degrees
     * @param bias       the signal bias
     * @param amplitude  the signal magnitude
     */
    public Oscillator(String paramName, float frequency, float phase, float bias, float amplitude)
    {
        super(paramName);
        this.frequency = frequency;
        this.angle     = phase;
        this.bias      = bias;
        this.amplitude = amplitude;
    }
    
    
    @Override
    public void apply(TimeBase timeBase)
    {
        angle += timeBase.frameTime * frequency * 360;
        if ( angle > 360 ) { angle -= 360; }
        if ( angle <   0 ) { angle += 360; }
        parameter.set(bias + amplitude * (float) Math.sin(Math.toRadians(angle)));
    }

    
    private final float frequency, bias, amplitude;
    private       float angle;
}
