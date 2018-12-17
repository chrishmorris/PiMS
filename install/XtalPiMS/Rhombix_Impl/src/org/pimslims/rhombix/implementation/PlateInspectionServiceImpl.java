/**
 * Rhombix_Impl org.pimslims.rhombix PlateInspectionServiceImpl.java
 * 
 * @author cm65
 * @date 14 Mar 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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

/**
 * PlateInspectionServiceImpl
 * 
 */
public class PlateInspectionServiceImpl implements PlateInspectionService {

    /**
     * ACTUAL_START_DATETIME is often null
     */
    static final String PROP_SCHEDULED_DATE = "scheduled_start_datetime";

    private static final Location IMAGER = new Location();
    static {
        IMAGER.setName("Rhombix");
 //       IMAGER.setDefaultImageType(ImageType.COMPOSITE);
    }

    private final RhombixConnection connection;

    private final RhombixVersion version;

    private final Map<Long, Sample> sampleCache;

    /**
     * Constructor for PlateInspectionServiceImpl
     * 
     * @param rhombixConnection
     * @param dataStorage
     */
    public PlateInspectionServiceImpl(RhombixConnection rhombixConnection, RhombixVersion version,
        RhombixDataStorageImpl dataStorage) {
        this.connection = rhombixConnection;
        this.version = version;
        this.sampleCache = dataStorage.getSampleCache();
    }

