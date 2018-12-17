/**
 * xtalPiMS Business API - PIMSDB Impl org.pimslims.crystallization.implementation ImagerServiceImpl.java
 * 
 * @author jon
 * @date 25 Aug 2010
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2010 jon The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.implementation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.hibernate.Query;
import org.pimslims.access.Access;
import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Project;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.ImageType;
import org.pimslims.business.crystallization.model.Imager;
import org.pimslims.business.crystallization.service.ImagerService;
import org.pimslims.business.crystallization.view.ScheduleView;
import org.pimslims.business.crystallization.view.SimpleSampleView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.SampleDAO;
import org.pimslims.crystallization.dao.view.AbstractViewDAO;
import org.pimslims.dao.FlushMode;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.schedule.SchedulePlan;
import org.pimslims.model.schedule.SchedulePlanOffset;
import org.pimslims.model.schedule.ScheduledTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ImagerServiceImpl
 * 
 */
public class ImagerServiceImpl extends BaseServiceImpl implements ImagerService {

	/**
	 * SimpleDateFormat used to generate datetime portion of the
	 * imagingID/strsessionid
	 */
	public static final String IMAGINGID_DATE_FORMAT = "yyyyMMdd-HHmmss";

	/**
	 * Constructor for ImagerServiceImpl
	 * 
	 * @param baseStorage
	 */
	public ImagerServiceImpl(final DataStorage dataStorage) {
		super(dataStorage);
	}

	/**
	 * ImagerServiceImpl.find
	 * 
	 * @see org.pimslims.business.crystallization.service.ImagerService#find(java.lang.String)
	 */
	@Deprecated
	// use Location
	public Imager find(String name) {
		Instrument instrument = this.version.findFirst(Instrument.class,
				Instrument.PROP_NAME, name);
		if (null == instrument) {
			return null;
		}
		return getImager(instrument);
	}

	@Deprecated
	// use Location
	private Imager getImager(Instrument instrument) {
		Imager ret = new Imager();
		ret.setId(instrument.getDbId());
		ret.setName(instrument.getName());

		// find name of image type
		String name = null;
		org.pimslims.model.reference.ImageType wit = instrument
				.getDefaultImageType();
		if (null == wit) {
			// obsolete
			org.pimslims.model.reference.ImageType dit = instrument
					.getDefaultImageType();
			assert null != dit : "Not an imager: " + instrument.getName();
			name = dit.getName();
		} else {
			name = wit.getName();
		}

		ImageType[] values = org.pimslims.business.crystallization.model.ImageType
				.values();
		ImageType type = null;
		for (int i = 0; i < values.length; i++) {
			if (values[i].name().equals(name)) {
				type = values[i];
			}
		}
		ret.setDefaultImageType(type);
		return ret;
	}

	/**
	 * ImagerServiceImpl.save
	 * 
	 * @throws BusinessException
	 * 
	 * @see org.pimslims.business.crystallization.service.ImagerService#save(org.pimslims.business.crystallization.model.Imager)
	 */
	@Deprecated
	// use Location
	public Imager save(Imager imager) throws BusinessException {
		try {
			Instrument instrument = new Instrument(this.getWritableVersion(),
					imager.getName());

			// TODO obsolete, can lead to disclosure of file path
			org.pimslims.model.reference.ImageType type = new org.pimslims.model.reference.ImageType(
					this.getWritableVersion(), imager.getDefaultImageType()
							.name()); // TODO or find it
			instrument.setDefaultImageType(type);

			org.pimslims.model.reference.ImageType wtype = version.findFirst(
					org.pimslims.model.reference.ImageType.class,
					org.pimslims.model.reference.ImageType.PROP_NAME, imager
							.getDefaultImageType().name());
			if (null == wtype) {
				wtype = new org.pimslims.model.reference.ImageType(
						this.getWritableVersion(), imager.getDefaultImageType()
								.name());
			}
			instrument.setDefaultImageType(wtype);
		} catch (ConstraintException e) {
			throw new BusinessException(e);
		}
		return this.find(imager.getName());
	}

	/**
	 * ImagerServiceImpl.findSchedules - Find the Collection of ScheduleView for
	 * the plate with the specified barcode on the specified robot. NB This
	 * implementation doesn't use the robot information.
	 * 
	 * @see org.pimslims.crystallization.implementation.ImagerService#findSchedules(java.lang.String,
	 *      java.lang.String)
	 */
	public Collection<ScheduleView> findSchedules(final String barcode,
			final String robot) throws BusinessException {
		final Query q = version
				.getSession()
				.createQuery(
						"from ScheduledTask st inner join st.holder h left join fetch st.instrument where h.name = :barcode order by st.scheduledTime");
		q.setString("barcode", barcode);

		final Iterator<?> rs = q.list().iterator();
		final List<ScheduledTask> sts = new ArrayList<ScheduledTask>();
		while (rs.hasNext()) {
			Object[] tuple = (Object[]) rs.next();
			sts.add((ScheduledTask) tuple[0]);
		}

		return mapScheduledTasks(sts, barcode);
	}

