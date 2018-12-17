/**
 *  ImagerServiceImpl.java
 * 
 * @author cm65
 * @date 17 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.formulatrix.implementation;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.pimslims.business.core.model.Project;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.Imager;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.ImagerService;
import org.pimslims.business.crystallization.view.ScheduleView;
import org.pimslims.business.crystallization.view.SimpleSampleView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.formulatrix.command.Importer;

/**
 * ImagerServiceImpl
 * 
 */
// see LocationServiceImpl
public class ImagerServiceImpl implements ImagerService {

    private final ManufacturerConnection connection;
    private final ManufacturerVersion version;

    /**
     * Constructor for ImagerServiceImpl
     * 
     * @param mfrConnection
     */
    public ImagerServiceImpl(ManufacturerConnection mfrConnection, ManufacturerVersion version) {
        this.connection = mfrConnection;
        this.version = version;
    }

    /**
     * ImagerServiceImpl.find
     * 
     * @throws BusinessException
     * @see org.pimslims.business.crystallization.service.ImagerService#find(java.lang.String)
     */
    public Imager find(String name) throws BusinessException {
        try {
            ManufacturerPreparedStatement statement =
                this.connection.prepareStatement("SELECT * FROM RockImager.dbo.Robot WHERE Name=?");
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            return makeImagerBean(result);
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    public List<Imager> findAll() throws BusinessException {
        try {
            ManufacturerPreparedStatement statement =
                this.connection.prepareStatement("SELECT * FROM RockImager.dbo.Robot");
            ResultSet result = statement.executeQuery();
            LinkedList<Imager> imagers=new LinkedList<Imager>();
            while(result.next()){
                Imager imager = new Imager();
                imager.setId(result.getLong("ID"));
                imager.setName(result.getString("Name"));
            	imagers.add(imager);
            }
            return imagers;
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }
    
    @Deprecated
    private Imager makeImagerBean(ResultSet result) throws SQLException {
        boolean found = result.next();
        if (!found) {
            return null;
        }

        Imager ret = new Imager();
        ret.setId(result.getLong("ID"));
        ret.setName(result.getString("Name"));

        assert !result.next() : "More than one result for imager";
        result.close();
        return null;
    }
    
    public int getCapacity(String robotName) throws BusinessException{
    	Imager i=find(robotName);
    	if(null==i){ throw new BusinessException("No imager with name "+robotName); }
    	long robotId=i.getId();
    	return getCapacity(robotId);
    }
    public int getCapacity(long robotId) throws BusinessException{
        try {
            ManufacturerPreparedStatement statement =
                this.connection.prepareStatement("SELECT COUNT(RockImager.dbo.Address.ID) AS capacity " +
                		" FROM RockImager.dbo.Address, RockImager.dbo.Hotel " +
                		" WHERE RockImager.dbo.Address.HotelID=RockImager.dbo.Hotel.ID " +
                		" AND RockImager.dbo.Hotel.isPresent=1 " +
                		" AND RockImager.dbo.Address.RobotID=? ");
            statement.setLong(1, robotId);
            ResultSet result = statement.executeQuery();
            boolean found = result.next();
            if (!found) {
                return 0;
            }
            return (int) result.getLong("capacity");
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }
    
    public Map getContents(String robotName) throws BusinessException{
    	Imager i=find(robotName);
    	if(null==i){ throw new BusinessException("No imager with name "+robotName); }
    	long robotId=i.getId();
    	return getContents(robotId);
    }
    public Map getContents(long robotId) throws BusinessException{
        try {
            ManufacturerPreparedStatement statement =
                this.connection.prepareStatement("SELECT riPlate.Barcode AS Barcode, " +
                		" Users.Name AS Owner, " +
                		" CONVERT(varchar(25), MIN(ImagingTask.DateToImage), 120) AS NextInspection, " +
                		" CONVERT(varchar(25), MAX(ImagingTask.DateToImage), 120) AS FinalInspection, " +
                		" COUNT(ImagingTask.DateToImage) AS InspectionsRemaining " +
                		" FROM RockImager.dbo.Plate AS riPlate, " +
                		"	RockImager.dbo.PlateAddress AS PlateAddress, " +
                		" 	RockMaker.dbo.Plate AS rmPlate, " +
                		" 	RockMaker.dbo.Experiment AS Experiment, " +
                		" 	RockMaker.dbo.Users AS Users, " +
                		" 	RockMaker.dbo.ExperimentPlate AS ExperimentPlate " +
                		" LEFT OUTER JOIN RockMaker.dbo.ImagingTask AS ImagingTask " +
                		"	ON ImagingTask.ExperimentPlateID=ExperimentPlate.ID " +
                		"	AND ImagingTask.State<>1 " + //Not the Skipped ones
                		"	AND ImagingTask.State<>6 " + //Not the Completed ones
                		" WHERE riPlate.ID=PlateAddress.PlateId " +
                		"	AND rmPlate.Barcode=riPlate.Barcode " +
                		"	AND rmPlate.ID=ExperimentPlate.PlateID " +
                		"	AND rmPlate.ExperimentId=Experiment.ID " +
                		"	AND Experiment.UserID=Users.ID " +
                		"	AND riPlate.RobotID=?" +
                		" GROUP BY riPlate.Barcode, Users.Name " +
                		" ORDER BY NextInspection ASC");
            statement.setLong(1, robotId);
            ResultSet result = statement.executeQuery();
			Map<String, HashMap<String, Object>> plateMap=new HashMap<String, HashMap<String, Object>>();
			while(result.next()){
				String barcode=result.getString("Barcode");
				String owner=result.getString("Owner");
				String nextInspection=result.getString("NextInspection");
				String finalInspection=result.getString("FinalInspection");
				HashMap<String, Object> plate=new HashMap<String, Object>();
				plate.put("barcode", barcode);
				plate.put("owner", owner);
				plate.put("nextInspection",nextInspection);
				plate.put("finalInspection", finalInspection);
				plate.put("inspectionsRemaining", new Integer(result.getInt("InspectionsRemaining")));
				plateMap.put(barcode, plate);
			}
			return plateMap;
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }
    
    /**
     * ImagerServiceImpl.save
     * 
     * @see org.pimslims.business.crystallization.service.ImagerService#save(org.pimslims.business.crystallization.model.Imager)
     */
    public Imager save(Imager imager) throws BusinessException {
        // MAYBE also insert other values
        String insert = /* FIXME rewrite */ "insert into device (site_id, device_id, name) values (1, device_seq.nextval, ?)";
        try {
            ManufacturerPreparedStatement statement = this.connection.prepareStatement(insert);
            statement.setString(1, imager.getName());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
        return find(imager.getName());
    }

//    /**
//     * ImagerServiceImpl.createAndLink
//     * This is the only method in this class that is useful
//     * @see org.pimslims.business.crystallization.service.ImagerService#createAndLink(org.pimslims.business.crystallization.model.Image)
//     */
//    @Override
//    public void createAndLink(Image image) throws BusinessException {
//        doCreateAndLink(image);
//
//    }
    public void createAndLink(Image image) throws BusinessException {
    	//DON'T doCreateAndLink(image);
    }

    
/*
    private void doCreateAndLink(Image image) throws BusinessException {
        
        TrialDrop drop = image.getDrop();
        WellPosition position = drop.getWellPosition();

        try {
            String select =
            		"SELECT ImageBatch.ID AS ImageBatchID, Well.ID AS WellID," +
            		" WellDrop.ID AS WellDropID, Plate.ID AS PlateID" +
            		" FROM ImageBatch, ImagingTask, ExperimentPlate, Plate, Well, WellDrop" +
            		" WHERE Well.PlateID=Plate.ID AND WellDrop.WellID=Well.ID" +
            		" AND ImageBatch.ImagingTaskID=ImagingTask.ID AND ImagingTask.ExperimentPlateID=ExperimentPlate.ID" +
            		" AND ExperimentPlate.PlateID=Plate.ID " +
            		" AND Plate.Barcode=? AND Well.RowLetter=? AND Well.ColumnNumber=? AND WellDrop.DropNumber=?";
            ManufacturerPreparedStatement statement = this.connection.prepareStatement(select);
            String barcode = drop.getPlate().getBarcode();
            statement.setString(1, barcode);
            statement.setString(2, position.toString().substring(0, 1));
            statement.setInt(3, position.getColumn());
            statement.setInt(4, position.getSubPosition());
            ResultSet result = statement.executeQuery();
            assert result.next() : "No well for: " + barcode + ":" + position.toString();
            long plateId = result.getLong("PlateID");
            long wellId = result.getLong("WellID");
            long wellDropId=result.getLong("WellDropID");
            long imageBatchId=result.getLong("ImageBatchID");
            assert !result.next() : "Too many wells for: " + barcode + ":" + position.toString();

            // make the Region record
            String insert_region="INSERT INTO Region(WellDropID, RegionTypeID, RegionName, ImagingEnabled)" +
            		" OUTPUT INSERTED.*" +
            		" VALUES(?, 1, ?, 1)";
            statement = this.connection.prepareStatement(insert_region);
            statement.setLong(1, wellDropId);
            statement.setString(2, "Drop");
            result = statement.executeQuery();
            result.next();
            long regionId=result.getLong("ID");
            statement.close();

            //get the default capture profile (that's UV versus visible, condenser settings, etc.)
            String get_capture_profile="SELECT CaptureProfile.ID AS CaptureProfileID, CaptureProfileVersion.ID AS CaptureProfileVersionID" +
            		" FROM CaptureProfile, CaptureProfileVersion " +
            		" WHERE Name=? AND CaptureProfileVersion.CaptureProfileID=CaptureProfile.ID";
            statement = this.connection.prepareStatement(get_capture_profile);
            statement.setString(1, "Imager Defaults");
            result = statement.executeQuery();
            assert result.next(): "No capture profile called 'Imager Defaults'";
            long captureProfileId=result.getLong("CaptureProfileID");
            long captureProfileVersionId=result.getLong("CaptureProfileVersionID");
            assert !result.next(): "More than one capture profile called 'Imager Defaults'";
            statement.close();
            
            //make the capture result record
            String insert_capture_result="INSERT INTO CaptureResult(RegionID, CaptureProfileVersionID, ImageBatchID)" +
            		" OUTPUT INSERTED.* " +
            		" VALUES(?, ?, ?)";
            statement = this.connection.prepareStatement(insert_capture_result);
            statement.setLong(1, regionId);
            statement.setLong(2, captureProfileVersionId);
            statement.setLong(3, imageBatchId);
            result = statement.executeQuery();
            result.next();
            long captureResultId=result.getLong("ID");
            statement.close();
            
            // make the image record
            String insert_image = "INSERT INTO Image(CaptureResultID, ImageStoreID, ImageTypeID, ImageTypeIndex,PixelSize)" +
            		" OUTPUT INSERTED.*" +
            		" VALUES(?, ?, ?, ?, ?)";
            statement = this.connection.prepareStatement(insert_image);
            statement.setLong(1, captureResultId);
            statement.setLong(2, 1); //just use the first image store for test
            statement.setLong(3, 2); // 2 is the "extended focus" image type
            statement.setLong(4, 1); // There is only one "extended focus" image ImageTypeIndex counts upward for several of same type, e.g., z-slices
            statement.setLong(5, 2); // Dummy value, microns per pixel
            statement.executeQuery();
            statement.close();
            
            String get_image_path = this.version.getImageSelectStatement() + " AND Region.ID=?";
            statement = this.connection.prepareStatement(get_image_path);
            statement.setLong(1, plateId);
            statement.setLong(2, regionId);
            result = statement.executeQuery();
            result.next();
            String imagePath=result.getString("ImagePath");
            System.out.println("Image path from DB: "+imagePath);
            image.setImagePath(imagePath);
            statement.close();

            File srcTestFile=new File(ImporterTest.pathToTestImage);
            File destTestFile=new File(Importer.formulatrixPathPrefix + image.getImagePath());
            assert(srcTestFile.exists());
            assert(!destTestFile.exists());
            FileUtils.copyFile(srcTestFile, destTestFile);
            assert(srcTestFile.exists());
            assert(destTestFile.exists());
            
        } catch (SQLException e) {
            throw new BusinessException(e);
        } catch (IOException e) {
            throw new BusinessException(e);
        }
    }
*/
    private static final Pattern PATH = Pattern.compile("(\\w+://)?(.*?)([^/+])");

    /**
     * ImagerServiceImpl.getPath
     * 
     * @param imagePath e.g. file:////DC1512086/images_fdrv/DCA/EM-TFPDM-MG-20/090421/file.bmp
     * @return the path component .e.g //DC1512086/images_fdrv/DCA/EM-TFPDM-MG-20/090421/
     */
    String[] parsePath(String imagePath) {
        Matcher m = PATH.matcher(imagePath);
        assert m.matches() : "Unable to understand image path: " + imagePath;
        return new String[] { m.group(1), m.group(2) };
    }

    /**
     * ImagerServiceImpl.createInstrument
     * 
     * @see org.pimslims.business.crystallization.service.ImagerService#createInstrument(java.lang.String,
     *      float)
     */
    @Override
    @Deprecated
    // see LocationService.save
    public void createInstrument(String name, float temperature) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ImagerServiceImpl.createAndLink
     * 
     * @see org.pimslims.business.crystallization.service.ImagerService#createAndLink(java.util.Collection)
     */
    @Override
    public void createAndLink(Collection<Image> images) throws BusinessException {
        for (Iterator iterator = images.iterator(); iterator.hasNext();) {
            Image image = (Image) iterator.next();
            this.createAndLink(image);
        }

    }

    /**
     * ImagerServiceImpl.findProjects
     * 
     * @see org.pimslims.business.crystallization.service.ImagerService#findProjects()
     */
    @Override
    public Collection<Project> findProjects() throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ImagerServiceImpl.findSchedules
     * 
     * @see org.pimslims.business.crystallization.service.ImagerService#findSchedules(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public Collection<ScheduleView> findSchedules(String barcode, String robot) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ImagerServiceImpl.findSimpleSampleViews
     * 
     * @see org.pimslims.business.crystallization.service.ImagerService#findSimpleSampleViews()
     */
    @Override
    public Collection<SimpleSampleView> findSimpleSampleViews() throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ImagerServiceImpl.finishedImaging
     * 
     * @see org.pimslims.business.crystallization.service.ImagerService#finishedImaging(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public boolean finishedImaging(String barcode, String sessionId, String robot) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ImagerServiceImpl.schedulePlate
     * 
     * @see org.pimslims.business.crystallization.service.ImagerService#schedulePlate(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public Collection<ScheduleView> schedulePlate(String barcode, String robot) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ImagerServiceImpl.setPriority
     * 
     * @see org.pimslims.business.crystallization.service.ImagerService#setPriority(java.lang.String,
     *      java.util.Calendar, java.lang.String, int)
     */
    @Override
    public boolean setPriority(String barcode, Calendar dateToImage, String robot, int priority)
        throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ImagerServiceImpl.skippedImaging
     * 
     * @see org.pimslims.business.crystallization.service.ImagerService#skippedImaging(java.lang.String,
     *      java.util.Calendar, java.lang.String)
     */
    @Override
    public boolean skippedImaging(String barcode, Calendar dateToImage, String robot)
        throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ImagerServiceImpl.startedImaging
     * 
     * @see org.pimslims.business.crystallization.service.ImagerService#startedImaging(java.lang.String,
     *      java.util.Calendar, java.util.Calendar, java.lang.String)
     */
    @Override
    public String startedImaging(String barcode, Calendar dateToImage, Calendar dateImaged, String robot)
        throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ImagerServiceImpl.startedUnscheduledImaging
     * 
     * @see org.pimslims.business.crystallization.service.ImagerService#startedUnscheduledImaging(java.lang.String,
     *      java.util.Calendar, java.util.Calendar, java.lang.String)
     */
    @Override
    public String startedUnscheduledImaging(String barcode, Calendar dateToImage, Calendar dateImaged,
        String robot) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }


}
