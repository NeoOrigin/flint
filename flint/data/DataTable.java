package flint.data;

// Core Java classes
import java.util.LinkedHashMap;
import java.util.Map;

// Application classes
import flint.data.DataColumn;
import flint.data.DataRow;

/**
 * An abstraction of a Fixture's data table, encapsulating data, column names, fixture parameters etc for easier processing
 * @author Philip Bowditch
 */
public class DataTable {

    /**
     * A reference to the first element
     */
    protected Parse m_pointer;
    
    /**
     * The columns for this table
     */
    protected DataColumn[] m_cols;
    
    /**
     * The data rows of this table
     */
    protected DataRow[] m_rows;
    
    /**
     * The name of the targeted Fixture
     */
    protected String m_fixtureName;
    
    /**
     * The name of the TypeInstance to action on
     */
    protected String m_name;
    
    /**
     * Name value pair parameters specified on the Fixture to influence its action
     */
    protected Map<String, String> m_params;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the DataTable class
     */
    public DataTable() {
        this( null );
    }
    
    /**
     * Constructor for the DataTable class
     * @param cols An array of data columns specifying the structure of this table
     */
    public DataTable( DataColumn[] cols ) {
        this( cols, null );
    }
    
    /**
     * Constructor for the DataTable class
     * @param cols An array of data columns specifying the structure of this table
     * @param rows An array of rows representing the data
     */
    public DataTable( DataColumn[] cols, DataRow[] rows ) {
        m_cols = cols;
        m_rows = rows;
        
        m_pointer = null;
        m_fixture = null;
        m_name    = null;
        m_params  = new LinkedHashMap<String, String>();
    }
    
    
    //--------------------------------------------------------------------------
    
    public Parse getPointer() {
        return m_pointer;
    }
    
    public void setPointer( Parse table ) {
        m_pointer = table;
    }
    
    /**
     * Returns the column definitions used in this table
     */
    public DataColumn[] getColumns() {
        return m_cols;
    }
    
    public void setColumns( DataColumn[] cols ) {
        m_cols = cols;
    }
    
    /**
     * Returns the data rows used in this table
     */
    public DataRow[] getRows() {
        return m_rows;
    }
    
    public void setRows( DataRow[] rows ) {
        m_rows = rows;
    }
    
    /**
     * Returns the name of the fixture in the first cell
     */
    public String getFixture() {
        return m_fixtureName;
    }
    
    public void setFixture( String fixtureName ) {
        m_fixtureName = fixtureName;
    }
    
    /**
     * Returns the name of the dataset, the target of the fixture
     */
    public String getName() {
        return m_name;
    }
    
    public void setName( String name ) {
        m_name = name;
    }
    
    /**
     * Returns any additional parameters specified on the table
     */
    public Map<String, String> getParameters() {
        return m_params;
    }
    
    public void setParameters( Map<String, String> params ) {
        m_params = params;
    }
}
