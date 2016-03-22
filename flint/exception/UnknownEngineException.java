package flint.exception;

/**
 * Thrown when trying to access an engine that could not be found
 * @author Philip Bowditch
 */
public class UnknownEngineException extends Exception {
    
    public UnknownEngineException( String engine ) {
        super( "Engine '" + engine + "' is not understood or could not be found" );
    }
}
