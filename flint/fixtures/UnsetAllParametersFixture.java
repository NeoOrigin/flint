package flint.fixtures;

// Core Java classes
import java.util.ArrayList;

// 3rd Party classes
import fit.Parse;

// Application classes
import flint.environment.Environment;
import flint.environment.EnvironmentParameter;

/**
 * Unsets an environment variable
 * 
 * @author Philip Bowditch
 */
public class UnsetAllParametersFixture extends fit.ColumnFixture {
    
    /**
     * Holds a reference to the environment
     */
    protected Environment m_environment;

    
    /*------------------------------------------------------------------------*/

    /**
     * Constructor for the UnsetAllParametersFixture class
     *
     * @param environment The environment to use
     */
    public UnsetParameterFixture( Environment environment ) {
        this.m_environment = environment;
    }

    
    /*------------------------------------------------------------------------*/

    /**
     * 
     * @param table 
     */
    @Override
    public void doTable(Parse table) {
    
        ArrayList<EnvironmentParameter> params = m_environment.getParameters();
        
        for (int i = 0; i < params.size(); i++) {
            params.remove(i);
        }
        
        this.right(table.parts.parts);
        counts.right--;
    }
}