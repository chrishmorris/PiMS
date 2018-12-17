/**
 * current-pims-web org.pimslims.servlet SearchTest.java
 * 
 * @author cm65
 * @date 27 Mar 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.presentation.vector.RefSampleBean;
import org.pimslims.search.Paging;
import org.pimslims.search.Paging.Order;

/**
 * SearchTest
 * 
 */
public class QuickSearchTest extends TestCase {

    private static final String UNIQUE = "Search" + System.currentTimeMillis();

    private static final String NAME = "n" + QuickSearchTest.UNIQUE;

    private final AbstractModel model;

    /**
     * @param name
     */
    public QuickSearchTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.Search#getResults(org.pimslims.dao.ReadableVersion, org.pimslims.metamodel.MetaClass, java.util.Map, int, int)}
     * . zzz
     * 
     * @throws IOException
     * @throws ServletException
     */
    public final void testGetNoResults() throws ServletException, IOException {
        final MetaClass metaClass = this.model.getMetaClass(Organisation.class.getName());
        final Map<String, String[]> parms = new HashMap();
        parms.put(Organisation.PROP_NAME, new String[] { "nonesuch" });
        final ReadableVersion version = this.model.getReadableVersion(Access.ADMINISTRATOR);
        try {
            final Collection<ModelObjectBean> results =
                QuickSearch.search(version, metaClass, new Paging(0, 20),
                    QuickSearch.getOnlyCriteria(version, metaClass, parms), null);
            Assert.assertEquals(0, results.size());
        } finally {
            version.abort();
        }
    }

