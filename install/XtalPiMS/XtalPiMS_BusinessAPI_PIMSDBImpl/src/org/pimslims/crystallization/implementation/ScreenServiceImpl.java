/*
 * ScreenServiceImpl.java
 */
package org.pimslims.crystallization.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pimslims.logging.Logger;
import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Component;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.DeepWellPlate;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.ScreenService;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.crystallization.view.ScreenView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.DeepWellPlateDAO;
import org.pimslims.crystallization.dao.ScreenDAO;
import org.pimslims.crystallization.dao.view.ScreenTypeViewDAO;
import org.pimslims.crystallization.dao.view.ScreenViewDAO;
import org.pimslims.crystallization.dao.view.ViewDAO;
import org.pimslims.crystallization.implementation.view.ScreenTypeView;
import org.pimslims.model.holder.RefHolder;
import org.pimslims.model.holder.RefHolderSource;
import org.pimslims.search.Conditions;
import org.pimslims.search.Paging;

/**
 * <p>
 * Implementation of {@link org.pimslims.business.crystallization.service.ScreenService}.
 * </p>
 * 
 * @author Ian Berry, Jon Diprose, Bill Lin
 * @see org.pimslims.business.crystallization.service.ScreenService
 */
public class ScreenServiceImpl extends BaseServiceImpl implements ScreenService {
    final private ScreenDAO screenDAO;

    /**
     * <p>
     * Logger for this class.
     * </p>
     */
    private static final Logger log = Logger.getLogger(ScreenServiceImpl.class);

    /**
     * <p>
     * Construct a ScreenServiceImpl for the specified DataStorage.
     * </p>
     * 
     * @param dataStorage - the DataStorage that this ScreenServiceImpl wraps
     */
    public ScreenServiceImpl(final DataStorage dataStorage) {

        super(dataStorage);
        screenDAO = new ScreenDAO(version);
    }

    /**
     * <p>
     * Find the Screen represented by a RefHolder with the specified id in the underlying DataStorage.
     * </p>
     * 
     * <p>
     * TODO Screen does not have an id. Does this method make sense?
     * </p>
     * 
     * @param id - the dbid of the underlying RefHolder that represents the Screen
     * @throws org.pimslims.business.exception.BusinessException if something goes wrong
     * @return The Screen with the specified id
     * @see org.pimslims.business.crystallization.service.ScreenService#find(long)
     */
    public Screen find(final long id) throws BusinessException {

        log.debug("In ScreenServiceImpl.find(long)");
        RefHolder refHolder = (RefHolder) version.get(id);
        return screenDAO.getScreen(refHolder);

    }

    /**
     * <p>
     * Find the Screen represented by a RefHolder with the specified id in the underlying DataStorage. The id
     * will first be checked to see if it is an appropriate hook before it is converted to a long and passed
     * to {@link ScreenServiceImpl#find(long)}
     * </p>
     * 
     * <p>
     * TODO Screen does not have an id. Does this method make sense?
     * </p>
     * 
     * @param id - the hook or dbid of the underlying RefHolder that represents the Screen
     * @throws org.pimslims.business.exception.BusinessException if something goes wrong
     * @return The Screen with the specified id
     * @see ScreenServiceImpl#find(long)
     * @see org.pimslims.business.crystallization.service.ScreenService#find(String)
     */
    public Screen find(final String id) throws BusinessException {

        log.debug("In ScreenServiceImpl.find(String)");

        try {

            if (id.startsWith("org.pimslims.model.holder.RefHolder:")) {
                return find(Long.parseLong(id.substring(35)));
            } else {
                return find(Long.parseLong(id));
            }

        } catch (final NumberFormatException e) {

            throw new BusinessException("id must be hook or long", e);

        }

    }

    /**
     * <p>
     * Find the Screen represented by a RefHolder with the specified name in the underlying DataStorage.
     * </p>
     * 
     * @param name - the name of the underlying RefHolder that represents the Screen
     * @throws org.pimslims.business.exception.BusinessException if something goes wrong
     * @return The Screen with the specified name
     * @see org.pimslims.business.crystallization.service.ScreenService#findByName(String)
     */
    public Screen findByName(final String name) throws BusinessException {

        log.debug("In ScreenServiceImpl.findByName(String)");
        return screenDAO.getFullXO(version.findFirst(RefHolder.class, RefHolder.PROP_NAME, name));

    }

