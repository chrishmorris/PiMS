package org.pimslims.crystallization.xml;

import java.util.Collection;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.pimslims.business.crystallization.view.ComponentQuantityView;
import org.pimslims.business.crystallization.view.ConditionView;

@XmlRootElement(name = "screen")
public class ConditionXMLBean {

    String solutionId;

    String localNum;

    private Collection<ComponentXMLBean> components;

    /**
     * @return Returns the solutionId.
     */
    @XmlElement
    public String getSolutionId() {
        return solutionId;
    }

    /**
     * @return Returns the localNum.
     */
    @XmlElement
    public String getLocalNum() {
        return localNum;
    }

    /**
     * @return Returns the components.
     */
    @XmlElementWrapper(name = "components")
    @XmlElement(name = "component")
    public Collection<ComponentXMLBean> getComponents() {
        return components;
    }

    public ConditionXMLBean() {
        super();
    }

    public ConditionXMLBean(final ConditionView conditionView) {
        this.localNum = conditionView.getLocalNumber();
        this.solutionId = conditionView.getManufacturerCode();
        components = new LinkedList<ComponentXMLBean>();
        for (final ComponentQuantityView component : conditionView.getComponents()) {
            components.add(new ComponentXMLBean(component));
        }
    }

}
