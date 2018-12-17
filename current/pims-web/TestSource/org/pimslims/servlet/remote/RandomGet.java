/*
 * Created on 27-May-2005 @author: Chris Morris
 */
package org.pimslims.servlet.remote;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;
import junit.framework.Test;
import junit.textui.TestRunner;

import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.SubmitButton;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

/**
 * Visit as many pages as possible
 * 
 * TODO modify to provide referer header
 * 
 * @version 0.1
 */
public class RandomGet extends RemoteTest {
    protected static final int maxNumberOfError = 100;

    protected static final int maxNumberOfPage = 100000;

    // login page
    protected static final String START_PAGE = "Login";

    // was "Search/org.pimslims.model.experiment.ExperimentGroup";

    /**
     * Names of form fields that must have integer values
     */
    private static final Collection<String> INTEGER_FIELDS = new HashSet<String>();
    static {
        RandomGet.INTEGER_FIELDS.add("days_of_no_progress");
        RandomGet.INTEGER_FIELDS.add("daysAgo");
    }

    private static final String[] RESTART_PAGES = new String[] { "Search/org.pimslims.model.target.Target",
        "Search/org.pimslims.model.experiment.Experiment", "Search.org/pimslims.model.sample.Sample" };

    // TODO private static final String ATTACK = "'\"\\><nonesuch>";
    protected final long seed;

    protected Random random;

    protected int pages;

    /**
     * @param methodName name of test method to call
     * @throws MalformedURLException
     */
    public RandomGet(final String methodName, final long seed, final URL base) throws MalformedURLException {
        super(methodName, base);
        this.seed = seed;
        this.random = new Random(seed);
    }

    @Override
    protected void printHistory() {
        System.out.println("At page:" + this.pages);
        super.printHistory();
    }

    /**
     * @param methodName name of test method to call
     * @throws MalformedURLException
     */
    public RandomGet(final String methodName) throws MalformedURLException {
        super(methodName, new java.net.URL("http://localhost:4040/current/"));
        this.seed = System.currentTimeMillis();
        this.random = new Random(this.seed);
        System.out.println("Seed is: " + this.seed);
    }

    /**
     * Get the first page, and check all pages linked from it
     * 
     * @throws IOException
     * 
     */
    public void test() {
        System.out.println(this.getClass().getName());
        int lastErrorPage = 0;
        this.pages = 1;
        com.meterware.httpunit.WebResponse response;
        try {
            response = this.get(RandomGet.START_PAGE, Collections.EMPTY_MAP);
            response = this.login(response);
        } catch (final IOException e1) {
            Assert.fail(e1.getMessage());
        }
        for (int errors = 0; errors < RandomGet.maxNumberOfError; errors++) {
            this.random = new Random(this.seed + errors);
            try {
                response = this.get(RandomGet.START_PAGE, Collections.EMPTY_MAP);
                while (this.pages++ < RandomGet.maxNumberOfPage) {
                    WebRequest request = this.chooseNextRequest(response);
                    if (null == request) {
                        // all links from this page have been checked already
                        request = this.restart(errors);
                    }
                    if (null == request || null == request.getURL()) {
                        response = null;
                    } else {
                        response = this.click(request);
                    }
                    if (null != response && HttpServletResponse.SC_BAD_REQUEST == response.getResponseCode()) {
                        this.onBadRequest(this.pages, errors, null);
                    }
                }
                //CHECKSTYLE:OFF ok to catch exception here
            } catch (final Throwable e) {
                if (lastErrorPage == this.pages) {
                    break; // we are in a loop
                }
                lastErrorPage = this.pages;
                if (this.onError(this.pages, errors, e)) {
                    break;
                }
            }
            // CHECKSTYLE:ON

        }
    }

