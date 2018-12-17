package org.pimslims.servlet.recipe;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.RecipeUtility;
import org.pimslims.lab.Util;
import org.pimslims.model.sample.RefSample;
import org.pimslims.servlet.PIMSServlet;

@Deprecated
//obsolete
public class CopyRecipe extends PIMSServlet {

    @Override
    public String getServletInfo() {
        return "Copy recipe";
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

        final String pathInfo = request.getPathInfo();
        String protHook = null;
        if (null != pathInfo && 0 < pathInfo.length()) {
            protHook = pathInfo.substring(1); // e.g.
            // Search/org.pimslims.model.experiment.Experiment
        }
        final PrintWriter writer = response.getWriter();
        if ((protHook == null || !Util.isHookValid(protHook))
            && protHook.startsWith(org.pimslims.model.protocol.Protocol.class.getName())) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            writer.print("No type or wrong type specified in request ");
            return;
        }
        if (!this.checkStarted(request, response)) {
            return;
        }

        final WritableVersion rw = this.getWritableVersion(request, response);
        RefSample duplicate = null;
        try {
            if (rw == null) {
                return;
            }
            final RefSample recipe = rw.get(protHook);
            if (recipe == null) {
                PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
                writer.print("No protocol found");
                return;
            }
            duplicate = RecipeUtility.duplicate(recipe, rw);
            rw.commit();
        } catch (final AbortedException e) {
            e.printStackTrace();
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            e.printStackTrace();
            throw new ServletException(e);
        } catch (final AccessException e) {
            e.printStackTrace();
            throw new ServletException(e);
        } finally {
            if (rw != null) {
                if (!rw.isCompleted()) {
                    rw.abort();
                }
            }

        }

        this.redirect(response, request.getContextPath() + "/View/" + duplicate.get_Hook());
    }
}
