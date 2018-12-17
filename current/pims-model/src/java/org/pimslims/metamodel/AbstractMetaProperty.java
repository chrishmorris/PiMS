/**
 * org.pimslims.metamodel AbstractMetaProperty.java Represents an attribute or role of a data model class. Not
 * suitable for digestedPassword.
 * 
 * @date 7 Dec 2007 09:55:10
 * 
 * @author Bill Lin
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 2.0
 * 
 *           Copyright (c) 2007
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.metamodel;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabBookEntry;

/**
 * AbstractProperty
 * 
 */
public abstract class AbstractMetaProperty implements MetaProperty {
    protected final String name;

    protected final MetaClass metaClass;

    private java.lang.reflect.Method getter = null;

    private java.lang.reflect.Field field = null;

    private Class type = null;

    /**
     * 
     */
    public AbstractMetaProperty(final MetaClass metaClass, final String name) {
        super();
        this.name = name;
        this.metaClass = metaClass;
    }

    protected AbstractMetaProperty(final MetaClass metaClass, final AbstractMetaProperty parentProperty) {
        super();
        this.metaClass = metaClass;
        this.name = parentProperty.name;
        this.getter = parentProperty.getter;
        this.field = parentProperty.field;
    }

    Map annotations = new HashMap();

    @Override
    public <T extends Annotation> T getAnnotation(final Class<T> annotationClass) {
        if (annotations.keySet().contains(annotationClass)) {
            return (T) annotations.get(annotationClass);
        }
        T annotation = null;

        //field's annotation which is in JPA style
        try {
            if (this.getField() != null) {
                annotation = this.getField().getAnnotation(annotationClass);
                field = this.getField();
                field.setAccessible(true);
            }
        } catch (final IllegalArgumentException e) {
            annotation = null;
        }

        //derived method's annotation
        if (annotation == null) {
            annotation = this.getGetter().getAnnotation(annotationClass);
        }
        annotations.put(annotationClass, annotation);
        return annotation;
    }

    /**
     * get field of property in pojo may be null for derived property
     * 
     * @return
     */
    private java.lang.reflect.Field getField() {
        if (field != null) {
            return field;
        }
        final Class javaClass = metaClass.getJavaClass();
        try {

            return javaClass.getDeclaredField(name);
        } catch (final SecurityException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (final NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * set value in modelObject's property by using field directly
     * 
     * @param object
     * @param value
     */
    protected void setValue(final ModelObject object, final Object value) {
        if (isDerived()) {
            throw new IllegalArgumentException("can not set value to derived property: " + this);
        }
        //assert field != null; //only derived property has null field
        try {
            field.set(object, value);
        } catch (final IllegalArgumentException e) {
            throw new RuntimeException("Can not set " + object + "'s " + name + ". Expect: "
                + field.getType() + ", but got: " + value);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * get value from modelObject's property by using field directly if derived (no field), using getter
     * 
     * @param object
     * @return
     */

    protected Object getValue(final ModelObject object) {
        if (object == null) {
            throw new IllegalArgumentException("can not get value from: " + object);
        }
        try {
            if (!isDerived()) {
                return field.get(object);
            }
            assert field == null; //only derived property has null field

            return getGetter().invoke(object, new Object[] {});

        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (final IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (final InvocationTargetException ex) {
            Throwable cause = ex;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            if (cause instanceof org.hibernate.ObjectNotFoundException) {
                return Collections.emptySet();//TODO check this caused by one way + delete????
            }
            throw new RuntimeException(cause);
        }
    }

    /**
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    private Method getGetter() {
        if (null == getter) {
            try {
                getter =
                    metaClass.getJavaClass().getMethod(
                        "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1), new Class[] {});
            } catch (final SecurityException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (final NoSuchMethodException e) {
                throw new IllegalArgumentException(metaClass.getName() + " has no such property: " + name);
            }
        }
        return getter;
    }

    /**
     * {@inheritDoc}
     */
    public Class<?> getType() {
        if (type == null) {
            type = getGetter().getReturnType();
        }
        return type;

    }

    /**
     * @see org.pimslims.metamodel.MetaElement#getName()
     */
    public String getName() {
        return name;
    }

    public Type getGenericType() {

        return getGetter().getGenericReturnType();

    }

    /**
     * @see org.pimslims.metamodel.MetaProperty#isDerived()
     */
    public boolean isDerived() {
        // derived attribue do not have a field
        if (field != null) {
            return false;
        }
        return true;
    }

    /**
     * @see org.pimslims.metamodel.MetaElement#getAlias()
     */
    public String getAlias() {
        if (LabBookEntry.PROP_ACCESS.equals(name)) {
            return "Lab Notebook";
        }
        return name;
    }

    /**
     * @see org.pimslims.metamodel.MetaElement#getHelpText()
     */
    public abstract String getHelpText();

    /**
     * @see org.pimslims.metamodel.MetaElement#getMetaClass()
     */
    public MetaClass getMetaClass() {
        return metaClass;
    }

    /**
     * @see org.pimslims.metamodel.MetaElement#isHidden()
     */
    public boolean isHidden() {
        return false;
    }

    public abstract boolean isRequired();

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return metaClass.hashCode() * 37 + name.hashCode();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + this.getMetaClass().getName() + "'s " + this.name;
    }

    public javax.persistence.JoinTable getDBJoinTable() {
        try {
            final javax.persistence.JoinTable table = getAnnotation(javax.persistence.JoinTable.class);
            return table;
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

    public abstract void set_prop(ModelObject mo, Object value) throws ConstraintException, AccessException;
}
