package org.pimslims.presentation.protocol;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.test.POJOFactory;

public class ProtocolBeanTest extends TestCase {

    private static final String UNIQUE = "pb" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    public ProtocolBeanTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    public final void testCreateBean() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, "test" + System.currentTimeMillis());
            final Protocol protocol = new Protocol(version, "test" + System.currentTimeMillis(), type);
            final ModelObjectBean bean = new ProtocolBean(protocol);
            Assert.assertEquals("Protocol", bean.getClassDisplayName());
            Assert.assertEquals(Protocol.class.getName(), bean.getClassName());
            Assert.assertEquals(protocol.get_Hook(), bean.getHook());
            Assert.assertEquals(protocol.get_Name(), bean.getName());
            Assert.assertEquals("Protocol: " + protocol.get_Name(), bean.toString());
            Assert.assertEquals(protocol.getDbId(), bean.getDbId());
            Assert.assertFalse(bean.getValues().containsKey(LabBookEntry.PROP_LASTEDITEDDATE));
            Assert.assertTrue(bean.getMayDelete());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testMayNotDelete() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, "test" + System.currentTimeMillis());
            final Protocol protocol = new Protocol(version, "test" + System.currentTimeMillis(), type);
            new Experiment(version, ProtocolBeanTest.UNIQUE, ProtocolBeanTest.NOW, ProtocolBeanTest.NOW, type)
                .setProtocol(protocol);
            final ProtocolBean bean = new ProtocolBean(protocol);
            Assert.assertEquals(1, bean.getNumberOfExperiments());
            Assert.assertFalse(bean.getMayDelete());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testInputs() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, "test" + System.currentTimeMillis());
            final Protocol protocol = new Protocol(version, "test" + System.currentTimeMillis(), type);
            final SampleCategory sc = POJOFactory.createSampleCategory(version);
            final RefInputSample ris = new RefInputSample(version, sc, protocol);
            ris.setName("input");
            final ProtocolBean bean = new ProtocolBean(protocol);
            Assert.assertEquals(1, bean.getInputSamples().size());
            final RefInputSampleBean input = bean.getInputSamples().iterator().next();
            Assert.assertEquals("input", input.getName());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testOutputs() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, "test" + System.currentTimeMillis());
            final Protocol protocol = new Protocol(version, "test" + System.currentTimeMillis(), type);
            final SampleCategory sc = POJOFactory.createSampleCategory(version);
            final RefOutputSample ros = new RefOutputSample(version, sc, protocol);
            ros.setName("output");
            final ProtocolBean bean = new ProtocolBean(protocol);
            Assert.assertEquals(1, bean.getOutputSamples().size());
            final RefOutputSampleBean output = bean.getOutputSamples().iterator().next();
            Assert.assertEquals("output", output.getName());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testParameters() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, "test" + System.currentTimeMillis());
            final Protocol protocol = new Protocol(version, "test" + System.currentTimeMillis(), type);
            final Map<String, Object> parms = new HashMap();
            parms.put(ParameterDefinition.PROP_PROTOCOL, protocol);
            parms.put(ParameterDefinition.PROP_NAME, "parm");
            parms.put(ParameterDefinition.PROP_PARAMTYPE, "Boolean");
            final ParameterDefinition pd = new ParameterDefinition(version, parms);
            pd.setDefaultValue("No");
            final ProtocolBean bean = new ProtocolBean(protocol);
            Assert.assertEquals(1, bean.getParameterDefinitions().size());
            final ModelObjectBean parm = bean.getParameterDefinitions().iterator().next();
            Assert.assertEquals("parm", parm.getName());
            Assert.assertEquals("No", parm.getValues().get(ParameterDefinition.PROP_DEFAULTVALUE));
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }
}
