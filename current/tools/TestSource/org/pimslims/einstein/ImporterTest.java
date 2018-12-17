/**
 * tools org.pimslims.einstein ImporterTest.java
 * 
 * @author cm65
 * @date 9 Sep 2014
 * 
 *       Protein Information Management System
 * @version: 5.0
 * 
 *           Copyright (c) 2014 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.einstein;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.Database;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;

/**
 * ImporterTest
 * 
 */
public class ImporterTest extends TestCase {

    private static final String HEADERS =
        "WELL-#    LIMS-ID Accession   Domain-ID   Group    "
            + "Subgroup Template    TWELL-# Vector  Barcode-ID  F-Primer    R-Primer    "
            + "Start   End Target NT   Target AA   Construct NT    Construct AA     AA count   MW  pI  EXT Coeff   Clone   Sequence    "
            + "Expression  Auto Solubility \r\n" + "";

    private static final String UNIQUE = "eit" + System.currentTimeMillis();

    // TODO  it would be nice to be bale to upload the gel and link it to each rack (A1-H12). 

    public final String[] IGNORED_COLUMNS = new String[] { "AA count", "MW", "pI", "EXT Coeff", // either calculated or imported would work, whatever is easier for you.  Calculated could be pretty awesome tool to have.
        "Group", "Subgroup", // Does it ever vary? >very rarely changes and can be ignored in database
        "Sample Size(KDA", "Sample Conc(ng/ul)", // >These numbers can also be ignored. They are calculated from the gel
        "Start", "End" // > this was for primers, but is not needed and can be ignored for this scenario 

    };

    private final AbstractModel model;

    /**
     * Constructor for ImporterTest
     * 
     * @param name
     */
    public ImporterTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    // note "all data can be seen/updated by anyone from our group"
    public final void testImporter() throws IOException, ConstraintException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            doImport(version, IGNORED_COLUMNS[0]);
        } finally {
            version.abort();
        }
    }

    public final void testTarget() throws IOException, ConstraintException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            String string =
                "Accession,LIMS-ID,Target NT,Target AA,Domain-ID,Construct NT,Construct AA,F-Primer,R-Primer,WELL-#,Auto Solubility,Start,End\n"
                    + UNIQUE + ",," + "TGAAAATCGTCA," + "VKIVTYS\r\n";
            doImport(version, string);
            Target target = version.findFirst(Target.class, Target.PROP_NAME, UNIQUE);
            assertNotNull(target);
            assertEquals("VKIVTYS", target.getProtein().getSequence());
            Molecule gene = target.getNucleicAcids().iterator().next();
            assertEquals("TGAAAATCGTCA", gene.getSequence());

            ExternalDbLink link = target.getExternalDbLinks().iterator().next();
            assertEquals(UNIQUE, link.getAccessionNumber());
            assertEquals("http://www.ncbi.nlm.nih.gov/protein/" + UNIQUE, link.getUrl());
            Database database = link.getDatabaseName();
            assertEquals("NCBI Protein", database.getName());
        } finally {
            version.abort();
        }
    }

    private void doImport(WritableVersion version, String string) throws IOException, ConstraintException,
        AccessException {
        new Importer(version).importTab("h" + UNIQUE, new ByteArrayInputStream(string.getBytes("UTF8")));
    }

    public final void testConstruct() throws ConstraintException, IOException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            String string =
                "LIMS-ID,Start,End,Vector,Accession,Domain-ID,Construct NT,Construct AA,F-Primer,R-Primer,Target NT,Target AA,WELL-#,Auto Solubility\n"
                    + ",N/A,N/A,pSGC-His,"
                    + UNIQUE
                    + "T,C"
                    + UNIQUE
                    + ",TGAAAATCGTCA,"
                    + "VKIVTYS,"
                    + "TACTTCCAATCCATGGTGAAAATCGTCACATACTCCAAAAAC,"
                    + "TATCCACCTTTACTGTTAAGGGCGAGCATAACCATCAGC\r\n";
            doImport(version, string);
            ResearchObjective ro =
                version.findFirst(ResearchObjective.class, ResearchObjective.PROP_COMMONNAME, "CC" + UNIQUE);
            assertNotNull(ro);
            version.flush();
            ConstructBean bean = ConstructBeanReader.readConstruct(ro);
            assertEquals("TGAAAATCGTCA", bean.getDnaSeq());
            assertEquals("VKIVTYS", bean.getExpressedProt());
            assertEquals("TACTTCCAATCCATGGTGAAAATCGTCACATACTCCAAAAAC", bean.getFwdPrimer());
            assertEquals("TATCCACCTTTACTGTTAAGGGCGAGCATAACCATCAGC", bean.getRevPrimer());
            assertNotNull(bean.getVectorInputSampleHook());
            InputSample vector = version.get(bean.getVectorInputSampleHook());
            assertEquals("pSGC-His", vector.getSample().getName());

        } finally {
            version.abort();
        }
    }

