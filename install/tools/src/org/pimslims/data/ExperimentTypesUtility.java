/*
 * Created on 07-Feb-2006 17:57:31 @author Anne Pajon, pajon@ebi.ac.uk PIMS project, www.pims-lims.org
 * Copyright (C) 2006 EMBL - European Bioinformatics Institute - MSD group Wellcome Trust Genome Campus,
 * Hinxton, Cambridge CB10 1SD, UK
 */
package org.pimslims.data;

import java.io.File;
import java.io.Reader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.pimslims.csv.CsvParser;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.upgrader.DataFixer;

/**
 * 
 * Command line utility for adding experimet type data from CSV files for ExperimentType Data is:
 * "name","topExperimentType","details"
 * 
 */
public class ExperimentTypesUtility extends AbstractLoader {

    /**
     * @param CSV filename with a list of experiment types
     * @throws java.io.IOException
     */
    public static void loadFile(final WritableVersion wv, final Reader reader) throws java.io.IOException,
        AccessException, ConstraintException {

        final CsvParser parser = new CsvParser(reader);
        parser.getLabels();
        while (parser.getLine() != null) {
            final String name = parser.getValueByLabel("name");

            if ("".equals(name.trim())) {
                // spacer line
                continue;
            }

            final Map<String, Object> attrMap = new java.util.HashMap<String, Object>();
            attrMap.put(ExperimentType.PROP_NAME, parser.getValueByLabel("name"));
            attrMap.put(LabBookEntry.PROP_DETAILS, parser.getValueByLabel("details"));

            final Map<String, Object> keyAttrMap = new java.util.HashMap<String, Object>();
            keyAttrMap.put(ExperimentType.PROP_NAME, parser.getValueByLabel("name"));

            AbstractLoader.UpdateOrCreate(wv, ExperimentType.class, keyAttrMap, Arrays.asList(parser
                .getValueByLabel("previousName")), attrMap);

        }
    }

    /**
     * @param file TODO
     * @param CSV filename with a list of experiment types to merge
     * @throws java.io.IOException
     * @throws SQLException
     */
    static void loadMergeFile(final WritableVersion wv, File file)
        throws java.io.IOException, AccessException, ConstraintException, SQLException {
        final java.io.Reader reader = new java.io.FileReader(file);
        final CsvParser parser = new CsvParser(reader);

        while (parser.getLine() != null) {
            final String action = parser.getValueByLabel("action");
            final Map<String, Object> oldAttrMap = new java.util.HashMap<String, Object>();
            oldAttrMap.put(ExperimentType.PROP_NAME, parser.getValueByLabel("oldName"));
            final Map<String, Object> newAttrMap = new java.util.HashMap<String, Object>();
            newAttrMap.put(ExperimentType.PROP_NAME, parser.getValueByLabel("newName"));
            final String newDetails = parser.getValueByLabel("newDetails");

            if ("".equals(action.trim())) {
                // spacer line
                continue;
            } else if ("SAME".equalsIgnoreCase(action.trim())) {
                // same not changes
                continue;
            } else if ("Remove".equalsIgnoreCase(action.trim())) {
                final ExperimentType et = wv.findFirst(ExperimentType.class, oldAttrMap);
                if (et == null
                    || ((et.getExperiments() == null || et.getExperiments().size() == 0) && (et
                        .getProtocols() == null || et.getProtocols().size() == 0))) {
                    RefDataUtility.removeRecord(wv, ExperimentType.class, oldAttrMap);
                    wv.getSession().flush();
                    continue;
                }
                newAttrMap.put(ExperimentType.PROP_NAME, "Other");
            }

            final java.util.Collection<ExperimentType> experimentTypeFound =
                wv.findAll(ExperimentType.class, oldAttrMap);
            if (experimentTypeFound.size() > 1) {
                throw new RuntimeException("Found more than 1 ExperimentType for "
                    + parser.getValueByLabel("oldNamingSystem") + "," + parser.getValueByLabel("oldName"));
            }
            ExperimentType newET;
            newET = RefDataUtility.findOrCreate(wv, ExperimentType.class, newAttrMap);
            newET.setDetails(newDetails);

            if (experimentTypeFound.size() == 1) {
                final ExperimentType oldET = experimentTypeFound.iterator().next();
                for (final Protocol protocol : oldET.getProtocols()) {
                    protocol.setExperimentType(newET);
                }
                final Collection<Experiment> oldExp = oldET.getExperiments();
                for (final Experiment exp : oldExp) {
                    DataFixer.updateExpType(wv, exp, newET);// it is difficault
                    // to change the exp
                    // type as no api
                    // from exp can do
                    // it(unchangeable)
                }

                oldET.delete();
                wv.getSession().flush();
            }

        }
    }

    public static void main(final String[] args) {

        if (args.length == 0) {
            AbstractLoader.print("Usage: ExperimentTypesUtility filename.csv");
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
                    ExperimentTypesUtility.loadMergeFile(wv, new java.io.File(filename));
                } else {
                    AbstractLoader.print("Loading details from file: " + filename);
                    final java.io.Reader reader = new java.io.FileReader(filename);
                    ExperimentTypesUtility.loadFile(wv, reader);
                }
                wv.commit();
                AbstractLoader.print("Loaded details from file: " + filename);
            } catch (final java.io.IOException ex) {
                AbstractLoader.print("Unable to read from file: " + filename);
                ex.printStackTrace();
            } catch (final ModelException ex) {
                AbstractLoader.print("Unable to add details from file: " + filename);
                ex.printStackTrace();
            } catch (final SQLException e) {
                AbstractLoader.print("Unable to add details from file: " + filename);
                e.printStackTrace();
            } finally {
                if (!wv.isCompleted()) {
                    wv.abort();
                }
            }
        }
        AbstractLoader.print("ExperimentTypes utility has finished");
    }

}
