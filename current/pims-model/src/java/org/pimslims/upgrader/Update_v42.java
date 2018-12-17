/**
 * pims-dm org.pimslims.upgrader Update_v41.java
 * 
 * @author cm65
 * @date 1 Feb 2011
 * 
 *       Protein Information Management System
 * @version: 4.2
 * 
 *           Copyright (c) 2011 Chris Morris The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.upgrader;

import java.sql.SQLException;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader;

public class Update_v42 extends SpecialUpgrader {

    /**
     * Constructor
     * 
     * @param muv
     */
    public Update_v42(ModelUpdateVersionImpl muv) {
        super(muv);
    }

    /**
     * getVersion
     * 
     * @see org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader#getVersion()
     */
    @Override
    public int getVersion() {
        return 42;
    }

    /**
     * Update_v41.upgrade
     * 
     * @see org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader#upgrade()
     */
    @Override
    public void upgrade() throws SQLException, ConstraintException, AccessException, AbortedException {

        // no action needed
    }

}
