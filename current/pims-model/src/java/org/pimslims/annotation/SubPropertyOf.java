/**
 * pims-model org.pimslims.annotation equivalentClass.java
 * 
 * @author cm65
 * @date 16 Jun 2014
 * 
 *       Protein Information Management System
 * @version: 5.1
 * 
 *           Copyright (c) 2014 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.annotation;

/**
 * SubPropertyOf
 * 
 * Indicates that a PiMS data model property implies a property in another ontology. This information is used
 * when exporting data from PiMS.
 * 
 * If the converse also holds, then use EquivalentProperty, so import will also be supported.
 * 
 */
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface SubPropertyOf {

    @Uri
    String value();

}
