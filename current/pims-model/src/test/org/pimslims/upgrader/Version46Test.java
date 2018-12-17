/**
 * pims-model org.pimslims.upgrader Version43Test.java
 * 
 * @author cm65
 * @date 10 Feb 2012
 * 
 *       Protein Information Management System
 * @version: 4
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
import org.pimslims.model.accessControl.Preference;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;

/**
 * Version43Test Tests model changes for PiMS5.0 / PiMSPro0.7
 */
public class Version46Test extends TestCase {

    public static Test suite() {
        TestSuite ret = new TestSuite();
        ret.addTestSuite(Version46Test.class);
        return ret;
    }

    private static final String UNIQUE = "v44_" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private static final String LONG = "long" + UNIQUE + UNIQUE + UNIQUE + UNIQUE + UNIQUE + UNIQUE + UNIQUE
        + UNIQUE + UNIQUE + UNIQUE;

    private final AbstractModel model;

    /**
     * @param name
     */
    public Version46Test(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /* add attribute to existing class
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
    } */

    public void testSeeAlso() throws ConstraintException, AccessException {
        MetaClass metaClass = this.model.getMetaClass(SampleCategory.class.getName());
        MetaAttribute attribute = metaClass.getAttribute(SampleCategory.PROP_SAME_AS);
        assertNotNull(attribute);

        WritableVersion version = this.model.getTestVersion();
        try {
            SampleCategory category = new SampleCategory(version, UNIQUE);
            assertTrue(category.getSameAs().isEmpty());
            category.addSameAs("http://www.example.com/rdfs#aaa");
            version.flush(); // ensure all DB constraints are exercised

            assertEquals(1, category.getSameAs().size());
            assertEquals("http://www.example.com/rdfs#aaa", category.getSameAs().iterator().next());

            category.setSameAs(Collections.singletonList("http://www.example.com/rdfs#bbb"));
            category.addSameAs("http://www.example.com/rdfs#ccc");

        } finally {
            version.abort();
        }
    }

    /* New roles
     *     public void testuser() throws ConstraintException, AccessException, SecurityException,
        NoSuchFieldException {
        MetaClass metaClass = this.model.getMetaClass(Workflow.class.getName());
        assertNotNull(metaClass.getMetaRole(Workflow.PROP_PROTOCOLS));

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

        } finally {
            version.abort();
        }
    }
    */

    // new class
    public void testPreference() throws ConstraintException, AccessException {
        assertTrue(this.model.getClassNames().contains(Preference.class.getName()));
        MetaClass metaClass = this.model.getMetaClass(Preference.class.getName());
        assertNotNull(metaClass);
        MetaAttribute attribute = metaClass.getAttribute(Preference.PROP_NAME);
        assertNotNull(attribute);

        WritableVersion version = this.model.getTestVersion();
        try {
            User user = new User(version, UNIQUE);
            Preference object = new Preference(version, user, UNIQUE, UNIQUE + "url");

            assertEquals(UNIQUE, object.getName());
            object.setValue(UNIQUE + "v");
            version.flush(); // ensure all DB constraints are exercised

            Collection<Preference> preferences = user.getPreferences();
            assertEquals(1, preferences.size());
            assertTrue(preferences.contains(object));

            assertEquals(UNIQUE + "v", object.get_Value(Preference.PROP_VALUE));

            // sample categories
            assertTrue(user.getSampleCategories().isEmpty());

            SampleCategory category = new SampleCategory(version, UNIQUE);
            assertTrue(user.isPreferred(category));
            user.addSampleCategory(category);
            version.flush(); // ensure all DB constraints are exercised

            assertTrue(user.isPreferred(category));

            assertTrue(((Collection<SampleCategory>) user.get_Value(User.PROP_SAMPLE_CATEGORIES))
                .contains(category));

            user.set_Value(User.PROP_SAMPLE_CATEGORIES, Collections.EMPTY_SET);
            assertEquals(0, user.getSampleCategories().size());

            SampleCategory category2 = new SampleCategory(version, UNIQUE + "two");
            user.setSampleCategories(Collections.singleton(category2));
            version.flush(); // ensure all DB constraints are exercised
            assertFalse(user.isPreferred(category));
            assertEquals(1, preferences.size());

        } finally {
            version.abort();
        }
    }

    // Freezer preferences PRIV-273
    public void testSubscribe() throws ConstraintException, AccessException, SecurityException {
        MetaClass metaClass = this.model.getMetaClass(Holder.class.getName());
        assertNotNull(metaClass.getMetaRole(Holder.PROP_SUBSCRIBERS));

        WritableVersion version = this.model.getTestVersion();
        try {
            Holder holder = new Holder(version, UNIQUE);
            assertTrue(holder.getSubscribers().isEmpty());

            User user = new User(version, UNIQUE);
            holder.addSubscriber(user);
            version.flush(); // ensure all DB constraints are exercised

            assertTrue(((Collection<User>) holder.get_Value(Holder.PROP_SUBSCRIBERS)).contains(user));

            holder.set_Value(Holder.PROP_SUBSCRIBERS, Collections.EMPTY_SET);
            assertEquals(0, holder.getSubscribers().size());

            assertTrue(user.getHolders().isEmpty());

            user.addHolder(holder);
            version.flush(); // ensure all DB constraints are exercised

            assertTrue(((Collection<Holder>) user.getHolders()).contains(holder));

            user.clearHolders();
            assertEquals(0, user.getHolders().size());

        } finally {
            version.abort();
        }
    }

    // planned test
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
             * Then deprecate the old API
             * Then implement the new API more efficiently, with a special upgrader if necessary
            // the API we want
            {
                Protocol protocol = new Protocol(version, UNIQUE, type);
                ParameterDefinition setup =
                    new ParameterDefinition(version, UNIQUE + "setup", "String", protocol);
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
