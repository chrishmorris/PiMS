/**
 * V2_0-pims-web org.pimslims.servlet.mock MockHttpSession.java
 * 
 * @author cm65
 * @date 11 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.presentation.mock;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 * MockHttpSession
 * 
 */
public class MockHttpSession implements HttpSession {

    private final String username;

    private final Map<String, Object> attributes = new HashMap();

    /**
     * @param administrator
     */
    public MockHttpSession(final String username) {
        this.username = username;
    }

    /**
     * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
     */
    public Object getAttribute(final String arg0) {
        return this.attributes.get(arg0);
    }

    /**
     * @see javax.servlet.http.HttpSession#getAttributeNames()
     */
    public Enumeration getAttributeNames() {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.http.HttpSession#getCreationTime()
     */
    public long getCreationTime() {
        // // COULD implement
        return 0;
    }

    /**
     * @see javax.servlet.http.HttpSession#getId()
     */
    public String getId() {
        return Integer.toHexString(this.hashCode());
    }

    /**
     * @see javax.servlet.http.HttpSession#getLastAccessedTime()
     */
    public long getLastAccessedTime() {
        // // COULD implement
        return 0;
    }

    /**
     * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
     */
    public int getMaxInactiveInterval() {
        // // COULD implement
        return 0;
    }

    /**
     * @see javax.servlet.http.HttpSession#getServletContext()
     */
    public ServletContext getServletContext() {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.http.HttpSession#getSessionContext()
     * @deprecated
     */
    @Deprecated
    public javax.servlet.http.HttpSessionContext getSessionContext() {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.http.HttpSession#getValue(java.lang.String)
     * @deprecated
     */
    @Deprecated
    public Object getValue(final String arg0) {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.http.HttpSession#getValueNames()
     * @deprecated
     */
    @Deprecated
    public String[] getValueNames() {
        // // COULD implement
        return null;
    }

    /**
     * @see javax.servlet.http.HttpSession#invalidate()
     */
    public void invalidate() {
        // // COULD implement

    }

    /**
     * @see javax.servlet.http.HttpSession#isNew()
     */
    public boolean isNew() {
        // // COULD implement
        return false;
    }

    /**
     * @see javax.servlet.http.HttpSession#putValue(java.lang.String, java.lang.Object)
     * @deprecated
     */
    @Deprecated
    public void putValue(final String arg0, final Object arg1) {
        // // COULD implement

    }

    /**
     * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
     */
    public void removeAttribute(final String arg0) {
        // // COULD implement

    }

    /**
     * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
     * @deprecated
     */
    @Deprecated
    public void removeValue(final String arg0) {
        // // COULD implement

    }

    /**
     * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String, java.lang.Object)
     */
    public void setAttribute(final String arg0, final Object arg1) {
        this.attributes.put(arg0, arg1);
    }

    /**
     * @see javax.servlet.http.HttpSession#setMaxInactiveInterval(int)
     */
    public void setMaxInactiveInterval(final int arg0) {
        // // COULD implement

    }

    /**
     * @return
     */
    public String getUsername() {
        return this.username;
    }

}
