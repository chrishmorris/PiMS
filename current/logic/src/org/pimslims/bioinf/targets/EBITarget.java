package org.pimslims.bioinf.targets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.pimslims.lab.Util;

/**
 * 
 * Class is designed to fill the EBI Targets Tracker (EBI TT) database (DB)
 * 
 * @author Petr Troshin <br>
 *         Created on 01-Apr-2005
 * @see LeedsHTMLTargetsParser.LeedsTarget
 * @link uk.ac.mpsi.Util
 * @deprecated This class was used to transfer targets from EBI target tracker to PIMS since then there were
 *             changes in PIMS regarding target recording which are not reflected in this class. This class is
 *             no longer supported
 */
@Deprecated
public class EBITarget {

    /**
     * Array of EBITargetStatus objects every targets may have unlimited number of statuses which represented
     * by this object
     */
    EBITargetStatus[] eBITargetStatus;

    // Staff for filling EBI TT
    /**
     * These all should be the same as declared in partner.xml EBI Target Tracker template Project Name
     */
    static final String projectName = "MPSI";

    /**
     * Project organisation
     */
    static final String projectOrg = "MPSI consortium";

    /**
     * Project organisation code (something that is added to all target names)
     */
    static final String projectCode = "P";

    /**
     * Project organisation web site
     */
    static final String projectWebAddr = "http://www.mpsi.ac.uk/";

    /**
     * Project head contact details
     */
    static final String projectHead = "Prof. Neil Isaacs, neil@chem.gla.ac.uk";

    /**
     * Default value which goes to the database in case if some field is undefined
     */
    static final String defFill = "\"";

    /**
     * Project web server address
     */
    static final String serverPath = "http://www.mpsi.ac.uk/";

    // Database connection static settings
    /*
     * static final String dBserverName = "148.79.117.119"; // kumlnx.mpsi.ac.uk static final String
     * dBserverPort = "5432"; static final String dBserverDbName = "tracker"; static final String
     * dBserverDbUser = "petr"; static final String dBserverDbUserPw = "MPSIMirTr";
     */
    /**
     * Database connection details DB server name or IP address
     */
    static final String dBserverName = ""; // 148.79.117.113 // www.mpsi.ac.uk

    /**
     * DB server port
     */
    static final String dBserverPort = "5432";

    /**
     * DB server database name
     */
    static final String dBserverDbName = ""; // mpsiweb

    /**
     * DB server user
     */
    static final String dBserverDbUser = ""; // mpsi

    /**
     * DB server password
     */
    static final String dBserverDbUserPw = ""; // mPsI314

