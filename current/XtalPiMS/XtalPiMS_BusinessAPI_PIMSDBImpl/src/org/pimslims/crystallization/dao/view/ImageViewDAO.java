/**
 * xtalPiMSImpl org.pimslims.crystallization.dao.view ImageViewDAO.java
 * 
 * @author bl67
 * @date 22 May 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.dao.view;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessCriterion;
import org.pimslims.business.criteria.PropertyNameConvertor;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.crystallization.Image;
import org.pimslims.model.holder.Holder;

/**
 * ImageViewDAO
 * 
 */
public class ImageViewDAO extends AbstractSQLViewDAO<ImageView> implements
		PropertyNameConvertor<ImageView> {

	/**
	 * ImageViewDAO.findViewCount
	 * 
	 * @see org.pimslims.crystallization.dao.view.AbstractSQLViewDAO#findViewCount(org.pimslims.business.criteria.BusinessCriteria)
	 */
	@Override
	public Integer findViewCount(final BusinessCriteria criteria)
			throws BusinessException {
		processCriteria(criteria);
		return super.findViewCount(criteria);
	}

	private void processCriteria(final BusinessCriteria criteria)
			throws BusinessException {
		BusinessCriterion wellCriterion = null;
		for (final BusinessCriterion businessCriterion : criteria.getCriteria()) {
			if (businessCriterion.getProperty().equals(ImageView.PROP_WELL)) {
				wellCriterion = businessCriterion;
			}
		}
		if (wellCriterion != null) {
			criteria.remove(wellCriterion);
			TrialDropViewDAO.addWellCriterion(criteria, wellCriterion);
		}
		return;

	}

	/**
	 * ImageViewDAO.findViews
	 * 
	 * @see org.pimslims.crystallization.dao.view.AbstractSQLViewDAO#findViews(org.pimslims.business.criteria.BusinessCriteria)
	 */
	@Override
	public Collection<ImageView> findViews(final BusinessCriteria criteria)
			throws BusinessException {
		processCriteria(criteria);
		return super.findViews(criteria);
	}

	public ImageViewDAO(final ReadableVersion version) {
		super(version);
	}

	@Override
	public String convertPropertyName(final String propertyName)
			throws BusinessException {
		if (propertyName == null) {
			throw new IllegalArgumentException("Null filter name");
		}
		if (propertyName.equals(ImageView.PROP_BARCODE)) {
			return "plate.name";
		} else if (propertyName.equals(ImageView.PROP_DATE)) {
			return "inspection.completionTime";
		} else if (propertyName.equals(ImageView.PROP_INSPECTION_NAME)) {
			return "inspection.name";
		} else if (propertyName.equals("row")) {
			return "sample.ROWPOSITION";
		} else if (propertyName.equals("col")) {
			return "sample.COLPOSITION";
		} else if (propertyName.equals("sub")) {
			return "sample.SUBPOSITION";
		} else if (propertyName.equals(ImageView.PROP_DESCRIPTION)) {
			return "image.details";
		} else if (propertyName.equals(ImageView.PROP_HEIGHT)) {
			return "imageType.sizeY";
		} else if (propertyName.equals(ImageView.PROP_HEIGHT_PER_PIXEL)) {
			return "imageType.ylengthPerPixel";
		} else if (propertyName.equals(ImageView.PROP_INSTRUMENT)) {
			return "instrument.name";
		} else if (propertyName.equals(ImageView.PROP_TEMPERATURE)) {
			return "location.temperature";
		} else if (propertyName.equals(ImageView.PROP_SCREEN)) {
			return "screen.name";
		} else if (propertyName.equals(ImageView.PROP_TIME_POINT)) {
			return "timePoint";
		} else if (propertyName.equals(ImageView.PROP_TYPE)) {
			return "imageType.catorgory";
		} else if (propertyName.equals(ImageView.PROP_URL)) {
			return "image.fileName";
		} else if (propertyName.equals(ImageView.PROP_WIDTH)) {
			return "imageType.sizeX";
		} else if (propertyName.equals(ImageView.PROP_WIDTH_PER_PIXEL)) {
			return "imageType.xlengthPerPixel";
		} else if (propertyName.equals(ImageView.PROP_WELL)) {
			return "sample.ROWPOSITION";
		}
		throw new BusinessException("Unable to find matching property in "
				+ this.getClass());

	}

	@Override
	String getCountableName() {
		return "image";
	}

	@Override
	Class<? extends ModelObject> getRootClass() {
		return Holder.class;
	}

	@Override
	public String getViewSql(final BusinessCriteria criteria) {
		// TODO fix this for WellImageType - fix ImageViewTest when done
		final String selectHQL = "select plate.name as barcode, "
				+ "inspection.completionTime as completionTime, "
				+ "image.fileName as fileName, "
				+ "image.filePath as filePath, "
				+ "sample.ROWPOSITION as row, "
				+ "sample.COLPOSITION as col, "
				+ "sample.SUBPOSITION as sub,"
				+ "instrument.NAME as instrumentName, "
				+ "instrument.temperature as temperature, "
				+ "imagetype.sizeX as sizeX, "
				+ "imagetype.sizeY as sizeY, "
				+ "imagetype.xlengthPerPixel as xlengthPerPixel, "
				+ "imagetype.ylengthPerPixel as ylengthPerPixel, "
				+ "imagetype.url as url, "
				+ "imagetype.catorgory as catorgory, "
				+ "screen.NAME as screenname, "
				+ "inspection.NAME as inspectionname, "
				+ "cast(inspection.COMPLETIONTIME-plate0.STARTDATE as text) as timePoint, "
				+ "image0.DETAILS as description, "
				+ "plate1.accessid as accessid "
				+ "from HOLD_ABSTRACTHOLDER plate "
				+ "inner join hold_holder plate0 on plate0.abstractholderid=plate.LabBookEntryID "
				+ "inner join CORE_LABBOOKENTRY plate1 on plate1.DBID=plate.LabBookEntryID "
				+ "join SCHE_SCHEDULEDTASK inspection on  inspection.holderid=plate.LabBookEntryID "
				+ "join CRYZ_IMAGE image on image.scheduledtaskid=inspection.labbookentryid "

				// TODO shouldn't this be an inner join?
				+ "left join SAM_SAMPLE sample on image.sampleid=sample.abstractsampleid "

				+ "left join CORE_LABBOOKENTRY image0 on image.LabBookEntryID=image0.DBID "
				+ "left join HOLD_REFHOLDEROFFSET refholdero5_ on plate.LabBookEntryID=refholdero5_.HOLDERID "
				+ "left join HOLD_ABSTRACTHOLDER screen on refholdero5_.REFHOLDERID=screen.LabBookEntryID "
				+ "left join EXPE_INSTRUMENT instrument on inspection.INSTRUMENTID=instrument.LabBookEntryID "
				// TODO or CRYZ_WELLIMAGETYPE on
				// image.WELLIMAGETYPEID=wellimagetype.LabBookEntryID
				+ "left join REF_IMAGETYPE imagetype on image.IMAGETYPEID=imagetype.PublicEntryID ";
		return buildViewQuerySQL(criteria, selectHQL, null, "plate1",
				Image.class);
	}

	@Override
	public Collection<ImageView> runSearch(final org.hibernate.SQLQuery hqlQuery) {
		final Collection<ImageView> views = new LinkedList<ImageView>();
		final SQLQuery q = hqlQuery;
		/*
		 * q.addScalar("barcode", StandardBasicTypes.STRING);
		 * q.addScalar("completionTime", StandardBasicTypes.CALENDAR);
		 * q.addScalar("fileName", StandardBasicTypes.STRING);
		 * q.addScalar("filePath", StandardBasicTypes.STRING);
		 * q.addScalar("row", StandardBasicTypes.INTEGER); q.addScalar("col",
		 * StandardBasicTypes.INTEGER); q.addScalar("sub",
		 * StandardBasicTypes.INTEGER); q.addScalar("instrumentName",
		 * StandardBasicTypes.STRING); q.addScalar("temperature",
		 * StandardBasicTypes.FLOAT); q.addScalar("sizeX",
		 * StandardBasicTypes.INTEGER); q.addScalar("sizeY",
		 * StandardBasicTypes.INTEGER); q.addScalar("xlengthPerPixel",
		 * StandardBasicTypes.FLOAT); q.addScalar("ylengthPerPixel",
		 * StandardBasicTypes.FLOAT); q.addScalar("url",
		 * StandardBasicTypes.STRING); q.addScalar("catorgory",
		 * StandardBasicTypes.STRING); q.addScalar("screenname",
		 * StandardBasicTypes.STRING); q.addScalar("inspectionname",
		 * StandardBasicTypes.STRING); q.addScalar("timePoint",
		 * StandardBasicTypes.STRING); q.addScalar("description",
		 * StandardBasicTypes.STRING);
		 */

		final List<Object[]> results = hqlQuery.list();
		for (final Object object : results) {
			final Object result[] = (Object[]) object;

			final ImageView view = new ImageView();
			views.add(view);
			view.setBarcode((String) result[0]);
			view.setDate(getCalDate(result[1]));
			final Integer row = (Integer) result[4];
			final Integer col = (Integer) result[5];
			final Integer sub = (Integer) result[6];
			view.setWell(getWell(row, col, sub));
			view.setInstrument((String) result[7]);
			if (result[8] != null) {
				view.setTemperature((AbstractSQLViewDAO.getFloat(result[8]))
						.doubleValue());
			}
			view.setWidth((Integer) result[9]);
			view.setHeight((Integer) result[10]);
			if (result[11] != null) {
				view.setWidthPerPixel((AbstractSQLViewDAO.getFloat(result[11]))
						.doubleValue());
			}
			if (result[12] != null) {
				view.setHeightPerPixel((AbstractSQLViewDAO.getFloat(result[12]))
						.doubleValue());
			}
			view.setImageType((String) result[14]);
			view.setScreen((String) result[15]);
			view.setInspectionName((String) result[16]);
			view.setTimePoint((String) result[17]);
			if (result[2] == null && result[3] == null) {
				continue; // no image
			}
			view.setDescription((String) result[18]);
			view.setUrl(ImageViewDAO.getFullImagePath((String) result[2],
					(String) result[3], (String) result[13]));
		}
		return views;
	}

	static String getFullImagePath(final String fileName,
			final String filePath, final String imagerPath) {
		if (filePath == null) {
			return imagerPath + fileName;
		} else if (filePath.toLowerCase().startsWith("http"))
		/*
		 * never happens, would need changes to xtalPiMS ||
		 * filePath.toLowerCase().startsWith("file"))
		 */
		{
			return filePath + fileName;
		} else if (filePath.contains("/ViewFile/")) {
			return filePath + fileName;
		} else {
			assert null != imagerPath : "Cannot determine path to image";
			return imagerPath + filePath + fileName;
		}
	}
}
