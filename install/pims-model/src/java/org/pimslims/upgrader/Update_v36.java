/**
 * org.pimslims.upgrader Upate_v28.java
 * 
 * @date 22-May-2007 08:24:11
 * 
 * @author Bill Lin
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 1.3
 * 
 *           Copyright (c) 2006
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.upgrader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaClassImpl;
import org.pimslims.model.core.Annotation;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.Citation;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.Note;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.reference.PublicEntry;
import org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader;

/**
 * 
 * DM change 32: • change the protocol's parameter definition from Collection to List
 * 
 * DM change 32: • new package for schedule
 */
public class Update_v36 extends SpecialUpgrader {

    public Update_v36(final ModelUpdateVersionImpl muv) {
        super(muv);
    }

    @Override
    public int getVersion() {
        return 36;
    }

    @Override
    protected void upgrade() throws SQLException, ConstraintException, AccessException {
        final DatabaseUpdater dup = muv.getDbUpdater();

        preProcess(dup);
        processOrganism(dup);
        processCitation(dup);
        processExternalDbLink(dup);
        processAnnotation(dup);
        processEntryClass(dup);
        processAccess(dup);
        processNamelist(dup);
        processDetails(dup);
        processMPSIExperiment(dup);
        processCreator(dup);
        processPrimer(dup);
        processProtocol(dup);

        // rm in Citation attr casAbstractCode, doi, medlineUiCode, pubMedId. 
        // rm Citation.keywords
        dup.dropColumn("core_citation", "doi");
        dup.dropColumn("core_citation", "medlineuicode");
        dup.dropColumn("core_citation", "pubMedId");
        dup.dropTable("core_citation_keywords");

        //drop old tabls for applicationData
        dup.dropTable("core_appdataboolean");
        dup.dropTable("core_appdatadouble");
        dup.dropTable("core_appdatafloat");
        dup.dropTable("core_appdataint");
        dup.dropTable("core_appdatalong");
        dup.dropTable("core_appdatastring");
        dup.dropTable("core_applicationdata");
        muv.flush();
    }

    /**
     * @param dup
     * @throws SQLException
     */
    private void processMPSIExperiment(final DatabaseUpdater dup) throws SQLException {
        Boolean isMPSI = false;
        //fix parameter value 
        ResultSet rs =
            dup.sqlExecuteQueryIgnorCase("select * from expe_parameter where value like 'org.pimslims.pojo.experiment.Experiment:%'");
        while (rs.next()) {
            String value = rs.getString("value");
            value = value.replace("org.pimslims.pojo.experiment.Experiment", Experiment.class.getName());
            rs.updateString("value", value);
            rs.updateRow();
            isMPSI = true;
        }
        //fix 
        if (isMPSI) {
            //remove invalid experiment hook
            dup.createColumn("expe_parameter", "expid", "int8");
            rs =
                dup.sqlExecuteQueryIgnorCase("select * from expe_parameter where value like 'org.pimslims.model.experiment.Experiment:%'");
            while (rs.next()) {
                String value = rs.getString("value");
                value = value.replace("org.pimslims.model.experiment.Experiment:", "");
                final Integer expid = new Integer(value);
                rs.updateInt("expid", expid);
                rs.updateRow();
                isMPSI = true;
            }
            dup.sqlExecuteUpdate("delete from expe_parameter where expid is not null and (select exp.baseclassid from expe_parameter as p join expe_experiment as exp on exp.baseclassid=p.expid where expid is not null and p.expid is not null and p.expid=expid and p.baseclassid=expe_parameter.baseclassid) is null ");
            dup.dropColumn("expe_parameter", "expid");

            //set operatorid
            dup.sqlExecuteUpdate("update expe_experiment set operatorid= creatorid");

        }
    }

    /**
     * @param dup
     * @throws SQLException
     */
    private void processProtocol(final DatabaseUpdater dup) throws SQLException {
        dup.createColumn("prot_parameterdefinition", "ISRESULT", "BOOLEAN");
        dup.setDefaultValue("prot_parameterdefinition", "ISRESULT", false);
        dup.setColumnNotNull("prot_parameterdefinition", "ISRESULT");

        dup.dropColumn("prot_parameterdefinition", "stepid");
        dup.dropTable("prot_procedure2instty");
        dup.dropTable("prot_protocol2resude");
        dup.dropTable("prot_protocol2setude");
        if (dup.isColumnExist("prot_parameterdefinition", "mandatory")) {
            dup.dropColumn("prot_parameterdefinition", "mandatory");
        }

    }

