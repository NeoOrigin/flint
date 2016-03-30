package flint.fixtures;

// Core Java classes
import java.util.ArrayList;

// 3rd Party classes
import fit.Parse;
import flint.data.DataTable;
import flint.engine.InvokationOutput;

// Application classes
import flint.environment.Environment;
import flint.environment.EnvironmentParameter;
import flint.framework.type.TypeInstance;

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

    @Override
    protected TypeInstance lookupTypeInstance( Parse table, DataTable dt ) {
        return new TypeInstance();
    }
    
    @Override
    public InvokationOutput invokePrototype( TypeInstance t, DataTable table ) throws Exception {
        
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
        
        InvokationOutput res = new InvokationOutput();
        ArrayList<String[]> rc = new ArrayList<>();
        rc.add( new String[]{ "0" } );
        res.setReturnCode( rc );
        return res;
    }
}

