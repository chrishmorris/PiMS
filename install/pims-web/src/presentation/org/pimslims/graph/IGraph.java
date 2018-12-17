package org.pimslims.graph;

import java.util.Collection;
import java.util.Set;

/**
 * Implement this when you want your object to become a subgraph a IGraph Primary purpose - to provide a
 * Subgraph information for AT&T Graphviz dot.exe and java Grapha library for GraphViz
 * 
 * @see IGraphElement
 * @author Ekaterina Pilicheva
 * @since March 2006
 */
public interface IGraph {

    /**
     * getter for the set of nodes that belong to this subgraph
     * 
     * @return set of nodes
     * @see INode
     */
    public abstract Collection<INode> getNodes();

    /**
     * getter for the set of edges that belong to this subgraph
     * 
     * @return set of edges
     * @see IEdge
     */
    public abstract Set<IEdge> getEdges();

}
