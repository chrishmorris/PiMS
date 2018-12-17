/*
 * XtalPIMSServlet.java Created on 16 October 2007, 21:38
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.business.DataStorage;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.servlet.PIMSServlet;

/**
 * 
 * @author Ian Berry
 * @version
 */
public abstract class XtalPIMSServlet extends PIMSServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6527234514518674138L;

    private DataStorageFactory factory = null;

    protected void setContentTypeJson(final HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
    }

    protected void setNoCache(final HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
        response.setHeader("Expires", "Sun, 15 Jan 1998 17:00:00 GMT");
        response.setHeader("Pragma", "no-cache");
    }

    protected DataStorage openResources(HttpServletRequest request) {
        DataStorage dataStorage = getDataStorage();
        try {
            if (dataStorage != null) {
                dataStorage
                    .openResources(request.getRemoteUser(), request.isUserInRole(DataStorage.ADMINISTRATOR),
                        request.isUserInRole(DataStorage.ADMINISTRATOR));
                //System.out.println("Open resource for " + this);

            }
        } catch (BusinessException ex) {
            throw new RuntimeException(ex);
        }
        return dataStorage;
    }

    protected DataStorage openResourcesReadAll(HttpServletRequest request) {
        DataStorage dataStorage = getDataStorage();
        try {
            if (dataStorage != null) {
                dataStorage.openResources(request.getRemoteUser(), true, request
                    .isUserInRole(DataStorage.ADMINISTRATOR));
                //System.out.println("Open resource for " + this);

            }
        } catch (BusinessException ex) {
            throw new RuntimeException(ex);
        }
        return dataStorage;
    }

    protected void closeResources(DataStorage dataStorage) {
        try {
            if (dataStorage != null) {
                dataStorage.closeResources();
                //System.out.println("Close resources in  " + this);
            }
        } catch (BusinessException ex) {
            ex.printStackTrace(); // can we report this error?
        }
    }

    private final DataStorage getDataStorage() {
        return this.factory.getDataStorage();
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    abstract protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException;

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    abstract protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException;

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Abstract XtalPiMS Serlvet";
    }

    @Override
    public final void init(ServletConfig config) throws ServletException {
        super.init(config);
        factory = (DataStorageFactory) config.getServletContext().getAttribute("dataStorageFactory");
    }

}
