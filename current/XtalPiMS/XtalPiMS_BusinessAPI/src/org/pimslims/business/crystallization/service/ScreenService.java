/*
 * ScreenService.java Created on 17 April 2007, 09:10 To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pimslims.business.crystallization.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.pimslims.business.BaseService;
import org.pimslims.business.ViewService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Component;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.view.ScreenView;
import org.pimslims.business.exception.BusinessException;

/**
 * <p>
 * The ScreenService API is the interface through which XtalPiMS Screens are managed. The interface declares
 * the methods available to record and access Screens.
 * </p>
 * 
 * @author Ian Berry
 */
public interface ScreenService extends BaseService, ViewService<ScreenView> {

    /**
     * <p>
     * Find an {@link org.pimslims.crystallization.Screen} by numeric id.
     * </p>
     * 
     * @param id - the id of the Screen to find
     * @return The Screen with the specified id
     * @throws org.pimslims.business.exception.BusinessException if an error occurs
     * 
     * @see #find(String)
     * @see org.pimslims.crystallization.Screen
     */
    public Screen find(long id) throws BusinessException;

    /**
     * <p>
     * Finds all the screens that are available to view
     * </p>
     * 
     * @param criteria
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Screen> findAll(BusinessCriteria criteria) throws BusinessException;

    public Integer findAllCount() throws BusinessException;

    /**
     * <p>
     * Find the {@link org.pimslims.crystallization.Condition} in the specified
     * {@link org.pimslims.crystallization.WellPosition} of the specified
     * {@link org.pimslims.crystallization.Plate}.
     * </p>
     * 
     * @param plate - the Plate to use
     * @param wellPosition - the WellPosition (Row, Column, SubPosition) to use
     * @return The Condition in plate's wellPosition
     * @throws org.pimslims.business.exception.BusinessException if an error occurs
     * 
     * @see #findByPlateAndWell(String, WellPosition)
     */
    public Condition findByPlateAndWell(TrialPlate plate, WellPosition wellPosition) throws BusinessException;

    /**
     * <p>
     * Find the {@link org.pimslims.crystallization.Condition} in the specified
     * {@link org.pimslims.crystallization.WellPosition} of {@link org.pimslims.crystallization.Plate} with
     * the specified barcode.
     * </p>
     * 
     * @param barcode - the barcode of the Plate to use
     * @param wellPosition - the WellPosition (Row, Column, SubPosition) to use
     * @return The Condition in plate's wellPosition
     * @throws org.pimslims.business.exception.BusinessException if an error occurs
     * 
     * @see #findByPlateAndWell(Plate, WellPosition)
     */
    public Condition findByPlateAndWell(String barcode, WellPosition wellPosition) throws BusinessException;

    /**
     * <p>
     * Find the Screen used in the specified Plate.
     * </p>
     * 
     * @param plate - the Plate whose underlying Screen is to be found
     * @return The Screen used in plate
     * @throws org.pimslims.business.exception.BusinessException if an error occurs
     * 
     * @see #findByPlate(String)
     */
    public Screen findByPlate(TrialPlate plate) throws BusinessException;

    /**
     * <p>
     * Find the Screen used in the Plate with the specified barcode.
     * </p>
     * 
     * @param barcode - the barcode of the Plate whose underlying Screen is to be found
     * @return The Screen used in the Plate with the specified barcode
     * @throws org.pimslims.business.exception.BusinessException if an error occurs
     * 
     * @see #findByPlate(Plate)
     */
    public Screen findByPlate(String barcode) throws BusinessException;

    /**
     * <p>
     * Find the Screen with the specified name.
     * </p>
     * 
     * @param name - the name of the Screen to find
     * @return The Screen with the specified name
     * @throws org.pimslims.business.exception.BusinessException if an error occurs
     */
    public Screen findByName(String name) throws BusinessException;

    /**
     * <p>
     * Find all the Screen for the manufacturer with the specified name.
     * </p>
     * 
     * @param manufacturer - the name of the manufacturer whose Screen are to be found
     * @param paging
     * @return The List of Screen for the specified manufacturer
     * @throws org.pimslims.business.exception.BusinessException if an error occurs
     */
    public Collection<Screen> findByManufacturer(String manufacturer, BusinessCriteria criteria)
        throws BusinessException;

    /**
     * <p>
     * Find all the screens that use this condition
     * </p>
     * 
     * @param condition the condition to search for
     * @param paging if the table is to be paged on the server
     * @return a list of screens that use that condition
     * @throws org.pimslims.business.exception.BusinessException if an error occurs
     */
    public Collection<Screen> findByCondition(Condition condition, BusinessCriteria criteria)
        throws BusinessException;

