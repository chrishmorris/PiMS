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
import org.pimslims.dao.ReadableVersionImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.search.PIMSCriteria;
import org.pimslims.search.Paging;
import org.pimslims.servlet.protocol.SearchProtocol;

/**
 * SearchTest
 * 
 */
public class SearchProtocolTest extends TestCase {

    private static final String UNIQUE = "Search" + System.currentTimeMillis();

    private static final String NAME = "n" + SearchProtocolTest.UNIQUE;

    private final AbstractModel model;

    /**
     * @param name
     */
    public SearchProtocolTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public final void testSearchNoParms() throws ServletException, IOException {
        final SearchProtocol servlet = new SearchProtocol();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request =
            new MockHttpServletRequest("get", session, Collections.EMPTY_MAP);
        request.setPathInfo("/" + Protocol.class.getName());
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNull(request.getAttribute("noSearch"));
        Assert.assertNotNull(request.getAttribute("experimentTypes"));
        Assert.assertNotNull(request.getAttribute("instrumentTypes"));
        Assert.assertTrue(0 <= (Integer) request.getAttribute("resultSize"));
    }

    public final void testSearch() throws ServletException, IOException {
        final SearchProtocol servlet = new SearchProtocol();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put("SUBMIT", new String[] { "Search" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("/" + Protocol.class.getName());
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNull(request.getAttribute("noSearch"));
        Assert.assertTrue(0 <= (Integer) request.getAttribute("resultSize"));
    }

    public final void testSearchAll() throws ServletException, IOException {
        final SearchProtocol servlet = new SearchProtocol();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put("SUBMIT", new String[] { "Quick+Search" });
        parms.put("search_all", new String[] { "mobio" }); //TODO fix dependency on Emerald data
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("/" + Protocol.class.getName());
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNull(request.getAttribute("noSearch"));
        final Integer resultSize = (Integer) request.getAttribute("resultSize");
        Assert.assertTrue(0 <= resultSize);
        if (resultSize <= 20) {
            final Collection results = (Collection) request.getAttribute("beans");
            Assert.assertEquals(results.size(), resultSize.intValue());
        }
    }

    public void testSearchProduct() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        final String categoryName = SearchProtocolTest.UNIQUE + "cat";
        try {
            final SampleCategory category = new SampleCategory(version, categoryName);
            final ExperimentType type = new ExperimentType(version, SearchProtocolTest.UNIQUE);
            final Protocol protocol = new Protocol(version, SearchProtocolTest.UNIQUE, type);
            new RefOutputSample(version, category, protocol);

            final Map<String, Object> categoryCriteria = new HashMap();
            categoryCriteria.put(SampleCategory.PROP_NAME, categoryName);
            final Map<String, Object> rosCriteria = new HashMap();
            rosCriteria.put(RefOutputSample.PROP_SAMPLECATEGORY, categoryCriteria);
            final Map<String, Object> protocolCriteria = new HashMap();
            protocolCriteria.put(Protocol.PROP_REFOUTPUTSAMPLES, rosCriteria);

            PIMSCriteria query =
                ((ReadableVersionImpl) version).CreateQuery(RefOutputSample.class, new Paging(0, 2), null);
            query.setAttributeMap(rosCriteria);
            Assert.assertEquals(1, query.count());

            query = ((ReadableVersionImpl) version).CreateQuery(Protocol.class, new Paging(0, 2), null);
            query.setAttributeMap(protocolCriteria);
            Assert.assertEquals(1, query.count());

        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    public final void testServletSearchProduct() throws ServletException, IOException, ConstraintException,
        AccessException, AbortedException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        final String categoryName = SearchProtocolTest.UNIQUE + "cat";
        try {
            final SampleCategory category = new SampleCategory(version, categoryName);
            final ExperimentType type = new ExperimentType(version, SearchProtocolTest.UNIQUE);
            final Protocol protocol = new Protocol(version, SearchProtocolTest.UNIQUE, type);
            new RefOutputSample(version, category, protocol);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final SearchProtocol servlet = new SearchProtocol();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put("SUBMIT", new String[] { "Search" });
        parms.put("experimentType", new String[] { "" });
        parms.put(SearchProtocol.PRODUCT, new String[] { categoryName, SearchProtocolTest.UNIQUE + "other" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("/" + Protocol.class.getName());
        servlet.doGet(request, response);
        Assert.assertEquals(1, request.getAttribute("resultSize"));
        Assert.assertNull(request.getAttribute("noSearch"));

        // tidy up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final SampleCategory category =
                version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, categoryName);
            final RefOutputSample ros =
                version.findFirst(RefOutputSample.class, RefOutputSample.PROP_SAMPLECATEGORY, category);
            final ExperimentType type = ros.getProtocol().getExperimentType();
            ros.getProtocol().delete();
            type.delete();
            category.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public final void testDoGetMetaClassInParm() throws ServletException, IOException {
        final SearchProtocol servlet = new SearchProtocol();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map<String, String[]> parms = new HashMap();
        parms.put("SUBMIT", new String[] { "Search" });
        parms.put("_metaClass", new String[] { Protocol.class.getName() });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNull(request.getAttribute("noSearch"));
        Assert.assertTrue(0 <= (Integer) request.getAttribute("resultSize"));
    }

}
