/**
 * V3_3-web org.pimslims.servlet SessionListener.java
 * 
 * @author cm65
 * @date 8 Jan 2010
 * 
 * Protein Information Management System
 * @version: 3.2
 * 
 * Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

/**
 * SessionListener
 * 
 */
public class SessionListener implements javax.servlet.http.HttpSessionListener {

    /**
     * index int Used to make session ids unique, more or less The use of this is not thread safe. For this
     * purpose, that doesn't really matter.
     */
    private static int index = 0;

    /**
     * SESSION_TOKEN String
     */
    public static final String SESSION_TOKEN = "_SESSION_TOKEN";

    /**
     * SessionListener.sessionCreated
     * 
     * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
     */
    public void sessionCreated(final HttpSessionEvent event) {
        final HttpSession session = event.getSession();
        //final byte[] seed = (byte[]) session.getServletContext().getAttribute(Listener.RANDOM_SEED);
        final String token = "security token" + (SessionListener.index++);
        session.setAttribute(SessionListener.SESSION_TOKEN, token); //TODO make it with a hash
    }

    /**
     * SessionListener.sessionDestroyed
     * 
     * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
     */
    public void sessionDestroyed(final HttpSessionEvent event) {
        // nothing to do. See Logout servlet.
    }

}
