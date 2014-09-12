package parameter;

/**
 * Named parameter that can be changed via effects.
 * 
 * @author  Stefan Marks
 */
public interface Parameter 
{
    /**
     * Gets the name of the parameter.
     * 
     * @return  the name of the parameter.
     */
    public String getName();
    

    /**
     * Gets the current value of the parameter.
     * 
     * @return the current value of the parameter
     */
    public float get();
    
    /**
     * Sets the new value of the parameter
     * 
     * @param newValue the new value
     */
    public void set(float newValue);
}
