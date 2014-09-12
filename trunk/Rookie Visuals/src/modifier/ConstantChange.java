package modifier;

import timing.TimeBase;

/**
 * Modifier that constantly changes a parameter.
 * 
 * @author  Stefan Marks
 */
public class ConstantChange extends AbstractSingleModifier
{
    public ConstantChange(String paramName, float change)
    {
        super(paramName);
        this.change = change;
    }
    
    
    @Override
    public void apply(TimeBase timeBase)
    {
        parameter.set(parameter.get() + change * timeBase.frameTime);
    }

    
    private final float change;
}
