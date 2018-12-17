package org.pimslims.graph.implementation;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.graph.IGraph;
import org.pimslims.graph.INode;
import org.pimslims.lab.ConstructUtility;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ComplexBeanWriter;

public class TargetGraphModel extends AbstractGraphModel {

    ModelObject modelObject = null;

    public static final String blueprintComponents = "blueprintComponents";

    public TargetGraphModel(final ModelObject centre) {
        super(centre);

    }

    @Override
    public IGraph createGraphModel(final ModelObject object) {
        this.modelObject = object;
        final ReadableVersion version = this.modelObject.get_Version();

        final LabBookEntry baseClass = (LabBookEntry) this.modelObject;
        if (baseClass instanceof Target) {
            this.startFromTarget(version);
        }
        return this;
    }

    private void startFromTarget(final ReadableVersion version) {
        final INode targetNode = this.addNode(this.modelObject);

        final Target target = (Target) this.modelObject;

        // get BluprintComponents
        final Set<ResearchObjectiveElement> blueprintComps = target.getResearchObjectiveElements();
        int edgeCount = 0;
        for (final Iterator i = blueprintComps.iterator(); i.hasNext();) {
            final ResearchObjectiveElement blueprintComponent = (ResearchObjectiveElement) i.next();

            final String compType = blueprintComponent.getComponentType();

            if (TargetGraphModel.isComplex(compType)) {

                final INode compNode = this.addNode(blueprintComponent.getResearchObjective());
                edgeCount = this.addEdge(targetNode, compNode);

            } else {

                // create BluprintComponents nodes
                final ResearchObjective expBlueprint = blueprintComponent.getResearchObjective();
                final INode compNode = this.addNode(expBlueprint);
                this.addEdge(targetNode, compNode);

                // create ResearchObjective nodes
                //final String hook = expBlueprint.get_Hook();
                //ModelObjectNode expBlueprintNode = this.getNodeForHook(hook);
                //if (expBlueprintNode == null) {
                //    expBlueprintNode = new ModelObjectNode(expBlueprint);
                //    this.addNode(expBlueprintNode);
                //}
                //edge = new DefaultModelEdge("edge" + edgeCount++, null, compNode, expBlueprintNode);
                //this.getEdges().add(edge);

                // create experiments nodes
                if (TargetGraphModel.isConstruct(compType)) {
                    final Set<Milestone> statLst = expBlueprint.getMilestones();
                    for (final Milestone exp : statLst) {
                        final INode expNode = this.addNode(exp.getExperiment());
                        this.addEdge(compNode, expNode);
                    }
                } else {
                    final Set<Experiment> expLst = expBlueprint.getExperiments();

                    for (final Experiment exp : expLst) {
                        final INode expNode = this.addNode(exp);
                        this.addEdge(compNode, expNode);
                    }
                }
            }
        }
    }

    public static boolean isConstruct(final String compType) {
        if (compType != null) {
            return compType.startsWith(ConstructUtility.SPOTCONSTRUCT);
        }
        return false;
    }

    private static boolean isComplex(final String compType) {
        if (compType != null) {
            return compType.contains(ComplexBeanWriter.COMPLEXTYPE);
        }
        return false;
    }

}
