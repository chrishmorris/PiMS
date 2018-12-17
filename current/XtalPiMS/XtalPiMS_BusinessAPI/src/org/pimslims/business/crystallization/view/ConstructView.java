/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pimslims.business.crystallization.view;

import net.sf.json.JSONObject;


/**
 *
 * @author ian
 */
public class ConstructView {
    public static final String PROP_ID = "constructId";
    public static final String PROP_NAME = "constructName";
    public static final String PROP_DESCRIPTION = "description";
    public static final String PROP_GROUP = "group";
    public static final String PROP_OWNER = "owner";
    public static final String PROP_TARGET_NAME = "targetName";
    public static final String PROP_TARGET_ID = "targetId";
    
    private Long constructId;
    private String constructName;
    private String description;
    private String group;
    private String owner;
    private String targetName;
    private Long targetId;

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public ConstructView() {
        
    }
    
    public  Long getConstructId() {
        return constructId;
    }

    public void setConstructId(Long id) {
        this.constructId = id;
    }

    public String getConstructName() {
        return constructName;
    }

    public void setConstructName(String name) {
        this.constructName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String groupName) {
        this.group = groupName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result
                + ((group == null) ? 0 : group.hashCode());
        result = prime * result + ((constructId == null) ? 0 : constructId.hashCode());
        result = prime * result + ((constructName == null) ? 0 : constructName.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result
                + ((targetId == null) ? 0 : targetId.hashCode());
        result = prime * result
                + ((targetName == null) ? 0 : targetName.hashCode());
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
        final ConstructView other = (ConstructView) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (!group.equals(other.group))
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
        if (owner == null) {
            if (other.owner != null)
                return false;
        } else if (!owner.equals(other.owner))
            return false;
        if (targetId == null) {
            if (other.targetId != null)
                return false;
        } else if (!targetId.equals(other.targetId))
            return false;
        if (targetName == null) {
            if (other.targetName != null)
                return false;
        } else if (!targetName.equals(other.targetName))
            return false;
        return true;
    }
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put(PROP_ID, this.getConstructId());
        obj.put(PROP_NAME, this.getConstructName());
        obj.put(PROP_DESCRIPTION, this.getDescription());
        obj.put(PROP_GROUP, this.getGroup());
        obj.put(PROP_OWNER, this.getOwner());
        obj.put(PROP_TARGET_NAME, this.getTargetName());
        obj.put(PROP_TARGET_ID, this.getTargetId());        
        
        return obj;
    }
}
