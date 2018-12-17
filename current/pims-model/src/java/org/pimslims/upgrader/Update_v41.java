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

public class Update_v41 extends SpecialUpgrader {

    /**
     * Constructor
     * 
     * @param muv
     */
    public Update_v41(ModelUpdateVersionImpl muv) {
        super(muv);
    }

    /**
     * getVersion
     * 
     * @see org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader#getVersion()
     */
    @Override
    public int getVersion() {
        return 41;
    }

    /**
     * Update_v41.upgrade
     * 
     * @see org.pimslims.upgrader.SpecialUpgraderList.SpecialUpgrader#upgrade()
     */
    @Override
    public void upgrade() throws SQLException, ConstraintException, AccessException, AbortedException {

        // PIMS-3443 upgrade expe_parameter.value
        muv.changeColumnLength("expe_parameter", "value", "text", 0);

        /* These changes are needed, but given the small number of Oracle databases, it is better to make them by hand:
        if (HibernateUtil.isOracleDB()) {
            alter table hold_holderlocation drop CONSTRAINT "SYS_C0023779" 
            alter table hold_holderlocation drop CONSTRAINT "SYS_C0023780" 
            alter table hold_holderlocation drop CONSTRAINT HOLD_HOLDERLOCATION_PK
            "alter table hold_holderlocation add CONSTRAINT \"HOLD_HOLDLO_HOLDER_FK\" FOREIGN KEY (\"HOLDERID\")\r\n" + 
            "REFERENCES  \"HOLD_HOLDER\" (\"ABSTRACTHOLDERID\") " +
            " on delete cascade ENABLE";
            
            alter table hold_holderlocation add CONSTRAINT "HOLD_HOLDLO_LOCATION_FK" FOREIGN KEY ("LOCATIONID")
            REFERENCES  "LOCA_LOCATION" ("LABBOOKENTRYID") 
             on delete cascade ENABLE
        } */
    }

}
