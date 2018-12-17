/**
 * Rhombix_Impl org.pimslims.rhombix RhombixDataStorageImplTest.java
 * 
 * @author cm65
 * @date 12 Apr 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

import junit.framework.TestCase;

import org.junit.Test;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.rhombix.implementation.RhombixDataStorageImpl;

/**
 * RhombixDataStorageImplTest
 * 
 */
public class RhombixDataStorageImplTest extends TestCase {

    /**
     * Test method for {@link org.pimslims.rhombix.implementation.RhombixDataStorageImpl#openResources(java.lang.String)}.
     * 
     * @throws BusinessException
     */
    @Test
    public void testOpenResourcesReadOnly() throws BusinessException {
        RhombixDataStorageImpl ds = new RhombixDataStorageImpl(true, RhombixVersion.HELSINKI);
        ds.openResources("administrator");
        ds.abort();
    }

}
