/*
 * SampleServiceImplTest.java JUnit based test Created on 09 August 2007, 16:56
 */

package org.pimslims.crystallization.implementation;

import java.util.Calendar;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.service.PersonService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.ImageType;
import org.pimslims.business.crystallization.model.SampleQuantity;
import org.pimslims.business.crystallization.model.Software;
import org.pimslims.business.crystallization.model.SoftwareScore;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.UserScore;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.HumanScoreService;
import org.pimslims.business.crystallization.service.ImageService;
import org.pimslims.business.crystallization.service.ScoringSchemeService;
import org.pimslims.business.crystallization.service.SoftwareScoreService;
import org.pimslims.business.crystallization.service.SoftwareService;
import org.pimslims.business.crystallization.util.ColorUtil;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.sample.Sample;

/**
 * 
 * @author ian
 */
public class ScoreServiceImplTest extends ScoringSchemeServiceImplTest {
	private static final String BARCODE = "ScoreServiceTest"
			+ System.currentTimeMillis();

	private static final Calendar NOW = Calendar.getInstance();

	public ScoreServiceImplTest(final String testName) {
		super(testName);

	}

	public static Test suite() {
		final TestSuite suite = new TestSuite(ScoreServiceImplTest.class);

		return suite;
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

	}

	public void testHumanScoreView() throws BusinessException,
			ConstraintException {

		this.dataStorage.openResources("administrator");
		try {
			// create a person
			final Person person = createPerson();

			final TrialDrop trialDrop = createTrialDrop();
			// create pims ScoringScheme
			final UserScore userScore = createHumanScore(person, trialDrop);

			// create pims userScore
			final HumanScoreService service = this.dataStorage
					.getHumanScoreService();
			service.create(userScore);
			dataStorage.getVersion().getSession().flush();
			// check view
			final BusinessCriteria criteria = new BusinessCriteria(service);
			criteria.add(BusinessExpression.Equals(ScoreView.PROP_BARCODE,
					BARCODE, true));
			final Collection<ScoreView> views = service.findViews(criteria);
			assertEquals(1, views.size());
			final ScoreView view = views.iterator().next();
			assertEquals(BARCODE, view.getBarcode());
			assertEquals("human", view.getType());
			assertEquals(ColorUtil.convertColorToHex(scoreValue1.getColour()
					.getRGB()), view.getColour());
			assertEquals(NOW, view.getDate());
			assertEquals("", view.getInspectionName());
			// was assertEquals("user" + UNIQUE, view.getName());
			assertEquals("K12.3", view.getWell());
			assertEquals(scoreValue1.getDescription(), view.getDescription());

			// check count
			assertEquals(1, service.findViewCount(criteria).intValue());
		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	public void testSoftwareScoreView() throws BusinessException,
			ConstraintException {
		this.dataStorage.openResources("administrator");
		try {
			// create a image
			final Image xtImage = createImage();

			// create a software
			final Software xSoftware = createSoftware();

			final SoftwareScore softScore = createSoftwareScore(xtImage,
					xSoftware);

			final WritableVersion version = this.dataStorage
					.getWritableVersion();

			// create pims userScore
			final SoftwareScoreService service = this.dataStorage
					.getSoftwareScoreService();
			service.create(softScore);
			final Long pimsID = softScore.getId();
			dataStorage.getVersion().getSession().flush();
			// check view
			final BusinessCriteria criteria = new BusinessCriteria(service);
			criteria.add(BusinessExpression.Equals(ScoreView.PROP_BARCODE,
					BARCODE, true));
			final Collection<ScoreView> views = service.findViews(criteria);
			assertEquals(1, views.size());
			final ScoreView view = views.iterator().next();
			assertEquals(BARCODE, view.getBarcode());
			assertEquals("software", view.getType());
			assertEquals(ColorUtil.convertColorToHex(scoreValue1.getColour()),
					view.getColour());
			assertEquals(NOW, view.getDate());
			// assertEquals("inspect" + BARCODE, view.getInspectionName());
			assertEquals("software" + UNIQUE, view.getName());
			assertEquals("K12.3", view.getWell());
			assertEquals(scoreValue1.getDescription(), view.getDescription());

			// check count
			assertEquals(1, service.findViewCount(criteria).intValue());
		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	/**
	 * Test of create user Score
	 */
	public void testCreateuUserScore() throws Exception {
		this.dataStorage.openResources("administrator");
		try {
			// create a person
			final Person person = createPerson();

			final TrialDrop trialDrop = createTrialDrop();
			// create pims ScoringScheme
			final UserScore userScore = createHumanScore(person, trialDrop);

			final WritableVersion version = this.dataStorage
					.getWritableVersion();

			// create pims userScore
			final HumanScoreService service = this.dataStorage
					.getHumanScoreService();
			service.create(userScore);
			final Long pimsID = userScore.getId();

			// verify pims userScore
			assertNotNull(pimsID);
			final org.pimslims.model.crystallization.DropAnnotation pimsObject = version
					.get(pimsID);
			assertNotNull(pimsObject);
			// was assertEquals(person.getFamilyName(),
			// pimsObject.getCreatorPerson().getFamilyName());
			assertEquals(trialDrop.getName(), pimsObject.getSample().getName());
			assertEquals(scoreValue1.getValue(), pimsObject.getScore()
					.getValue().intValue());
			assertEquals(userScore.getDate(), pimsObject.getScoreDate());
			assertNull(pimsObject.getSoftware());
		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	private UserScore createHumanScore(final Person person,
			final TrialDrop trialDrop) throws BusinessException {
		final ScoringSchemeService ssService = this.dataStorage
				.getScoringSchemeService();
		scoringScheme.setName("name" + System.currentTimeMillis());
		ssService.create(scoringScheme);

		// create UserScore
		final UserScore userScore = new UserScore();
		userScore.setDate(NOW);
		userScore.setDrop(trialDrop);
		userScore.setUser(person);
		userScore.setValue(scoreValue1);
		return userScore;
	}

	private TrialDrop createTrialDrop() throws BusinessException,
			ConstraintException {
		// create a trialDrop
		final TrialDrop trialDrop = new TrialDrop();
		trialDrop.setName("trialDropName" + UNIQUE);
		trialDrop.setWellPosition(new WellPosition(11, 12, 3));
		// SampleQuantity
		final org.pimslims.business.core.model.Sample xSample = new org.pimslims.business.core.model.Sample();
		xSample.setName("xsample" + UNIQUE);
		final SampleQuantity sq = new SampleQuantity(xSample, 0.001, "L");
		trialDrop.addSample(sq);
		// create pims trialDrop
		final TrialDropServiceImpl tdService = this.dataStorage
				.getTrialDropService();
		tdService.create(trialDrop);
		// create a holder for the sample
		final WritableVersion version = this.dataStorage.getWritableVersion();
		final Sample sample = version.get(trialDrop.getId());
		final Holder holder = new Holder(version, BARCODE);
		sample.setHolder(holder);
		// ??? PlateInspectionImplTest.createInspection(version, sample,
		// "inspect" + BARCODE, 1, NOW);
		return trialDrop;
	}

	private Person createPerson() throws BusinessException {
		final PersonService personService = this.dataStorage.getPersonService();
		final Person person = new Person();
		person.setFamilyName("family" + UNIQUE);
		person.setUsername("user" + UNIQUE);
		personService.create(person);
		return person;
	}

	/**
	 * Test of create SoftwareScore
	 */
	public void testCreateSoftwareScore() throws Exception {
		this.dataStorage.openResources("administrator");
		try {
			// create a image
			final Image xtImage = createImage();

			// create a software
			final Software xSoftware = createSoftware();

			final SoftwareScore softScore = createSoftwareScore(xtImage,
					xSoftware);

			final WritableVersion version = this.dataStorage
					.getWritableVersion();

			// create pims userScore
			final SoftwareScoreService service = this.dataStorage
					.getSoftwareScoreService();
			service.create(softScore);
			final Long pimsID = softScore.getId();

			// verify pims userScore
			assertNotNull(pimsID);
			final org.pimslims.model.crystallization.DropAnnotation pimsObject = version
					.get(pimsID);
			assertNotNull(pimsObject);
			assertNull(pimsObject.getCreator());
			assertEquals(xtImage.getImageDate(), pimsObject.getImage()
					.getCreationDate());
			assertEquals(xSoftware.getName(), pimsObject.getSoftware()
					.getName());
			assertEquals(scoreValue1.getValue(), pimsObject.getScore()
					.getValue().intValue());
			assertEquals(softScore.getDate(), pimsObject.getScoreDate());
		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	private SoftwareScore createSoftwareScore(final Image xtImage,
			final Software xSoftware) throws BusinessException {
		// create pims ScoringScheme
		final ScoringSchemeService ssService = this.dataStorage
				.getScoringSchemeService();
		scoringScheme.setName("name" + System.currentTimeMillis());
		ssService.create(scoringScheme);

		// create SoftwareScore
		final SoftwareScore softScore = new SoftwareScore();

		softScore.setImage(xtImage);
		softScore.setSoftwareAnnotator(xSoftware);
		softScore.setValue(scoreValue1);
		softScore.setDate(NOW);
		return softScore;
	}

	private Software createSoftware() throws BusinessException {
		final Software xSoftware = new Software();
		xSoftware.setDescription("description" + UNIQUE);
		xSoftware.setName("software" + UNIQUE);
		xSoftware.setVendorAddress("vendorAddress" + UNIQUE);
		xSoftware.setVendorName("vendorName" + UNIQUE);
		xSoftware.setVersion("sofwareversion" + UNIQUE);
		final SoftwareService swervice = this.dataStorage.getSoftwareService();
		swervice.create(xSoftware);
		return xSoftware;
	}

	private Image createImage() throws BusinessException, ConstraintException {
		final Image xtImage = new Image();
		xtImage.setColourDepth(1024);
		xtImage.setImageDate(Calendar.getInstance());
		xtImage.setImagePath("../path/images/image_"
				+ System.currentTimeMillis());
		xtImage.setSizeX(2560);
		xtImage.setSizeY(1280);
		xtImage.setImageType(ImageType.ZOOMED);
		final ImageService imService = this.dataStorage.getImageService();
		((ImageServiceImpl) imService).create(xtImage);
		final WritableVersion version = this.dataStorage.getWritableVersion();
		final org.pimslims.model.crystallization.Image image = version
				.get(xtImage.getId());
		final TrialDrop trialDop = createTrialDrop();
		final Sample sample = version.get(trialDop.getId());
		image.setSample(sample);
		return xtImage;
	}

}
