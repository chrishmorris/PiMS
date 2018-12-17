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
package org.pimslims.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.presentation.create.ValueConverter;
import org.pimslims.search.Paging;
import org.pimslims.search.Paging.Order;
import org.pimslims.search.Searcher;

/**
 * Search Note that there are some custom searches with more specific mappings
 */
@javax.servlet.annotation.WebServlet("/Search/*")
public class QuickSearch extends PIMSServlet {

    /**
     * IS_IN_MODAL_WINDOW String
     */
    public static final String IS_IN_MODAL_WINDOW = "isInModalWindow";

    /**
     * SUBMIT String
     */
    public static final String SUBMIT = "SUBMIT";

    /**
     * SEARCH_ALL String
     */
    public static final String SEARCH_ALL = "search_all";

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
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final java.io.PrintWriter writer = response.getWriter();
        //final Map params = request.getParameterMap();
        final String metaClassName = this.getMetaClassName(request);

        if (metaClassName == null || "".equals(metaClassName)) {
            response.setStatus(HttpServletResponse.SC_OK);
            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/customSearch/WhichClass.jsp");
            dispatcher.forward(request, response);
            return;
        }
/*
        if ("org.pimslims.model.target.ResearchObjective".equals(metaClassName)
            || "org.pimslims.model.experiment.ExperimentGroup".equals(metaClassName)) {
            response.setStatus(HttpServletResponse.SC_OK);
            String dispatchURL = "/Search/" + metaClassName;
            if (null != request.getParameter("_only_experiment_groups")) {
                dispatchURL += "?_only_experiment_groups=true";
            }
            final RequestDispatcher dispatcher = request.getRequestDispatcher(dispatchURL);
            dispatcher.forward(request, response);
            return;
        } */

        final MetaClass metaClass = this.getModel().getMetaClass(metaClassName);

        if (!this.checkStarted(request, response)) {
            return;
        }

