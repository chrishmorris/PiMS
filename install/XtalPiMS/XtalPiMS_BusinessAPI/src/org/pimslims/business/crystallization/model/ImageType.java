/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

/**
 * TODO add URL to this, the PiMS table contains this TODO make this no longer an enum, others may be defined
 * in database
 * 
 * @author ian
 */
public enum ImageType {
    COMPOSITE, SLICE, ZOOMED;

    public static ImageType getImageType(String name) {
        ImageType[] values = values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].name().equals(name)) {
                return values[i];
            }
        }
        return null;
    }

    /**
     * ImageType.toString
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return name();
    }

}
