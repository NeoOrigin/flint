package flint.framework.property.constraint;

import flint.framework.type.TypeInstance;

/**
 * Abstract base class used to construct constraints that must pass when setting type properties
 * @author Philip Bowditch
 */
public abstract class AbstractConstraint {

    public boolean isValid( TypeInstance inst );
    
}
