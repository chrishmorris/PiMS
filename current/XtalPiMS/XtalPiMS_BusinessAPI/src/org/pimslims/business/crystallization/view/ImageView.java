/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.business.crystallization.view;

import java.util.Calendar;

import net.sf.json.JSONObject;

/**
 * 
 * @author ian
 */
public class ImageView {
    public static final String PROP_URL = "url";

    public static final String PROP_BARCODE = "barcode";

    public static final String PROP_WELL = "well";

    public static final String PROP_DATE = "date";

    public static final String PROP_INSPECTION_NAME = "inspectionName";

    public static final String PROP_WIDTH = "width";

    public static final String PROP_HEIGHT = "height";

    public static final String PROP_WIDTH_PER_PIXEL = "widthPerPixel";

    public static final String PROP_HEIGHT_PER_PIXEL = "heightPerPixel";

    public static final String PROP_DESCRIPTION = "description";

    public static final String PROP_INSTRUMENT = "instrument";

    public static final String PROP_TEMPERATURE = "temperature";

    public static final String PROP_TIME_POINT = "timePoint";

    public static final String PROP_SCREEN = "screen";

    public static final String PROP_TYPE = "imageType";

    //@TODO These are not currently used
    //public static String PROP_SAMPLES = "samples";
    //public static String PROP_CONDITION = "condition";
    //public static String PROP_COMPONENTS = "components";

    public static final String TYPE_ZOOMED = "zoomed";

    public static final String TYPE_COMPOSITE = "composite";

    public static final String TYPE_SLICE = "slice";
    
    public static final String TYPE_UV = "uv";

    private String url;

    private String barcode;

    private String well;

    private Calendar date;

    private String inspectionName;

    private Integer width;

    private Integer height;

    private Double widthPerPixel;

    private Double heightPerPixel;

    private String description;

    private String instrument;

    private Double temperature;

    private String timePoint;

    private String imageType;

    private String screen;

    //@TODO These need thinking about!
    //private String condition;
    //private List<SampleQuantityView> samples;
    //private List<ComponentQuantityView> components;
    //private List<ScoreView> annotations;

    public ImageView() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(final String barcode) {
        this.barcode = barcode;
    }

    public String getWell() {
        return well;
    }

