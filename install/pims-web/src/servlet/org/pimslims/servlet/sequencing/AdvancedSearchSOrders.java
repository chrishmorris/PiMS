/**
 * *
 * 
 * @author Petr Troshin
 * @date March 2009
 * 
 *       Protein Information Management System
 * @version: 3.1
 * 
 *           Copyright (c) 2009 Petr Troshin * *
 * 
 */
package org.pimslims.servlet.sequencing;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.Util;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.search.Paging;
import org.pimslims.search.Paging.Order;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.utils.experiment.ExperimentWizardCreator;
import org.pimslims.utils.sequenator.ParameterSearcher;
import org.pimslims.utils.sequenator.SequencingInputDataParser;

/**
 * Custom Search
 * 
 */

public class AdvancedSearchSOrders extends PIMSServlet {

    private static final long serialVersionUID = -23116567408485L;

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Custom Search for Sequencing orders ";
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse) Search in Sequencing Order Experiment parameters Logic:
     * 
     *      1. Get Paramaters with appropriate name 2. Match the value 3.
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        //TODO comment out
        final Map<String, String[]> pars = request.getParameterMap();
        for (final String key : pars.keySet()) {
            System.out.println(key + ":" + pars.get(key)[0]);
        }

        final String searchAllVal = request.getParameter("searchAll");

        final String page = request.getParameter("page");
        int pageNum = 1;
        if (!Util.isEmpty(page)) {
            pageNum = Integer.parseInt(page);
        }

        final int pageSize = 100; //1000=~100 experiments =~ 1 plate. Parameters but much fewer Orders 
        ReadableVersion version = null;
        try {
            version = this.getReadableVersion(request, response);

            ParameterSearcher ps = null;
            List<Experiment> exps = null;
            int numberOfPages = 0;
            int count = 0;

            if (Util.isEmpty(searchAllVal)) {
                final String searchFromDate = request.getParameter("dateFrom");
                final String searchToDate = request.getParameter("dateTo");
                final Calendar from = ExperimentWizardCreator.getCalendar(searchFromDate);
                final Calendar to = ExperimentWizardCreator.getCalendar(searchToDate);
                ps = new ParameterSearcher(version, from, to);
                //TODO make sure only one method recieves not empty value!
                ps.setSearchByAccountNum(request.getParameter("accountNum"));
                ps.setSearchByDepartment(request.getParameter("department"));
                ps.setSearchByEmail(request.getParameter("email"));
                ps.setSearchByPiName(request.getParameter("pi"));
                ps.setSearchByPrimerName(request.getParameter("pname"));
                ps.setSearchBySoId(request.getParameter("soid"));
                ps.setSearchByTemplateName(request.getParameter("tname"));
                ps.setSearchByUserName(request.getParameter("user"));

                // This is to do with result pagination
                count = ps.getCount();
                numberOfPages = new Double(Math.ceil((double) count / pageSize)).intValue();
                if (count != 0) {
                    exps = ps.getResults(this.getPaging(pageNum, pageSize, count));
                }

            } else {
                //  Now searchAll
                ps = new ParameterSearcher(version);
                // This is to do with result pagination
                count = ps.getCount(searchAllVal);
                numberOfPages = new Double(Math.ceil((double) count / pageSize)).intValue();
                if (count != 0) {
                    exps = ps.searchAll(searchAllVal, this.getPaging(pageNum, pageSize, count));
                }
            }
            System.out.println("Criteria used: ");
            ps.printCriteria();

            if (exps != null && exps.size() == 1) {
                final Experiment exp = exps.iterator().next();
                final String soid =
                    org.pimslims.utils.experiment.Utils.getParameterValue(exp,
                        SequencingInputDataParser.orderParamName);
                this.redirect(response, request.getContextPath() + "/read/ViewSequencingOrder/" + soid);
            } else {

                // Cannot send this earlier as redirect will not work
                // send the header now, since searching may take some time
                //this.writeHead(request, response, "Search Sequencing Orders");
                // do the search

                if (exps == null || exps.isEmpty()) {
                    request.setAttribute("message", "No record matches the criteria!");
                }

                final RequestDispatcher rd =
                    request.getRequestDispatcher("/JSP/sequencing/AdvSearchMain.jsp");

                request.setAttribute("exps", exps);

                request.setAttribute("rcount", count);
                // This just converts Map<String, String[]> to Map<String, String> as i know there are no params with multiple values
                final Map<String, String> params = ServletUtil.convert(request.getParameterMap());
                request.setAttribute("params", params);
                request.setAttribute("pagesize", pageSize);
                request.setAttribute("npages", numberOfPages);
                request.setAttribute("curPageNum", pageNum);
                request.setAttribute("criteria", this.encodeParameters(params));

                rd.forward(request, response);

            }

            this.commit(request, response, version);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    //TODO use QuickSearch.getPaging
    private Paging getPaging(final int curPageNum, final int pageSize, final int totalRecords) {
        assert pageSize != 0 && totalRecords != 0;
        assert curPageNum != 0 : "Page numeration must start from 1!";
        final int numberOfPages = totalRecords / pageSize;
        assert numberOfPages <= totalRecords;
/*
        System.out.println("c" + totalRecords);
        System.out.println("np" + numberOfPages);
        System.out.println("ps" + pageSize);
        System.out.println("Paging: " + (curPageNum - 1) * pageSize + ":" + (curPageNum * pageSize)); */

        return new Paging((curPageNum - 1) * pageSize, (curPageNum * pageSize),
            LabBookEntry.PROP_CREATIONDATE, Order.DESC);
    }

    private String encodeParameters(final Map<String, String> criteria) {
        String encoded = "";
        for (final String key : criteria.keySet()) {
            if (!Util.isEmpty(criteria.get(key))) {
                if (key.equals("page")) {
                    // Do not add page parameter it will be added later
                    continue;
                }
                encoded +=
                    StringEscapeUtils.escapeHtml(key) + "=" + StringEscapeUtils.escapeHtml(criteria.get(key))
                        + "&";
            }
        }
        return Util.isEmpty(encoded) ? "" : encoded.substring(0, encoded.length() - 1);
    }
}
