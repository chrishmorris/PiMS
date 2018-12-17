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
public class GroupView {
    public static final String PROP_ID = "groupId";
    public static final String PROP_NAME = "groupName";
    public static final String PROP_GROUP_HEAD = "groupHead";
    public static final String PROP_ORGANISATION = "organisation";
    
    private Long groupId;
    private String groupName;
    private String groupHead;
    private String organisation;

    public GroupView() {
        
    }
    


    public Long getGroupId() {
        return groupId;
    }



    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }



    public String getGroupName() {
        return groupName;
    }



    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }



    public String getGroupHead() {
        return groupHead;
    }

    public void setGroupHead(String groupHead) {
        this.groupHead = groupHead;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((groupHead == null) ? 0 : groupHead.hashCode());
        result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
        result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
        result = prime * result
                + ((organisation == null) ? 0 : organisation.hashCode());
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
        final GroupView other = (GroupView) obj;
        if (groupHead == null) {
            if (other.groupHead != null)
                return false;
        } else if (!groupHead.equals(other.groupHead))
            return false;
        if (groupId == null) {
            if (other.groupId != null)
                return false;
        } else if (!groupId.equals(other.groupId))
            return false;
        if (groupName == null) {
            if (other.groupName != null)
                return false;
        } else if (!groupName.equals(other.groupName))
            return false;
        if (organisation == null) {
            if (other.organisation != null)
                return false;
        } else if (!organisation.equals(other.organisation))
            return false;
        return true;
    }
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put(PROP_ID, this.getGroupId());
        obj.put(PROP_NAME, this.getGroupName());
        obj.put(PROP_GROUP_HEAD, this.getGroupHead());
        obj.put(PROP_ORGANISATION, this.getOrganisation());        
        
        return obj;
    }
}
