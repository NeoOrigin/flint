// 3rd party imports
import fit.Fixture;
import fit.Parse;

import fitlibrary.SequenceFixture;

// Application imports
import flint.environment.Environment;

import flint.fixtures.*;

import flint.util.NameNormalizer;
import java.util.Map;

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
     * @param usingFramework The name of the framework to run with
     * @throws Exception 
     */
    public NewTest( String withName, String usingFramework ) throws Exception {
        
        // Create the environment with a test name, and a lookup string for the underlying framework
        m_environment = new Environment( withName, usingFramework );
        m_environment.initialise();
    
    }
    
    
    /*- Functions ------------------------------------------------------------*/

    @Override
    public void interpretTables( Parse tables ) {

        // Mark this as correct and move on, have to do this now
        // Before the sequence fixture takes over, shame
        // fitnesse needs at least one definitive count, cant all be non
        // testable so by default this is always right
        this.right(tables.parts.parts);
        
        SequenceFixture sf = new SequenceFixture();
    	sf.listener = listener;
    	sf.counts   = counts;
        sf.summary  = summary;

        sf.setSystemUnderTest(this);
      	sf.interpretTables(tables);
        
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Creates a type instance
     * @param label The name of type to create
     * @return
     */
    public Fixture create( String label ) {
        return create( label, null );
    }
    
    /**
     * Creates a type instance
     * @param label The name of type to create
     * @return
     */
    public Fixture create( String label, Map<String, String> args ) {
        return new CreateFixture(  m_environment, NameNormalizer.normalizeName( label ) );
    }
    
    /**
     * Creates a type instance, replacing it if it already exists
     * @param label The name of type to create
     * @return
     */
    public Fixture createOrReplace( String label ) {
        return createOrReplace( label, null );
    }
    
    /**
     * Creates a type instance, replacing it if it already exists
     * @param label The name of type to create
     * @return
     */
    public Fixture createOrReplace( String label, Map<String, String> args ) {
        return new CreateOrReplaceFixture(  m_environment, NameNormalizer.normalizeName( label ) );
    }
    
    /**
     * Defines a type that can later be instantiated
     * @param label The name of the type to define
     * @return
     */
    public Fixture define( String label ) {
        return define( label, null );
    }
    
    /**
     * Defines a type that can later be instantiated
     * @param label The name of the type to define
     * @param args Any fixture arguments
     * @return
     */
    public Fixture define( String label, Map<String, String> args ) {
        return new DefineFixture( m_environment, NameNormalizer.normalizeName( label ) );
    }
    
    /**
     * Declares a dataset that can be operated upon.
     * @param label The lookup name of the dataset
     * @return
     */
    public Fixture declare( String label ) {
        return declare( label, null );
    }
    
    /**
     * Declares a dataset that can be operated upon.
     * @param label The lookup name of the dataset
     * @return
     */
    public Fixture declare( String label, Map<String, String> args ) {
        return new DeclareFixture( m_environment, NameNormalizer.normalizeName( label ) );
    }
    
    /**
     * Drops a dataset
     * @param label The dataset to drop
     * @return
     */
    public Fixture drop( String label ) {
        return drop( label, null );
    }
    
    /**
     * Drops a dataset
     * @param label The dataset to drop
     * @return
     */
    public Fixture drop( String label, Map<String, String> args ) {
        return new DropFixture( m_environment, NameNormalizer.normalizeName( label ) );
    }
    
    /**
     * Acts as a simple comment, formatted as a data table
     * @param label The text to use as a comment
     * @return
     */
    public Fixture remark( String label ) {
        return remark( label, null );
    }
    
    /**
     * Acts as a simple comment, formatted as a data table
     * @param label The text to use as a comment
     * @return
     */
    public Fixture remark( String label, Map<String, String> args ) {
        // Do not normalize a comment, we only normalize legitimate names
        return new RemarkFixture( m_environment, label );
    }
    
    /**
     * Resets all previously applied options
     * @return
     */
    public Fixture resetAllOptions(){
        return resetAllOptions( null );
    }
    
    /**
     * Resets all previously applied options
     * @return
     */
    public Fixture resetAllOptions( Map<String, String> args ){
        return new ResetAllOptionsFixture( m_environment );
    }
    
    /**
     * Resets a given option back to its default value
     * @param label The name of the option to reset
     * @return
     */
    public Fixture resetOption( String label ){
        return resetOption( label, null );
    }
    
    /**
     * Resets a given option back to its default value
     * @param label The name of the option to reset
     * @return
     */
    public Fixture resetOption( String label, Map<String, String> args ){
        return new ResetOptionFixture( m_environment, NameNormalizer.normalizeName( label ) );
    }
    
    /**
     * Sets an options value
     * @param label The option name to set
     * @param value The value to set
     * @return
     */
    public Fixture setOption( String label, String value ){
        return setOption( label, value, null );
    }
    
    /**
     * Sets an options value
     * @param label The option name to set
     * @param value The value to set
     * @return
     */
    public Fixture setOption( String label, String value, Map<String, String> args ){
        return new SetOptionFixture( m_environment, NameNormalizer.normalizeName( label ), value );
    }
    
    /**
     * Sets an environment parameter value
     * @param label The environment parameter name to set
     * @param value The value to set
     * @return
     */
    public Fixture setParameter( String label, String value ){
        return setParameter( label, value, null );
    }
    
    /**
     * Sets an environment parameter value
     * @param label The environment parameter name to set
     * @param value The value to set
     * @return
     */
    public Fixture setParameter( String label, String value, Map<String, String> args ){
        return new SetParameterFixture( m_environment, label, value );
    }
    
    /**
     * Truncates a dataset
     * @param label The dataset to truncate
     * @return
     */
    public Fixture truncate( String label ) {
        return truncate( label, null );
    }
    
    /**
     * Truncates a dataset
     * @param label The dataset to truncate
     * @return
     */
    public Fixture truncate( String label, Map<String, String> args ) {
        return new TruncateFixture( m_environment, NameNormalizer.normalizeName( label ) );
    }
    
    /**
     * Removes a dataset that can be operated upon.
     * @param label The lookup name of the dataset
     * @return
     */
    public Fixture undeclare( String label ) {
        return undeclare( label, null );
    }
    
    /**
     * Removes a dataset that can be operated upon.
     * @param label The lookup name of the dataset
     * @return
     */
    public Fixture undeclare( String label, Map<String, String> args ) {
        return new UndeclareFixture( m_environment, NameNormalizer.normalizeName( label ) );
    }
    
    /**
     * Removes all environment parameters
     * @return
     */
    public Fixture unsetAllParameters(){
        return unsetAllParameters( null );
    }
    
    /**
     * Removes all environment parameters
     * @return
     */
    public Fixture unsetAllParameters( Map<String, String> args ){
        return new UnsetAllParametersFixture( m_environment );
    }
    
    /**
     * Removes an environment parameter value
     * @param label The environment parameter name to remove
     * @return
     */
    public Fixture unsetParameter( String label ){
        return unsetParameter( label, null );
    }
    
    /**
     * Removes an environment parameter value
     * @param label The environment parameter name to remove
     * @return
     */
    public Fixture unsetParameter( String label, Map<String, String> args ){
        return new UnsetParameterFixture( m_environment, label );
    }
}
