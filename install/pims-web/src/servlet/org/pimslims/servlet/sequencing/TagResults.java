package org.pimslims.servlet.sequencing;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.utils.experiment.Utils;
import org.pimslims.utils.sequenator.NoteManager;

/**
 * @author Peter Troshin
 * 
 *         April 2009
 */
public class TagResults extends PIMSServlet {

    @Override
    public String getServletInfo() {
        return "Add note(s) to the sequencing experiment/order/or run at a stage of scoring "
            + "results indicating whether is has been reinjected/reprocessed or else";
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final boolean isSeqAdmin = AdminHome.isSequecingAdmin(request);
        if (!isSeqAdmin) {
            request.setAttribute("message",
                "You are not allowed to perform this action. Only sequencing-administartor can do this!");
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
            dispatcher.forward(request, response);
            return;
        }

        final String expHook = request.getParameter("hook");
        final String tagType = request.getParameter("tagType");
        final String isPublic = request.getParameter("isPublic");

        final String details = request.getParameter("details");

        if (Util.isEmpty(expHook) || !Util.isHookValid(expHook)) {
            request.setAttribute("message", "Experiment hook is expected but is not found! Data sent:"
                + expHook);
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
            dispatcher.forward(request, response);
            return;
        }
        String orderId = "";
        WritableVersion rw = null;
        try {
            rw = this.getWritableVersion(request, response);
            final Experiment exp = rw.get(expHook);
            assert exp != null;
            final NoteManager nm = new NoteManager(exp);
            orderId = Utils.getOrderId(exp);
            if (Util.isEmpty(tagType)) {
                if (Util.isEmpty(isPublic)) {
                    nm.addPrivateNote(rw, details);
                } else {
                    nm.addPublicNote(rw, details);
                }
            } else if (tagType.equalsIgnoreCase(NoteManager.reinjectTag)) {
                nm.addReinjectTag(rw);
            } else if (tagType.equalsIgnoreCase(NoteManager.reprocessTag)) {
                nm.addReprocessTag(rw);
            } else {
                throw new ServletException("Unrecognised Note Type! " + tagType + " isPublic " + isPublic);
            }

            this.commit(request, response, rw);
        } catch (final ConstraintException e) {
            throw new RuntimeException(e);
        } finally {
            if (!rw.isCompleted()) {
                rw.abort();
            }
        }
        PIMSServlet.redirectPostToReferer(request, response, orderId);
    }
}