    /**
     * WorkPackage Type settings must correspond with EBI TT workpackages These values should be the same as
     * in the EBI TT workpackage.xml template For MPSI consortsium the default values are the following:
     * 
     * <PRE>
     * 	 &quot;ABC Substrate-binding proteins&quot;
     * 	 &quot;ABC ATP-binding proteins&quot;
     * 	 &quot;ABC Regulatory proteins&quot;
     * 	 &quot;ABC Porins&quot;
     * 	 &quot;ABC Substrate specific proteins&quot;
     * 	 &quot;1.A. a-Type channels&quot;
     * 	 &quot;1.B. b-Barrel porins&quot;
     * 	 &quot;1.C. Pore-forming toxins (proteins and peptides)&quot;
     * 	 &quot;1.D. Non-ribosomally synthesized channels&quot;
     * 	 &quot;1.E. Holins&quot;
     * 	 &quot;2.A. Porters (uniporters, symporters, antiporters)&quot;
     * 	 &quot;2.B. Nonribosomally synthesized porters&quot;
     * 	 &quot;2.C. Ion-gradient-driven energizers&quot;
     * 	 &quot;3.A. P-P-bond-hydrolysis-driven transporters&quot;
     * 	 &quot;3.B. Decarboxylation-driven transporters&quot;
     * 	 &quot;3.C. Methyltransfer-driven transporters&quot;
     * 	 &quot;3.D. Oxidoreduction-driven transporters&quot;
     * 	 &quot;3.E. Light absorption-driven transporters&quot;
     * 	 &quot;4.A. Phosphotransfer-driven group translocators&quot;
     * 	 &quot;5.A. Transmembrane 2-Electron Transfer Carriers&quot;
     * 	 &quot;5.B. Transmembrane 1-Electron Transfer Carriers&quot;
     * 	 &quot;8.A. Auxiliary transport proteins&quot;
     * 	 &quot;9.A. Recognized transporters of unknown biochemical mechanism&quot;
     * 	 &quot;9.B. Putative uncharacterized transport proteins&quot;
     * 	 &quot;9.C. Functionally characterized transporters lacking identified sequences&quot;
     * </PRE>
     * 
     * These values are partly TCDB classification.
     */
    static final HashMap<String, String> classWpackage = new HashMap<String, String>();
    static {
        classWpackage.put("ABC.S", "ABC Substrate-binding proteins");
        classWpackage.put("ABC.ATP", "ABC ATP-binding proteins");
        classWpackage.put("ABC.R", "ABC Regulatory proteins");
        classWpackage.put("ABC.P", "ABC Porins");
        classWpackage.put("ABC.SS", "ABC Substrate specific proteins");
        classWpackage.put("1.A.", "1.A. a-Type channels");
        classWpackage.put("1.B.", "1.B. b-Barrel porins");
        classWpackage.put("1.C.", "1.C. Pore-forming toxins (proteins and peptides)");
        classWpackage.put("1.D.", "1.D. Non-ribosomally synthesized channels");
        classWpackage.put("1.E.", "1.E. Holins");
        classWpackage.put("2.A.", "2.A. Porters (uniporters, symporters, antiporters)");
        classWpackage.put("2.B.", "2.B. Nonribosomally synthesized porters");
        classWpackage.put("2.C.", "2.C. Ion-gradient-driven energizers");
        classWpackage.put("3.A.", "3.A. P-P-bond-hydrolysis-driven transporters");
        classWpackage.put("3.B.", "3.B. Decarboxylation-driven transporters");
        classWpackage.put("3.C.", "3.C. Methyltransfer-driven transporters");
        classWpackage.put("3.D.", "3.D. Oxidoreduction-driven transporters");
        classWpackage.put("3.E.", "3.E. Light absorption-driven transporters");
        classWpackage.put("4.A.", "4.A. Phosphotransfer-driven group translocators");
        classWpackage.put("5.A.", "5.A. Transmembrane 2-Electron Transfer Carriers");
        classWpackage.put("5.B.", "5.B. Transmembrane 1-Electron Transfer Carriers");
        classWpackage.put("8.A.", "8.A. Auxiliary transport proteins");
        classWpackage.put("9.A.", "9.A. Recognized transporters of unknown biochemical mechanism");
        classWpackage.put("9.B.", "9.B. Putative uncharacterized transport proteins");
        classWpackage
            .put("9.C.", "9.C. Functionally characterized transporters lacking identified sequences");
    }

    // EBI TT data fields
    /**
     * Target ID <br>
     * Example UM_0001
     */
    String targetID;

    /**
     * WorkPackage <br>
     * Example "3.A.P-P-bond-hydrolysis-driven transporters"
     * 
     * @see #classWpackage
     */
    String workpackage;

    /**
     * Protein sequence
     */
    String sequence;

    /**
     * Protein name <br>
     * Example ParA
     */
    String proteinName;

    /**
     * Organism species <br>
     * Example E.coli
     */
    String species;

    /**
     * Protein database name <br>
     * Example GENBANK
     */
    String database;

    /**
     * accession number of the protein from databaseID <br>
     * Example AE009876
     */
    String databaseID;

    /**
     * Annotation link
     */
    String annotationUrl;

    /**
     * Generated field
     */
    String allStaffInfo;

    /**
     * By default one EBITargetStatus object is created together with EBITarget object. This is done to help
     * import the new targets to the database. If the EBITarget is used to copy some values from one TT
     * database to another the EBITargetStatus array should be redefined. All values should be set directly
     * after EBITarget object is created.
     */
    public EBITarget() {
        EBITargetStatus eTstatus = new EBITargetStatus();
        eBITargetStatus = new EBITargetStatus[1];
        eBITargetStatus[0] = eTstatus;
    }

    /**
     * EBITargetStatus class represents the status of the the EBI target.
     * 
     * @author Petr Troshin
     */
    public static class EBITargetStatus {
        /**
         * Default preffered date
         */
        static final String prefDate = "10-04-2005";

        /**
         * Default Assess rights
         */
        static final String accessionRights = "PUBLIC";

        /**
         * Default progress stage
         */
        static final String progressStage = "Selected";

        /**
         * Work Progress stage <BR>
         * The value of this field overrides the #progressStage value.
         */
        String progressionStage;

