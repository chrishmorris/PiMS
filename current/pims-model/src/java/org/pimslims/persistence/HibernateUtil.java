package org.pimslims.persistence;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.stat.Statistics;
import org.pimslims.dao.ModelImpl;
import org.pimslims.util.InstallationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * It is modified version from CaveatEmptor (Second version of "Hibernate in Action") Basic Hibernate helper
 * class for Hibernate configuration and startup.
 * <p>
 * This class also tries to figure out if JNDI binding of the <tt>SessionFactory</tt> is used, otherwise it
 * falls back to a global static variable (Singleton). If you use this helper class to obtain a
 * <tt>SessionFactory</tt> in your code, you are shielded from these deployment differences.
 * <p>
 * Another advantage of this class is access to the <tt>Configuration</tt> object that was used to build the
 * current <tt>SessionFactory</tt>. You can access mapping metadata programmatically with this API, and even
 * change it and rebuild the <tt>SessionFactory</tt>.
 * <p>
 * If you want to assign a global interceptor, set its fully qualified class name with the system (or
 * hibernate.properties/hibernate.cfg.xml) property <tt>hibernate.util.interceptor_class</tt>. It will be
 * loaded and instantiated on static initialization of HibernateUtil; it has to have a no-argument
 * constructor. You can call <tt>HibernateUtil.getInterceptor()</tt> if you need to provide settings before
 * using the interceptor.
 * <p>
 * Note: This class supports annotations by default, hence needs JDK 5.0 and the Hibernate Annotations library
 * on the classpath. Change the single commented line in the source to make it compile and run on older JDKs
 * with XML mapping files only.
 * <p>
 * Note: This class supports only one data store. Support for several <tt>SessionFactory</tt> instances can be
 * easily added (through a static <tt>Map</tt>, for example). You could then lookup a <tt>SessionFactory</tt>
 * by its name.
 * 
 * @author Bill Lin
 */
public class HibernateUtil {

    public final Logger log = LoggerFactory.getLogger(HibernateUtil.class);

    private final InstallationProperties properties;

    private EntityManagerFactory emf;

    private static Boolean isOracleDB = false;

    //TODO test this again
    public static Boolean isOracleDB() {
        return isOracleDB;
    }

    public HibernateUtil() {
        super();
        //setLoggerProperties();
        this.properties = ModelImpl.getInstallationProperties();
        this.emf = rebuildSessionFactory();
    }

    @Deprecated
    // hibernate specific
    public static Session getSession(EntityManager entityManager) {
        HibernateEntityManager hem = entityManager.unwrap(HibernateEntityManager.class);
        return hem.getSession();
    }

    public EntityManager getEntityManager() {
        return this.emf.createEntityManager();
    }

