/**
 * 
 */
package org.pimslims.upgrader;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.exception.AbortedException;
import org.pimslims.metamodel.MetaUtils;
import org.pimslims.util.InstallationProperties;
import org.pimslims.util.MessageHandler;

/**
 * @author bl67
 * 
 */
public class TestNormalUpgrader extends TestCase {
    static final File propertyFile = null;

    AbstractModel model = null;

    java.sql.Connection connection;

    DatabaseUpdater dbUpdater;

    public TestNormalUpgrader(final String method) {
        super(method);
    }

/*
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        // suite.addTestSuite(TestUpgrader.class);
        suite.addTest(new TestNormalUpgrader("testAutoUpgrader"));
        return suite;
    } */

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
        dbUpdater = new DatabaseUpdater(connection);
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

    public void testUpgradeClass() throws Exception {
        // note that this must be a class that contains no reference data
        final String table = "expe_software";

        if (dbUpdater.isTableExist(table)) {
            dbUpdater.dropTable(table);
        }
        connection.commit();

        /*
         * try to get the column of target back by using upgrader
         */
        doNormalUpgrade();

        if (!dbUpdater.isTableExist(table)) {
            /*
             * TODO when upgrader is failed, recreate this table by using sql
             */

            fail("Upgrader failed to create " + table);
        }
    }

