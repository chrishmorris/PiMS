package org.pimslims.bioinf;

import java.io.Serializable;

/**
 * TopHitBean, used to store details of the Top Hits for a single target
 * from weekly Blast searches against the PDB and TargetDb, recorded as DbRefs
 *  
 * @author Susy Griffiths YSBL October 2007
 */
public class TopHitBean implements Comparable, Serializable {
    /**
     * To satisfy Serializable Interface
     */
    private static final long serialVersionUID = 1;

    /**
     * properties
     */
    //Information from the Target
    private String targetId;
    
    private int targetSeqLen;
    
    private String Details;
    
    //Information for PDB Hits
        
    private String datePDBHit;
    
    private String hitIDPDB;

    private Double expectPDB;
    
    private Double simPDB; //% identity over target length
    
    private Boolean isSolvedLocally;  //PDB Hits only

    private Boolean isSameAsPrevious;  //PDB Hits
    
    private String pdbHitSince;
        
    //Information for TargetDb Hits
    
    private String dateTDBHit;

    private String hitIDTDB;

    private Double expectTDB;
    
    private Double simTDB; //% identity over target length
    
    private Boolean isGt50;  //is simTDB > 50%
    
    private String targetDBStatus;  //TargetDB Hits
    
    private Boolean isCrystallizedOrLater;  //TargetDB Hits
    
    private Double numIdentitiesPDB;  //number of identities in PDB top match
    
    private Double numIdentitiesTDB;  //number of identities in PDB top match
    
    private Boolean isSameAsPreviousTDB;  //PDB Hits
    
    private String tdbHitSince;


    public int compareTo(Object obj) {
        if (!(obj instanceof TopHitBean)) {
            throw new ClassCastException("obj1 is not a Blast Top hit");
        }
        return 0;
    }

    /**
     * @return Returns the targetId.
     */
    public String getTargetId() {
        return this.targetId;
    }

    /**
     * @param targetId The targetId to set.
     */
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    /**
     * @return Returns the targetSeqLen.
     */
    public int getTargetSeqLen() {
        return this.targetSeqLen;
    }

    /**
     * @param targetSeqLen The targetSeqLen to set.
     */
    public void setTargetSeqLen(int targetSeqLen) {
        this.targetSeqLen = targetSeqLen;
    }

    /**
     * @return Returns the details.
     */
    public String getDetails() {
        return this.Details;
    }

    /**
     * @param details The details to set.
     */
    public void setDetails(String details) {
        this.Details = details;
    }

    /**
     * @return Returns the datePDBHit.
     */
    public String getDatePDBHit() {
        return this.datePDBHit;
    }

    /**
     * @param datePDBHit The datePDBHit to set.
     */
    public void setDatePDBHit(String datePDBHit) {
        this.datePDBHit = datePDBHit;
    }

    /**
     * @return Returns the dateTDBHit.
     */
    public String getDateTDBHit() {
        return this.dateTDBHit;
    }

    /**
     * @param dateTDBHit The dateTDBHit to set.
     */
    public void setDateTDBHit(String dateTDBHit) {
        this.dateTDBHit = dateTDBHit;
    }

    /**
     * @return Returns the expectPDB.
     */
    public Double getExpectPDB() {
        return this.expectPDB;
    }

    /**
     * @param expectPDB The expectPDB to set.
     */
    public void setExpectPDB(Double expectPDB) {
        this.expectPDB = expectPDB;
    }

    /**
     * @return Returns the expectTDB.
     */
    public Double getExpectTDB() {
        return this.expectTDB;
    }

    /**
     * @param expectTDB The expectTDB to set.
     */
    public void setExpectTDB(Double expectTDB) {
        this.expectTDB = expectTDB;
    }

    /**
     * @return Returns the hitIDPDB.
     */
    public String getHitIDPDB() {
        return this.hitIDPDB;
    }

    /**
     * @param hitIDPDB The hitIDPDB to set.
     */
    public void setHitIDPDB(String hitIDPDB) {
        this.hitIDPDB = hitIDPDB;
    }

    /**
     * @return Returns the hitIDTDB.
     */
    public String getHitIDTDB() {
        return this.hitIDTDB;
    }

    /**
     * @param hitIDTDB The hitIDTDB to set.
     */
    public void setHitIDTDB(String hitIDTDB) {
        this.hitIDTDB = hitIDTDB;
    }

