package org.pimslims.bioinf;

import java.io.Serializable;

/**
 * BlastHeaderBean, used to store parameters used for web service Blast
 * 
 * @author Susy Griffiths YSBL June 2007
 */
public class BlastHeaderBean implements Comparable, Serializable {
    /**
     * To satisfy Serializable Interface
     */
    private static final long serialVersionUID = 1;

    /**
     * properties
     */

    private String targetId;

    private String databaseSearched;

    private String searchDate;

    private int targetLen;

    public int compareTo(Object obj) {
        if (!(obj instanceof BlastHeaderBean)) {
            throw new ClassCastException("obj1 is not a Blast header ");
        }
        return 0;
    }

    /**
     * @return Returns the databaseSearched.
     */
    public String getDatabaseSearched() {
        return databaseSearched;
    }

    /**
     * @param databaseSearched The databaseSearched to set.
     */
    public void setDatabaseSearched(String databaseSearched) {
        this.databaseSearched = databaseSearched;
    }

    /**
     * @return Returns the searchDate.
     */
    public String getSearchDate() {
        return searchDate;
    }

    /**
     * @param searchDate The searchDate to set.
     */
    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }

    /**
     * @return Returns the targetId.
     */
    public String getTargetId() {
        return targetId;
    }

    /**
     * @param targetId The targetId to set.
     */
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    /**
     * @return Returns the targetLen.
     */
    public int getTargetLen() {
        return targetLen;
    }

    /**
     * @param targetLen The targetLen to set.
     */
    public void setTargetLen(int targetLen) {
        this.targetLen = targetLen;
    }

}
