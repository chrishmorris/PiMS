package org.pimslims.crystallization.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.model.Project;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.core.model.Target;
import org.pimslims.business.core.service.SampleService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.SampleQuantity;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.crystallization.view.SampleQuantityView;
import org.pimslims.business.crystallization.view.SampleView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.ConstructDAO;
import org.pimslims.crystallization.dao.PersonDAO;
import org.pimslims.crystallization.dao.SampleDAO;
import org.pimslims.crystallization.dao.TrialDropDAO;
import org.pimslims.crystallization.dao.view.SampleViewDAO;
import org.pimslims.crystallization.dao.view.ViewDAO;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.AbstractModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.target.ResearchObjective;

public class SampleServiceImpl extends BaseServiceImpl implements SampleService {

	public static final String PROTEIN = TrialDropDAO.PURIFIED_PROTEIN;

	private final PersonDAO personDAO;

	private final SampleDAO sampleDAO;

	/**
	 * 
	 * @param dataStorage
	 */
	public SampleServiceImpl(final DataStorage dataStorage) {
		super(dataStorage);
		personDAO = new PersonDAO(getVersion());
		sampleDAO = new SampleDAO(version);
	}

	/**
	 * 
	 * @param sampleId
	 * @throws org.pimslims.business.exception.BusinessException
	 * @return
	 */
	public Sample find(final long sampleId) throws BusinessException {
		return sampleDAO.getFullXO((org.pimslims.model.sample.Sample) version
				.get(sampleId));
	}

	/**
	 * 
	 * @param sampleId
	 * @throws org.pimslims.business.exception.BusinessException
	 * @return
	 */
	public Sample find(final String sampleId) throws BusinessException {
		return sampleDAO.getFullXO((org.pimslims.model.sample.Sample) version
				.get(sampleId));
	}

	/**
	 * 
	 * @param name
	 * @throws org.pimslims.business.exception.BusinessException
	 * @return
	 */
	public Sample findByName(final String name) throws BusinessException {
		return sampleDAO.findByName(name);
	}

	public static SampleQuantity getXtalSampleQuantity(
			final org.pimslims.model.sample.Sample pimsSample)
			throws BusinessException {
		if (pimsSample == null) {
			return null;
		}
		final Sample xSample = (new SampleDAO(pimsSample.get_Version()))
				.getFullXO(pimsSample);
		double quantity = 0D;
		if (null != pimsSample.getCurrentAmount()) {
		    quantity = pimsSample.getCurrentAmount().doubleValue();
		    if (0D > quantity) {
		        quantity = 0D;
		    }
		}
        String unit = "L";
        if (null != pimsSample.getAmountUnit()) {
            unit = pimsSample.getAmountUnit();
        }
		final SampleQuantity sq = new SampleQuantity(xSample, quantity,
				unit);
		return sq;
	}

	/**
	 * 
	 * @param scientist
	 * @throws org.pimslims.business.exception.BusinessException
	 * @return
	 */
	public Collection<Sample> findByUser(final Person scientist,
			final BusinessCriteria criteria) throws BusinessException {
		final User pimsScientist = personDAO.getUser(scientist);
		final Collection<ResearchObjective> expBlueprints = pimsScientist
				.getResearchObjectives();
		final Collection<Sample> ret = new HashSet<Sample>();
		for (final ResearchObjective eb : expBlueprints) {
			ret.addAll(ConstructServiceImpl.getSamples(eb));
		}
		return ret;
	}

