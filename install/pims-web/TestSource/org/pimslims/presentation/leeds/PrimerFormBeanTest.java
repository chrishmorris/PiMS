/**
 * V3_3-web org.pimslims.presentation.leeds PrimerFormBeanTest.java
 * 
 * @author cm65
 * @date 7 Oct 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.leeds;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ConstructUtility;
import org.pimslims.leeds.FormFieldsNames;
import org.pimslims.metamodel.AbstractModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;

/**
 * PrimerFormBeanTest
 * 
 */

@Deprecated
// Leeds code is no longer supported
public class PrimerFormBeanTest extends TestCase {

    static final String UNIQUE = "pfb" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for PrimerFormBeanTest
     * 
     * @param name
     */
    public PrimerFormBeanTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.leeds.PrimerFormBean#PrimerFormBean(org.pimslims.presentation.PrimerBean, org.pimslims.presentation.PrimerBean)}
     * .
     */
    @Deprecated
    // tests obsolete functionality
    public void wastestSave() throws AccessException, ConstraintException, ClassNotFoundException {
        final PrimerBean forward =
            new PrimerBean(PrimerFormBeanTest.UNIQUE + "F", "CATG", PrimerBean.FORWARD, "ftag");
        final PrimerBean reverse =
            new PrimerBean(PrimerFormBeanTest.UNIQUE + "R", "CCAATTGG", PrimerBean.REVERSE, "rtag");
        final PrimerFormBean bean = new PrimerFormBean(forward, reverse);
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Experiment experiment = bean.create(version);
            Assert.assertNotNull(experiment);
            Assert.assertEquals(2, experiment.getOutputSamples().size());
            Assert.assertEquals(FormFieldsNames.leedscloningDesign, experiment.getExperimentType().getName());
            Assert.assertTrue(ConstructUtility.isSpotConstruct(experiment.getProject()));
        } finally {
            version.abort();
        }
    }

    // MPSIL0067-F1 as made by PrimerPlateXlsReader
    public void testTargetLinkF() throws AccessException, ConstraintException, ClassNotFoundException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Molecule protein = new Molecule(version, "protein", "prot" + PrimerFormBeanTest.UNIQUE);
            final Target target = new Target(version, "t" + PrimerFormBeanTest.UNIQUE, protein);
            //final ResearchObjective ro = new ResearchObjective(version, PrimerFormBeanTest.UNIQUE, "test");
            //new ResearchObjectiveElement(version, "target", "test", ro).setTarget(target);
            version.flush();
            final PrimerBean forward =
                new PrimerBean(target.getName() + "-F1", "CATG", PrimerBean.FORWARD, "ftag");
            final PrimerBean reverse =
                new PrimerBean("r" + PrimerFormBeanTest.UNIQUE, "CCAATTGG", PrimerBean.REVERSE, "rtag");
            final PrimerFormBean bean = new PrimerFormBean(forward, reverse);

            final Experiment experiment = bean.create(version);

            Assert.assertNotNull(experiment);
            Assert.assertEquals(2, experiment.getOutputSamples().size());
            Assert.assertEquals(FormFieldsNames.leedscloningDesign, experiment.getExperimentType().getName());
            Assert.assertNotNull(experiment.getProject());
        } finally {
            version.abort();
        }
    }

    // MPSIL0033R1 as documented on PrimerBeanReader
    public void testTargetLinkR() throws AccessException, ConstraintException, ClassNotFoundException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Molecule protein = new Molecule(version, "protein", "prot" + PrimerFormBeanTest.UNIQUE);
            final Target target = new Target(version, PrimerFormBeanTest.UNIQUE, protein);
            //final ResearchObjective ro = new ResearchObjective(version, PrimerFormBeanTest.UNIQUE, "test");
            //new ResearchObjectiveElement(version, "target", "test", ro).setTarget(target);
            final PrimerBean forward =
                new PrimerBean("f" + PrimerFormBeanTest.UNIQUE, "CATG", PrimerBean.FORWARD, "ftag");
            final PrimerBean reverse =
                new PrimerBean(target.getName() + "R1", "CCAATTGG", PrimerBean.REVERSE, "rtag");
            final PrimerFormBean bean = new PrimerFormBean(forward, reverse);

            final Experiment experiment = bean.create(version);

            Assert.assertNotNull(experiment);
            Assert.assertEquals(2, experiment.getOutputSamples().size());
            Assert.assertEquals(FormFieldsNames.leedscloningDesign, experiment.getExperimentType().getName());
            Assert.assertNotNull(experiment.getProject());
        } finally {
            version.abort();
        }
    }

    public void testReadAsSpot() throws AccessException, ConstraintException, ClassNotFoundException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Molecule protein = new Molecule(version, "protein", "prot" + PrimerFormBeanTest.UNIQUE);
            final Target target = new Target(version, PrimerFormBeanTest.UNIQUE, protein);
            final ResearchObjective ro = new ResearchObjective(version, PrimerFormBeanTest.UNIQUE, "test");
            new ResearchObjectiveElement(version, "target", "test", ro).setTarget(target);
            final PrimerBean forward =
                new PrimerBean("f" + PrimerFormBeanTest.UNIQUE, "CATG", PrimerBean.FORWARD, "ftag");
            final PrimerBean reverse =
                new PrimerBean(target.getName() + "R1", "CCAATTGG", PrimerBean.REVERSE, "rtag");
            final PrimerFormBean formBean = new PrimerFormBean(forward, reverse);
            formBean.setFrom("2");
            formBean.setTo("100");

            final Experiment experiment = formBean.create(version);

            final ConstructBean cb = ConstructBeanReader.readConstruct(experiment);
            Assert.assertEquals(1, cb.getTargetBeans().size());
            Assert.assertEquals(target.getName(), cb.getTargetBeans().iterator().next().getName());
            Assert.assertEquals(cb.getHook(), ((AbstractModelObject) experiment.getProject()).get_Hook());
            Assert.assertEquals(new Integer(2), cb.getTargetProtStart());
            Assert.assertEquals(new Integer(100), cb.getTargetProtEnd());

        } finally {
            version.abort();
        }
    }
}
