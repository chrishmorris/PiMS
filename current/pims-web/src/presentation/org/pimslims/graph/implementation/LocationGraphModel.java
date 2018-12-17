/**
 * pims-web org.pimslims.utils.graph.implementation LocationGraphModel.java
 * 
 * @author Marc Savitsky
 * @date 3 Jan 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky * *
 * 
 */
package org.pimslims.graph.implementation;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.pimslims.graph.IEdge;
import org.pimslims.graph.INode;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.holder.Containable;
import org.pimslims.model.holder.Container;

/**
 * LocationGraphModel
 * 
 */
public class LocationGraphModel {

    final Set<INode> nodes = new LinkedHashSet();

    final Set<IEdge> edges = new LinkedHashSet();

    private final int edgeCount = 0;

    public static final String blueprintComponents = "blueprintComponents";

    public LocationGraphModel() {
        super();
    }

    public LocationGraphModel createGraphModel(final Containable object) {

        //System.out.println("org.pimslims.utils.graph.implementation.LocationGraphModel createGraphModel");

        final LabBookEntry baseClass = (LabBookEntry) object;
        if (baseClass instanceof Containable) {
            this.startFromGroup(object);
        }

        return this;
    }

    private void startFromGroup(final Containable object) {

        final INode groupNode = new LocationNode(object);
        this.addNode(groupNode);

        final Container parent = object.getContainer();
        if (null != parent) {
            this.addParent(groupNode, parent);
        }

        if (object instanceof Container) {
            final Collection<Containable> components = ((Container) object).getContained();
            for (final Containable component : components) {
                this.addChild(groupNode, component);
            }
        }

    }

    /**
     * LocationGraphModel.addNode
     * 
     * @param groupNode
     */
    private void addNode(final INode node) {
        this.nodes.add(node);

    }

    private void addChild(final INode parent, final Containable child) {

        //System.out.println("org.pimslims.utils.graph.implementation.LocationGraphModel addChild ["
        //    + child.get_Name() + ":" + child.get_Hook() + "]");

        final INode node = new LocationNode(child);

        //if (!this.getNodes().contains(node)) {
        if (!this.contains(node)) {
            this.addNode(node);
            final IEdge edge = new DefaultModelEdge(parent, node);
            this.getEdges().add(edge);
            if (child instanceof Container) {
                final Collection<Containable> components = ((Container) child).getContained();
                for (final Containable component : components) {
                    this.addChild(node, component);
                }
            }
        }
    }

    private void addParent(final INode child, final Container parent) {

        //System.out.println("org.pimslims.utils.graph.implementation.LocationGraphModel addParent ["
        //    + parent.get_Name() + ":" + parent.get_Hook() + "]");

        final INode node = new LocationNode(parent);
        this.addNode(node);
        final IEdge edge = new DefaultModelEdge(node, child);
        this.getEdges().add(edge);

        final Collection<Containable> components = parent.getContained();
        for (final Containable component : components) {
            //TODO don't make loops!
            this.addChild(node, component);
        }

        final Container grandParent = parent.getContainer();
        if (null != grandParent) {
            this.addParent(node, grandParent);
        }
    }

    private boolean contains(final INode myNode) {
        final String myLabel = ((LocationNode) myNode).getLabel();
        for (final INode node : this.getNodes()) {
            if (((LocationNode) node).getLabel().equals(myLabel)) {
                return true;
            }
        }
        return false;
    }

    public Set<INode> getNodes() {
        return this.nodes;
    }

    public Set<IEdge> getEdges() {
        return this.edges;
    }

}
