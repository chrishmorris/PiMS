package org.pimslims.crystallization.implementation;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.ConditionQuantity;
import org.pimslims.business.crystallization.model.SampleQuantity;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.TrialPlateDAO;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;

public class TrialDropServiceImpl extends BaseServiceImpl {

    private final SampleServiceImpl sampleService;

    public TrialDropServiceImpl(final DataStorage baseStorage) {
        super(baseStorage);
        sampleService = new SampleServiceImpl(baseStorage);

    }

    /**
     * This create should only be used once for each triaDrop
     * 
     * @param trialDrop
     * @throws BusinessException
     */
    TrialDrop create(final TrialDrop trialDrop) throws BusinessException {
        //create a pims' sample for trialDrop
        try {
            //create by trialDrop name
            final org.pimslims.model.sample.Sample pimsSample =
                new org.pimslims.model.sample.Sample(getWritableVersion(), getTrialDropName(trialDrop));
            //set well position
            if (trialDrop.getWellPosition() != null) {
                pimsSample.setRowPosition(trialDrop.getWellPosition().getRow());
                pimsSample.setColPosition(trialDrop.getWellPosition().getColumn());
                pimsSample.setSubPosition(trialDrop.getWellPosition().getSubPosition());
            }

            //set pims' sample name and ID back to trialDrop
            trialDrop.setId(pimsSample.getDbId());
            trialDrop.setName(pimsSample.getName());
            //create SampleQuantity
            createSampleQuantities(trialDrop);

            //add into trialPlate
            if (trialDrop.getPlate() != null) {
                final Holder holder =
                    version.findFirst(Holder.class, Holder.PROP_NAME, trialDrop.getPlate().getBarcode());
                holder.addSample(pimsSample);
            }
            return trialDrop;
        } catch (final ConstraintException e) {
            // invalid values, e.g. name already exists
            throw new BusinessException(e);
        }
    }

    /**
     * This will create SampleQuantity of a trialDrop, should only be used once for each triaDrop and pims'
     * sample has been created.
     * 
     * @param trialDrop
     * @return
     * @throws ConstraintException
     * @throws BusinessException
     */
    List<SampleQuantity> createSampleQuantities(final TrialDrop trialDrop) throws BusinessException {
        if (trialDrop.getSamples() == null || trialDrop.getSamples().isEmpty()) {
            return trialDrop.getSamples();
        }
        final org.pimslims.model.sample.Sample pimsSample = getVersion().get(trialDrop.getId());
        if (pimsSample.getOutputSample() != null) {
            throw new RuntimeException("SampleQuantity has been created for trialDrop:" + trialDrop);
        }

        //prepare experiment attributes
        //using trialDrop's name as exp name TODO is it ok?
        final String expName = getTrialDropName(trialDrop);
        //exp type is "Crystallogenesis" TODO is it ok?
        final org.pimslims.model.reference.ExperimentType expType =
            getVersion().findFirst(org.pimslims.model.reference.ExperimentType.class,
                org.pimslims.model.reference.ExperimentType.PROP_NAME,
                PlateExperimentServiceImpl.EXPERIMENT_TYPE);
        //exp date
        final Calendar expdate = Calendar.getInstance();

        org.pimslims.model.experiment.Experiment pimsExp;
        try {
            //create experiment and outputSample
            pimsExp =
                new org.pimslims.model.experiment.Experiment(getWritableVersion(), expName, expdate, expdate,
                    expType);
            final org.pimslims.model.experiment.OutputSample outputSample =
                new org.pimslims.model.experiment.OutputSample(getWritableVersion(), pimsExp);

            //link outputSample with trialDrop sample
            outputSample.setSample(pimsSample);

        } catch (final ConstraintException e) {
            throw new BusinessException(e);
        }
        //create sample and inputSample for SampleQuantity and link with same  trialDrop sample through experiment
        for (final SampleQuantity sq : trialDrop.getSamples()) {
            createSampleQuantity(sq, pimsExp);
        }
        return trialDrop.getSamples();
    }

