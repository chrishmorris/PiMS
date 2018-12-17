/**
 *  DataStorageImpl.java
 * 
 * @author cm65
 * @date 12 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.formulatrix.implementation;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.core.service.ConstructService;
import org.pimslims.business.core.service.GroupService;
import org.pimslims.business.core.service.LocationService;
import org.pimslims.business.core.service.OrganisationService;
import org.pimslims.business.core.service.PersonService;
import org.pimslims.business.core.service.ProjectService;
import org.pimslims.business.core.service.SampleService;
import org.pimslims.business.core.service.TargetService;
import org.pimslims.business.crystallization.service.ComponentService;
import org.pimslims.business.crystallization.service.ConditionService;
import org.pimslims.business.crystallization.service.HumanScoreService;
import org.pimslims.business.crystallization.service.ImageService;
import org.pimslims.business.crystallization.service.ImagerService;
import org.pimslims.business.crystallization.service.PlateExperimentService;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.crystallization.service.ScheduleService;
import org.pimslims.business.crystallization.service.ScoringSchemeService;
import org.pimslims.business.crystallization.service.ScreenService;
import org.pimslims.business.crystallization.service.SoftwareScoreService;
import org.pimslims.business.crystallization.service.SoftwareService;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.exception.BusinessException;

/**
 * DataStorageImpl
 * 
 */
public class ManufacturerDataStorageImpl implements DataStorage {

    ManufacturerConnection connection;

    private final boolean readOnly;

    private final ManufacturerVersion version;

    private final Map<Long, Sample> sampleCache = new HashMap<Long, Sample>();

    /**
     * Constructor for RhombixDataStorageImpl
     * 
     * @param readOnly
     * @param version 
     */
    public ManufacturerDataStorageImpl(boolean readOnly, ManufacturerVersion version) {
        super();
        this.readOnly = readOnly;
        this.version = version;
    }

