/**
 * V5_0-web org.pimslims.servlet BookmarkServletTest.java
 * 
 * @author cm65
 * @date 30 Sep 2013
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet;

import java.io.IOException;
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
import org.pimslims.model.core.Bookmark;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockHttpSession;
import org.pimslims.presentation.mock.MockServletConfig;

/**
 * BookmarkServletTest
 * 
 */
public class BookmarkServletTest extends TestCase {

    private static final String UNIQUE = "bs" + System.currentTimeMillis();

    private static final String TEN = "1234567890";

    private final AbstractModel model;

    /**
     * Constructor for BookmarkServletTest
     * 
     * @param name
     */
    public BookmarkServletTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testPost() throws ServletException, IOException, AbortedException, ConstraintException,
        AccessException {
        final String url = BookmarkServletTest.UNIQUE;
        this.doTest(url);
    }

    public void testLongPost() throws ServletException, IOException, AbortedException, ConstraintException,
        AccessException {
        final String url =
            BookmarkServletTest.UNIQUE + BookmarkServletTest.TEN + BookmarkServletTest.TEN
                + BookmarkServletTest.TEN + BookmarkServletTest.TEN + BookmarkServletTest.TEN
                + BookmarkServletTest.TEN;
        this.doTest(url);
    }

    /**
     * BookmarkServletTest.doTest
     * 
     * @param url
     * @throws ServletException
     * @throws IOException
     * @throws AccessException
     * @throws ConstraintException
     * @throws AbortedException
     */
    private void doTest(final String url) throws ServletException, IOException, AccessException,
        ConstraintException, AbortedException {
        final BookmarkServlet servlet = new BookmarkServlet();
        servlet.init(new MockServletConfig(this.model));
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockHttpSession session = new MockHttpSession(Access.ADMINISTRATOR);
        final Map parms = new HashMap();
        parms.put(Bookmark.PROP_NAME, new String[] { BookmarkServletTest.UNIQUE });
        parms.put(Bookmark.PROP_URL, new String[] { url });
        final MockHttpServletRequest request = new MockHttpServletRequest("post", session, parms);
        // request.setPathInfo("/" + hook);
        servlet.doPost(request, response);

        // tidy up
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Bookmark bookmark =
                version.findFirst(Bookmark.class, Bookmark.PROP_NAME, BookmarkServletTest.UNIQUE);
            Assert.assertEquals(url, bookmark.getUrl());
            bookmark.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

} // http://localhost:8080/embiosbs1380548427600123456789012345678901234567890123456789012345%20678901234567890