    /**
     * create a xtalPims SampleQuantity which will link with pims' Experiment
     * 
     * @param sq
     * @param pimsExp
     * @throws BusinessException
     * @throws ConstraintException
     */
    void createSampleQuantity(final SampleQuantity sq, final Experiment pimsExp) throws BusinessException {

        if (-1L == sq.getSample().getId()) {
            // create a sample for SampleQuantity
            sampleService.create(sq);
        }
        final org.pimslims.model.sample.Sample protein = getVersion().get(sq.getSample().getId());

        try {
            TrialServiceUtils.createInputSample(getWritableVersion(), pimsExp, SampleServiceImpl.PROTEIN,
                protein, new Float(sq.getQuantity()));
        } catch (ConstraintException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * get name of trialDrop if empty generate one
     * 
     * @param trialDrop
     * @return
     */
    private String getTrialDropName(final TrialDrop trialDrop) {
        if (trialDrop.getName() != null && trialDrop.getName().length() > 0) {
            return trialDrop.getName();
        }
        return "TrialDrop" + System.currentTimeMillis();
    }

    /**
     * @TODO REQUIRED
     * @param trialDrop
     * @throws BusinessException
     */
    public void updateTrialDrop(final TrialDrop trialDrop) throws BusinessException {
        assert null != trialDrop.getId() && -1L != trialDrop.getId().longValue() : "Updating drop, but not previously saved";
        final org.pimslims.model.sample.Sample pimsSample = getVersion().get(trialDrop.getId());
        if (pimsSample.getOutputSample() == null) {
            throw new RuntimeException("Trial drop is not yet in database" + trialDrop.getName());
        }
        Experiment pimsExp = pimsSample.getOutputSample().getExperiment();
        try {
            this.getWritableVersion().delete(pimsExp.getInputSamples()); //TODO no, just is.setSample(null)
            for (final SampleQuantity sq : trialDrop.getSamples()) {
                createSampleQuantity(sq, pimsExp);
            }
        } catch (AccessException e) {
            throw new BusinessException(e);
        } catch (ConstraintException e) {
            throw new BusinessException(e);
        }
    }

    public TrialDrop findTrialDrop(final TrialPlate plate, final WellPosition wellPosition) {
        return plate.getTrialDrop(wellPosition);
    }

    /**
     * 
     * @param version
     * @param barcode
     * @return
     * @throws BusinessException static List<TrialDrop> getWells(final ReadableVersion version, final String
     *             barcode) throws BusinessException { final Holder holder =
     *             TrialServiceUtils.getHolder(version, barcode); final
     *             Collection<org.pimslims.model.sample.Sample> samples = holder.getSamples(); final
     *             List<TrialDrop> ret = new ArrayList<TrialDrop>(samples.size()); for (final
     *             org.pimslims.model.sample.Sample sample : samples) { ret.add(getWell(sample)); } return
     *             ret; }
     */

    /**
     * @param output the output sample of a crystallization experiment
     * @return a TrialDrop bean representing the experiment
     * @throws BusinessException
     */
    static TrialDrop getWell(final org.pimslims.model.sample.Sample output) throws BusinessException {
        final TrialDrop well = new TrialDrop();
        final WellPosition position;
        if (null != output.getSubPosition()) {
            position =
                new WellPosition(output.getRowPosition(), output.getColPosition(), output.getSubPosition());
        } else {
            position = new WellPosition(output.getRowPosition(), output.getColPosition());
        }
        well.setWellPosition(position);
        well.setId(output.getDbId());
        well.setName(output.getName());

        final OutputSample os = output.getOutputSample();
        if (null != os) {
            final Experiment experiment = os.getExperiment();
            Parameter parm =
                experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME,
                    TrialServiceImpl.ADDITIVE_SCREEN);
            if (null != parm) {
                well.setAdditiveScreen(Boolean.valueOf(parm.getValue()));
            }
            final Collection<InputSample> inputSamples =
                experiment.findAll(Experiment.PROP_INPUTSAMPLES, InputSample.PROP_NAME,
                    SampleServiceImpl.PROTEIN); //TODO TrialDropDAO.PURIFIED_PROTEIN
            for (final InputSample is : inputSamples) {
                if (null != is && null != is.getSample()) {
                    final SampleQuantity xSample = SampleServiceImpl.getXtalSampleQuantity(is.getSample());
                    well.addSample(xSample);
                }
            }
            InputSample is =
                experiment.findFirst(Experiment.PROP_INPUTSAMPLES, InputSample.PROP_NAME,
                    TrialServiceImpl.MOTHER_LIQUOR);
            if (null != is) {
                well.setReservoir(TrialServiceImpl.getCondition(is));
            }

            Screen screen = null;
            if (null != output.getHolder()) {
                screen = TrialPlateDAO.getRefHolder(output.getHolder());
            }
            if (well.isAdditiveScreen()) {
                well.setReservoir(well.getMotherLiquor());
            } else if (null != screen) {
                Condition condition = screen.getCondition(position);
                well.setReservoir(new ConditionQuantity(condition, 1.0d, "ml/ml"));
                //TODO mix in the mother liquor if any
            }

        }
        return well;
    }

    public Set<TrialDrop> findTrialDrops(final Sample sample, final BusinessCriteria criteria) {
        throw new UnsupportedOperationException("This method is not implemented");

    }

    public Set<TrialDrop> findTrialDrops(final TrialPlate plate, final BusinessCriteria criteria) {
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public Set<TrialDrop> findTrialDrops(final Sample sample, final TrialPlate plate,
        final BusinessCriteria criteria) {
        throw new UnsupportedOperationException("This method is not implemented");
    }

    public Collection<TrialDrop> findTrialDrops(final Sample sample, final Collection<Condition> conditions,
        final BusinessCriteria criteria) {
        throw new UnsupportedOperationException("This method is not implemented");
    }
}
