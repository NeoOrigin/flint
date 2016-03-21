package flint.fixture;

import flint.environment.Environment;

/**
 * A simple fixture that is used to drop a dataset
 * @author Philip Bowditch
 */
public class DropFixture extends SimpleFixture {
    
    //--------------------------------------------------------------------------
    
    public DropFixture( Environment environment, String label ) {
        super( environment, label );
    }
    
    //--------------------------------------------------------------------------
    
    /*
    @Override
    public String getFixtureName() {
        return "DROP";
    }*/
}
