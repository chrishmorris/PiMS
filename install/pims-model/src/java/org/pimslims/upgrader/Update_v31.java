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

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader;

/**
 * 
 * DM change 31: • Add “operator” person for experiment
 * 
 */
public class Update_v31 extends SpecialUpgrader {

    public Update_v31(ModelUpdateVersionImpl muv) {
        super(muv);
    }

    @Override
    public int getVersion() {
        return 31;
    }

    @Override
    protected void upgrade() throws SQLException, ConstraintException, AccessException {
        DatabaseUpdater dbu = new DatabaseUpdater(muv);
        //add column for operator
        dbu.createColumn("expe_experiment", "operatorid", "int8");
        dbu.setForeignKey("expe_experiment", "expe_experiment_operator", "peop_person", "labbookentryid",
            "operatorid", "NO ACTION");

        dbu.setDBRevisionNumber();
    }

}
