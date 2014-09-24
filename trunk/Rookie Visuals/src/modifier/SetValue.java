package modifier;

import parameter.DefaultParameter;
import parameter.Parameter;
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
        this.value = new DefaultParameter("value", value); modifierParams.add(this.value);
    }
    
    
    @Override
    public void apply(TimeBase timeBase)
    {
        if ( visualParam != null ) { visualParam.set(value.get()); }
    }

    
    private final Parameter value;
}
