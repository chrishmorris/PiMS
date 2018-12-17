/**
 * pims-web org.pimslims.utils.graph.implementation ComplexGraphModel.java
 * 
 * @author Marc Savitsky
 * @date 23 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky * *
 * 
 */
package org.pimslims.graph.implementation;

import java.util.Set;

import org.pimslims.graph.IGraph;
import org.pimslims.graph.INode;
import org.pimslims.lab.ConstructUtility;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;

/**
 * ComplexGraphModel Model for workflow of Complex and Construct
 * 
 */
public class ComplexGraphModel extends AbstractGraphModel {

    public static final String blueprintComponents = "blueprintComponents";

    public ComplexGraphModel(final ModelObject centre) {
        super(centre);
    }

    @Override
    public IGraph createGraphModel(final ModelObject object) {

        System.out.println("org.pimslims.utils.graph.implementation.ComplexGraphModel createGraphModel");

        final LabBookEntry baseClass = (LabBookEntry) object;
        if (baseClass instanceof ResearchObjective) {
            this.startFromGroup(object);
        }

        return this;
    }

    private void startFromGroup(final ModelObject object) {

        System.out.println("org.pimslims.utils.graph.implementation.ComplexGraphModel startFrom ["
            + object.get_Name() + ":" + object.get_Hook() + "]");

        //ComplexBean bean = ComplexBeanReader.readComplexHook(version, hook);

        final ResearchObjective group = (ResearchObjective) object;
        if (this.isComplex(group)) {
            this.makeComplexModel(group);
        }
        if (this.isConstruct(group)) {
            this.makeConstructModel(group);
        }
    }

    private void makeComplexModel(final ResearchObjective group) {

        final INode groupNode = this.addNode(group);

        final Set<ResearchObjectiveElement> components = group.getResearchObjectiveElements();
        for (final ResearchObjectiveElement component : components) {
            if (component.getComponentType().equals("complex")) {
                if (null != component.getTarget()) {
                    this.addChild(groupNode, component.getTarget());
                }
            }
        }
    }

    private void makeConstructModel(final ResearchObjective group) {

        final INode groupNode = this.addNode(group);

        final Set<ResearchObjectiveElement> components = group.getResearchObjectiveElements();
        for (final ResearchObjectiveElement component : components) {
            if (component.getComponentType().equals("OpticConstruct")
                || component.getComponentType().equals(ConstructUtility.SPOTCONSTRUCT)) {

                //final INode compNode = new ModelObjectNode(component);
                //this.addNode(compNode);
                //this.getEdges().add(
                //    new DefaultModelEdge("edge" + this.edgeCount++, null, compNode, groupNode));
                if (null != component.getTarget()) {
                    final INode targetNode = this.addNode(component.getTarget());
                    this.addEdge(targetNode, groupNode);
                }

                // create experiments nodes
                if (component.getComponentType().equals(ConstructUtility.SPOTCONSTRUCT)) {
                    final Set<Milestone> statLst = group.getMilestones();
                    for (final Milestone exp : statLst) {
                        final INode expNode = this.addNode(exp.getExperiment());
                        this.getEdges().add(new DefaultModelEdge(groupNode, expNode));
                    }
                } else {
                    final Set<Experiment> expLst = group.getExperiments();
                    for (final Experiment exp : expLst) {
                        final INode expNode = this.addNode(exp);
                        this.getEdges().add(new DefaultModelEdge(groupNode, expNode));
                    }
                }
            }
        }

    }

    private boolean isComplex(final ResearchObjective objective) {
        final Set<ResearchObjectiveElement> components = objective.getResearchObjectiveElements();
        for (final ResearchObjectiveElement component : components) {
            if (component.getComponentType().equals("complex")) {
                return true;
            }
        }
        return false;
    }

    private boolean isConstruct(final ResearchObjective objective) {
        return ConstructUtility.isSpotConstruct(objective);
    }
}
