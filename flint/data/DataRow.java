package flint.data;

/**
 * Abstraction to represent a data row in a table
 * @author Philip Bowditch
 */
public class DataRow {

    protected String[] m_cells;
    
    public DataRow() {
        this( null );
    }
    
    public DataRow( String[] cells ) {
        this( cells, null );
    }
    
    public DataRow( String[] cells ) {
        m_cells = cells;
    }
    
    public String[] getCells() {
        return m_cells;
    }
    
    public void setCells( String[] cells ) {
        m_cells = cells;
    }
}
