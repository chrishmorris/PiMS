/**
 * current-pims-web org.pimslims.data TargetCsvLoaderTest.java
 * 
 * @author susy
 * @date 05-Feb-2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 susy The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.biojava.bio.BioException;
import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.Target;

/**
 * TargetCsvLoaderTest
 * 
 */
public class TargetCsvLoaderTest extends TestCase {

    private static final String UNIQUE = "tcl" + System.currentTimeMillis();

    // see data/test/targets.csv
    private static final String HEADERS =
        "Name,Protein Name,Protein Sequence,DNA Sequence,Function Description,Comments,Organism,Gi Number,Project"
            + "\n";

    private static final String PROTEIN_SEQUENCE = "KKK";

    private static final String DNA_SEQUENCE = "ATGTTT";

    private static final String FUNCTION_DESCRIPTION = "Unknown";

    private static final String COMMENTS = "Because";

    private static final String ORGANISM = "org" + System.currentTimeMillis();

    private static final String GINUMBER = "" + System.currentTimeMillis();

    //private static final String PROJECT = "" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for TargetCsvLoaderTest
     * 
     * @param methodName
     */
    public TargetCsvLoaderTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    public void testNone() throws ConstraintException, IOException, BioException {
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        final InputStream in = new ByteArrayInputStream(TargetCsvLoaderTest.HEADERS.getBytes("UTF8"));
        try {
            final Collection<Target> targets = TargetCsvLoader.load(version, in);
            Assert.assertEquals(0, targets.size());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testName() throws IOException, ConstraintException, BioException {
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        final InputStream in =
            new ByteArrayInputStream((TargetCsvLoaderTest.HEADERS + TargetCsvLoaderTest.UNIQUE + ",,,,,,,")
                .getBytes("UTF8"));
        try {
            final Collection<Target> targets = TargetCsvLoader.load(version, in);
            Assert.assertEquals(1, targets.size());
            final Target target = targets.iterator().next();
            Assert.assertEquals(TargetCsvLoaderTest.UNIQUE, target.getName());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testProteinName() throws IOException, ConstraintException, BioException {
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        final InputStream in =
            new ByteArrayInputStream((TargetCsvLoaderTest.HEADERS + TargetCsvLoaderTest.UNIQUE + "t,"
                + TargetCsvLoaderTest.UNIQUE + "p," + TargetCsvLoaderTest.PROTEIN_SEQUENCE + ",,,,,")
                .getBytes("UTF8"));
        try {
            final Collection<Target> targets = TargetCsvLoader.load(version, in);
            Assert.assertEquals(1, targets.size());
            final Target target = targets.iterator().next();
            Assert.assertEquals(TargetCsvLoaderTest.UNIQUE + "t", target.getName());
            Assert.assertEquals(TargetCsvLoaderTest.UNIQUE + "p", target.getProtein().getName());
            Assert.assertEquals(TargetCsvLoaderTest.PROTEIN_SEQUENCE, target.getProtein().getSeqString());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testDna() throws IOException, ConstraintException, BioException {
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        final InputStream in =
            new ByteArrayInputStream((TargetCsvLoaderTest.HEADERS + TargetCsvLoaderTest.UNIQUE + "t,"
                + TargetCsvLoaderTest.UNIQUE + "p," + TargetCsvLoaderTest.PROTEIN_SEQUENCE + ","
                + TargetCsvLoaderTest.DNA_SEQUENCE + ",,,,").getBytes("UTF8"));
        try {
            final Collection<Target> targets = TargetCsvLoader.load(version, in);
            Assert.assertEquals(1, targets.size());
            final Target target = targets.iterator().next();
            Assert.assertEquals(1, target.getNucleicAcids().size());
            final Molecule dna = target.getNucleicAcids().iterator().next();
            Assert.assertEquals(TargetCsvLoaderTest.DNA_SEQUENCE, dna.getSeqString());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testFunction() throws IOException, ConstraintException, BioException {
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        final InputStream in =
            new ByteArrayInputStream((TargetCsvLoaderTest.HEADERS + TargetCsvLoaderTest.UNIQUE + "t,"
                + TargetCsvLoaderTest.UNIQUE + "p," + TargetCsvLoaderTest.PROTEIN_SEQUENCE + ","
                + TargetCsvLoaderTest.DNA_SEQUENCE + "," + TargetCsvLoaderTest.FUNCTION_DESCRIPTION + ",,,")
                .getBytes("UTF8"));
        try {
            final Collection<Target> targets = TargetCsvLoader.load(version, in);
            Assert.assertEquals(1, targets.size());
            final Target target = targets.iterator().next();
            Assert.assertEquals(TargetCsvLoaderTest.FUNCTION_DESCRIPTION, target.getFunctionDescription());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testComments() throws IOException, ConstraintException, BioException {
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        final InputStream in =
            new ByteArrayInputStream((TargetCsvLoaderTest.HEADERS + TargetCsvLoaderTest.UNIQUE + "t,"
                + TargetCsvLoaderTest.UNIQUE + "p," + TargetCsvLoaderTest.PROTEIN_SEQUENCE + ","
                + TargetCsvLoaderTest.DNA_SEQUENCE + "," + TargetCsvLoaderTest.FUNCTION_DESCRIPTION + ","
                + TargetCsvLoaderTest.COMMENTS + ",,,").getBytes("UTF8"));
        try {
            final Collection<Target> targets = TargetCsvLoader.load(version, in);
            Assert.assertEquals(1, targets.size());
            final Target target = targets.iterator().next();
            Assert.assertEquals(TargetCsvLoaderTest.COMMENTS, target.getWhyChosen());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testOrganism() throws IOException, ConstraintException, BioException {
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        final InputStream in =
            new ByteArrayInputStream((TargetCsvLoaderTest.HEADERS + TargetCsvLoaderTest.UNIQUE + "t,"
                + TargetCsvLoaderTest.UNIQUE + "p," + TargetCsvLoaderTest.PROTEIN_SEQUENCE + ","
                + TargetCsvLoaderTest.DNA_SEQUENCE + "," + TargetCsvLoaderTest.FUNCTION_DESCRIPTION + ","
                + TargetCsvLoaderTest.COMMENTS + "," + TargetCsvLoaderTest.ORGANISM + ",").getBytes("UTF8"));
        try {
            final Collection<Target> targets = TargetCsvLoader.load(version, in);
            Assert.assertEquals(1, targets.size());
            final Target target = targets.iterator().next();
            Assert.assertEquals(TargetCsvLoaderTest.ORGANISM, target.getSpecies().getName());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testDbLinks() throws IOException, ConstraintException, BioException {
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        final InputStream in =
            new ByteArrayInputStream(
                (TargetCsvLoaderTest.HEADERS + TargetCsvLoaderTest.UNIQUE + "t," + TargetCsvLoaderTest.UNIQUE
                    + "p," + TargetCsvLoaderTest.PROTEIN_SEQUENCE + "," + TargetCsvLoaderTest.DNA_SEQUENCE
                    + "," + TargetCsvLoaderTest.FUNCTION_DESCRIPTION + "," + TargetCsvLoaderTest.COMMENTS
                    + "," + TargetCsvLoaderTest.ORGANISM + "," + TargetCsvLoaderTest.GINUMBER)
                    .getBytes("UTF8"));
        try {
            final Collection<Target> targets = TargetCsvLoader.load(version, in);
            Assert.assertEquals(1, targets.size());
            final Target target = targets.iterator().next();

            final Iterator<ExternalDbLink> iterator = target.getExternalDbLinks().iterator();
            final ExternalDbLink edbl1 = iterator.next();

            Assert.assertEquals(TargetCsvLoaderTest.GINUMBER, edbl1.getCode());
        } finally {
            version.abort(); // not testing persistence
        }
    }
/*
    public void testProject() throws IOException, ConstraintException, BioException {
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        final InputStream in =
            new ByteArrayInputStream(
                (TargetCsvLoaderTest.HEADERS + TargetCsvLoaderTest.UNIQUE + "t," + TargetCsvLoaderTest.UNIQUE
                    + "p," + TargetCsvLoaderTest.PROTEIN_SEQUENCE + "," + TargetCsvLoaderTest.DNA_SEQUENCE
                    + "," + TargetCsvLoaderTest.FUNCTION_DESCRIPTION + "," + TargetCsvLoaderTest.COMMENTS
                    + "," + TargetCsvLoaderTest.ORGANISM + "," + TargetCsvLoaderTest.GINUMBER + "," + TargetCsvLoaderTest.PROJECT)
                    .getBytes("UTF8"));
        try {
            final Collection<Target> targets = TargetCsvLoader.load(version, in);
            Assert.assertEquals(1, targets.size());
            final Target target = targets.iterator().next();

            Assert.assertEquals(TargetCsvLoaderTest.PROJECT, target.getProjects());
        } finally {
            version.abort(); // not testing persistence
        }
    }
*/
}
