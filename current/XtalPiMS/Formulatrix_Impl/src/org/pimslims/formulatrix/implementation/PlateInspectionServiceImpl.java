/**
 *  PlateInspectionServiceImpl.java
 * 
 * @author cm65
 * @date 14 Mar 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.formulatrix.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessCriterion;
import org.pimslims.business.criteria.BusinessOrder;
import org.pimslims.business.criteria.GreaterThanExpression;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.ImageType;
import org.pimslims.business.crystallization.model.Imager;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.crystallization.view.InspectionView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.formulatrix.command.Importer;

/**
 * PlateInspectionServiceImpl
 * 
 */
public class PlateInspectionServiceImpl implements PlateInspectionService {

    /**
     * ACTUAL_START_DATETIME is often null
     */
    static final String PROP_SCHEDULED_DATE = "DateToImage";

    /* @Deprecated // This should be a Location - and there are two!
    private static final Imager IMAGER = new Imager();
    static {
        IMAGER.setName("Formulatrix");
        IMAGER.setDefaultImageType(ImageType.COMPOSITE);
    } */

    private final ManufacturerConnection connection;

    private final ManufacturerVersion version;

    private final Map<Long, Sample> sampleCache;

    /**
     * Constructor for PlateInspectionServiceImpl
     * 
     * @param mfrConnection
     * @param dataStorage
     */
    public PlateInspectionServiceImpl(ManufacturerConnection mfrConnection, ManufacturerVersion version,
        ManufacturerDataStorageImpl dataStorage) {
        this.connection = mfrConnection;
        this.version = version;
        this.sampleCache = dataStorage.getSampleCache();
    }

