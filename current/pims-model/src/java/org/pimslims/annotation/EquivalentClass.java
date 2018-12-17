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
 * equivalentClass
 * 
 * Indicates that a PiMS data model class is equivalent to a type in another ontology.
 * 
 * Note that an owl:equivalentClass annotations has large implication. It is usually better to express this
 * situation by an rdfs:subClassOf.
 * 
 */
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface EquivalentClass {

    @Uri
    String value();

}
