package flint.fixtures;

// Core Java classes
import java.util.Map;

// 3rd party classes
import fit.ColumnFixture;
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
import java.util.Iterator;


/**
 * Used as a base class for all queryable fixtures
 * that return data
 * 
 * @author Philip Bowditch
 */
public abstract class QueryableFixture extends ColumnFixture implements IBaseFixture {
    
    /**
     * Reference to the environment for variables, options etc
     */
    protected Environment m_environment;
    
    /**
     * The name of target
     */
    protected String m_label;
    
    /**
     * An abstraction of the underlying table
     */
    protected DataTable m_table;
    
    /**
     * Determines if this fixtures results count towards a failure
     */
    protected boolean m_isTestable;
    
    protected int m_requiredParameters;
    
    
    //--------------------------------------------------------------------------
    
    public QueryableFixture( Environment environment, String label ) {
        super();
        
        m_environment        = environment;
        m_label              = label;
        m_table              = null;
        m_isTestable         = true;
        m_requiredParameters = 1;
    }
    
    
    //--------------------------------------------------------------------------
    
    @Override
    public DataTable getTable() {
        return m_table;
    }
    
    @Override
    public boolean isTestable() {
        return m_isTestable;
    }
    
    @Override
    public void setTestable( boolean testable ) {
        m_isTestable = testable;
    }
    
    @Override
    public int getNumberRequiredParameters() {
        return m_requiredParameters;
    }
    
    @Override
    public void configure( Map<String, String> props ) {
        Iterator it = props.keySet().iterator();
        String key;
        
        while ( it.hasNext() ) {
            key = (String)it.next();
            
            switch ( key ) {
                
                case "name"     : m_label = props.getOrDefault( key, m_label );
                                  break;
                case "testable" : m_isTestable = Boolean.valueOf( props.getOrDefault( key, "false" ) );
                                  break;
                                  
            }
        }
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
        
        TypeDefinition      def        = t.getDefinition();
        String              fixture    = table.getFixture();
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
        
        // Create a table to encapsulate all the data
        TableProcessor processor = new TableProcessor();
        processor.setTable( table );
        processor.setNumberRequiredParameters( m_requiredParameters );
        
        DataTable dt;
        
        try {
            dt = processor.process();
        }
        catch ( Exception ex ) {
            this.exception( table.parts.parts, ex );
            return;
        }
        
        
        super.doTable(table);
        
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
        
        if ( ! isTestable() ) {
            counts.right--;
        }
    }
    
    /**
     * Default implementation sets cells to ignored, we simply move over them
     * @param cell
     * @param columnNumber 
     */
    @Override
    public void doCell(Parse cell, int columnNumber) {
    }
    
}