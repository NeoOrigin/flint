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
        m_cells = cells;
        m_pointer = null;
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Returns the raw Parse object that represents the start of the row
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
}
