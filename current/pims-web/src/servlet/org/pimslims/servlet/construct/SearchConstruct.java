/**
 * pims-web org.pimslims.servlet.construct SearchConstruct.java
 * 
 * @author Marc Savitsky
 * @date 27 Jan 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.servlet.construct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ConstructUtility;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;
import org.pimslims.presentation.construct.ConstructBeanForList;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.search.Conditions;
import org.pimslims.search.Paging;
import org.pimslims.search.Paging.Order;
import org.pimslims.search.Searcher;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.QuickSearch;

/**
 * SearchConstruct
 * 
 */

@javax.servlet.annotation.WebServlet("/Search/org.pimslims.model.target.ResearchObjective")
public class SearchConstruct extends PIMSServlet {

    /**
     * IS_IN_MODAL_WINDOW String
     */
    private static final String IS_IN_MODAL_WINDOW = QuickSearch.IS_IN_MODAL_WINDOW;

    /**
     * 
     */
    public SearchConstruct() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Prepare the list of complex and html header, then forward to the presentation page";
    }

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        /*
         * for testing
         */

        /*
        System.out.println("org.pimslims.servlet.construct.SearchConstruct.doGet()");
        final Map m = request.getParameterMap();
        for (final Iterator it = m.entrySet().iterator(); it.hasNext();) {
            final Map.Entry e = (Map.Entry) it.next();
            final String[] values = (String[]) e.getValue();
            final StringBuffer s = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                s.append(values[i] + ",");
            }
            s.deleteCharAt(s.length() - 1);
            System.out.println("org.pimslims.servlet.construct.SearchConstruct request parameter ["
                + e.getKey() + ":" + s.toString() + "]");
        }
        */
        final java.io.PrintWriter writer = response.getWriter();
        // dont put thisng in the session final HttpSession session = request.getSession();
        final Map params = request.getParameterMap();
        //final String pathInfo = request.getPathInfo();

        final MetaClass metaClass = this.getModel().getMetaClass(ResearchObjective.class.getName());

        if (!this.checkStarted(request, response)) {
            return;
        }

        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return;
        }

        final String displayName = "Construct";
        request.setAttribute("metaClass", metaClass);
        request.setAttribute("searchMetaClass", metaClass);
        request.setAttribute("displayName", displayName);

        // for handling pop-up roles

        if (null != request.getParameter("isInPopup")) {
            request.setAttribute("isInPopup", request.getParameter("isInPopup"));
        }

        if (null != request.getParameter(SearchConstruct.IS_IN_MODAL_WINDOW)) {
            request.setAttribute(SearchConstruct.IS_IN_MODAL_WINDOW,
                request.getParameter(SearchConstruct.IS_IN_MODAL_WINDOW));
        }

        if (null != request.getParameter("barcodeSearch")) {
            request.setAttribute("barcodeSearch", request.getParameter("barcodeSearch"));
        }

        if (null != request.getParameter("experimentType")) {
            request.setAttribute("experimentType", request.getParameter("experimentType"));
        }

        if (null != request.getParameter("targetGroup")) {
            request.setAttribute("targetGroup", request.getParameter("targetGroup"));
        }

        if (null != request.getParameter("hook")) {
            request.setAttribute("hook", request.getParameter("hook"));
        }

        if (null != request.getParameter("callbackFunction")) {
            request.setAttribute("callbackFunction", request.getParameter("callbackFunction"));
            //TODO I don't think this is used
        }

        int pageSize = 100;
        if (null != request.getParameter(SearchConstruct.IS_IN_MODAL_WINDOW)) {
            pageSize = 20;
        }

        // end first access page preparation
        try {

            final Map<String, Object> criteria =
                this.getMyCriteria(version, metaClass, request.getParameterMap());

            final String searchString = (String) criteria.get("search_all");
            criteria.remove("search_all");
            QuickSearch.prepareSearchForm(request, version, metaClass, criteria);

            int pageStart = 0;
            String searchBox = "closed";
            if (params.size() > 0) {
                if (null == params.get("search_all")) {
                    searchBox = "open";
                }
                if (null != params.get("pagesize")) {
                    pageSize = Integer.parseInt(request.getParameter("pagesize"));
                }

                final String tableId =
                    new ParamEncoder("mytable").encodeParameterName(TableTagParameters.PARAMETER_PAGE);
                if (null != params.get(tableId)) {
                    pageStart = (Integer.parseInt(((String[]) params.get(tableId))[0]) - 1) * pageSize;
                }
            }

            final Collection<ConstructBeanForList> results =
                SearchConstruct.getResults(version, criteria, searchString, pageStart, pageSize);

            // TODO remove, can now search everything
            if (null == results) {
                version.abort();
                writer.print("Sorry, searching " + displayName + "s is not currently supported");
                PIMSServlet.writeFoot(writer, request);
                return;
            }

            // Cannot send this earlier as redirect will not work
            // send the header now, since searching may take some time
            this.writeHead(request, response, "Search " + displayName + "s");
            // do the search

            // Not used new List will do this actions
            // prepare the controls for the objects
            final Map<String, String> control = new java.util.HashMap<String, String>();
            //for (final ModelObject object : results) {
            //    control.put(object.get_Hook(), DeleteButton.getForSearch(object, request.getContextPath()));
            //}
            if (version.isCompleted()) {
                throw new RuntimeException("version has been closed before prepareSearchForm!");
            }

            request.setAttribute("accessObjects", PIMSServlet.getPossibleCreateOwners(version));

            request.setAttribute("control", control);
            request.setAttribute("controlHeader", " ");
            request.setAttribute("resultSize",
                QuickSearch.getCountOfResults(version, metaClass, criteria, searchString));

            request.setAttribute("pagesize", pageSize);
            request.setAttribute("beans", results);
            request.setAttribute("searchBox", searchBox);

            PIMSServlet.dispatchCustomJsp(request, response, metaClass.getMetaClassName(), "search",
                this.getServletContext());
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

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        System.out.println("SearchConstruct.doPost");
    }

    private Map<String, Object> getMyCriteria(final ReadableVersion version, final MetaClass metaClass,
        final Map parameterMap) {

        //System.out.println("SearchConstruct.getMyCriteria");

        final Map criteria = QuickSearch.getCriteria(version, metaClass, parameterMap);

        ExperimentType orderType = null;
        TargetGroup targetGroup = null;
        String[] values;

        values = (String[]) parameterMap.get("experimentType");
        if (null != values && 0 != values.length) {
            if (1 == values.length) {
                final String experimentTypeName = values[0].trim();
                if (null != experimentTypeName) {
                    final Map<String, Object> etCriteria =
                        Conditions.newMap(ExperimentType.PROP_NAME, experimentTypeName);
                    orderType = version.findFirst(ExperimentType.class, etCriteria);
                }
            }
        }

        values = (String[]) parameterMap.get("targetGroup");
        if (null != values && 0 != values.length) {
            if (1 == values.length) {
                final String targetGroupHook = values[0].trim();
                if (null != targetGroupHook) {
                    targetGroup = version.get(targetGroupHook);
                }
            }
        }

        if (null != orderType) {

            final Map<String, Object> expCriteria =
                Conditions.newMap(Experiment.PROP_EXPERIMENTTYPE, Conditions.eq(orderType));

            final Map<String, Object> roCriteria =
                Conditions.notExistsMap(Conditions.newMap(ResearchObjective.PROP_EXPERIMENTS, expCriteria));

            criteria.putAll(roCriteria);
        }

        final Map<String, Object> roeCriteria =
            Conditions
                .newMap(
                    ResearchObjectiveElement.PROP_COMPONENTTYPE,
                    Conditions.or(Conditions.eq("OpticConstruct"),
                        Conditions.eq(ConstructUtility.SPOTCONSTRUCT)));

        if (null != targetGroup) {
            final Map<String, Object> targetCriteria =
                Conditions.newMap(Target.PROP_TARGETGROUPS, targetGroup);
            roeCriteria.put(ResearchObjectiveElement.PROP_TARGET, targetCriteria);
        }

        criteria.put(ResearchObjective.PROP_RESEARCHOBJECTIVEELEMENTS, roeCriteria);

        criteria.remove("experimentType");
        criteria.remove("targetGroup");
        criteria.remove("SUBMIT");

        return criteria;

    }

    /**
     * 
     * SearchConstruct.getResults
     * 
     * @param version
     * @param metaClass
     * @param criteria
     * @param searchString
     * @param pageStart
     * @param pageSize
     * @return
     * @throws ServletException
     * @throws IOException
     */
    static Collection<ConstructBeanForList> getResults(final ReadableVersion version, final Map criteria,
        final String searchString, final int pageStart, final int pageSize) throws ServletException,
        IOException {

        final MetaClass metaClass = version.getModel().getMetaClass(ResearchObjective.class.getName());

        //final long start = System.currentTimeMillis();
        final Collection<ConstructBeanForList> results = new ArrayList<ConstructBeanForList>();
        final Paging paging = new Paging(pageStart, pageSize);
        paging.addOrderBy(ResearchObjective.PROP_LOCALNAME, Order.ASC);

        //TODO use new Report class, see SearchExperiment
        final Searcher s = new Searcher(version);
        Collection<ModelObject> objectives = new ArrayList<ModelObject>();
        if (QuickSearch.isValid(searchString)) {
            objectives = s.search(metaClass, criteria, searchString, paging);
        } else {
            objectives = s.search(criteria, metaClass, paging);
        }
        //System.out.println("getResults objects [" + (System.currentTimeMillis() - start) + "]");

        //TODO this can give an OutOfMemory error, make smaller beans
        for (final ModelObject rObjective : objectives) {
            results.add(ConstructBeanReader.readConstruct((ResearchObjective) rObjective));
        }

        //System.out.println("getResults beans [" + (System.currentTimeMillis() - start) + "]");
        return results;
    }

    /**
     * valid String
     * 
     * @param s
     * @return
     */
    static boolean isValid(final String s) {
        if (s != null && s.trim().length() > 0) {
            return true;
        }
        return false;
    }
}
