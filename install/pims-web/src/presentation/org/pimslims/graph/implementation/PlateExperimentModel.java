package org.pimslims.graph.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.graph.GraphFormat;
import org.pimslims.graph.IEdge;
import org.pimslims.graph.IGraph;
import org.pimslims.graph.INode;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.presentation.plateExperiment.PlateExperimentUtility;

/**
 * Represents a graph of plate experiments *
 * 
 * @author cm65
 * 
 */
public class PlateExperimentModel implements IGraph {

    /**
     * The nodes of the graph.
     */
    private final Map<Object, ModelObjectNode> nodes = new HashMap<Object, ModelObjectNode>();

    private Map<String, String> graphAttributes = new HashMap<String, String>();

    private ModelObjectNode getNode(final ModelObject object) {
        if (!this.nodes.containsKey(object)) {
            this.nodes.put(object, new ModelObjectNode(object));
        }
        return this.nodes.get(object);
    }

    /**
     * The links
     */
    private final Set<org.pimslims.graph.IEdge> links = new LinkedHashSet<org.pimslims.graph.IEdge>();

    private final Set<ExperimentGroup> groups = new LinkedHashSet<ExperimentGroup>();

    /**
     * Finds the graph the covers the experiments and samples provided. Applications will usually prefer to
     * use one of the convenience constructors below.
     * 
     * @param version the current transactions
     * @param experiments
     * @param samples
     */
    public @SuppressWarnings("unchecked")
    PlateExperimentModel(final ExperimentGroup group) {
        this.complete(group);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.ISubgraphModel#getEdges()
     */
    public Set<IEdge> getEdges() {
        return new LinkedHashSet<IEdge>(this.links);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.IGraphModel#getSubgraphs()
     */
    public Set<IGraph> getSubgraphs() {
        return Collections.EMPTY_SET;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.ISubgraph#getNodesAttributes()
     */
    public Map getNodesAttributes() {
        return GraphFormat.getGraphNodeMap();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.IGraphElement#getAttributes()
     */
    public Map getAttributes() {
        return this.graphAttributes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.IGraphElement#getGraph()
     */
    public IGraph getGraph() {
        return null;
    }

    private final ModelObjectLink newLink(final ModelObject from, final ModelObject to) {
        final ModelObjectNode head = this.getNode(from);
        final ModelObjectNode tail = this.getNode(to);
        final ModelObjectLink edge = new ModelObjectLink(head, tail);
        this.links.add(edge);
        return edge;
    }

    /**
     * Follows the associations to populate the whole graph.
     * 
     */
    @SuppressWarnings("unchecked")
    private void complete(final ExperimentGroup group) {
        final List<ExperimentGroup> groupsToProcess = new ArrayList<ExperimentGroup>();
        groupsToProcess.add(group);
        while (!groupsToProcess.isEmpty()) {
            /*
             * Loop invariant: For all g in this.groups there is a node For all g,h in this.groups there is a
             * link, if appropriate No g in groupsToProcess is in this.groups
             */
            final ExperimentGroup current = groupsToProcess.get(0);
            this.getNode(current);
            final Set<ExperimentGroup> nextGroups = PlateExperimentUtility.getNextPlateExperiments(current);
            for (final Iterator iter = nextGroups.iterator(); iter.hasNext();) {
                final ExperimentGroup next = (ExperimentGroup) iter.next();
                if (this.groups.contains(next)) {
                    this.newLink(current, next);
                } else {
                    groupsToProcess.add(next);
                }
            }
            final Set<ExperimentGroup> previousGroups =
                PlateExperimentUtility.getPreviousPlateExperiments(current);
            for (final Iterator iter = previousGroups.iterator(); iter.hasNext();) {
                final ExperimentGroup previous = (ExperimentGroup) iter.next();
                if (this.groups.contains(previous)) {
                    this.newLink(previous, current);
                } else {
                    groupsToProcess.add(previous);
                }
            }
            this.groups.add(current);
            groupsToProcess.remove(current);
        }
    }

    public Collection<INode> getNodes() {
        return new LinkedHashSet<INode>(this.nodes.values());
    }

    @Deprecated
    public void setNodes(final Set<INode> nodesSet) {
        throw new UnsupportedOperationException("immutable");
    }

    @Deprecated
    public void setEdges(final Set<IEdge> edgesCollection) {
        throw new UnsupportedOperationException("immutable");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.IGraphModel#setSubgraphs(java.util.List)
     */
    @Deprecated
    public void setSubgraphs(final Set<IGraph> subgraphCollection) {
        throw new UnsupportedOperationException("immutable");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.ISubgraph#addEdge(org.pimslims.utils.graph.IEdge)
     */
    @Deprecated
    public void addEdge(final IEdge edge) {
        throw new UnsupportedOperationException("immutable");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.ISubgraph#addNode(org.pimslims.utils.graph.INode)
     */
    @Deprecated
    public void addNode(final INode node) {
        throw new UnsupportedOperationException("immutable");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.ISubgraph#setEdgesAttributes(java.util.Map)
     */
    @Deprecated
    public void setEdgesAttributes(final Map egdesAttributes) {
        throw new UnsupportedOperationException("immutable");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.ISubgraph#setNodesAttributes(java.util.Map)
     */
    @Deprecated
    public void setNodesAttributes(final Map nodesAttributes) {
        throw new UnsupportedOperationException("immutable");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.IGraphElement#setAttribute(java.lang.String, java.lang.String)
     */
    @Deprecated
    public void setAttribute(final String attName, final String attValue) {
        if (this.graphAttributes == null) {
            this.graphAttributes = new HashMap<String, String>();
        }
        if (attName != null) {
            this.graphAttributes.put(attName, attValue);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.IGraphElement#resetAttributes(java.util.Map)
     */
    @Deprecated
    public void resetAttributes(final Map<String, String> attributeMap) {
        if (this.graphAttributes == null) {
            this.graphAttributes = new HashMap<String, String>(attributeMap);
        } else {
            if (attributeMap != null && !attributeMap.isEmpty()) {
                this.graphAttributes.clear();
                this.graphAttributes.putAll(attributeMap);
            }

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.IGraphElement#addAttributes(java.util.Map)
     */
    @Deprecated
    public void addAttributes(final Map<String, String> attributeMap) {
        if (this.graphAttributes == null) {
            this.graphAttributes = new HashMap<String, String>(attributeMap);
        } else {
            if (attributeMap != null && !attributeMap.isEmpty()) {
                this.graphAttributes.putAll(attributeMap);
            }

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.IGraphElement#setGraph(org.pimslims.utils.graph.ISubgraph)
     */
    @Deprecated
    public void setGraph(final IGraph parentGraph) {
        throw new UnsupportedOperationException("immutable");
    }

}
