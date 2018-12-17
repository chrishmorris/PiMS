/**
 * current-pims-web org.pimslims.presentation ModelObjectUpdateBean.java
 * 
 * @author cm65
 * @date 3 Mar 2008
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2008 cm65 
 * 
 * 
 */
package org.pimslims.presentation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.pimslims.metamodel.ModelObject;

/**
 * ModelObjectUpdateBean
 * 
 */
public class ModelObjectUpdateBean extends ModelObjectBean implements UpdateBean {

    private final Map<String, Object> changed;

    /**
     * @param modelObject
     */
    public ModelObjectUpdateBean(ModelObject modelObject, Map<String, Object> changed) {
        super(modelObject);
        this.changed = new HashMap<String, Object>(changed.size());
        this.changed.putAll(changed);
    }

    /**
     * @return Returns the names and values of the properties that have been changed e.g.
     *         Experiment.PROP_STARTDATE => 2008/10/12
     */
    public Map<String, Object> getChanged() {
        return this.changed;
    }

    /**
     * @param editedObjects
     * @return
     */
    public static Set<ModelObjectUpdateBean> getBeans(Map<ModelObject, Map<String, Object>> updatedObjects) {
        Set<ModelObjectUpdateBean> ret = new HashSet<ModelObjectUpdateBean>();
        for (Iterator iterator = updatedObjects.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<ModelObject, Map<String, Object>> entry =
                (Map.Entry<ModelObject, Map<String, Object>>) iterator.next();
            ret.add(new ModelObjectUpdateBean(entry.getKey(), entry.getValue()));
        }
        return ret;
    }

}