    /**
     * @return Returns the simPDB.
     */
    public Double getSimPDB() {
        return this.simPDB;
    }

    /**
     * @param simPDB The simPDB to set.
     */
    public void setSimPDB(Double simPDB) {
        this.simPDB = simPDB;
    }

    /**
     * @return Returns the simTDB.
     */
    public Double getSimTDB() {
        return this.simTDB;
    }

    /**
     * @param simTDB The simTDB to set.
     */
    public void setSimTDB(Double simTDB) {
        this.simTDB = simTDB;
    }

    /**
     * @return Returns the isGt50.
     */
    public Boolean getIsGt50() {
        return this.isGt50;
    }

    /**
     * @param isGt50 The isGt50 to set.
     */
    public void setIsGt50(Boolean isGt50) {
        this.isGt50 = isGt50;
    }

    /**
     * @return Returns the isSameAsPrevious.
     */
    public Boolean getIsSameAsPrevious() {
        return this.isSameAsPrevious;
    }

    /**
     * @param isSameAsPrevious The isSameAsPrevious to set.
     */
    public void setIsSameAsPrevious(Boolean isSameAsPrevious) {
        this.isSameAsPrevious = isSameAsPrevious;
    }

    /**
     * @return Returns the pDBHitSince.
     */
    public String getPdbHitSince() {
        return this.pdbHitSince;
    }

    /**
     * @param hitSince The pDBHitSince to set.
     */
    public void setPdbHitSince(String hitSince) {
        this.pdbHitSince = hitSince;
    }

    /**
     * @return Returns the isSolvedLocally.
     */
    public Boolean getIsSolvedLocally() {
        return this.isSolvedLocally;
    }

    /**
     * @param isSolvedLocally The isSolvedLocally to set.
     */
    public void setIsSolvedLocally(Boolean isSolvedLocally) {
        this.isSolvedLocally = isSolvedLocally;
    }

    /**
     * @return Returns the isStatusChanged.
     */
    public Boolean getIsCrystallizedOrLater() {
        return this.isCrystallizedOrLater;
    }

    /**
     * @param isStatusChanged The isStatusChanged to set.
     */
    public void setIsCrystallizedOrLater(Boolean isCrystallizedOrLater) {
        this.isCrystallizedOrLater = isCrystallizedOrLater;
    }

    /**
     * @return Returns the targetDBStatus.
     */
    public String getTargetDBStatus() {
        return this.targetDBStatus;
    }

    /**
     * @param targetDBStatus The targetDBStatus to set.
     */
    public void setTargetDBStatus(String targetDBStatus) {
        this.targetDBStatus = targetDBStatus;
    }

    /**
     * @return Returns the numIdentitiesPDB.
     */
    public Double getNumIdentitiesPDB() {
        return this.numIdentitiesPDB;
    }

    /**
     * @param numIdentitiesPDB The numIdentitiesPDB to set.
     */
    public void setNumIdentitiesPDB(Double numIdentitiesPDB) {
        this.numIdentitiesPDB = numIdentitiesPDB;
    }

    /**
     * @return Returns the numIdentitiesTDB.
     */
    public Double getNumIdentitiesTDB() {
        return this.numIdentitiesTDB;
    }

    /**
     * @param numIdentitiesTDB The numIdentitiesTDB to set.
     */
    public void setNumIdentitiesTDB(Double numIdentitiesTDB) {
        this.numIdentitiesTDB = numIdentitiesTDB;
    }

    /**
     * @return Returns the isSameAsPreviousTDB.
     */
    public Boolean getIsSameAsPreviousTDB() {
        return this.isSameAsPreviousTDB;
    }

    /**
     * @param isSameAsPreviousTDB The isSameAsPreviousTDB to set.
     */
    public void setIsSameAsPreviousTDB(Boolean isSameAsPreviousTDB) {
        this.isSameAsPreviousTDB = isSameAsPreviousTDB;
    }

    /**
     * @return Returns the tdbHitSince.
     */
    public String getTdbHitSince() {
        return this.tdbHitSince;
    }

    /**
     * @param tdbHitSince The tdbHitSince to set.
     */
    public void setTdbHitSince(String tdbHitSince) {
        this.tdbHitSince = tdbHitSince;
    }

}
