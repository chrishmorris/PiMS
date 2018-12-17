package org.pimslims.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.metamodel.AbstractModelObject;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Containable;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.sample.Sample;

/**
 * A bean representing a PiMS record for use in a JSP, containing enough information to make a link to the
 * view page.
 * 
 * @author cm65
 * 
 */
public class ModelObjectShortBean implements Comparable<Object> {

    //TODO this should be an interface not a class, eg: for constructBean

    private String hook;

    protected String name;

    private String classDisplayName;

    private Map<String, String> extraProperties;

    protected String extraActions;

    private Long dbId;

    private boolean mayDelete;

    private boolean mayUpdate;

    protected MetaClass metaClass;

    protected boolean isPlate = false;

    public ModelObjectShortBean() {
        //empty bean 
        this.dbId = null;
        this.mayDelete = false;
        this.mayUpdate = false;
        this.extraProperties = new HashMap();
        this.extraActions = "";
        this.metaClass = null;
        this.name = "";
    }

    public ModelObjectShortBean(final ModelObject modelObject) {
        assert modelObject != null;
        this.name = "";
        this.init(modelObject);
        if (modelObject instanceof AbstractHolder) {
            final AbstractHolder holder = (AbstractHolder) modelObject;
            this.isPlate = HolderFactory.isPlate(holder.getHolderType());
        }
    }

    protected void init(final ModelObject modelObject) {
        this.metaClass = modelObject.get_MetaClass();
        this.hook = modelObject.get_Hook();
        this.dbId = ((AbstractModelObject) modelObject).getDbId();
        this.mayDelete = modelObject.get_MayDelete();
        this.mayUpdate = modelObject.get_MayUpdate();

        this.name = modelObject.get_Name();
        if (null == this.name) {
            this.name = "";
        }
        // PIMSServlet translate the metaClass to display name
        this.classDisplayName = ServletUtil.getDisplayName(this.metaClass);

        final Map props = new HashMap();
        if (modelObject instanceof Sample) {
            final Sample s = (Sample) modelObject;
            if (s != null && s.getIsActive() != null) {
                props.put("Active", s.getIsActive() ? "Yes" : "No");
            }
        }
        this.extraProperties = props;
        this.extraActions = this.setExtraContextMenuActions(modelObject);
    }

    public String getClassName() {
        return this.getMetaClass().getMetaClassName();
    }

