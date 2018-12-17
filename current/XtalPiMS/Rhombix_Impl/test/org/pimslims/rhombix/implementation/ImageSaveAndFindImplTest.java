/**
 * Rhombix_Impl org.pimslims.rhombix ImageSaveAndFindImplTest.java
 * 
 * @author cm65
 * @date 26 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

import org.pimslims.business.crystallization.service.ImageSaveAndFindTest;

/**
 * ImageSaveAndFindImplTest
 * 
 */
public class ImageSaveAndFindImplTest extends ImageSaveAndFindTest {

    public ImageSaveAndFindImplTest(String methodName) {
        super(methodName, new RhombixDataStorageImpl(false, RhombixVersion.HELSINKI));
    }

}
