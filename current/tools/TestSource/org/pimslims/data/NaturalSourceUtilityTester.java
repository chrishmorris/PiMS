package org.pimslims.data;

import java.io.IOException;
import java.util.Collection;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.reference.Organism;

public class NaturalSourceUtilityTester extends TestCase {
    AbstractModel model = ModelImpl.getModel();

    public void testloadMergeFile() throws AccessException, ConstraintException, IOException {

        final String filename = "./data/MergedNaturalSources.csv";
        java.io.File file = new java.io.File(filename);
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            if (wv.findFirst(Organism.class, Organism.PROP_NAME, "E.coli") == null) {
                new Organism(wv, "E.coli");
            }
            NaturalSourceUtility.loadMergeFile(wv, file);
            // load some data for testing
            final Organism ns = wv.findFirst(Organism.class, Organism.PROP_NAME, "E.coli");
            Assert.assertTrue(ns == null);
            final Collection<Organism> nss =
                wv.findAll(Organism.class, Organism.PROP_NAME, "Escherichia coli");
            Assert.assertEquals(1, nss.size());
        } catch (final java.io.FileNotFoundException ex) {
            System.out.println("Unable to find file: " + file.getAbsolutePath());
            ex.printStackTrace();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        System.out.println("NaturalSourceUtilityTester has finished");
    }

}
