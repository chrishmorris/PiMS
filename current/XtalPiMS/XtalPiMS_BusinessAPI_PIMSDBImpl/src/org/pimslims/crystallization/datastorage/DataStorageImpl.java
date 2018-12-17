package org.pimslims.crystallization.datastorage;

import org.pimslims.business.DataStorage;
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
import org.pimslims.business.crystallization.service.CrystalService;
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
import org.pimslims.crystallization.implementation.ComponentServiceImpl;
import org.pimslims.crystallization.implementation.ConditionServiceImpl;
import org.pimslims.crystallization.implementation.ConstructServiceImpl;
import org.pimslims.crystallization.implementation.CrystalServiceImpl;
import org.pimslims.crystallization.implementation.GroupServiceImpl;
import org.pimslims.crystallization.implementation.HumanScoreServiceImpl;
import org.pimslims.crystallization.implementation.ImageServiceImpl;
import org.pimslims.crystallization.implementation.ImagerServiceImpl;
import org.pimslims.crystallization.implementation.LocationServiceImpl;
import org.pimslims.crystallization.implementation.OrganisationServiceImpl;
import org.pimslims.crystallization.implementation.PersonServiceImpl;
import org.pimslims.crystallization.implementation.PlateExperimentServiceImpl;
import org.pimslims.crystallization.implementation.PlateInspectionServiceImpl;
import org.pimslims.crystallization.implementation.ProjectServiceImpl;
import org.pimslims.crystallization.implementation.SampleServiceImpl;
import org.pimslims.crystallization.implementation.ScheduleServiceImpl;
import org.pimslims.crystallization.implementation.ScoringSchemeServiceImpl;
import org.pimslims.crystallization.implementation.ScreenServiceImpl;
import org.pimslims.crystallization.implementation.SoftwareScoreServiceImpl;
import org.pimslims.crystallization.implementation.SoftwareServiceImpl;
import org.pimslims.crystallization.implementation.TargetServiceImpl;
import org.pimslims.crystallization.implementation.TrialDropServiceImpl;
import org.pimslims.crystallization.implementation.TrialServiceImpl;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.FlushMode;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.ReadableVersionImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;

/**
 * Represents the PiMS database
 * 
 * @author cm65
 * 
 */
public class DataStorageImpl implements DataStorage {

    private final AbstractModel model;

    private ReadableVersion version = null;

    private String username = "";

    private boolean canReadAll = false;

    private boolean canWriteAll = false;

    public DataStorageImpl(final AbstractModel model) {
        super();
        this.model = model;
    }

    public DataStorageImpl(final ReadableVersion version) {
        super();
        assert version != null;
        this.model = version.getModel();
        this.version = version;
        this.username = version.getUsername();
    }

    /**
     * 
     * @return
     */
    public ReadableVersion getVersion() {
        return version;
    }

    public WritableVersion getWritableVersion() {
        return (WritableVersion) getVersion();
    }

    /**
     * <p>
     * This function will completely close any connections that have been created to data stores such as
     * databases or files.
     * </p>
     * 
     * @throws org.pimslims.business.exception.BusinessException <p>
     *             Thrown if there is any type of failure within the function
     *             </p>
     */
    public void disconnectResources() throws BusinessException {
        closeResources();
    }

    /**
     * 
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void connectResources() throws BusinessException {

    }

    /**
     * <p>
     * This will close any resources that have been opened, however this does not include the termination of a
     * connection (to say a database), to do this use the terminateResources function
     * </p>
     * 
     * @throws BusinessException <p>
     *             Thrown if there is any type of failure within the function
     *             </p>
     */
    public void closeResources() throws BusinessException {
        if (this.version != null && !this.version.isCompleted()) {
            abort();
        }

    }

    /**
     * <p>
     * This function will open any resources that will be used to store or retrieve data from a data source.
     * </p>
     * <p>
     * This may include opening a connection to that resource if it has not already been done
     * </p>
     * 
     * @param username <p>
     *            The username of the user making the connection
     *            </p>
     * @throws BusinessException <p>
     *             Thrown if there is any type of failure within the function
     *             </p>
     */
    public void openResources(final String username) throws BusinessException {
        this.username = username;
        if (version != null) {
            System.out.println("Version already opened:" + version);
            final StackTraceElement[] trace = ((ReadableVersionImpl) version).getStackTrace();
            for (final StackTraceElement stackTraceElement : trace) {
                System.out.println(stackTraceElement.toString());

            }
            throw new BusinessException("Version already opened:" + version);
        }
        // open a connection
        version = model.getWritableVersion(username);
    }

