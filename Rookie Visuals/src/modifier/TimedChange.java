package modifier;

import parameter.DefaultParameter;
import parameter.Parameter;
import timing.TimeBase;

/**
 * Modifier that constantly changes a parameter.
 * 
 * @author  Stefan Marks
 */
public class TimedChange extends AbstractSingleModifier
{
    public TimedChange(String paramName, float change, float duration)
    {
        super(paramName);
        
        paramChange   = new DefaultParameter("change",   change);   modifierParams.add(paramChange);
        paramDuration = new DefaultParameter("duration", duration); modifierParams.add(paramDuration);
        
        startTime = -1;
    }
    
    
    @Override
    public void apply(TimeBase timeBase)
    {
        if ( startTime < 0 )
        {
            startTime = timeBase.absTime;
            if ( visualParam != null )
            {
                startValue = visualParam.get();
            }
        }
        
        if ( visualParam != null )
        {
            float delta = (float) (timeBase.absTime - startTime) / paramDuration.get();
            if ( delta > 1 ) 
            {
                delta    = 1;
                finished = true;
            }
            
            visualParam.set(startValue + paramChange.get() * delta);
        }
    }

    
    private final  Parameter paramChange, paramDuration;
    private        float     startValue;
    private        double    startTime;
}
