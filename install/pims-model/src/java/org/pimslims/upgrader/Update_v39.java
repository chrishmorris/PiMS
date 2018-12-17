/**
 * pims-dm org.pimslims.upgrader Update_v39.java
 * 
 * @author cm65
 * @date 5 Oct 2009
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.upgrader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.holder.RefHolderSource;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.persistence.HibernateUtil;
import org.pimslims.search.Paging;
import org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader;

/**
 * Update_v39
 * 
 */
public class Update_v39 extends SpecialUpgrader {

    /**
     * Constructor for Update_v39
     * 
     * @param muv
     */
    Update_v39(ModelUpdateVersionImpl muv) {
        super(muv);
    }

    /**
     * Update_v39.getVersion
     * 
     * @see org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader#getVersion()
     */
    @Override
    public int getVersion() {
        return 39;
    }

    /**
     * Update_v39.upgrade
     * 
     * @see org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader#upgrade()
     */
    @Override
    protected void upgrade() throws SQLException, ConstraintException, AccessException, AbortedException {
        final DatabaseUpdater dbu = muv.getDbUpdater();
        //rename SC
        RefDataTool.renameSampleCategory("Strain", "Competent Cells", this.muv);
        //drop old table
        if (dbu.isTableExist("acco_usergroup2members")) {
            dbu.dropTable("acco_usergroup2members");
        }
        //PIMS-2898 rename the columns for postgres DB
        if (!HibernateUtil.isOracleDB()) {
            dbu.renameColumn("CORE_ATTACHMENT", "date", "date_");
            dbu.renameColumn("MOLE_MOLECULE2RELAREOBEL", "RELATEDRESEARCHOBJECTIVEELEMENTID",
                "RELATEDROELEMENTID");
            dbu.renameColumn("MOLE_ABSTCO_SYNONYMS", "SYNONYM", "SYNONYM_");
        }
        //PIMS-2946: set protocol's isForUse to true if null
        if (!HibernateUtil.isOracleDB()) {
            dbu.sqlExecuteUpdate("Update prot_protocol set ISFORUSE=true where ISFORUSE is null");
        } else {
            dbu.sqlExecuteUpdate("Update prot_protocol set ISFORUSE=1 where ISFORUSE is null");
        }
        dbu.setColumnNotNull("prot_protocol", "ISFORUSE");

        //PIMS-2775 Odd constructor for org.pimslims.model.molecule.Construct
        dbu.setColumnCanBeNull("MOLE_CONSTRUCT", "CONSTRUCTSTATUS");

        //PIMS-1506 Order of Input Samples
        dbu.createColumn("prot_refinputsample", "order_", "int4");
        muv.flush();
        int numberofProtocol = muv.getCountOfAll(muv.getMetaClass(Protocol.class));
        //int step = 100;
        int numberofStep = numberofProtocol / 100;
        dbu.resetStmt = true;
        for (int i = 0; i <= numberofStep; i++) {

            Paging page = new Paging(i * 100, 100);

            Collection<Protocol> protocols = muv.getAll(Protocol.class, page);
            for (Protocol protocol : protocols) {
                resetIndex(protocol);
                if (!protocol.getRefInputSamples().isEmpty()) {
                    System.out.println(protocol + " processing");
                    List<RefInputSample> inSamples = protocol.getRefInputSamples();
                    Comparator<RefInputSample> comparator = new RefInputComparator<RefInputSample>();
                    Collections.sort(inSamples, comparator);
                    protocol.setRefInputSamples(inSamples);
                    for (RefInputSample inSample : inSamples) {
                        inSample.setProtocol(protocol);
                    }
                    System.out.println(protocol + " is upgraded!");
                }
            }
        }
        dbu.setColumnNotNull("prot_refinputsample", "order_");
        muv.flush();
        dbu.resetStmt = false;

        MetaClass metaClass = muv.getMetaClass(RefHolderSource.class);
        //PIMS-2807 Link between RefSampleSource and RefHolder is inappropriate
        //PIMS-2851 RefSampleSource should require RefSample
        muv.updateMetaClassAttributes(metaClass);
        muv.updateMetaRoleOfClass(metaClass);

        //move refHolder's source
        if (muv.isColumnExist("sam_refsamplesource", "refholderid")) {
            dbu.sqlExecuteUpdate("insert into HOLD_REFHOLDERSOURCE(labbookentryid,supplierid,catalognum,datapageurl,productgroupname,productname,refholderid) SELECT labbookentryid,supplierid,catalognum,datapageurl,productgroupname,productname,refholderid from sam_refsamplesource where refholderid IS NOT NULL");
            //drop old refHolderSource from refSampleSource
            dbu.sqlExecuteUpdate("delete from sam_refsamplesource WHERE refHolderid IS NOT NULL");
            dbu.sqlExecuteUpdate("delete from sam_refsamplesource WHERE refSampleid IS NULL");
            dbu.dropColumn("sam_refsamplesource", "refholderid");
            dbu.setColumnNotNull("sam_refsamplesource", "refsampleid");
        }
        //change sample's assignedTo  from person to user
        if (muv.isColumnExist("targ_researchobjective", "ownerid")) {
            dbu.createColumn("sam_sample", "userid", "int8");
            dbu.sqlExecuteUpdate("Update sam_sample set userid= (select systemclassid from acco_user where acco_user.personid=sam_sample.assigntoid and systemclassid in (select user2.systemclassid from acco_user user2 join acco_usergroup2user on acco_usergroup2user.memberuserid=user2.systemclassid join acco_permission on acco_permission.usergroupid=acco_usergroup2user.usergroupid join core_labbookentry on core_labbookentry.accessid=acco_permission.accessobjectid where core_labbookentry.dbid=sam_sample.abstractsampleid)) ");
            dbu.dropColumn("sam_sample", "assigntoid");
            dbu.renameColumn("sam_sample", "userid", "assigntoid");
            //change researchobjective's ownerid from person to user
            dbu.createColumn("targ_researchobjective", "userid", "int8");
            dbu.sqlExecuteUpdate("Update targ_researchobjective set userid= (select systemclassid from acco_user where acco_user.personid=targ_researchobjective.ownerid and systemclassid in (select user2.systemclassid from acco_user user2 join acco_usergroup2user on acco_usergroup2user.memberuserid=user2.systemclassid join acco_permission on acco_permission.usergroupid=acco_usergroup2user.usergroupid join core_labbookentry on core_labbookentry.accessid=acco_permission.accessobjectid where core_labbookentry.dbid=targ_researchobjective.labbookentryid)) ");
            dbu.dropColumn("targ_researchobjective", "ownerid");
            dbu.renameColumn("targ_researchobjective", "userid", "ownerid");
        }
        dbu.resetStmt = true;

    }

    private void resetIndex(Protocol protocol) throws SQLException {
        ResultSet rs =
            muv.getDbUpdater().sqlExecuteQuery(
                "select labbookentryid, order_ from prot_refinputsample where protocolid="
                    + protocol.getDbId());
        int i = 0;
        while (rs.next()) {
            rs.updateInt("order_", i);
            rs.updateRow();
            i++;
        }
        rs.close();

    }

    class RefInputComparator<T> implements java.util.Comparator<T> {

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object arg0, Object arg1) {
            RefInputSample pd0 = (RefInputSample) arg0;
            RefInputSample pd1 = (RefInputSample) arg1;
            return pd0.getName().compareTo(pd1.getName());
        }

    }
}
