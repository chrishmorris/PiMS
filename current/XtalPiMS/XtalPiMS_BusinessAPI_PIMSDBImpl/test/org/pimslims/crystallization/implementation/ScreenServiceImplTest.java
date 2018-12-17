/*
 * ScreenServiceImplTest.java JUnit based test Created on 09 August 2007, 16:56
 */

package org.pimslims.crystallization.implementation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Component;
import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.DeepWellPlate;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.ScreenService;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.ScreenServiceTest;
import org.pimslims.crystallization.dao.ScreenDAO;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.holder.RefHolder;
import org.pimslims.model.holder.RefSamplePosition;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.SampleComponent;

/**
 * <p>
 * JUnit3 TestCase for {@link org.pimslims.crystallization.implementation.ScreenServiceImpl}, implementing
 * {@link org.pimslims.crystallization.business.ScreenServiceTest}.
 * </p>
 * 
 * @see org.pimslims.crystallization.implementation.ScreenServiceImpl
 * @see org.pimslims.crystallization.business.ScreenServiceTest
 * 
 * @author Ian Berry, Chris Morris, Jon Diprose
 */
public class ScreenServiceImplTest extends ScreenServiceTest {

    /**
     * <p>
     * A screen name to use for testing that should be unique to each run.
     * </p>
     */
    private static final String SCREEN_NAME = "screen" + System.currentTimeMillis();

