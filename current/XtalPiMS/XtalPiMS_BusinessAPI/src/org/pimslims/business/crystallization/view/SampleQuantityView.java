/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business.crystallization.view;

import net.sf.json.JSONObject;


/**
 *
 * @author ian
 */
public class SampleQuantityView extends SampleView {
    public static final String PROP_SAMPLE_QUANTITY = "sampleQuantity";
    
    /**
     * combination of sampleQuantity.getQuantity() + sampleQuantity.getUnit().
     */
    private String quantity;

    public SampleQuantityView() {
        
    }
    
    /**
     * combination of sampleQuantity.getQuantity() + sampleQuantity.getUnit().
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
        int result = super.hashCode();
        result = prime * result
                + ((quantity == null) ? 0 : quantity.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SampleQuantityView other = (SampleQuantityView) obj;
        if (quantity == null) {
            if (other.quantity != null)
                return false;
        } else if (!quantity.equals(other.quantity))
            return false;
        return true;
    }

    public JSONObject toJSON() {
        
        JSONObject obj = super.toJSON();
        obj.put(PROP_SAMPLE_QUANTITY, this.getQuantity());
        
        return obj;
    }
}
