/**
 * 
 */
package org.pimslims.crystallization.implementation;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.business.crystallization.model.PlateExperimentInfo;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.AbstractModelObject;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.people.Group;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.model.target.ResearchObjective;

/**
 * @author Jon Diprose
 */
public class TrialServiceUtils {

	/**
	 * <p>
	 * Find a Sample by its [row, col, sub]-position in a Holder. This method
	 * hits the database.
	 * </p>
	 * 
	 * @param rv
	 *            - the ReadableVersion to query
	 * @param holder
	 *            - the Sample's Holder
	 * @param wellPosition
	 *            - the Sample's WellPosition
	 * @return The Sample at the specified position in the specified Holder
	 */
	public static Sample getSampleByHolderPosition(ReadableVersion rv,
			AbstractModelObject holder, WellPosition wellPosition) {

		Map<String, Object> attr = new HashMap<String, Object>();
		attr.put(org.pimslims.model.sample.Sample.PROP_HOLDER, holder);
		attr.put(org.pimslims.model.sample.Sample.PROP_ROWPOSITION,
				new Integer(wellPosition.getRow()));
		attr.put(org.pimslims.model.sample.Sample.PROP_COLPOSITION,
				new Integer(wellPosition.getColumn()));
		attr.put(org.pimslims.model.sample.Sample.PROP_SUBPOSITION,
				new Integer(wellPosition.getSubPosition()));
		return rv.findFirst(org.pimslims.model.sample.Sample.class, attr);

	}

	/**
	 * <p>
	 * Find a Sample by its [row, col, sub]-position in a Holder. This method
	 * only hits database if holder.getSamples() is not initialised.
	 * </p>
	 * 
	 * @param holder
	 * @param wellPosition
	 * @return
	 */
	public static Sample getSampleByHolderPosition(Holder holder,
			WellPosition wellPosition) {
		return getSampleByHolderPosition(holder.getSamples(), wellPosition);
	}

	/**
	 * <p>
	 * Find a Sample by its [row, col, sub]-position in a Holder. This method
	 * should not hit the database.
	 * </p>
	 * 
	 * @param holder
	 * @param wellPosition
	 * @return
	 */
	public static Sample getSampleByHolderPosition(Collection<Sample> samples,
			WellPosition wellPosition) {

		for (org.pimslims.model.sample.Sample s : samples) {
			if ((wellPosition.getRow() == s.getRowPosition())
					&& (wellPosition.getColumn() == s.getColPosition())
					&& (wellPosition.getSubPosition() == s.getSubPosition())) {

				return s;

			}
		}

		// TODO Throw an exception instead?
		// throw new RuntimeException("No sample at " + wellPosition);
		return null;

	}

	/**
	 * <p>
	 * Get a PiMS Sample for the specified xtalPiMS Sample.
	 * </p>
	 * 
	 * @param rv
	 * @param sample
	 * @return
	 */
	public static Sample getSample(ReadableVersion rv,
			org.pimslims.business.core.model.Sample sample) {

		if (sample.getId() > 0) {
			return rv.get(sample.getId());
		} else if (null != sample.getName()) {
			return rv.findFirst(org.pimslims.model.sample.Sample.class,
					org.pimslims.model.sample.Sample.PROP_NAME,
					sample.getName());
		}

		throw new IllegalArgumentException("sample must have valid id or name");

	}

	/**
	 * 
	 * @param wv
	 * @param name
	 * @param holder
	 * @param wellPosition
	 * @param refSample
	 * @param volume
	 * @param unit
	 * @return
	 * @throws ConstraintException
	 */
	public static Sample createSample(WritableVersion wv, String name,
			AbstractModelObject holder, WellPosition wellPosition,
			RefSample refSample, Float volume, String unit)
			throws ConstraintException {

		// Create trial Sample
		Map<String, Object> attr = new HashMap<String, Object>();
		attr.put(org.pimslims.model.sample.Sample.PROP_AMOUNTDISPLAYUNIT, unit);
		attr.put(org.pimslims.model.sample.Sample.PROP_AMOUNTUNIT, unit);
		attr.put(org.pimslims.model.sample.Sample.PROP_COLPOSITION,
				wellPosition.getColumn());
		attr.put(org.pimslims.model.sample.Sample.PROP_CURRENTAMOUNT, volume);
		attr.put(org.pimslims.model.sample.Sample.PROP_HOLDER, holder);
		attr.put(org.pimslims.model.sample.Sample.PROP_INITIALAMOUNT, volume);
		attr.put(org.pimslims.model.sample.Sample.PROP_REFSAMPLE, refSample);
		attr.put(org.pimslims.model.sample.Sample.PROP_ROWPOSITION,
				wellPosition.getRow());
		attr.put(org.pimslims.model.sample.Sample.PROP_SUBPOSITION,
				wellPosition.getSubPosition());
		attr.put(org.pimslims.model.sample.Sample.PROP_NAME, name);
		return new org.pimslims.model.sample.Sample(wv, attr);

	}

