package org.pimslims.upgrader;

import java.sql.SQLException;
import java.util.Iterator;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader;
import org.pimslims.util.MessageHandler;

/**
 * Upgrades the data model to the latest version
 * 
 * TODO add code that calculates table names and column names from the metadata (class name, role name,
 * attribute name)
 * 
 */
public class Upgrader {

    // init special upgraders
    // private static SpecialUpgraderList specialUpgraderList=new
    // SpecialUpgraderList();
    /*
     * private static final SpecialUpgrader[] upgraders = new SpecialUpgrader[] { // put any special upgraders
     * here, in order of version specialUpgraderList.new updateTableName_v_test(), //a test
     * specialUpgraderList.new update_v9(), specialUpgraderList.new update_v10(), specialUpgraderList.new
     * update_v12(), specialUpgraderList.new Update_v17(), };
     */

    public static void upgradeAuto(AbstractModel model) throws SQLException, AbortedException {
        ModelUpdateVersionImpl muv = (ModelUpdateVersionImpl) model.getModelUpdateVersion();
        int dbrn = muv.getDBRevisionNumber();
        int jarRN = RevisionNumber.getRevision();
        upgrade(model, dbrn, jarRN, muv);
        System.out.println("Upgraded PiMS data model to version: " + RevisionNumber.REVISION);

    }

    public static void upgrade(AbstractModel model, int oldVersion, int newVersion) throws AbortedException,
        SQLException {
        ModelUpdateVersionImpl muv = (ModelUpdateVersionImpl) model.getModelUpdateVersion();
        upgrade(model, oldVersion, newVersion, muv);
    }

    public static void upgrade(AbstractModel model, int oldVersion, int newVersion, ModelUpdateVersionImpl muv)
        throws AbortedException, SQLException {

        if (muv == null) {
            DebugMessageHandler.LOG(MessageHandler.ERROR, "ModelUpdateVersion is null!");
            return;
        }

        try {
            SpecialUpgrader[] upgraders = SpecialUpgraderList.getUpgraders(muv);

            if (upgraders.length > 0) {
                DebugMessageHandler.LOG(MessageHandler.INFO, "Checking Special Upgrade...");
                // first run special upgraders
                // specialUpgraderList.setMuv(muv);
                for (SpecialUpgrader upgrader : upgraders) {
                    int version = upgrader.getVersion();
                    if (version <= oldVersion) {
                        continue;
                    }
                    if (version > newVersion) {
                        continue;
                    }
                    DebugMessageHandler.LOG(MessageHandler.INFO, "Running special upgrader for version: "
                        + version);
                    upgrader.execute();
                }
            }

            // update RN
            muv.setDBRevisionNumber();

            boolean success = doNormalUpgrade(muv);

            if (success) {
                CommitMUV(muv, model);
            } else {
                DebugMessageHandler.LOG(MessageHandler.INFO, "Upgrade is aborted due to some errors!");
                muv.abort();
            }

        } catch (ConstraintException e) {
            throw new RuntimeException(e);
        } catch (AccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (!muv.isCompleted()) {
                muv.abort();
            }

        }

    }

    /**
     * @param muv
     * @param model
     * @throws SQLException
     * @throws ConstraintException
     * @throws AbortedException
     */
    private static void CommitMUV(ModelUpdateVersionImpl muv, AbstractModel model) throws SQLException,
        AbortedException, ConstraintException {
        if (SchemaValidator.verifySchema(muv, model)) {
            muv.commit();
            System.out.println("Database Schema is verified!Upgrade is finished successfully!");
            return;
        }
        muv.abort();
        System.out.println("Found problems in Database Schema! Upgrader aborted!");

    }

    public static boolean doNormalUpgrade(ModelUpdateVersion muv) throws ConstraintException, SQLException {
        DebugMessageHandler.LOG(MessageHandler.INFO,
            "Special Upgrade is finished successfully and normal upgrader is started!");
        // upgrade class first (Step 1)
        boolean successStep1 = true, successStep2 = true;
        DebugMessageHandler.LOG(MessageHandler.INFO, "Updating Attributes...");
        AbstractModel model = ((ReadableVersion) muv).getModel();
        java.util.Collection<String> metaClassNames = model.getClassNames();
        for (Iterator iter = metaClassNames.iterator(); iter.hasNext();) {
            String metaClassName = (String) iter.next();

            muv.updateMetaClassAttributes(model.getMetaClass(metaClassName));

        }

        // upgrade roles(Step 2)
        // if(success)
        if (true) {
            DebugMessageHandler.LOG(MessageHandler.INFO, "Updating Associations...");
            for (Iterator iter = metaClassNames.iterator(); iter.hasNext();) {
                String metaClassName = (String) iter.next();

                muv.updateMetaRoleOfClass(model.getMetaClass(metaClassName));

            }
        }
        return successStep1 && successStep2;
    }

    public static void main(String[] args) {
        org.pimslims.dao.ModelImpl.setCheckRevisionNumber(false);
        AbstractModel model = org.pimslims.dao.ModelImpl.getModel();
        ModelUpdateVersionImpl muv = (ModelUpdateVersionImpl) model.getModelUpdateVersion();
        try {

            int dbrn = muv.getDBRevisionNumber();
            int jarRN = RevisionNumber.getRevision();
            upgrade(model, dbrn, jarRN, muv);
            System.out.println("Upgraded PiMS data model to version: " + RevisionNumber.REVISION);
            System.exit(0);
        } catch (AbortedException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

}
