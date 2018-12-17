package org.pimslims.servlet.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;
import org.pimslims.presentation.TargetBeanForLists;
import org.pimslims.search.Paging;
import org.pimslims.search.Paging.Order;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.QuickSearch;

/*
 */
/**
 * Servlet based on BrowseTargets.java, modified to create list of Targets in a group
 * 
 * @author Susy Griffiths Created on 01.09.2005
 */
public class TargetGroupReport extends PIMSServlet {

    /**
     * Constructor
     */
    public TargetGroupReport() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Prepare the list of targets and html header, then forward to the presentation page";
    }

    /**
     * Show the requested Target Group TargetGroupReport.doGet
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }

        final java.io.PrintWriter writer = response.getWriter();
        int pageSize = 20;

        // get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        try {
            final Map params = request.getParameterMap();

            int pageStart = 0;
            //final int pn = 1;
            if (params.size() > 0) {
                //pagesize is from pimsWidget:pageControls tag
                if (this.isValid(request.getParameter("pagesize"))) {
                    pageSize = Integer.parseInt(request.getParameter("pagesize"));
                }

                pageStart = QuickSearch.getPageStartForDisplayTag(params, pageSize);
            }
            request.setAttribute("pagenumber", pageStart + 1);
            final Paging paging = new Paging(pageStart, pageSize);
            paging.addOrderBy("name", Order.ASC);

            final String pathInfo = request.getPathInfo();
            final String hook = pathInfo.substring(1);
            TargetGroup group = null;
            group = (TargetGroup) version.get(hook);
            final String groupName = group.get_Name();

            final Map<String, Object> gpAttrMap = new java.util.HashMap<String, Object>();
            gpAttrMap.put(Target.PROP_TARGETGROUPS, group);
            final Collection<Target> targs = version.findAll(Target.class, gpAttrMap);
            final int numTinGroup = targs.size();

            request.setAttribute("hook", hook);
            request.setAttribute("targets", TargetGroupReport.getTargetBeans(targs, paging));
            request.setAttribute("nameAttr", groupName);
            request.setAttribute("mayUpdate", Boolean.TRUE);
            request.setAttribute("pagesize", pageSize);
            request.setAttribute("resultSize", numTinGroup);
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/TargetGroupReport.jsp");
            //r21168 changed from dispatcher.forward because need include response from another Servlet within the caller Servlet 
            dispatcher.forward(request, response);
            version.commit();
        } catch (final AbortedException e1) {
            this.log("example aborted", e1);
            writer.print("Sorry, there has been a problem, please try again.");
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            } // tidy up the transaction
        }
    }

    static Collection<TargetBeanForLists> getTargetBeans(final Collection<Target> targets, final Paging paging) {
        //final java.util.Collection<Target> targs = version.findAll(Experiment.class, expAttrMap, paging);

        final ArrayList targetBeans = new ArrayList();
        int count = 0;
        for (final java.util.Iterator i = targets.iterator(); i.hasNext();) {
            final ModelObject object = (ModelObject) i.next();
            final TargetBeanForLists target = new TargetBeanForLists((Target) object);
            targetBeans.add(target);
            count = count + 1;
        }
        Collections.sort(targetBeans);
        return targetBeans;
    }

    private boolean isValid(final String s) {
        if (s != null && s.trim().length() > 0) {
            return true;
        }
        return false;
    }

} // servlet end
