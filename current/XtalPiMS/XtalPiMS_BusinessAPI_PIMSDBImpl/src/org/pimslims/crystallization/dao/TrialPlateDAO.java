/**
 * 
 */
package org.pimslims.crystallization.dao;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.pimslims.access.PIMSAccess;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.SampleQuantity;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.implementation.TrialServiceImpl;
import org.pimslims.crystallization.implementation.TrialServiceUtils;
import org.pimslims.dao.WritableVersion;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.logging.Logger;
import org.pimslims.metamodel.AbstractModelObject;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.holder.RefHolder;
import org.pimslims.model.holder.RefHolderOffset;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.CrystalType;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;

/**
 * @author Bill Lin
 * 
 */
public class TrialPlateDAO {

    /**
     * <p>
     * Logger for this class.
     * </p>
     */
    private static final Logger log = Logger.getLogger(TrialPlateDAO.class);

    private static final String TRIALPLATEEXPTYPENAME = "Trials";

    private static final String TRIALPLATEPROTOCOLNAME = "CrystalTrial";

    public static final Object TRIALPLATEHOLDERCATEGORYNAME = "TrialPlate";

    /**
     * <p>
     * Insert the specified TrialPlate into the data store.
     * </p>
     * 
     * @param trialPlate - the TrialPlate to store
     * @param wv - the WritableVersion in which to store the TrialPlate
     * @throws BusinessException if something goes wrong
     */
    public ModelObject create(final TrialPlate trialPlate, final WritableVersion wv) throws BusinessException {

        ModelObject object = null;

        try {
            final org.pimslims.dao.FlushMode oldFlushModel = ((WritableVersionImpl) wv).getFlushMode();
            // improve performance
            ((WritableVersionImpl) wv).setFlushMode(org.pimslims.dao.FlushMode.batchMode());

            object = createHolder(trialPlate, wv, false);

            ((WritableVersionImpl) wv).setFlushMode(oldFlushModel);

        }

        catch (final ConstraintException e) {

            throw new BusinessException(e.getMessage(), e);

        } catch (final AccessException e) {
            throw new BusinessException(e.getMessage(), e);

        }

        return object;
    }

    /**
     * <p>
     * Insert the specified TrialPlate into the data store.
     * </p>
     * 
     * @param trialPlate - the TrialPlate to store
     * @param wv - the WritableVersion in which to store the TrialPlate
     * @throws BusinessException if something goes wrong
     */
    public ModelObject createHolderOnly(final TrialPlate trialPlate, final WritableVersion wv)
        throws BusinessException {

        ModelObject object = null;

        try {
            final org.pimslims.dao.FlushMode oldFlushModel = ((WritableVersionImpl) wv).getFlushMode();
            // improve performance
            ((WritableVersionImpl) wv).setFlushMode(org.pimslims.dao.FlushMode.batchMode());

            object = createHolder(trialPlate, wv, true);

            ((WritableVersionImpl) wv).setFlushMode(oldFlushModel);

        }

        catch (final ConstraintException e) {

            throw new BusinessException(e.getMessage(), e);

        } catch (final AccessException e) {
            throw new BusinessException(e.getMessage(), e);

        }

        return object;
    }