	/**
	 * <p>
	 * Create an attribute map for an Experiment.
	 * </p>
	 * 
	 * @param wv
	 * @param name
	 * @param eg
	 * @param ei
	 * @return
	 * @throws ConstraintException
	 */
	// JMD Used
	public static Map<String, Object> createExperimentAttributeMap(
			WritableVersion wv, ExperimentGroup eg, PlateExperimentInfo ei)
			throws ConstraintException {

		// Find creator
		User creator = findPerson(wv, ei.getOperator());
		if (null == creator) {
			throw new IllegalArgumentException("no such operator: "
					+ ei.getOperator().getUsername());
		}

		// Find the protocol for this experimentInfo
		Protocol protocol = wv.findFirst(Protocol.class, Protocol.PROP_NAME,
				ei.getProtocolName());
		if (null == protocol) {
			throw new IllegalArgumentException("no such protocol: "
					+ ei.getProtocolName());
		}

		// Find instrument
		Instrument instrument = null;
		if (null != ei.getInstrument()) {
			instrument = wv.findFirst(Instrument.class, Instrument.PROP_NAME,
					ei.getInstrument());
		}

		// Find ExpBlueprint
		ResearchObjective eb = null;
		if (null != ei.getExpBlueprintName()) {
			eb = wv.findFirst(ResearchObjective.class,
					ResearchObjective.PROP_LOCALNAME, ei.getExpBlueprintName());
		}

		// Find Group
		Group group = null;
		if (null != ei.getGroupName()) {
			group = wv.findFirst(Group.class, Group.PROP_NAME,
					ei.getGroupName());
		}

		// Create production Experiment
		Map<String, Object> attr = new HashMap<String, Object>();
		attr.put(Experiment.PROP_CREATOR, creator);
		attr.put(Experiment.PROP_DETAILS, ei.getDetails());
		attr.put(Experiment.PROP_ENDDATE, ei.getRunAt());
		attr.put(Experiment.PROP_RESEARCHOBJECTIVE, eb);
		attr.put(Experiment.PROP_EXPERIMENTGROUP, eg);
		attr.put(Experiment.PROP_EXPERIMENTTYPE, protocol.getExperimentType());
		attr.put(Experiment.PROP_GROUP, group);
		attr.put(Experiment.PROP_INSTRUMENT, instrument);
		attr.put(Experiment.PROP_PROTOCOL, protocol);
		attr.put(Experiment.PROP_STARTDATE, ei.getRunAt());
		attr.put(Experiment.PROP_STATUS, "To_be_run");

		return attr;

	}

	/**
	 * <p>
	 * Create an Experiment.
	 * </p>
	 * 
	 * @param wv
	 * @param name
	 * @param eg
	 * @param ei
	 * @return
	 * @throws ConstraintException
	 */
	// JMD Used
	public static Experiment createExperiment(WritableVersion wv, String name,
			Map<String, Object> attr, PlateExperimentInfo ei)
			throws ConstraintException {

		// Create production Experiment
		Map<String, Object> eattr = new HashMap<String, Object>(attr);
		eattr.put(Experiment.PROP_NAME, name);

		Experiment expt = new Experiment(wv, eattr);

		createParameters(wv, expt, ei);

		return expt;

	}

	/**
	 * <p>
	 * Create the Parameters for the specified experiment.
	 * </p>
	 * 
	 * @param wv
	 * @param expt
	 * @param ei
	 * @throws ConstraintException
	 */
	// JMD Used
	public static void createParameters(WritableVersion wv, Experiment expt,
			PlateExperimentInfo ei) throws ConstraintException {

		for (ParameterDefinition pd : expt.getProtocol()
				.getParameterDefinitions()) {

			Map<String, Object> attr = new HashMap<String, Object>();
			attr.put(Parameter.PROP_EXPERIMENT, expt);
			attr.put(Parameter.PROP_NAME, pd.getName());
			attr.put(Parameter.PROP_PARAMETERDEFINITION, pd);
			attr.put(Parameter.PROP_PARAMTYPE, pd.getParamType());
			attr.put(Parameter.PROP_VALUE, ei.getParameter(pd.getName()));

			new Parameter(wv, attr);

		}

	}

