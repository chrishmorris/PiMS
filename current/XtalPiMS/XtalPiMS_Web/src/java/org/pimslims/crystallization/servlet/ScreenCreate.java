/*
 * PlateServlet.java Created on 19 October 2007, 14:54
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.view.ScreenTypeViewDAO;
import org.pimslims.crystallization.implementation.view.ScreenTypeView;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.holder.RefHolder;
import org.pimslims.model.holder.RefHolderSource;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.servlet.PIMSServlet;

/**
 * 
 * @author Bill Lin
 * @version
 */
public class ScreenCreate extends PIMSServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7643062815567251311L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * 
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void processRequest(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final ReadableVersion rv = this.getReadableVersion(request, response);
        try {

            //screen type
            final ScreenTypeViewDAO screenTypeDAO = new ScreenTypeViewDAO(rv);
            final BusinessCriteria criteria = new BusinessCriteria(screenTypeDAO);
            final Collection<ScreenTypeView> views = screenTypeDAO.findViews(criteria);
            request.setAttribute("screenTypeViews", views);
            //manufacturers
            final Collection<Organisation> manufacturers = rv.getAll(Organisation.class, 0, 150);
            request.setAttribute("manufacturers", manufacturers);

            //----------------------------------------------------------------------------
            //Finished & forward
            //
            rv.commit();
            final RequestDispatcher rd = request.getRequestDispatcher("/ScreenCreate.jsp");
            rd.forward(request, response);

        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final BusinessException e) {
            throw new ServletException(e);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on
    // the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        processPostRequest(request, response);
    }

    private void processPostRequest(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final WritableVersion wv = getWritableVersion(request, response);

        try {
            //HolderCategory
            final String typeName = request.getParameter("typeName");
            final HolderCategory hc = wv.findFirst(HolderCategory.class, HolderCategory.PROP_NAME, typeName);
            final HolderCategory hcScreen =
                wv.findFirst(HolderCategory.class, HolderCategory.PROP_NAME, "Screen");
            //manufacturers
            final String orgName = request.getParameter("manufacturerName");
            final Organisation org = wv.findFirst(Organisation.class, Organisation.PROP_NAME, orgName);

            //create screen
            final String screenName = request.getParameter("screenName");
            final RefHolder screen = new RefHolder(wv, screenName);
            screen.addHolderCategory(hc);
            screen.addHolderCategory(hcScreen);
            if (screen.getRefHolderSources().size() > 0) {
                for (final RefHolderSource source : screen.getRefHolderSources()) {
                    source.delete();
                }

            }
            final RefHolderSource newSource = new RefHolderSource(wv, screenName, screen, org);
            screen.setDetails(request.getParameter("description"));

            wv.commit();
            this.redirect(response, request.getContextPath() + "/update/EditScreen/" + screen.getName());
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "create screen";
    }
    // </editor-fold>
}
