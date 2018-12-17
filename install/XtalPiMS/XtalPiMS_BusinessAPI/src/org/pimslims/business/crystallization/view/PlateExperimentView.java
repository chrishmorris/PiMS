/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.business.crystallization.view;

import java.util.Calendar;

import net.sf.json.JSONObject;

/**
 * This is a view class representing a plate and is used in the ViewPlates Screen of the GUI. It provides a
 * simplified or extended view of a plate following the requirements of the page rather than an exact
 * definition of the object (where the model object should be used). This is somewhat similar to a JSF Backing
 * bean This object represents a "plate", or rather one experiment on a plate.
 * 
 * @author IMB
 */
public class PlateExperimentView {
    public static final String PROP_BARCODE = "barcode";

    public static final String PROP_CREATE_DATE = "createDate";

    public static final String PROP_DESTROY_DATE = "destroyDate";

    public static final String PROP_DESCRIPTION = "description";

    public static final String PROP_OWNER = "owner";

    public static final String PROP_RUN_BY = "runBy";

    public static final String PROP_GROUP = "group";

    public static final String PROP_TEMPERATURE = "temperature";

    public static final String PROP_IMAGER = "imager";

    public static final String PROP_STATUS = "status";

    public static final String PROP_NUMBER_OF_CRYSTALS = "numberOfCrystals";

    public static final String PROP_SAMPLE_NAME = "sampleName";

    public static final String PROP_SAMPLE_ID = "sampleId";

    public static final String PROP_CONSTRUCT_NAME = "constructName";

    public static final String PROP_CONSTRUCT_ID = "constructId";

    public static final String PROP_LAST_IMAGE_DATE = "lastImageDate";

    public static final String PROP_PLATE_TYPE = "plateType";

    public static final String PROP_SCREEN = "screen";

    public static final String PROP_LINK = "link";

    private static final String PROP_ISADDITIVESCREEN = "isAdditiveScreen";

    private String barcode = "";

    private Calendar createDate = null;

    private Calendar destroyDate = null;

    private String description = "";

    private String owner = "";

    private String runBy = "";

    private String group = "";

    private Float temperature = 0.0f;

    private String imager = "";

    private String status = "";

    private Integer numberOfCrystals = 0;

    private String sampleName = "";

    private Long sampleId = null;

    private String constructName = "";

    private Long constructId = null;

    private Calendar lastImageDate = null;

    private String plateType = "";

    private String screen = "";

    private String link = "";

    /**
     * @return Returns the link.
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link The link to set.
     */
    public void setLink(final String link) {
        this.link = link;
    }

