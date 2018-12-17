package org.pimslims.crystallization.implementation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.pimslims.logging.Logger;
import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Location;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.ImageType;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.crystallization.view.InspectionView;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.PlateInspectionDAO;
import org.pimslims.crystallization.dao.view.InspectionViewDAO;
import org.pimslims.crystallization.dao.view.ViewDAO;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.schedule.ScheduledTask;

public class PlateInspectionServiceImpl extends BaseServiceImpl implements PlateInspectionService {
    private static final Logger log = Logger.getLogger(PlateInspectionServiceImpl.class);

    /**
     * Name of experiment type for plate inspections
     */
    private static final String EXPERIMENT_TYPE = "Crystallization plate inspection";

    /**
     * Name of experiment parameter that records the inspection number
     */
    public static final String INSPECTION_NUMBER = "Inspection Number";

    private final PlateInspectionDAO inspectionDAO;

    public PlateInspectionServiceImpl(final DataStorage impl) {
        super(impl);
        inspectionDAO = new PlateInspectionDAO(this.version);
    }

    /**
     * @param plateInspection
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(final PlateInspection plateInspection) throws BusinessException {

        final org.pimslims.dao.FlushMode oldFlushModel = ((WritableVersionImpl) version).getFlushMode();
        // improve performance
        ((WritableVersionImpl) version).setFlushMode(org.pimslims.dao.FlushMode.batchMode());

        inspectionDAO.createPO(plateInspection);

        ((WritableVersionImpl) version).setFlushMode(oldFlushModel);

    }

    public PlateInspection find(final long id) throws BusinessException {
        final ScheduledTask pobject = this.version.get(id);
        return inspectionDAO.getFullXO(pobject);

    }

    public PlateInspection find(final String id) throws BusinessException {
        final ScheduledTask pobject = this.version.get(id);
        return inspectionDAO.getFullXO(pobject);
    }

    /**
     * @TODO REQUIRED
     * @param image
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public PlateInspection findByImage(final Image image) throws BusinessException {
        // LATER implement
        throw new UnsupportedOperationException("This method is not implemented");
    }

    /**
     * TODO this will probably need more work when the ImageService is implemented
     * 
     * @see org.pimslims.crystallization.business.PlateInspectionService#findByInspectionName(java.lang.String)
     */
    public PlateInspection findByInspectionName(final String name) throws BusinessException {
        final ScheduledTask pobject =
            this.version.findFirst(ScheduledTask.class, ScheduledTask.PROP_NAME, name);
        return inspectionDAO.getFullXO(pobject);
    }

    public List<PlateInspection> findByLocation(final Location location, final Calendar date,
        final BusinessCriteria criteria) throws BusinessException {
        // LATER implement
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public List<PlateInspection> findByLocation(final Location location, final Calendar startDate,
        final Calendar endDate, final BusinessCriteria criteria) throws BusinessException {
        // LATER implement
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public List<PlateInspection> findByPlate(final String barcode) throws BusinessException {
        final Holder holder = this.version.findFirst(Holder.class, Holder.PROP_NAME, barcode);
        if (null == holder) {
            return Collections.EMPTY_LIST;
        }
        final TrialPlate plate = TrialServiceImpl.getPlate(holder);

        final List<PlateInspection> ret = new ArrayList<PlateInspection>();

        for (final ScheduledTask st : holder.getScheduledTasks()) {
            if (st.getCompletionTime() != null) {
                final PlateInspection bean = inspectionDAO.getFullXO(st);
                //TODO set images
                bean.setPlate(plate);
                ret.add(bean);
            }
        }
        Collections.sort(ret);
        return ret;
    }

    public List<PlateInspection> findByPlate(final TrialPlate plate) throws BusinessException {
        return findByPlate(plate.getBarcode());
    }

    public List<PlateInspection> findByPlate(final PlateExperimentView plateExperiment)
        throws BusinessException {
        return findByPlate(plateExperiment.getBarcode());
    }

    public PlateInspection findByPlate(final String barcode, final int inspectionNumber)
        throws BusinessException {
        // TODO implement
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public PlateInspection findByPlate(final TrialPlate plate, final int inspectionNumber)
        throws BusinessException {
        return findByPlate(plate.getBarcode(), inspectionNumber);
    }

    public PlateInspection findLatestByPlate(final String barcode) throws BusinessException {
        final List<PlateInspection> inspections = this.findByPlate(barcode);
        if (inspections == null || inspections.isEmpty()) {
            return null;
        }
        final PlateInspection latest = Collections.max(inspections, LATEST);
        return latest;
    }

    public PlateInspection findLatestByPlate(final TrialPlate plate) throws BusinessException {
        return this.findLatestByPlate(plate.getBarcode());
    }

    public PlateInspection findLatestByPlate(final PlateExperimentView plateExperiment)
        throws BusinessException {
        return this.findLatestByPlate(plateExperiment.getBarcode());
    }

    public List<Image> getImages(final PlateInspection plateInspection, final ImageType imageType,
        final BusinessCriteria criteria) throws BusinessException {
        // TODO implement
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public Location getLocation(final PlateInspection plateInspection) throws BusinessException {
        // LATER implement
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public List<PlateExperimentView> getPlateExperiment(final PlateInspection plateInspection,
        final BusinessCriteria criteria) throws BusinessException {
        // TODO implement
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public void update(final PlateInspection plateInspection) throws BusinessException {
        throw new UnsupportedOperationException("this method has not been implemented");
    }

    /**
     * Comparator to find the latest in a set of inspections
     */
    private static final Comparator<PlateInspection> LATEST = new Comparator<PlateInspection>() {

        public int compare(final PlateInspection arg0, final PlateInspection arg1) {
            final PlateInspection ins0 = arg0;
            final PlateInspection ins1 = arg1;
            final long time0 = ins0.getInspectionDate().getTimeInMillis();
            final long time1 = ins1.getInspectionDate().getTimeInMillis();
            return time0 < time1 ? -1 : time0 == time1 ? 0 : 1;
        }
    };

    public Collection<InspectionView> findViews(final BusinessCriteria criteria) throws BusinessException {
        return getViewDAO().findViews(criteria);

    }

    public Integer findViewCount(final BusinessCriteria criteria) throws BusinessException {
        return getViewDAO().findViewCount(criteria);
    }

    public String convertPropertyName(final String propertyName) throws BusinessException {
        return getViewDAO().convertPropertyName(propertyName);
    }

    public Collection<PlateInspection> findAll(final BusinessCriteria criteria) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<InspectionView> findLatest(final BusinessCriteria criteria) throws BusinessException {
        return ((InspectionViewDAO) getViewDAO()).findLatest(criteria);
    }

    private ViewDAO<InspectionView> viewDAO;

    private ViewDAO<InspectionView> getViewDAO() {
        if (viewDAO == null) {
            viewDAO = new InspectionViewDAO(version);
        }
        return viewDAO;
    }
}
