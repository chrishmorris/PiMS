/**
 * hibernate org.pimslims.annotation Class.java
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
 * MetaClass
 * 
 */
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface MetaClass {

    /**
     * Every data model class has a "parent". This should not be confused with a superclass. LATER Do we need
     * this?
     * 
     * @return the java class of the parent
     */
    java.lang.Class parent() default java.lang.Object.class;

    java.lang.String parentRoleName() default "";

    /**
     * @return documentation for this class
     */
    String helpText();

    /**
     * @return the child classes in the model
     */
    Class[] subclasses() default {};

    /**
     * The attributes that serve as the key. LATER allow for alternative keys. This design only allows for a
     * single key, perhaps compound.
     * 
     * @return the attribute that serves as a key
     */
    String[] keyNames() default { "dbId" };

    /**
     * the attribute name will be used to order search result
     * 
     * @return
     */
    String orderBy() default ("dbId");

}
