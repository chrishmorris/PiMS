/*
 * Created on 17-Dec-2005-01:32:50 @author Anne Pajon, pajon@ebi.ac.uk PIMS project, www.pims-lims.org
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
import org.pimslims.model.reference.ComponentCategory;

/**
 * 
 * Command line utility for adding reference data from CSV files for ComponentCategory Data is: name,details
 * 
 */
public class ComponentCatsUtility extends AbstractLoader {

    /**
     * @param CSV filename with a list of sample categories
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
            //full attributes
            final Map<String, Object> fullAttrMap = new java.util.HashMap<String, Object>();
            fullAttrMap.put("name", lcsvp.getValueByLabel("name"));
            fullAttrMap.put("details", lcsvp.getValueByLabel("details"));
            //key attribute: name
            final Map<String, Object> keyAttrMap = new java.util.HashMap<String, Object>();
            keyAttrMap.put("name", lcsvp.getValueByLabel("name"));

            AbstractLoader.UpdateOrCreate(wv, ComponentCategory.class, keyAttrMap, null, fullAttrMap);

        }
    }

    public static void main(final String[] args) {

        if (args.length == 0) {
            AbstractLoader.print("Usage: ComponentCatsUtility filename.csv");
        }

        final AbstractModel model = ModelImpl.getModel();
        //AbstractLoader.silent = false;
        for (int i = 0; i < args.length; i++) {
            final String filename = args[i];
            final WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
            try {
                ComponentCatsUtility.loadFile(wv, filename);
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
        AbstractLoader.print("Component category utility has finished");
    }

}
