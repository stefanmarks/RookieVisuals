package modifier;

import parameter.DefaultParameter;
import timing.TimeBase;

/**
 * Modifier that sets the rotation of a visual once.
 * 
 * @author  Stefan Marks
 */
public class SetRotation extends AbstractSingleModifier
{
    public SetRotation(float angle)
    {
        super("angle");
        parmAngle = new DefaultParameter("angle", angle); modifierParams.add(parmAngle);
    }

    
    @Override
    public void apply(TimeBase timeBase)
    {
        if ( visualParam != null ) { visualParam.set(parmAngle.get()); }
        finished = true; // one-shot
    }

    
    @Override
    public String toString()
    {
        return "SetRotation(" + parmAngle.get() + ")";
    }

    
    private final DefaultParameter parmAngle;
}
