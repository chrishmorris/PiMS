package org.pimslims.crystallization.implementation.integration;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

import org.pimslims.business.core.model.Location;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.service.ImageService;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.crystallization.implementation.TrialServiceImplTest;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.reference.ImageType;
import org.pimslims.model.schedule.ScheduledTask;

public class ImageViewTest extends
		org.pimslims.crystallization.integration.ImageViewTest {
	public static final String IMAGE_SERVER = "http://www.oppf.ox.ac.uk/vault/images/lowres/";

	final TrialServiceImplTest trialTest;

	public ImageViewTest(final String methodName) {
		super(methodName, DataStorageFactory.getDataStorageFactory()
				.getDataStorage());
		trialTest = new TrialServiceImplTest(this.dataStorage);

	}

	public void testInspectionImageFullMapping() throws BusinessException,
			ConstraintException {
		this.dataStorage.openResources("administrator");
		try {
			// create a plate experiment
			final WritableVersion version = (WritableVersion) ((DataStorageImpl) this.dataStorage)
					.getVersion();
			final TrialService trialservice = this.dataStorage
					.getTrialService();
			final TrialPlate plate = trialTest.createFilledTrialPlate(version,
					BARCODE + 0);
			trialservice.saveTrialPlate(plate);
			final PlateInspection plateInspection = createInspection(plate,
					version);
			// create some images for each trialDrop
			final Collection<Image> images = new HashSet<Image>();
			for (final TrialDrop drop : plate.getTrialDrops()) {
				final Image image = new Image();
				image.setImagePath("image/" + plate.getBarcode() + "-"
						+ drop.getWellPosition() + ".jpg");
				image.setDrop(drop);
				image.setImageDate(Calendar.getInstance());
				image.setPlateInspection(plateInspection);
				images.add(image);
			}
			// add image to inspection
			plateInspection.setImages(images);

			// create inspection
			final PlateInspectionService service = dataStorage
					.getPlateInspectionService();
			service.create(plateInspection);
			// check pims' images
			final ScheduledTask task = version.findFirst(ScheduledTask.class,
					ScheduledTask.PROP_NAME,
					plateInspection.getInspectionName());
			assertEquals(96, task.getImages().size());
			final org.pimslims.model.crystallization.Image pImage = task
					.getImages().iterator().next();
			assertTrue(pImage.getImageType().getName()
					.startsWith("testImagerType"));
			assertEquals("image/", pImage.getFilePath());
			assertNotNull(pImage.getSample());
			assertNotNull(pImage.getScheduledTask());
			assertEquals(plate.getBarcode(), pImage.getScheduledTask()
					.getHolder().getName());
			assertTrue(pImage.getFileName().endsWith(".jpg"));
			assertTrue(pImage.getFileName().startsWith(plate.getBarcode()));
		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	public void testInspectionImageView() throws BusinessException,
			ConstraintException {
		this.dataStorage.openResources("administrator");
		try {
			// create a plate experiment
			final WritableVersion version = (WritableVersion) ((DataStorageImpl) this.dataStorage)
					.getVersion();
			final TrialService trialservice = this.dataStorage
					.getTrialService();
			final TrialPlate plate = trialTest.createFilledTrialPlate(version,
					BARCODE);
			trialservice.saveTrialPlate(plate);
			final PlateInspection plateInspection = createInspection(plate,
					version);
			// create some images for each trialDrop
			final Collection<Image> images = new HashSet<Image>();
			for (final TrialDrop drop : plate.getTrialDrops()) {
				final Image image = new Image();
				image.setImagePath("image/" + plate.getBarcode() + "-"
						+ drop.getWellPosition() + ".jpg");
				image.setDrop(drop);
				image.setImageDate(plateInspection.getInspectionDate());
				image.setPlateInspection(plateInspection);
				images.add(image);
			}
			// add image to inspection
			plateInspection.setImages(images);

			// create inspection
			final PlateInspectionService service = dataStorage
					.getPlateInspectionService();
			service.create(plateInspection);
			version.flush();
			// check pims' images
			final ScheduledTask task = version.findFirst(ScheduledTask.class,
					ScheduledTask.PROP_NAME,
					plateInspection.getInspectionName());
			assertEquals(96, task.getImages().size());

			// find Views
			final ImageService imageService = this.dataStorage
					.getImageService();
			final BusinessCriteria criteria = new BusinessCriteria(imageService);
			criteria.add(BusinessExpression.Equals(ImageView.PROP_BARCODE,
					plate.getBarcode(), true));
			final Collection<ImageView> imageViews = imageService
					.findViews(criteria);

			// check views
			assertEquals(96, imageViews.size());
			final ImageView imageView = imageViews.iterator().next();

			assertEquals(plate.getBarcode(), imageView.getBarcode());
			// oasis
			assertEquals(750, imageView.getWidth().intValue());
			assertEquals(700, imageView.getHeight().intValue());
			assertEquals(2.857f, imageView.getWidthPerPixel().floatValue());
			assertEquals(2.857f, imageView.getHeightPerPixel().floatValue());
			assertEquals("composite", imageView.getImageType());

			assertEquals(plate.getScreen().getName(), imageView.getScreen());
			assertEquals(plateInspection.getInspectionDate(),
					imageView.getDate());
			assertEquals(plateInspection.getInspectionName(),
					imageView.getInspectionName());
			assertEquals(plateInspection.getLocation().getName(),
					imageView.getInstrument());
			assertNotNull(imageView.getTimePoint());
			// System.out.println("TimePoint:" + imageView.getTimePoint());
			// System.out.println("URL:" + imageView.getUrl());
			// System.out.println("Well:" + imageView.getWell());
			assertTrue(imageView.getUrl().startsWith(
					"http://www.oppf.ox.ac.uk/vault/images/lowres/image/"
							+ plate.getBarcode() + "-"));
			assertNotNull(imageView.getWell());

		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	public static PlateInspection createInspection(final TrialPlate trialPlate,
			final WritableVersion version) throws ConstraintException {
		final String imagerName = "testImager" + System.currentTimeMillis();
		final Instrument instrument = new Instrument(version, imagerName);

		final ImageType imageType = new ImageType(version, "testImagerType"
				+ System.currentTimeMillis());
		imageType.setCatorgory("composite");
		imageType.setSizeX(750);
		imageType.setSizeY(700);
		imageType.setXlengthPerPixel(2.857f);
		imageType.setYlengthPerPixel(2.857f);
		imageType.setUrl(IMAGE_SERVER);
		instrument.setDefaultImageType(imageType);
		version.flush();
		final PlateInspection inspection = new PlateInspection();
		inspection.setPlate(trialPlate);
		final Location location = new Location();
		location.setName(imagerName);
		inspection.setLocation(location);
		inspection.setInspectionDate(Calendar.getInstance());
		inspection.setInspectionName("inspectionName"
				+ System.currentTimeMillis());
		return inspection;
	}
}