	/**
	 * <p>
	 * Find a PiMS person from an xtalPiMS person.
	 * </p>
	 * 
	 * @param rv
	 * @param p
	 * @return
	 */
	private static User findPerson(ReadableVersion rv,
			org.pimslims.business.core.model.Person p) {

		User u = rv.findFirst(User.class, User.PROP_NAME, p.getUsername());
		return u;

	}

	/**
	 * 
	 * @param wv
	 * @param expt
	 * @param sample
	 * @param volume
	 *            in litres
	 * @return
	 * @throws ConstraintException
	 */
	@Deprecated
	/*
	 * use faster method: createInputSample(WritableVersion wv, ExperimentGroup
	 * group, String name, Sample sample, Float volume)
	 */
	public static InputSample createInputSample(WritableVersion wv,
			Experiment expt, String name, Sample sample, Float volume)
			throws ConstraintException {

		Map<String, Object> attr = new HashMap<String, Object>();
		attr.put(InputSample.PROP_AMOUNT, volume);
		attr.put(InputSample.PROP_AMOUNTUNIT, "L");
		attr.put(InputSample.PROP_EXPERIMENT, expt);
		attr.put(InputSample.PROP_NAME, name);
		if (null != sample) {
			attr.put(InputSample.PROP_SAMPLE, sample);
		}

		// Are these required/useful?
		// attr.put(InputSample.PROP_AMOUNTDISPLAYUNIT, unit);
		// attr.put(InputSample.PROP_ROLE, role);

		InputSample ret = new InputSample(wv, attr);
		if (null != expt.getProtocol()) {
			Protocol protocol = expt.getProtocol();
			RefInputSample ris = protocol.findFirst(
					Protocol.PROP_REFINPUTSAMPLES, RefInputSample.PROP_NAME,
					name);
			ret.setRefInputSample(ris);
		}
		return ret;
	}

	public static void createParameters(WritableVersion wv,
			ExperimentGroup group, String name, String value)
			throws ConstraintException {
		Set<Experiment> experiments = group.getExperiments();
		ParameterDefinition pd = null;
		Protocol protocol = experiments.iterator().next().getProtocol();
		if (null != protocol) {
			pd = protocol.findFirst(Protocol.PROP_PARAMETERDEFINITIONS,
					ParameterDefinition.PROP_NAME, name);
		}
		for (Iterator iterator = experiments.iterator(); iterator.hasNext();) {
			Experiment experiment = (Experiment) iterator.next();

			Map<String, Object> attr = new HashMap<String, Object>();
			attr.put(Parameter.PROP_EXPERIMENT, experiment);
			attr.put(Parameter.PROP_NAME, name);
			attr.put(Parameter.PROP_VALUE, value);
			new Parameter(wv, attr).setParameterDefinition(pd);
		}
		/*
		 * This would be quicker: but it gets: org.hibernate.QueryException: can
		 * only generate ids as part of bulk insert with either sequence or
		 * post-insert style generators
		 * 
		 * Query q = wv.getSession().createQuery(
		 * "insert into Parameter (experiment, name,   parameterDefinition) " +
		 * "select experiment,  pd.name,   pd from Experiment experiment, ParameterDefinition pd "
		 * + "where experiment.group=:group   and pd=:pd"); q.setEntity("pd",
		 * pd); q.setEntity("group", group); int count = q.executeUpdate();
		 * assert 96 == count; q = wv.getSession().createQuery(
		 * "update Parameter set value=:value  " +
		 * "where parameter.experiment.group=:group   and pd=:pd");
		 * q.setParameter("value", value); q.setEntity("pd", pd);
		 * q.setEntity("group", group); count = q.executeUpdate(); assert 96 ==
		 * count;
		 */

	}

