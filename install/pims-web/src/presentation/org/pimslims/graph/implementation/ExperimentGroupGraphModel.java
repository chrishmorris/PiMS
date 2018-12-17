package org.pimslims.graph.implementation;

import java.util.Set;

import org.pimslims.graph.IGraph;
import org.pimslims.graph.INode;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.presentation.plateExperiment.PlateExperimentUtility;

public class ExperimentGroupGraphModel extends AbstractGraphModel {

    public static final String blueprintComponents = "blueprintComponents";

    public ExperimentGroupGraphModel(final ModelObject centre) {
        super(centre);
    }

    @Override
    public IGraph createGraphModel(final ModelObject object) {

        System.out
            .println("org.pimslims.utils.graph.implementation.ExperimentGroupGraphModel createGraphModel");

        final LabBookEntry baseClass = (LabBookEntry) object;
        if (baseClass instanceof ExperimentGroup) {
            this.startFromGroup(object);
        }

        return this;
    }

    private void startFromGroup(final ModelObject object) {

        System.out
            .println("org.pimslims.utils.graph.implementation.ExperimentGroupGraphModel startFromGroup ["
                + object.get_Name() + ":" + object.get_Hook() + "]");

        final INode groupNode = this.addNode(object);

        final ExperimentGroup group = (ExperimentGroup) object;

        Set<ExperimentGroup> experimentGroups = PlateExperimentUtility.getPreviousPlateExperiments(group);
        for (final ExperimentGroup experimentGroup : experimentGroups) {
            this.addParent(groupNode, experimentGroup);
        }

        experimentGroups = PlateExperimentUtility.getNextPlateExperiments(group);
        for (final ExperimentGroup experimentGroup : experimentGroups) {
            this.addChild(groupNode, experimentGroup);
        }
    }

    @Override
    protected ModelObjectNode addChild(final INode parent, final ModelObject child) {
        final INode childNode = super.addChild(parent, child);

        final ExperimentGroup group = (ExperimentGroup) child;
        final Set<ExperimentGroup> experimentGroups = PlateExperimentUtility.getNextPlateExperiments(group);

        for (final ExperimentGroup experimentGroup : experimentGroups) {
            this.addChild(childNode, experimentGroup);
        }
        return (ModelObjectNode) childNode;
    }

    protected void addParent(final INode child, final ModelObject parent) {

        final INode compNode = this.doAddParent(child, parent);

        final ExperimentGroup group = (ExperimentGroup) parent;
        final Set<ExperimentGroup> experimentGroups =
            PlateExperimentUtility.getPreviousPlateExperiments(group);

        for (final ExperimentGroup experimentGroup : experimentGroups) {
            this.addParent(compNode, experimentGroup);
        }
    }
}
