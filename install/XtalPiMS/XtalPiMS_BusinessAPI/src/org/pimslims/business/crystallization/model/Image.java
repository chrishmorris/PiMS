/*
 * Image.java Created on 17 April 2007, 10:23 To change this template, choose Tools | Template Manager and
 * open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

import java.util.Calendar;

import org.pimslims.business.XtalObject;
import org.pimslims.business.core.model.Location;

/**
 * <p>
 * Describes an Image
 * </p>
 * <p>
 * This is the physical image and its details, i.e. location is the imager, well is the well it is an image
 * of, etc.
 * </p>
 * 
 * @author IMB
 */
public class Image extends XtalObject {

    private String imagePath = null;

    private double xLengthPerPixel = 0.0;

    private double yLengthPerPixel = 0.0;

    private int sizeX = 0;

    private int sizeY = 0;

    private int colourDepth = 0;

    private Calendar imageDate = null;

    private ImageType imageType = ImageType.COMPOSITE;



    //This is the minimum of information required to show where this image has come from...
    private TrialDrop drop = null;

    //Removed from bean - get using the Service objects - saves loading stuff we do not need all at once!

    //TODO use inspection.getLocation
    private Location location = null;

    private PlateInspection plateInspection = null;

    //private List<Score> scores = new ArrayList<Score>();

    /**
     * Creates a new instance of an Image
     */
    public Image() {

    }

    /**
     * 
     * @return
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * 
     * @param imagePath
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * 
     * @return
     */
    public double getXLengthPerPixel() {
        return xLengthPerPixel;
    }

    /**
     * 
     * @param xLengthPerPixel
     */
    public void setXLengthPerPixel(double xLengthPerPixel) {
        this.xLengthPerPixel = xLengthPerPixel;
    }

    /**
     * 
     * @return
     */
    public double getYLengthPerPixel() {
        return yLengthPerPixel;
    }

    /**
     * 
     * @param yLengthPerPixel
     */
    public void setYLengthPerPixel(double yLengthPerPixel) {
        this.yLengthPerPixel = yLengthPerPixel;
    }

    /**
     * 
     * @return
     */
    public int getSizeX() {
        return sizeX;
    }

    /**
     * 
     * @param sizeX
     */
    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    /**
     * 
     * @return
     */
    public int getSizeY() {
        return sizeY;
    }

    /**
     * 
     * @param sizeY
     */
    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    /**
     * 
     * @return
     */
    public int getColourDepth() {
        return colourDepth;
    }

    /**
     * 
     * @param colourDepth
     */
    public void setColourDepth(int colourDepth) {
        this.colourDepth = colourDepth;
    }

    /**
     * 
     * @return
     */
    public ImageType getImageType() {
        return imageType;
    }

    /**
     * 
     * @param imageType
     */
    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    public TrialDrop getDrop() {
        return drop;
    }

    public void setDrop(TrialDrop drop) {
        this.drop = drop;
    }

    public Calendar getImageDate() {
        return imageDate;
    }

    public void setImageDate(Calendar imageDate) {
        this.imageDate = imageDate;
    }

    //TODO use inspection.getLocation
    public Location getLocation() {
        return location;
    }

    //TODO use inspection.setLocation
    public void setLocation(Location location) {
        this.location = location;
    }

    public PlateInspection getPlateInspection() {
        return plateInspection;
    }

    public void setPlateInspection(PlateInspection plateInspection) {
        this.plateInspection = plateInspection;
    }



}
