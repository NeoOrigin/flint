package flint.framework.property;

/**
 *
 * @author Philip Bowditch
 */
public class AttributeProperty extends AbstractProperty {
    
    /**
     * Determines whether the property has any effect
     */
    protected boolean m_disabled;
    
    /**
     * Can the end user change the value
     */
    protected boolean m_readOnly;
    
    /**
     * Is this property hidden from the user
     */
    protected boolean m_hidden;
    
    /**
     * Textual description for ease of use
     */
    protected String m_description;
    
    /**
     * Does the property need a value
     */
    protected boolean m_optional;
    
    /**
     * Default value if not set
     */
    protected String m_default;
    
    /**
     * Basic constraints to certain values
     */
    protected String[] m_allowedValues;
    
    /**
     * int, real etc
     */
    protected String m_type;
    
    //protected Formatter m_formatter;
    
    
    //--------------------------------------------------------------------------
    
    public AttributeProperty( String name ) {
        super( name );
        
        m_disabled      = false;
        m_readOnly      = false;
        m_hidden        = false;
        m_description   = "";
        m_optional      = false;
        m_default       = null;
        m_allowedValues = null;
        
        m_type          = "text";
    }
    
    
    //--------------------------------------------------------------------------
    
    
    public boolean isOptional() {
        return m_optional;
    }
    
    public void setOptional( boolean optional ) {
        m_optional = optional;
    }
    
    public String getDefault() {
        return m_default;
    }
    
    public void setDefault( String dflt ) {
        m_default = dflt;
    }
    
    public String getType() {
        return m_type;
    }
    
    public void setType( String typ ) {
        m_type = typ;
    }
}