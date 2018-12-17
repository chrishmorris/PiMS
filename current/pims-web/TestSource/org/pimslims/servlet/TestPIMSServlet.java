/*
 * Created on 07-Apr-2005 @author: Chris Morris
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Milestone;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.test.AbstractTestCase;

/**
 * Simple tests of common servelt
 * 
 * @version 0.1
 */
public class TestPIMSServlet extends AbstractTestCase {

    private final AbstractModel model;

    /**
     * @param methodName name to test method to run
     */
    public TestPIMSServlet(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /*
     * public void testHidden() { org.pimslims.metamodel.MetaClass metaClass =
     * model.getMetaClass("org.pimslims.model.molecule.Molecule"); assertTrue("hidden",
     * PIMSServlet.isHidden(metaClass, "molecule")); }
     */
    /**
     * @return a suite including all tests for this class
     */
    public static Test suite() {
        return new junit.framework.TestSuite(TestPIMSServlet.class);
    }

    /**
     * Runs these tests from the command line
     * 
     * @param args ignored
     */
    public static void main(final String[] args) {
        TestRunner.run(TestPIMSServlet.suite());
    }

    public void testGetCriteria() {
        this.rv = this.getRV();
        try {
            final Map parms = new HashMap();
            parms.put("name", new String[] { "" });
            parms.put("isActive", new String[] { "true" });

            final Map criteria =
                QuickSearch.getCriteria(this.rv, this.model.getMetaClass(Sample.class.getName()), parms);
            Assert.assertEquals(null, criteria.get("name"));
            Assert.assertEquals(true, criteria.get("isActive"));
        } finally {
            this.rv.abort();
        }
    }

    public void testSearchByHook() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {
            // create samples
            final Sample sample = this.create(Sample.class);
            sample.setIsActive(true);
            final RefSample refSample = this.create(RefSample.class);
            sample.setRefSample(refSample);

            final Sample sample2 = this.create(Sample.class);
            sample2.setIsActive(true);
            // create search criteria
            final Map parms = new HashMap();
            parms.put("isActive", new String[] { "true" });
            parms.put("refSample", new String[] { refSample.get_Hook() });
            final Map criteria =
                QuickSearch.getCriteria(this.wv, this.model.getMetaClass(Sample.class.getName()), parms);

            final Sample sampleFound = this.wv.findFirst(Sample.class, criteria);
            Assert.assertNotNull(sampleFound);
            Assert.assertEquals(sample.get_Hook(), sampleFound.get_Hook());

        } finally {
            this.wv.abort();
        }
    }

    public void testSearchByName() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {
            // create samples
            final Sample sample = this.create(Sample.class);
            sample.setIsActive(true);
            final RefSample refSample = this.create(RefSample.class);
            sample.setRefSample(refSample);

            final Sample sample2 = this.create(Sample.class);
            sample2.setIsActive(true);
            // create search criteria
            final Map parms = new HashMap();
            parms.put("isActive", new String[] { "true" });
            parms.put("refSample", new String[] { refSample.get_Name() });
            final Map criteria =
                QuickSearch.getCriteria(this.wv, this.model.getMetaClass(Sample.class.getName()), parms);

            final Sample sampleFound = this.wv.findFirst(Sample.class, criteria);
            Assert.assertNotNull(sampleFound);
            Assert.assertEquals(sample.get_Hook(), sampleFound.get_Hook());

        } finally {
            this.wv.abort();
        }
    }

    public static class TestServlet extends PIMSServlet {

        TestServlet(final AbstractModel model) throws ServletException {
            super();
            this.init(new MockServletConfig(model));
        }

        /**
         * Test.getServletInfo
         * 
         * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
         */
        @Override
        public String getServletInfo() {
            return "testing";
        }

    }

    public void testDispatchCustomJsp() throws ServletException, IOException {
        final MetaClass metaClass = this.model.getMetaClass(Holder.class.getName());
        final TestServlet servlet = new TestServlet(this.model);
        final Map<String, String[]> parms = new HashMap();
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        final HttpServletResponse response = new MockHttpServletResponse();
        PIMSServlet.dispatchCustomJsp(request, response, metaClass.getMetaClassName(), "view",
            servlet.getServletContext());
        Assert.assertEquals("/JSP/view/" + Holder.class.getName() + ".jsp", request.getDispatchedPath());
    }

    public void testDispatchDefaultJsp() throws ServletException, IOException {
        final MetaClass metaClass = this.model.getMetaClass(Milestone.class.getName());
        final TestServlet servlet = new TestServlet(this.model);
        final Map<String, String[]> parms = new HashMap();
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        final HttpServletResponse response = new MockHttpServletResponse();
        PIMSServlet.dispatchCustomJsp(request, response, metaClass.getMetaClassName(), "search",
            servlet.getServletContext());
        Assert.assertEquals("/JSP/search/Default.jsp", request.getDispatchedPath());
    }
}
