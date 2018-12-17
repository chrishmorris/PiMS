/**
 * current-pims-web org.pimslims.servlet.spot ViewExpBlueprint.java
 * 
 * @author Marc Savitsky
 * @date 2 Jul 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Marc Savitsky
 * 
 * 
 */
package org.pimslims.servlet.construct;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ConstructUtility;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ComplexBeanReader;
import org.pimslims.servlet.PIMSServlet;

public class ViewExpressionObjective extends PIMSServlet {

    /**
     * 
     */
    public ViewExpressionObjective() {
        super();
    }

    /**
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        if (!this.checkStarted(request, response)) {
            return;
        }
        String pathInfo = request.getPathInfo();
        if (null != pathInfo && 0 < pathInfo.length()) {
            pathInfo = pathInfo.substring(1); // strip initial slash
        }
        final ReadableVersion version = this.getReadableVersion(request, response);
        try {
            final ResearchObjective ro =
                (ResearchObjective) this.getRequiredObject(version, request, response, pathInfo);
            if (null == ro) {
                return; // 404 already sent
            }

            if (ComplexBeanReader.isComplex(ro)) {
                final RequestDispatcher rd = request.getRequestDispatcher("/ViewComplex/" + ro.get_Hook());
                rd.forward(request, response);
                return;
            }

            if (ConstructUtility.isSpotConstruct(ro)) {
                final RequestDispatcher rd =
                    request.getRequestDispatcher("/read/ConstructView/" + ro.get_Hook());
                rd.forward(request, response);
                return;
            }

            // Construct a link to create a add new modelObject in role to
            // displayed object
            final HashMap addnewLinks = new HashMap();
            final HashMap searchLinks = new HashMap();

            final Map metaRoles = ro.get_MetaClass().getMetaRoles();
            for (final Iterator iter = metaRoles.entrySet().iterator(); iter.hasNext();) {
                final Map.Entry element = (Map.Entry) iter.next();
                final MetaRole mrole = (MetaRole) element.getValue();

                final MetaClass otherMetaClass = mrole.getOtherMetaClass();
                final MetaRole otherRole = mrole.getOtherRole();
                final String roleName = mrole.getRoleName();
                if (otherRole != null && mrole.getHigh() == -1) {

                    String searchAddLink = "";
                    searchAddLink = "../EditRole/" + ro.get_Hook() + "/" + mrole.getRoleName();

                    String createLink = "";
                    if (mrole.getHigh() == -1 || mrole.getHigh() > 1) {
                        createLink =
                            "../Create/" + otherMetaClass.getJavaClass().getName() + "?"
                                + otherRole.getRoleName() + "=" + ro.get_Hook();
                    }

                    addnewLinks.put(roleName, createLink);
                    searchLinks.put(roleName, searchAddLink);
                }

            }
/*
            request.setAttribute("createLinks", addnewLinks);
            request.setAttribute("chooseLinks", searchLinks);
            request.setAttribute("head", new Boolean(true));
            request.setAttribute("ObjName", "Research Objective"); */

            request.setAttribute("bean", BeanFactory.newBean(ro));
            version.commit();
            // request.setAttribute("mayUpdate", new Boolean(PIMSServlet.mayUpdate(request)));
            final RequestDispatcher rd = request.getRequestDispatcher("/JSP/view/Default.jsp");
            rd.forward(request, response);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
            //} catch (final AccessException e) {
            //    throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    /**
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new ServletException("Cannot post to ViewExperimentGroup");
    }

    /**
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "show an experiment blueprint";
    }

    private boolean isSpotConstruct(final ResearchObjective expBlueprint) {
        return true;
    }

}
