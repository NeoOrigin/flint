
package flint.fixtures;

import flint.environment.Environment;


/**
 * Physically creates the target described by a TypeInstance, e.g a database table
 * a file etc. 
 * @author Philip Bowditch
 */
public class CreateFixture extends SimpleFixture {
    
    /**
     * The constructor for the CreateFixture class
     * @param environment The environment to use
     * @param label The name of the target
     */
    public CreateFixture( Environment environment, String label ) {
        super( environment, label );
    }
    
    //--------------------------------------------------------------------------
    
    /*
    @Override
    public String getFixtureName() {
        return "CREATE";
    }*/
}
