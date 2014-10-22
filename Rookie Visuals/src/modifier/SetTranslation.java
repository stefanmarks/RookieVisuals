package modifier;

import parameter.DefaultParameter;
import parameter.Parameter;
import timing.TimeBase;

/**
 * Modifier that sets the position of a visual once.
 * 
 * @author  Stefan Marks
 */
public class SetTranslation extends AbstractMultiModifier
{
    public SetTranslation(float x, float y)
    {
        super(new String[] { "tX", "tY" });
        tX = new DefaultParameter("tX", x); modifierParams.add(tX);
        tY = new DefaultParameter("tY", y); modifierParams.add(tY);
    }

    
    @Override
    public void apply(TimeBase timeBase)
    {
        if ( visualParams[0] != null ) { visualParams[0].set(tX.get()); }
        if ( visualParams[1] != null ) { visualParams[1].set(tY.get()); }
    }

    
    @Override
    public String toString()
    {
        return "SetTranslation(" + tX.get() + ", " + tY.get() + ")";
    }

    
    private final Parameter tX, tY;
}
