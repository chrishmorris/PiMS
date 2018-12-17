/** 
 * xtalpims-ws-stub uk.ac.ox.oppf.www.wsplate.impl MockDataStorageFactoryProvider.java
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
 * MockDataStorageFactoryProvider
 *
 */
public class MockDataStorageFactoryProvider implements DataStorageFactoryProvider {

    /**
     * The username to connect with
     */
    private String username;

    /**
     * Constructor for MockDataStorageFactoryProvider
     */
    public MockDataStorageFactoryProvider() {
        this(org.pimslims.access.Access.ADMINISTRATOR);
    }
    
    /**
     * Constructor for MockDataStorageFactoryProvider 
     * @param username
     */
    public MockDataStorageFactoryProvider(String username) {
        setUsername(username);
    }
    
    
    /**
     * MockDataStorageFactoryProvider.getDataStorageFactory
     * @see uk.ac.ox.oppf.www.wsplate.impl.DataStorageFactoryProvider#getDataStorageFactory()
     */
    @Override
    public DataStorageFactory getDataStorageFactory() {
        return DataStorageFactory.getDataStorageFactory();
    }

    /**
     * MockDataStorageFactoryProvider.getUsername
     * @see uk.ac.ox.oppf.www.wsplate.impl.DataStorageFactoryProvider#getUsername()
     */
    @Override
    public String getUsername() {
        return username;
    }
    
    /**
     * 
     * MockDataStorageFactoryProvider.setUsername
     * @param username
     * @return
     */
    public void setUsername(String username) {
        this.username = username;
    }

}
