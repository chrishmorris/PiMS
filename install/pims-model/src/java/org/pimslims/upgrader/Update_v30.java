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
import java.util.LinkedList;
import java.util.List;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader;

/**
 * DATA MODEL CHANGES 29
 * 
 * DM change 30:
 * 
 * 1. Score • Add String Column color • Drop Column Serial
 * 
 * 2. DropAnnotation • Drop not-null constraint for Annotator • Drop column Serial
 * 
 */
public class Update_v30 extends SpecialUpgrader {

    public Update_v30(ModelUpdateVersionImpl muv) {
        super(muv);
    }

    @Override
    public int getVersion() {
        return 30;
    }

    @Override
    protected void upgrade() throws SQLException, ConstraintException, AccessException {
        DatabaseUpdater dbu = new DatabaseUpdater(muv);
        //create table for color
        dbu.createColumn("cryz_score", "color", "varchar(80)");

        //drop old table,column
        dbu.dropColumn("cryz_score", "serial");
        dbu.dropColumn("cryz_dropannotation", "serial");

        //drop constrain
        if (dbu.isConstraintExist("cryz_dropannotation",
            "cryz_dropannotation_serial_projectid_must_be_unique"))
            dbu.dropConstraint("cryz_dropannotation", "cryz_dropannotation_serial_projectid_must_be_unique");
        else if (dbu.isConstraintExist("cryz_dropannotation", "cryz_dropannotation_serial_key"))
            dbu.dropConstraint("cryz_dropannotation", "cryz_dropannotation_serial_key");

        if (dbu.isConstraintExist("cryz_score", "cryz_score_serial_key"))
            dbu.dropConstraint("cryz_score", "cryz_score_serial_key");
        else if (dbu.isConstraintExist("cryz_score", "cryz_score_serial_scoringschemeid_must_be_unique"))
            dbu.dropConstraint("cryz_score", "cryz_score_serial_scoringschemeid_must_be_unique");

        List<String> keys = new LinkedList<String>();
        keys.add("value");
        keys.add("scoringschemeid");
        dbu.setUniqueKey("cryz_score", keys);
        //change nullable
        dbu.setColumnCanBeNull("cryz_dropannotation", "annotatorid");
        dbu.setColumnNotNull("cryz_score", "value");
        dbu.setDBRevisionNumber();
    }

}
