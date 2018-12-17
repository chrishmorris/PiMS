/**
 * V4_3-web org.pimslims.servlet.experiment CreateExperimentTest.java
 * 
 * @author cm65
 * @date 26 Jun 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.experiment;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.molecule.MoleculeFeature;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.InstrumentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.reference.WorkflowItem;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.servlet.Create;
import org.pimslims.servlet.CreateTest;

/**
 * CreateExperimentTest
 * 
 */
public class CreateExperimentTest extends TestCase {

    private final AbstractModel model;

    public CreateExperimentTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    public void testFindProtocolsForInstrument() throws ConstraintException {
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ExperimentType experimentType =
                new ExperimentType(version, CreateExperimentTest.UNIQUE + "c");
            final Protocol protocol = new Protocol(version, CreateExperimentTest.UNIQUE, experimentType);
            final InstrumentType type = new InstrumentType(version, CreateExperimentTest.UNIQUE);
            protocol.setInstrumentType(type);
            final Instrument instrument = new Instrument(version, CreateExperimentTest.UNIQUE);
            instrument.setInstrumentTypes(Collections.singleton(type));

            final Collection<Protocol> protocols = CreateExperiment.getProtocols(type);
            Assert.assertEquals(1, protocols.size());
            Assert.assertTrue(protocols.contains(protocol));
        } finally {
            version.abort();
        }

    }

    public final void testDoGetProject() throws ServletException, IOException {
        final CreateExperiment servlet = new CreateExperiment();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put(Experiment.PROP_PROJECT, new String[] { "" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("/" + Organisation.class.getName());
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        final List<String> owners = (List<String>) request.getAttribute("owners");
        Assert.assertNotNull(owners);
        Assert.assertTrue(owners.contains(Access.REFERENCE));

        Assert.assertNotNull(request.getAttribute("metaclass"));
        Assert.assertNull(request.getAttribute("rolename"));
        Assert.assertNull(request.getAttribute("pathinfo"));

        final Collection reqRoles = (Collection) request.getAttribute("reqRoles");
        Assert.assertEquals(1, reqRoles.size());
        final Map messages = (Map) request.getAttribute("errorMessages");
        Assert.assertEquals(2, messages.size());
        Assert.assertEquals(0, ((Collection) messages.get("missedErrorFields")).size());
    }

    public final void testDoGetSample() throws ServletException, IOException {
        final CreateExperiment servlet = new CreateExperiment();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put(CreateExperiment.SAMPLE, new String[] { "" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("/" + Organisation.class.getName());
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        final List<String> owners = (List<String>) request.getAttribute("owners");
        Assert.assertNotNull(owners);
        Assert.assertTrue(owners.contains(Access.REFERENCE));

        Assert.assertNotNull(request.getAttribute("metaclass"));
        Assert.assertNull(request.getAttribute("rolename"));
        Assert.assertNull(request.getAttribute("pathinfo"));

        final Collection reqRoles = (Collection) request.getAttribute("reqRoles");
        Assert.assertEquals(1, reqRoles.size());
        final Map messages = (Map) request.getAttribute("errorMessages");
        Assert.assertEquals(2, messages.size());
        Assert.assertEquals(0, ((Collection) messages.get("missedErrorFields")).size());
    }

    public final void testDoGetReqRoleMissing() throws ServletException, IOException {
        final CreateExperiment servlet = new CreateExperiment();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setPathInfo("/" + WorkflowItem.class.getName());
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        final Collection reqRoles = (Collection) request.getAttribute("reqRoles");
        Assert.assertEquals(1, reqRoles.size());

        final Map messages = (Map) request.getAttribute("errorMessages");
        Assert.assertEquals(2, messages.size());
        Assert.assertEquals(0, ((Collection) messages.get("missedErrorFields")).size());
        Assert.assertEquals("experimentType", messages.get("notProvided"));
    }

    public final void testDoGetReqRoleSupplied() throws ServletException, IOException, AbortedException,
        ConstraintException, AccessException {
        // set up
        String hook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            hook = new ExperimentType(version, CreateExperimentTest.UNIQUE + "c").get_Hook();
        } finally {
            version.commit();
        }
        final CreateExperiment servlet = new CreateExperiment();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put(Experiment.PROP_EXPERIMENTTYPE, new String[] { hook });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("/" + MoleculeFeature.class.getName());
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        final Collection reqRoles = (Collection) request.getAttribute("reqRoles");
        Assert.assertEquals(1, reqRoles.size());

        final Map messages = (Map) request.getAttribute("errorMessages");
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals(0, ((Collection) messages.get("missedErrorFields")).size());

        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ModelObject modelObject = version.get(hook);
            modelObject.delete();
        } finally {
            version.commit();
        }
    }

    public static final Pattern VIEW = Pattern.compile(".*/View/(.*)");

    private static final String UNIQUE = "c" + System.currentTimeMillis();

    public final void testDoPost() throws ServletException, IOException, AccessException,
        ConstraintException, AbortedException {
        String protocolHook = null;
        String projectHook = null;
        String sampleHook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ExperimentType type = new ExperimentType(version, CreateExperimentTest.UNIQUE + "c");
            final Protocol protocol = new Protocol(version, CreateExperimentTest.UNIQUE, type);
            final SampleCategory category = new SampleCategory(version, CreateExperimentTest.UNIQUE);
            new RefInputSample(version, category, protocol);
            protocolHook = protocol.get_Hook();
            projectHook =
                new ResearchObjective(version, CreateExperimentTest.UNIQUE, CreateExperimentTest.UNIQUE)
                    .get_Hook();
            final Sample sample = new Sample(version, CreateExperimentTest.UNIQUE);
            sample.addSampleCategory(category);
            sampleHook = sample.get_Hook();
        } finally {
            version.commit();
        }

        final CreateExperiment servlet = new CreateExperiment();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put(Create.PARM_METACLASSNAME, new String[] { Experiment.class.getName() });
        parms.put(Experiment.class.getName() + ":" + Experiment.PROP_PROJECT, new String[] { projectHook });
        parms.put(CreateExperiment.SAMPLE, new String[] { sampleHook });
        parms.put(Experiment.class.getName() + ":" + Experiment.PROP_NAME,
            new String[] { CreateExperimentTest.UNIQUE });
        parms
            .put(Experiment.class.getName() + ":" + Experiment.PROP_STARTDATE, new String[] { "26/06/2012" });
        parms.put(Experiment.class.getName() + ":" + Experiment.PROP_ENDDATE, new String[] { "26/06/2012" });

        parms.put(Experiment.class.getName() + ":" + Experiment.PROP_PROTOCOL, new String[] { protocolHook });

        parms.put("_OWNER", new String[] { Access.REFERENCE });
        final MockHttpServletRequest request = new MockHttpServletRequest("post", session, parms);
        request.setPathInfo("/" + Organisation.class.getName());
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_SEE_OTHER, response.getStatus());
        final String url = response.getHeader("Location");
        final Matcher m = CreateTest.VIEW.matcher(url);
        Assert.assertTrue(url, m.matches());
        final String objectHook = m.group(1);

        // test and tidy up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Sample sample = version.get(sampleHook);
            final Experiment experiment = version.get(objectHook);
            Assert.assertEquals(CreateExperimentTest.UNIQUE, experiment.getName());
            Assert.assertEquals(Access.REFERENCE, experiment.get_Owner());
            Assert.assertNotNull(experiment.getProject());
            Assert.assertEquals(1, experiment.getInputSamples().size());
            final InputSample is = experiment.getInputSamples().iterator().next();
            Assert.assertEquals(sample, is.getSample());

            // tidy up
            experiment.delete();
            final Protocol protocol = version.get(protocolHook);
            final ExperimentType experimentType = protocol.getExperimentType();
            protocol.delete();
            experimentType.delete();
            version.get(projectHook).delete();
            version.delete(sample.getSampleCategories());
            sample.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }
    // TODO test POST sample

}
