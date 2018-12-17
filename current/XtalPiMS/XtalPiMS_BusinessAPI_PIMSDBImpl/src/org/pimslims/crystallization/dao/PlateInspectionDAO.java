/**
 * xtalPiMS Business API - PIMSDB Impl org.pimslims.crystallization.dao PlateInspectionDAO.java
 * 
 * @author bl67
 * @date 2 Mar 2009
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2009 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.dao;

import java.util.HashMap;
import java.util.Map;

import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.AbstractModelObject;
import org.pimslims.model.crystallization.WellImageType;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.ImageType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.schedule.ScheduledTask;

/**
 * PlateInspectionDAO
 * 
 */
public class PlateInspectionDAO extends
		GenericDAO<ScheduledTask, PlateInspection> {
	final LocationDAO locationDAO;

	public PlateInspectionDAO(final ReadableVersion version) {
		super(version);
		locationDAO = new LocationDAO(this.version);

	}

	@Override
	protected void createPORelated(final ScheduledTask pobject,
			final PlateInspection xobject) throws ConstraintException,
			BusinessException, ModelException {
		// this.getWritableVersion().flush();
		final Holder pHolder = pobject.getHolder();
		// update holder's lastInspection
		if (pHolder != null) {
			pHolder.updateDerivedData();
		}
		// create PlateInspection's Image
		if (xobject.getImages() != null && xobject.getImages().size() > 0) {
			final LocationDAO locationDAO = new LocationDAO(this.version);
			WellImageType wellImageType = null; // TODO remove this, it was a
												// mistake
			if (null != xobject.getLocation()) {
				wellImageType = locationDAO.getPO(xobject.getLocation())
						.getDefaultWellImageType();
			}
			ImageType pImageType = null;
			if (null != xobject.getLocation()) {
				pImageType = locationDAO.getPO(xobject.getLocation())
						.getDefaultImageType();
			}
			final ImageDAO imageDAO = new ImageDAO(this.version);
			for (final Image xImage : xobject.getImages()) {
				final org.pimslims.model.crystallization.Image pImage = imageDAO
						.createPO(xImage);
				if (null == pImage.getImageType()) {
					pImage.setImageType(pImageType);
				}
				if (null == pImage.getWellImageType()) {
					// use the default type from the instrument.
					// if image type is not known, then the whole path must be
					// saved in the image.
					pImage.setWellImageType(wellImageType);
				}
				pImage.setScheduledTask(pobject);
				final WellPosition wellPosition = xImage.getDrop()
						.getWellPosition();
				final Sample pSample = DAOUtils.getSampleByPosition(pHolder,
						wellPosition);
				assert null != pSample;
				pImage.setSample(pSample);
			}
		}

	}

	@Override
	protected Map<String, Object> getFullAttributes(
			final PlateInspection xobject) throws BusinessException {
		final Map<String, Object> attributes = new HashMap<String, Object>(
				getKeyAttributes(xobject));
		attributes.put(ScheduledTask.PROP_COMPLETIONTIME,
				xobject.getInspectionDate());
		attributes.put(ScheduledTask.PROP_SCHEDULEDTIME,
				xobject.getInspectionDate());
		if (null != xobject.getLocation()) {
			attributes.put(ScheduledTask.PROP_INSTRUMENT,
					findorCreateInstrument(xobject.getLocation()));
		}
		attributes.put(ScheduledTask.PROP_DETAILS, xobject.getDetails());
		final AbstractModelObject holder = getHolder(xobject.getPlate());
		attributes.put(ScheduledTask.PROP_HOLDER, holder);

		return attributes;
	}

	private AbstractModelObject getHolder(final TrialPlate plate) {
		AbstractModelObject holder = null;
		if (plate.getId() != null) {
			holder = this.version.get(Holder.class, plate.getId());
		}
		if (holder == null) {
			holder = this.version.findFirst(Holder.class, Holder.PROP_NAME,
					plate.getBarcode());
		}

		return holder;
	}

	@Override
	protected Map<String, Object> getKeyAttributes(final PlateInspection xobject) {
		final Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put(ScheduledTask.PROP_NAME, xobject.getInspectionName());
		return attributes;
	}

	@Override
	protected Class<ScheduledTask> getPClass() {

		return ScheduledTask.class;
	}

	@Override
	protected PlateInspection loadXAttribute(final ScheduledTask pobject)
			throws BusinessException {
		final PlateInspection inspection = new PlateInspection();
		inspection.setInspectionDate(pobject.getCompletionTime());
		inspection.setInspectionName(pobject.getName());
		inspection.setDetails(pobject.getDetails());
		return inspection;
	}

	@Override
	protected PlateInspection loadXRole(final PlateInspection xobject,
			final ScheduledTask pobject) throws BusinessException {
		xobject.setLocation(locationDAO.getSimpleXO(pobject.getInstrument()));
		xobject.setPlate(TrialPlateDAO.getSimplePlate(pobject.getHolder()));
		return xobject;
	}

	@Override
	protected void updatePORelated(final ScheduledTask pobject,
			final PlateInspection xobject) throws ModelException {
		// nothing

	}

	// cached location
	static Map<String, String> locationMap = new HashMap<String, String>();

	private Instrument findorCreateInstrument(
			final org.pimslims.business.core.model.Location location)
			throws BusinessException {
		if (locationMap.containsKey(location.getName())) {
			final Instrument pInstrument = version.get(locationMap.get(location
					.getName()));
			if (pInstrument != null) {
				return pInstrument;
			}
		}
		final LocationDAO locationDAO = new LocationDAO(version);
		final Instrument pInstrument = locationDAO.findOrCreate(location);
		locationMap.put(location.getName(), pInstrument.get_Hook());
		return pInstrument;
	}

}
