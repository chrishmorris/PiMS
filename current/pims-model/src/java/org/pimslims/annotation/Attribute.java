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
 * Attribute.
 * 
 * This annotation should be supplied on the get method for the attribute
 * 
 */
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Attribute {

    /**
     * @return documentation for this attribute
     */
    String helpText();

    /**
     * @return the default value for this attribute
     */
    // could Object defaultValue()

    /**
     * 
     */
    String datatype() default "";

    /**
     * e.g. {"contains_no_whitespace", "unique", "not_null"}
     * 
     * @return the names of the constraints for this attribute, including the constraints for the datatype
     */
    String[] constraints() default {};

    /**
     * @return an indication of whether this attribute can be changed after creation time.
     */
    boolean isChangeable() default true;

    /**
     * Derived attributes cannot be set.
     * 
     * @return true if this attribute is calculated
     */
    boolean isDerived() default false;
}
