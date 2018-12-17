/**
 * 
 */
package org.pimslims.crystallization.implementation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.model.Organisation;
import org.pimslims.business.core.service.LocationService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.DAOUtils;
import org.pimslims.crystallization.dao.LocationDAO;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.holder.Holder;

/**
 * @author cm65
 */
public class LocationServiceImpl extends BaseServiceImpl implements
		LocationService {
	final LocationDAO locationDAO;

	public LocationServiceImpl(final DataStorageImpl impl) {
		super(impl);
		this.locationDAO = new LocationDAO(version);
	}

	/**
	 * @TODO Update with pressure and temperature...
	 * @param location
	 * @throws org.pimslims.business.exception.BusinessException
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pimslims.crystallization.business.LocationService#create(org.pimslims
	 * .crystallization.Location)
	 */
	public void create(final Location location) throws BusinessException {
		locationDAO.createPO(location);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pimslims.crystallization.business.LocationService#find(long)
	 */
	public Location find(final long id) throws BusinessException {
		return this.find(org.pimslims.model.experiment.Instrument.class
				.getName() + ":" + id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pimslims.crystallization.business.LocationService#find(java.lang.
	 * String)
	 */
	public Location find(final String id) throws BusinessException {
		return locationDAO.getFullXO((Instrument) version.get(id));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pimslims.crystallization.business.LocationService#findByName(java
	 * .lang.String)
	 */
	public Location findByName(final String name) throws BusinessException {
		final Instrument pimsLocation = getPimsLocation(this.version, name);
		if (null == pimsLocation) {
			return null;
		}
		return getXtalLocation(pimsLocation);
	}

	/**
	 * @param name
	 * @return
	 */
	private static Instrument getPimsLocation(final ReadableVersion version,
			final String name) {
		final Instrument pimsLocation = version.findFirst(Instrument.class,
				Instrument.PROP_NAME, name);
		return pimsLocation;
	}

	Location getXtalLocation(final Instrument instrument)
			throws BusinessException {
		return locationDAO.getFullXO(instrument);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pimslims.crystallization.business.LocationService#findByOrganisation
	 * (org.pimslims.crystallization.Organisation)
	 */
	public List<Location> findByOrganisation(final Organisation organisation,
			final BusinessCriteria criteria) throws BusinessException {
		// LATER implement
		throw new UnsupportedOperationException(
				"This method is not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pimslims.crystallization.business.LocationService#findByPlateInspection
	 * (org.pimslims.crystallization.PlateInspection)
	 */
	public Location findByPlateInspection(final PlateInspection plateInspection)
			throws BusinessException {
		if (null != plateInspection) {
			return plateInspection.getLocation();
		} else {
			return null;
		}
	}

	/**
	 * @TODO REQUIRED
	 * @param location
	 * @throws org.pimslims.business.exception.BusinessException
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pimslims.crystallization.business.LocationService#update(org.pimslims
	 * .crystallization.Location)
	 */
	public void update(final Location location) throws BusinessException {
		throw new UnsupportedOperationException(
				"this method is not implemented");
	}

	static org.pimslims.model.location.Location getCurrentLocation(
			final Holder holder) {
		return DAOUtils.getCurrentLocation(holder);
	}

	/**
	 * @TODO REQUIRED
	 * @param paging
	 * @return
	 * @throws org.pimslims.business.exception.BusinessException
	 */
	public Collection<Location> findAll(final BusinessCriteria criteria)
			throws BusinessException {
		final Collection<Location> locations = new LinkedList<Location>();
		for (final Instrument instrument : this.version
				.getAll(Instrument.class)) {
			locations.add(locationDAO.getSimpleXO(instrument));
		}
		return locations;
	}

	/**
	 * When an imager exists but is unusable
	 * 
	 * @param location
	 * @throws org.pimslims.business.exception.BusinessException
	 */
	public void close(final Location location) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Deprecated
	public void createImageAndLink(Image image) throws BusinessException {
		throw new UnsupportedOperationException("Not implemented.");
	}

	@Deprecated
	public void createImagesAndLink(Collection<Image> images)
			throws BusinessException {
		throw new UnsupportedOperationException("Not implemented.");

	}
}
