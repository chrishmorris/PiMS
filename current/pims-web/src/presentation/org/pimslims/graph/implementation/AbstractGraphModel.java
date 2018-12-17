/**
 * 
 */
package org.pimslims.graph.implementation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.graph.GraphFormat;
import org.pimslims.graph.IEdge;
import org.pimslims.graph.IGraph;
import org.pimslims.graph.INode;
import org.pimslims.metamodel.AbstractModelObject;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.sample.Sample;
import org.pimslims.properties.PropertyGetter;

/**
 * @author katya
 * 
 */
public abstract class AbstractGraphModel implements IGraph {

    protected final Map<Long, ModelObjectNode> nodes = new HashMap();

    public ModelObjectNode getNode(final Long dbId) {
        return this.nodes.get(dbId);
    }

    protected final Set<IEdge> edges = new LinkedHashSet();

    Map<String, String> edgesAttributes = null;

    public static final int MAX_NODES = 30;

    public static final Map<String, String> GRAPH_ATTRIBUTES = new HashMap<String, String>();
    {
        AbstractGraphModel.GRAPH_ATTRIBUTES.put("bgcolor", "#ccccccff");
        AbstractGraphModel.GRAPH_ATTRIBUTES.put("center", "true");
        AbstractGraphModel.GRAPH_ATTRIBUTES.put("packMode", "clust");

        // no, map does not match image AbstractGraphModel.GRAPH_ATTRIBUTES.put("rankdir", "LR"); 

        // if the mode is neato:
        AbstractGraphModel.GRAPH_ATTRIBUTES.put("mode", "heir");
        AbstractGraphModel.GRAPH_ATTRIBUTES.put("diredgeconstraints", "true");
        AbstractGraphModel.GRAPH_ATTRIBUTES.put("levelsgap", "0.5");

        // if sfdp
        AbstractGraphModel.GRAPH_ATTRIBUTES.put("smoothing", "true");

        AbstractGraphModel.GRAPH_ATTRIBUTES.put("weight", "2"); // default weight of edges
        AbstractGraphModel.GRAPH_ATTRIBUTES.put("center", "true");
        AbstractGraphModel.GRAPH_ATTRIBUTES.put("splines", "spline");

        AbstractGraphModel.GRAPH_ATTRIBUTES.put("ratio", "expand"); // or compress, but may be too small to read  
        // not fill, because map does not match image
        AbstractGraphModel.GRAPH_ATTRIBUTES.put("overlap", "false"); // true, false, fill, compress

        //AbstractGraphModel.GRAPH_ATTRIBUTES.put("color", "#888888ff:#000000ff"); // edges from grey to black
    }

    public static final Map<String, String> SUB_GRAPH_ATTRIBUTES = new HashMap<String, String>();
    {
        AbstractGraphModel.SUB_GRAPH_ATTRIBUTES.put("color", "#ffffffff");
        AbstractGraphModel.SUB_GRAPH_ATTRIBUTES.put("style", "filled,radial");
        AbstractGraphModel.SUB_GRAPH_ATTRIBUTES.put("gradientangle", "0");
        AbstractGraphModel.SUB_GRAPH_ATTRIBUTES.put("labelloc", "t");
        AbstractGraphModel.SUB_GRAPH_ATTRIBUTES.put("labeljust", "l");
        AbstractGraphModel.SUB_GRAPH_ATTRIBUTES.put("margin", "0.11,0.055");
    }

    protected int edgeCount = 0;

    protected final List<Long> dbIds = new ArrayList<Long>();

    protected final ModelObject centre;

    protected final int maxNodes;

    /**
     * TODO add String urlContext
     */
    public AbstractGraphModel(final ModelObject centre) {
        this(centre, AbstractGraphModel.MAX_NODES);
    }

