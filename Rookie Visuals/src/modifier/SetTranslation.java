package modifier;

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
        this.tX = x;
        this.tY = y;
    }

    
    @Override
    public void apply(TimeBase timeBase)
    {
        parameters[0].set(tX);
        parameters[1].set(tY);
        finished = true; // one-shot modifier
    }

    
    @Override
    public String toString()
    {
        return "SetTranslation(" + tX + ", " + tY + ")";
    }

    
    private final float tX, tY;
}
