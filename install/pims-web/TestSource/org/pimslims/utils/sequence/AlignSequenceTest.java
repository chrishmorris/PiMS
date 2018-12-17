/**
 * pims-web org.pimslims.utils.sequence AlignSequenceTest.java
 * 
 * @author Marc Savitsky
 * @date 9 Sep 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.utils.sequence;

import java.util.Collection;
import java.util.Collections;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.bioinf.local.PimsAlignment;
import org.pimslims.bioinf.local.SWSearch;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Creator;
import org.pimslims.lab.file.CaliperFile;
import org.pimslims.lab.sequence.PcrDnaSequence;
import org.pimslims.lab.sequence.PearsonDnaSequence;
import org.pimslims.model.core.Annotation;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.test.POJOFactory;

/**
 * AlignSequenceTest
 * 
 */
public class AlignSequenceTest extends TestCase {

    private static final String UNIQUE = "as" + System.currentTimeMillis();

    /**
     * The database being tested
     */
    private final AbstractModel model;

    /**
     * The transaction to use for testing
     */
    private WritableVersion version = null;

    private Experiment experiment;

    private final String fwdpcrString = "AGGAGATATACCATGAACAAAAAAGCCAGACTGTGCACCAGAGAGGAGTTTGTGAAAAAA"
        + "GTGAACAGCCACGCCGCCTTGGGAGCCATGTTTGAAGAGCAAAACCAGTGGAAGAACGCA"
        + "CGAGAAGCTGTGGAAGACCCAAAGTTCTGGGAAATGGTGGATGAGGAAAGGGAATGCCAC"
        + "CTCCGAGGGGAATGCCGAACCTGCATATATAACATGATGGGCAAACGTGAGAAAAAACCC"
        + "GGAGAATTTGGAAAGGCCAAGGGAAGCAGAGCCATCTGGTTCATGTGGCTGGGAGCCAGA"
        + "TTCTTGGAGTTTGAAGCTCTAGGATTCCTCAATGAAGACCATTGGATGAGTAGAGAGAAT"
        + "TCAGGAGGAGGAGTTGAAGGAGCTGGTATTCAGAAGCTGGGATACATCTTGAGAGATGTG"
        + "GCTCAAAAGCCTGGAGGGAAAATTTATGCCGATGACACAGCTGGTTGGGACACCCGCATC"
        + "ACACAAGCTGACCTTGAGAACGAAGCTAAAGTTCTGGAGTTGATGGAAGGTGAGCAGCGG"
        + "ACTTTGGCAAGAGCAATCATTGAGCTGACATACAGGCACAAAGTGGTCAAGGTCATGCGC"
        + "CCGGCAGCTGGAGGAAAGACTGTGATGGATGTCATTTCACGCGAAGATCAAAGAGGAAGT"
        + "GGACAAGTAGTGACCTATGCTTTAAACACATTCACCAACATCGCTGTGCAATTGGTTAGA"
        + "CTCATGGAAGCCGAAGCGGTCATAGGTCCAGATGACATTGAAAGCATTGAAAGGAAAAAG"
        + "AAATTTGCAGTCCGCACATGGCTTTTTGAGAACGCAGAAGAAAGAGTGCAGCGCATGGCT"
        + "GTTAGTGGTGATGACTGTGTTGTCAAACCATTGGATGATAGATTTTCCACTGCTTTGCAT"
        + "TTCTTGAACGCCATGTCAAAGGTGAGAAAAGACATCCAGGAGTGGAAACCCTCACAAGGC"
        + "TGGTATGACTGGCAACAAGTCCCCTTCTGTTCGAATCATTTCCAGGAAGTCATCATGAAA"
        + "GATGGGAGGACCTTGGTGGTGCCCTGCAGAGGACAGGACGAGCTGATTGGAAGAGCACGA"
        + "ATATCCCCTGGCTCAGGATGGAACGTCAGGGACACGGCATGCTTAGCAAAAGCATATGCA"
        + "CAGATGTGGCTTGTGTTGTACTTCCATCGACGGGATCTGCGCCTGATGGCCAACGCCATA"
        + "TGCTCTTCAGTCCCAGTTGACTGGGTTCCAACCGGCAGGACTACTTGGTCCATCCATGGA"
        + "AAAGGAGAATGGATGACAACAGAAGACATGCTAAGCGTGTGGAACCGTGTGTGGATTTTG"
        + "GAAAATGAATGGATGGAGGACAAGACCACGGTTTCCGACTGGACGGAAGTCCCATATGTA" + "GGGAAGAAACATCACCATCAC";

