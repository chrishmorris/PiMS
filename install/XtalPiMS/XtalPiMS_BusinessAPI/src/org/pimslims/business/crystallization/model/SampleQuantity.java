/**
 * 
 */
package org.pimslims.business.crystallization.model;

import org.pimslims.business.core.model.Sample;


/**
 * <p>Simple class to combine a Sample with a quantity.</p>
 * 
 * @author Jon Diprose
 */
public class SampleQuantity extends Quantity {
    
    private final Sample sample;

    /**
     * Create a new SampleQuantity with the specified info
     * 
     * @param sample
     * @param quantity
     * @param unit
     */
    public SampleQuantity(Sample sample, double quantity, String unit) {
        super(quantity, unit);
        this.sample = sample;
    }
    
    /**
     * @return the sample
     */
    public Sample getSample() {
        return sample;
    }
    
}