    public final void testSearchAll() throws ServletException, IOException, ConstraintException {
        final MetaClass metaClass = this.model.getMetaClass(Molecule.class.getName());
        final Map<String, String[]> parms = new HashMap();
        parms.put("search_all", new String[] { QuickSearchTest.NAME });
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            new Molecule(version, "protein", QuickSearchTest.NAME);
            final Collection<ModelObjectBean> results =
                QuickSearch.search(version, metaClass, new Paging(0, 20),
                    QuickSearch.getOnlyCriteria(version, metaClass, parms), QuickSearchTest.NAME);
            Assert.assertEquals(1, results.size());
            // at this point, we could also test that it is a specific bean.
        } finally {
            version.abort();
        }
    }

    public final void testSearchAllROE() throws ServletException, IOException, ConstraintException {
        final MetaClass metaClass = this.model.getMetaClass(ResearchObjectiveElement.class.getName());
        final Map<String, String[]> parms = new HashMap();
        parms.put("isInModelWindow", new String[] { "true" });
        parms.put("selectMultiple", new String[] { "true" });
        parms.put("componentType", new String[] { "target" });
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final String search_all = null;
            final Map criteria = QuickSearch.getOnlyCriteria(version, metaClass, parms);
            final Paging paging = QuickSearch.getPaging(Collections.EMPTY_MAP, metaClass);
            QuickSearch.search(version, metaClass, paging, criteria, search_all);
        } finally {
            version.abort();
        }
    }

    public final void testPagingRO() throws ServletException, IOException, ConstraintException {
        final MetaClass metaClass = this.model.getMetaClass(ResearchObjective.class.getName());
        final Paging paging = QuickSearch.getPaging(Collections.EMPTY_MAP, metaClass);

        final HashMap<String, Order> orderBy = paging.getOrderBy();
        Assert.assertFalse(orderBy.containsKey("name"));
    }

    public final void testPagingDefault() throws ServletException, IOException, ConstraintException {
        final MetaClass metaClass = this.model.getMetaClass(ResearchObjective.class.getName());
        final Map<String, String[]> parms = new HashMap();
        parms.put("pagenumber", new String[] { "" });
        final Paging paging = QuickSearch.getPaging(parms, metaClass);
        Assert.assertEquals(20, paging.getLimit());
        Assert.assertEquals(0, paging.getStart());
    }

    public final void testPaging10() throws ServletException, IOException, ConstraintException {
        final MetaClass metaClass = this.model.getMetaClass(ResearchObjective.class.getName());
        final Map<String, String[]> parms = new HashMap();
        parms.put("pagenumber", new String[] { "9" });
        parms.put("pagesize", new String[] { "11" });
        final Paging paging = QuickSearch.getPaging(parms, metaClass);
        Assert.assertEquals(11, paging.getLimit());
        Assert.assertEquals(88, paging.getStart());
    }

    public final void testSearchAllAbstract() throws ServletException, IOException, ConstraintException {
        final MetaClass metaClass = this.model.getMetaClass(Substance.class.getName());
        final Map<String, String[]> parms = new HashMap();
        parms.put("search_all", new String[] { QuickSearchTest.NAME });
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            new Molecule(version, "protein", QuickSearchTest.NAME);
            final Collection<ModelObjectBean> results =
                QuickSearch.search(version, metaClass, new Paging(0, 20),
                    QuickSearch.getOnlyCriteria(version, metaClass, parms), QuickSearchTest.NAME);
            Assert.assertEquals(1, results.size());
        } finally {
            version.abort();
        }
    }

    public final void testDoGetNoClass() throws ServletException, IOException {
        final QuickSearch servlet = new QuickSearch();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals("/JSP/customSearch/WhichClass.jsp", request.getDispatchedPath());
    }

    public final void testDoGetNoParms() throws ServletException, IOException {
        final QuickSearch servlet = new QuickSearch();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setPathInfo("/" + RefSample.class.getName());
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNull(request.getAttribute("noSearch"));
        Assert.assertTrue(0 < (Integer) request.getAttribute("resultSize"));
    }

    public final void testDoGet() throws ServletException, IOException {
        final QuickSearch servlet = new QuickSearch();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setPathInfo("/" + RefSample.class.getName());
        request.setQueryString("SUBMIT=Search");
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNull(request.getAttribute("noSearch"));
        Assert.assertTrue(0 < (Integer) request.getAttribute("resultSize"));
        Assert.assertEquals(new Integer(1), request.getAttribute("pagenumber"));
        Assert.assertEquals(new Integer(20), request.getAttribute("pagesize"));
    }

    public final void testDoGetOne() throws ServletException, IOException, ConstraintException,
        AccessException, AbortedException {
        final String hook1, hook2;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            hook1 = new RefSample(version, QuickSearchTest.UNIQUE + "one").get_Hook();
            hook2 = new RefSample(version, QuickSearchTest.UNIQUE + "two").get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        // test
        final QuickSearch servlet = new QuickSearch();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put("SUBMIT", new String[] { "Search" });
        parms.put(AbstractSample.PROP_NAME, new String[] { QuickSearchTest.UNIQUE + "one" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("/" + RefSample.class.getName());
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_MOVED_TEMPORARILY, response.getStatus());

        // clean up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ModelObject mo1 = version.get(hook1);
            mo1.delete();
            final ModelObject mo2 = version.get(hook2);
            mo2.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public final void testDoGetTwo() throws ServletException, IOException, ConstraintException,
        AccessException, AbortedException {
        final String hook1, hook2;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            hook1 = new RefSample(version, QuickSearchTest.UNIQUE + "one").get_Hook();
            hook2 = new RefSample(version, QuickSearchTest.UNIQUE + "two").get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        // test
        final QuickSearch servlet = new QuickSearch();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put("search_all", new String[] { QuickSearchTest.UNIQUE });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("/" + RefSample.class.getName());
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals(new Integer(2), request.getAttribute("resultSize"));

        // clean up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ModelObject mo1 = version.get(hook1);
            mo1.delete();
            final ModelObject mo2 = version.get(hook2);
            mo2.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public final void testPaging() throws ServletException, IOException {
        final QuickSearch servlet = new QuickSearch();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put("pagesize", new String[] { "5" });
        parms.put("pagenumber", new String[] { "2" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("/" + RefSample.class.getName());
        //request.setQueryString("SUBMIT=Search");
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNull(request.getAttribute("noSearch"));
        Assert.assertTrue(0 < (Integer) request.getAttribute("resultSize"));
        Assert.assertEquals(new Integer(2), request.getAttribute("pagenumber"));
        Assert.assertEquals(new Integer(5), request.getAttribute("pagesize"));
    }

    public final void testDoGetMetaClassInParm() throws ServletException, IOException {
        final QuickSearch servlet = new QuickSearch();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put("SUBMIT", new String[] { "Search" });
        parms.put("_metaClass", new String[] { RefSample.class.getName() });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNull(request.getAttribute("noSearch"));
        Assert.assertTrue(0 < (Integer) request.getAttribute("resultSize"));
        Assert.assertNotNull(request.getAttribute("beans"));
        final Collection beans = (Collection) request.getAttribute("beans");
        Assert.assertTrue(0 < beans.size());
        Assert.assertTrue(beans.iterator().next() instanceof RefSampleBean);
    }

}
