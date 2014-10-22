package modifier;

import parameter.DefaultParameter;
import parameter.Parameter;
import timing.TimeBase;

/**
 * Modifier that applies an offset to a paramter over time.
 * 
 * @author  Stefan Marks
 */
public class TimedOffset extends AbstractSingleModifier
{
    public TimedOffset(String paramName, float start, float end, float duration)
    {
        super(paramName);
        
        paramStart    = new DefaultParameter("start",    start);    modifierParams.add(paramStart);
        paramEnd      = new DefaultParameter("end",      end);      modifierParams.add(paramEnd);
        paramDuration = new DefaultParameter("duration", duration); modifierParams.add(paramDuration);
        
        delta     = 0;
        direction = 0;
    }
    
    
    public void forwards()
    {
        direction = 1;
    }
    
    
    public void backwards()
    {
        direction = -1;
    }

    
    @Override
    public void apply(TimeBase timeBase)
    {
        if ( visualParam != null )
        {
            delta += direction * timeBase.frameTime;
            if ( delta > 1 ) 
            {
                delta     = 1;
                direction = 0;
            }
            if ( delta < 0 ) 
            {
                delta     = 0;
                direction = 0;
            }
            
            visualParam.set(visualParam.get() + paramStart.get() * (1 - delta) + paramEnd.get() * delta);
        }
    }

    
    private final  Parameter paramStart, paramEnd, paramDuration;
    private        float     delta, direction;
}
