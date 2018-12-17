/**
 * pims-web org.pimslims.servlet Search.java
 * 
 * @author Petr Troshin
 * @date May 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Petr Troshin * *
 * 
 */
package org.pimslims.servlet.sample;

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
import org.pimslims.lab.Util;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.PrimerBeanReader;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.search.Searcher;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.QuickSearch;

/**
 * Custom Search
 * 
 */

public class SearchPrimers extends PIMSServlet {

    private static final long serialVersionUID = -231173787408485L;

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Custom Search ";
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse) Parameters for customisation
     *      form=PrimersForm&view=PrimersResults
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        //final java.io.PrintWriter writer = response.getWriter();

        final Map params = request.getParameterMap();
        //final String pathInfo = request.getPathInfo();
        //final String metaClassName = null;
        int pageSize = 20;

        if (!this.checkStarted(request, response)) {
            return;
        }

        final MetaClass metaClass =
            this.getModel().getMetaClass(org.pimslims.model.sample.Sample.class.getName());

        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return;
        }

        request.setAttribute("metaClass", ServletUtil.getPIMSMetaClass(metaClass));
        request.setAttribute("searchMetaClass", ServletUtil.getPIMSMetaClass(metaClass));
        request.setAttribute("displayName", "Primers");

        // end first access page preparation

        try {

            final Map criteria = QuickSearch.getCriteria(version, metaClass, request.getParameterMap());
            SearchPrimers.prepareSearchForm(request, version, metaClass, criteria);

            int pageStart = 0;

            if (params.size() > 0) {

                if (null != params.get("pagesize")) {
                    pageSize = Integer.parseInt(request.getParameter("pagesize"));
                }

                pageStart = QuickSearch.getPageStartForDisplayTag(params, pageSize);
            }

            if (null == request.getQueryString() || "".equals(request.getQueryString())) {

                this.writeHead(request, response, "Search Primers");
                // tell JSP to show search form, with no results
                QuickSearch.prepareSearchForm(request, version, metaClass, Collections.EMPTY_MAP);

                request.setAttribute("noSearch", Boolean.TRUE);
                request.setAttribute("results", Collections.EMPTY_LIST);
                request.setAttribute("displayName", "Primer");
                request.setAttribute("_form", "SearchPrimersForm.jsp");
                request.setAttribute("_view", "SearchPrimersResults.jsp");
                request.setAttribute("resultSize", 0);
                request.setAttribute("pagesize", pageSize);

                final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/SearchPrimers.jsp");
                dispatcher.forward(request, response);
                version.abort();
                return;
            }
            // End first time access page preparation
            // Now search

            final Collection<ModelObject> results =
                SearchPrimers.getResults(version, metaClass, criteria, pageStart, pageSize);
            final Collection<PrimerBean> primers = this.preparePrimerBeans(results);

            if (results.size() == 1) {
                this.redirect(response, request.getContextPath() + "/ViewPrimer/"
                    + results.iterator().next().get_Hook());
            } else {

                // Cannot send this earlier as redirect will not work
                // send the header now, since searching may take some time
                this.writeHead(request, response, "Search Primers");
                // do the search

                // Not used new List will do this actions
                // prepare the controls for the objects
                final Map<String, String> control = new java.util.HashMap<String, String>();

                if (version.isCompleted()) {
                    throw new RuntimeException("version has been closed before prepareSearchForm!");
                }

                request.setAttribute("control", control);
                request.setAttribute("controlHeader", " ");
                request.setAttribute("resultSize", results.size());
                request.setAttribute("pagesize", pageSize);
                request.setAttribute("listMetaClass", metaClass);
                request.setAttribute("displayName", "Primer");
                request.setAttribute("_form", "SearchPrimersForm.jsp");
                request.setAttribute("_view", "SearchPrimersResults.jsp");
                request.setAttribute("results", primers);

                // PiMS 1852 map to contain only those attributes from the metaClass, not those from subClasses
                //Map attributes = ServletUtil.getAllFilledAttributes(results, null);

                final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/SearchPrimers.jsp");
                dispatcher.forward(request, response);
            }

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

    Collection<PrimerBean> preparePrimerBeans(final Collection<ModelObject> mObjs) {
        if (mObjs == null || mObjs.isEmpty()) {
            return null;
        }
        final ArrayList<PrimerBean> primers = new ArrayList<PrimerBean>(mObjs.size());
        for (final ModelObject mObj : mObjs) {
            final Sample sample = (Sample) mObj;
            final OutputSample oSample = sample.getOutputSample();
            if (oSample == null) {
                continue;
            }
            final Experiment exp = oSample.getExperiment();
            if (exp == null) {
                continue;
            }
            final ExperimentType expType = exp.getExperimentType();
            if (expType == null) {
                continue;
            }
            if (expType.getName().equalsIgnoreCase(org.pimslims.leeds.FormFieldsNames.primersDesign)) {
                primers.add(PrimerBeanReader.readPrimer(sample));
            }
        }
        return primers;
    }

    /**
     * See SearchForm.jsp TODO there was some copy and paste. Unify this with other methods of the same name.
     * 
     * @param request the HTTP request to process
     * @param ReadableVersion version
     * @param the metaClass to search
     * @param criteria attribute name => value
     */
    public static void prepareSearchForm(final HttpServletRequest request, final ReadableVersion version,
        final MetaClass metaClass, final Map criteria) {

        int totalRecords = -1;
        Map notEmptyAttributes = Collections.EMPTY_MAP;
        final Searcher s = new Searcher(version);

        //final long startTime = System.currentTimeMillis();
        totalRecords = s.countRecords(metaClass);
        //final long l = System.currentTimeMillis() - startTime;

        if (totalRecords != 0 && totalRecords != -1) {
            notEmptyAttributes = s.getSearchableFields(ServletUtil.getPIMSMetaClass(metaClass));
        }

        if (totalRecords == -1) {
            System.out.println("Sorry, there likely to has been an error ");
        }

        request.setAttribute("searchAttributes", notEmptyAttributes.keySet());
        request.setAttribute("criteria", criteria);
    }

    static Collection<ModelObject> getResults(final ReadableVersion version, final MetaClass metaClass,
        final Map criteria, final int pageStart, final int pageSize) throws ServletException, IOException {
        //TODO use new Report class, see SearchExperiment
        final String svalue = (String) criteria.get("search_all");
        Collection<ModelObject> results = new ArrayList<ModelObject>();
        final Searcher s = new Searcher(version);

        if (!Util.isEmpty(svalue)) {
            results = s.searchAll(metaClass, svalue);
        } else {
            results = s.search(criteria, metaClass);
        }

        return results;
    }

}