    /**
     * @param dup
     * @throws SQLException
     */
    private void processAccess(final DatabaseUpdater dup) throws SQLException {
        System.out.println("Processing  accessID of core_labbookentry");
        //copy accessid from base to labBookEntry
        dup.sqlExecuteUpdate("update core_labbookentry set accessid=core_baseclass.accessid from core_baseclass where core_labbookentry.baseclassid=core_baseclass.dbid");
        //drop acessid from base
        dup.dropColumn("core_baseclass", "accessid");

    }

    /**
     * @param dup
     * @throws SQLException
     */
    private void processNamelist(final DatabaseUpdater dup) throws SQLException {
        final List<String> tableNames = dup.getAllTableNames();
        for (final String tableName : tableNames) {
            if (tableName.equalsIgnoreCase("core_namelist")) {
                continue;
            } else if (tableName.equalsIgnoreCase("revisionnumber")) {
                continue;
            } else if (dup.isColumnExist(tableName, "name")) {
                final String pmID = dup.getPrimaryColumn(tableName);
                System.out.println("Copying names from " + tableName + " to Core_NameList");
                moveName(dup, pmID, tableName);
            }
        }

    }

    /**
     * @param dup
     * @param pmID
     * @throws SQLException
     */
    private void moveName(final DatabaseUpdater dup, final String pmID, final String tableName)
        throws SQLException {

        dup.sqlExecuteUpdate("insert into core_namelist (dbid,name,parententryid) select nextval('hibernate_sequence')-1, name, "
            + pmID + " from " + tableName + " where name is not null");

    }

    /**
     * @param dup
     * @throws SQLException
     */
    private void processPrimer(final DatabaseUpdater dup) throws SQLException {
        System.out.println("Processing  details of primer");
        dup.sqlExecuteUpdateIgnorCase("update MOLE_primer set restrictionsite=(select core_appdatastring.value from core_applicationdata join core_appdatastring on core_appdatastring.applicationdataid=core_applicationdata.baseclassid where core_applicationdata.keyword='Restriction sites' and core_applicationdata.ownerid=MOLE_primer.moleculeid)");
        dup.sqlExecuteUpdateIgnorCase("update MOLE_primer set particularity=(select core_appdatastring.value from core_applicationdata join core_appdatastring on core_appdatastring.applicationdataid=core_applicationdata.baseclassid where core_applicationdata.keyword='Particularity' and core_applicationdata.ownerid=MOLE_primer.moleculeid)");
        dup.sqlExecuteUpdateIgnorCase("update MOLE_primer set meltingtemperaturegene=(select to_number(core_appdatastring.value,'99999D99999') from core_applicationdata join core_appdatastring on core_appdatastring.applicationdataid=core_applicationdata.baseclassid where core_applicationdata.keyword='Tm gene' and core_applicationdata.ownerid=MOLE_primer.moleculeid)");
        dup.sqlExecuteUpdateIgnorCase("update MOLE_primer set meltingtemperatureseller=(select to_number(core_appdatastring.value,'99999D99999') from core_applicationdata join core_appdatastring on core_appdatastring.applicationdataid=core_applicationdata.baseclassid where core_applicationdata.keyword='Tm seller' and core_applicationdata.ownerid=MOLE_primer.moleculeid)");
        dup.sqlExecuteUpdateIgnorCase("update MOLE_primer set gcgene=(select core_appdatastring.value from core_applicationdata join core_appdatastring on core_appdatastring.applicationdataid=core_applicationdata.baseclassid where core_applicationdata.keyword='GC gene' and core_applicationdata.ownerid=MOLE_primer.moleculeid)");
        dup.sqlExecuteUpdateIgnorCase("update MOLE_primer set lengthongene=(select core_appdatastring.value from core_applicationdata join core_appdatastring on core_appdatastring.applicationdataid=core_applicationdata.baseclassid where core_applicationdata.keyword='Length on the gene' and core_applicationdata.ownerid=MOLE_primer.moleculeid)");
        dup.sqlExecuteUpdateIgnorCase("update MOLE_primer set opticaldensity=(select core_appdatastring.value from core_applicationdata join core_appdatastring on core_appdatastring.applicationdataid=core_applicationdata.baseclassid where core_applicationdata.keyword='OD' and core_applicationdata.ownerid=MOLE_primer.moleculeid)");
        //drop old primer's application data
        dup.sqlExecuteUpdate("delete from core_appdatastring where applicationdataid in (select baseclassid from core_applicationdata where core_applicationdata. ownerid in (select moleculeid from MOLE_primer))");
        dup.sqlExecuteUpdate("delete from core_applicationdata where core_applicationdata.ownerid in (select moleculeid from MOLE_primer)");

    }

