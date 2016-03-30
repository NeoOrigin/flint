package flint.fixtures;

// 3rd Party classes
import fit.Parse;
import flint.data.DataTable;
import flint.engine.InvokationOutput;

// Application classes
import flint.environment.Environment;
import flint.environment.Options;
import flint.framework.type.TypeInstance;
import java.util.ArrayList;


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
        this.m_requiredParameters = 2;
    }


    /*------------------------------------------------------------------------*/

    @Override
    protected TypeInstance lookupTypeInstance( Parse table, DataTable dt ) {
        return new TypeInstance();
    }
    
    @Override
    public InvokationOutput invokePrototype( TypeInstance t, DataTable table ) throws Exception {
        
        // Get ootions
        Options opts = m_environment.getOptions();
        
        opts.setOption(m_label, m_value);
        
        InvokationOutput res = new InvokationOutput();
        ArrayList<String[]> rc = new ArrayList<>();
        rc.add( new String[]{ "0" } );
        res.setReturnCode( rc );
        return res;
    }
}