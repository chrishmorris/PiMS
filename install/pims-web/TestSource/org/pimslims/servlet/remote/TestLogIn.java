/**
 * 
 */
package org.pimslims.servlet.remote;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;

import org.xml.sax.SAXException;

import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;

/**
 * @author cm65
 * 
 * TODO accept path parameter
 * 
 */
public class TestLogIn extends RemoteTest {

    public static final HashMap BAD_LOG_IN = new HashMap();
    static {
        BAD_LOG_IN.put("j_username", "none such");
        BAD_LOG_IN.put("j_password", "bad password");
    }

    public static final HashMap ADMIN_LOG_IN = new HashMap();
    static {
        ADMIN_LOG_IN.put("j_username", org.pimslims.access.Access.ADMINISTRATOR);
        ADMIN_LOG_IN.put("j_password", "sm1p");
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestLogIn.class);
    }

    /**
     * Constructor for TestLogIn.
     * 
     * @param methodName
     * @throws MalformedURLException
     */
    public TestLogIn(String methodName) throws MalformedURLException {
        super(methodName, new java.net.URL("http://localhost:8080/V2_0/"));
    }

    public void testMustLogIn() throws IOException {
        WebResponse response = get("BrowseTargets", Collections.EMPTY_MAP);
        try {
            String title = response.getTitle();
            assertEquals("log in page", "Log in to PIMS", title);

            response = post("j_security_check", BAD_LOG_IN);
            title = response.getTitle();
            assertEquals("bad log in", "Permission denied", title);
        } catch (SAXException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    public void testAdminLogIn() throws IOException {
        WebResponse response = get("Create/org.pimslims.model.target.Project", Collections.EMPTY_MAP);
        try {
            String title = response.getTitle();
            assertEquals("log in page", "Log in to PIMS", title);

            response = post("j_security_check", ADMIN_LOG_IN);
            WebLink log = response.getLinks()[2]; // first link is logo,
            // second is "Home"
            assertTrue("logged in: " + log.getText(), log.getText().startsWith("Log out"));
        } catch (SAXException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    public void testLogin() throws IOException {
        WebResponse response = get("BrowseTargets", Collections.EMPTY_MAP);
        login(response);
    }

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown() {
        super.tearDown();
    }

}
