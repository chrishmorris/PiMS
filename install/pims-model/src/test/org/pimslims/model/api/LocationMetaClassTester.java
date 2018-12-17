/*
 * Created on 09-Dec-2004 @author: Chris Morris
 */
package org.pimslims.model.api;

import java.util.Collections;
import java.util.HashMap;

import org.pimslims.dao.ModelImpl;
import org.pimslims.model.location.Location;

/**
 * Tests generated class for Location
 * 
 */
@Deprecated
// obsolete
public class LocationMetaClassTester extends org.pimslims.metamodel.TestAMetaClass {

    /**
     * Values for creating a test sample
     */
    public static final HashMap ATTRIBUTES = new HashMap();
    static {
        ATTRIBUTES.put("name", "testLocation" + new java.util.Date().getTime());
    }

    /**
     * 
     */
    public LocationMetaClassTester() {
        super(Location.class.getName(), ModelImpl.getModel(), "testing implementation of Location class",
            ATTRIBUTES);
    }

    /**
     * {@inheritDoc}
     */
    public void testSimple() {
        // ensure this is called first
        super.testSimple();
    }

    /**
     * Test supplying an empty collection
     */
    public void testEmpty() {
        ATTRIBUTES.put("organisation", Collections.EMPTY_SET);
        super.testSimple();
        super.testVersion();
    }

    /**
     * {@inheritDoc}
     */
    public void testFindAll() {
        // LATER implement this later
    }
}
