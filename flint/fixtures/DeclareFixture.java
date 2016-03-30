package flint.fixtures;

// Core Java classes
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Properties;

// 3rd Party classes
import fit.Fixture;
import fit.Parse;

// Application classes
import flint.environment.Environment;
import flint.exception.UndefinedTypeDefinitionException;
import flint.framework.BaseFramework;
import flint.framework.type.TypeDefinition;
import flint.framework.type.TypeInstance;
import flint.util.NameNormalizer;


/**
 * Represents a fixture that declares a type i.e. a file
 * a script, a table, a database etc
 *
 * @author Philip Bowditch
 */
public class DeclareFixture extends Fixture {

    /**
     * A definition consists of many sub key value pairs e.g. PATH=/tmp
     * Some of these values could themselves be references to other definitions
     */
    protected Map<String, Object> m_attributes;

    /**
     * A unique label/key for the definition
     */
    protected String m_label;
    
    /**
     * Used to hold validity status across doTable and doRow functions
     */
    protected boolean m_isValid;
    
    /**
     * Holds information about the environment we are running within
     */
    protected Environment m_env;


    /*------------------------------------------------------------------------*/

    /**
     * Constructor for the DeclareFixture class
     *
     * @param env The environment being used
     * @param label A Unique label/key for this definition
     */
    public DeclareFixture( Environment env, String label ) {
        this.m_label      = label;
        this.m_env        = env;
        this.m_isValid    = true;
        this.m_attributes = new LinkedHashMap<>();
    }


    /*------------------------------------------------------------------------*/
    
    /**
     * Returns whether we can overwrite or mask an item that is already declared
     * with the same name.  This is always set to true for the Redeclare Fixture
     * as effectively the user has explicitly declared it
     * 
     * @return True
     */
    public boolean isRedeclareAllowed() {
        return m_env.getOptions().isRedefineAllowed();
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Called on table parsing.
     * @param table The Parse representing the table being parsed
     */
    @Override
    public void doTable(Parse table) {
        BaseFramework framework = m_env.getFramework();
        
        // Error if we already have this item and we are not allowed to redeclare items
        // ensure it is removed before parsing the table incase of errors and we never get
        // around to redefining
        if ( m_env.getTypeInstances().containsKey( m_label ) ) {
            if ( isRedeclareAllowed() ) {
                m_env.getTypeInstances().remove( m_label );
            }
            else {
                this.exception( table.parts.parts, new Exception( m_label + " is already defined, try the REDEFINE fixture or set the option 'allowredefine' to true" ) );
                return;
            }
        }
            
        super.doTable(table); // load all attributes found, calls doRows before anything else
        
        //ArrayList<ArrayList<String>> tab = FixtureHelpers.parseToArrayList( table );
        
        // If no errors occurred during row parsing
        if ( m_isValid ) {

            try
            {
                // Every definition has a label to identify it add it even if user supplied one
                // already we always use what was declared
                m_attributes.put( "LABEL", m_label );

                Properties p = new Properties();
                p.putAll( m_attributes );
                
                // Get the type all these config entries are defining
                // and build an instance (overrides default values etc
                String         typ = p.getProperty( "type" );
                TypeDefinition d   = framework.getTypeDefinitions().get( typ );
                if ( d == null ) {
                    throw new UndefinedTypeDefinitionException( typ );
                }
                
                TypeInstance   t   = d.newInstance( p );
                
                m_env.getTypeInstances().put( m_label, t );
                
                //ValidationLevel lev = m_env.getOptions().getDefaultValidationLevel();

                // Success
                this.right(table.parts.parts);
                counts.right--;
            }
            catch (Exception ex) {
                this.exception(table.parts.parts, ex);
                m_isValid = false;
            }
        }
    }

    /**
     * Called on row parsing. All rows of this table are added to the attributes
     * @param row The Parse representing the row being parsed
     */
    @Override
    public void doRow(Parse row) {

        String name;
        String value;

        Parse newRow  = row;
        Parse newCell = newRow.parts;

        //
        // While there are cells in this row get the next key cell
        // move one, then get the text of the value cell.  Cells in declare statements
        // always come in pairs, attribute name / attribute value
        //
        for (int j = 0; newCell != null; j += 2, newCell = newCell.more) {
            name = NameNormalizer.normalizeName( newCell.text() );

            // Exit loop, there must be two cells, key and value, if just key then
            // ignore it
            if (newCell.more == null) {
                this.exception( newCell , new Exception( "Missing value for cell '" + name + "'" ) );
                m_isValid = false;
                break;
            }

            newCell = newCell.more;
            value   = newCell.text();

            m_attributes.put(name, value);
        }
    }
}