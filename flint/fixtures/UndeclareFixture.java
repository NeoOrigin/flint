
package flint.fixtures;

// Core Java classes
import java.util.Map;

// 3rd Party classes
import fit.Parse;

// Application classes
import flint.environment.Environment;
import flint.framework.type.TypeInstance;


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
    
    /**
     * Called on table parsing.
     * @param table The Parse representing the table being parsed
     */
    @Override
    public void doTable(Parse table) {
        
        Map<String, TypeInstance> instances = m_environment.getTypeInstances();
        
        // Error if we already have this item and we are not allowed to redeclare items
        // ensure it is removed before parsing the table incase of errors and we never get
        // around to redefining
        instances.remove( m_label );
            
        //super.doTable(table);
        
        this.right(table.parts.parts);
        if ( ! isTestable() ) {
            counts.right--;
        }
    }
}