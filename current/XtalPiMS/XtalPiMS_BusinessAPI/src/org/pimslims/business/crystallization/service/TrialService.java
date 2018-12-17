package org.pimslims.business.crystallization.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

import org.pimslims.business.BaseService;
import org.pimslims.business.ViewService;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.SchedulePlan;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.view.TrialDropView;
import org.pimslims.business.exception.BusinessException;

public interface TrialService extends BaseService, ViewService<TrialDropView> {

    /* **************************************************************
     * PlateType-related
     * ************************************************************** */
    /**
     * Find a PlateType
     * 
     * @param id
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public abstract PlateType findPlateType(long id) throws BusinessException;

    /**
     * Find a PlateType
     * 
     * @param name
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public abstract PlateType findPlateType(String name) throws BusinessException;

    /**
     * Find all PlateTypes
     * 
     * @return A collection of PlateType
     * @throws org.pimslims.business.exception.BusinessException
     */
    public abstract Collection<PlateType> findAllPlateTypes() throws BusinessException;

    /* **************************************************************
     * TrialPlate-related
     * ************************************************************** */
    /**
     * Get a new, empty TrialPlate for this PlateType
     * 
     * @param plateType
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public abstract TrialPlate createTrialPlate(String barcode, PlateType plateType) throws BusinessException;

    /**
     * Find a TrialPlate
     * 
     * @param id
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public abstract TrialPlate findTrialPlate(long id) throws BusinessException;

    /**
     * Find a TrialPlate.
     * 
     * @param barcode
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public abstract TrialPlate findTrialPlate(String barcode) throws BusinessException;

    /**
     * Finds all trial plates.
     * 
     * @param paging
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public abstract Collection<TrialPlate> findAllTrialPlates(BusinessCriteria criteria)
        throws BusinessException;

    /**
     * Find a TrialPlate with only part of the barcode
     * 
     * @param barcode
     * @param paging
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public abstract Collection<TrialPlate> findTrialPlateByPartialBarcode(String barcode,
        BusinessCriteria criteria) throws BusinessException;

    /**
     * Save a TrialPlate
     * 
     * @param trialPlate
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public abstract TrialPlate saveTrialPlate(TrialPlate trialPlate) throws BusinessException;

    /**
     * Update a TrialPlate
     * 
     * @param trialPlate
     * @throws org.pimslims.business.exception.BusinessException
     */
    public abstract void updateTrialPlate(TrialPlate trialPlate) throws BusinessException;

    /* **************************************************************
     * TrialDrop-related
     * ************************************************************** */
    /**
	 * <p>
	 * Find the set of TrialDrop that share the specified Sample.
	 * </p>
	 * 
	 * @param sample
	 * @return
	 * @throws org.pimslims.business.exception.BusinessException
	 * @Deprecated // not used, may be slow public abstract
	 *             Collection<TrialDrop> findTrialDrops(Sample sample,
	 *             BusinessCriteria criteria) throws BusinessException;
	 */

    /**
	 * <p>
	 * Find the set of TrialDrop that share the specified TrialPlate.
	 * </p>
	 * 
	 * @param plate
	 * @return
	 * @throws org.pimslims.business.exception.BusinessException
	 * @Deprecated // not used, may be slow public abstract
	 *             Collection<TrialDrop> findTrialDrops(TrialPlate plate,
	 *             BusinessCriteria criteria) throws BusinessException;
	 */

    /**
     * <p>
     * Find the set of TrialDrop that share the specified Sample and TrialPlate.
     * </p>
     * 
     * @param sample
     * @param plate
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    @Deprecated
    // not used, may be slow
    public abstract Collection<TrialDrop> findTrialDrops(Sample sample, TrialPlate plate,
        BusinessCriteria criteria) throws BusinessException;

    /**
	 * <p>
	 * Find the set of TrialDrop that share the specified Sample and use any of
	 * the specified Conditions.
	 * </p>
	 * 
	 * @param sample
	 * @param conditions
	 * @return
	 * @throws org.pimslims.business.exception.BusinessException
	 * @Deprecated // not used, may be slow public abstract
	 *             Collection<TrialDrop> findTrialDrops(Sample sample,
	 *             Collection<Condition> conditions, BusinessCriteria criteria)
	 *             throws BusinessException;
	 */

