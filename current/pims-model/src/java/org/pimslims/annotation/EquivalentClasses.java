/**
 * pims-model org.pimslims.annotation EquivalentClasses.java
 * 
 * @author cm65
 * @date 17 Jun 2014
 * 
 *       Protein Information Management System
 * @version: 5.1
 * 
 *           Copyright (c) 2014 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.annotation;

/**
 * EquivalentClasses
 * 
 * Provides a way to make multiple annotations. Note that this will be unnecessary in Java8.
 */
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface EquivalentClasses {

    EquivalentClass[] value();

}
