/*
 * Created on 25-May-2005 @author: Chris Morris
 */
package org.pimslims.servlet.remote;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.textui.TestRunner;

/**
 * Generic test class for the servlet interface
 * 
 * @version 0.1
 */
public class TestErrorRecovery extends RemoteTest {

    /**
     * model type being tested
     */
    protected final String metaClassName = "nonesuch";

    /**
     * name => values, for use when creating an object of the MetaClass
     */
    private final Map createAttributes = Collections.EMPTY_MAP;

    /**
     * name => values, for use when updating an object of the MetaClass
     */
    // private final Map editAttributes = Collections.EMPTY_MAP;
    /**
     * 
     * @param methodName name of test to run, null for all an instance of the MetaClass
     * @throws MalformedURLException
     */
    public TestErrorRecovery(final String methodName) throws MalformedURLException {
        super(methodName, new java.net.URL("http://localhost:8080/V2_0/"));
    }

    /**
     * Test the basic operations, when given a bad metaClassName
     * 
     * @throws IOException
     */
    public void testBadMetaClassName() throws IOException {
        Map map = new HashMap();
        map.put("_type", this.metaClassName);

        // create
        this.getNotOK("Create/nonesuch", map);
        map = new HashMap(this.createAttributes);
        map.put("_metaClass", this.metaClassName);
        this.postNotOK("Create", map);

        // check view page
        this.getNotOK("View/nonesuch", Collections.EMPTY_MAP);

        // check listt page
        this.getNotOK("Search/nonesuch", Collections.EMPTY_MAP);

        // check edit page
        this.getNotOK("Edit/nonesuch", Collections.EMPTY_MAP);

        // edit
        this.postNotOK("Edit/nonesuch", map); // TODO check this

        // check delete
        this.postNotOK("Delete?nonesuch", map); // TODO check this

    }

    /**
     * @return a suite including all tests for this class
     */
    public static Test suite() {
        return new junit.framework.TestSuite(TestErrorRecovery.class);
    }

    /**
     * Runs these tests from the command line
     * 
     * @param args ignored
     */
    public static void main(final String[] args) {
        TestRunner.run(TestErrorRecovery.suite());
    }
}
