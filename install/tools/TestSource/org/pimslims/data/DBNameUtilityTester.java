package org.pimslims.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.reference.Database;
import org.pimslims.model.reference.DbName;
import org.pimslims.presentation.TargetBeanReader;

public class DBNameUtilityTester extends TestCase {
    AbstractModel model = ModelImpl.getModel();

    public void testLoadMergeFile() throws IOException, AccessException, ConstraintException {

        final String filename = "./data/MPSI/MergedDbNames.csv";
        java.io.File file = new java.io.File(filename);
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            if (wv.findFirst(Database.class, Database.PROP_NAME, TargetBeanReader.HYPERLINK_DBNAME) == null) {
                new Database(wv, TargetBeanReader.HYPERLINK_DBNAME);
            }
            DbNameUtility.loadMergeFile(wv, file);
            // load some data for testing
            final Map<String, Object> attributes = new HashMap<String, Object>();
            attributes.put(Database.PROP_NAME, TargetBeanReader.HYPERLINK_DBNAME);
            attributes.put(Database.PROP_FULLNAME, TargetBeanReader.HYPERLINK_DBNAME);
            attributes.put(LabBookEntry.PROP_DETAILS, "Unspecified database");
            final DbName dbName1 = wv.findFirst(Database.class, attributes);
            Assert.assertTrue(dbName1 != null);

            System.out.println("Loaded details from file: " + filename);
        } catch (final java.io.FileNotFoundException ex) {
            System.err.println("Unable to find file: " + file.getAbsolutePath());
            throw ex;
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

}