    /**
     * @param dup
     * @throws SQLException
     */
    private void processCreator(final DatabaseUpdater dup) throws SQLException {
        System.out.println("Processing creater details of experiments & samples");
        //annotation
        dup.createColumn("core_annotation", "creatorUser", "int8");
        dup.sqlExecuteUpdate("Update core_annotation set creatoruser= (select baseclassid from acco_user where acco_user.personid=core_annotation.authorid) ");
        final Map<String, String> columnNames_anno = new HashMap<String, String>();
        columnNames_anno.put("creatoruser", "creatorid");
        columnNames_anno.put("date_", "creationdate");
        dup.copyColumnData("core_annotation", "core_labbookentry", "attachmentid", "baseclassid",
            columnNames_anno, true);

        //experiment
        dup.createColumn("expe_experiment", "creatorUser", "int8");
        dup.createColumn("expe_experiment", "lasteditorUser", "int8");
        //calculate ID
        dup.sqlExecuteUpdate("Update expe_experiment set creatoruser= (select baseclassid from acco_user where acco_user.personid=expe_experiment.creatorid and baseclassid in (select user2.baseclassid from acco_user as user2 join acco_usergroup2leaders on acco_usergroup2leaders.leaderid=user2.baseclassid join acco_permission on acco_permission.usergroupid=acco_usergroup2leaders.ledgroupid join core_labbookentry on core_labbookentry.accessid=acco_permission.accessobjectid where core_labbookentry.baseclassid=expe_experiment.baseclassid)) ");
        dup.sqlExecuteUpdate("Update expe_experiment set lasteditorUser= (select baseclassid from acco_user where acco_user.personid=expe_experiment.lasteditorid and baseclassid in (select user2.baseclassid from acco_user as user2 join acco_usergroup2leaders on acco_usergroup2leaders.leaderid=user2.baseclassid join acco_permission on acco_permission.usergroupid=acco_usergroup2leaders.ledgroupid join core_labbookentry on core_labbookentry.accessid=acco_permission.accessobjectid where core_labbookentry.baseclassid=expe_experiment.baseclassid)) ");
        //copy data
        final Map<String, String> columnNames = new HashMap<String, String>();
        columnNames.put("creatoruser", "creatorid");
        columnNames.put("startdate", "creationdate");
        columnNames.put("lastediteddate", "lastediteddate");
        columnNames.put("lasteditorUser", "lasteditorid");
        dup.copyColumnData("expe_experiment", "core_labbookentry", "baseclassid", "baseclassid", columnNames,
            false);

        //Sample
        dup.createColumn("expe_experiment", "sampleid", "int8");
        dup.sqlExecuteUpdate("Update expe_experiment set sampleid= (select max(sampleid) from expe_outputsample where expe_outputsample.experimentid=expe_experiment.baseclassid )  ");
        dup.copyColumnData("expe_experiment", "core_labbookentry", "sampleid", "baseclassid", columnNames,
            false);

        //protocol
        System.out.println("Processing creater details of protocol");
        dup.createColumn("prot_protocol", "creatorUser", "int8");
        dup.createColumn("prot_protocol", "lasteditorUser", "int8");
        columnNames.clear();
        columnNames.put("creatorUser", "creatorid");
        columnNames.put("creationdate", "creationdate");
        columnNames.put("lastediteddate", "lastediteddate");
        columnNames.put("lasteditorUser", "lasteditorid");
        dup.sqlExecuteUpdate("Update prot_protocol set creatoruser= (select baseclassid from acco_user where acco_user.personid=prot_protocol.creatorid) ");
        dup.sqlExecuteUpdate("Update prot_protocol set lasteditorUser= (select baseclassid from acco_user where acco_user.personid=prot_protocol.lasteditorid) ");

        dup.copyColumnData("prot_protocol", "core_labbookentry", "baseclassid", "baseclassid", columnNames,
            false);

        //target
        dup.createColumn("targ_target", "creationdate", "timestamptz");
        dup.createColumn("targ_target", "creatorUser", "int8");
        dup.sqlExecuteUpdate("Update targ_target set creationdate= (select min(date_) from targ_milestone where targ_milestone.targetid=targ_target.baseclassid )  ");
        dup.sqlExecuteUpdate("Update targ_target set creatoruser= (select baseclassid from acco_user where acco_user.personid=targ_target.creatorid and baseclassid in (select user2.baseclassid from acco_user as user2 join acco_usergroup2leaders on acco_usergroup2leaders.leaderid=user2.baseclassid join acco_permission on acco_permission.usergroupid=acco_usergroup2leaders.ledgroupid join core_labbookentry on core_labbookentry.accessid=acco_permission.accessobjectid where core_labbookentry.baseclassid=targ_target.baseclassid))");

        columnNames.clear();
        columnNames.put("creatorUser", "creatorid");
        columnNames.put("creationdate", "creationdate");
        dup.copyColumnData("targ_target", "core_labbookentry", "baseclassid", "baseclassid", columnNames,
            false);

        //drop old columns
        dup.dropColumn("core_annotation", "creatorUser");
        dup.dropColumn("core_annotation", "date_");
        dup.dropColumn("core_annotation", "authorid");
        dup.dropColumn("expe_experiment", "creatorid");
        dup.dropColumn("expe_experiment", "creatoruser");
        dup.dropColumn("expe_experiment", "lastediteddate");
        dup.dropColumn("expe_experiment", "lasteditorUser");
        dup.dropColumn("expe_experiment", "lasteditorid");
        dup.dropColumn("expe_experiment", "sampleid");
        dup.dropColumn("prot_protocol", "creatorid");
        dup.dropColumn("prot_protocol", "creationdate");
        dup.dropColumn("prot_protocol", "lastediteddate");
        dup.dropColumn("prot_protocol", "lasteditorid");
        dup.dropColumn("prot_protocol", "lasteditorUser");
        dup.dropColumn("prot_protocol", "creatoruser");
        dup.dropColumn("targ_target", "creatoruser");
        dup.dropColumn("targ_target", "creatorid");
        dup.dropColumn("targ_target", "creationdate");
        dup.dropColumn("cryz_image", "createdate");
        dup.dropColumn("cryz_image", "creatorid");
        dup.dropColumn("cryz_dropannotation", "annotatorid");

    }