        /**
         * The date when the entry is entered <BR>
         * The value of this field overrides the #prefDate value.
         */
        String preferredDate;

        /**
         * Access rights PUBLIC/PRIVATE <BR>
         * The value of this field overrides the #accessionRights value.
         */
        String accessRight; // 

        /**
         * Additional remarks
         */
        String additionalRemarks;

        public EBITargetStatus() {/* empty */
        }

        /**
         * Method returns progress stage
         * 
         * @return if #progressionStage is defined it is returned over wise default #progressStage is returned
         */
        protected String getProgressStage() {
            if (Util.isEmpty(progressionStage)) {
                return progressStage;
            }
            return progressionStage;
        }

        /**
         * Method returns progress stage
         * 
         * @return if #preferredDate is defined it is returned over wise default #prefDate is returned
         */
        protected String getPrefferedDate() {
            if (Util.isEmpty(preferredDate)) {
                return prefDate;
            }
            return preferredDate;
        }

        /**
         * Method returns progress stage
         * 
         * @return if #accessRight is defined it is returned over wise default #accessionRights is returned
         */
        private String getAccessRights() {
            if (Util.isEmpty(accessRight)) {
                return accessionRights;
            }
            return accessRight;
        }

        /**
         * Method returns progress stage
         * 
         * @return if #additionalRemarks is defined it is returned over wise default #defFill is returned
         */
        protected String getAdditionalRemarks() {
            if (Util.isEmpty(additionalRemarks)) {
                return defFill;
            }
            return additionalRemarks;
        }

        /**
         * Method writes EBITarget to EBI TT database
         * 
         * @throws SQLException
         */
        public void writeToDb(int targetNum, String targetID) throws SQLException {
            Connection con = getPGSQLConnection();
            String insTargetsStatus = "INSERT INTO nspntarget_status VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pstTargetsStatus = con.prepareStatement(insTargetsStatus);
            pstTargetsStatus.setInt(1, targetNum);
            pstTargetsStatus.setString(2, targetID);
            pstTargetsStatus.setString(3, getAccessRights());
            pstTargetsStatus.setString(4, getProgressStage());
            pstTargetsStatus.setString(5, getPrefferedDate());
            pstTargetsStatus.setString(6, projectCode);
            pstTargetsStatus.setString(7, projectOrg);
            pstTargetsStatus.setString(8, projectName);
            pstTargetsStatus.setString(9, projectWebAddr);
            pstTargetsStatus.setString(10, projectHead);
            pstTargetsStatus.setString(11, getAdditionalRemarks());
            pstTargetsStatus.execute();
            pstTargetsStatus.close();

        }

        /**
         * Standard String representation of a EBITargetStatus
         * 
         * @return String
         */
        @Override
        public String toString() {
            String eBITargetStatus = "progressionStage:" + progressionStage + "\n";
            eBITargetStatus += "preferredDate:" + preferredDate + "\n";
            eBITargetStatus += "accessRight:" + accessRight + "\n";
            eBITargetStatus += "additionalRemarks:" + additionalRemarks + "\n";
            return eBITargetStatus;
        }

        public void validate() {
            if (Util.isEmpty(additionalRemarks)) {
                additionalRemarks = defFill;
            }
            if (Util.isEmpty(preferredDate)) {
                preferredDate = prefDate;
            }
            if (Util.isEmpty(progressionStage)) {
                progressionStage = progressStage;
            }
            if (Util.isEmpty(accessRight)) {
                accessRight = accessionRights;
            }
        }

        public boolean isChanged() {
            if (this.accessRight.equals(accessionRights) && this.preferredDate.equals(prefDate)
                && this.progressionStage.equals(progressStage)) {
                return false;
            }
            return true;
        }

    } // EBITargetStatus class

    /**
     * Standard String representation of the object
     * 
     * @return String representation of EBITargetStatus object
     */
    @Override
    public String toString() {
        String eBITarget = "tagretID:" + targetID + "\n";
        eBITarget += "workpackage:" + workpackage + "\n";
        eBITarget += "sequence:" + sequence + "\n";
        eBITarget += "proteinName:" + proteinName + "\n";
        eBITarget += "species:" + species + "\n";
        eBITarget += "database:" + database + "\n";
        eBITarget += "databaseID:" + databaseID + "\n";
        eBITarget += "annotationUrl:" + annotationUrl + "\n";
        eBITarget += "allStaffInfo:" + allStaffInfo + "\n";
        String status = "";
        for (int i = 0; i < eBITargetStatus.length; i++) {
            status += eBITargetStatus[i].toString();
        }
        return eBITarget + status;
    }

