/**
 * V5_0-web org.pimslims.servlet RSS.java
 * 
 * @author cm65
 * @date 1 Mar 2013
 * 
 *       Protein Information Management System
 * @version: 4.4
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.properties.PropertyGetter;

/**
 * RSS For PRIV-69. A framework allowing us to add RSS feeds to PiMS.
 */
public abstract class RSS extends PIMSServlet {

    /**
     * Link An element of an RSS feed
     */
    public interface Link {

        String getTitle();

        String getDescription();

        String getLink();

        String getAuthor();

        String getGuid();

    }

    /**
     * @return Returns the title of the feed.
     */
    abstract String getTitle();

    /**
     * @return Returns the description of the feed.
     */
    abstract String getDescription();

    /**
     * @return Returns the copyright of the feed.
     */
    String getCopyright(final HttpServletRequest request) {
        return PropertyGetter.getStringProperty("copyright", request.getLocalName());
    }

    /**
     * @return Returns the publication date of the feed.
     */
    String getPubDate() {
        return Calendar.getInstance().toString();
    }

    /**
     * RSS.doGet
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final List<Link> links = this.getLinks(request, response);

        final String accept = request.getHeader("Accept");
        String feed = null;
        if (null == accept || accept.contains("application/atom+xml")) {
            feed = this.getAtomFeed(links);
            response.setHeader("Content-Type", "application/atom+xml;charset=UTF-8");
        } /* else if (accept.contains("application/rss+xml")
            && (accept.contains("revision=\"2.0\"") || !accept.contains("revision"))) {
            feed = this.getRss2Feed(links);
            response.setHeader("Content-Type", "application/rss+xml; revision=\"2.0\" ;charset=UTF-8");
          } */else {
            throw new ServletException("Unsupported RSS type: " + accept);
            /* TODO please test with Outlook 2007 and 2010, and Firefox Live Bookmarks.
             and add extra formats if this exception is thrown.
             In particular, the code above may help.
             If time permits please also test with:
             Google Reader, IE8 and IE9, Chrome RSS subscription extension, Safari (iPad and iPhone), Android browser 
            */
        }
        response.getWriter().write(feed);
    }

    /**
     * RSS.getAtomFeed
     * 
     * @param links
     * @return
     */
    final String getAtomFeed(final List<Link> links) {
        return null; // TODO please implement
    }

    /**
     * RSS.getLinks TODO please make a concrete subclass with an implementation of this, for testing
     * 
     * @param request
     * @param response
     * @return the liks to go into the feed
     */
    abstract List<Link> getLinks(HttpServletRequest request, HttpServletResponse response);

    /**
     * RSS.doPost
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new ServletException("Post is not supported");
    }

}
