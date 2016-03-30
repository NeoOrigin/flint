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
public class UnsetParameterFixture extends SimpleFixture {

    /**
     * Constructor for the UnsetParameterFixture class
     * @param environment The environment to use
     * @param label The name of the parameter to unset
     */
    public UnsetParameterFixture( Environment environment, String label ) {
        super( environment, label );
    }

    
    /*------------------------------------------------------------------------*/

    /**
     * 
     * @param table 
     */
    @Override
    public void doTable(Parse table) {
        ArrayList<EnvironmentParameter> params = m_environment.getParameters();
        
        EnvironmentParameter e;
        //int                  foundCount = 0;
       
        // TODO - want to be able to unset multiple at once
        for (int i = 0; i < params.size(); i++) {
            
            e = params.get(i);
            if ( e.getName().equals( m_label ) ) {
                params.remove(i);
                //foundCount++;
                i--;
            }
        }
        
        this.right(table.parts.parts);
        if ( ! isTestable() ) {
            counts.right--;
        }
    }
}

