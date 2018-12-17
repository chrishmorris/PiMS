/*
 * Component.java
 *
 * Created on 01 May 2007, 10:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

import net.sf.json.JSONObject;

import org.pimslims.business.XtalObject;

/**
 * <p>
 * A class to describe a chemical component used in a Condition that makes up a Crystallization Screen
 * </p>
 * <p>
 * Although this will not be strictly necessary for every condition, they are likely to be stored in the
 * database as reference data, so are unlikely to be edited very often.
 * </p>
 * <p>
 * A component could be included in multiple Screen Conditions
 * </p>
 * <p>
 * A component will have its own safety information, which may or may not affect the safety information of the
 * Condition
 * </p>
 * 
 * @author IMB
 */
public class Crystal extends XtalObject {

    private static final Object PROP_BARCODE = "barcode";

    private static final Object PROP_WELL = "well";

    private static final Object PROP_R = "r";

    private static final Object PROP_X = "x";

    private static final Object PROP_Y = "y";

    private static final Object PROP_NUM = "num";

    private String barcode = "";

    private WellPosition wellPosition = null;

    private Integer x = null;
    
    private Integer y = null;
    
    private Integer r = null;
    
    private Integer num = null;
    
    /**
     * Creates a new instance of a Component
     */
    public Crystal() {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Crystal other = (Crystal) obj;
        return (this.hashCode() == other.hashCode());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash += 17 * ((int) (this.id ^ (this.id >>> 32)));
        hash += 53 * (this.barcode != null ? this.barcode.hashCode() : 0);
        hash += 19 * (this.wellPosition != null ? this.wellPosition.hashCode() : 0);
        hash += 5 * (this.x != null ? this.x.hashCode() : 0);
        hash += 23 * (this.y != null ? this.y.hashCode() : 0);
        hash += 11 * (this.r != null ? this.r.hashCode() : 0);
        // num intentionally not included
        return hash;
    }

    /**
     * @return Returns the barcode.
     */
    public String getBarcode() {
        return this.barcode;
    }

    /**
     * @param barcode The barcode to set.
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * @return Returns the wellPosition.
     */
    public WellPosition getWellPosition() {
        return this.wellPosition;
    }

    /**
     * @param wellPosition The wellPosition to set.
     */
    public void setWellPosition(WellPosition wellPosition) {
        this.wellPosition = wellPosition;
    }

    /**
     * @return Returns the x.
     */
    public Integer getX() {
        return this.x;
    }

    /**
     * @param x The x to set.
     */
    public void setX(Integer x) {
        this.x = x;
    }

    /**
     * @return Returns the y.
     */
    public Integer getY() {
        return this.y;
    }

    /**
     * @param y The y to set.
     */
    public void setY(Integer y) {
        this.y = y;
    }

    /**
     * @return Returns the r.
     */
    public Integer getR() {
        return this.r;
    }

    /**
     * @param r The r to set.
     */
    public void setR(Integer r) {
        this.r = r;
    }

    /**
     * @return Returns the num.
     */
    public Integer getNum() {
        return this.num;
    }

    /**
     * @param num The num to set.
     */
    public void setNum(Integer num) {
        this.num = num;
    }

    public JSONObject toJSON() {
        final JSONObject obj = new JSONObject();

        obj.put(PROP_BARCODE, this.getBarcode());
        obj.put(PROP_WELL, this.getWellPosition().toString());
        obj.put(PROP_X, this.getX());
        obj.put(PROP_Y, this.getY());
        obj.put(PROP_R, this.getR());
        obj.put(PROP_NUM, this.getNum());
        obj.put(PROP_ID, this.getId());
        return obj;
    }

}
