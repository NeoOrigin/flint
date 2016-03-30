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
 * Re-sets all framework options back to their defaults
 * @author Philip Bowditch
 */
public class ResetAllOptionsFixture extends SimpleFixture {

    /**
     * Constructor for the ResetOption class
     * @param environment The environment to use
     */
    public ResetAllOptionsFixture( Environment environment ) {
        super( environment, null );
        m_requiredParameters = 0;
    }


    /*------------------------------------------------------------------------*/

    @Override
    protected TypeInstance lookupTypeInstance( Parse table, DataTable dt ) {
        return new TypeInstance();
    }
    
    @Override
    public InvokationOutput invokePrototype( TypeInstance t, DataTable table ) throws Exception {
        
        // Get options and reset
        Options opts = m_environment.getOptions();
        opts.reset();
        
        InvokationOutput res = new InvokationOutput();
        ArrayList<String[]> rc = new ArrayList<>();
        rc.add( new String[]{ "0" } );
        res.setReturnCode( rc );
        return res;
    }
}