    /**
     * <p>
     * Create a Holder for the specified TrialPlate.
     * </p>
     * 
     * @param trialPlate
     * @param wv
     * @return
     * @throws ConstraintException
     * @throws BusinessException
     * @throws AccessException
     */
    private AbstractModelObject createHolder(final TrialPlate trialPlate, final WritableVersion wv,
        boolean holderOnly) throws ConstraintException, BusinessException, AccessException {

        log.debug("In createHolder()");

        final CrystalType holderType = findorCreateHolderType(trialPlate.getPlateType(), wv);
        // set creator
        User creator = null;
        if (trialPlate.getOwner() != null) {
            creator = getUser(trialPlate.getOwner(), wv);
            /*
             * if (creator != null) { wv.setDefaultCreator(creator); }
             */
        } else {
            creator = wv.getCurrentUser();
        }
        if (trialPlate.getGroup() != null && null == wv.getCurrentDefaultOwner()) {
            final UserGroup ug =
                wv.findFirst(UserGroup.class, UserGroup.PROP_NAME, trialPlate.getGroup().getName());
            LabNotebook owner = null;
            for (final Permission permission : ug.getPermissions()) {
                if (permission.getOpType().equals(PIMSAccess.CREATE)) {
                    owner = permission.getAccessObject();
                }

            }
            if (owner != null) {
                wv.setDefaultOwner(owner);
            }

        }

        // create holder
        final Map<String, Object> attr = new HashMap<String, Object>();
        attr.put(Holder.PROP_HOLDERCATEGORIES, getTrialPlateHolerCatergory(wv));
        attr.put(Holder.PROP_HOLDERTYPE, holderType);
        attr.put(Holder.PROP_NAME, trialPlate.getBarcode());
        attr.put(Holder.PROP_DETAILS, trialPlate.getDescription());
        if (trialPlate.getDestroyDate() != null) {
            attr.put(Holder.PROP_ENDDATE, trialPlate.getDestroyDate());
        }
        /*
         * JMD Fix to update trialPlate.createDate if necessary
         * 
         * if (trialPlate.getCreateDate() == null) {
         * attr.put(Holder.PROP_STARTDATE, Calendar.getInstance()); } else {
         * attr.put(Holder.PROP_STARTDATE, trialPlate.getCreateDate()); }
         */
        if (trialPlate.getCreateDate() == null) {
            trialPlate.setCreateDate(Calendar.getInstance());
        }
        attr.put(Holder.PROP_STARTDATE, trialPlate.getCreateDate());

        if (creator != null) {
            attr.put(Holder.PROP_CREATOR, creator);
        }

        final Holder holder = new Holder(wv, attr);

        // set refHolder
        if (trialPlate.getScreen() != null) {
            final RefHolder refHolder = getTrialPlateRefHolder(wv, trialPlate.getScreen());
            if (refHolder != null) {
                setRefHolder(wv, holder, refHolder, holderType.getResSubPosition());
            }

        }

        // Apply Id
        trialPlate.setId(holder.getDbId());

        if (holderOnly) {
            return holder;
        }

        // create pims' plateExperiment

        // create exp group
        final Map<String, Object> a = new java.util.HashMap<String, Object>();
        a.put(ExperimentGroup.PROP_NAME, trialPlate.getBarcode());
        a.put(ExperimentGroup.PROP_PURPOSE, "Trial Plate Experiment");
        a.put(ExperimentGroup.PROP_STARTDATE, holder.getStartDate());
        a.put(ExperimentGroup.PROP_ENDDATE, holder.getEndDate());
        final ExperimentGroup expGroup = wv.create(ExperimentGroup.class, a);

        /*
         * HolderFactory.createPlateExperiment(wv, holder, holder.getName(),
         * "Trial Plate Experiment", types, holder.getStartDate(),
         * holder.getEndDate(), holder.getDetails(), protocol, attributes,
         * null);
         */

        // create each experiment's details
        createExperiments(expGroup, holder, trialPlate);
        holder.updateDerivedData();
        return holder;

    }

    static Map<String, String> userIDMap = new HashMap<String, String>();

    private User getUser(final Person owner, final WritableVersion wv) {
        if (userIDMap.keySet().contains(owner.getUsername())) {
            final User user = wv.get(userIDMap.get(owner.getUsername()));
            if (user != null) {
                return user;
            }
        }
        final PersonDAO personDAO = new PersonDAO(wv);
        final User user = personDAO.getUser(owner);
        if (user == null) {
            System.err.println("Can not find user in pims db:" + owner.getUsername());
            return null;
        }
        userIDMap.put(owner.getUsername(), user.get_Hook());
        return user;

    }

