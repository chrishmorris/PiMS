/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

/**
 * This should probably be reference data in the db?
 * @author ian
 */
public enum ScreenType {
    Matrix ("Sparse Matrix Screen"),
    Optimisation ("Optimisation Screen"),
    Additive ("Additive Screen");
    
    private final String name;  
    ScreenType(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
