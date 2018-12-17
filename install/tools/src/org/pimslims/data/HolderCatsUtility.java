/*
 * Created on 18-Dec-2006 16:34:56 @author Anne Pajon, pajon@ebi.ac.uk PIMS project, www.pims-lims.org
 * Copyright (C) 2006 EMBL - European Bioinformatics Institute - MSD group Wellcome Trust Genome Campus,
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
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.reference.HolderCategory;

/**
 * 
 * Command line utility for adding reference data from CSV files for HolderCategory Data is:
 * namingSystem,name,details
 * 
 */
public class HolderCatsUtility extends AbstractLoader {

    /**
     * @param CSV filename with a list of holder categories
     * @throws java.io.IOException
     */
    public static void loadFile(final WritableVersion wv, final String filename) throws java.io.IOException,
        AccessException, ConstraintException {

        final java.io.Reader reader = new java.io.FileReader(filename);
        final CsvParser parser = new CsvParser(reader);

        while (parser.getLine() != null) {
            final String name = parser.getValueByLabel("name");

            if (null == name || "".equals(name.trim())) { // spacer line
                continue;
            }

            final Map<String, Object> attrMap = new java.util.HashMap<String, Object>();
            attrMap.put(HolderCategory.PROP_NAME, parser.getValueByLabel("name"));

            final java.util.Collection holderCatList = wv.findAll(HolderCategory.class, attrMap);
            if (0 == holderCatList.size()) {
                attrMap.put(LabBookEntry.PROP_DETAILS, parser.getValueByLabel("details"));
                wv.create(HolderCategory.class, attrMap);
                // print("Adding HolderCategory: [" +
                // attrMap.get("namingSystem") + ", " + attrMap.get("name") +
                // "]");
            } else if (1 == holderCatList.size()) {
                AbstractLoader.debug("HolderCategory already exists: [" + attrMap.get("name") + "]");
            }

        }
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        if (args.length == 0) {
            AbstractLoader.print("Usage: HolderCatsUtility filename.csv");
        }

        final AbstractModel model = ModelImpl.getModel();
        //AbstractLoader.silent = false;
        for (int i = 0; i < args.length; i++) {
            final String filename = args[i];
            final WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
            try {
                HolderCatsUtility.loadFile(wv, filename);
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
        AbstractLoader.print("Holder category utility has finished");
    }

}
