/**
 * V2_0-pims-web org.pimslims.servlet.mock MockInitialontext.java
 * 
 * @author cm65
 * @date 2 Jan 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.properties;

import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * MockInitialontext
 * 
 */
public class PimsInitialContext implements Context {

    /**
     * @see javax.naming.Context#addToEnvironment(java.lang.String, java.lang.Object)
     */
    public Object addToEnvironment(String arg0, Object arg1) throws NamingException {

        return null;
    }

    /**
     * @see javax.naming.Context#bind(javax.naming.Name, java.lang.Object)
     */
    public void bind(Name arg0, Object arg1) throws NamingException {

    }

    /**
     * @see javax.naming.Context#bind(java.lang.String, java.lang.Object)
     */
    public void bind(String arg0, Object arg1) throws NamingException {

    }

    /**
     * @see javax.naming.Context#close()
     */
    public void close() throws NamingException {

    }

    /**
     * @see javax.naming.Context#composeName(javax.naming.Name, javax.naming.Name)
     */
    public Name composeName(Name arg0, Name arg1) throws NamingException {

        return null;
    }

    /**
     * @see javax.naming.Context#composeName(java.lang.String, java.lang.String)
     */
    public String composeName(String arg0, String arg1) throws NamingException {

        return null;
    }

    /**
     * @see javax.naming.Context#createSubcontext(javax.naming.Name)
     */
    public Context createSubcontext(Name arg0) throws NamingException {

        return null;
    }

    /**
     * @see javax.naming.Context#createSubcontext(java.lang.String)
     */
    public Context createSubcontext(String arg0) throws NamingException {

        return null;
    }

    /**
     * @see javax.naming.Context#destroySubcontext(javax.naming.Name)
     */
    public void destroySubcontext(Name arg0) throws NamingException {

    }

    /**
     * @see javax.naming.Context#destroySubcontext(java.lang.String)
     */
    public void destroySubcontext(String arg0) throws NamingException {

    }

    /**
     * @see javax.naming.Context#getEnvironment()
     */
    public Hashtable<?, ?> getEnvironment() throws NamingException {

        return null;
    }

    /**
     * @see javax.naming.Context#getNameInNamespace()
     */
    public String getNameInNamespace() throws NamingException {

        return null;
    }

    /**
     * @see javax.naming.Context#getNameParser(javax.naming.Name)
     */
    public NameParser getNameParser(Name arg0) throws NamingException {

        return null;
    }

    /**
     * @see javax.naming.Context#getNameParser(java.lang.String)
     */
    public NameParser getNameParser(String arg0) throws NamingException {

        return null;
    }

    /**
     * @see javax.naming.Context#list(javax.naming.Name)
     */
    public NamingEnumeration<NameClassPair> list(Name arg0) throws NamingException {

        return null;
    }

    /**
     * @see javax.naming.Context#list(java.lang.String)
     */
    public NamingEnumeration<NameClassPair> list(String arg0) throws NamingException {

        return null;
    }

    /**
     * @see javax.naming.Context#listBindings(javax.naming.Name)
     */
    public NamingEnumeration<Binding> listBindings(Name arg0) throws NamingException {

        return null;
    }

    /**
     * @see javax.naming.Context#listBindings(java.lang.String)
     */
    public NamingEnumeration<Binding> listBindings(String arg0) throws NamingException {

        return null;
    }

    /**
     * @see javax.naming.Context#lookup(javax.naming.Name)
     */
    public Object lookup(Name arg0) throws NamingException {

        return null;
    }

    /**
     * @see javax.naming.Context#lookup(java.lang.String)
     */
    public Object lookup(String arg0) throws NamingException {
        if (PropertyGetter.TOMCAT_NAMING_CONTEXT.equals(arg0)) {
            return new PimsTomcatContext();
        }
        return null;
    }

    /**
     * @see javax.naming.Context#lookupLink(javax.naming.Name)
     */
    public Object lookupLink(Name arg0) throws NamingException {

        return null;
    }

    /**
     * @see javax.naming.Context#lookupLink(java.lang.String)
     */
    public Object lookupLink(String arg0) throws NamingException {

        return null;
    }

    /**
     * @see javax.naming.Context#rebind(javax.naming.Name, java.lang.Object)
     */
    public void rebind(Name arg0, Object arg1) throws NamingException {

    }

    /**
     * @see javax.naming.Context#rebind(java.lang.String, java.lang.Object)
     */
    public void rebind(String arg0, Object arg1) throws NamingException {

    }

    /**
     * @see javax.naming.Context#removeFromEnvironment(java.lang.String)
     */
    public Object removeFromEnvironment(String arg0) throws NamingException {

        return null;
    }

    /**
     * @see javax.naming.Context#rename(javax.naming.Name, javax.naming.Name)
     */
    public void rename(Name arg0, Name arg1) throws NamingException {

    }

    /**
     * @see javax.naming.Context#rename(java.lang.String, java.lang.String)
     */
    public void rename(String arg0, String arg1) throws NamingException {

    }

    /**
     * @see javax.naming.Context#unbind(javax.naming.Name)
     */
    public void unbind(Name arg0) throws NamingException {

    }

    /**
     * @see javax.naming.Context#unbind(java.lang.String)
     */
    public void unbind(String arg0) throws NamingException {

    }

}