	/**
	 * 
	 * @param wv
	 * @param expt
	 * @param sample
	 * @param role
	 * @param volume
	 * @param unit
	 * @return
	 * @throws ConstraintException
	 */
	// JMD Used
	public static OutputSample createOutputSample(WritableVersion wv,
			Experiment expt, String name, Sample sample, Float volume,
			String unit) throws ConstraintException {

		// Create OutputSample
		Map<String, Object> attr = new HashMap<String, Object>();
		attr.put(OutputSample.PROP_AMOUNT, volume);
		attr.put(OutputSample.PROP_AMOUNTUNIT, unit);
		attr.put(OutputSample.PROP_EXPERIMENT, expt);
		attr.put(OutputSample.PROP_NAME, name);
		attr.put(OutputSample.PROP_SAMPLE, sample);

		// Are these required/useful?
		// attr.put(OutputSample.PROP_AMOUNTDISPLAYUNIT, unit);
		// attr.put(OutputSample.PROP_ROLE, role);

		RefOutputSample ros = null;
		if (null != expt.getProtocol()) {
			Protocol protocol = expt.getProtocol();
			ros = protocol.findFirst(Protocol.PROP_REFOUTPUTSAMPLES,
					RefOutputSample.PROP_NAME, name);
			if (null != ros.getSampleCategory()) {
				sample.addSampleCategory(ros.getSampleCategory());
			}
		}
		attr.put(OutputSample.PROP_REFOUTPUTSAMPLE, ros);
		return new OutputSample(wv, attr);

	}

	/**
	 * 
	 * @param wv
	 * @param name
	 * @param purpose
	 * @param runAt
	 * @return
	 * @throws ConstraintException
	 */
	public static ExperimentGroup createExperimentGroup(WritableVersion wv,
			String name, String purpose, Calendar runAt)
			throws ConstraintException {

		// Create ExperimentGroup
		Map<String, Object> attr = new HashMap<String, Object>();
		attr.put(ExperimentGroup.PROP_DETAILS, null);
		attr.put(ExperimentGroup.PROP_ENDDATE, runAt);
		attr.put(ExperimentGroup.PROP_NAME, name);
		attr.put(ExperimentGroup.PROP_PURPOSE, purpose);
		attr.put(ExperimentGroup.PROP_STARTDATE, runAt);
		return new ExperimentGroup(wv, attr);

	}

	/**
	 * 
	 * @param wv
	 * @param inputSamples
	 * @param sample
	 * @param finalVolume
	 * @throws ConstraintException
	 */
	// JMD Used
	public static void copySampleComponents(WritableVersion wv,
			List<InputSample> inputSamples, Sample sample, double finalVolume)
			throws ConstraintException {

		// Copy SampleComponents from InputSamples to output Sample
		for (InputSample is : inputSamples) {

			org.pimslims.model.sample.Sample s = is.getSample();

			for (SampleComponent sc : s.getSampleComponents()) {
				Map<String, Object> attr = sc.get_Values();
				attr.put(SampleComponent.PROP_ABSTRACTSAMPLE, sample);
				attr.put(SampleComponent.PROP_CONCENTRATION, new Float(sc
						.getConcentration().floatValue()
						* is.getAmount().floatValue() / (float) finalVolume));
				new SampleComponent(wv, attr);
			}

		}

	}

	/**
	 * 
	 * @param v
	 * @param barcode
	 * @return
	 */
	static AbstractModelObject getHolder(ReadableVersion v, String barcode) {
		return v.findFirst(Holder.class, Holder.PROP_NAME, barcode);
	}

	public static void createInputSample(WritableVersion wv,
			ExperimentGroup group, String name, Sample sample, Float volume)
			throws ConstraintException, AccessException {

		Map<String, Object> attr = new HashMap<String, Object>();
		attr.put(InputSample.PROP_AMOUNT, volume);
		attr.put(InputSample.PROP_AMOUNTUNIT, "L");
		attr.put(InputSample.PROP_NAME, name);
		if (null != sample) {
			attr.put(InputSample.PROP_SAMPLE, sample);
		}

		// Are these required/useful?
		// attr.put(InputSample.PROP_AMOUNTDISPLAYUNIT, unit);
		// attr.put(InputSample.PROP_ROLE, role);

		Set<Experiment> experiments = group.getExperiments();
		Experiment expt = experiments.iterator().next();
		if (null != expt.getProtocol()) {
			Protocol protocol = expt.getProtocol();
			RefInputSample ris = protocol.findFirst(
					Protocol.PROP_REFINPUTSAMPLES, RefInputSample.PROP_NAME,
					name);
			attr.put(InputSample.PROP_REFINPUTSAMPLE, ris);
		}
		for (Iterator iterator = experiments.iterator(); iterator.hasNext();) {
			Experiment experiment = (Experiment) iterator.next();
			ModelObject is = experiment.findFirst(Experiment.PROP_INPUTSAMPLES,
					InputSample.PROP_NAME, name);
			if (null == is) {
				attr.put(InputSample.PROP_EXPERIMENT, experiment);
				InputSample ret = new InputSample(wv, attr);
			} else {
				is.set_Values(attr);
			}
		}
		;
	}

}
