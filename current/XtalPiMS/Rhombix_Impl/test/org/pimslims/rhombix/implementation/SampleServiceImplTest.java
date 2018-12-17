/**
 * Rhombix_Impl org.pimslims.rhombix SampleServiceImplTest.java
 * 
 * @author cm65
 * @date 12 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

import org.pimslims.crystallization.business.SampleServiceTest;
import org.pimslims.rhombix.implementation.RhombixDataStorageImpl;

/**
 * SampleServiceImplTest
 * 
 */
public class SampleServiceImplTest extends SampleServiceTest {

    /**
     * Constructor for SampleServiceImplTest
     * 
     * @param methodName
     * @param dataStorage
     */
    public SampleServiceImplTest(String methodName) {
        super(methodName, new RhombixDataStorageImpl(false, RhombixVersion.HELSINKI));
    }

    /**
     * SampleServiceImplTest.testScientist
     * 
     * @see org.pimslims.crystallization.business.SampleServiceTest#testScientist()
     */
    @Override
    public void testScientist() throws Exception {
        // TODO reinstate this test when PersonService is implemented
    }

}
