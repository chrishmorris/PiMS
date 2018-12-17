/*
 * Created on 25-May-2005 @author: Chris Morris
 */
package org.pimslims.servlet.remote;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;

import junit.framework.Test;
import junit.textui.TestRunner;

import org.pimslims.model.people.Organisation;

/**
 * Test the web pages for Organisation
 * 
 * @version 0.1
 */
public class TestOrganisation extends TestAMetaClass {

    /**
     * Values for creating a test sample
     */
    public static final HashMap ATTRIBUTES = new HashMap();
    static {
        ATTRIBUTES.put(Organisation.class.getName() + ":name", "testOrganisation" + new Date().getTime());
        ATTRIBUTES.put(Organisation.class.getName() + ":addresses", "10 Downing Street\nGotham City");
    }

    /**
     * Values for creating a test sample
     */
    public static final HashMap EDIT_ATTRIBUTES = new HashMap();
    static {
        EDIT_ATTRIBUTES.put("name", "testOrganisation" + new Date().getTime());
        EDIT_ATTRIBUTES.put("addresses", "10 Downing Street\nGotham City");
    }

    /**
     * @param methodName the test to run
     * @throws MalformedURLException
     */
    public TestOrganisation(String methodName) throws MalformedURLException {
        super(Organisation.class.getName(), ATTRIBUTES, EDIT_ATTRIBUTES, methodName);
    }

    /**
     * @return a suite including all tests for this class
     */
    public static Test suite() {
        return new junit.framework.TestSuite(TestOrganisation.class);
    }

    /**
     * @param args ignored
     */
    public static void main(final String[] args) {
        TestRunner.run(suite());
    }
}
