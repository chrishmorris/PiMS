package org.pimslims.presentation.plateExperiment;

import org.pimslims.presentation.mru.MRUController;

public class PlateCriteria {

    static final int UNLIMITED = Integer.MAX_VALUE;

    String plateName = null;

    String expTypeName = null;

    String holdTypeName = null;

    int limit = PlateCriteria.UNLIMITED;

    int start = 0;

    /**
     * onlyGroups boolean: plate experiments not wanted
     */
    private boolean onlyGroups = false;

    /**
     * PlateCriteria.getStart
     * 
     * @return
     */
    public int getStart() {
        return this.start;
    }

    /**
     * @param start The start to set.
     */
    public void setStart(final int start) {
        this.start = start;
    }

    /**
     * @return the plateName
     */
    public String getPlateName() {
        return this.plateName;
    }

    /**
     * @param plateName the plateName to set
     */
    public void setPlateName(String plateName) {
        //System.out.println("setPlateName[" + plateName + "]");
        if (plateName == null || plateName.trim().equals("")
            || plateName.equalsIgnoreCase(MRUController.NONE)) {
            plateName = null;
        }
        this.plateName = plateName;
    }

    /**
     * @return the expTypeName
     */
    public String getExpTypeName() {
        return this.expTypeName;
    }

    /**
     * @param expTypeName the expTypeName to set
     */
    public void setExpTypeName(String expTypeName) {
        //System.out.println("setExpTypeName[" + expTypeName + "]");
        if (expTypeName == null || expTypeName.trim().equals("")
            || expTypeName.equalsIgnoreCase(MRUController.NONE)) {
            expTypeName = null;
        }
        this.expTypeName = expTypeName;
    }

    /**
     * @return the holdTypeName
     */
    public String getHoldTypeName() {
        return this.holdTypeName;
    }

    /**
     * @param holdTypeName the holdTypeName to set
     */
    public void setHoldTypeName(String holdTypeName) {
        //System.out.println("setHoldTypeName[" + holdTypeName + "]");
        if (holdTypeName == null || holdTypeName.trim().equals("")
            || holdTypeName.equalsIgnoreCase(MRUController.NONE)) {
            holdTypeName = null;
        }
        this.holdTypeName = holdTypeName;
    }

    /**
     * @return the page size for the search
     */
    public int getLimit() {
        return this.limit;
    }

    /**
     * @param limit the page size for the search
     */
    public void setLimit(final int limit) {
        this.limit = limit;
    }

    /**
     * PlateCriteria.setOnlyGroups
     * 
     * @param b
     */
    public void setOnlyGroups(final boolean b) {
        this.onlyGroups = b;
        assert null == this.holdTypeName;
    }

    /**
     * @return Returns the onlyGroups.
     */
    public boolean isOnlyGroups() {
        return this.onlyGroups;
    }

}
