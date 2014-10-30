package modifier;

import parameter.DefaultParameter;
import parameter.Parameter;
import timing.TimeBase;

/**
 * Modifier that adds a value to a parameter.
 * 
 * @author  Stefan Marks
 */
public class OffsetValue extends AbstractSingleModifier
{
    public OffsetValue(String paramName, float offset)
    {
        super(paramName);
        this.offset = new DefaultParameter("offset", offset); modifierParams.add(this.offset);
    }
    
    
    public float getOffset()
    {
        return offset.get();
    }
    
    
    public void setOffset(float offset)
    {
        this.offset.set(offset);
    }
    
    
    public void lerpOffset(float newOffset, float factor)
    {
        offset.set(offset.get() + (newOffset - offset.get()) * factor);
    }
    
    
    @Override
    public void apply(TimeBase timeBase)
    {
        if ( visualParam != null ) { visualParam.set(visualParam.get() + offset.get()); }
    }

    
    private final Parameter offset;
}
