package flint.fixtures;

import flint.environment.Environment;

/**
 *
 * @author Philip Bowditch
 */
public class CreateOrReplaceFixture extends SimpleFixture {
    
    //--------------------------------------------------------------------------
    
    public CreateOrReplaceFixture( Environment environment, String label ) {
        super( environment, label );
    }
    
    //--------------------------------------------------------------------------
    
    /*
    @Override
    public String getFixtureName() {
        return "CREATE_OR_REPLACE";
    }*/
}