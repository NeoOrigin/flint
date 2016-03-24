package flint.data;

// 3rd Party classes
import fit.Parse;

// Application classes
import flint.data.aggregate.IAggregator;

/**
 * An abstraction of a data tables column.  
 * @author Philip Bowditch
 */
public class DataColumn {

    /**
     * Determines how a column is to be used during querying
     * KEY                      - Use for matching
     * QUERY                    - name? action
     * QUERY_INVERSE_COMPARISON - name! action - query but fail on match
     * QUERY_IGNORE_COMPARISON  - name# action - query but ignore for comparison
     * IGNORE                   - #name action - do not query
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
    
    /**
     * An aggregator for the column
     */
    protected IAggregator m_aggregator;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the DataColumn class
     */
    public DataColumn() {
        this( null );
    }
    
    /**
     * Constructor for the DataColumn class
     * @param name The name of the column
     */
    public DataColumn( String name ) {
        this( name, AccessModifier.KEY );
    }
    
    /**
     * Constructor for the DataColumn class
     * @param name The name of the column
     * @param modifier Determines the usage of the column for querying
     */
    public DataColumn( String name, AccessModifier modifier ) {
        m_name       = name;
        m_modifier   = modifier;
        m_pointer    = null;
        m_aggregator = null
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Returns the raw cell thst represents this column header
     * @return
     */
    public Parse getPointer() {
        return m_pointer;
    }
    
    /**
     * Sets the raw cell that represents this column header
     * @param cell The raw cell
     */
    public void setPointer( Parse cell ) {
        m_pointer = cell;
    }
    
    /**
     * Returns the name of the column
     * @return
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
     * @return
     */
    public AccessModifier getAccessModifier() {
        return m_modifier;
    }
    
    /**
     * Sets how this column is to be accessed / queried
     * @param modifier The modifier to set
     */
    public void setAccessModifier( AccessModifier modifier ) {
        m_modifier = modifier;
    }
    
    public IAggregator getAggregator() {
        return m_aggregator;
    }
    
    public void setAggregator( IAggregator aggregator ) {
        m_aggregator = aggregator;
    }
    
}
