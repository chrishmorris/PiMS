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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.target.Alias;
import org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader;

/**
 * 
 * DM change 32: • change the protocol's parameter definition from Collection to List
 * 
 * DM change 32: • new package for schedule
 */
public class Update_v37 extends SpecialUpgrader {

    public Update_v37(final ModelUpdateVersionImpl muv) {
        super(muv);
    }

    @Override
    public int getVersion() {
        return 37;
    }

    @Override
    protected void upgrade() throws SQLException, ConstraintException, AccessException {
        final DatabaseUpdater dup = muv.getDbUpdater();
        /**
         * Normal upgrader will do: •ImageType (New Class) •Image's new roles •ScheduledTask's new
         * role/attribute
         */
        //ScheduledTask drops
        dup.dropColumn("sche_scheduledtask", "protocolid");
        dup.dropColumn("sche_scheduledtask", "experimentgroupid");
        dup.dropColumn("sche_scheduledtask", "locationid");

        //drop namelist
        dup.dropTable("core_namelist");

        //change exp's operator from person to user
        processExperiment(dup);
        //drop baseclass
        ProcessBaseClass(dup);

        //Change target’s alias -> role
        processTargetAlias(muv);

        //make RO's commonName unique 
        final List<String> keys = new LinkedList<String>();
        keys.add("commonname");
        //could fix duplicate name into example db dup.setUniqueKey("targ_researchobjective", keys);
        muv.flush();
        assert muv.isColumnExist("core_labbookentry", "dbid");
        assert muv.isColumnExist("CORE_SYSTEMCLASS", "DBID");

    }

    /**
     * Update_v37.processExperiment
     * 
     * @param dup
     * @throws SQLException
     */
    private void processExperiment(final DatabaseUpdater dup) throws SQLException {
        //experiment
        dup.createColumn("expe_experiment", "operatorUser", "int8");
        //calculate ID
        dup.sqlExecuteUpdate("Update expe_experiment set operatorUser= (select baseclassid from acco_user where acco_user.personid=expe_experiment.operatorid and baseclassid in (select user2.baseclassid from acco_user as user2 join acco_usergroup2leaders on acco_usergroup2leaders.leaderid=user2.baseclassid join acco_permission on acco_permission.usergroupid=acco_usergroup2leaders.ledgroupid join core_labbookentry on core_labbookentry.accessid=acco_permission.accessobjectid where core_labbookentry.baseclassid=expe_experiment.labbookentryid)) ");
        //repace old column
        dup.dropColumn("expe_experiment", "operatorid");
        dup.renameColumn("expe_experiment", "operatorUser", "operatorid");
    }

    /**
     * Update_v37.ProcessBaseClass
     * 
     * @param dup
     * @throws SQLException
     */
    private void ProcessBaseClass(final DatabaseUpdater dup) throws SQLException {

        //rename primary key of AbstractModelObject's sublcass
        dup.renameColumn("REF_PUBLICENTRY", "baseclassid", "dbid");
        dup.renameColumn("core_attachment", "baseclassid", "dbid");
        dup.renameColumn("core_labbookentry", "baseclassid", "dbid");

        //rename primary key of SYSTEMCLASS's sublcass
        dup.renameColumn("ACCO_USERGROUP", "baseclassid", "SYSTEMCLASSID");
        dup.renameColumn("ACCO_USER", "baseclassid", "SYSTEMCLASSID");
        dup.renameColumn("ACCO_PERMISSION", "baseclassid", "SYSTEMCLASSID");
        dup.renameColumn("CORE_ACCESSOBJECT", "baseclassid", "SYSTEMCLASSID");

        //create details field in AbstractModelObject's sublcass
        dup.createColumn("REF_PUBLICENTRY", "details", "text");
        dup.createColumn("core_attachment", "details", "text");
        dup.createColumn("core_labbookentry", "details", "text");

        //change baseclass to systemclass 
        dup.renameTable("core_baseclass", "CORE_SYSTEMCLASS");

        //move 'details' field from baseclass to AbstractModelObject's sublcass
        dup.sqlExecuteUpdate("Update REF_PUBLICENTRY set details= (select details from CORE_SYSTEMCLASS where CORE_SYSTEMCLASS.dbid=REF_PUBLICENTRY.dbid) ");
        dup.sqlExecuteUpdate("delete from CORE_SYSTEMCLASS where CORE_SYSTEMCLASS.dbid in (select dbid from REF_PUBLICENTRY )");
        dup.sqlExecuteUpdate("Update core_attachment set details= (select details from CORE_SYSTEMCLASS where CORE_SYSTEMCLASS.dbid=core_attachment.dbid) ");
        dup.sqlExecuteUpdate("delete from CORE_SYSTEMCLASS where CORE_SYSTEMCLASS.dbid in (select dbid from core_attachment )");
        dup.sqlExecuteUpdate("Update core_labbookentry set details= (select details from CORE_SYSTEMCLASS where CORE_SYSTEMCLASS.dbid=core_labbookentry.dbid) ");
        dup.sqlExecuteUpdate("delete from CORE_SYSTEMCLASS where CORE_SYSTEMCLASS.dbid in (select dbid from core_labbookentry )");

    }

    /**
     * Update_v37.processTargetAlias
     * 
     * @param muv
     * @throws SQLException
     * @throws ConstraintException
     */
    private void processTargetAlias(final ModelUpdateVersionImpl muv) throws ConstraintException,
        SQLException {
        //create proper table
        final MetaClass metaClass = muv.getMetaClass(Alias.class);
        muv.updateMetaClassAttributes(metaClass);
        muv.updateMetaRoleOfClass(metaClass);

        final DatabaseUpdater dup = muv.getDbUpdater();
        //get all values from previous alias table
        final ResultSet rsFrom =
            dup.sqlExecuteQuery("select targetid,alias from targ_target_aliases where targetid in (select labbookentryid from targ_target)");
        dup.resetStatement();
        final PreparedStatement psp_insert_alias =
            dup.sqlPrepareUpdate("insert into TARG_ALIAS (labbookentryid,name,targetid) values (?,?,?)");
        final PreparedStatement psp_insert_labid =
            dup.sqlPrepareUpdate("insert into core_labbookentry (dbid) values (?)");
        while (rsFrom.next()) {
            final Long targetID = rsFrom.getLong("targetid");
            final String name = rsFrom.getString("alias");
            final Long dbid = dup.getNextSeqVal("hibernate_sequence");
            //insert to core_labbookentry
            psp_insert_labid.setLong(1, dbid);
            psp_insert_labid.executeUpdate();
            //insert to TARG_ALIAS
            psp_insert_alias.setLong(1, dbid);
            psp_insert_alias.setString(2, name);
            psp_insert_alias.setLong(3, targetID);
            psp_insert_alias.executeUpdate();
        }

        //drop old table 
        dup.dropTable("targ_target_aliases");
    }
}
