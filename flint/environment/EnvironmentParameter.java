package flint.environment;

/**
 * A simple POJO representing an environment parameter
 * @author Philip Bowditch
 */
public class EnvironmentParameter {
    
    /**
     * The name of the variable
     */
    protected String m_label;
    
    /**
     * Its value
     */
    protected String m_value;
    
    /**
     * Type of the parameter e.g int
     */
    protected Type m_type;
    
    /**
     * Once set is it read only
     */
    protected boolean m_readonly;
    
    // TODO - Valid types for parameters
    public enum Type {
        STRING,
        STRING_UPPERCASE,
        STRING_LOWERCASE,
        INTEGER,
        FUNCTION,
        ALIAS
    }
    
    //--------------------------------------------------------------------------
    
    public EnvironmentParameter( String name, String value ) {
        this( name, value, Type.STRING );
    }
    
    public EnvironmentParameter( String name, String value, Type typ ) {
        m_label = name;
        m_value = value;
        m_type  = typ;
        
        m_readonly = false;
    }
    
    //--------------------------------------------------------------------------
    
    public String getName() {
        return m_label;
    }
    
    public void setName( String name ) {
        m_label = name;
    }
    
    public String getValue() {
        return m_value;
    }
    
    public void setValue( String value ) {
        m_value = value;
    }
    
    public Type getType() {
        return m_type;
    }
    
    public void setType( Type typ ) {
        m_type = typ;
    }
    
    public boolean getReadOnly() {
        return m_readonly;
    }
    
    public void setReadOnly( boolean readonly ) {
        m_readonly = readonly;
    }
}