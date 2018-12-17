/**
 * pims-web org.pimslims.presentation ModelObjectTreeBean.java
 * 
 * @author Marc Savitsky
 * @date 12 Sep 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation;

import java.util.Comparator;
import java.util.TreeSet;

import org.pimslims.metamodel.ModelObject;

/**
 * ModelObjectTreeBean
 */
public class ModelObjectBeanTree extends TreeSet implements Comparator, Comparable<ModelObjectBeanTree> {

    private final ModelObjectBean modelObjectBean;

    public ModelObjectBeanTree(final ModelObject object) {
        this.modelObjectBean = new ModelObjectBean(object);
        this.clear();
    }

    public ModelObjectBean getBean() {
        return this.modelObjectBean;
    }

    public String getName() {
        return this.modelObjectBean.getName();
    }

    /**
     * ModelObjectTreeBean.compare
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(final Object o1, final Object o2) {
        return ((ModelObjectBeanTree) o1).getName().compareTo(((ModelObjectBeanTree) o2).getName());
    }

    /**
     * ModelObjectTreeBean.compareTo
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final ModelObjectBeanTree other) {
        return (this.modelObjectBean.getName()).compareTo((other).getName());
    }

    @Override
    public boolean equals(final Object obj) {

        ModelObjectBean bean = null;

        if (obj instanceof ModelObject) {
            bean = new ModelObjectBean((ModelObject) obj);
        }
        if (obj instanceof ModelObjectBean) {
            bean = (ModelObjectBean) obj;
        }
        if (obj instanceof ModelObjectBeanTree) {
            final ModelObjectBeanTree modelObjectTreeBean = (ModelObjectBeanTree) obj;
            bean = modelObjectTreeBean.getBean();
        }

        final Long l = new Long(bean.getDbId());
        if (this.hashCode() == l.intValue()) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final Long l = new Long(this.modelObjectBean.getDbId());
        return l.intValue();
    }

}
