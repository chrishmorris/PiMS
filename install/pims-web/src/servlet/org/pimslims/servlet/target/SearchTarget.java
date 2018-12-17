/**
 * pims-web org.pimslims.servlet.target SearchTarget.java
 * 
 * @author Marc Savitsky
 * @date 18 Feb 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.servlet.target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.target.Alias;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.presentation.TargetBeanForLists;
import org.pimslims.search.Conditions;
import org.pimslims.search.Paging;
import org.pimslims.search.Paging.Order;
import org.pimslims.search.Searcher;
import org.pimslims.search.conditions.HQLCondition;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.QuickSearch;
import org.pimslims.servlet.SearchFilter;

/**
 * SearchTarget TODO extends QuickSearch
 */
public class SearchTarget extends PIMSServlet {

    public SearchTarget() {
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

        //final java.io.PrintWriter writer = response.getWriter();
        //final HttpSession session = request.getSession();
        final Map params = request.getParameterMap();
        //final String pathInfo = request.getPathInfo();
        int pageSize = 20;

        final MetaClass metaClass = this.getModel().getMetaClass(Target.class.getName());

        if (!this.checkStarted(request, response)) {
            return;
        }

        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return;
        }

        final String displayName = ServletUtil.getDisplayName(metaClass);
        request.setAttribute("metaClass", metaClass);
        request.setAttribute("searchMetaClass", metaClass);
        request.setAttribute("displayName", displayName);

        // for handling pop-up roles
/*
        if (null != request.getParameter("isInPopup")) {
            request.setAttribute("isInPopup", request.getParameter("isInPopup"));
        } */

        if (null != request.getParameter(QuickSearch.IS_IN_MODAL_WINDOW)) {
            request.setAttribute(QuickSearch.IS_IN_MODAL_WINDOW,
                request.getParameter(QuickSearch.IS_IN_MODAL_WINDOW));
        }

        if (null != request.getParameter("proteinName")) {
            request.setAttribute("proteinName", request.getParameter("proteinName"));
        }

        if (null != request.getParameter("alias")) {
            request.setAttribute("alias", request.getParameter("alias"));
        }

        if (null != request.getParameter("milestones")) {
            request.setAttribute("status", request.getParameter("milestones"));
        }

        if (null != request.getParameter("targetGroups")) {
            request.setAttribute("targetGroup", request.getParameter("targetGroups"));
        }

        if (null != request.getParameter("projects")) {
            request.setAttribute("project", request.getParameter("projects"));
        }

        if (null != request.getParameter("access")) {
            request.setAttribute("access", request.getParameter("access"));
        }

        /*if (null != request.getParameter("callbackFunction")) {
            request.setAttribute("callbackFunction", request.getParameter("callbackFunction"));
            //  I don't think this is used
        } */

        // end first access page preparation
        try {

            final SearchFilter criteria =
                SearchTarget.getCriteria(version, metaClass, request.getParameterMap());

            final String searchString = request.getParameter(QuickSearch.SEARCH_ALL);
            //criteria.remove("SUBMIT"); //  probably not needed

            QuickSearch.prepareSearchForm(request, version, metaClass, criteria);

            //System.out.println("SearchTarget pageSize [" + pageSize + "]");
            int pageStart = 0;
            String searchBox = "closed";
            if (params.size() > 0) {
                if (null == params.get(QuickSearch.SEARCH_ALL)) {
                    searchBox = "open";
                }
                if (null != params.get("pagesize")) {
                    pageSize = Integer.parseInt(request.getParameter("pagesize"));
                }

                pageStart = QuickSearch.getPageStartForDisplayTag(params, pageSize);
            }

            final Collection<TargetBeanForLists> results =
                SearchTarget.getResults(version, metaClass, criteria, searchString, pageStart, pageSize);
            request.setAttribute("beans", results);

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

            //System.out.println("SearchTarget pageSize [" + pageSize + "]");
            request.setAttribute("control", control);
            request.setAttribute("controlHeader", " ");
            request.setAttribute("resultSize",
                QuickSearch.getCountOfResults(version, metaClass, criteria, searchString));

            Collection<ModelObjectShortBean> beans;
            beans =
                ModelObjectShortBean
                    .getModelObjectShortBeans(PIMSServlet.getAll(version, TargetStatus.class));
            request.setAttribute("targetStatus", beans);
            //System.out.println("SearchTarget targetStatus [" + beans.size() + "]");

            beans =
                ModelObjectShortBean.getModelObjectShortBeans(PIMSServlet.getAll(version, TargetGroup.class));
            request.setAttribute("targetGroups", beans);
            //System.out.println("SearchTarget targetGroup [" + beans.size() + "]");

            //beans = ModelObjectShortBean.getModelObjectShortBeans(PIMSServlet.getAll(version, Project.class));
            beans = PIMSServlet.getPossibleCreateOwners(version);
            request.setAttribute("projects", beans);
            //System.out.println("SearchTarget project [" + beans.size() + "]");

            request.setAttribute("pagesize", pageSize);
            request.setAttribute("searchBox", searchBox);
            //request.setAttribute("targetMetaClass", metaClass);

            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_OK);
            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/search/org.pimslims.model.target.Target.jsp");
            dispatcher.include(request, response);

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

