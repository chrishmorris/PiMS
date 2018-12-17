/**
 * Rhombix_Impl org.pimslims.rhombix TrialServiceImplTest.java
 * 
 * @author cm65
 * @date 13 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

import org.pimslims.business.crystallization.service.TrialServiceTest;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.rhombix.implementation.RhombixDataStorageImpl;

/**
 * TrialServiceImplTest
 * 
 */
public class TrialServiceImplTest extends TrialServiceTest {

    /**
     * Constructor for SampleServiceImplTest
     * 
     * @param methodName
     * @param dataStorage
     */
    public TrialServiceImplTest(String methodName) {
        super(methodName, new RhombixDataStorageImpl(false, RhombixVersion.HELSINKI));
    }

    /**
     * TrialServiceImplTest.testSetScreen
     * 
     * @see org.pimslims.business.crystallization.service.TrialServiceTest#testSetScreen()
     */
    @Override
    public void testSetScreen() throws BusinessException {
        // TODO reinstate this 
    }

}
