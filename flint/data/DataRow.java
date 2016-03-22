package flint.data;

// 3rd Party classes
import fit.Parse;

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
    
    
    //--------------------------------------------------------------------------
    
    public DataRow() {
        this( null );
    }
    
    public DataRow( String[] cells ) {
        this( cells, null );
    }
    
    public DataRow( String[] cells ) {
        m_cells = cells;
        m_pointer = null;
    }
    
    
    //--------------------------------------------------------------------------
    
    public Parse getPointer() {
        return m_pointer;
    }
    
    public void setPointer( Parse row ) {
        m_pointer = row;
    }
    
    /**
     * Returns the cell values that make up this row
     */
    public String[] getCells() {
        return m_cells;
    }
    
    public void setCells( String[] cells ) {
        m_cells = cells;
    }
}
