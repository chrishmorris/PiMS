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

import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader;

/**
 * 
 * DM change 32: • change the protocol's parameter definition from Collection to List
 * 
 * DM change 32: • new package for schedule
 */
public class Update_v35 extends SpecialUpgrader {

    public Update_v35(ModelUpdateVersionImpl muv) {
        super(muv);
    }

    @Override
    public int getVersion() {
        return 35;
    }

    @Override
    protected void upgrade() throws SQLException, ConstraintException, AccessException {
        DatabaseUpdater dup = muv.getDbUpdater();
        //sche_scheduledtask's scheduledtime vchar to timestamptz
        //dup.sqlExecuteUpdate("ALTER TABLE sche_scheduledtask ALTER scheduledtime TYPE timestamptz");
        dup.dropColumn("sche_scheduledtask", "scheduledtime");
        dup.createColumn("sche_scheduledtask", "scheduledtime", "timestamptz");
        dup.setColumnNotNull("sche_scheduledtask", "scheduledtime");
        //drop serial
        Set<String> serialTableNames =
            Collections
                .unmodifiableSet(new java.util.HashSet<String>(java.util.Arrays.asList(new String[] {
                    "anno_annotation", "cita_citation", "cryz_parameter", "cryz_parameterdefinition",
                    "dbr_dbref", "expb_blueprintcomponent", "expb_expblueprint", "expe_experimentgroup",
                    "expe_inputsample", "expe_outputsample", "hold_holderlocation",
                    "hold_holdertypeposition", "impl_applicationdata", "impl_datalocation", "impl_storage",
                    "impl_url", "inst_instrument", "loca_location", "meth_method", "meth_parameter",
                    "peop_group", "peop_person", "peop_personingroup", "prot_procedure",
                    " prot_refinputsample", "prot_refoutputsample", "prot_step", "prot_workflowitem",
                    "refs_molcompfeature", "sam_samplecomponent", "targ_milestone", "targ_project",
                    "taxo_naturalsource", "impl_storage", "expe_parameter", })));
        for (String tablename : serialTableNames) {
            if (dup.isColumnExist(tablename, "serial"))
                dup.dropColumn(tablename, "serial");

        }
        //change the type of order_
        Set<String> orderTableNames =
            Collections.unmodifiableSet(new java.util.HashSet<String>(java.util.Arrays.asList(new String[] {
                "cita_citation_keywords", "cryz_parade_possva", "meth_software_tasks", "peop_orga_addresses",
                "peop_persingr_phonnu", "peop_person_middin", "prot_parade_possva", "prot_protocol_remarks",
                "refs_abstco_keywords", "refs_abstco_synonyms", "targ_target_aliases" })));
        for (String tablename : orderTableNames) {
            dup.sqlExecuteUpdate("ALTER TABLE " + tablename + " ALTER order_ TYPE int4");
        }
        muv.flush();
    }
}
