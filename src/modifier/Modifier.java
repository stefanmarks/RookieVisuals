package modifier;

import timing.TimeBase;
import visual.Visual;

/**
 * Interface for modifications being applied to visuals.
 * 
 * @author Stefan Marks
 */
public interface Modifier
{
    /**
     * Sets the visual that this modifier should be applied to.
     * 
     * @param v the visual this modifier will be applied to
     * 
     * @return <code>true</code> if the visual is valid for this modifier,
     *         <code>false</code> if not
     */
    public boolean setVisual(Visual v);
    
        
    /**
     * Applies the modifier.
     * 
     * @param timeBase  timing used for the operation
     */
    public void apply(TimeBase timeBase);
    
    
    /**
     * Checks if the modifier has finished it's job and can be removed.
     * 
     * @return <code>true</code> if modifier can be removed,
     *         <code>false</code> if not
     */
    public boolean isFinished();
}