    private final String fwdzipString =
        ">H01-T7 1794229.seq - ID: H01-T7 on 2006/10/17-8:7:25 automatically "
            + "edited with PhredPhrap, start with base no.: 26 Internal Params: "
            + "Windowsize: 20, Goodqual: 19, Badqual: 10, Minseqlength: 50, nbadelimit: 1\n"
            + "aTCCGGGaccTTTaaTTCAACCCAACACAATATATTATAGTTAAATAAGAATTATTATCA"
            + "AATCATTTGTATATTAATTAAAATACTATACTGTAAATTACATTTTATTTACAATCAAAG"
            + "GAGATATACCATGAACAAAAAAGCCAGACTGTGCACCAGAGAGGAGTTTGTGAAAAAAGT"
            + "GAACAGCCACGCCGCCTTGGGAGCCATGTTTGAAGAGCAAAACCAGTGGAAGAACGCACG"
            + "AGAAGCTGTGGAAGACCCAAAGTTCTGGGAAATGGTGGATGAGGAAAGGGAATGCCACCT"
            + "CCGAGGGGAATGCCGAACCTGCATATATAACATGATGGGCAAACGTGAGAAAAAACCCGG"
            + "AGAATTTGGAAAGGCCAAGGGAAGCAGAGCCATCTGGTTCATGTGGCTGGGAGCCAGATT"
            + "CTTGGAGTTTGAAGCTCTAGGATTCCTCAATGAAGACCATTGGATGAGTAGAGAGAATTC"
            + "AGGAGGAGGAGTTGAAGGAGCTGGTATTCAGAAGCTGGGATACATCTTGAGAGATGTGGC"
            + "TCAAAAGCCTGGAGGGAAAATTTATGCCGATGACACAGCTGGTTGGGACACCCGCATCAC"
            + "ACAAGCTGACCTTGAGAACGAAGCTAAAGTTCTGGAGTTGATGGAAGGTGAGCAGCGGAC"
            + "TTTGGCAAGAGCAATCATTGAGCTGACATACAGGCACAAAGTGGTCAAGGTCATGCGCCC"
            + "GGCAGCTGGAGGAAAGACTGTGATGGATGTCATTTCACGCGAAGATCAAAGAGGAAGTgG"
            + "ACAAGTAGTGACCTATGCTTTAAACACATTCACCAACATCGCTGTGCAATTGGTTAGaCT" + "CATGGAaGcCGaaGCgGTCAT";

    private final String revpcrString = "ACAAGTTTGTACAAAAAAGCAGGCTTCGAAGGAGATAGAACCATGGCACATCACCACCAC"
        + "CATCACATGACTAAAATTGGTATTAATGGATTTGGACGTATCGGACGTAACGTATTCCGC"
        + "GCAGCTCTTAACAACTCTGAGGTAGAAGTAGTAGCAATCAACGACTTAACAGATGCTAAA"
        + "ACATTAGCTCACCTTTTAAAATATGACACAGTTCACGGAACTTTAAATGCAGAAGTATCT"
        + "GCTAACGAAAACAGCATCGTTGTTAACGGTAAAGAAATTAAAGTTATCGCTGAGCGTGAC"
        + "CCAGCTCAATTACCATGGAGCGACTACGGAGTAGAAGTAGTAGTAGAATCTACTGGCCGT"
        + "TTCACTAAAAAATCAGACGCTGAAAAACACTTAGGTGGATCAGTTAAGAAAGTTATCATC"
        + "TCAGCTCCAGCTTCTGACGAAGATATCACTGTAGTTATGGGTGTTAACCACGAACAATAC"
        + "GATGCAGCTAACCACAACGTAGTATCTAACGCTTCTTGTACTACAAACTGTCTAGCTCCA"
        + "TTCGCTAAAGTATTAAACGAAAAATTCGGCGTAAAACGCGGAATGATGACAACAATTCAC"
        + "TCTTACACTAACGACCAACAAATCTTAGACTTACCACACAAAGATTTACGTCGTGCTCGT"
        + "GCAGCAGCTGAAAACATGATCCCAACATCTACTGGTGCAGCTAAAGCTGTAGCATTAGTA"
        + "TTACCAGAACTTAAAGGTAAATTAAACGGTGGCGCTGTACGTGTTCCAACTGCTAACGTT"
        + "TCTCTAGTTGACCTAGTTGTTGAACTTGACAAAGAAGTAACAGTTGAAGAAGTAAATGCA"
        + "GCATTCAAAGCAGCAGCTGAAGGCGAATTAAAAGGTATCCTTGGATACAGCGAAGAGCCA"
        + "TTAGTATCTATCGACTATAACGGATGTACAGCTTCTTCTACAATCGATGCATTATCTACA"
        + "ATGGTAATGGAAGGTAACATGGTTAAAGTACTTTCTTGGTACGATAACGAAACAGGTTAC"
        + "TCTAACCGCGTAGTAGACTTAGCAGCTTACATGACTTCAAAAGGTCTTTAATGAGACCCA" + "GCTTTCTTGTACAAAGTGGT";

