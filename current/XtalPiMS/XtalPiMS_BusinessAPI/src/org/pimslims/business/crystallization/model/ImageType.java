/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

/**
 * Note: not the same as PiMS ImageType
 * 
 * @author ian
 */
public enum ImageType {
    COMPOSITE, SLICE, ZOOMED, UV;

    public static ImageType getImageType(String name) {
        ImageType[] values = values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].name().equalsIgnoreCase(name)) {
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
        return name().toLowerCase();
    }

}
