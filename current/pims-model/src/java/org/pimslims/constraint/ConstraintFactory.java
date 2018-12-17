/*
 * Created on 21-Apr-2005 @author: Chris Morris
 */
package org.pimslims.constraint;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import org.pimslims.metamodel.ModelObject;

/**
 * Makes java constraints from the model information
 * 
 */
public class ConstraintFactory {

    /**
     * make a constraint object, capable of checking that the value of an attribute is valid
     * 
     * @param constraintNames names from model
     * @return an implementation of the constraint
     */
    public static Constraint getConstraint(final Set constraintNames) {
        if (constraintNames.isEmpty()) {
            return Constraint.NONE;
        }
        Set<Constraint> constraints = new java.util.HashSet<Constraint>();
        Constraint constraint = null; // last constraint in set
        for (Iterator iter = constraintNames.iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            constraint = getAConstraint(name);
            constraints.add(constraint);
        }
        if (constraints.size() == 1) {
            return constraint;
        }
        return new Constraint.And(constraints);
    }

    private static Constraint getAConstraint(final String name) {
        if (!CONSTRAINTS.containsKey(name))
            if (name.startsWith("value_must_be_one_of")) {
                CONSTRAINTS.put(name,
                    new Constraint.EnumerationConstraint(name.substring("value_must_be_one_of".length())));
                // e.g. value_must_be_one_of('kg', 'L', 'number')
            } else if (name.startsWith("value_must_be_")) {
                CONSTRAINTS.put(name, new Constraint.ValueMustBe(name.substring("value_must_be_".length())));
                // e.g. value_must_be_gel
            }
        assert CONSTRAINTS.containsKey(name) : "unsupported constraint: " + name;
        return CONSTRAINTS.get(name);
    }

    public static final Constraint CONTAINS_NO_BACKSLASH = new StringConstraint() {
        private static final long serialVersionUID = 3569247614126059749L;

        @Override
        public boolean doVerify(final String value) {
            return null == value || -1 == (value).indexOf('\\');
        }

        @Override
        public String toString() {
            return "contains_no_backslash";
        }
    };

    public static final Constraint CONTAINS_NO_LINEBREAK = new StringConstraint() {
        private static final long serialVersionUID = -677550331124536069L;

        @Override
        public boolean doVerify(final String value) {
            if (null == value) {
                return true;
            }
            return -1 == (value).indexOf('\n');
        }

        @Override
        public String toString() {
            return "contains_no_linebreak";
        }
    };

    public static final Constraint NO_WHITE_SPACE = new StringConstraint() {
        private static final long serialVersionUID = 5552115298299287476L;

        @Override
        public boolean doVerify(final String value) {
            return null == value || !value.matches("\\s");
        }

        @Override
        public String toString() {
            return "no_white_space";
        }
    };

    public static final Constraint NAME_TYPE_CHARACTERS = new StringConstraint() {
        private static final long serialVersionUID = -8503641161871931982L;

        @Override
        public boolean doVerify(final String value) {
            String strValue = value;
            if ((value == null) || (strValue.length() == 0))
                return false;
            else if ("'abcdefghjiklmnopqrstuvwxyz_ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(strValue.charAt(0)) < 0)
                return false;
            else {
                for (int cc = 1; cc < strValue.length(); cc++) {
                    if ("abcdefghjiklmnopqrstuvwxyz_ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".indexOf(strValue
                        .charAt(cc)) < 0) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public String toString() {
            return "name_type_characters";
        }
    };

    public static final Constraint SEQ_STRING_CHARACTERS = new StringConstraint() {
        private static final long serialVersionUID = -8917494288624780953L;

        @Override
        public boolean doVerify(final String value) {
            String strValue = value;
            if ((value == null) || (strValue.length() == 0))
                return false;
            for (int cc = 0; cc < strValue.length(); cc++) {
                if ("abcdefghjiklmnopqrstuvwxyz_ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".indexOf(strValue
                    .charAt(cc)) < 0) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public String toString() {
            return "seq_string_characters";
        }
    };

    public static Constraint INT_VALUE_GT_0 = new Constraint() {
        private static final long serialVersionUID = 4949946600808427794L;

        public boolean verify(final String name, final Object value, final ModelObject object) {
            if (value == null)
                return true;
            return (0 < ((Integer) value).intValue());
        }

        @Override
        public String toString() {
            return "int_value_gt_0";
        }
    };

    public static Constraint VALUE_BETWEEN_0_AND_1_INCL = new Constraint() {
        private static final long serialVersionUID = -1230786618262710158L;

        public boolean verify(final String name, final Object value, final ModelObject object) {
            if (value == null)
                return true;
            return (0.0f <= ((Float) value).floatValue()) && (((Float) value).floatValue() <= 1.0);
        }

        @Override
        public String toString() {
            return "value_between_0_and_1_incl";
        }
    };

    public static final Constraint NOT_NULL = Constraint.NOT_NULL;

    private static final java.util.Map<String, Constraint> CONSTRAINTS =
        new java.util.HashMap<String, Constraint>();
    static {
        CONSTRAINTS.put("not_null", Constraint.NOT_NULL);
        CONSTRAINTS.put("contains_no_linebreak", CONTAINS_NO_LINEBREAK);
        CONSTRAINTS.put("contains_no_backslash", CONTAINS_NO_BACKSLASH);
        CONSTRAINTS.put("no_white_space", NO_WHITE_SPACE);
        CONSTRAINTS.put("seq_string_characters", SEQ_STRING_CHARACTERS);
        CONSTRAINTS.put("int_value_gt_0", INT_VALUE_GT_0);
    }

    /**
     * Dummy constructor - cannot be instantiated.
     */
    private ConstraintFactory() {
        // this class has static methods only
    }

    /**
     * StringConstraint Utility class for making constraints that apply to strings, lists of strings, and
     * arrays of strings
     */
    public static abstract class StringConstraint implements Constraint {

        public boolean verify(final String name, final Object value, final ModelObject object) {
            if (null == value) {
                return true;
            }
            // value could be a string or a list or array of strings

            if (value instanceof String) {
                return doVerify((String) value);
            }
            java.util.Collection<String> strings = null;
            if (value instanceof java.util.Collection) {
                strings = (java.util.Collection) value;
            } else {
                assert value instanceof String[];
                strings = Arrays.asList((String[]) value);
            }
            for (Object element : strings) {
                String s = (String) element;
                if (!doVerify(s)) {
                    return false;
                }
            }
            return true;
        }

        /**
         * @param string
         * @return boolean
         */
        protected abstract boolean doVerify(String string);

        @Override
        public abstract String toString();
    }

}