    /**
     * move all details + description to baseclass
     * 
     * @param dup
     * @throws SQLException
     */
    private void processDetails(final DatabaseUpdater dup) throws SQLException {
        final List<String> tableNames = dup.getAllTableNames();
        for (final String tableName : tableNames) {
            if (tableName.equalsIgnoreCase("core_baseclass")) {
                continue;
            } else if (dup.isColumnExist(tableName, "details")) {
                System.out.println("Moving details from " + tableName + " to core_baseclass");
                final String primaryIDFrom = dup.getPrimaryColumn(tableName);
                final Map<String, String> columnNames = new HashMap<String, String>();
                columnNames.put("details", "details");
                dup.copyColumnData(tableName, "core_baseclass", primaryIDFrom, "dbid", columnNames, false);
                dup.dropColumn(tableName, "details");
            } else if (dup.isColumnExist(tableName, "description")) {
                System.out.println("Moving description from " + tableName + " to core_baseclass");
                final String primaryIDFrom = dup.getPrimaryColumn(tableName);
                final Map<String, String> columnNames = new HashMap<String, String>();
                columnNames.put("description", "details");
                dup.copyColumnData(tableName, "core_baseclass", primaryIDFrom, "dbid", columnNames, false);
                dup.dropColumn(tableName, "description");
            }

        }

        System.out.println("Details moved");

    }

    /**
     * @param dup
     * @throws SQLException
     */
    private void processOrganism(final DatabaseUpdater dup) throws SQLException {
        //rename NaturalSource to Organism
        dup.renameTable("taxo_naturalsource", "REF_ORGANISM");
        //refactor Organism.organismName to .name
        dup.renameColumn("REF_ORGANISM", "organismName", "name");

        //avoid duplicated name
        final Collection<String> names = new HashSet<String>();
        final ResultSet rs =
            dup.sqlExecuteQuery("select baseclassid, name,ncbitaxonomyid from REF_ORGANISM order by baseclassid");
        while (rs.next()) {
            final String ncbitaxonomyid = rs.getString("ncbitaxonomyid");
            if (ncbitaxonomyid != null && ncbitaxonomyid.contains(";")) {
                rs.updateString("ncbitaxonomyid", ncbitaxonomyid.replaceAll(";", ""));
            }

            final String name = rs.getString("name");
            if (names.contains(name)) {
                rs.updateString("name", name + "_");
                rs.updateRow();
                names.add(name + "_");
            } else {
                names.add(name);
            }
        }
        //confirm 
        dup.setUniqueKey("REF_ORGANISM", Collections.singletonList("name"));
        System.out.println("Organism processed");
    }

