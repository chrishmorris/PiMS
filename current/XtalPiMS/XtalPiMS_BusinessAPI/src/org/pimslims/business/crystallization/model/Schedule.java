/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

import java.util.Calendar;

import org.pimslims.business.XtalObject;
import org.pimslims.business.core.model.Location;

/**
 * 
 * @author ian
 */
public class Schedule extends XtalObject {
    private TrialPlate plate = null;

    private Calendar dateToImage = null;

    private int priority = 5;

    private Location location = null;

    private int state = 1;

    private PlateInspection plateInspection = null;

    public TrialPlate getPlate() {
        return plate;
    }

    public void setPlate(final TrialPlate plate) {
        this.plate = plate;
    }

    public Calendar getDateToImage() {
        return dateToImage;
    }

    public void setDateToImage(final Calendar dateToImage) {
        this.dateToImage = dateToImage;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(final int priority) {
        this.priority = priority;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    public int getState() {
        return state;
    }

    public void setState(final int state) {
        this.state = state;
    }

    public PlateInspection getPlateInspection() {
        return plateInspection;
    }

    public void setPlateInspection(final PlateInspection plateInspection) {
        this.plateInspection = plateInspection;
    }

}
