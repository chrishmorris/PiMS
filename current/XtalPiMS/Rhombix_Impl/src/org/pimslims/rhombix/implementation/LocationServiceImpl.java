/**
 * Rhombix_Impl org.pimslims.rhombix.implementation LocationServiceImpl.java
 * 
 * @author cm65
 * @date 13 Jun 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.service.LocationService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.exception.BusinessException;

/**
 * LocationServiceImpl
 * 
 */
public class LocationServiceImpl implements LocationService {

    private final RhombixConnection connection;

    /**
     * Constructor for LocationServiceImpl
     * 
     * @param connection
     */
    public LocationServiceImpl(RhombixConnection connection) {
        this.connection = connection;
    }

    /**
     * LocationServiceImpl.close
     * 
     * @see org.pimslims.business.core.service.LocationService#close(org.pimslims.business.core.model.Location)
     */
    @Override
    public void close(Location location) throws BusinessException {
        // TODO Auto-generated method stub

    }

    public Location save(Location location) throws BusinessException {
   		create(location);
   		return findByName(location.getName());
    }
    
    /**
     * LocationServiceImpl.create
     * 
     * @see org.pimslims.business.core.service.LocationService#create(org.pimslims.business.core.model.Location)
     */
    @Override
    public void create(Location location) throws BusinessException {
        // TODO also insert other values
        String insert = "insert into device (site_id, device_id, name) values (1, device_seq.nextval, ?)";
        try {
            RhombixPreparedStatement statement = this.connection.prepareStatement(insert);
            statement.setString(1, location.getName());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
        //TODO set id
    }

    /**
     * LocationServiceImpl.find
     * 
     * @see org.pimslims.business.core.service.LocationService#find(long)
     */
    @Override
    public Location find(long id) throws BusinessException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * LocationServiceImpl.findAll
     * 
     * @see org.pimslims.business.core.service.LocationService#findAll(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<Location> findAll(BusinessCriteria criteria) throws BusinessException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * LocationServiceImpl.findByName
     * 
     * @see org.pimslims.business.core.service.LocationService#findByName(java.lang.String)
     */
    @Override
    public Location findByName(String name) throws BusinessException {
        try {
            RhombixPreparedStatement statement =
                this.connection.prepareStatement("select * from device where name=?");
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            return makeBean(result);
        } catch (SQLException e) {
            throw new BusinessException(e);
        }

    }

    private Location makeBean(ResultSet result) throws SQLException {
        boolean found = result.next();
        if (!found) {
            return null;
        }

        Location ret = new Location();
        ret.setId(result.getLong("device_id"));
        ret.setName(result.getString("name"));

        assert !result.next() : "More than one result for imager";
        result.close();
        return ret;
    }

    /**
     * LocationServiceImpl.update
     * 
     * @see org.pimslims.business.core.service.LocationService#update(org.pimslims.business.core.model.Location)
     */
    @Override
    public void update(Location location) throws BusinessException {
        // TODO Auto-generated method stub

    }

    /**
     * LocationServiceImpl.getDataStorage
     * 
     * @see org.pimslims.business.BaseService#getDataStorage()
     */
    @Override
    public DataStorage getDataStorage() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * LocationServiceImpl.createImagesAndLink
     * 
     * @see org.pimslims.business.crystallization.service.LocationService#createAndLink(java.util.Collection)
     */
    @Override
    public void createImagesAndLink(Collection<Image> images) throws BusinessException {
        for (Iterator iterator = images.iterator(); iterator.hasNext();) {
            Image image = (Image) iterator.next();
            this.createImageAndLink(image);
        }
    }

    /**
     * ImagerServiceImpl.createImageAndLink
     * 
     * @see org.pimslims.business.crystallization.service.ImagerService#createAndLink(org.pimslims.business.crystallization.model.Image)
     */
    @Override
    public void createImageAndLink(Image image) throws BusinessException {
        doCreateImageAndLink(image);

    }

    private void doCreateImageAndLink(Image image) throws BusinessException {
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
    
    
    
}