    /**
     * DataStorageImpl.abort
     * 
     * @see org.pimslims.business.DataStorage#abort()
     */
    @Override
    public void abort() throws BusinessException {
        try {
            if (null != this.connection && !this.connection.isClosed()) {
                this.connection.rollback();
                this.connection.close();
                this.sampleCache.clear();
            }
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * DataStorageImpl.beginTransaction
     * 
     * @see org.pimslims.business.DataStorage#beginTransaction()
     */
    @Override
    public void beginTransaction() throws BusinessException {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new BusinessException(e);
        }

    }

    /**
     * DataStorageImpl.canReadAll
     * 
     * @see org.pimslims.business.DataStorage#canReadAll()
     */
    @Override
    public boolean canReadAll() throws BusinessException {
        // this simple implementation does not include access rights
        return true;
    }

    /**
     * DataStorageImpl.canWriteAll
     * 
     * @see org.pimslims.business.DataStorage#canWriteAll()
     */
    @Override
    public boolean canWriteAll() throws BusinessException {
        // this simple implementation does not include access rights
        return true;
    }

    /**
     * DataStorageImpl.closeResources
     * 
     * @see org.pimslims.business.DataStorage#closeResources()
     */
    @Override
    public void closeResources() throws BusinessException {

        try {
            if (null != this.connection && !this.connection.isClosed()) {
                this.connection.commit();
                this.connection.close();
            }
        } catch (SQLException e) {
            throw new BusinessException(e);
        }

    }

    /**
     * DataStorageImpl.commit
     * 
     * @see org.pimslims.business.DataStorage#commit()
     */
    @Override
    public void commit() throws BusinessException {
        try {
            this.connection.commit();
        } catch (SQLException e) {
            throw new BusinessException(e);
        }

    }

    /**
     * DataStorageImpl.connectResources
     * 
     * @see org.pimslims.business.DataStorage#connectResources()
     */
    @Override
    public void connectResources() throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * DataStorageImpl.disconnectResources
     * 
     * @see org.pimslims.business.DataStorage#disconnectResources()
     */
    @Override
    public void disconnectResources() throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * DataStorageImpl.flush
     * 
     * @see org.pimslims.business.DataStorage#flush()
     */
    @Override
    public void flush() throws BusinessException {
        // MAYBE make this transactional
    }

    /**
     * DataStorageImpl.getComponentService
     * 
     * @see org.pimslims.business.DataStorage#getComponentService()
     */
    @Deprecated
    // not implemented
    public ComponentService getComponentService() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * DataStorageImpl.getConditionService
     * 
     * @see org.pimslims.business.DataStorage#getConditionService()
     */
    @Override
    public ConditionService getConditionService() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * DataStorageImpl.getConstructService
     * 
     * @see org.pimslims.business.DataStorage#getConstructService()
     */
    @Override
    public ConstructService getConstructService() {
        return new ConstructServiceImpl(this.connection);
    }

    /**
     * DataStorageImpl.getGroupService
     * 
     * @see org.pimslims.business.DataStorage#getGroupService()
     */
    @Override
    public GroupService getGroupService() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * DataStorageImpl.getHumanScoreService
     * 
     * @see org.pimslims.business.DataStorage#getHumanScoreService()
     */
    @Override
    public HumanScoreService getHumanScoreService() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * DataStorageImpl.getImageService
     * 
     * @see org.pimslims.business.DataStorage#getImageService()
     */
    @Override
    public ImageService getImageService() {
        return new ImageServiceImpl(this.connection, this.version);
    }

    /**
     * DataStorageImpl.getImagerService
     * 
     * @see org.pimslims.business.DataStorage#getImagerService()
     */
    @Override
    public ImagerService getImagerService() {
        return new ImagerServiceImpl(this.connection, this.version);
    }

    /**
     * DataStorageImpl.getLocationService
     * 
     * @see org.pimslims.business.DataStorage#getLocationService()
     */
    @Override
    public LocationService getLocationService() {
        return new LocationServiceImpl(this.connection, this);
    }

    /**
     * DataStorageImpl.getOrganisationService
     * 
     * @see org.pimslims.business.DataStorage#getOrganisationService()
     */
    @Override
    public OrganisationService getOrganisationService() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * DataStorageImpl.getPersonService
     * 
     * @see org.pimslims.business.DataStorage#getPersonService()
     */
    @Override
    public PersonService getPersonService() {
        return new PersonServiceImpl(this.connection);
    }

    /**
     * DataStorageImpl.getPlateExperimentService
     * 
     * @see org.pimslims.business.DataStorage#getPlateExperimentService()
     */
    @Override
    public PlateExperimentService getPlateExperimentService() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * DataStorageImpl.getPlateInspectionService
     * 
     * @see org.pimslims.business.DataStorage#getPlateInspectionService()
     */
    @Override
    public PlateInspectionService getPlateInspectionService() {
        return new PlateInspectionServiceImpl(this.connection, this.version, this);
    }

    /**
     * DataStorageImpl.getProjectService
     * 
     * @see org.pimslims.business.DataStorage#getProjectService()
     */
    @Override
    public ProjectService getProjectService() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * DataStorageImpl.getSampleService
     * 
     * @see org.pimslims.business.DataStorage#getSampleService()
     */
    @Override
    public SampleService getSampleService() {
        return new SampleServiceImpl(this.connection);
    }

    /**
     * DataStorageImpl.getScheduleService
     * 
     * @see org.pimslims.business.DataStorage#getScheduleService()
     */
    @Override
    public ScheduleService getScheduleService() {
        return new ScheduleServiceImpl(this.connection);
    }

    /**
     * DataStorageImpl.getScoringSchemeService
     * 
     * @see org.pimslims.business.DataStorage#getScoringSchemeService()
     */
    @Override
    public ScoringSchemeService getScoringSchemeService() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * DataStorageImpl.getScreenService
     * 
     * @see org.pimslims.business.DataStorage#getScreenService()
     */
    @Override
    public ScreenService getScreenService() {
        return new ScreenServiceImpl(this.connection);
    }

    /**
     * DataStorageImpl.getSoftwareScoreService
     * 
     * @see org.pimslims.business.DataStorage#getSoftwareScoreService()
     */
    @Override
    public SoftwareScoreService getSoftwareScoreService() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * DataStorageImpl.getSoftwareService
     * 
     * @see org.pimslims.business.DataStorage#getSoftwareService()
     */
    @Override
    public SoftwareService getSoftwareService() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * DataStorageImpl.getTargetService
     * 
     * @see org.pimslims.business.DataStorage#getTargetService()
     */
    @Override
    public TargetService getTargetService() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * DataStorageImpl.getTrialService
     * 
     * @see org.pimslims.business.DataStorage#getTrialService()
     */
    @Override
    public TrialService getTrialService() {
        return new TrialServiceImpl(this.connection, this);
    }

    /**
     * DataStorageImpl.openResources
     * TODO 00
     * @see org.pimslims.business.DataStorage#openResources(java.lang.String)
     */
    @Override
    public void openResources(String username) throws BusinessException {
        this.connection = new ManufacturerConnectionImpl(this.readOnly);
        this.beginTransaction();
        System.out.println("Connected to Manufacturer's database");
    }

    /**
     * DataStorageImpl.openResources
     * 
     * @see org.pimslims.business.DataStorage#openResources(java.lang.String, boolean, boolean)
     */
    @Override
    public void openResources(String username, boolean canReadAll, boolean canWriteAll)
        throws BusinessException {
        this.openResources(username);

    }

    static void insert(String tableName, String keyName, ManufacturerConnection mfrConnection, String value) {
        try {
            ManufacturerPreparedStatement statement =
                mfrConnection.prepareStatement(/* FIXME rewrite */ "insert into " 
                	+ tableName + " ("
                    + keyName + ") values (?)");
            statement.setString(1, value);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * RhombixDataStorageImpl.getSampleCache
     * 
     * @return
     */
    public Map<Long, Sample> getSampleCache() {
        return this.sampleCache;
    }

}
