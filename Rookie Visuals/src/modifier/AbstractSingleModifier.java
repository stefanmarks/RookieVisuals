package modifier;

import java.util.logging.Level;
import java.util.logging.Logger;
import parameter.Parameter;
import parameter.ParameterList;
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
        this.visualParamName = paramName;
        this.modifierParams  = new ParameterList();
        this.finished        = false;
    }

    
    @Override
    public boolean setVisual(Visual v)
    {
        visualParam = v.getParameters().find(visualParamName);
        if ( visualParam == null )
        {
            LOG.log(Level.WARNING, 
                    "Could not find parameter ''{0}'' in visual ''{1}''.", 
                    new Object[]{visualParamName, v.getName()});
        }
        return (visualParam != null);
    }
    
    
    @Override
    public boolean isFinished()
    {
        return finished;
    }
    
    
    protected final String        visualParamName;
    protected       Parameter     visualParam;
    protected final ParameterList modifierParams;
    protected       boolean       finished;
    
    private final static Logger LOG = Logger.getLogger(AbstractSingleModifier.class.getName());
}
