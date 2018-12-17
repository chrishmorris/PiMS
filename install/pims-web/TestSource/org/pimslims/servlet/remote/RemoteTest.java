/*
 * Created on 27-May-2005 @author: Chris Morris
 */
package org.pimslims.servlet.remote;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.displaytag.tags.TableTagParameters;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.ScriptException;
import com.meterware.httpunit.WebClient;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebImage;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.cookies.CookieJar;
import com.meterware.httpunit.cookies.CookieListener;
import com.meterware.httpunit.cookies.CookieProperties;
import com.meterware.httpunit.parsing.HTMLParserListener;

/**
 * Utility superclass, to help create tests with HTTPUnit TODO accept path paramater
 * 
 * @see http://httpunit.sourceforge.net/doc/api/overview-summary.html
 * 
 * @version 0.1
 */
public abstract class RemoteTest extends TestCase {

    protected final java.util.Set<String> existedErrorLink = new java.util.HashSet<String>();

    private static final HashMap LOG_IN = new HashMap();

    static {
        RemoteTest.LOG_IN.put("j_username", "demo");
        RemoteTest.LOG_IN.put("j_password", "demo");
    }

    protected Vector<String> history = new Vector<String>();

    protected static int maxNumber = 4;

    /**
     * @param url
     * @param msg
     * @param line
     * @param column
     */
    protected void onHtmlError(final URL url, final String msg, final int line, final int column) {
        Assert.fail("HTML error for : " + url + "\n    line: " + line + " column: " + column + "\n   " + msg);
    }

    private final HTMLParserListener listener = new HTMLParserListener() {

        public void warning(final URL arg0, final String arg1, final int arg2, final int arg3) {
            // LATER report these
        }

        public void error(final URL url, final String msg, final int line, final int column) {
            if (msg.contains("<error>")) {
                throw new AssertionError("JSP error for: " + url);
            }

            if (!this.isKnownIssue(url, msg)) {
                RemoteTest.this.onHtmlError(url, msg, line, column);
            }
        }

        private boolean isKnownIssue(final URL url, final String msg) {
            //known issue PIMS-1373
            if (msg.contains("missing quotemark for attribute value") && url.toString().contains("/Graph/")) {
                return true;
            }

            return false;
        }

    };

    /**
     * root of simulated web application
     */
    protected final URL base;

    /**
     * @param methodName name of test method to execute, null for all
     */
    protected RemoteTest(final String methodName, final java.net.URL base) {
        super(methodName);
        this.base = base;
    }

    /**
     * web client for use in tests
     */
    protected WebClient browser;

    static final CookieListener cookieListener = new CookieListener() {

        @Override
        public void cookieRejected(final String arg0, final int arg1, final String arg2) {
            System.err.println("Cookie rejected: " + arg0 + " because: " + arg1 + " details: " + arg2);
        }
    };

