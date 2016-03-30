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
 * Re-sets a framework option back to its default
 * @author Philip Bowditch
 */
public class ResetOptionFixture extends SimpleFixture {

    /**
     * Constructor for the ResetOptionFixture class
     * @param environment The environment to use
     * @param label The option to reset
     */
    public ResetOptionFixture( Environment environment, String label ) {
        super( environment, label );
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
        
        // reset
        opts.resetOption(m_label);
        
        InvokationOutput res = new InvokationOutput();
        ArrayList<String[]> rc = new ArrayList<>();
        rc.add( new String[]{ "0" } );
        res.setReturnCode( rc );
        return res;
    }
}