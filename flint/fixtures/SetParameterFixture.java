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
import flint.environment.Options;
import flint.framework.type.TypeInstance;
import flint.util.TableProcessor;


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
        this.m_requiredParameters = 2;
    }


    /*------------------------------------------------------------------------*/

    @Override
    protected TypeInstance lookupTypeInstance( Parse table, DataTable dt ) {
        return new TypeInstance();
    }
    
    @Override
    public InvokationOutput invokePrototype( TypeInstance t, DataTable table ) throws Exception {
        
        // Get all current parameters
        ArrayList<EnvironmentParameter> params = m_environment.getParameters();
        
        // Create a new one and add it
        EnvironmentParameter e = new EnvironmentParameter( m_label, m_value );
        params.add(e);
        
        InvokationOutput res = new InvokationOutput();
        ArrayList<String[]> rc = new ArrayList<>();
        rc.add( new String[]{ "0" } );
        res.setReturnCode( rc );
        return res;
    }
}