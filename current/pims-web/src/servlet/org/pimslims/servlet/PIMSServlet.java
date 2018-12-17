/*
 * Created on 18-Jan-2005 @author: Chris Morris
 */
package org.pimslims.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.access.Access;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.people.Person;
import org.pimslims.presentation.CsrfDefence;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.ServletUtil;

/**
 * Common base class for PIMS servlets, containing utility methods
 * 
 * @version 0.5
 */
public abstract class PIMSServlet extends javax.servlet.http.HttpServlet {

    /**
     * VERSION String
     */
    public static final String PIMS_VERSION = "5.0.0";

    public static final String PIMS_DISTRIBUTION = "academic";

    /**
     * NOT_FOUND_HOOK String
     */
    private static final String NOT_FOUND_HOOK = "hook";

    /**
     * The PIMS database
     * 
     * @deprecated
     */
    @Deprecated
    protected org.pimslims.dao.AbstractModel model;

    /**
     * 
     * @param request
     * @return an object representing the database
     * @throws ServletException
     */
    public org.pimslims.dao.AbstractModel getModel() throws ServletException {
        final javax.servlet.ServletContext context = this.getServletContext();
        return (org.pimslims.dao.AbstractModel) context.getAttribute("model");
    }

    /**
     * If a subclass wishes to override this method, its implementation should begin with super.init(config)
     * to ensure that the model is found. {@inheritDoc}
     */
    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        final javax.servlet.ServletContext context = config.getServletContext();
        this.model = (org.pimslims.dao.AbstractModel) context.getAttribute("model");
        // if ( null!=model && ((ModelImpl)model).isClosed() ) {model=null;}
        if (null == this.model) {
            this.log("Model not found for: " + this.getClass().getName());
            // do not throw an exception, if you do
            // it is not possible to customise the error screen
        } else {
            this.log("Initialised servlet: " + this.getClass().getName());
        }
        context.setAttribute("PIMS_VERSION", PIMSServlet.PIMS_VERSION);
        context.setAttribute("PIMS_DISTRIBUTION", PIMSServlet.PIMS_DISTRIBUTION);
    }

    /**
     * This method is provided to ensure that subclasses overrride init(ServletConfig) instead of this one.
     * 
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public final void init() throws ServletException {
        super.init();
    }

    /**
     * @return the name of the current user
     */
    public static String getUsername(final HttpServletRequest request) {
        return request.getRemoteUser();
    }

    /**
     * @param request the HTTP request being serviced
     * @return true if the current user is an administrator
     */
    public static boolean isAdmin(final ServletRequest request) {
        return ((HttpServletRequest) request).isUserInRole("pims-administrator");
    }

    /**
     * @param request the HTTP request
     * @param response the HTTP request
     * @return a read transaction, or null if one cannot be provided
     * @throws ServletException
     * @throws IOException
     */
    public ReadableVersion getReadableVersion(final HttpServletRequest request,
        final HttpServletResponse response) throws ServletException, IOException {
        if (!this.checkStarted(request, response)) {
            this.writeErrorHead(request, response, "Sorry, PiMs is not running",
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
        if (PIMSServlet.isAdmin(request)) {
            return this.getModel().getReadableVersion(Access.ADMINISTRATOR);
        }
        final javax.servlet.http.HttpSession session = request.getSession();
        final String username = PIMSServlet.getUsername(request);
        if (null == username) {
            this.writeErrorHead(request, response, "You are not logged in to PiMS",
                HttpServletResponse.SC_FORBIDDEN);
            this.log("Visited when not logged on, needs security specification in web.xml: "
                + request.getRequestURI());
            return null;
        }
        ReadableVersion rv = null;
        try {
            rv = this.getModel().getReadableVersion(username);
        } catch (final IllegalArgumentException e) {
            if (e.getMessage().startsWith("Unknown PIMS user name:")) {
                ErrorPage.reportNoSuchPimsUser(request, response, response.getWriter());
                return null;
            }
            throw e;
        }
        if (null == rv) {
            this.writeErrorHead(request, response, "Sorry, PIMS is busy, please retry",
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            return null;
        }
        //session.setAttribute("getVersion", request.getRequestURI());
        return rv;
    }

    /**
     * @param request the HTTP request
     * @param response the HTTP request
     * @return a write transaction
     * @throws ServletException
     */
    protected WritableVersion getWritableVersion(final HttpServletRequest request,
        final HttpServletResponse response) throws ServletException {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            throw new ServletException("attempt to update with method: " + request.getMethod());
        }
        if (null == request.getParameter(CsrfDefence.PARAMETER)) {
            System.err.println("No CSRF token for: " + request.getRequestURI());
            new Throwable().fillInStackTrace().printStackTrace();
        } else {
            CsrfDefence.validate(request);
        }
        if (!this.checkStarted(request, response)) {
            return null;
        }
        if (PIMSServlet.isAdmin(request)) {
            return this.getModel().getWritableVersion(Access.ADMINISTRATOR);
        }
        final javax.servlet.http.HttpSession session = request.getSession();
        final String username = PIMSServlet.getUsername(request);
        if (null == username) {
            this.writeErrorHead(request, response, "You are not logged in to PiMS",
                HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        final WritableVersion wv = this.getModel().getWritableVersion(username);
        if (null == wv) {
            this.writeErrorHead(request, response, "Sorry, PIMS is busy, please retry",
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            return null;
        }
        //session.setAttribute("getVersion", request.getRequestURI());
        return wv;
    }

    public static final String LAB_NOTEBOOK_ID = "labNotebookId";

    /**
     * Displays an appropriate error message if startup was not successful TODO make static
     * 
     * @param request the HTTP request
     * @param response the HTTP request
     * @return true if the model is found
     * @throws ServletException
     */
    protected boolean checkStarted(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException {
        if (null == this.getModel()) {
            try {
                PIMSServlet.writeCriticalErrorHead(request, response, "PIMS is not running",
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                final java.io.PrintWriter writer = response.getWriter();
                final String path = request.getContextPath(); // usually "/pims"
                writer.print("<a href=\"" + path + "/Installation\">View installation details</a>");
                PIMSServlet.writeFoot(writer, request);
            } catch (final IOException ex) {
                // e.g. browser window has been closed
                this.log("Could not show error message", ex);
            }
            return false;
        }
        return true;
    }

    /**
     * 
     */
    protected PIMSServlet() {
        super();
    }

    /**
     * Open a resource file from the class path. For example, org.pimslims.implemenation.ModelImpl uses a file
     * called "Properties". The live version is at web\WEB-INF\classes\org\pimslims\implementation\Properties
     * It is copied by the compiler from java\src\org\pimslims\implementation\Properties
     * 
     * @param name final part of name of file
     * @return
     */
    protected java.io.InputStream openFile(final String name) {
        return this.getClass().getResourceAsStream(name);
    }

    /**
     * Open a resource file from the class path.
     * 
     * @param clazz a class in the same directory as the file wanted
     * @param name
     * @return an input stream for the file
     */
    public static java.io.InputStream openFile(final Class clazz, final String name) {
        return clazz.getResourceAsStream(name);
    }

    /**
     * @return a description of this servlet
     */
    @Override
    public abstract String getServletInfo();

    /**
     * Nearly all servlets implement this method, to display results or to provide a form to fill in.
     * 
     * The coding convention for PIMS is that this method is responsible for error recovery. In the case of an
     * error, it not only returns a human-readable message, but also provides the appropriate HTTP error code.
     * 
     * @param request the HTTP request
     * @param response the response this servlet has made
     * @throws IOException if it cannot write to the user's browser
     * @throws ServletException if it cannot serve the request
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new ServletException("Get is not supported by " + this.getClass().getName());
    }

    /**
     * Process a request which updates the database. If there is an error, the servlet will provide the
     * appropriate HTTP error code, as well as a human-readable message.
     * 
     * If the request succeeds, most servlets redirect the user to a view of the modified object.
     * 
     * @param request the HTTP request
     * @param response the response this servlet has made
     * @throws IOException if it cannot write to the user's browser
     * @throws ServletException if it cannot serve the request
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        super.doPost(request, response);
    }

    protected void ReportBadRequest(final HttpServletRequest request, final HttpServletResponse response,
        final String title, final Throwable cause) throws IOException {
        this.writeErrorHead(request, response, title, HttpServletResponse.SC_BAD_REQUEST);
        final java.io.PrintWriter writer = response.getWriter();
        writer.print("Invalid value: " + cause.getLocalizedMessage());
        cause.printStackTrace();
        PIMSServlet.writeFoot(writer, request);
        return;
    }

    /**
     * Write the start of the HTML document, for a request that has been accepted. If this routine is called,
     * then writeFoot should be called later. It first sends the OK HTTP header. TODO make static
     * 
     * @param request the HTTP request being serviced *
     * @param response the HTTP response
     * @param title the title of this web page
     */
    public void writeHead(final HttpServletRequest request, final HttpServletResponse response,
        final String title) {
        this.writeErrorHead(request, response, title, HttpServletResponse.SC_OK);
    }

    /**
     * Write the start of the HTML document. If this routine is called, then writeFoot should be called later.
     * It first sends the HTTP headers. This is used for normal responses, and minor errors
     * 
     * TODO make static
     * 
     * @param request the HTTP request
     * @param response the HTTP response
     * @param title the title of this web page
     * @param code HTTP error code
     * @throws IOException
     * @throws ServletException
     * @see <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html#sec6.1.1">HTTP spec< /a>
     */
    public void writeErrorHead(final HttpServletRequest request, final HttpServletResponse response,
        final String title, final int code) {
        PIMSServlet.sendHttpHeaders(response, code);
        final RequestDispatcher dispatcher =
            request.getRequestDispatcher("/JSP/core/Header.jsp?HeaderName=" + title);
        try {
            // headers already sent
            dispatcher.include(request, response);
        } catch (final IOException e) {
            // probably the user has closed the page
            this.log("error sending header", e);
        } catch (final ServletException e) {
            // LATER throw the servlet exception
            throw new RuntimeException(e);
        }
        // it would seem to be nice to flush,
        // but you can't use a custom error page after flushing
    }

    /**
     * Report a serious error
     * 
     * @param request
     * @param response
     * @param title
     * @param code
     */
    public static void writeCriticalErrorHead(final HttpServletRequest request,
        final HttpServletResponse response, final String title, final int code) {
        PIMSServlet.sendHttpHeaders(response, code);
        final RequestDispatcher dispatcher =
            request.getRequestDispatcher("/JSP/core/ErrorHeader.jsp?HeaderName=" + title);
        try {
            // this is correctly an include
            dispatcher.include(request, response);
            // it would seem to be nice to flush,
            // but you can't use a custom error page after flushing
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final ServletException e) {
            // There has already been a serious error
            // if we allow this exception to be thrown,
            // there is a risk of recursion
            e.printStackTrace();
        }
    }

    /**
     * Send the HTTP headers for the response
     * 
     * @param response the current response object
     * @param status HTTP status, e.g. 400 for OK See HttpServletResponse for suitable constants.
     */
    public static void sendHttpHeaders(final HttpServletResponse response, final int status) {
        response.setStatus(status);

        //TODO reconsider, see PIMS-3721
        response.addHeader("Cache-Control", "private");
        response.addHeader("Cache-Control", "must-revalidate");
        response.addHeader("Cache-Control", "no-store");
        response.addHeader("Cache-Control", "max-age=0");
        response.setContentType("text/html;charset=utf-8");
        // LATER response.setContentType("application/xhtml+xml;charset=utf-8");
        // if the server accepts that type
        // but see http://hixie.ch/advocacy/xhtml
    }

    /**
     * Finish writing an HTML page that was begun using writeHead TODO use Footer.jsp
     * 
     * @param writer the PrintWriter for the response TODO include footer.jsp instead
     */
    @Deprecated
    // no,     <jsp:include page="/JSP/core/Footer.jsp" />
    public static void writeFoot(final java.io.PrintWriter writer, final HttpServletRequest request) {
        final String contextPath = request.getContextPath();
        writer
            .print("<!-- start of footer -->\r\n" + "</div></div>\r\n"
                + "<div class=\"noprint main_footer\">\r\n"
                + "<div style=\"color:#006;width:29em;text-align:left;margin:0 auto\">\r\n"
                + "<img style=\"float:left;position:relative;top:-0.25em\" src=\""
                + request.getContextPath()
                + "/skins/default/"
                + PIMSServlet.PIMS_DISTRIBUTION
                + "/footer_logo.png\" />\r\n"
                + "Protein Information Management System<br/>Version "
                + PIMSServlet.PIMS_VERSION
                + "\r\n"
                + "</div>\r\n"
                + "</div>\r\n"

                + "<!--Modal window, dialog and mask-->"
                + "<div id=\"modalWindow_mask\" class=\"noprint\" style=\"display:none\">"

                + "<div class=\" collapsiblebox fixedbox\" id=\"modalWindow_window\">"
                + "<div class=\"boxheader\">"
                + "<h3 id=\"modalWindow_window_head\" onclick=\"toggleCollapsibleBox(this, '')\" onmouseover=\"this.onselectstart='return false';\">Window</h3>"
                + "<span class=\"extraheader\"><img src=\""
                + contextPath
                + "/skins/default/images/icons/closeModalWindow.png\""
                + "style=\"cursor:pointer\" title=\"Close and cancel\" alt=\"Close\""
                + "onclick=\"closeModalWindow()\"/></span></div>"
                + "<div id=\"modalWindow_window_body\" class=\"boxcontent\">"
                + "<iframe frameborder=\"0\" id=\"modalWindow_window_iframe\" src=\""
                + contextPath
                + "/skins/default/images/icons/actions/waiting.gif\"></iframe>"
                + "</div>"
                + "</div>"

                + "<div class=\" collapsiblebox fixedbox\" id=\"modalWindow_dialog\">"
                + "<div class=\"boxheader\">"
                + "<h3 id=\"modalWindow_dialog_head\" onclick=\"toggleCollapsibleBox(this, '')\" onmouseover=\"this.onselectstart='return false';\">Window</h3>"
                + "</div>"
                + "<div id=\"modalWindow_dialog_body\" class=\"boxcontent\">&nbsp;</div>"
                + "</div>"

                + "<div class=\" collapsiblebox fixedbox\" id=\"quickSearch_window\">"
                + "<div class=\"boxheader\">"
                + "<h3 id=\"quickSearch_window_head\" onclick=\"toggleCollapsibleBox(this, '')\" onmouseover=\"this.onselectstart='return false';\">Quick search</h3>"
                + "<span class=\"extraheader\"><img src=\""
                + contextPath
                + "/skins/default/images/icons/closeModalWindow.png\""
                + "style=\"cursor:pointer\" title=\"Close and cancel\" alt=\"Close\""
                + "onclick=\"closeQuickSearchWindow()\"/></span></div>"
                + "<div id=\"quickSearch_window_body\" class=\"boxcontent\">"
                + "<iframe frameborder=\"0\" id=\"quickSearch_window_iframe\" src=\""
                + contextPath
                + "/skins/default/images/icons/actions/waiting.gif\"></iframe>"
                + "</div>"
                + "</div>"
                + "</div>"

                + "</body></html>\r\n");
        writer.close();
    }

    /**
     * Every doPost method should call this. It checks that the POST data comes from a PIMS web page. This is
     * in order to guard against a Cross Site Request Forgery attack.
     * 
     * @throws ServletException if the request is unacceptable
     */
    public static void validatePost(final HttpServletRequest request) throws ServletException {
        final String method = request.getMethod();
        if (!"post".equalsIgnoreCase(method)) {
            throw new ServletException("This functionality is only available by POST");
        }
        // TODO check referer
        //TODO check _sessionId == request.getSession().getId() 
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new ServletException(e);
        }
        return;
    }

    /**
     * Redirects the user's browser to another page
     * 
     * @see redirectPost
     * 
     * @param response the HTTP response object
     * @param location the page to view
     */
    public final void redirect(final HttpServletResponse response, final String location) {
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        final String url = response.encodeRedirectURL(location);
        response.setHeader("Location", url);
        try {
            // no more output is permitted
            response.getWriter().close();
        } catch (final IOException ex) {
            // e.g. browser window has been closed
            this.log("Could not redirect", ex);
        }
    }

    /**
     * Redirects the user's browser to a page where the results of the post operation can be seen. Calling
     * this method is the normal way to report a successful update operation. This method is called from a
     * doPost method, and normally the method will then return at once.
     * 
     * @param response the HTTP response object
     * @param location the relative location to get the results from
     */
    public static final void redirectPost(final HttpServletResponse response, final String location) {
        //System.out.println("PiMSServlet.redirectPost [" + location + "]");
        assert !response.containsHeader("Status") : "Cant redirect after writing";
        response.setStatus(HttpServletResponse.SC_SEE_OTHER);
        final String url = response.encodeRedirectURL(location);
        response.setHeader("Location", url);
        try {
            // no more output is permitted
            response.getWriter().close();
        } catch (final IOException ex) {
            ex.printStackTrace(); // probably not important
        }
    }

    /**
     * Gets an object by its hook. This should be called before any headers are sent, for objects that are
     * necessary for performing the request.
     * 
     * If the object is unknown or may not be read, a suitable error header and message are sent, the
     * transaction is cancelled, and null is returned. In this case the caller only has to return.
     * 
     * If an object is found, no headers are sent. In this case the caller should send an OK header when all
     * the preconditions for the request are established, and commit the transaction after processing is
     * complete.
     * 
     * @param version transaction being performed
     * @param request
     * @param response reply that is being prepared
     * @param hook specifies the object to retrieve
     * @return the object requested
     * @throws IOException
     * @throws ServletException
     */
    protected ModelObject getRequiredObject(final org.pimslims.dao.ReadableVersion version,
        final HttpServletRequest request, final HttpServletResponse response, final String hook)
        throws ServletException, IOException {

        if (null == version) {
            // error message has already been printed
            return null;
        }
        if (null == hook) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.getRequestDispatcher("/JSP/Error404.jsp" + request.getRequestURI()).forward(request,
                response);
        }

        org.pimslims.metamodel.ModelObject object = null;
        object = version.get(hook);
        if (null == object) {
            version.abort();
            request.setAttribute(PIMSServlet.NOT_FOUND_HOOK, hook);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.getRequestDispatcher("/JSP/Error404.jsp" + request.getRequestURI()).forward(request,
                response);
        }

        return object;
    }

    /**
     * Utility method to find all instances of a model type
     * 
     * TODO Don't try to get big types like Experiment or Sample Instead make your servlet page through them
     * 
     * @param version transaction to use
     * @param metaClass type to get
     * @return a collection of the model objects in this type
     * @throws ServletException
     */
    public static Collection getAll(final ReadableVersion version, final Class javaClass)
        throws ServletException {
        final Collection results = version.getAll(javaClass, 0, 1000);
        if (1000 <= results.size()) {
            throw new ServletException("Too many records to display [" + javaClass.getName() + ","
                + results.size() + "]");
        }
        return results;
    }

    /**
     * similar with getAll() but better performance by using join
     * 
     * @param joinedRoles a list of role name which used for join
     */
    public Collection getAll(final ReadableVersion version, final MetaClass metaClass,
        final List<String> joinedRoles) {
        try {
            return version.getAll(metaClass.getJavaClass(), joinedRoles);
        } catch (final UnsupportedOperationException ex) {
            this.log("getAll failed for: " + metaClass.getMetaClassName());
            return null;
        }
    }

    /**
     * Commits a transaction, or sends the user an appropriate error report
     * 
     * @precondition: HTTP headers have not yet been sent.
     * @param response the HTTP response
     * @param version the transaction, read or write, to commit
     * @return true if the operation succeeds
     * @throws IOException
     * @throws ServletException
     */
    public boolean commit(final HttpServletRequest request, final HttpServletResponse response,
        final ReadableVersion version) throws ServletException, IOException {
        try {
            version.commit();
            final javax.servlet.http.HttpSession session = request.getSession();
            //session.setAttribute("getVersion", null);
            return true;
        } catch (final AbortedException e1) {
            throw new ServletException(e1);
        } catch (final ConstraintException cex) {
            request.setAttribute("javax.servlet.error.exception", cex);
            request.getRequestDispatcher("/update/ConstraintErrorPage").forward(request, response);
            return false;
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * PIMSServlet.dispatchCustomJsp
     * 
     * Includes a JSP in the output, from the folder specified. This is Default.jsp, or one that is custom for
     * the class concerned.
     * 
     * This method works both in pims-web, where the JSP source files are, and in extensions, where the JSPs
     * are present only in compiled form
     * 
     * @param request
     * @param response
     * @param metaClass the class concerned
     * @param folder the folder in JSP/, e.g. "search"
     * @throws ServletException
     * @throws IOException
     */
    protected static void dispatchCustomJsp(final HttpServletRequest request,
        final HttpServletResponse response, final String jspName, final String folder,
        final ServletContext context) throws ServletException, IOException {

        // Note that this will have no effect if headers already sent:
        PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_OK);

        String url = "/JSP/" + folder + "/" + jspName + ".jsp";

        // see http://docs.oracle.com/javaee/6/api/javax/servlet/ServletContext.html#getRealPath(java.lang.String) 
        final InputStream jsp = context.getResourceAsStream(url);
        final String jspNameWithUnderscores = jspName.replace(".", "_");
        final String pathForClass =
            "WebContent/WEB-INF/classes/org/apache/jsp/JSP/" + folder + "/" + jspNameWithUnderscores
                + "_jsp.class";
        // e.g. C:\current\pims-web\WebContent\WEB-INF\classes\org\apache\jsp\JSP\view\org_pimslims_model_target_Target.class
        final File compiled = new File(pathForClass);
        if (!compiled.exists() // check this case, will usually pass            
            && null == jsp) {
            // TODO log(DEBUG,"No custom JSP: " + compiled.getAbsolutePath());
            url = "/JSP/" + folder + "/Default.jsp";
        }
        final RequestDispatcher dispatcher = request.getRequestDispatcher(url);
        dispatcher.include(request, response);
    }

    /**
     * This method is to simplify supplying the creator of the object
     * 
     * @return User ModelObject (org.pimslims.model.accessControl.User) if there is a user with such name
     *         recorded in the database, null otherwise
     */
    protected static User getCurrentUser(final ReadableVersion rv, final HttpServletRequest request) {
        final HashMap parm = new HashMap();
        parm.put(org.pimslims.model.accessControl.User.PROP_NAME, PIMSServlet.getUsername(request));
        final Collection<User> users = rv.findAll(org.pimslims.model.accessControl.User.class, parm);
        if (users != null && users.size() == 1) {
            final ModelObject user = (new ArrayList<ModelObject>(users)).get(0);
            return (User) user;
        }
        return null;
    }

    /**
     * Return a Person from User object
     * 
     * @return Person if there is one user from currentUser otherwise null
     */
    @Deprecated
    // should no longer use Person
    protected static Person getPersonfromUser(final User user) {
        if (user != null) {
            final ModelObject person =
                (ModelObject) ServletUtil.getModelObject(org.pimslims.model.accessControl.User.PROP_PERSON,
                    user);
            return (Person) person;
        }
        return null;
    }

    public static Collection<ModelObjectShortBean> getPossibleCreateOwners(final ReadableVersion version) {
        final Collection<LabNotebook> accessObjects = version.getCurrentProjects();
        return ModelObjectShortBean.getModelObjectShortBeans(accessObjects);
    }

    /**
     * Returns the URL of the referring page, as accurately as possible.
     * 
     * Note that this information has come from the client, and could be spoofed. There can be security
     * implications if you rely on it to validate a request.
     * 
     * @param request
     * @return the URL of the referring page
     */

    public static String getReferer(final HttpServletRequest request) {
        final String referer = request.getHeader("referer");
        if (null == referer || "".equals(referer)) {
            // belt and braces
            return request.getRequestURI();
        }
        return referer;
    }

    public static String getReferer(final HttpServletRequest request, final Map parameterMap) {

        final StringBuffer referer = new StringBuffer(request.getHeader("referer"));
        if (null == referer || "".equals(referer)) {
            // belt and braces
            return request.getRequestURI();
        }

        for (final Iterator it = parameterMap.entrySet().iterator(); it.hasNext();) {

            if (referer.indexOf("?") > 0) {
                referer.append('&');
            } else {
                referer.append('?');
            }
            final Map.Entry e = (Map.Entry) it.next();
            final String[] values = (String[]) e.getValue();
            final StringBuffer s = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                s.append(values[i] + ",");
            }
            s.deleteCharAt(s.length() - 1);
            referer.append(e.getKey() + "=" + s.toString());
        }

        return referer.toString();
    }

    /**
     * Refreshes the page from which the current form was submitted. This is normally the best way to show the
     * results of a POST.
     * 
     * Note that the usual security issues with referer do not apply to this method. It redirects the client
     * to the page identified by the referer header. If this is spoofed, no harm is done - the client could
     * visit it anyway.
     * 
     * @param request
     * @param response
     */
    public static void redirectPostToReferer(final HttpServletRequest request,
        final HttpServletResponse response) {
        PIMSServlet.redirectPost(response, PIMSServlet.getReferer(request));
    }

    public static void redirectPostToReferer(final HttpServletRequest request,
        final HttpServletResponse response, final Map parameterMap) {
        PIMSServlet.redirectPost(response, PIMSServlet.getReferer(request, parameterMap));
    }

    public static void redirectPostToReferer(final HttpServletRequest request,
        final HttpServletResponse response, final String anchor) {
        PIMSServlet.redirectPost(response, PIMSServlet.getReferer(request) + "#" + anchor);
    }

    public static void redirectPostToReferer(final String referer, final HttpServletResponse response,
        final String anchor) {
        PIMSServlet.redirectPost(response, referer + "#" + anchor);
    }

    /**
     * Extract the parameter values from an Http Request This method requires that there is only one value for
     * each parameter name. If not, you should use HttpServletRequest.getParameterMap()
     * 
     * @param request
     * @return a map name = > value
     * @throws ServletException
     */
    public static Map<String, String> getParameterMap(final HttpServletRequest request)
        throws ServletException {
        final Map<String, String[]> parms = request.getParameterMap();
        final Map<String, String> ret = new HashMap<String, String>(parms.size());
        for (final Iterator iterator = parms.entrySet().iterator(); iterator.hasNext();) {
            final Map.Entry parm = (Map.Entry) iterator.next();
            final String[] values = (String[]) parm.getValue();
            if (0 == values.length) {
                ret.put((String) parm.getKey(), null);
            } else if (1 == values.length) {
                ret.put((String) parm.getKey(), values[0]);
            } else {
                throw new ServletException("Too many values for: " + parm.getKey());
            }
        }
        return ret;
    }

    /**
     * 
     * PIMSServlet.findWriters
     * 
     * @param dataOwner
     * @return
     */
    public static Collection<User> findWriters(final LabNotebook dataOwner) {
        final Collection<User> writers = new HashSet<User>();
        if (null == dataOwner) {
            return Collections.EMPTY_LIST;
        }
        final Collection<Permission> permissions = dataOwner.getPermissions();
        for (final Permission permission : permissions) {
            writers.addAll(permission.getUserGroup().getMemberUsers());
        }
        return writers;

    }

}
