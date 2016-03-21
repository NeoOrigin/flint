package flint.framework.type;

public class TypeInstanceFactory {

    protected TypeDefinition m_default;
    
    public TypeInstanceFactory() {
        this( null );
    }
    
    public TypeInstanceFactory( TypeDefinition defaultDefinition ) {
        m_default = defaultDefinition;
    }
    
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
    
    public TypeInstance getInstance( String key ) {
        return getInstance( key, new Properties() );
    }
    
    public TypeInstance getInstance( String key, Properties overrides ) {
        TypeDefinition implments = getRegistered( key );
        
        return getInstance( implments, overrides );
    }
    
    public TypeInstance getInstance( TypeDefinition implments ) {
        return getInstance( implments, new Properties() );
    }
    
    public TypeInstance getInstance( TypeDefinition implments, Properties overrides ) {
        TypeInstance t = new TypeInstance( implments );
        t.setOverrides(overrides);
        
        return t;
    }
}