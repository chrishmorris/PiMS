package org.pimslims.embl;

public class DbLinkBean {

    private final String accession;
    private final String dbName;

    public DbLinkBean(String dbName, String accession) {
        super();
        this.accession = accession;
        this.dbName = dbName;
    }

    public String getAccession() {
        return this.accession;
    }

    public String getDbName() {
        return this.dbName;
    }

}
