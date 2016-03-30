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
import java.util.Map;


/**
 * Unsets an environment variable
 * 
 * @author Philip Bowditch
 */
public class UnsetAllParametersFixture extends SimpleFixture {

    /**
     * Constructor for the UnsetAllParametersFixture class
     *
     * @param environment The environment to use
     */
    public UnsetAllParametersFixture( Environment environment ) {
        super( environment, "" );
        m_requiredParameters = 0;
    }

    
    /*------------------------------------------------------------------------*/

    @Override
    protected TypeInstance lookupTypeInstance( Parse table, DataTable dt ) {
        return new TypeInstance();
    }
    
    @Override
    public InvokationOutput invokePrototype( TypeInstance t, DataTable table ) throws Exception {
        
        ArrayList<EnvironmentParameter> params = m_environment.getParameters();
        
        for (int i = 0; i < params.size(); i++) {
            params.remove(i);
        }
        
        InvokationOutput res = new InvokationOutput();
        ArrayList<String[]> rc = new ArrayList<>();
        rc.add( new String[]{ "0" } );
        res.setReturnCode( rc );
        return res;
    }
}