    /**
     * To find a Trial Drop at a particular position on a particular plate...
     * 
     * @param barcode
     * @param wellPosition
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public abstract TrialDrop findTrialDrop(String barcode, WellPosition wellPosition)
        throws BusinessException;

    /**
     * To find a trial drop given a plate...
     * 
     * @param plate
     * @param wellPosition
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public abstract TrialDrop findTrialDrop(TrialPlate plate, WellPosition wellPosition)
        throws BusinessException;

    /**
     * <p>
     * Save the specified TrialDrop.
     * </p>
     * 
     * @param trialDrop
     * @return
     * @throws org.pimslims.business.exception.BusinessException This method is only used in tests
     */
    public abstract TrialDrop saveTrialDrop(TrialDrop trialDrop) throws BusinessException;

    /**
     * <p>
     * Update the specified TrialDrop.
     * </p>
     * 
     * @param trialDrop
     * @throws org.pimslims.business.exception.BusinessException
     */
    public abstract void updateTrialDrop(TrialDrop trialDrop) throws BusinessException;

    /* **************************************************************
     * TrialPlate imager interration related
     * ************************************************************** */
    /**
     * a User request to have extra imaging session on a plate.
     * 
     * @param barcode the barcode of the plate
     * @param date the date by which the user would like their imaging to happen
     * @throws org.pimslims.business.exception.BusinessException if an error
     */
    public abstract void requestAdditionalImaging(String barcode, Calendar date) throws BusinessException;

    /**
     * Get a list of all the additional imaging requests received.
     * 
     * @return the list of barcodes and times requested
     * @throws org.pimslims.business.exception.BusinessException
     */
    public abstract Map<String, Calendar> findAdditionalImagingRequests() throws BusinessException;

    /**
     * remove the additional imaging request from the queue - in reality it will probably be useful to keep
     * all requests, just marking them complete once they are done (thus allowing us to note if the system is
     * being abused
     * 
     * @param barcode the barcode of the plate imaged
     * @throws org.pimslims.business.exception.BusinessException on an error
     */
    public abstract void removeAdditionalImaging(String barcode) throws BusinessException;

    /**
     * A user may request to retrieve a plate from the storage device in order to, for example, take it on a
     * synchrotron trip, or look at it under a microscope, etc.
     * 
     * @param barcode the barcode of the plate
     * @param dueDate the date by which the plate has to be taken out.
     * @throws org.pimslims.business.exception.BusinessException
     */
    public abstract void requestPlateRetrieval(String barcode, Calendar dueDate) throws BusinessException;

    /**
     * Get a list of the plates that have been requested to be taken out of the store.
     * 
     * @return the list of plates and a time by which they should be taken out
     * @throws org.pimslims.business.exception.BusinessException on an error
     */
    public abstract Map<String, Calendar> findPlateRetrievals() throws BusinessException;

    /**
     * remove the plate from the list of plates that have been requested to be withdrawn from the storage
     * unit. In reality it will probably be useful to keep all requests, just marking them complete once they
     * are done (thus allowing us to note if the system is being abused.
     * 
     * @param barcode
     * @throws org.pimslims.business.exception.BusinessException
     */
    public abstract void removePlateRetrievalRequest(String barcode) throws BusinessException;

    /**
     * save the PlateType into DB
     * 
     * @param plateType
     * @return
     */
    public abstract PlateType create(PlateType plateType);

    /**
	 * Not sure if this is the best way of doing this, but I need to have access
	 * to the current url stub for images of different types...
	 * 
	 * @Deprecated // value never used public abstract void
	 *             setZoomedImageURLStub(String urlStub);
	 * @Deprecated // value never used public abstract void
	 *             setCompositeImageURLStub(String urlStub);
	 * @Deprecated // value never used public abstract void
	 *             setSliceImageURLStub(String urlStub);
	 */
    /**
     * TrialService.create
     * 
     * @param plateType
     * @param defaultSchedulePlan
     * @return
     */
    public abstract PlateType create(PlateType plateType, SchedulePlan defaultSchedulePlan);
}
