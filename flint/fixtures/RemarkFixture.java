package flint.fixtures;

// Application classes
import flint.environment.Environment;

/**
 * A simple fixture that is just a placeholder for creating cells that act as comments
 * @author Philip Bowditch
 */
public class RemarkFixture extends SimpleFixture {
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the RemarkFixture class
     * @param environment The environment to use
     * @param label The comment to set
     */
    public RemarkFixture( Environment environment, String label ) {
        super( environment, label );
    }
    
    //--------------------------------------------------------------------------
    
    /*
    @Override
    public String getFixtureName() {
        return "COMMENT";
    }*/
}

