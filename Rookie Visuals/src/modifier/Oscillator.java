package modifier;

import parameter.DefaultParameter;
import parameter.ModuloParameter;
import parameter.Parameter;
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
        this.frequency = new DefaultParameter("frequency", frequency); modifierParams.add(this.frequency);
        this.angle     = new ModuloParameter( "angle", phase, -360, 360); modifierParams.add(this.angle);
        this.bias      = new DefaultParameter("bias", bias); modifierParams.add(this.bias);
        this.amplitude = new DefaultParameter("amplitude", amplitude); modifierParams.add(this.amplitude);
    }
    
    
    @Override
    public void apply(TimeBase timeBase)
    {
        angle.set(angle.get() + timeBase.frameTime * frequency.get() * 360);
        visualParam.set(bias.get() + amplitude.get() * (float) Math.sin(Math.toRadians(angle.get())));
    }

    
    private final Parameter frequency, amplitude, angle, bias;
}
