/**
 * 
 */
package org.pimslims.business.crystallization.model;



/**
 * <p>Simple class to combine a Condition with a quantity.</p>
 * 
 * @author Jon Diprose
 */
public class ConditionQuantity extends Quantity {
    
    

    /**
     * The Condition
     */
    private final Condition condition;
    
    /**
     * Create a new ConditionQuantity with the specified info
     * 
     * @param condition
     * @param quantity
     * @param unit
     */
    public ConditionQuantity(Condition condition, double quantity, String unit) {
        super(quantity, unit);
        this.condition = condition;
    }
    
    /**
     * @return the sample
     */
    public Condition getCondition() {
        return condition;
    }
    
}
