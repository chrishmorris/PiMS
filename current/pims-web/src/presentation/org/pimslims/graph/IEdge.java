package org.pimslims.graph;

import java.util.Map;

/**
 * Interface to be implemented by objects that want to be edges (links) of IGraph
 * 
 * @author Ekaterina Pilicheva
 * @since 03.04.2006
 * 
 * @see IGraph, INode, ISubgraph
 */
public interface IEdge {

    /**
     * getter for head of edge
     * 
     * @return INode - a node
     */
    public abstract INode getHead();

    /**
     * getter for tail of edge
     * 
     * @return INode - a node that
     */
    public abstract INode getTail();

    /**
     * IEdge.getAttributes
     * 
     * @return
     */
    public abstract Map<String, String> getAttributes();

}