    /**
     * <p>
     * Finds a list of names of all the manufacturers that are used in the screens
     * </p>
     * 
     * @return the list of manufacturer names
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<String> findManufacturerNames() throws BusinessException;

    /**
     * <p>
     * Find a list of components that use the chemical (in whatever form, salt, buffer, etc.)
     * </p>
     * 
     * @param chemicalName the name of the chemical
     * @param paging if server-side paging is required
     * @return the list of components that use this chemical
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Component> findByChemicalName(String chemicalName, BusinessCriteria criteria)
        throws BusinessException;

    /**
     * <p>
     * Finds all components that use similar chemicals, by similar, if the name is Calcium Chloride, it would
     * return all the matches for both calcium and chloride
     * </p>
     * 
     * @param chemicalName
     * @param paging
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Component> findBySimilarChemicalName(String chemicalName, BusinessCriteria criteria)
        throws BusinessException;

    /**
	 * <p>
	 * Get the List of Condition for the specified Screen.
	 * </p>
	 * 
	 * @param screen
	 *            - the Screen whose Condition are to be returned
	 * @param paging
	 * @return The List of Condition in the specified Screen
	 * @throws org.pimslims.business.exception.BusinessException
	 *             if an error occurs
	 * 
	 * @see #getConditions(String)
	 * @see #getConditionsForManufacturerScreen(String)
	 * @deprecated use the one from ConditionService
	 * @Deprecated public Collection<Condition> getConditions(Screen screen,
	 *             BusinessCriteria criteria) throws BusinessException;
	 */

    /**
	 * <p>
	 * Get the List of Condition for the Screen with the specified name.
	 * </p>
	 * 
	 * @param screenName
	 *            - the name of the Screen whose Condition are to be returned
	 * @param paging
	 * @return The List of Condition in the Screen with the specified name
	 * @throws org.pimslims.business.exception.BusinessException
	 *             if an error occurs
	 * 
	 * @see #getConditions(Screen)
	 * @see #getConditionsForManufacturerScreen(String)
	 * @deprecated use the one from ConditionService
	 * @Deprecated public Collection<Condition> getConditions(String screenName,
	 *             BusinessCriteria criteria) throws BusinessException;
	 */

    /**
	 * <p>
	 * Get the List of Condition for Screens with the specified manufacturer.
	 * </p>
	 * 
	 * @param manufacturer
	 *            - the name of the manufacturer who Screen Condition are to be
	 *            returned
	 * @param paging
	 * @return The List of Condition in all the Screen with the specified
	 *         manufacturer
	 * @throws org.pimslims.business.exception.BusinessException
	 *             if an error occurs
	 * 
	 * @see #getConditions(Screen)
	 * @see #getConditions(String)
	 * @deprecated use the one from ConditionService
	 * @Deprecated public Collection<Condition>
	 *             getConditionsForManufacturerScreen(String manufacturer,
	 *             BusinessCriteria criteria) throws BusinessException;
	 */

    /**
     * Add a new Screen to the database.
     * 
     * @param screen The new Screen
     * @throws org.pimslims.business.exception.BusinessException if an error occurs
     * 
     * @see #create(String, List)
     */
    public void create(Screen screen) throws BusinessException;

    /**
     * <p>
     * Add a new Screen to the data store.
     * </p>
     * 
     * @param screenName The name of the new Screen
     * @param conditionPositions The List of Condition that make up the new Screen
     * @return The newly created Screen
     * @throws org.pimslims.business.exception.BusinessException if an error occurs
     * 
     * @see #create(Screen)
     */
    public Screen create(String screenName, Map<WellPosition, Condition> conditionPositions)
        throws BusinessException;

    /**
     * <p>
     * Persist changes to an existing Screen.
     * </p>
     * 
     * @param screen - the Screen to update
     * @throws org.pimslims.business.exception.BusinessException if an error occurs
     * 
     */
    public void update(Screen screen) throws BusinessException;

    /**
     * <p>
     * Close the specified Screen. What does this mean?
     * </p>
     * 
     * @param screen - the screen to close
     * @return The closed screen
     * @throws org.pimslims.business.exception.BusinessException if an error occurs
     */
    public void close(Screen screen) throws BusinessException;

    /**
     * This function provides a simple String list of all the types of Screen that are available. A simple
     * list might be: Sparse Matrix Screen, Optimisation, Additive, etc.
     * 
     * @return
     * @throws BusinessException
     */
    public Collection<String> findScreenTypes() throws BusinessException;
}
