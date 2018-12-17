/**
 * pims-web org.pimslims.presentation ComplexBean.java
 * 
 * @author Marc Savitsky
 * @date 10 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky * *
 * 
 */
package org.pimslims.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.pimslims.presentation.target.ResearchObjectiveElementBean;

/**
 * ComplexBean
 * 
 */
public class ComplexBean extends ModelObjectBean implements Serializable {

    private String blueprintHook;

    private String labNotebookHook;

    private String whyChosen;

    private String details;

    private Collection<ResearchObjectiveElementBeanI> components = new ArrayList<ResearchObjectiveElementBeanI>();

    public ComplexBean() {
        // Empty Constructor
    }

    public static final Comparator BLUEPRINT_ORDER = new Comparator() {
        public int compare(final Object o1, final Object o2) {
            final ComplexBean c1 = (ComplexBean) o1;
            final ComplexBean c2 = (ComplexBean) o2;
            return c1.getBlueprintHook().compareTo(c2.getBlueprintHook());
        }
    };

    @Override
    public boolean equals(final Object obj) {

        if (!(obj instanceof ComplexBean)) {
            throw new ClassCastException("obj1 is not a ComplexBean! ");
        }

        final String argHook = ((ComplexBean) obj).getBlueprintHook();
        final String thisHook = this.getBlueprintHook();
        if (thisHook.equals(argHook)) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.getBlueprintHook().hashCode();
    }

    public void setBlueprintHook(final String hook) {
        this.blueprintHook = hook;
    }

    public String getBlueprintHook() {
        return this.blueprintHook;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setlabNotebookHook(final String hook) {
        this.labNotebookHook = hook;
    }

    public String getLabNotebookHook() {
        return this.labNotebookHook;
    }

    public void setWhyChosen(final String whyChosen) {
        this.whyChosen = whyChosen;
    }

    public String getWhyChosen() {
        return this.whyChosen;
    }

    public void setDetails(final String details) {
        this.details = details;
    }

    public String getDetails() {
        return this.details;
    }

    //TODO make this private
    public void addComponent(final ResearchObjectiveElementBeanI componentBean) {
        this.components.add(componentBean);
    }

    /* no, this will fail if two different domains are included as different components
    public void removeComponent(final BlueprintComponentBean component) {
        for (final BlueprintComponentBean bean : this.components) {
            if (component.getTarget() == bean.getTarget()) {
                this.components.remove(bean);
            }
        }
    } */

    public Collection<ResearchObjectiveElementBeanI> getComponents() {
        final List<ResearchObjectiveElementBeanI> sortedComponents =
            new ArrayList<ResearchObjectiveElementBeanI>(this.components);
        Collections.sort(sortedComponents, ResearchObjectiveElementBean.NAME_ORDER);
        return sortedComponents;
    }

    public void setComponents(final Collection<ResearchObjectiveElementBeanI> components) {
        this.components = components;
    }

    /**
     * Does this complex contain this Target?
     * 
     * @param target
     * @return
     */
    public boolean containsTarget(final ModelObjectShortBean target) {
        for (final ResearchObjectiveElementBeanI bean : this.components) {
            if (target != null && target.getName().equals(bean.getTargetName())) {
                return true;
            }
        }
        return false;
    }

/*
    public boolean containsTarget(final Target target) {
        for (final BlueprintComponentBean bean : this.components) {
            if (bean.getTarget().getName().equals(target.getName())) {
                return true;
            }
        }
        return false;
    } */

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("ComplexName " + this.name);
        sb.append("\n whyChosen " + this.whyChosen);
        sb.append("\n details " + this.details);
        sb.append("\nComponents:" + this.components.size() + ":");
        if (!this.components.isEmpty()) {
            for (final ResearchObjectiveElementBeanI component : this.components) {
                sb.append(component.getProteinName());
            }
        }
        return sb.toString();
    }
}
