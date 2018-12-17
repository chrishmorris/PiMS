/**
 * command Importer.java
 * 
 * @author cm65
 * @date 12 May 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.formulatrix.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.service.ConstructService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.Imager;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.crystallization.service.ScreenService;
import org.pimslims.business.crystallization.view.InspectionView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.formulatrix.implementation.ImagerServiceImpl;
import org.pimslims.formulatrix.implementation.ManufacturerPreparedStatement;
import org.pimslims.formulatrix.implementation.PlateInspectionServiceImpl;
import org.pimslims.formulatrix.implementation.ManufacturerDataStorageImpl;
import org.pimslims.formulatrix.implementation.RockImagerConnection;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.reference.ImageType;

/**
 * Importer
 * 
 * This imports data from a Manufacturer's database to a PiMS database. It uses the following concepts;
 * 
 * the PiMS "Construct"
 * 
 * "Most recent update" - the most recent import of images to PiMS
 * 
 * "Start time" - the date and time of the earliest imported data
 * 
 * The different methods available preserve the following invariant:
 * 
 * EITHER
 * 
 * There are not yet any images in PiMS
 * 
 * OR BOTH
 * 
 * Every image that is not linked to a specific construct (macromolecule) is in PiMS, if it was made after the
 * "start time" and before the latest update time.
 * 
 * AND
 * 
 * Every image that is linked to a construct (macromolecule) known to PiMS is in PiMS, if it was made before
 * the latest update time.
 * 
 */
public class Importer {

    /**
     * MAX_TRANSACTION_SIZE int
     */
    private static final int MAX_TRANSACTION_SIZE = 5;

    static final String INSTRUMENT_NAME = "Formulatrix";

    private final DataStorage pims;

    private final DataStorage rm; //RockMaker DB
    private final RockImagerConnection ri; //RockImager DB
    
    private static final int IMPORTED=0;
    private static final int NOTIMPORTED=1;
    private static final int NOTIMPORTED_BEINGIMAGED=2;
    
    private Location pimsImager;
    
    public static String formulatrixUsername;
    public static String formulatrixPathPrefix;
    public static String pimsUsername;
    public static String pimsPathPrefix;
    public static String pimsUrlPrefix;
    public static int imageSizeX;
    public static int imageSizeY;
    public static float imageXlengthPerPixel;
    public static float imageYlengthPerPixel;
    
    private static Properties properties;
    
