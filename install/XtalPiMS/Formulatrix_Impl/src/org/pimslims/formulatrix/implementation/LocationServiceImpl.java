/**
 * implementation LocationServiceImpl.java
 * 
 * @author cm65
 * @date 13 Jun 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.formulatrix.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.service.LocationService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.exception.BusinessException;

/**
 * LocationServiceImpl
 * 
 */
public class LocationServiceImpl implements LocationService {

    private final ManufacturerConnection connection;
    private final DataStorage dataStorage;
    
    /**
     * Constructor for LocationServiceImpl
     * 
     * @param connection
     * @param dataStorage TODO
     */
    public LocationServiceImpl(ManufacturerConnection connection, DataStorage dataStorage) {
        this.connection = connection;
        this.dataStorage=dataStorage;
    }

    /**
     * LocationServiceImpl.close
     * 
     * @see org.pimslims.business.core.service.LocationService#close(org.pimslims.business.core.model.Location)
     */
    @Override
    public void close(Location location) throws BusinessException {

    }

    /**
     * LocationServiceImpl.create
     * 
     * @see org.pimslims.business.core.service.LocationService#create(org.pimslims.business.core.model.Location)
     */
    @Override
    public void create(Location location) throws BusinessException {
        // MAYBE also insert other values
        String insert = "INSERT INTO Imager" +
        		" (RobotID, Name, Status)" +
        		" VALUES (0,?,1)";
        try {
            ManufacturerPreparedStatement statement = this.connection.prepareStatement(insert);
            statement.setString(1, location.getName());
            statement.executeUpdate();
            
            statement=this.connection.prepareStatement("UPDATE Imager SET RobotID=ID WHERE RobotID=0");
            statement.executeUpdate();
            
            statement.close();
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * LocationServiceImpl.find
     * 
     * @see org.pimslims.business.core.service.LocationService#find(long)
     */
    @Override
    public Location find(long id) throws BusinessException {
        try {
            ManufacturerPreparedStatement statement =
                this.connection.prepareStatement("SELECT * FROM Imager WHERE ID=?");
            statement.setLong(1, id);
            Location ret = makeBean(statement);
            statement.close();
            return ret;
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    static Location doFind(ManufacturerConnection connection2, long id) throws SQLException {
        ManufacturerPreparedStatement statement =
            connection2.prepareStatement("SELECT * FROM Imager WHERE ID=?");
        statement.setLong(1, id);
        Location ret = makeBean(statement);
        statement.close();
        return ret;
    }
    
    /**
     * LocationServiceImpl.findAll
     * 
     * @see org.pimslims.business.core.service.LocationService#findAll(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<Location> findAll(BusinessCriteria criteria) throws BusinessException {
        try {
        	ManufacturerPreparedStatement statement =
                    this.connection.prepareStatement("SELECT * FROM Imager ORDER BY ID");
                ResultSet rs = statement.executeQuery();
                Collection<Location> locations = new ArrayList<Location>();
                while (rs.next()) {
                    Location location = new Location();
                    location.setId(rs.getLong("ID"));
                    location.setName(rs.getString("Name"));
                    locations.add(location);
                }
                statement.close();
                return locations;
        }
        catch (Throwable t) {
        	throw new BusinessException(t);
        }
    }

    /**
     * LocationServiceImpl.findByName
     * 
     * @see org.pimslims.business.core.service.LocationService#findByName(java.lang.String)
     */
    @Override
    public Location findByName(String name) throws BusinessException {
        try {
            ManufacturerPreparedStatement statement =
                this.connection.prepareStatement("SELECT * FROM Imager WHERE Name=?");
            statement.setString(1, name);
            return makeBean(statement);
        } catch (SQLException e) {
            throw new BusinessException(e);
        }

    }

    private static Location makeBean(ManufacturerPreparedStatement statement) throws SQLException {
    	ResultSet result = statement.executeQuery();
    	boolean found = result.next();
        if (!found) {
            return null;
        }

        Location ret = new Location();
        ret.setId(result.getLong("ID"));
        ret.setName(result.getString("Name"));

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
        
    }

    /**
     * LocationServiceImpl.getDataStorage
     * 
     * @see org.pimslims.business.BaseService#getDataStorage()
     */
    @Override
    public DataStorage getDataStorage() {
        return this.dataStorage;
    }

	@Override
	public void createImageAndLink(Image arg0) throws BusinessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createImagesAndLink(Collection<Image> arg0)
			throws BusinessException {
		// TODO Auto-generated method stub
		
	}

}