    public PlateExperimentView() {

    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(final String barcode) {
        this.barcode = barcode;
    }

    public Calendar getCreateDate() {
        return createDate;
    }

    public void setCreateDate(final Calendar createDate) {
        this.createDate = createDate;
    }

    public Calendar getDestroyDate() {
        return destroyDate;
    }

    public void setDestroyDate(final Calendar destroyDate) {
        this.destroyDate = destroyDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(final Float temperature) {
        this.temperature = temperature;
    }

    public String getImager() {
        return imager;
    }

    public void setImager(final String imager) {
        this.imager = imager;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public Integer getNumberOfCrystals() {
        return numberOfCrystals;
    }

    public void setNumberOfCrystals(final Integer numberOfCrystals) {
        this.numberOfCrystals = numberOfCrystals;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(final String sampleName) {
        this.sampleName = sampleName;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(final Long sampleId) {
        this.sampleId = sampleId;
    }

    public String getConstructName() {
        return constructName;
    }

    public void setConstructName(final String constructName) {
        this.constructName = constructName;
    }

    public Long getConstructId() {
        return constructId;
    }

    public void setConstructId(final Long constructId) {
        if (constructId != null) {
            this.constructId = constructId;
        }
    }

    public Calendar getLastImageDate() {
        return lastImageDate;
    }

    public void setLastImageDate(final Calendar lastImageDate) {
        this.lastImageDate = lastImageDate;
    }

    public String getRunBy() {
        return runBy;
    }

    public void setRunBy(final String runBy) {
        this.runBy = runBy;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(final String group) {
        this.group = group;
    }

    public String getPlateType() {
        return plateType;
    }

    public void setPlateType(final String plateType) {
        this.plateType = plateType;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(final String screen) {
        this.screen = screen;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
        result = prime * result + ((constructId == null) ? 0 : constructId.hashCode());
        result = prime * result + ((constructName == null) ? 0 : constructName.hashCode());
        result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((destroyDate == null) ? 0 : destroyDate.hashCode());
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + ((imager == null) ? 0 : imager.hashCode());
        result = prime * result + ((lastImageDate == null) ? 0 : lastImageDate.hashCode());
        result = prime * result + ((numberOfCrystals == null) ? 0 : numberOfCrystals.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result + ((runBy == null) ? 0 : runBy.hashCode());
        result = prime * result + ((sampleId == null) ? 0 : sampleId.hashCode());
        result = prime * result + ((sampleName == null) ? 0 : sampleName.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((temperature == null) ? 0 : temperature.hashCode());
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
        final PlateExperimentView other = (PlateExperimentView) obj;
        if (barcode == null) {
            if (other.barcode != null) {
                return false;
            }
        } else if (!barcode.equals(other.barcode)) {
            return false;
        }
        if (constructId == null) {
            if (other.constructId != null) {
                return false;
            }
        } else if (!constructId.equals(other.constructId)) {
            return false;
        }
        if (constructName == null) {
            if (other.constructName != null) {
                return false;
            }
        } else if (!constructName.equals(other.constructName)) {
            return false;
        }
        if (createDate == null) {
            if (other.createDate != null) {
                return false;
            }
        } else if (!createDate.equals(other.createDate)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (destroyDate == null) {
            if (other.destroyDate != null) {
                return false;
            }
        } else if (!destroyDate.equals(other.destroyDate)) {
            return false;
        }
        if (group == null) {
            if (other.group != null) {
                return false;
            }
        } else if (!group.equals(other.group)) {
            return false;
        }
        if (imager == null) {
            if (other.imager != null) {
                return false;
            }
        } else if (!imager.equals(other.imager)) {
            return false;
        }
        if (lastImageDate == null) {
            if (other.lastImageDate != null) {
                return false;
            }
        } else if (!lastImageDate.equals(other.lastImageDate)) {
            return false;
        }
        if (numberOfCrystals == null) {
            if (other.numberOfCrystals != null) {
                return false;
            }
        } else if (!numberOfCrystals.equals(other.numberOfCrystals)) {
            return false;
        }
        if (owner == null) {
            if (other.owner != null) {
                return false;
            }
        } else if (!owner.equals(other.owner)) {
            return false;
        }
        if (runBy == null) {
            if (other.runBy != null) {
                return false;
            }
        } else if (!runBy.equals(other.runBy)) {
            return false;
        }
        if (sampleId == null) {
            if (other.sampleId != null) {
                return false;
            }
        } else if (!sampleId.equals(other.sampleId)) {
            return false;
        }
        if (sampleName == null) {
            if (other.sampleName != null) {
                return false;
            }
        } else if (!sampleName.equals(other.sampleName)) {
            return false;
        }
        if (status == null) {
            if (other.status != null) {
                return false;
            }
        } else if (!status.equals(other.status)) {
            return false;
        }
        if (temperature == null) {
            if (other.temperature != null) {
                return false;
            }
        } else if (!temperature.equals(other.temperature)) {
            return false;
        }
        return true;
    }

    public JSONObject toJSON() {
        final JSONObject obj = new JSONObject();
        obj.put(PROP_LINK, this.getLink());
        obj.put(PROP_BARCODE, this.getBarcode());
        if (this.getCreateDate() != null) {
            obj.put(PROP_CREATE_DATE, this.getCreateDate().getTimeInMillis());
        }
        if (this.getDestroyDate() != null) {
            obj.put(PROP_DESTROY_DATE, this.getDestroyDate().getTimeInMillis());
        }
        obj.put(PROP_DESCRIPTION, this.getDescription());
        obj.put(PROP_OWNER, this.getOwner());
        obj.put(PROP_RUN_BY, this.getRunBy());
        obj.put(PROP_GROUP, this.getGroup());
        if (getImager() != null) {
            obj.put(PROP_IMAGER, this.getImager());
            obj.put(PROP_TEMPERATURE, this.getTemperature());
        }
        obj.put(PROP_STATUS, this.getStatus());
        obj.put(PROP_NUMBER_OF_CRYSTALS, this.getNumberOfCrystals());
        if (this.getSampleName() != null) {
            obj.put(PROP_SAMPLE_NAME, this.getSampleName());
            obj.put(PROP_SAMPLE_ID, this.getSampleId());
        } else {
            obj.put(PROP_SAMPLE_NAME, "");
            obj.put(PROP_SAMPLE_ID, -1);
        }
        if (this.getConstructId() != null) {
            obj.put(PROP_CONSTRUCT_NAME, this.getConstructName());
            obj.put(PROP_CONSTRUCT_ID, this.getConstructId());
        } else {
            obj.put(PROP_CONSTRUCT_NAME, "");
            obj.put(PROP_CONSTRUCT_ID, -1);
        }
        if (this.getLastImageDate() != null) {
            obj.put(PROP_LAST_IMAGE_DATE, this.getLastImageDate().getTimeInMillis());
        }
        obj.put(PROP_PLATE_TYPE, this.getPlateType());
        obj.put(PROP_SCREEN, this.getScreen());
        if (this.isAdditiveScreen()) {
            obj.put(PROP_ISADDITIVESCREEN, "true");
        }

        return obj;
    }

    public boolean isAdditiveScreen() {
        // TODO Auto-generated method stub
        return false;
    }
}
