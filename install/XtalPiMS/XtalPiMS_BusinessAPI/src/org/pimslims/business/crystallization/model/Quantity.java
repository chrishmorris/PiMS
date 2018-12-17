package org.pimslims.business.crystallization.model;

public abstract class Quantity {

    /**
     * The quantity
     */
    private final double quantity;
    
    /**
     * The units of the quantity
     */
    private final String unit;
    
    /**
     * Constructor that sets the quantity and unit
     * 
     * @param quantity
     * @param unit
     */
    protected Quantity(double quantity, String unit) {
        if (quantity < 0d) {
            throw new IllegalArgumentException("quantity must >= 0");
        }
        this.quantity = quantity;
        this.unit = unit;
    }

    /**
     * @return the quantity
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

}