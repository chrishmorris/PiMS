/**
 * V2_0-pims-web org.pimslims.servlet.mock MockContextFactory.java
 * 
 * @author cm65
 * @date 2 Jan 2008
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2008 cm65 
 * 
 * 
 */
package org.pimslims.properties;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 * MockContextFactory
 * 
 */
/**
 * Provides a JNDI initial context factory for the MockContext.
 */
public class PimsInitialContextFactory implements InitialContextFactory {

    public Context getInitialContext(Hashtable env) throws NamingException {
        return new PimsInitialContext();
    }

}
