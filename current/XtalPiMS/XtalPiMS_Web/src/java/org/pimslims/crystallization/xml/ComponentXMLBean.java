package org.pimslims.crystallization.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.pimslims.business.crystallization.view.ComponentQuantityView;

@XmlType(name = "", propOrder = { "name", "type", "concentration", "concentrationUnits", "pH" })
public class ComponentXMLBean {
    String name;

    String type;

    String concentration;

    String concentrationUnits;

    String pH;

    /**
     * @return Returns the name.
     */
    @XmlAttribute
    public String getName() {
        return name;
    }

    /**
     * @return Returns the type.
     */
    @XmlAttribute
    public String getType() {
        return type;
    }

    /**
     * @return Returns the concentration.
     */
    @XmlAttribute
    public String getConcentration() {
        return concentration;
    }

    /**
     * @return Returns the concentrationUnits.
     */
    @XmlAttribute
    public String getConcentrationUnits() {
        return concentrationUnits;
    }

    /**
     * @return Returns the pH.
     */
    @XmlAttribute
    public String getpH() {
        return pH;
    }

    public ComponentXMLBean() {
    }

    public ComponentXMLBean(final ComponentQuantityView componentView) {
        this.name = componentView.getComponentName();
        this.type = componentView.getComponentType();
        this.concentration = componentView.getQuantity().split(" ")[0];
        this.concentrationUnits = componentView.getQuantity().split(" ")[1];
        if (name.contains("pH=")) {
            name = componentView.getComponentName().split("pH=")[0];
            pH = componentView.getComponentName().split("pH=")[1];
        }
        this.name = componentView.getComponentName();
    }

}
