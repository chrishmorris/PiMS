package org.pimslims.presentation.mru;

import java.util.Map;

import org.pimslims.metamodel.ModelObject;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;

public class MRURoleChoice {
    String modelObject_hook = null; // the hook of model oject choose for

    String role_name = null; // the name of role to choose

    String role_currentHook = null; // the hook of current object of role

    String role_currentObjectName = null; // the name of current object of

    // role

    Map<String, String> possibleMRUItems = null; // possible choices from mru

    private ModelObjectBean currentBean;

    /**
     * for chooseFromMRU to choose from related MRUs,currentObject,[none]
     */
    public MRURoleChoice() {
        super();

    }

    public void setModelObject_hook(final String modelObject_hook) {
        this.modelObject_hook = modelObject_hook;
    }

    public void setPossibleMRUItems(final Map<String, String> possibleMRUItems) {
        this.possibleMRUItems = possibleMRUItems;
    }

    public void setRole_currentObject(final ModelObject object) {
        this.role_currentHook = object.get_Hook();
        this.currentBean = BeanFactory.newBean(object);
    }

    public void setRole_currentObjectName(final String role_currentObjectName) {
        this.role_currentObjectName = role_currentObjectName;
    }

    public void setRole_name(final String role_name) {
        this.role_name = role_name;
    }

    public String getModelObject_hook() {
        return this.modelObject_hook;
    }

    public Map<String, String> getPossibleMRUItems() {
        return this.possibleMRUItems;
    }

    public String getRole_currentHook() {
        return this.role_currentHook;
    }

    public String getRole_currentObjectName() {
        return this.role_currentObjectName;
    }

    public String getRole_name() {
        return this.role_name;
    }

    /**
     * MRURoleChoice.getRole_currentBean
     * 
     * @return
     */
    public ModelObjectBean getRole_currentBean() {
        return this.currentBean;
    }
}
