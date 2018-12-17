/*
 * Created on 17-Dec-2005-00:42:39 @author Anne Pajon, pajon@ebi.ac.uk PIMS project, www.pims-lims.org
 * Copyright (C) 2005 EMBL - European Bioinformatics Institute - MSD group Wellcome Trust Genome Campus,
 * Hinxton, Cambridge CB10 1SD, UK
 */
package org.pimslims.data;

import java.util.Map;

import org.pimslims.csv.CsvParser;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.reference.SampleCategory;

/**
 * 
 * Command line utility for adding reference data from CSV files for SampleCategory Data is: name,details
 * 
 */
public class SampleCatsUtility extends AbstractLoader {

    /**
     * @param filename an CSV file with a list of sample categories
     * @throws java.io.IOException
     */
    public static void loadFile(final WritableVersion wv, final String filename) throws java.io.IOException,
        AccessException, ConstraintException {

        final java.io.Reader reader = new java.io.FileReader(filename);
        final CsvParser lcsvp = new CsvParser(reader);

        while (lcsvp.getLine() != null) {
            final String name = lcsvp.getValueByLabel("name");

            if ("".equals(name.trim())) {
                // spacer line
                continue;
            }
            final Map<String, Object> attrMap = new java.util.HashMap<String, Object>();
            attrMap.put("name", lcsvp.getValueByLabel("name"));

            final java.util.Collection sampleCatList = wv.findAll(SampleCategory.class, attrMap);
            if (0 == sampleCatList.size()) {
                attrMap.put("details", lcsvp.getValueByLabel("details"));
                wv.create(SampleCategory.class, attrMap);
                AbstractLoader.print("Adding SampleCategory: [" + attrMap.get("name") + "]");
            } else if (1 == sampleCatList.size()) {
                final SampleCategory sc = (SampleCategory) sampleCatList.iterator().next();
                if ((sc.getDetails() == null && lcsvp.getValueByLabel("details") != null)
                    || (sc.getDetails() != null && !sc.getDetails().equalsIgnoreCase(
                        lcsvp.getValueByLabel("details")))) {
                    sc.setDetails(lcsvp.getValueByLabel("details"));
                    AbstractLoader.print("-SampleCategory already exists: [" + attrMap.get("name")
                        + "]'s details is UPDATED!");
                } else {
                    AbstractLoader.debug("=SampleCategory already exists: [" + attrMap.get("name") + "]");
                }
            }
        }
    }

    public static void main(final String[] args) {

        if (args.length == 0) {
            AbstractLoader.print("Usage: SampleCatsUtility filename.csv");
        }

        final AbstractModel model = ModelImpl.getModel();
        //AbstractLoader.silent = false;
        final WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);

        for (int i = 0; i < args.length; i++) {
            final String filename = args[i];
            try {
                SampleCatsUtility.loadFile(wv, filename);
                wv.commit();
                AbstractLoader.print("Loaded details from file: " + filename);
            } catch (final java.io.IOException ex) {
                AbstractLoader.print("Unable to read from file: " + filename);
                ex.printStackTrace();
            } catch (final ModelException ex) {
                AbstractLoader.print("Unable to add details from file: " + filename);
                ex.printStackTrace();
            } finally {
                if (!wv.isCompleted()) {
                    wv.abort();
                }
            }
        }
        AbstractLoader.print("Sample category utility has finished");
    }

}
