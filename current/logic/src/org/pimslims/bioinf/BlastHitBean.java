package org.pimslims.bioinf;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * BlastHitBean, used to store details of a single BlastHit ?How to link to the Header Bean ?
 * 
 * @author Susy Griffiths YSBL June 2007
 */
public class BlastHitBean implements Comparable, Serializable {
    /**
     * To satisfy Serializable Interface
     */
    private static final long serialVersionUID = 1;

    /**
     * properties
     */

    private int hitNum;

    private String hitDbId;

    private String database;

    private String dbBaseURL;

    private String hitURLid;

    private int hitLength;

    private String description;

    private int noOfAlignments;

    private ArrayList<BlastAlignmentBean> alignmentBeans;

    private BlastAlignmentBean blastAlignmentBean;

    public int compareTo(Object obj) {
        if (!(obj instanceof BlastHitBean)) {
            throw new ClassCastException("obj1 is not a Blast hit");
        }
        return 0;
    }

    /**
     * @return Returns the hitNum.
     */
    public int getHitNum() {
        return hitNum;
    }

    /**
     * @param hitNum The hitNum to set.
     */
    public void setHitNum(int hitNum) {
        this.hitNum = hitNum;
    }

    /**
     * @return Returns the database.
     */
    public String getDatabase() {
        return database;
    }

    /**
     * @param database The database to set.
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the hitDbId.
     */
    public String getHitDbId() {
        return hitDbId;
    }

    /**
     * @param hitDbId The hitDbId to set.
     */
    public void setHitDbId(String hitDbId) {
        this.hitDbId = hitDbId;
    }

    /**
     * @return Returns the hitLength.
     */
    public int getHitLength() {
        return hitLength;
    }

    /**
     * @param hitLength The hitLength to set.
     */
    public void setHitLength(int hitLength) {
        this.hitLength = hitLength;
    }

    /**
     * @return Returns the noOfAlignments.
     */
    public int getNoOfAlignments() {
        return noOfAlignments;
    }

    /**
     * @param noOfAlignments The noOfAlignments to set.
     */
    public void setNoOfAlignments(int noOfAlignments) {
        this.noOfAlignments = noOfAlignments;
    }

    /**
     * @return Returns the alignmentBeans.
     */
    public ArrayList<BlastAlignmentBean> getAlignmentBeans() {
        return alignmentBeans;
    }

    /**
     * @param alignmentBeans The alignmentBeans to set.
     */
    public void setAlignmentBeans(ArrayList<BlastAlignmentBean> alignmentBeans) {
        this.alignmentBeans = alignmentBeans;
    }

    /**
     * @return Returns an alignment bean
     */
    public BlastAlignmentBean getAlignmentBean(int index, BlastAlignmentBean bean) {
        return alignmentBeans.get(0);
    }

    public void setAlignmentBean(BlastAlignmentBean bean) {
        this.alignmentBeans.add(blastAlignmentBean);
    }

    /**
     * @return Returns the dbBaseURL.
     */
    public String getDbBaseURL() {
        return dbBaseURL;
    }

    /**
     * @param dbBaseURL The dbBaseURL to set.
     */
    public void setDbBaseURL(String dbBaseURL) {
        this.dbBaseURL = dbBaseURL;
    }

    /**
     * @return Returns the hitURLid.
     */
    public String getHitURLid() {
        return hitURLid;
    }

    /**
     * @param hitURLid The hitURLid to set.
     */
    public void setHitURLid(String hitURLid) {
        this.hitURLid = hitURLid;
    }

}
