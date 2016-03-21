package flint.data;

import java.util.Map;

import flint.data.DataColumn;
import flint.data.DataRow;

/**
 * An abstraction of a Fixture's data table, encapsulating data, column names, fixture parameters etc for easier processing
 * @author Philip Bowditch
 */
public class DataTable {

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
        
        m_fixture = null;
        m_name    = null;
        m_params  = new LinkedHashMap<String, String>();
    }
    
    
    //--------------------------------------------------------------------------
    
    public DataColumn[] getColumns() {
        return m_cols;
    }
    
    public void setColumns( DataColumn[] cols ) {
        m_cols = cols;
    }
    
    public DataRow[] getRows() {
        return m_rows;
    }
    
    public void setRows( DataRow[] rows ) {
        m_rows = rows;
    }
    
    public String getFixture() {
        return m_fixtureName;
    }
    
    public void setFixture( String fixtureName ) {
        m_fixtureName = fixtureName;
    }
    
    public String getName() {
        return m_name;
    }
    
    public void setName( String name ) {
        m_name = name;
    }
    
    public Map<String, String> getParameters() {
        return m_params;
    }
    
    public void setParameters( Map<String, String> params ) {
        m_params = params;
    }
}
