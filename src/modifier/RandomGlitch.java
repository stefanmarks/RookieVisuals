package modifier;

import parameter.DefaultParameter;
import parameter.LimitedParameter;
import parameter.Parameter;
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
        this.probability = new LimitedParameter("probability", probability); modifierParams.add(this.probability);
        this.min         = new DefaultParameter("min", min); modifierParams.add(this.min);
        this.max         = new DefaultParameter("max", max); modifierParams.add(this.max);
        
        delta = 0;
    }
    
    
    @Override
    public void apply(TimeBase timeBase)
    {
        if ( Math.random() < probability.get() )
        {
            if ( delta == 0 )
            {
                delta = (((float) Math.random()) * (max.get() - min.get())) + min.get();
                visualParam.set(visualParam.get() + delta);
            }
        }
        else
        {
            if ( delta != 0 )
            {
                // we need to restore the parameter
                visualParam.set(visualParam.get() - delta);
                delta = 0;
            }
        }
    }

    
    private final Parameter  probability, min, max;
    private       float      delta;
}
