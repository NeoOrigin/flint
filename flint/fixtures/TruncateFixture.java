package flint.fixtures;

import flint.environment.Environment;

/**
 * Truncates a data source
 * @author Philip Bowditch
 */
public class TruncateFixture extends SimpleFixture {
    
    //--------------------------------------------------------------------------
    
    public TruncateFixture( Environment environment, String label ) {
        super( environment, label );
    }
    
    //--------------------------------------------------------------------------
    
    /*
    @Override
    public String getFixtureName() {
        return "TRUNCATE";
    }*/
}