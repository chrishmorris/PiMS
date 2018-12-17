/*
 * ImageServiceImpl.java Created on 24 September 2007, 23:42 To change this template, choose Tools | Template
 * Manager and open the template in the editor.
 */

package org.pimslims.crystallization.implementation;

import java.util.Collection;
import java.util.Iterator;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.service.ImageService;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.ImageDAO;
import org.pimslims.crystallization.dao.view.ImageViewDAO;
import org.pimslims.crystallization.dao.view.ViewDAO;

/**
 * 
 * @author ian & Bill
 */
public class ImageServiceImpl extends BaseServiceImpl implements ImageService {

	ImageDAO imageDAO;

	/**
	 * Creates a new instance of ImageServiceImpl
	 * 
	 * @param dataStorage
	 */
	public ImageServiceImpl(final DataStorage dataStorage) {
		super(dataStorage);
		imageDAO = new ImageDAO(this.version);
	}

	public Image find(final long id) throws BusinessException {
		final org.pimslims.model.crystallization.Image pimsImage = getVersion()
				.get(id);
		return imageDAO.getFullXO(pimsImage);

	}

	/**
	 * <p>
	 * Note that, as implemented, this method only creates the image record - it
	 * does not link it to the sample, scheduled task, imager or image type.
	 * </p>
	 * 
	 * @TODO REQUIRED
	 * @param image
	 * @throws org.pimslims.business.exception.BusinessException
	 */
	public void create(final Image image) throws BusinessException {
		imageDAO.createPO(image);
	}

	/**
	 * @TODO REQUIRED
	 * @param image
	 * @throws org.pimslims.business.exception.BusinessException
	 */
	public void update(final Image image) throws BusinessException {
		throw new UnsupportedOperationException(
				"This method is not implemented");
	}

	@Deprecated
	// use createAndLink
	public void create(final Collection<Image> images) throws BusinessException {
		final Iterator<Image> it = images.iterator();
		while (it.hasNext()) {
			create(it.next());
		}
	}

	/**
	 * 
	 * @param images
	 * @throws org.pimslims.business.exception.BusinessException
	 */
	public void update(final Collection<Image> images) throws BusinessException {
		final Iterator<Image> it = images.iterator();
		while (it.hasNext()) {
			update(it.next());
		}
	}

	public Collection<Image> findAll(final BusinessCriteria criteria)
			throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Collection<ImageView> findViews(final BusinessCriteria criteria)
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

	public Collection<ImageView> findLatestImages(
			final BusinessCriteria criteria) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private ViewDAO<ImageView> viewDAO;

	private ViewDAO<ImageView> getViewDAO() {
		if (viewDAO == null) {
			viewDAO = new ImageViewDAO(version);
		}
		return viewDAO;
	}
}
