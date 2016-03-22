package flint.data;

// 3rd Party classes
import fit.Parse;

/**
 * An abstraction of a data tables column.  
 * @author Philip Bowditch
 */
public class DataColumn {

    /**
     * Determines how a column is to be used during querying
     * KEY - Use for matching
     * QUERY - ? action
     * QUERY_INVERSE_COMPARISON - ! action - query but fail on match
     * QUERY_IGNORE_COMPARISON - % action - query but ignore for comparison
     * IGNORE - # action - do not query
     */
    public enum AccessModifier {
        
        KEY,
        QUERY,
        QUERY_INVERSE_COMPARISON,
        QUERY_IGNORE_COMPARISON,
        IGNORE
        
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * A reference to the first element
     */
    protected Parse m_pointer;
    
    /**
     * Holds the name of the column
     */
    protected String m_name;

    /**
     * Query action to perform with this column
     */
    protected AccessModifier m_modifier;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor gor the DataColumn class
     */
    public DataColumn() {
        this( null );
    }
    
    public DataColumn( String name ) {
        this( name, AccessModifier.KEY );
    }
    
    public DataColumn( String name, AccessModifier modifier ) {
        m_name = name;
        m_modifier = modifier;
        m_pointer  = null;
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Returns the raw cell thst tepresents this column header
     */
    public Parse getPointer() {
        return m_pointer;
    }
    
    public void setPointer( Parse cell ) {
        m_pointer = cell;
    }
    
    /**
     * Returns the name of the column
     */
    public String getName() {
        return m_name;
    }
    
    /**
     * Sets the name of the column
     * @param name The name of the column
     */
    public void setName( String name ) {
        m_name = name;
    }
    
    /**
     * Returns the AccessModifer for the column, dictating how the column is to be used
     */
    public AccessModifier getAccessModifier() {
        return m_modifier;
    }
    
    /**
     * Sets how this column is to be accessed / queried
     * @param modifier The modifier to set
     */
    public setAccessModifier( AccessModifier modifier ) {
        m_modifier = modifier;
    }
    
}
