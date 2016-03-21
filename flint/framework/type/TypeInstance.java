package flint.framework.type;

import java.util.Properties;

/**
 * Represents an instance of a TypeDefinition
 * @author Philip Bowditch
 */
public class TypeInstance {
    
    /**
     * The base definition this represents an instance of
     */
    protected TypeDefinition m_implements;
    
    /**
     * Personal overrides to any base definition default properties
     */
    protected Properties m_overrides;
            
    //--------------------------------------------------------------------------
    
    public TypeInstance() {
        this( null );
    }
    
    public TypeInstance( TypeDefinition implments ) {
        this( implments, new Properties() );
    }
    
    public TypeInstance( TypeDefinition implments, Properties props ) {
        super();
        
        m_implements = implments;
        m_overrides  = props;
    }
    
    //--------------------------------------------------------------------------

    /**
     * Returns the definition this implements
     */
    public TypeDefinition getDefinition() {
        return m_implements;
    }
    
    /**
     * 
     */
    public void setDefinition( final TypeDefinition implments ) {
        m_implements = implments;
    }
    
    public Properties getOverrides() {
        return m_overrides;
    }
    
    public void setOverrides( final Properties overrides ) {
        m_overrides = overrides;
    }
}