    /**
     * <p>
     * Used by TestSuite runners.
     * </p>
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite(ScreenServiceImplTest.class);
        return suite;
    }

    /**
     * <p>
     * Construct a new instance of this test case.
     * </p>
     * 
     * @param testName - the name of the test case
     */
    public ScreenServiceImplTest(final String testName) {
        super(testName, DataStorageFactory.getDataStorageFactory().getDataStorage());
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

    /**
     * a screen(refHolder) is linked with 2 holderCatorgory: one is "Screen" Another one is its screenType
     * 
     * @throws BusinessException
     * @throws ConstraintException
     * @throws AccessException
     */
    public void testFindScreenTypes() throws BusinessException, ConstraintException, AccessException {

        // set up
        String username = UNIQUE + "u";
        this.dataStorage.openResources("administrator");
        createUser(username);

        this.dataStorage.openResources(username);

        try {

            // Get a ScreenService implementation
            final ScreenService service = this.dataStorage.getScreenService();

            // Create a Screen
            final Screen newScreen = new Screen();
            newScreen.setName(SCREEN_NAME);
            service.create(newScreen);
            // Get a WritableVersion
            final WritableVersion version =
                (WritableVersion) (((DataStorageImpl) this.dataStorage).getVersion());
            // get the refholder
            final RefHolder rh = version.findFirst(RefHolder.class, RefHolder.PROP_NAME, SCREEN_NAME);
            final HolderCategory hc = new HolderCategory(version, "HCName " + SCREEN_NAME);
            rh.addHolderCategory(hc);
            version.flush();

            // get ScreenTypes
            final Collection<String> screenTypesNames = service.findScreenTypes();
            assertTrue(screenTypesNames.contains("HCName " + SCREEN_NAME));
        } finally {
            // Not testing persistence
            this.dataStorage.abort();
        }

        deleteUser(username);

    }

    /**
     * <p>
     * This test checks that {@link org.pimslims.model.holder.RefHolder} is mapped onto
     * {@link org.pimslims.crystallization.Screen} as expected.
     * {@link org.pimslims.crystallization.implementation.ScreenServiceImpl#findByName(String)} is also
     * tested.
     * </p>
     * 
     * 
     * @see org.pimslims.crystallization.implementation.ScreenServiceImpl#findByName(String)
     * @see org.pimslims.crystallization.Screen
     * @see org.pimslims.model.holder.Holder
     */
    public void testReadScreenMapping() throws Exception {

        // Prepare the DataStorage implementation
        this.dataStorage.openResources("administrator");

        try {

            // Get a ScreenService implementation
            final ScreenService service = this.dataStorage.getScreenService();

            // Get a WritableVersion
            final WritableVersion version =
                (WritableVersion) (((DataStorageImpl) this.dataStorage).getVersion());

            // A Screen is modelled as a RefHolder with HolderCategory
            // xtalPiMS:Screen
            new RefHolder(version, SCREEN_NAME + "not-screen");

            // Test what happens when the Screen is not correctly mapped
            assertNull(service.findByName(SCREEN_NAME + "not-screen"));

            // A Screen is modelled as a RefHolder with HolderCategory
            // xtalPiMS:Screen
            new RefHolder(version, SCREEN_NAME).setHolderCategories(buildHolderCategorySet(version));

            // Test what happens when the Screen is not found
            assertNull(service.findByName("nonesuch"));

            // Check that we can recover the Screen
            final Screen screen = service.findByName(SCREEN_NAME);
            assertNotNull(screen);
            assertEquals(SCREEN_NAME, screen.getName());
            assertEquals(0, screen.getConditionPositions().size());

        }

        finally {

            // Not testing persistence
            this.dataStorage.abort();

        }

    }

    /**
     * <p>
     * This test checks that {@link org.pimslims.crystallization.Screen} is mapped onto
     * {@link org.pimslims.model.holder.Holder} as expected.
     * {@link org.pimslims.crystallization.implementation.ScreenServiceImpl#create(Screen)} is also tested.
     * </p>
     * 
     * @see org.pimslims.crystallization.implementation.ScreenServiceImpl#create(Screen)
     * @see org.pimslims.crystallization.Screen
     * @see org.pimslims.model.holder.Holder
     */
    public void testWriteScreenMapping() throws Exception {

        // Prepare the DataStorage implementation
        this.dataStorage.openResources("administrator");

        try {

            // Get a ScreenService implementation
            final ScreenService service = this.dataStorage.getScreenService();

            // Get a ReadableVersion
            final ReadableVersion version = (((DataStorageImpl) this.dataStorage).getVersion());

            // Create a Screen
            final Screen newScreen = new Screen();
            newScreen.setName(SCREEN_NAME);
            newScreen.setManufacturerName("manufacturerName");
            service.create(newScreen);

            // Recover the Holder
            final Map<String, Object> attr = new HashMap<String, Object>();
            attr.put("name", SCREEN_NAME);
            final RefHolder holder = version.findFirst(RefHolder.class, attr);

            // Check that the Holder is as we expect
            assertNotNull(holder);
            assertEquals(SCREEN_NAME, holder.getName());
            assertEquals(0, holder.getRefSamplePositions().size());
            assertEquals("manufacturerName", holder.getRefHolderSources().iterator().next().getSupplier()
                .getName());

        }

        finally {

            // Not testing persistence
            this.dataStorage.abort();

        }

    }

    /**
     * <p>
     * This test checks that {@link org.pimslims.model.sample.RefSample} is mapped onto
     * {@link org.pimslims.crystallization.Condition} as expected.
     * {@link org.pimslims.crystallization.implementation.ScreenServiceImpl#findByName(String)} is also
     * tested.
     * </p>
     * 
     * @see org.pimslims.crystallization.implementation.ScreenServiceImpl#findByName(String)
     * @see org.pimslims.crystallization.Condition
     * @see org.pimslims.model.holder.RefSamplePosition
     * @see org.pimslims.model.sample.RefSample
     */
    public void testReadConditionMapping() throws Exception {

        // Prepare the DataStorage implementation
        this.dataStorage.openResources("administrator");

        try {

            // Get a ScreenService implementation
            final ScreenService service = this.dataStorage.getScreenService();

            // Get a WritableVersion
            final WritableVersion version =
                (WritableVersion) (((DataStorageImpl) this.dataStorage).getVersion());

            // Create the screen
            final RefHolder holder = new RefHolder(version, SCREEN_NAME);
            holder.setHolderCategories(buildHolderCategorySet(version));

            // Create the condition
            final RefSample sample = new RefSample(version, SCREEN_NAME + "B01");

            // Add the condition to the screen
            final RefSamplePosition rp = new RefSamplePosition(version, holder);
            rp.setColPosition(1);
            rp.setRowPosition(2);
            rp.setRefSample(sample);

            // Recover the screen from the service
            final Screen screen = service.findByName(SCREEN_NAME);

            // Check the screen matches our expectations
            assertEquals(1, screen.getConditionPositions().size());

            // Recover the condition from the screen
            final Map.Entry<WellPosition, Condition> cp =
                screen.getConditionPositions().entrySet().iterator().next();
            final Condition condition = cp.getValue();

            // Check the condition matches our expectations
            assertEquals(new WellPosition("B01"), cp.getKey());
            assertEquals(sample.getName(), condition.getLocalName());
            assertEquals(0, condition.getComponents().size());

        }

        finally {

            // Not testing persistence
            this.dataStorage.abort();

        }

    }

    /**
     * <p>
     * This test checks that {@link org.pimslims.crystallization.Condition} is mapped onto
     * {@link org.pimslims.model.sample.RefSample} as expected.
     * {@link org.pimslims.crystallization.implementation.ScreenServiceImpl#create(Screen)} is also tested.
     * </p>
     * 
     * @see org.pimslims.crystallization.implementation.ScreenServiceImpl#create(Screen)
     * @see org.pimslims.crystallization.Condition
     * @see org.pimslims.model.holder.RefSamplePosition
     * @see org.pimslims.model.sample.RefSample
     */
    public void testWriteConditionMapping() throws Exception {

        // Prepare the DataStorage implementation
        this.dataStorage.openResources("administrator");

        try {

            // Get a ScreenService implementation
            final ScreenService service = this.dataStorage.getScreenService();

            // Get a ReadableVersion
            final ReadableVersion version = (((DataStorageImpl) this.dataStorage).getVersion());

            // Instantiate and name a new Screen
            final Screen toMake = new Screen();
            toMake.setName(SCREEN_NAME);

            // Create a new Condition
            final Condition condition = new Condition();
            condition.setLocalName("c" + System.currentTimeMillis());
            condition.setSaltCondition(true);

            // Add the Condition to the Screen
            toMake.getConditionPositions().put(new WellPosition("H12"), condition);

            // Store the new screen
            service.create(toMake);

            // Recover the Holder
            final Map<String, Object> attr = new HashMap<String, Object>();
            attr.put("name", SCREEN_NAME);
            final RefHolder holder = version.findFirst(RefHolder.class, attr);

            // Check that the Holder is as we expect
            assertNotNull(holder);
            assertEquals(SCREEN_NAME, holder.getName());
            assertEquals(1, holder.getRefSamplePositions().size());

            // Recover the RefSamplePosition
            final RefSamplePosition rp = holder.getRefSamplePositions().iterator().next();

            // Check the RefSamplePosition is as we expect
            assertEquals(12, rp.getColPosition().intValue());
            assertEquals(8, rp.getRowPosition().intValue());

            // Recover the RefSample
            final RefSample rs = rp.getRefSample();

            // Check the RefSample is as we expect
            assertNotNull(rs);
            assertTrue(rs.getIsSaltCrystal());
            assertEquals(condition.getLocalName(), rs.getName());
            assertEquals(0, rs.getSampleComponents().size());

        } finally {
            // Not testing persistence
            this.dataStorage.abort();
        }
    }

    public void testFindAll() throws BusinessException, ConstraintException {
        // Prepare the DataStorage implementation
        this.dataStorage.openResources("administrator");

        try {

            // Get a ScreenService implementation
            final ScreenService service = this.dataStorage.getScreenService();
            // Get a ReadableVersion
            final WritableVersion version =
                (WritableVersion) (((DataStorageImpl) this.dataStorage).getVersion());
            final HolderCategory hc = new HolderCategory(version, "test" + System.currentTimeMillis());

            // Instantiate and name a new Screen
            final Screen toMake1 = new Screen();
            toMake1.setName(SCREEN_NAME + 1);

            final Screen toMake2 = new Screen();
            toMake2.setName(SCREEN_NAME + 2);

            // Store the new screens
            service.create(toMake1);

            // this is not a screen, A Screen is modelled as a RefHolder with
            // HolderCategory xtalPiMS:Screen
            new RefHolder(version, SCREEN_NAME + "not-screen1")
                .setHolderCategories(Collections.singleton(hc));

            // second screen
            service.create(toMake2);

            // this is not a screen, A Screen is modelled as a RefHolder with
            // HolderCategory xtalPiMS:Screen
            new RefHolder(version, SCREEN_NAME + "not-screen2")
                .setHolderCategories(Collections.singleton(hc));

            // get first one
            final BusinessCriteria criteria = new BusinessCriteria(null);
            criteria.setMaxResults(1);
            Collection<Screen> results = service.findAll(criteria);
            assertEquals(1, results.size());
            assertEquals(toMake2.getName(), results.iterator().next().getName());
            // get second one
            criteria.setFirstResult(1);
            results = service.findAll(criteria);
            assertEquals(1, results.size());
            assertEquals(toMake1.getName(), results.iterator().next().getName());
            // get first two
            criteria.setFirstResult(0);
            criteria.setMaxResults(2);
            results = service.findAll(criteria);
            assertEquals(2, results.size());
            for (final Screen screen : results) {
                final String name = screen.getName();
                assertTrue(name.equals(toMake1.getName()) || name.equals(toMake2.getName()));
            }
        } finally {
            // Not testing persistence
            this.dataStorage.abort();
        }
    }

    /**
     * <p>
     * {@link org.pimslims.crystallization.implementation.ScreenServiceImpl#findAllCount()} is tested
     * </p>
     * 
     * @throws ConstraintException
     * 
     */
    public void testFindAllCount() throws BusinessException, ConstraintException {
        // Prepare the DataStorage implementation
        this.dataStorage.openResources("administrator");

        try {

            // Get a ScreenService implementation
            final ScreenService service = this.dataStorage.getScreenService();
            // Get a ReadableVersion
            final ReadableVersion version = (((DataStorageImpl) this.dataStorage).getVersion());

            final Integer initialCount = service.findAllCount();
            // Instantiate and name a new Screen
            final Screen toMake = new Screen();
            toMake.setName(SCREEN_NAME);
            // Store the new screen
            service.create(toMake);

            // this is not a screen, A Screen is modelled as a RefHolder with
            // HolderCategory xtalPiMS:Screen
            new RefHolder((WritableVersion) version, SCREEN_NAME + "not-screen");

            final int newCount = service.findAllCount();
            assertEquals(initialCount + 1, newCount);
        } finally {
            // Not testing persistence
            this.dataStorage.abort();
        }
    }

    /**
     * <p>
     * This test checks that {@link org.pimslims.model.sample.Substance} is mapped onto
     * {@link org.pimslims.crystallization.Component} as expected.
     * {@link org.pimslims.crystallization.implementation.ScreenServiceImpl#findByName(String)} is also
     * tested.
     * </p>
     * 
     * @see org.pimslims.crystallization.implementation.ScreenServiceImpl#findByName(String)
     * @see org.pimslims.crystallization.Component
     * @see org.pimslims.model.molecule.Substance
     * @see org.pimslims.model.sample.SampleComponent
     */
    public void testReadComponent() throws Exception {

        // Prepare the DataStorage implementation
        this.dataStorage.openResources("administrator");

        try {

            // Get a ScreenService implementation
            final ScreenService service = this.dataStorage.getScreenService();

            // Get a WritableVersion
            final WritableVersion version =
                (WritableVersion) (((DataStorageImpl) this.dataStorage).getVersion());

            // Create the screen
            final RefHolder holder = new RefHolder(version, SCREEN_NAME);
            holder.setHolderCategories(buildHolderCategorySet(version));
            // Create the condition
            final RefSample sample = new RefSample(version, SCREEN_NAME + "B01");

            // Add the condition to the screen
            final RefSamplePosition rp = new RefSamplePosition(version, holder);
            rp.setColPosition(1);
            rp.setRowPosition(2);
            rp.setRefSample(sample);

            // Add a component to the condition
            final Substance component = new Molecule(version, "other", "test" + System.currentTimeMillis());
            final SampleComponent sc = new SampleComponent(version, component, sample);
            sc.setConcentrationUnit("M");
            sc.setConcentration(0.1f);

            // Recover the screen from the service
            final Screen screen = service.findByName(SCREEN_NAME);

            // Check the screen matches our expectations
            assertEquals(1, screen.getConditionPositions().size());

            // Recover the condition from the screen
            final Map.Entry<WellPosition, Condition> cp =
                screen.getConditionPositions().entrySet().iterator().next();
            final Condition condition = cp.getValue();

            // Check the condition matches our expectations
            assertEquals(new WellPosition("B01"), cp.getKey());
            assertEquals(sample.getName(), condition.getLocalName());
            assertEquals(1, condition.getComponents().size());

            // Recover the component
            final ComponentQuantity bean = condition.getComponents().iterator().next();

            // Check the component matches our expectations
            assertEquals(component.getName(), bean.getComponent().getChemicalName());
            assertEquals("M", bean.getUnits());
            assertEquals(0.1d, bean.getQuantity(), 0.0000001d);
            // LATER safety information

        }

        finally {

            // Not testing persistence
            this.dataStorage.abort();

        }

    }

    /**
     * <p>
     * This test checks that {@link org.pimslims.crystallization.Component} is mapped onto
     * {@link org.pimslims.model.sample.Substance} as expected.
     * {@link org.pimslims.crystallization.implementation.ScreenServiceImpl#create(Screen)} is also tested.
     * </p>
     * 
     * @see org.pimslims.crystallization.implementation.ScreenServiceImpl#create(Screen)
     * @see org.pimslims.crystallization.Component
     * @see org.pimslims.model.molecule.Substance
     * @see org.pimslims.model.sample.SampleComponent
     */
    public void testWriteComponent() throws Exception {

        // Prepare the DataStorage implementation
        this.dataStorage.openResources("administrator");

        try {

            // Get a ScreenService implementation
            final ScreenService service = this.dataStorage.getScreenService();

            // Get a ReadableVersion
            final ReadableVersion version = (((DataStorageImpl) this.dataStorage).getVersion());

            // Instantiate and name a new Screen
            final Screen toMake = new Screen();
            toMake.setName(SCREEN_NAME);

            // Create a new Condition
            final Condition condition = new Condition();
            condition.setLocalName("c" + System.currentTimeMillis());

            // Create a new component
            final Component component = new Component();
            component.setChemicalName("" + System.currentTimeMillis());
            final ComponentQuantity componentQ = new ComponentQuantity();
            componentQ.setComponent(component);
            componentQ.setUnits("M");
            componentQ.setQuantity(1d);

            // Add the Component to the Condition
            condition.addComponent(componentQ);

            // Add the Condition to the Screen
            toMake.getConditionPositions().put(new WellPosition("H12"), condition);

            // Store the new screen
            service.create(toMake);

            // Recover the Holder
            final Map<String, Object> attr = new HashMap<String, Object>();
            attr.put("name", SCREEN_NAME);
            final RefHolder holder = version.findFirst(RefHolder.class, attr);

            // Check that the Holder is as we expect
            assertNotNull(holder);
            assertEquals(SCREEN_NAME, holder.getName());
            assertEquals(1, holder.getRefSamplePositions().size());

            // Recover the RefSamplePosition
            final RefSamplePosition rp = holder.getRefSamplePositions().iterator().next();

            // Check the RefSamplePosition is as we expect
            assertEquals(12, rp.getColPosition().intValue());
            assertEquals(8, rp.getRowPosition().intValue());

            // Recover the RefSample
            final RefSample rs = rp.getRefSample();

            // Check the RefSample is as we expect
            assertNotNull(rs);
            assertEquals(condition.getLocalName(), rs.getName());
            assertEquals(1, rs.getSampleComponents().size());

            // Recover the SampleComponent
            final SampleComponent sc = rs.getSampleComponents().iterator().next();

            // Check SampleComponent is as we expect
            assertNotNull(sc);
            assertEquals(componentQ.getUnits(), sc.getConcentrationUnit());
            assertEquals(componentQ.getQuantity(), sc.getConcentration().doubleValue());

            final Substance ac = sc.getRefComponent();
            assertNotNull(ac);
            assertTrue(ac instanceof Molecule);
            assertEquals(component.getChemicalName(), ac.getName());

        }

        finally {

            // Not testing persistence
            this.dataStorage.abort();

        }

    }

    private Set<HolderCategory> buildHolderCategorySet(final ReadableVersion rv) {

        final Map<String, Object> hcattr = new HashMap<String, Object>();
        hcattr.put(HolderCategory.PROP_NAME, ScreenDAO.HOLDER_CATEGORY_NAME);
        final HolderCategory hc = rv.findFirst(HolderCategory.class, hcattr);

        if (null == hc) {
            throw new RuntimeException("Failed to find HolderCategory for " + ScreenDAO.HOLDER_CATEGORY_NAME
                + " - xtalPiMS ref data loaded?");
        }

        final Set<HolderCategory> hcs = new HashSet<HolderCategory>();
        hcs.add(hc);

        return hcs;

    }

    public void testFindManufacturerNames() throws BusinessException {
        // Prepare the DataStorage implementation
        this.dataStorage.openResources("administrator");

        try {

            // Get a ScreenService implementation
            final ScreenService service = this.dataStorage.getScreenService();
            // Get a ReadableVersion
            final WritableVersion version =
                (WritableVersion) (((DataStorageImpl) this.dataStorage).getVersion());

            // Instantiate and name a new Screen
            final Screen toMake1 = new Screen();
            toMake1.setName(SCREEN_NAME + 1);
            toMake1.setManufacturerName("ManufacturerName1");
            // Store the new screens
            service.create(toMake1);
            final Collection<String> names = service.findManufacturerNames();
            assertTrue(names.contains("ManufacturerName1"));
        } finally {

            // Not testing persistence
            this.dataStorage.abort();
        }

    }

    /**
     * ScreenServiceImplTest.testCreateDeepWellPlate
     * 
     * TODO Use a standard (non-OPPF) reference screen
     * 
     * @throws BusinessException
     */
    public void xtestCreateDeepWellPlate() throws BusinessException {
        // Prepare the DataStorage implementation
        this.dataStorage.openResources("administrator");
        String testScreenName = "Block 1";

        try {

            // Get a ScreenService implementation
            final ScreenServiceImpl service = (ScreenServiceImpl) this.dataStorage.getScreenService();
            Screen screen = service.findByName(testScreenName);
            assertEquals(testScreenName, screen.getName());
            DeepWellPlate dwp = service.createDeepWellPlate(screen, SCREEN_NAME + "-dwp");
            assertEquals(screen, dwp.getScreen());
            assertEquals(SCREEN_NAME + "-dwp", dwp.getBarcode());
            assertTrue(0 < dwp.getConditionPositions().size());
        } finally {

            // Not testing persistence
            this.dataStorage.abort();
        }

    }
}