    static {
    	Properties properties=new Properties();
    	File file=new File("conf/Properties");
    	try {
			InputStream in = new FileInputStream(file);
			properties.load(in);
			Importer.formulatrixPathPrefix=properties.getProperty("fx.formulatrixPathPrefix");
			Importer.pimsPathPrefix=properties.getProperty("fx.pimsPathPrefix");
			Importer.pimsUrlPrefix=properties.getProperty("fx.pimsUrlPrefix");
			Importer.imageSizeX=Integer.parseInt(properties.getProperty("fx.imageSizeX"));
			Importer.imageSizeY=Integer.parseInt(properties.getProperty("fx.imageSizeY"));
			Importer.imageXlengthPerPixel=Float.parseFloat(properties.getProperty("fx.imageXlengthPerPixel"));
			Importer.imageYlengthPerPixel=Float.parseFloat(properties.getProperty("fx.imageYlengthPerPixel"));
			Importer.pimsUsername=properties.getProperty("fx.pimsUsername");
			Importer.formulatrixUsername=properties.getProperty("fx.username");
			Importer.properties=properties;
		} catch (IOException e) {
			if (e instanceof FileNotFoundException) {
                System.err.println("Could not find connection details at: " + file.getAbsolutePath());
            }
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Constructor for Importer
     * 
     * @param rm
     * @param pims
     * @throws BusinessException
     */
    Importer(ManufacturerDataStorageImpl rm, RockImagerConnection ri, DataStorageImpl pims) throws BusinessException {
        super();
        this.pims = pims;
        this.rm = rm;
        this.ri = ri;
        this.pimsImager = this.pims.getLocationService().findByName(INSTRUMENT_NAME);
        if (null == this.pimsImager) {
            this.pimsImager = Importer.createInstrument((DataStorageImpl) this.pims);
        }
        getPlatesBeingImaged();
    }

    /**
     * 
     * @return an ArrayList of barcodes, for plates being imaged right now
     * @throws BusinessException
     */
    private ArrayList getPlatesBeingImaged() throws BusinessException {
    	try {
			ArrayList platesBeingImaged=new ArrayList();
			
			//Firstly, get all entries in the imaging log where StartTime = EndTime. This *usually*, but not always, indicates that the plate  
			//in question is still being imaged.
			ManufacturerPreparedStatement query = this.ri.prepareStatement("SELECT PlateBarcode,RobotId,EndTime FROM ImagingLog WHERE EndTime=StartTime");
			ResultSet result = query.executeQuery();
            while(result.next()){
            	//Sometimes the Formulatrix doesn't update the EndTime when it finishes imaging the plate. We need to ignore these cases, so let's see
            	//whether anything else has been imaged ON THE SAME ROBOT since EndTime. Each robot can only image one plate at a time, so if it's done
            	//another job since, this job must have finished and can be safely ignored.
    			ManufacturerPreparedStatement subquery = this.ri.prepareStatement("SELECT PlateBarcode,RobotId,EndTime FROM ImagingLog " +
    					"WHERE RobotID=? AND StartTime > ?");
    			subquery.setInt(1, result.getInt("RobotID"));
    			subquery.setString(2, result.getString("EndTime"));
    			ResultSet subresult = subquery.executeQuery();
    			if(subresult.next()){
    				//we found a subsequent job on this robot, so ignore this job
    			} else {
    				//no subsequent job on this robot. So either
    				// - this job is still running, or 
    				// - this job finished, but it didn't update the EndTime.
    				//We can't tell which it is. So we'll assume it's still in progress. When the robot images another plate, we'll know this one finished.
	    			platesBeingImaged.add(result.getString("PlateBarcode"));
	            	System.out.println("Plate "+result.getString("PlateBarcode")+ " is being imaged.");
    			}
    			subquery.close();
            }
            if(0==platesBeingImaged.size()){
            	System.out.println("No plates are being imaged at the moment.");
            }
            query.close();
			return platesBeingImaged;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }
    
    /**
     * Importer.getLatestUpdateTime
     * 
     * @return the datetime of the most recent plate inspection in PiMS Returns null if neither importAfter
     *         nor recordConstruct has been run
     * 
     * @throws BusinessException
     */
    Calendar getLatestUpdateTime() throws BusinessException {
        PlateInspectionService pis = this.pims.getPlateInspectionService();
        BusinessCriteria criteria = new BusinessCriteria(pis);
        criteria.setMaxResults(1);
        Collection<InspectionView> found = pis.findLatest(criteria);
        if (found.isEmpty()) {
            return null;
        }
        return found.iterator().next().getDate();
    }

    /**
     * Importer.update
     * 
     * imports all images taken after the latest update time, if their construct is known to PiMS, or
     * undefined.
     * 
     * @throws BusinessException
     */
    public void update() throws BusinessException {
        importAfter(getLatestUpdateTime());
    }

    /**
     * Importer.recordConstruct
     * 
     * @param name the name of the Macromolecule to import
     * 
     *            Record a macromolecule in PiMS. If there are already images in PiMS, this also imports all
     *            images for the construct that are before the most recent update. In that way, the invariant
     *            is preserved.
     * 
     * @throws BusinessException
     */
    public void recordConstruct(String name) throws BusinessException {
        Construct construct = null;
        //MAYBE record the construct, user, lab notebook
        if (null != this.getLatestUpdateTime()) {
            importBefore(construct, this.getLatestUpdateTime());
        }

    }

    /**
     * Importer.importBefore
     * 
     * @param name
     * @param latestUpdateTime
     * @throws BusinessException
     */
    private void importBefore(Construct construct, Calendar latestUpdateTime) throws BusinessException {
        Collection<Integer> inspections = inspectionsBefore(construct, latestUpdateTime);
        for (Iterator<Integer> iterator = inspections.iterator(); iterator.hasNext();) {
            importInspection(iterator.next());
        }
    }

    /**
     * Importer.importImage
     * 
     * @param inspectionId Import the inspection, if its construct is undefined or known to PiMS
     * @throws BusinessException
     */
    int importInspection(long inspectionId) throws BusinessException {
    	System.out.println(inspectionId);
    	int imported=Importer.NOTIMPORTED;
    	PlateInspectionServiceImpl pis = (PlateInspectionServiceImpl) this.rm.getPlateInspectionService();
    	ArrayList captureProfileIds=(ArrayList) pis.findCaptureProfileIdsForInspection(inspectionId);
    	for(Iterator iterator = captureProfileIds.iterator(); iterator.hasNext();){
    		long cpid=(Long) iterator.next();
    		PlateInspection inspection = pis.findWithCaptureProfileId(inspectionId, cpid);
    		String barcode=inspection.getPlate().getBarcode();
            ArrayList platesBeingImaged=this.getPlatesBeingImaged();
    		if(platesBeingImaged.contains(barcode)){
    			imported=Importer.NOTIMPORTED_BEINGIMAGED;
    			System.out.println("Plate for this inspection (barcode "+barcode+") is being imaged. Importer will stop.");
    			return imported;
    		} else {
        		if(0==cpid){
        			System.out.println("No capture profiles were found, importing all images as default");
        		} else {
        			System.out.println("Inspection has capture profile: "+cpid);
        		}
    			if(importInspection(inspection)){ 
    				imported=Importer.IMPORTED;
    			}
    		}
    	}
    	return imported;
    }
    
    boolean importInspection(PlateInspection inspection) throws BusinessException {
        try {
			System.out.println("Importing "+inspection.getPlate().getBarcode()+" "+inspection.getInspectionName());
			Long pimsPlateId = null;
			TrialPlate pimsPlate = this.pims.getTrialService().findTrialPlate(inspection.getPlate().getBarcode());
			if (null == pimsPlate) {
			    pimsPlateId = this.importPlate(inspection.getPlate().getBarcode());
				System.out.println("Importing inspection "+inspection.getId() +"; plate "+inspection.getPlate().getBarcode()+" not in PiMS database");
			} else {
			    pimsPlateId = pimsPlate.getId();
				System.out.println("Importing inspection "+inspection.getId()+" for plate "+inspection.getPlate().getBarcode()+"; plate ID in PiMS database is "+pimsPlateId);
			}
			
			//copy images over
			Collection<Image> images = inspection.getImages();
			if(0!=images.size()){
				for(Iterator<Image> iterator = images.iterator(); iterator.hasNext();){
					Image image=iterator.next();
					File formulatrixFile=new File(Importer.formulatrixPathPrefix + image.getImagePath());
					File xtalpimsFile=new File(Importer.pimsPathPrefix + image.getImagePath());
	//				assert(formulatrixFile.exists());
	//				assert(!xtalpimsFile.exists());
					if(formulatrixFile.exists() && !xtalpimsFile.exists()){
						FileUtils.copyFile(formulatrixFile, xtalpimsFile);
					}
					assert(formulatrixFile.exists());
					assert(xtalpimsFile.exists());
				}
				
				inspection.getPlate().setId(pimsPlateId); // overwrite value from exporting DB
				inspection.setLocation(this.pimsImager);
				PlateInspectionService pis = this.pims.getPlateInspectionService();
				this.pims.flush();
				pis.create(inspection);
				this.pims.flush();
				return true;
			} else {
				System.out.println("Inspection has no whole-well images, not importing.");
				return false;
			}
		} catch (IOException e) {
			throw new BusinessException(e);
		}
    }

    /**
     * Importer.importPlate
     * 
     * @param barcode
     * @return
     * @throws BusinessException
     */
    Long importPlate(String barcode) throws BusinessException {

        TrialPlate plate = this.rm.getTrialService().findTrialPlate(barcode);
        plate.setId(null); // overwrite value from exporting DB
        System.out.println(">>> Plate type is "+plate.getPlateType().getName()+", ID in Formulatrix DB is "+plate.getPlateType().getId());

        // import screen
        Screen screen = plate.getScreen();
        ScreenService pss = this.pims.getScreenService();
        if (null != screen && null == pss.findByName(screen.getName())) {
            pss.create(screen);
        }

        // import construct
        Construct construct = plate.getConstruct();
        ConstructService pcs = this.pims.getConstructService();
        if (null != construct && null == pcs.findByName(construct.getName())) {
            pcs.create(construct);
        }
        plate=this.pims.getTrialService().saveTrialPlate(plate);
        return plate.getId();
    }

    /**
     * Importer.importAfter
     * 
     * @param start the first ten after this date/time will be processed
     * 
     * 
     * @throws BusinessException
     */
    void importAfter(Calendar start) throws BusinessException {
        Collection<Integer> inspections = inspectionsAfter(start);
        int count = 0;
		System.out.println("----------------------------------------------");
        for (Iterator<Integer> iterator = inspections.iterator(); iterator.hasNext();) {
        	if (count > MAX_TRANSACTION_SIZE) {
                break;
            }
            Integer view = iterator.next();
            int result=importInspection(view);
            if(Importer.IMPORTED==result){
            	count++;
            } else if(Importer.NOTIMPORTED_BEINGIMAGED==result){
            	break;
            }
			System.out.println("----------------------------------------------");
        }
    }

    /**
     * Importer.imagesBefore
     * 
     * @param name
     * @param latestUpdateTime
     * @return
     * @throws BusinessException
     */
    private Collection<Integer> inspectionsBefore(Construct construct, Calendar end) throws BusinessException {
        return findInspections(end, false);
    }

    /**
     * Importer.imagesAfter
     * 
     * @param start
     * @return Rhombix inspections recorded after the start date
     * @throws BusinessException
     */
    Collection<Integer> inspectionsAfter(Calendar start) throws BusinessException {
        return findInspections(start, true);
    }

    private Collection<Integer> findInspections(Calendar date, boolean after) throws BusinessException {
        PlateInspectionServiceImpl pis =
            (PlateInspectionServiceImpl) this.rm.getPlateInspectionService();
        return pis.findInspectionIds(date, after);
    }

    /**
     * Importer.setup
     * 
     * @param start Import all images of unspecified construct, after the start date. Also import all images
     *            of known constructs.
     * @throws BusinessException public void setup(Calendar start) throws BusinessException { Calendar now =
     *             Calendar.getInstance(); Collection<Construct> constructs = getAllConstructs(); for
     *             (Iterator<Construct> iterator = constructs.iterator(); iterator.hasNext();) { Construct
     *             construct = iterator.next(); importBefore(construct, now); }
     * 
     *             TrialService rts = this.rhombix.getTrialService(); Collection<Long> inspections =
     *             inspectionsAfter(start); for (Iterator<Long> iterator = inspections.iterator();
     *             iterator.hasNext();) { Long view = iterator.next(); TrialPlate plate =
     *             rts.findTrialPlate(view.getBarcode()); if (null == plate.getConstruct()) {
     *             importInspection(((InspectionViewImpl) view).getId()); } }
     * 
     *             }
     */

    /**
     * Importer.getAllConstructs
     * 
     * @return
     * @throws BusinessException
     */
    private Collection<Construct> getAllConstructs() throws BusinessException {
        ConstructService cs = this.pims.getConstructService();
        return cs.findAll(new BusinessCriteria(cs));
    }

    public static Location createInstrument(DataStorageImpl pims) throws BusinessException {
        WritableVersion version = pims.getWritableVersion();

        try {
        	//TODO use WellImageType
            ImageType type = new ImageType(version, INSTRUMENT_NAME);
            type.setDetails("Formulatrix images");
            type.setUrl(pimsUrlPrefix);
            type.setSizeX(Importer.imageSizeX);
            type.setSizeY(Importer.imageSizeY);
            type.setXlengthPerPixel(Importer.imageXlengthPerPixel);
            type.setYlengthPerPixel(Importer.imageYlengthPerPixel);
            Instrument instrument = new Instrument(version, INSTRUMENT_NAME);
            instrument.setDefaultImageType(type);
            instrument.setDetails("The Formulatrix imager");
            version.flush();
            return pims.getLocationService().findByName(INSTRUMENT_NAME);
        } catch (ConstraintException e) {
            throw new BusinessException(e);
        }
    }
    
    public void writeImagerContentsJSON() throws BusinessException{
    	RockImagerConnection ri=this.ri;
    	String fileRoot=pimsPathPrefix+File.separator+"imagerContents"+File.separator;
    	ImagerServiceImpl im=(ImagerServiceImpl) this.rm.getImagerService();
    	List imagerBeans=im.findAll();
    	Iterator i=imagerBeans.iterator();
    	System.out.println("Imagers:");
    	Map<String, Map> imagers=new HashMap();
    	boolean imagerScanning=false;
    	while(i.hasNext()){
    		Imager imager=(Imager)i.next();
    		String serial=imager.getName();
    		long robotId=imager.getId();
    		//Serial numbers are, e.g., "RI54-0039" where 54 is the nominal capacity
    		//Actual capacity can be significantly less due to licensing, automation ports, deep-plate hotels...
    		String nominalCapacity=serial.split("-")[0].substring(2);
    		if("2".equals(nominalCapacity)){
    			//RI2 is a waste of time. Scan is not tied to plate addition/removal and may return 0, 1 or 2!
    	    	System.out.println("Ignoring 2-plate imager "+serial+" - can't guarantee up-to-date inventory");
    	    	continue;
    		}
			//RI54, RI182, RI1000
	    	System.out.println("Processing "+nominalCapacity+"-plate imager "+serial);
	    	int actualCapacity=im.getCapacity(robotId);
			System.out.println("Actual capacity: "+actualCapacity);
			if(0==actualCapacity){
    			System.out.println("Imager appears to be updating hotel inventory. Leaving old inventories in place.");
    			imagerScanning=true;
				break;
    		}
			
			Map<String, Object> imagerMap=new HashMap<String, Object>();
			Map contents=im.getContents(robotId);
			imagerMap.put("contents", contents);
			imagerMap.put("capacity", actualCapacity);
			imagers.put(serial, imagerMap);
    	}
    	if(!imagerScanning){
    		try {
	    		Date d=new Date();
	    		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	    		String today=sdf.format(d);
    			JSONObject imagersOverview=new JSONObject();
    			String defaultAlertThreshold=Importer.properties.getProperty("imagers.default.alertThreshold","85%");
    			String defaultWarningThreshold=Importer.properties.getProperty("imagers.default.warningThreshold","95%");
	    		Iterator j=imagers.keySet().iterator();
	    		while(j.hasNext()){
	    			int active=0;
	    			int expired=0;
	    			String serial=(String) j.next();
	    			String friendlyName=Importer.properties.getProperty("imagers."+serial+".friendlyName", "");
	    			String alertThreshold=Importer.properties.getProperty("imagers."+serial+".alertThreshold", defaultAlertThreshold);
	    			String warningThreshold=Importer.properties.getProperty("imagers."+serial+".warningThreshold", defaultWarningThreshold);
	    			JSONObject plates=new JSONObject();
	    			Map imager=imagers.get(serial);
	    			Map contents=(Map) imager.get("contents");
	    			Iterator k=contents.keySet().iterator();
	    			while(k.hasNext()){
	    				String barcode=(String)k.next();
	    				Map plate=(Map) contents.get(barcode);
	    				int inspectionsRemaining=((Integer)plate.get("inspectionsRemaining")).intValue();
	    				if(inspectionsRemaining>0){
	    					active++;
	    				} else {
	    					expired++;
	    				}
	    				plates.put(barcode, plate);
	    			}
	    			JSONObject imgr=new JSONObject();
	    			imgr.put("serial",serial);
	    			imgr.put("capacity",imager.get("capacity"));
	    			imgr.put("active",active);
	    			imgr.put("expired",expired);
	    			imgr.put("friendlyName", friendlyName);
	    			if(null!=alertThreshold){
	    				imgr.put("alertThreshold", alertThreshold);
	    				imgr.put("warningThreshold", warningThreshold);
	    			}
	    			imagersOverview.put(serial, imgr);
	    			imgr.put("contents", plates);

		    		//write imager contents JSON
	    			System.out.println("Writing imager contents files for "+serial+"...");
	    			File f=new File(fileRoot+serial+File.separator+serial+"-latest.json");
	    			File copy=new File(fileRoot+serial+File.separator+serial+"-"+today+".json");
					String json=imgr.toString();
					FileUtils.writeStringToFile(f, json, "utf-8");
					FileUtils.copyFile(f, copy);
	    			System.out.println("...done.");
	    		}
	    		
	    		//write overview JSON
    			System.out.println("Writing imagers overview files...");
    			File f=new File(fileRoot+"overview"+File.separator+"overview-latest.json");
    			File copy=new File(fileRoot+"overview"+File.separator+"overview-"+today+".json");
				String json=imagersOverview.toString();
				FileUtils.writeStringToFile(f, json, "utf-8");
				FileUtils.copyFile(f, copy);
    			System.out.println("...done.");
			} catch (FileNotFoundException e) {
				System.out.println("Could not write imager contents/overview files");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Could not write imager contents/overview files");
				e.printStackTrace();
			}
    	}
    }
}
