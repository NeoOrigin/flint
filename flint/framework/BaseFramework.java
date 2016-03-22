package flint.framework;

import java.util.LinkedHashMap;
import java.util.Map;

import flint.framework.type.TypeDefinition;


/**
 *
 * @author Philip Bowditch
 */
public class BaseFramework {
    
    /**
     * Holds the name of this framework
     */
    protected String m_name;
    
    /**
     * Holds the types recognized by this framework
     */
    protected Map<String, TypeDefinition> m_types;
    
    /*------------------------------------------------------------------------*/

    /**
     * 
     * @param name - The name of the framework
     */
    public BaseFramework( String name ) {
        m_name = name;
    }
    
    //--------------------------------------------------------------------------
    
    public void initialise() throws Exception {
        loadTypeDefinitions();
    }
    
    public void loadTypeDefinitions() throws Exception {
        m_types = new LinkedHashMap<String, TypeDefinition>();
    }
    
    public void clearTypeDefinitions() {
        m_types.clear();
    }
    
    //--------------------------------------------------------------------------
    
    public String getName() {
        return m_name;
    }
    
    public void setName( String name ) {
        m_name = name;
    }
    
    public Map<String, TypeDefinition> getTypeDefinitions() {
        return m_types;
    }
    
    public void setTypeDefinitions( Map<String, TypeDefinition> newTypes ) {
        m_types = newTypes;
    }
    
    //--------------------------------------------------------------------------
    
}
