/**
 * org.pimslims.upgrader Upate_v24.java
 * 
 * @date 18-Dec-2006 08:24:11
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 *           Copyright (c) 2006
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.upgrader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader;

/**
 * Revision 23 to 24 x PIMS-301: add one-to-many link between Protocol and Annotation to associate files to a
 * Protocol ALTER TABLE anno_annotation ADD COLUMN protocolid int8; x change attr
 * ParameterDefinition.paramType to mandatory ALTER TABLE prot_parameterdefinition ALTER COLUMN paramtype SET
 * NOT NULL; x change role TargetStatus.code to unchangeable (ignored) x change attribute MolComponent.molType
 * to mandatory ALTER TABLE refs_molcomponent ALTER COLUMN moltype SET NOT NULL; x PIMS-390: change mandatory
 * link between Sample/SampleCategory to non-mandatory (ignored) (*-* table) x rename Target.TargetStatus to
 * Target.Milestone ALTER TABLE targ_targetstatus RENAME TO targ_milestone ; x add new class WorkflowItem with
 * links *---0..1 to Status, *---1..1 to ExperimentType and 2 *---* links to SampleCategory -normal upgrader x
 * delete many-to-many link between SampleCategory and ExperimentType Delete TABLE sam_sampca2expety x
 * PIMS-79: TargetStatus/Experiment link x delete BlueprintStatus Delete TABLE expb_blueprintstatus x rename
 * TargetStatus to Milestone Same x add a one-to-many link between ExpBlueprint and Milestone ALTER TABLE
 * targ_milestone ADD COLUMN expblueprintid int8;(1) x add a many-to-one link between Milestone and Experiment
 * ALTER TABLE targ_milestone ADD COLUMN experimentid int8; (2) new record base on old BlueprintStatus (1&2)
 * 
 * x PIMS-250: add constraint *either* a title *or* (a year and at least one author) in Citation ??? not in
 * DB??? ** For Paris *** x add attr rotorType in CentrifugationStep ALTER TABLE prot_centrifugationstep ADD
 * COLUMN rotortype varchar(80); x add attr columnType in FlowStep ALTER TABLE prot_flowstep ADD COLUMN
 * columntype varchar(80); x add new subclass of Step called CrunchingStep -normal upgrader x add attr
 * revolutionPerMinute x add attr ballDiameterSize x add attr ballVolume ** End for Paris ***
 */
public class Update_v24 extends SpecialUpgrader {

    public Update_v24(ModelUpdateVersionImpl muv) {
        super(muv);
    }

    @Override
    public int getVersion() {
        return 24;
    }

