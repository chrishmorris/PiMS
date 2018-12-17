/**
 * V3_2-web org.pimslims.bioinf.targets TargetFromFastaTest.java
 * 
 * @author cm65
 * @date 12 Jun 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.bioinf.targets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.bioinf.newtarget.PIMSTarget;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.Target;

/**
 * TargetFromFastaTest
 * 
 */
public class TargetFromFastaTest extends TestCase {

    private static final String UNIQUE = "tff" + System.currentTimeMillis();

    /**
     * NAME_AND_DESCRIPTION String
     */
    private static final String NAME_AND_DESCRIPTION = ">" + TargetFromFastaTest.UNIQUE + " a  b\r\n";

    private final AbstractModel model;

    /**
     * Constructor for TargetFromFastaTest
     * 
     * @param name
     */
    public TargetFromFastaTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testCreateNone() throws ConstraintException, IOException, AccessException {
        final InputStream in = new ByteArrayInputStream("".getBytes("UTF8"));
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Collection<Target> targets =
                new TargetFromFasta(version, null).save(in, TargetFromFasta.PROTEIN);
            Assert.assertEquals(0, targets.size());
        } finally {
            version.abort();
        }
    }

    /**
     * FIRST_SEQUENCE String
     */
    private static final String FIRST_SEQUENCE = "GTGATCGGGCGTGCGGAAATGCTTGATGCGGCAGCGCCAGGGGAGCTGGG\r\n"
        + "CGGCACGTATGCCGGCAGCCCGCTCGGCTGCGCGGCGGCTTTGGCAGTCT\r\n"
        + "TGGATATTATCGAAGAAGAAGGACTGAATGAGCGATCTGAAGAAATTGGC\r\n"
        + "AAAATCATTGAAGACAAGGCGTATGAGTGGAAACAAGAATTCCCGTTCAT\r\n"
        + "CGGTGACATCCGCAGACTCGGGGCGATGGCCGCAATCGAAATCGTCAAGG\r\n"
        + "ATCCTGACACGCGTGAGCCTGATAAGACAAAAGCAGCAGCGATCGCGGCC\r\n"
        + "TATGCGAATCAAAACGGATTACTTTTGCTGACAGCGGGAATTAACGGTAA\r\n"
        + "TATCATCCGCTTTTTGACACCGCTCGTCATCTCAGACAGCCTATTAAATG\r\n"
        + "AAGGGCTCAGCATCTTGGAGGCGGGCCTGCGAGCTTAATCATTGGAAAGA\r\n"
        + "AAATGGCCGGGTCTGCTATCATTTTAAGTAATCATGAAA\r\n";

    private static final String TWO_SEQUENCES = TargetFromFastaTest.NAME_AND_DESCRIPTION
        + TargetFromFastaTest.FIRST_SEQUENCE + ">" + TargetFromFastaTest.UNIQUE + "two\r\n"
        + "GACAGATCATCTGTATTTAGTGGATACTGAAATTTCCTGCATCGAAGATA\r\n"
        + "TTGCCCATACAGAACGTCCGTTTATCCAGTTTAAAAGTGACAGCACTTAT\r\n"
        + "TTTCAGGAAATTCAGCACTGGTGGCATCAAAAATTTAAAACGTCGCCGAA\r\n"
        + "ACAGACGATATTGGTTGATCAGATTGAAACGTGCAAACAGATGGCGCTGC\r\n"
        + "ACGGAATCGGTTATGCCATTTTGCCGTCTGTTACCCTTCAAAATGAAGAT\r\n"
        + "AAAGTGAATAAAATGCCTCTTTTAGACATGAAAGGGCATCCGATCGGTCG\r\n"
        + "GGATACATGGTTATTAGGTTATGAGCCTGCCTTTGAACTGAAACAAGTTC\r\n"
        + "AAGCTTTTGTACAAGTGATAAAGGATATGCTGGATCAGGAAAATCCATTT\r\n"
        + "TAAAGACAGCGAGGTGCTGTCTTTTTTTTATTTATCTGTTGACAATGAAA\r\n"
        + "ATCATTATCATTTAAAGTGATACATATGATATTGAAAATCATTATCA";

    public void testProject() throws ConstraintException, IOException, AccessException {
        final InputStream in =
            new ByteArrayInputStream(
                (TargetFromFastaTest.NAME_AND_DESCRIPTION + TargetFromFastaTest.FIRST_SEQUENCE)
                    .getBytes("UTF8"));
        final WritableVersion version = this.model.getTestVersion();
        try {
            final LabNotebook owner = new LabNotebook(version, TargetFromFastaTest.UNIQUE);
            //final Project project = new Project(version, owner.getName(), tff);
            final Collection<Target> targets =
                new TargetFromFasta(version, owner.getName()).save(in, TargetFromFasta.PROTEIN);
            Assert.assertEquals(1, targets.size());
            final Target first = targets.iterator().next();
            Assert.assertEquals("A01_" + TargetFromFastaTest.UNIQUE, first.getName());
            Assert.assertEquals(owner, first.getAccess());
            //Assert.assertEquals(project, first.getProjects().iterator().next());

        } finally {
            version.abort();
        }
    }

    public void testCreateTwo() throws ConstraintException, IOException, AccessException {
        final InputStream in = new ByteArrayInputStream(TargetFromFastaTest.TWO_SEQUENCES.getBytes("UTF8"));
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Collection<Target> targets =
                new TargetFromFasta(version, null).save(in, TargetFromFasta.PROTEIN);
            Assert.assertEquals(2, targets.size());
            final Iterator<Target> iterator = targets.iterator();
            final Target first = iterator.next();
            Assert.assertEquals("A01_" + TargetFromFastaTest.UNIQUE, first.getName());
            Assert.assertEquals(new DnaSequence(TargetFromFastaTest.FIRST_SEQUENCE).getSequence(), first
                .getNucleicAcids().iterator().next().getSequence());
            Assert.assertEquals("a  b", first.getDetails());
            Assert.assertFalse(PIMSTarget.isDNATarget(first));
            final Target second = iterator.next();
            Assert.assertEquals("B01_" + TargetFromFastaTest.UNIQUE + "two", second.getName());

        } finally {
            version.abort();
        }
    }

    public void testCreateDnaTarget() throws ConstraintException, IOException, AccessException {
        final InputStream in =
            new ByteArrayInputStream(
                (TargetFromFastaTest.NAME_AND_DESCRIPTION + TargetFromFastaTest.FIRST_SEQUENCE)
                    .getBytes("UTF8"));
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Collection<Target> targets =
                new TargetFromFasta(version, null).save(in, TargetFromFasta.DNA);
            Assert.assertEquals(1, targets.size());
            final Target first = targets.iterator().next();
            Assert.assertEquals("A01_" + TargetFromFastaTest.UNIQUE, first.getName());
            Assert.assertEquals(0, first.getNucleicAcids().size());
            final Molecule molecule = first.getProtein();
            Assert.assertEquals(new DnaSequence(TargetFromFastaTest.FIRST_SEQUENCE).getSequence(),
                molecule.getSequence());
            Assert.assertEquals("DNA", molecule.getMolType());
            Assert.assertTrue(PIMSTarget.isDNATarget(first));

        } finally {
            version.abort();
        }
    }
}
