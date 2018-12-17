package org.pimslims.crystallization.dao;

import java.util.HashMap;
import java.util.Map;

import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.crystallization.Image;
import org.pimslims.model.crystallization.WellImageType;
import org.pimslims.model.reference.ImageType;

/**
 * <p>
 * Data Access Object for
 * {@link org.pimslims.business.crystallization.model.Image}
 * </p>
 * 
 * @author Bill Lin
 */
public class ImageDAO extends
		GenericDAO<Image, org.pimslims.business.crystallization.model.Image> {

	public ImageDAO(final ReadableVersion version) {
		super(version);
	}

	@Override
	protected void createPORelated(final Image pobject,
			final org.pimslims.business.crystallization.model.Image xobject)
			throws ConstraintException, BusinessException, ModelException {
		// nothing

	}

	@Override
	protected Map<String, Object> getFullAttributes(
			final org.pimslims.business.crystallization.model.Image xobject) {
		final Map<String, Object> attributes = new HashMap<String, Object>(
				getKeyAttributes(xobject));
		attributes.put(Image.PROP_CREATIONDATE, xobject.getImageDate());
		return attributes;
	}

	@Override
	protected Map<String, Object> getKeyAttributes(
			final org.pimslims.business.crystallization.model.Image xobject) {
		final Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put(Image.PROP_FILENAME, getName(xobject.getImagePath()));
		attributes.put(Image.PROP_FILEPATH, getPath(xobject.getImagePath()));
		return attributes;
	}

	@Override
	protected Class<Image> getPClass() {
		return Image.class;
	}

	@Override
	protected org.pimslims.business.crystallization.model.Image loadXAttribute(
			final Image pobject) throws BusinessException {
		final org.pimslims.business.crystallization.model.Image xImage = new org.pimslims.business.crystallization.model.Image();
		xImage.setImagePath(pobject.getFilePath() + pobject.getFileName());
		xImage.setImageDate(pobject.getCreationDate());
		if (pobject.getImageType() != null) {
			final ImageType iType = pobject.getImageType();
			xImage.setColourDepth(iType.getColourDepth());
			xImage.setSizeX(iType.getSizeX());
			xImage.setSizeY(iType.getSizeY());
			xImage.setXLengthPerPixel(iType.getXlengthPerPixel());
			xImage.setYLengthPerPixel(iType.getYlengthPerPixel());
		}
		if (pobject.getWellImageType() != null) {
			// TODO remove this, it was a mistake
			final WellImageType iType = pobject.getWellImageType();
			xImage.setColourDepth(iType.getColourDepth());
			xImage.setSizeX(iType.getSizeX());
			xImage.setSizeY(iType.getSizeY());
			xImage.setXLengthPerPixel(iType.getXlengthPerPixel());
			xImage.setYLengthPerPixel(iType.getYlengthPerPixel());
		}
		return xImage;
	}

	@Override
	protected org.pimslims.business.crystallization.model.Image loadXRole(
			final org.pimslims.business.crystallization.model.Image xobject,
			final Image pobject) throws BusinessException {
		return xobject;
	}

	@Override
	protected void updatePORelated(final Image pobject,
			final org.pimslims.business.crystallization.model.Image xobject)
			throws ModelException {
		// TODO Auto-generated method stub

	}

	public static Image getpimsImage(final ReadableVersion version,
			final org.pimslims.business.crystallization.model.Image xImage) {
		if (xImage == null) {
			return null;
		}

		Image pImage = version.get(xImage.getId());
		final Map<String, Object> pMap = new HashMap<String, Object>();

		if (pImage == null) {
			pImage = version.findFirst(Image.class, pMap);
		}

		return pImage;
	}

	public static String getPath(final String imagePath) {
		String ret = imagePath.substring(0,
				imagePath.length() - getName(imagePath).length());
		if (null == ret || "".equals(ret)) {
			return "/";
		}
		return ret;
	}

	// returns the last part of the path, the actual file name
	public static String getName(final String imagePath) {
		final String[] fileNames = imagePath.split("/");
		return fileNames[fileNames.length - 1];
	}

}
