package org.pimslims.business.crystallization.view;

import java.util.Calendar;

import net.sf.json.JSONObject;

/**
*
* @author ian
*/
public class ScheduleView {
    public static final String PROP_NAME = "name";
    public static final String PROP_BARCODE = "barcode";
    public static final String PROP_DATE_TO_IMAGE = "dateToImage";
    public static final String PROP_DATE_IMAGED = "dateImaged";
    public static final String PROP_PRIORITY = "priority";
    public static final String PROP_IMAGER = "imager";
    public static final String PROP_TEMPERATURE = "temperature";
    public static final String PROP_STATE = "state";
    public static final String PROP_DETAILS = "details";
    
    private String name = "";
    private String barcode = "";
    private Calendar dateToImage = null;
    private Calendar dateImaged = null;
    private int priority = 5;
    private String imager = "";
    private double temperature = 21.0;
    private int state = 1;
    private String details;
      
    /**
     * 
     */
    public ScheduleView() {
        super();
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBarcode() {
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    public Calendar getDateToImage() {
        return dateToImage;
    }
    public void setDateToImage(Calendar dateToImage) {
        this.dateToImage = dateToImage;
    }
    public Calendar getDateImaged() {
        return dateImaged;
    }
    public void setDateImaged(Calendar dateImaged) {
        this.dateImaged = dateImaged;
    }
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public String getImager() {
        return imager;
    }
    public void setImager(String imager) {
        this.imager = imager;
    }
    public double getTemperature() {
        return temperature;
    }
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    
    /**
     * @return Returns the details.
     */
    public String getDetails() {
        return details;
    }

    /**
     * @param details The details to set.
     */
    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
        result = prime * result + ((details == null) ? 0 : details.hashCode());
        result = prime * result + ((dateToImage == null) ? 0 : dateToImage.hashCode());
        result = prime * result + ((dateImaged == null) ? 0 : dateImaged.hashCode());
        result = prime * result + ((imager == null) ? 0 : imager.hashCode());
        result = prime * result + priority;
        result = prime * result + state;
        long temp;
        temp = Double.doubleToLongBits(temperature);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ScheduleView other = (ScheduleView) obj;
        if (barcode == null) {
            if (other.barcode != null)
                return false;
        } else if (!barcode.equals(other.barcode))
            return false;
        if (details == null) {
            if (other.details != null)
                return false;
        } else if (!details.equals(other.details))
            return false;
        if (dateToImage == null) {
            if (other.dateToImage != null)
                return false;
        } else if (!dateToImage.equals(other.dateToImage))
            return false;
        if (dateImaged == null) {
            if (other.dateImaged != null)
                return false;
        } else if (!dateImaged.equals(other.dateImaged))
            return false;
        if (imager == null) {
            if (other.imager != null)
                return false;
        } else if (!imager.equals(other.imager))
            return false;
        if (priority != other.priority)
            return false;
        if (state != other.state)
            return false;
        if (Double.doubleToLongBits(temperature) != Double.doubleToLongBits(other.temperature))
            return false;
        return true;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        
        obj.put(PROP_BARCODE, this.getBarcode());
        obj.put(PROP_PRIORITY, this.getPriority());
        obj.put(PROP_IMAGER, this.getImager());
        obj.put(PROP_TEMPERATURE, this.getTemperature());
        obj.put(PROP_STATE, this.getState());
        obj.put(PROP_DATE_TO_IMAGE, this.getDateToImage().getTimeInMillis());
        obj.put(PROP_DATE_IMAGED, this.getDateToImage().getTimeInMillis());
        obj.put(PROP_DETAILS, this.getDetails());
        
        return obj;
    }
}
