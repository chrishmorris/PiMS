/*
 * Created on 26-Jul-2004 @author: Chris Morris
 */
package org.pimslims.metamodel;

import org.pimslims.constraint.Constraint;

/**
 * Represents a consistency rule for a model type.
 * <p>
 * Note that if the rule will be checked by the RDBMS, it is acceptable for this to return true. There should
 * still be a distinct Invariant object to represent each semantic invariant.
 * </p>
 * 
 * @see Constraint
 * @version 0.1
 */
public interface Invariant extends java.io.Serializable {

    /**
     * @param object to check
     * @return true if the state of the object is consistent
     */
    boolean verify(ModelObject object);

    /**
     * An invariant that is always satisfied.
     */
    Invariant NONE = new Invariant() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public boolean verify(final ModelObject object) {
            return true;
        }
    };
}
