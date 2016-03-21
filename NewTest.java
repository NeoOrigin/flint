// 3rd party imports
import fit.Fixture;
import fit.Parse;

import fitlibrary.SequenceFixture;

// Application imports
import flint.environment.Environment;

import flint.fixture.CommentFixture;

import flint.util.NameNormalizer;

/**
 * Most of the functions here relate to Fixtures that can be built by the framework
 * they accept a ObjectInstanceProxy object, as it contains a parse method fitnesse
 * automatically takes the string value and returns the delegate
 * 
 * @author Philip Bowditch
 */
public class NewTest extends Fixture {

    /*- Variables ------------------------------------------------------------*/
    
    /**
     * Holds a reference to the environment used by this test, i.e. variables, options etc
     */
    protected Environment m_environment;
    
    
    /*- Constructors ---------------------------------------------------------*/
    
    /**
     * Constructor for the test class
     * @throws Exception 
     */
    public NewTest() throws Exception {
        this( "" );
    }

    /**
     * Constructor for the test class
     * 
     * @param withName The default name of the test
     * @throws Exception 
     */
    public NewTest( String withName ) throws Exception {
        this( withName, "default" );
    }

    /**
     * Constructor for the test class
     * @param withName The default name of the test
     * @param framework The name of the framework to run with
     * @throws Exception 
     */
    public NewTest( String withName, String usingFramework ) throws Exception {
        m_environment = new Environment( withName, usingFramework );
        m_environment.initialise();
    }
    
    
    /*- Functions ------------------------------------------------------------*/
    
    // ugly workaround since fitlibrary no longer allows this to be
    // overridden; we create an inner sequence fixture and pass the
    // execution to it, but this one is now a fixture to allow things to be overridden
    @Override
    public void interpretTables( Parse tables ) {

        //clearOptions();

    	SequenceFixture sf = new SequenceFixture();
    	sf.listener = listener;
    	sf.counts   = counts;
        sf.summary  = summary;

        sf.setSystemUnderTest(this);
      	sf.interpretTables(tables);
    }
    
    //--------------------------------------------------------------------------
    
    public Fixture comment( String label ) {
        return new CommentFixture(  m_environment, label );
    }
}
