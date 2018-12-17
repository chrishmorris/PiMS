/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

/**
 *
 * @author ian
 */
public class TubePosition {
    Tube tube = null;
    WellPosition wellPosition = null;

    public TubePosition() {
    }

    public Tube getTube() {
        return tube;
    }

    public void setTube(Tube tube) {
        this.tube = tube;
    }

    public WellPosition getWellPosition() {
        return wellPosition;
    }

    public void setWellPosition(WellPosition wellPosition) {
        this.wellPosition = wellPosition;
    }
    
    
    
}
