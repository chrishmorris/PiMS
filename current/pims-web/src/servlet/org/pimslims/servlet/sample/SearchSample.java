/**
 * pims-web org.pimslims.servlet Search.java
 * 
 * @author Marc Savitsky
 * @date 14 Feb 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky * *
 * 
 */
package org.pimslims.servlet.sample;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.people.Person;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.search.Conditions;
import org.pimslims.search.Paging;
import org.pimslims.search.Searcher;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.QuickSearch;

/**
 * Search
 * 
 */

@javax.servlet.annotation.WebServlet("/Search/org.pimslims.model.sample.Sample")
public class SearchSample extends PIMSServlet {

    /**
     * IS_IN_MODAL_WINDOW String
     */
    public static final String IS_IN_MODAL_WINDOW = QuickSearch.IS_IN_MODAL_WINDOW;

    /**
     * SUBMIT String
     */
    private static final String SUBMIT = QuickSearch.SUBMIT;

    /**
     * SEARCH_ALL String
     */
    public static final String SEARCH_ALL = QuickSearch.SEARCH_ALL;

    private static final long serialVersionUID = -2823117367106408485L;

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Search for records of a given type";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final Map m = request.getParameterMap();
        /*for (final Iterator it = m.entrySet().iterator(); it.hasNext();) {
            final Map.Entry e = (Map.Entry) it.next();
            final String[] values = (String[]) e.getValue();
            final StringBuffer s = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                s.append(values[i] + ",");
            }
            s.deleteCharAt(s.length() - 1);
            //System.out.println("org.pimslims.servlet.sample.SampleSearch request parameter [" + e.getKey()                 + ":" + s.toString() + "]");
        } */

        final java.io.PrintWriter writer = response.getWriter();
        final String metaClassName = this.getMetaClassName(request);

        if (metaClassName == null || "".equals(metaClassName)) {
            throw new ServletException("Expected: " + Sample.class.getName());
        }

        final MetaClass metaClass = this.getModel().getMetaClass(metaClassName);

        if (!this.checkStarted(request, response)) {
            return;
        }

