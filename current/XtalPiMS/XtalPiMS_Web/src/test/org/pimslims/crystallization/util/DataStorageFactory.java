/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.pimslims.crystallization.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.pimslims.business.DataStorage;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ModelImpl;

/**
 * 
 * @author bl67
 */
public class DataStorageFactory {

    /**
     * The name of the DataSource as stated in persistence.xml. Currently java:comp/env/jdbc/ISGO2008DB
     */
    // public static final String DATA_SOURCE_NAME =
    // "java:comp/env/jdbc/ISGO2008DB";
    /**
     * The DataSource to use
     */
    // private static DataSource pimsDataSource = null;

    private static DataStorage pimsdbDataStorage = null;

    private static DataStorage platedbDataStorage = null;

    public DataStorageFactory() {
    }

    /*
     * private static DataStorage getPlatedbDataStorage() { if
     * (platedbDataStorage != null) { return platedbDataStorage; }
     * BasicDataSource ds = new BasicDataSource();
     * ds.setDriverClassName("org.postgresql.Driver");
     * ds.setUrl("jdbc:postgresql://ccp4p:5432/platedb_full");//platedb2
     * ds.setUsername("postgres"); ds.setPassword("****");
     * ds.setDefaultAutoCommit(false); ds.setInitialSize(1); plateDataSource =
     * ds;
     * 
     * ds = new BasicDataSource();
     * ds.setDriverClassName("org.postgresql.Driver");
     * ds.setUrl("jdbc:postgresql://localhost:5432/userdb");
     * ds.setUsername("postgres"); ds.setPassword("****");
     * ds.setDefaultAutoCommit(false); ds.setInitialSize(1); userDataSource =
     * ds;
     * 
     * try { System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
     * "org.apache.naming.java.javaURLContextFactory");
     * System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
     * 
     * // Add java:comp/env/jdbc to the InitialContext final Context
     * initialContext = new InitialContext();
     * initialContext.createSubcontext("java:");
     * initialContext.createSubcontext("java:comp");
     * initialContext.createSubcontext("java:comp/env");
     * initialContext.createSubcontext("java:comp/env/jdbc");
     * 
     * // Put the DataSource into the directory
     * initialContext.bind("java:comp/env/jdbc/PlateDB", plateDataSource);
     * initialContext.bind("java:comp/env/jdbc/UserDB", userDataSource);
     * 
     * final Context context = (Context) initialContext.lookup("java:comp/env");
     * 
     * // was platedbDataStorage = new
     * uk.ac.ox.oppf.platedb.business.DataStorageImpl(context);
     * platedbDataStorage.connectResources();
     * 
     * } catch (final BusinessException ex) {
     * Logger.getLogger(DataStorageFactory.class.getName()).log(Level.SEVERE,
     * null, ex); } catch (final NamingException ex) {
     * Logger.getLogger(DataStorageFactory.class.getName()).log(Level.SEVERE,
     * null, ex); }
     * 
     * return platedbDataStorage; }
     */
    private static DataStorage getPimsdbDataStorage() {
        if (pimsdbDataStorage != null) {
            return pimsdbDataStorage;
        }
        pimsdbDataStorage =
            new org.pimslims.crystallization.datastorage.DataStorageImpl(ModelImpl.getModel());
        try {
            pimsdbDataStorage.connectResources();
        } catch (final BusinessException e) {
            Logger.getLogger(DataStorageFactory.class.getName()).log(Level.SEVERE, null, e);
        }
        return pimsdbDataStorage;
    }

    /*
     * public static DataStorage getPlateDS() { return getPlatedbDataStorage();
     * 
     * }
     */
    public static DataStorage getPiMSDS() {
        return getPimsdbDataStorage();

    }

}
