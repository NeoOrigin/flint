package flint.fixture;

// Core Java classes
import java.util.ArrayList;

// 3rd Party classes
import fit.Parse;

// Application classes
import flint.environment.Environment;
import flint.environment.EnvironmentParameter;


/**
 * Sets an environment parameter
 * @author Philip Bowditch
 */
public class SetParameterFixture extends fit.ColumnFixture {

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
     * Constructor for the SetParameterFixture class
     * @param environment The environment to use
     * @param label The name of the parameter
     * @param value The value to set
     */
    public SetParameterFixture( Environment environment, String label, String value ) {
        this.m_label       = label;
        this.m_value       = value;
        this.m_environment = environment;
    }


    /*------------------------------------------------------------------------*/

    /**
     * Processes all cells in the table, for
     * the SetParameterFixture it will set 
     * the given environment variable
     * @param table The table to process
     */
    @Override
    public void doTable(Parse table) {
    
        // Get all current parameters
        ArrayList<EnvironmentParameter> params = m_environment.getParameters();
        
        // Create a new one and add it
        EnvironmentParameter e = new EnvironmentParameter( m_label, m_value );
        params.add(e);
        
        this.right(table.parts.parts);
        counts.right--;
    }
}