    @Override
    protected void upgrade() throws SQLException, ConstraintException {

        // PIMS-301: add one-to-many link between Protocol and Annotation to
        // associate files to a Protocol
        // ALTER TABLE anno_annotation ADD COLUMN protocolid int8;
        muv.createColumn("anno_annotation", "protocolid", "int8");

        // x change attr ParameterDefinition.paramType to mandatory
        // ALTER TABLE prot_parameterdefinition ALTER COLUMN paramtype SET NOT
        // NULL;
        updateParamType(muv);
        muv.setColumnNotNull("prot_parameterdefinition", "paramtype");

        // x change attribute MolComponent.molType to mandatory
        // TABLE refs_molcomponent ALTER COLUMN moltype SET NOT NULL;
        updateMolType(muv);
        muv.setColumnNotNull("refs_molcomponent", "moltype");

        // x rename Target.TargetStatus to Target.Milestone
        // ALTER TABLE targ_targetstatus RENAME TO targ_milestone ;
        muv.renameTable("targ_targetstatus", "targ_milestone");
        muv.getDbUpdater().setPrimaryKey("targ_milestone", "memopsbaseclassid");
        // x delete many-to-many link between SampleCategory and ExperimentType
        // Delete TABLE sam_sampca2expety
        muv.dropTable("sam_sampca2expety");
        // x PIMS-79: TargetStatus/Experiment link
        // x rename TargetStatus to Milestone
        // Same
        // x add a one-to-many link between ExpBlueprint and Milestone
        // ALTER TABLE targ_milestone ADD COLUMN expblueprintid int8;(1)
        muv.createColumn("targ_milestone", "expblueprintid", "int8");
        // x add a many-to-one link between Milestone and Experiment
        // ALTER TABLE targ_milestone ADD COLUMN experimentid int8; (2)
        muv.createColumn("targ_milestone", "experimentid", "int8");
        // new record base on old BlueprintStatus (1&2)
        transferFromBlueprintstatusToMilestone(muv);
        // x delete BlueprintStatus
        // Delete TABLE expb_blueprintstatus
        muv.dropTable("expb_blueprintstatus");
        // x PIMS-250: add constraint *either* a title *or* (a year and at least
        // one author) in Citation
        // ??? not in DB???
        //
        // *** For Paris ***
        // x add attr rotorType in CentrifugationStep
        // ALTER TABLE prot_centrifugationstep ADD COLUMN rotortype varchar(80);
        muv.createColumn("prot_centrifugationstep", "prot_centrifugationstep", "varchar(80)");
        // x add attr columnType in FlowStep
        // ALTER TABLE prot_flowstep ADD COLUMN columntype varchar(80);
        muv.createColumn("prot_flowstep", "columntype", "varchar(80)");
        // x add new subclass of Step called CrunchingStep
        // -normal upgrader
        // x add attr revolutionPerMinute
        // x add attr ballDiameterSize
        // x add attr ballVolume
        // *** End for Paris ***

        // additional changes:
        // Column anno_annotation.name has changed (80 - 32)
        muv.changeColumnLength("anno_annotation", "name", "varchar", 80);
        // change the type of the AbstractHolderType.name from Word to Line
        muv.changeColumnLength("hold_abstractholdertype", "name", "varchar", 80);

        // update revision number
        muv.setDBRevisionNumber();
    }

    /**
     * prot_parameterdefinition 's column paramtype is not null now
     * 
     * @param muv
     * @throws SQLException
     */
    private void updateParamType(ModelUpdateVersionImpl muv) throws SQLException {
        DatabaseUpdater dbu = muv.getDbUpdater();

        ResultSet rs =
            dbu.sqlExecuteQuery("select memopsbaseclassid, paramtype,name from prot_parameterdefinition");
        while (rs.next()) {
            String paramtype = rs.getString("paramtype");
            // invalid molType, may have other cases but not concerned here
            if (paramtype == null || paramtype.trim().length() == 0) {
                paramtype = "String";
                System.out.println("Warning:paramtype is set as 'String' for parameterdefinition (name="
                    + rs.getString("name") + ", id=" + rs.getString("memopsbaseclassid") + ")!");
            }
            rs.updateString("paramtype", paramtype);
            rs.updateRow();
        }
        rs.close();

    }

    /**
     * "refs_molcomponent"'s "moltype" changed to not null we need to create molType for null/"" value
     * 
     * @param muv
     * @throws SQLException
     */
    private void updateMolType(ModelUpdateVersionImpl muv) throws SQLException {
        DatabaseUpdater dbu = muv.getDbUpdater();

        ResultSet rs =
            dbu.sqlExecuteQuery("select abstractcomponentid, moltype,seqstring from refs_molcomponent");
        while (rs.next()) {
            String molType = rs.getString("moltype");
            // invalid molType, may have other cases but not concerned here
            if (molType == null || molType.trim().length() == 0) {
                String seqstring = rs.getString("seqstring");
                if (seqstring == null || seqstring.trim().length() == 0) // no
                    // seq
                    // ->
                    // "other"
                    molType = "other";
                else// DNA or protein
                {
                    if (isValidDNA(seqstring))
                        molType = "DNA";
                    else if (isValidProtien(seqstring))
                        molType = "protein";
                    else
                        molType = "other";
                }
                rs.updateString("moltype", molType);
                rs.updateRow();
                System.out.println("Warning:moltype is set as '" + molType + "' for molcomponent (seqstring="
                    + rs.getString("seqstring") + ", id=" + rs.getString("abstractcomponentid") + ")!");

            }
        }
        rs.close();

    }

