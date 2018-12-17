package org.pimslims.embl;

/**
 * @author cm65
 * 
 */
public class HamburgOrfBean {

    private final String name;

    private final String organism;

    // note: sample files also contain empty <taxoid> element

    private final String proteinSequence;

    /**
     * e.g. Swissprot
     */
    private final String database;

    /**
     * Database access number
     */
    private final String accessId;

    private String dnaSequence;

    public HamburgOrfBean(String name, String proteinSequence, String organism,
            String database, String accessId) {
        super();
        this.name = name;
        this.organism = organism;
        this.proteinSequence = proteinSequence;
        this.database = database;
        this.accessId = accessId;
    }

    public String getName() {
        return name;
    }

    public String getOrganism() {
        return organism;
    }

    public String getProteinSequence() {
        return proteinSequence;
    }

    public String getDatabase() {
        return database;
    }

    public String getAccessId() {
        return accessId;
    }

    public String getDnaSequence() {
        return this.dnaSequence;
    }

    public void setDnaSequence(String dnaSequence) {
        this.dnaSequence = dnaSequence;
    }

}
