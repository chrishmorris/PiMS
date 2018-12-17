/**
 * pims-model org.pimslims.upgrader Version43Test.java
 * 
 * @author cm65
 * @date 10 Feb 2012
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.upgrader;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaClassImpl;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.Workflow;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;

/**
 * Version43Test Tests model changes for PiMS5.0 / PiMSPro0.7
 */
public class Version44Test extends TestCase {

    public static Test suite() {
        TestSuite ret = new TestSuite();
        ret.addTestSuite(Version44Test.class);
        return ret;
    }

    private static final String UNIQUE = "v44_" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    /**
     * @param name
     */
    public Version44Test(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    //  PRIV-27
    public void testUser() throws ConstraintException, AccessException {
        MetaClass metaClass = this.model.getMetaClass(User.class.getName());
        MetaAttribute attribute = metaClass.getAttribute(User.PROP_ISACTIVE);
        assertNotNull(attribute);

        WritableVersion version = this.model.getTestVersion();
        try {
            User user = new User(version, UNIQUE);
            assertTrue(user.getIsActive());
            user.setIsActive(false);
            version.flush(); // ensure all DB constraints are exercised

            assertEquals(Boolean.FALSE, user.get_Value(User.PROP_ISACTIVE));

            user.set_Value(User.PROP_ISACTIVE, true);
            assertEquals(Boolean.TRUE, user.getIsActive());

        } finally {
            version.abort();
        }
    }

    public void testGroup() throws ConstraintException, AccessException {
        MetaClass metaClass = this.model.getMetaClass(UserGroup.class.getName());
        MetaAttribute attribute = metaClass.getAttribute(UserGroup.PROP_MAXSIZE);
        assertNotNull(attribute);

        WritableVersion version = this.model.getTestVersion();
        try {
            UserGroup group = new UserGroup(version, UNIQUE);
            assertEquals(null, group.getMaxSize());
            group.setMaxSize(1);
            version.flush(); // ensure all DB constraints are exercised

            assertEquals(new Integer(1), group.getMaxSize());

            group.set_Value(UserGroup.PROP_MAXSIZE, 2);
            assertEquals(new Integer(2), group.getMaxSize());

        } finally {
            version.abort();
        }
    } // */

    // PRIV-20
    public void testSample() throws ConstraintException, AccessException {
        MetaClass metaClass = this.model.getMetaClass(Sample.class.getName());
        MetaAttribute attribute = metaClass.getAttribute(Sample.PROP_CONCENTRATION);
        assertNotNull(attribute);
        assertTrue(((MetaClassImpl) metaClass).getAllAttributes().keySet()
            .contains(Sample.PROP_CONCENTRATION));
        attribute = metaClass.getAttribute(Sample.PROP_CONCENTRATIONUNIT);
        assertNotNull(attribute);

        WritableVersion version = this.model.getTestVersion();
        try {
            Sample sample = new Sample(version, UNIQUE);
            assertEquals("", sample.getConcentrationUnit());
            assertEquals("", sample.getConcDisplayUnit());
            sample.setConcentration(1.0f);
            sample.setConcentrationUnit("M");
            sample.setConcDisplayUnit("M");
            version.flush(); // ensure all DB constraints are exercised

            assertEquals(1.0f, sample.get_Value(Sample.PROP_CONCENTRATION));
            assertEquals("M", sample.get_Value(Sample.PROP_CONCENTRATIONUNIT));
            assertEquals("M", sample.get_Value(Sample.PROP_CONCDISPLAYUNIT));

            sample.set_Value(Sample.PROP_CONCENTRATION, 2.0f);
            sample.set_Value(Sample.PROP_CONCENTRATIONUNIT, "kg/kg");
            sample.set_Value(Sample.PROP_CONCDISPLAYUNIT, "w/w");
            assertEquals(2.0f, sample.getConcentration());
            assertEquals("kg/kg", sample.getConcentrationUnit());
            assertEquals("w/w", sample.getConcDisplayUnit());

            try {
                sample.set_Value(Sample.PROP_CONCENTRATIONUNIT, "seconds");
                fail("bad concentration unit accepted");
            } catch (ConstraintException e) {
                // that's as it should be
            }
        } finally {
            version.abort();
        }
    }

    //TODO test a sample can't have both a concentration, and a list of components

    // PRIV-17
    public void testWorkflow() throws ConstraintException, AccessException, SecurityException,
        NoSuchFieldException {
        MetaClass metaClass = this.model.getMetaClass(Workflow.class.getName());
        assertNotNull(metaClass.getMetaRole(Workflow.PROP_PROTOCOLS));
        assertNotNull(metaClass.getMetaRole(Workflow.PROP_PROJECTS));

        WritableVersion version = this.model.getTestVersion();
        try {
            Workflow workflow = new Workflow(version, UNIQUE);
            assertTrue(workflow.getProtocols().isEmpty());

            ExperimentType type = new ExperimentType(version, UNIQUE);
            Protocol protocol = new Protocol(version, UNIQUE, type);
            workflow.addProtocol(protocol);
            version.flush(); // ensure all DB constraints are exercised

            assertTrue(((Collection<Protocol>) workflow.get_Value(Workflow.PROP_PROTOCOLS))
                .contains(protocol));

            workflow.set_Value(Workflow.PROP_PROTOCOLS, Collections.EMPTY_SET);
            assertEquals(0, workflow.getProtocols().size());

            // check a construct can have assigned workflows
            Project project = new ResearchObjective(version, UNIQUE, "test"); // VCID
            project.addWorkflow(workflow);
            version.flush(); // ensure all DB constraints are exercised
            assertTrue(workflow.getProjects().contains(project));

        } finally {
            version.abort();
        }
    }

    public void testExperimentGroup() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            ExperimentType type = new ExperimentType(version, UNIQUE);
            {
                // the API we have
                Experiment experiment = new Experiment(version, UNIQUE, NOW, NOW, type);
                new InputSample(version, experiment).setSample(new Sample(version, UNIQUE + "ino"));
                Parameter setup = new Parameter(version, experiment);
                setup.setName(UNIQUE + "setup");
                setup.setValue("one");
                Parameter result = new Parameter(version, experiment);
                result.setName(UNIQUE + "result");
                result.setValue("two");
                version.flush(); // that's a perfectly good characterisation experiment
                new OutputSample(version, experiment).setSample(new Sample(version, UNIQUE + "outo"));
                version.flush(); // that's a perfectly good well experiment
                new OutputSample(version, experiment).setSample(new Sample(version, UNIQUE + "out2o"));
                version.flush(); // that's a perfectly good purification experiment

                Parameter found =
                    experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, setup.getName());
                assertEquals("one", found.getValue());
                found =
                    experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, result.getName());
                assertEquals("two", found.getValue());
            }
            /* TODO first implement this new API with only a normal upgrader
             * The deprecate the old API
             * Then implement the new API more efficiently, with a special upgrader if necessary
            // the API we want
            {
                Protocol protocol = new Protocol(version, UNIQUE, type);
                ParameterDefinition setup =
                    new ParameterDefinition(version, UNIQUE + "setup", "Strin", protocol);
                ParameterDefinition result =
                    new ParameterDefinition(version, UNIQUE + "result", "String", protocol);
                ExperimentGroup group = new ExperimentGroup(version, UNIQUE, NOW, NOW, protocol);
                Experiment experiment = new Experiment(version, group);
                new InputSample(version, experiment).setSample(new Sample(version, UNIQUE + "in"));
                group.setParameter(setup, "one");
                Sample output = new Sample(version, UNIQUE + "out");
                output.setExperiment(experiment);
                output.setParameter(result, "two");
                version.flush(); // that's a perfectly good characterisation experiment
                // and that's a perfectly good well experiment
                Sample output2 = new Sample(version, UNIQUE + "out2");
                output2.setExperiment(experiment);
                version.flush(); // that's a perfectly good purification experiment

                assertEquals(NOW, experiment.getStartDate());
                assertEquals(NOW, experiment.getEndDate());
                assertEquals(group, experiment.getExperimentGroup());
                assertEquals("one", group.getParameter(setup));
                assertEquals("two", output.getParameter(result));      
                assertFalse(experiment instanceof LabBookEntry);          
            } */

        } finally {
            version.abort();
        }
    }

}