    /*
     * Make an experiment etc for each trial drop. This method is organised to
     * make 96 experiments, then 96 output samples, etc. This gives Hibernate a
     * chance to use bulk inserts. It would be even better to program bulk
     * inserts ourselves. That would require some reorganisation of the DM, to
     * allow Hibernate to manage dbids.
     */
    private void createExperiments(final ExperimentGroup expGroup, final Holder holder,
        final TrialPlate trialPlate) throws BusinessException, ConstraintException, AccessException {
        final String barcode = trialPlate.getBarcode();
        final WritableVersion wv = (WritableVersion) expGroup.get_Version();

        // final RefHolder screen = getTrialPlateRefHolder(wv,
        // trialPlate.getScreen());
        final Collection<ExperimentType> types = getTrialPlateExpType(wv);
        final Protocol protocol = getTrialPlateProtocol(wv);
        ResearchObjective ro = null;
        User operator = null;
        // final User creator = null;

        if (trialPlate.getConstruct() != null) {
            // construct
            ro = (new ConstructDAO(wv)).getPO(trialPlate.getConstruct());
        }
        if (trialPlate.getOperator() != null) {
            // operator
            operator = (getUser(trialPlate.getOperator(), wv));
        } else {
            operator = holder.getCreator();
        }
        final Map<String, Object> baseExpAttributes = new HashMap<String, Object>();
        baseExpAttributes.put(Experiment.PROP_EXPERIMENTTYPE, types);
        baseExpAttributes.put(Experiment.PROP_EXPERIMENTGROUP, expGroup);
        baseExpAttributes.put(Experiment.PROP_PROTOCOL, protocol);
        baseExpAttributes.put(Experiment.PROP_RESEARCHOBJECTIVE, ro);
        baseExpAttributes.put(Experiment.PROP_OPERATOR, operator);
        baseExpAttributes.put(Experiment.PROP_CREATOR, holder.getCreator());
        baseExpAttributes.put(Experiment.PROP_STATUS, "To_be_run");
        baseExpAttributes.put(Experiment.PROP_STARTDATE, holder.getStartDate());
        if (holder.getEndDate() != null) {
            baseExpAttributes.put(Experiment.PROP_ENDDATE, holder.getEndDate());
        } else {
            baseExpAttributes.put(Experiment.PROP_ENDDATE, holder.getStartDate());
        }

        if (trialPlate.getTrialDrops() == null || trialPlate.getTrialDrops().size() < 1) {
            initTrialDrops(trialPlate, holder);
        }
        // TODO createSample(trialPlate.getMotherLiquor());
        // create experiment,sample for each trialDrop
        wv.flush();
        Map<TrialDrop, Experiment> experiments = new HashMap<TrialDrop, Experiment>();
        for (final TrialDrop trialDrop : trialPlate.getTrialDrops()) {
            final WellPosition wellPosition = trialDrop.getWellPosition();
            // experiment
            final Map<String, Object> expAttributes = new HashMap<String, Object>(baseExpAttributes);
            setConstruct(wv, ro, trialDrop, expAttributes);
            expAttributes.put(Experiment.PROP_NAME, barcode + wellPosition);
            final Experiment exp = new Experiment(wv, expAttributes);
            experiments.put(trialDrop, exp);
            // inputSample

            // TODO how use: ScreenDAO.getCondition(screen, wellPosition);

        }
        wv.flush();
        for (Iterator<Map.Entry<TrialDrop, Experiment>> iterator = experiments.entrySet().iterator(); iterator
            .hasNext();) {
            Entry<TrialDrop, Experiment> entry = iterator.next();
            Experiment exp = entry.getValue();
            TrialDrop trialDrop = entry.getKey();
            createOrFindProteinSample(wv, exp, trialDrop);
        }
        RefOutputSample ros = null;
        if (null != protocol) {
            ros = protocol.findFirst(Protocol.PROP_REFOUTPUTSAMPLES, RefOutputSample.PROP_NAME, "Trial Drop");

        }
        for (Iterator<Map.Entry<TrialDrop, Experiment>> iterator = experiments.entrySet().iterator(); iterator
            .hasNext();) {
            Entry<TrialDrop, Experiment> entry = iterator.next();
            Experiment exp = entry.getValue();
            TrialDrop trialDrop = entry.getKey();
            final Collection<SampleCategory> trialDropSc = TrialDropDAO.getTrialDropSampleCategory(wv);
            final WellPosition wellPosition = trialDrop.getWellPosition();

            final Map<String, Object> attributes = new HashMap<String, Object>();
            attributes.put(Sample.PROP_NAME, exp.getName());
            trialDrop.setName(exp.getName());
            attributes.put(Sample.PROP_DETAILS, trialDrop.getDescription());
            attributes.put(Sample.PROP_CREATOR, exp.getCreator());
            attributes.put(Sample.PROP_HOLDER, holder);
            attributes.put(Sample.PROP_ROWPOSITION, wellPosition.getRow());
            attributes.put(Sample.PROP_COLPOSITION, wellPosition.getColumn());
            attributes.put(Sample.PROP_SUBPOSITION, wellPosition.getSubPosition());
            attributes.put(Sample.PROP_SAMPLECATEGORIES, trialDropSc);

            final Sample trialSample = new Sample(wv, attributes);
            trialDrop.setId(trialSample.getDbId());

        }
        for (Iterator<Map.Entry<TrialDrop, Experiment>> iterator = experiments.entrySet().iterator(); iterator
            .hasNext();) {
            Entry<TrialDrop, Experiment> entry = iterator.next();
            Experiment exp = entry.getValue();
            TrialDrop trialDrop = entry.getKey();
            final OutputSample os = new OutputSample(wv, exp);
            Sample trialSample = wv.get(trialDrop.getId());
            os.setSample(trialSample);
            os.setName("Trial Drop");
            os.setRefOutputSample(ros);
        }
        Sample additive = null;
        TrialServiceUtils.createInputSample(wv, expGroup, TrialServiceImpl.MOTHER_LIQUOR, additive, 50f);
        TrialServiceUtils.createParameters(wv, expGroup, TrialServiceImpl.ADDITIVE_SCREEN,
            Boolean.toString(trialPlate.isAdditiveScreen()));
        wv.flush();
    }

