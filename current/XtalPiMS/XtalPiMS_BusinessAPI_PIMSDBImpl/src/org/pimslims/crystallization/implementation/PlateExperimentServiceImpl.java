/**
 * 
 */
package org.pimslims.crystallization.implementation;

import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.service.PlateExperimentService;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.view.PlateExperimentViewDAO;
import org.pimslims.crystallization.dao.view.ViewDAO;
import org.pimslims.logging.Logger;
import org.pimslims.metamodel.AbstractModelObject;

/**
 * @author cm65
 * 
 */
public class PlateExperimentServiceImpl extends BaseServiceImpl implements PlateExperimentService {
    private static final Logger log = Logger.getLogger(PlateExperimentServiceImpl.class);

    /**
     * The name of the PiMS ExperimentType for crystallization
     */
    public static final String EXPERIMENT_TYPE = "Crystallogenesis";

    /**
     * Input sample name used for the crystallization condition
     */
    //public static final String CONDITION = "Condition";

    //  private final ReadableVersion version;
    //  private final DataStorage dataStorage;
    //  private final XtalSessionImpl session;

    /**
     * @param dataStorage
     * 
     */
    public PlateExperimentServiceImpl(final DataStorage dataStorage) {
        super(dataStorage);
    }

    /**
     * 
     * @param id
     * @throws org.pimslims.business.exception.BusinessException
     * @return
     */
    public PlateExperimentView find(final long id) throws BusinessException {
        final AbstractModelObject holder = this.version.get(id);
        return findByPlateBarcode(holder.getName()).iterator().next();
    }

    /**
     * 
     * @param id
     * @throws org.pimslims.business.exception.BusinessException
     * @return
     */
    public PlateExperimentView find(final String id) throws BusinessException {
        final AbstractModelObject holder = this.version.get(id);
        return findByPlateBarcode(holder.getName()).iterator().next();

    }

    /**
     * 
     * @param plate
     * @throws org.pimslims.business.exception.BusinessException
     * @return
     */
    public Collection<PlateExperimentView> findByPlate(final TrialPlate plate) throws BusinessException {
        return findByPlateBarcode(plate.getBarcode());
    }

    /**
     * 
     * @param barcode
     * @throws org.pimslims.business.exception.BusinessException
     * @return used in xtalPiMS_WS so get under test
     */
    @SuppressWarnings("unchecked")
    public Collection<PlateExperimentView> findByPlateBarcode(final String barcode) throws BusinessException {
        final BusinessCriteria criteria = new BusinessCriteria(this);
        criteria.add(BusinessExpression.Equals(PlateExperimentView.PROP_BARCODE, barcode, true));
        return findViews(criteria);
    }

    /**
     * Search plate by plateView's properties
     * 
     * @see org.pimslims.business.crystallization.service.PlateExperimentService#findPlateViews(org.pimslims.business.criteria.BusinessCriteria)
     */
    public Collection<PlateExperimentView> findViews(final BusinessCriteria criteria)
        throws BusinessException {
        return getViewDAO().findViews(criteria);
    }

    public Integer findViewCount(final BusinessCriteria criteria) throws BusinessException {
        return getViewDAO().findViewCount(criteria);
    }

    public String convertPropertyName(final String propertyName) throws BusinessException {
        return getViewDAO().convertPropertyName(propertyName);

    }

    private ViewDAO<PlateExperimentView> viewDAO;

    private ViewDAO<PlateExperimentView> getViewDAO() {
        if (viewDAO == null) {
            viewDAO = new PlateExperimentViewDAO(version);
        }
        return viewDAO;
    }
}