    //static final CookieProperties cookieProperties = new CookieProperties();
    static {
        CookieProperties.addCookieListener(RemoteTest.cookieListener);
        CookieProperties.setDomainMatchingStrict(false);
        CookieProperties.setPathMatchingStrict(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() {
        /*
         * if using a mock servlet containter do: This would be good, but I don't know how to configure the
         * default servlet - Chris try { java.io.File webXML = new java.io.File("web/WEB-INF/web.xml");
         * InputStream in = new FileInputStream(webXML); runner = new ServletRunner(in,"/pims"); } catch
         * (FileNotFoundException ex) { fail(ex.getMessage()); throw new RuntimeException(ex); } catch
         * (IOException ex) { fail(ex.getMessage()); throw new RuntimeException(ex); } catch (SAXException ex) {
         * fail(ex.getMessage()); throw new RuntimeException(ex); } runner.registerServlet("default",
         * "org.apache.catalina.servlets.DefaultServlet"); browser = runner.newClient();
         */

        HttpUnitOptions.setScriptingEnabled(false); // HttpUnit cant parse prototype.js
        // HttpUnitOptions.setExceptionsThrownOnScriptError(false);
        // TODO traverse the DOM to find event properties, then call org.mozilla.javascript.Context.compileString

        this.browser = new WebConversation();
        this.browser.setExceptionsThrownOnErrorStatus(false); // will check
        // response codes

        // unfortunately the redirection does not work for 303 See Other
        this.browser.getClientProperties().setAutoRedirect(false);
        com.meterware.httpunit.parsing.HTMLParserFactory.setParserWarningsEnabled(false);
        com.meterware.httpunit.parsing.HTMLParserFactory.addHTMLParserListener(this.listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() {
        // runner.shutDown();
    }

    /**
     * utilty method to get a web page
     * 
     * @param request request ot process
     * @param parms parameters to add to request
     * @return the response
     * @throws IOException
     */
    protected WebResponse service(final WebRequest request, final Map parms) throws IOException {
        WebResponse response = null;
        String url = "unknown";
        try {
            url = request.getURL().toExternalForm();
        } catch (final MalformedURLException ex2) {
            Assert.fail(ex2.getLocalizedMessage());
        }
        try {
            // send request
            for (final Iterator iter = parms.entrySet().iterator(); iter.hasNext();) {
                final Map.Entry element = (Map.Entry) iter.next();
                request.setParameter((String) element.getKey(), (String) element.getValue());
            }
            response = this.browser.getResponse(request);
            Assert.assertFalse("Response code not set: " + url, 0 == response.getResponseCode());

            Assert.assertNotNull("No response received to: " + url, response);
            // usually could assertEquals( "content type", "text/html",
            // response.getContentType() );

            return response;

        } catch (final MalformedURLException ex) {
            Assert.fail("MalformedURLException: " + ex.getMessage());
        } catch (final IOException ex) {
            if (ex instanceof java.net.SocketException
                && "Unexpected end of file from server".equals(ex.getMessage())) {
                Assert.fail(ex.getMessage() + " for: " + url);
            } else if (ex instanceof UnknownHostException) {
                // maybe external link and no net connection
                return null;
            }
            Assert.assertFalse("file URL", url.startsWith("file:"));
            if (url.startsWith("mailto:")) {
                return response; // that's OK
            }
            throw ex;
        } catch (final SAXException ex) {
            Assert.fail(ex.getMessage());
        } catch (final ScriptException ex) {
            Assert.fail(ex.getMessage());
        }
        return null; // not actually invoked - fail() does not return
    }

    /**
     * Get a web page from a servlet
     * 
     * @param servletName
     * @param parms
     * @return the response
     * @throws IOException
     */
    protected WebResponse get(final String servletName, final Map parms) throws IOException {
        final WebRequest request = new GetMethodWebRequest(this.base, servletName);
        final WebResponse response = this.service(request, parms);
        try {
            Assert.assertNotNull("no response for: " + request.getURL(), response);
        } catch (final MalformedURLException e1) {
            Assert.fail("bad URL in request");
        }
        final int code = response.getResponseCode();
        if (HttpServletResponse.SC_SEE_OTHER == code || HttpServletResponse.SC_MOVED_TEMPORARILY == code) {
            // note that there is a risk of infinite recursion,
            // may have to introduce a counter
            final String url = response.getHeaderField("Location");
            return this.get(url, Collections.EMPTY_MAP);
        }
        if (HttpServletResponse.SC_OK == code || HttpServletResponse.SC_ACCEPTED == code
            || HttpServletResponse.SC_BAD_REQUEST == code) {
            return response;
        }
        try {
            final String urlString = request.getURL().toString();
            Assert.fail(code + " not OK getting: " + urlString);
        } catch (final MalformedURLException e) {
            Assert.fail(e.getMessage());
        }
        return null;
    }

    /**
     * Make an invalid get request, to check error handling
     * 
     * @param servletName
     * @param parms
     * @return the response
     * @throws IOException
     */
    protected WebResponse getNotOK(final String servletName, final Map parms) throws IOException {
        final WebRequest request = new GetMethodWebRequest(this.base, servletName);
        final WebResponse response = this.service(request, parms);
        final int code = response.getResponseCode();
        Assert.assertFalse("Error not detected: " + servletName, HttpServletResponse.SC_OK == code
            || HttpServletResponse.SC_ACCEPTED == code);
        return response;
    }

    /**
     * Post
     * 
     * @param servletName
     * @param parms
     * @return the response, after following any redirect
     * @throws IOException
     */
    protected WebResponse post(final String servletName, final Map parms) throws IOException {
        final WebRequest request = new PostMethodWebRequest(this.base, servletName, "");
        //final URL target = new URL(this.base.toString() + "/" + servletName);
        // doesn't seem to work request.setHeaderField("Cookie", this.cookies.getCookieHeaderField(target));
        WebResponse response = this.service(request, parms);
        String url = "";
        if (HttpServletResponse.SC_SEE_OTHER == response.getResponseCode()
            || HttpServletResponse.SC_MOVED_TEMPORARILY == response.getResponseCode()) {
            url = response.getHeaderField("Location");
            response = this.get(url, Collections.EMPTY_MAP);
        }
        final int code = response.getResponseCode();
        if (HttpServletResponse.SC_INTERNAL_SERVER_ERROR == code) {
            System.out.print(response.getText());
            Assert.fail("Error found for: " + servletName);
        }
        Assert.assertTrue("not OK: " + response.getResponseMessage(), HttpServletResponse.SC_OK == code
            || HttpServletResponse.SC_ACCEPTED == code

        );
        return response;
    }

    /**
     * Post an invalid request, and check it is rejected
     * 
     * @param servletName
     * @param parms
     * @return the response, after following any redirect
     * @throws IOException
     */
    protected WebResponse postNotOK(final String servletName, final Map parms) throws IOException {
        final WebRequest request = new PostMethodWebRequest(this.base, servletName, "");
        final WebResponse response = this.service(request, parms);
        Assert.assertFalse("Error not detected: " + response.getResponseMessage(),
            HttpServletResponse.SC_ACCEPTED == response.getResponseCode());
        return response;
    }

    /**
     * utilty method to get a web page
     * 
     * @param request request to process
     * @return the response
     * @throws IOException
     */
    protected WebResponse service(final WebRequest request) throws IOException {
        return this.service(request, java.util.Collections.EMPTY_MAP);
    }

    /**
     * Perform a request
     * 
     * @param request the link to follow
     * @return the response
     * @throws IOException
     */
    protected WebResponse click(final WebRequest request) throws IOException {

        WebResponse response = this.service(request);
        try {
            Assert.assertNotNull("No response for:" + request.getURL(), response);
        } catch (final MalformedURLException e) {
            Assert.fail("Invalid URL in request");
        }
        int code = response.getResponseCode();
        this.addHistory(request.getMethod() + " " + code + " " + response.getURL());
        if (HttpServletResponse.SC_MOVED_TEMPORARILY == code || HttpServletResponse.SC_SEE_OTHER == code) {
            final String location = response.getHeaderField("location");
            response = this.get(location, Collections.EMPTY_MAP);
            code = response.getResponseCode();
        }
        if (HttpServletResponse.SC_OK == code || HttpServletResponse.SC_ACCEPTED == code
            || HttpServletResponse.SC_BAD_REQUEST == code
            || HttpServletResponse.SC_INTERNAL_SERVER_ERROR == code) {
            return response;

        }
        if (HttpServletResponse.SC_FORBIDDEN == code) {
            this.onForbidden(request, response);
            return null;
        }
        this.printHistory();
        Assert.fail("not OK getting: " + response.getURL() + " " + response.getResponseMessage());
        return null; // not actually executed, fail does not return
    }

    /**
     * Override this to provide a message when a link take you to admin functionality
     * 
     * @param request
     * @param response
     */
    protected void onForbidden(final WebRequest request, final WebResponse response) {
        // ignore error

    }

    protected void addHistory(final String string) {
        // System.out.println("+Checked: " + string);
        this.history.add(0, string);
        this.history.setSize(RemoteTest.maxNumber);
    }

    void clearHistory() {
        this.history = new Vector<String>(RemoteTest.maxNumber);
    }

    protected void printHistory() {
        System.out.println("Previous pages were:");
        for (final String msg : this.history) {
            System.out.println("-" + msg);
        }
    }

    // URLS which have been checked
    // the DBID is stripped first, so we don't revisit similar pages
    // In particular, it's possible to get stuck copying a recipe or protocol
    protected final java.util.Set checked = new java.util.HashSet();

    /**
     * Check a page, recursively
     * 
     * @param page WebResponse representing the page to check
     * @param depth depth of search when following links
     * @throws IOException
     */
    static int totoalCheckedPage = 0;

    protected void checkPage(final WebResponse page, final int depth) throws IOException {

        // System.out.println("*Depth=" + depth);
        if (0 >= depth) {
            return;
        }
        RemoteTest.totoalCheckedPage++;
        try {
            Assert.assertTrue("character set: " + page.getCharacterSet(),
                "utf-8".equalsIgnoreCase(page.getCharacterSet())

            );
            if (!page.isHTML()) {
                System.err.println(page.getURL().toString() + " is not a html page!");
            }
            Assert.assertTrue("html", page.isHTML());
            final String text = page.getText();
            Assert.assertTrue("complete", text.contains("</html>"));
            Assert.assertNotNull("no logo in: " + page.getURL().toExternalForm(),
                page.getImageWithAltText("PIMS"));
            // e.g. The requested resource (/pims/JSP/ViewAttributes.JSP/org.pimslims.model.reference.ExperimentType) is not available
            Assert.assertFalse("bad jsp:include in: ", text.contains("The requested resource"));

            //check images
            final WebImage[] images = page.getImages();
            for (int i = 0; i < images.length; i++) {
                final WebImage image = images[i];
                //System.out.println("+Image checked: " + image.getSource());
                if (!this.isLinkRelevant(image.getSource(), page)) {
                    continue;
                }
                this.addHistory(page.getURL().toString());
                this.click(image.getRequest());
                this.checked.add(image.getSource());
            }
            // follow the links
            final WebLink[] links = page.getLinks();
            for (int i = 0; i < links.length; i++) {
                final WebLink link = links[i];
                final String urlString = link.getURLString();
                //  System.out.println("+Checked url: " + urlString);
                if (!this.isLinkRelevant(urlString, page)) {
                    continue;
                }
                final WebResponse response = this.click(link.getRequest());

                this.addToChecked(urlString);
                final String url = response.getURL().toExternalForm();
                if (url.startsWith(this.base.toExternalForm())) {
                    // the new page is also part of PIMS
                    this.checkPage(response, depth - 1);
                }
            }
        } catch (final SAXException ex) {
            Assert.fail("Parse error: " + ex.getMessage());
        }
    }

    protected void addToChecked(final String url) {
        final String urlString = this.truncateURL(url);
        this.checked.add(urlString);
    }

    // remove DBID from end of URL
    protected static final Pattern DBID = Pattern.compile(":\\d+$");

    protected boolean isLinkRelevant(final String url, final WebResponse page) {

        final String urlString = this.truncateURL(url);
        if (this.checked.contains(urlString)) {
            return false;
        }
        if (urlString.endsWith("update/AjaxRemove/") || urlString.endsWith("update/DeleteAndBackToReferer")) {
            return false; // dummy form
        }
        if (urlString.toLowerCase().equals("javascript:void(0)")) {
            return false; // dummy link, e.g. in help files
        }
        //avoid a dead loop
        if (urlString.contains("CopyProtocol")
            && page.getURL().toString().contains("View/org.pimslims.model.protocol.Protocol")) {
            return false;
        }
        if (urlString.endsWith("Logout")) {
            return false; // logging out now would break other tests
        }
        if (urlString.startsWith("#") || urlString.equals(page.getURL().toString())) {
            return false; // it's this page again
        }
        if (urlString.contains("mailto:")) {
            return false; // can't check email links
        }
        if (urlString.startsWith("javascript:")) {
            return false; // LATER test these
        }
        if (urlString.endsWith(".pdf")) {
            return false;
        }
        if (urlString.startsWith("http:") && !urlString.startsWith(this.base.toExternalForm())) {
            // it's an external link
            // TODO check external links - involves setting a proxy
            return false;
        }

        // filter out links to displaytab export
        if (urlString.contains("-" + TableTagParameters.PARAMETER_EXPORTTYPE + "=")) {
            return false;
        }

        return true;
    }

    /**
     * @param url
     * @return
     */
    private String truncateURL(final String url) {
        final Matcher m = RemoteTest.DBID.matcher(url);
        final String urlString = m.replaceAll(":XXXX");
        return urlString;
    }

    /**
     * Check a page
     * 
     * @param page WebResponse representing the page to check
     * @throws IOException
     */
    protected void checkPage(final WebResponse page) throws IOException {
        this.checkPage(page, 1);
    }

    public WebResponse login(WebResponse response) {
        try {
            final String title = response.getTitle();
            Assert.assertEquals("log in page:" + response.getText(), "Log in to PiMS", title);
            System.out.println(response.getHeaderField("Set-Cookie"));
            final CookieJar cookies = new CookieJar(response);
            this.browser.putCookie("JSESSIONID", cookies.getCookieValue("JSESSIONID"));

            response = this.post("j_security_check", RemoteTest.LOG_IN);
            Assert
                .assertFalse("Unknown user name", response.getTitle().startsWith("Unknown PIMS user name:"));
            Assert.assertFalse("log in error:\n" + response.getText(),
                response.getText().contains("<p class=error>"));
            final WebLink log = response.getLinks()[2]; // first link quick search, second is "Home"
            Assert.assertFalse("Log in as user demo rejected", log.getText().startsWith("Log in"));
            Assert.assertTrue("Not logged in: " + log.getText() + " " + response.getURL(), log.getText()
                .startsWith("Log out"));
            return response;
        } catch (final SAXException e) {
            Assert.fail(e.getMessage());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
        return null; // not actually executed, fail does not return
    }
}
