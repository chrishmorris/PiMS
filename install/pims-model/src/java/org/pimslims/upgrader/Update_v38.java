/**
 * pims-model org.pimslims.upgrader Update_v38.java
 * 
 * @author pajanne
 * @date Jun 26, 2009
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
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
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.persistence.HibernateUtil;
import org.pimslims.search.Paging;
import org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader;

/**
 * Update_v38
 * 
 */
public class Update_v38 extends SpecialUpgrader {

    /**
     * Constructor for Update_v38
     * 
     * @param muv
     */
    protected Update_v38(ModelUpdateVersionImpl muv) {
        super(muv);
    }

    /**
     * SpecialUpgrader.getVersion
     * 
     * @see org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader#getVersion()
     */
    @Override
    public int getVersion() {
        return 38;
    }

    /**
     * SpecialUpgrader.upgrade
     * 
     * @see org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader#upgrade()
     */
    @Override
    protected void upgrade() throws SQLException, ConstraintException, AccessException, AbortedException {
        final DatabaseUpdater dbUpgrader = muv.getDbUpdater();
        /**
         * Normal upgrader will do PIMS-2764: add class Host PIMS-2788: add class Extension PIMS-466: add
         * HolderTypeSource PIMS-2144: add SimilarityHit
         */

        // PIMS-2535: remove attribute TargetGroup.namingSystem
        dbUpgrader.dropColumn("targ_targetgroup", "namingsystem");

        // PIMS-2772: rename TargetGroup.shortName to name and changed constraint 
        dbUpgrader.renameColumn("targ_targetgroup", "shortname", "name");
        if (dbUpgrader.isColumnExist("targ_targetgroup", "completename")) {
            dbUpgrader.dropColumn("targ_targetgroup", "completename");
        }

        // PIMS-2492: change ResearchObjectiveElement.whyChoosen to non mandatory
        dbUpgrader.setColumnCanBeNull("trag_researchobjectiveelement", "whychosen");

        dbUpgrader.resetStmt = false;
        // PIMS-2746: change Primer.lengthOnGene type from String to Integer
        dbUpgrader.renameColumn("mole_primer", "lengthongene", "lengthongene_string");
        dbUpgrader.createColumn("mole_primer", "lengthongene", "int4");
        if (HibernateUtil.isOracleDB())
            dbUpgrader
                .sqlExecuteUpdate("UPDATE MOLE_PRIMER SET LENGTHONGENE = to_number(LENGTHONGENE_STRING) WHERE LENGTHONGENE_STRING IS NOT NULL");
        else
            dbUpgrader
                .sqlExecuteUpdate("UPDATE mole_primer SET lengthongene = INT4(lengthongene_string) WHERE lengthongene_string IS NOT NULL");
        dbUpgrader.dropColumn("mole_primer", "lengthongene_string");

        // PIMS-1506: change Set to List for Experiment.inputSamples with extra order_ column
        dbUpgrader.createColumn("expe_inputsample", "order_", "int4");
        muv.flush();
        int numberofExperiment = muv.getCountOfAll(muv.getMetaClass(Experiment.class));
        //int step = 100;
        int numberofStep = numberofExperiment / 100;
        dbUpgrader.resetStmt = true;
        for (int i = 0; i <= numberofStep; i++) {

            Paging page = new Paging(i * 100, 100);

            Collection<Experiment> experiments = muv.getAll(Experiment.class, page);
            for (Experiment experiment : experiments) {
                resetIndex(experiment);
                System.out.println(experiment + " processing");
                if (!experiment.getInputSamples().isEmpty()) {
                    List<InputSample> inSamples = experiment.getInputSamples();
                    Comparator<InputSample> comparator = new SampleComparator();
                    Collections.sort(inSamples, comparator);
                    experiment.setInputSamples(inSamples);
                    for (InputSample inSample : inSamples) {
                        inSample.setExperiment(experiment);
                    }
                    System.out.println(experiment + " is upgraded!");
                }
            }
        }
        dbUpgrader.setColumnNotNull("expe_inputsample", "order_");
        muv.flush();
        dbUpgrader.resetStmt = false;
        // PIMS-2811: remove UserGroup.members and User.legGroups
        // PIMS-2811: rename UserGroup.leaders to memberUsers
        if (dbUpgrader.isColumnExist("acco_permission", "baseclassid")) {
            dbUpgrader.renameColumn("acco_permission", "baseclassid", "systemclassid");
        }
        if (dbUpgrader.isColumnExist("acco_user", "baseclassid")) {
            dbUpgrader.renameColumn("acco_user", "baseclassid", "systemclassid");
        }
        if (dbUpgrader.isColumnExist("acco_usergroup", "baseclassid")) {
            dbUpgrader.renameColumn("acco_usergroup", "baseclassid", "systemclassid");
        }
        dbUpgrader.createColumn("acco_usergroup", "headerid", "int8");
        dbUpgrader.setForeignKey("acco_usergroup", "acco_usergroup_header", "acco_user", "systemclassid",
            "headerid", "no action");
        // assert dbUpgrader.isTableEmpty("acco_usergroup2leaders");
        // dbUpgrader.dropTable("acco_usergroup2leaders");
        // was dbUpgrader.renameTable("acco_usergroup2members", "acco_usergroup2user");
        // but actually pims-web was using leaders role - because members role was not correctly implemented in early generated java
        if (dbUpgrader.isTableEmpty("acco_usergroup2members")) {
            dbUpgrader.dropTable("acco_usergroup2members");
        } else {
            System.err.println("WARNING - orphaned table acco_usergroup2members contains data");
        }
        dbUpgrader.resetStmt = true;
        dbUpgrader.renameTable("acco_usergroup2leaders", "acco_usergroup2user");
        dbUpgrader.renameColumn("acco_usergroup2user", "leaderid", "memberuserid");
        dbUpgrader.renameColumn("acco_usergroup2user", "ledgroupid", "usergroupid");

        // Change table name between Instrument and InstrumentType
        dbUpgrader.renameTable("inst_instty2inst", "expe_instrument2insttype");

    }

    private void resetIndex(Experiment experiment) throws SQLException {
        ResultSet rs =
            muv.getDbUpdater().sqlExecuteQuery(
                "select labbookentryid, order_ from expe_inputsample where experimentid="
                    + experiment.getDbId());
        int i = 0;
        while (rs.next()) {
            rs.updateInt("order_", i);
            rs.updateRow();
            i++;
        }
        rs.close();

    }

    class SampleComparator implements java.util.Comparator {

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object arg0, Object arg1) {
            InputSample pd0 = (InputSample) arg0;
            InputSample pd1 = (InputSample) arg1;
            return pd0.getName().compareTo(pd1.getName());
        }

    }

}
