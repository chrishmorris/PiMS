/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.pimslims.business.crystallization.view;

import net.sf.json.JSONObject;

/**
 * 
 * @author ian
 */
public class ComponentQuantityView extends ComponentView {
    public static final String PROP_QUANTITY = "quantity";

    /**
     * cq.getQuantity() + cq.getUnits().
     */
    private String quantity;

    public ComponentQuantityView() {
    }

    /**
     * @param name
     * @param quantity
     * @param type
     * @param volatileComponent
     * @param casNumber
     * @param ph
     * @param imageURL
     * @param safetyFormId
     */
    public ComponentQuantityView(String name, String quantity, String type, boolean volatileComponent,
        String casNumber, float ph, String imageURL, Long safetyFormId) {
        super(name, type, volatileComponent, casNumber, ph, imageURL, safetyFormId);
        this.quantity = quantity;
    }

    public ComponentQuantityView(String name, String quantity, String type) {
        super(name, type);
        this.quantity = quantity;
    }

    /**
     * cq.getQuantity() + cq.getUnits().
     */
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getCasNumber() == null) ? 0 : this.getCasNumber().hashCode());
        result = prime * result + ((this.getImageURL() == null) ? 0 : this.getImageURL().hashCode());
        result =
            prime * result + ((this.getComponentName() == null) ? 0 : this.getComponentName().hashCode());
        result = prime * result + Float.floatToIntBits(this.getComponentPH());
        result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
        result = prime * result + ((this.getSafetyFormId() == null) ? 0 : this.getSafetyFormId().hashCode());
        result =
            prime * result + ((this.getComponentType() == null) ? 0 : this.getComponentType().hashCode());
        result = prime * result + (this.isVolatileComponent() ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ComponentQuantityView other = (ComponentQuantityView) obj;
        if (this.getCasNumber() == null) {
            if (other.getCasNumber() != null)
                return false;
        } else if (!this.getCasNumber().equals(other.getCasNumber()))
            return false;
        
        if (this.getComponentName() == null) {
            if (other.getComponentName() != null)
                return false;
        } else if (!getComponentName().equals(other.getComponentName()))
            return false;
        if (Float.floatToIntBits(this.getComponentPH()) != Float.floatToIntBits(other.getComponentPH()))
            return false;
        if (quantity == null) {
            if (other.quantity != null)
                return false;
        } else if (!quantity.equals(other.quantity))
            return false;

        return true;
    }

    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();
        obj.put(PROP_QUANTITY, this.getQuantity());

        return obj;
    }
}