    /**
     * Move id to labBookEntry or publicEntry
     * 
     * @param dup
     * @throws SQLException
     */
    private void processEntryClass(final DatabaseUpdater dup) throws SQLException {
        final Collection<String> classNames = muv.getModel().getClassNames();
        for (final String className : classNames) {
            final MetaClass metaClass = muv.getModel().getMetaClass(className);
            final MetaClass supMetaClass = metaClass.getSupertype();
            String supClassTableName = null;
            if (supMetaClass.getJavaClass().equals(LabBookEntry.class)) {
                supClassTableName = "core_labbookentry";
            } else if (supMetaClass.getJavaClass().equals(PublicEntry.class)) {
                supClassTableName = "ref_publicentry";
            }

            if (supClassTableName != null) {
                System.out.println("copying " + className + "'s dbid to " + supClassTableName + "...");
                final String tableName = ((MetaClassImpl) metaClass).getDBTableName();
                final String primaryIDName = dup.getPrimaryColumn(tableName);
                dup.copyColumnData(tableName, supClassTableName, primaryIDName, "baseclassid", null, true);
            }
        }
        System.out.println("LabBookEntry & publicentry processed");
    }

    /**
     * @param dup
     * @throws SQLException
     * @throws ConstraintException
     */
    private void processExternalDbLink(final DatabaseUpdater dup) throws SQLException, ConstraintException {
        //copy id from ExternalDbLink to attachment
        dup.copyColumnData("core_externaldblink", "core_attachment", "baseclassid", "baseclassid", null, true);
        final MetaClass metaClass = muv.getMetaClass(ExternalDbLink.class);
        muv.updateMetaClassAttributes(metaClass);

        //copy parententryid from ExternalDbLink related table
        final Map<String, String> columnMap = new HashMap<String, String>();
        columnMap.put("targetgroupid", "parententryid");
        dup.copyColumnData("targ_targgr2dbrefs", "core_attachment", "dbrefid", "baseclassid", columnMap,
            false);
        dup.dropTable("targ_targgr2dbrefs");
        columnMap.clear();
        columnMap.put("targetid", "parententryid");
        dup.copyColumnData("targ_target2dbrefs", "core_attachment", "dbrefid", "baseclassid", columnMap,
            false);
        dup.dropTable("targ_target2dbrefs");

        //drop all ExternalDbLink which do not have parententryid

        String sql =
            "delete from core_externaldblink where core_externaldblink.attachmentid in (select baseclassid from core_attachment where core_attachment.parententryid is null)";
        dup.sqlExecuteUpdate(sql);
        sql = "delete from core_attachment where parententryid is null";
        dup.sqlExecuteUpdate(sql);

        System.out.println("ExternalDbLink processed");
    }

    /**
     * @param dup
     * @throws SQLException
     * @throws ConstraintException
     */
    private void processAnnotation(final DatabaseUpdater dup) throws SQLException, ConstraintException {
        dup.renameColumn("core_annotation", "description", "LEGEND");
        dup.sqlExecuteUpdate("ALTER TABLE core_annotation  ALTER LEGEND TYPE text");

        //copy id from Annotation to attachment
        dup.copyColumnData("core_annotation", "core_attachment", "baseclassid", "baseclassid", null, true);
        dup.dropColumn("core_annotation", "urlid");
        final MetaClass metaClass = muv.getMetaClass(Annotation.class);
        muv.updateMetaClassAttributes(metaClass);
        final Map<String, String> columnMap = new HashMap<String, String>();
        //copy parententryid from Annotation.protocolid
        columnMap.put("protocolid", "parententryid");
        dup.copyColumnData("core_annotation", "core_attachment", "attachmentid", "baseclassid", columnMap,
            true);
        dup.dropColumn("core_annotation", "protocolid");
        columnMap.clear();
        //copy parententryid from Annotation related table

        columnMap.put("experimentid", "parententryid");
        dup.copyColumnData("expe_experiment2anno", "core_attachment", "annotationid", "baseclassid",
            columnMap, false);
        dup.dropTable("expe_experiment2anno");
        columnMap.clear();

        columnMap.put("experimentgroupid", "parententryid");
        dup.copyColumnData("expe_expegr2anno", "core_attachment", "annotationid", "baseclassid", columnMap,
            false);
        dup.dropTable("expe_expegr2anno");
        columnMap.clear();

        columnMap.put("projectid", "parententryid");
        dup.copyColumnData("targ_project2anno", "core_attachment", "annotationid", "baseclassid", columnMap,
            false);
        dup.dropTable("targ_project2anno");
        columnMap.clear();
        columnMap.put("targetgroupid", "parententryid");
        dup.copyColumnData("targ_targgr2anno", "core_attachment", "annotationid", "baseclassid", columnMap,
            false);
        dup.dropTable("targ_targgr2anno");
        columnMap.clear();
        columnMap.put("targetid", "parententryid");
        dup.copyColumnData("targ_target2anno", "core_attachment", "annotationid", "baseclassid", columnMap,
            false);
        dup.dropTable("targ_target2anno");

        //drop all Annotation which do not have parententryid

        String sql =
            "delete from core_annotation where core_annotation.attachmentid in (select baseclassid from core_attachment where core_attachment.parententryid is null)";
        dup.sqlExecuteUpdate(sql);
        sql = "delete from core_attachment where parententryid is null";
        dup.sqlExecuteUpdate(sql);

        //drop other tables
        System.out.println("Annotation processed");
    }

