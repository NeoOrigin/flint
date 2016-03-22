package flint.framework.type;

import java.util.Properties;

/**
 * A factory class for producing TypeInstances from definitions
 * @author Philip Bowditch
 */
public class TypeInstanceFactory {

    /**
     * Default definition if one not specified
     */
    protected TypeDefinition m_default;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the TypeInstanceFactory class
     */
    public TypeInstanceFactory() {
        this( null );
    }
    
    /**
     * Constructor for the TypeInstanceFactory class
     * @param defaultDefinition The default definition to use if none specified
     */
    public TypeInstanceFactory( TypeDefinition defaultDefinition ) {
        m_default = defaultDefinition;
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Returns the default TypeDefinition that will be used
     * @return
     */
    public TypeDefinition getDefault() {
        return m_default;
    }
    
    public void setDefault( TypeDefinition defaultDefinition ) {
        m_default = defaultDefinition;
    }
    
    public TypeInstance getInstance() {
        return getInstance( new Properties() );
    }
    
    public TypeInstance getInstance( Properties overrides ) {
        return getInstance( m_default, overrides );
    }
    
    /*public TypeInstance getInstance( String key ) {
        return getInstance( key, new Properties() );
    }
    
    public TypeInstance getInstance( String key, Properties overrides ) {
        TypeDefinition implments = getRegistered( key );
        
        return getInstance( implments, overrides );
    }*/
    
    public TypeInstance getInstance( TypeDefinition implments ) {
        return getInstance( implments, new Properties() );
    }
    
    public TypeInstance getInstance( TypeDefinition implments, Properties overrides ) {
        TypeInstance t = new TypeInstance( implments );
        t.setOverrides(overrides);
        
        return t;
    }
}