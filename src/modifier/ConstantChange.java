package modifier;

import parameter.DefaultParameter;
import parameter.Parameter;
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
        paramChange = new DefaultParameter("change", change); modifierParams.add(paramChange);
    }
    
    
    @Override
    public void apply(TimeBase timeBase)
    {
        if ( visualParam != null )
        {
            visualParam.set(visualParam.get() + paramChange.get() * timeBase.frameTime);
        }
    }

    
    private final Parameter paramChange;
}
