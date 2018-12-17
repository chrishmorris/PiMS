package org.pimslims.data;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.reference.ExperimentType;

public class ExperimentTypeUtilityTester extends TestCase {
    AbstractModel model = ModelImpl.getModel();

    public void testLoad() {

        final String filename = "./data/ExperimentTypes.csv";
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final java.io.Reader reader = new java.io.FileReader(filename);
            ExperimentTypesUtility.loadFile(wv, reader);
            wv.commit();
            System.out.println("Loaded details from file: " + filename);
        } catch (final java.io.IOException ex) {
            System.out.println("Unable to read from file: " + filename);
            ex.printStackTrace();
        } catch (final ModelException ex) {
            System.out.println("Unable to add details from file: " + filename);
            ex.printStackTrace();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        //
        final ReadableVersion rv = this.model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final ExperimentType et = rv.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, "PCR");
            if (et != null) {
                Assert.assertTrue("Polymerase chain reaction".equalsIgnoreCase(et.getDetails()));
            } else {
                Assert.fail("Can not find ExperimentType: 'PCR'");
            }
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
        System.out.println("ExperimentTypeUtility has finished");
    }

    public void xtestLoadMergeFile() throws SQLException, AccessException, ConstraintException, IOException {

        final String filename = "data/MergedExperimentTypes.csv";
        java.io.File file = new java.io.File(filename);
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            ExperimentTypesUtility.loadMergeFile(wv, file);
            // load some data for testing
            final Map<String, Object> attributes = new HashMap<String, Object>();
            attributes.put(ExperimentType.PROP_NAME, "PCR");

            final ExperimentType code2 = wv.findFirst(ExperimentType.class, attributes);
            Assert.assertTrue(code2 == null);

            final ExperimentType et = wv.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, "PCR");
            if (et != null) {
                Assert.assertTrue("Polymerase chain reaction".equalsIgnoreCase(et.getDetails()));
            } else {
                Assert.fail("Can not find ExperimentType: 'PCR'");
            }
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
