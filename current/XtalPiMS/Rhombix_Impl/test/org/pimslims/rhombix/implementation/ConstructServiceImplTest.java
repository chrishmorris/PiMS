/**
 * Rhombix_Impl org.pimslims.rhombix ConstructServiceImplTest.java
 * 
 * @author cm65
 * @date 10 May 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

import org.pimslims.crystallization.business.ConstructServiceTest;
import org.pimslims.rhombix.implementation.RhombixDataStorageImpl;

/**
 * ConstructServiceImplTest
 * 
 */
public class ConstructServiceImplTest extends ConstructServiceTest {

    /**
     * Constructor for ConstructServiceImplTest
     * 
     * @param methodName
     * @param dataStorage
     */
    public ConstructServiceImplTest(String methodName) {
        super(methodName, new RhombixDataStorageImpl(false, RhombixVersion.HELSINKI));
    }

    /**
     * ConstructServiceImplTest.testUpdate
     * 
     * @see org.pimslims.crystallization.business.ConstructServiceTest#testUpdate()
     */
    @Override
    public void testUpdate() throws Exception {
        // probably not needed
    }

    /**
     * ConstructServiceImplTest.testFindByUsername
     * 
     * @see org.pimslims.crystallization.business.ConstructServiceTest#testFindByUsername()
     */
    @Override
    public void testFindByUsername() throws Exception {
        // TODO link to user
    }

}
