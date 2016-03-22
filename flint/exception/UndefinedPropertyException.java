package flint.exception;

/**
 * Raised when a property is accessed on a TypeInstance but does not exist
 * @author Philip Bowditch
 */
public class UndefinedPropertyException extends Exception {
    
    public UndefinedPropertyException( String property ) {
        super( "Property '" + property + "' is unknown or not defined" );
    }
}