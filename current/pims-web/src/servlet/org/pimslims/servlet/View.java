package org.pimslims.servlet;

/*
 * Making a new Perspective To add a new perspective to PiMS, you need to carry out the following steps. Make
 * a new folder in WebContent, e.g. WebContent/admin. This is where JSPs for your new perspective go. If you
 * are going to make custom menus, you will probably want to copy WebContent/standard/meubar.jsp into your new
 * folder. Search pims-web for the string "Add new perspective here". There are five matches, including the
 * line above. There is one match in web.xml. Add a mapping for JSPs in your new folder there. There is one
 * match in Header.jsp. Add your new perspective there. There are two other matches in this file. Make a new
 * Perspective Definition, e.g. public static final Perspective ADMIN_PERSPECTIVE = new Perspective() { public
 * String getViewUrl(HttpServletRequest request, String className) { // see STANDARD_PERSPECTIVE for how to
 * make custom views return STANDARD_PERSPECTIVE.getViewUrl(request, className); } public String getName() {
 * return "admin"; // the name of your new perspective } }; and add it to the Perspective List. Stop and
 * restart tomcat. Check that you can see your new perspective. Then commit your changes. If you add servlets
 * for your new perspective, you must add them to web.xml in the normal way.
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * View a record in the PiMS database. This servlet forwards to the view appropriate for the current
 * perspective.
 * 
 * @baseUrl /View/org.pimslims.model.sample.Sample:653576
 * 
 * 
 * 
 * @author cm65
 * 
 */
public class View extends PIMSServlet {

    public static final String DEFAULT_PERSPECTIVE = "defaultPerspective";

    public static final String PERSPECTIVE = "perspective";

    public static final String ENABLEDPERSPECTIVENAMES = "enabledPerspectiveNames";

    // was public static final String ALLPERSPECTIVENAMES = "allPerspectiveNames";

    // Perspective definitions
    // Add new perspective here

    public static final Perspective EXPERT_PERSPECTIVE = new Perspective() {
        @Override
        public String getViewUrl(final HttpServletRequest request, final String className) {
            return "expert/View";
        }

        @Override
        public String getName() {
            return "expert";
        }

    };

    public static final Perspective STANDARD_PERSPECTIVE = new Perspective() {
        @Override
        public String getViewUrl(final HttpServletRequest request, final String className) {

            // if there is a standard view, return it
            if (org.pimslims.model.target.Milestone.class.getName().equals(className)) {
                return "read/ViewMilestone";
            } else if (org.pimslims.model.sample.Sample.class.getName().equals(className)) {
                return "standard/ViewSample";
            } else if (org.pimslims.model.sample.RefSample.class.getName().equals(className)) {
                return "standard/ViewRefSample";
            } /* else if (org.pimslims.model.location.Location.class.getName().equals(className)) {
                return "standard/ViewLocation";
              } */else if (org.pimslims.model.target.Target.class.getName().equals(className)) {
                return "read/TargetView";
            } else if (org.pimslims.model.accessControl.User.class.getName().equals(className)) {
                return "read/ViewUser";
            } else if (org.pimslims.model.experiment.Experiment.class.getName().equals(className)) {
                return "standard/ViewExperiment";
            } else if (org.pimslims.model.holder.Holder.class.getName().equals(className)) {
                return "ViewHolder";
            } else if (org.pimslims.model.experiment.ExperimentGroup.class.getName().equals(className)) {
                return "read/ViewExperimentGroup";
            } else if (org.pimslims.model.target.ResearchObjective.class.getName().equals(className)) {
                return "/read/ViewExpressionObjective";
            } else if (org.pimslims.model.protocol.Protocol.class.getName().equals(className)) {
                return "standard/ViewProtocol";
            } else if (org.pimslims.model.protocol.ParameterDefinition.class.getName().equals(className)) {
                return "standard/ViewParent";
            } else if (org.pimslims.model.protocol.RefInputSample.class.getName().equals(className)) {
                return "standard/ViewParent";
            } else if (org.pimslims.model.protocol.RefOutputSample.class.getName().equals(className)) {
                return "standard/ViewParent";
            } else if (org.pimslims.model.sample.SampleComponent.class.getName().equals(className)) {
                return "standard/ViewParent";
            } else if (org.pimslims.model.molecule.Extension.class.getName().equals(className)) {
                return "read/ViewExtension";
            }
            // use expert view as default
            return View.EXPERT_PERSPECTIVE.getViewUrl(request, className);
        }

        @Override
        public String getName() {
            return "standard";
        }

        @SuppressWarnings("unused")
        public String getMenuBarImage() {
            return "images/header/background/standard.gif";
        }
    };

    public static final Perspective XTAL_PERSPECTIVE = new Perspective() {

        @SuppressWarnings("unused")
        public String getMenuBarImage() {
            return "images/header/background/xtal.gif";
        }

        @Override
        public String getName() {
            return "xtal";
        }

        @Override
        public String getViewUrl(final HttpServletRequest request, final String className) {
            //TODO add some custom views
            return View.STANDARD_PERSPECTIVE.getViewUrl(request, className);
        }
    };

