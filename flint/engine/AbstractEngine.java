package flint.engine;

// Core Java Classes
import java.util.LinkedHashMap;
import java.util.Map;

// Application classes
import flint.environment.Environment;
import flint.framework.type.TypeDefinition;


/**
 * Provides an abstraction layer between an execution request and the underlying
 * technology/language that implements the request.  The engine accepts a generic
 * prototype string and should be override for each technology to support, taking
 * that prototype, interpreting it and executing its instructions
 * @author Philip Bowditch
 */
public abstract class AbstractEngine {
    
    /**
     * Holds a reference to the environment so we can reference variables/options etc
     */
    protected Environment m_env;
    
    /**
     * Holds the definition of a Type used to validate a type and its options
     * declared by the user
     */
    protected Map<String, TypeDefinition> m_definition;
    
    //--------------------------------------------------------------------------
    
    public AbstractEngine() {
        m_env        = null;
        m_definition = new LinkedHashMap<>();
    }
    
    //--------------------------------------------------------------------------
    
    public abstract void initialise() throws Exception;
    
    public abstract InvokationOutput invoke(  String fixtureType
                                            , InvokationInput input ) throws Exception;
    
    public abstract void destroy() throws Exception;
    
    //--------------------------------------------------------------------------
    
    public Environment getEnvironment() {
        return m_env;
    }
    
    public void setEnvironment( Environment env ) {
        m_env = env;
    }
    
    //--------------------------------------------------------------------------
    
    public void registerType( String alias, TypeDefinition typ ) {
        m_definition.put( alias, typ);
    }
    
    public TypeDefinition unregisterType( String alias ) {
        return m_definition.remove( alias );
    }
    
    public TypeDefinition getType( String alias ) {
        return m_definition.get( alias );
    }
    
    //--------------------------------------------------------------------------
    
}