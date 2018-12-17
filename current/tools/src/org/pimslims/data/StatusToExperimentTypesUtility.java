/*
 * Created on 03-Jan-2007 17:27:01 @author Anne Pajon, pajon@ebi.ac.uk PIMS project, www.pims-lims.org
 * Copyright (C) 2007 EMBL - European Bioinformatics Institute - MSD group Wellcome Trust Genome Campus,
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
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.reference.WorkflowItem;

/**
 * 
 * Command line utility for associating experiment type and target status data from CSV file Data is:
 * "experimentTypeName","targetStatusName"
 * 
 */

public class StatusToExperimentTypesUtility extends AbstractLoader {

    /**
     * @param CSV filename with a list of experiment type / target status
     * @throws java.io.IOException
     */
    public static void loadFile(final WritableVersion wv, final String filename) throws java.io.IOException,
        AccessException, ConstraintException {

        final java.io.Reader reader = new java.io.FileReader(filename);
        final CsvParser parser = new CsvParser(reader);

        while (parser.getLine() != null) {
            final String expTypeName = parser.getValueByLabel("experimentTypeName");
            final String statusName = parser.getValueByLabel("targetStatusName");

            if ("".equals(expTypeName.trim())) {
                // spacer line
                continue;
            }

            // Protocol.ExperimentType
            final Map<String, Object> expTypeAttrMap = new java.util.HashMap<String, Object>();
            expTypeAttrMap.put(TargetStatus.PROP_NAME, expTypeName);
            final java.util.Collection expTypeFound = wv.findAll(ExperimentType.class, expTypeAttrMap);
            ExperimentType expType = null;
            if (1 == expTypeFound.size()) {
                expType = (ExperimentType) expTypeFound.iterator().next();
            } else {
                AbstractLoader.print(">>> ExperimentType [" + expTypeName
                    + "] does not exist. PLEASE LOAD ExperimentTypes.csv FIRST!");
            }

            // Target.Status
            final Map<String, Object> statusAttrMap = new java.util.HashMap<String, Object>();
            statusAttrMap.put(TargetStatus.PROP_NAME, statusName);
            final java.util.Collection statusFound = wv.findAll(TargetStatus.class, statusAttrMap);
            TargetStatus status = null;
            if (1 == statusFound.size()) {
                status = (TargetStatus) statusFound.iterator().next();
            } else {
                AbstractLoader.print(">>> Status [" + statusName
                    + "] does not exist. PLEASE LOAD Status.csv FIRST!");
            }

            // create the association between Protocol.ExperimentType and
            // Target.Status via Protocol.WorkflowItem
            final Map<String, Object> attrMap = new java.util.HashMap<String, Object>();
            if ((null != expType) && (null != status)) {
                attrMap.put(WorkflowItem.PROP_EXPERIMENTTYPE, expType);
                attrMap.put(WorkflowItem.PROP_STATUS, status);
                if (wv.findAll(WorkflowItem.class, attrMap).size() == 0) {
                    new WorkflowItem(wv, attrMap);
                    // print("Adding WorkflowItem for [" +
                    // expType.getName() + ", " + status.getName() + "]");
                } else {
                    AbstractLoader.debug("Found existing WorkflowItem for [" + expType.getName() + ", "
                        + status.getName() + "]");

                }
            }

        }
    }

    public static void main(final String[] args) {

        if (args.length == 0) {
            AbstractLoader.print("Usage: StatusToExperimentTypesUtility filename.csv");
        }

        final AbstractModel model = ModelImpl.getModel();

        for (int i = 0; i < args.length; i++) {
            final String filename = args[i];
            final WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
            try {
                StatusToExperimentTypesUtility.loadFile(wv, filename);
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
        AbstractLoader.print("Status/ExperimentTypes utility has finished");
    }

}
