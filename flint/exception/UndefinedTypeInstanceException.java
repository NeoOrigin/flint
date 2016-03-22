package flint.exception;

/**
 * Thrown when trying to access a TypeInstance that does not exist
 * @author Philip Bowditch
 */
public class UndefinedTypeInstanceException extends Exception {

    public UndefinedTypeInstanceException( String typ ) {
        super( "Type '" + typ + "' is unknown or not declared" );
    }
    
}
