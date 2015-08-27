package modifier;

import parameter.DefaultParameter;
import timing.TimeBase;

/**
 * Modifier that sets the scale of a visual once.
 * 
 * @author  Stefan Marks
 */
public class SetScale extends AbstractMultiModifier
{
    public SetScale(float s)
    {
        this(s, s);
    }

    
    public SetScale(float x, float y)
    {
        super(new String[] { "sX", "sY" });
        sX = new DefaultParameter("sX", x); modifierParams.add(sX);
        sY = new DefaultParameter("sY", y); modifierParams.add(sY);
    }

    
    @Override
    public void apply(TimeBase timeBase)
    {
        if ( visualParams[0] != null ) { visualParams[0].set(sX.get()); }
        if ( visualParams[1] != null ) { visualParams[1].set(sY.get()); }
    }

    
    @Override
    public String toString()
    {
        return "SetScale(" + sX.get() + ", " + sY.get() + ")";
    }

    
    private final DefaultParameter sX, sY;
}
