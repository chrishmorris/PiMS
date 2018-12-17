/**
 * V4_3-web org.pimslims.presentation CsrfDefence.java
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

import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * CsrfDefence TODO encrypt the tokens, so they cant be forged TODO add these to remove, copy, and update
 * forms
 */
public class CsrfDefence {

    public static final String PARAMETER = "_csrf";

    /**
     * CsrfDefence.validate
     * 
     * @param request
     */
    public static void validate(final HttpServletRequest request) throws ServletException {
        final CsrfDefence defence = CsrfDefence.getDefence(request);
        defence.doValidate(request);

    }

    /**
     * CsrfDefence.getDefence
     * 
     * @param request
     * @return
     */
    private static CsrfDefence getDefence(final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        CsrfDefence defence = (CsrfDefence) session.getAttribute(CsrfDefence.PARAMETER);
        if (null == defence) {
            defence = new CsrfDefence();
            session.setAttribute(CsrfDefence.PARAMETER, defence);
        }
        return defence;
    }

    /**
     * CsrfDefence.getReusableToken
     * 
     * @param request
     * @param string
     * @return
     */
    public static String getReusableToken(final HttpServletRequest request, final String action) {
        final CsrfDefence defence = CsrfDefence.getDefence(request);
        return defence.doGetReusableToken(request, action);
    }

    private final String sessionToken;

    public CsrfDefence() {
        super();
        final long random = new SecureRandom().nextLong();
        this.sessionToken = Long.toHexString(random);
    }

    /**
     * CsrfDefence.doValidate
     * 
     * @param request
     * @return
     */
    private void doValidate(final HttpServletRequest request) throws ServletException {
        final String token = request.getParameter(CsrfDefence.PARAMETER);
        if (null == token) {
            throw new ServletException("No request token");
        }
        String pathInfo = request.getPathInfo();
        if (null == pathInfo) {
            pathInfo = "";
        }
        final String validToken =
            CsrfDefence.getReferer(request) + ":" + this.sessionToken + ":" + request.getContextPath()
                + request.getServletPath() + pathInfo;
        final boolean ok = validToken.equals(token);
        if (!ok) {
            final ServletException ex =
                new ServletException("Request token not valid: " + token + " expected: " + validToken
                    + ".\nWas that page from a previous PiMS session?");
            ex.fillInStackTrace();
            ex.printStackTrace(); //TODO after 4.4.0 throw ex;            
        }
    }

    /**
     * CsrfDefence.getReferer
     * 
     * @param request
     * @return
     */
    private static String getReferer(final HttpServletRequest request) {
        final String header = request.getHeader("Referer");
        if (null == header) {
            return "";
        }
        return header;
    }

    /**
     * CsrfDefence.getPredictedReferer
     * 
     * @param request
     * @return
     */
    private static String getPredictedReferer(final HttpServletRequest request) {
        if (null == request.getHeader("Referer")) {
            // user agent does not report referrer
            return "";
        }
        String action = (String) request.getAttribute("javax.servlet.forward.request_uri");
        if (null == action) {
            // no RequestDispatcher used
            action = request.getRequestURL().toString();
            // that is a complete URL, starting with http://
        } else {
            // relative path from "/", so must complete it
            String protocol = "https";
            if (request.getProtocol().startsWith("HTTP")) {
                protocol = "http";
            }
            action = protocol + "://" + request.getServerName() + ":" + request.getLocalPort() + action;
        }
        if (null != request.getQueryString() && !"".equals(request.getQueryString())) {
            action += "?" + request.getQueryString();
        }
        return action;
    }

    /**
     * CsrfDefence.doGetReusableToken
     * 
     * @param request
     * @param action The URL the form was submitted to
     * @return
     */
    private String doGetReusableToken(final HttpServletRequest request, final String action) {
        /*final StringBuffer context = request.getRequestURL();
        final Pattern pattern = Pattern.compile("^(http://.*?" + request.getContextPath() + ").*");
        final Matcher matcher = pattern.matcher(context);
        if (!matcher.matches()) {
            throw new AssertionError("unable to recognise URL: " + context + " path: "
                + request.getContextPath());
        }
        final String actionUri = matcher.group(1) + action; */
        return CsrfDefence.getPredictedReferer(request) + ":" + this.sessionToken + ":"
            + request.getContextPath() + action;
    }

}
