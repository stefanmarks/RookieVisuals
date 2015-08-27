package parameter;

/**
 * Named parameter that can be changed via effects.
 * 
 * @author  Stefan Marks
 */
public class DefaultParameter implements Parameter
{
    /**
     * Creates a new named parameter.
     * 
     * @param name          name of the parameter
     * @param initialValue  the initial value of the parameter
     */
    public DefaultParameter(String name, float initialValue)
    {
        this.name  = name;
        this.value = initialValue;
    }
    

    @Override
    public String getName()
    {
        return name;
    }
    

    @Override
    public float get()
    {
        return value;
    }

    
    @Override
    public void set(float newValue)
    {
        this.value = newValue;
    }

    
    @Override
    public String toString()
    {
        return name + "=" + value;
    }
    
    
    protected final String name;
    protected       float  value;
}
