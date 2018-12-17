/*
 * Created on 02-Dec-2004 @author Chris Morris
 */
package org.pimslims.metamodel;

// import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.Collection;

import org.pimslims.annotation.Attribute;
import org.pimslims.constraint.Constraint;
import org.pimslims.constraint.ConstraintFactory;
import org.pimslims.dao.ReadableVersionImpl;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;

/**
 * Represents a data model attribute. Not suitable for digestedPassword
 */
public class MetaAttributeImpl extends AbstractMetaProperty implements MetaAttribute {

    private Constraint constraint = null;

    protected final Attribute annotation;

    protected final boolean isNameAttribute;

    /**
     * @param metaClass the type this attribute belongs to
     * @param name the name of this attribute
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    MetaAttributeImpl(final MetaClassImpl metaClass, final String name) {
        super(metaClass, name);

        this.annotation = getAnnotation(Attribute.class);
        if (annotation == null) {
            throw new IllegalArgumentException("No annotation for attribute: " + name);
        }
        isNameAttribute = getIsName();

    }

    MetaAttributeImpl(final MetaClassImpl metaClass, final MetaAttributeImpl parentAttribute) {
        super(metaClass, parentAttribute);
        this.annotation = parentAttribute.annotation;
        this.constraint = parentAttribute.constraint;
        isNameAttribute = getIsName();

    }

    /**
     * @return
     */
    private boolean getIsName() {
        if (this.name.equalsIgnoreCase("name")) {
            return true;

        }
        return false;
    }

    public javax.persistence.Column getDBColumn() {
        try {
            final javax.persistence.Column column = getAnnotation(javax.persistence.Column.class);
            return column;
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object get(final ModelObject object) {
        return get_prop(object);
    }

    /**
     * {@inheritDoc}
     */
    public Constraint getConstraint() {
        if (constraint == null) {
            final java.util.Set<String> constraints = new java.util.HashSet<String>();
            for (final String constraintName : annotation.constraints()) {
                constraints.add(constraintName);
            }
            // TODO if (isUnique()) {constraints.add("unique");}
            if (isRequired()) {
                constraints.add("not_null");
            }
            constraint = ConstraintFactory.getConstraint(constraints);
        }
        return constraint;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.MetaAttribute#isUnique()
     */
    public boolean isUnique() {
        final javax.persistence.Column column = getAnnotation(javax.persistence.Column.class);
        return column.unique();

    }

    public Object getDefaultValue() {
        // TODO add default value in annotation
        return null;
    }

    /**
     * The maximum allowed length for string values. If it is not a string, 0 will be returned.
     * 
     * @return length of value
     */
    Integer length = null;

    public int getLength() {
        if (length == null) {
            final javax.persistence.Column column = getAnnotation(javax.persistence.Column.class);
            if (column == null) {
                length = 0;
            } else if (column.columnDefinition().equalsIgnoreCase("TEXT")) {
                length = -1;
            } else {
                length = column.length();
            }
        }
        return length;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(final Object value) {
        if (null == value && !isRequired()) {
            return true;
        }
        return getConstraint().verify(getName(), value, null);
    }

    /**
     * {@inheritDoc}
     */
    public void set(final ModelObject object, final Object value) throws AccessException, ConstraintException {
        set_prop(object, value);
    }

    /**
     * {@inheritDoc}
     */
    Boolean isChangeable = null;

    public boolean isChangeable() {
        if (isChangeable == null) {
            if (isDerived()) {
                isChangeable = false;
            } else {
                isChangeable = annotation.isChangeable();
            }

        }
        return isChangeable;
    }

    /**
     * {@inheritDoc}
     */
    Boolean isRequired = null;

    @Override
    public boolean isRequired() {
        if (isRequired == null) {
            final javax.persistence.Column column = getAnnotation(javax.persistence.Column.class);
            if (column == null) {
                isRequired = false;
            } else {
                isRequired = !column.nullable();
            }
        }
        return isRequired;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof MetaAttributeImpl)) {
            return false;
        }
        final MetaAttributeImpl othera = (MetaAttributeImpl) other;
        return othera.hashCode() == othera.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHelpText() {
        if (null == annotation.helpText()) {
            return "";
        }
        return annotation.helpText().trim();
    }

    /**
     * @return
     * 
     *         private String getBaseName() { return MetaUtils.getSingleName(this.name); }
     */

    /**
     * @see org.pimslims.hibernate.AbstractMetaProperty#set_prop(org.pimslims.metamodel.ModelObject,
     *      java.lang.Object)
     */
    @Override
    public void set_prop(final ModelObject mo, Object value) throws ConstraintException {
        if (((ReadableVersionImpl) mo.get_Version()).getFlushMode().isCheckValue()) {
            //access check
            if (!mo.get_MayUpdate()) {
                throw new ConstraintException("You do not have right to update " + mo.get_Name() + "["
                    + mo.get_Hook() + "]", new AccessException(mo));
                //TODO namelist desabled
                //if (this.isNameAttribute && BaseClass.class.isInstance(mo))
                //    updateNamelist((BaseClass) mo, value);
            }

            //set attribute
            //treat empty string as null
            if (value instanceof String && ((String) value).length() == 0) {
                value = null;
            }
            //to avoid set collection/list to a null
            if (value == null && Collection.class.isAssignableFrom(this.getType())) {
                value = new ArrayList();
            }
            //convert array to List
            if (value != null && value.getClass().isArray()) {
                value = java.util.Arrays.asList(new Object[] { value });
            }
            //check value by constraint
            if (this.getConstraint() != null) {
                if (!this.getConstraint().verify(this.getName(), value, mo)) {

                    throw new ConstraintException(mo, this.getName(), value, this.getConstraint().toString()
                        + " constraint violated!");
                }
            }
        }
        super.setValue(mo, value);

    }

    /**
     * @see org.pimslims.metamodel.MetaProperty#get_prop(org.pimslims.metamodel.ModelObject)
     */
    public Object get_prop(final ModelObject mo) {

        //get from attribute
        Object returnValue = super.getValue(mo);
        if (!((ReadableVersionImpl) mo.get_Version()).getFlushMode().isCheckValue()) {
            return returnValue;
        }
        //treat null string as empty
        if (returnValue == null && String.class.isAssignableFrom(getType())) {
            returnValue = "";
        } else if (returnValue == null && Collection.class.isAssignableFrom(getType())) {
            returnValue = new ArrayList();
        } else if (returnValue != null && Collection.class.isAssignableFrom(getType())) {
            returnValue = new ArrayList((Collection) returnValue);
        }

        return returnValue;

    }

    /**
     * MetaAttribute.isMulti
     * 
     * @see org.pimslims.metamodel.MetaAttribute#isMulti()
     */
    public boolean isMulti() {
        return MetaUtils.isMultiAttribute(this.getType());
    }

}
