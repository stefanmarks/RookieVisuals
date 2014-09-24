package modifier;

import java.util.logging.Level;
import java.util.logging.Logger;
import parameter.Parameter;
import parameter.ParameterList;
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
        this.visualParamNames = paramNames;
        this.modifierParams   = new ParameterList();
        this.finished         = false;
    }

    
    @Override
    public boolean setVisual(Visual v)
    {
        boolean foundAll = true;
        visualParams = new Parameter[visualParamNames.length];
        for ( int i = 0; i < visualParamNames.length; i++ )
        {
            String parameterName = visualParamNames[i];
            visualParams[i] = v.getParameters().find(parameterName);
            if ( visualParams[i] == null )
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
    
    
    protected final String[]      visualParamNames;
    protected       Parameter[]   visualParams;
    protected final ParameterList modifierParams;
    protected boolean             finished;
    
    private final static Logger LOG = Logger.getLogger(AbstractMultiModifier.class.getName());
}