    /**
     * @param dup
     * @throws SQLException
     * @throws ConstraintException
     */
    private void processCitation(final DatabaseUpdater dup) throws SQLException, ConstraintException {
        //copy id from citation to attachment
        dup.dropColumn("core_citation", "casabstractcode");
        dup.copyColumnData("core_citation", "core_attachment", "baseclassid", "baseclassid", null, true);
        final MetaClass metaClass = muv.getMetaClass(Citation.class);
        muv.updateMetaClassAttributes(metaClass);

        //copy id from citation related table
        final Map<String, String> columnMap = new HashMap<String, String>();
        columnMap.put("methodid", "parententryid");
        dup.copyColumnData("EXPE_method2citations", "core_attachment", "citationid", "baseclassid",
            columnMap, false);
        columnMap.clear();
        columnMap.put("softwareid", "parententryid");
        dup.copyColumnData("EXPE_software2citations", "core_attachment", "citationid", "baseclassid",
            columnMap, false);
        columnMap.clear();
        columnMap.put("targetid", "parententryid");
        dup.copyColumnData("targ_target2citations", "core_attachment", "citationid", "baseclassid",
            columnMap, false);
        columnMap.clear();
        columnMap.put("protocolid", "parententryid");
        dup.copyColumnData("prot_protocol2references", "core_attachment", "referenceid", "baseclassid",
            columnMap, false);

        dup.dropTable("EXPE_method2citations");
        dup.dropTable("EXPE_software2citations");
        dup.dropTable("targ_target2citations");
        dup.dropTable("prot_protocol2references");
        //drop all citation which do not have parententryid
        String sql =
            "delete from core_citation where core_citation.attachmentid in (select baseclassid from core_attachment where core_attachment.parententryid is null)";
        dup.sqlExecuteUpdate(sql);
        sql =
            "delete from core_bookcitation where core_bookcitation.citationid in (select baseclassid from core_attachment where core_attachment.parententryid is null)";
        dup.sqlExecuteUpdate(sql);
        sql =
            "delete from core_conferencecitation where core_conferencecitation.citationid in (select baseclassid from core_attachment where core_attachment.parententryid is null)";
        dup.sqlExecuteUpdate(sql);
        sql =
            "delete from core_journalcitation where core_journalcitation.citationid in (select baseclassid from core_attachment where core_attachment.parententryid is null)";
        dup.sqlExecuteUpdate(sql);
        sql =
            "delete from core_thesiscitation where core_thesiscitation.citationid in (select baseclassid from core_attachment where core_attachment.parententryid is null)";
        dup.sqlExecuteUpdate(sql);
        sql = "delete from core_attachment where parententryid is null";
        dup.sqlExecuteUpdate(sql);

        System.out.println("citation processed");
    }