	/**
	 * ImagerServiceImpl.schedulePlate - Schedule a plate on its default
	 * schedule plan.
	 * 
	 * @see org.pimslims.business.crystallization.service.ImagerService#schedulePlate(java.lang.String,
	 *      java.lang.String)
	 */
	public Collection<ScheduleView> schedulePlate(final String barcode,
			final String robot) throws BusinessException {
		final Holder plate = findHolder(barcode);
		if (null == plate) {
			// No plate - nothing can be done
			throw new BusinessException("Holder " + barcode
					+ " not found - cannot schedule");
		}
		final HolderType ht = (HolderType) plate.getHolderType();
		if (null == ht) {
			// No plate - nothing can be done
			throw new BusinessException("Holder " + barcode
					+ " HolderType unknown - cannot schedule");
		}
		final SchedulePlan sp = ht.getDefaultSchedulePlan();
		if (null == sp) {
			// No default schedule plan - nothing can be done
			throw new BusinessException("Holder " + barcode
					+ " with HolderType " + ht.get_Name()
					+ " has no defaultSchedulePlan - cannot schedule");
		}

		// plate.getCreationDate() is not ideal as the schedule start point as
		// the plate may be
		// created sometime before it is used - it should be the date the setup
		// experiment was run.
		// This query is a bit of a cheat - it relies on:
		// - The holder.firstSample having been properly configured to point at
		// a trial drop
		// - The experiment that created holder.firstSample plus its
		// experimentType and startDate having been properly recorded
		// - The hard-coded use of the expected experimentType.name "Trials" for
		// crystal trial experiments
		final Query q = version
				.getSession()
				.createSQLQuery(
						"SELECT ee.startdate FROM hold_abstractholder ah INNER JOIN hold_holder hh ON ah.labbookentryid = hh.abstractholderid INNER JOIN expe_outputsample eoe ON hh.firstsampleid = eoe.sampleid INNER JOIN expe_experiment ee ON eoe.experimentid = ee.labbookentryid INNER JOIN ref_experimenttype ret ON ee.experimenttypeid = ret.publicentryid AND ret.name = 'Trials' WHERE ah.name = :barcode");
		q.setString("barcode", barcode);
		Object obj = q.uniqueResult();
		java.sql.Timestamp startDate = null;
		if (null != obj) {
			startDate = (java.sql.Timestamp) obj;
		} else {
			// Not yet set up - nothing can be done
			throw new BusinessException("Holder " + barcode
					+ " with HolderType " + ht.get_Name()
					+ " has not yet been set up - cannot schedule");
		}

		final Collection<SchedulePlanOffset> spos = sp.getSchedulePlanOffsets();

		// plate.getCreationDate() is not ideal - it should be the date the
		// setup experiment was run
		// final long createdAt = plate.getCreationDate().getTimeInMillis();
		final long createdAt = startDate.getTime();
		final TimeZone utc = TimeZone.getTimeZone("UTC");
		final Calendar now = Calendar.getInstance();
		final List<ScheduledTask> sts = new ArrayList<ScheduledTask>(
				spos.size());

		// Improve performance
		// Although this technique is used elsewhere at this level it
		// effectively
		// removes control of flushing from the caller and so is probably
		// odious.
		final WritableVersionImpl wv = (WritableVersionImpl) this
				.getWritableVersion();
		final org.pimslims.dao.FlushMode oldFlushMode = wv.getFlushMode();
		wv.setFlushMode(org.pimslims.dao.FlushMode.batchMode());
		LabNotebook ao = plate.getAccess();
		User u = plate.getCreator();

		for (SchedulePlanOffset spo : spos) {

			// spo.getOffsetTime() is milliseconds from t=0
			Calendar dateToImage = Calendar.getInstance(utc);
			dateToImage.setTimeInMillis(spo.getOffsetTime().longValue()
					+ createdAt);

			String name = createName(barcode, dateToImage);

			Map<String, Object> attr = new HashMap<String, Object>();
			attr.put(ScheduledTask.PROP_HOLDER, plate);
			attr.put(ScheduledTask.PROP_NAME, name);
			attr.put(ScheduledTask.PROP_PRIORITY, spo.getPriority());
			attr.put(ScheduledTask.PROP_SCHEDULEDTIME, dateToImage);
			attr.put(ScheduledTask.PROP_SCHEDULEPLANOFFSET, spo);
			attr.put(ScheduledTask.PROP_STATE, IMAGING_STATE_SCHEDULED);
			if (null != ao) {
				attr.put(ScheduledTask.PROP_ACCESS, ao);
			}
			attr.put(ScheduledTask.PROP_CREATIONDATE, now);
			if (null != u) {
				attr.put(ScheduledTask.PROP_CREATOR, u);
			}
			attr.put(ScheduledTask.PROP_DETAILS, spo.getDetails());
			try {
				sts.add(new ScheduledTask(wv, attr));
			} catch (ConstraintException e) {
				throw new BusinessException(e.getMessage(), e);
			}
		}

		wv.getSession().flush();
		wv.setFlushMode(oldFlushMode);

		return mapScheduledTasks(sts, barcode);
	}

	/**
	 * ImagerServiceImpl.startedImaging - Call this method to indicate that the
	 * scheduled imaging specified by barcode and dateToImage began imaging at
	 * dateImaged on the specified robot. The returned sessionId should be
	 * passed into {@link #finishedImaging(String, String, String)} when the
	 * session is complete.
	 * 
	 * @see org.pimslims.crystallization.implementation.ImagerService#startedImaging(java.lang.String,
	 *      java.util.Calendar, java.util.Calendar, java.lang.String)
	 */
	public String startedImaging(final String barcode,
			final Calendar dateToImage, final Calendar dateImaged,
			final String robot) throws BusinessException {
		final ScheduledTask st = findScheduledTask(barcode, dateToImage);
		final Instrument inst = findInstrument(robot);
		try {
			st.setCompletionTime(dateImaged);
			st.setInstrument(inst);
			st.setState(IMAGING_STATE_IMAGING);
			version.getSession().saveOrUpdate(st);
			version.getSession().flush();

			// This unfortunately breaks - but only on Jon's production system
			// Suspicions of calls to Holder.setLastTask incorrectly updating
			// ScheduledTask.holder
			// st.getHolder().setLastTask(st);
			// version.getSession().saveOrUpdate(st.getHolder());

			// So, lets try:
			st.getHolder().updateDerivedData();
			version.getSession().flush();
			// TODO Create stub PlateInspection?
		} catch (ConstraintException e) {
			throw new BusinessException(e.getMessage(), e);
		} catch (Throwable t) {
			throw new BusinessException(t.getMessage(), t);
		}
		return st.getName();
	}