    /**
     * Rebuild the SessionFactory with the static Configuration.
     * <p>
     * This method also closes the old SessionFactory before, if still open. Note that this method should only
     * be used with static SessionFactory management, not with JNDI or any other external registry.
     */
    private EntityManagerFactory rebuildSessionFactory() {
        if (this.properties.getRequiredProperty("db.url").toLowerCase().contains("oracle")) {
            isOracleDB = true;
        } else {
            isOracleDB = false;
        }
        if (properties.isMock()) {
            return null;
        }
        // checks to make usable error messages
        assert null != properties.getRequiredProperty("db.url") : "Property db.url not found";
        assert null != properties.getRequiredProperty("db.username") : "Property db.username not found";
        assert null != properties.getRequiredProperty("db.password") : "Property db.password not found";
        try {
            Class.forName("org.hibernate.ejb.HibernatePersistence");
            InputStream config = HibernateUtil.class.getResourceAsStream("/META-INF/persistence.xml");
            assert null != config : "Cannot find META-INF/persistence.xml";
            config.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        log.debug("Using current Configuration to build hibernate session factory");
        Map overrideProperties = new HashMap();
        overrideProperties.put("hibernate.connection.url", properties.getRequiredProperty("db.url"));
        overrideProperties.put("hibernate.autoCommit", "false");
        overrideProperties
            .put("hibernate.connection.username", properties.getRequiredProperty("db.username"));
        overrideProperties
            .put("hibernate.connection.password", properties.getRequiredProperty("db.password"));
        if (isOracleDB()) {
            overrideProperties.put("hibernate.dialect", "org.hibernate.dialect.Oracle9Dialect");
            overrideProperties.put("hibernate.connection.driver_class", "oracle.jdbc.driver.OracleDriver");
        } else { //postgres
            overrideProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            overrideProperties.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        }

        return Persistence.createEntityManagerFactory("pims", overrideProperties);

    }

    /* print the detail statistics of current sessionFactory
    @Deprecated
    // not used
    public void showStatisticDetails() {
        Statistics stats = getSessionFactory().getStatistics();
        System.out.println("\n---General---");
        System.out.println("Start Time: " + stats.getStartTime());
        System.out.println("ConnectCount: " + stats.getConnectCount());
        System.out.println("TransactionCount: " + stats.getTransactionCount() + "; "
            + stats.getSuccessfulTransactionCount() + " successed");
        System.out.println("PrepareStatementCount: " + stats.getPrepareStatementCount());
        System.out.println("CloseStatementCount: " + stats.getCloseStatementCount());
        System.out.println("FlushCount: " + stats.getFlushCount());
        System.out.println("OptimisticFailureCount: " + stats.getOptimisticFailureCount());
        System.out.println("SessionOpenCount: " + stats.getSessionOpenCount() + ";" + "SessionCloseCount:"
            + stats.getSessionCloseCount());

        System.out.println("\n---Collection---");
        System.out.println("CollectionFetchCount: " + stats.getCollectionFetchCount());
        System.out.println("CollectionLoadCount: " + stats.getCollectionLoadCount());
        System.out.println("CollectionRecreateCount: " + stats.getCollectionRecreateCount());
        System.out.println("CollectionRemoveCount: " + stats.getCollectionRemoveCount());
        System.out.println("CollectionUpdateCount: " + stats.getCollectionUpdateCount());

        System.out.println("\n---Entity---");
        System.out.println("EntityDeleteCount: " + stats.getEntityDeleteCount());
        System.out.println("EntityFetchCount: " + stats.getEntityFetchCount());
        System.out.println("EntityInsertCount: " + stats.getEntityInsertCount());
        System.out.println("EntityLoadCount: " + stats.getEntityLoadCount());
        System.out.println("EntityUpdateCount: " + stats.getEntityUpdateCount());
        System.out.println("EntityNames: " + stats.getEntityNames());

        System.out.println("\n---QueryCache---");
        System.out.println("QueryCacheHitCount: " + stats.getQueryCacheHitCount());
        System.out.println("QueryCacheMissCount: " + stats.getQueryCacheMissCount());
        System.out.println("QueryCachePutCount: " + stats.getQueryCachePutCount());

        System.out.println("\n---Query---");
        System.out.println("QueryExecutionCount: " + stats.getQueryExecutionCount());
        System.out.println("QueryExecutionMaxTime: " + stats.getQueryExecutionMaxTime());
        System.out
            .println("QueryExecutionMaxTimeQueryString: " + stats.getQueryExecutionMaxTimeQueryString());

        System.out.println("\n---SecondLevelCache---");
        System.out.println("SecondLevelCacheHitCount: " + stats.getSecondLevelCacheHitCount());
        System.out.println("SecondLevelCacheMissCount: " + stats.getSecondLevelCacheMissCount());
        System.out.println("SecondLevelCachePutCount(): " + stats.getSecondLevelCachePutCount());

        System.out.println("\n==================More Details===================");
        Collection<String> SecondLevelCacheRegionNames =
            Arrays.asList(stats.getSecondLevelCacheRegionNames());
        long totalSizeInMemory = 0;
        System.out.println("\n------------------SecondLevelCache Details------------");
        for (String SecondLevelCacheRegionName : SecondLevelCacheRegionNames) {
            org.hibernate.stat.SecondLevelCacheStatistics scStats =
                stats.getSecondLevelCacheStatistics(SecondLevelCacheRegionName);
            if (scStats.getSizeInMemory() > 0) {
                System.out.println("\n-" + SecondLevelCacheRegionName + "-");
                System.out.println("ElementCountInMemory: " + scStats.getElementCountInMemory());
                System.out.println("ElementCountOnDisk(: " + scStats.getElementCountOnDisk());
                System.out.println("HitCount: " + scStats.getHitCount());
                System.out.println("MissCount: " + scStats.getMissCount());
                System.out.println("PutCount: " + scStats.getPutCount());
                totalSizeInMemory = +scStats.getSizeInMemory();
                System.out.println("SizeInMemory: " + scStats.getSizeInMemory());
                // JMD No longer present in hibernate 3.6.8
                //System.out.println("CategoryName: " + scStats.getCategoryName());
            }
        }
        System.out.println("\n SecondLevelCache total Size In Memory: " + totalSizeInMemory);

        Collection<String> QueryNames = Arrays.asList(stats.getQueries());
        System.out.println("\n---------------------Query Details-------------------");
        for (String QueryName : QueryNames) {
            org.hibernate.stat.QueryStatistics queryStats = stats.getQueryStatistics(QueryName);
            if (queryStats.getExecutionCount() > 0) {
                System.out.println("\n-" + QueryName + "-");
                System.out.println("ExecutionCount: " + queryStats.getExecutionCount());
                System.out.println("ExecutionRowCount: " + queryStats.getExecutionRowCount());
                System.out.println("ExecutionAvgTime: " + queryStats.getExecutionAvgTime());
                System.out.println("ExecutionMaxTime: " + queryStats.getExecutionMaxTime());
                System.out.println("ExecutionMinTime: " + queryStats.getExecutionMinTime());
                System.out.println("CacheHitCount: " + queryStats.getCacheHitCount());
                System.out.println("CacheMissCount: " + queryStats.getCacheMissCount());
                System.out.println("CachePutCount: " + queryStats.getCachePutCount());
                // JMD No longer present in hibernate 3.6.8
                //System.out.println("CategoryName: " + queryStats.getCategoryName());
            }
        }

        Collection<String> entityNames = Arrays.asList(stats.getEntityNames());
        System.out.println("\n----------------------Entity Details---------------------");
        for (String entityName : entityNames) {
            org.hibernate.stat.EntityStatistics entityStats = stats.getEntityStatistics(entityName);
            if (entityStats.getLoadCount() + entityStats.getDeleteCount() + entityStats.getFetchCount()
                + entityStats.getInsertCount() + entityStats.getUpdateCount() > 0) {
                System.out.println("\n-" + entityName + "-");
                System.out.println("LoadCount: " + entityStats.getLoadCount());
                System.out.println("DeleteCount: " + entityStats.getDeleteCount());
                System.out.println("FetchCount: " + entityStats.getFetchCount());
                System.out.println("InsertCount: " + entityStats.getInsertCount());
                System.out.println("UpdateCount: " + entityStats.getUpdateCount());
            }
        }
    } */

    @Override
    public void finalize() throws Throwable {
        Statistics stats = getSessionFactory().getStatistics();
        stats.logSummary();

        log.debug("Shutting down Hibernate.");
        // Close caches and connection pools
        getSessionFactory().close();
        super.finalize();
    }

    /**
     * areAllSessionsClosed This is hibernate specific, I don't know any JPA way to do this
     * 
     * @return
     */
    public boolean areAllSessionsClosed() {
        Statistics stat = ((HibernateEntityManagerFactory) this.emf).getSessionFactory().getStatistics();
        if (stat.getSessionCloseCount() - stat.getSessionOpenCount() < 0)
            System.err.println((stat.getSessionOpenCount() - stat.getSessionCloseCount())
                + " session is not closed ! (You may miss version.abort() for un-handled exception.) ");
        try {
            return stat.getSessionCloseCount() == stat.getSessionOpenCount();
        } finally {
            stat.clear();
        }
    }

    /**
     * @return Returns the sessionFactory.
     */
    @Deprecated
    // only for testing
    public SessionFactory getSessionFactory() {
        return ((HibernateEntityManagerFactory) this.emf).getSessionFactory();

    }

}