    public static final Perspective CSIC_PERSPECTIVE = new Perspective() {
        @Override
        public String getViewUrl(final HttpServletRequest request, final String className) {

            // use standard view as default
            return View.STANDARD_PERSPECTIVE.getViewUrl(request, className);
        }

        @Override
        public String getName() {
            return "CSIC";
        }

        @SuppressWarnings("unused")
        @Deprecated
        // obsolete
        public String getMenuBarImage() {
            return "images/header/background/CSIC.gif";
        }
    };

/*
    private static final Perspective LEEDS_PERSPECTIVE = new Perspective() {

        public String getName() {
            return "Leeds";
        }

        public String getViewUrl(final HttpServletRequest request, final String className) {
            return View.STANDARD_PERSPECTIVE.getViewUrl(request, className);
        }


        public String getMenuBarImage() {
            return "images/header/background/Leeds.gif";
        }

    }; */

    private static final Perspective OPPF_PERSPECTIVE = new Perspective() {

        @Override
        public String getName() {
            return "OPPF";
        }

        public String getViewUrl(final HttpServletRequest request, final String className) {
            return View.STANDARD_PERSPECTIVE.getViewUrl(request, className);
        }

        public String getMenuBarImage() {
            return "images/header/background/oppf.gif";
        }
    };

    private static final Perspective ADMIN_PERSPECTIVE = new Perspective() {

        public String getName() {
            return "admin";
        }

        public String getViewUrl(final HttpServletRequest request, final String className) {
            return View.STANDARD_PERSPECTIVE.getViewUrl(request, className);
        }

        public String getMenuBarImage() {
            return "images/header/background/admin.gif";
        }

    };

    // Perspective List
    // Add new perspective here
    public static final Map<String, Perspective> PERSPECTIVES = new HashMap<String, Perspective>();
    static {
        View.PERSPECTIVES.put("expert", View.EXPERT_PERSPECTIVE);
        View.PERSPECTIVES.put("standard", View.STANDARD_PERSPECTIVE);
        View.PERSPECTIVES.put("xtal", View.XTAL_PERSPECTIVE);
        View.PERSPECTIVES.put("CSIC", View.CSIC_PERSPECTIVE);
        //View.PERSPECTIVES.put("Leeds", View.LEEDS_PERSPECTIVE);
        View.PERSPECTIVES.put("OPPF", View.OPPF_PERSPECTIVE);
        View.PERSPECTIVES.put("admin", View.ADMIN_PERSPECTIVE);
    }

    @Override
    public String getServletInfo() {
        return "Forward to appropriate view for current perspective";
    }

    public static final Pattern CLASSNAME = Pattern.compile("^/(.*?):\\d+$");

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        final String pathInfo = request.getPathInfo();
        if (null == pathInfo) {
            this.writeErrorHead(request, response, "You must specify a record to view",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        final Matcher m = View.CLASSNAME.matcher(pathInfo);
        if (!m.matches()) {
            this.writeErrorHead(request, response, "Record not found: " + pathInfo,
                HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        final String className = m.group(1);

        // add MRU
        final String username = PIMSServlet.getUsername(request);
        final String hook = pathInfo.substring(1);// is it always correct?
        org.pimslims.presentation.mru.MRUController.addObject(username, hook);

        final Perspective perspective = View.getCurrentPerspective(request);

        View.viewWithPerspective(request, response, className, perspective);
    }

    /**
     * @param request
     * @param response
     * @param className
     * @param perspective
     * @throws ServletException
     * @throws IOException
     */
    private static void viewWithPerspective(final HttpServletRequest request,
        final HttpServletResponse response, final String className, final Perspective perspective)
        throws ServletException, IOException {
        String url = "/" + perspective.getViewUrl(request, className) + request.getPathInfo();
        if (null != request.getQueryString()) {
            url = url + "?" + request.getQueryString();
        }
        final RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }

    public static void showExpertView(final HttpServletRequest request, final HttpServletResponse response,
        final String className) throws ServletException, IOException {
        View.viewWithPerspective(request, response, className, View.EXPERT_PERSPECTIVE);
    }

    public static Perspective getCurrentPerspective(final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        Perspective p = (Perspective) session.getAttribute(View.PERSPECTIVE);
        if (null == p || session.getAttribute(View.ENABLEDPERSPECTIVENAMES) == null) {
            final String name = View.getDefaultPerspective(session);
            p = View.PERSPECTIVES.get(name);
            session.setAttribute(View.PERSPECTIVE, p);
            session.setAttribute(View.ENABLEDPERSPECTIVENAMES,
                session.getServletContext().getAttribute(View.ENABLEDPERSPECTIVENAMES));
            // was session.setAttribute(View.ALLPERSPECTIVENAMES, View.PERSPECTIVES.keySet());
        }
        return p;
    }

    /**
     * View.getDefaultPerspective
     * 
     * @param session
     * @return
     */
    public static String getDefaultPerspective(final HttpSession session) {
        String name = (String) session.getServletContext().getAttribute(View.DEFAULT_PERSPECTIVE);
        if (null == name) {
            name = "standard";
        }
        return name;
    }

    public static void viewHolder(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        String url = "/ViewHolder" + request.getPathInfo();
        if (null != request.getQueryString()) {
            url = url + "?" + request.getQueryString();
        }
        System.out.println("View.viewHolder url [" + url.toString() + "]");
        final RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }

    /**
     * Change perspective
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final String p = request.getParameter(View.PERSPECTIVE);
        View.setPerspective(request, p);

        // now redisplay the calling page
        PIMSServlet.redirectPostToReferer(request, response);
    }

    /**
     * @param request
     * @param perspectiveName
     */
    public static void setPerspective(final HttpServletRequest request, final String perspectiveName) {
        final Perspective perspective = View.PERSPECTIVES.get(perspectiveName);
        request.getSession().setAttribute(View.PERSPECTIVE, perspective);
    }

}
