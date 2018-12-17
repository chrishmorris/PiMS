/**
 * hibernate org.pimslims.annotation Attribute.java
 * 
 * @author cm65
 * @date 05-Jun-2006
 * 
 *       Protein Information Management System
 * @version: 0.5
 * 
 *           Copyright (c) 2006 cm65 This library is free software; you can redistribute it and/or modify it
 *           under the terms of the GNU Lesser General Public License as published by the Free Software
 *           Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.annotation;

/**
 * Role
 * 
 * This annotation should be supplied on the get method for the association
 * 
 */
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Role {

    /**
     * @return documentation for this association
     */
    String helpText();

    /**
     * 1 or -1. -1 means that there is no upper limit.
     * 
     * Instead could check for:
     * 
     * @javax.persistence.ManyToOne
     * @javax.persistence.ManyToMany
     * 
     * @return upper limit for number of associates
     */
    int high() default -1;

    /**
     * Usually 0 or 1. for manytoOne use instead:
     * 
     * @javax.persistence.ManyToOne(optional=false)
     * @return lower limit for number of associates
     */
    int low() default 0;

    /**
     * @return an indication of whether this association can be changed after creation time.
     */
    boolean isChangeable() default true;

    /**
     * For ordered associations, the collections returned are Lists, not Sets.
     * 
     * @return and indication of whether the order of associates is saved.
     */
    boolean isOrdered() default false;

    /**
     * @return e.g. "experiment" is this is called "getExperiment"
     */
    String baseName() default ""; // TODO remove default

    /**
     * @return the name of the reverse role, or null if this is one way.
     */
    String reverseRoleName() default ""; // TODO remove default

    /**
     * 
     * @return true if this is an abstract role
     */
    boolean isAbstract() default false;

    /**
     * 
     * @return true if this is a derived role
     */
    boolean isDerived() default false;
}
