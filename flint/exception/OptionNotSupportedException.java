package flint.exception;

/**
 * Thrown when an option is accessed that is unknown
 * @author Philip Bowditch
 */
public class OptionNotSupportedException extends Exception {
    
    public OptionNotSupportedException( String name ) {
        super( "Option '" + name + "' is not supported" );
    }
    
}
