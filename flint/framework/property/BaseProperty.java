
package flint.framework.property;

/**
 * AttributeProperty and InterfaceProperty both share these common
 * attributes
 * 
 * @author Philip Bowditch
 */
public class BaseProperty extends AbstractProperty {
    
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
    
    //--------------------------------------------------------------------------
    
    public BaseProperty( String name) {
        super( name );
        
        m_disabled    = false;
        m_readOnly    = false;
        m_hidden      = false;
        m_description = "";
    }
    
    //--------------------------------------------------------------------------
    
    public boolean isDisabled() {
        return m_disabled;
    }
    
    public void setDisabled( boolean disabled ) {
        m_disabled = disabled;
    }
    
    public boolean isHidden() {
        return m_hidden;
    }
    
    public void setHidden( boolean hidden ) {
        m_hidden = hidden;
    }
    
    public boolean isReadOnly() {
        return m_readOnly;
    }
    
    public void setReadOnly( boolean readOnly ) {
        m_readOnly = readOnly;
    }
    
    public String getDescription() {
        return m_description;
    }
    
    public void setDescription( String description ) {
        m_description = description;
    }

}