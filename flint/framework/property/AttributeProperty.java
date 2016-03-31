package flint.framework.property;

/**
 * An implementation of a property thats sets
 * core attributes such as if its read only, has a default etc
 * @author Philip Bowditch
 */
public class AttributeProperty extends AbstractProperty {
    
    /**
     * Determines who can see this property
     *
     * ALL    - Everything, no restriction
     * ENGINE - Not visible in the ui
     * FRAMEWORK - Not visible in the ui or passed to the engine
     */
    public enum VisibilityLevel {
        ALL,
        ENGINE,
        FRAMEWORK
    }
    
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
    protected VisibilityLevel m_hidden;
    
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
        m_hidden        = VisibilityLevel.ALL;
        m_description   = "";
        m_optional      = false;
        m_default       = null;
        m_allowedValues = null;
        
        m_type          = "text";
    }
    
    
    //--------------------------------------------------------------------------
    
    
    public boolean isDisabled() {
        return m_disabled;
    }
    
    public void setDisabled( boolean disabled ) {
        m_disabled = disabled;
    }
    
    public boolean isReadOnly() {
        return m_readOnly;
    }
    
    public void setReadOnly
    ( boolean readOnly ) {
        m_readOnly = readOnly
    }
    
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
    
    public VisibilityLevel getVisibilityLevel() {
        return m_hidden;
    }
    
    public void setVisibilityLevel( VisibilityLevel hidden ) {
        m_hidden = hidden;
    }
    
    public String getDescription() {
        return m_description;
    }
    
    public void setDescription( String description ) {
        m_description = description;
    }
    
    public String[] getAllowedValues() {
        return m_allowedValues;
    }
    
    public void setAllowedValues( String[] allowed ) {
        m_allowedValues = allowed;
    }
}