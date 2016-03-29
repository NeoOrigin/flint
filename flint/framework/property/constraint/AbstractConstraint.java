package flint.framework.property.constraint;

// Application classes
import flint.framework.type.TypeInstance;


/**
 * Abstract base class used to construct constraints that must pass when setting type properties
 * @author Philip Bowditch
 */
public abstract class AbstractConstraint {

    /**
     * Returns whether the constraint succeeded.
     */
    public abstract boolean isValid( TypeInstance inst );
    
}
