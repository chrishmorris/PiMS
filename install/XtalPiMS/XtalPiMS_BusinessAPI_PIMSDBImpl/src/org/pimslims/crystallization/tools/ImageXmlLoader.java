package org.pimslims.crystallization.tools;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Location;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.exception.BusinessException;
import org.w3c.dom.Element;

public class ImageXmlLoader extends XmlLoader {

	private final String date;

	static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'KK:mm:ss");

	public ImageXmlLoader(InputStream input, DataStorage dataStorage) {
		super(dataStorage, parse(input));
		this.date = this.getChildElement(this.document, "imagegroup")
				.getAttribute("date");
	}

	@Override
	protected String getNameSpace() {
		return "http://www.helsinki.fi/~vpkestil/schemas/imagesSchema";
	}

	@Override
	public String save() throws BusinessException {

		String instrument = this.getChildContent(this.document, "instrument");
		Location location = this.dataStorage.getLocationService().findByName(
				instrument);
		// note that this must have a default image type

		String barcode = this.getChildContent(this.document, "barcode");
		TrialPlate plate = this.dataStorage.getTrialService().findTrialPlate(
				barcode);
		assert null != plate : "Plate not found: " + barcode;
		plate.buildAllTrialDrops();
		PlateInspectionService pis = this.dataStorage
				.getPlateInspectionService();
		PlateInspection inspection = new PlateInspection();
		inspection.setInspectionName(plate.getBarcode() + " " + this.date);
		inspection.setDetails("");
		// TODO inspection.setInspectionNumber(inspectionNumber);
		inspection.setPlate(plate);
		inspection.setLocation(location);

		try {
			Date time = DATE_FORMAT.parse(this.date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(time);
			inspection.setInspectionDate(calendar);
		} catch (ParseException e) {
			throw new BusinessException(e);
		}

		// process images
		Collection<Image> images = new ArrayList();
		/*
		 * for (Iterator iterator = images.iterator(); iterator.hasNext();) {
		 * Image image = (Image) iterator.next();
		 * 
		 * }
		 */
		inspection.setImages(images);
		Element imageGroup = this.getChildElement(this.document, "imagegroup");
		for (Iterator<Element> iterator = this.getChildrenIterator(imageGroup,
				"file"); iterator.hasNext();) {
			Element element = iterator.next();

			Image image = new Image();
			image.setPlateInspection(inspection);
			String row = element.getAttribute("row");
			String column = element.getAttribute("column");
			String sub = element.getAttribute("droplocation");
			WellPosition position = new WellPosition(row + column + "." + sub);
			TrialDrop drop = plate.getTrialDrop(position);
			image.setDrop(drop);
			assert null != drop : "No such drop: " + position;
			image.setImagePath(this.getChildContent(element, "path") + "/"
					+ this.getChildContent(element, "name"));
			image.setLocation(location);

			images.add(image);
		}

		pis.create(inspection);
		return inspection.getInspectionName();
	}
}
