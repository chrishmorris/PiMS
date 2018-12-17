package org.pimslims.graph.implementation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.pimslims.graph.GraphFormat;
import org.pimslims.graph.INode;
import org.pimslims.lab.Utils;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.plateExperiment.PlateExperimentUtility;
import org.pimslims.presentation.servlet.utils.ValueFormatter;

/**
 * A graph node that represents a an object which is recorded in PIMS.
 * 
 */
public class ModelObjectNode implements INode {

    protected static final String NONE = "none";

    public static final String CLASSNAME_EXPERIMENT = Experiment.class.getSimpleName();

    public static final String CLASSNAME_SAMPLE = Sample.class.getSimpleName();

    public static final String CLASSNAME_BLUEPRINTCOMPONENT = ResearchObjectiveElement.class.getSimpleName();

    public static final String CLASSNAME_RESEARCHOBJECTIVE = ResearchObjective.class.getSimpleName();

    public static final String CLASSNAME_TARGET = Target.class.getSimpleName();

    public static final String CLASSNAME_EXPERIMENTGROUP = ExperimentGroup.class.getSimpleName();

    public static final String CLASSNAME_PROTOCOL = Protocol.class.getSimpleName();

    protected static final String DEFAULT_EXPERIMENT_LABEL = "Experiment";

    protected static final String DEFAULT_SAMPLE_LABEL = "Sample";

    protected static final String DEFAULT_EXPERIMENTTYPE_LABEL = "ExperimentType";

    // PiMS 2450
    //protected static final String DEFAULT_BLUEPRINTCOMPONENT_LABEL = "Full length";
    protected static final String DEFAULT_BLUEPRINTCOMPONENT_LABEL = "Default";

    // common logging log reference

    /**
     * the object this node represents
     */
    protected final org.pimslims.metamodel.ModelObject object;

    /**
     * attribute Map
     */
    protected Map<String, String> attrMap = new HashMap<String, String>();

    /**
     * is this node extensible
     */
    private boolean extensible = false;

    private String cluster;

    /**
     * @param object
     * 
     */
    public ModelObjectNode(final org.pimslims.metamodel.ModelObject object) {
        this.object = object;
        this.initAttributes();

        // was this.setAttribute(GraphFormat.DOT_URL, "../View/" + object.get_Hook());

        final String menu = new ModelObjectShortBean(object).getMenu();
        this.setAttribute(GraphFormat.DOT_URL, "javascript:contextMenuName='" + object.get_Name()
            + "'; showContextMenu(document.getElementById('" + this.getId() + "'),{ " + menu + " })");
        // Graphviz urlescapes, so the servlet that serves the map has to restore the apostrophes

        this.cluster = null;

    }

    protected void initAttributes() {
        if (this.attrMap == null) {
            this.attrMap = new HashMap<String, String>();
        }
        this.initDefaultAttributes();

        return;
    }

    private void initDefaultAttributes() {
        this.setAttribute(GraphFormat.DOT_LABEL, this.getLabel());
        // was this.setAttribute(GraphFormat.DOT_STYLE, "filled");
        this.setAttribute(GraphFormat.DOT_FILLCOLOR, "#bfefff55");
        this.setAttribute(GraphFormat.DOT_TOOLTIP, this.getComment());
        this.setAttribute(GraphFormat.DOT_FONTSIZE, "12");
        final String shape = this.getShape();
        if (shape != null) {
            this.setAttribute(GraphFormat.DOT_SHAPE, shape);
        }
    }

    /**
     * @return Returns the object which this node represents
     */
    public org.pimslims.metamodel.ModelObject getObject() {
        return this.object;
    }

