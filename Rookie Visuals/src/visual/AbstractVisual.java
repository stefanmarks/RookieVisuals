package visual;

import modifier.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import parameter.DefaultParameter;
import parameter.Parameter;
import parameter.ParameterList;
import processing.core.PGraphics;
import timing.TimeBase;

/**
 * Abstract base class for visuals. 
 * This base class implements affine transformations parameters and modifier management.
 *
 * @author Stefan Marks
 */
public abstract class AbstractVisual implements Visual
{
    /**
     * Creates a new visual abstract base instance.
     * 
     * @param name the name of the visual
     */
    protected AbstractVisual(String name)
    {
        this.name  = name;
        
        modifiers  = new LinkedList<>();
        parameters = new ParameterList();
        // create parameter list
        paramPosX   = new DefaultParameter("tX", 0);    parameters.add(paramPosX);
        paramPosY   = new DefaultParameter("tY", 0);    parameters.add(paramPosY);
        paramAngle  = new DefaultParameter("angle", 0); parameters.add(paramAngle);
        paramScaleX = new DefaultParameter("sX", 1);    parameters.add(paramScaleX);
        paramScaleY = new DefaultParameter("sY", 1);    parameters.add(paramScaleY);
        
        paramEnabled = new DefaultParameter("enabled", 1); parameters.add(paramEnabled);
    }

    
    @Override
    public String getName()
    {
        return name;
    }
    
    
    @Override
    public ParameterList getParameters()
    {
        return parameters;
    }
    
    
    @Override
    public boolean addModifier(Modifier m)
    {
        boolean added = false;
        if ( !modifiers.contains(m) && m.setVisual(this) )
        {
            modifiers.add(m);
            added = true;
            LOG.log(Level.INFO, 
                    "Added modifier ''{0}'' to visual ''{1}''.", 
                    new Object[]{m.toString(), name});
        }
        return added;
    }

    
    @Override
    public void update(TimeBase timeBase)
    {
        Modifier toRemove = null;
        // apply modifiers
        for ( Modifier modifier : modifiers )
        {
            if ( modifier.isFinished() )
            {
                // remember modifier for removal
                toRemove = modifier;
            }
            else
            {
                modifier.apply(timeBase);
            }
        }
        
        if ( toRemove != null )
        {
            // remove the last finished modifier
            // it might take a bit more time to clean the list,
            // but it's faster per frame
            modifiers.remove(toRemove);
            LOG.log(Level.INFO, 
                    "Removed finished modifier ''{0}''.", 
                    toRemove);
        }
    }
    
    
    @Override
    public boolean isEnabled()
    {
        return paramEnabled.get() > 0;
    }
    

    @Override
    public void setEnabled(boolean enabled)
    {
        paramEnabled.set(enabled ? 1 : 0);
    }
    

    /**
     * Applies transformations before actually rendering the visual
     * 
     * @param g the render context to work with
     */
    protected void preRender(PGraphics g)
    {
        g.translate(paramPosX.get(), paramPosY.get());
        g.rotate((float) Math.toRadians(paramAngle.get()));
        g.scale(paramScaleX.get(), paramScaleY.get());
    }

    
    protected final String    name;
    protected List<Modifier>  modifiers;
    protected ParameterList   parameters;
    protected Parameter       paramEnabled;
    protected Parameter       paramPosX, paramPosY;
    protected Parameter       paramAngle;
    protected Parameter       paramScaleX, paramScaleY;
    
    private final static Logger LOG = Logger.getLogger(AbstractVisual.class.getName());
}
