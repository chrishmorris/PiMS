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

import java.sql.SQLException;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.persistence.HibernateUtil;
import org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader;

public class Update_v40 extends SpecialUpgrader {

    /**
     * Constructor
     * 
     * @param muv
     */
    Update_v40(ModelUpdateVersionImpl muv) {
        super(muv);
    }

    /**
     * getVersion
     * 
     * @see org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader#getVersion()
     */
    @Override
    public int getVersion() {
        return 40;
    }

    /**
     * Update_v39.upgrade
     * 
     * @see org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader#upgrade()
     */
    @Override
    protected void upgrade() throws SQLException, ConstraintException, AccessException, AbortedException {
        final DatabaseUpdater dbu = muv.getDbUpdater();

        // rename a column, so the name is not an Oracle Keyword
        if (!HibernateUtil.isOracleDB() && dbu.isColumnExist("REVISIONNUMBER", "date")) {
            dbu.renameColumn("REVISIONNUMBER", "date", "date_");
        }

    }

}