        //System.out.println("SearchTarget.doPost");
    }

    //TODO get under test
    //TODO override doGetCriteria
    public static SearchFilter getCriteria(final ReadableVersion version, final MetaClass metaClass,
        final Map parms) {

        //System.out.println("SearchTarget.getCriteria");
        final SearchFilter criteria = QuickSearch.getCriteria(version, metaClass, parms);

        if (criteria.containsKey("proteinName")) {
            final Map<String, Object> pCriteria =
                Conditions.newMap(Substance.PROP_NAME,
                    Conditions.contains((String) criteria.get("proteinName")));
            criteria.put(Target.PROP_PROTEIN, pCriteria);
            criteria.remove("proteinName");
        }

        if (criteria.containsKey("alias")) {
            final String aliasValue = (String) criteria.get("alias");
            criteria.remove("alias");
            criteria.put(Target.PROP_ALIASES,
                Conditions.newMap(Alias.PROP_NAME, Conditions.contains(aliasValue)));
        }

        if (criteria.containsKey("milestones")) {
            final Collection<TargetStatus> tss = (Collection<TargetStatus>) criteria.get("milestones");
            criteria.remove("milestones");
            if (!tss.isEmpty()) {
                assert 1 == tss.size() : "Searching for multiple statuses is not yet implemented";
                final TargetStatus ts = tss.iterator().next();

                final Map<String, Object> mCriteria = Conditions.newMap(Milestone.PROP_STATUS, ts);

                mCriteria
                    .put(
                        Milestone.PROP_DATE,
                        new HQLCondition(
                            " milestones.date=(select max(milestone2.date) from "
                                + Milestone.class.getName()
                                + " milestone2 where milestone2.target=A) and  milestones.dbId=(select max(milestone3.dbId) from "
                                + Milestone.class.getName()
                                + " milestone3 where milestone3.target=A and milestone3.date=milestones.date )"));

                criteria.put(Target.PROP_MILESTONES, mCriteria);
            }
        } else {
/* This was intended to enable us to show date of most recent milestone,
 * but it restricts the  results to targets for which a milestone is set
            final Map<String, Object> mCriteria =
                Conditions.newMap(Milestone.PROP_DATE, new HQLCondition(
                    " milestones.date=(select max(milestone2.date) from " + Milestone.class.getName()
                        + " milestone2 where milestone2.target=A) "));
            criteria.put(Target.PROP_MILESTONES, mCriteria);
            */
        }

        if (criteria.containsKey("status")) {
            final TargetStatus ts = version.get((String) criteria.get("status"));
            criteria.remove("status");
            criteria.put(Target.PROP_MILESTONES, Conditions.newMap(Milestone.PROP_STATUS, ts));
        }

        return criteria;
    }

    /**
     * 
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
    static Collection<TargetBeanForLists> getResults(final ReadableVersion version,
        final MetaClass metaClass, final SearchFilter criteria, final String searchString,
        final int pageStart, final int pageSize) throws ServletException, IOException {

        //System.out.println("SearchTarget.getResults [" + pageStart + ":" + pageSize + "]");
        //System.out.println("SearchTarget.getResults searchString [" + searchString + "]");
        //SearchTarget.printMap(criteria);

        final Collection<TargetBeanForLists> results = new ArrayList<TargetBeanForLists>();
        final Paging paging = new Paging(pageStart, pageSize);
        final LinkedHashMap<String, Order> orderBys = new LinkedHashMap<String, Order>();
        // unfortunately, this restricts the results to ones with milestone orderBys.put("milestones.date", Order.DESC);
        orderBys.put(LabBookEntry.PROP_DBID, Order.DESC);
        paging.setOrderBy(orderBys);
        Collection<ModelObject> targets = new ArrayList<ModelObject>();
        //TODO use new Report class, see SearchExperiment
        final Searcher s = new Searcher(version);
        if (SearchTarget.isValid(searchString)) {
            targets = s.search(metaClass, criteria.getMap(), searchString, paging);
        } else {
            targets = s.search(criteria.getMap(), metaClass, paging);
        }

        for (final ModelObject target : targets) {
            results.add(new TargetBeanForLists((Target) target));
        }

        return results;
    }

    /**
     * valid String
     * 
     * @param s
     * @return
     */
    private static boolean isValid(final String s) {
        return QuickSearch.isValid(s);
    }
}
