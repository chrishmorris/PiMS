package org.pimslims.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Bookmark extends PIMSServlet {

    /**
     * HOME String
     */
    private static final String HOME = "/JSP/core/Dashboard.jsp?"
        + "&_top=0.5em&_left=1.5%25&_path=/JSP/homepage-bricks/calendar.jsp&_height=14em&_width=30%25"
        + "&_top=15.5em&_left=1.5%25&_path=/JSP/homepage-bricks/activeTargets.jsp&_height=14em&_width=30%25"
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
        //TODO look up the bookmark and redirect to it
        final RequestDispatcher rd = request.getRequestDispatcher(Bookmark.HOME);
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
        // TODO save a bookmark
        throw new ServletException("Not implemented yet");
    }

}
