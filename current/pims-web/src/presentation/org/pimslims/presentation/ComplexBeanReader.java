/**
 * pims-web org.pimslims.presentation ComplexBeanReader.java
 * 
 * @author Marc Savitsky
 * @date 15 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky * *
 * 
 */
package org.pimslims.presentation;

import java.util.Collection;

import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.target.ResearchObjectiveElementBean;

/**
 * ComplexBeanReader
 * 
 */
public class ComplexBeanReader {

    public static ComplexBean readComplexBean(final ResearchObjective blueprint) {

        final ComplexBean bean = new ComplexBean();

        bean.setBlueprintHook(blueprint.get_Hook());
        bean.setName(blueprint.getLocalName());
        bean.setWhyChosen(blueprint.getWhyChosen());
        bean.setDetails(blueprint.getDetails());

        for (final ResearchObjectiveElement component : blueprint.getResearchObjectiveElements()) {
            final ResearchObjectiveElementBeanI componentBean = new ResearchObjectiveElementBean(component);
            bean.addComponent(componentBean);
        }

        return bean;
    }

    public static boolean isComplex(final ResearchObjective expBlueprint) {
        final Collection<ResearchObjectiveElement> components = expBlueprint.getResearchObjectiveElements();
        Target t1 = null;
        for (final ResearchObjectiveElement component : components) {
            /* was if (component.getComponentType().equals("complex")) {
                 return true; //complex could be defined by component type
             } */
            if (t1 == null) {
                t1 = component.getTarget();
            } else {
                final Target t2 = component.getTarget();
                if (t2 != null && (!t2.get_Hook().equals(t1.get_Hook()))) {
                    return true; //complex is ResearchObjective links with 2 different target though ResearchObjectiveElement
                }
            }
        }
        return false;
    }
}
