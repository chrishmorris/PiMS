/**
 * 
 */
package org.pimslims.crystallization.implementation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.ConditionQuantity;
import org.pimslims.business.crystallization.model.DeepWellPlate;
import org.pimslims.business.crystallization.model.PlateExperimentInfo;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.SampleQuantity;
import org.pimslims.business.crystallization.model.SampleVolumeMap;
import org.pimslims.business.crystallization.model.SchedulePlan;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.VolumeMap;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.TrialDropView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.ConditionDAO;
import org.pimslims.crystallization.dao.PersonDAO;
import org.pimslims.crystallization.dao.SampleDAO;
import org.pimslims.crystallization.dao.TrialPlateDAO;
import org.pimslims.crystallization.dao.view.TrialDropViewDAO;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.logging.Logger;
import org.pimslims.metamodel.AbstractModelObject;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.holder.RefHolder;
import org.pimslims.model.holder.RefHolderOffset;
import org.pimslims.model.reference.CrystalType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.RefSample;
import org.pimslims.search.Searcher;

/**
 * @author Jon Diprose
 */
public class TrialServiceImpl extends BaseServiceImpl implements TrialService {

    public static final String MOTHER_LIQUOR = "Mother liquor";

    public static final String ADDITIVE_SCREEN = "Additive Screen?";

    private static final Logger log = Logger.getLogger(TrialServiceImpl.class);

    private TrialDropServiceImpl trialDropSerice = null;

    private final TrialDropViewDAO trialDropViewDAO;

    /**
     * 
     * @param dataStorage
     */
    public TrialServiceImpl(final DataStorage dataStorage) {
        super(dataStorage);
        trialDropSerice = new TrialDropServiceImpl(dataStorage);
        trialDropViewDAO = new TrialDropViewDAO(this.version);
    }