    /**
     * Drop projectid from all tables
     * 
     * @param dup
     * @throws SQLException
     * @throws ConstraintException
     */
    private void preProcess(final DatabaseUpdater dup) throws SQLException, ConstraintException {
        processNamemingSystem(dup);
        //rm ContentStored, DataLocation, Storage, NormalStorage, ContentStorage 
        //rm AbstractStorage & Project 
        dup.dropTable("impl_abstractstorage");
        dup.dropTable("impl_contentstorage");
        dup.dropTable("impl_contentstored");
        dup.dropTable("impl_datalocation");
        dup.dropTable("impl_normalstorage");
        dup.dropTable("impl_storage");
        dup.dropTable("targ_scoreboard");
        dup.dropColumn("sam_sample", "outputsampleid");
        dup.dropSequence("dbidsequence");
        dup.renameTable("cryz_parade_possva", "cryz_cypade_possva");
        dup.dropTable("peop_person2authci");
        dup.dropTable("peop_person2editci");
        //mv Instrument to experiment
        dup.renameTable("inst_instrument", "EXPE_INSTRUMENT");
        //rm in protocol pkg Procedure, Step, WaitStep, TemperatureStep
        dup.dropTable("prot_procedure");
        dup.dropTable("prot_step");
        dup.dropColumn("prot_refinputsample", "stepid");
        dup.dropColumn("prot_refoutputsample", "stepid");
        dup.dropTable("prot_waitstep");
        dup.dropTable("prot_temperaturestep");
        //rename ExpBlueprint into ResearchObjective & rename all links from Experiment, Milestone & Person
        dup.renameTable("expb_expblueprint", "TARG_RESEARCHOBJECTIVE");
        dup.renameColumn("expe_experiment", "expblueprintid", "RESEARCHOBJECTIVEID");

        //rename BlueprintComponent into ResearchObjectiveElement & rename all links from ResearchObjective, MolComponent, Target, SampleComponent
        dup.renameTable("expb_blueprintcomponent", "TRAG_RESEARCHOBJECTIVEELEMENT");
        dup.renameColumn("TRAG_RESEARCHOBJECTIVEELEMENT", "expblueprintid", "RESEARCHOBJECTIVEID");
        dup.renameColumn("sam_samplecomponent", "blueprintcomponentid", "RESEARCHOBJECTIVELEMENTID");

        //rename MolComponent to Molecule & rename links from ResearchObjectiveElement and Molecule, target
        dup.renameTable("refs_molcomponent", "MOLE_MOLECULE");
        dup.renameColumn("TRAG_RESEARCHOBJECTIVEELEMENT", "molcomponentid", "MOLECULEID");

        //rename MolCompFeature into MoleculeFeature & rename links from Molecule
        dup.renameTable("refs_molcompfeature", "MOLE_MOLECULEFEATURE");
        dup.renameColumn("MOLE_MOLECULEFEATURE", "molcomponentid", "MOLECULEID");
        dup.renameColumn("MOLE_MOLECULEFEATURE", "refmolcomponentid ", "REFMOLECULEID");
        dup.renameTable("refs_molco2relaexbl", "MOLE_MOLECULE2RELAREOBEL");
        dup.renameColumn("MOLE_MOLECULE2RELAREOBEL", "relatedexpblueprintid",
            "RELATEDRESEARCHOBJECTIVEELEMENTID");
        dup.renameColumn("MOLE_MOLECULE2RELAREOBEL", "trialmolcomponentid", "TRIALMOLECULEID");

        //PIMS-2039 ExpBlueprint.whyChosen should be optional
        dup.setColumnCanBeNull("TARG_RESEARCHOBJECTIVE", "whychosen");
        //rename the link called code to status between Milestone and Status in target package
        dup.renameColumn("targ_milestone", "codeid", "STATUSID");

        //remove link between Milestone and ResearchObjective and create helper methods in both classes by using Experiment
        dup.dropColumn("targ_milestone", "expblueprintid");

        // rm Url 
        dup.dropTable("impl_url");

        dup.renameTable("meth_parameter", "EXPE_METHODPARAMETER");

        final List<String> tableNames = dup.getAllTableNames();
        for (final String tableName : tableNames) {
            //rm all project property in subclasses of BaseClass
            if (tableName.equalsIgnoreCase("targ_project") || tableName.equalsIgnoreCase("targ_target")) {
                dup.dropColumn(tableName, "rootprojectid");
            } else if (tableName.equalsIgnoreCase("targ_project2anno")
                || tableName.equalsIgnoreCase("targ_target2projects")) {
                /*ignore*/
            } else if (dup.isColumnExist(tableName, "projectid")) {
                dup.dropColumn(tableName, "projectid");
            } else if (dup.isColumnExist(tableName, "serial")) {
                dup.dropColumn(tableName, "serial");
            }
            //rename MemopsBaseClass to BaseClass & rename MEMOPSBASECLASSID to BASECLASSID & rename CORE_MEMOPSBASECLASS to CORE_BASECLASS & remove memops everywhere else 
            if (dup.isColumnExist(tableName, "MEMOPSBASECLASSID")) {
                dup.renameColumn(tableName, "MEMOPSBASECLASSID", "baseclassid");
            }
            //drop namingsystem
            if (dup.isColumnExist(tableName, "namingsystem")) {
                if (!tableName.equalsIgnoreCase("targ_targetgroup")) {
                    dup.dropColumn(tableName, "namingsystem");
                }
            }
            //rename implementation to core & rename tables IMPL_XXX to CORE_XXX 
            if (tableName.startsWith("impl_")) {
                final String newTableName = tableName.replace("impl_", "core_");
                dup.renameTable(tableName, newTableName);
            }

            //move citation to core 
            if (tableName.startsWith("cita_")) {
                final String newTableName = tableName.replace("cita_", "core_");
                dup.renameTable(tableName, newTableName);
            }
            //rename package refSampleComponent to molecule
            if (tableName.startsWith("refs_")) {
                final String newTableName = tableName.replace("refs_", "MOLE_");
                dup.renameTable(tableName, newTableName);
            }

            //mv Method, MethodParameter & Software to experiment 
            if (tableName.startsWith("meth_")) {
                final String newTableName = tableName.replace("meth_", "EXPE_");
                dup.renameTable(tableName, newTableName);
            }

        }
        dup.dropTable("core_project");

        //mv DbName to reference
        dup.renameTable("dbr_dbname", "ref_dbname");

        //mv HolderCategory, InstrumentType, ComponentCategory, ExperimentType, WorkflowItem, 
        //HazardPhrase, SampleCategory, Scoreboard, Status, NaturalSource,
        //AbstractHolderType, PinType, HolderType, CrystalType into reference
        dup.renameTable("hold_abstractholdertype", "ref_abstractholdertype");
        dup.renameTable("MOLE_componentcategory", "ref_componentcategory");
        dup.renameTable("hold_crystaltype", "ref_crystaltype");
        dup.renameTable("prot_experimenttype", "ref_experimenttype");
        dup.renameTable("risk_hazardphrase", "ref_hazardphrase");
        dup.renameTable("hold_holdercategory", "ref_holdercategory");
        dup.renameTable("hold_holdertype", "ref_holdertype");
        dup.renameTable("hold_pintype", "ref_pintype");
        dup.renameTable("sam_samplecategory", "ref_samplecategory");
        dup.renameTable("prot_workflowitem", "ref_workflowitem");
        dup.renameTable("anno_annotation", "core_annotation");

        //rename Status to TargetStatus
        dup.renameTable("targ_status", "REF_TARGETSTATUS");
        //rename MemopsBaseClass to BaseClass & rename MEMOPSBASECLASSID to BASECLASSID & rename CORE_MEMOPSBASECLASS to CORE_BASECLASS & remove memops everywhere else 
        dup.renameTable("core_memopsbaseclass", "core_baseclass");
        dup.renameTable("dbr_dbref", "core_externaldblink");

        dup.renameTable("inst_instrumenttype", "REF_INSTRUMENTTYPE");

        dup.dropTable("targ_status2scor");

        //
        //columns to be added
        dup.createColumn("core_baseclass", "details", "text");

        //create LabBookEntry in core
        //  MetaClass metaClass = muv.getMetaClass(BaseClass.class);
        //  muv.updateMetaClassWithoutRole(metaClass);

        MetaClass metaClass = muv.getMetaClass(LabBookEntry.class);
        muv.updateMetaClassAttributes(metaClass);
        muv.updateMetaRoleOfClass(metaClass);

        //create Attachment 
        metaClass = muv.getMetaClass(Attachment.class);
        muv.updateMetaClassAttributes(metaClass);
        muv.updateMetaRoleOfClass(metaClass);
        dup.setColumnCanBeNull("core_attachment", "parententryid");
        dup.dropConstraintInTable("core_attachment");
        dup.dropConstraintOnTable("core_attachment");

        metaClass = muv.getMetaClass(Note.class);
        muv.updateMetaClassAttributes(metaClass);
        muv.updateMetaRoleOfClass(metaClass);

        metaClass = muv.getMetaClass(PublicEntry.class);
        muv.updateMetaClassAttributes(metaClass);
        muv.updateMetaRoleOfClass(metaClass);

        metaClass = muv.getMetaClass(Primer.class);
        muv.updateMetaClassAttributes(metaClass);

        System.out.println("preProcess finished");

    }

