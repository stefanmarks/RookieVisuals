package modifier;

import timing.TimeBase;

/**
 * Modifier that sets a parameter to a constant value.
 * 
 * @author  Stefan Marks
 */
public class SetValue extends AbstractSingleModifier
{
    public SetValue(String paramName, float value)
    {
        super(paramName);
        this.value = value;
    }
    
    
    @Override
    public void apply(TimeBase timeBase)
    {
        parameter.set(value);
    }

    
    private final float value;
}
