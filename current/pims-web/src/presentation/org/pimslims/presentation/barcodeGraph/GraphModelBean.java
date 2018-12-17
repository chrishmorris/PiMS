/**
 * pims-web org.pimslims.presentation ImageModelBean.java
 * 
 * @author Marc Savitsky
 * @date 9 Jan 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky * *
 * 
 */
package org.pimslims.presentation.barcodeGraph;

import java.util.Set;

import org.pimslims.graph.IEdge;
import org.pimslims.graph.INode;
import org.pimslims.graph.implementation.LocationGraphModel;
import org.pimslims.graph.implementation.LocationNode;

/**
 * ImageModelBean
 * 
 */
public class GraphModelBean {

    public static BarcodeGraphNode getModel(final LocationGraphModel graph) {

        IEdge edge = null;
        // choose an edge at random
        if (!graph.getEdges().isEmpty()) {
            edge = graph.getEdges().iterator().next();
        }
        INode node = null;
        // find a root
        while (null != edge) {
            node = edge.getHead();
            edge = GraphModelBean.getParent(graph.getEdges(), edge);
        }

        if (null == node) {
            node = (INode) graph.getNodes().toArray()[0];
        }

        final BarcodeGraphNode ancestorNode = new BarcodeGraphNode((LocationNode) node);
        GraphModelBean.addChildren(ancestorNode, graph);

        /*
        System.out.println("GraphModelBean.getModel");
        for (BarcodeGraphNode child : ancestorNode.getChildren()) {
            System.out.println("GraphModelBean child [" + child.getName() + "]");
            for (BarcodeGraphNode grandchild : child.getChildren()) {
                System.out.println("GraphModelBean grandchild [" + grandchild.getName() + "]");
                for (BarcodeGraphNode greatgrandchild : grandchild.getChildren()) {
                    System.out.println("GraphModelBean greatgrandchild [" + greatgrandchild.getName() + "]");
                }
            }
        }
        */

        return ancestorNode;
    }

    private static void addChildren(final BarcodeGraphNode parent, final LocationGraphModel graph) {

        for (final IEdge edge : graph.getEdges()) {
            if ((new BarcodeGraphNode((LocationNode) edge.getHead()).equals(parent))) {
                final BarcodeGraphNode child = new BarcodeGraphNode((LocationNode) edge.getTail());
                if (!parent.contains(child)) {
                    parent.addChild(child);
                    GraphModelBean.addChildren(child, graph);
                }
            }
        }
    }

    private static IEdge getParent(final Set<IEdge> edges, final IEdge edge) {
        for (final IEdge myEdge : edges) {
            if (myEdge.getTail() == edge.getHead()) {
                return myEdge;
            }
        }
        return null;
    }
}