    /**
     * Check the mandatory fields which should be filled before writing the Target to the Database
     * 
     * @return true if all mandatory values are found, false otherwise
     */
    public boolean isValid() {
        if (Util.isEmpty(targetID)) {
            return false;
        }
        if (Util.isEmpty(proteinName)) {
            return false;
        }
        if (Util.isEmpty(sequence)) {
            return false;
        }
        if (Util.isEmpty(species)) {
            return false;
        }
        if (Util.isEmpty(databaseID)) {
            return false;
        }
        if (Util.isEmpty(workpackage)) {
            return false;
        }
        if (Util.isEmpty(database)) {
            return false;
        }
        if (Util.isEmpty(annotationUrl)) {
            return false;
        }
        return true;
    }

    /**
     * Method does specific for EBI TT database staff like adding spaces and letters as delimiters If some not
     * mandatory field has a value the value will be saved otherwise default value is added This method called
     * before writing of the EBITarget to the Database
     */
    private void validate() {

        for (int i = 0; i < eBITargetStatus.length; i++) {
            eBITargetStatus[i].validate();
        }
        targetID = projectCode + "_" + targetID;
        if (Util.isEmpty(database)) {
            database = defFill;
        } else {
            database = " # " + database;
        }
        if (Util.isEmpty(databaseID)) {
            databaseID = defFill;
        } else {
            databaseID = " # " + databaseID.trim();
        }
    }

    /**
     * Construct the AllStaffInfo TT database field
     * 
     * @return String AllStaffInfo field
     */
    private String getAllStafInfo() {
        // Selected 01-01-2001 P1 MPSI consortium MPSI http://www.mpsi.ac.uk/
        // Doctor Peter Troshin, p.v.troshin.ac.uk Additional remarks
        String allStaff = "", stage = "", date = "", remarks = "";
        for (int i = 0; i < eBITargetStatus.length; i++) {
            stage = eBITargetStatus[i].getProgressStage();
            date = eBITargetStatus[i].getPrefferedDate();
            remarks = eBITargetStatus[i].getAdditionalRemarks();
            allStaff +=
                stage + " " + date + " " + projectCode + " " + projectOrg + " " + projectName + " "
                    + projectWebAddr + " " + projectHead + " " + remarks;
        }
        if (Util.isEmpty(allStaffInfo)) {
            return allStaff;
        }
        return allStaffInfo;
    }

    /**
     * Method writes EBITarget to the database
     * 
     * @throws SQLException
     */
    public void writeToDb() throws SQLException {
        this.validate();
        Connection con = getPGSQLConnection();
        String insTargets = "INSERT INTO nspntarget VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstTargets = con.prepareStatement(insTargets);
        int rowId = getLastTargetsId(con);
        pstTargets.setInt(1, rowId);
        pstTargets.setString(2, targetID);
        pstTargets.setString(3, workpackage);
        pstTargets.setString(4, sequence);
        pstTargets.setString(5, proteinName);
        pstTargets.setString(6, species);
        pstTargets.setString(7, database);
        pstTargets.setString(8, null); // Should be empty
        pstTargets.setString(9, databaseID);
        pstTargets.setString(10, annotationUrl);
        pstTargets.setString(11, defFill); // Should be filled with "
        pstTargets.setString(12, defFill); // defFill //Should be filled with "
        pstTargets.setString(13, getAllStafInfo()); //
        pstTargets.execute();
        pstTargets.close();
        for (int i = 0; i < eBITargetStatus.length; i++) {
            eBITargetStatus[i].writeToDb(rowId, targetID);
        }
        con.close();
    }

    /**
     * Get next database ID
     * 
     * @param con DB connection
     * @return next free TT DB id
     * @throws SQLException
     */
    private static int getLastTargetsId(Connection con) throws SQLException {
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("select nextval( 'ntars_incrmnt1' )");
        rs.next();
        return rs.getInt(1);
    }

    /**
     * Get PGSQL connection in order to establish connection it uses static values defined in this class
     * 
     * @return connection to PGSQL DB server
     */
    public static Connection getPGSQLConnection() {
        Connection pGConn = null;
        try {
            Class.forName("org.postgresql.Driver");
            // LIMS Server connection String url =
            // "jdbc:postgresql://148.79.162.81:5432";
            String url = "jdbc:postgresql://" + dBserverName + ":" + dBserverPort + "/" + dBserverDbName;
            // url, username, password
            pGConn = DriverManager.getConnection(url, dBserverDbUser, dBserverDbUserPw); // "petr","MPSIMirTr"
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException sqe) {
            sqe.printStackTrace();
            System.err.println("Couldn't access the database!");
        }
        return pGConn;
    }

