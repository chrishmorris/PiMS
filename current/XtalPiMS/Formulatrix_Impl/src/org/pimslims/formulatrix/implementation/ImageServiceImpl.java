/**
 *  ImageServiceImpl.java
 * 
 * @author cm65
 * @date 26 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.formulatrix.implementation;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessCriterion;
import org.pimslims.business.criteria.EqualsExpression;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.ImageType;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.ImageService;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.formulatrix.command.Importer;

/**
 * ImageServiceImpl
 * 
 */
public class ImageServiceImpl implements ImageService {

    private final ManufacturerConnection connection;

    private final ManufacturerVersion version;

    /**
     * Constructor for ImageServiceImpl
     * 
     * @param mfrConnection
     */
    public ImageServiceImpl(ManufacturerConnection mfrConnection, ManufacturerVersion version) {
        this.connection = mfrConnection;
        this.version = version;
    }

    /**
     * ImageServiceImpl.create
     * 
     * @see org.pimslims.business.crystallization.service.ImageService#create(org.pimslims.business.crystallization.model.Image)
     */
    public void create(Image image) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ImageServiceImpl.create
     * 
     * @see org.pimslims.business.crystallization.service.ImageService#create(java.util.Collection)
     */
    @Override
    public void create(Collection<Image> image) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ImageServiceImpl.find
     * 
     * @see org.pimslims.business.crystallization.service.ImageService#find(long)
     */
    @Override
    public Image find(long id) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ImageServiceImpl.findAll
     * 
     * @see org.pimslims.business.crystallization.service.ImageService#findAll(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<Image> findAll(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ImageServiceImpl.findLatestImages
     * 
     * @see org.pimslims.business.crystallization.service.ImageService#findLatestImages(org.pimslims.business.criteria.BusinessCriteria)
     */
    public Collection<ImageView> findLatestImages(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ImageServiceImpl.update
     * 
     * @see org.pimslims.business.crystallization.service.ImageService#update(org.pimslims.business.crystallization.model.Image)
     */
    public void update(Image image) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ImageServiceImpl.update
     * 
     * @see org.pimslims.business.crystallization.service.ImageService#update(java.util.Collection)
     */
    @Override
    public void update(Collection<Image> images) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ImageServiceImpl.getDataStorage
     * 
     * @see org.pimslims.business.BaseService#getDataStorage()
     */
    @Override
    public DataStorage getDataStorage() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ImageServiceImpl.findViewCount
     * 
     * @see org.pimslims.business.ViewService#findViewCount(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Integer findViewCount(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ImageServiceImpl.findViews
     * 
     * @see org.pimslims.business.ViewService#findViews(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<ImageView> findViews(BusinessCriteria searchTerms) throws BusinessException {
        List<BusinessCriterion> criteria = searchTerms.getCriteria();
        assert 1 == criteria.size();
        BusinessCriterion criterion = criteria.iterator().next();
        assert (criterion instanceof EqualsExpression);
        EqualsExpression expression = (EqualsExpression) criterion;
        assert expression.isEq();
        try {
            Collection<ImageView> ret;
            if (ImageView.PROP_BARCODE.equals(expression.getProperty())) {
                String barcode = (String) expression.getValue();
                long plateId = TrialServiceImpl.doFindTrialPlateId(this.connection, barcode);
                String sql = getImageSelectStatement();
                ManufacturerPreparedStatement statement = this.connection.prepareStatement(sql);
                statement.setLong(1, plateId);
                ret = makeImageViewBeans(statement, barcode);
                statement.close();
            } else if (ImageView.PROP_INSPECTION_NAME.equals(expression.getProperty())) {
                ret = findByInspectionName((String) expression.getValue());
            } else {
                throw new IllegalArgumentException("Unexpected search expression");
            }
            return ret;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    static Pattern INSPECTION_NAME = Pattern.compile("^(.*)_(\\d{8}_\\d{6})$");

    /**
     * ImageServiceImpl.prepareInspectionNameQuery
     * 
     * @param value
     * @return
     * @throws SQLException
     */
    private Collection<ImageView> findByInspectionName(String name) throws SQLException {
    	System.out.println(name);
        Matcher m = INSPECTION_NAME.matcher(name);
        assert m.matches() : "Unexpected inspection name: " + name;
        String barcode = m.group(1);
        String time = m.group(2);
        Date startTime;
        try {
            startTime = PlateInspectionServiceImpl.FORMAT.parse(time);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long plateId = TrialServiceImpl.doFindTrialPlateId(this.connection, barcode);

        String sql =
            getImageSelectStatement()
                + " AND CONVERT(varchar(25),ImagingTask.DateToImage,120)=?";
        ManufacturerPreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setLong(1, plateId);
        statement.setTimestamp(2, new Timestamp(startTime.getTime()));
        Collection<ImageView> ret = makeImageViewBeans(statement, barcode);
        statement.close();
        return ret;
    }

    private Collection<ImageView> makeImageViewBeans(ManufacturerPreparedStatement statement, String barcode)
        throws SQLException {
        ResultSet result = statement.executeQuery();
        Collection<ImageView> ret = new ArrayList<ImageView>();
        while (result.next()) {
            ret.add(makeImageViewBean(result, barcode));
        }
        result.close();
        return ret;
    }

    private ImageView makeImageViewBean(ResultSet result, String barcode) throws SQLException {

        ImageView ret = new ImageView();
        //ret.setId(result.getLong("well_compartment_id"));
        ret.setBarcode(barcode);
        long mmPerPixel = result.getLong("MicronsPerPixel");
        ret.setWidthPerPixel(new Double(mmPerPixel));
        ret.setHeightPerPixel(new Double(mmPerPixel));
        String row = result.getString("RowLetter");
        int column = result.getInt("ColumnNumber"); 
        int subPosition = result.getInt("DropNumber");
//      int height = result.getInt("Height");
//      int width = result.getInt("Width");

		ImageIcon formulatrixFile=new ImageIcon(Importer.formulatrixPathPrefix + result.getString("ImagePath"));
		int height = formulatrixFile.getIconHeight();
		int width = formulatrixFile.getIconWidth();
		formulatrixFile=null;

		WellPosition well = new WellPosition(row+String.format("%02d", column)+"."+subPosition); //pad column to 2 digits
        ret.setWell(well.toString());

        //MAYBE use path column of image_path table.
        // Example value: \\DC1512086\images_fdrv\DCA\EM-TFPDM-MG-20\090421\

        Calendar startDate = Calendar.getInstance();
        // note this is actual time of the image, not scheduled time, to save a slow join
        Timestamp date = result.getTimestamp("DateImaged", startDate);
        startDate.setTimeInMillis(date.getTime());
        ret.setDate(startDate); //create_datetime
        //ret.setDescription(description);
        ret.setHeight(height);
        ret.setImageType(ImageView.TYPE_COMPOSITE); // image.lu_image_type_id
        ret.setInspectionName(PlateInspectionServiceImpl.getInspectionName(startDate, barcode));
        //ret.setInstrument(instrument); // image.device_id
        //MAYBE ret.setScreen(screen);
        //ret.setTemperature(temperature);
        //ret.setTimePoint(timePoint);
        ret.setUrl(result.getString("ImagePath"));
        ret.setWidth(width);

        //What is image.image_index?
        //what is image.lu_image_format_mask?
        // Image table also has polarization_angle, offset_long_axis, offset_short_axis, focus_height
        // well_compartment also has offset_long_axis, offset_short_axis, focus_height, field_of_view, 
        // exposure_base, exposure_base_dark, exposure_base_polar, drop_volume

        return ret;
    }

    /**
     * ImageServiceImpl.convertPropertyName
     * 
     * @see org.pimslims.business.criteria.PropertyNameConvertor#convertPropertyName(java.lang.String)
     */
    @Override
    public String convertPropertyName(String property) throws BusinessException {
        // e.g. "barcode"
        return property;
    }

    /**
     * @return Returns the sELECT.
     */
    String getImageSelectStatement() {
        return this.version.getImageSelectStatement();
    }

}
