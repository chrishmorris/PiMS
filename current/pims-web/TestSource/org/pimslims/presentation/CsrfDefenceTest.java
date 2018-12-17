/**
 * V4_3-web org.pimslims.presentation CsrfDefenceTest.java
 * 
 * @author cm65
 * @date 11 May 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpSession;

/**
 * CsrfDefenceTest
 * 
 */
public class CsrfDefenceTest extends TestCase {

    public void testRegExp() {
        final Pattern pattern = Pattern.compile("^(http://.*?)/current/.*");
        final Matcher matcher =
            pattern.matcher("http://localhost:4040/current/JSP/experiment/ExperimentDetails.jsp");
        Assert.assertTrue(matcher.matches());
        //System.out.println(matcher.group(1));
    }

    public void testGoodToken() throws ServletException {
        final MockHttpSession session = new MockHttpSession("");
        final Map<String, String[]> parms = new HashMap();
        final MockHttpServletRequest request1 = new MockHttpServletRequest("get", session, parms);
        request1.setReferer(MockHttpServletRequest.PIMS_URI + "Referer");
        request1.setRequestURL(MockHttpServletRequest.PIMS_URI + "Form");
        final String token = CsrfDefence.getReusableToken(request1, "/Delete");
        Assert.assertTrue(token, token.endsWith(":/pims/Delete"));
    }

    public void testGoodTokenAccepted() throws ServletException {
        final MockHttpSession session = new MockHttpSession("");
        final Map<String, String[]> parms = new HashMap();
        final MockHttpServletRequest request1 = new MockHttpServletRequest("get", session, parms);
        request1.setReferer(MockHttpServletRequest.PIMS_URI + "Referer");
        request1.setRequestURL(MockHttpServletRequest.PIMS_URI + "Form");
        final String token = CsrfDefence.getReusableToken(request1, "/Delete");
        parms.put(CsrfDefence.PARAMETER, new String[] { token });
        final MockHttpServletRequest request2 = new MockHttpServletRequest("post", session, parms);
        request2.setReferer(MockHttpServletRequest.PIMS_URI + "Form");
        request2.setRequestURL(MockHttpServletRequest.PIMS_URI + "Delete");
        request2.setServletPath("/Delete");
        CsrfDefence.validate(request2);
    }

    public void TODOtestValidate() {
        final MockHttpSession session = new MockHttpSession("");
        final Map<String, String[]> parms = new HashMap();
        final MockHttpServletRequest request = new MockHttpServletRequest("", session, parms);
        parms.put(CsrfDefence.PARAMETER, new String[] { "badToken" });
        try {
            CsrfDefence.validate(request);
            Assert.fail("Bad token accepted");
        } catch (final ServletException e) {
            // that's right
        }
    }

    public void TODOtestOtherUser() throws ServletException {
        final MockHttpSession session = new MockHttpSession("user1");
        final Map<String, String[]> parms = new HashMap();
        final MockHttpServletRequest request = new MockHttpServletRequest("", session, parms);
        request.setRequestURL(MockHttpServletRequest.PIMS_URI + "Form");
        final String token = CsrfDefence.getReusableToken(request, "");
        parms.put(CsrfDefence.PARAMETER, new String[] { token });
        final MockHttpServletRequest request2 =
            new MockHttpServletRequest("", new MockHttpSession("user2"), parms);
        try {
            CsrfDefence.validate(request2);
            Assert.fail("Other user's token accepted");
        } catch (final ServletException e) {
            // that's right
        }
    }

    public void TODOtestOtherAction() throws ServletException {
        final MockHttpSession session = new MockHttpSession("user1");
        final Map<String, String[]> parms = new HashMap();
        final MockHttpServletRequest request = new MockHttpServletRequest("", session, parms);
        request.setRequestURL(MockHttpServletRequest.PIMS_URI + "Form");
        final String token = CsrfDefence.getReusableToken(request, "/Remove");
        parms.put(CsrfDefence.PARAMETER, new String[] { token });
        final MockHttpServletRequest request2 = new MockHttpServletRequest("", session, parms);
        request2.setServletPath("/Delete");
        try {
            CsrfDefence.validate(request2);
            Assert.fail("Other form's token accepted");
        } catch (final ServletException e) {
            // that's right
        }
    }

    public void TODOtestOtherReferer() throws ServletException {
        final MockHttpSession session = new MockHttpSession("user1");
        final Map<String, String[]> parms = new HashMap();
        final MockHttpServletRequest request = new MockHttpServletRequest("post", session, parms);
        request.setRequestURL(MockHttpServletRequest.PIMS_URI + "Form");
        request.setReferer("http://www.example.org/pims/a");
        final String token = CsrfDefence.getReusableToken(request, "/Remove");
        parms.put(CsrfDefence.PARAMETER, new String[] { token });
        final MockHttpServletRequest request2 = new MockHttpServletRequest("post", session, parms);
        request2.setServletPath("/Remove");
        request2.setReferer("http://www.example.org/pims/b");
        try {
            CsrfDefence.validate(request2);
            Assert.fail("Other referer's token accepted");
        } catch (final ServletException e) {
            // that's right
        }
    }

}
