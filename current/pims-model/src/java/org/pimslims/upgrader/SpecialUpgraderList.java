package org.pimslims.upgrader;

import java.sql.SQLException;
import java.util.Iterator;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaClassImpl;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.util.MessageHandler;

/**
 * Contains jobs to upgrade a customer database for a new data model. Usually these are small, and can be put
 * here. Large ones should be in a separate class.
 * 
 */
public class SpecialUpgraderList {

    /**
     * Interface for custom code for complicated version updates.
     * 
     * Note that most version updates can be accomplished automatically by comparing the metadata with the
     * database. See the methods below.
     * 
     */
    /**
     * 
     * 
     */
    static public abstract class SpecialUpgrader {

        protected final ModelUpdateVersionImpl muv;

        /**
         * @param muv the current transaction
         */
        protected SpecialUpgrader(final ModelUpdateVersionImpl muv) {
            this.muv = muv;
        }

        /**
         * @return the version number this upgrades from
         */
        public abstract int getVersion();

        /**
         * upgrade action
         * 
         * @throws ApiException
         * @throws AccessException
         * @throws AbortedException
         */
        protected abstract void upgrade() throws SQLException, ConstraintException, AccessException,
            AbortedException;

        /**
         * upgrade action
         * 
         * @throws ApiException
         * @throws AccessException
         * @throws AbortedException
         */
        public void execute() throws SQLException, ConstraintException, AccessException, AbortedException {
            DebugMessageHandler.LOG(MessageHandler.Level.INFO, "(Start-- Special Upgrade Version: "
                + getVersion());
            upgrade();
            DebugMessageHandler.LOG(MessageHandler.Level.INFO, "Completed-- Special Upgrade Version: "
                + getVersion());
        }

    }

    protected static final SpecialUpgrader[] getUpgraders(final ModelUpdateVersion modelUpdateVersion) {

        final ModelUpdateVersionImpl muv = (ModelUpdateVersionImpl) modelUpdateVersion;
        return new SpecialUpgrader[] {
            // put all special upgraders here, in order of version
            new SpecialUpgraderList.updateTableName_v_test(muv), // a
            // test
            new Update_v30(muv), new Update_v31(muv), new Update_v32_33(muv), new Update_v34(muv),
            new Update_v35(muv), new Update_v36(muv), new Update_v37(muv), new Update_v38(muv),
            new Update_v39(muv), new Update_v40(muv), new Update_v41(muv), new Update_v42(muv)
        //TODO , new Update_v43(muv)
        ///PLEASE ALSO UPDATE THE RN NUMBER in install-revision.sql, messages.peroperties and RevisionNumber.java
        };
    }

    // test patch to change table name
    public static class updateTableName_v_test extends SpecialUpgrader {

        public updateTableName_v_test(final ModelUpdateVersionImpl muv) {
            super(muv);
        }

        @Override
        public int getVersion() {
            return -1;
        }

        @Override
        protected void upgrade() throws SQLException {
            muv.renameTable("test_a", "test_b");
            muv.renameConstraint("test_b", "test_a_pkey", "test_b_pkey", "PRIMARY KEY(id)");
            muv.renameColumn("test_b", "value", "changedvalue");
        }

    }

    protected static void printAllRole(final MetaClass metaClass) {
        final java.util.Map metaRoles = ((MetaClassImpl) metaClass).getDeclaredMetaRoles();
        for (final Iterator iter = metaRoles.values().iterator(); iter.hasNext();) {
            final MetaRole role = (MetaRole) iter.next();
            System.out.println(role.getName());
        }

    }

    /**
     * 
     */
    private SpecialUpgraderList() {
        // this class has only static members
    }

    // init special upgraders
    // private static SpecialUpgraderList specialUpgraderList=new
    // SpecialUpgraderList();
    /*
     * private static final SpecialUpgrader[] upgraders = new SpecialUpgrader[] { // put any special upgraders
     * here, in order of version specialUpgraderList.new updateTableName_v_test(), //a test
     * specialUpgraderList.new update_v9(), specialUpgraderList.new update_v10(), specialUpgraderList.new
     * update_v12(), specialUpgraderList.new Update_v17(), };
     */

}
