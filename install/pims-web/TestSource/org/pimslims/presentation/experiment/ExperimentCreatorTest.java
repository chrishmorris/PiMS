package org.pimslims.presentation.experiment;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.people.Person;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

@Deprecated
// obsolete
public class ExperimentCreatorTest extends AbstractTestCase {

    private static final String UNIQUE = "ec" + System.currentTimeMillis();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ExperimentCreatorTest.class);
    }

    private AbstractModel model;

    public ExperimentCreatorTest(final String methodName) {
        super(methodName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.model = ModelImpl.getModel();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSave() {
        final ExperimentCreator.ExperimentCreationForm form = new ExperimentCreator.ExperimentCreationForm();
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, System.currentTimeMillis() + "test");
            form.setExperimentTypeHook(type.get_Hook());
            final Protocol protocol = new Protocol(version, "test" + System.currentTimeMillis(), type);
            form.setProtocolHook(protocol.get_Hook());
            final ExperimentCreator creator = new ExperimentCreator(form, version);
            final Experiment experiment = creator.save();
            Assert.assertNotNull(experiment);
            Assert.assertEquals("To_be_run", experiment.getStatus());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    public void testSaveWithUser() throws AccessException, ConstraintException, AbortedException {
        final ExperimentCreator.ExperimentCreationForm form = new ExperimentCreator.ExperimentCreationForm();
        this.wv = this.getWV();
        String userName;
        try {
            final User user = this.create(User.class);
            final Person person = this.create(Person.class);
            user.setPerson(person);
            userName = user.getName();
            this.wv.commit();
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
        final WritableVersion version = this.model.getWritableVersion(userName);
        try {
            final ExperimentType type = new ExperimentType(version, System.currentTimeMillis() + "test");
            form.setExperimentTypeHook(type.get_Hook());
            final Protocol protocol = new Protocol(version, "test" + System.currentTimeMillis(), type);
            form.setProtocolHook(protocol.get_Hook());
            final ExperimentCreator creator = new ExperimentCreator(form, version);
            final Experiment experiment = creator.save();
            Assert.assertNotNull(experiment);
            Assert.assertEquals("To_be_run", experiment.getStatus());
            Assert.assertEquals(userName, experiment.getCreator().getName());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    public void testSetProtocol() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, System.currentTimeMillis() + "test");
            final Protocol protocol = new Protocol(version, "test" + System.currentTimeMillis(), type);
            final SampleCategory sc = POJOFactory.createSampleCategory(version);
            new RefInputSample(version, sc, protocol);
            new RefOutputSample(version, sc, protocol);
            final Map<String, Object> attr = new HashMap<String, Object>();
            attr.put(ParameterDefinition.PROP_PROTOCOL, protocol);
            attr.put(ParameterDefinition.PROP_PARAMTYPE, "String");
            attr.put(ParameterDefinition.PROP_NAME, "testPD" + System.currentTimeMillis());
            new ParameterDefinition(version, attr);

            final Calendar now = java.util.Calendar.getInstance();
            final Experiment experiment =
                new Experiment(version, "test" + System.currentTimeMillis(), now, now, type);
            ExperimentCreator.setProtocol(version, protocol, experiment);
            Assert.assertEquals(protocol, experiment.getProtocol());
            Assert.assertEquals(protocol.getRefInputSamples().size(), experiment.getInputSamples().size());
            Assert.assertEquals(protocol.getRefOutputSamples().size(), experiment.getOutputSamples().size());
            Assert.assertEquals(protocol.getParameterDefinitions().size(), experiment.getParameters().size());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    public void testSampleHasUniqueName() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, System.currentTimeMillis() + "test");
            final Protocol protocol = new Protocol(version, "test" + System.currentTimeMillis(), type);
            final SampleCategory sc = POJOFactory.createSampleCategory(version);
            new RefOutputSample(version, sc, protocol);

            final Calendar now = java.util.Calendar.getInstance();
            final Experiment experiment =
                new Experiment(version, "test" + System.currentTimeMillis(), now, now, type);
            ExperimentCreator.setProtocol(version, protocol, experiment);
            final Experiment experiment2 =
                new Experiment(version, "testTwo" + System.currentTimeMillis(), now, now, type);
            ExperimentCreator.setProtocol(version, protocol, experiment2);
            version.flush(); // reveals constraint violations
            final Sample output = experiment.getOutputSamples().iterator().next().getSample();
            Assert.assertEquals(experiment.getName(), output.getName());

        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    public void testSaveWithInput() throws AccessException {
        final ExperimentCreator.ExperimentCreationForm form = new ExperimentCreator.ExperimentCreationForm();
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, "tst" + System.currentTimeMillis());
            form.setExperimentTypeHook(type.get_Hook());
            final Protocol protocol = new Protocol(version, "test" + System.currentTimeMillis(), type);
            final SampleCategory sc = POJOFactory.createSampleCategory(version);
            final RefInputSample ris = new RefInputSample(version, sc, protocol);
            ris.setName("input");

            form.setProtocolHook(protocol.get_Hook());
            final ExperimentCreator creator = new ExperimentCreator(form, version);
            final Set<SampleCategory> categories = Collections.emptySet();
            final Sample sample = new Sample(version, ExperimentCreatorTest.UNIQUE + "T00013.full.N13-pcr");
            sample.setSampleCategories(categories);
            form.set_Input("input:" + sample.get_Hook());

            final Experiment experiment = creator.save();

            final Collection<InputSample> inputSamples = experiment.getInputSamples();
            Assert.assertEquals(1, inputSamples.size());
            final InputSample input = inputSamples.iterator().next();
            Assert.assertEquals(sample, input.getSample());

            // test names
            Assert.assertEquals(ExperimentCreatorTest.UNIQUE + "T00013.full.N13-tst administrator 1",
                experiment.getName());

        } catch (final ConstraintException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

    public void testSaveWithInputInOtherProject() throws ConstraintException, AccessException {
        final ExperimentCreator.ExperimentCreationForm form = new ExperimentCreator.ExperimentCreationForm();
        final WritableVersion version = this.model.getTestVersion();
        try {
            final LabNotebook owner = new LabNotebook(version, "owner" + System.currentTimeMillis());
            final ExperimentType type = new ExperimentType(version, System.currentTimeMillis() + "test");
            form.setExperimentTypeHook(type.get_Hook());
            final Protocol protocol = new Protocol(version, "test" + System.currentTimeMillis(), type);
            final SampleCategory sc = POJOFactory.createSampleCategory(version);
            final RefInputSample ris = new RefInputSample(version, sc, protocol);
            ris.setName("input");

            form.setProtocolHook(protocol.get_Hook());
            final ExperimentCreator creator = new ExperimentCreator(form, version);
            final Set<SampleCategory> categories = Collections.emptySet();
            final Sample sample = new Sample(version, "sample");
            sample.setAccess(owner);
            sample.setSampleCategories(categories);
            form.set_Input("input:" + sample.get_Hook());

            final Experiment experiment = creator.save();

            Assert.assertEquals(owner, experiment.getAccess());

        } finally {
            version.abort();
        }
    }

    public void testSaveWithRO() {
        final ExperimentCreator.ExperimentCreationForm form = new ExperimentCreator.ExperimentCreationForm();
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ResearchObjective ro =
                new ResearchObjective(version, "ro" + System.currentTimeMillis(), "test"
                    + System.currentTimeMillis());
            form.setExperimentBlueprintHook(ro.get_Hook());
            final ExperimentType type =
                new ExperimentType(version, "ety" + System.currentTimeMillis() + "test");
            form.setExperimentTypeHook(type.get_Hook());
            final Protocol protocol = new Protocol(version, "test" + System.currentTimeMillis(), type);
            form.setProtocolHook(protocol.get_Hook());
            final ExperimentCreator creator = new ExperimentCreator(form, version);
            final Experiment experiment = creator.save();
            Assert.assertEquals(ro, experiment.getProject());
            Assert.assertEquals(ro.getName() + "-ety administrator 1", experiment.getName());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            version.abort();
        }
    }

}
