/**
 * pims-model org.pimslims.annotation SubClassOf.java
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
 * SubClassOf
 * 
 * Indicates that a PiMS class is a subset of a type in another ontology. Used for generating export rules. If
 * the converse also holds, use EquivalentClass so import rules can also be generated.
 */
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface SubClassOf {

    @Uri
    String value();

}
