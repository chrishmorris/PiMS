/*
 * XtalPIMSServlet.java
 *
 * Created on 16 October 2007, 21:38
 */
package org.pimslims.crystallization.servlet.deprecated;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.business.DataStorage;
import org.pimslims.business.exception.BusinessException;

/**
 *
 * @author Ian Berry
 * @version
 */
abstract class XtalPIMSServlet extends HttpServlet {

    private PrintWriter out = null;
    private StringBuffer sessionBookmark = null;
    private int maxResults = 10;
    private int firstResult = 0;
    private String sort = null;
    private boolean ascending = false;

    protected void processParams(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException, BusinessException {

        response.setContentType("text/json;charset=UTF-8");
        setOut(response.getWriter());

        String maxResultsStr = request.getParameter("results");
        String firstResultStr = request.getParameter("startIndex");
        setSort(request.getParameter("sort"));
        String dirStr = request.getParameter("dir");

        setSessionBookmark(new StringBuffer());
        if (firstResultStr != null) {
            getSessionBookmark().append("startIndex-" + firstResultStr);
            setFirstResult(Integer.parseInt(firstResultStr));
        }
        if (maxResultsStr != null) {
            getSessionBookmark().append("_results-" + maxResultsStr);
            setMaxResults(Integer.parseInt(maxResultsStr));
        }
        if (getSort() != null) {
            sessionBookmark.append("_sort-" + getSort());
        }
        if (dirStr != null) {
            getSessionBookmark().append("_dir-" + dirStr);
            if (dirStr.equals("asc")) {
                setAscending(true);
            } else {
                setAscending(false);
            }
        }
        getDataStorage().openResources(request.getRemoteUser(), request.isUserInRole(DataStorage.ADMINISTRATOR), request.isUserInRole(DataStorage.ADMINISTRATOR));
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    abstract protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    abstract protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
    private DataStorage dataStorage = null;

    @Override
    public final void destroy() {
        try {
            if (getDataStorage() != null) {
                getDataStorage().closeResources();
            }
        } catch (BusinessException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public final void init(ServletConfig config) throws ServletException {
        super.init(config);
        setDataStorage((DataStorage) config.getServletContext().getAttribute("dataStorage"));
        if (getDataStorage() == null) {
            throw new ServletException("Unable to get access to the database...");
        }
    }

    public final DataStorage getDataStorage() {
        return dataStorage;
    }

    public final void setDataStorage(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public StringBuffer getSessionBookmark() {
        return sessionBookmark;
    }

    public void setSessionBookmark(StringBuffer sessionBookmark) {
        this.sessionBookmark = sessionBookmark;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }
    // </editor-fold>
}
