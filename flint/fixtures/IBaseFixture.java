package flint.fixtures;

// Core Java classes
import java.util.Map;

// Application classes
import flint.data.DataTable;
import flint.engine.InvokationInput;
import flint.engine.InvokationOutput;
import flint.framework.type.TypeInstance;


/**
 * Base interface all fixtures should adhere to
 * so that they inter-operate with the framework
 * @author Philip Bowditch
 */
public interface IBaseFixture {
    
    DataTable getTable();
    
    boolean isTestable();
    
    void setTestable( boolean testable );
    
    int getNumberRequiredParameters();

    void configure( Map<String, String> props );
    
    
    //--------------------------------------------------------------------------
    
    InvokationInput preInvokeAction( TypeInstance t, DataTable table, InvokationInput inp );
    
    InvokationOutput postInvokeAction( TypeInstance t, DataTable table, InvokationInput inp, InvokationOutput outp );
    
    Exception postInvokeException( TypeInstance t, DataTable table, InvokationInput inp, InvokationOutput outp, Exception ex );
    
    InvokationOutput invokePrototype( TypeInstance t, DataTable table ) throws Exception;
    
}