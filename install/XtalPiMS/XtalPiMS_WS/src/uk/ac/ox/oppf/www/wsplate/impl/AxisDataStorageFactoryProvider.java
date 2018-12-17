/** 
 * xtalpims-ws-stub uk.ac.ox.oppf.www.wsplate.impl AxisDataStorageFactoryProvider.java
 * @author Jon
 * @date 26 Aug 2010
 *
 * Protein Information Management System
 * @version: 4.1
 *
 * Copyright (c) 2010 Jon 
 * The copyright holder has licenced the STFC to redistribute this software
 */
package uk.ac.ox.oppf.www.wsplate.impl;

import org.pimslims.crystallization.datastorage.DataStorageFactory;

import uk.ac.ox.oppf.www.wsplate.util.AxisUtil;

/**
 * AxisDataStorageFactoryProvider
 *
 */
public class AxisDataStorageFactoryProvider implements DataStorageFactoryProvider {

    /**
     * AxisDataStorageFactoryProvider.getDataStorageFactory
     * @see uk.ac.ox.oppf.www.wsplate.impl.DataStorageFactoryProvider#getDataStorageFactory()
     */
    @Override
    public DataStorageFactory getDataStorageFactory() {
        return AxisUtil.getDataStorageFactory();
    }

    /**
     * AxisDataStorageFactoryProvider.getUsername
     * @see uk.ac.ox.oppf.www.wsplate.impl.DataStorageFactoryProvider#getUsername()
     */
    @Override
    public String getUsername() {
        return AxisUtil.getBasicUsername();
    }

}
