

package flint.fixtures;

// Core Java classes
import java.util.Map;

// 3rd party classes
import fit.Fixture;
import fit.Parse;
import fit.exception.FitFailureException;

// Application classes
import flint.data.DataTable;
import flint.engine.AbstractEngine;
import flint.engine.InvokationInput;
import flint.engine.InvokationOutput;
import flint.environment.Environment;
import flint.framework.type.TypeDefinition;
import flint.framework.type.TypeInstance;
import flint.util.FixtureHelpers;
import flint.util.TableProcessor;


/**
 * 
 * @author Philip Bowditch
 */
public interface IBaseFixture {
    
    DataTable getTable();
    
    
    //--------------------------------------------------------------------------
    
    InvokationInput preInvokeAction( TypeInstance t, DataTable table, InvokationInput inp );
    
    InvokationOutput postInvokeAction( TypeInstance t, DataTable table, InvokationInput inp, InvokationOutput outp );
    
    Exception postInvokeException( TypeInstance t, DataTable table, InvokationInput inp, InvokationOutput outp, Exception ex );
    
    InvokationOutput invokePrototype( TypeInstance t, DataTable table ) throws Exception;
    
}