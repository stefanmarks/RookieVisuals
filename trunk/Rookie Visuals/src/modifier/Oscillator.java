package modifier;

import timing.TimeBase;

/**
 * Modifier that constantly changes a parameter.
 * 
 * @author  Stefan Marks
 */
public class Oscillator extends AbstractSingleModifier
{
    public Oscillator(String paramName, float frequency, float phase, float magnitude)
    {
        super(paramName);
        this.frequency = frequency;
        this.angle     = phase;
        this.magnitude = magnitude;
    }
    
    
    @Override
    public void apply(TimeBase timeBase)
    {
        angle += timeBase.frameTime * frequency * 360;
        if ( angle > 360 ) { angle -= 360; }
        if ( angle <   0 ) { angle += 360; }
        parameter.set(parameter.get() + magnitude * (float) Math.sin(Math.toRadians(angle)));
    }

    
    private final float frequency, magnitude;
    private       float angle;
}
