/**
 * 
 */
package org.pimslims.upgrader;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.people.Organisation;
import org.pimslims.util.InstallationProperties;
import org.pimslims.util.MessageHandler;

/**
 * @author bl67
 * 
 */
public class TestUpgrader extends TestCase {

    private static final String UNIQUE = "up" + System.currentTimeMillis();

    static File propertyFile = null;

    AbstractModel model = null;

    java.sql.Connection connection;

    DatabaseUpdater dbUpdater;

    public TestUpgrader(final String method) {
        super(method);
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        // suite.addTestSuite(TestUpgrader.class);
        suite.addTest(new TestUpgrader("testAutoUpgrader"));
        return suite;
    }

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        org.pimslims.dao.ModelImpl.setCheckRevisionNumber(false);
        if (propertyFile == null) {
            model = org.pimslims.dao.ModelImpl.getModel();
        } else {
            model = org.pimslims.dao.ModelImpl.getModel(propertyFile);
        }
        connection = getConnection();
        dbUpdater = DatabaseUpdater.getUpdater(connection);
    }

    /*
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        connection.close();
        org.pimslims.dao.ModelImpl.setCheckRevisionNumber(true);
    }

    public void testAutoUpgrader() throws SQLException, ConstraintException, AbortedException {

        Upgrader.upgradeAuto(model);
        assertTrue(dbUpdater.isTableExist("revisionnumber"));

        // now test it
        WritableVersion wv = this.model.getTestVersion();
        try {
            LabBookEntry page = new Organisation(wv, UNIQUE + "name");
            page.setPageNumber(UNIQUE);
            wv.flush();
            LabBookEntry found = wv.get(page.get_Hook());
            assertEquals(UNIQUE, found.getPageNumber());

        } finally {
            wv.abort();
        }

        assert dbUpdater.isTableExist("revisionnumber");
        assert RevisionNumber.REVISION == dbUpdater.getDBRevisionNumber();
        assert RevisionNumber.REVISION == ModelUpdateVersionImpl.getJavaRevisionNumber();
    }

    public void testNormalUpgrader() {
        final org.pimslims.upgrader.ModelUpdateVersionImpl muv =
            (org.pimslims.upgrader.ModelUpdateVersionImpl) model.getModelUpdateVersion();
        try {
            Upgrader.doNormalUpgrade(muv);
            System.out.println("Upgrade completed");

            muv.commit();
            System.out.println("Upgraded");
        } catch (final ConstraintException e) {
            throw new RuntimeException(e);
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } catch (final AbortedException e) {
            throw new RuntimeException(e);
        } finally {
            if (!muv.isCompleted()) {
                muv.abort();
            }
        }
    }

    public void xtestMPSISequence() throws SQLException {
        final List<String> MPSISequence = new LinkedList<String>();
        MPSISequence.add("test_target");
        MPSISequence.add("generic_target");

        for (final String sequenceName : MPSISequence) {
            if (!dbUpdater.isSequenceExist(sequenceName)) {
                fail("Upgrader failed to create sequence:" + sequenceName);
            }
            dbUpdater.getNextSeqVal(sequenceName);

        }
    }

    private java.sql.Connection getConnection() throws FileNotFoundException {
        java.sql.Connection connection = null;
        try {
            InstallationProperties properties;
            if (propertyFile == null) {
                properties = new InstallationProperties();
            } else {
                properties = new InstallationProperties(propertyFile);
            }

            final String url = properties.getRequiredProperty("db.url");
            final String username = properties.getRequiredProperty("db.username");
            final String password = properties.getRequiredProperty("db.password");
            String driver = "org.postgresql.Driver";
            if (url.contains("oracle")) {
                driver = "oracle.jdbc.driver.OracleDriver";
            }
            try {

                Class.forName(driver);
            } catch (ClassNotFoundException cnfe) {
                throw new RuntimeException("JDBC driver class not found");
            }

            connection = java.sql.DriverManager.getConnection(url, username, password);
            DebugMessageHandler.LOG(MessageHandler.INFO, "$PIMS has connected to database: " + url);

            connection.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);

        } catch (final SQLException ex) {
            Throwable cause = ex;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            cause.printStackTrace(); // LOG
            throw new RuntimeException(cause);
        }
        return connection;
    }

}