    static void initTrialDrops(final TrialPlate trialPlate, final Holder holder) {
        final CrystalType holderType = (CrystalType) holder.getHolderType();
        for (int row = 1; row <= holderType.getMaxRow(); row++) {
            for (int col = 1; col <= holderType.getMaxColumn(); col++) {
                for (int sub = 1; sub <= holderType.getMaxSubPosition(); sub++) {
                    if (holderType.getResSubPosition() == sub) {
                        continue;
                    }
                    final TrialDrop trialDrop = new TrialDrop();
                    trialDrop.setWellPosition(new WellPosition(row, col, sub));
                    trialPlate.addTrialDrop(trialDrop);
                }
            }
        }
    }

    private void setConstruct(final WritableVersion wv, final ResearchObjective ro,
        final TrialDrop trialDrop, final Map<String, Object> expAttributes) throws BusinessException {
        // construct if different with plate
        if (trialDrop.getSamples() == null || trialDrop.getSamples().isEmpty()) {
            return;
        }
        final Construct sampleConstruct = trialDrop.getSamples().iterator().next().getSample().getConstruct();
        if (sampleConstruct != null && (ro == null || !sampleConstruct.getName().equals(ro.getName()))) {
            final ResearchObjective sampleRO = (new ConstructDAO(wv)).getPO(sampleConstruct);
            expAttributes.put(Experiment.PROP_RESEARCHOBJECTIVE, sampleRO);
        }
    }

    static Map<String, String> refHolderIDMap = new HashMap<String, String>();