    private final String revzipString =
        ">1278_pET-RP_A07 654481.seq - on 2004/5/5-8:37:20 automatically edited with PhredPhrap, "
            + "start with base no.: 20 Internal Params: Windowsize: 20, Goodqual: 19, Badqual: 10, "
            + "Minseqlength: 100, nbadelimit: 1\n"
            + "gCTTTGTTAGCAGCCGGATCATCACCACTTTGTACAAGAAAGCTGGGTCTcnnnaAaGAC"
            + "CTTTTGAAGTCATGTAAGCTGCTAAGTCTACTACGCGGTTAGAGTAACCTGTTTCGTTAT"
            + "CGTACCAAGAAAGTACTTTAACCATGTTACCTTCCATTACCATTGTAGATAATGCATCGA"
            + "TTGTAGAAGAAGCTGTACATCCGTTATAGTCGATAGATACTAATGGCTCTTCGCTGTATC"
            + "CAAGGATACCTTTTAATTCGCCTTCAGCTGCTGCTTTGAATGCTGCATTTACTTCTTCAA"
            + "CTGTTACTTCTTTGTCAAGTTCAACAACTAGGTCAACTAGAGAAACGTTAGCAGTTGGAA"
            + "CACGTACAGCGCCACCGTTTAATTTACCTTTAAGTTCTGGTAATACTAATGCTACAGCTT"
            + "TAGCTGCACCAGTAGATGTTGGGATCATGTTTTCAGCTGCTGCACGAGCACGACGTAAAT"
            + "CTTTGTGTGGTAAGTCTAAGATTTGTTGGTCGTTAGTGTAAGAGTGAATTGTTGTCATCA"
            + "TTCCGCGTTTTACGCCGAATTTTTCGTTTAATACTTTAGCGAATGGAGCTAGACAGTTTG"
            + "TAGTACAAGAAGCGTTAGATACTACGTTGTGGTTAGCTGCATCGTATTGTTCGTGGTTAA"
            + "CACCCATAACTACAGTGATAtctTCGTCAGAAGCTGGAGCTGAGATGATAACTTTCTTAA"
            + "CTGATCCACCTAAGTGTTTTTCAgcnnntGATTTTTTAGtgaaACGgccagtAgattcta"
            + "CTACTACTTCTACTCCGTAgtcgctccATggTAATTGagcTGGGTCACGCttCaGCGAtA" + "acTTTAATTTtcnttACcg";

