package flint.data;

/**
 * An abstraction of a data tables column.  
 * @author Philip Bowditch
 */
public class DataColumn {

    /**
     * NONE - no action
     * QUERY - ? action
     * QUERY_NO_MATCH - ! action
     * IGNORE - # action
     */
    public enum AccessModifier {
        
        NONE,
        QUERY,
        QUERY_NO_MATCH,
        IGNORE
        
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Holds the name of the column
     */
    protected String m_name;

    /**
     * Query action to perform with this column
     */
    protected AccessModifier m_modifier;
    
    
    //--------------------------------------------------------------------------
    
    public DataColumn() {
        this( null );
    }
    
    public DataColumn( String name ) {
        this( name, AccessModifier.NONE );
    }
    
    public DataColumn( String name, AccessModifier modifier ) {
        m_name = name;
        m_modifier = modifier;
    }
    
    
    //--------------------------------------------------------------------------
    
    public String getName() {
        return m_name;
    }
    
    public void setName( String name ) {
        m_name = name;
    }
    
    public AccessModifier getAccessModifier() {
        return m_modifier;
    }
    
    public setAccessModifier( AccessModifier modifier ) {
        m_modifier = modifier;
    }
    
}
