package org.pimslims.graph;

import java.util.Map;

/**
 * Implement this when you want your object to become a node of a IGraph Primary purpose - to provide a node
 * information for AT&T Graphviz dot.exe and java Grapha library for GraphViz
 * 
 * @author Ekaterina Pilicheva
 * @since March 2006
 */

public interface INode {

    /**
     * 
     * @return a String id - identifies of the node
     */
    public abstract String getId();

    /**
     * getter for the Map of attributes that are set to this particular instance of IGraphElement
     * 
     * @return Map of attributes specific this instance of IGraphElement
     */
    public abstract Map getAttributes();

    /**
     * INode.getCluster
     * 
     * @return null if this node is in no cluster, label of cluster otherwise
     */
    public abstract String getCluster();

}
