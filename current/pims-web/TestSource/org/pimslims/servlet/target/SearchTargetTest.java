/**
 * V5_0-web org.pimslims.servlet.target SearchTargetTest.java
 * 
 * @author cm65
 * @date 25 Apr 2013
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.target;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;

/**
 * SearchTargetTest
 * 
 */
public class SearchTargetTest extends TestCase {

    private static final String UNIQUE = "st" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for SearchTargetTest
     * 
     * @param name
     */
    public SearchTargetTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testLikeNone() throws ServletException, IOException {
        final SearchTarget servlet = new SearchTarget();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap();
        parms.put("search_all", new String[] { "nonesuch" + SearchTargetTest.UNIQUE });
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        servlet.doGet(request, new MockHttpServletResponse());
        final Collection results = (Collection) request.getAttribute("results");
        Assert.assertNotNull(results);
        Assert.assertEquals(0, results.size());
    }

    public void testLikeOne() throws ServletException, IOException, AbortedException, ConstraintException,
        AccessException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Molecule protein = new Molecule(version, "protein", SearchTargetTest.UNIQUE);
            new Target(version, "One" + SearchTargetTest.UNIQUE, protein);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final SearchTarget servlet = new SearchTarget();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap();
        parms.put("search_all", new String[] { SearchTargetTest.UNIQUE });
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final MockHttpServletRequest request = new MockHttpServletRequest("get", session, parms);
        servlet.doGet(request, new MockHttpServletResponse());
        final Collection results = (Collection) request.getAttribute("results");
        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.size());

        // tidy up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Molecule protein =
                version.findFirst(Molecule.class, Substance.PROP_NAME, SearchTargetTest.UNIQUE);
            version.delete(protein.getProtTargets());
            protein.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