    /**
     * @TODO REQUIRED
     * @param barcode
     * @param plateType
     * @return
     * @throws BusinessException
     * @see org.pimslims.crystallization.model.TrialService#createTrialPlate(org.pimslims.crystallization.model.PlateType)
     */
    public TrialPlate createTrialPlate(final String barcode, final PlateType plateType)
        throws BusinessException {

        final TrialPlate plate = new TrialPlate(plateType);
        plate.setBarcode(barcode);

        this.saveTrialPlate(plate);

        return plate;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.crystallization.model.TrialService#findPlateType(long)
     * used in xtalPiMS_WS 
     */
    public PlateType findPlateType(final long id) {
        CrystalType ct = this.version.findFirst(CrystalType.class, CrystalType.PROP_DBID, new Long(id));
        if (null == ct) {
            return null;
        }

        PlateType ret = new PlateType();
        ret.setId(ct.getDbId());
        ret.setColumns(ct.getMaxColumn()); // Number of columns
        ret.setRows(ct.getMaxRow()); // Number of rows
        ret.setSubPositions(ct.getMaxSubPosition()); // Number of sub-positions,
                                                     // including reservoir
                                                     // if assigned
        ret.setName(ct.getName()); // Name of the plate type
        ret.setReservoir(ct.getResSubPosition()); // The index of the
                                                  // sub-position that
                                                  // contains the reservoir
        return ret;
    }

    /**
     * Find a specific crystallization plate type by name.
     * 
     * @TODO REQUIRED
     * @param name - the name of the plate type to find
     * @return The plate ype with the specified name, or null if no such plate type exists
     * @see org.pimslims.crystallization.model.TrialService#findPlateType(java.lang.String)
     */
    public PlateType findPlateType(final String name) {
        CrystalType ct = this.version.findFirst(CrystalType.class, CrystalType.PROP_NAME, name);
        if (null == ct) {
            return null;
        }
        PlateType ret = new PlateType();
        ret.setId(ct.getDbId());
        ret.setColumns(ct.getMaxColumn()); // Number of columns
        ret.setRows(ct.getMaxRow()); // Number of rows
        ret.setSubPositions(ct.getMaxSubPosition()); // Number of sub-positions,
                                                     // including reservoir
                                                     // if assigned
        ret.setName(ct.getName()); // Name of the plate type
        ret.setReservoir(ct.getResSubPosition()); // The index of the
                                                  // sub-position that
                                                  // contains the reservoir
        return ret;
    }

    /**
     * Find all the crystallization plate types.
     * 
     * @TODO REQUIRED
     * @return The collection of PlateType
     * @see org.pimslims.crystallization.model.TrialService#findAllPlateTypes()
     */
    public Collection<PlateType> findAllPlateTypes() {
        Collection<CrystalType> cts = this.version.findAll(CrystalType.class, new HashMap<String, Object>());
        Collection<PlateType> ret = new ArrayList<PlateType>(cts.size());
        for (CrystalType ct : cts) {
            PlateType pt = new PlateType();
            pt.setId(ct.getDbId());
            pt.setColumns(ct.getMaxColumn()); // Number of columns
            pt.setRows(ct.getMaxRow()); // Number of rows
            pt.setSubPositions(ct.getMaxSubPosition()); // Number of
                                                        // sub-positions,
                                                        // including reservoir
                                                        // if assigned
            pt.setName(ct.getName()); // Name of the plate type
            pt.setReservoir(ct.getResSubPosition()); // The index of the
                                                     // sub-position that
                                                     // contains the
                                                     // reservoir
            ret.add(pt);
        }
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.pimslims.crystallization.model.TrialService#findTrialDrops(org.pimslims
     * .crystallization.Sample)
     */
    public Set<TrialDrop> findTrialDrops(final Sample sample, final BusinessCriteria criteria) {
        return trialDropSerice.findTrialDrops(sample, criteria);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.pimslims.crystallization.model.TrialService#findTrialDrops(org.pimslims
     * .crystallization.model.TrialPlate)
     */
    public Set<TrialDrop> findTrialDrops(final TrialPlate plate, final BusinessCriteria criteria) {
        return trialDropSerice.findTrialDrops(plate, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.pimslims.crystallization.model.TrialService#findTrialDrops(org.pimslims
     * .crystallization.Sample, org.pimslims.crystallization.model.TrialPlate)
     */
    public Set<TrialDrop> findTrialDrops(final Sample sample, final TrialPlate plate,
        final BusinessCriteria criteria) {
        return trialDropSerice.findTrialDrops(sample, plate, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.pimslims.crystallization.model.TrialService#findTrialDrops(org.pimslims
     * .crystallization.Sample, java.util.Set)
     */
    public Collection<TrialDrop> findTrialDrops(final Sample sample, final Collection<Condition> conditions,
        final BusinessCriteria criteria) {
        return trialDropSerice.findTrialDrops(sample, conditions, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.crystallization.model.TrialService#findTrialPlate(long)
     */
    public TrialPlate findTrialPlate(final long id) {
        throw new UnsupportedOperationException("This method is not implemented");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.pimslims.crystallization.model.TrialService#findTrialPlate(java.lang
     * .String)
     */
    /**
     * 
     * @param barcode
     * @return
     * @throws BusinessException
     */
    public TrialPlate findTrialPlate(final String barcode) throws BusinessException {
        final Holder holder = getVersion().findFirst(Holder.class, Holder.PROP_NAME, barcode);
        if (null == holder) {
            return null;
        }
        return getPlate(holder);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.pimslims.crystallization.model.TrialService#saveTrialDrop(org.pimslims
     * .crystallization.model.TrialDrop)
     */
    /**
     * @TODO REQUIRED
     * @param trialDrop
     * @return
     * @throws BusinessException
     */
    @Override
    public TrialDrop saveTrialDrop(final TrialDrop trialDrop) throws BusinessException {
        return trialDropSerice.create(trialDrop);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.pimslims.crystallization.model.TrialService#saveTrialPlate(org.pimslims
     * .crystallization.model.TrialPlate)
     */
    /**
     * @TODO REQUIRED
     * @param trialPlate
     * @return
     * @throws BusinessException
     */
    public TrialPlate saveTrialPlate(final TrialPlate trialPlate) throws BusinessException {
        final TrialPlateDAO trialPlateDAO = new TrialPlateDAO();
        trialPlateDAO.create(trialPlate, getWritableVersion());
        return trialPlate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.pimslims.crystallization.model.TrialService#updateTrialDrop(org.pimslims
     * .crystallization.model.TrialDrop)
     */
    /**
     * @TODO REQUIRED
     * @param trialDrop
     * @throws BusinessException
     */
    public void updateTrialDrop(final TrialDrop trialDrop) throws BusinessException {
        trialDropSerice.updateTrialDrop(trialDrop);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.pimslims.crystallization.model.TrialService#updateTrialPlate(org.
     * pimslims.crystallization.model.TrialPlate)
     */
    /**
     * @TODO REQUIRED
     * @param trialPlate
     */
    public void updateTrialPlate(final TrialPlate trialPlate) throws BusinessException {
        final TrialPlateDAO trialPlateDAO = new TrialPlateDAO();
        trialPlateDAO.update(trialPlate, getWritableVersion());
    }

    /**
     * @TODO REQUIRED
     * @param barcode
     * @param paging
     * @return
     * @throws BusinessException
     */
    public Collection<TrialPlate> findTrialPlateByPartialBarcode(final String barcode,
        final BusinessCriteria criterion) throws BusinessException {
        final Searcher searcher = new Searcher(this.version);

        final Map<String, Object> criteria = new HashMap<String, Object>(1);
        criteria.put(Holder.PROP_NAME, barcode);
        final MetaClass metaClass = version.getModel().getMetaClass(Holder.class.getName());
        final Collection<ModelObject> holders = searcher.search(criteria, metaClass);
        return getPlates(holders);
    }

    /**
     * @TODO REQUIRED
     * @param barcode
     * @param wellPosition
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    @Override
    public TrialDrop findTrialDrop(final String barcode, final WellPosition wellPosition)
        throws BusinessException {
        final AbstractModelObject holder = getVersion().findFirst(Holder.class, Holder.PROP_NAME, barcode);
        if (null == holder) {
            return null;
        }
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put(org.pimslims.model.sample.Sample.PROP_ROWPOSITION, wellPosition.getRow());
        criteria.put(org.pimslims.model.sample.Sample.PROP_COLPOSITION, wellPosition.getColumn());
        criteria.put(org.pimslims.model.sample.Sample.PROP_SUBPOSITION, wellPosition.getSubPosition());
        org.pimslims.model.sample.Sample sample = holder.findFirst(Holder.PROP_SAMPLES, criteria);
        if (null == sample) {
            return null;
        }

        final TrialDrop drop = TrialDropServiceImpl.getWell(sample);
        drop.setPlate(this.findTrialPlate(barcode));

        return drop;
    }

    public static ConditionQuantity getCondition(InputSample is) throws BusinessException {
        ConditionQuantity additive = null;
        if (null != is && null != is.getSample() && null != is.getSample().getRefSample()) {
            RefSample recipe = is.getSample().getRefSample();
            ConditionDAO conditionDAO = new ConditionDAO(is.get_Version());
            Condition condition = conditionDAO.getFullXO(recipe);
            additive = new ConditionQuantity(condition, 1.0d, "ml/ml");
        }
        return additive;
    }

    /**
     * Reads plate details from database
     * 
     * @param holder
     * @return
     * @throws BusinessException
     */
    static TrialPlate getPlate(final Holder holder) throws BusinessException {
        final CrystalType type = (CrystalType) holder.getHolderType();
        final PlateType plateType = new PlateType();
        if (null != type) {
            plateType.setId(type.getDbId());
            plateType.setName(type.getName());
            if (null != type.getMaxColumn()) {
                plateType.setColumns(type.getMaxColumn());
            }
            if (null != type.getMaxRow()) {
                plateType.setRows(type.getMaxRow());
            }
            if (null != type.getMaxSubPosition()) {
                plateType.setSubPositions(type.getMaxSubPosition());
            } else {
                plateType.setSubPositions(1);
            }
            plateType.setReservoir(type.getResSubPosition());
        }
        final TrialPlate plate = new TrialPlate(plateType);
        plate.setBarcode(holder.getName());
        plate.setDescription(holder.getDetails());

        Screen screen = TrialPlateDAO.getRefHolder(holder);
        plate.setScreen(screen);
        plate.setAdditiveScreen(false); // TODO find parm
        // TODO plate.setMotherLiquor(additive);
        // TODO should we get the drop details?
        User creator = holder.getCreator();
        Person owner = PersonDAO.getXPerson(creator);
        plate.setOwner(owner);
        return plate;
    }

    static List<TrialPlate> getPlates(final Collection<ModelObject> holders) throws BusinessException {
        final List<TrialPlate> ret = new ArrayList<TrialPlate>(holders.size());
        for (final ModelObject modelObject : holders) {
            final Holder holder = (Holder) modelObject;
            ret.add(getPlate(holder));
        }
        return ret;
    }

    public TrialDrop findTrialDrop(final TrialPlate plate, final WellPosition wellPosition)
        throws BusinessException {
        TrialDrop trialDrop = plate.getTrialDrop(wellPosition);
        if (null == trialDrop) {
            trialDrop = this.findTrialDrop(plate.getBarcode(), wellPosition);
            plate.addTrialDrop(trialDrop);
        }
        return trialDrop;
    }

    /* **************************************************************
     * TrialPlate imager interration related
     * **************************************************************
     */
    /**
     * a User request to have extra imaging session on a plate.
     * 
     * @param barcode the barcode of the plate
     * @param date the date by which the user would like their imaging to happen
     * @throws org.pimslims.business.exception.BusinessException if an error
     */
    public void requestAdditionalImaging(final String barcode, final Calendar date) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Get a list of all the additional imaging requests received.
     * 
     * @return the list of barcodes and times requested
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Map<String, Calendar> findAdditionalImagingRequests() throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * remove the additional imaging request from the queue - in reality it will probably be useful to keep
     * all requests, just marking them complete once they are done (thus allowing us to note if the system is
     * being abused
     * 
     * @param barcode the barcode of the plate imaged
     * @throws org.pimslims.business.exception.BusinessException on an error
     */
    public void removeAdditionalImaging(final String barcode) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * A user may request to retrieve a plate from the storage device in order to, for example, take it on a
     * synchrotron trip, or look at it under a microscope, etc.
     * 
     * @param barcode the barcode of the plate
     * @param dueDate the date by which the plate has to be taken out.
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void requestPlateRetrieval(final String barcode, final Calendar dueDate) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Get a list of the plates that have been requested to be taken out of the store.
     * 
     * @return the list of plates and a time by which they should be taken out
     * @throws org.pimslims.business.exception.BusinessException on an error
     */
    public Map<String, Calendar> findPlateRetrievals() throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * remove the plate from the list of plates that have been requested to be withdrawn from the storage
     * unit. In reality it will probably be useful to keep all requests, just marking them complete once they
     * are done (thus allowing us to note if the system is being abused.
     * 
     * @param barcode
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void removePlateRetrievalRequest(final String barcode) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<TrialPlate> findAllTrialPlates(final BusinessCriteria criteria)
        throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // @Override
    public PlateType create(final PlateType plateType) {

        final Map<String, Object> attr = new HashMap<String, Object>();
        attr.put(HolderType.PROP_NAME, plateType.getName());
        attr.put(HolderType.PROP_MAXCOLUMN, plateType.getColumns());
        attr.put(HolderType.PROP_MAXROW, plateType.getRows());
        attr.put(HolderType.PROP_MAXSUBPOSITION, plateType.getSubPositions());
        attr.put(CrystalType.PROP_RESSUBPOSITION, plateType.getReservoir());
        try {
            CrystalType ct = new CrystalType((WritableVersion) version, attr);
            plateType.setId(ct.getDbId());
        } catch (final ConstraintException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return plateType;
    }

    /**
     * Create a PiMS PlateType and assign a default SchedulePlan.
     * 
     * TODO Add defaultSchedulePlan to PlateType and replace the {@link #create(PlateType)} method
     * implementation with this one.
     * 
     * TrialServiceImpl.create
     * 
     * @param plateType
     * @param defaultSchedulePlan
     * @return
     */
    public PlateType create(final PlateType plateType, final SchedulePlan defaultSchedulePlan) {

        org.pimslims.model.schedule.SchedulePlan schedulePlan =
            this.getVersion().findFirst(org.pimslims.model.schedule.SchedulePlan.class,
                org.pimslims.model.schedule.SchedulePlan.PROP_NAME, defaultSchedulePlan.getName());
        final Map<String, Object> attr = new HashMap<String, Object>();
        attr.put(HolderType.PROP_NAME, plateType.getName());
        attr.put(HolderType.PROP_MAXCOLUMN, plateType.getColumns());
        attr.put(HolderType.PROP_MAXROW, plateType.getRows());
        attr.put(HolderType.PROP_MAXSUBPOSITION, plateType.getSubPositions());
        attr.put(CrystalType.PROP_RESSUBPOSITION, plateType.getReservoir());
        attr.put(HolderType.PROP_DEFAULTSCHEDULEPLAN, schedulePlan);
        try {
            CrystalType ct = new CrystalType((WritableVersion) version, attr);
            plateType.setId(ct.getDbId());
        } catch (final ConstraintException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return plateType;
    }

    public Integer findViewCount(final BusinessCriteria criteria) throws BusinessException {
        return trialDropViewDAO.getViewCount(criteria);
    }

    /**
     * TrialServiceImpl.findViews
     * 
     * @see org.pimslims.business.ViewService#findViews(org.pimslims.business.criteria.BusinessCriteria)
     * @return a list of drop images
     */
    public Collection<TrialDropView> findViews(final BusinessCriteria criteria) throws BusinessException {
        return trialDropViewDAO.findViews(criteria);
    }

    public String convertPropertyName(final String property) throws BusinessException {
        if (property == null) {
            throw new IllegalArgumentException("Null filter name");
        }
        return property;
    }

    /*
     * //String compositeURLStub;
     * 
     * //String sliceURLStub;
     * 
     * //String zoomedURLStub;
     * 
     * @Deprecated // value never used public void
     * setCompositeImageURLStub(final String urlStub) { compositeURLStub =
     * urlStub; }
     * 
     * @Deprecated // value never used public void setSliceImageURLStub(final
     * String urlStub) { sliceURLStub = urlStub; }
     * 
     * @Deprecated // value never used public void setZoomedImageURLStub(final
     * String urlStub) { zoomedURLStub = urlStub; }
     */

    /**
     * <p>
     * Fill the reservoirs in a TrialPlate from the specified DeepWellPlate.
     * </p>
     * 
     * @param trialPlate - the TrialPlate to be filled. trialPlate must already be represented in PiMS
     * @param deepWellPlate - the deepWellPlate containing the screen condition
     * @param volumeMap - describes how much screen condition has been put in each reservoir
     * @param experimentInfo - describes the experiment to be created
     * @return The TrialPlate with reservoirs filled
     * @throws BusinessException
     * 
     *             Called from xtalpims-importer
     */
    public TrialPlate fillTrialPlate(TrialPlate trialPlate, DeepWellPlate deepWellPlate, VolumeMap volumeMap,
        PlateExperimentInfo experimentInfo) throws BusinessException {

        log.debug("In fillTrialPlate: " + trialPlate.getBarcode());

        // Run away if there is nothing to do
        if (null == volumeMap) {
            log.debug("nothing to do");
            return trialPlate;
        }

        // Find relevant WellPosition
        Set<WellPosition> wellPositions = volumeMap.getSetWellPositions();

        // Run away if there is nothing to do
        if (wellPositions.isEmpty()) {
            log.debug("nothing to do");
            return trialPlate;
        }

        try {

            // Get a WritableVersion and bootstrap permissions
            WritableVersion wv = (WritableVersion) this.getVersion();
            if (null != experimentInfo.getGroupName()) {
                wv.setDefaultOwner(experimentInfo.getGroupName());
            }

            // Find the holder for this trial plate
            // log.debug("find trial holder");
            // Holder trialHolder = wv.findFirst(Holder.class, Holder.PROP_NAME,
            // trialPlate.getBarcode());

            log.debug("create trial holder");
            TrialPlateDAO trialPlateDAO = new TrialPlateDAO();
            AbstractModelObject trialHolder =
                (AbstractModelObject) trialPlateDAO.createHolderOnly(trialPlate, wv);

            // Find the holder for this trial plate
            log.debug("find deep holder");
            // Holder deepHolder = wv.findFirst(Holder.class, Holder.PROP_NAME,
            // deepWellPlate.getBarcode());
            // TODO This needs access control!
            Query q =
                wv.getSession()
                    .createQuery(
                        "select h from org.pimslims.model.holder.Holder h left outer join fetch h.samples s left outer join fetch s.outputSample where h.name = :barcode");
            q.setString("barcode", deepWellPlate.getBarcode());
            Holder deepHolder = (Holder) q.uniqueResult();

            // Create an ExperimentGroup
            log.debug("create eg");
            ExperimentGroup eg =
                TrialServiceUtils.createExperimentGroup(wv, "Fill " + trialPlate.getBarcode(),
                    "Fill crystallization trial plate reservoirs ready for trial", experimentInfo.getRunAt());

            // Create attribute map
            log.debug("create eattr");
            Map<String, Object> eattr =
                TrialServiceUtils.createExperimentAttributeMap(wv, eg, experimentInfo);

            log.debug("find stocks");
            Set<org.pimslims.model.sample.Sample> stocks = deepHolder.getSamples();
            log.debug("found " + stocks.size() + " stocks");

            // Loop over all the relevant positions
            for (WellPosition wellPosition : wellPositions) {

                log.debug("start loop");

                // Find the volume transfered at this WellPosition
                Double volume = volumeMap.getVolume(wellPosition);

                // If some volume was transferred
                if (volume.doubleValue() > 0d) {

                    // Get the stock Sample
                    WellPosition stockPosition = new WellPosition(wellPosition, 1);
                    org.pimslims.model.sample.Sample stock =
                        TrialServiceUtils.getSampleByHolderPosition(stocks, stockPosition);

                    if (null == stock) {
                        log.error("No stock at " + stockPosition);
                        throw new BusinessException("No stock at " + stockPosition);
                    }
                    stocks.remove(stock);

                    // Why float?
                    Float fvolume = new Float(volume.doubleValue());

                    // Create reservoir Sample
                    String sampleName = trialPlate.getBarcode() + ":" + wellPosition.toString();
                    org.pimslims.model.sample.Sample reservoir =
                        TrialServiceUtils.createSample(wv, sampleName, trialHolder, wellPosition,
                            stock.getRefSample(), fvolume, volumeMap.getUnit());

                    // Create production Experiment
                    Experiment expt =
                        TrialServiceUtils.createExperiment(wv, "Fill " + sampleName, eattr, experimentInfo);

                    // Create InputSample
                    // JMD FIXME Check loss of functionality - method signature
                    // has changed
                    // TrialServiceUtils.createInputSample(wv, expt, stock,
                    // "Stock", fvolume, volumeMap
                    // .getUnit());
                    TrialServiceUtils.createInputSample(wv, expt, "Screen", stock, fvolume);

                    // Create OutputSample
                    TrialServiceUtils.createOutputSample(wv, expt, "Reservoir", reservoir, fvolume,
                        volumeMap.getUnit());

                    // And translate into API-speak
                    trialPlate.addReservoir(
                        wellPosition,
                        new ConditionQuantity(deepWellPlate.getCondition(wellPosition), volume, volumeMap
                            .getUnit()));

                }

                log.debug("end loop");

            }

            log.debug("copying RefHolderOffsets");

            // Link trialHolder to Screen
            Set<RefHolderOffset> rhos = deepHolder.getRefHolderOffsets();
            for (RefHolderOffset rho : rhos) {
                log.debug("refholderoffset loop");
                Map<String, Object> attr = new HashMap<String, Object>();
                attr.put(RefHolderOffset.PROP_COLOFFSET, rho.getColOffset());
                attr.put(RefHolderOffset.PROP_HOLDER, trialHolder);
                attr.put(RefHolderOffset.PROP_REFHOLDER, rho.getRefHolder());
                attr.put(RefHolderOffset.PROP_ROWOFFSET, rho.getRowOffset());
                attr.put(RefHolderOffset.PROP_SUBOFFSET, trialPlate.getPlateType().getReservoir() - 1);
                new RefHolderOffset(wv, attr);
            }
            log.debug("done copying RefHolderOffsets");

            log.debug("done create");

            return trialPlate;

        }

        catch (ConstraintException e) {

            // inconsistent values, e.g. bar code already on record
            throw new BusinessException(e);

        }

    }

    /**
     * <p>
     * Fill the reservoirs in a TrialPlate with the specified Screen. This method allows the creation of
     * filled TrialPlates without the use of a DeepWellPlate.
     * </p>
     * 
     * @param trialPlate - the TrialPlate to be filled
     * @param screen - the set of screen conditions to be put in the reservoirs
     * @param volumeMap - describes how much screen condition has been put in each reservoir
     * @param experimentInfo - describes the experiment to be created
     * @param source - the source sample if this is an optimisation plate
     * @return The TrialPlate with reservoirs filled
     * @throws BusinessException
     */
    public TrialPlate fillTrialPlate(TrialPlate trialPlate, Screen screen, VolumeMap volumeMap,
        PlateExperimentInfo experimentInfo, Sample source) throws BusinessException {

        if (null == trialPlate) {
            log.debug("In fillTrialPlate: trialPlate is null");
            throw new BusinessException("trialPlate must not be null");
        }

        if (null == screen) {
            log.debug("In fillTrialPlate: screen is null");
            throw new BusinessException("screen must not be null");
        }

        log.debug("In fillTrialPlate: " + trialPlate.getBarcode());

        // Run away if there is nothing to do
        if (null == volumeMap) {
            log.debug("nothing to do");
            return trialPlate;
        }

        // Find relevant WellPosition
        Set<WellPosition> wellPositions = volumeMap.getSetWellPositions();

        // Run away if there is nothing to do
        if (wellPositions.isEmpty()) {
            log.debug("nothing to do");
            return trialPlate;
        }

        try {

            // Get a WritableVersion and bootstrap permissions
            WritableVersion wv = (WritableVersion) this.getVersion();
            if (null != experimentInfo.getGroupName()) {
                wv.setDefaultOwner(experimentInfo.getGroupName());
            }

            // Find the holder for this trial plate
            // log.debug("find trial holder");
            // Holder trialHolder = wv.findFirst(Holder.class, Holder.PROP_NAME,
            // trialPlate.getBarcode());

            log.debug("apply the screen to the trialPlate");
            trialPlate.setScreen(screen);

            log.debug("create trial holder");
            TrialPlateDAO trialPlateDAO = new TrialPlateDAO();
            Holder trialHolder = (Holder) trialPlateDAO.createHolderOnly(trialPlate, wv);

            // Find the RefHolder for the screen
            log.debug("find screen");
            Query q =
                wv.getSession()
                    .createQuery(
                        "select rh from org.pimslims.model.holder.RefHolder rh left outer join fetch rh.refSamplePositions rsp left outer join fetch rsp.refSample where rh.name = :name");
            q.setString("name", screen.getName());
            RefHolder screenRefHolder = (RefHolder) q.uniqueResult();

            // Find the source, if there is one
            String egName = "Fill " + trialPlate.getBarcode();
            String egDesc = "Pre-filled crystallization trial plate";
            org.pimslims.model.sample.Sample sourceSample = null;
            if (null != source) {
                sourceSample = new SampleDAO(wv).getPO(source);
                egName += " as optimization from " + source.getName();
                egDesc = "Pre-filled optimization plate from " + source.getName();
                trialHolder.setDetails("Optimization from " + source.getName());
            }

            // Create an ExperimentGroup
            log.debug("create eg");
            ExperimentGroup eg =
                TrialServiceUtils.createExperimentGroup(wv, egName, egDesc, experimentInfo.getRunAt());

            // Create attribute map
            log.debug("create eattr");
            Map<String, Object> eattr =
                TrialServiceUtils.createExperimentAttributeMap(wv, eg, experimentInfo);

            log.debug("find stocks");
            Set<org.pimslims.model.holder.RefSamplePosition> stocks = screenRefHolder.getRefSamplePositions();

            // Loop over all the relevant positions
            for (WellPosition wellPosition : wellPositions) {

                log.debug("start loop");

                // Find the volume transfered at this WellPosition
                Double volume = volumeMap.getVolume(wellPosition);

                // If some volume was transferred
                if (volume.doubleValue() > 0d) {

                    // Get the stock Sample
                    WellPosition stockPosition = new WellPosition(wellPosition, 1);
                    org.pimslims.model.sample.RefSample stock = null;
                    for (org.pimslims.model.holder.RefSamplePosition rsp : stocks) {
                        if ((wellPosition.getRow() == rsp.getRowPosition())
                            && (wellPosition.getColumn() == rsp.getColPosition())
                            && (1 == rsp.getSubPosition())) {

                            stock = rsp.getRefSample();
                            break;

                        }
                    }

                    if (null == stock) {
                        log.error("No stock at " + stockPosition);
                        throw new BusinessException("No stock at " + stockPosition);
                    }
                    stocks.remove(stock);

                    // Why float?
                    Float fvolume = new Float(volume.doubleValue());

                    // Create reservoir Sample
                    String sampleName = trialPlate.getBarcode() + ":" + wellPosition.toString();
                    org.pimslims.model.sample.Sample reservoir =
                        TrialServiceUtils.createSample(wv, sampleName, trialHolder, wellPosition, stock,
                            fvolume, volumeMap.getUnit());

                    // Create production Experiment
                    Experiment expt =
                        TrialServiceUtils.createExperiment(wv, "Fill " + sampleName, eattr, experimentInfo);

                    // Create InputSample
                    if (null != sourceSample) {
                        // JMD FIXME Check loss of functionality - method
                        // signature has changed
                        // TrialServiceUtils.createInputSample(wv, expt,
                        // sourceSample, "Source", fvolume,
                        // volumeMap.getUnit());
                        TrialServiceUtils.createInputSample(wv, expt, "Source", sourceSample, fvolume);
                    }

                    // Create OutputSample
                    TrialServiceUtils.createOutputSample(wv, expt, "Reservoir", reservoir, fvolume,
                        volumeMap.getUnit());

                    // And translate into API-speak
                    trialPlate
                        .addReservoir(wellPosition, new ConditionQuantity(screen.getCondition(wellPosition),
                            volume, volumeMap.getUnit()));

                }

                log.debug("end loop");

            }

            log.debug("done create");

            return trialPlate;

        }

        catch (ConstraintException e) {

            // inconsistent values, e.g. bar code already on record
            throw new BusinessException(e);

        }

    }

    /**
     * 
     * @param filledTrialPlate
     * @param proteinVolumeMaps
     * @param reservoirVolumeMap - how much was reservoir gets put INTO each wellPosition
     * @param experimentInfo
     * @throws BusinessException
     */
    // used in OPPF, TODO get under test
    public TrialPlate setupTrialPlate(TrialPlate filledTrialPlate, List<SampleVolumeMap> proteinVolumeMaps,
        VolumeMap reservoirVolumeMap, PlateExperimentInfo experimentInfo) throws BusinessException {

        // All volumeMaps must have same unit
        String commonUnit;
        try {
            commonUnit = VolumeMap.getCommonUnit(proteinVolumeMaps, reservoirVolumeMap);
        } catch (BusinessException e) {
            throw new IllegalArgumentException(
                "all proteinVolumeMaps and reservoirVolumeMap must use the same unit");
        }

        // Find relevant WellPosition
        Set<WellPosition> wellPositions = new HashSet<WellPosition>();
        if (null != reservoirVolumeMap) {
            for (WellPosition wp : reservoirVolumeMap.getSetWellPositions()) {
                wellPositions.add(wp);
            }
        }

        for (SampleVolumeMap proteinVolumeMap : proteinVolumeMaps) {
            for (WellPosition wp : proteinVolumeMap.getSetWellPositions()) {
                wellPositions.add(wp);
            }
        }

        // Run away if there is nothing to do
        if (wellPositions.isEmpty()) {
            return filledTrialPlate;
        }

        try {

            ReadableVersion rv = this.getVersion();

            // Find the holder for this trial plate
            Holder holder = rv.findFirst(Holder.class, Holder.PROP_NAME, filledTrialPlate.getBarcode());
            if (null == holder) {
                throw new IllegalArgumentException("no such holder: " + filledTrialPlate.getBarcode());
            }
            // TODO Fetch Samples and OutputSamples?

            // Fetch Samples for SampleVolumeMaps
            Map<Sample, org.pimslims.model.sample.Sample> sampleMap =
                new HashMap<Sample, org.pimslims.model.sample.Sample>();
            org.pimslims.model.core.LabNotebook accessObject = null;
            for (SampleVolumeMap proteinVolumeMap : proteinVolumeMaps) {
                // Get the protein Sample
                org.pimslims.model.sample.Sample proteinSample =
                    TrialServiceUtils.getSample(rv, proteinVolumeMap.getSample());
                if (null == proteinSample) {
                    throw new BusinessException("No sample found in PiMS for "
                        + proteinVolumeMap.getSample().getName() + "(" + proteinVolumeMap.getSample().getId()
                        + ")");
                }
                // TODO Check implementation of equals and hashCode for Sample
                sampleMap.put(proteinVolumeMap.getSample(), proteinSample);

                // Establish accessObject
                if ((null != proteinSample.getAccess()) && (null != proteinSample.getAccess().getDbId())) {
                    if (null == accessObject) {
                        accessObject = proteinSample.getAccess();
                    } else if (!accessObject.getDbId().equals(proteinSample.getAccess().getDbId())) {
                        throw new BusinessException("Plate uses samples from different projects! "
                            + filledTrialPlate.getBarcode());
                    }
                }
            }

            // Get a WritableVersion and bootstrap the permissions
            WritableVersion wv = (WritableVersion) rv;
            wv.setDefaultOwner(accessObject);

            // Create an ExperimentGroup
            ExperimentGroup exptGroup =
                TrialServiceUtils.createExperimentGroup(wv, "Set up " + filledTrialPlate.getBarcode(),
                    "Set up crystallization trial plate", experimentInfo.getRunAt());

            // Create attribute map
            Map<String, Object> eattr =
                TrialServiceUtils.createExperimentAttributeMap(wv, exptGroup, experimentInfo);

            // Flag to indicate whether we have set Holder.firstSample
            boolean firstSampleApplied = false;

            // Sort the well positions so that the 'first' in first sample is as
            // one would expect
            List<WellPosition> sortedWellPositions = new ArrayList<WellPosition>(wellPositions);
            Collections.sort(sortedWellPositions);

            // Loop over all the relevant positions
            for (WellPosition wellPosition : sortedWellPositions) {

                WellPosition reservoirPosition =
                    new WellPosition(wellPosition, filledTrialPlate.getPlateType().getReservoir());

                // Get the relevant TrialDrop
                // NB It would appear that we can't rely on trialdrop or
                // reservoir
                // being populated the first time through!
                TrialDrop trialDrop = filledTrialPlate.getTrialDrop(wellPosition);
                if (null == trialDrop) {
                    trialDrop = new TrialDrop();
                    trialDrop.setWellPosition(wellPosition);
                    filledTrialPlate.addTrialDrop(trialDrop);
                }
                trialDrop.setReservoir(filledTrialPlate.getReservoir(wellPosition));

                // Sample name
                String sampleName = filledTrialPlate.getBarcode() + ":" + wellPosition.toString();

                // Create production Experiment
                Experiment expt = TrialServiceUtils.createExperiment(wv, sampleName, eattr, experimentInfo);

                // Keep a list of InputSamples, so we can copy stuff down
                List<InputSample> inputSamples = new ArrayList<InputSample>();

                // The mother liquor is handled by setting the RefSample
                RefSample trialRefSample = null;

                // Track total input sample volume
                double isv = 0D;

                // Flag to indicate that there is reservoir present in this
                // wellPosition
                // TODO Might use to choose by which method to calculate output
                // sample volume
                // boolean usesReservoir = false;

                // Reservoir to mother liquor
                if (null != reservoirVolumeMap) {

                    Double reservoirVolume = reservoirVolumeMap.getVolume(wellPosition);

                    // If reservoirVolume > VolumeMap.ZERO_VOLUME
                    if (VolumeMap.ZERO_VOLUME.compareTo(reservoirVolume) < 0) {

                        // TODO Flag reservoir used
                        // usesReservoir = true;

                        // Get the reservoir Sample
                        org.pimslims.model.sample.Sample reservoirSample =
                            TrialServiceUtils.getSampleByHolderPosition(holder, reservoirPosition);

                        if (null == reservoirSample) {
                            log.error("Reservoir not found at " + reservoirPosition);
                            throw new BusinessException("Reservoir not found at " + reservoirPosition);
                        }

                        // Create InputSample for reservoir
                        // JMD FIXME Check loss of functionality - method
                        // signature has changed
                        // inputSamples.add(TrialServiceUtils.createInputSample(wv,
                        // expt, reservoirSample,
                        // TrialServiceImpl.MOTHER_LIQUOR, new
                        // Float(reservoirVolume), commonUnit));
                        inputSamples.add(TrialServiceUtils.createInputSample(wv, expt, MOTHER_LIQUOR,
                            reservoirSample, new Float(reservoirVolume)));

                        // Output sample volume is reservoir volume
                        isv = reservoirVolume.doubleValue();

                        // Copy the reservoir's RefSample onto the trial
                        trialRefSample = reservoirSample.getRefSample();

                        Condition reservoirCondition = null;
                        if (null == trialDrop.getReservoir()) {
                            reservoirCondition = new ConditionDAO(wv).getFullXO(trialRefSample);
                            ConditionQuantity reservoir =
                                new ConditionQuantity(reservoirCondition, reservoirSample.getInitialAmount()
                                    .doubleValue(), "L");
                            trialDrop.setReservoir(reservoir);
                            filledTrialPlate.addReservoir(wellPosition, reservoir);
                        } else {
                            reservoirCondition = trialDrop.getReservoir().getCondition();
                        }

                        // And translate into API-speak
                        // trialDrop.setMotherLiquor(new
                        // ConditionQuantity(trialDrop.getReservoir().getCondition(),
                        // reservoirVolume, reservoirVolumeMap.getUnit()));
                        trialDrop.setMotherLiquor(new ConditionQuantity(reservoirCondition, reservoirVolume,
                            reservoirVolumeMap.getUnit()));

                    }

                }

                // Loop over all the protein VolumeMaps
                // FIXME Need a different parameter name per loop for
                // co-crystallizations!
                for (SampleVolumeMap proteinVolumeMap : proteinVolumeMaps) {

                    // Get the volume at this WellPosition
                    Double proteinVolume = proteinVolumeMap.getVolume(wellPosition);

                    // If proteinVolume > VolumeMap.ZERO_VOLUME
                    if (VolumeMap.ZERO_VOLUME.compareTo(proteinVolume) < 0) {

                        // Get the protein Sample
                        org.pimslims.model.sample.Sample proteinSample =
                            sampleMap.get(proteinVolumeMap.getSample());

                        // Create InputSample for protein
                        // JMD FIXME Check loss of functionality - method
                        // signature has changed
                        // inputSamples.add(TrialServiceUtils.createInputSample(wv,
                        // expt, proteinSample,
                        // "Sample", new Float(proteinVolume), commonUnit));
                        inputSamples.add(TrialServiceUtils.createInputSample(wv, expt,
                            proteinVolumeMap.getName(), proteinSample, new Float(proteinVolume)));

                        // Update the total input sample volume
                        isv += proteinVolume.doubleValue();

                        // And translate into API-speak
                        trialDrop.addSample(new SampleQuantity(proteinVolumeMap.getSample(), proteinVolume,
                            proteinVolumeMap.getUnit()));

                    }

                }

                /*
                 * Determine the effective output sample volume. This is my best
                 * guess as to the final volume of the equilibrated drop. For
                 * wellPositions which use the reservoir, this is the volume of
                 * reservoir added. For wellPositions that don't, one of the
                 * input samples is probably contributing something reservoir-
                 * like, but I can't tell which. In this case, my best guess is
                 * the total volume of the input samples.
                 * 
                 * TODO Can I do better than this?
                 * 
                 * Actually, its probably better just to use the total input
                 * sample volume, as that its what people expect to see
                 */
                double osv = isv;
                // if (usesReservoir) {
                // osv = reservoirVolume.doubleValue();
                // }

                // If there is some output sample
                if (osv > 0D) {

                    // Create trial Sample
                    org.pimslims.model.sample.Sample trial =
                        TrialServiceUtils.createSample(wv, sampleName, holder, wellPosition, trialRefSample,
                            new Float(osv), commonUnit);

                    // Copy SampleComponents from InputSamples to output Sample
                    TrialServiceUtils.copySampleComponents(wv, inputSamples, trial, osv);

                    // Create OutputSample
                    TrialServiceUtils.createOutputSample(wv, expt, "Trial Drop", trial, new Float(osv),
                        commonUnit);

                    // Apply the first sample to the plate -
                    // PlateExperimentViewDAO requires this to be set
                    if (!firstSampleApplied) {
                        try {
                            holder.set_Role(Holder.PROP_FIRSTSAMPLE, trial);
                            firstSampleApplied = true;
                        } catch (AccessException e) {
                            log.error(
                                "Failed to set firstSample " + trial.getName() + " on holder "
                                    + holder.getName(), e);
                            throw new BusinessException(e);
                        }
                    }

                }

            }

        }

        catch (ConstraintException e) {

            // inconsistent values, e.g. bar code already on record
            throw new BusinessException(e);

        }

        // Return the now-setup trial plate
        return filledTrialPlate;

    }

}
