/*
 * Created on 09-Dec-2004 @author: Chris Morris
 */
package org.pimslims.model.api;

import java.util.Collections;

import org.pimslims.dao.ModelImpl;
import org.pimslims.model.core.Citation;

/**
 * Tests generated class for Citation, an abstract class
 */
public class CitationMetaClassTester extends org.pimslims.metamodel.TestAMetaClass {

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    public CitationMetaClassTester() {
        super(Citation.class.getName(), ModelImpl.getModel(), "testing implementation of citation class",
            Collections.EMPTY_MAP);
    }

    public void testGetSubTypes() {
        assertEquals("subtypes", 4, metaClass.getSubtypes().size());
    }

    public void testIsAbstract() {
        assertTrue("abstract", metaClass.isAbstract());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.TestAMetaClass#testVersion()
     */
    @Override
    public void testVersion() {
        try {
            super.testVersion();
            fail("Created an abstract class");
        } catch (RuntimeException e) {
            // that's fine, it's an abstract class
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.TestAMetaClass#testFindAll()
     */
    @Override
    public void testFindAll() {
        // dont need this test
    }

}