    public String getHook() {
        return this.hook;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return Returns the metaClass.
     */
    public MetaClass getMetaClass() {
        return this.metaClass;
    }

    /**
     * @return the classDisplayName
     */
    public String getClassDisplayName() {
        return this.classDisplayName;
    }

    @Override
    public String toString() {
        return this.getClassDisplayName() + ": " + this.getName();
    }

    /**
     * For sorting into alphabetical order of name
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final Object other) {
        if (!(other instanceof ModelObjectShortBean)) {
            throw new IllegalArgumentException("Cant compare bean to: " + other.getClass().getName());
        }
        return this.getName().compareTo(((ModelObjectShortBean) other).getName());
    }

    /**
     * @return
     */
    public Long getDbId() {
        return this.dbId;
    }

    /**
     * @param objects a collection of ModelObjects
     * @return beans representing them, in alphabetical order of name
     */
    public static <T extends ModelObject> List<ModelObjectShortBean> getModelObjectShortBeans(
        final Collection<T> objects) {
        final List<ModelObjectShortBean> ret = new ArrayList<ModelObjectShortBean>(objects.size());
        for (final Iterator iter = objects.iterator(); iter.hasNext();) {
            final ModelObject object = (ModelObject) iter.next();
            ret.add(new ModelObjectShortBean(object));
        }
        Collections.sort(ret);
        return ret;
    }

    /**
     * ModelObjectShortBean.getMayDelete
     * 
     * @return
     */
    public boolean getMayDelete() {
        return this.mayDelete;
    }

    public boolean getMayUpdate() {
        return this.mayUpdate;
    }

    /**
     * ModelObjectShortBean.getBeans
     * 
     * @param <T>
     * @param objects
     * @return beans representing the model objects, sorted by name
     */
    public static <T extends ModelObject> List<ModelObjectShortBean> getBeans(final Collection<T> objects) {
        final List<ModelObjectShortBean> ret = ModelObjectShortBean.getBeansInOriginalOrder(objects);
        Collections.sort(ret);
        return ret;
    }

    public static <T> List<ModelObjectShortBean> getBeansInOriginalOrder(final Collection<T> objects) {
        final List<ModelObjectShortBean> ret = new ArrayList<ModelObjectShortBean>(objects.size());
        for (final Iterator iter = objects.iterator(); iter.hasNext();) {
            final ModelObject object = (ModelObject) iter.next();
            ret.add(new ModelObjectShortBean(object));
        }
        return ret;
    }

    /**
     * ModelObjectShortBean.getMenu
     * 
     * @return
     */
    public String getMenu() {
        String ret =
            "properties:[ { property:\'Name\', val:contextMenuName}" + this.getExtraContextMenuProperties()
                + " ],\r\n" + "actions:[ {text:\'View " + this.getClassDisplayName()
                + "\', icon:\'actions/view.gif\', url:'/View/" + this.getHook() + "' } ";

        //TODO PIMS-3740 need specific delete for Construct, Experiment, etc
        if (this.getMayDelete()) {
            ret += this.extraActions;
            ret +=
                ",{text:'Delete', icon:'actions/delete.gif',\r\n onclick:'contextmenu_delete(contextMenuName,\\'"
                    + this.getHook() + "\\')',\r\n url:'/Delete/" + this.getHook() + "' }";
        }
        if (this.getMayUpdate()) {
            ret +=
                ",{text:'Rename', icon:'actions/edit.gif',\r\n onclick:'contextmenu_rename(\\\'"
                    + this.getHook() + "\\\');return false',\r\n url:'/View/" + this.getHook() + "' }";
        }
        return ret + "]";
    }

    /**
     * 
     * ModelObjectShortBean.getExtraProperties
     * 
     * @return A String representing the properties. For each: ,{ property:\'nameOfProperty\',
     *         val:\'valueOfProperty\'}
     */
    protected String getExtraContextMenuProperties() {
        String ret = "";
        //iterate through properties map
        //make JSON
        final Iterator i = this.extraProperties.entrySet().iterator();
        while (i.hasNext()) {
            final Map.Entry entry = (Map.Entry) i.next();
            ret += ",{ property:\'" + entry.getKey() + "\', val:\'" + entry.getValue() + "\'}";

        }
        return ret;
    }

    /**
     * 
     * ModelObjectShortBean.getExtraActions
     * 
     * @return a String representing the actions. For each: ,{text:'linkText', icon:'actions/delete.gif',
     *         onclick:'onClickAction', url:'destinationURL' }
     */
    private String setExtraContextMenuActions(final ModelObject modelObject) {
        String ret = "";
        //do instanceof check and make appropriate action JSON for each

        if (modelObject instanceof Containable) {
            final Containable s = (Containable) modelObject;
            if (null != s.getContainer()) {
                ret +=
                    ",{text:'Remove', icon:'actions/next.gif',\r\n onclick:'submitRemove('"
                        + ModelObjectShortBean.escapeJS(modelObject.get_Name()) + "',\\'" + this.getHook()
                        + "\\')',\r\n url:'/Move/" + this.getHook() + "' }";
            }
        }

        if (modelObject instanceof Organism) {
            final Organism o = (Organism) modelObject;
            ret +=
                ",{text:'View NCBI record', icon:'actions/view.gif', url:'http://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?id="
                    + o.getNcbiTaxonomyId() + "' }";
        } else if (modelObject instanceof Sample) {
            final Sample s = (Sample) modelObject;
            ret +=
                ",{text:'Use in new experiment', icon:'actions/create/experiment.gif', url:'/Create/org.pimslims.model.experiment.Experiment?_Sample="
                    + s.get_Hook() + "' }";
        } else if (modelObject instanceof ExperimentGroup) {
            final ExperimentGroup e = (ExperimentGroup) modelObject;
            ret += ",{text:'Diagram', icon:'actions/viewdiagram.gif', url:'/Graph/" + e.get_Hook() + "' }";
        } else if (modelObject instanceof UserGroup) {
            ret +=
                ",{text:'Permissions',  url:'/access/Permission?userGroup=" + modelObject.get_Hook() + "' }";
        } else if (modelObject instanceof LabNotebook) {
            ret += ",{text:'Permissions',  url:'/access/Permission?owner=" + modelObject.get_Hook() + "' }";
        }

        return ret;
    }

    /**
     * For ConstructBeanReader ModelObjectShortBean.setHook
     * 
     * @param hook
     */
    protected void setHook(final String hook) {
        this.hook = hook;
    }

    public boolean getIsPlate() {
        return this.isPlate;
    }

    /**
     * Utils.escapeJS
     * 
     * @param string
     * @return
     */
    public static String escapeJS(final String string) {
        final String ret = string.replace("\\", "\\\\").replace("'", "\\'").replace("\"", "&quot;");
        return ret;
    }

}