    /**
     * Constructor for AbstractGraphModel
     * 
     * @param centre2
     * @param maxNodes2
     */
    public AbstractGraphModel(final ModelObject centre2, final int maxNodes2) {
        this.centre = centre2;
        this.maxNodes = maxNodes2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.ISubgraph#getNodes()
     */
    public Collection<INode> getNodes() {
        return new LinkedHashSet(this.nodes.values());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.ISubgraph#getEdges()
     */
    public Set<IEdge> getEdges() {

        return this.edges;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.ISubgraph#addNode(org.pimslims.utils.graph.INode)
     */
    public ModelObjectNode addNode(final ModelObject object) {
        final ModelObjectNode node = new ModelObjectNode(object);
        this.nodes.put(((AbstractModelObject) object).getDbId(), node);
        if (object == this.centre) {
            node.setIsCentre();
        }
        return node;

    }

    //TODO make this void
    public abstract IGraph createGraphModel(ModelObject object);

    protected ModelObjectNode addChild(final INode parent, final ModelObject child) {
        final ModelObjectNode childNode = this.addNode(child);
        final IEdge edge = new DefaultModelEdge(parent, childNode);
        this.getEdges().add(edge);
        return childNode;
    }

    protected INode doAddParent(final INode child, final ModelObject parent) {
        final INode compNode = this.addNode(parent);
        final IEdge edge = new DefaultModelEdge(compNode, child);
        this.getEdges().add(edge);
        return compNode;
    }

    protected int addEdge(final INode from, final INode to) {
        final IEdge edge = new DefaultModelEdge(from, to);
        this.getEdges().add(edge);
        return this.edgeCount;
    }

    protected int getWeight(final Experiment experiment) {
        int weight = DefaultModelEdge.STANDARD;
        if ("Failed".equals(experiment.getStatus())) {
            weight = DefaultModelEdge.UNIMPORTANT;
        } else if ("OK".equals(experiment.getStatus())) {
            weight = DefaultModelEdge.IMPORTANT;
        }
        return weight;
    }

    /**
     * AbstractGraphModel.getNode
     * 
     * @param object
     * @return
     */
    ModelObjectNode getNode(final ModelObject object) {

        if (!(object instanceof LabBookEntry)) {
            // Subclass should override this method
            throw new AbstractMethodError("This diagram is not designed to represent a: "
                + object.getClass().getName());
        }

        final LabBookEntry page = (LabBookEntry) object;

        if (!this.nodes.containsKey(page.getDbId())) {
            final String classname = page.get_MetaClass().getShortName();

            final ModelObjectNode node = new ModelObjectNode(page);
            if (page == this.centre) {
                node.setIsCentre();
            }
            if (classname.equals(ModelObjectNode.CLASSNAME_SAMPLE)) {
                final File image = PropertyGetter.getFileProperty("sample_icon", "diagram/sample.gif");
                node.setAttribute("image", image.getAbsolutePath());
                node.setAttribute("label", "");
                node.setAttribute("style", "");
                this.nodes.put(page.getDbId(), node);
                final Sample sample = (Sample) page;
                if (1 == sample.getSampleCategories().size()) {
                    node.setCluster(sample.getSampleCategories().iterator().next().getName());
                }
            } else if (classname.equals(ModelObjectNode.CLASSNAME_EXPERIMENT)) {
                node.setAttribute(GraphFormat.DOT_FILLCOLOR, "#ffff0055");
                final Experiment experiment = (Experiment) page;
                if (null != experiment.getProtocol()) {
                    node.setCluster(experiment.getProtocol().getName());
                }
                // colour by status. Gradients reversed, to give a cue to colourblind users.
                if ("Failed".equals(experiment.getStatus())) {
                    node.setAttribute(GraphFormat.DOT_FILLCOLOR, "#ffffff55:#ff000055");
                } else if ("OK".equals(experiment.getStatus())) {
                    node.setAttribute(GraphFormat.DOT_FILLCOLOR, "#00ff0055:#ffffff55");
                }
                this.nodes.put(page.getDbId(), node);
            } else {
                this.nodes.put(page.getDbId(), node);
            }
        }
        return this.nodes.get(page.getDbId());
    }

    protected final ModelObjectLink newInputLink(final LabBookEntry from, final LabBookEntry to) {
        final ModelObjectNode head = this.getNode(from);
        final ModelObjectNode tail = this.getNode(to);
        int weight = DefaultModelEdge.STANDARD;
        if (to instanceof Experiment) {
            weight = this.getWeight((Experiment) to);
        }
        return new ModelObjectLink(head, tail, weight);
    }

    protected final ModelObjectLink newOutputLink(final Experiment experiment, final Sample sample) {
        final ModelObjectNode head = this.getNode(experiment);
        final ModelObjectNode tail = this.getNode(sample);
        final int weight = this.getWeight(experiment);
        return new ModelObjectLink(head, tail, weight);
    }

    protected int getMaxNodes() {
        return this.maxNodes;
    }

    protected void flagNode(final ModelObject object) {
        //System.out.println("flagNode [" + object.get_Name() + "]");
        final ModelObjectNode node = this.nodes.get(((LabBookEntry) object).getDbId());
        if (null == node) {
            return;
        }
        node.setExtensible();
        this.nodes.remove(((LabBookEntry) object).getDbId());
        this.nodes.put(((LabBookEntry) object).getDbId(), node);
    }

    protected Set<ModelObjectLink> getLinks() {
        return new LinkedHashSet<ModelObjectLink>((Collection) this.edges);
    }

}