    /**
     * search the screen/refholder by name TrialPlateDAO.getTrialPlateRefHolder
     * 
     * @param wv
     * @param screen
     * @return
     */
    private RefHolder getTrialPlateRefHolder(final WritableVersion wv, final Screen screen) {
        if (screen == null) {
            return null;
        }
        final String screenName = screen.getName();
        if (refHolderIDMap.keySet().contains(screenName)) {
            final RefHolder refHolder = wv.get(refHolderIDMap.get(screenName));
            if (refHolder != null) {
                return refHolder;
            }
        }
        // else
        final RefHolder refHolder = wv.findFirst(RefHolder.class, RefHolder.PROP_NAME, screenName);
        if (refHolder == null) {
            System.err.println("Can not find RefHolder/Screen:" + screenName);
        } else {
            refHolderIDMap.put(screenName, refHolder.get_Hook());
        }
        return refHolder;
    }

    static String trialPlateProtocolID = null;

    /**
     * find the protocol used in trialplate TrialPlateDAO.getTrialPlateProtocol
     * 
     * @param wv
     * @return
     */
    private Protocol getTrialPlateProtocol(final WritableVersion wv) {
        Protocol protocol = null;
        if (trialPlateProtocolID != null) {
            protocol = wv.get(trialPlateProtocolID);
        } else {
            protocol = wv.findFirst(Protocol.class, Protocol.PROP_NAME, TRIALPLATEPROTOCOLNAME);
            assert protocol != null : "Can not find reference protocol: " + TRIALPLATEPROTOCOLNAME;
            trialPlateProtocolID = protocol.get_Hook();
        }
        return protocol;
    }

    static String trialPlateExpTypeID = null;

