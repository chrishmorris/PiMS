/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

/**
 * ComponentQuantity
 * 
 * @author ian
 */
public class ComponentQuantity {

    private double quantity;

    private String units;

    private Component component = null;

    private String componentType = "";

    private String displayUnit;

    public ComponentQuantity() {
    }

    public ComponentQuantity(final double quantity, final String units) {
        this.quantity = quantity;
        this.units = units;
    }

    public ComponentQuantity(final Component component, final double quantity, final String units) {
        this.quantity = quantity;
        this.units = units;
        this.component = component;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ComponentQuantity other = (ComponentQuantity) obj;
        if (this.quantity != other.quantity) {
            return false;
        }
        if (this.units != other.units && (this.units == null || !this.units.equals(other.units))) {
            return false;
        }
        if (this.component != other.component
            && (this.component == null || !this.component.equals(other.component))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int hash = 7;
        return hash;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(final double quantity) {
        this.quantity = quantity;
    }

    public String getUnits() {
        return units;
    }

    /**
     * ComponentQuantity.setUnits
     * 
     * @param units kg/m3,M,m3/m3,mol/mol,kg/kg
     */
    public void setUnits(final String units) {
        this.units = units;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(final Component component) {
        this.component = component;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(final String componentType) {
        this.componentType = componentType;
    }

    public void setDisplyUnit(final String displayUnit) {
        this.displayUnit = displayUnit;

    }

    public String getDisplayUnit() {
        return displayUnit;
    }

    public void processUnit(final String concentrationUnit) {
        String unit = "M";
        String displayUnit = "M";
        if (concentrationUnit.toLowerCase().contains("v/v")) {
            unit = "m3/m3";
            displayUnit = "%v/v";
        } else if (concentrationUnit.toLowerCase().contains("w/v")) {
            unit = "kg/m3";
            displayUnit = "%w/v";
        } else if (concentrationUnit.toLowerCase().contains("w/w")) {
            unit = "kg/kg";
            displayUnit = "%w/w";
        } else if (concentrationUnit.trim().equalsIgnoreCase("M")) {
            unit = "M";
            displayUnit = "M";
        } else if (concentrationUnit.trim().equalsIgnoreCase("%")) {
        	// Rhombix uses "%", probably means v/v
        	 unit = "m3/m3";
             displayUnit = "%v/v";
        } else {
            throw new IllegalArgumentException("Unknown concentrationUnit: " + concentrationUnit);
        }
        this.setUnits(unit);
        this.setDisplyUnit(displayUnit);
    }

}
