package org.pimslims.lab;

import java.util.Collection;
import java.util.Iterator;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.reference.WorkflowItem;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;

public class StatusUtility {

    //TODO link milestone to Project
    public static void createMilestone(final Experiment experiment) throws ConstraintException {

        final Collection<WorkflowItem> items = experiment.getExperimentType().getWorkflowItems();
        assert 1 == items.size() : "There must be one status to make a milestone";
        final TargetStatus code = items.iterator().next().getStatus();

        final Collection<ResearchObjectiveElement> bpcs =
            ((ResearchObjective) experiment.getProject()).getResearchObjectiveElements();
        Target target = null;
        for (final Iterator iterator = bpcs.iterator(); iterator.hasNext();) {
            final ResearchObjectiveElement bpc = (ResearchObjectiveElement) iterator.next();
            if (null != bpc.getTarget()) {
                target = bpc.getTarget();
                final Milestone milestone =
                    new Milestone((WritableVersion) experiment.get_Version(), experiment.getEndDate(), code,
                        target);
                milestone.setExperiment(experiment);
            }
        }
        if (null == target) {
            throw new IllegalArgumentException("no target");
        }

    }

}
