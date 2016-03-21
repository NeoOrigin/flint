package flint.fixture;

// Core Java classes
import java.util.LinkedHashMap;
import java.util.Map;

// 3rd party classes
import fit.Fixture;
import fit.Parse;
import fit.exception.FitFailureException;

import flint.engine.AbstractEngine;
import flint.engine.InvokationInput;
import flint.engine.InvokationOutput;
import flint.environment.Environment;
import flint.framework.type.TypeDefinition;
import flint.framework.type.TypeInstance;
import flint.util.FixtureHelpers;


/**
 * Used as a base class for all simple fixtures
 * that do not return data
 * 
 * @author Philip Bowditch
 */
public abstract class SimpleFixture extends Fixture {
    
    /**
     * Holds the name of this type / how it will be referred as
     */
    protected String m_label;
    
    /**
     * Reference to the environment for variables, options etc
     */
    protected Environment m_environment;
    
    /**
     * Customises the Fixtures options
     */
    protected Map<String, String> fixtureParameters;
    
    //--------------------------------------------------------------------------
    
    public SimpleFixture( Environment environment, String label ) {
        
        m_environment = environment;
        m_label       = label;
        
        fixtureParameters = new LinkedHashMap<String, String>();
    }
    
    //--------------------------------------------------------------------------
    
    public String getLabel() {
        return m_label;
    }
    
    public void setLabel( String label ) {
        m_label = label;
    }
    
    public Map<String, String> getArguments() {
        return fixtureParameters;
    }
    
    //--------------------------------------------------------------------------
    
    public InvokationOutput invokePrototype( TypeInstance t, String fixture ) throws Exception {
        TypeDefinition def = t.getDefinition();
        
        // Get the underlying types base definitions and any overrides applied
        // byt the type declaration
                
        // Get the interface that supports this fixture
        InvokationOutput  o         = new InvokationOutput();
        InvokationInput   inp       = new InvokationInput();
        
        inp = FixtureHelpers.addTypeDefinition(inp, def);
        inp = FixtureHelpers.addTypeInstance(  inp, t, false          );
        inp = FixtureHelpers.addEnvironment(   inp, m_environment     );
        inp = FixtureHelpers.addArguments(     inp, fixtureParameters );

        inp.addControlParameter( "CALLING_FIXTURE" , fixture );
        
        //inp.setData( new String[][]{} );
        //inp.setArguments( new String[][]{} );
        
        // Get the engine that supports this definition
        AbstractEngine eng = m_environment.getSupportingEngine( def );
        
        // Invoke the protocol on the engine, this should return our data table
        try {
            inp = preInvokeAction( inp ); // Hook for subclasses
            
            // Invoke the protocol on the engine, this should return our data table
            o = eng.invoke( fixture, inp );
            
            o = postInvokeAction( inp, o );
        }
        catch (Exception ex) {
            ex.printStackTrace();
            ex = postInvokeException( inp, o, ex );
            if ( ex != null )
                throw ex;
        }
        
        return o;
    }
    
    @Override
    public void doTables(Parse table) {
    
        // Extract parameters from first line
        fixtureParameters = FixtureHelpers.getFixtureParameters( this, table );

        // Parse the table
        super.doTables(table);
    }
    
    /**
     * Called on table parsing.
     * @param table The Parse representing the table being parsed
     */
    @Override
    public void doTable(Parse table) {
        super.doTable(table);
        
        TypeInstance t = null;
        
        // Find the instance to work on
        // should have already been created
        try {
            t = FixtureHelpers.getTypeInstance( m_environment, m_label );
        }
        catch ( FitFailureException ex ) {
            this.exception( table.parts.parts, ex );
            return;
        }

        // Invoke the engine using the fixtures name as the action
        InvokationOutput  o = null;
        try {
            o = invokePrototype( t, getFixtureName() );
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
     */
    public abstract String getFixtureName();
}

