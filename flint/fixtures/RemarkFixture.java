package flint.fixtures;

// Application classes
import fit.Parse;
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
    
    /**
     * Called on table parsing.
     * @param table The Parse representing the table being parsed
     */
    @Override
    public void doTable(Parse table) {
        this.right(table.parts);
        counts.right--;
    }
}

