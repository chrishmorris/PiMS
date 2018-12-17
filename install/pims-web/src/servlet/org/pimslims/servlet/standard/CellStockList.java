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
import org.pimslims.presentation.CellStockBean;
import org.pimslims.presentation.construct.SyntheticGeneManager;
import org.pimslims.search.Paging;

/**
 * Servlet implementation class for Servlet: CellStockList
 * 
 */
public class CellStockList extends org.pimslims.servlet.PIMSServlet {

    /**
     * Constructor for CellStockList
     */
    public CellStockList() {
        super();
        // 
    }

    /**
     * CellStockList.getServletInfo
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "CellStockList Prepare a list of CellStockBeans and html header, then forward to the presentation page";
    }

    /**
     * CellStockList.doGet
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
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
            int pn = 1;
            if (params.size() > 0) {
                //pagesize is from pimsWidget:pageControls tag
                if (this.isValid(request.getParameter("pagesize"))) {
                    pageSize = Integer.parseInt(request.getParameter("pagesize"));
                }

                if (null != params.get("pagenumber")) {
                    pageStart = (Integer.parseInt(((String[]) params.get("pagenumber"))[0]) - 1) * pageSize;

                    //need to get value for pagenumber from ListBeanspaging.jsp
                    pn = Integer.parseInt(request.getParameter("pagenumber"));
                }
            }
            request.setAttribute("pagenumber", pn);
            final Paging paging = new Paging(pageStart, pageSize);

            final String stockType = "Cell stock";
            final Map<String, Object> cStockAttrMap = SyntheticGeneManager.makeExpMap(version, stockType);

            final int totalExpts = version.count(Experiment.class, cStockAttrMap);
            final Collection<CellStockBean> csBeans = CellStockList.getCellStock(version, paging);

            request.setAttribute("mayUpdate", Boolean.TRUE);
            request.setAttribute("csBeans", csBeans);
            request.setAttribute("pagesize", pageSize);
            request.setAttribute("resultSize", totalExpts);
            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/standard/CellStockList.jsp");
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

    static Collection<CellStockBean> getCellStock(final ReadableVersion version, final Paging paging) {
        final Collection<CellStockBean> csBeans = new java.util.ArrayList<CellStockBean>();

        final String stockType = "Cell stock";
        final Map<String, Object> expAttrMap = SyntheticGeneManager.makeExpMap(version, stockType);
        //Now find all experiments of type Cell stock

        final java.util.Collection<Experiment> expts = version.findAll(Experiment.class, expAttrMap, paging);
        for (final Experiment experiment : expts) {
            final CellStockBean csBean = CellStockBean.getCellStockBean(experiment);
            csBeans.add(csBean);
        }

        return csBeans;
    }

    private boolean isValid(final String s) {
        if (s != null && s.trim().length() > 0) {
            return true;
        }
        return false;
    }

}