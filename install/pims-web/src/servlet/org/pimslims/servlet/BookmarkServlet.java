package org.pimslims.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.Bookmark;

public class BookmarkServlet extends PIMSServlet {

    /**
     * default home page TODO add this to reference data
     * 
     */
    private static final String HOME = "/JSP/core/Dashboard.jsp?"
        + "_top=0.5em&_left=1.5%25&_path=/JSP/homepage-bricks/calendar.jsp&_height=14em&_width=30%25"
        + "&_top=15.5em&_left=1.5%25&_path=/JSP/homepage-bricks/bookmarks.jsp&_height=14em&_width=30%25"
        + "&_top=0.5em&_left=34.5%25&_path=/JSP/homepage-bricks/newTarget.jsp&_height=14em&_width=30%25"
        + "&_top=15.5em&_left=34.5%25&_path=/JSP/homepage-bricks/quickSearch.jsp&_height=14em&_width=30%25"
        + "&_top=0.5em&_left=67.5%25&_path=/JSP/homepage-bricks/history.jsp&_height=21.5em&_width=30%25"
        + "&_top=23em&_left=67.5%25&_path=/JSP/homepage-bricks/barcodeSearch.jsp&_height=6.5em&_width=30%25";

    @Override
    public String getServletInfo() {
        return "Provide short URLs, redirects to named PiMS pages";
    }

    /**
     * Bookmark.doGet
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        View.getCurrentPerspective(request);
        String url = BookmarkServlet.HOME;
        final String pathInfo = request.getPathInfo();
        if (null != pathInfo && 1 < pathInfo.length()) {
            final ReadableVersion version = this.getReadableVersion(request, response);
            try {
                final org.pimslims.model.core.Bookmark bookmark =
                    version.findFirst(Bookmark.class, Bookmark.PROP_NAME, pathInfo.substring(1));
                if (null != bookmark) {
                    url = bookmark.getUrl();
                }
            } finally {
                version.abort();
            }
        }
        final RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }

    /**
     * Bookmark.doPost
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        String url = request.getParameter(Bookmark.PROP_URL);
        if (null == url) {
            url = PIMSServlet.getReferer(request); // TODO check this does not contain http://.../pims/
        }
        final WritableVersion version = this.getWritableVersion(request, response);
        try {
            final String name = request.getParameter(Bookmark.PROP_NAME);
            final org.pimslims.model.core.Bookmark bookmark =
                version.findFirst(Bookmark.class, Bookmark.PROP_NAME, name);
            if (null == bookmark) {
                new Bookmark(version, name, url);
            } else {
                bookmark.setUrl(url);
            }
            version.commit();
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        //TODO if (!isAjax()) r
        PIMSServlet.redirectPostToReferer(request, response);
    }

}
