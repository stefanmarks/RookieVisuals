package modifier;

import com.illposed.osc.OSCParameter;
import timing.TimeBase;

/**
 * Modifier that changes a parameter based on an OSC variable.
 * 
 * @author  Stefan Marks
 */
public class OSCVariable extends AbstractSingleModifier
{
    public OSCVariable(String paramName, OSCParameter<Float> parameter, float min, float max)
    {
        super(paramName);
        this.parameter = parameter;
        this.min       = min;
        this.max       = max;
    }
    
    
    @Override
    public void apply(TimeBase timeBase)
    {
        if ( visualParam != null )
        {
            visualParam.set(min + (max - min) * parameter.get());
        }
    }

    
    private final OSCParameter<Float> parameter;
    private final float               min, max;
}
