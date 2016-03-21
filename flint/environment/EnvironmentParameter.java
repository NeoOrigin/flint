package flint.environment;

/**
 * A simple POJO representing an environment parameter
 * @author Philip Bowditch
 */
public class EnvironmentParameter {
    
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
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the EnvironmentParameter class
     * @param name The name of the variable
     * @param value The value of the variable
     */
    public EnvironmentParameter( String name, String value ) {
        this( name, value, Type.STRING );
    }
    
    /**
     * Constructor for the EnvironmentParameter class
     * @param name The name of the variable
     * @param value The value of the variable
     * @param typ The value's type, e.g. String, Integer
     */
    public EnvironmentParameter( String name, String value, Type typ ) {
        this( name, value, typ, false );
    }
    
    /**
     * Constructor for the EnvironmentParameter class
     * @param name The name of the variable
     * @param value The value of the variable
     * @param typ The value's type, e.g. String, Integer
     * @param readonly Can the value be changed once set
     */
    public EnvironmentParameter( String name, String value, Type typ, boolean readonly ) {
        m_label    = name;
        m_value    = value;
        m_type     = typ;
        m_readonly = readonly;
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
