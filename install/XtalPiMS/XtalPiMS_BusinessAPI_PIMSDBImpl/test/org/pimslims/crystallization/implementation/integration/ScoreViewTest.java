package org.pimslims.crystallization.implementation.integration;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

import org.pimslims.business.core.model.Location;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.SoftwareScore;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.UserScore;
import org.pimslims.business.crystallization.service.HumanScoreService;
import org.pimslims.business.crystallization.service.PlateExperimentService;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.crystallization.service.SoftwareScoreService;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.util.ColorUtil;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.crystallization.implementation.TrialServiceImplTest;
import org.pimslims.dao.WritableVersion;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.AbstractModelObject;
import org.pimslims.model.crystallization.DropAnnotation;
import org.pimslims.model.holder.Holder;

public class ScoreViewTest extends
		org.pimslims.crystallization.integration.ScoreViewTest {
	final TrialServiceImplTest trialTest;

	private static String PLATEBARCODE = UNIQUE;

	private static final Calendar NOW = Calendar.getInstance();

	public ScoreViewTest(final String methodName) {
		super(methodName, DataStorageFactory.getDataStorageFactory()
				.getDataStorage());
		trialTest = new TrialServiceImplTest(this.dataStorage);

	}

	public void testScoreView_Plate_Drop_UserScore() throws BusinessException,
			ConstraintException {
		this.dataStorage.openResources("administrator");
		try {
			// create a plate experiment
			final WritableVersion version = (WritableVersion) ((DataStorageImpl) this.dataStorage)
					.getVersion();
			final TrialService trialservice = this.dataStorage
					.getTrialService();
			final TrialPlate plate = trialTest.createFilledTrialPlate(version,
					PLATEBARCODE);
			trialservice.saveTrialPlate(plate);

			// create a trialPlate with 2 trialDrop
			final TrialDrop trialDrop1 = plate.getTrialDrop("A02.1");

			trialDrop1.setPlate(plate);
			// create Human Score for drop1
			final UserScore userScore = getUserScore(NOW);
			userScore.setDrop(trialDrop1);

			// save beans
			save(userScore);
			version.flush();
			((WritableVersionImpl) version).getFlushMode().setCheckValue(true);
			// find Views
			final HumanScoreService humanScoreService = this.dataStorage
					.getHumanScoreService();
			final BusinessCriteria criteria = new BusinessCriteria(
					humanScoreService);
			criteria.add(BusinessExpression.Equals(ScoreView.PROP_BARCODE,
					PLATEBARCODE, true));
			criteria.add(BusinessExpression.Equals("row", 1, true));
			criteria.add(BusinessExpression.Equals("col", 2, true));
			criteria.add(BusinessExpression.Equals("sub", 1, true));

			final Collection<ScoreView> views = humanScoreService
					.findViews(criteria);

			// check pims object
			final AbstractModelObject holder = version.findFirst(Holder.class,
					Holder.PROP_NAME, PLATEBARCODE);
			// check views
			assertEquals("view query problem", 1, views.size());
			final ScoreView view = views.iterator().next();
			assertEquals(PLATEBARCODE, view.getBarcode());
			assertEquals("human", view.getType());
			assertEquals(ColorUtil.convertColorToHex(userScore.getValue()
					.getColour()), view.getColour());
			assertEquals(NOW, view.getDate());
			// can no longer change createor
			// assertEquals(userScore.getUser().getUsername(), view.getName());
			assertEquals(userScore.getValue().getDescription(),
					view.getDescription());
			assertEquals(trialDrop1.getWellPosition().toString(),
					view.getWell());

			// check count
			assertEquals(1, humanScoreService.findViewCount(criteria)
					.intValue());

			// check plate's number of crystals
			final PlateExperimentService plateService = dataStorage
					.getPlateExperimentService();
			final BusinessCriteria plateCriteria = new BusinessCriteria(
					plateService);
			plateCriteria
					.add(BusinessExpression.Equals(
							PlateExperimentView.PROP_BARCODE,
							plate.getBarcode(), true));
			final Collection<PlateExperimentView> plateViews = plateService
					.findViews(plateCriteria);
			final PlateExperimentView plateview = plateViews.iterator().next();
			assertEquals(plate.getBarcode(), plateview.getBarcode());
			assertEquals(1, plateview.getNumberOfCrystals().intValue());

		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	public void testScoreView_Plate_Drop_Image_SoftwareScore()
			throws BusinessException, ConstraintException {
		this.dataStorage.openResources("administrator");
		try {
			// create a plate experiment
			final WritableVersion version = (WritableVersion) ((DataStorageImpl) this.dataStorage)
					.getVersion();
			final TrialService trialservice = this.dataStorage
					.getTrialService();
			final TrialPlate plate = trialTest.createFilledTrialPlate(version,
					PLATEBARCODE);
			trialservice.saveTrialPlate(plate);
			final PlateInspection plateInspection = createInspection(plate);
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

			// create software Score for drop1
			final SoftwareScore softScore = getSoftwareScore(NOW);
			final Image image = images.iterator().next();
			softScore.setImage(image);

			// save beans
			save(softScore);
			version.flush();
			((WritableVersionImpl) version).getFlushMode().setCheckValue(true);
			// verify pims mapping
			final org.pimslims.model.crystallization.Image pimage = version
					.get(image.getId());
			assertEquals("creation problem", 1, pimage.getDropAnnotations()
					.size());
			final DropAnnotation humanScore = pimage.getDropAnnotations()
					.iterator().next();
			// find Views
			final SoftwareScoreService softwareScoreService = this.dataStorage
					.getSoftwareScoreService();
			final BusinessCriteria criteria = new BusinessCriteria(
					softwareScoreService);
			criteria.add(BusinessExpression.Equals(ScoreView.PROP_BARCODE,
					PLATEBARCODE, true));
			final Collection<ScoreView> views = softwareScoreService
					.findViews(criteria);

			// check views
			assertEquals(1, views.size());
			final ScoreView view = views.iterator().next();
			assertEquals(PLATEBARCODE, view.getBarcode());
			assertEquals("software", view.getType());
			assertEquals(ColorUtil.convertColorToHex(softScore.getValue()
					.getColour()), view.getColour());
			assertEquals(NOW, view.getDate());
			assertEquals(softScore.getSoftwareAnnotator().getName(),
					view.getName());
			assertEquals(softScore.getSoftwareAnnotator().getVersion(),
					view.getVersion());

			assertEquals(softScore.getValue().getDescription(),
					view.getDescription());
			assertEquals(image.getDrop().getWellPosition().toString(),
					view.getWell());

			// check count
			assertEquals(1, service.findViewCount(criteria).intValue());

		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	private PlateInspection createInspection(final TrialPlate trialPlate)
			throws BusinessException {
		final PlateInspection inspection = new PlateInspection();
		inspection.setPlate(trialPlate);
		final Location location = super.createLocation("Oasis 12345");
		inspection.setLocation(location);
		inspection.setInspectionDate(Calendar.getInstance());
		inspection.setInspectionName("inspectionName"
				+ System.currentTimeMillis());
		return inspection;
	}
}