    /**
     * @param dup
     * @throws SQLException
     */
    private void processNamemingSystem(final DatabaseUpdater dup) throws SQLException {
        //remove duplication of name(Protein) in refs_componentcategory
        final Set<String> caNames = new HashSet<String>();
        ResultSet rs = dup.sqlExecuteQueryIgnorCase("select name from refs_componentcategory ");
        while (rs.next()) {
            caNames.add(rs.getString("name"));
        }
        for (final String caName : caNames) {
            Long defaultProeinId = null;
            Long refProeinId = null;
            rs =
                dup.sqlExecuteQueryIgnorCase("select memopsbaseclassid,namingsystem from refs_componentcategory where name='"
                    + caName + "'");
            while (rs.next()) {
                if (rs.getString("namingsystem").equalsIgnoreCase("default")) {
                    defaultProeinId = rs.getLong("memopsbaseclassid");
                } else {
                    refProeinId = rs.getLong("memopsbaseclassid");
                }
            }
            if (defaultProeinId != null && refProeinId != null) {
                dup.sqlExecuteUpdate("update refs_compca2components set categoryid=" + defaultProeinId
                    + " where categoryid=" + refProeinId);
            }
            dup.sqlExecuteUpdate("delete from refs_componentcategory where memopsbaseclassid=" + refProeinId);

        }
    }
}