    /**
     * PlateInspectionServiceImpl.create
     * 
     * @see org.pimslims.business.crystallization.service.PlateInspectionService#create(org.pimslims.business.crystallization.model.PlateInspection)
     */
    @Override
  //TODO 01.1
    public void create(PlateInspection inspection) throws BusinessException {

    	long plateId=inspection.getPlate().getId();
    	long expPlateId;

    	String fetch="SELECT ID FROM ExperimentPlate WHERE PlateID=?";
    	try {
	        ManufacturerPreparedStatement statement = this.connection.prepareStatement(fetch);
	        statement.setLong(1, plateId);
	        ResultSet r=statement.executeQuery();
	        if(r.next()){
	        	expPlateId=r.getLong("ID");
	        } else {
	        	//need an ExperimentPlate record
	        	String makeExpPlate="INSERT INTO ExperimentPlate " +
	        			"(PlateID, ImagingTasksEnabled) " +
	        			"OUTPUT INSERTED.*" +
	        			"VALUES (?, 1)";
	        			statement = this.connection.prepareStatement(makeExpPlate);
	                    statement.setLong(1, plateId);
	        			ResultSet rs=statement.executeQuery();
	        			rs.next();
	        			expPlateId=rs.getLong("ID");
	        			statement.close();
	        }
	        
	        String insert="INSERT INTO ImagingTask" +
	        	" (ExperimentPlateID, State, DateToImage, DateImaged, ClearedFromToImage, ClearedFromToScore) " +
	        	" OUTPUT INSERTED.*" +
	        	" VALUES(?, 0, ?, ?, 0, 0)";

	        String insertImageBatch="INSERT INTO ImageBatch(ImagingTaskID)" +
	        		" OUTPUT INSERTED.*" +
	        		" VALUES(?)";
	        
            statement = this.connection.prepareStatement(insert);
            TrialPlateImpl plate = (TrialPlateImpl) inspection.getPlate();
            statement.setLong(1, expPlateId);
            Calendar startDate = inspection.getInspectionDate();
            statement.setTimestamp(2, new java.sql.Timestamp(startDate.getTimeInMillis()));
            statement.setTimestamp(3, new java.sql.Timestamp(startDate.getTimeInMillis()));
            ResultSet result=statement.executeQuery();
            result.next();
            long imagingTaskID=result.getLong("ID");
            statement.close();


//            inspection.setId(imagingTaskID);
            String barcode = inspection.getPlate().getBarcode();
            String inspectionName = getInspectionName(startDate, barcode);
            inspection.setInspectionName(inspectionName);

            statement = this.connection.prepareStatement(insertImageBatch);
            statement.setLong(1, imagingTaskID);
            result=statement.executeQuery();
            result.next();
            long imageBatchID=result.getLong("ID");
            statement.close();
            inspection.setId(imageBatchID);

            /* Check: PlateInspection found = find(inspectionId);
            System.out.println("Saved: " + found.getInspectionDate().getTimeInMillis()); */
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    //MAYBE will need something like this
    static long getCurrentValue(ManufacturerConnection mfrConnection, String sequence) throws SQLException {
        ManufacturerPreparedStatement query =
            mfrConnection.prepareStatement(/* FIXME rewrite */ "select " + sequence + "_seq.currval from dual");
        ResultSet result = query.executeQuery();
        assert result.next();
        long inspectionId = result.getLong(1);
        assert !result.next();
        result.close();
        query.close();
        return inspectionId;
    }

    static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");

    static String getInspectionName(Calendar startDate, String barcode) {
        String time = FORMAT.format(startDate.getTime());
        String inspectionName = barcode + "_" + time;
        return inspectionName;
    }

    public Collection findCaptureProfileIdsForInspection(long inspectionId) throws BusinessException {
        try {
            ManufacturerPreparedStatement query = this.connection.prepareStatement(
            		"SELECT CaptureProfileVersion.CaptureProfileID AS CaptureProfileID " +
    	    		" FROM Image,  ImageBatch, ImageStore, ImagingTask, " +
    	    			" ExperimentPlate, Plate, Well, WellDrop, Region, CaptureResult, CaptureProfileVersion " +
    	    		" WHERE Region.WellDropID=WellDrop.ID AND WellDrop.WellID=Well.ID AND Well.PlateID=Plate.ID  " +
    	    			" AND ImageBatch.ImagingTaskID=ImagingTask.ID AND ImagingTask.ExperimentPlateID=ExperimentPlate.ID " + 
    	    			" AND ExperimentPlate.PlateID=Plate.ID  " +
    	    			" AND Image.ImageStoreID=ImageStore.ID AND Image.CaptureResultID=CaptureResult.ID  " +
    	    			" AND CaptureResult.RegionID=Region.ID AND CaptureResult.ImageBatchID=ImageBatch.ID AND CaptureResult.CaptureProfileVersionID=CaptureProfileVersion.ID" +
    	    			" AND Image.ImageTypeID=2 " +
    					" AND Region.RegionTypeID=1" + // Drop - only import full, not part, images
    	    			" AND ImagingTask.ID=? " +
    	    		"GROUP BY CaptureProfileVersion.CaptureProfileID"
    		);
            query.setLong(1, inspectionId);
            ResultSet result = query.executeQuery();
            ArrayList ret = new ArrayList();
            while(result.next()){
            	ret.add(result.getLong("CaptureProfileID"));
            }
            if(0==ret.size()){
            	//maybe no images, or maybe something else...
            	//...try the old way
            	ret.add(0L);
            }
            result.close();
            query.close();
            
            return ret;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }
    
    /**
     * PlateInspectionServiceImpl.find
     * 
     * @see org.pimslims.business.crystallization.service.PlateInspectionService#find(long)
     */
    @Override
    public PlateInspection find(long id) throws BusinessException {
    	return findWithCaptureProfileId(id,1);
    }

   
    public PlateInspection findWithCaptureProfileId(long id, long captureProfileId) throws BusinessException {
    	try {
            ManufacturerPreparedStatement query =
            		//TODO JOIN TO robot ID
		            this.connection.prepareStatement("SELECT ImageBatch.ID AS ImageBatchID, ImagingTask.ID AS ImagingTaskID, " +
		            		" CONVERT(varchar(25),ImagingTask.DateImaged,121) AS DateImaged, " +
		            		" CONVERT(varchar(25),ImagingTask.DateToImage,121) AS DateToImage, " +
		            		" ExperimentPlate.PlateID AS PlateID" +
		            		" FROM ImageBatch, ImagingTask, ExperimentPlate" +
		            		" WHERE ImagingTask.ExperimentPlateID=ExperimentPlate.ID " +
		            			" AND ImageBatch.ImagingTaskID=ImagingTask.ID " +
		            			" AND ImageBatch.ID=?");
            query.setLong(1, id);
            ResultSet result = query.executeQuery();
            boolean hasNext = result.next();
            assert hasNext;

            PlateInspection ret = makeBean(result, captureProfileId);
            
            // FIXME PLEASE! Horrible horrible bodge
            Location fixme = new Location();
            fixme.setName("Formulatrix");
            fixme.setTemperature(20d);
            fixme.setId(new Long(2));
            ret.setLocation(fixme);
            
            assert !result.next();
            result.close();
            query.close();
            return ret;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    private PlateInspection makeBean(ResultSet result, long captureProfileId) throws SQLException, BusinessException {
        PlateInspection ret = new PlateInspection();
        // was ret.setImager(IMAGER); 
        //TODO MUST MAKE A LOCATOPN BEAN TO REPRESEN THE ACTUAL IMAGER
        long id = result.getLong("ImageBatchID"); 
        ret.setId(id);

//        long locationId=result.getLong("RobotID");
//        Location location=LocationServiceImpl.doFind(this.connection, locationId);
//        ret.setLocation(location);
        
        long plateId = result.getLong("PlateID"); 
        // process experiment
        TrialPlate plate = TrialServiceImpl.doFindTrialPlate(this.connection, plateId, this.sampleCache);
        ret.setPlate(plate);

        // process plate

        Calendar inspectionDate = Calendar.getInstance();
        long millis = result.getTimestamp("DateImaged").getTime();
        millis += (int)captureProfileId; //avoid having two inspections with same timestamp
        inspectionDate.setTimeInMillis(millis);
        ret.setInspectionDate(inspectionDate);
		ret.setId((100*id) + captureProfileId);

        String inspectionName = getInspectionName(inspectionDate, plate.getBarcode())+"_"+captureProfileId;
        ret.setInspectionName(inspectionName);
        addImages(id, ret, plate, captureProfileId);
        return ret;
    }

    private void addImages(long id, PlateInspection ret, TrialPlate plate, long captureProfileId) throws SQLException {
        ManufacturerPreparedStatement query;
        ResultSet result;
        // add images
        Collection<Image> images = new ArrayList();
        String sql=this.version.getImageSelectStatement()
                    + " AND ImageBatch.ID=?";
        if(0!=captureProfileId){
        	sql+="AND CaptureProfileVersion.CaptureProfileID=?";
        }
        query = this.connection.prepareStatement(sql);
        query.setLong(1, plate.getId());
        query.setLong(2, id);
        if(0!=captureProfileId){
        	query.setLong(3, captureProfileId);
        }
        result = query.executeQuery();
        Map<Long, Sample> cache = new HashMap<Long, Sample>();
        while (result.next()) {
            Image image = makeImageBean(result, plate, cache);
            if (null != image) {
                image.setPlateInspection(ret);
                images.add(image);
            }
        }
        result.close();
        query.close();
        ret.setImages(images);
    }

    private static final Pattern IS_URL = Pattern.compile("^file://|^http://");

    static final Pattern HAS_EXTENSION = Pattern.compile(".*(\\.\\w+)$");

    private Image makeImageBean(ResultSet result, TrialPlate plate, Map<Long, Sample> cache)
        throws SQLException {

        Image ret = new Image();
        long umPerPixel = result.getLong("MicronsPerPixel");
        long mmPerPixel= umPerPixel/1000; //Rhombix code dealt with mm, Formulatrix gives us um.
        ret.setXLengthPerPixel(new Double(mmPerPixel));
        ret.setYLengthPerPixel(new Double(mmPerPixel));

		ImageIcon formulatrixFile=new ImageIcon(Importer.formulatrixPathPrefix + result.getString("ImagePath"));
		int height = formulatrixFile.getIconHeight();
		int width = formulatrixFile.getIconWidth();
		formulatrixFile=null;
        ret.setSizeY(height);
        ret.setSizeX(width);

        String row = result.getString("RowLetter");
        int column = result.getInt("ColumnNumber");
        int subPosition = result.getInt("DropNumber");
        WellPosition well = new WellPosition(row+String.format("%02d", column)+"."+subPosition); //pad column to 2 digits
        //MAYBE 
        TrialDrop drop = plate.getTrialDrop(well);
        if (null == drop) {
            System.err.println("No drop found: " + plate.getBarcode() + ":" + well.toString());
        }
        ret.setDrop(drop);

        Calendar startDate = Calendar.getInstance();
        Timestamp date = result.getTimestamp("DateImaged", startDate);
        startDate.setTimeInMillis(date.getTime());
        ret.setImageDate(startDate); //create_datetime
        //ret.setImageType(imageType); // image.lu_image_type_id
        //ret.setInstrument(instrument); // image.device_id

        String formulatrix_path = result.getString("ImagePath");
        String filepath = getPimsPath(formulatrix_path);
        ret.setImagePath(filepath);
        ret.setId(result.getLong("ImageID"));

        return ret;
    }

    /**
     * @param rhombix_path
     * @return
     */
    String getPimsPath(String formulatrix_path) {
        String filepath = formulatrix_path;
        if (!HAS_EXTENSION.matcher(filepath).matches()) {
            filepath += ".jpg";
        }
        String pathPrefix=Importer.formulatrixPathPrefix;
        if (filepath.startsWith(pathPrefix)) {
            filepath = filepath.substring(pathPrefix.length());
        }
        return filepath.replace("\\", "/");
    }

    /**
     * PlateInspectionServiceImpl.findAll
     * 
     * @see org.pimslims.business.crystallization.service.PlateInspectionService#findAll(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<PlateInspection> findAll(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * PlateInspectionServiceImpl.findByInspectionName
     * 
     * @see org.pimslims.business.crystallization.service.PlateInspectionService#findByInspectionName(java.lang.String)
     */
    @Override
    public PlateInspection findByInspectionName(String name) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
//        try {
//            ManufacturerPreparedStatement query =
//                this.connection.prepareStatement(/* FIXME rewrite */ "select * from image_sched where image_sched_id=?");
//            query.setString(1, name);
//            ResultSet result = query.executeQuery();
//            assert result.next();
//
//            PlateInspection ret = makeBean(result);
//
//            assert !result.next();
//            result.close();
//            query.close();
//
//            return ret;
//        } catch (SQLException e) {
//            throw new BusinessException(e);
//        }
    }

    /**
     * PlateInspectionServiceImpl.findByPlate
     * 
     * @see org.pimslims.business.crystallization.service.PlateInspectionService#findByPlate(java.lang.String)
     */
    @Override
    public Collection<PlateInspection> findByPlate(String barcode) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * PlateInspectionServiceImpl.findLatest
     * 
     * @see org.pimslims.business.crystallization.service.PlateInspectionService#findLatest(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<InspectionView> findLatest(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * PlateInspectionServiceImpl.update
     * 
     * @see org.pimslims.business.crystallization.service.PlateInspectionService#update(org.pimslims.business.crystallization.model.PlateInspection)
     */
    @Override
    public void update(PlateInspection plateInspection) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * PlateInspectionServiceImpl.getDataStorage
     * 
     * @see org.pimslims.business.BaseService#getDataStorage()
     */
    @Override
    public DataStorage getDataStorage() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * PlateInspectionServiceImpl.findViewCount
     * 
     * @see org.pimslims.business.ViewService#findViewCount(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Integer findViewCount(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * PlateInspectionServiceImpl.findViews
     * 
     * @see org.pimslims.business.ViewService#findViews(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<InspectionView> findViews(BusinessCriteria criteria) throws BusinessException {

        int maxResults = criteria.getMaxResults();
        String sql = /* FIXME rewrite */ "select * from image_sched where ";

        Timestamp after = null;
        for (Iterator iterator = criteria.getCriteria().iterator(); iterator.hasNext();) {
            BusinessCriterion criterion = (BusinessCriterion) iterator.next();
            if (criterion instanceof GreaterThanExpression) {
                GreaterThanExpression ge = (GreaterThanExpression) criterion;
                if (ge.getValue() instanceof Calendar) {
                    Calendar value = (Calendar) ge.getValue();
                    // the standard formatClause() does not work for Oracle
                    sql += ge.getProperty() + "> ? ";
                    // perhaps it would be better to use ":after" instead of "?"
                    after = new Timestamp(value.getTimeInMillis());
                } else {
                    sql += criterion.formatClause();

                }
            } else {
                sql += criterion.formatClause();
            }
            if (iterator.hasNext()) {
                sql += " and ";
            }
        }

        List<BusinessOrder> orderBy = criteria.getOrderBy();
        assert 1 >= orderBy.size();
        if (0 < orderBy.size()) {
            BusinessOrder order = orderBy.iterator().next();
            String direction = order.isAscending() ? " asc " : " desc ";
            sql += " order by " + order.getProperty() + direction;
        }
        if (0 < maxResults) {
            // In Oracle, limit is applied BEFORE sort, not after!
            sql = /* FIXME rewrite */ "select * from (" + sql + ") where rownum<=" + maxResults;
        }
        try {
            Collection<InspectionView> ret = new ArrayList();
            ManufacturerPreparedStatement query = this.connection.prepareStatement(sql);
            if (null != after) {
                query.setTimestamp(1, after); // note this is fragile if you add more criteria
            }
            ResultSet result = query.executeQuery();
            while (result.next()) {
                InspectionView bean = makeView(result);
                ret.add(bean);
            }
            result.close();
            query.close();
            return ret;
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    private InspectionView makeView(ResultSet result) throws SQLException, BusinessException {
        InspectionViewImpl ret = new InspectionViewImpl();
        long id = result.getLong("image_sched_id"); /* FIXME column name */
        ret.setId(id); 
        // experiment_id, plate_id, 
        ret.setDetails(result.getString("description")); /* FIXME column name */

        long plateId = result.getLong("plate_id"); /* FIXME column name */
        // process experiment
        TrialPlate plate = TrialServiceImpl.doFindTrialPlate(this.connection, plateId, this.sampleCache);
        ret.setBarcode(plate.getBarcode());

        // process plate

        Calendar inspectionDate = Calendar.getInstance();
        long millis = result.getTimestamp(PROP_SCHEDULED_DATE).getTime();
        inspectionDate.setTimeInMillis(millis);
        ret.setDate(inspectionDate);

        String inspectionName = getInspectionName(inspectionDate, plate.getBarcode());
        ret.setInspectionName(inspectionName);
        //MAYBE addImages(id, ret, plate);
        return ret;
    }

    /**
     * PlateInspectionServiceImpl.convertPropertyName
     * 
     * @see org.pimslims.business.criteria.PropertyNameConvertor#convertPropertyName(java.lang.String)
     */
    @Override
    public String convertPropertyName(String property) throws BusinessException {
        if ("date".equals(property)) {
            return PROP_SCHEDULED_DATE;
        }
        return property;
    }

    /**
     * PlateInspectionServiceImpl.findInspectionIds
     * 
     * @param date
     * @param after
     * @return
     * @throws BusinessException
     */
    public Collection<Integer> findInspectionIds(Calendar date, boolean after) throws BusinessException {

    	
    	String cutoff=new Timestamp(date.getTimeInMillis()).toString();
    	assert(cutoff.length()>20);
    	while(cutoff.length()<23){
    		cutoff+="0";
    	}
//        String sql =
//            	"SELECT ID,CONVERT(varchar(25),DateImaged,121) AS DateImaged " +
//            	"FROM ImagingTask " +
//    			"WHERE CONVERT(varchar(25),DateImaged,121) " + (after ? ">" : "<=") + " '"+cutoff+"' ORDER BY DateImaged ASC";
        String sql =
            	"SELECT ImageBatch.ID AS ID,CONVERT(varchar(25),ImagingTask.DateImaged,121) AS DateImaged " +
            	"FROM ImagingTask, ImageBatch " +
    			"WHERE ImagingTask.ID=ImageBatch.ImagingTaskID " +
				"AND CONVERT(varchar(25),ImagingTask.DateImaged,121) " + (after ? ">" : "<=") + " '"+cutoff+"' " +
    			" ORDER BY ImagingTask.DateImaged ASC";
        try {
            Collection<Integer> ret = new ArrayList<Integer>();
            ManufacturerPreparedStatement query = this.connection.prepareStatement(sql);
            ResultSet result = query.executeQuery();
            while (result.next()) {
            	int inspectionId=result.getInt("ID");
                ret.add(inspectionId);
            }
            result.close();
            query.close();
            return ret;
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

}
