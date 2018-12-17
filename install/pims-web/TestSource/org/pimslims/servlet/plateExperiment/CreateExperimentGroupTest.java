/**
 * current-pims-web org.pimslims.servlet.plateExperiment CreateExperimentGroupTest.java
 * 
 * @author cm65
 * @date 20 Mar 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.servlet.plateExperiment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
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
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.servlet.PIMSServlet;

/**
 * CreateExperimentGroupTest
 * 
 */
public class CreateExperimentGroupTest extends TestCase {

    private static final String UNIQUE = "ceg" + System.currentTimeMillis();

    /**
     * 
     */
    private static final String DETAILS = "details" + CreateExperimentGroupTest.UNIQUE;

    /**
     * 
     */
    private static final String GROUP_NAME = "group" + CreateExperimentGroupTest.UNIQUE;

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    private String protocolHook;

    /**
     * objects to delete
     */
    private final List<String> hooks = new ArrayList<String>();

    private String projectHook;

    private String categoryHook;

    /**
     * @param name
     */
    public CreateExperimentGroupTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {

            final ExperimentType type = new ExperimentType(version, CreateExperimentGroupTest.UNIQUE);
            final Protocol protocol = new Protocol(version, CreateExperimentGroupTest.UNIQUE, type);
            this.protocolHook = protocol.get_Hook();
            final ModelObject owner = new LabNotebook(version, CreateExperimentGroupTest.UNIQUE);
            this.projectHook = owner.get_Hook();
            final SampleCategory category = new SampleCategory(version, CreateExperimentGroupTest.UNIQUE);
            this.categoryHook = category.get_Hook();
            new RefOutputSample(version, category, protocol);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        // delete them all
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Protocol protocol = version.get(this.protocolHook);
            version.delete(protocol.getRefInputSamples());
            final ExperimentType type = protocol.getExperimentType();
            final Collection<Experiment> experiments =
                version.findAll(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, type);
            for (final Iterator iterator = experiments.iterator(); iterator.hasNext();) {
                final Experiment experiment = (Experiment) iterator.next();
                final Set<OutputSample> oss = experiment.getOutputSamples();
                for (final Iterator iterator2 = oss.iterator(); iterator2.hasNext();) {
                    final OutputSample os = (OutputSample) iterator2.next();
                    os.getSample().delete();
                }
                final ExperimentGroup group = experiment.getExperimentGroup();
                if (null != group) {
                    group.delete();
                }
                experiment.delete();
            }
            final Set<RefOutputSample> ross = protocol.getRefOutputSamples();
            for (final Iterator iterator = ross.iterator(); iterator.hasNext();) {
                final RefOutputSample ros = (RefOutputSample) iterator.next();
                final SampleCategory category = ros.getSampleCategory();
                ros.delete();
                category.delete();
            }
            protocol.delete();
            for (final ListIterator<String> iterator = this.hooks.listIterator(); iterator.hasNext();) {
                final String hook = iterator.next();
                final ModelObject modelObject = version.get(hook);
                modelObject.delete();
                iterator.remove();
            }
            version.flush();
            type.delete();
            final LabNotebook project = (LabNotebook) version.get(this.projectHook);
            version.delete(version.findAll(LabBookEntry.class, LabBookEntry.PROP_ACCESS, project));
            project.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.plateExperiment.CreateExperimentGroup#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     * 
     * @throws ServletException
     * @throws IOException
     */
    public final void testNoCount() throws ServletException, IOException {
        final CreateExperimentGroup servlet = new CreateExperimentGroup();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap();
        parms.put("protocol", new String[] { this.protocolHook });
        parms.put("groupName", new String[] { CreateExperimentGroupTest.GROUP_NAME });

        final MockHttpServletRequest request =
            new MockHttpServletRequest("post", new MockHttpSession(Access.ADMINISTRATOR), parms);
        final MockHttpServletResponse response = new MockHttpServletResponse(true);
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }

    // e.g. pims/View/org.pimslims.model.experiment.ExperimentGroup:364367
    private final Pattern VIEW_URL = Pattern.compile("^/pims/View/(" + ExperimentGroup.class.getName()
        + ":\\d+)$");

    public final void testDoPost() throws ServletException, IOException, AccessException,
        ConstraintException, AbortedException {
        final CreateExperimentGroup servlet = new CreateExperimentGroup();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap();
        parms.put("protocol", new String[] { this.protocolHook });
        parms.put(PIMSServlet.LAB_NOTEBOOK_ID, new String[] { this.projectHook });
        parms.put("groupName", new String[] { CreateExperimentGroupTest.GROUP_NAME });
        parms.put("numExperiments", new String[] { "1" });

        final MockHttpServletRequest request =
            new MockHttpServletRequest("post", new MockHttpSession(Access.ADMINISTRATOR), parms);
        final MockHttpServletResponse response = new MockHttpServletResponse(true);
        servlet.doPost(request, response);

        // now check
        Assert.assertEquals(HttpServletResponse.SC_SEE_OTHER, response.getStatus());
        final String url = response.getHeader("Location");
        final Matcher m = this.VIEW_URL.matcher(url);
        Assert.assertTrue(url, m.matches());
        final String hook = m.group(1);

        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ExperimentGroup group = version.get(hook);
            Assert.assertNotNull(hook, group);
            Assert.assertEquals(CreateExperimentGroupTest.GROUP_NAME, group.getName());
            Assert.assertEquals(1, group.getExperiments().size());

            // tidy up
            group.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public final void testSaveDetails() throws ServletException, IOException, AccessException,
        ConstraintException, AbortedException {
        final CreateExperimentGroup servlet = new CreateExperimentGroup();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap();
        parms.put("protocol", new String[] { this.protocolHook });
        parms.put(PIMSServlet.LAB_NOTEBOOK_ID, new String[] { this.projectHook });
        parms.put("groupName", new String[] { CreateExperimentGroupTest.GROUP_NAME });
        parms.put("numExperiments", new String[] { "2" });
        parms.put("details", new String[] { CreateExperimentGroupTest.DETAILS });

        final MockHttpServletRequest request =
            new MockHttpServletRequest("post", new MockHttpSession(Access.ADMINISTRATOR), parms);
        final MockHttpServletResponse response = new MockHttpServletResponse(true);
        servlet.doPost(request, response);

        // now check
        Assert.assertEquals(HttpServletResponse.SC_SEE_OTHER, response.getStatus());
        final String url = response.getHeader("Location");
        final Matcher m = this.VIEW_URL.matcher(url);
        Assert.assertTrue(url, m.matches());
        final String hook = m.group(1);

        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ExperimentGroup group = version.get(hook);
            Assert.assertNotNull(hook, group);
            Assert.assertEquals(CreateExperimentGroupTest.GROUP_NAME, group.getName());
            Assert.assertEquals(CreateExperimentGroupTest.DETAILS, group.getDetails());
            Assert.assertEquals(2, group.getExperiments().size());

            // tidy up
            group.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /* testNextExperimentGroup
     * e.g.
     * groupName=myg2&numExperiments=12&protocol=org.pimslims.model.protocol.Protocol%3A156192&projectHook=org.pimslims.model.core.AccessObject%3A14603
     * &refInputSample=org.pimslims.model.protocol.RefInputSample%3A156196
     * &inputGroup=org.pimslims.model.experiment.ExperimentGroup%3A1708786
     * &startDate=26%2F03%2F2010&endDate=26%2F03%2F2010&details=%5Bnone%5D&
     * _tab=expgrp_basicdetails
     *  */

    public final void testNextExperimentGroup() throws ServletException, IOException {
        String risHook = null;
        String fromGroupHook = null;
        String sampleHook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Protocol protocol = version.get(this.protocolHook);
            final SampleCategory category =
                ((RefOutputSample) protocol.findFirst(Protocol.PROP_REFOUTPUTSAMPLES)).getSampleCategory();
            risHook = new RefInputSample(version, category, protocol).get_Hook();
            final ExperimentGroup fromGroup =
                new ExperimentGroup(version, CreateExperimentGroupTest.UNIQUE + "from",
                    CreateExperimentGroupTest.UNIQUE + "from");
            fromGroupHook = fromGroup.get_Hook();
            final Experiment from =
                new Experiment(version, CreateExperimentGroupTest.UNIQUE, protocol.getCreationDate(),
                    protocol.getCreationDate(), protocol.getExperimentType());
            final Sample sample = new Sample(version, CreateExperimentGroupTest.UNIQUE);
            new OutputSample(version, from).setSample(sample);
            sampleHook = sample.get_Hook();
            fromGroup.addExperiment(from);
            version.commit();
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AbortedException e) {
            Assert.fail(e.getMessage());
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final CreateExperimentGroup servlet = new CreateExperimentGroup();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap();
        parms.put("protocol", new String[] { this.protocolHook });
        parms.put("refInputSample", new String[] { risHook });
        parms.put("inputGroup", new String[] { fromGroupHook });
        parms.put(PIMSServlet.LAB_NOTEBOOK_ID, new String[] { this.projectHook });
        parms.put("groupName", new String[] { CreateExperimentGroupTest.GROUP_NAME + "neg" });
        parms.put("numExperiments", new String[] { "1" });

        final MockHttpServletRequest request =
            new MockHttpServletRequest("post", new MockHttpSession(Access.ADMINISTRATOR), parms);
        final MockHttpServletResponse response = new MockHttpServletResponse(true);
        servlet.doPost(request, response);

        // now check
        Assert.assertEquals(HttpServletResponse.SC_SEE_OTHER, response.getStatus());
        final String url = response.getHeader("Location");
        final Matcher m = this.VIEW_URL.matcher(url);
        Assert.assertTrue(url, m.matches());
        final String hook = m.group(1);

        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ExperimentGroup group = version.get(hook);
            Assert.assertNotNull(hook, group);
            Assert.assertEquals(CreateExperimentGroupTest.GROUP_NAME + "neg", group.getName());
            Assert.assertEquals(1, group.getExperiments().size());

            // check joined up
            final Experiment experiment = group.getExperiments().iterator().next();
            Assert.assertEquals(1, experiment.getInputSamples().size());
            final InputSample is = experiment.getInputSamples().iterator().next();
            Assert.assertNotNull(is.getSample());
            Assert.assertEquals(sampleHook, is.getSample().get_Hook());

            // tidy up
            is.getSample().delete();
            experiment.delete();
            group.delete();
            final ExperimentGroup fromGroup = (ExperimentGroup) version.get(fromGroupHook);
            final Set<Experiment> experiments = fromGroup.getExperiments();
            for (final Iterator iterator = experiments.iterator(); iterator.hasNext();) {
                final Experiment exp = (Experiment) iterator.next();
                final Set<OutputSample> oss = experiment.getOutputSamples();
                for (final Iterator iterator2 = oss.iterator(); iterator2.hasNext();) {
                    final OutputSample os = (OutputSample) iterator2.next();
                    Assert.assertNotNull(os.getSample());
                    os.getSample().delete();
                }
                exp.delete();
            }
            fromGroup.delete();
            version.commit();
        } catch (final AbortedException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public final void testTwoOutputs() throws ServletException, IOException, AccessException,
        ConstraintException, AbortedException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Protocol protocol = version.get(this.protocolHook);
            final SampleCategory category =
                new SampleCategory(version, CreateExperimentGroupTest.UNIQUE + "b");
            new RefOutputSample(version, category, protocol).setName("two");
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final CreateExperimentGroup servlet = new CreateExperimentGroup();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap();
        parms.put("protocol", new String[] { this.protocolHook });
        parms.put(PIMSServlet.LAB_NOTEBOOK_ID, new String[] { this.projectHook });
        parms.put("groupName", new String[] { CreateExperimentGroupTest.GROUP_NAME });
        parms.put("numExperiments", new String[] { "1" });

        final MockHttpServletRequest request =
            new MockHttpServletRequest("post", new MockHttpSession(Access.ADMINISTRATOR), parms);
        final MockHttpServletResponse response = new MockHttpServletResponse(true);
        servlet.doPost(request, response);

        // now check
        Assert.assertEquals(HttpServletResponse.SC_SEE_OTHER, response.getStatus());
        final String url = response.getHeader("Location");
        final Matcher m = this.VIEW_URL.matcher(url);
        Assert.assertTrue(url, m.matches());
        final String hook = m.group(1);

        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ExperimentGroup group = version.get(hook);
            Assert.assertNotNull(hook, group);
            Assert.assertEquals(CreateExperimentGroupTest.GROUP_NAME, group.getName());
            Assert.assertEquals(1, group.getExperiments().size());
            // tidy up
            group.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();

            }
        }
    }

    public void testCreateExperimentGroup() throws AccessException, ConstraintException,
        UnsupportedEncodingException, IOException {
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Protocol protocol = (Protocol) version.get(this.protocolHook);
            final Collection<ExperimentType> experimentTypes =
                Collections.singleton(protocol.getExperimentType());
            final LabNotebook access = (LabNotebook) version.get(this.projectHook);
            final String csvAsString =
                "GroupId,Experiment,Construct\n" + CreateExperimentGroupTest.UNIQUE + ",1,";
            final ExperimentGroup group =
                CreateExperimentGroup.createGroup(csvAsString, CreateExperimentGroupTest.NOW,
                    CreateExperimentGroupTest.NOW, 1, version, protocol, experimentTypes, access,
                    CreateExperimentGroupTest.UNIQUE, "", null, null);
            Assert.assertNotNull(group);
            Assert.assertEquals(1, group.getExperiments().size());
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