        QuickSearch.parmToAttribute(request);

        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return;
        }

        final MetaClass sampleMetaClass = this.getModel().getMetaClass(Sample.class.getName());

        try {

            this.setSpecialBeans(request, metaClass, version);

            request.setAttribute("metaClass", metaClass);
            request.setAttribute("searchMetaClass", metaClass);
            request.setAttribute("displayName", "Sample");

            final Map criteria = SearchSample.doGetCriteria(version, metaClass, request.getParameterMap());

            final Paging paging = QuickSearch.getPaging(request.getParameterMap(), sampleMetaClass);
            final String search_all = request.getParameter(SearchSample.SEARCH_ALL);
            final int countOfResults =
                QuickSearch.getCountOfResults(version, metaClass, criteria, search_all);
            final List<ModelObjectBean> beans =
                QuickSearch.search(version, metaClass, paging, criteria, search_all);

            if (countOfResults == 1
                && (request.getParameter("isInPopup") == null && request
                    .getParameter(SearchSample.IS_IN_MODAL_WINDOW) == null)) {
                this.redirect(response, request.getContextPath() + "/View/"
                    + beans.iterator().next().getHook());
            } else {

                // TODO no, put head in JSP
                this.writeHead(request, response, "Search Samples");

                final int totalRecords = QuickSearch.getCountOfRecords(version, metaClass);
                request.setAttribute("totalRecords", totalRecords);

                request.setAttribute("resultSize", countOfResults);

                QuickSearch.setPagingAttributes(request, paging);
                request.setAttribute("beans", beans);
                final String searchBox = "closed";
                request.setAttribute("searchBox", searchBox);
                request.setAttribute(SearchSample.SEARCH_ALL, search_all);
                request.setAttribute("sampleCategoryList", ModelObjectShortBean
                    .getModelObjectShortBeans(version
                        .getAll(org.pimslims.model.reference.SampleCategory.class)));

                QuickSearch.passDisplayAttributes(request, metaClass, beans);
                QuickSearch.passSearchAttributes(request, metaClass, version, totalRecords);
                request.setAttribute("sampleMetaClass", sampleMetaClass);

                PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_OK);
                final RequestDispatcher dispatcher =
                    request.getRequestDispatcher("/JSP/search/org.pimslims.model.sample.Sample.jsp");
                dispatcher.include(request, response);
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

    /**
     * 
     * QuickSearch.getCountOfResults
     * 
     * @param version
     * @param metaClass
     * @param criteria
     * @param searchString
     * @return
     * @throws ServletException
     * @throws IOException
     */

    @Deprecated
    //TODO use QuickSearch.getCountOfResults
    static int getCountOfResults(final ReadableVersion version, final MetaClass metaClass,
        final Map criteria, final String searchString) throws ServletException, IOException {

        int count;
        final Searcher s = new Searcher(version);
        if (ServletUtil.validString(searchString)) {
            count = s.count(metaClass, criteria, searchString);
        } else {
            count = s.count(criteria, metaClass);
        }

        return count;
    }

    /**
     * SearchSample.getMetaClassName
     * 
     * @see org.pimslims.servlet.QuickSearch#getMetaClassName(javax.servlet.http.HttpServletRequest)
     */
    protected String getMetaClassName(final HttpServletRequest request) {
        return Sample.class.getName();
    }

    /**
     * SearchSample.setSpecialBeans
     * 
     * @see org.pimslims.servlet.QuickSearch#setSpecialBeans(javax.servlet.http.HttpServletRequest,
     *      org.pimslims.metamodel.MetaClass, org.pimslims.dao.ReadableVersion)
     */
    protected void setSpecialBeans(final HttpServletRequest request, final MetaClass metaClass,
        final ReadableVersion version) {

        if (metaClass.getName().equals(Sample.class.getName())) {
            // request.setAttribute("userPersons",                 PersonUtility.getHookAndName(PersonUtility.getUserPersons(version)));

            final String dbid =
                request.getParameter("org.pimslims.model.sample.Sample:" + Sample.PROP_ASSIGNTO);
            if (null != dbid) { // probably obsolete
                request.setAttribute("personAssignedTo", this.getSelectedPerson(dbid, version));
            }
        }
    }

    private Map getSelectedPerson(final String personHook, final ReadableVersion version) {
        // Get person selected
        final Map hookPerson = new HashMap();

        final Person person = version.get(personHook);
        if (person != null) {
            hookPerson.put("hook", person.get_Hook());
            hookPerson.put("name", person.getGivenName() + " " + person.getFamilyName());
        }

        return hookPerson;
    }

    /**
     * SearchSample.doGetCriteria
     * 
     * @see org.pimslims.servlet.QuickSearch#doGetCriteria(org.pimslims.dao.ReadableVersion,
     *      org.pimslims.metamodel.MetaClass, java.util.Map)
     */
    static protected Map doGetCriteria(final ReadableVersion version, final MetaClass metaClass,
        final Map parameterMap) {
        final Map criteria = QuickSearch.getCriteria(version, metaClass, parameterMap);
        SearchSample.updateCriteriaForInputsForGroup(version, metaClass, criteria);
        if (parameterMap.containsKey("isInPopup")) {
            criteria.put(AbstractSample.PROP_ISACTIVE, true);
        }
        return criteria;
    }

    /**
     * OldSearch.updateCriteriaForInputsForGroup
     * 
     * @param version
     * @param metaClass
     * @param criteria
     */
    public static void updateCriteriaForInputsForGroup(final ReadableVersion version,
        final MetaClass metaClass, final Map criteria) {

        // PiMS 1038 exclude samples that are output samples from this experimentGroup
        final String experimentGroupHook = (String) criteria.get("experimentGroup");
        ExperimentGroup group = null;

        // note that even with this complicated condition, Searcher generates a single select
        if (null != experimentGroupHook) {
            group = version.get(experimentGroupHook);

            if (null != group) {
                final Map<String, Object> expCriterials =
                    Conditions.newMap(Experiment.PROP_EXPERIMENTGROUP,
                        Conditions.or(Conditions.isNull(), Conditions.notEquals(group)));

                final Map<String, Object> osCriterials =
                    Conditions.newMap(OutputSample.PROP_EXPERIMENT, expCriterials);

                criteria.put(Sample.PROP_OUTPUTSAMPLE, osCriterials);
            }
        }

    }
}
