package org.pimslims.servlet.sequencing;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.model.core.Annotation;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.utils.sequenator.SOrdersManager;

/**
 * @author Petr Troshin
 * 
 *         Cancel planned run by preserve sequencing orders
 * 
 * 
 * @baseURL /update/AbandonPlannedRun/expGroupHook
 * @baseURL
 */
public class AbandonPlannedRun extends PIMSServlet {

    @Override
    public String getServletInfo() {
        return "Cancel planned run by preserve sequencing orders";
    }

    /**
     * DeleteSOrder.doGet
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final String pathInfo = request.getPathInfo();
        if (Util.isEmpty(request.getPathInfo())) {
            this.writeErrorHead(request, response, "Please specify records to delete",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        final String expGroupHook = pathInfo.substring(1);

        org.pimslims.dao.ReadableVersion version = null;
        try {
            version = this.getReadableVersion(request, response);
            final ExperimentGroup expGroup = version.get(expGroupHook);

            final ModelObjectShortBean bean = new ModelObjectShortBean(expGroup);
            if (!bean.getMayDelete()) {
                this.writeErrorHead(request, response, "Insufficent access rights to delete Planned Run "
                    + expGroup, HttpServletResponse.SC_BAD_REQUEST);
                this.commit(request, response, version);
                return;
            }

            request.setAttribute("records", expGroup);
            request.setAttribute("mayUpdate", expGroup.get_MayDelete());
            request.setAttribute("viewLabel", expGroup.getName());
            request.setAttribute("formAction", "/update/AbandonPlannedRun/" + expGroup.get_Hook());
            request.setAttribute("viewAction", "/View/" + expGroup.get_Hook() + "?_tab=plateview");
            request.setAttribute("referer", PIMSServlet.getReferer(request));
            response.setStatus(HttpServletResponse.SC_OK);
            final RequestDispatcher dispatcher =
                this.getServletContext().getRequestDispatcher("/JSP/sequencing/AbandonPlannedRun.jsp");
            dispatcher.forward(request, response);
            PIMSServlet.writeFoot(response.getWriter(), request);

            this.commit(request, response, version);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
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
        String egHook = null;
        if (null != pathInfo && 0 < pathInfo.length()) {
            egHook = pathInfo.substring(1); // e.g.
            // Search/org.pimslims.model.experiment.Experiment
        }

        final PrintWriter writer = response.getWriter();
        if (!Util.isHookValid(egHook)) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            writer.print("Experiment group hook is not valid or provided! Cannot continue. Data provided: "
                + egHook);
            return;
        }
        final String originalReferer = request.getParameter("referer");
        WritableVersion rw = null;
        try {

            rw = this.getWritableVersion(request, response);

            final ExperimentGroup plannedRun = rw.get(egHook);

            final SOrdersManager som = new SOrdersManager(plannedRun);
            som.abandonPlannedRun();
            // Remove annotation ie. files
            for (final Annotation fa : plannedRun.getAnnotations()) {
                fa.delete();
            }
            plannedRun.delete();

            this.commit(request, response, rw);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } finally {
            if (!rw.isCompleted()) {
                rw.abort();
            }
        }

        this.redirect(response, originalReferer);

    }
}
