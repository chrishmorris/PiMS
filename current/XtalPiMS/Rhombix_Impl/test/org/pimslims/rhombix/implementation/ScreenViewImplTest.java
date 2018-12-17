/**
 * Rhombix_Impl org.pimslims.rhombix.implementation ScreenViewImplTest.java
 * 
 * @author cm65
 * @date 23 Jun 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

import org.pimslims.crystallization.integration.ScreenViewTest;

/**
 * ScreenViewImplTest
 * 
 */
public class ScreenViewImplTest extends ScreenViewTest {

    /**
     * Constructor for ScreenViewImplTest
     * 
     * @param methodName
     * @param dataStorage
     */
    public ScreenViewImplTest(String methodName) {
        super(methodName, new RhombixDataStorageImpl(false, RhombixVersion.HELSINKI));
    }

}
