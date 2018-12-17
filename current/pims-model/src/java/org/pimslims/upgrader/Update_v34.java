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

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader;

/**
 * 
 * DM change 32: • change the protocol's parameter definition from Collection to List
 * 
 * DM change 32: • new package for schedule
 */
public class Update_v34 extends SpecialUpgrader {

    public Update_v34(ModelUpdateVersionImpl muv) {
        super(muv);
    }

    @Override
    public int getVersion() {
        return 34;
    }

    @Override
    protected void upgrade() throws SQLException, ConstraintException, AccessException {
        //DatabaseUpdater dbu = new DatabaseUpdater(muv);
        DatabaseUpdater dup = muv.getDbUpdater();
        //could dup.setColumnCanBeNull("crys_image", "creator");
        dup.setColumnCanBeNull("targ_target", "whychosen");
        dup.changeColumnLength("dbr_dbref", "code", "varchar", 80);
        //could dbre_dbref.code length 80
        muv.flush();
    }

    /**
     * @param dbu
     * @throws SQLException
     * @throws ConstraintException
     */
    private void checkDuplication() throws SQLException, ConstraintException {
        ResultSet rs =
            muv.getDbUpdater()
                .sqlExecuteQuery(
                    "select memopsbaseclassid,protocolid,name from prot_parameterdefinition as p where memopsbaseclassid <> (select max(  pp.memopsbaseclassid) from prot_parameterdefinition as pp where pp.protocolid=p.protocolid and pp.name=p.name)");
        int i = 0;
        while (rs.next()) {
            System.out.println("Found more than one parameterdefinition which have same name("
                + rs.getString("name") + ") and same protocol(id=" + rs.getLong("protocolid") + ")");
            i++;
        }

        rs.close();
        if (i > 0)
            throw new ConstraintException(
                "Found duplicated parameterdefinitions! please remove duplication and make sure no duplcation on parameterdefinition's name+protocolid");

    }

    class PDComparator implements java.util.Comparator {

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object arg0, Object arg1) {
            ParameterDefinition pd0 = (ParameterDefinition) arg0;
            ParameterDefinition pd1 = (ParameterDefinition) arg1;
            // 
            return pd0.getName().compareTo(pd1.getName());
        }

    }
}
