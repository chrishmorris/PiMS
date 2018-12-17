/*
 * Created on 09-Feb-2006 14:27:12 @author Anne Pajon, pajon@ebi.ac.uk PIMS project, www.pims-lims.org
 * Copyright (C) 2006 EMBL - European Bioinformatics Institute - MSD group Wellcome Trust Genome Campus,
 * Hinxton, Cambridge CB10 1SD, UK
 */
package org.pimslims.data;

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
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.target.Milestone;

/**
 * 
 * Command line utility for adding target status data from CSV files for Target.Status Data is:
 * "namingSystem","name","details","scoreboard"
 * 
 */
public class TargetStatusUtility extends AbstractLoader {

    /**
     * @param CSV filename with a list of target status
     * @throws java.io.IOException
     */
    static void loadFile(final WritableVersion wv, final String filename) throws java.io.IOException,
        AccessException, ConstraintException {

        final java.io.Reader reader = new java.io.FileReader(filename);
        final CsvParser parser = new CsvParser(reader);

        while (parser.getLine() != null) {
            final String name = parser.getValueByLabel("name");

            if ("".equals(name.trim())) {
                // spacer line
                continue;
            }

            final Map<String, Object> attrMap = new java.util.HashMap<String, Object>();
            attrMap.put(TargetStatus.PROP_NAME, parser.getValueByLabel("name"));

            final java.util.Collection statusFound = wv.findAll(TargetStatus.class, attrMap);
            if (statusFound.isEmpty()) {
                attrMap.put(LabBookEntry.PROP_DETAILS, parser.getValueByLabel("details"));
                new TargetStatus(wv, attrMap);
                // print("Adding Status: [" + status.getName() +
                // "]");

            } else if (1 == statusFound.size()) {
                final TargetStatus status = (TargetStatus) statusFound.iterator().next();
                if ((status.getDetails() == null && parser.getValueByLabel("details") != null)
                    || (status.getDetails() != null && !status.getDetails().equalsIgnoreCase(
                        parser.getValueByLabel("details")))) {
                    status.setDetails(parser.getValueByLabel("details"));
                    AbstractLoader.print("-status already exists: [" + attrMap.get("name")
                        + "]'s details is UPDATED!");
                } else {
                    AbstractLoader.debug("=status already exists: [" + attrMap.get("name")
                        + "]'s details is the same!");
                }
            }

        }
    }

    /**
     * @param CSV filename with a list of target status to merge
     * @throws java.io.IOException
     */
    static void loadMergeFile(final WritableVersion wv, final String filename) throws java.io.IOException,
        AccessException, ConstraintException {

        final java.io.Reader reader = new java.io.FileReader(filename);
        final CsvParser parser = new CsvParser(reader);

        while (parser.getLine() != null) {
            final Map<String, Object> oldAttrMap = new java.util.HashMap<String, Object>();
            oldAttrMap.put(TargetStatus.PROP_NAME, parser.getValueByLabel("oldTargetStatus"));
            final Map<String, Object> newAttrMap = new java.util.HashMap<String, Object>();
            newAttrMap.put(TargetStatus.PROP_NAME, parser.getValueByLabel("newTargetStatus"));

            final String action = parser.getValueByLabel("action");

            if ("".equals(action.trim())) {
                // spacer line
                continue;
            } else if ("SAME".equalsIgnoreCase(action.trim())) {
                // same not changes
                continue;
            } else if ("Remove".equalsIgnoreCase(action.trim())) {
                final TargetStatus statusFound = wv.findFirst(TargetStatus.class, oldAttrMap);
                if (statusFound == null
                    || wv.findAll(Milestone.class, Milestone.PROP_STATUS, statusFound).size() == 0) {
                    RefDataUtility.removeRecord(wv, TargetStatus.class, oldAttrMap);
                    continue;
                }

                newAttrMap.put(TargetStatus.PROP_NAME, "Other");

            }

            final java.util.Collection<TargetStatus> statusFound = wv.findAll(TargetStatus.class, oldAttrMap);
            if (statusFound.size() > 1) {
                throw new RuntimeException("Found more than 1 status for "
                    + parser.getValueByLabel("oldNamingSystem") + ","
                    + parser.getValueByLabel("oldTargetStatus"));
            }
            TargetStatus newstatus;
            newstatus = RefDataUtility.findOrCreate(wv, TargetStatus.class, newAttrMap);

            // transfer the milestone
            for (final TargetStatus oldstatus : statusFound) {
                if (oldstatus.get_Hook().equalsIgnoreCase(newstatus.get_Hook())) {
                    continue;
                }
                final Collection<Milestone> mss =
                    wv.findAll(Milestone.class, Milestone.PROP_STATUS, oldstatus);
                for (final Milestone ms : mss) {
                    final Milestone newMilestone = new Milestone(wv, ms.getDate(), newstatus, ms.getTarget());
                    newMilestone.setAccess(ms.getAccess());
                    newMilestone.setAttachments(ms.getAttachments());
                    newMilestone.setExperiment(ms.getExperiment());
                    ms.delete();
                    wv.getSession().flush();
                }

                oldstatus.delete();

                wv.getSession().flush();
            }

        }
    }

    public static void main(final String[] args) {

        if (args.length == 0) {
            AbstractLoader.print("Usage: TargetStatusUtility filename.csv");
        }

        final AbstractModel model = ModelImpl.getModel();
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
                    TargetStatusUtility.loadMergeFile(wv, filename);
                } else {
                    AbstractLoader.print("Loading details from file: " + filename);
                    TargetStatusUtility.loadFile(wv, filename);
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
        AbstractLoader.print(TargetStatusUtility.class.getName() + " has finished");
    }

}