	/**
	 * 
	 * @param group
	 * @throws org.pimslims.business.exception.BusinessException
	 * @return
	 */
	public List<Sample> findByGroup(final Group group,
			final BusinessCriteria criteria) throws BusinessException {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * 
	 * @param construct
	 * @throws org.pimslims.business.exception.BusinessException
	 * @return
	 */
	public List<Sample> findByConstruct(final Construct construct,
			final BusinessCriteria criteria) throws BusinessException {
		// TODO implement
		throw new UnsupportedOperationException(
				"This method is not implemented");
	}

	/**
	 * 
	 * @param project
	 * @throws org.pimslims.business.exception.BusinessException
	 * @return
	 */
	public List<Sample> findByProject(final Project project,
			final BusinessCriteria criteria) throws BusinessException {
		// LATER implement
		throw new UnsupportedOperationException(
				"This method is not implemented");
	}

	/**
	 * 
	 * @param target
	 * @throws org.pimslims.business.exception.BusinessException
	 * @return
	 */
	public List<Sample> findByTarget(final Target target,
			final BusinessCriteria criteria) throws BusinessException {
		// LATER implement
		throw new UnsupportedOperationException(
				"This method is not implemented");
	}

	/**
	 * 
	 * @param plate
	 * @param wellPosition
	 * @throws org.pimslims.business.exception.BusinessException
	 * @return
	 */
	@Deprecated
	// believed unused
	List<Sample> findByPlateAndWell(final TrialPlate plate,
			final WellPosition wellPosition, final BusinessCriteria criteria)
			throws BusinessException {
		return findByPlateAndWell(plate.getBarcode(), wellPosition, criteria);
	}

	/**
	 * 
	 * @param plateExperiment
	 * @param wellPosition
	 * @throws org.pimslims.business.exception.BusinessException
	 * @return
	 */
	@Deprecated
	// believed unused
	List<Sample> findByPlateAndWell(final PlateExperimentView plateExperiment,
			final WellPosition wellPosition, final BusinessCriteria criteria)
			throws BusinessException {
		return findByPlateAndWell(plateExperiment.getBarcode(), wellPosition,
				criteria);
	}

	/**
	 * 
	 * @param barcode
	 * @param wellPosition
	 * @throws org.pimslims.business.exception.BusinessException
	 * @return
	 */
	@Deprecated
	// believed unused
	List<Sample> findByPlateAndWell(final String barcode,
			final WellPosition wellPosition, final BusinessCriteria criteria)
			throws BusinessException {
		final org.pimslims.model.sample.Sample output = SampleServiceImpl
				.findOutputSample(this.getVersion(), wellPosition, barcode);
		if (null == output) {
			return null;
		}
		final OutputSample os = output.getOutputSample();
		if (null == os) {
			return null;
		}
		final Experiment experiment = os.getExperiment();
		final InputSample is = experiment.findFirst(
				Experiment.PROP_INPUTSAMPLES, InputSample.PROP_NAME, PROTEIN);
		if (null == is) {
			return null;
		}
		final org.pimslims.model.sample.Sample pimsSample = is.getSample();
		if (null == pimsSample) {
			return null;
		}
		final Sample s = sampleDAO.getSimpleXO(pimsSample);
		final List<Sample> samples = new ArrayList<Sample>();
		samples.add(s);
		return samples;
	}

	/**
	 * 
	 * @param version
	 * @param wellPosition
	 * @param barcode
	 * @return
	 */
	static org.pimslims.model.sample.Sample findOutputSample(
			final ReadableVersion version, final WellPosition wellPosition,
			final String barcode) {
		final AbstractModelObject holder = TrialServiceUtils.getHolder(version, barcode);
		// holder.getSamples()
		org.pimslims.model.sample.Sample sample;
		{
			final Map<String, Object> criteria = new HashMap<String, Object>();
			criteria.put(org.pimslims.model.sample.Sample.PROP_ROWPOSITION,
					wellPosition.getRow());
			criteria.put(org.pimslims.model.sample.Sample.PROP_COLPOSITION,
					wellPosition.getColumn());
			criteria.put(org.pimslims.model.sample.Sample.PROP_SUBPOSITION,
					wellPosition.getSubPosition());
			sample = holder.findFirst(Holder.PROP_SAMPLES, criteria);
		}
		return sample;
	}

	/**
	 * 
	 * @param sample
	 * @throws org.pimslims.business.exception.BusinessException
	 * @return
	 */
	public Project getProject(final Sample sample) throws BusinessException {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * 
	 * @param sample
	 * @throws org.pimslims.business.exception.BusinessException
	 * @return
	 */
	public Target getTarget(final Sample sample) throws BusinessException {
		return this.getConstruct(sample).getTarget();
	}

	/**
	 * 
	 * @param sample
	 * @throws org.pimslims.business.exception.BusinessException
	 * @return
	 */
	public Construct getConstruct(final Sample sample) throws BusinessException {
		if (sample == null) {
			return null;
		}
		final ConstructDAO constructDAO = new ConstructDAO(version);

		final org.pimslims.model.sample.Sample pimsSample = version.findFirst(
				org.pimslims.model.sample.Sample.class,
				org.pimslims.model.sample.Sample.PROP_NAME, sample.getName());
		final OutputSample os = pimsSample.getOutputSample();
		if (null == os) {
			return null;
		}
		final ResearchObjective eb = os.getExperiment().getResearchObjective();
		if (null == eb) {
			return null;
		}
		return constructDAO.getFullXO(eb);
	}

	/**
	 * Creates a PiMS record representing a sample of soluble protein
	 * 
	 * @TODO Update
	 * @param sample
	 * @throws org.pimslims.business.exception.BusinessException
	 */
	public void create(final Sample sample) throws BusinessException {
		try {
			org.pimslims.model.sample.Sample pSample = sampleDAO
					.createPO(sample);
			if (null == pSample.getCurrentAmount()) {
				pSample.setCurrentAmount(0f);
			}
			// TODO set sample category
		} catch (ConstraintException e) {
			throw new BusinessException(e);
		}
	}

	public void create(final SampleQuantity sq) throws BusinessException {
		create(sq.getSample());
		final org.pimslims.model.sample.Sample pimsSample = getVersion().get(
				sq.getSample().getId());
		try {
			pimsSample.setAmountUnit(sq.getUnit());
			pimsSample.setCurrentAmount(new Float(sq.getQuantity()));
		} catch (final ConstraintException e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * REQUIRED
	 * 
	 * @param sample
	 * @throws org.pimslims.business.exception.BusinessException
	 */
	public void update(final Sample sample) throws BusinessException {
		sampleDAO.updatePO(sample);
	}

	/**
	 * 
	 * @param sample
	 * @throws org.pimslims.business.exception.BusinessException
	 * @return
	 */
	public void close(final Sample sample) throws BusinessException {
		try {
			final org.pimslims.model.sample.Sample pimsSample = getVersion()
					.findFirst(org.pimslims.model.sample.Sample.class,
							org.pimslims.model.sample.Sample.PROP_NAME,
							sample.getName());
			pimsSample.setIsActive(Boolean.FALSE);
		} catch (final ConstraintException e) {
			// cannot happen
			throw new RuntimeException(e);
		}
	}

	public Person getOperator(final Sample sample) throws BusinessException {
		// LATER implement
		throw new UnsupportedOperationException(
				"This method is not implemented");
	}

	public Person getOwner(final Sample sample) throws BusinessException {
		final Construct construct = this.getConstruct(sample);
		if (null == construct) {
			return null;
		}
		return construct.getOwner();
	}

	public void setConstructForSample(final Sample sample,
			final Construct construct) throws BusinessException {
		// TODO implement
		throw new UnsupportedOperationException(
				"This method is not implemented");
	}

	public void setOperatorForSample(final Sample sample, final Person person)
			throws BusinessException {
		// LATER implement
		throw new UnsupportedOperationException(
				"This method is not implemented");
	}

	/**
	 * Note that in the PiMS DM, an experiment has only one person, the
	 * operator. So we assume the researcher the crystallogenesis is for is the
	 * one who is owner of the construct.
	 * 
	 * @param scientist
	 * @see org.pimslims.crystallization.business.SampleService#setScientistForSample(org.pimslims.crystallization.Sample,
	 *      org.pimslims.crystallization.Person)
	 */
	public void setOwnerForSample(final Sample sample, final Person scientist)
			throws BusinessException {
		final Construct construct = this.getConstruct(sample);
		if (null == construct) {
			throw new IllegalStateException("You must set construct first");
		}
		try {
			final ResearchObjective eb = ConstructServiceImpl.getExpBlueprint(
					this.version, construct);
			final User pimsScientist = personDAO.getUser(scientist);
			if (null == eb.getOwner()) {
				eb.setOwner(pimsScientist);
			} else {
				if (eb.getOwner() != pimsScientist) {
					throw new IllegalStateException(
							"That construct is already owned by: "
									+ eb.getOwner().get_Name());
				}
			}
		} catch (final ConstraintException e) {
			// should not happen - Chris
			throw new RuntimeException(e);
		}

	}

	/**
	 * @param paging
	 * @return
	 * @throws org.pimslims.business.exception.BusinessException
	 */
	public Collection<Sample> findAll(final BusinessCriteria criteria)
			throws BusinessException {
		final User pimsScientist = this.version.getCurrentUser();
		final Collection<ResearchObjective> expBlueprints = pimsScientist
				.getResearchObjectives();
		final Collection<Sample> ret = new HashSet<Sample>();
		for (final ResearchObjective eb : expBlueprints) {
			ret.addAll(ConstructServiceImpl.getSamples(eb));
		}
		return ret;
	}

	/**
	 * @TODO REQUIRED
	 * @param barcode
	 * @param paging
	 * @return
	 * @throws org.pimslims.business.exception.BusinessException
	 */
	public Collection<Sample> findByPlate(final String barcode,
			final BusinessCriteria criteria) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Collection<SampleQuantityView> findSampleQuantities(
			final BusinessCriteria criteria) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Collection<SampleView> findViews(final BusinessCriteria criteria)
			throws BusinessException {
		return getViewDAO().findViews(criteria);
	}

	public Integer findViewCount(final BusinessCriteria criteria)
			throws BusinessException {
		return getViewDAO().findViewCount(criteria);
	}

	public String convertPropertyName(final String property)
			throws BusinessException {
		return getViewDAO().convertPropertyName(property);
	}

	private ViewDAO<SampleView> viewDAO;

	private ViewDAO<SampleView> getViewDAO() {
		if (viewDAO == null) {
			viewDAO = new SampleViewDAO(version);
		}
		return viewDAO;
	}
}
