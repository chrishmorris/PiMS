/**
 * V5_0-web org.pimslims.servlet FindBeans.java
 * 
 * @author cm65
 * @date 3 Dec 2013
 * 
 *       Protein Information Management System
 * @version: 4.5
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.search.Paging;

/**
 * FindBeans See homepage-bricks/bookmarks.jsp
 */
public class FindBeans extends PIMSServlet {

    /**
     * FindBeans.getServletInfo
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "make a list of beans representing records matching some search criteria";
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final String metaClassName = this.getMetaClassName(request);
        final MetaClass metaClass = this.getModel().getMetaClass(metaClassName);

        if (metaClass == null) {
            throw new ServletException("Unknown type: " + metaClassName);
        }
        QuickSearch.parmToAttribute(request);

        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return;
        }

        try {

            final String displayName = ServletUtil.getDisplayName(metaClass);

            final SearchFilter criteria = this.doGetCriteria(version, metaClass, request.getParameterMap());

            final Paging paging = QuickSearch.getPaging(request.getParameterMap(), metaClass);
            final String search_all = request.getParameter(QuickSearch.SEARCH_ALL);

            QuickSearch.setPagingAttributes(request, paging);
            final List<ModelObjectBean> beans =
                QuickSearch.search(version, metaClass, paging, criteria.getMap(), search_all);
            request.setAttribute("beans", beans);

            version.commit();

        } catch (final AbortedException e1) {
            Throwable cause = e1;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }

            throw new ServletException("Sorry, there has been a problem in PiMS, please try again.", cause);

        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);

        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    protected String getMetaClassName(final HttpServletRequest request) {
        String metaClassName = null;
        {
            final String pathInfo = request.getPathInfo();

            if (null != pathInfo && 1 < pathInfo.length()) {
                metaClassName = pathInfo.substring(1); // e.g.Search/org.pimslims.model.experiment.Experiment
            } else {
                metaClassName = request.getParameter("_metaClass");
            }
        }
        return metaClassName;
    }

    protected SearchFilter doGetCriteria(final ReadableVersion version, final MetaClass metaClass,
        final Map<String, String[]> parameterMap) {
        return new SearchFilter(QuickSearch.getCriteria(version, metaClass, parameterMap));
    }

}
