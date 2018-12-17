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
 * EquivalentProperty
 * 
 * Indicates that a PiMS data model property is equivalent to a property in another ontology.
 * 
 * Note that an owl:equivalentproperty annotation has large implications. It is usually better to express this
 * situation by an rdfs:subPropertyOf.
 * 
 */
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface EquivalentProperty {

    @Uri
    String value();

}
