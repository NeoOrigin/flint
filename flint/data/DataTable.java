package flint.data;

// Core Java classes
import java.util.LinkedHashMap;
import java.util.Map;

// 3rd Party classes
import fit.Parse;

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
        
        m_pointer     = null;
        m_fixtureName = null;
        m_name        = null;
        m_params      = new LinkedHashMap<>();
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Returns the raw Parse object that represents the start of the table
     * @return
     */
    public Parse getPointer() {
        return m_pointer;
    }
    
    /**
     * Sets the raw Parse object that represents the start of the table
     * @param table The pointer to the table
     */
    public void setPointer( Parse table ) {
        m_pointer = table;
    }
    
    /**
     * Returns the column definitions used in this table
     * @return
     */
    public DataColumn[] getColumns() {
        return m_cols;
    }
    
    /**
     * Sets the column definitions used in this table
     * @param cols The columns
     */
    public void setColumns( DataColumn[] cols ) {
        m_cols = cols;
    }
    
    /**
     * Returns the data rows used in this table
     * @return
     */
    public DataRow[] getRows() {
        return m_rows;
    }
    
    /**
     * Sets the data rows used in this table
     * @param rows The rows of data
     */
    public void setRows( DataRow[] rows ) {
        m_rows = rows;
    }
    
    /**
     * Returns the name of the fixture in the first cell
     * @return
     */
    public String getFixture() {
        return m_fixtureName;
    }
    
    /**
     * Sets the name of the fixture to be run
     * @param fixtureName
     */
    public void setFixture( String fixtureName ) {
        m_fixtureName = fixtureName;
    }
    
    /**
     * Returns the name of the dataset, the target of the fixture
     * @return
     */
    public String getName() {
        return m_name;
    }
    
    /**
     * Sets the name of the target object
     * @param name The name of the dataset to target
     */
    public void setName( String name ) {
        m_name = name;
    }
    
    /**
     * Returns any additional parameters specified on the table
     * @return
     */
    public Map<String, String> getParameters() {
        return m_params;
    }
    
    /**
     * Sets a map of parameters that influences the run of the fixture
     * @param params The key value pair configuration
     */
    public void setParameters( Map<String, String> params ) {
        m_params = params;
    }
}
