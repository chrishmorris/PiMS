/**
 * Rhombix_Impl org.pimslims.rhombix ScreenServiceImplTest.java
 * 
 * @author cm65
 * @date 25 Mar 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

import org.pimslims.crystallization.business.ScreenServiceTest;

/**
 * ScreenServiceImplTest
 * 
 */
public class ScreenServiceImplTest extends ScreenServiceTest {

    /**
     * Constructor for ScreenServiceImplTest
     * 
     * @param methodName
     * @param dataStorage
     */
    public ScreenServiceImplTest(String methodName) {
        super(methodName, new RhombixDataStorageImpl(false, RhombixVersion.HELSINKI));
    }

}
