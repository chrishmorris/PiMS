/**
 * xtalPiMSApi org.pimslims.business.crystallization.model Imager.java
 * 
 * @author cm65
 * @date 28 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.business.crystallization.model;

/**
 * Imager
 * 
 * Represents the instrument (robot) that takes the images
 * No, actually we use Location
 */
@Deprecated 
public class Imager {

    private long id;

    private String name;

    private ImageType defaultImageType = ImageType.COMPOSITE;

    /**
     * @return Returns the id.
     */
    public long getId() {
        return this.id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Imager.getDefaultImageType
     * 
     * @return
     */
    public ImageType getDefaultImageType() {
        return this.defaultImageType;
    }

    /**
     * @param defaultImageType The defaultImageType to set.
     */
    public void setDefaultImageType(ImageType defaultImageType) {
        this.defaultImageType = defaultImageType;
    }

}
