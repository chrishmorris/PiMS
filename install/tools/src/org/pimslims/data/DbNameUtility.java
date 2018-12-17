/*
 * Created on 09-Feb-2006 14:27:12 @author Anne Pajon, pajon@ebi.ac.uk PIMS project, www.pims-lims.org
 * Copyright (C) 2006 EMBL - European Bioinformatics Institute - MSD group Wellcome Trust Genome Campus,
 * Hinxton, Cambridge CB10 1SD, UK
 */
package org.pimslims.data;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;

import org.pimslims.csv.CsvParser;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.reference.Database;

/**
 * 
 * Command line utility for adding database name data from CSV files for DbRef.DbName
 * 
 */
public class DbNameUtility extends AbstractLoader {

    /**
     * @param CSV filename with a list of target status
     * @throws java.io.IOException
     */
    static void loadFile(final WritableVersion wv, final String filename) throws java.io.IOException,
        AccessException, ConstraintException {

        final java.io.Reader reader = new java.io.FileReader(filename);
        final CsvParser lcsvp = new CsvParser(reader);
        lcsvp.getLabels();
        while (lcsvp.getLine() != null) {
            final String name = lcsvp.getValueByLabel("name");

            if ("".equals(name.trim())) {
                // spacer line
                continue;
            }

            final Map<String, Object> attrMap = new java.util.HashMap<String, Object>();
            attrMap.put("name", lcsvp.getValueByLabel("name"));

            final java.util.Collection dbNameList = wv.findAll(Database.class, attrMap);
            if (0 == dbNameList.size()) {
                attrMap.put("fullName", lcsvp.getValueByLabel("fullName"));
                attrMap.put("url", lcsvp.getValueByLabel("url"));
                attrMap.put("details", lcsvp.getValueByLabel("details"));
                wv.create(Database.class, attrMap);
                // print("Adding DbName: [" + attrMap.get("name") +
                // "]");
            } else if (1 == dbNameList.size()) {
                AbstractLoader.debug("DbName already exists: [" + attrMap.get("name") + "]");
            }

        }
    }

    /**
     * @param file TODO
     * @param CSV filename with a list of DBName to merge
     * @throws java.io.IOException
     * @throws SQLException
     */
    static void loadMergeFile(final WritableVersion wv, File file) throws java.io.IOException,
        AccessException, ConstraintException {

        final java.io.Reader reader = new java.io.FileReader(file);
        final CsvParser parser = new CsvParser(reader);

        while (parser.getLine() != null) {
            final String action = parser.getValueByLabel("action");
            final Map<String, Object> oldAttrMap = new java.util.HashMap<String, Object>();
            oldAttrMap.put(Database.PROP_NAME, parser.getValueByLabel("oldName"));
            final Map<String, Object> newAttrMap = new java.util.HashMap<String, Object>();
            newAttrMap.put(Database.PROP_NAME, parser.getValueByLabel("newName"));
            newAttrMap.put(Database.PROP_FULLNAME, parser.getValueByLabel("newFullName"));
            newAttrMap.put(Database.PROP_URL, parser.getValueByLabel("newUrl"));
            newAttrMap.put(LabBookEntry.PROP_DETAILS, parser.getValueByLabel("newDetails"));
            final Map<String, Object> tempAttrMap = new java.util.HashMap<String, Object>();
            tempAttrMap.put(Database.PROP_NAME, "_temp_DbName");
            if ("".equals(action.trim())) {
                // spacer line
                continue;
            } else if ("SAME".equalsIgnoreCase(action.trim())) {
                // same not changes
                continue;
            } else if ("Remove".equalsIgnoreCase(action.trim())) {
                RefDataUtility.removeRecord(wv, Database.class, oldAttrMap);
                wv.flush();
                continue;
            }

            final java.util.Collection<Database> DbNameFound = wv.findAll(Database.class, oldAttrMap);
            if (DbNameFound.size() < 1) {
                AbstractLoader.print("Did not found " + parser.getValueByLabel("oldName"));
                continue;
            }
            final Database tempDbName = RefDataUtility.findOrCreate(wv, Database.class, tempAttrMap);
            wv.flush();
            // get related DBrefs and link them to temp DbName
            for (final Database oldDbName : DbNameFound) {
                AbstractLoader.print("Mergeing " + oldDbName.getName());
                for (final ExternalDbLink dbref : oldDbName.getDbRefs()) {
                    dbref.setDatabaseName(tempDbName);
                }
                // remove old one
                oldDbName.delete();
            }
            wv.flush();

            final Database newDbName = RefDataUtility.findOrCreate(wv, Database.class, newAttrMap);
            wv.flush();
            for (final ExternalDbLink dbref : tempDbName.getDbRefs()) {
                dbref.setDatabaseName(newDbName);
            }
            // remove old one
            assert tempDbName.getDbRefs().size() == 0;
            tempDbName.delete();
            wv.flush();
        }

    }

    public static void main(final String[] args) {

        if (args.length == 0) {
            AbstractLoader.print("Usage: DbNameUtility filename.csv");
        }

        final AbstractModel model = ModelImpl.getModel();
        //AbstractLoader.silent = false;
        for (int i = 0; i < args.length; i++) {
            final String filename = args[i];
            final WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
            try {
                DbNameUtility.loadFile(wv, filename);
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
