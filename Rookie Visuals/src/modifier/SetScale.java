package modifier;

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
        this.sX = x;
        this.sY = y;
    }

    
    @Override
    public void apply(TimeBase timeBase)
    {
        parameters[0].set(sX);
        parameters[1].set(sY);
        finished = true; // one-shot
    }

    
    @Override
    public String toString()
    {
        return "SetScale(" + sX + ", " + sY + ")";
    }

    
    private final float sX, sY;
}
