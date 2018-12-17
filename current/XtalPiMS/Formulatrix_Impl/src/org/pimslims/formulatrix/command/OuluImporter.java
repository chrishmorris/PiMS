package org.pimslims.formulatrix.command;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.pimslims.business.DataStorage;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.formulatrix.implementation.ManufacturerDataStorageImpl;
import org.pimslims.formulatrix.implementation.ManufacturerVersion;
import org.pimslims.formulatrix.implementation.RockImagerConnection;

public class OuluImporter extends Importer {

	private OuluImporter(ManufacturerDataStorageImpl formulatrix, RockImagerConnection ri, DataStorageImpl pims) throws BusinessException {
		super(formulatrix,ri,pims);
	}
	
    private static SimpleDateFormat FORMAT = (SimpleDateFormat) SimpleDateFormat.getDateInstance();

    public static void main(String[] args) throws BusinessException {

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
        int returnCode=0;
        DataStorage pims = DataStorageFactory.getDataStorageFactory().getDataStorage();
        ManufacturerDataStorageImpl formulatrix = new ManufacturerDataStorageImpl(true, ManufacturerVersion.OULU); // read only
        RockImagerConnection rockImager = new RockImagerConnection(true); // read only
        try {
            formulatrix.openResources(Importer.formulatrixUsername);
            pims.openResources(Importer.pimsUsername);
            Importer importer = new OuluImporter(formulatrix, rockImager, (DataStorageImpl) pims);
            if (null == date) {
                date = importer.getLatestUpdateTime();
            }
            if (null == date) {
                System.err
                    .println("No data yet loaded into PiMS. Please enter date to start export from, e.g. "
                        + FORMAT.format(new Date()));
                returnCode=2;
            } else {
            	
            	importer.importAfter(date);
            	pims.commit();
            	System.out.println("Data imported, getting imager inventories:");
            	
            	importer.writeImagerContentsJSON();
            	System.out.println("OK: data imported");
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            returnCode=1;
        } finally {
        	if(0!=returnCode){ pims.abort(); }
            formulatrix.abort();
        }
        System.exit(returnCode);
    }
    
    
}
