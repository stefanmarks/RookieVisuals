package parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * Managing class for parameters.
 * 
 * @author  Stefan Marks
 */
public class ParameterList 
{
    public ParameterList()
    {
        parameters = new HashMap<>();
    }
    
    
    public boolean add(Parameter param)
    {
        boolean added = false;
        if ( !parameters.containsKey(param.getName()) )
        {
            parameters.put(param.getName(), param);
            added = true;
        }
        return added;
    }
    
    
    public Parameter find(String name)
    {
        return parameters.get(name);
    }
    
    
    @Override
    public String toString()
    {
        return parameters.toString();
    }
    
    private final Map<String, Parameter> parameters;
}
