/**
 *  ImageSaveAndFindImplTest.java
 * 
 * @author cm65
 * @date 26 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.formulatrix.implementation;

import org.pimslims.business.crystallization.service.ImageSaveAndFindTest;
import org.pimslims.formulatrix.implementation.ManufacturerDataStorageImpl;
import org.pimslims.formulatrix.implementation.ManufacturerVersion;

/**
 * ImageSaveAndFindImplTest
 * 
 */
//TODO 03
public class ImageSaveAndFindImplTest extends ImageSaveAndFindTest {

    public ImageSaveAndFindImplTest(String methodName) {
        super(methodName, new ManufacturerDataStorageImpl(false, ManufacturerVersion.OULU));
    }

}
