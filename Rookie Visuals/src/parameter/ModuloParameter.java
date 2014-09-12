package parameter;

/**
 * Named parameter that can be changed via effects.
 * The parameter has an upper and a lower limit and will 
 * jump between the limits when exceeding them
 * in a Modulo fashion, e.g., [-1...+1] -> 0, 0.5, 1, -0.5, 0, 0.5, ...
 * 
 * @author  Stefan Marks
 */
public class ModuloParameter extends DefaultParameter
{
    /**
     * Creates a new named parameter.
     * 
     * @param name          name of the parameter
     * @param initialValue  the initial value of the parameter
     * @param minValue      the minimum value
     * @param maxValue      the maximum value
     */
    public ModuloParameter(String name, float initialValue, float minValue, float maxValue)
    {
        super(name, initialValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    
    @Override
    public void set(float newValue)
    {
        final float span = maxValue - minValue;
        while ( newValue < minValue ) { newValue += span; }
        while ( newValue > maxValue ) { newValue -= span; }
        super.set(newValue); 
    }
    
    
    public float getMinimum()
    {
        return minValue;
    }
    
    
    public void setMinimum(float newMinimum)
    {
        this.minValue = newMinimum;
        set(value); // check new minimum immediately
    }
    
    
    public float getMaximum()
    {
        return maxValue;
    }
    
    
    public void setMaximum(float newMaximum)
    {
        this.maxValue = newMaximum;
        set(value); // check new maximum immediately
    }
    

    public void setLimits(float newMinimum, float newMaximum)
    {
        setMinimum(newMinimum);
        setMaximum(newMaximum);
    }
    
    
    private float  minValue, maxValue;
}
