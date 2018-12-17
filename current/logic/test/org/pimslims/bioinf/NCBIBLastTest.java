package org.pimslims.bioinf;

import java.io.IOException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.ModelImpl;
import org.pimslims.properties.PropertyGetter;

import uk.ac.ebi.webservices.InputParams;

public class NCBIBLastTest extends TestCase {

    /**
     * SEQUENCE String
     */
    private static final String SEQUENCE =
        ">gi|113636|sp|P03973|SLPI_HUMAN Antileukoproteinase precursor (ALP) (Secretory leukocyte protease inhibitor) (HUSI-1) (Seminal proteinase inhibitor) (BLPI) (Mucus proteinase inhibitor) (MPI) (WAP four-disulfide core domain protein 4) (Protease inhibitor WAP4)\r\n"
            + "MKSSGLFPFLVLLALGTLAPWAVEGSGKSFKAGVCPPKKSAQCLRYKKPECQSDWQCPGKKRCCPDTCGI\r\n"
            + "KCLDPVDTPNPTRRKPGKCPVTYGQCLMLNPPNFCEMDGQCKRDLKCCMGMCGKSCVSPVKA";

    private static final String EMAIL = "pims-defects@dl.ac.uk";

    private static final String PROGRAM = "blastp";

    private static final String PDB = "uniprotkb_pdb";

    private static final String NOFILTER = "no"; // was  "none";

    private static final String SETCONTENT = "QWERTY";

