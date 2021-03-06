package flint.data;

// 3rd Party classes
import fit.Parse;

// Application classes
import flint.data.aggregate.IAggregator;
import java.util.Arrays;
import java.util.List;

/**
 * Abstraction to represent a data row in a table
 * @author Philip Bowditch
 */
public class DataRow {

    /**
     * A reference to the first element
     */
    protected Parse m_pointer;
    
    /**
     * Holds the actual values of cells / data
     */
    protected String[] m_cells;
    
    /**
     * An aggregator for the row
     */
    protected IAggregator m_aggregator;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the DataRow class
     */
    public DataRow() {
        this( null );
    }
    
    /**
     * Constructor for the DataRow class
     * @param cells The cells that represent this row
     */
    public DataRow( String[] cells ) {
        m_cells      = cells;
        m_pointer    = null;
        m_aggregator = null;
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Returns the raw Parse object that represents the start of the row
     * @return
     */
    public Parse getPointer() {
        return m_pointer;
    }
    
    /**
     * Sets the raw Parse object that represents the start of the row
     * @param row
     */
    public void setPointer( Parse row ) {
        m_pointer = row;
    }
    
    /**
     * Returns the cell values that make up this row
     * @return
     */
    public String[] getCells() {
        return m_cells;
    }
    
    /**
     * Sets the cell values that make up this row
     * @param cells The array of cells
     */
    public void setCells( String[] cells ) {
        m_cells = cells;
    }
    
    public IAggregator getAggregator() {
        return m_aggregator;
    }
    
    public void setAggregator( IAggregator aggregator ) {
        m_aggregator = aggregator;
    }
    
    public List<String> toList() {
        return Arrays.asList( m_cells );
    }
}
