package org.pimslims.command;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.data.ChemicalUtility;
import org.pimslims.exception.ModelException;

public class ChemicalUtilityTester extends TestCase {
    AbstractModel model = ModelImpl.getModel();

    //TODO this should actually test
    public void testLoad() {

        String filename = "./data/Chemicals.csv";
        WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            ChemicalUtility.loadFile(wv, filename);
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

        System.out.println("Chemical utility has finished");
    }

}
