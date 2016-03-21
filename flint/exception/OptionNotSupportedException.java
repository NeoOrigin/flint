package flint.exception;

/**
 *
 * @author Philip Bowditch
 */
public class OptionNotSupportedException extends Exception {
    
    public OptionNotSupportedException( String name ) {
        super( "Option '" + name + "' is not supported" );
    }
    
}
