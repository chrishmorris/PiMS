package org.pimslims.lab.experiment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.reference.WorkflowItem;

public class ExperimentTypeUtil {
    public static Map<TargetStatus, List<ExperimentType>> getExpTypeByMilestone(final ReadableVersion version) {
        return ExperimentTypeUtil.getExpTypeByMilestone(ExperimentTypeUtil.getExpTypes(version));
    }

    public static Map<TargetStatus, List<ExperimentType>> getExpTypeByMilestone(
        final List<ExperimentType> expTypes) {
        final Map<TargetStatus, List<ExperimentType>> expTypeByMilestone =
            new HashMap<TargetStatus, List<ExperimentType>>();
        final List<TargetStatus> possibleStatus = new LinkedList<TargetStatus>();
        // get all possible milestone status
        for (final ExperimentType expType : expTypes) {
            for (final WorkflowItem workflowItem : expType.getWorkflowItems()) {
                if (workflowItem.getStatus() != null) {
                    if (!possibleStatus.contains(workflowItem.getStatus())) {
                        possibleStatus.add(workflowItem.getStatus());
                    }
                }
            }
        }
        // add experimentTypes for each status
        for (final TargetStatus status : possibleStatus) {
            // System.out.println(status.getName());
            final List<ExperimentType> expTypesByStatus = new LinkedList<ExperimentType>();
            for (final ExperimentType expType : expTypes) {
                for (final WorkflowItem workflowItem : expType.getWorkflowItems()) {
                    if (workflowItem.getStatus().equals(status)) {
                        expTypesByStatus.add(expType);
                        // System.out.println("-"+expType.getName());
                    }
                }
            }
            assert expTypesByStatus.size() >= 1 : "expTypes of " + status.get_Hook() + " should not empty";
            expTypeByMilestone.put(status, expTypesByStatus);
        }
        return expTypeByMilestone;
    }

    /**
     * @param version the current transaction
     * @param expTypes a colelction of experiment types suitable for milestones
     */
    public static List<ExperimentType> getExpTypes(final ReadableVersion version) {
        // use all exptype for which there is exactly one WorkFlowItem
        final Collection<WorkflowItem> wfis = version.getAll(WorkflowItem.class, 0, 1000);
        assert 1000 > wfis.size() : "Sorry, you have too many workflow items";
        final Collection<ExperimentType> allExpTypes = version.getAll(ExperimentType.class, 0, 200);
        assert 200 > allExpTypes.size() : "Sorry, you have too many experiment types";

        final List<ExperimentType> expTypes = new ArrayList<ExperimentType>(allExpTypes.size());
        for (final Iterator iter = allExpTypes.iterator(); iter.hasNext();) {
            final ExperimentType type = (ExperimentType) iter.next();
            if (1 == type.getWorkflowItems().size()) {
                expTypes.add(type);
            }
        }

        Collections.sort(expTypes, ExperimentTypeUtil.ALPHABETICAL_ORDER_OF_NAME);
        assert 0 < expTypes.size() : "No SPOT experiment types found";
        return expTypes;
    }

    //TODO this function should be moved to DM project
    public static final Comparator ALPHABETICAL_ORDER_OF_NAME = new Comparator() {
        public int compare(final Object arg0, final Object arg1) {
            final ModelObject parm0 = (ModelObject) arg0;
            final ModelObject parm1 = (ModelObject) arg1;
            if (parm0 == null || parm0.get_Name() == null) {
                return -1;
            }
            if (parm1 == null || parm1.get_Name() == null) {
                return 1;
            }
            return parm0.get_Name().compareTo(parm1.get_Name());
        }

    };

}