    /**
     * TestNormalUpgrader.doNormalUpgrade
     * 
     * @throws AbortedException
     */
    private void doNormalUpgrade() throws AbortedException {
        ModelUpdateVersion muv = this.model.getModelUpdateVersion();
        try {
            Upgrader.doNormalUpgrade(muv);
            muv.commit();
        } catch (final Exception e) {
            muv.abort();
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    public void donttestColumnOfRole() throws Exception {
        final String table = "targ_target";
        final String column = "proteinid";
        /*
         * before test upgrader, drop a column of target first
         */
        assertTrue(dbUpdater.isColumnExist(table, column));
        dbUpdater.dropColumn(table, column);
        connection.commit();

        doNormalUpgrade();

        if (!dbUpdater.isColumnExist(table, column)) {
            /*
             * when upgrader is failed, recreate this column for target
             */
            dbUpdater.createColumn(table, column, MetaUtils.getDBType("BIGINT"));
            dbUpdater.setColumnNotNull(table, column);
            dbUpdater.setForeignKey(table, "targ_target_protein_fk", "mole_molecule", "labbookentryid",
                column, null);
            connection.commit();
            fail("Upgrader failed to create" + column + " for " + table);

        }
    }

    public void xtestUpgradeMultiColumn() throws Exception {
        final String table = "acco_usergroup2user";
        /*
         * before test upgrader, drop a column of target first
         */
        // in case this column is droped and can not be re-created:
        // dbUpdater.createColumn("targ_target","localname",null);
        assertTrue(dbUpdater.isTableExist(table));
        dbUpdater.dropTable(table);
        connection.commit();
        try {
            doNormalUpgrade();

            assertTrue(dbUpdater.isTableExist(table));
        } finally {
            if (!dbUpdater.isTableExist(table)) {
                /*
                 * when upgrader is failed, recreate this table for mole_molecule_commna
                 */
                dbUpdater.createTable(table);
                dbUpdater.createColumn(table, "memberuserid", MetaUtils.getDBType("BIGINT"));
                dbUpdater.setColumnNotNull(table, "memberuserid");
                dbUpdater.setForeignKeyOfMultiAttributeTable(table, "acco_user", "memberuserid");
                dbUpdater.createColumn(table, "usergroupid", MetaUtils.getDBType("BIGINT"));
                dbUpdater.setColumnNotNull(table, "usergroupid");
                dbUpdater.setForeignKeyOfMultiAttributeTable(table, "acco_usergroup", "usergroupid");
                dbUpdater.setPrimaryKeysMtoM(table, "memberuserid", "usergroupid");

                connection.commit();
                connection.close();
                //fail("Upgrader failed to create " + table);
            }
        }
    }

    public void donttestUpgradeSimpleColumn() throws Exception {
        final String table = "targ_target";
        final String column = "celllocation";
        /*
         * before test upgrader, drop a column of target first
         */
        // in case this column is droped and can not be re-created:
        if (dbUpdater.isColumnExist(table, column)) {
            dbUpdater.dropColumn(table, column);
        }
        connection.commit();

        doNormalUpgrade();

        if (!dbUpdater.isColumnExist(table, column)) {
            /*
             * when upgrader is failed, recreate this column for target
             */
            dbUpdater.createColumn(table, column, null);
            connection.commit();
            fail("Upgrader failed to create" + column + " for " + table);
        }
    }

    public void TODOtestNotNullSimpleColumn() throws Exception {
        final String table = "targ_target";
        final String column = "whychosen";
        /*
         * before test upgrader, drop a column of target first
         */
        // in case this column is droped and can not be re-created:
        // dbUpdater.createColumn(table,column,null);
        assertTrue(dbUpdater.isColumnExist(table, column));
        dbUpdater.dropColumn(table, column);
        connection.commit();

        doNormalUpgrade();

        if (!dbUpdater.isColumnExist(table, column)) {
            /*
             * when upgrader is failed, recreate this column for target
             */
            dbUpdater.createColumn(table, column, null);
            dbUpdater.setColumnNotNull(table, column);
            connection.commit();
            fail("Upgrader failed to create" + column + " for " + table);

        }
    }

    public void TODOtestSpecialUpdate() throws Exception {
        final String oldtable = "test_a";
        final String newtable = "test_b";
        final String column1 = "id";
        final String column2 = "value";
        final String newColumn2 = "changedvalue";

        /*
         * before test upgrader, create/drop column/table
         */
        // in case this table was droped
        if (!dbUpdater.isTableExist(oldtable)) {
            dbUpdater.createTable(oldtable);
        }
        // in case this column was droped
        if (!dbUpdater.isColumnExist(oldtable, column1)) {
            dbUpdater.createColumn(oldtable, column1, "int8");
            dbUpdater.setPrimaryKey(oldtable, column1);
        }
        if (!dbUpdater.isColumnExist(oldtable, column2)) {
            dbUpdater.createColumn(oldtable, column2, "int8");
        }
        // in case new table was not droped
        if (dbUpdater.isTableExist(newtable)) {
            dbUpdater.dropTable(newtable);
        }
        connection.commit();

        /*
         * do the sepcial upgrader which version is -1
         */
        try {
            Upgrader.upgrade(model, -1, 0);
            if (!dbUpdater.isTableExist(newtable) || !dbUpdater.isColumnExist(newtable, newColumn2)) {
                fail("failed to do the special upgrade!");
            }
        } catch (final Exception e) {
            System.out.print(e);
            e.printStackTrace();
            fail("failed to do the special upgrade!");
        } finally {
            // drop created tables
            if (dbUpdater.isTableExist(oldtable)) {
                dbUpdater.dropTable(oldtable);
            }
            if (dbUpdater.isTableExist(newtable)) {
                dbUpdater.dropTable(newtable);
            }
            connection.commit();
        }

    }

    public void xtestSpecialUpdate159() throws Exception {
        final String table = "mole_molecule";
        final String column = "protTarget";

        System.err
            .println("testSpecialUpdate159 could not rollback automatically, if is successfully finished!");
        System.err.println("It will need manually re-install the old DB with data!");
        System.err.println("However, it will be rollback if any error is found!");
        Thread.sleep(2000);

        try {
            Upgrader.upgrade(model, 0, 10);
            // Upgrader.upgrade(model, 0, 0);
            if (dbUpdater.isColumnExist(table, column)) {
                fail("failed to do the special upgrade 159!");
            }
        } catch (final Exception e) {
            System.out.print(e);
            e.printStackTrace();
            fail("failed to do the special upgrade 159!");
        }

    }

    public void xtestSpecialUpdate23() throws Exception {
        final String table = "mole_molecule_keywords";

        System.err
            .println("testSpecialUpdate23 could not rollback automatically, if is successfully finished!");
        System.err.println("It will need manually re-install the old DB with data!");
        System.err.println("However, it will be rollback if any error is found!");
        Thread.sleep(2000);

        try {

            Upgrader.upgrade(model, 22, 23);
            // Upgrader.upgrade(model, 0, 0);
            if (!dbUpdater.isTableEmpty(table)) {
                fail("failed to do the special upgrade 23!");
            }
        } catch (final Exception e) {
            System.out.print(e);
            e.printStackTrace();
            fail("failed to do the special upgrade 23!");
        }

    }

    public void xtestSpecialUpdate26() throws Exception {
        final Map<String, String> newConstraints = new HashMap<String, String>();
        // the rule to generate constraint seems changed, however the db is ok
        // to use.
        // newConstraints.put(
        // "ccp_experiment_experiment__projectid_name__must_be_unique","expe_experiment");
        // newConstraints.put(
        // "ccp_people_organisation__projectid_name__must_be_unique","peop_organisation");
        // newConstraints.put(
        // "ccp_protocol_protocol__projectid_name__must_be_unique","prot_protocol");
        newConstraints.put("cryz_image_creator_fk", "cryz_image");
        newConstraints.put("cryz_image_instrument_fk", "cryz_image");
        newConstraints.put("cryz_image_sample_fk", "cryz_image");

        System.err
            .println("testSpecialUpdate23 could not rollback automatically, if is successfully finished!");
        System.err.println("It will need manually re-install the old DB with data!");
        System.err.println("However, it will be rollback if any error is found!");
        Thread.sleep(2000);

        try {

            Upgrader.upgrade(model, 24, 26);
            // Upgrader.upgrade(model, 0, 0);
            Boolean success = true;
            for (final String constraintName : newConstraints.keySet()) {
                if (!dbUpdater.isConstraintExist(newConstraints.get(constraintName), constraintName)) {
                    System.err.println("Can not find " + constraintName);
                    success = false;
                }
            }
            if (!success) {
                fail("failed to do the special upgrade 26!");
            }

        } catch (final Exception e) {
            System.out.print(e);
            e.printStackTrace();
            fail("failed to do the special upgrade 23!");
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
            String className = "org.postgresql.Driver";
            final String url = properties.getRequiredProperty("db.url");
            final String username = properties.getRequiredProperty("db.username");
            final String password = properties.getRequiredProperty("db.password");

            Class.forName(className);
            connection = java.sql.DriverManager.getConnection(url, username, password);
            DebugMessageHandler.LOG(MessageHandler.Level.INFO, "$PIMS has connected to database: " + url);

            connection.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);

        } catch (final SQLException ex) {
            Throwable cause = ex;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            cause.printStackTrace(); // LOG
            throw new RuntimeException(cause);
        } catch (final ClassNotFoundException ex) {
            ex.printStackTrace(); // LOG
            throw new RuntimeException(ex);
        }
        return connection;
    }

    public void xtestIsValidDNA() {

        assertTrue(Update_v24.isValidDNA("AAA"));
        assertTrue(Update_v24.isValidDNA("aaa"));
        assertTrue(Update_v24.isValidDNA("cccaccc"));
        assertTrue(Update_v24.isValidDNA("aaaccc"));
        assertTrue(Update_v24.isValidDNA("ccc a ccc"));
        assertFalse(Update_v24.isValidDNA("bbb"));
        assertFalse(Update_v24.isValidDNA("123"));
        assertFalse(Update_v24.isValidDNA("abbb"));
        assertFalse(Update_v24.isValidDNA(" "));

        assertTrue(Update_v24.isValidProtien("abc"));
        assertFalse(Update_v24.isValidProtien("abj"));
    }

    /**
     * Runs these tests from the command line
     * 
     * @param args ignored
     * @throws FileNotFoundException public static void main(final String[] args) {
     *             System.out.println("loading property file:" + args[0]); propertyFile = new
     *             java.io.File(args[0]); if (propertyFile != null) { TestRunner.run(suite()); }
     * 
     *             }
     */
}