    /**
     * find the trialPLate experiment type TrialPlateDAO.getTrialPlateExpType
     * 
     * @param wv
     * @return
     */
    private Collection<ExperimentType> getTrialPlateExpType(final WritableVersion wv) {
        ExperimentType et = null;
        if (trialPlateExpTypeID != null) {
            et = wv.get(trialPlateExpTypeID);
        } else {
            et = wv.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, TRIALPLATEEXPTYPENAME);
            assert et != null : "Can not find reference experiment type: " + TRIALPLATEEXPTYPENAME;
            trialPlateExpTypeID = et.get_Hook();
        }
        final Collection<ExperimentType> ets = new HashSet<ExperimentType>();
        ets.add(et);
        return ets;
    }

    static String trialPlateHolderCatergoryID = null;

    private Collection<HolderCategory> getTrialPlateHolerCatergory(final WritableVersion wv) {
        HolderCategory hc = null;
        if (trialPlateHolderCatergoryID != null) {
            hc = wv.get(trialPlateHolderCatergoryID);
        } else {
            hc = wv.findFirst(HolderCategory.class, HolderCategory.PROP_NAME, TRIALPLATEHOLDERCATEGORYNAME);
            assert hc != null : "Can not find reference HolderCategory: " + TRIALPLATEHOLDERCATEGORYNAME;
            trialPlateHolderCatergoryID = hc.get_Hook();
        }
        final Collection<HolderCategory> hcs = new HashSet<HolderCategory>();
        hcs.add(hc);
        return hcs;
    }

    static String trialDropSampleCategoryID = null;

    /**
     * <p>
     * Find the HolderType for the specified PlateType.
     * </p>
     * 
     * TODO Split out into PlateTypeDAO
     * 
     * @param plateType
     * @param rv
     * @return
     * @throws BusinessException
     */
    static Map<String, String> holderTypeIDMap = new HashMap<String, String>();

    private CrystalType findorCreateHolderType(final PlateType plateType, final WritableVersion wv)
        throws BusinessException {
        if (holderTypeIDMap.keySet().contains(plateType.getName())) {
            final CrystalType type = wv.get(holderTypeIDMap.get(plateType.getName()));
            if (type != null) {
                return type;
            }
        }

        CrystalType type = wv.findFirst(CrystalType.class, CrystalType.PROP_NAME, plateType.getName());

        if (null == type) {
            try {
                final Map<String, Object> attr = new HashMap<String, Object>();
                attr.put(HolderType.PROP_NAME, plateType.getName());
                attr.put(HolderType.PROP_MAXCOLUMN, plateType.getColumns());
                attr.put(HolderType.PROP_MAXROW, plateType.getRows());
                attr.put(HolderType.PROP_MAXSUBPOSITION, plateType.getSubPositions());
                attr.put(CrystalType.PROP_RESSUBPOSITION, plateType.getReservoir());
                type = wv.create(CrystalType.class, attr);
            } catch (final AccessException e) {
                throw new BusinessException(e);
            } catch (final ConstraintException e) {
                throw new BusinessException(e);
            }
        }
        holderTypeIDMap.put(plateType.getName(), type.get_Hook());
        return type;

    }

    public static TrialPlate getSimplePlate(final Holder holder) {
        final TrialPlate trialPlate = new TrialPlate(null);
        trialPlate.setBarcode(holder.getName());
        trialPlate.setCreateDate(holder.getStartDate());
        trialPlate.setDescription(holder.getDetails());
        trialPlate.setId(holder.getDbId());
        trialPlate.setDestroyDate(holder.getEndDate());
        return trialPlate;
    }

    public void update(final TrialPlate trialPlate, final WritableVersion wv) throws BusinessException {
        final Holder holder = getHolder(trialPlate, wv);
        if (holder == null) {
            throw new BusinessException("Can not find plate for update:" + trialPlate);
        }
        try {
            holder.setDetails(trialPlate.getDescription());
            holder.setName(trialPlate.getBarcode());
            holder.setStartDate(trialPlate.getCreateDate());
            holder.setEndDate(trialPlate.getDestroyDate());
            // update screen
            final Screen newScreen = trialPlate.getScreen();
            if (newScreen != null) {
                if (holder.getRefHolderOffsets().isEmpty()
                    || !holder.getRefHolderOffsets().iterator().next().getRefHolder().getName()
                        .equals(newScreen.getName())) {
                    final ScreenDAO screenDAO = new ScreenDAO(wv);
                    final RefHolder newRefHolder = screenDAO.getPO(newScreen);
                    setRefHolder(wv, holder, newRefHolder, null);
                }
            }
            // update group
            final Group newGroup = trialPlate.getGroup();
            if (newGroup != null) {
                LabNotebook newOwner = null;
                final UserGroup ug = wv.findFirst(UserGroup.class, UserGroup.PROP_NAME, newGroup.getName());
                for (final Permission permission : ug.getPermissions()) {
                    if (permission.getOpType().equals(PIMSAccess.CREATE)) {
                        newOwner = permission.getAccessObject();
                    }

                }

                if (holder.getAccess() == null || !holder.getAccess().equals(newOwner)) {
                    // TODO update expGroup, exp, os,is,sample
                    holder.setAccess(newOwner);
                }
            }
            // update creator
            final Person creator = trialPlate.getOwner();
            final PersonDAO personDAO = new PersonDAO(wv);
            final User pOwner = personDAO.getUser(creator);
            // no longer permitted holder.setCreator(pOwner);
            // update operator
            final Person operator = trialPlate.getOperator();
            if (operator != null) {
                final User pOperator = personDAO.getUser(operator);
                for (final Sample sample : holder.getSamples()) {
                    sample.getOutputSample().getExperiment().setOperator(pOperator);
                    // no longer permitted
                    // sample.getOutputSample().getExperiment().setCreator(pOwner);
                }
            }

            // we need to set mother liquor and additive screen. These may be
            // slow - see
            // http://www.redhat.com/docs/en-US/JBoss_Hibernate/3.2.4.sp01.cp03/html/Reference_Guide/Batch_processing-DML_style_operations.html
            // TODO trialPlate.getMotherLiquor();
            // TODO trialPlate.isAdditiveScreen();
        } catch (final ConstraintException e) {
            throw new BusinessException(e);
        }

    }

    private void setRefHolder(final WritableVersion wv, final Holder holder, final RefHolder newRefHolder,
        final Integer resSubPosition) throws ConstraintException {
        Integer resSubPos = null;
        if (null == resSubPosition) {
            AbstractHolderType type = holder.getHolderType();
            if (type instanceof CrystalType) {
                resSubPos = ((CrystalType) type).getResSubPosition();
            }
        } else {
            resSubPos = new Integer(resSubPosition.intValue());
        }
        if (holder.getRefHolderOffsets().size() > 0) {
            RefHolderOffset rho = holder.getRefHolderOffsets().iterator().next();
            rho.setRefHolder(newRefHolder);
            rho.setColOffset(0);
            rho.setRowOffset(0);
            if (null != resSubPos) {
                rho.setSubOffset(resSubPos);
            }
        } else {
            log.debug("create RefHolderOffset");
            Map<String, Object> attr = new HashMap<String, Object>();
            attr.put(RefHolderOffset.PROP_COLOFFSET, 0);
            attr.put(RefHolderOffset.PROP_HOLDER, holder);
            attr.put(RefHolderOffset.PROP_REFHOLDER, newRefHolder);
            attr.put(RefHolderOffset.PROP_ROWOFFSET, 0);
            attr.put(RefHolderOffset.PROP_SUBOFFSET, resSubPos - 1);
            new RefHolderOffset(wv, attr);
            log.debug("done creating RefHolderOffset");
        }
    }

    private String lastSampleName = null;

    private Long lastSampleID = null;

    // TODO what if there is more than one protein sample?
    private void createOrFindProteinSample(final WritableVersion wv, final Experiment exp,
        final TrialDrop trialDrop) throws ConstraintException, BusinessException {

        // TODO how record concentration?

        InputSample is =
            TrialServiceUtils.createInputSample(wv, exp, TrialDropDAO.PURIFIED_PROTEIN, null, 0f);
        if (!trialDrop.getSamples().isEmpty()) {
            final SampleQuantity xSample = trialDrop.getSamples().iterator().next();
            String name = xSample.getSample().getName();
            Sample sample = null;
            if (lastSampleName != null && name.equals(lastSampleName)) {
                sample = wv.get(lastSampleID);
            } else {
                sample = wv.findFirst(Sample.class, Sample.PROP_NAME, name);
                if (sample != null) {
                    lastSampleName = name;
                    lastSampleID = sample.getDbId();
                }
            }
            if (sample == null) {
                final SampleDAO sampleDAO = new SampleDAO(wv);
                sample = sampleDAO.createPO(xSample.getSample());
                sample.setCurrentAmount(new Float(xSample.getQuantity())); // TODO
                                                                           // shouldn't
                                                                           // this
                                                                           // be
                                                                           // is.setAmount?
                sample.setAmountUnit(xSample.getUnit());
                wv.flush();
                lastSampleName = sample.getName();
                lastSampleID = sample.getDbId();
            }
            is.setSample(sample);
        }

    }

    public static Screen getRefHolder(Holder holder) throws BusinessException {
        int count = holder.getRefHolderOffsets().size();
        assert count <= 1 : "Too many screens for plate: " + holder.getName();
        if (0 == count) {
            return null;
        }
        RefHolder refHolder = holder.getRefHolderOffsets().iterator().next().getRefHolder();
        final ScreenDAO screenDAO = new ScreenDAO(holder.get_Version());
        return screenDAO.getScreen(refHolder);
    }

    public static Holder getHolder(final TrialPlate trialPlate, final WritableVersion wv) {
        Holder holder = null;
        if (trialPlate.getId() != null) {
            holder = wv.get(Holder.class, trialPlate.getId());
        }
        if (holder == null) {
            holder = wv.findFirst(Holder.class, Holder.PROP_NAME, trialPlate.getBarcode());
        }
        return holder;
    }

}
