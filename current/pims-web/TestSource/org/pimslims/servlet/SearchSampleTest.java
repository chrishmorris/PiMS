/**
 * pims-web org.pimslims.servlet SearchSamplesTest.java
 * 
 * @author Marc Savitsky
 * @date 31 Oct 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.presentation.sample.SampleBean;
import org.pimslims.search.Paging;
import org.pimslims.servlet.sample.SearchSample;
import org.pimslims.test.AbstractTestCase;

/**
 * DivideSample
 * 
 */
public class SearchSampleTest extends TestCase {

    private static final String UNIQUE = "ss" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    String details = "Sample Details" + System.currentTimeMillis();

    /**
     * @param name
     */
    public SearchSampleTest(final String name) {
        super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

    }

    public final void testDoGetOneInPopup() throws ServletException, IOException, ConstraintException,
        AccessException, AbortedException {
        final String hook1, hook2;
        WritableVersion version = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Sample sampleOne = new Sample(version, SearchSampleTest.UNIQUE + "one");
            hook1 = sampleOne.get_Hook();
            sampleOne.setIsActive(true);
            final Sample sampleTwo = new Sample(version, SearchSampleTest.UNIQUE + "two");
            hook2 = sampleTwo.get_Hook();
            sampleTwo.setIsActive(true);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        // test
        final SearchSample servlet = new SearchSample();
        servlet.init(new MockServletConfig(AbstractTestCase.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put("SUBMIT", new String[] { "Search" });
        parms.put("isInPopup", new String[] { "true" });
        parms.put(AbstractSample.PROP_NAME, new String[] { SearchSampleTest.UNIQUE + "one" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("/" + Sample.class.getName());
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals(new Integer(1), request.getAttribute("resultSize"));

        // clean up
        version = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ModelObject mo1 = version.get(hook1);
            mo1.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        version = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ModelObject mo2 = version.get(hook2);
            mo2.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public final void TODOtestDoGetNoGroupOutputs() throws ServletException, IOException,
        ConstraintException, AccessException, AbortedException {
        final String sampleHook, groupHook, categoryHook;
        WritableVersion version = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Sample sample = new Sample(version, SearchSampleTest.UNIQUE);
            sample.setIsActive(true);
            sampleHook = sample.get_Hook();
            final SampleCategory category = new SampleCategory(version, SearchSampleTest.UNIQUE);
            sample.addSampleCategory(category);
            categoryHook = category.get_Hook();
            final ExperimentGroup group =
                new ExperimentGroup(version, SearchSampleTest.UNIQUE, SearchSampleTest.UNIQUE);
            groupHook = group.get_Hook();
            final ExperimentType type = new ExperimentType(version, SearchSampleTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, SearchSampleTest.UNIQUE, SearchSampleTest.NOW, SearchSampleTest.NOW,
                    type);
            new OutputSample(version, experiment).setSample(sample);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        // test
        final SearchSample servlet = new SearchSample();
        servlet.init(new MockServletConfig(AbstractTestCase.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put("SUBMIT", new String[] { "Search" });
        parms.put("isInPopup", new String[] { "true" });
        parms.put(AbstractSample.PROP_SAMPLECATEGORIES, new String[] { categoryHook });
        parms.put("experimentGroup", new String[] { groupHook });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("/" + Sample.class.getName());
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals(new Integer(0), request.getAttribute("resultSize"));

        // clean up
        version = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Sample sample = (Sample) version.get(sampleHook);
            final ExperimentType type = sample.getOutputSample().getExperiment().getExperimentType();
            final Set<SampleCategory> categories = sample.getSampleCategories();
            sample.delete();
            version.delete(categories);
            final ExperimentGroup group = (ExperimentGroup) version.get(groupHook);
            group.delete();
            version.delete(type.getExperiments());
            type.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public void testGroupOutputs() throws ConstraintException, ServletException, IOException {
        final MetaClass metaClass = AbstractTestCase.model.getMetaClass(Sample.class.getName());
        final WritableVersion version = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Sample sample = new Sample(version, SearchSampleTest.UNIQUE + "s");

            final ExperimentGroup group = new ExperimentGroup(version, SearchSampleTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, SearchSampleTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, SearchSampleTest.UNIQUE, SearchSampleTest.NOW, SearchSampleTest.NOW,
                    type);
            new OutputSample(version, experiment).setSample(sample);
            group.addExperiment(experiment);
            final Map criteria = new HashMap();
            criteria.put("experimentGroup", group.get_Hook());
            criteria.put("name", sample.getName());
            SearchSample.updateCriteriaForInputsForGroup(version, metaClass, criteria);

            //final Paging paging = QuickSearch.getPaging(request.getParameterMap(), sampleMetaClass);
            final int count = QuickSearch.getCountOfResults(version, metaClass, criteria, null);
            Assert.assertEquals(0, count);
            //final List<ModelObjectBean> beans =                 SearchSample.search(version, metaClass, paging, criteria, search_all);
        } finally {

            version.abort();

        }
    }

    public void testSearch() throws ConstraintException, ServletException, IOException {
        final MetaClass metaClass = AbstractTestCase.model.getMetaClass(Sample.class.getName());
        final WritableVersion version = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Sample sample = new Sample(version, SearchSampleTest.UNIQUE + "s");

            final ExperimentGroup group = new ExperimentGroup(version, SearchSampleTest.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, SearchSampleTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, SearchSampleTest.UNIQUE, SearchSampleTest.NOW, SearchSampleTest.NOW,
                    type);
            // not for this test new OutputSample(version, experiment).setSample(sample);
            group.addExperiment(experiment);
            final Map criteria = new HashMap();
            criteria.put("experimentGroup", group.get_Hook());
            criteria.put("name", sample.getName());
            SearchSample.updateCriteriaForInputsForGroup(version, metaClass, criteria);

            final Paging paging = new Paging(0, 2);
            final Collection<ModelObjectBean> beans =
                QuickSearch.search(version, metaClass, paging, criteria, "");
            Assert.assertEquals(1, beans.size());
        } finally {

            version.abort();

        }
    }

    public final void testSampleByCategory() throws ServletException, IOException, AbortedException,
        ConstraintException, AccessException {
        String sample1Hook = null;
        String sample2Hook = null;
        WritableVersion version = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final SampleCategory category = new SampleCategory(version, SearchSampleTest.UNIQUE + "cat");
            final Sample sample = new Sample(version, SearchSampleTest.UNIQUE + "one");
            sample.addSampleCategory(category);
            sample1Hook = sample.get_Hook();
            final Sample sample2 = new Sample(version, SearchSampleTest.UNIQUE + "two");
            sample2.addSampleCategory(category);
            sample2Hook = sample2.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        //this.model.getMetaClass(Sample.class.getName());
        final SearchSample servlet = new SearchSample();
        servlet.init(new MockServletConfig(AbstractTestCase.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put(AbstractSample.PROP_SAMPLECATEGORIES, new String[] { SearchSampleTest.UNIQUE + "cat" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNull(request.getAttribute("noSearch"));
        Assert.assertEquals(new Integer(2), request.getAttribute("resultSize"));
        Assert.assertNotNull(request.getAttribute("beans"));
        final Collection beans = (Collection) request.getAttribute("beans");
        Assert.assertEquals(2, beans.size());
        final ModelObjectBean bean = (ModelObjectBean) beans.iterator().next();
        Assert.assertTrue(bean instanceof SampleBean);

        // clean up
        version = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Sample sample1 = (Sample) version.get(sample1Hook);
            final Set<SampleCategory> categories = sample1.getSampleCategories();
            sample1.delete();
            final Sample sample2 = (Sample) version.get(sample2Hook);
            sample2.delete();
            version.delete(categories);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
