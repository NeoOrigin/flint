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
 * Used as a base class for all simple fixtures
 * that do not return data
 * 
 * @author Philip Bowditch
 */
public abstract class SimpleFixture extends Fixture implements IBaseFixture {
    
    /**
     * Reference to the environment for variables, options etc
     */
    protected Environment m_environment;
    
    /**
     * An abstraction of the underlying table
     */
    protected DataTable m_table;
    
    
    //--------------------------------------------------------------------------
    
    public SimpleFixture( Environment environment, String label ) {
        super();
        
        m_environment = environment;
        
        m_table = null;
    }
    
    
    //--------------------------------------------------------------------------
    
    @Override
    public DataTable getTable() {
        return m_table;
    }
    
    
    //--------------------------------------------------------------------------
    
    @Override
    public InvokationInput preInvokeAction( TypeInstance t, DataTable table, InvokationInput inp ) {
        return inp;
    }
    
    @Override
    public InvokationOutput postInvokeAction( TypeInstance t, DataTable table, InvokationInput inp, InvokationOutput outp ) {
        return outp;
    }
    
    @Override
    public Exception postInvokeException( TypeInstance t, DataTable table, InvokationInput inp, InvokationOutput outp, Exception ex ) {
        return ex;
    }
    
    @Override
    public InvokationOutput invokePrototype( TypeInstance t, DataTable table ) throws Exception {
        TypeDefinition def = t.getDefinition();
        String fixture = table.getFixture();
        Map<String, String> parameters = table.getParameters();
        
        // Get the underlying types base definitions and any overrides applied
        // byt the type declaration
                
        // Get the interface that supports this fixture
        InvokationOutput  o         = new InvokationOutput();
        InvokationInput   inp       = new InvokationInput();
        
        inp = FixtureHelpers.addTypeDefinition(inp, def);
        inp = FixtureHelpers.addTypeInstance(  inp, t, false       );
        inp = FixtureHelpers.addEnvironment(   inp, m_environment  );
        inp = FixtureHelpers.addArguments(     inp, parameters     );

        inp.addControlParameter( "CALLING_FIXTURE" , fixture );
        
        //inp.setColumns( table.getColumns() );
        //inp.setData( table.getRows() );
        
        // Get the engine that supports this definition
        AbstractEngine eng = m_environment.getSupportingEngine( def );
        
        // Invoke the protocol on the engine, this should return our data table
        try {
            inp = preInvokeAction( t, table, inp ); // Hook for subclasses
            
            // Invoke the protocol on the engine, this should return our data table
            o = eng.invoke( fixture, inp );
            
            o = postInvokeAction( t, table, inp, o );
        }
        catch (Exception ex) {
            ex = postInvokeException( t, table, inp, o, ex );
            if ( ex != null )
                throw ex;
        }
        
        return o;
    }
    
    /**
     * Called on table parsing.
     * @param table The Parse representing the table being parsed
     */
    @Override
    public void doTable(Parse table) {
        super.doTable(table);
        
        // Create a table to encapsulate all the data
        TableProcessor processor = new TableProcessor();
        processor.setTable( table );
        
        DataTable dt;
        
        try {
            dt = processor.process();
        }
        catch ( Exception ex ) {
            this.exception( table.parts.parts, ex );
            return;
        }
        
        
        // Try to obtain the instance pointed to by the data
        TypeInstance t;
        
        // Find the instance to work on
        // should have already been created
        try {
            t = FixtureHelpers.getTypeInstance( m_environment, dt.getName() );
        }
        catch ( FitFailureException ex ) {
            this.exception( table.parts.parts, ex );
            return;
        }


        // Invoke the engine using the data nad the instance pointed to
        InvokationOutput  o;
        try {
            o = invokePrototype( t, dt );
        }
        catch ( Exception ex ) {
            this.exception(table.parts, ex );
            return;
        }

        // If something went wrong then error
        if ( o == null ) {
            this.exception(table.parts, new Exception( "Interface returned a null result" ) );
            return;
        }
        
        if ( !o.getReturnCode().get(0)[0].equals( "0" ) ) {
            this.exception(table.parts.parts, new Exception( o.getErrorData().get(0)[0] ) );
            return;
        }
        
        // So we can go green and yet not add to the count of tests passing
        this.right(table.parts);
        counts.right--;
    }
    
    /**
     * Returns the key detailing which prototype should be used on the instance to
     * handle this Fixtures capability
     * @return The lookup key to use
     *
    public abstract String getFixtureName();*/
}

