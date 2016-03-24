package flint.fixtures;

// 3rd Party classes
import fit.Parse;

// Application classes
import flint.environment.Environment;
import flint.environment.Options;

/**
 * Re-sets a framework option back to its default
 * @author Philip Bowditch
 */
public class ResetOptionFixture extends fit.ColumnFixture {

    /**
     * The name of the parameter
     */
    protected String m_label;

    /**
     * Holds a reference to the environment
     */
    protected Environment m_environment;


    /*------------------------------------------------------------------------*/

    /**
     * Constructor for the ResetOptionFixture class
     * @param environment The environment to use
     * @param label The option to reset
     */
    public ResetOptionFixture( Environment environment, String label ) {
        this.m_label       = label;
        this.m_environment = environment;
    }


    /*------------------------------------------------------------------------*/

    /**
     * Processes all cells in the table, for
     * the ResetOptionFixture it will 
     * reset the previous option overrides
     * @param table The table to process
     */
    @Override
    public void doTable(Parse table) {
    
        // Get ootions
        Options opts = m_environment.getOptions();
        
        // reset
        try {
            opts.resetOption(m_label);
        }
        catch ( Exception ex ) {
            this.exception(table.parts, ex);
            return;
        }
        
        this.right(table.parts.parts);
        counts.right--;
    }
}