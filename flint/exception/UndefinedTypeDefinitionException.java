package flint.exception;

/**
 * Thrown when trying to access a TypeDefinition that does not exist
 * @author Philip Bowditch
 */
public class UndefinedTypeDefinitionException extends Exception {

    public UndefinedTypeDefinitionException( String typ ) {
        super( "Definition '" + typ + "' is unknown or not definition" );
    }
    
}
