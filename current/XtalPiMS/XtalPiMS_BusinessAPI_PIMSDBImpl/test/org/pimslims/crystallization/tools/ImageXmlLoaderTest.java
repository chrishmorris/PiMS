/*
 * PlateExperimentServiceImplTest.java JUnit based test Created on 09 August 2007, 16:56
 */

package org.pimslims.crystallization.tools;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.service.PersonService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.crystallization.view.TrialDropView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.AbstractXtalTest;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.crystallization.Image;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.ImageType;
import org.pimslims.model.schedule.ScheduledTask;

/**
 * 
 * @author Chris Morris
 */
public class ImageXmlLoaderTest extends AbstractXtalTest {

	private static final String UNIQUE = "pxl" + System.currentTimeMillis();

	private static final Calendar NOW = Calendar.getInstance();

	private static final String DATE = String.format(
			"%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:%1$tS", NOW); // e.g.
															// 2001-12-31T12:00:00

	private static final String PATH = "var/xtalPiMS/images";

	private static final String TYPE = "jpg";

	private static final String FILENAME = UNIQUE + ".jpg";

	private static final String INSTRUMENT = "Imager" + UNIQUE;

	// static { System.out.println(DATE); }

	private static final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
			+ "<tns:images xmlns:tns=\"http://www.helsinki.fi/~vpkestil/schemas/imagesSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.helsinki.fi/~vpkestil/schemas/imagesSchema ../XML_Schemas/imagesSchema.xsd \">\r\n"
			+ "  <tns:instrument>"
			+ INSTRUMENT
			+ "</tns:instrument>\r\n"
			+ "  <tns:barcode>"
			+ BARCODE
			+ "</tns:barcode>\r\n"
			+ "  <tns:imagegroup date=\""
			+ DATE
			+ "\">\r\n"
			+ "    <tns:file column=\"1\" droplocation=\"1\" row=\"A\">\r\n"
			+ "      <tns:path>"
			+ PATH
			+ "</tns:path>\r\n"
			+ "      <tns:name>"
			+ FILENAME
			+ "</tns:name>\r\n"
			+ "      <tns:extension>tns:extension</tns:extension>\r\n"
			+ "      <tns:type>"
			+ TYPE
			+ "</tns:type>\r\n"
			+ "      <tns:rating>tns:rating</tns:rating>\r\n"
			+ "    </tns:file>\r\n"
			+ "  </tns:imagegroup>\r\n"
			+ "</tns:images>\r\n";

	public ImageXmlLoaderTest(final String testName) {
		super(testName, DataStorageFactory.getDataStorageFactory()
				.getDataStorage());
	}

	public static Test suite() {
		final TestSuite suite = new TestSuite(ImageXmlLoaderTest.class);

		return suite;
	}

	public void testSaveInspection() throws Exception {
		this.dataStorage.openResources("administrator");
		try {

			save();

			// test the inspection record
			PlateInspectionService pis = this.dataStorage
					.getPlateInspectionService();
			Collection<PlateInspection> inspections = pis.findByPlate(BARCODE);
			assertEquals(1, inspections.size());
			PlateInspection inspection = inspections.iterator().next();
			assertEquals(0, inspection.getInspectionNumber());
			assertEquals(BARCODE, inspection.getPlate().getBarcode());
			Date date = inspection.getInspectionDate().getTime();
			assertEquals(ImageXmlLoader.DATE_FORMAT.format(date),
					NOW.getTimeInMillis() / 1000, inspection
							.getInspectionDate().getTimeInMillis() / 1000);

		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	public void testSaveImage() throws Exception {
		this.dataStorage.openResources("administrator");
		try {

			save();

			// test the images
			final TrialService trialService = dataStorage.getTrialService();
			final BusinessCriteria criteria = new BusinessCriteria(trialService);
			criteria.add(BusinessExpression.Equals(TrialDropView.PROP_BARCODE,
					BARCODE, true));
			final Collection<TrialDropView> trialDropViews = trialService
					.findViews(criteria);
			assertEquals(1, trialDropViews.size());
			List<ImageView> images = trialDropViews.iterator().next()
					.getImages();
			assertEquals(1, images.size());
			ImageView image = images.iterator().next();
			assertEquals(BARCODE, image.getBarcode());
			assertEquals("A01.1", image.getWell());
			assertEquals(NOW.getTimeInMillis() / 1000, image.getDate()
					.getTimeInMillis() / 1000);
			assertEquals("/RhombixImages/" + PATH + "/" + FILENAME,
					image.getUrl());
			// could assertEquals(, image.getTimePoint());
			// could assertEquals(4, image.getTemperature());

		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	public void testAccessRights() throws Exception {
		this.dataStorage.openResources("administrator");
		try {

			save();

			ReadableVersion version = ((DataStorageImpl) this.dataStorage)
					.getVersion();
			Holder holder = version.findFirst(
					org.pimslims.model.holder.Holder.class, "name", BARCODE);
			ScheduledTask task = holder.getScheduledTasks().iterator().next();
			assertEquals(OrderXmlLoaderTest.GROUP_NAME, task.get_Owner());
			Image image = task.getImages().iterator().next();
			assertEquals(OrderXmlLoaderTest.GROUP_NAME, image.get_Owner());

		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	private void save() throws UnsupportedEncodingException, BusinessException {
		PersonService ps = this.dataStorage.getPersonService();
		Person user = new Person();
		user.setUsername(UNIQUE);
		ps.create(user);
		makeImager(((DataStorageImpl) this.dataStorage).getWritableVersion(),
				INSTRUMENT);

		OrderXmlLoaderTest.save(this.dataStorage);
		final InputStream input = new ByteArrayInputStream(
				xml.getBytes("UTF-8"));
		new ImageXmlLoader(input, this.dataStorage).save();
		this.dataStorage.flush();
	}

	private void makeImager(WritableVersion version, String name)
			throws BusinessException {
		try {
			// TODO use WellImageType
			ImageType imageType = new ImageType(version, name);
			imageType.setUrl("/RhombixImages/");
			new Instrument(version, name).setDefaultImageType(imageType);
		} catch (ConstraintException e) {
			throw new BusinessException(e);
		}
	}

}
