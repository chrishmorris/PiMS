package org.pimslims.lab;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.reference.WorkflowItem;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;

public class ConstructMileStoneUtil {

    /**
     * @param version
     * @param personHook
     * @param expBlueprintInPims
     * @param experimentTypeInPims
     * @param dt
     * @param comments
     * @param cb
     * @throws AccessException
     * @throws ConstraintException
     */
    public static Milestone createMilestone(final WritableVersion version,
        final ResearchObjective expBlueprintInPims, final String targetHook,
        final ExperimentType experimentTypeInPims, Experiment experiment, final java.util.Date dt,
        final String comments) throws AccessException, ConstraintException {

        final Collection<WorkflowItem> wfis = experimentTypeInPims.getWorkflowItems();
        TargetStatus targetStatusInPims = null;
        assert 1 == wfis.size() : "Must be exactly one status for experiment type: "
            + experimentTypeInPims.get_Name();
        targetStatusInPims = wfis.iterator().next().getStatus();
        assert null != targetStatusInPims : "No target status for: " + experimentTypeInPims.get_Name();

        final Target targetInPims = version.get(targetHook);
        //set default owner
        if (experiment != null) {
            version.setDefaultOwner(experiment.getAccess());
        } else if (expBlueprintInPims != null) {
            version.setDefaultOwner(expBlueprintInPims.getAccess());
        } else if (targetInPims != null) {
            version.setDefaultOwner(targetInPims.getAccess());
        }
        final Calendar date = java.util.Calendar.getInstance();
        date.setTimeInMillis((dt).getTime());
        /*HashMap targetStatusAttributes = new HashMap();
        targetStatusAttributes.put("target", targetInPims);
        targetStatusAttributes.put("code", targetStatusInPims);
        targetStatusAttributes.put("date", date);
        version.create(Milestone.class, targetStatusAttributes); //TODO do we really need this one? */

        if (null == experiment) {
            experiment =
                ConstructMileStoneUtil.createMilestoneExperiment(comments, version, expBlueprintInPims,
                    experimentTypeInPims, date);
        }
        experiment.setDetails(comments);
        experiment.setStatus("OK");
        final HashMap bluePrintStatusAttributes = new HashMap();
        bluePrintStatusAttributes.put(Milestone.PROP_STATUS, targetStatusInPims);
        bluePrintStatusAttributes.put(Milestone.PROP_DATE, date);
        bluePrintStatusAttributes.put(Milestone.PROP_TARGET, targetInPims);

        final Milestone blueprintStatus = version.create(Milestone.class, bluePrintStatusAttributes);
        blueprintStatus.setExperiment(experiment);
        experiment.setProject(expBlueprintInPims);
        return blueprintStatus;
    }

    /**
     * @param personHook
     * @param comments
     * @param version
     * @param expBlueprintInPims
     * @param cb
     * @param experimentTypeInPims
     * @param date
     * @return
     * @throws ConstraintException
     * @throws AccessException
     */
    public static Experiment createMilestoneExperiment(final String comments, final WritableVersion version,
        final ResearchObjective expBlueprintInPims, final ExperimentType experimentTypeInPims,
        final Calendar date) throws ConstraintException, AccessException {
        final HashMap expeAttributes = new HashMap();
        expeAttributes.put(Experiment.PROP_STARTDATE, date);
        expeAttributes.put(Experiment.PROP_ENDDATE, date);
        expeAttributes.put(Experiment.PROP_EXPERIMENTTYPE, experimentTypeInPims);
        expeAttributes.put(Experiment.PROP_STATUS, "OK");
        expeAttributes.put(Experiment.PROP_PROJECT, expBlueprintInPims);

        /**
         * the name was SPOT_NAMING_SYSTEM+"_"+cb.getConstructId()+"_"+experiment.getDbId() it was set after
         * create() However name is not changeable now and id is not available before create Using a unique id
         * instead By Bill
         */
        expeAttributes.put(Experiment.PROP_NAME, ConstructMileStoneUtil.SPOT_NAMING_SYSTEM + "_"
            + expBlueprintInPims.getCommonName() + "_" + version.getUniqueID());

        final Experiment experiment = version.create(Experiment.class, expeAttributes);
        // TODO don't say "SPOT"
        experiment.setName(ConstructMileStoneUtil.SPOT_NAMING_SYSTEM + "_"
            + expBlueprintInPims.getCommonName() + "_" + experiment.getDbId());
        // was experiment.setDetails(request.getParameter("result"));
        experiment.setProject(expBlueprintInPims);
        return experiment;
    }

    public static final String SPOT_NAMING_SYSTEM = ConstructUtility.SPOT_NAMING_SYSTEM;

    public static Milestone getLatestMilestone(final ResearchObjective researchObjective) {
        final Set<Milestone> miles = researchObjective.getMilestones();
        Milestone recentMile = null;
        for (final Milestone mile : miles) {
            if (recentMile == null) {
                recentMile = mile;
            } else {
                if (mile.getDate().after(recentMile.getDate())) {
                    recentMile = mile;
                }
            }
        }
        return recentMile;
    }

}
