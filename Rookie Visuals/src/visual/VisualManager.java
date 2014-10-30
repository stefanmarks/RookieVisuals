package visual;

import java.util.LinkedList;
import java.util.List;
import processing.core.PGraphics;
import timing.TimeBase;

/**
 * Class for managing the list of visuals,
 * for adding, removing, finding, updating, and rendering.
 * 
 * @author  Stefan Marks
 */
public class VisualManager 
{
    /**
     * Creates an instance of the visual manager.
     */
    public VisualManager()
    {
        visuals = new LinkedList<Visual>();
    }
    
    
    /**
     * Adds a visual to the manager.
     * 
     * @param v the visual to add
     * 
     * @return <code>true</code> if the visual was added,
     *         <code>false</code> if not
     */
    public boolean add(Visual v)
    {
        return visuals.add(v);
    }
    
    
    /**
     * Replaces a visual.
     *
     * @param oldVis  the visual to replace
     * @param newVis  the visual to replace it with
     * 
     * @return <code>true</code> if the visual was replaced,
     *         <code>false</code> if not
     */
    public boolean replace(Visual oldVis, Visual newVis)
    {
        boolean success = false;
        int idx = visuals.indexOf(oldVis);
        if ( idx >= 0 )
        {
            visuals.set(idx, newVis);
            success = true;
        }
        return success;
    }
    
    
    /**
     * Removes a visual from the manager.
     * 
     * @param v the visual to remove
     * 
     * @return <code>true</code> if the visual was removed,
     *         <code>false</code> if not
     */
    public boolean remove(Visual v)
    {
        return visuals.remove(v);
    }
    
    
    /**
     * Finds the first visual by a given name.
     * 
     * @param name the name to search for
     * 
     * @return the visual or <code>null</code> if visual was not found
     */
    public Visual find(String name)
    {
        for (Visual visual : visuals)
        {
            if ( visual.getName().equals(name) ) return visual;
        }
        return null;
    }
    
    
    /**
     * Updates the visuals.
     * 
     * @param timeBase the time base to use for the update
     */
    public void update(TimeBase timeBase)
    {
        for ( Visual visual : visuals )
        {
              visual.update(timeBase);
        }
    }
    
    
    /**
     * Renders the visuals to a graphics context.
     * 
     * @param g the graphics context to render toW
     */
    public void render(PGraphics g)
    {
        for ( Visual visual : visuals )
        {
            g.pushMatrix();
            g.pushStyle();
            visual.render(g);
            g.popStyle();
            g.popMatrix();
        }
    }
    
    
    private final List<Visual>  visuals;
}
