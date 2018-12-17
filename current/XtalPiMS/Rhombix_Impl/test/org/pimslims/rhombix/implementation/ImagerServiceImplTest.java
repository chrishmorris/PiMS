/**
 * Rhombix_Impl org.pimslims.rhombix ImagerServiceImplTest.java
 * 
 * @author cm65
 * @date 17 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

import org.pimslims.business.crystallization.service.ImagerServiceTest;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.rhombix.implementation.RhombixDataStorageImpl;

/**
 * ImagerServiceImplTest
 * 
 */
public class ImagerServiceImplTest extends ImagerServiceTest {

    /**
     * Constructor for ImagerServiceImplTest
     * 
     * @param methodName
     * @param dataStorage
     */
    public ImagerServiceImplTest(String methodName) {
        super(methodName, new RhombixDataStorageImpl(false, RhombixVersion.HELSINKI));
    }


    /**
     * ImagerServiceImplTest.testFindSchedules
     * 
     * @see org.pimslims.business.crystallization.service.ImagerServiceTest#testFindSchedules()
     */
    @Override
    public void testFindSchedules() throws BusinessException {
        // not needed
    }

    /**
     * ImagerServiceImplTest.testSetPriority
     * 
     * @see org.pimslims.business.crystallization.service.ImagerServiceTest#testSetPriority()
     */
    @Override
    public void testSetPriority() throws BusinessException {
        // not needed
    }

    /**
     * ImagerServiceImplTest.testSchedulePlate
     * 
     * @see org.pimslims.business.crystallization.service.ImagerServiceTest#testSchedulePlate()
     */
    @Override
    public void testSchedulePlate() throws BusinessException {
        // not needed
    }

    /**
     * ImagerServiceImplTest.testFinishedImaging
     * 
     * @see org.pimslims.business.crystallization.service.ImagerServiceTest#testFinishedImaging()
     */
    @Override
    public void testFinishedImaging() throws BusinessException {
        // not needed
    }

    /**
     * ImagerServiceImplTest.testSkippedImaging
     * 
     * @see org.pimslims.business.crystallization.service.ImagerServiceTest#testSkippedImaging()
     */
    @Override
    public void testSkippedImaging() throws BusinessException {
        // not needed
    }

    /**
     * ImagerServiceImplTest.testStartedImaging
     * 
     * @see org.pimslims.business.crystallization.service.ImagerServiceTest#testStartedImaging()
     */
    @Override
    public void testStartedImaging() throws BusinessException {
        // not needed
    }

    /**
     * ImagerServiceImplTest.testStartedUnscheduledImaging
     * 
     * @see org.pimslims.business.crystallization.service.ImagerServiceTest#testStartedUnscheduledImaging()
     */
    @Override
    public void testStartedUnscheduledImaging() throws BusinessException {
        // not needed
    }

}
