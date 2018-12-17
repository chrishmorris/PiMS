/**
 * current-pims-web org.pimslims.bioinf.local SequenceGetterTester.java
 * 
 * @author Petr
 * @date 28 Sep 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Petr
 * 
 * 
 */
package org.pimslims.bioinf.local;

import java.util.Collection;

import junit.framework.Assert;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Molecule;

/**
 * SequenceGetterTester
 * 
 */
public class SequenceGetterTester extends junit.framework.TestCase {
    private static final String UNIQUE = "sg" + System.currentTimeMillis();

    /**
     * The database being tested
     */
    private final AbstractModel model;

    /**
     * The transaction to use for testing
     */
    private WritableVersion wv = null;

    //private Molecule protein = null;

    private final String protSeq = "DGDGLA";

    //private Molecule dna = null;

    private final String dnaSeq = "TTTCCC";

    /**
     * Constructor for TestPlasmid.
     * 
     * @param methodName
     */
    public SequenceGetterTester(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

/*
    void recordProtein() throws AccessException, ConstraintException {

        protein = Creator.recordProtein(this.wv, "protein" + StuffProducer.getlong(), this.protSeq);

    }

    void recordDNA() throws AccessException, ConstraintException {
        dna = Creator.recordDNA(this.wv, "dna" + StuffProducer.getlong(), this.dnaSeq);

    } */

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws AccessException, ConstraintException {
        this.wv = this.model.getTestVersion();
        Assert.assertNotNull(this.wv);
        //this.recordProtein();
        //this.recordDNA();
    }

    /*
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        Assert.assertFalse(this.wv.isCompleted());
        if (this.wv.isCompleted()) {
            // can't tidy up
            return;
        }
        if (null != this.wv) {
            this.wv.abort(); // not testing persistence
        }
    }

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(SequenceGetterTester.class);
    }

    /**
     * 
     */
    public void testGetProteins() throws ConstraintException, AccessException {
        final Molecule protein = new Molecule(this.wv, "protein", SequenceGetterTester.UNIQUE);
        final Collection<Molecule> proteins = SequenceGetter.getProteins(this.wv);
        Assert.assertNotNull(proteins);
        Assert.assertTrue(proteins.contains(protein));
        protein.delete();
    }

    public void testGetDNA() throws AccessException, ConstraintException {
        final Molecule dna = new Molecule(this.wv, "DNA", SequenceGetterTester.UNIQUE);
        final Collection<Molecule> dnas = SequenceGetter.getDNAs(this.wv);
        Assert.assertNotNull(dnas);
        Assert.assertTrue(dnas.contains(dna));
        dna.delete();
    }

    public void testIsDNA() {
        Assert.assertTrue(SequenceGetter.isDNA(this.dnaSeq));
        Assert.assertFalse(SequenceGetter.isDNA(this.protSeq));
    }

}
