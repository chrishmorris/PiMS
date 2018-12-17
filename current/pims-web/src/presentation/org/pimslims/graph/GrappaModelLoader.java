package org.pimslims.graph;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.pimslims.graph.implementation.AbstractGraphModel;

import att.grappa.Edge;
import att.grappa.Element;
import att.grappa.Graph;
import att.grappa.GrappaConstants;
import att.grappa.Node;
import att.grappa.Subgraph;

// import org.apache.commons.logging.Log;
// import org.apache.commons.logging.LogFactory;
/**
 * class loads IGraphModel interface - implementing data model into Grappa data model structure and creates
 * input data for dot.exe Grappa library is a java library for creating graphs with GraphViz see
 * www.graphviz.org and www.research.att.com/~john/Grappa/
 * 
 * 
 * @author Ekaterina Pilicheva
 * @since 25.04.2006
 */

public class GrappaModelLoader {

    // map that keeps track of created all Nodes using keys - id of INode,
    // values - Node objects

    private final Map<Object, Node> nodeMap = new HashMap();

    // IGraphModel - a data model
    private final IGraph graphModel;

    private final Graph mainGraph = new Graph("Graph", true, true);

    private int edge = 1; // to make unique keys

    private final Map<String, Subgraph> clusters = new HashMap();

    public static final String ErrorMessage_EmptyGraph = "Diagram is empty";

    /**
     * Constructor for GrappaModelLoader
     * 
     * @param graph_model PiMS graph model
     * @param width desired width in pixels
     * @param height desired height in pixels
     */
    public GrappaModelLoader(final IGraph graph_model, final float width, final float height) {
        super();
        this.graphModel = graph_model;
        final Graph graph = this.mainGraph;
        if (this.graphModel.getNodes().isEmpty()) {
            graph.setAttribute("label", GrappaModelLoader.ErrorMessage_EmptyGraph);
        }
        final Map<String, String> attr = AbstractGraphModel.GRAPH_ATTRIBUTES;
        for (final Iterator iterator = attr.entrySet().iterator(); iterator.hasNext();) {
            final Map.Entry entry = (Map.Entry) iterator.next();
            graph.setAttribute((String) entry.getKey(), entry.getValue());
        }

        // default node attributes for all nodes in the subgraph

        final Map nodeAttrMap = GraphFormat.getGraphNodeMap();
        if (nodeAttrMap != null && !nodeAttrMap.isEmpty()) {
            final Set attrNameSet = nodeAttrMap.keySet();
            for (final Iterator nameIter = attrNameSet.iterator(); nameIter.hasNext();) {
                final String attrName = nameIter.next().toString();
                graph.setNodeAttribute(attrName, nodeAttrMap.get(attrName));
            }
        }

        // create nodes
        this.setSubgraphNodes(this.graphModel);
        // create edges
        this.setSubgraphEdges(this.graphModel);

        // Graphviz expects measurements in inches, but assumes 96dpi
        graph.setAttribute("size", "" + (width / 96f) + "," + (height / 96f) + "!");
        // note we must do this after adding subgraphs, the "!" breaks them

    }

    private void setSubgraphAttributes(final Subgraph subgraph, final Map attributes) {
        for (final Iterator iterator = attributes.entrySet().iterator(); iterator.hasNext();) {
            final Map.Entry entry = (Map.Entry) iterator.next();
            subgraph.setAttribute((String) entry.getKey(), entry.getValue());
        }
    }

    /**
     * Constructor for testing GrappaModelLoader
     * 
     * @param model
     */
    public GrappaModelLoader(final IGraph model) {
        this(model, 800, 1024);
    }

    /**
     * method creates stream data for DOT.EXE from data of graph model
     * 
     * @return input data for dot.exe
     * @throws GraphModelException
     */
    public String produceData() {

        final Writer output = new StringWriter();
        this.mainGraph.printGraph(output);
        return output.toString();
    }

    /**
     * creates a new Node object
     * 
     * @see att.grappa.Node for the graph and returns updated graph
     * @param graph - a subgraph
     * @param inode
     * @return
     * @throws GraphModelException
     */
    private void addNode(final INode inode) {

        assert null != inode;

        //System.out.println("GrappaModelLoader.addNode [" + graph.getName() + ":" + inode.getId() + "]");

        // create a new node
        final String id = inode.getId();
        if (!this.nodeMap.containsKey(id)) {
            final Node node = new Node(this.mainGraph, id.toString());
            node.setAttribute(GraphFormat.DOT_ID, id);
            // get instance - specific attributes from inode
            final Map attrMap = inode.getAttributes();

            // set instance - attributes to node
            if (attrMap != null && !attrMap.isEmpty()) {
                final Set attrNameSet = attrMap.keySet();
                for (final Iterator nameIter = attrNameSet.iterator(); nameIter.hasNext();) {
                    final String attrName = nameIter.next().toString();
                    Element.setUserAttributeType(attrName, GrappaConstants.STRING_TYPE);
                    final String string = (String) attrMap.get(attrName);
                    node.setAttribute(attrName, GrappaModelLoader.escape(string));
                    //System.out.println("GrappaModelLoader.attribute [" + attrName + ":" + string + ":"
                    //    + escape(string) + "]");
                }
            }
            final String clusterLabel = inode.getCluster();
            if (null != clusterLabel) {
                if (!this.clusters.containsKey(clusterLabel)) {
                    final Subgraph subgraph = new Subgraph(this.mainGraph);
                    subgraph.setName("cluster " + clusterLabel);
                    this.setSubgraphAttributes(subgraph, AbstractGraphModel.SUB_GRAPH_ATTRIBUTES);
                    subgraph.setAttribute("label", clusterLabel);
                    this.mainGraph.addSubgraph(subgraph);
                    this.clusters.put(clusterLabel, subgraph);
                }
                final Subgraph cluster = this.clusters.get(clusterLabel);
                cluster.addNode(node);
            }
            this.nodeMap.put(id, node);
            assert !this.mainGraph.hasEmptySubgraphs();
        }
    }

