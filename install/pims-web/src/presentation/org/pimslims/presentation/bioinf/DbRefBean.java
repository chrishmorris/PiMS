/**
 * V2_0-pims-web org.pimslims.presentation.bioinf DbRefBean.java
 * 
 * @author pvt43 aka Petr Troshin
 * @date 17 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 pvt43
 * 
 * 
 */
package org.pimslims.presentation.bioinf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pimslims.bioinf.DBFetch;
import org.pimslims.bioinf.DBFetch.Viewtype;
import org.pimslims.bioinf.newtarget.RecordParser.DATABASES;
import org.pimslims.bioinf.newtarget.RecordParser.EMBLDbs;
import org.pimslims.bioinf.targets.PIMSTargetWriter;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.reference.Database;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectShortBean;

/**
 * DbRefBean
 * 
 */
public class DbRefBean extends ModelObjectShortBean implements Comparable<Object> {

    private final String url;

    private final String accession;

    private final String release;

    private final String details;

    protected String database;

    protected String subDB = "";

    protected String dbname;

    private final String dbUrl;

    public DbRefBean(final Database dbname, final ExternalDbLink dbref) {
        super(dbref);
        this.dbname = dbname.getName();
        this.setDatabase();
        this.url = dbref.getUrl();
        this.accession = dbref.getAccessionNumber();
        this.release = dbref.getRelease();
        this.details = dbref.getDetails();
        this.dbUrl = dbref.getDatabaseName().getUrl();
    }

    public String getAccession() {
        return this.accession;
    }

    public String getRelease() {
        return this.release;
    }

    public String getType() {
        return this.details;
    }

    @Override
    public String getName() {
        return PIMSTargetWriter.getDbName(this.dbname);
    }

    public String getLink() {

        if (this.database.equals(DATABASES.GenBank.toString())) {
            return DBFetch.getEntrezEntryURL(this.getAccession(), Viewtype.full);
        }
        if (this.database.equalsIgnoreCase(EMBLDbs.UniProt.toString())
            || this.database.equalsIgnoreCase("SwissProt") || this.database.equalsIgnoreCase("Swiss-Prot")) {
            return DBFetch.getUniprotEntryURL(this.getAccession(), Viewtype.full); // The latter is for compatibility with older records
        }
        if (this.database.equalsIgnoreCase(DATABASES.GO.toString())) {
            return DBFetch.getGOURL(this.getAccession());
        }
        if (this.database.equalsIgnoreCase(DATABASES.Prosite.toString())) {
            return DBFetch.getPrositeURL(this.getAccession());
        }
        if (this.database.equalsIgnoreCase(DATABASES.Pfam.toString())) {
            return DBFetch.getPfamURL(this.getAccession());
        }
        if (this.database.equalsIgnoreCase(DATABASES.SMR.toString())) {
            return DBFetch.getSMRURL(this.getAccession());
        }
        if (this.database.equalsIgnoreCase(DATABASES.TIGRFAMs.toString())) {
            return DBFetch.getTIGRFAMsURL(this.getAccession());
        }
        if (this.database.equalsIgnoreCase(DATABASES.RefSeq.toString())) {
            return DBFetch.getRefSeqURL(this.getAccession());
        }
        if (this.database.equalsIgnoreCase(DATABASES.PIR.toString())) {
            return DBFetch.getPIRURL(this.getAccession());
        }
        if (this.database.equalsIgnoreCase(DATABASES.KEGG.toString())) {
            return DBFetch.getKEGGURL(this.getAccession());
        }
        if (this.database.equalsIgnoreCase(DATABASES.SMART.toString())) {
            return DBFetch.getSMARTURL(this.getAccession());
        }
        if (this.database.equalsIgnoreCase(DATABASES.ProDom.toString())) {
            return DBFetch.getProDomURL(this.getAccession());
        }
        if (this.database.equalsIgnoreCase(DATABASES.HAMAP.toString())) {
            return DBFetch.getHAMAPURL(this.getAccession());
        }
        if (this.database.equalsIgnoreCase(DATABASES.PANTHER.toString())) {
            return DBFetch.getPantherURL(this.getAccession());
        }
        if (this.database.equalsIgnoreCase(DATABASES.UniGene.toString())) {
            return DBFetch.getUniGeneURL(this.getAccession());
        }
        if (this.database.equalsIgnoreCase(DATABASES.JCVI.toString())) {
            return DBFetch.getJCVIURL(this.getAccession());
        }
        if (this.database.equalsIgnoreCase(DATABASES.XtalPims.toString())) {
            return DBFetch.getXtalPimsURL(this.getAccession(), this.dbUrl);
        }
        if (this.database.equalsIgnoreCase(DATABASES.ISPyBDiamond.toString())) {
            return DBFetch.getISPyBDiamondURL(this.getAccession());
        }
        if (this.database.equalsIgnoreCase(DATABASES.ISPyBESRF.toString())) {
            return DBFetch.getISPyBESRFURL(this.getAccession());
        }
        if (this.database.equalsIgnoreCase(DATABASES.unspecified.toString())) {
            return "";
        }

        final String ret = DBFetch.getEMBLDBsURL(this.database, this.getAccession());
        if (null == ret) {
            return this.url;
        }

        return ret;
    }

    public static List<DbRefBean> getDBRefs(final Target target) {
        final List<DbRefBean> dbBeans = new ArrayList<DbRefBean>();
        for (final ExternalDbLink dbref : target.getExternalDbLinks()) {
            //final String db = dbref.getDbName().getName();
            if (dbref.getDetails().contains(BlastMultipleUtility.TEXT)) {
                continue; // this is manually provided URL, or temporary stored here search results 
            }
            /* Now handle these like other DbRefs
            if (db.equals(TargetBeanReader.HYPERLINK_DBNAME) || db.equals("TargetDB") || db.equals("PDB")) {
                continue; // this is manually provided URL, or temporary stored here search results 
            } */
            dbBeans.add(new DbRefBean(dbref.getDatabaseName(), dbref));
        }
        Collections.sort(dbBeans);
        return dbBeans;
    }

    public static String getTaxonomyLink(final Target target) {
        final Organism source = target.getSpecies();
        if (source == null) {
            return null;
        }

        int tid = -1;
        final String taxId = source.getNcbiTaxonomyId();
        try {
            tid = Integer.parseInt(taxId);
        } catch (final NumberFormatException e) {
            // Ignore
        }
        if (tid != -1) {
            return DBFetch.getNCBITaxonomyURL(tid);
        }
        return DBFetch.getNCBITaxonomyURL(source.getName());
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final Object o) {
        if (!(o instanceof DbRefBean)) {
            return 0;
        }
        return (((DbRefBean) o).getName().compareTo(this.getName()));
    }

    /**
     * DbRefBean.getUrl
     * 
     * @return
     */
    public String getUrl() {
        return this.url;
    }

    public String getRealUrl() {
        if (!this.url.startsWith("http://")) {
            return "http://" + this.url;
        }
        return this.url;
    }

    protected void setDatabase() {
        if (this.dbname.equals(DATABASES.GenbankNucleotide.toString())) {
            this.database = DATABASES.GenBank.toString();
            this.subDB = "Nucleotide";
        }
        if (this.dbname.equals(DATABASES.GenbankProtein.toString())) {
            this.database = DATABASES.GenBank.toString();
            this.subDB = "Protein";
        }
        if (this.dbname.equals("GeneID")) {
            this.database = DATABASES.GenBank.toString();
            this.subDB = "Gene";
        }
        if (this.database == null) {
            this.database = this.dbname;
        }
    }

}
