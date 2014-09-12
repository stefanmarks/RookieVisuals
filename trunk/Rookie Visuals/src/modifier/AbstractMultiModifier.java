package modifier;

import java.util.logging.Level;
import java.util.logging.Logger;
import parameter.Parameter;
import visual.Visual;

/**
 * Abstract modifier class that handles multiple parameters.
 * 
 * @author  Stefan Marks
 */
public abstract class AbstractMultiModifier implements Modifier
{
    public AbstractMultiModifier(String[] paramNames)
    {
        this.parameterNames = paramNames;
        this.finished   = false;
    }

    
    @Override
    public boolean setVisual(Visual v)
    {
        boolean foundAll = true;
        parameters = new Parameter[parameterNames.length];
        for ( int i = 0; i < parameterNames.length; i++ )
        {
            String parameterName = parameterNames[i];
            parameters[i] = v.getParameters().find(parameterName);
            if ( parameters[i] == null )
            {
                LOG.log(Level.WARNING, 
                    "Could not find parameter ''{0}'' in visual ''{1}''.", 
                    new Object[]{parameterName, v.getName()});

                foundAll = false;
            }
        }
        return foundAll;
    }
    
    
    @Override
    public boolean isFinished()
    {
        return finished;
    }
    
    
    protected final String[] parameterNames;
    protected Parameter[]    parameters;
    protected boolean        finished;
    
    private final static Logger LOG = Logger.getLogger(AbstractMultiModifier.class.getName());
}