	/**
	 * ImagerServiceImpl.startedUnscheduledImaging - Call this method to
	 * indicate that an unscheduled imaging specified by barcode and dateToImage
	 * began imaging at dateImaged on the specified robot. The returned
	 * sessionId should be passed into
	 * {@link #finishedImaging(String, String, String)} when the session is
	 * complete.
	 * 
	 * @see org.pimslims.crystallization.implementation.ImagerService#startedUnscheduledImaging(java.lang.String,
	 *      java.util.Calendar, java.util.Calendar, java.lang.String)
	 */
	public String startedUnscheduledImaging(final String barcode,
			final Calendar dateToImage, final Calendar dateImaged,
			final String robot) throws BusinessException {
		final String name = createName(barcode, dateToImage);
		final Instrument inst = findInstrument(robot);
		final Holder plate = findHolder(barcode);
		final Map<String, Object> attr = new HashMap<String, Object>();
		attr.put(ScheduledTask.PROP_COMPLETIONTIME, dateImaged);
		attr.put(ScheduledTask.PROP_HOLDER, plate);
		attr.put(ScheduledTask.PROP_INSTRUMENT, inst);
		attr.put(ScheduledTask.PROP_NAME, name);
		attr.put(ScheduledTask.PROP_PRIORITY, 0);
		attr.put(ScheduledTask.PROP_SCHEDULEDTIME, dateToImage);
		attr.put(ScheduledTask.PROP_STATE, IMAGING_STATE_IMAGING);
		attr.put(ScheduledTask.PROP_ACCESS, plate.getAccess());
		attr.put(ScheduledTask.PROP_CREATIONDATE, dateImaged);
		attr.put(ScheduledTask.PROP_CREATOR, plate.getCreator());
		try {
			new ScheduledTask(this.getWritableVersion(), attr);
		} catch (ConstraintException e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return name;
	}

	/**
	 * ImagerServiceImpl.finishedImaging - Call this method to indicate that the
	 * imaging of the plate with the specified barcode has been completed by the
	 * specified robot. The imaging session is identified by sessionId as
	 * returned from {@link #startedImaging(String, Calendar, Calendar, String)}
	 * or {@link #startedUnscheduledImaging(String, Calendar, Calendar, String)}
	 * .
	 * 
	 * @see org.pimslims.crystallization.implementation.ImagerService#finishedImaging(java.lang.String)
	 */
	public boolean finishedImaging(final String barcode,
			final String sessionId, final String robot)
			throws BusinessException {
		final ScheduledTask st = findScheduledTask(sessionId);
		if ((null == st.getState())
				|| IMAGING_STATE_COMPLETED != st.getState().intValue()) {
			try {
				st.setState(IMAGING_STATE_COMPLETED);
				version.getSession().saveOrUpdate(st);
			} catch (ConstraintException e) {
				throw new BusinessException(e.getMessage(), e);
			}
		}
		return true;
	}

	/**
	 * ImagerServiceImpl.skippedImaging - Call this method to indicate that the
	 * scheduled imaging identified by barcode and dateToImage has been skipped
	 * on the specified robot.
	 * 
	 * @see org.pimslims.crystallization.implementation.ImagerService#skippedImaging(java.lang.String,
	 *      java.util.Calendar, java.lang.String)
	 */
	public boolean skippedImaging(final String barcode,
			final Calendar dateToImage, final String robot)
			throws BusinessException {
		final ScheduledTask st = findScheduledTask(barcode, dateToImage);
		if ((null == st.getState())
				|| (IMAGING_STATE_SKIPPED != st.getState().intValue())
				&& (IMAGING_STATE_COMPLETED != st.getState().intValue())) {
			final Instrument inst = findInstrument(robot);
			try {
				st.setCompletionTime(Calendar.getInstance());
				st.setInstrument(inst);
				st.setState(IMAGING_STATE_SKIPPED);
				version.getSession().saveOrUpdate(st);
			} catch (ConstraintException e) {
				throw new BusinessException(e.getMessage(), e);
			}
		}
		return true;
	}

	/**
	 * ImagerServiceImpl.setPriority - Call this method to change the priority
	 * of the scheduled imaging task identified by barcode and dateToImage on
	 * the specified robot.
	 * 
	 * @see org.pimslims.crystallization.implementation.ImagerService#setPriority(java.lang.String,
	 *      java.util.Calendar, java.lang.String, int)
	 */
	public boolean setPriority(final String barcode,
			final Calendar dateToImage, final String robot, final int priority)
			throws BusinessException {
		final ScheduledTask st = findScheduledTask(barcode, dateToImage);
		if ((null == st.getPriority()) || (priority != st.getPriority())) {
			try {
				st.setPriority(new Integer(priority));
				version.getSession().saveOrUpdate(st);
			} catch (ConstraintException e) {
				throw new BusinessException(e.getMessage(), e);
			}
		}
		return true;
	}

	/**
	 * ImagerServiceImpl.findScheduledTask - Find a ScheduledTask by the barcode
	 * of the associated Holder and the dateToImage (scheduledTime). NB This
	 * does not do a fetch join on Holder, as the calls made to this method from
	 * this class do not subsequently reference the Holder.
	 * 
	 * @param barcode
	 * @param dateToImage
	 * @param robot
	 * @return
	 * @throws BusinessException
	 */
	public ScheduledTask findScheduledTask(final String barcode,
			final Calendar dateToImage) throws BusinessException {
		final Query q = version
				.getSession()
				.createQuery(
						"from ScheduledTask st where st.scheduledTime = :dateToImage and st.holder.name = :barcode");
		q.setCalendar("dateToImage", dateToImage);
		q.setString("barcode", barcode);
		q.setMaxResults(2);

		final Iterator<?> rs = q.list().iterator();
		ScheduledTask st = null;
		if (rs.hasNext()) {
			st = (ScheduledTask) rs.next();
		}

		if (null == st) {
			throw new BusinessException("No ScheduledTask found for barcode = "
					+ barcode + ", dateToImage = " + dateToImage);
		}

		// TODO Should be single result - validate?
		// if (rs.hasNext()) {
		// throw new BusinessException("Multiple ScheduledTask found for barcode
		// = " + barcode
		// + ", dateToImage = " + dateToImage);
		// }

		return st;
	}

	/**
	 * 
	 * ImagerServiceImpl.findScheduledTask
	 * 
	 * @param name
	 * @return
	 * @throws BusinessException
	 */
	public ScheduledTask findScheduledTask(final String name)
			throws BusinessException {
		final Query q = version.getSession().createQuery(
				"from ScheduledTask st where st.name = :name");
		q.setString("name", name);

		final ScheduledTask st = (ScheduledTask) q.uniqueResult();

		if (null == st) {
			throw new BusinessException("No ScheduledTask found for name = "
					+ name);
		}

		return st;
	}

	/**
	 * 
	 * ImagerServiceImpl.findInstrument
	 * 
	 * @param name
	 * @return
	 * @throws BusinessException
	 */
	Instrument findInstrument(final String name) throws BusinessException {
		final Query q = version.getSession().createQuery(
				"from Instrument inst where inst.name = :name");
		q.setString("name", name);

		final Instrument inst = (Instrument) q.uniqueResult();

		if (null == inst) {
			throw new BusinessException("No Instrument found for name = "
					+ name);
		}

		return inst;
	}

	/**
	 * 
	 * ImagerServiceImpl.createInstrument
	 * 
	 * TODO Expand to cover more attributes and/or use the xtalPiMSAPI model
	 * object
	 * 
	 * @param name
	 * @param temperature
	 * @return
	 * @throws BusinessException
	 */
	public void createInstrument(final String name, final float temperature)
			throws BusinessException {
		Map<String, Object> attr = new HashMap<String, Object>();
		attr.put(Instrument.PROP_NAME, name);
		attr.put(Instrument.PROP_TEMPERATURE, new Float(temperature));
		try {
			new Instrument(this.getWritableVersion(), attr);
		} catch (ConstraintException e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}

	/**
	 * ImagerServiceImpl.findHolder - Find a PiMS Holder model object by
	 * barcode. NB there is no access control applied.
	 * 
	 * @param name
	 * @return
	 * @throws BusinessException
	 */
	public Holder findHolder(final String name) throws BusinessException {
		final Query q = version
				.getSession()
				.createQuery(
						"from Holder h left join fetch h.access left join fetch h.creator where h.name = :name");
		q.setString("name", name);

		final Holder h = (Holder) q.uniqueResult();

		if (null == h) {
			throw new BusinessException("No Holder found for name = " + name);
		}

		return h;
	}

	/**
	 * ImagerServiceImpl.createName - Construct an appropriate name to use as
	 * the name of a ScheduledTask / ScheduleView.
	 * 
	 * @param barcode
	 * @param dateToImage
	 * @param robot
	 * @return
	 */
	public static String createName(final String barcode,
			final Calendar dateToImage) {
		final SimpleDateFormat sdf = new SimpleDateFormat(IMAGINGID_DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return barcode + "-" + sdf.format(dateToImage.getTime());
	}

	/**
	 * ImagerServiceImpl.mapScheduledTasks - Convert a Collection of the PiMS
	 * model object ScheduledTask to a Collection of the xtalPiMS business
	 * object ScheduleView. Passing in the barcode is not necessary, though if
	 * the barcode is the same in all cases it may save some database traffic.
	 * 
	 * @param scheduledTasks
	 * @param barcode
	 * @return
	 */
	public Collection<ScheduleView> mapScheduledTasks(
			final Collection<ScheduledTask> scheduledTasks, final String barcode) {
		final ArrayList<ScheduleView> schedules = new ArrayList<ScheduleView>(
				scheduledTasks.size());
		for (ScheduledTask st : scheduledTasks) {
			final ScheduleView view = new ScheduleView();
			if (null != barcode) {
				view.setBarcode(barcode);
			} else {
				view.setBarcode(st.getHolder().getName());
			}
			view.setDateImaged(st.getCompletionTime());
			view.setDateToImage(st.getScheduledTime());
			view.setName(st.getName());
			view.setPriority(st.getPriority());
			view.setState(st.getState());
			view.setDetails(st.getDetails());
			if (null != st.getInstrument()) {
				view.setImager(st.getInstrument().getName());
				Float t = st.getInstrument().getTemperature();
				if (null != t) {
					view.setTemperature(t.doubleValue());
				} else {
					view.setTemperature(20D);
				}
			}
			schedules.add(view);
		}
		return schedules;
	}

	/* Additional methods for liquid handling / sample management */

	/*
	 * I've had a few more thoughts on how to record samples for xtalPiMS. They
	 * are a little disjointed, though hopefully this email will make me smarten
	 * them up. These thoughts all relate to how to implement the old OPPF
	 * sample registration system in xtalPiMS and particularly the bits required
	 * for the integration of the liquid handling instruments.
	 * 
	 * 
	 * 1. SampleDAO uses a system in which samples available for crystallization
	 * belong to a specific SampleCategory {SC}. As previously stated, I don't
	 * like the choice of {SC} due to it being insufficiently specific - it
	 * tells me the samples that are appropriate for crystallization rather than
	 * those that are intended for crystallization. However, the assignment of
	 * {SC} may be necessary for subsequent processing.
	 * 
	 * 2. SampleDAO also uses a system in which Experiments representing the act
	 * of producing, or making a sample available for, crystallization can be
	 * identified by an xtalPiMS-specific ExperimentType {ET}. Whilst {SC} gives
	 * me all the samples that are appropriate for crystallization, {ET} allows
	 * me to identify those samples that are intended for crystallization.
	 * 
	 * 3. Currently SampleDAO makes no attempt to store most of the information
	 * associated with a sample. A protocol of ExperimentType {ET} can be used
	 * to provide custom data collection, though id, createDate, name and
	 * details should be stored against the sample (batchNumber and pH are also
	 * currently stored). The protocol to be used could be xtalPiMS reference
	 * data or made a site-configurable item via our usual configuration route.
	 * The existing experiment UI could be used to actually gather the data,
	 * though a custom version may be better. The xtalPiMS sample entity itself
	 * perhaps ought to be more flexible to better support this (eg most of the
	 * properties dropped in favour of a collection of name-value-pairs).
	 * 
	 * 4. We can then query sample details by:
	 * 
	 * select s.id, s.name, s.createDate, s.details, p.name, p.value from Sample
	 * s, Experiment e, Parameter p where s.outputSample.experiment.id = e.id
	 * and e.experimentType.id = :{ET}.id and e.id = p.experiment.id [ and [
	 * s.name = :name | s.id = :id ] ] [ and access control clause ] order by
	 * s.id ;
	 * 
	 * 5. Sample registration at the old OPPF uses a three-layer hierarchy of
	 * project-sample-aliquot. The project is essentially an access control item
	 * (name, description, group leader, members) and maps reasonably well onto
	 * PiMS' AccessObject (aka LabBook). The old OPPF sample is used to store
	 * batch-independent sample information to facilitate reuse and its mapping
	 * is unclear - construct (ie ResearchObjective) is probably closest. The
	 * old aliquot maps onto PiMS' Sample. Note that xtalPiMS' Project entity is
	 * mapped on PiMS' Project entity but getting from Sample to Project appears
	 * to require a large number of joins. Using the mapping
	 * AccessObject-ResearchObjective-Sample then the query to get basic
	 * information about all samples with their constructs and projects is:
	 * 
	 * select s.id, s.name, s.details, ro.id, ro.name, ro.details, ao.id,
	 * ao.name, ao.details from Sample s left join s.access ao,
	 * ResearchObjective ro where s.outputSample.experiment.experimentType.id =
	 * :{ET}.id and s.outputSample.experiment.researchObjective.id = ro.id [ and
	 * access control clause ] ;
	 * 
	 * If (null == ao) is still a possibility then it should be post-processed
	 * appropriately. If not, or alternatively, the left join could be replaced
	 * with an inner join.
	 * 
	 * 6. Project creation requires the user-driven creation of an AccessObject,
	 * UserGroup, Permission, etc., plus management of UserGroup.members. A
	 * specific privilege (membership of the "grouphead" role) is required to
	 * perform this act in the old OPPF, with group heads able to manage the
	 * member lists only for the projects that they lead. PiMS doesn't appear to
	 * have any mechanism to delegate or assign this privilege.
	 * 
	 * 7. The registration of new constructs may require the ability to create a
	 * construct without a target. Later we may also need the ability to
	 * retrospectively create a target for a construct or link a construct to an
	 * existing target. Constructs that entered the pipeline at an earlier stage
	 * will already exist and thus not need to be created. However, it may be
	 * unnecessary or inappropriate to show them if there are a large number
	 * that don't make it through to crystallization.
	 */

	public Collection<org.pimslims.business.core.model.Sample> findSamples(
			final String construct) {
		// Access control required, so probably best done via PiMS criteria?
		// Hmmm, massive join to may need to even be SQL
		return null;
	}

	/**
	 * The id of the ExperimentType that represents the import of a Sample
	 * intended for crystallization
	 */
	private static Long magicExperimentTypeId = null;

	/**
	 * ImagerServiceImpl.findSimpleSampleViews
	 * 
	 * @see org.pimslims.business.crystallization.service.ImagerService#findSimpleSampleViews()
	 */
	public Collection<SimpleSampleView> findSimpleSampleViews()
			throws BusinessException {

		/*
		 * Implementation of point 5 above
		 */

		// Ensure magicExperimentTypeId is populated
		if (null == magicExperimentTypeId) {
			ExperimentType et = SampleDAO.getSampleDefaultExpType(this
					.getVersion());
			if ((null == et) || (null == et.getDbId())) {
				throw new BusinessException(
						"Failed to find default ExperimentType - have the xtalPiMS reference data been loaded?");
			}
			magicExperimentTypeId = et.getDbId();
		}

		// HQL without access control:
		// "select s.id, s.name, s.details, ro.id, ro.commonName, ro.details,
		// ao.id, ao.name, ao.details from Sample s left join s.access ao,
		// ResearchObjective ro where s.isActive = true and
		// s.outputSample.experiment.experimentType.id = :etid and
		// s.outputSample.experiment.researchObjective.id = ro.id";

		/*
		 * JMD: Performance fell off a cliff with upgrade to v4.3 - not sure why
		 * 
		 * // BUild HQL with access control List<String> conditions = new
		 * ArrayList<String>(); conditions .add(
		 * "s.isActive = true and s.outputSample.experiment.experimentType.id = :etid and s.outputSample.experiment.researchObjective.id = ro.id"
		 * ); String hql =
		 * "select s.id, s.name, s.details, ro.id, ro.commonName, ro.details, ao.id, ao.name, ao.details from Sample s left join s.access ao, ResearchObjective ro"
		 * + HQLBuilder.getWhereHQL(this.getVersion(), "s", conditions,
		 * org.pimslims.model.sample.Sample.class);
		 */

		// Build SQL with access control
		String sql = "select "
				+ "sample0_1_.LABBOOKENTRYID as col_0_0_, " // sample id
				+ "sample0_1_.NAME as col_1_0_, " // sample name
				+ "sample0_2_.DETAILS as col_2_0_, " // sample details
				+ "researchob2_.LabBookEntryID as col_3_0_, " // construct id
				+ "researchob2_.COMMONNAME as col_4_0_, " // construct name
				+ "researchob2_1_.DETAILS as col_5_0_, " // construct
															// description
				+ "accessobje1_.SYSTEMCLASSID as col_6_0_, " // project id
				+ "accessobje1_.NAME as col_7_0_, " // project name
				+ "accessobje1_1_.DETAILS as col_8_0_ " // project description
				+ "from "
				+ "SAM_ABSTRACTSAMPLE sample0_1_ " // sample id, sample name
				+ "inner join CORE_LABBOOKENTRY sample0_2_ on sample0_1_.LABBOOKENTRYID=sample0_2_.DBID " // sample
																											// details
				+ "left outer join CORE_ACCESSOBJECT accessobje1_ on sample0_2_.ACCESSID=accessobje1_.SYSTEMCLASSID " // project
																														// id,
																														// project
																														// name,
																														// allowing
																														// null
				+ "left outer join CORE_SYSTEMCLASS accessobje1_1_ on accessobje1_.SYSTEMCLASSID=accessobje1_1_.DBID " // project
																														// details,
																														// allowing
																														// null
				+ "inner join EXPE_OUTPUTSAMPLE outputsamp3_ on sample0_1_.LABBOOKENTRYID = outputsamp3_.sampleid " // link
																													// sample
																													// to
																													// outputsample
				+ "inner join EXPE_EXPERIMENT experiment4_ on outputsamp3_.EXPERIMENTID = experiment4_.LABBOOKENTRYID " // link
																														// outputsample
																														// to
																														// experiment
				+ "inner join TARG_RESEARCHOBJECTIVE researchob2_ on experiment4_.RESEARCHOBJECTIVEID = researchob2_.LabBookEntryID " // construct
																																		// id,
																																		// construct
																																		// name
				+ "inner join CORE_LABBOOKENTRY researchob2_1_ on researchob2_.LabBookEntryID=researchob2_1_.DBID "; // construct
																														// details

		List<String> conditions = new ArrayList<String>();
		conditions
				.add("experiment4_.EXPERIMENTTYPEID=:etid and sample0_1_.ISACTIVE=true");

		String where = AbstractViewDAO.getWhereHQL(this.getVersion(),
				"sample0_2_", conditions,
				org.pimslims.model.sample.Sample.class);
		where = where.replace("sample0_2_.access", "sample0_2_.accessid");
		where = where.replace("sample0_2_.accessid.id", "sample0_2_.accessid");

		sql += where;
		sql += " order by sample0_1_.NAME";

		Query q = this.getVersion().getSession().createSQLQuery(sql);
		q.setLong("etid", magicExperimentTypeId);

		List<?> rs = q.list();
		Iterator<?> iter = rs.iterator();
		Collection<SimpleSampleView> simpleSamples = new ArrayList<SimpleSampleView>();
		HashMap<Long, String> projectLeaderMap = new HashMap<Long, String>();
		while (iter.hasNext()) {
			Object[] row = (Object[]) iter.next();
			SimpleSampleView ss = new SimpleSampleView();
			// ss.setSampleId((Long) row[0]);
			ss.setSampleId(bigIntegerToLong((java.math.BigInteger) row[0]));
			ss.setSampleName((String) row[1]);
			ss.setSampleDetails((String) row[2]);
			// ss.setConstructId((Long) row[3]);
			ss.setConstructId(bigIntegerToLong((java.math.BigInteger) row[3]));
			ss.setConstructName((String) row[4]);
			ss.setConstructDetails((String) row[5]);
			if (null == row[6]) {
				ss.setProjectId(null);
				ss.setProjectName("Reference Data");
				ss.setProjectDetails("Reference Data");
			} else {
				// ss.setProjectId((Long) row[6]);
				ss.setProjectId(bigIntegerToLong((java.math.BigInteger) row[6]));
				ss.setProjectName((String) row[7]);
				ss.setProjectDetails((String) row[8]);
				if (projectLeaderMap.containsKey(ss.getProjectId())) {
					ss.setProjectLeader(projectLeaderMap.get(ss.getProjectId()));
				} else {
					String leader = findProjectLeader(ss.getProjectId());
					projectLeaderMap.put(ss.getProjectId(), leader);
					ss.setProjectLeader(leader);
				}
			}

			simpleSamples.add(ss);
		}

		return simpleSamples;
	}

	/**
	 * ImagerServiceImpl.fromBigInteger - get a Long from a BigInteger
	 * 
	 * @param bi
	 * @return
	 */
	private Long bigIntegerToLong(java.math.BigInteger bi) {
		if (null == bi) {
			return null;
		}
		return new Long(bi.longValue());
	}

	/**
	 * ImagerServiceImpl.findProjectLeader - find leaders of UserGroups with
	 * create permission of permissionClass 'PIMS' and roleName 'any' for the
	 * specified AccessObject id.
	 * 
	 * @param projectId
	 * @return
	 */
	public String findProjectLeader(Long projectId) {
		String hql = "select u.name, up.givenName, up.familyName from Permission p join p.userGroup ug join ug.header u left join u.person up where p.accessObject.id = :aoid and p.permission = true and p.opType = 'create' and p.permissionClass = 'PIMS' and p.roleName = 'any'";
		Query q = this.getVersion().getSession().createQuery(hql);
		q.setLong("aoid", projectId);
		Iterator<?> iter = q.list().iterator();
		boolean sep = false;
		String leader = "";
		// TODO Exclude duplicate leaders - use distinct above - with u.id
		while (iter.hasNext()) {
			if (sep) {
				leader += ", ";
			}
			Object[] tuple = (Object[]) iter.next();
			if ((null != tuple[1]) || (null != tuple[2])) {
				leader += (String) tuple[1] + " " + (String) tuple[2];
			} else {
				leader += (String) tuple[0];
			}
			sep = true;
		}
		if ("".equals(leader)) {
			leader = "Unspecified";
		}
		return leader;
	}

	/**
	 * ImagerServiceImpl.createAndLink
	 * 
	 * <p>
	 * Unfortunately, {@link ImageServiceImpl#create(Image)} does not actually
	 * link the image it creates to the sample, scheduled task or image type.
	 * This is an implementation that does so, and is more focused on automated
	 * imagers.
	 * </p>
	 * 
	 * <p>
	 * Any changes to this code must remain compatible with ImageDAO,
	 * ImageViewDAO and ImageServiceImpl.
	 * </p>
	 * 
	 * <p>
	 * This code needs the following items populated:
	 * image.getPlateInspection();
	 * image.getPlateInspection().getInspectionName();
	 * image.getDrop().getName(); if the base url is not that of the
	 * imager-default but could be parsed automatically using the barcode,
	 * image.getPlateInspection().getPlate().getBarcode().
	 * </p>
	 * 
	 * @param image
	 * @throws BusinessException
	 */
	public void createAndLink(Image image) throws BusinessException {

		/*
		 * This is what PlateInspection does:
		 * 
		 * if (xobject.getImages() != null && xobject.getImages().size() > 0) {
		 * final LocationDAO locationDAO = new LocationDAO(this.version);
		 * ImageType pImageType = null; if (null != xobject.getLocation()) {
		 * pImageType =
		 * locationDAO.getPO(xobject.getLocation()).getDefaultImageType(); }
		 * final ImageDAO imageDAO = new ImageDAO(this.version); for (final
		 * Image xImage : xobject.getImages()) { final
		 * org.pimslims.model.crystallization.Image pImage =
		 * imageDAO.createPO(xImage); if (null == pImage.getImageType()) {
		 * pImage.setImageType(pImageType); } pImage.setScheduledTask(pobject);
		 * final WellPosition wellPosition = xImage.getDrop().getWellPosition();
		 * final Sample pSample = DAOUtils.getSampleByPosition(pHolder,
		 * wellPosition); pImage.setSample(pSample); } }
		 */

		Logger log = LoggerFactory.getLogger(this.getClass());
		// log.setLevel(Level.WARN);

		String imagerBaseUrl = null;
		String imageBaseUrl = null;
		String imagePath = null;
		String imageName = image.getImagePath();

		// Improve performance
		// Although this technique is used elsewhere at this level it
		// effectively
		// removes control of flushing from the caller and so is probably
		// odious.
		final WritableVersionImpl wv = (WritableVersionImpl) this
				.getWritableVersion();
		FlushMode flushMode = org.pimslims.dao.FlushMode.batchMode();
		flushMode.setAutoUpdateInverseRoles(false);
		flushMode.setCheckValue(false);
		flushMode.setFlushSessionAfterCreate(false);
		flushMode.setHibernateFlushMode(org.hibernate.FlushMode.MANUAL);
		final org.pimslims.dao.FlushMode oldFlushMode = wv.getFlushMode();
		wv.setFlushMode(flushMode);
		Query q;

		log.debug("Finding ScheduledTask");
		// ScheduledTask task = new
		// PlateInspectionDAO(wv).getPO(image.getPlateInspection());
		// q =
		// wv.getSession().createQuery(
		// "select st from ScheduledTask st left join fetch st.images where
		// st.name = ?");
		// If the flushMode.autoUpdateInverseRoles actually works, we can do:
		q = wv.getSession().createQuery(
				"select st from ScheduledTask st where st.name = ?");
		q.setString(0, image.getPlateInspection().getInspectionName());
		log.debug("Finding ScheduledTask");

		ScheduledTask task = (ScheduledTask) q.uniqueResult();
		assert null != task : "No inspection for image";
		log.debug("Done finding ScheduledTask");

		// Requires that we know the name or id of the sample / trialDrop
		log.debug("Finding Sample");
		// org.pimslims.model.sample.Sample sample =
		// TrialDropDAO.getpimsSample(wv, image.getDrop());
		// q =
		// wv.getSession().createQuery(
		// "select s from Sample s left join fetch s.access left join fetch
		// s.images where s.name = ?");
		// If the flushMode.autoUpdateInverseRoles actually works, we can do:

		// q = wv.getSession().createQuery("select s from Sample s left join
		// fetch s.access where s.name = ?");
		// q.setString(0, image.getDrop().getName());
		// org.pimslims.model.sample.Sample sample =
		// (org.pimslims.model.sample.Sample) q.uniqueResult();

		org.pimslims.model.sample.Sample sample = wv.findFirst(
				org.pimslims.model.sample.Sample.class,
				org.pimslims.model.sample.Sample.PROP_NAME, image.getDrop()
						.getName());
		assert null != sample : "Cant find sample: "
				+ image.getDrop().getName();

		// Alternatively
		// Holder h = TrialServiceUtils.getHolder(wv,
		// image.getPlateInspection().getPlate().getBarcode());
		// TrialServiceUtils.getSampleByHolderPosition(h.getSamples(),
		// image.getDrop().getWellPosition());

		org.pimslims.model.reference.ImageType imageType = null;
		Instrument instrument = null;
		if (null != image.getLocation()) {
			log.debug("Finding Instrument");
			// Instrument instrument = new
			// LocationDAO(wv).getPO(image.getLocation());
			q = wv.getSession()
					.createQuery(
							"select i, i.defaultImageType from Instrument i left join fetch i.defaultImageType where i.name = ?");
			q.setString(0, image.getLocation().getName());
			// Query adapted to also return defaultImageType
			// instrument = (Instrument) q.uniqueResult();
			Object[] row = (Object[]) q.uniqueResult();
			instrument = (Instrument) row[0];
			org.pimslims.model.reference.ImageType defaultImageType = (org.pimslims.model.reference.ImageType) row[1];
			log.debug("Done finding Instrument");

			if (null == instrument) {
				throw new BusinessException("Imager not specified!");
			}

			// Now done in query above
			// log.debug("Finding Instrument.defaultImageType");
			// org.pimslims.model.reference.ImageType defaultImageType =
			// instrument.getDefaultImageType();
			// log.debug("Done finding Instrument.defaultImageType");
			if (null != defaultImageType) {
				imagerBaseUrl = defaultImageType.getUrl();

				// If we have a default base url for the imager, see if this
				// matches
				if (null != imagerBaseUrl) {
					if (imageName.startsWith(imagerBaseUrl)) {
						imageBaseUrl = imagerBaseUrl;
						imageName = imageName.substring(imagerBaseUrl.length());
					}
				}
				// Check defaultImageType settings against image
				if (imageTypeMatches(image, defaultImageType)) {
					imageType = defaultImageType;
				}

			}
		}

		// If we still haven't got imageBaseUrl, try again on the basis of the
		// barcode
		if (null != imageBaseUrl) {
			String barcode = image.getPlateInspection().getPlate().getBarcode();
			int barcodeIndex = imageName.indexOf("/" + barcode + "/");
			if (0 <= barcodeIndex) {
				// http://foo.bar.com/foo/bar/barcode/name.jpg
				// 0123456789012345678901234567890123456789012
				// Want to get:
				// http://foo.bar.com/foo/bar/
				// barcode/name.jpg
				imageBaseUrl = imageName.substring(0, barcodeIndex + 1);
				imageName = imageName.substring(barcodeIndex + 1);
			}
		}

		// Split into path and name
		int separatorPos = imageName.lastIndexOf("/");
		if (0 <= separatorPos) {
			imagePath = imageName.substring(0, separatorPos + 1);
			imageName = imageName.substring(separatorPos + 1); // FIXME Bug if
			// "/".equals(imagePath),
			// which is
			// invalid
			// anyway
		}

		try {
			if (null == imageType) {
				// TODO get this code under test.
				Map<String, Object> itattr = new HashMap<String, Object>();
				itattr.put(
						org.pimslims.model.reference.ImageType.PROP_CATORGORY,
						image.getImageType().toString());
				itattr.put(
						org.pimslims.model.reference.ImageType.PROP_COLOURDEPTH,
						new Integer(image.getColourDepth()));
				itattr.put(org.pimslims.model.reference.ImageType.PROP_SIZEX,
						new Integer(image.getSizeX()));
				itattr.put(org.pimslims.model.reference.ImageType.PROP_SIZEY,
						new Integer(image.getSizeY()));
				itattr.put(org.pimslims.model.reference.ImageType.PROP_URL,
						imageBaseUrl);
				itattr.put(
						org.pimslims.model.reference.ImageType.PROP_XLENGTHPERPIXEL,
						new Float(image.getXLengthPerPixel()));
				itattr.put(
						org.pimslims.model.reference.ImageType.PROP_YLENGTHPERPIXEL,
						new Float(image.getYLengthPerPixel()));

				log.debug("Finding ImageType");
				imageType = wv.findFirst(
						org.pimslims.model.reference.ImageType.class, itattr);
				log.debug("Done finding ImageType");

				if (null == imageType) {
					// Create with privileges from sample
					wv.setDefaultOwner(Access.REFERENCE);
					itattr.put(
							org.pimslims.model.reference.ImageType.PROP_NAME,
							"From " + imageName);
					log.debug("Creating ImageType");
					imageType = wv.create(
							org.pimslims.model.reference.ImageType.class,
							itattr);
					wv.flush();
					log.debug("Created ImageType");
				}
			}

			// Attribute map for image
			Map<String, Object> iattr = new HashMap<String, Object>();
			// Create with privileges from sample
			assert null != sample;
			if (null != sample) {
				iattr.put(org.pimslims.model.crystallization.Image.PROP_SAMPLE,
						sample);
				wv.setDefaultOwner(sample.getAccess());
			}
			iattr.put(org.pimslims.model.crystallization.Image.PROP_FILENAME,
					imageName);
			iattr.put(org.pimslims.model.crystallization.Image.PROP_FILEPATH,
					imagePath);
			iattr.put(org.pimslims.model.crystallization.Image.PROP_IMAGETYPE,
					imageType);
			if (null != instrument) {
				iattr.put(
						org.pimslims.model.crystallization.Image.PROP_INSTRUMENT,
						instrument);
			}
			// probably safe to leave empty for now
			// itattr.put(org.pimslims.model.crystallization.Image.PROP_MIMETYPE,
			// mimeType);
			if (null != task) {
				iattr.put(
						org.pimslims.model.crystallization.Image.PROP_SCHEDULEDTASK,
						task);
			}
			iattr.put(
					org.pimslims.model.crystallization.Image.PROP_CREATIONDATE,
					image.getImageDate());

			log.debug("Creating Image");
			wv.create(org.pimslims.model.crystallization.Image.class, iattr);
			wv.flush();
			log.debug("Created Image");

			wv.setFlushMode(oldFlushMode);

		} catch (AccessException e) {
			throw new BusinessException("Failed to create image", e);
		} catch (ConstraintException e) {
			throw new BusinessException("Failed to create image", e);
		}

	}

	/**
	 * ImagerServiceImpl.imageTypeMatches
	 * 
	 * @param image
	 * @param defaultImageType
	 * @return
	 */
	private boolean imageTypeMatches(Image image,
			org.pimslims.model.reference.ImageType defaultImageType) {
		try {
			return (null == image.getImageType())
					|| (defaultImageType.getCatorgory().equals(image
							.getImageType().toString()))
					&& (defaultImageType.getColourDepth().intValue() == image
							.getColourDepth())
					&& (defaultImageType.getSizeX().intValue() == image
							.getSizeX())
					&& (defaultImageType.getSizeX().intValue() == image
							.getSizeY())
					&& (defaultImageType.getXlengthPerPixel().doubleValue() == image
							.getXLengthPerPixel())
					&& (defaultImageType.getYlengthPerPixel().doubleValue() == image
							.getYLengthPerPixel());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * ImagerServiceImpl.createAndLink
	 * 
	 * TODO We can do better than this by caching the scheduled task,
	 * instrument, imagetype, etc.
	 * 
	 * @param images
	 * @throws BusinessException
	 */
	public void createAndLink(Collection<Image> images)
			throws BusinessException {
		for (Image image : images) {
			createAndLink(image);
		}
	}

	/**
	 * <p>
	 * ImagerServiceImpl.findProjects
	 * </p>
	 * 
	 * <p>
	 * Note that this implementation does not populate Project.localContact,
	 * owner or targets.
	 * </p>
	 * 
	 * <p>
	 * TODO Use a cut down project with no targets property and whose owner
	 * property has been converted to a string
	 * </p>
	 * 
	 * @see org.pimslims.business.crystallization.service.ImagerService#findProjects()
	 */
	public Collection<Project> findProjects() throws BusinessException {
		Collection<LabNotebook> aos = this.getVersion()
				.getCurrentLabNotebooks();
		Collection<Project> projects = new ArrayList<Project>(aos.size());
		for (LabNotebook ao : aos) {
			Project project = new Project();
			project.setDescription(ao.getDetails());
			project.setId(ao.getDbId());
			// TODO PiMS does not support
			// project.setLocalContact(localContact);
			project.setName(ao.getName());

			// TODO Dig out the group head(s) via create permissions
			// project.setOwner(owner);

			// Irrelevant for our purposes
			// project.setTargets(targets);

			projects.add(project);
		}
		return projects;
	}

	/*
	 * OK, that still leaves some further issues. The OPPF workflow is: create a
	 * deep well block as an instance of a specific screen; transfer an aliquot
	 * from the deep well block to the reservoir well of what will become the
	 * trial plate; add the sample to the trial plate and mix with a subaliquot
	 * of the reservoir; add any additive / seeds as appropriate.
	 * 
	 * So, we have:
	 * 
	 * 1. Import deep well block with specified barcode as an instance of a
	 * specified screen 2. Action to fill reservoirs in trial plate from deep
	 * well block 3. Action to add sample and subaliquot from reservoir to trial
	 * plate
	 * 
	 * In practice, whilst 2 & 3 may be separated by some time they are seen as
	 * a single step.
	 * 
	 * This code has gone into ScreenServiceImpl and TrialServiceImpl - see:
	 * ScreenServiceImpl#createDeepWellPlate TrialServiceImpl#fillTrialPlate and
	 * TrialServiceImpl#setupTrialPlate
	 */

}
