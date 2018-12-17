package org.pimslims.business.crystallization.view;

import net.sf.json.JSONObject;

public class ComponentView {
    public static final String PROP_NAME               = "componentName";
    public static final String PROP_TYPE               = "componentType";
    public static final String PROP_VOLATILE_COMPONENT = "volatileComponent";
    public static final String PROP_CAS_NUMBER         = "casNumber";
    public static final String PROP_PH                 = "componentPH";
    public static final String PROP_IMAGE_URL          = "imageURL";
    public static final String PROP_SAFETY_ID          = "safetyId";

    private String       componentName;

    /**
     * e.g. Buffer, Salt, Precipitant, etc.
     */
    private String       componentType;

    private boolean      volatileComponent       = false;
    private String       casNumber               = "";
    private float        componentPH             = 7.0f;
    private String       imageURL                = "";
    private Long         safetyFormId            = -1L;

    public ComponentView() {
    }

    /**
     * @param name
     * @param quantity
     * @param type
     * @param volatileComponent
     * @param casNumber
     * @param ph
     * @param imageURL
     * @param safetyFormId
     */
    public ComponentView(String name, String type, boolean volatileComponent,
            String casNumber, float ph, String imageURL, Long safetyFormId) {
        super();
        this.componentName = name;
        this.componentType = type;
        this.volatileComponent = volatileComponent;
        this.casNumber = casNumber;
        this.componentPH = ph;
        this.imageURL = imageURL;
        this.safetyFormId = safetyFormId;
    }

    public ComponentView(String name, String type) {
        this.componentName = name;
        this.componentType = type;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String name) {
        this.componentName = name;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String type) {
        this.componentType = type;
    }

    public boolean isVolatileComponent() {
        return volatileComponent;
    }

    public void setVolatileComponent(boolean volatileComponent) {
        this.volatileComponent = volatileComponent;
    }

    public String getCasNumber() {
        return casNumber;
    }

    public void setCasNumber(String casNumber) {
        this.casNumber = casNumber;
    }

    public float getComponentPH() {
        return componentPH;
    }

    public void setComponentPH(float ph) {
        this.componentPH = ph;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Long getSafetyFormId() {
        return safetyFormId;
    }

    public void setSafetyFormId(Long safetyFormId) {
        this.safetyFormId = safetyFormId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((casNumber == null) ? 0 : casNumber.hashCode());
        result = prime * result
                + ((imageURL == null) ? 0 : imageURL.hashCode());
        result = prime * result + ((componentName == null) ? 0 : componentName.hashCode());
        result = prime * result + Float.floatToIntBits(componentPH);

        result = prime * result
                + ((safetyFormId == null) ? 0 : safetyFormId.hashCode());
        result = prime * result + ((componentType == null) ? 0 : componentType.hashCode());
        result = prime * result + (volatileComponent ? 1231 : 1237);
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
        final ComponentView other = (ComponentView) obj;
        if (casNumber == null) {
            if (other.casNumber != null)
                return false;
        } else if (!casNumber.equals(other.casNumber))
            return false;
        if (imageURL == null) {
            if (other.imageURL != null)
                return false;
        } else if (!imageURL.equals(other.imageURL))
            return false;
        if (componentName == null) {
            if (other.componentName != null)
                return false;
        } else if (!componentName.equals(other.componentName))
            return false;
        if (Float.floatToIntBits(componentPH) != Float.floatToIntBits(other.componentPH))
            return false;
        if (safetyFormId == null) {
            if (other.safetyFormId != null)
                return false;
        } else if (!safetyFormId.equals(other.safetyFormId))
            return false;
        if (componentType == null) {
            if (other.componentType != null)
                return false;
        } else if (!componentType.equals(other.componentType))
            return false;
        if (volatileComponent != other.volatileComponent)
            return false;
        return true;
    }
    
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put(PROP_NAME, this.getComponentName());
        obj.put(PROP_TYPE, this.getComponentType());
        obj.put(PROP_VOLATILE_COMPONENT, this.isVolatileComponent());
        obj.put(PROP_CAS_NUMBER, this.getCasNumber());
        obj.put(PROP_PH, this.getComponentPH());
        obj.put(PROP_IMAGE_URL, this.getImageURL());
        obj.put(PROP_SAFETY_ID, this.getSafetyFormId());
                
        return obj;
    }    
}