        if (metaClass == null) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_NOT_FOUND);
            writer.print("Unknown type: " + metaClassName);
            // LATER could offer drop down menu from model.getClassNames()
            return;
        }
        QuickSearch.parmToAttribute(request);

        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return;
        }

        try {

            this.setSpecialBeans(request, metaClass, version);

            final String displayName = ServletUtil.getDisplayName(metaClass);
            request.setAttribute("metaClass", metaClass);
            request.setAttribute("searchMetaClass", metaClass);
            request.setAttribute("displayName", displayName);

            final Map criteria = this.doGetCriteria(version, metaClass, request.getParameterMap());

            final Paging paging = QuickSearch.getPaging(request.getParameterMap(), metaClass);
            final String search_all = request.getParameter(QuickSearch.SEARCH_ALL);
            final int countOfResults =
                QuickSearch.getCountOfResults(version, metaClass, criteria, search_all);

            if (countOfResults == 1
                && (request.getParameter("isInPopup") == null && request
                    .getParameter(QuickSearch.IS_IN_MODAL_WINDOW) == null)) {
                final List<ModelObjectBean> beans =
                    QuickSearch.search(version, metaClass, paging, criteria, search_all);
                this.redirect(response, request.getContextPath() + "/View/"
                    + beans.iterator().next().getHook());
            } else {

                // Cannot send this earlier as redirect will not work
                // send it now, because search may be slow
                this.writeHead(request, response, "Search " + displayName + "s");

                final int totalRecords = QuickSearch.getCountOfRecords(version, metaClass);
                //TODO could display more than count, some sort of KPI
                request.setAttribute("totalRecords", totalRecords);

                request.setAttribute("resultSize", countOfResults);

                QuickSearch.setPagingAttributes(request, paging);
                final List<ModelObjectBean> beans =
                    QuickSearch.search(version, metaClass, paging, criteria, search_all);
                request.setAttribute("beans", beans);
                final String searchBox = "closed";
                request.setAttribute("searchBox", searchBox);
                request.setAttribute(QuickSearch.SEARCH_ALL, search_all);

                QuickSearch.passDisplayAttributes(request, metaClass, beans);
                QuickSearch.passSearchAttributes(request, metaClass, version, totalRecords);

                PIMSServlet.dispatchCustomJsp(request, response, metaClass.getMetaClassName(), "search",
                    this.getServletContext());
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
     * QuickSearch.setPagingAttributes
     * 
     * @param request
     * @param paging
     */
    public static void setPagingAttributes(final HttpServletRequest request, final Paging paging) {
        final int limit = Math.max(1, paging.getLimit());
        request.setAttribute("pagesize", limit);
        request.setAttribute("pagenumber", 1 + paging.getStart() / limit);
    }

    /**
     * QuickSearch.doGetCriteria
     * 
     * @param version
     * @param metaClass
     * @param parameterMap
     * @return
     */
    protected Map doGetCriteria(final ReadableVersion version, final MetaClass metaClass,
        final Map parameterMap) {
        return QuickSearch.getOnlyCriteria(version, metaClass, parameterMap);
    }

    public static void passDisplayAttributes(final HttpServletRequest request, final MetaClass metaClass,
        final Collection<ModelObjectBean> beans) {
        final Map<String, MetaAttribute> attributes = new TreeMap<String, MetaAttribute>();
        for (final String name : ListRole.getAttributeNames(beans)) {
            attributes.put(name, metaClass.getAttribute(name)); // getAttribute(name)
        }
        request.setAttribute("attributes", QuickSearch.sortAttributes(attributes));
    }

    public static void passSearchAttributes(final HttpServletRequest request, final MetaClass metaClass,
        final ReadableVersion version, final int totalRecords) {
        Map searchAttributes = Collections.EMPTY_MAP;
        if (totalRecords != 0 && totalRecords != -1) {
            final Searcher s = new Searcher(version);
            searchAttributes = s.getSearchableFields(ServletUtil.getPIMSMetaClass(metaClass));
        }
        request.setAttribute("searchAttributes", QuickSearch.sortAttributes(searchAttributes).keySet());
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

    protected void setSpecialBeans(final HttpServletRequest request, final MetaClass metaClass,
        final ReadableVersion version) {
        // override this if extra beans are needed for a custom JSP
    }

    /**
     * process the request parameters to find the search criteria. See SearchForm.jsp TODO support filtering
     * by lab notebook
     * 
     * @param parms
     * @return
     */
    public static Map<String, Object> getCriteria(final ReadableVersion rv, final MetaClass metaClass,
        final Map<String, String[]> parms) {
        final Map<String, Object> criteria = new java.util.HashMap<String, Object>();
        final Map errors = new HashMap();
        final ValueConverter vc = new ValueConverter(metaClass, errors);
        for (final Iterator iter = parms.keySet().iterator(); iter.hasNext();) {
            final String name = (String) iter.next();
            final String[] values = parms.get(name);
            if (null == values || 0 == values.length) {
                continue;
            }
            if (1 == values.length) {
                final String value = values[0].trim();
                if (!"".equals(value)) {
                    // TODO add conversion code for floats, roles
                    // This can reuse the code for method
                    // org.pimslims.servlet.Edit.getParms()
                    if (metaClass.getAttributes().containsKey(name)) {
                        //final MetaAttribute attribute = metaClass.getAttributes().get(name);
                        final Object convertedValue = vc.getConvertedValue(name, value);
                        criteria.put(name, convertedValue);

                    } else if (metaClass.getMetaRoles().containsKey(name)) {

                        // Frig to improve performance of search 
                        if (metaClass.getMetaClassName().equals(Sample.class.getName())
                            && name.equals(AbstractSample.PROP_SAMPLECATEGORIES)) {
                            final Map<String, Object> scCriteria = new HashMap<String, Object>();
                            scCriteria.put(SampleCategory.PROP_NAME, value);
                            criteria.put(name, scCriteria);
                        } else {
                            ModelObject mo = rv.get(value);
                            if (null == mo) {
                                // find by name
                                final MetaRole metaRole = metaClass.getMetaRoles().get(name);
                                final MetaClass otherMetaClass = metaRole.getOtherMetaClass();
                                mo = rv.findFirst(otherMetaClass.getJavaClass(), "name", value);
                            }
                            criteria.put(name, mo);
                        }

                    } else {//how come we get here for "pagesize" and "pagenumber"?
                        criteria.put(name, value /* QuickSearch.convertValue(String.class, value) */);
                    }
                }
                continue;
            }
            // multiple values have been returned, for multi-valued attribute
            // note that the API doesn't actually work in this case
            criteria.put(name, values);
        }
        if (!errors.isEmpty()) {
            throw new RuntimeException((String) errors.values().iterator().next());
        }
        return criteria;
    }

    @Deprecated
    // dont use displaytag!
    public static int getPageStartForDisplayTag(final Map params, final int pageSize) {
        final String tableId =
            new ParamEncoder("mytable").encodeParameterName(TableTagParameters.PARAMETER_PAGE);
        int pageStart = 0;
        if (null != params.get(tableId)) {
            pageStart = (Integer.parseInt(((String[]) params.get(tableId))[0]) - 1) * pageSize;
        }
        return pageStart;
    }

    /**
     * QuickSearch.parmToAttribute
     * 
     * @param request
     */
    public static void parmToAttribute(final HttpServletRequest request) {
        // for handling pop-up roles
        if (null != request.getParameter("isInPopup")) {
            request.setAttribute("isInPopup", request.getParameter("isInPopup"));
        }
        if (null != request.getParameter(QuickSearch.IS_IN_MODAL_WINDOW)) {
            request.setAttribute(QuickSearch.IS_IN_MODAL_WINDOW,
                request.getParameter(QuickSearch.IS_IN_MODAL_WINDOW));
        }
        if (null != request.getParameter("barcodeSearch")) {
            request.setAttribute("barcodeSearch", request.getParameter("barcodeSearch"));
        }
        if (null != request.getParameter("sampleCategories")) {
            request.setAttribute("sampleCategories", request.getParameter("sampleCategories"));
        }
        if (null != request.getParameter("experimentGroup")) {
            request.setAttribute("experimentGroup", request.getParameter("experimentGroup"));
        }
        if (null != request.getParameter("hook")) {
            request.setAttribute("hook", request.getParameter("hook"));
        }
        if (null != request.getParameter("callbackFunction")) {
            request.setAttribute("callbackFunction", request.getParameter("callbackFunction"));
        }
    }

    /**
     * 
     * QuickSearch.search
     * 
     * @param parameters
     * @return
     */
    public static List<ModelObjectBean> search(final ReadableVersion version, final MetaClass metaClass,
        final Paging paging, final Map<String, Object> criteria, final String search_all)
        throws ServletException {
        //TODO use new Report class, see SearchExperiment
        final Searcher s = new Searcher(version);
        Collection<ModelObject> results = new ArrayList<ModelObject>();

        if (null != search_all) {
            results = s.search(metaClass, criteria, search_all, paging);
        } else {
            results = s.search(criteria, metaClass, paging);
        }

        final List<ModelObjectBean> beans = new ArrayList<ModelObjectBean>();
        for (final ModelObject result : results) {
            beans.add(BeanFactory.newBean(result));
        }
        return beans;
    }

    //TODO merge with setPagingAttributes
    public static Paging getPaging(final Map<String, String[]> parameters, final MetaClass metaClass) {
        int pageSize = 20;
        if (null != parameters.get("pagesize") && 0 < parameters.get("pagesize").length
            && !"".equals(parameters.get("pagesize")[0])) {
            pageSize = Integer.parseInt((parameters.get("pagesize"))[0]);
        }

        int pageStart = 0;
        if (null != parameters.get("pagenumber") && 0 < parameters.get("pagenumber").length
            && !"".equals(parameters.get("pagenumber")[0])) {
            final int pageNumber = Integer.parseInt((parameters.get("pagenumber")[0]));
            pageStart = (pageNumber - 1) * pageSize;
        }
        final Paging paging = new Paging(pageStart, pageSize);
        if (metaClass.getAttributes().containsKey("name")) {
            paging.addOrderBy("name", Order.ASC);
        }
        return paging;
    }

    /**
     * Sort the attributes with Name to the front
     * 
     * Search.sortAttributes
     * 
     * @param attributes
     * @return
     */
    public static Map<String, MetaAttribute> sortAttributes(final Map<String, MetaAttribute> attributes) {

        final List list = new LinkedList(attributes.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(final Object o1, final Object o2) {
                final String s1 = (String) ((Map.Entry) o1).getKey();
                if (s1.equalsIgnoreCase("name")) {
                    return -1;
                }
                final String s2 = (String) ((Map.Entry) o2).getKey();
                if (s2.equalsIgnoreCase("name")) {
                    return 1;
                }
                return s1.compareTo(s2);
            }

        });
        final Map<String, MetaAttribute> result = new LinkedHashMap();
        for (final Iterator it = list.iterator(); it.hasNext();) {
            final Map.Entry entry = (Map.Entry) it.next();
            result.put((String) entry.getKey(), (MetaAttribute) entry.getValue());
        }
        return result;

    }

    /**
     * See SearchForm.jsp
     * 
     * @param request the HTTP request to process
     * @param ReadableVersion version
     * @param the metaClass to search
     * @param criteria attribute name => value
     */
    public static void prepareSearchForm(final HttpServletRequest request, final ReadableVersion version,
        final MetaClass metaClass, final Map criteria) throws ServletException {

        int totalRecords = -1;
        Map notEmptyAttributes = Collections.EMPTY_MAP;
        final Searcher s = new Searcher(version);
        totalRecords = s.countRecords(metaClass);

        if (totalRecords != 0 && totalRecords != -1) {
            notEmptyAttributes = s.getSearchableFields(ServletUtil.getPIMSMetaClass(metaClass));
        }

        if (totalRecords == -1) {
            throw new ServletException("Sorry, there likely to has been an error ");
        }

        request.setAttribute("searchAttributes", notEmptyAttributes.keySet());
        request.setAttribute("criteria", criteria);
        request.setAttribute("totalRecords", totalRecords);
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
    public static int getCountOfResults(final ReadableVersion version, final MetaClass metaClass,
        final Map criteria, final String searchString) throws ServletException, IOException {

        int count;
        final Searcher s = new Searcher(version);
        if (QuickSearch.isValid(searchString)) {
            count = s.count(metaClass, criteria, searchString);
        } else {
            count = s.count(criteria, metaClass);
        }

        return count;
    }

    public static int getCountOfRecords(final ReadableVersion version, final MetaClass metaClass)
        throws ServletException {
        //TODO use new Report class, see SearchExperiment
        int totalRecords = -1;
        final Searcher s = new Searcher(version);
        totalRecords = s.countRecords(metaClass);
        return totalRecords;
    }

    /**
     * 
     * QuickSearch.getCriteria
     * 
     * @param version
     * @param metaClass
     * @param parms
     * @return
     */
    public static Map getOnlyCriteria(final ReadableVersion version, final MetaClass metaClass,
        final Map<String, String[]> parms) {

        final Map criteria = QuickSearch.getCriteria(version, metaClass, parms);
        //TODO remove "pagesize" and "pagenumber"
        criteria.remove("isInPopup");
        criteria.remove(QuickSearch.SUBMIT);
        criteria.remove(QuickSearch.SEARCH_ALL);
        return criteria;
    }

    /**
     * valid String
     * 
     * @param s
     * @return
     */
    public static boolean isValid(final String s) {
        if (s != null && s.trim().length() > 0) {
            return true;
        }
        return false;
    }
}
