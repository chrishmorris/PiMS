/**
 * pims-model org.pimslims.metamodel RulesTest.java
 * 
 * @author cm65
 * @date 17 Jun 2014
 * 
 *       Protein Information Management System
 * @version: 5.1
 * 
 *           Copyright (c) 2014 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.metamodel;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.model.core.JournalCitation;
import org.pimslims.model.experiment.Experiment;

/**
 * RulesTest
 * 
 */
public class RulesTest extends TestCase {

    private final AbstractModel model;

    public RulesTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testAnnotation() throws NoSuchFieldException, SecurityException {
        final org.pimslims.annotation.EquivalentClass annotation =
            JournalCitation.class.getAnnotation(org.pimslims.annotation.EquivalentClass.class);
        assertNotNull(annotation);
        assertNotNull(Experiment.class.getDeclaredField("protocol").getAnnotation(
            org.pimslims.annotation.SubPropertyOf.class));
    }

    public void testExportEquivalentClass() {
        String rules = this.model.getExportRules();
        assertTrue(
            rules,
            rules
                .contains("pimsSchema:JournalCitation rdfs:subClassOf http://purl.uniprot.org/core/Journal_Citation."));
        assertTrue(rules,
            rules.contains("pimsSchema:Experiment rdfs:subClassOf http://www.w3.org/ns/prov#Activity."));
    }

    public void testExportSubClass() {
        String rules = this.model.getExportRules();
        assertTrue(rules,
            rules.contains("pimsSchema:Protocol rdfs:subClassOf http://purl.obolibrary.org/obo/OBI_0000260."));
        assertTrue(rules,
            rules.contains("pimsSchema:Sample rdfs:subClassOf http://purl.org/dc/dcmitype/PhysicalObject."));
    }

    public void testMultipleEquivalentClass() {
        String rules = this.model.getExportRules();
        assertTrue(rules,
            rules.contains("pimsSchema:Protocol rdfs:subClassOf http://purl.uniprot.org/core/Method."));
        assertTrue(rules,
            rules.contains("pimsSchema:Protocol rdfs:subClassOf http://www.w3.org/ns/prov#Plan."));
    }

    public void testExportProperty() {
        String rules = this.model.getExportRules();

        // equivalent property
        assertTrue(rules,
            rules.contains("pimsSchema:title rdfs:subPropertyOf http://purl.uniprot.org/core/title."));
        assertTrue(rules,
            rules.contains("pimsSchema:sameAs rdfs:subPropertyOf http://www.w3.org/2002/07/owl#sameAs."));

        // sub property
        assertTrue(rules, rules.contains("pimsSchema:protocol rdfs:subPropertyOf "
            + "http://www.w3.org/ns/prov#wasInfluencedBy."));
    }

    public void testExportMultipleProperties() {
        String rules = this.model.getExportRules();

        assertTrue(rules,
            rules.contains("pimsSchema:creator rdfs:subPropertyOf http://purl.org/dc/terms/creator."));

        assertTrue(rules, rules.contains("pimsSchema:creator rdfs:subPropertyOf "
            + "http://www.w3.org/ns/prov#wasAttributedTo."));
    }

    public void testImport() {
        String rules = this.model.getImportRules();
        assertTrue(
            rules,
            rules
                .contains("http://purl.uniprot.org/core/Journal_Citation rdfs:subClassOf pimsSchema:JournalCitation."));
        assertTrue(rules,
            rules.contains("http://purl.uniprot.org/core/title rdfs:subPropertyOf pimsSchema:title."));
    }

    public void couldtestPrint() {
        String rules = this.model.getExportRules();
        System.out.println(rules);
    }

}
