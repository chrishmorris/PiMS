/**
 * 
 */
package org.pimslims.servlet.protocol;

import java.io.IOException;
import java.io.PrintWriter;
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
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.search.Paging;
import org.pimslims.search.Paging.Order;
import org.pimslims.search.Searcher;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.QuickSearch;
import org.pimslims.servlet.experiment.CreateExperiment;

/**
 * @author cm65
 * 
 */

@javax.servlet.annotation.WebServlet("/Search/org.pimslims.model.protocol.Protocol")
public class SearchProtocol extends PIMSServlet {

    /*
     * (non-Javadoc)
     * TODO make this extend QuickSearch
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Show custom list of protocols";
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

        /*
        System.out.println("org.pimslims.servlet.SearchProtocol.doGet()");
        final Map m = request.getParameterMap();
        for (final Iterator it = m.entrySet().iterator(); it.hasNext();) {
            final Map.Entry e = (Map.Entry) it.next();
            final String[] values = (String[]) e.getValue();
            final StringBuffer s = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                s.append(values[i] + ",");
            }
            s.deleteCharAt(s.length() - 1);
            System.out.println("org.pimslims.servlet.SearchProtocol request parameter [" + e.getKey() + ":"
                + s.toString() + "]");
        }
        */
        int pageSize = 20;
        final ReadableVersion version = this.getReadableVersion(request, response);
        if (null == version) {
            return;
        }

        final PrintWriter writer = response.getWriter();

        final MetaClass protocolMetaClass = this.getModel().getMetaClass(Protocol.class.getName());
        try {

            this.writeHead(request, response, "Search Protocols");
            final Map params = request.getParameterMap();
            final Map<String, Object> criteria = QuickSearch.getCriteria(version, protocolMetaClass, params);

            QuickSearch.prepareSearchForm(request, version, protocolMetaClass, criteria);

            //TODO use QuickSearch.getPaging
            int pageStart = 0;
            if (params.size() > 0) {
                if (this.isValid(request.getParameter("pagesize"))) {
                    pageSize = Integer.parseInt(request.getParameter("pagesize"));
                }

                if (this.isValid(request.getParameter("pagenumber"))) {
                    pageStart = (Integer.parseInt(((String[]) params.get("pagenumber"))[0]) - 1) * pageSize;
                }

            }

            /* Use this for debugging:
            System.out.println("org.pimslims.servlet.experiment.SearchProtocol");
            for (final Iterator iter = criteria.entrySet().iterator(); iter.hasNext();) {
                final Map.Entry entry = (Map.Entry) iter.next();
                final String key = (String) entry.getKey();
                final Object value = entry.getValue();
                if (null == value) {
                    System.out.println("Criteria [" + key + ",null]");
                } else if (value instanceof java.lang.String) {
                    System.out.println("Criteria [" + key + "," + (String) value + "]");
                } else if (value instanceof org.pimslims.model.reference.ExperimentType) {
                    final ExperimentType object = (ExperimentType) value;
                    System.out.println("Criteria [" + key + "," + object.get_Hook() + "]");
                } else {
                    System.out.println("Criteria [" + key + "," + value.toString() + "]");
                }
            }
            */
            //TODO use new Report class, see SearchExperiment
            final Searcher s = new Searcher(version);
            Collection<ModelObject> results;
            //final long start = System.currentTimeMillis();
            final String svalue = (String) criteria.get("search_all");
            int resultsize;
            criteria.remove("SUBMIT");
            criteria.remove("search_all");
            final Paging paging = new Paging(pageStart, pageSize); //TODO remove
            paging.addOrderBy("name", Order.ASC);

            if (this.isValid(svalue)) {
                results = s.search(protocolMetaClass, criteria, svalue, paging);
                resultsize = s.count(protocolMetaClass, criteria, svalue);
            } else {
                results = s.search(criteria, protocolMetaClass, paging);
                resultsize = s.count(criteria, protocolMetaClass);
            }
            // System.out.println("SearchProtocol found " + results.size() + " protocols using "
            //      + (System.currentTimeMillis() - start) + "ms");
            ExperimentType type = null;
            final String selectExpTypeHook = request.getParameter("experimentType");
            if (selectExpTypeHook != null && selectExpTypeHook.length() > 0) {
                type = version.get(selectExpTypeHook);
            }

            if (this.isValid(request.getParameter("experimentType"))) {
                request.setAttribute("experimentType", type);
            }
            //if (null != request.getParameter("status")) {
            request.setAttribute("status", request.getParameter("status"));
            //}
            if (this.isValid(request.getParameter("name"))) {
                request.setAttribute("name", request.getParameter("name"));
            }
            if (this.isValid(request.getParameter("details"))) {
                request.setAttribute("details", request.getParameter("details"));
            }

            request.setAttribute("experimentTypes", ModelObjectShortBean
                .getModelObjectShortBeans(CreateExperiment.activeExperimentTypes(version)));
            request.setAttribute("instrumentTypes", ModelObjectShortBean.getModelObjectShortBeans(PIMSServlet
                .getAll(version, org.pimslims.model.reference.InstrumentType.class)));

            request.setAttribute("totalRecords", version.count(Protocol.class, Collections.EMPTY_MAP));
            request.setAttribute("resultSize", resultsize);
            request.setAttribute("isAdmin", PIMSServlet.isAdmin(request));
            //request.setAttribute("experimentType", type);
            request.setAttribute("protocols", SearchProtocol.getProtocols(results));
            QuickSearch.setPagingAttributes(request, paging);
            request.setAttribute("protocolMetaClass", protocolMetaClass);
            request.setAttribute("searchMetaClass", protocolMetaClass);

            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_OK);
            final RequestDispatcher rd =
                request.getRequestDispatcher("/JSP/search/org.pimslims.model.protocol.Protocol.jsp");
            rd.include(request, response);
            version.commit();
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final ServletException e) {
            throw new ServletException(e);
        } catch (final IOException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    private boolean isValid(final String s) {
        if (s != null && s.trim().length() > 0) {
            return true;
        }
        return false;
    }

    private static Collection<ModelObjectBean> getProtocols(final Collection<ModelObject> protocols) {
        //final long start = System.currentTimeMillis();
        final Collection<ModelObjectBean> ret = new ArrayList<ModelObjectBean>();
        for (final ModelObject mObj : protocols) {
            final Protocol protocol = (Protocol) mObj;
            ret.add(BeanFactory.newBean(protocol));
        }
        //  System.out.println("SearchExperiment found " + ret.size() + " SINGLE experiments using "
        //      + (System.currentTimeMillis() - start) + "ms");

        return ret;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new ServletException("cannot post");
    }

}
