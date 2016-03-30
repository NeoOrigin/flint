package flint.fixtures;

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
public class SetParameterFixture extends SimpleFixture {
    
    /**
     * The value of the parameter
     */
    protected String m_value;


    /*------------------------------------------------------------------------*/

    /**
     * Constructor for the SetParameterFixture class
     * @param environment The environment to use
     * @param label The name of the parameter
     * @param value The value to set
     */
    public SetParameterFixture( Environment environment, String label, String value ) {
        super( environment, label );
        
        this.m_value = value;
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
        if ( ! isTestable() ) {
            counts.right--;
        }
    }
}