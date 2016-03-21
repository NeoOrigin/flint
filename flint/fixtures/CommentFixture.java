package flint.fixture;

import flint.environment.Environment;

/**
 * A simple fixture that is just a placeholder for creating cells that act as comments
 * @author Philip Bowditch
 */
public class CommentFixture extends SimpleFixture {
    
    //--------------------------------------------------------------------------
    
    public CommentFixture( Environment environment, String label ) {
        super( environment, label );
    }
    
    //--------------------------------------------------------------------------
    
    /*
    @Override
    public String getFixtureName() {
        return "COMMENT";
    }*/
}

