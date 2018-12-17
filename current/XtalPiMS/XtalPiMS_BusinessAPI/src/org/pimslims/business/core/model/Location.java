/*
 * Location.java Created on 03 May 2007, 09:43 To change this template, choose Tools | Template Manager and
 * open the template in the editor.
 */

package org.pimslims.business.core.model;

import org.pimslims.business.XtalObject;

/**
 * <p>
 * A location is an area / room / imager / device within an organisation
 * </p>
 * <p>
 * This corresponds to a PiMS Instrument
 * </p>
 * 
 * @author IMB
 */
public class Location extends XtalObject {
    public static String PROP_NAME = "name";

    public static String PROP_LOCATION_TYPE = "locationType";

    public static String PROP_TEMPERATURE = "temperature";

    public static String PROP_PRESSURE = "pressure";

    private String name = "";

    private String locationType = "";

    private double temperature = 0.0;

    private double pressure = 0.0;

    /**
     * Creates a new instance of a Location
     */
    public Location() {

    }

    /**
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     */
    public String getLocationType() {
        return locationType;
    }

    /**
     * 
     * @param locationType
     */
    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    /**
     * 
     * @return
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * 
     * @param temperature TODO whaqt units?
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /**
     * 
     * @return TODO what units?
     */
    public double getPressure() {
        return pressure;
    }

    /**
     * 
     * @param pressure
     */
    public void setPressure(double pressure) {
        this.pressure = pressure;
    }
}