    public void setWell(final String well) {
        this.well = well;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(final Calendar date) {
        this.date = date;
    }

    public String getInspectionName() {
        return inspectionName;
    }

    public void setInspectionName(final String inspectionName) {
        this.inspectionName = inspectionName;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(final Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(final Integer height) {
        this.height = height;
    }

    public Double getWidthPerPixel() {
        return widthPerPixel;
    }

    public void setWidthPerPixel(final Double widthPerPixel) {
        this.widthPerPixel = widthPerPixel;
    }

    public Double getHeightPerPixel() {
        return heightPerPixel;
    }

    public void setHeightPerPixel(final Double heightPerPixel) {
        this.heightPerPixel = heightPerPixel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(final String instrument) {
        this.instrument = instrument;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(final Double temperature) {
        this.temperature = temperature;
    }

    public String getTimePoint() {
        return timePoint;
    }

    public void setTimePoint(final String timePoint) {
        this.timePoint = timePoint;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(final String screen) {
        this.screen = screen;
    }

    //    public List<SampleQuantityView> getSamples() {
    //        return samples;
    //    }
    //
    //    public void setSamples(List<SampleQuantityView> samples) {
    //        this.samples = samples;
    //    }
    //
    //    public List<ComponentQuantityView> getComponents() {
    //        return components;
    //    }
    //
    //    public void setComponents(List<ComponentQuantityView> components) {
    //        this.components = components;
    //    }
    //
    //    public List<ScoreView> getAnnotations() {
    //        return annotations;
    //    }
    //
    //    public void setAnnotations(List<ScoreView> annotations) {
    //        this.annotations = annotations;
    //    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(final String imageType) {
        this.imageType = imageType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((height == null) ? 0 : height.hashCode());
        result = prime * result + ((heightPerPixel == null) ? 0 : heightPerPixel.hashCode());
        result = prime * result + ((inspectionName == null) ? 0 : inspectionName.hashCode());
        result = prime * result + ((instrument == null) ? 0 : instrument.hashCode());
        result = prime * result + ((screen == null) ? 0 : screen.hashCode());
        result = prime * result + ((temperature == null) ? 0 : temperature.hashCode());
        result = prime * result + ((timePoint == null) ? 0 : timePoint.hashCode());
        result = prime * result + ((imageType == null) ? 0 : imageType.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        result = prime * result + ((well == null) ? 0 : well.hashCode());
        result = prime * result + ((width == null) ? 0 : width.hashCode());
        result = prime * result + ((widthPerPixel == null) ? 0 : widthPerPixel.hashCode());
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
        final ImageView other = (ImageView) obj;
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
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (height == null) {
            if (other.height != null) {
                return false;
            }
        } else if (!height.equals(other.height)) {
            return false;
        }
        if (heightPerPixel == null) {
            if (other.heightPerPixel != null) {
                return false;
            }
        } else if (!heightPerPixel.equals(other.heightPerPixel)) {
            return false;
        }
        if (inspectionName == null) {
            if (other.inspectionName != null) {
                return false;
            }
        } else if (!inspectionName.equals(other.inspectionName)) {
            return false;
        }
        if (instrument == null) {
            if (other.instrument != null) {
                return false;
            }
        } else if (!instrument.equals(other.instrument)) {
            return false;
        }
        if (screen == null) {
            if (other.screen != null) {
                return false;
            }
        } else if (!screen.equals(other.screen)) {
            return false;
        }
        if (temperature == null) {
            if (other.temperature != null) {
                return false;
            }
        } else if (!temperature.equals(other.temperature)) {
            return false;
        }
        if (timePoint == null) {
            if (other.timePoint != null) {
                return false;
            }
        } else if (!timePoint.equals(other.timePoint)) {
            return false;
        }
        if (imageType == null) {
            if (other.imageType != null) {
                return false;
            }
        } else if (!imageType.equals(other.imageType)) {
            return false;
        }
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
            return false;
        }
        if (well == null) {
            if (other.well != null) {
                return false;
            }
        } else if (!well.equals(other.well)) {
            return false;
        }
        if (width == null) {
            if (other.width != null) {
                return false;
            }
        } else if (!width.equals(other.width)) {
            return false;
        }
        if (widthPerPixel == null) {
            if (other.widthPerPixel != null) {
                return false;
            }
        } else if (!widthPerPixel.equals(other.widthPerPixel)) {
            return false;
        }
        return true;
    }

    public JSONObject toJSON() {
        final JSONObject obj = new JSONObject();
        obj.put(PROP_URL, this.getUrl());
        obj.put(PROP_BARCODE, this.getBarcode());
        obj.put(PROP_WELL, this.getWell());
        obj.put(PROP_DATE, this.getDate().getTimeInMillis());
        obj.put(PROP_INSPECTION_NAME, this.getInspectionName());
        obj.put(PROP_WIDTH, this.getWidth());
        obj.put(PROP_HEIGHT, this.getHeight());
        obj.put(PROP_WIDTH_PER_PIXEL, this.getWidthPerPixel());
        obj.put(PROP_HEIGHT_PER_PIXEL, this.getHeightPerPixel());
        obj.put(PROP_DESCRIPTION, this.getDescription());
        obj.put(PROP_INSTRUMENT, this.getInstrument());
        obj.put(PROP_TEMPERATURE, this.getTemperature());
        obj.put(PROP_TIME_POINT, this.getTimePoint());
        obj.put(PROP_SCREEN, this.getScreen());
        obj.put(PROP_TYPE, this.getImageType());

        return obj;
    }
}
