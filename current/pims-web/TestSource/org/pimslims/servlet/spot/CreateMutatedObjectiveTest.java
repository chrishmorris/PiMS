/**
 * V3_3-web org.pimslims.servlet.spot CreateMutatedObjectiveTest.java
 * 
 * @author cm65
 * @date 29 Sep 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.spot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.primer.SDMPrimerBean;
import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;

/**
 * CreateMutatedObjectiveTest
 * 
 */
public class CreateMutatedObjectiveTest extends TestCase {

    /**
     * TEN_BASES String The primer designer needs a target of at least 51 bases
     */
    private static final String TEN_BASES = "CATGATGATG";

    private static final String WILD_TYPE = CreateMutatedObjectiveTest.TEN_BASES
        + CreateMutatedObjectiveTest.TEN_BASES + CreateMutatedObjectiveTest.TEN_BASES + "TTTTTTTTTT"
        + CreateMutatedObjectiveTest.TEN_BASES + "C";

    private static final String INSERTION = CreateMutatedObjectiveTest.TEN_BASES
        + CreateMutatedObjectiveTest.TEN_BASES + CreateMutatedObjectiveTest.TEN_BASES + "CCC" + "TTTTTTTTTT"
        + CreateMutatedObjectiveTest.TEN_BASES + "C";

    private static final String UNIQUE = "cmo" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for CreateMutatedObjectiveTest
     * 
     * @param name
     */
    public CreateMutatedObjectiveTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.spot.CreateMutatedObjective#makePrimerBeanList(org.pimslims.presentation.construct.ConstructBean, org.pimslims.lab.primer.DesignPrimers, float, java.lang.String)}
     * .
     */
    public void testSenseInsertionPrimer() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ConstructBean cb = this.getConstructBean(version);
            final List<SDMPrimerBean> beans =
                SDMPrimerBean.makePrimerBeanList(60f, "S", cb.getDnaSeq(), cb.getWildDnaSeq());
            Assert.assertFalse(beans.isEmpty());
            final String primerSeq = beans.iterator().next().getPrimerSeq();
            Assert.assertEquals("GATGCATGATGATGCCCTTTTTTTTTTC", primerSeq);
        } finally {
            version.abort();
        }
    }

    //PIMS-3605
    public void testReverseComplement() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ConstructBean cb = this.getConstructBean(version);
            List<SDMPrimerBean> beans =
                SDMPrimerBean.makePrimerBeanList(60f, "S", cb.getDnaSeq(), cb.getWildDnaSeq());
            Assert.assertFalse(beans.isEmpty());
            final String sense = beans.iterator().next().getPrimerSeq();
            beans = SDMPrimerBean.makePrimerBeanList(60f, "A", cb.getDnaSeq(), cb.getWildDnaSeq());
            final String antisense = beans.iterator().next().getPrimerSeq();
            final DnaSequence rc = new DnaSequence(sense).getReverseComplement();
            Assert.assertEquals(rc, new DnaSequence(antisense));
        } finally {
            version.abort();
        }
    }

    public void testSave() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            //final PrimerBean reverse = new PrimerBean();
            //final PrimerBean forward = new PrimerBean();

            final ResearchObjective objective =
                new ResearchObjective(version, CreateMutatedObjectiveTest.UNIQUE, "test");
            final Molecule protein = new Molecule(version, "protein", CreateMutatedObjectiveTest.UNIQUE);
            final Target target = new Target(version, CreateMutatedObjectiveTest.UNIQUE, protein);
            final ResearchObjectiveElement roe =
                new ResearchObjectiveElement(version, CreateMutatedObjectiveTest.UNIQUE, "test", objective);
            roe.setTarget(target);
            roe.setApproxBeginSeqId(3);
            roe.setApproxEndSeqId(102);

            final ConstructBean cb = ConstructBeanReader.readConstruct(objective);
            Assert.assertEquals(3, cb.getTargetProtStart().intValue());
            Assert.assertEquals(102, cb.getTargetProtEnd().intValue());
            cb.setSDMConstruct(true);
            //was cb.setDnaSeq(CreateMutatedObjectiveTest.INSERTION);

            cb.setConstructId(CreateMutatedObjectiveTest.UNIQUE + "save1");

            Map<String, String> parms = new HashMap();
            parms.put("sense_primer", "ATG");
            parms.put("antisense_primer", "TAG");
            final ResearchObjective eo = (ResearchObjective) CreateMutatedObjective.save(parms, version, cb);
            final ConstructBean saved = ConstructBeanReader.readConstruct(eo);
            Assert.assertEquals(3, saved.getTargetProtStart().intValue());
            Assert.assertEquals(102, saved.getTargetProtEnd().intValue());

            Assert.assertEquals(CreateMutatedObjectiveTest.UNIQUE + "save1S administrator 1", saved
                .getFwdPrimerBean().getName());

            // now mutate again
            saved.setSDMConstruct(true);

            saved.setConstructId(CreateMutatedObjectiveTest.UNIQUE + "save2");
            parms = new HashMap();
            parms.put("sense_primer", "ATG");
            parms.put("antisense_primer", "TAG");
            final ResearchObjective eo2 =
                (ResearchObjective) CreateMutatedObjective.save(parms, version, saved);
            ConstructBeanReader.readConstruct(eo2);

        } finally {
            version.abort();
        }
    }

    private ConstructBean getConstructBean(final WritableVersion version) throws ConstraintException {

        final ResearchObjective objective =
            new ResearchObjective(version, CreateMutatedObjectiveTest.UNIQUE, "test");
        final ConstructBean cb = ConstructBeanReader.readConstruct(objective);
        cb.setSDMConstruct(true);
        cb.setDnaSeq(CreateMutatedObjectiveTest.INSERTION);
        cb.setWildDnaSeq(CreateMutatedObjectiveTest.WILD_TYPE);

        // could cb.setProtSeq(request.getParameter("construct_prot_seq"));

        // could cb.setExpressedProt(ThreeLetterProteinSeq.translate(cb.getDnaSeq()));
        // could cb.setFinalProt(ThreeLetterProteinSeq.translate(cb.getDnaSeq()));

        // construct id - set again if this is the first step
        // could cb.setConstructId(request.getParameter("construct_id"));

        return cb;
    }
}
