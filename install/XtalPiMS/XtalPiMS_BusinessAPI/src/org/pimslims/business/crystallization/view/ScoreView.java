/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pimslims.business.crystallization.view;

import java.util.Calendar;

import net.sf.json.JSONObject;

import org.pimslims.business.crystallization.util.ColorUtil;

/**
 *
 * @author ian
 */
public class ScoreView {
    public static final String PROP_BARCODE = "barcode";
    public static final String PROP_WELL = "well";
    public static final String PROP_DATE = "date";
    public static final String PROP_DESCRIPTION = "description";
    public static final String PROP_COLOUR = "colour";
    public static final String PROP_NAME = "name";
    public static final String PROP_VERSION = "version";
    public static final String PROP_TYPE = "type";
    public static final String PROP_INSPECTION_NAME = "inspectionName";
    public static final String PROP_CONSTRUCT_ID = "constructId";
    public static final String PROP_CONSTRUCT_NAME = "constructName";
    public static final String PROP_SAMPLE_ID = "sampleId";
    public static final String PROP_SAMPLE_NAME = "sampleName";
    
    
    public static final String TYPE_HUMAN = "human";
    public static final String TYPE_SOFTWARE = "software";
    /**
     * The plate barcode to which the score belongs.
     */
    private String barcode = "";
    /**
     * The well to which the score belongs.
     */
    private String well = "";
    /**
     * The date when the score was made.
     */
    private Calendar date;
    /**
     * The description of the score.
     */
    private String description = "";
    /**
     * This is the colour - in HEX format. i.e. #FFFFFF for white, etc.
     */
    private String colour = "";
    /**
     * The name of the person, e.g. firstname lastname or name of piece of
     * software.
     */
    private String name;
    /**
     * The version of the software. (Not applicable to human scores)
     */
    private String version;
    /**
     * The type of annotation: human or software.
     */
    private String type;
    /**
     * The inspection name. (not applicable to human scores, although could be 
     * a better indication of when the annotation changes from one to another
     * as annotations may not necessarily happen in chronological order)
     */
    private String inspectionName;

    private Long constructId = 0L;
    private String constructName;
    private Long sampleId = 0L;
    private String sampleName;
    
    public ScoreView() {
    }

    public ScoreView(Calendar date, String name, String version, String type, String inspectionName) {
        this.date = date;
        this.name = name;
        this.version = version;
        this.type = type;
        this.inspectionName = inspectionName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getWell() {
        return well;
    }

    public void setWell(String well) {
        this.well = well;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    /**
     * Work around for those databases that store the colour information as an int/long
     * @param colour
     */
    public void setColourLong(long colour) {
        this.colour = ColorUtil.convertColorToHex(colour);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInspectionName() {
        return inspectionName;
    }

    public void setInspectionName(String inspectionName) {
        this.inspectionName = inspectionName;
    }

    public Long getConstructId() {
        return constructId;
    }

    public void setConstructId(Long constructId) {
        this.constructId = constructId;
    }

    public String getConstructName() {
        return constructName;
    }

    public void setConstructName(String constructName) {
        this.constructName = constructName;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
        result = prime * result + ((colour == null) ? 0 : colour.hashCode());
        result = prime * result
                + ((constructId == null) ? 0 : constructId.hashCode());
        result = prime * result
                + ((constructName == null) ? 0 : constructName.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result
                + ((inspectionName == null) ? 0 : inspectionName.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((sampleId == null) ? 0 : sampleId.hashCode());
        result = prime * result
                + ((sampleName == null) ? 0 : sampleName.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        result = prime * result + ((well == null) ? 0 : well.hashCode());
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
        final ScoreView other = (ScoreView) obj;
        if (barcode == null) {
            if (other.barcode != null)
                return false;
        } else if (!barcode.equals(other.barcode))
            return false;
        if (colour == null) {
            if (other.colour != null)
                return false;
        } else if (!colour.equals(other.colour))
            return false;
        if (constructId == null) {
            if (other.constructId != null)
                return false;
        } else if (!constructId.equals(other.constructId))
            return false;
        if (constructName == null) {
            if (other.constructName != null)
                return false;
        } else if (!constructName.equals(other.constructName))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (inspectionName == null) {
            if (other.inspectionName != null)
                return false;
        } else if (!inspectionName.equals(other.inspectionName))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (sampleId == null) {
            if (other.sampleId != null)
                return false;
        } else if (!sampleId.equals(other.sampleId))
            return false;
        if (sampleName == null) {
            if (other.sampleName != null)
                return false;
        } else if (!sampleName.equals(other.sampleName))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        if (well == null) {
            if (other.well != null)
                return false;
        } else if (!well.equals(other.well))
            return false;
        return true;
    }
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        
        obj.put(PROP_BARCODE, this.getBarcode());
        obj.put(PROP_WELL, this.getWell());
        obj.put(PROP_DATE, this.getDate().getTimeInMillis());
        obj.put(PROP_DESCRIPTION, this.getDescription());
        obj.put(PROP_COLOUR, this.getColour());
        obj.put(PROP_NAME, this.getName());
        obj.put(PROP_VERSION, this.getVersion());
        obj.put(PROP_TYPE, this.getType());
        obj.put(PROP_INSPECTION_NAME, this.getInspectionName());
        obj.put(PROP_CONSTRUCT_ID, this.getConstructId());
        obj.put(PROP_CONSTRUCT_NAME, this.getConstructName());
        obj.put(PROP_SAMPLE_ID, this.getSampleId());
        obj.put(PROP_SAMPLE_NAME, this.getSampleName());
        
        return obj;
    }
    
}
