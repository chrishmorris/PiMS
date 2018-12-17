/**
 * pims-web org.pimslims.servlet.complex BrowseComplexServlet.java
 * 
 * @author Marc Savitsky
 * @date 18 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky * *
 * 
 */
package org.pimslims.servlet.complex;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.presentation.ResearchObjectiveElementBeanI;
import org.pimslims.presentation.ComplexBean;
import org.pimslims.presentation.target.ResearchObjectiveElementBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * BrowseComplexServlet
 * 
 */
public class BrowseComplexServlet extends PIMSServlet {

    /**
     * 
     */
    public BrowseComplexServlet() {
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
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        if (!this.checkStarted(request, response)) {
            return;
        }

        String pagerSize = "20";
        final String rowNum = "off";
        final String tableId =
            new ParamEncoder("browse_table1").encodeParameterName(TableTagParameters.PARAMETER_PAGE);
        //final String exportId =
        new ParamEncoder("browse_table1").encodeParameterName(TableTagParameters.PARAMETER_EXPORTTYPE);
        int pageStart = 0;
        int resultSize = 0;
        //boolean export = false;

        final Map params = request.getParameterMap();
        if (params.size() > 0) {
            if (null != params.get("pagerSize")) {
                pagerSize = ((String[]) params.get("pagerSize"))[0];
            }

            if (null != params.get(tableId)) {
                pageStart =
                    (Integer.parseInt(((String[]) params.get(tableId))[0]) - 1) * Integer.parseInt(pagerSize);
            }
/*
            if (null != params.get(exportId)) {
                export = true;
            } */
        }

        Collection<ComplexBean> results = null;
        final PrintWriter writer = response.getWriter();
        final ReadableVersion rv = this.getReadableVersion(request, response);
        if (rv == null) {
            return;
        }

        try {

            final List<ComplexBean> myList = new ArrayList<ComplexBean>(this.getComplexes(rv));
            resultSize = myList.size();

            if (resultSize == 0) {
                this.writeHead(request, response, "Browse Complexes");
                writer.print(" No Complexes are recorded yet.<br />"
                    + "<a href='NewComplex'>Create a complex</a>"

                );
                PIMSServlet.writeFoot(writer, request);
                return;
            }
            // now show the list

            int toIndex = Integer.parseInt(pagerSize);
            if (toIndex > resultSize) {
                toIndex = resultSize;
            }

            results = myList.subList(pageStart, toIndex);

            //this.writeHead(request, response, "");

            //ProgressListener progress =
            //    new ProgressListener(results.size(), writer, "Formatting complex list...");

            final HttpSession session = request.getSession();
            session.setAttribute("_Complexes_List_", results); //TODO no, could overflow memory
            session.setAttribute("resultSize", new Integer(resultSize));
            session.setAttribute("pagerSize", new Integer(pagerSize).toString());
            //request.setAttribute("includeHeader", new Boolean(false));

            final StringBuffer parameters = new StringBuffer("?");
            if (params.size() > 0) {
                for (final Iterator iter = params.entrySet().iterator(); iter.hasNext();) {
                    final Map.Entry elem = (Map.Entry) iter.next();
                    parameters.append(elem.getKey() + "=" + ((String[]) elem.getValue())[0] + "&");
                }
                parameters.deleteCharAt(parameters.length() - 1);
            } else {
                parameters.append("pagerSize=" + pagerSize + "&rowNum=" + rowNum);
            }

            final ServletContext sCont = this.getServletContext();
            final RequestDispatcher dispatcher =
                sCont.getRequestDispatcher("/JSP/complex/browseComplexes.jsp" + parameters.toString());
            dispatcher.include(request, response);
            rv.commit();
        } catch (final AbortedException e1) {
            this.writeHead(request, response, "Browse Complexes");
            writer.print(" Sorry, there has been a problem, please try again.");
            PIMSServlet.writeFoot(writer, request);
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }

    }

    private Collection<ComplexBean> getComplexes(final ReadableVersion version) {

        final Set<ComplexBean> results = new TreeSet<ComplexBean>(ComplexBean.BLUEPRINT_ORDER);

        final Collection<ResearchObjectiveElement> components =
            BrowseComplexServlet.getResearchObjectiveElementsList(version, "complex");

        for (final ResearchObjectiveElement roe : components) {
            final ComplexBean complexBean =
                ResearchObjectiveElementBean.readComplexHook(version, roe.getResearchObjective().get_Hook());
            results.add(complexBean);
        }

        return results;
    }

    /**
     * get all blueprintComponents which component type is 'type'
     * 
     * 
     * @param rv
     * @return Collection<ResearchObjectiveElement> which is ordered
     */

    private static Collection<ResearchObjectiveElement> getResearchObjectiveElementsList(
        final ReadableVersion rv, final String type) {

        final Set<ResearchObjectiveElementBeanI> collection = new HashSet<ResearchObjectiveElementBeanI>();

        final Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put(org.pimslims.model.target.ResearchObjectiveElement.PROP_COMPONENTTYPE, type);
        final Collection<ResearchObjectiveElement> blueprints =
            rv.findAll(org.pimslims.model.target.ResearchObjectiveElement.class, criteria);

        return blueprints;
    }
}
