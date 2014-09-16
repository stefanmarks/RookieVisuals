package modifier;

import java.util.logging.Level;
import java.util.logging.Logger;
import parameter.Parameter;
import visual.Visual;

/**
 * Abstract modifier class that handles a single parameter.
 * 
 * @author  Stefan Marks
 */
public abstract class AbstractSingleModifier implements Modifier
{
    public AbstractSingleModifier(String paramName)
    {
        this.parameterName = paramName;
        this.finished      = false;
    }

    
    @Override
    public boolean setVisual(Visual v)
    {
        parameter = v.getParameters().find(parameterName);
        if ( parameter == null )
        {
            LOG.log(Level.WARNING, 
                    "Could not find parameter ''{0}'' in visual ''{1}''.", 
                    new Object[]{parameterName, v.getName()});
        }
        return (parameter != null);
    }
    
    
    @Override
    public boolean isFinished()
    {
        return finished;
    }
    
    
    protected final String parameterName;
    protected Parameter    parameter;
    protected boolean      finished;
    
    private final static Logger LOG = Logger.getLogger(AbstractSingleModifier.class.getName());
}