    /**
     * PlateInspectionServiceImpl.create
     * 
     * @see org.pimslims.business.crystallization.service.PlateInspectionService#create(org.pimslims.business.crystallization.model.PlateInspection)
     */
    @Override
    public void create(PlateInspection inspection) throws BusinessException {

        String insert =
            "insert into image_sched (site_id, image_sched_id, experiment_id, plate_id, description, "
                + PROP_SCHEDULED_DATE + ") values (1, image_sched_seq.nextval, ?, ?, ?, ?)";
        try {
            RhombixPreparedStatement statement = this.connection.prepareStatement(insert);
            TrialPlateImpl plate = (TrialPlateImpl) inspection.getPlate();
            statement.setLong(1, plate.getRhombixExperimentId());
            statement.setLong(2, plate.getId());
            statement.setString(3, inspection.getDetails());
            Calendar startDate = inspection.getInspectionDate();
            statement.setTimestamp(4, new java.sql.Timestamp(startDate.getTimeInMillis()));
            statement.executeUpdate();
            statement.close();

            String sequence = "image_sched";
            long inspectionId = getCurrentValue(this.connection, sequence);
            inspection.setId(inspectionId);
            String barcode = inspection.getPlate().getBarcode();
            String inspectionName = getInspectionName(startDate, barcode);
            inspection.setInspectionName(inspectionName);

            /* Check: PlateInspection found = find(inspectionId);
            System.out.println("Saved: " + found.getInspectionDate().getTimeInMillis()); */
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    static long getCurrentValue(RhombixConnection rhombixConnection, String sequence) throws SQLException {
        RhombixPreparedStatement query =
            rhombixConnection.prepareStatement("select " + sequence + "_seq.currval from dual");
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

    /**
     * PlateInspectionServiceImpl.find
     * 
     * @see org.pimslims.business.crystallization.service.PlateInspectionService#find(long)
     */
    @Override
    public PlateInspection find(long id) throws BusinessException {
        try {
            RhombixPreparedStatement query =
                this.connection.prepareStatement("select * from image_sched where image_sched_id=?");
            query.setLong(1, id);
            ResultSet result = query.executeQuery();
            boolean hasNext = result.next();
            assert hasNext;

            PlateInspection ret = makeBean(result);

            assert !result.next();
            result.close();
            query.close();
            return ret;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    private PlateInspection makeBean(ResultSet result) throws SQLException, BusinessException {
        PlateInspection ret = new PlateInspection();
        ret.setLocation(IMAGER);
        long id = result.getLong("image_sched_id");
        ret.setId(id);
        // experiment_id, plate_id, 
        ret.setDetails(result.getString("description"));

        long plateId = result.getLong("plate_id");
        // process experiment
        TrialPlate plate = TrialServiceImpl.doFindTrialPlate(this.connection, plateId, this.sampleCache);
        ret.setPlate(plate);

        // process plate

        Calendar inspectionDate = Calendar.getInstance();
        long millis = result.getTimestamp(PROP_SCHEDULED_DATE).getTime();
        inspectionDate.setTimeInMillis(millis);
        ret.setInspectionDate(inspectionDate);

        String inspectionName = getInspectionName(inspectionDate, plate.getBarcode());
        ret.setInspectionName(inspectionName);
        addImages(id, ret, plate);
        return ret;
    }

    private void addImages(long id, PlateInspection ret, TrialPlate plate) throws SQLException {
        RhombixPreparedStatement query;
        ResultSet result;
        // add images
        Collection<Image> images = new ArrayList();
        //TODO this has an unnecessary join to image_sched
        query =
            this.connection.prepareStatement(this.version.getImageSelectStatement()
                + " and image.image_sched_id = ?");
        query.setLong(1, plate.getId());
        query.setLong(2, id);
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
        //ret.setId(result.getLong("well_compartment_id"));
        long mmPerPixel = result.getLong("millimeters_per_pixel");
        ret.setXLengthPerPixel(new Double(mmPerPixel));
        ret.setYLengthPerPixel(new Double(mmPerPixel));

        int row = result.getInt("row_num");
        int column = result.getInt("column_num");
        int subPosition = result.getInt("lu_compartment_id");
        WellPosition well = new WellPosition(row, column, subPosition);
        //TODO 
        TrialDrop drop = plate.getTrialDrop(well);
        if (null == drop) {
            System.err.println("No drop found: " + plate.getBarcode() + ":" + well.toString());
        }
        ret.setDrop(drop);

        Calendar startDate = Calendar.getInstance();
        Timestamp date = result.getTimestamp("CREATE_DATETIME", startDate);
        startDate.setTimeInMillis(date.getTime());
        ret.setImageDate(startDate); //create_datetime
        //ret.setDescription(description);
        //ret.setSizeY(height);
        //ret.setImageType(imageType); // image.lu_image_type_id
        //ret.setInstrument(instrument); // image.device_id
        //TODO ret.setScreen(screen);
        //ret.setTemperature(temperature);
        //ret.setTimePoint(timePoint);

        //TODO some MPL records use path column of image_path table.
        // Example value: \\DC1512086\images_fdrv\DCA\EM-TFPDM-MG-20\090421\

        String rhombix_path = result.getString("filename");
        String filepath = getPimsPath(rhombix_path);
        ret.setImagePath(filepath);
        //ret.setSizeX(width);

        //What is image.image_index?
        //what is image.lu_image_format_mask?
        // Image table also has polarization_angle, offset_long_axis, offset_short_axis, focus_height
        // well_compartment also has offset_long_axis, offset_short_axis, focus_height, field_of_view, 
        // exposure_base, exposure_base_dark, exposure_base_polar, drop_volume

        return ret;
    }

    /**
     * @param rhombix_path
     * @return
     */
    String getPimsPath(String rhombix_path) {
        String filepath = rhombix_path;
        /* if (!IS_URL.matcher(filepath).matches()) {
            filepath = "file://" + filepath;
        } */
        if (!HAS_EXTENSION.matcher(filepath).matches()) {
            filepath += ".jpg";
        }
        if (filepath.startsWith(this.version.getPathPrefix())) {
            filepath = filepath.substring(this.version.getPathPrefix().length());
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
        try {
            RhombixPreparedStatement query =
                this.connection.prepareStatement("select * from image_sched where image_sched_id=?");
            query.setString(1, name);
            ResultSet result = query.executeQuery();
            assert result.next();

            PlateInspection ret = makeBean(result);

            assert !result.next();
            result.close();
            query.close();

            return ret;
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
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
        String sql = "select * from image_sched where ";

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
            sql = "select * from (" + sql + ") where rownum<=" + maxResults;
        }
        try {
            Collection<InspectionView> ret = new ArrayList();
            RhombixPreparedStatement query = this.connection.prepareStatement(sql);
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
        long id = result.getLong("image_sched_id");
        ret.setId(id);
        // experiment_id, plate_id, 
        ret.setDetails(result.getString("description"));

        long plateId = result.getLong("plate_id");
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
        //TODO addImages(id, ret, plate);
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
    public Collection<Long> findInspectionIds(Calendar date, boolean after) throws BusinessException {

        String sql =
            "select * from image_sched where scheduled_start_datetime " + (after ? ">" : "<=") + " ?";

        try {
            Collection<Long> ret = new ArrayList();
            RhombixPreparedStatement query = this.connection.prepareStatement(sql);
            query.setTimestamp(1, new Timestamp(date.getTimeInMillis()));
            ResultSet result = query.executeQuery();
            while (result.next()) {
                ret.add(result.getLong(1));
            }
            result.close();
            query.close();
            return ret;
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }
}
