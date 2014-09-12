package parameter;

/**
 * Named parameter that can be changed via effects.
 * 
 * @author  Stefan Marks
 */
public class LimitedParameter extends DefaultParameter
{
    /**
     * Creates a new named parameter.
     * 
     * @param name          name of the parameter
     * @param initialValue  the initial value of the parameter
     */
    public LimitedParameter(String name, float initialValue)
    {
        super(name, initialValue);
        this.minValue = Float.NEGATIVE_INFINITY;
        this.maxValue = Float.POSITIVE_INFINITY;
    }

    
    @Override
    public void set(float newValue)
    {
        if      ( newValue < minValue ) { super.set(minValue); }
        else if ( newValue > maxValue ) { super.set(maxValue); }
        else                            { super.set(newValue); }
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
