package org.pimslims.servlet.target;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.servlet.PIMSServlet;

/**
 * forward to related servlet depends on "action" selected
 * 
 * @author bl67
 * 
 */
public class TargetsReportAction extends PIMSServlet {
    static final Map<String, String> actionMap = new HashMap<String, String>();
    {// action -> url
        TargetsReportAction.actionMap.put("Compare Experiment Parameter", "/report/SelectTargetParameters");
        TargetsReportAction.actionMap.put("Constructs Summary", "/report/ConstructsSummary");
    }

    @Override
    public String getServletInfo() {
        return "actions on selected targets";
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        if (!this.checkStarted(request, response)) {
            return;
        }
        // get selected Target
        final Collection<String> targetHooks =
            ServletUtil.getSelectedHooks(request.getParameterMap(), Target.class.getName());

        // return when not target selected
        final PrintWriter writer = response.getWriter();
        if ((targetHooks == null || targetHooks.size() == 0)) {
            this.writeHead(request, response, "No Target Selected");
            writer.print("Please select some targets first!");
            PIMSServlet.writeFoot(writer, request);
            return;
        }

        // get url from action parameter
        final String actionString = request.getParameter("action");
        String forwardURL = null;
        ReadableVersion version = null;

        try {
            version = this.getReadableVersion(request, response);
            if (version == null) {
                return;
            }
            forwardURL = TargetsReportAction.getForwardUrl(actionString);
            //180710 Need referrer for breadcrumb trail
            final String referrer = request.getHeader("referer");
            request.setAttribute("referrer", referrer);

            //190710 Need the TargetGroup name
            if (null != referrer && referrer.contains("TargetGroupReport")) {
                final int startHook = referrer.indexOf("org");
                final String hook = referrer.substring(startHook);
                final TargetGroup group = (TargetGroup) version.get(hook);
                final String groupName = group.get_Name();
                request.setAttribute("groupName", groupName);
            }
        } catch (final IllegalArgumentException e) {
            this.writeHead(request, response, "Illegal Action");
            writer.print("Illegal action: " + e.getLocalizedMessage());
            PIMSServlet.writeFoot(writer, request);
            return;
        }
        // forward
        final RequestDispatcher rd = request.getRequestDispatcher(forwardURL);
        rd.forward(request, response);
    }

    static String getForwardUrl(final String actionString) {
        if (actionString == null || actionString.trim().length() == 0) {
            throw new IllegalArgumentException(" no action selected!");
        }
        if (!TargetsReportAction.actionMap.containsKey(actionString)) {
            throw new IllegalArgumentException(" unknown action: " + actionString);
        }
        return TargetsReportAction.actionMap.get(actionString);
    }
}
