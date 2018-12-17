/**
 * current-pims-web org.pimslims.command.newtarget PIMSTargetWriterTester.java
 * 
 * @author Petr
 * @date 29 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Petr
 * 
 * 
 */
package org.pimslims.bioinf.newtarget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.biojava.bio.BioException;
import org.pimslims.bioinf.BioInfException;
import org.pimslims.bioinf.targets.PIMSTargetWriter;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.sequence.ProteinSequence;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.Target;
import org.pimslims.properties.PropertyGetter;

/**
 * PI import junit.framework.TestCase; MSTargetWriterTester
 * 
 */
public class PIMSTargetWriterTester extends TestCase {

    private static final String UNIQUE = "ptw" + System.currentTimeMillis();

    //private static final String SCIENTIST = "scientist" + PIMSTargetWriterTester.UNIQUE;

    private static final String PROJECT = "PROJECT" + PIMSTargetWriterTester.UNIQUE;

    /**
     * The database being tested
     */
    private final AbstractModel model;

    /**
     * @param name
     */
    public PIMSTargetWriterTester(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
        PropertyGetter.setProxySetting();
    }

    /**
     * @param rw
     * @return
     * @throws AccessException
     * @throws ConstraintException
     * @throws IOException
     */
    private PIMSTarget getPIMSTarget(final WritableVersion rw, final RecordParser parser)
        throws AccessException, ConstraintException, IOException {
        final LabNotebook owner = new LabNotebook(rw, PIMSTargetWriterTester.PROJECT);

        final PIMSTarget ptarget = new PIMSTarget(parser);
        ptarget.setAccess(owner.get_Hook());
        //final Map<String, Object> params = Util.getNewMap();
        //.put(Project.PROP_COMPLETENAME, PIMSTargetWriterTester.PROJECT);
        //params.put(Project.PROP_SHORTNAME, PIMSTargetWriterTester.PROJECT);

        //final Project pr = Util.getOrCreate(rw, Project.class, params);
        //pr.setAccess(owner);
        //ptarget.setProject(pr.get_Hook());
        //params.clear();
        //params.put(Person.PROP_FAMILYNAME, PIMSTargetWriterTester.SCIENTIST);
        return ptarget;
    }

    static String read(final String filename) throws UnsupportedEncodingException, IOException {
        final URL url = PIMSTargetWriterTester.class.getResource(filename);
        final InputStream is = url.openStream();
        Assert.assertNotNull("Resource not found: " + url.toString(), is);
        final InputStreamReader reader = new InputStreamReader(is, "UTF-8");
        final BufferedReader br = new BufferedReader(reader);
        final StringBuilder builder = new StringBuilder();
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            builder.append(line);
            builder.append('\n');
        }
        return builder.toString();
    }

    public void testProteinTargetWriter() throws AccessException, ConstraintException, IOException {
        final WritableVersion rw = this.model.getTestVersion();
        try {
            final PIMSTarget ptarget =
                this.getPIMSTarget(rw, new GenBankProteinParser(
                    BioJavaGenBankProteinParserTester.genBankProtein));
            final PIMSTargetWriter pwriter = new PIMSTargetWriter(rw, ptarget);
            final Target target = pwriter.record();
            /* TODO Assert.assertEquals(PIMSTargetWriterTester.SCIENTIST, target.getCreator()
                .getFamilyName()); */
            Assert.assertEquals(0, target.getCitations().size());
            Assert.assertTrue(target.getProtein().getName().startsWith(ptarget.getTargetName()));
            Assert.assertEquals("GenbankProtein", ptarget.getDatabaseFormat());
            Assert.assertEquals(null, ptarget.getDatabaseName());
            Assert.assertEquals(PIMSTargetWriterTester.PROJECT, target.get_Owner());

            final Set<Molecule> nucleicAcids = target.getNucleicAcids();
            Assert.assertEquals(1, nucleicAcids.size());
            final Molecule dna = nucleicAcids.iterator().next();
            Assert.assertFalse("DNA seq expected", "".equals(dna.getSequence()));
        } finally {
            rw.abort();
        }
    }

    public void testNucTargetWriter() throws AccessException, ConstraintException, BioException, IOException,
        BioInfException {
        final WritableVersion rw = this.model.getTestVersion();
        try {
            final RecordParser parser = new GenBankNucleotideParser(GenBankNucleotideParserTester.record);
            final PIMSTarget ptarget = this.getPIMSTarget(rw, parser);
            final PIMSTargetWriter pwriter = new PIMSTargetWriter(rw, ptarget);
            final Target target = pwriter.record();
            //Assert.assertEquals("Oryza sativa Japonica Group", target.getSpecies().getName());
            Assert.assertNull(target.getSpecies()); // can no longer save an unknown species
            Assert.assertEquals(new ProteinSequence(GenBankNucleotideParserTester.PROTEIN_SEQUENCE),
                new ProteinSequence(target.getProtein().getSequence()));
            final Set<Molecule> nucleicAcids = target.getNucleicAcids();
            Assert.assertEquals(1, nucleicAcids.size());
            /* Molecule dna = nucleicAcids.iterator().next();
            TODO assertEquals(new DnaSequence(GenBankNucleotideParserTester.DNA_SEQUENCE), new DnaSequence(dna
                .getSeqString())); */
        } finally {
            rw.abort();
        }
    }

    public void testUniProtTargetWriter() throws AccessException, ConstraintException {
        final WritableVersion rw = this.model.getTestVersion();
        try {
            final RecordParser parser = new UniProtParser(UniProtParserTester.swissprotEntry);
            final PIMSTarget ptarget = this.getPIMSTarget(rw, parser);
            final PIMSTargetWriter pwriter = new PIMSTargetWriter(rw, ptarget);
            final Target target = pwriter.record();
            //TODO 3.2 this test failed due to "UniProtParserTester.ORGANISM" can not be found 
            Assert.assertEquals(UniProtParserTester.ORGANISM, target.getSpecies().getName());
            Assert.assertEquals("Putative 3-methyladenine DNA glycosylase (EC 3.2.2.-)",
                target.getFunctionDescription());
            Assert.assertEquals("UniProt", ptarget.getDatabaseFormat());
            // could assertEquals("MAP_1395", ptarget.getGeneName());
            Assert.assertEquals(null, ptarget.getDatabaseName());

        } catch (final IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            rw.abort();
        }
    }

    /* TODO 
    public void testGenBankMuptipleCDSNucleotideParser() {
        GenBankMuptipleCDSNucleotideParser()
    } */

    public void testOperonProtein() throws IOException, BioInfException {
        final WritableVersion rw = this.model.getTestVersion();
        try {
            final String gb = PIMSTargetWriterTester.read("CAA61933.gb");
            Assert.assertTrue(gb.startsWith("LOCUS"));
            Assert.assertTrue(gb.endsWith("//\n"));
            final PIMSTarget ptarget = new PIMSTarget(new GenBankProteinParser(gb));

            // or final RecordParser parser = DBFetch.getParser(gb, null);

            final PIMSTargetWriter pwriter = new PIMSTargetWriter(rw, ptarget);
            final Target target = pwriter.record();
            final Set<Molecule> nucleicAcids = target.getNucleicAcids();
            Assert.assertEquals(1, nucleicAcids.size());
            final Molecule dna = nucleicAcids.iterator().next();
            Assert.assertTrue("DNA seq expected", 4 < dna.getSequence().length());
            Assert.assertTrue(dna.getSequence().startsWith("ATGA"));
            System.out.println(dna.getSequence());
        } finally {
            rw.abort();
        }
    }
}
