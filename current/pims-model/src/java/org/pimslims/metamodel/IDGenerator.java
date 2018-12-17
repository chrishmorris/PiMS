/**
 * org.pimslims.hibernate IDGenerator.java
 * 
 * @date 15 Nov 2007 09:59:52
 * 
 * @author Bill Lin
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 2.0
 * 
 *           Copyright (c) 2007
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
package org.pimslims.metamodel;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.jdbc.Work;
import org.pimslims.dao.WritableVersion;
import org.pimslims.persistence.HibernateUtil;
import org.pimslims.upgrader.DatabaseUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IDGenerator
 * 
 */
public class IDGenerator {

    static Logger log = LoggerFactory.getLogger(org.pimslims.metamodel.IDGenerator.class.getName());

    static Long highValue = new Long(-1);

    static Long currentValue = new Long(highValue);

    static private final int gap = 2000;

    public static synchronized Long getNextID(WritableVersion wv) {

        if (currentValue.equals(highValue))
            getMore(wv);
        currentValue += 1;
        log.debug("Allocated id: " + currentValue);
        return currentValue;

    }

    static private void getMore(WritableVersion wv) {
        wv.getSession().doWork(new Work() {

            public void execute(Connection connection) throws SQLException {
                DatabaseUpdater dbu = DatabaseUpdater.getUpdater(connection);
                if (!HibernateUtil.isOracleDB()) {
                    //for postgres
                    currentValue = dbu.getNextSeqVal("hibernate_sequence") - 1;
                    highValue = currentValue + gap;
                    dbu.setSequenceStart("hibernate_sequence", highValue);
                } else {
                    //for oracle
                    for (int i = 0; i < gap; i++)
                        highValue = dbu.getNextSeqVal("hibernate_sequence");
                    currentValue = highValue - gap;

                }

                log.info("Reserved more ids");
            }
        });

    }

}
