package modifier;

import timing.TimeBase;

/**
 * Modifier that randomly glitches a parameter.
 * 
 * @author  Stefan Marks
 */
public class RandomGlitch extends AbstractSingleModifier
{
    public RandomGlitch(String paramName, float probability, float min, float max)
    {
        super(paramName);
        this.probability = probability;
        this.min         = min;
        this.max         = max;
        
        delta = 0;
    }
    
    
    @Override
    public void apply(TimeBase timeBase)
    {
        if ( Math.random() < probability )
        {
            if ( delta == 0 )
            {
                delta = (((float) Math.random()) * (max - min)) + min;
                parameter.set(parameter.get() + delta);
            }
        }
        else
        {
            if ( delta != 0 )
            {
                // we need to restore the parameter
                parameter.set(parameter.get() - delta);
                delta = 0;
            }
        }
    }

    
    private final float  probability, min, max;
    private       float  delta;
}
