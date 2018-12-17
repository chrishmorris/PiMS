/**
 * V2_3-pims-web org.pimslims.servlet.expert ExpertViewTest.java
 * 
 * @author cm65
 * @date 3 Oct 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.expert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;

/**
 * ExpertViewTest
 * 
 */
public class ExpertViewTest extends TestCase {

    private static final String UNIQUE = "ev" + System.currentTimeMillis();

    private final AbstractModel model;

    private final List<String> hooks = new ArrayList<String>();

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {

            final ExperimentType type = new ExperimentType(version, ExpertViewTest.UNIQUE);
            this.hooks.add(type.get_Hook());
            final Molecule molecule = new Molecule(version, "other", ExpertViewTest.UNIQUE);
            this.hooks.add(molecule.get_Hook());
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

            for (final ListIterator<String> iterator = this.hooks.listIterator(); iterator.hasNext();) {
                final String hook = iterator.next();
                final ModelObject modelObject = version.get(hook);
                modelObject.delete();
                iterator.remove();
            }
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * Constructor for ExpertViewTest
     * 
     * @param name
     */
    public ExpertViewTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testDoGetNotFound() throws ServletException, IOException {
        final View servlet = new View();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("nonesuch");
        final MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
    }

    public void testDoGet() throws ServletException, IOException {
        final View servlet = new View();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("/" + this.hooks.get(0));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertNotNull(request.getAttribute("bean"));
        Assert.assertNotNull(request.getAttribute("ObjName"));
        Assert.assertNotNull(request.getAttribute("head"));

    }

    public void wastestCantCreateIfNobackLink() throws ServletException, IOException {
        final View servlet = new View();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        request.setPathInfo("/" + this.hooks.get(1));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

    }

}