    /**
     * RandomGet.restart start again from here, when no next page Changing this will help focus the tests to a
     * particular area of functionality
     * 
     * @param errors
     * @return
     */
    WebRequest restart(final int errors) {
        WebRequest request;
        System.out.println("Pages checked so far: " + this.pages + ", errors: " + errors + " at "
            + new Date(System.currentTimeMillis()));
        this.checked.clear();
        this.clearHistory();

        final int index = this.random.nextInt(RandomGet.RESTART_PAGES.length);
        final String page = RandomGet.RESTART_PAGES[index];
        request = new GetMethodWebRequest(this.base, page);
        return request;
    }

    /**
     * Override this to stop on Error 400 response. This means that this code has failed to sumbit wuitable
     * values.
     * 
     * @param pages
     * @param i
     * @param object
     */
    private void onBadRequest(final int pages, final int i, final Object object) {
        // no action needed
    }

    /**
     * @param pages
     * @param i
     * @param e
     */
    private boolean onError(final int pages, final int i, final Throwable e) {
        System.err.println("error-" + i + " found after : " + pages + " pages, seed was: " + (this.seed + i));
        this.printHistory();
        if (null != e) {
            if (null != e.getMessage() && e.getMessage().startsWith("HTML error for")) {
                System.err.println(e.getMessage());
            } else {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Subclasses can override this to change the behaviour
     * 
     * @return true if a "bad request" should be considered a failure
     */
    protected boolean stopOnBadRequest() {
        return false;
    }

    /**
     * Check a page, and select a link or form from it TODO bias against links to browse non-HTML reports
     * 
     * @param page WebResponse representing the page to check
     * @param depth depth of search when following links
     * @throws IOException
     */
    /**
     * RandomGet.chooseNextRequest
     * 
     * @param page
     * @return
     */
    protected WebRequest chooseNextRequest(final WebResponse page) {
        if (null == page) {
            return null;
        }
        try {

            //Document dom = null;
            try {
                /* Assert.assertTrue("character set: " + page.getCharacterSet(),
                    "utf-8".equalsIgnoreCase(page.getCharacterSet())
                ); */

                page.getDOM();
            } catch (final junit.framework.AssertionFailedError e) {
                // probably an HTML error
                /* If you are having trouble reproducing it, uncomment these lines:
                try {
                    System.out.println(page.getText());
                } catch (final IOException e1) {
                    throw new RuntimeException(e1);
                } */
                throw e;
            }
            if (!page.isHTML()) {
                return null;
            }
            if (0 == this.random.nextInt(10)) {
                return null; // prevents following a closed loop, e.g. going forward in calendar
            }

            // often use a form, if there is one
            if (!this.isStaticTest() && 0 == this.random.nextInt(5)) {
                // TODO bias against reset, cancel, and abandon buttons and barcode
                // form

                final WebForm[] forms = page.getForms();
                final List<WebForm> relevantForms = new ArrayList(forms.length);
                for (int i = 0; i < forms.length; i++) {
                    final WebForm form = forms[i];
                    if (100 < form.getParameterNames().length) {
                        // some lists have lots of check boxes, if all checked makes a big URL
                        // ... and we get an IO Exception
                        System.err.println("Form too big to test in: " + page.getURL());
                        continue;
                    }
                    final String urlString = form.getAction();
                    if (!this.isLinkRelevant(urlString, page)) {
                        continue;
                    }
                    relevantForms.add(form);
                }
                if (0 < relevantForms.size()) {
                    final int index = this.random.nextInt(relevantForms.size());
                    final WebForm form = relevantForms.get(index);
                    this.addToChecked(form.getAction());
                    System.out.println("Submitting: " + form.getAction()); //TODO remove
                    return this.submit(form); // TODO check that the form has an
                    // enabled button
                }
            }

            // check the links
            final WebLink[] links = page.getLinks();
            final List<WebLink> relevantLinks = new ArrayList(links.length);
            for (int i = 0; i < links.length; i++) {
                final WebLink link = links[i];
                final String urlString = link.getURLString();
                if (!this.isLinkRelevant(urlString, page)) {
                    continue;
                }
                relevantLinks.add(link);
            }
            if (0 == relevantLinks.size()) {
                return null;
            }
            final int index = this.random.nextInt(relevantLinks.size());
            final WebLink webLink = relevantLinks.get(index);
            System.out.println("Getting: " + webLink.getURLString());
            this.addToChecked(webLink.getURLString());
            return webLink.getRequest();
        } catch (final SAXException ex) {
            // page is not HTML. Unfortunately the isHtml() mthod that is called above is not reliable
            System.out.println("Parse error: " + ex.getMessage());
            return null;
        } catch (final OutOfMemoryError e) {
            System.err.println("Page too big to parse: " + page.getURL());
        }
        return null;
    }

    private WebRequest submit(final WebForm form) {
        final SubmitButton[] buttons = form.getSubmitButtons();
        if (0 == buttons.length) {
            Assert.fail("No buttons in form for: " + form.getAction());
        }
        final SubmitButton button = buttons[this.random.nextInt(buttons.length)];
        //System.out.println(form.getAction() + " submitted");
        if (button.isDisabled()) {
            System.out.println("Disabled button"); // could check for this
            // earlier
            return null;
        }
        // try to fill in the form
        final String[] names = form.getParameterNames();
        for (int i = 0; i < names.length; i++) {
            final String name = names[i];
            String value = this.chooseValue(name);
            if (form.isHiddenParameter(name) || form.isDisabledParameter(name)
                || form.isFileParameter(name) // LATER have some standard
                // files
                || form.isReadOnlyParameter(name) || null != form.getSubmitButton(name)
                || name.contains("date") // LATER set some dates
            ) {
                continue; // can't set these
            }
            final String[] options = form.getOptionValues(name);
            if (0 < options.length) {
                value = options[this.random.nextInt(options.length)];
                if ("Search".equals(value)) {
                    continue; // this one has an onclick event
                }
            }
            try {
                // TODO sometimes use attack value
                // - at present attack values in name cause errors for targets
                form.setParameter(name, value);
                //TODO if it's a checkbox could  form.removeParameter( "name" );
            } catch (final com.meterware.httpunit.IllegalRequestParameterException e) {
                // better leave it as it was
                // this will happen if a select has no options
            } catch (final IndexOutOfBoundsException e) {
                // better leave it as it was
                // this will happen if a radio button control has no options
            }
        }
        return form.getRequest(button);
    }

    /**
     * Make a suitable value for filling in a form field
     * 
     * TODO sometimes use attack values
     * 
     * @param name the name of the form field
     * @return a value to try
     */
    private String chooseValue(final String name) {
        String value = "auto" + name + "auto'\"<auto/>";
        if ("name".contains(name) || name.endsWith("Id") || name.endsWith("Name")) {
            // may have a uniqueness constraint
            value = "auto'\"<auto/>" + this.random.nextInt();
            // TODO sometimes cause a name clash,
        } else if (name.endsWith("amount") || name.endsWith("Amount")) {
            value = "0.0mL";
        } else if (RandomGet.INTEGER_FIELDS.contains(name) || name.endsWith("end") || name.endsWith("start")) {
            value = Integer.toString(this.random.nextInt());
        } else if ("date".equalsIgnoreCase(name) || name.endsWith("Date")) {
            value = "01/11/2006";
        } else if (name.startsWith("Position") || name.endsWith("position")) {
            value = "A12";
        } else if (name.endsWith("hyperlink")) {
            value = "";
        } else if (name.endsWith("emperature") || name.endsWith("essure")) {
            value = "0";
        }
        return value;
    }

    /**
     * @return a suite including all tests for this class
     */
    public static Test suite() {
        return new junit.framework.TestSuite(RandomGet.class);
    }

    /**
     * @param args ignored
     */
    public static void main(final String[] args) {
        TestRunner.run(RandomGet.suite());
    }

    public boolean isStaticTest() {
        return false;
    }
}
