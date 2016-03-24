package flint.fixtures;


// 3rd Party classes
import fit.Parse;

// Application classes
import flint.environment.Environment;
import flint.environment.Options;

/**
 * Re-sets all framework options back to their defaults
 * @author Philip Bowditch
 */
public class ResetAllOptionsFixture extends fit.ColumnFixture {

    /**
     * Holds a reference to the environment
     */
    protected Environment m_environment;


    /*------------------------------------------------------------------------*/

    /**
     * Constructor for the ResetOption class
     * @param environment The environment to use
     */
    public ResetAllOptionsFixture( Environment environment ) {
        this.m_environment = environment;
    }


    /*------------------------------------------------------------------------*/

    /**
     * Processes all cells in the table, for
     * the ResetAllOptionsFixture it will 
     * reset all previous option overrides
     * @param table The table to process
     */
    @Override
    public void doTable(Parse table) {
        super.doTable(table);
        
        // Get options and reset
        Options opts = m_environment.getOptions();
        opts.reset();
        
        this.right(table.parts.parts);
        counts.right--;
    }
}