    /**
     * 
     * @throws org.pimslims.business.exception.BusinessException
     */
    @Override
    public void abort() throws BusinessException {
        if (this.version != null) {
            this.version.abort();
        }
        this.version = null;
        // System.out.println(this.getClass()+" aborted a session for "+username);

    }

    /**
     * 
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void commit() throws BusinessException {
        try {
            this.version.commit();
            this.version = null;
            // System.out.println(this.getClass()+" committed a session for "+username);
        } catch (final AbortedException e) {
            // retriable error
            throw new BusinessException(e);
        } catch (final ConstraintException e) {
            // data model constraints violated
            throw new BusinessException(e);
        }
    }

    /**
     * 
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * 
     * @return
     */
    public ScreenService getScreenService() {
        return new ScreenServiceImpl(this);
    }

    /**
     * 
     * @return
     */
    public ScoringSchemeService getScoringSchemeService() {
        return new ScoringSchemeServiceImpl(this);
    }

    /**
     * 
     * @return
     */
    public SampleService getSampleService() {
        return new SampleServiceImpl(this);
    }

    /**
     * 
     * @return
     */
    public ProjectService getProjectService() {
        return new ProjectServiceImpl(this);
    }

    /**
     * 
     * @return
     */
    public ConstructService getConstructService() {
        return new ConstructServiceImpl(this);
    }

    /**
     * 
     * @return
     */
    public GroupService getGroupService() {
        return new GroupServiceImpl(this);

    }

    /**
     * 
     * @return
     */
    public ImageService getImageService() {
        return new ImageServiceImpl(this);
    }

    /**
     * 
     * @return
     */
    public LocationService getLocationService() {
        return new LocationServiceImpl(this);
    }

    /**
     * 
     * @return
     */
    public OrganisationService getOrganisationService() {
        return new OrganisationServiceImpl(this);
    }

    /**
     * 
     * @return
     */
    public PersonService getPersonService() {
        return new PersonServiceImpl(this);
    }

    /**
     * 
     * @return
     */
    public PlateExperimentService getPlateExperimentService() {
        return new PlateExperimentServiceImpl(this);
    }

    /**
     * 
     * @return
     */
    public PlateInspectionService getPlateInspectionService() {
        return new PlateInspectionServiceImpl(this);
    }

    /**
     * 
     * 
     * @return
     */
    public TargetService getTargetService() {
        return new TargetServiceImpl(this);
    }

    /**
     * 
     * 
     * @return
     */
    public TrialService getTrialService() {
        return new TrialServiceImpl(this);
    }

    public TrialDropServiceImpl getTrialDropService() {
        return new TrialDropServiceImpl(this);
    }

    /**
     * 
     * @return
     */
    public SoftwareService getSoftwareService() {
        return new SoftwareServiceImpl(this);
    }

    /**
     * 
     * @throws org.pimslims.business.exception.BusinessException
     */
    @Override
    public void beginTransaction() throws BusinessException {
        // Nothing to do - done automatically
    }

    public ScheduleService getScheduleService() {
        return new ScheduleServiceImpl(this);
    }

    public boolean canReadAll() throws BusinessException {
        return this.canReadAll;
    }

    public boolean canWriteAll() throws BusinessException {
        return this.canWriteAll;
    }

    public void openResources(final String username, final boolean canReadAll, final boolean canWriteAll)
        throws BusinessException {
        openResources(username);
        this.canReadAll = canReadAll;
        this.canWriteAll = canWriteAll;
    }

    public void noAutoFlush() {
        ((WritableVersionImpl) this.version).setFlushMode(FlushMode.fastMode());
    }

    public HumanScoreService getHumanScoreService() {
        return new HumanScoreServiceImpl(this);
    }

    public SoftwareScoreService getSoftwareScoreService() {
        return new SoftwareScoreServiceImpl(this);
    }

    public ConditionService getConditionService() {
        return new ConditionServiceImpl(this);
    }

    @Deprecated
    // not implemented
    public ComponentService getComponentService() {
        return new ComponentServiceImpl(this);
    }

    public void flush() throws BusinessException {
        try {
            ((WritableVersionImpl) this.version).flush();
        } catch (final ConstraintException e) {
            throw new BusinessException(e);
        }

    }

    /**
     * DataStorage.getImagerService
     * 
     * @see org.pimslims.business.DataStorage#getImagerService()
     */
    public ImagerService getImagerService() {
        return new ImagerServiceImpl(this);
    }

    /**
     * DataStorageImpl.getCrystalService
     * 
     * @see org.pimslims.business.DataStorage#getCrystalService()
     */
    @Override
    public CrystalService getCrystalService() {
        return new CrystalServiceImpl(this);
    }
}