    // the following methods are not implemented, because this class is
    // immutable

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.IGraphElement#getId()
     */
    public String getHook() {
        return this.get_Hook();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.IGraphElement#getId()
     */
    public final String getId() {
        return this.get_Hook();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.IGraphElement#getAttribute(java.lang.String)
     */
    public String getAttribute(final String attributeName) {
        return this.attrMap.get(attributeName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.graph.IGraphElement#getAttributes()
     */
    public Map getAttributes() {
        return this.attrMap;
    }

    /*
     * (non-Javadoc)
     * @see org.pimslims.utils.graph.IGraphElement#setAttribute(java.lang.String, java.lang.String)
     */
    void setAttribute(final String attName, final String attValue) {
        // if(!attrMap.containsKey(attName)){
        this.attrMap.put(attName, attValue);
        // }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.ModelObject#getHook()
     */
    public String get_Hook() {
        return this.object.get_Hook();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.ModelObject#getMayDelete()
     */
    public boolean get_MayDelete() {
        return this.object.get_MayDelete();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.ModelObject#getMayUpdate()
     */
    public boolean get_MayUpdate() {
        return this.object.get_MayUpdate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.ModelObject#getMetaClass()
     */
    public MetaClass getMetaClass() {
        return this.object.get_MetaClass();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.ModelObject#getName()
     */
    public String getName() {
        return this.object.get_Name();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.ModelObject#getOwner()
     */
    public String getOwner() {
        return this.object.get_Owner();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.ModelObject#getValue(java.lang.String)
     */
    public Object getValue(final String attributeName) {
        return this.object.get_Value(attributeName);
    }

    protected final String getShape() {
        final String classname = this.getObject().get_MetaClass().getShortName();
        if (classname.equalsIgnoreCase(ModelObjectNode.CLASSNAME_EXPERIMENT)) {
            // do nothing, ellipse is a default shape for graph
        } else if (classname.equalsIgnoreCase(ModelObjectNode.CLASSNAME_SAMPLE)
            || SampleCategory.class.getSimpleName().equals(classname)) {
            return GraphFormat.DOT_SHAPE_DIAMOND;
        } else if (classname.equalsIgnoreCase(ModelObjectNode.CLASSNAME_RESEARCHOBJECTIVE)) {
            return GraphFormat.DOT_SHAPE_OCTAGON;
        } else if (classname.equalsIgnoreCase(ModelObjectNode.CLASSNAME_BLUEPRINTCOMPONENT)) {
            return GraphFormat.DOT_SHAPE_OCTAGON;
        } else if (classname.equalsIgnoreCase(ModelObjectNode.CLASSNAME_TARGET)) {
            return GraphFormat.DOT_SHAPE_INVHOUSE;
        } else if (classname.equalsIgnoreCase(ModelObjectNode.CLASSNAME_EXPERIMENTGROUP)) {
            return GraphFormat.DOT_SHAPE_RECTANGLE;
        } else if (classname.equalsIgnoreCase(ModelObjectNode.CLASSNAME_PROTOCOL)) {
            // do nothing, ellipse is a default shape for graph
        }

        return null;
    }

    // delegated methods, for client's convenience

    protected final String getLabel() {

        final String classname = this.getObject().get_MetaClass().getShortName();
        String label = this.object.get_Name();
        if (classname.equalsIgnoreCase(ModelObjectNode.CLASSNAME_SAMPLE)) {
            final Sample sample = (Sample) this.getObject();
            label = "";
            final Set<SampleCategory> sampleCategories = sample.getSampleCategories();
            for (final Iterator iterator = sampleCategories.iterator(); iterator.hasNext();) {
                final SampleCategory sampleCategory = (SampleCategory) iterator.next();
                label += sampleCategory.getName() + " ";
            }
            return label;
        }
        if (classname.equalsIgnoreCase(ModelObjectNode.CLASSNAME_EXPERIMENT)) {
            final Experiment experiment = (Experiment) this.getObject();
            label = experiment.getExperimentType().getName();
            return label;
        } else if (classname.equalsIgnoreCase(ModelObjectNode.CLASSNAME_BLUEPRINTCOMPONENT)) {
            final ResearchObjectiveElement blueprintComponent = (ResearchObjectiveElement) this.getObject();
            //try {
            final Integer beginId = blueprintComponent.getApproxBeginSeqId();
            final Integer endId = blueprintComponent.getApproxEndSeqId();
            if (beginId != null && endId != null) {
                label = "Res " + beginId.toString() + "-" + endId.toString();
            } else {
                label = ModelObjectNode.DEFAULT_BLUEPRINTCOMPONENT_LABEL;
            }

        }
        //System.out.println("ModelObjectNode.getLabel [" + classname + ":" + label + "]");
        return label;
    }

    protected String getComment() {
        final StringBuffer comment = new StringBuffer();
        final String classname = this.getObject().get_MetaClass().getShortName();
        if (classname != null) {
            if (classname.equalsIgnoreCase(ModelObjectNode.CLASSNAME_EXPERIMENT)) {
                this.addAttributeDescription(comment, "name");
                //this.addAttributeDescription(comment, "experimentType");
                this.addAttributeDescription(comment, Experiment.PROP_PROJECT);
                this.addAttributeDescription(comment, "status");
                this.addAttributeDescription(comment, "creator");
                //this.addAttributeDescription(comment, "startDate");
            } else if (classname.equalsIgnoreCase(ModelObjectNode.CLASSNAME_SAMPLE)) {
                this.addAttributeDescription(comment, "name");
                //this.addAttributeDescription(comment, AbstractSample.PROP_SAMPLECATEGORIES);
                this.addAttributeDescription(comment, Sample.PROP_HOLDER);
                this.addAttributeDescription(comment, "currentAmount");
                this.addAttributeDescription(comment, "amountDisplayUnit");
                //this.addAttributeDescription(comment, "creationDate");
            } else if (classname.equalsIgnoreCase(ModelObjectNode.CLASSNAME_RESEARCHOBJECTIVE)) {
                this.addAttributeDescription(comment, ResearchObjective.PROP_COMMONNAME);
                this.addAttributeDescription(comment, ResearchObjective.PROP_LOCALNAME);
                this.addAttributeDescription(comment, ResearchObjective.PROP_RESEARCHOBJECTIVEELEMENTS);
                //addAttributeDescription(attributes, roles, this.getObject(), comment, "details");
            } else if (classname.equalsIgnoreCase(ModelObjectNode.CLASSNAME_BLUEPRINTCOMPONENT)) {
                this.addAttributeDescription(comment, "componentType");
                this.addAttributeDescription(comment, "molecule");
                this.addAttributeDescription(comment, "target");
                this.addAttributeDescription(comment, ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE);
                //addAttributeDescription(attributes, roles, this.getObject(), comment, "details");
            } else if (classname.equalsIgnoreCase(ModelObjectNode.CLASSNAME_TARGET)) {
                this.addAttributeDescription(comment, "name");
                // this.addAttributeDescription(comment, "expBlueprint");
            }

            else if (classname.equalsIgnoreCase(ModelObjectNode.CLASSNAME_EXPERIMENTGROUP)) {
                final ExperimentGroup expGroup = (ExperimentGroup) this.getObject();
                final Protocol protocol = PlateExperimentUtility.getGroupProtocol(expGroup);
                if (null != protocol) {
                    comment.append("experimentType " + protocol.getExperimentType().getName() + ";");
                }
                if (null != expGroup.getStartDate()) {
                    comment.append(" startDate " + ValueFormatter.formatDate(expGroup.getStartDate()));
                }
            }
        }
        return comment.toString();
    }// End of getComment

    /*
     * 
     * 
     */
    protected StringBuffer addAttributeDescription(final StringBuffer buf, final String pimsAttrName) {

        final ModelObject modelObject = this.getObject();
        final Map attributes = modelObject.get_MetaClass().getAttributes();
        final Map roles = modelObject.get_MetaClass().getMetaRoles();

        if (attributes.containsKey(pimsAttrName)) {
            final MetaAttribute metaAttr = modelObject.get_MetaClass().getAttribute(pimsAttrName);
            final Object value = modelObject.get_Value(pimsAttrName);

            buf.append(" " + (metaAttr != null ? metaAttr.getAlias() : pimsAttrName) + " "
                + (value != null ? this.getValue(value) : ModelObjectNode.NONE) + ";");

        } else if (roles.containsKey(pimsAttrName)) {
            final MetaRole metaRole = modelObject.get_MetaClass().getMetaRole(pimsAttrName);
            final Collection col = modelObject.get(pimsAttrName);
            buf.append(" " + (metaRole != null ? metaRole.getAlias() : pimsAttrName) + " ");
            final StringBuffer value = new StringBuffer();
            for (final Iterator icol = col.iterator(); icol.hasNext();) {
                final Object obj = icol.next();
                if (obj instanceof ModelObject) {
                    value.append(((ModelObject) obj).get_Name() + " ");
                }
            }
            buf.append((value.length() > 0 ? value : ModelObjectNode.NONE) + ";");

        } else {
            throw new RuntimeException("Can not find property or role '" + pimsAttrName + "' for "
                + modelObject.get_Hook());
        }

        return buf;

    }// EndOf addAttributeDescription

    private String getValue(final Object object) {

        if (object instanceof java.util.Calendar) {
            return ValueFormatter.formatDate((java.util.Calendar) object);

        } else if (object instanceof java.util.Date) {
            return Utils.getDateFormat().format(((java.util.Date) object));

        } else {
            return object.toString();
        }
    }

    public void setExtensible() {
        this.setAttribute(GraphFormat.DOT_FILLCOLOR, "#0ff7f55");
        this.extensible = true;
    }

    /**
     * ModelObjectNode.setIsCentre Indicate that this node is the node of interest used to draw the graph TODO
     * we want this node to be in top layer, i.e. defined last in dot file
     */
    void setIsCentre() {
        this.setAttribute(GraphFormat.DOT_FILLCOLOR, "#ffffff55");
        this.setAttribute(GraphFormat.DOT_COLOR, "red");
        this.setAttribute("root", "true"); // circo or twopi
    }

    /**
     * is this node extensible outside the model
     * 
     * @return
     */
    public boolean isExtensible() {
        return this.extensible;
    }

    public String limitAttribute(final String attribute) {
        if (attribute.length() > 98) {
            return attribute.substring(0, 98) + "...";
        }
        return attribute;
    }

    public int compareTo(final Object object2) {
        return this.getLabel().compareTo(object2.toString());
    }

    /**
     * INode.getCluster
     * 
     * @see org.pimslims.graph.INode#getCluster()
     */
    @Override
    public String getCluster() {
        return this.cluster;
    }

    /**
     * ModelObjectNode.setCluster
     * 
     * @param name
     */
    public void setCluster(final String name) {
        this.cluster = name;
    }
}
