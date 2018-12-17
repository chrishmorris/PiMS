/**
 * pims-web org.pimslims.servlet ChooseComponentForRecipe.java
 * 
 * @author Marc Savitsky
 * @date 7 Feb 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky * *
 * 
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.SampleComponent;

/**
 * ChooseComponentForRecipe
 * 
 */
public class ChooseComponentForRecipe extends ChooseForCreate {

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        super.doGet(request, response);
    }

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        // TODO go right ahead and create the SampleComponent
        // then redirect to the view of the recipe (RefSample)

        final java.io.PrintWriter writer = response.getWriter();

        final String metaClassName = request.getParameter(Create.PARM_METACLASSNAME);
        final String metaRoleName = request.getParameter("METAROLENAME");
        final String parameters = request.getParameter("parametersString");
        final String refComponent = request.getParameter("add");

        final MetaClass metaClass = this.getModel().getMetaClass(metaClassName);
        if (null == metaClass) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        final WritableVersion rw = this.getWritableVersion(request, response);
        if (rw == null) {
            return;
        }

        try {

            final Map<String, Object> params = new HashMap<String, Object>();
            final Map<String, Object> parms = new HashMap<String, Object>();

            //System.out.println("ChooseComponentForRecipe.doPost [" + metaClassName + "]");
            //System.out.println("ChooseComponentForRecipe.doPost [" + metaRoleName + "]");
            //System.out.println("ChooseComponentForRecipe.doPost [" + parameters + "]");
            //System.out.println("ChooseComponentForRecipe.doPost [" + refComponent + "]");

            parms.put(metaRoleName, refComponent);

            final String[] unphook = parameters.split("&");
            for (int i = 0; i < unphook.length; i++) {
                final String roleId = unphook[i];
                final int eqs = roleId.indexOf("=");
                assert -1 != eqs : "no '=' in rolename and hook string";
                final String roleName = roleId.substring(0, eqs);
                final String hookId = roleId.substring(eqs + 1);
                //System.out.println("ChooseComponentForRecipe.doPost put [" + roleName + ":" + hookId + "]");
                parms.put(roleName, hookId);
            }

            if (parms.containsKey(SampleComponent.PROP_ABSTRACTSAMPLE)) {
                if (parms.get(SampleComponent.PROP_ABSTRACTSAMPLE) == null) {
                    writer.print("Please provide a sample!");
                    return;
                }
                final AbstractSample object = rw.get((String) parms.get(SampleComponent.PROP_ABSTRACTSAMPLE));
                params.put(SampleComponent.PROP_ABSTRACTSAMPLE, object);
                if (null != object.getAccess()) {
                    params.put(LabBookEntry.PROP_ACCESS, object.getAccess());
                }
            }

            if (parms.containsKey(SampleComponent.PROP_REFCOMPONENT)) {
                if (parms.get(SampleComponent.PROP_REFCOMPONENT) == null) {
                    writer.print("Please select a component!");
                    return;
                }
                final ModelObject object = rw.get((String) parms.get(SampleComponent.PROP_REFCOMPONENT));
                params.put(SampleComponent.PROP_REFCOMPONENT, object);
            }

            final ModelObject mObj = rw.create(metaClass.getJavaClass(), params);
            System.out.println("OBJECT created " + mObj);
            rw.commit();

            PIMSServlet.redirectPost(response, request.getContextPath() + "/View/"
                + parms.get(SampleComponent.PROP_ABSTRACTSAMPLE));

        } catch (final AccessException aex) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_FORBIDDEN);
            writer.print("You are not allowed to make these changes");
            return;
        } catch (final ConstraintException cex) {
            rw.abort();
            // TODO could use ConstraintException.attributeName to show the
            // error message by the input field
            request.setAttribute("javax.servlet.error.exception", cex);
            request.getRequestDispatcher("/update/ConstraintErrorPage").forward(request, response);
            return;
        } catch (final AbortedException abx) {
            throw new ServletException(abx);

            //} catch (UnsupportedEncodingException e) {
            // should not happen
            //throw new ServletException(e);

        } finally {
            if (!rw.isCompleted()) {
                rw.abort();
            }
        }
    }

    @Override
    protected String getPathInfo(final HttpServletRequest request) {
        return "/" + SampleComponent.class.getName() + request.getPathInfo();
    }
}
