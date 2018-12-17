/**
 *  DataStorageImplTest.java
 * 
 * @author cm65
 * @date 12 Apr 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.formulatrix.implementation;

import junit.framework.TestCase;

import org.junit.Test;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.formulatrix.implementation.ManufacturerDataStorageImpl;
import org.pimslims.formulatrix.implementation.ManufacturerVersion;

/**
 * DataStorageImplTest
 * 
 */
public class ManufacturerDataStorageImplTest extends TestCase {

    /**
     * Test method for {@link org.pimslims.formulatrix.implementation.ManufacturerDataStorageImpl#openResources(java.lang.String)}.
     * 
     * @throws BusinessException
     */
    @Test
    public void testOpenResourcesReadOnly() throws BusinessException {
        ManufacturerDataStorageImpl ds = new ManufacturerDataStorageImpl(true, ManufacturerVersion.OULU);
        ds.openResources("administrator");
        ds.abort();
    }

}
