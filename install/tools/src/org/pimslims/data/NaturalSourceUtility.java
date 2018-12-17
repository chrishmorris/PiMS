/*
 * Created on 04-Jan-2007 15:55:03 @author Anne Pajon, pajon@ebi.ac.uk PIMS project, www.pims-lims.org
 * Copyright (C) 2007 EMBL - European Bioinformatics Institute - MSD group Wellcome Trust Genome Campus,
 * Hinxton, Cambridge CB10 1SD, UK
 */
package org.pimslims.data;

import java.io.File;
import java.util.Map;

import org.pimslims.csv.CsvParser;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.Target;

/**
 * 
 * Command line utility for loading natural source data from CSV files into the database as reference data
 * Data is: "organismName","ncbiTaxonomyId"
 * 
 */
public class NaturalSourceUtility extends AbstractLoader {

    /**
     * @param file TODO
     * @param CSV filename with a list of natural sources
     * @throws java.io.IOException
     */
    public static void loadFile(final WritableVersion wv, File file) throws java.io.IOException,
        AccessException, ConstraintException {

        final java.io.Reader reader = new java.io.FileReader(file);
        final CsvParser parser = new CsvParser(reader);

        while (parser.getLine() != null) {
            final String organismName = parser.getValueByLabel("organismName");
            final String ncbiTaxId = parser.getValueByLabel("ncbiTaxonomyId");

            if ("".equals(organismName.trim())) {
                // spacer line
                continue;
            }

            final Map<String, Object> attrMap = new java.util.HashMap<String, Object>();
            attrMap.put(Organism.PROP_NAME, organismName);

            final java.util.Collection naturalSourcesFound = wv.findAll(Organism.class, attrMap);
            if (0 == naturalSourcesFound.size()) {
                attrMap.put(Organism.PROP_NCBITAXONOMYID, ncbiTaxId);
                new Organism(wv, attrMap);
                // print("+Adding NaturalSource: [" + organismName
                // + "]");

            } else if (1 == naturalSourcesFound.size()) {
                final Organism naturalSource = (Organism) naturalSourcesFound.iterator().next();
                if ((naturalSource.getNcbiTaxonomyId() == null && ncbiTaxId != null)
                    || (naturalSource.getNcbiTaxonomyId() != null && !naturalSource.getNcbiTaxonomyId()
                        .equalsIgnoreCase(ncbiTaxId))) {
                    naturalSource.setNcbiTaxonomyId(ncbiTaxId);
                    AbstractLoader.print("-NaturalSource already exists: [" + naturalSource.getName()
                        + "]'s NcbiTaxonomyId is UPDATED!");
                } else {
                    AbstractLoader.debug("=NaturalSource already exists: [" + naturalSource.getName()
                        + "]'s NcbiTaxonomyId is the same!");
                }

            }

        }
    }

    /**
     * @param file TODO
     * @param CSV filename with a list of natural sources to merge
     * @throws java.io.IOException
     */
    static void loadMergeFile(final WritableVersion wv, File file) throws java.io.IOException,
        AccessException, ConstraintException {

        final java.io.Reader reader = new java.io.FileReader(file);
        final CsvParser parser = new CsvParser(reader);

        while (parser.getLine() != null) {
            final String action = parser.getValueByLabel("action");
            final Map<String, Object> oldAttrMap = new java.util.HashMap<String, Object>();
            oldAttrMap.put(Organism.PROP_NAME, parser.getValueByLabel("oldOrganismName"));
            final Map<String, Object> newAttrMap = new java.util.HashMap<String, Object>();
            newAttrMap.put(Organism.PROP_NAME, parser.getValueByLabel("newOrganismName"));
            newAttrMap.put(Organism.PROP_NCBITAXONOMYID, parser.getValueByLabel("newNcbiTaxonomyId"));

            if ("".equals(action.trim())) {
                // spacer line
                continue;
            } else if ("SAME".equalsIgnoreCase(action.trim())) {
                // same not changes
                continue;
            } else if ("Remove".equalsIgnoreCase(action.trim())) {
                RefDataUtility.removeRecord(wv, Organism.class, oldAttrMap);
                wv.getSession().flush();
                continue; // remove old one
            }

            final java.util.Collection<Organism> nssFound = wv.findAll(Organism.class, oldAttrMap);
            if (nssFound.size() > 1) {
                throw new RuntimeException("Found more than 1 NaturalSource for "
                    + parser.getValueByLabel("oldOrganismName"));
            } else if (nssFound.size() == 0) {
                continue; // no this record
            }
            // update old one
            final Organism ns = nssFound.iterator().next();
            final Organism newNS = wv.findFirst(Organism.class, newAttrMap);
            if (newNS != null) {
                if (newNS.getDbId() != ns.getDbId()) {
                    for (final Target t : ns.getTargets()) {
                        newNS.addTarget(t);
                    }
                    ns.delete();
                }
                continue;// already created
            }
            ns.setName((String) newAttrMap.get(Organism.PROP_NAME));
            ns.setNcbiTaxonomyId((String) newAttrMap.get(Organism.PROP_NCBITAXONOMYID));
            wv.getSession().flush();
        }

    }

    public static void main(final String[] args) {

        if (args.length == 0) {
            AbstractLoader.print("Usage: NaturalSourceUtility filename.csv");
        }

        final AbstractModel model = ModelImpl.getModel();
        //AbstractLoader.silent = false;
        boolean isMerge = false;
        for (int i = 0; i < args.length; i++) {
            final String filename = args[i];
            if (filename.equalsIgnoreCase("-merge")) {
                isMerge = true;
                continue;
            }
            final WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
            try {

                if (isMerge) {
                    AbstractLoader.print("Merging details from file: " + filename);
                    NaturalSourceUtility.loadMergeFile(wv, new java.io.File(filename));
                } else {
                    AbstractLoader.print("Loading details from file: " + filename);
                    NaturalSourceUtility.loadFile(wv, new java.io.File(filename));
                }
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
        AbstractLoader.print("NaturalSource utility has finished");
    }

}
