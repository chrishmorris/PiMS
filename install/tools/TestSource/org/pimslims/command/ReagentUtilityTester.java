package org.pimslims.command;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.data.ReagentUtility;
import org.pimslims.exception.ModelException;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.RefSample;

public class ReagentUtilityTester extends TestCase {
    AbstractModel model = ModelImpl.getModel();

    public void testLoad() {

        final String filename = "./data/Reagents.csv";
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            ReagentUtility.loadFile(wv, filename);
            final RefSample refSample =
                wv.findFirst(RefSample.class, AbstractSample.PROP_NAME, "LIC qualfied T4 Polymerase");
            Assert.assertNotNull(refSample);
            Assert.assertEquals("Enzyme -polymerase", refSample.getSampleCategories().iterator().next()
                .getName());

            wv.commit();
            System.out.println("Loaded details from file: " + filename);
        } catch (final java.io.IOException ex) {
            System.out.println("Unable to read from file: " + filename);
            ex.printStackTrace();
            Assert.fail();
        } catch (final ModelException ex) {
            System.out.println("Unable to add details from file: " + filename);
            ex.printStackTrace();
            Assert.fail();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        System.out.println("Reagents utility has finished");
    }

}
