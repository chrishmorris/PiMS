/*
 * DataStorage.java Created on 10 August 2007, 13:06 To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business;

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
 * This class should be used to store the details of accessing the underlying data store, so this might be a
 * Hibernate Session, a PIMS Transaction, even a file handle according to what the implementation might
 * require.... e.g. for platedb, we require two Hibernate Session variables for use: one for platedb and one
 * for the generic User Management App database.
 * 
 * @author ian
 */
public interface DataStorage {
    public static final String ADMINISTRATOR = "pims-administrator";

    /**
     * <p>
     * This function will provide an instance of the implementation of the DataStorage object
     * </p>
     */
    //abstract DataStorage getInstance() throws BusinessException;
    /**
     * <p>
     * This function will open any resources that will be used to store or retrieve data from a data source.
     * </p>
     * <p>
     * This may include opening a connection to that resource if it has not already been done
     * </p>
     * TODO allow read  only connections, it makes reasoning about security easier
     * @param username <p>
     *            The username of the user making the connection
     *            </p>
     * @throws BusinessException <p>
     *             Thrown if there is any type of failure within the function
     *             </p>
     */
    public void openResources(String username) throws BusinessException;

    /**
     * 
     * @param username
     * @param canReadAll
     * @param canWriteAll
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void openResources(String username, boolean canReadAll, boolean canWriteAll)
        throws BusinessException;

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
    public void closeResources() throws BusinessException;

    /**
     * <p>
     * This function will open any connections that are required by the implementation to data stores such as
     * databases or files.
     * </p>
     * 
     * @throws org.pimslims.business.exception.BusinessException <p>
     *             Thrown if there is any type of failure within the function
     *             </p>
     */
    public void connectResources() throws BusinessException;

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
    public void disconnectResources() throws BusinessException;

    /**
     * <p>
     * Aborts the current write to the data storage
     * </p>
     * 
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void abort() throws BusinessException;

    /**
     * <p>
     * Commits the write to the data storage
     * </p>
     * 
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void commit() throws BusinessException;

    /**
     * <p>
     * Starts a transaction
     * </p>
     * 
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void beginTransaction() throws BusinessException;

    /**
     * Check to see if this user is able to see everything in the database. It is possible that we might need
     * to make this more fine grained?
     * 
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public boolean canReadAll() throws BusinessException;

    /**
     * Check to see if the user is able to write to everything in the db. May need to make this more fine
     * grained?
     * 
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public boolean canWriteAll() throws BusinessException;

    /**
     * 
     * @return
     */
    public PlateExperimentService getPlateExperimentService();

    /**
     * 
     * @return
     */
    public ScreenService getScreenService();

    /**
     * 
     * @return
     */
    public SoftwareService getSoftwareService();

    /**
     * 
     * @return
     */
    public SampleService getSampleService();

    /**
     * 
     * @return
     */
    public PlateInspectionService getPlateInspectionService();

    /**
     * 
     * @return
     */
    public ConstructService getConstructService();

    /**
     * 
     * @return
     */
    public GroupService getGroupService();

    /**
     * 
     * @return
     */
    public ImageService getImageService();

    /**
     * 
     * @return
     */
    public LocationService getLocationService();

    /**
     * 
     * @return
     */
    public OrganisationService getOrganisationService();

    /**
     * 
     * @return
     */
    public PersonService getPersonService();

    /**
     * 
     * @return
     */
    public ProjectService getProjectService();

    /**
     * 
     * @return
     */
    public HumanScoreService getHumanScoreService();

    /**
     * 
     * @return
     */
    public ConditionService getConditionService();



    /**
     * 
     * @return
     */
    public SoftwareScoreService getSoftwareScoreService();

    /**
     * 
     * @return
     */
    public ScoringSchemeService getScoringSchemeService();

    /**
     * 
     * @return
     */
    public TargetService getTargetService();

    /**
     * 
     * @return
     */
    public TrialService getTrialService();

    /**
     * 
     * @return
     */
    public ScheduleService getScheduleService();

    /**
     * 
     * @return
     */
    public org.pimslims.business.crystallization.service.ImagerService getImagerService();
    
    /**
     * 
     * @return
     */
    public org.pimslims.business.crystallization.service.CrystalService getCrystalService();

    public void flush() throws BusinessException;

}
