/** 
 * xtalpims-ws-stub uk.ac.ox.oppf.www.wsplate.impl DataStorageFactoryProvider.java
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

/**
 * DataStorageFactoryProvider
 *
 */
public interface DataStorageFactoryProvider {

    /**
     * 
     * DataStorageFactoryProvider.getDataStorageFactory
     * @return
     */
    DataStorageFactory getDataStorageFactory();

    /**
     * 
     * DataStorageFactoryProvider.getUsername
     * @return
     */
    String getUsername();

}
