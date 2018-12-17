/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.business.crystallization.view;

import java.util.Calendar;

import net.sf.json.JSONObject;

/**
 * This class provides information to summarise a plate inspection
 * 
 * @author ian
 */
public class InspectionView {
    public static final String PROP_DATE = "date";

    public static final String PROP_BARCODE = "barcode";

    public static final String PROP_INSPECTION_NAME = "inspectionName";

    public static final String PROP_INSPECTION_NUMBER = "inspectionNumber";

    public static final String PROP_IMAGER = "imager";

    public static final String PROP_TEMPERATURE = "temperature";

    public static final String PROP_DETAILS = "details";

    private Calendar date = null;

    private String barcode = "";

    private String inspectionName = "";

    private String imager = "";

    private String details;

    private double temperature = 21.0;

    private int inspectionNumber = 0;

    public Calendar getDate() {
        return date;
    }

    public void setDate(final Calendar date) {
        this.date = date;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(final String barcode) {
        this.barcode = barcode;
    }

    public String getImager() {
        return imager;
    }

    public void setImager(final String imager) {
        this.imager = imager;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(final double temperature) {
        this.temperature = temperature;
    }

    public String getInspectionName() {
        return inspectionName;
    }

    public void setInspectionName(final String inspectionName) {
        this.inspectionName = inspectionName;
    }

    public int getInspectionNumber() {
        return inspectionNumber;
    }

    public void setInspectionNumber(final int inspectionNumber) {
        this.inspectionNumber = inspectionNumber;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((imager == null) ? 0 : imager.hashCode());
        result = prime * result + ((inspectionName == null) ? 0 : inspectionName.hashCode());
        result = prime * result + inspectionNumber;
        long temp;
        temp = Double.doubleToLongBits(temperature);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InspectionView other = (InspectionView) obj;
        if (barcode == null) {
            if (other.barcode != null) {
                return false;
            }
        } else if (!barcode.equals(other.barcode)) {
            return false;
        }
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }
        if (imager == null) {
            if (other.imager != null) {
                return false;
            }
        } else if (!imager.equals(other.imager)) {
            return false;
        }
        if (inspectionName == null) {
            if (other.inspectionName != null) {
                return false;
            }
        } else if (!inspectionName.equals(other.inspectionName)) {
            return false;
        }
        if (inspectionNumber != other.inspectionNumber) {
            return false;
        }
        if (Double.doubleToLongBits(temperature) != Double.doubleToLongBits(other.temperature)) {
            return false;
        }
        return true;
    }

    public JSONObject toJSON() {
        final JSONObject obj = new JSONObject();

        obj.put(PROP_DATE, this.getDate().getTimeInMillis());
        obj.put(PROP_BARCODE, this.getBarcode());
        obj.put(PROP_INSPECTION_NAME, this.getInspectionName());
        obj.put(PROP_INSPECTION_NUMBER, this.getInspectionNumber());
        obj.put(PROP_IMAGER, this.getImager());
        obj.put(PROP_TEMPERATURE, this.getTemperature());
        obj.put(PROP_DETAILS, this.getDetails());
        return obj;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(final String details) {
        this.details = details;
    }
}
