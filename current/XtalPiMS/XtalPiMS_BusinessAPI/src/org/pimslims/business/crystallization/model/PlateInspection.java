/*
 * PlateInspection.java Created on 27 April 2007, 10:28 To change this template, choose Tools | Template
 * Manager and open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

import java.util.Calendar;
import java.util.Collection;

import org.pimslims.business.XtalObject;
import org.pimslims.business.core.model.Location;

/**
 * <p>
 * A Plate Inspection is an imaging session, this may be of the whole plate using an imaging system, or just a
 * single well (or subPosition) through an imaging system or microscope
 * </p>
 * 
 * @author IMB
 */
public class PlateInspection extends XtalObject implements Comparable {

    public final static String PROP_INSPECTION_DATE = "inspectionDate";

    public final static String PROP_INSPECTION_NAME = "inspectionName";

    public final static String PROP_INSPECTION_NUMBER = "inspectionNumber";

    public final static String PROP_LOCATION = "location";

    public final static String PROP_PLATE = "plate";

    public final static String PROP_DETAILS = "details";

    /**
     * The Date of the inspection
     */
    private Calendar inspectionDate = null;

    /**
     * The name/number of the inspection - generally either the name or the number will be set or perhaps
     * both?
     */
    private String inspectionName = "";

    /**
     * The name/number of the inspection - generally either the name or the number will be set or perhaps
     * both?
     */
    private int inspectionNumber = 0;

    /**
     * The location where this inspection took place This is to cover the eventuality that a plate will be
     * imaged in multiple places by multiple devices, e.g. 20degree then into 4 degree and then occasionally
     * through the microscope.
     */
    private Location location = null;

    /**
     * the description of this inspection
     */
    private String details;

    /**
     * @return Returns the details.
     */
    public String getDetails() {
        return details;
    }

    /**
     * @param details The details to set.
     */
    public void setDetails(final String details) {
        this.details = details;
    }

    private TrialPlate plate = null;

    private Collection<Image> images;

    /**
     * Used for create, but note images not set by find methods
     * 
     * @return Returns the images.
     */
    public Collection<Image> getImages() {
        return images;
    }

    /**
     * @param images The images to set.
     */
    public void setImages(final Collection<Image> images) {
        this.images = images;
    }

    //Removed from bean - get using the Service objects - saves loading stuff we do not need all at once!    
    /**
     * The list of images associated with this inspection
     */
    //private List<Image> images = new ArrayList<Image>();
    /**
     * Creates a new instance of a PlateInspection
     */
    public PlateInspection() {

    }

    /**
     * 
     * @return
     */
    public Calendar getInspectionDate() {
        return inspectionDate;
    }

    /**
     * 
     * @param inspectionDate
     */
    public void setInspectionDate(final Calendar inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    /**
     * 
     * @return
     */
    public int getInspectionNumber() {
        return inspectionNumber;
    }

    /**
     * 
     * @param inspectionNumber
     */
    public void setInspectionNumber(final int inspectionNumber) {
        this.inspectionNumber = inspectionNumber;
    }

    /**
     * 
     * @return
     */
    public String getInspectionName() {
        return inspectionName;
    }

    /**
     * 
     * @param inspectionName
     */
    public void setInspectionName(final String inspectionName) {
        this.inspectionName = inspectionName;
    }

    /**
     * 
     * @return
     */
    public TrialPlate getPlate() {
        return plate;
    }

    /**
     * 
     * @param plate
     */
    public void setPlate(final TrialPlate plate) {
        this.plate = plate;
    }

    /**
     * 
     * @return
     */
    public Location getLocation() {
        return location;
    }

    /**
     * 
     * @param location
     */
    public void setLocation(final Location location) {
        this.location = location;
    }



    @Override
	public int compareTo(final Object o) {
        if (o instanceof PlateInspection) {
            return this.inspectionDate.compareTo(((PlateInspection) o).getInspectionDate());
        }
        throw new RuntimeException("Not Comparable:" + this + "," + o);
    }

}
