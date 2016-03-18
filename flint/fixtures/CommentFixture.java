package flint.fixture;

import flint.environment.Environment;

/**
 *
 * @author Philip Bowditch
 */
public class CommentFixture extends SimpleFixture {
    
    //--------------------------------------------------------------------------
    
    public CommentFixture( Environment environment, String label ) {
        super( environment, label );
    }
    
    //--------------------------------------------------------------------------
    
    @Override
    public String getFixtureName() {
        return "COMMENT";
    }
}

