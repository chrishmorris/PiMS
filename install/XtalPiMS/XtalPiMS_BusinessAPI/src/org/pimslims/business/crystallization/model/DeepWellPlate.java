/*
 * Screen.java
 *
 * Created on 17 April 2007, 13:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pimslims.business.crystallization.model;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Represents a deep well plate containing a crystallization
 * screen. Essentially it wraps the ConditionPosition information
 * in an {@link org.pimslims.crystallization.model.Screen},
 * adding a minimal amount of physical instance information of its
 * own, e.g. the barcode.</p>
 * 
 * @author CM
 * @see org.pimslims.crystallization.model.Screen
 */
public class DeepWellPlate {

    /**
     * <p>The barcode of this DeepWellPlate.</p>
     */
    private final String barcode;
    /**
     * <p>The Screen wrapped by this DeepWellPlate.</p>
     */
    private final Screen screen;

    /**
     * <p>To track, roughly, when this deep well plate was created (or when 
     * it came it in to the lab)</p>
     */
    private Calendar createDate;
    /**
     * <p>We also need to keep track of when the deep well plate was destroyed (we 
     * might in the future to know what crystallization plates were set up with
     * this deep well plate.</p>
     */
    private Calendar destroyDate;
    /**
     * <p>In some labs, a deep well plate may not be made accessible to users until
     * it is activated, so in a GUI, for example only deep well plates that are 
     * activated should be listed in the form for creating a new plate - 
     * alternatively, this will indicate the date of first use of the plate.</p>
     */
    private Calendar activationDate;
    
    /**
     * Provides a map of the quantities of each condition 
     */
    private Map<WellPosition, ConditionQuantity> conditions = new HashMap<WellPosition, ConditionQuantity>();
    
    
    /**
     * @param barcode
     * @param screen
     */
    public DeepWellPlate(String barcode, Screen screen) {
        this.barcode = barcode;
        this.screen = screen;
    }

    /**
     * @return Returns the barcode.
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * @return Returns the screen.
     */
    public Screen getScreen() {
        return screen;
    }

    /**
     * <p>Get the Map of Position => Condition from the underlying
     * Screen, returning an empty map if the screen has not been
     * set.</p>
     * 
     * @return The Map of Position => Condition
     */
    public Map<WellPosition, Condition> getConditionPositions() {
        if (null == this.screen) {
            return Collections.emptyMap();
        }
        return this.screen.getConditionPositions();
    }

    /**
     * <p>Get the Condition for the specified WellPosition from the
     * underlying Screen, returning a null if the screen has not been
     * set.</p>
     * 
     * @param wellPosition 
     * @return The Condition at the specified WellPosition
     */
    public Condition getCondition(WellPosition wellPosition) {
        if (null == this.screen) {
            return null;
        }
        return this.screen.getCondition(wellPosition);
    }

    public Calendar getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Calendar createDate) {
        this.createDate = createDate;
    }

    public Calendar getDestroyDate() {
        return destroyDate;
    }

    public void setDestroyDate(Calendar destroyDate) {
        this.destroyDate = destroyDate;
    }

    public Calendar getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Calendar activationDate) {
        this.activationDate = activationDate;
    }

    public Map<WellPosition, ConditionQuantity> getConditions() {
        return conditions;
    }

    public void setConditions(Map<WellPosition, ConditionQuantity> conditions) {
        this.conditions = conditions;
    }
}
