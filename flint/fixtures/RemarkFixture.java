package flint.fixtures;

// Application classes
import fit.Parse;
import flint.data.DataTable;
import flint.engine.InvokationOutput;
import flint.environment.Environment;
import flint.framework.type.TypeInstance;
import java.util.ArrayList;

/**
 * A simple fixture that is just a placeholder for creating cells that act as comments
 * @author Philip Bowditch
 */
public class RemarkFixture extends SimpleFixture {
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the RemarkFixture class
     * @param environment The environment to use
     * @param label The comment to set
     */
    public RemarkFixture( Environment environment, String label ) {
        super( environment, label );
    }
    
    //--------------------------------------------------------------------------
    
    /*
    @Override
    public String getFixtureName() {
        return "COMMENT";
    }*/
    
    @Override
    protected TypeInstance lookupTypeInstance( Parse table, DataTable dt ) {
        return new TypeInstance();
    }
    
    @Override
    public InvokationOutput invokePrototype( TypeInstance t, DataTable table ) throws Exception {
        InvokationOutput res = new InvokationOutput();
        ArrayList<String[]> rc = new ArrayList<>();
        rc.add( new String[]{ "0" } );
        res.setReturnCode( rc );
        return res;
    }
}

