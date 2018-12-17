/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business.crystallization.model;


/**
 *
 * @author ian
 */
public class ConditionPosition {
    private WellPosition wellPosition = null;
    private Condition condition = null;

    public WellPosition getWellPosition() {
        return wellPosition;
    }

    public void setWellPosition(WellPosition wellPosition) {
        this.wellPosition = wellPosition;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
    
}
