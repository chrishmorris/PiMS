package org.pimslims.crystallization.implementation.integration;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.ImageType;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.model.SoftwareScore;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.UserScore;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.ConditionView;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.crystallization.view.TrialDropView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.crystallization.implementation.TrialServiceImplTest;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;

public class TrialDropViewTest extends org.pimslims.crystallization.business.AbstractXtalTest {
    final TrialServiceImplTest trialTest;

    public TrialDropViewTest(final String methodName) {
        super(methodName, DataStorageFactory.getDataStorageFactory().getDataStorage());
        trialTest = new TrialServiceImplTest(this.dataStorage);

    }

    public void testUser() throws BusinessException, ConstraintException, AccessException, AbortedException {

        // set up
        String username = UNIQUE + "u";
        this.dataStorage.openResources("administrator");
        createUser(username);

        this.dataStorage.openResources(username);
        try {

            // find all trialDropViews related to this inspection
            final TrialService trialService = this.dataStorage.getTrialService();
            final BusinessCriteria trialCriteria = new BusinessCriteria(trialService);
            Collection<TrialDropView> views = trialService.findViews(trialCriteria);
            assertEquals(0, views.size());

        } finally {
            this.dataStorage.abort();
        }

        // tidy up
        deleteUser(username);
    }

    private void deleteUser(String username) throws BusinessException, AccessException, ConstraintException {
        WritableVersion version;
        this.dataStorage.openResources("administrator");
        version = (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();
        try {
            User user = version.findFirst(User.class, User.PROP_NAME, username);
            Set<UserGroup> groups = user.getUserGroups();
            for (Iterator iterator = groups.iterator(); iterator.hasNext();) {
                UserGroup userGroup = (UserGroup) iterator.next();
                Set<Permission> permissions = userGroup.getPermissions();
                Set<LabNotebook> books = new HashSet();
                for (Iterator iterator2 = permissions.iterator(); iterator2.hasNext();) {
                    Permission permission = (Permission) iterator2.next();
                    books.add(permission.getLabNotebook());
                }
                version.delete(books);
            }
            version.delete(groups);
            user.delete();
            this.dataStorage.commit();
            this.dataStorage.closeResources();
        } finally {
            if (!version.isCompleted()) {
                this.dataStorage.abort();
            }
        }
    }

    private void createUser(String username) throws ConstraintException, BusinessException {
        WritableVersion version = (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();
        try {
            UserGroup group = new UserGroup(version, UNIQUE);
            LabNotebook book = new LabNotebook(version, UNIQUE);
            new Permission(version, "read", book, group);
            new Permission(version, "create", book, group);
            new Permission(version, "update", book, group);
            new Permission(version, "delete", book, group);
            new User(version, username).addUserGroup(group);
            this.dataStorage.commit();
            this.dataStorage.closeResources();
        } finally {
            if (!version.isCompleted()) {
                this.dataStorage.abort();
            }
        }
    }

    public void testInpsection_TrialDropViews() throws BusinessException, ConstraintException,
        AccessException {
        // set up

        this.dataStorage.openResources("administrator");

        try {
            //create a plate experiment
            final WritableVersion version =
                (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();
            final TrialService trialservice = this.dataStorage.getTrialService();
            final TrialPlate plate = trialTest.createFilledTrialPlate(version, BARCODE + 0);
            trialservice.saveTrialPlate(plate);
            final PlateInspection plateInspection = ImageViewTest.createInspection(plate, version);
            //create some images for each trialDrop
            TrialDrop drop_A1 = null;
            Image image_A1 = null;
            final Collection<Image> images = new HashSet<Image>();
            for (final TrialDrop drop : plate.getTrialDrops()) {
                final Image image = new Image();
                image.setImagePath("image/" + plate.getBarcode() + "-" + drop.getWellPosition() + ".jpg");
                image.setDrop(drop);
                image.setImageDate(Calendar.getInstance());
                image.setPlateInspection(plateInspection);
                images.add(image);

                if (drop.getWellPosition().toStringNoSubPosition().equals("A01")) {
                    drop_A1 = drop;
                    image_A1 = image;

                }
            }
            //add image to inspection
            plateInspection.setImages(images);

            //create inspection
            final PlateInspectionService service = dataStorage.getPlateInspectionService();
            service.create(plateInspection);

            //create Human Score for drop1
            final UserScore userScore = getUserScore(Calendar.getInstance());
            userScore.setDrop(drop_A1);
            save(userScore);
            final UserScore userScore2 = getUserScore(Calendar.getInstance());
            userScore2.setDrop(drop_A1);
            save(userScore2);
            //create software Score for drop1
            final SoftwareScore softScore = getSoftwareScore(Calendar.getInstance());
            softScore.setImage(image_A1);
            save(softScore);
            version.flush();
            //find trialDrop Views
            final TrialService trialService = dataStorage.getTrialService();
            final BusinessCriteria criteria = new BusinessCriteria(trialService);
            criteria.add(BusinessExpression.Equals(TrialDropView.PROP_BARCODE, plate.getBarcode(), true));
            criteria.add(BusinessExpression.Equals(ImageView.PROP_INSPECTION_NAME,
                plateInspection.getInspectionName(), true));
            final Collection<TrialDropView> trialDropViews = trialService.findViews(criteria);
            assertNotNull(trialDropViews);
            assertEquals("should has 96 well", 96, trialDropViews.size());//this plate has 96 well
            //verify a view
            TrialDropView trialDropView = null;
            for (final TrialDropView dropView : trialDropViews) {
                if (dropView.getWell().startsWith("A01")) {
                    trialDropView = dropView;
                    break;
                }
            }
            assertEquals(plate.getBarcode(), trialDropView.getBarcode());
            assertNotNull(trialDropView.getWell());

            //imageviews
            assertEquals("Only 1 image per drop per inspection", 1, trialDropView.getImages().size());
            final ImageView imageView = trialDropView.getImages().iterator().next();
            assertEquals(trialDropView.getWell(), imageView.getWell());
            assertEquals("composite", imageView.getImageType());
            assertEquals(plate.getScreen().getName(), imageView.getScreen());
            assertEquals(plateInspection.getInspectionDate(), imageView.getDate());
            assertEquals(plateInspection.getInspectionName(), imageView.getInspectionName());
            assertEquals(plateInspection.getLocation().getName(), imageView.getInstrument());
            assertNotNull(imageView.getTimePoint());

            //condition
            final ConditionView condition = trialDropView.getCondition();
            assertNotNull(condition);
            assertEquals("A01", condition.getWell());
            assertEquals(imageView.getScreen(), condition.getLocalName());
            assertTrue(condition.getComponents().size() > 0);
            //scores
            assertTrue(trialDropView.getHumanScores().size() > 0);
            assertEquals(drop_A1.getWellPosition().toString(), trialDropView.getHumanScores().iterator()
                .next().getWell());
            assertEquals(userScore2.getDate().getTimeInMillis(), trialDropView.getHumanScores().iterator()
                .next().getDate().getTimeInMillis());
            assertTrue(trialDropView.getSoftwareScores().size() > 0);
            assertEquals(drop_A1.getWellPosition().toString(), trialDropView.getSoftwareScores().iterator()
                .next().getWell());
            assertEquals(plateInspection.getInspectionName(), trialDropView.getSoftwareScores().iterator()
                .next().getInspectionName());
            //TODO sample
            //assertEquals("1 sample per drop per inspection", 1, trialDropView.getSamples().size());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }

    }

    public void testInpsection_TrialDropViewsWith1Image() throws BusinessException, ConstraintException {
        this.dataStorage.openResources("administrator");
        try {
            //create a plate experiment
            final WritableVersion version =
                (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();
            final TrialService trialservice = this.dataStorage.getTrialService();
            final TrialPlate plate = trialTest.createFilledTrialPlate(version, BARCODE + 0);
            trialservice.saveTrialPlate(plate);
            final PlateInspection plateInspection = ImageViewTest.createInspection(plate, version);
            //create some images for each trialDrop
            TrialDrop drop_A1 = null;
            Image image_A1 = null;
            final Collection<Image> images = new HashSet<Image>();
            for (final TrialDrop drop : plate.getTrialDrops()) {

                if (drop.getWellPosition().toStringNoSubPosition().equals("A01")) {
                    final Image image = new Image();
                    image.setImagePath("image/" + plate.getBarcode() + "-" + drop.getWellPosition() + ".jpg");
                    image.setDrop(drop);
                    image.setImageDate(Calendar.getInstance());
                    image.setPlateInspection(plateInspection);
                    images.add(image);
                    drop_A1 = drop;
                    image_A1 = image;

                }
            }
            //add image to inspection
            plateInspection.setImages(images);

            //create inspection
            final PlateInspectionService service = dataStorage.getPlateInspectionService();
            service.create(plateInspection);

            //create Human Score for drop1
            final UserScore userScore = getUserScore(Calendar.getInstance());
            userScore.setDrop(drop_A1);
            save(userScore);
            //create software Score for drop1
            final SoftwareScore softScore = getSoftwareScore(Calendar.getInstance());
            softScore.setImage(image_A1);
            save(softScore);
            version.flush();
            //find trialDrop Views
            final TrialService trialService = dataStorage.getTrialService();
            final BusinessCriteria criteria = new BusinessCriteria(trialService);
            criteria.add(BusinessExpression.Equals(TrialDropView.PROP_BARCODE, plate.getBarcode(), true));
            criteria.add(BusinessExpression.Equals(ImageView.PROP_INSPECTION_NAME,
                plateInspection.getInspectionName(), true));
            final Collection<TrialDropView> trialDropViews = trialService.findViews(criteria);
            assertNotNull(trialDropViews);
            assertEquals(1, trialDropViews.size());//this plate has 96 well but only 1 has image
            //verify a view
            TrialDropView trialDropView = null;
            for (final TrialDropView dropView : trialDropViews) {
                if (dropView.getWell().startsWith("A01")) {
                    trialDropView = dropView;
                    break;
                }
            }
            assertEquals(plate.getBarcode(), trialDropView.getBarcode());
            assertNotNull(trialDropView.getWell());

            //imageviews
            assertEquals("Only 1 image per drop per inspection", 1, trialDropView.getImages().size());
            final ImageView imageView = trialDropView.getImages().iterator().next();
            assertEquals(trialDropView.getWell(), imageView.getWell());
            assertEquals("composite", imageView.getImageType());
            assertEquals(plate.getScreen().getName(), imageView.getScreen());
            assertEquals(plateInspection.getInspectionDate(), imageView.getDate());
            assertEquals(plateInspection.getInspectionName(), imageView.getInspectionName());
            assertEquals(plateInspection.getLocation().getName(), imageView.getInstrument());
            assertNotNull(imageView.getTimePoint());

            //condition
            final ConditionView condition = trialDropView.getCondition();
            assertNotNull(condition);
            assertEquals("A01", condition.getWell());
            assertEquals(imageView.getScreen(), condition.getLocalName());
            assertTrue(condition.getComponents().size() > 0);
            //scores
            assertTrue(trialDropView.getHumanScores().size() > 0);
            assertEquals(drop_A1.getWellPosition().toString(), trialDropView.getHumanScores().iterator()
                .next().getWell());
            assertTrue(trialDropView.getSoftwareScores().size() > 0);
            assertEquals(drop_A1.getWellPosition().toString(), trialDropView.getSoftwareScores().iterator()
                .next().getWell());
            assertEquals(plateInspection.getInspectionName(), trialDropView.getSoftwareScores().iterator()
                .next().getInspectionName());
            //TODO sample
            //assertEquals("1 sample per drop per inspection", 1, trialDropView.getSamples().size());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void testInpsection_TrialDropViews_findbyWell() throws BusinessException, ConstraintException {
        this.dataStorage.openResources("administrator");
        try {
            //create a plate experiment
            final WritableVersion version =
                (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();
            final TrialService trialservice = this.dataStorage.getTrialService();
            final TrialPlate plate = trialTest.createFilledTrialPlate(version, BARCODE + 0);
            trialservice.saveTrialPlate(plate);
            final PlateInspection plateInspection = ImageViewTest.createInspection(plate, version);
            //create some images for each trialDrop
            TrialDrop drop_A1 = null;
            Image image_A1 = null;
            final Collection<Image> images = new HashSet<Image>();
            for (final TrialDrop drop : plate.getTrialDrops()) {
                final Image image = new Image();
                image.setImagePath("image/" + plate.getBarcode() + "-" + drop.getWellPosition() + ".jpg");
                image.setDrop(drop);
                image.setImageDate(Calendar.getInstance());
                image.setPlateInspection(plateInspection);
                images.add(image);
                image.setImageType(ImageType.COMPOSITE);

                if (drop.getWellPosition().toStringNoSubPosition().equals("A01")) {
                    drop_A1 = drop;
                    image_A1 = image;

                }
            }
            //add image to inspection
            plateInspection.setImages(images);

            //create inspection
            final PlateInspectionService service = dataStorage.getPlateInspectionService();
            service.create(plateInspection);

            //create Human Score for drop1
            final UserScore userScore = getUserScore(Calendar.getInstance());
            userScore.setDrop(drop_A1);
            save(userScore);
            //create software Score for drop1
            final SoftwareScore softScore = getSoftwareScore(Calendar.getInstance());
            softScore.setImage(image_A1);
            save(softScore);
            version.flush();
            //find trialDrop Views
            final TrialService trialService = dataStorage.getTrialService();
            final BusinessCriteria criteria = new BusinessCriteria(trialService);
            criteria.add(BusinessExpression.Equals(TrialDropView.PROP_BARCODE, plate.getBarcode(), true));
            criteria.add(BusinessExpression.Equals(ImageView.PROP_WELL, "A01.1", true));
            final Collection<TrialDropView> trialDropViews = trialService.findViews(criteria);
            assertNotNull(trialDropViews);
            assertEquals("should has 1 image for well A01.1", 1, trialDropViews.size());//this plate has 96 well
            //verify a view
            final TrialDropView trialDropView = trialDropViews.iterator().next();
            assertEquals(plate.getBarcode(), trialDropView.getBarcode());
            assertEquals("A01.1", trialDropView.getWell());

            //imageviews
            assertEquals("Only 1 image per drop per inspection", 1, trialDropView.getImages().size());
            final ImageView imageView = trialDropView.getImages().iterator().next();
            assertEquals(trialDropView.getWell(), imageView.getWell());
            assertEquals("composite", imageView.getImageType());
            assertEquals(plate.getScreen().getName(), imageView.getScreen());
            assertEquals(plateInspection.getInspectionDate(), imageView.getDate());
            assertEquals(plateInspection.getInspectionName(), imageView.getInspectionName());
            assertEquals(plateInspection.getLocation().getName(), imageView.getInstrument());
            assertNotNull(imageView.getTimePoint());
            assertEquals(ImageType.COMPOSITE.toString().toLowerCase(), imageView.getImageType());

            //condition
            final ConditionView condition = trialDropView.getCondition();
            assertNotNull(condition);
            assertEquals("A01", condition.getWell());
            assertEquals(imageView.getScreen(), condition.getLocalName());
            assertTrue(condition.getComponents().size() > 0);
            //scores
            assertTrue(trialDropView.getHumanScores().size() > 0);
            assertEquals(drop_A1.getWellPosition().toString(), trialDropView.getHumanScores().iterator()
                .next().getWell());
            assertTrue(trialDropView.getSoftwareScores().size() > 0);
            assertEquals(drop_A1.getWellPosition().toString(), trialDropView.getSoftwareScores().iterator()
                .next().getWell());
            assertEquals(plateInspection.getInspectionName(), trialDropView.getSoftwareScores().iterator()
                .next().getInspectionName());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void testInpsection_TrialDropViewsFindByImageType() throws BusinessException, ConstraintException {
        this.dataStorage.openResources("administrator");
        try {
            //create a plate experiment
            final WritableVersion version =
                (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();
            final TrialService trialservice = this.dataStorage.getTrialService();
            final TrialPlate plate = trialTest.createFilledTrialPlate(version, BARCODE + 0);
            trialservice.saveTrialPlate(plate);
            final PlateInspection plateInspection = ImageViewTest.createInspection(plate, version);

            final Collection<Image> images = new HashSet<Image>();
            for (final TrialDrop drop : plate.getTrialDrops()) {

                if (drop.getWellPosition().toStringNoSubPosition().equals("A01")) {
                    final Image image = new Image();
                    image.setImagePath("image/" + plate.getBarcode() + "-" + drop.getWellPosition() + ".jpg");
                    image.setDrop(drop);
                    image.setImageDate(Calendar.getInstance());
                    image.setPlateInspection(plateInspection);
                    image.setImageType(ImageType.COMPOSITE);
                    images.add(image);
                    //drop_A1 = drop;
                    //image_A1 = image;

                }
            }
            //add image to inspection
            plateInspection.setImages(images);

            //create inspection
            final PlateInspectionService service = dataStorage.getPlateInspectionService();
            service.create(plateInspection);
            version.flush();
            //find trialDrop Views
            final TrialService trialService = dataStorage.getTrialService();
            final BusinessCriteria criteria = new BusinessCriteria(trialService);
            criteria.add(BusinessExpression.Equals(TrialDropView.PROP_BARCODE, plate.getBarcode(), true));
            criteria.add(BusinessExpression.Equals(ImageView.PROP_INSPECTION_NAME,
                plateInspection.getInspectionName(), true));
            criteria.add(BusinessExpression.Equals(ImageView.PROP_TYPE, ImageType.COMPOSITE.toString()
                .toLowerCase(), true));
            Collection<TrialDropView> trialDropViews = trialService.findViews(criteria);
            assertEquals(1, trialDropViews.size());//this plate has 96 well but only 1 has image
            criteria.add(BusinessExpression.Equals(ImageView.PROP_TYPE, ImageType.ZOOMED.toString(), true));
            trialDropViews = trialService.findViews(criteria);
            assertEquals(0, trialDropViews.size());//this plate has 96 well but only 1 has image

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

}
