/**
 * pims-web-current org.pimslims.servlet WeekServlet.java
 * 
 * @author ejd53
 * @date 29-Sep-2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 ejd53 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.Utils;
import org.pimslims.presentation.WeekBean;

/**
 * WeekServlet
 * 
 */
public class WeekServlet extends PIMSServlet {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final SimpleDateFormat linkDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public WeekServlet() {
        super();
    }

    @Override
    public final String getServletInfo() {
        return "Show the Week view";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected final void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (null != pathInfo && 0 < pathInfo.length()) {
            pathInfo = pathInfo.substring(1); // strip initial slash
        }

        Date requestDate = null;
        try {
            requestDate = this.dateFormat.parse(pathInfo);
        } catch (final ParseException e) {
            requestDate = new Date();
        }
        final Calendar requestDay = new GregorianCalendar();
        requestDay.setTime(requestDate);

        final ReadableVersion version = this.getReadableVersion(request, response);
        final String userName = PIMSServlet.getUsername(request);

        final WeekBean weekBean = new WeekBean(requestDay, version, userName);
        final Calendar monday = weekBean.getMonday();

        final SimpleDateFormat df = new SimpleDateFormat(Utils.full_date_format);
        request.setAttribute("displaydate", df.format(monday.getTime()));

        final Calendar prevWeek = (Calendar) monday.clone();
        prevWeek.add(Calendar.DATE, -7);
        final Calendar nextWeek = (Calendar) monday.clone();
        nextWeek.add(Calendar.DATE, 7);
        request.setAttribute("prevweek", this.linkDateFormat.format(prevWeek.getTime()));
        request.setAttribute("nextweek", this.linkDateFormat.format(nextWeek.getTime()));

        try {

            request.setAttribute("monday", weekBean.getDayBean("monday"));
            request.setAttribute("tuesday", weekBean.getDayBean("tuesday"));
            request.setAttribute("wednesday", weekBean.getDayBean("wednesday"));
            request.setAttribute("thursday", weekBean.getDayBean("thursday"));
            request.setAttribute("friday", weekBean.getDayBean("friday"));
            request.setAttribute("saturday", weekBean.getDayBean("saturday"));
            request.setAttribute("sunday", weekBean.getDayBean("sunday"));

            final RequestDispatcher rd = request.getRequestDispatcher("/JSP/weekView/WeekView.jsp");
            rd.forward(request, response);

        } finally {
            version.abort();
        }
    }

}
