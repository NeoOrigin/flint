package flint.fixtures;

// 3rd Party classes
import fit.Parse;

// Application classes
import flint.environment.Environment;
import flint.environment.Options;


/**
 * Sets an option on the framework
 * @author Philip Bowditch
 */
public class SetOptionFixture extends SimpleFixture {
    
    /**
     * The value of the parameter
     */
    protected String m_value;


    /*------------------------------------------------------------------------*/

    /**
     * Constructor for the SetOptionFixture class
     * @param environment The environment to use
     * @param label The name of the option
     * @param value The value to set
     */
    public SetOptionFixture( Environment environment, String label, String value ) {
        super( environment, label );
        
        this.m_value = value;
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
        if ( ! isTestable() ) {
            counts.right--;
        }
    }
}