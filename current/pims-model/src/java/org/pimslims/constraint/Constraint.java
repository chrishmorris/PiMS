/*
 * Created on 26-Jul-2004 @author: Chris Morris
 */
package org.pimslims.constraint;

import java.util.Collection;
import java.util.LinkedList;

import org.pimslims.metamodel.Invariant;
import org.pimslims.metamodel.ModelObject;

/**
 * Represents a rule restricting the value that a model attribute can have. Some examples are below.
 * <p>
 * Note that if the value will be checked by the database, it is acceptable for the implementation of
 * Constraint to always return true. There should still be a distinct Constraint object for each semantic
 * constraint. These may be used to provide more responsive checks in the UI, e.g. javascript functions.
 * </p>
 * <p>
 * The whole object is available, so this interface could be used for constraints that depend on the value of
 * other attributes. However, it is suggested that these are implemented as Invariants instead.
 * </p>
 * 
 * @see Invariant
 * @version 0.1
 */
public interface Constraint extends java.io.Serializable {

    /**
     * @param name of attribute being checked
     * @param value desired
     * @param object being checked
     * @return true if the constraint is satified
     */
    boolean verify(String name, Object value, ModelObject object);

    /**
     * @return an appropriate name for the constraint
     */
    String toString();

    /**
     * An empty constraint, used when the values are unconstrained.
     */
    Constraint NONE = new Constraint() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public boolean verify(final String name, final Object value, final ModelObject object) {
            return true;
        }

        @Override
        public String toString() {
            return "unconstrained";
        }
    };

    /**
     * A constraint to ensure that the attribute is not set to NULL
     */
    Constraint NOT_NULL = new Constraint() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public boolean verify(final String name, final Object value, final ModelObject object) {
            return value != null;
        }

        @Override
        public String toString() {
            return "not_null";
        }
    };

    /**
     * A constraint to ensures that the value is null.
     * 
     * Used for the required attribute owner, to make all access objects world-readable and only writable by
     * admins.
     */
    Constraint IS_NULL = new Constraint() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public boolean verify(final String name, final Object value, final ModelObject object) {
            return value == null;
        }

        @Override
        public String toString() {
            return "is_null";
        }
    };

    /**
     * A constraint to ensure that a floating point value is zero or greater
     */
    Constraint NON_NEGATIVE = new Constraint() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public boolean verify(final String name, final Object value, final ModelObject object) {
            Float f = (Float) value;
            return f.floatValue() >= 0.0f;
        }

        @Override
        public String toString() {
            return "non_negative";
        }
    };

    /**
     * Compounds constraints.
     * 
     * Inner type of Constraint
     * 
     * @version 0.1
     */
    class And implements Constraint {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        private final java.util.Set<Constraint> constraints;

        /**
         * @param c1 first sub-constraint
         * @param c2 second sub-constraint
         */
        public And(final Constraint c1, final Constraint c2) {
            super();
            constraints = new java.util.HashSet<Constraint>();
            constraints.add(c1);
            constraints.add(c2);
        }

        /**
         * @param constraints List of the sub-constraints
         */
        @SuppressWarnings("unchecked")
        public And(final Collection constraints) {
            super();
            this.constraints = new java.util.HashSet<Constraint>(constraints);
        }

        /**
         * {@inheritDoc}
         */
        public boolean verify(final String name, final Object value, final ModelObject object) {

            for (Object element : constraints) {
                Constraint c = (Constraint) element;
                if (!c.verify(name, value, object)) {
                    return false;
                }
            }
            return true;
        }

        public Collection<Constraint> getSubConstraints() {
            return constraints;
        }

        @Override
        public String toString() {
            String ret = "";
            for (Object element : constraints) {
                Constraint c = (Constraint) element;
                ret = ret + " and " + c.toString();
            }

            return ret.substring(" and ".length());

        }
    }

    /**
     * Sets a maximum length for a string attribute.
     * 
     * Inner type of Constraint
     * 
     * @version 0.1
     */
    class Length implements Constraint {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * the maximum length
         */
        public final int length;

        /**
         * @param length the maximum length allowed
         */
        public Length(final int length) {
            super();
            this.length = length;
        }

        /**
         * {@inheritDoc}
         */
        public boolean verify(final String name, final Object value, final ModelObject object) {
            return value instanceof String && ((String) value).length() <= length;
        }
    }

    /**
     * A constraint that only accepts a single value, e.g. for experimentType
     * 
     * Inner type of Constraint
     * 
     * @version 0.1
     */
    public class ValueMustBe implements Constraint {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * The only permitted value for this attribute
         */
        public final Object requiredValue;

        /**
         * @param requiredValue the only premitted value
         */
        public ValueMustBe(final Object requiredValue) {
            super();
            this.requiredValue = requiredValue;
        }

        /**
         * {@inheritDoc}
         */
        public boolean verify(final String name, final Object value, final ModelObject object) {
            return requiredValue.equals(value);
        }

        @Override
        public String toString() {
            return "value_must_be_" + requiredValue;
        }
    }

    /**
     * A constraint to ensure that the value is one of a fixed list.
     * 
     * Inner type of Constraint
     * 
     * @version 0.1
     */
    public class EnumerationConstraint implements Constraint {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * the permitted values
         */
        public final java.util.Collection<String> allowedValues;

        /**
         * @param allowedValues
         */
        public EnumerationConstraint(final java.util.List<String> allowedValues) {
            super();
            this.allowedValues = java.util.Collections.unmodifiableList(allowedValues);
        }

        /**
         * eg:('kg', 'L', 'number')
         * 
         * @param substring
         */
        public EnumerationConstraint(String allowedValuesString) {
            super();
            //remove char: ( ) '
            String valuesString = allowedValuesString.replace("'", "");
            valuesString = valuesString.replace("(", "");
            valuesString = valuesString.replace(")", "");
            //seperate by ,
            String[] rawAllowedValues = valuesString.split(",");
            java.util.List<String> allowedValues = new LinkedList<String>();
            for (int i = 0; i < rawAllowedValues.length; i++) {
                //trim and add to allowedValues
                allowedValues.add(rawAllowedValues[i].trim());
            }

            this.allowedValues = java.util.Collections.unmodifiableList(allowedValues);
        }

        /**
         * {@inheritDoc}
         */
        public boolean verify(String name, Object value, ModelObject object) {
            if (value == null) // this constrain does not check not null
                return true;
            return allowedValues.contains(value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            String ret = "";
            for (Object value : allowedValues) {
                ret = ret + "," + value.toString();
            }
            return "must_be_one_of(" + ret.substring(1) + ")";
        }
    }

}
