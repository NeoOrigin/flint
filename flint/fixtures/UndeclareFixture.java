
package flint.fixtures;

// Core Java classes
import java.util.Map;

// 3rd Party classes
import fit.Parse;
import flint.data.DataTable;
import flint.engine.InvokationOutput;

// Application classes
import flint.environment.Environment;
import flint.environment.EnvironmentParameter;
import flint.framework.type.TypeInstance;
import java.util.ArrayList;


/**
 * Represents a fixture that undeclares a type i.e. a file
 * a script, a table, a database etc
 *
 * @author Philip Bowditch
 */
public class UndeclareFixture extends SimpleFixture {

    /**
     * Constructor for the UndeclareFixture class
     *
     * @param env The environment being used
     * @param label A Unique label/key for this definition
     */
    public UndeclareFixture( Environment env, String label ) {
        super( env, label );
    }

    
    //--------------------------------------------------------------------------
    
    @Override
    protected TypeInstance lookupTypeInstance( Parse table, DataTable dt ) {
        return new TypeInstance();
    }
    
    @Override
    public InvokationOutput invokePrototype( TypeInstance t, DataTable table ) throws Exception {
        
        Map<String, TypeInstance> instances = m_environment.getTypeInstances();
        
        // Error if we already have this item and we are not allowed to redeclare items
        // ensure it is removed before parsing the table incase of errors and we never get
        // around to redefining
        instances.remove( m_label );
        
        InvokationOutput res = new InvokationOutput();
        ArrayList<String[]> rc = new ArrayList<>();
        rc.add( new String[]{ "0" } );
        res.setReturnCode( rc );
        return res;
    }
}