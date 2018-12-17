package org.pimslims.servlet;

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
import org.pimslims.lab.experiment.ExperimentCopier;
import org.pimslims.lab.protocol.ProtocolUtility;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.molecule.Extension;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.sample.RefSample;
import org.pimslims.presentation.construct.Extensions;

/**
 * @author Petr Troshin
 * 
 *         Generic Copy servlet.
 * 
 *         As for now protocol and experiment copying is supported
 * @baseURL /Copy/org.pimslims.model.experiment.Experiment:2323
 * @baseURL /Copy/org.pimslims.model.protocol.Protocol:3924
 */
public class Copy extends PIMSServlet {

    @Override
    public String getServletInfo() {
        return "Generic Copy. As for now protocol and experiment copying is supported";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final String pathInfo = request.getPathInfo();
        String hook = null;
        if (null != pathInfo && 0 < pathInfo.length()) {
            hook = pathInfo.substring(1); // e.g.
            // Search/org.pimslims.model.experiment.Experiment
        }
        final PrintWriter writer = response.getWriter();
        if ((hook == null || !Util.isHookValid(hook))
            && (hook.startsWith(Protocol.class.getName()) || hook.startsWith(Experiment.class.getName()))) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            writer.print("No type or wrong type specified in request ");
            return;
        }
        if (!this.checkStarted(request, response)) {
            return;
        }

        final WritableVersion rw = this.getWritableVersion(request, response);

        ModelObject duplicate = null;
        try {
            if (rw == null) {
                return;
            }
            final ModelObject object = rw.get(hook);
            if (object == null) {
                PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
                writer.print("No object found");
                return;
            }
            if (hook.startsWith(Protocol.class.getName())) {
                duplicate = ProtocolUtility.duplicate((Protocol) object, rw);
            } else if (hook.startsWith(RefSample.class.getName())) {
                duplicate = RecipeUtility.duplicate((RefSample) object, rw);
            } else if (hook.startsWith(Extension.class.getName())) {
                duplicate = Extensions.duplicate((Extension) object, rw);
            } else {
                duplicate = ExperimentCopier.duplicate((Experiment) object, rw);
            }

            rw.commit();
        } catch (final AbortedException abe) {
            abe.printStackTrace();
            throw new ServletException(abe);
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