    private static final Pattern SLASH = Pattern.compile("\\\\");

    private static final Pattern QUOTE = Pattern.compile("\"");

    private static final Pattern NEWLINE = Pattern.compile("\n");

    /**
     * dot accepts strings in two formats, but grappa only support the less tractable of the two.
     * 
     * We must change: \ => \\ newline => \n " => \"
     * 
     * See also DotServlet, which has to reverse some escaping
     * 
     * @param string
     * @return
     */
    static String escape(final String string) {
        final String doubled = GrappaModelLoader.SLASH.matcher(string).replaceAll("\\\\\\\\");
        //String quoted = QUOTE.matcher(doubled).replaceAll("\\\\\"");
        final String quoted = GrappaModelLoader.QUOTE.matcher(doubled).replaceAll("&#034;");
        return GrappaModelLoader.NEWLINE.matcher(quoted).replaceAll(" ");
    }

    /**
     * creates a new Edge and adds it to graph
     * 
     * @param graph -
     * @see att.grappa.Subgraph parent subgraph for the newly created edge
     * @param iedge -
     * @see org.pimslims.utils.graph.IEdge
     * @return an updated
     * @throws GraphModelException
     */
    private void addEdge(final Subgraph graph, final IEdge iedge) {
        System.out.println(iedge); //TODO remove
        assert null != iedge;

        // find existing head and tail nodes in nodeMap or generate exception if
        // not found

        final INode head = iedge.getHead();
        final INode tail = iedge.getTail();
        if (head == null || !this.nodeMap.containsKey(head.getId())) {
            throw new IllegalStateException("head is null or can not be found for edge ");
        }
        if (tail == null || !this.nodeMap.containsKey(tail.getId())) {
            throw new IllegalStateException("tail is null or can not be found for edge ");
        }
        final Node headNode = this.nodeMap.get(head.getId());
        final Node tailNode = this.nodeMap.get(tail.getId());

        //System.out.println("GrappaModelLoader.addEdge idege [" + graph.getName() + ":" + iedge.getId() + ":"
        //    + headNode.getName() + ":" + tailNode.getName() + "]");

        // exclude edges already in the graph 
        // nb. compare head/tail and tail/head
        final Enumeration en = graph.edgeElements();
        while (en.hasMoreElements()) {
            final Edge ed = (Edge) en.nextElement();
            if (headNode.getName().equals(ed.getTail().getName())
                && tailNode.getName().equals(ed.getHead().getName())) {
                return;
            }
        }

        final Edge edge = new Edge(graph, headNode, tailNode, "edge" + this.edge++);
        final Map attrMap = iedge.getAttributes();
        if (attrMap != null && !attrMap.isEmpty()) {
            final Set attrNameSet = attrMap.keySet();
            for (final Iterator nameIter = attrNameSet.iterator(); nameIter.hasNext();) {
                final String attrName = nameIter.next().toString();
                Element.setUserAttributeType(attrName, GrappaConstants.STRING_TYPE);
                edge.setAttribute(attrName, attrMap.get(attrName));
            }
        }
        return;
    }

    /**
     * creates nodes for a subgraph
     * 
     * @param graph
     * @param model
     * @return
     * @throws GraphModelException
     */

    private void setSubgraphNodes(final IGraph model) {

        final Collection<INode> inodes = model.getNodes();
        for (final Iterator<INode> inodeIter = inodes.iterator(); inodeIter.hasNext();) {
            final INode inode = inodeIter.next();
            this.addNode(inode);
        }
        return;
    }// End of setSubgraphNodes

    /**
     * creates edges for a subgraph
     * 
     * @param graph
     * @param model
     * @return
     * @throws GraphModelException
     */

    private void setSubgraphEdges(final IGraph model) {

        final Set<IEdge> iedges = model.getEdges();
        for (final Iterator<IEdge> iedgeIter = iedges.iterator(); iedgeIter.hasNext();) {
            final IEdge iedge = iedgeIter.next();
            this.addEdge(this.mainGraph, iedge);
        }

    }// End of setSubgraphEdges

}