    /**
     * Constructor for CheckSequenceServletTest
     * 
     * @param name
     */
    public AlignSequenceTest(final String name) {

        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * CheckSequenceServletTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        this.version = this.model.getTestVersion();
        Assert.assertNotNull(this.version);

        this.experiment = POJOFactory.create(this.version, Experiment.class);
        final ExperimentGroup experimentGroup = POJOFactory.create(this.version, ExperimentGroup.class);
        this.experiment.setExperimentGroup(experimentGroup);

        final OutputSample outputSample = POJOFactory.create(this.version, OutputSample.class);
        final Sample sample = POJOFactory.create(this.version, Sample.class);
        final Holder holder = POJOFactory.create(this.version, Holder.class);
        sample.setColPosition(2);
        sample.setRowPosition(2);
        sample.setHolder(holder);
        outputSample.setSample(sample);
        this.experiment.addOutputSample(outputSample);

        final Annotation annotation = POJOFactory.create(this.version, Annotation.class);
        annotation.setParentEntry(this.experiment);
        annotation.setDetails("Sequencing Results");
        annotation.setName("10835948566.zip");
        annotation.setFilename("108359.zip");
        this.experiment.addAttachment(annotation);

        final ResearchObjective expBlueprint = POJOFactory.create(this.version, ResearchObjective.class);
        final ResearchObjectiveElement blueprintComponent =
            POJOFactory.create(this.version, ResearchObjectiveElement.class);

        final Molecule trialMolComponent = POJOFactory.create(this.version, Molecule.class);

        this.version.flush();
        final ComponentCategory category =
            this.version.findFirst(ComponentCategory.class, ComponentCategory.PROP_NAME,
                CaliperFile.PCR_PRODUCT);
        trialMolComponent.setSequence(this.fwdpcrString);
        trialMolComponent.addCategory(category);

        blueprintComponent.addTrialMolComponent(trialMolComponent);
        expBlueprint.addResearchObjectiveElement(blueprintComponent);
        expBlueprint.setLocalName(AlignSequenceTest.UNIQUE);
        expBlueprint.setCommonName(AlignSequenceTest.UNIQUE);
        this.experiment.setProject(expBlueprint);

        //final PearsonDnaSequence zipSequence =
        new PearsonDnaSequence(expBlueprint.getCommonName(), this.fwdzipString);
        this.version.flush();
        final ConstructBean constructBean = ConstructBeanReader.readConstruct(expBlueprint);
        final PcrDnaSequence pcrSequence = new PcrDnaSequence(constructBean.getPcrProductSeq());
        pcrSequence.setName(constructBean.getName());
        pcrSequence.setTargetName(constructBean.getTargetName());
    }

    /**
     * CheckSequenceServletTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        Assert.assertFalse(this.version.isCompleted());
        if (this.version.isCompleted()) {
            return;
        } // can't tidy up

        if (null != this.version) {
            this.version.abort();
        } // not testing persistence
    }

    public void testRunFwdAlign() throws AccessException, ConstraintException {

        final PcrDnaSequence pcrSequence = new PcrDnaSequence(this.fwdpcrString);
        pcrSequence.setName("OPPF1234");
        pcrSequence.setTargetName("OPPF1234");

        final PearsonDnaSequence zipSequence =
            new PearsonDnaSequence(pcrSequence.getName(), this.fwdzipString);

        SWSearch search = null;
        search = new SWSearch("Template", pcrSequence.getSequence());

        search.setCutoff(87.3f);
        final Collection<PimsAlignment> alignments =
            search.align(Collections.singleton(AlignSequenceTest.getMolComponent(this.version,
                zipSequence.getID(), zipSequence.getSequence())), true, this.version);
        Assert.assertEquals(1, alignments.size());
        final PimsAlignment alignment = alignments.iterator().next();
        Assert.assertEquals(zipSequence.getName(), alignment.getHitName());
        Assert.assertFalse("not the actual read", alignment.getIsReverseComplement());

        // System.out.println(AlignSequenceServlet.format(alignment, this.zipSequence));
    }

    public void testRunRevAlign() throws AccessException, ConstraintException {

        final PcrDnaSequence pcrSequence = new PcrDnaSequence(this.revpcrString);
        pcrSequence.setName("OPPF1234");
        pcrSequence.setTargetName("OPPF1234");

        final PearsonDnaSequence zipSequence =
            new PearsonDnaSequence(pcrSequence.getName(), this.revzipString);

        SWSearch search = null;
        search = new SWSearch("Template", pcrSequence.getSequence());

        search.setCutoff(87.3f);
        final Collection<PimsAlignment> alignments =
            search.align(Collections.singleton(AlignSequenceTest.getMolComponent(this.version,
                zipSequence.getID(), zipSequence.getSequence())), true, this.version);
        Assert.assertEquals(1, alignments.size());
        final PimsAlignment alignment = alignments.iterator().next();
        Assert.assertEquals(zipSequence.getName(), alignment.getHitName());
        Assert.assertTrue("not the reverse complement", alignment.getIsReverseComplement());

        // System.out.println(AlignSequenceServlet.format(alignment, this.zipSequence));

    }

    public static Molecule getMolComponent(final WritableVersion rv, final String id, final String sequence)
        throws AccessException, ConstraintException {

        // this is heavyweight, alignment really just needs the name and sequence
        // Best to refactor SWSearch so it uses a wrapper, which this can wrap this class or MolComponent

        final Molecule component = Creator.recordDNA(rv, id, sequence);
        component.setMolType("DNA");

        return component;
    }
}
