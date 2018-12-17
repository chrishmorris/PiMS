/**
 * Rhombix_Impl org.pimslims.rhombix.command MplImporter.java
 * 
 * @author cm65
 * @date 20 May 2011
 * 
 *       Protein Information Management System
 * @version: 4.3
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.command;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.pimslims.business.DataStorage;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.rhombix.implementation.RhombixDataStorageImpl;
import org.pimslims.rhombix.implementation.RhombixVersion;

/**
 * MplImporter
 * 
 */
public class MplImporter extends Importer {

    /**
     * Constructor for MplImporter
     * 
     * @param rhombix
     * @param pims
     * @throws BusinessException
     */
    private MplImporter(RhombixDataStorageImpl rhombix, DataStorageImpl pims) throws BusinessException {
        super(rhombix, pims);
    }

    private static SimpleDateFormat FORMAT = (SimpleDateFormat) SimpleDateFormat.getDateInstance();

    public static void main(String[] args) {

        Calendar date = null;
        if (1 == args.length) {
            try {
                Date d = FORMAT.parse(args[0]);
                date = FORMAT.getCalendar();
                date.setTime(d);
            } catch (ParseException e1) {
                System.err.println("Date: " + args[0] + " does not match pattern: "
                    + FORMAT.format(new Date()));
                System.exit(2);
            }
        }
        try {
            RhombixDataStorageImpl rhombix = new RhombixDataStorageImpl(true, RhombixVersion.MPL); // read only
            rhombix.openResources("administrator"); // also known as "dca"

            DataStorage pims = DataStorageFactory.getDataStorageFactory().getDataStorage();
            pims.openResources("rhombix");
            Importer importer = new MplImporter(rhombix, (DataStorageImpl) pims);

            if (null == date) {
                date = importer.getLatestUpdateTime();
            }
            if (null == date) {
                System.err
                    .println("No data yet loaded into PiMS. Please enter date to start export from, e.g. "
                        + FORMAT.format(new Date()));
                System.exit(2);
            }
            importer.importAfter(date);

            pims.commit();
            rhombix.abort();

            System.out.println("OK: data imported");
        } catch (BusinessException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
