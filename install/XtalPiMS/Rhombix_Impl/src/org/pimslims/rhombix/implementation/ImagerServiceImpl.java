/**
 * Rhombix_Impl org.pimslims.rhombix ImagerServiceImpl.java
 * 
 * @author cm65
 * @date 17 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pimslims.business.core.model.Project;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.Imager;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.ImagerService;
import org.pimslims.business.crystallization.view.ScheduleView;
import org.pimslims.business.crystallization.view.SimpleSampleView;
import org.pimslims.business.exception.BusinessException;

/**
 * ImagerServiceImpl
 * 
 */
@Deprecated
// see LocationServiceImpl
public class ImagerServiceImpl implements ImagerService {

    private final RhombixConnection connection;

    /**
     * Constructor for ImagerServiceImpl
     * 
     * @param rhombixConnection
     */
    public ImagerServiceImpl(RhombixConnection rhombixConnection) {
        this.connection = rhombixConnection;
    }

    private Imager makeImagerBean(ResultSet result) throws SQLException {
        boolean found = result.next();
        if (!found) {
            return null;
        }

        Imager ret = new Imager();
        ret.setId(result.getLong("device_id"));
        ret.setName(result.getString("name"));

        assert !result.next() : "More than one result for imager";
        result.close();
        return ret;
    }

    /**
     * ImagerServiceImpl.createAndLink
     * 
     * @see org.pimslims.business.crystallization.service.ImagerService#createAndLink(org.pimslims.business.crystallization.model.Image)
     */
    @Override
    public void createAndLink(Image image) throws BusinessException {
        doCreateAndLink(image);

    }

    private void doCreateAndLink(Image image) throws BusinessException {
        String select =
            " select well_id\r\n" + "    from well, plate \r\n" + "    where barcode=?\r\n"
                + "    and row_num=? and column_num=?  \r\n"
                + "    and well.plate_type_id = plate.plate_type_id\r\n";
        String insert_wc =
            "insert into well_compartment (site_id, well_compartment_id, well_id, plate_id, lu_compartment_id) \r\n"
                + "  values ( 1, well_compartment_seq.nextval, ?, ?, ? )\r\n";

        String insert_image =
            "insert into image \r\n"
                + "(site_id, image_id, well_compartment_id,   filename, image_sched_id )\r\n" + "values\r\n"
                + "(1, image_seq.nextval, well_compartment_seq.currval,   ?, ? )";

        /* TODO recent records in MPL are like this:
        // but table image_path does not exist in Helsinki
        //String insert_path = "insert into image_path (image_path_id, path) values (image_path_seq.nextval, ? ) ";

        String insert_image =
            "insert into image \r\n"
                + "(image_id, well_compartment_id, image_path_id, filename, image_sched_id )\r\n"
                + "values\r\n"
                + "( image_seq.nextval, well_compartment_seq.currval, image_path_seq.currval, ?, ? )"; */
        TrialDrop drop = image.getDrop();
        WellPosition position = drop.getWellPosition();

        //TODO use path column of image_path table.
        String filename = image.getImagePath();
        /*String[] parsed = parsePath(image.getImagePath());
        String filename = parsed[0];
        String filepath = parsed[1]; */

        try {
            // find the well record
            // it might seem that we can do this with a subselect, 
            // but that gets an ORA-08002 from the second insert
            RhombixPreparedStatement statement = this.connection.prepareStatement(select);
            String barcode = drop.getPlate().getBarcode();
            statement.setString(1, barcode);
            statement.setInt(2, position.getRow());
            statement.setInt(3, position.getColumn());
            ResultSet result = statement.executeQuery();
            assert result.next() : "No well for: " + barcode + ":" + position.toString();
            long wellId = result.getLong("well_id");
            assert !result.next() : "Too many wells for: " + barcode + ":" + position.toString();

            // make the image_path record
            /*statement = this.connection.prepareStatement(insert_path);
            statement.setString(1, filepath);
            statement.executeUpdate();
            statement.close(); */

            // make the well_compartment record
            statement = this.connection.prepareStatement(insert_wc);
            statement.setLong(1, wellId);
            statement.setLong(2, drop.getPlate().getId());
            statement.setInt(3, position.getSubPosition());
            statement.executeUpdate();
            statement.close();

            // make the image record
            statement = this.connection.prepareStatement(insert_image);
            statement.setString(1, filename);
            statement.setLong(2, image.getPlateInspection().getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

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
