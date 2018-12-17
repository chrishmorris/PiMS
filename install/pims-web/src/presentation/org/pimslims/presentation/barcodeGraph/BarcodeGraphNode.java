/**
 * pims-web org.pimslims.presentation.barcodeGraph BarcodeGraphNode.java
 * 
 * @author Marc Savitsky
 * @date 14 Jan 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky * *
 * 
 */
package org.pimslims.presentation.barcodeGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.pimslims.graph.implementation.LocationNode;

/**
 * BarcodeGraphNode TODO abolish this class - most of its methods are delegated to LocationNode
 */
public class BarcodeGraphNode implements Comparable {

    private final LocationNode node;

    private final List<BarcodeGraphNode> nodes = new ArrayList<BarcodeGraphNode>();

    public BarcodeGraphNode(final LocationNode node) {
        this.node = node;
    }

    public int compareTo(final Object object) {
        return this.node.compareTo(object);
    }

    /**
     * Define equality of state.
     */
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if ((other instanceof BarcodeGraphNode)) {
            return this.node.equals(((BarcodeGraphNode) other).node);
        }
        return false;
    }

    /**
     * A class that overrides equals must also override hashCode.
     */
    @Override
    public int hashCode() {
        return this.node.hashCode();
    }

    public String getLabel() {
        return this.node.getLabel();
    }

    public String get_Hook() {
        return this.node.get_Hook();
    }

    public void addChild(final BarcodeGraphNode child) {
        this.nodes.add(child);
    }

    public Collection<BarcodeGraphNode> getChildren() {
        Collections.sort(this.nodes);
        return this.nodes;
    }

    /**
     * BarcodeGraphNode.contains
     * 
     * @param child
     * @return
     */
    public boolean contains(final BarcodeGraphNode child) {
        return this.nodes.contains(child);
    }
}
