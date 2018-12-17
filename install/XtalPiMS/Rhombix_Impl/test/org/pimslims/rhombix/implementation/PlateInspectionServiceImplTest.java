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

import org.pimslims.business.crystallization.service.PlateInspectionServiceTest;

/**
 * TrialServiceImplTest
 * 
 */
public class PlateInspectionServiceImplTest extends PlateInspectionServiceTest {

    /**
     * Constructor for SampleServiceImplTest
     * 
     * @param methodName
     * @param dataStorage
     */
    public PlateInspectionServiceImplTest(String methodName) {
        super(methodName, new RhombixDataStorageImpl(false, RhombixVersion.HELSINKI));
    }

    public void testGetPathPrefixHelsinki() {
        String pimsPath =
            new PlateInspectionServiceImpl(null, RhombixVersion.HELSINKI,
                (RhombixDataStorageImpl) this.dataStorage)
                .getPimsPath("\\\\cdbmaster\\Images\\2006\\ZZAA00123\\ZZA00123_A01-1_BRITE_110");
        assertEquals("/Images/2006/ZZAA00123/ZZA00123_A01-1_BRITE_110.jpg", pimsPath);
    }

    public void testGetPathPrefixMPL() {
        String pimsPath =
            new PlateInspectionServiceImpl(null, RhombixVersion.MPL,
                (RhombixDataStorageImpl) this.dataStorage)
                .getPimsPath("\\\\DC1512086\\Images_Fdrv\\DCA\\TG-CQ-MG-20\\110629\\TG-CQ-MG-20_A01-1_BRITE_110629_200901_1");
        assertEquals("/DCA/TG-CQ-MG-20/110629/TG-CQ-MG-20_A01-1_BRITE_110629_200901_1.jpg", pimsPath);
    }

    public void testGetPathSuffix() {
        String pimsPath =
            new PlateInspectionServiceImpl(null, RhombixVersion.HELSINKI,
                (RhombixDataStorageImpl) this.dataStorage).getPimsPath("aaa/C4.2.1.bmp");
        assertEquals("aaa/C4.2.1.bmp", pimsPath);
    }
}
