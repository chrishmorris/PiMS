/**
 * 
 */
package org.pimslims.presentation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.Note;
import org.pimslims.presentation.bioinf.DbRefBean;
import org.pimslims.presentation.mru.MRUController;
import org.pimslims.presentation.servlet.utils.ValueFormatter;

/**
 * A bean representing a PiMS record for use in a JSP, containing enough information to display and edit
 * attributes and single roles.
 * 
 * It is designed to be used as a base class for more specific bean classes.
 * 
 * @author cm65
 * 
 */
public class ModelObjectBean extends ModelObjectShortBean {

    protected Map<String, Object> values = new HashMap<String, Object>();

    private final Map<String, Map<String, String>> mru = new HashMap<String, Map<String, String>>();

    // finding attachments is expensive
    //protected final Collection<File> files;

    @Deprecated
    // finding attachments is expensive
    private final List<NoteBean> notes;

    @Deprecated
    // finding attachments is expensive
    private final List<DbRefBean> externalDbLinks;

    private ModelObjectShortBean creator;

    private ModelObjectShortBean access;

    protected ModelObjectBean() {
        super();
        this.notes = null;
        this.externalDbLinks = null;
        this.creator = null;
        this.access = null;
    }

    /**
     * @param modelObject
     */
    public ModelObjectBean(final ModelObject modelObject) {

        super(modelObject);

        this.processAttributes(modelObject);

        // process the roles
        final MetaClass metaClass = modelObject.get_MetaClass();
        final Map<String, MetaRole> roles = metaClass.getMetaRoles();
        for (final Iterator iter = roles.values().iterator(); iter.hasNext();) {
            final MetaRole role = (MetaRole) iter.next();
            if (1 != role.getHigh()) {
                continue; // LATER make a ModelObjectFullBean which has all
                // roles
            }
            final Collection<ModelObject> associates = modelObject.get(role.getRoleName());
            if (0 == associates.size()) {
                this.values.put(role.getRoleName(), null);
            } else {
                final ModelObjectShortBean bean = new ModelObjectShortBean(associates.iterator().next());
                this.values.put(role.getRoleName(), bean);
            }
            this.mru.put(role.getRoleName(), MRUController.getPossibleMRUItems(modelObject.get_Version(),
                role.getOtherMetaClass().getJavaClass(), modelObject.get_Hook(), role.isRequired()));
        }

        //process the notes        
        if (modelObject instanceof LabBookEntry) {
            final LabBookEntry entry = (LabBookEntry) modelObject;
            this.notes = new ArrayList<NoteBean>(entry.getNotes().size());
            for (final Iterator iterator = entry.getNotes().iterator(); iterator.hasNext();) {
                final Note note = (Note) iterator.next();
                this.notes.add(new NoteBean(note));
            }
        } else {
            this.notes = Collections.EMPTY_LIST;
        }

        //process the externalDbLinks        
        if (modelObject instanceof LabBookEntry) {
            final LabBookEntry entry = (LabBookEntry) modelObject;
            this.externalDbLinks = new ArrayList<DbRefBean>(entry.getExternalDbLinks().size());
            for (final Iterator iterator = entry.getExternalDbLinks().iterator(); iterator.hasNext();) {
                final ExternalDbLink externalDbLink = (ExternalDbLink) iterator.next();
                this.externalDbLinks.add(new DbRefBean(externalDbLink.getDatabaseName(), externalDbLink));
            }
        } else {
            this.externalDbLinks = Collections.EMPTY_LIST;
        }
    }

    protected void processAttributes(final ModelObject modelObject) {
        if (modelObject instanceof LabBookEntry && null != ((LabBookEntry) modelObject).getCreator()) {
            this.creator = new ModelObjectShortBean(((LabBookEntry) modelObject).getCreator());
        } else {
            this.creator = null;
        }

        if (modelObject instanceof LabBookEntry && null != ((LabBookEntry) modelObject).getAccess()) {
            this.access = new ModelObjectShortBean(((LabBookEntry) modelObject).getAccess());

        } else {
            this.access = null;
        }

        final ValueFormatter vf = new ValueFormatter(modelObject);

        this.metaClass = modelObject.get_MetaClass();
        // process the attributes
        for (final Iterator iter = this.metaClass.getAttributes().keySet().iterator(); iter.hasNext();) {
            final String attrName = (String) iter.next();
            Object value = null;
            final Class type = this.metaClass.getAttribute(attrName).getType();
            value = modelObject.get_Value(attrName); // keep it as a Calendar
            if (List.class.isInstance(value)) {
                value = vf.getFormatedValue(attrName);
            } else if (Calendar.class == type) {
                // no conversion required
            } else {
                value = vf.getFormatedValue(attrName);
            }
            // System.out.println(this.getClass().getName() + ": " + attrName + ": " + value);
            this.values.put(attrName, value);
        }
    }

    public Map<String, Object> getValues() {
        return this.values;
    }

    /**
     * ModelObjectBean.getMru
     * 
     * @return role name => (hook => name) for recently used possible associates
     */
    public Map<String, Map<String, String>> getMru() {
        return this.mru;
    }

    /**
     * Allows a custom bean to hide an attribute
     * 
     * @param attributeName the key to remove
     */
    protected void removeValue(final String attributeName) {
        this.values.remove(attributeName);
    }

    /**
     * Allows a custom bean to add a derived attribute
     * 
     * @param attributeName the key to remove
     */
    protected void putValue(final String attributeName, final String value) {
        this.values.put(attributeName, value);
    }

    public static <T extends ModelObject> List<ModelObjectBean> getModelObjectBeans(
        final Collection<T> objects) {
        final List<ModelObjectBean> ret = new ArrayList<ModelObjectBean>(objects.size());
        for (final Iterator iter = objects.iterator(); iter.hasNext();) {
            final ModelObject object = (ModelObject) iter.next();
            if (null == object) {
                continue; // cant view this one
            }
            ret.add(BeanFactory.newBean(object));
        }
        Collections.sort(ret);
        return ret;
    }

    /**
     * ModelObjectBean.getNotes
     * 
     * @return
     */
    public List<NoteBean> getNotes() {
        return this.notes;
    }

    /**
     * ModelObjectBean.getExternalDbLinks
     * 
     * @return
     */
    public List<DbRefBean> getExternalDbLinks() {
        return this.externalDbLinks;
    }

    /**
     * ModelObjectBean.getElements
     * 
     * @return
     */
    public Collection<MetaAttribute> getElements() {
        return this.metaClass.getAttributes().values();
    }

    public ModelObjectShortBean getAccess() {
        return this.access;
    }

    /**
     * ModelObjectBean.setAccess
     * 
     * @param modelObjectShortBean for TargetBean
     */
    public void setAccess(final ModelObjectShortBean modelObjectShortBean) {
        this.access = modelObjectShortBean;

    }

    public ModelObjectShortBean getCreator() {
        return this.creator;
    }

    /**
     * ModelObjectBean.setCreator
     * 
     * @param modelObjectShortBean for TargetBean
     */
    protected void setCreator(final ModelObjectShortBean modelObjectShortBean) {
        this.creator = modelObjectShortBean;

    }

    /**
     * For subclasses that have a zero argument constructor ModelObjectBean.setMetaClass
     * 
     * @param metaClass2
     */
    protected void setMetaClass(final MetaClass metaClass2) {
        this.metaClass = metaClass2;
    }

}
