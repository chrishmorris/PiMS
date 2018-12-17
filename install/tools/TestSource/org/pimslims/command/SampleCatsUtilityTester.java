package org.pimslims.command;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.data.SampleCatsUtility;
import org.pimslims.exception.ModelException;
import org.pimslims.model.reference.SampleCategory;

public class SampleCatsUtilityTester extends TestCase {
    AbstractModel model = ModelImpl.getModel();

    public void testLoad() {

        final String filename = "./data/SampleCats.csv";
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            SampleCatsUtility.loadFile(wv, filename);
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
            final SampleCategory et = rv.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Plasmid");
            if (et != null) {
                Assert.assertTrue("A recombinant or insert-containing vector".equalsIgnoreCase(et
                    .getDetails()));
            } else {
                Assert.fail("Can not find Sample Category: 'Plasmid'");
            }
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
        System.out.println("SampleCatsUtility has finished");
    }

}
