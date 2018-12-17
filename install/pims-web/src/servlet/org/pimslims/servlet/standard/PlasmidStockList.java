/**
 * pims-web org.pimslims.servlet.standard PlasmidStockList.java
 * 
 * @author susy
 * @date 2 Jul 2009
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2009 susy The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.standard;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.presentation.PlasmidStockBean;
import org.pimslims.presentation.construct.SyntheticGeneManager;
import org.pimslims.search.Paging;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.QuickSearch;

/**
 * PlasmidStockList
 * 
 */
public class PlasmidStockList extends PIMSServlet {

    /**
     * Constructor for PlasmidStockList
     */
    public PlasmidStockList() {
        super();
        // 
    }

    /**
     * PlasmidStockList.getServletInfo
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "PlasmidStockList Prepare a list of PlasmidStockBeans and html header, then forward to the presentation page";
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

            final String stockType = "Plasmid stock";
            final Map<String, Object> pstockAttrMap = SyntheticGeneManager.makeExpMap(version, stockType);
            // no, this is too expensive:
            //final int totalpsExpts = version.findAll(Experiment.class, pstockAttrMap).size();

            final int totalExpts = version.count(Experiment.class, pstockAttrMap);
            final Collection<PlasmidStockBean> psBeans = PlasmidStockList.getPlasmidStock(version, paging);

            request.setAttribute("mayUpdate", Boolean.TRUE);
            request.setAttribute("psBeans", psBeans);
            request.setAttribute("pagesize", pageSize);
            request.setAttribute("resultSize", totalExpts);
            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/standard/PlasmidStockList.jsp");
            //r21168 changed from dispatcher.forward because need include response from another Servlet within the caller Servlet 
            dispatcher.include(request, response);
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

    static Collection<PlasmidStockBean> getPlasmidStock(final ReadableVersion version, final Paging paging) {
        final Collection<PlasmidStockBean> psBeans = new java.util.ArrayList<PlasmidStockBean>();

        final String stockType = "Plasmid stock";
        final Map<String, Object> expAttrMap = SyntheticGeneManager.makeExpMap(version, stockType);
        //Now find all experiments of type Plasmid stock

        final java.util.Collection<Experiment> expts = version.findAll(Experiment.class, expAttrMap, paging);
        for (final Experiment experiment : expts) {
            final PlasmidStockBean psBean = PlasmidStockBean.getPlasmidStockBean(experiment);
            psBeans.add(psBean);
        }
        return psBeans;
    }

    private boolean isValid(final String s) {
        if (s != null && s.trim().length() > 0) {
            return true;
        }
        return false;
    }
}