    private static final String STARTSTRING = "<?xml version=";

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(NCBIBLastTest.class);
    }

    /**
     * 
     */
    public NCBIBLastTest(final String methodName) {
        super(methodName);

        ModelImpl.getModel();
        PropertyGetter.setProxySetting();

    }

    /*
     * Test method for 'org.pimslims.servlet.Blast.NCBIBlast.NCBIBlast()'
     */
    public void testNCBIBlast() {
        try {
            final NCBIBlast ncbilast = new NCBIBlast();
            Assert.assertNotNull(ncbilast);
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }

    }

    /*
     * Test method for 'org.pimslims.servlet.Blast.NCBIBlast.getBlastRecord(String[], String[])'
     */
    public void testGetBlastRecord() {
        System.gc();
        System.gc();
        final InputParams params = new InputParams();
        params.setProgram(NCBIBLastTest.PROGRAM);
        params.setDatabase(NCBIBLastTest.PDB);
        // params.setDatabase(InputParams.TARGETDB); //to blast against Target db
        params.setEmail(NCBIBLastTest.EMAIL);
        params.setNumal(InputParams.MIN_HITS); // to limit number of alignments retrieved
        params.setScores(InputParams.MIN_HITS); // to limit number of hits retrieved
        params.setExp("1.0f"); // to set E value to 1.0 default is 10
        params.setFilter(NCBIBLastTest.NOFILTER);

        String result = null;
        try {
            final NCBIBlast service = new NCBIBlast();
            result = service.getBlastResults(params, NCBIBLastTest.SETCONTENT);
            Assert.assertTrue(result, result.startsWith("BLASTP"));
            Assert.assertTrue(result.contains("No hits found"));
            //System.out.println(result);

        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertNotNull(result);

    }

    /*
     * Test method for 'org.pimslims.servlet.Blast.NCBIBlast.getBlastResults(String[], String[])' To determine
     * how to set output as xml
     */
    public void testGetBlastResults() {
        final InputParams params2 = new InputParams();
        params2.setProgram(NCBIBLastTest.PROGRAM);
        params2.setDatabase(NCBIBLastTest.PDB);
        params2.setEmail(NCBIBLastTest.EMAIL);
        // params.setOutformat("toolxml");

        // input.setSequence("uniprot:P03973");

        String result = null;
        String results = null;
        try {
            final NCBIBlast service = new NCBIBlast();
            result = service.getBlastRecord(params2, NCBIBLastTest.SETCONTENT);
            // assertTrue(result.startsWith(STARTSTRING));
            // System.out.println("......The possible output types are.......");
            results = service.getBlastRecord(params2, NCBIBLastTest.SETCONTENT);

        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertNotNull(result);
        Assert.assertNotNull(results);

    }

    /*
     * Test method for 'org.pimslims.servlet.Blast.NCBIBlast.getBlastResults(String[], String[])' To test xml
     * output xml, also input as FASTA sequence string or raw sequence
     */
    public void testGetBlastXml() {
        final InputParams params3 = new InputParams();
        params3.setProgram(NCBIBLastTest.PROGRAM);
        params3.setDatabase(NCBIBLastTest.PDB);
        // params.setDatabase(InputParams.TARGETDB); //to blast against Target db
        params3.setEmail(NCBIBLastTest.EMAIL);
        params3.setNumal(InputParams.MIN_HITS); // to limit number of alignments retrieved
        params3.setScores(InputParams.MIN_HITS); // to limit number of hits retrieved

        params3.setFilter(NCBIBLastTest.NOFILTER); // low complexity filter off, default is
        // "seg"
        // String seq=">gi|113636|sp|P03973|SLPI_HUMAN Antileukoproteinase
        // precursor (ALP) (Secretory leukocyte protease inhibitor) (HUSI-1)
        // (Seminal proteinase inhibitor) (BLPI) (Mucus proteinase inhibitor)
        // (MPI) (WAP four-disulfide core domain protein 4) (Protease inhibitor
        // WAP4)\r\n" +
        // "MKSSGLFPFLVLLALGTLAPWAVEGSGKSFKAGVCPPKKSAQCLRYKKPECQSDWQCPGKKRCCPDTCGI\r\n"
        // +
        // "KCLDPVDTPNPTRRKPGKCPVTYGQCLMLNPPNFCEMDGQCKRDLKCCMGMCGKSCVSPVKA";
        // input.setContent(seq);

        String result = null;
        try {
            final NCBIBlast service = new NCBIBlast();
            result = service.getBlastXml(params3, NCBIBLastTest.SETCONTENT);
            Assert.assertTrue(result.startsWith(NCBIBLastTest.STARTSTRING));
            Assert.assertTrue(result, result.contains("<sequences total=\"1\">"));
            // System.out.println("......The xml for PDB Blast is ....");
            //System.out.println(result);

        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertNotNull(result);
        // assertNotNull(results);

    }

    /*
     * Test method for 'org.pimslims.servlet.Blast.NCBIBlast.getBlastResults(String[], String[])' To test xml
     * output xml, also input as FASTA sequence string or raw sequence
     */
    public void testGetBlastXmlfromSGT() {
        final InputParams params4 = new InputParams();
        params4.setProgram(NCBIBLastTest.PROGRAM);
        params4.setDatabase(InputParams.TARGETDB); // to blast against Target db
        params4.setEmail(NCBIBLastTest.EMAIL);
        params4.setNumal(InputParams.MIN_HITS); // to limit number of alignments retrieved
        params4.setScores(InputParams.MIN_HITS); // to limit number of hits retrieved

        // params.setFilter(NOFILTER); //low complexity filter off, default is
        // "seg"

        final String seq = "QWERTY"; // TODO            SEQUENCE;

        String result = null;
        try {
            final NCBIBlast service = new NCBIBlast();
            result = service.getBlastXml(params4, seq);
            Assert.assertTrue(result.startsWith(NCBIBLastTest.STARTSTRING));
            Assert.assertTrue(result, result.contains("<sequences total=\"1\">")); // note this may change
            // System.out.println("......The xml for SGT Blast is ....");
            //System.out.println(result);

        } catch (final IOException ex) {
            Assert.fail(ex.getMessage());
        }
        Assert.assertNotNull(result);
    }

    //TODO awaiting answer from EBI
    public void testGetBlastRequestfromSGT() {

        final String seq =
            "MTHVSRRKFLFTTGAAAAASILVHGCTSNGSQSATTGEQAPSAAPAANVSAANAPKVETTKAKLGFIPLTDAAPLIIAKEKGFFAKYGMTDIEVIKQKSWPVTRDNLKIGSSGGGIDGAHILSPMPYLMTINDKVPMYILARLNTNGQAISVAEKFKELNVNLESKSLKDAAIKAKADKKALKMGITFPGGTHDLWMRYWLAAGGINPDQDVVLEAVPPPQMVANMKVNTVDGFCVGEPWNAQLVNQKIGYSALVTGELWKDHPEKAFSMRQDWIEQNPNAAQAILMAILEAQQWCDKAENKEEMCKICSDRKYFNVAAADIIERAKGNIDYGDGRKEQNFAHRMKFWADNASYPYKSHDIWFLTEDIRWGYLPKDTKVQDIVNQVNKEDLWKKAAKAIGVADAEIPASSSRGVETFFDGVKFDPEKPEEYLNSLKIKKV"; // TODO            SEQUENCE;

        String result = null;
        try {
            result = NCBIBlast.getBlastResult(seq, "Run TargetDB Blast");
            Assert.assertTrue(result.startsWith(NCBIBLastTest.STARTSTRING));
            Assert.assertTrue(result, result.contains("<sequences total=\"1\">")); // note this may change
            // System.out.println("......The xml for SGT Blast is ....");
            //System.out.println(result);

        } catch (final IOException ex) {
            Assert.fail(ex.getMessage());
        }
        Assert.assertNotNull(result);
    }

    public void testGetBlastXmlfromEMBL() {
        final InputParams params4 = new InputParams();
        params4.setProgram("blastn");
        params4.setDatabase(InputParams.EMBL);
        params4.setEmail(NCBIBLastTest.EMAIL);
        params4.setNumal(InputParams.MIN_HITS); // to limit number of alignments retrieved
        params4.setScores(InputParams.MIN_HITS); // to limit number of hits retrieved

        params4.setSequenceType("dna");
        // params.setFilter(NOFILTER); //low complexity filter off, default is
        // "seg"

        //was input4.setType(NCBIBLastTest.SETTYPE);
        final String seq = "ATGAAGTCCAGCGGCCTCTTCCCCTTCC"; // COULD be as below;
        /* final String seq =
             ">gi|113636|sp|P03973|SLPI_HUMAN Antileukoproteinase precursor (ALP) (Secretory leukocyte protease inhibitor) (HUSI-1) (Seminal proteinase inhibitor) (BLPI) (Mucus proteinase inhibitor) (MPI) (WAP four-disulfide core domain protein 4) (Protease inhibitor WAP4)\r\n"
                 + "ATGAAGTCCAGCGGCCTCTTCCCCTTCCTGGTGCTGCTTGCCCTGGGAACTCTGGCACCTTGGGCTGTGG\r\n"
                 + "AAGGCTCTGGAAAGTCCTTCAAAGCTGGAGTCTGTCCTCCTAAGAAATCTGCCCAGTGCCTTAGATACAA\r\n"
                 + "GAAACCTGAGTGCCAGAGTGACTGGCAGTGTCCAGGGAAGAAGAGATGTTGTCCTGACACTTGTGGCATC\r\n"
                 + "AAATGCCTGGATCCTGTTGACACCCCAAACCCAACAAGGAGGAAGCCTGGGAAGTGCCCAGTGACTTATG\r\n"
                 + "GCCAATGTTTGATGCTTAACCCCCCCAATTTCTGTGAGATGGATGGCCAGTGCAAGCGTGACTTGAAGTG\r\n"
                 + "TTGCATGGGCATGTGTGGGAAATCCTGCGTTTCCCCTGTGAAAGCTTGA"; */
        String result = null;
        try {
            final NCBIBlast service = new NCBIBlast();
            result = service.getBlastXml(params4, seq);
            Assert.assertTrue(result.startsWith(NCBIBLastTest.STARTSTRING));
            Assert.assertTrue(result, result.contains("<sequences total=\"1\">")); // note this may change
            // System.out.println("......The xml for SGT Blast is ....");
            //System.out.println(result);

        } catch (final IOException ex) {
            Assert.fail(ex.getMessage());
        }
        Assert.assertNotNull(result);

    }

    /*
     * Test method for 'org.pimslims.servlet.Blast.NCBIBlast.getBlastResults(String[], String[])' To
     * asynchronous job
     */
    public void testGetBlastXmlasynch() throws InterruptedException {
        final InputParams params5 = new InputParams();
        params5.setProgram(NCBIBLastTest.PROGRAM);
        params5.setDatabase(NCBIBLastTest.PDB);
        params5.setEmail(NCBIBLastTest.EMAIL);
        params5.setNumal(InputParams.MIN_HITS); // to limit number of alignments retrieved
        params5.setScores(InputParams.MIN_HITS); // to limit number of hits retrieved

        params5.setAsync(new Boolean(true)); // set the sumissions
        // asynchronous
        // params.setFilter(NOFILTER); //low complexity filter off, default is
        // "seg"
        final String seq = NCBIBLastTest.SEQUENCE;

        String result = null;
        try {
            final NCBIBlast service = new NCBIBlast();
            result = service.getBlastXmlAsync(params5, seq);
            if (result.startsWith(NCBIBLastTest.STARTSTRING)) {
                Assert.assertTrue(result, result.contains("<sequences total=\"1\">")); // note that this number could change
            } else {
                Assert.assertTrue(result.contains("PENDING")); //Susy 200510 couldn't handle this in test case before
            }
            //Assert.assertTrue(result.startsWith(NCBIBLastTest.STARTSTRING) || result.equals(""));
            //System.out.println("......The xml for Asynchronous Blast is ....");
            //System.out.println(result);
            //Assert.assertTrue(result, result.contains("<sequences total=\"5\">")); // note that this number could change

        } catch (final IOException ex) {
            Assert.fail(ex.getMessage());
        }
        Assert.assertNotNull(result);

    }

    /*
     * Test method for 'org.pimslims.servlet.Blast.NCBIBlast.getBlastResults(String[], String[])' To
     * asynchronous job
     */
    public void testGetBlastXmlasynchNullSeq() throws InterruptedException {
        final InputParams params6 = new InputParams();
        params6.setProgram(NCBIBLastTest.PROGRAM);
        params6.setDatabase(NCBIBLastTest.PDB); // to blast against Target db
        params6.setEmail(NCBIBLastTest.EMAIL);
        params6.setNumal(InputParams.MIN_HITS); // to limit number of alignments retrieved
        params6.setScores(InputParams.MIN_HITS); // to limit number of hits retrieved

        params6.setAsync(new Boolean(true)); // set the sumissions

        String result2 = null;
        try {

            final NCBIBlast service2 = new NCBIBlast();
            result2 = service2.getBlastXmlAsync(params6, null);
            Assert.assertTrue(result2.equals(""));
            System.out.println("......The xml for Asynchronous Blast with no sequence is ....");
            System.out.println(result2);

        } catch (final IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    /*
     * Test method for 'org.pimslims.servlet.Blast.NCBIBlast.getBlastResults(String[], String[])' To
     * asynchronous job
     */
    public void testGetBlastXmlasynchEmptySeq() throws InterruptedException {
        final InputParams params6 = new InputParams();
        params6.setProgram(NCBIBLastTest.PROGRAM);
        params6.setDatabase(NCBIBLastTest.PDB); // to blast against Target db
        params6.setEmail(NCBIBLastTest.EMAIL);
        params6.setNumal(InputParams.MIN_HITS); // to limit number of alignments retrieved
        params6.setScores(InputParams.MIN_HITS); // to limit number of hits retrieved

        params6.setAsync(new Boolean(true)); // set the sumissions

        String result2 = null;
        try {

            final NCBIBlast service2 = new NCBIBlast();
            result2 = service2.getBlastXmlAsync(params6, "");
            Assert.assertTrue(result2.equals(""));
            System.out.println("......The xml for Asynchronous Blast with no sequence is ....");
            System.out.println(result2);

        } catch (final IOException ex) {
            Assert.fail(ex.getMessage());
        }
    }

}
