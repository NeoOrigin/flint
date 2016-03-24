package flint.fixture;

// 3rd Party classes
import fit.Parse;

// Application classes
import flint.environment.Environment;
import flint.environment.Options;

/**
 * Sets an option on the framework
 * @author Philip Bowditch
 */
public class SetOptionFixture extends fit.ColumnFixture {

    /**
     * The name of the parameter
     */
    protected String m_label;
    
    /**
     * The value of the parameter
     */
    protected String m_value;

    /**
     * Holds a reference to the environment
     */
    protected Environment m_environment;


    /*------------------------------------------------------------------------*/

    /**
     * Constructor for the SetOptionFixture class
     * @param environment The environment to use
     * @param label The name of the option
     * @param value The value to set
     */
    public SetOptionFixture( Environment environment, String label, String value ) {
        this.m_label       = label;
        this.m_value       = value;
        this.m_environment = environment;
    }


    /*------------------------------------------------------------------------*/

    /**
     * Processes all cells in the table, for
     * the SetOptionFixture it will set a
     * value on a given key
     * @param table The table to process
     */
    @Override
    public void doTable(Parse table) {
    
        // Get our options
        Options opts = m_environment.getOptions();
        
        try {
            opts.setOption(m_label, m_value);
        }
        catch ( Exception ex ) {
            this.exception(table.parts.parts, ex);
            return;
        }
        
        this.right(table.parts.parts);
        counts.right--;
    }
}