    /**
     * 
     * @param plate
     * @throws org.pimslims.business.exception.BusinessException
     * @return
     */
    public Screen findByPlate(final TrialPlate plate) throws BusinessException {
        return findByPlate(plate.getBarcode());
    }

    /**
     * 
     * @param plateExperiment
     * @throws org.pimslims.business.exception.BusinessException
     * @return
     */
    public Screen findByPlate(final PlateExperimentView plateExperiment) throws BusinessException {
        return findByPlate(plateExperiment.getBarcode());
    }

    /**
     * 
     * @param barcode
     * @throws org.pimslims.business.exception.BusinessException
     * @return
     */
    public Screen findByPlate(final String barcode) throws BusinessException {
        // TODO Implement
        throw new UnsupportedOperationException("This method is not implemented");
    }

    /**
     * REQUIRED
     * 
     * @param paging
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Screen> findAll(final BusinessCriteria criteria) throws BusinessException {
        final Paging pPaging = new Paging(criteria.getFirstResult(), criteria.getMaxResults());
        final Collection<RefHolder> refHolders = screenDAO.findAll(pPaging);
        final Collection<Screen> screens = new LinkedList<Screen>();
        for (final RefHolder refHolder : refHolders) {
            final Screen screen = screenDAO.getFullXO(refHolder);
            if (screen != null) {
                screens.add(screen);
            }
        }
        return screens;
    }

    /**
     * REQUIRED
     * 
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Integer findAllCount() throws BusinessException {
        return screenDAO.getCount();
    }

    /**
     * 
     * @param manufacturer
     * @throws org.pimslims.business.exception.BusinessException
     * @return
     */
    public List<Screen> findByManufacturer(final String manufacturer, final BusinessCriteria criteria)
        throws BusinessException {
        // LATER implement
        throw new UnsupportedOperationException("This method is not implemented");
    }

    /**
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<String> findManufacturerNames() throws BusinessException {
        //find RefSampleSource linked with refholder/screen
        final Collection<RefHolderSource> rss =
            version.findAll(RefHolderSource.class, RefHolderSource.PROP_REFHOLDER, Conditions.notNull());
        final Collection<String> manufacturerNames = new HashSet<String>();
        for (final RefHolderSource rs : rss) {
            if (rs.getSupplier() != null) {
                manufacturerNames.add(rs.getSupplier().getName());
            }
        }
        return manufacturerNames;
    }

    /**
     * @TODO REQUIRED
     * @param condition
     * @param paging
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Screen> findByCondition(final Condition condition, final BusinessCriteria criteria)
        throws BusinessException {
        // TODO Implement
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * <p>
     * Store the specified Screen
     * </p>
     * 
     * @param screen
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(final Screen screen) throws BusinessException {

        log.debug("In ScreenServiceImpl.create(Screen): " + screen.getName());
        screenDAO.createPO(screen);

    }

    /**
     * 
     * @param screenName
     * @param conditionPositions
     * @throws org.pimslims.business.exception.BusinessException
     * @return
     */
    public Screen create(final String screenName, final Map<WellPosition, Condition> conditionPositions)
        throws BusinessException {

        log.debug("In ScreenServiceImpl.create(String, Map<WellPosition, Condition>): " + screenName);

        final Screen screen = new Screen();
        screen.setName(screenName);
        screen.setConditionPositions(conditionPositions);

        create(screen);

        return screen;

    }

    /**
     * @TODO REQUIRED
     * @param screen
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void update(final Screen screen) throws BusinessException {
        // TODO Implement
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * 
     * @param screen
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void close(final Screen screen) throws BusinessException {
        // TODO The purpose of this method is undefined
        // A no-op seems appropriate
    }

    // *****************************************************************************
    // *****************************************************************************
    //
    // Start of find Condition by plate
    //
    // *****************************************************************************
    // *****************************************************************************

    /**
     * 
     * @param plate
     * @param wellPosition
     * @throws org.pimslims.business.exception.BusinessException
     * @return
     */

    public Condition findByPlateAndWell(final TrialPlate plate, final WellPosition wellPosition)
        throws BusinessException {
        return this.findByPlateAndWell(plate.getBarcode(), wellPosition);
    }