    /**
     * Get PGSQL connection all information about connection must be provided
     * 
     * @return connection to PGSQL DB server
     */

    public static Connection getPGSQLConnection(String dBserverName, String dBserverPort,
        String dBserverDbName, String username, String passwd) {
        Connection pGConn = null;
        try {
            Class.forName("org.postgresql.Driver");
            // LIMS Server connection String url =
            // "jdbc:postgresql://148.79.162.81:5432";
            String url = "jdbc:postgresql://" + dBserverName + ":" + dBserverPort + "/" + dBserverDbName;
            // url, username, password
            pGConn = DriverManager.getConnection(url, username, passwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException sqe) {
            sqe.printStackTrace();
            System.err.println("Couldn't access the database!");
        }
        return pGConn;
    }

    /**
     * Method extracts the sequence information for all targets in the database together with target and
     * protein names and writes it to the file in FASTA format. This file used by EBI TT in order to perform
     * sequence based searches. After generation it should be put to the appropriate location in EBI TT.
     * <p>
     * Generated information example:
     * 
     * <PRE>
     * &gt;MPSI consortium:P_UL0114 | Name: panF | Status: PCR
     * MTMQLEVILPLVAYLVVVFGISVYAMRKRSTGTFLNEYFLGSRSMGGIVLAMTLTATYISASSFIGGPGAAYKYGLGWVL
     * </PRE>
     * 
     * @param fileName The File name to write the generated library
     */
    public static void makeFasta(String fileName) {
        String fasta = "";
        Connection c = getPGSQLConnection();
        try {
            Statement st = c.createStatement();
            String query = "select spntrgid, spntrprname, spntrseq  from nspntarget";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String tname = rs.getString(1);
                String pname = rs.getString(2);
                String sequence = rs.getString(3);
                String header = ">MPSI consortium:" + tname + " | Name: " + pname + " | Status: SELECTED";
                sequence = Util.getFormatedSequence(sequence);
                fasta += header + sequence + "\n\n";
            }
            Util.writeToFile(fasta, fileName);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }

    /**
     * Method for coping full EBI TT entries from one database to another The targets will be written to the
     * database configured in EBITarget class which is default database
     * 
     * @param dbconSource - source of the EBI TT data
     */
    public static void eBITTDBEntriesCopy(Connection dbconSource) {
        int id = -1;
        ResultSet rsStatus = null;
        try {
            Statement stTargets = dbconSource.createStatement();
            Statement stStatus = dbconSource.createStatement();

            String query = "select * from nspntarget where spntrgid like 'P_UM100%' ";
            ResultSet rsTargets = stTargets.executeQuery(query);

            while (rsTargets.next()) {
                EBITarget ebt = new EBITarget();
                id = rsTargets.getInt(1);
                ebt.targetID = (rsTargets.getString(2)).substring(2);
                ebt.workpackage = rsTargets.getString(3);
                ebt.sequence = rsTargets.getString(4);
                ebt.proteinName = rsTargets.getString(5);
                ebt.species = rsTargets.getString(6);
                ebt.database = rsTargets.getString(7).substring(3);
                ebt.databaseID = rsTargets.getString(9).substring(2);
                ebt.annotationUrl = rsTargets.getString(10);
                ebt.allStaffInfo = rsTargets.getString(13);

                String queryS = "select * from nspntarget_status where tabls_id=" + id;

                rsStatus = stStatus.executeQuery(queryS);
                Collection<Object> ar = new ArrayList<Object>();

                while (rsStatus.next()) {
                    EBITarget.EBITargetStatus etargetStatus = new EBITarget.EBITargetStatus();
                    etargetStatus.accessRight = rsStatus.getString(3);
                    etargetStatus.progressionStage = rsStatus.getString(4);
                    etargetStatus.preferredDate = rsStatus.getString(5);
                    etargetStatus.additionalRemarks = rsStatus.getString(11);
                    ar.add(etargetStatus);
                    /*
                     * getString(6, projectCode); //Default getString(7, projectOrg); //Default getString(8,
                     * projectName); //Default getString(9, projectWebAddr); //Default getString(10,
                     * projectHead); //Default
                     */
                }
                ebt.eBITargetStatus = ar.toArray(new EBITarget.EBITargetStatus[ar.size()]);
                ebt.writeToDb();
            }
            rsTargets.close();
            rsStatus.close();
            stTargets.close();
            stStatus.close();
            dbconSource.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }

    /**
     * Returns all the targets from the database
     * 
     * @param dbconSource
     * 
     */
    public static ArrayList getEBITargets(Connection dbconSource) {
        ArrayList<EBITarget> allTargets = new ArrayList<EBITarget>(400);
        int id = -1;
        ResultSet rsStatus = null;
        try {
            Statement stTargets = dbconSource.createStatement();
            Statement stStatus = dbconSource.createStatement();

            String query = "select * from nspntarget ";
            ResultSet rsTargets = stTargets.executeQuery(query);

            while (rsTargets.next()) {
                EBITarget ebt = new EBITarget();
                id = rsTargets.getInt(1);
                ebt.targetID = (rsTargets.getString(2)).substring(2);
                ebt.workpackage = rsTargets.getString(3);
                ebt.sequence = rsTargets.getString(4);
                ebt.proteinName = rsTargets.getString(5);
                ebt.species = rsTargets.getString(6);
                ebt.database = rsTargets.getString(7).substring(3);
                ebt.databaseID = rsTargets.getString(9).substring(2);
                ebt.annotationUrl = rsTargets.getString(10);
                ebt.allStaffInfo = rsTargets.getString(13);

                String queryS = "select * from nspntarget_status where tabls_id=" + id;

                rsStatus = stStatus.executeQuery(queryS);
                Collection<Object> ar = new ArrayList<Object>();

                while (rsStatus.next()) {
                    EBITarget.EBITargetStatus etargetStatus = new EBITarget.EBITargetStatus();
                    etargetStatus.accessRight = rsStatus.getString(3);
                    etargetStatus.progressionStage = rsStatus.getString(4);
                    etargetStatus.preferredDate = rsStatus.getString(5);
                    etargetStatus.additionalRemarks = rsStatus.getString(11);
                    ar.add(etargetStatus);
                    /*
                     * getString(6, projectCode); //Default getString(7, projectOrg); //Default getString(8,
                     * projectName); //Default getString(9, projectWebAddr); //Default getString(10,
                     * projectHead); //Default
                     */
                }
                ebt.eBITargetStatus = ar.toArray(new EBITarget.EBITargetStatus[ar.size()]);
                allTargets.add(ebt);
            }
            rsTargets.close();
            rsStatus.close();
            stTargets.close();
            stStatus.close();
            dbconSource.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return allTargets;
    }

    /**
     * Check whether any changes to EBITarget.EBITargetStatus
     * 
     * @return true in if only one status of the target was changed, if no statuses was changed return false
     */
    public boolean isChanged() {
        for (int i = 0; i < this.eBITargetStatus.length; i++) {
            EBITargetStatus status = this.eBITargetStatus[i];
            if (status.isChanged()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param connection
     * @return all changed targets from the database
     * @see EBITarget.EBITargetStatus.isChanged()
     */
    public static ArrayList getChangedTargets(Connection connection) {
        ArrayList ebitgs = getEBITargets(connection);
        ArrayList<Object> changedTargets = new ArrayList<Object>();
        // int count = 0 ;
        for (Iterator iter = ebitgs.iterator(); iter.hasNext();) {
            EBITarget target = (EBITarget) iter.next();
            if (target.isChanged()) {
                changedTargets.add(target);
            }
        }
        return changedTargets;
    }

    /**
     * Current connection parameters
     * 
     * @return Connection
     */
    public static Connection getMPSIDBConnection() {
        return EBITarget.getPGSQLConnection("www.mpsi.ac.uk", "5432", "mpsiweb", "petr", "314mPsI");
    }

    /**
     * Generate FASTA sequence library for EBI TT
     * 
     * @param args - not in use
     */
    public static void main(String[] args) {

        // Generate EBI TT FASTA sequence library
        // EBITarget.makeFasta("c:/P_MPSI_consortium.fasta");

        // Selective copy EBITT database entries from one DB to another
        // eBITTDBEntriesCopy(
        // EBITarget.getPGSQLConnection("148.79.117.119",
        // "5432", "tracker",
        // "petr", "MPSIMirTr")
        // );

    }
}