    /**
     * Pattern for DNA
     */
    private static final Pattern DNAEXP = Pattern.compile("[A*C*G*T*\\s*]+", Pattern.CASE_INSENSITIVE);

    static boolean isValidDNA(String DNAString) {

        return DNAEXP.matcher(DNAString.trim()).matches();

    }

    /**
     * Pattern for DNA
     */
    private static final Pattern PROTIENEXP = Pattern.compile(
        "[A*C*D*E*F*G*H*I*K*L*M*N*P*Q*R*S*T*V*W*X*Y*\\s*]+", Pattern.CASE_INSENSITIVE);

    // A C D E F G H I K L M N P Q R S T V W Y X
    static boolean isValidProtien(String ProtienString) {
        return PROTIENEXP.matcher(ProtienString.trim()).matches();

    }

    /**
     * the link from Blueprintstatus to experiment and expblueprint will be transfer to Milestone
     * 
     * @param muv
     * @throws SQLException
     * @throws ConstraintException
     */
    @SuppressWarnings("deprecation")
    private void transferFromBlueprintstatusToMilestone(ModelUpdateVersionImpl muv) throws SQLException,
        ConstraintException {
        DatabaseUpdater dbu = muv.getDbUpdater();

        // load the links from Blueprintstatus to code,exp,expblueprint
        Map<Long, Long> milestoneToExperiment = new HashMap<Long, Long>();
        Map<Long, Long> milestoneToExpblueprint = new HashMap<Long, Long>();
        Map<Long, Long> milestoneToCode = new HashMap<Long, Long>();
        Map<Long, Timestamp> milestoneToDate = new HashMap<Long, Timestamp>();
        ResultSet rs =
            dbu.sqlExecuteQuery("select memopsbaseclassid,codeid,experimentid,expblueprintid,date_"
                + " from expb_blueprintstatus");
        while (rs.next()) {
            Long blueprintstatusID = rs.getLong("memopsbaseclassid");
            milestoneToExperiment.put(blueprintstatusID, rs.getLong("experimentid"));
            milestoneToExpblueprint.put(blueprintstatusID, rs.getLong("expblueprintid"));
            milestoneToCode.put(blueprintstatusID, rs.getLong("codeid"));
            milestoneToDate.put(blueprintstatusID, rs.getTimestamp("date_"));
        }
        rs.close();
        // load the links from expblueprint to target
        Map<Long, Target> ExpblueprintToTarget = new HashMap<Long, Target>();
        Collection<ResearchObjective> expBPs = muv.getAll(ResearchObjective.class);
        for (ResearchObjective expBP : expBPs) {
            Target t = null;
            if (expBP.getBlueprintComponents() != null && expBP.getBlueprintComponents().size() > 1)
                throw new RuntimeException("ExpBlueprint has more than one BlueprintComponents! id="
                    + expBP.get_Hook());
            if (expBP.getBlueprintComponents() != null && expBP.getBlueprintComponents().size() == 1)
                t = expBP.getBlueprintComponents().iterator().next().getTarget();
            ExpblueprintToTarget.put(expBP.getDbId(), t);
        }
        // create new milestone base on Blueprintstatus
        for (Long blueprintstatusID : milestoneToCode.keySet()) {
            Target target = ExpblueprintToTarget.get(milestoneToExpblueprint.get(blueprintstatusID));
            TargetStatus code =
                muv.get("org.pimslims.model.target.Status:" + milestoneToCode.get(blueprintstatusID));
            Experiment exp =
                muv.get("org.pimslims.model.experiment.Experiment:"
                    + milestoneToExperiment.get(blueprintstatusID));
            ResearchObjective expBlueprint =
                muv.get("org.pimslims.model.expBlueprint.ExpBlueprint:"
                    + milestoneToExpblueprint.get(blueprintstatusID));
            Calendar calDate = Calendar.getInstance();
            calDate.setTime(milestoneToDate.get(blueprintstatusID));
            Milestone milstone = new Milestone(muv, calDate, code, target);
            milstone.setExperiment(exp);
            exp.setProject(expBlueprint);
        }

    }
}