    /**
     * 
     * @param barcode
     * @param wellPosition
     * @throws org.pimslims.business.exception.BusinessException
     * @return
     */
    public Condition findByPlateAndWell(final String barcode, final WellPosition wellPosition)
        throws BusinessException {
        // TODO Implement
        throw new UnsupportedOperationException("This method is not implemented");
    }

    /**
     * 
     * @param plateExperiment
     * @param wellPosition
     * @throws org.pimslims.business.exception.BusinessException
     * @return
     */

    public Condition findByPlateAndWell(final PlateExperimentView plateExperiment,
        final WellPosition wellPosition) throws BusinessException {
        // TODO Implement
        throw new UnsupportedOperationException("This method is not implemented");
        //return this.findByPlateAndWell(plateExperiment.getPlate(), wellPosition);
    }

    // *****************************************************************************
    // *****************************************************************************
    //
    // End of find Condition by plate
    //
    // *****************************************************************************
    // *****************************************************************************

    /**
     * @TODO REQUIRED
     * 
     * @param chemicalName
     * @param paging
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Component> findByChemicalName(final String chemicalName, final BusinessCriteria criteria)
        throws BusinessException {
        // TODO Implement
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @TODO REQUIRED
     * 
     * @param chemicalName
     * @param paging
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Component> findBySimilarChemicalName(final String chemicalName,
        final BusinessCriteria criteria) throws BusinessException {
        // TODO Implement
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<ScreenView> findViews(final BusinessCriteria criteria) throws BusinessException {
        return getViewDAO().findViews(criteria);

    }

    public Integer findViewCount(final BusinessCriteria criteria) throws BusinessException {
        return getViewDAO().findViewCount(criteria);
    }

    public String convertPropertyName(final String property) throws BusinessException {
        return getViewDAO().convertPropertyName(property);
    }

    public Collection<Condition> getConditions(final Screen screen, final BusinessCriteria criteria)
        throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Condition> getConditions(final String screenName, final BusinessCriteria criteria)
        throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Condition> getConditionsForManufacturerScreen(final String manufacturer,
        final BusinessCriteria criteria) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<String> findScreenTypes() throws BusinessException {
        final Collection<ScreenTypeView> typeViews = getTypeViewDAO().findViews(null);
        final List<String> types = new ArrayList<String>();
        for (final ScreenTypeView typeview : typeViews) {
            types.add(typeview.getName());
        }
        return types;
    }

    private ViewDAO<ScreenView> viewDAO;

    private ViewDAO<ScreenView> getViewDAO() {
        if (viewDAO == null) {
            viewDAO = new ScreenViewDAO(version);
        }
        return viewDAO;
    }

    private ViewDAO<ScreenTypeView> typeViewDAO;

    private ViewDAO<ScreenTypeView> getTypeViewDAO() {
        if (typeViewDAO == null) {
            typeViewDAO = new ScreenTypeViewDAO(version);
        }
        return typeViewDAO;
    }

    /**
     * <p>
     * Create a new DeepWellPlate with the specified barcode as an instance of the specified screen.
     * </p>
     * 
     * <p>
     * For the purposes of this method, screen.name must be populated and a Screen with this name must already
     * have been mapped into the database.
     * </p>
     * 
     * <p>
     * DeepWellPlate is mapped to Holder.
     * </p>
     * 
     * <p>
     * TODO This method currently records no information in Holder other than the barcode. It should at the
     * very least be extended to allow a description to be stored.
     * </p>
     * 
     * <p>
     * TODO This method should be extended to also record any necessary information concerning how the
     * DeepWellPlate was created. This will probably require the creation of various experiment records.
     * </p>
     * 
     * @param screen - the Screen from which this DeepWellPlate should be instantiated
     * @param barcode - the barcode of the new DeepWellPlate
     * @throws org.pimslims.business.exception.BusinessException
     * @return The newly-created DeepWellPlate
     * 
     * @see org.pimslims.crystallization.business.ScreenService#createDeepWellPlate(org.pimslims.crystallization.Screen,
     *      org.pimslims.crystallization.Barcode)
     */
    public DeepWellPlate createDeepWellPlate(Screen screen, String barcode) throws BusinessException {

        DeepWellPlate dwp = new DeepWellPlate(barcode, screen);
        DeepWellPlateDAO deepWellPlateDAO = new DeepWellPlateDAO();
        deepWellPlateDAO.create(dwp, getWritableVersion());

        return dwp;

    }

}
