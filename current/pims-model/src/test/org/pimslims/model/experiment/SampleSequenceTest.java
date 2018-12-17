/**
 * pims-model org.pimslims.model.experiment SampleSequenceTest.java
 * 
 * @author cm65
 * @date 1 Aug 2013
 * 
 *       Protein Information Management System
 * @version: 5.1
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.model.experiment;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;

/**
 * SampleSequenceTest
 * 
 */
public class SampleSequenceTest extends TestCase {

    private static final String UNIQUE = "ss" + System.currentTimeMillis();

    // note that this could be DNA or protein
    private static final String SEQUENCE = "CCCCCCCCCCCCCCCCCCCCCCCCC";

    private final AbstractModel model;

    /**
     * Constructor for SampleSequenceTest
     * 
     * @param name
     */
    public SampleSequenceTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testNoSequence() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            assertNull(new Sample(version, UNIQUE).getSequence());
        } finally {
            version.abort();
        }
    }

    public void testGetProteinSequence() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Sample sample = new Sample(version, UNIQUE);
            sample.addSampleCategory(new SampleCategory(version, " Protein " + UNIQUE));
            Molecule molecule = new Molecule(version, "protein", UNIQUE);
            molecule.setSequence(SEQUENCE);
            new SampleComponent(version, molecule, sample);
            assertEquals(SEQUENCE, sample.getSequence());

            // not try changing it
            sample.setSequence(SEQUENCE + "A");
            assertEquals(SEQUENCE + "A", sample.getSequence());
        } finally {
            version.abort();
        }
    }

    public void testSetProteinSequence() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            Sample sample = new Sample(version, UNIQUE);
            sample.addSampleCategory(new SampleCategory(version, " Protein " + UNIQUE));
            sample.setSequence(SEQUENCE + "A");
            assertEquals(SEQUENCE + "A", sample.getSequence());
        } finally {
            version.abort();
        }
    }

}