/* I attached a rare example where the LIMS ID, Domain ID, and barcode are all different.   So the constructs id number would change if a different construct of same target by a letter or a number.
    */
    public void testDomain() throws ConstraintException, IOException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            String string =
                "LIMS-ID,WELL-#,LIMS-ID,Accession,Domain-ID,Group,Subgroup,Template,TWELL-#,Vector,Barcode-ID,F-Primer,R-Primer,Start,End,Target NT,Target AA,Construct NT,Construct AA, AA count,MW,pI,EXT Coeff,Clone,Sequence,Expression,Auto Solubility\r\n"
                    + ",A01,30988,BC105455.1,030988a,PSI,SAM,SYNTHETIC,Bos taurus,pcDNA3.3 topo,140309881,GCCACCATGAGGAGCCGGCCG,CTACTActtgtcatcgtcgtccttgtagtcCCAGTCTAACTTCAGGTCTGCTTTG,46,364,GCCACCATGAGGAGCCGGCCGCTCCAGGACAGGAGGGAAGCGGGGGAGCCCGGCAGGGCGGAGGGAGAGGACGGCGGCGACCTGCCCACCACCCCCACCAGCGTCAATTACCACTTCACCCGCCAGTGCAACTACAAGTGTGGCTTCTGCTTCCACACAGCCAAAACGTCCTTCGTGCTGCCGCTGGAGGAGGCCAAGAAAGGTCTGCTGATGCTGAAAGAAGCAGGTATGGAGAAGATCAACTTCTCAGGCGGGGAGCCATTCCTCCAGGACCGGGGCGAGTACCTGGGCAGGCTGGTGAAGTTCTGTAAGCAGGAGCTGCAGCTGCCCAGCGTCAGCATTGTCAGCAACGGGAGCCTGATCCGCGAGAGGTGGTTCCAGAAGTACGGTGAATATTTGGACATTCTCGCCATCTCCTGTGACAGCTTTGACGAGCAGGTCAATGTCCTTATTGGCCGTGGTCAAGGAAAGAAGAACCACGTGGAAAATCTCCAGAAACTGAGGACATGGTGTAAGGAATACAGAGTGGCCTTCAAGATAAACTCAGTCATCAATCGATTCAACGTGGACGAGGATATGAAGGAACAGATAACCGCGCTCAACCCCGTCCGCTGGAAGGTCTTCCAGTGTCTCATAATTGAGGGTGAGAACTCTGGAGAAGATGCTCTGAGAGAAGCGGAACGGTTCGTGATCAGCGACGAGGAGTTTGAAGCGTTCTTGGATCGCCACAAGGATGTGTCCTGCTTGGTGCCCGAGTCTAACCAGAAGATGAGAGATTCCTACCTTATTCTGGATGAATATATGCGCTTCCTGAACTGTAGAAATGGGCGGAAGGATCCTTCCAAGTCCATCTTGGACGTGGGTGTAGAAGAAGCTATAAAATTCAGTGGCTTTGATGAGAAGATGTTTCTAAAGCGAGGAGGGAAATATGTATGGAGCAAAGCAGACCTGAAGTTAGACTGGgactacaaggacgacgatgacaagTAGTAG,RSRPLQDRREAGEPGRAEGEDGGDLPTTPTSVNYHFTRQCNYKCGFCFHTAKTSFVLPLEEAKKGLLMLKEAGMEKINFSGGEPFLQDRGEYLGRLVKFCKQELQLPSVSIVSNGSLIRERWFQKYGEYLDILAISCDSFDEQVNVLIGRGQGKKNHVENLQKLRTWCKEYRVAFKINSVINRFNVDEDMKEQITALNPVRWKVFQCLIIEGENSGEDALREAERFVISDEEFEAFLDRHKDVSCLVPESNQKMRDSYLILDEYMRFLNCRNGRKDPSKSILDVGVEEAIKFSGFDEKMFLKRGGKYVWSKADLKLDW,ATGCACCATCATCATCATCATTCTTCTGGTGTAGATCTGGGTACCGAGAACCTGTACTTCCAATCCATGAGGAGCCGGCCGCTCCAGGACAGGAGGGAAGCGGGGGAGCCCGGCAGGGCGGAGGGAGAGGACGGCGGCGACCTGCCCACCACCCCCACCAGCGTCAATTACCACTTCACCCGCCAGTGCAACTACAAGTGTGGCTTCTGCTTCCACACAGCCAAAACGTCCTTCGTGCTGCCGCTGGAGGAGGCCAAGAAAGGTCTGCTGATGCTGAAAGAAGCAGGTATGGAGAAGATCAACTTCTCAGGCGGGGAGCCATTCCTCCAGGACCGGGGCGAGTACCTGGGCAGGCTGGTGAAGTTCTGTAAGCAGGAGCTGCAGCTGCCCAGCGTCAGCATTGTCAGCAACGGGAGCCTGATCCGCGAGAGGTGGTTCCAGAAGTACGGTGAATATTTGGACATTCTCGCCATCTCCTGTGACAGCTTTGACGAGCAGGTCAATGTCCTTATTGGCCGTGGTCAAGGAAAGAAGAACCACGTGGAAAATCTCCAGAAACTGAGGACATGGTGTAAGGAATACAGAGTGGCCTTCAAGATAAACTCAGTCATCAATCGATTCAACGTGGACGAGGATATGAAGGAACAGATAACCGCGCTCAACCCCGTCCGCTGGAAGGTCTTCCAGTGTCTCATAATTGAGGGTGAGAACTCTGGAGAAGATGCTCTGAGAGAAGCGGAACGGTTCGTGATCAGCGACGAGGAGTTTGAAGCGTTCTTGGATCGCCACAAGGATGTGTCCTGCTTGGTGCCCGAGTCTAACCAGAAGATGAGAGATTCCTACCTTATTCTGGATGAATATATGCGCTTCCTGAACTGTAGAAATGGGCGGAAGGATCCTTCCAAGTCCATCTTGGACGTGGGTGTAGAAGAAGCTATAAAATTCAGTGGCTTTGATGAGAAGATGTTTCTAAAGCGAGGAGGGAAATATGTATGGAGCAAAGCAGACCTGAAGTTAGACTGG,,341,39.39,5.09,1.07,,,,\r\n"
                    + "";
            doImport(version, string);
            ResearchObjective ro =
                version.findFirst(ResearchObjective.class, ResearchObjective.PROP_COMMONNAME, "C030988a");
            assertNotNull(ro);
            version.flush();
            ConstructBean bean = ConstructBeanReader.readConstruct(ro);
            assertEquals("CTACTActtgtcatcgtcgtccttgtagtcCCAGTCTAACTTCAGGTCTGCTTTG".toUpperCase(),
                bean.getRevPrimer());
            assertEquals(46, bean.getTargetProtStart().intValue());
            assertEquals(364, bean.getTargetProtEnd().intValue());

        } finally {
            version.abort();
        }
    }

    /* Auto Solubility   Am I right to think that this is the key observation recorded in this spread sheet?

    >in this worksheet it tells us if small scale was successful or not and if we should proceed to large scale.  
    It is an important part of the document and this is probably where the ability to import information from the excel stops 
    and the rest would have to be manually typed in (see word document)

    */
    public final void testExperiment() throws ConstraintException, IOException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            String string =
                "LIMS-ID,Start,End,WELL-#,Auto Solubility,Vector,Accession,Domain-ID,Construct NT,Construct AA,F-Primer,R-Primer,Target NT,Target AA,Template\n"
                    + ",N/A,N/A, B03,3," // note space before well number, they do this occasionally
                    + "pSGC-His,"
                    + UNIQUE
                    + "T,C"
                    + UNIQUE
                    + ",TGAAAATCGTCA,"
                    + "VKIVTYS,"
                    + "TACTTCCAATCCATGGTGAAAATCGTCACATACTCCAAAAAC,"
                    + "TATCCACCTTTACTGTTAAGGGCGAGCATAACCATCAGC,,,,\r\n";
            doImport(version, string);

            Experiment experiment = findExperiment(version, "h" + UNIQUE);
            Parameter solubility =
                experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, "Solubility");
            assertEquals("3", solubility.getValue());
            assertNotNull(solubility.getParameterDefinition());
            assertNotNull(experiment.getProject());
            assertNotNull(experiment.getProtocol());
            assertNotNull(experiment.getExperimentGroup());

        } finally {
            version.abort();
        }
    }

    private Experiment findExperiment(WritableVersion version, String plateName) {
        Holder holder = version.findFirst(Holder.class, Holder.PROP_NAME, plateName);
        assertNotNull(holder);
        assertEquals(2, holder.getDimension());
        assertEquals(1, holder.getSamples().size());
        Sample sample = holder.getSamples().iterator().next();
        assertEquals(1, sample.getSampleCategories().size());
        assertEquals(3, sample.getColPosition().intValue());
        assertEquals(2, sample.getRowPosition().intValue());
        assertNotNull(sample.getOutputSample());
        assertNotNull(sample.getOutputSample().getRefOutputSample());
        Experiment experiment = sample.getOutputSample().getExperiment();
        return experiment;
    }

    // WELL-#, Template,  TWELL-#    Plate name, well number. Record the template plate and the pipette operation.
    public final void testTemplate() throws ConstraintException, AccessException, IOException {
        WritableVersion version = this.model.getTestVersion();
        try {
            String string =
                "LIMS-ID,Start,End,WELL-#,Auto Solubility,Vector,Accession,Domain-ID,Construct NT,Construct AA,F-Primer,R-Primer,Target NT,Target AA,"
                    + "Template,TWELL-#\n"
                    + ",N/A,N/A,B03,3,"
                    + "pSGC-His,"
                    + UNIQUE
                    + "T,C"
                    + UNIQUE
                    + ",TGAAAATCGTCA,"
                    + "VKIVTYS,"
                    + "TACTTCCAATCCATGGTGAAAATCGTCACATACTCCAAAAAC,"
                    + "TATCCACCTTTACTGTTAAGGGCGAGCATAACCATCAGC,,," + "gDNA Plate 1,F07\r\n";
            doImport(version, string);

            Experiment experiment = findExperiment(version, "h" + UNIQUE);
            assertEquals(1, experiment.getInputSamples().size());
            InputSample is = experiment.getInputSamples().iterator().next();
            assertNotNull(is.getRefInputSample());
            Sample template = is.getSample();
            assertNotNull(template);
            assertEquals(7, template.getColPosition().intValue());
            assertEquals(6, template.getRowPosition().intValue());
            assertNotNull(template.getContainer());
            assertEquals("gDNA Plate 1", template.getContainer().getName());

        } finally {
            version.abort();
        }
    }

    /* Note:
    I see the vector name in the file name SAMS 0658-pSGC-His PROD.xls. 
    Does the "SAMS 0658" also have some significance? What are the alternatives to "PROD"?

    >The PROD is production and it is just the name of the sheet there is no alternative to this. 
    and SAMS 0658 just is name of sheet as well
     * 
     * */

    /* 
    Clone, Sequence, Expression Are these values ever supplied?
    TODO >yes, in some of the sheets they are, if there is not a number yet it has not been small scaled.*/

    public final void testTwice() throws ConstraintException, AccessException, IOException {
        WritableVersion version = this.model.getTestVersion();
        try {
            String string =
                "LIMS-ID,Start,End,WELL-#,Auto Solubility,Vector,Accession,Domain-ID,Construct NT,Construct AA,F-Primer,R-Primer,Target NT,Target AA,"
                    + "Template,TWELL-#\n"
                    + ",N/A,N/A,B03,3,"
                    + "pSGC-His,"
                    + UNIQUE
                    + "T,Ca"
                    + UNIQUE
                    + ",TGAAAATCGTCA,"
                    + "VKIVTYS,"
                    + "TACTTCCAATCCATGGTGAAAATCGTCACATACTCCAAAAAC,"
                    + "TATCCACCTTTACTGTTAAGGGCGAGCATAACCATCAGC,,,"
                    + "gDNA Plate 1,F07\r\n"
                    + ",N/A,N/A,B04,3,"
                    + "pSGC-His,"
                    + UNIQUE
                    + "T,Cb"
                    + UNIQUE
                    + ",TGAAAATCGTCA,"
                    + "VKIVTYS,"
                    + "TACTTCCAATCCATGGTGAAAATCGTCACATACTCCAAAAAC,"
                    + "TATCCACCTTTACTGTTAAGGGCGAGCATAACCATCAGC,,," + "gDNA Plate 1,F07\r\n";
            doImport(version, string);
            Holder holder = version.findFirst(Holder.class, Holder.PROP_NAME, "h" + UNIQUE);
            assertNotNull(holder);
            assertEquals(2, holder.getSamples().size());

        } finally {
            version.abort();
        }
    }
}
