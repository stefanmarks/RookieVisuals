package parameter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import processing.data.JSONObject;

/**
 * Managing class for parameters.
 * 
 * @author  Stefan Marks
 */
public class ParameterList 
{
    public ParameterList()
    {
        parameters        = new HashMap<>();
        parametersOrdered = new LinkedList<>();
    }
    
    
    public boolean add(Parameter param)
    {
        boolean added = false;
        if ( !parameters.containsKey(param.getName()) )
        {
            parameters.put(param.getName(), param);
            parametersOrdered.add(param);
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
    
    
    public void writeJSON(JSONObject json)
    {
        for (Parameter parameter : parametersOrdered )
        {
            json.setFloat(parameter.getName(), parameter.get());
        }
    }
    
    
    public void readJSON(JSONObject json)
    {
        for (Object key : json.keys())
        {
            String    paramName = key.toString();
            Parameter param = find(paramName);
            if ( param != null )
            {
                param.set(json.getFloat(param.getName()));
            }
            else
            {
                LOG.log(Level.WARNING, "Invalid parameter ''{0}'' in JSON object.", paramName);
            }
        }
    }

    
    private final Map<String, Parameter> parameters;
    private final List<Parameter>        parametersOrdered;
    
    private final static Logger LOG = Logger.getLogger(Parameter.class.getName());
}
