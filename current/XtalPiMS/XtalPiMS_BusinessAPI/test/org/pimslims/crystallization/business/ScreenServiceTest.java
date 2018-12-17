package org.pimslims.crystallization.business;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.Component;
import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.ScreenService;
import org.pimslims.business.crystallization.view.ScreenView;

/**
 * <p>
 * Standard JUnit3 TestCase for {@link org.pimslims.crystallization.business.ScreenService} implementations.
 * </p>
 * 
 * <p>
 * The test case implementation should look similar to:
 * </p>
 * 
 * <p>
 * <code><pre>
 * public class ScreenServiceImplTest extends ScreenServiceTest {
 * 
 *     public ScreenServiceImplTest(String methodName) {
 *         super(methodName, new DataStorageImpl);
 *     }
 *     
 *     // Additional test cases here...
 *     
 * }
 * </pre></code>
 * </p>
 * 
 * <p>
 * This class provides test cases for:
 * </p>
 * 
 * <ul>
 * <li>{@link org.pimslims.crystallization.DataStorage#getScreenService()}</li>
 * <li>{@link org.pimslims.crystallization.business.ScreenService#create(Screen)}</li>
 * <li>{@link org.pimslims.crystallization.business.ScreenService#createDeepWellPlate(Screen, Barcode)}</li>
 * <li>{@link org.pimslims.crystallization.business.ScreenService#findByName(String)}</li>
 * <li>{@link org.pimslims.crystallization.business.ScreenService#getDeepWellPlates(Screen)}</li>
 * </ul>
 * 
 * @author Ian Berry
 * @see org.pimslims.crystallization.DataStorage
 * @see org.pimslims.crystallization.business.ScreenService
 */
public abstract class ScreenServiceTest extends TestCase {

    /**
     * <p>
     * A unique string to use as the name of a plate for these tests.
     * </p>
     */
    private static final String BARCODE = "bc" + System.currentTimeMillis();

	protected static final String UNIQUE = "ss"+System.currentTimeMillis();

    /**
     * <p>
     * The DataStorage implementation to use. This must be provided by the implementation via our constructor.
     * </p>
     */
    protected final DataStorage dataStorage;

    /**
     * <p>
     * Construct an instance of ScreenServiceTest.
     * </p>
     * 
     * @param methodName - passed on up the chain to {@link junit.framework.TestCase#TestCase(String)}
     * @param dataStorage - the {@link org.pimslims.crystallization.DataStorage} implementation that will be
     *            used to persist the objects and as such must not be null.
     */
    public ScreenServiceTest(final String methodName, final DataStorage dataStorage) {
        super(methodName);
        this.dataStorage = dataStorage;
    }

    /**
     * <p>
     * Test case for {@link org.pimslims.crystallization.DataStorage#getScreenService()}.
     * </p>
     * 
     * @throws Exception
     * @see org.pimslims.crystallization.DataStorage#getScreenService()
     */
    public void testGetService() throws Exception {

        // Prepare the DataStorage implementation
        this.dataStorage.openResources("administrator");

        try {
            final ScreenService service = this.dataStorage.getScreenService();
            assertNotNull(service);
        } finally {

            // Not testing persistence
            this.dataStorage.abort();

        }

    }

    /**
     * <p>
     * Simple test case for {@link org.pimslims.crystallization.business.ScreenService#create(Screen)}, which
     * also tests {@link org.pimslims.crystallization.business.ScreenService#findByName(String)}.
     * </p>
     * 
     * @throws Exception
     * @see org.pimslims.crystallization.business.ScreenService#create(Screen)
     * @see org.pimslims.crystallization.business.ScreenService#findByName(String)
     */
    public void testCreate() throws Exception {

        // Prepare the DataStorage implementation
        this.dataStorage.openResources("administrator");

        try {

            // Get a ScreenService implementation
            final ScreenService service = this.dataStorage.getScreenService();

            // Instantiate and name a new Screen
            final Screen toMake = new Screen();
            toMake.setName("screen" + System.currentTimeMillis());

            // Store the new screen
            service.create(toMake);

            // Find the new screen
            final Screen made = service.findByName(toMake.getName());

            // Check that we recovered the new screen
            assertNotNull(made);
            assertEquals(toMake.getName(), made.getName());
            assertEquals(0, made.getConditionPositions().size());

        } finally {

            // Not testing persistence
            this.dataStorage.abort();

        }

    }
    
    public void testFindViews() throws Exception {

        // Prepare the DataStorage implementation
        this.dataStorage.openResources("administrator");

        try {
            final ScreenService service = this.dataStorage.getScreenService();
            final Screen toMake = new Screen();
            toMake.setName(UNIQUE+"+screen");
            service.create(toMake);

			// Find the new screen
            BusinessCriteria criteria = new BusinessCriteria(service);
            criteria.add(BusinessExpression.Like(ScreenView.PROP_NAME,
					toMake.getName(), true, true));
            Collection<ScreenView> screens = service.findViews(criteria );
            assertEquals(1, screens.size());
            ScreenView found = screens.iterator().next();
            assertEquals(toMake.getName(), found.getName());

        } finally {

            // Not testing persistence
            this.dataStorage.abort();

        }

    }

    /**
     * <p>
     * Slightly more complex test case for
     * {@link org.pimslims.crystallization.business.ScreenService#create(Screen)} which creates a Screen with
     * a Condition.
     * </p>
     * 
     * @throws Exception
     * @see org.pimslims.crystallization.business.ScreenService#create(Screen)
     * @see org.pimslims.crystallization.business.ScreenService#findByName(String)
     * @see org.pimslims.crystallization.Component
     */
    public void testCreateCondition() throws Exception {

        // Prepare the DataStorage implementation
        this.dataStorage.openResources("administrator");

        try {

            // Get a ScreenService implementation
            final ScreenService service = this.dataStorage.getScreenService();

            // Instantiate and name a new Screen
            final Screen toMake = new Screen();
            toMake.setName("screen" + System.currentTimeMillis());

            // Create a new Condition
            final Condition condition = new Condition();
            condition.setLocalName("c" + System.currentTimeMillis());
            //condition.setWellPosition();

            Map<WellPosition, Condition> map = new HashMap();
            map.put(new WellPosition("H12"), condition);
            toMake.setConditionPositions(map);

            // Store the new screen
            service.create(toMake);

            // Find the new screen
            final Screen made = service.findByName(toMake.getName());

            // Check that we recovered the new screen
            assertEquals(1, made.getConditionPositions().size());
            final Condition madeCondition = made.getConditionPositions().values().iterator().next();
            assertEquals(condition.getLocalName(), madeCondition.getLocalName());
            assertEquals(new WellPosition("H12"), made.getConditionPositions().keySet().iterator().next());
            assertEquals(0, condition.getComponents().size());

        }

        finally {

            // Not testing persistence
            this.dataStorage.abort();

        }

    }

    /**
     * <p>
     * Even more complex test case for
     * {@link org.pimslims.crystallization.business.ScreenService#create(Screen)} which creates a Screen with
     * a Condition that has a Component.
     * </p>
     * 
     * @throws Exception
     * @see org.pimslims.crystallization.business.ScreenService#create(Screen)
     * @see org.pimslims.crystallization.business.ScreenService#findByName(String)
     * @see org.pimslims.crystallization.Component
     * @see org.pimslims.crystallization.Condition
     */
    public void testCreateComponent() throws Exception {

        // Prepare the DataStorage implementation
        this.dataStorage.openResources("administrator");

        try {

            // Get a ScreenService implementation
            final ScreenService service = this.dataStorage.getScreenService();

            // Instantiate and name a new Screen
            final Screen toMake = new Screen();
            toMake.setName("screen" + System.currentTimeMillis());

            // Create a new Condition
            Condition condition = new Condition();
            condition.setLocalName("c" + System.currentTimeMillis());

            // Create a new component
            Component component = new Component();
            component.setChemicalName("" + System.currentTimeMillis());

            ComponentQuantity quantity = new ComponentQuantity();
            quantity.setComponent(component);
            quantity.setQuantity(1d);
            quantity.setDisplyUnit("M");
            quantity.setUnits("M");
            // Add the Component to the Condition
            condition.addComponent(quantity);

            // Add the Condition to the Screen
            Map<WellPosition, Condition> map = new HashMap();
            map.put(new WellPosition("H12"), condition);
            toMake.setConditionPositions(map);

            // Store the new screen
            service.create(toMake);

            // Find the new screen
            final Screen made = service.findByName(toMake.getName());

            // Check that we recovered the new screen
            Collection<Condition> conditions = made.getConditionPositions().values();
            assertFalse(conditions.isEmpty());
            final Condition madeCondition = conditions.iterator().next();
            assertEquals(1, madeCondition.getComponents().size());
            ComponentQuantity madeQuantity = madeCondition.getComponents().iterator().next();
            assertEquals(quantity.getQuantity(), madeQuantity.getQuantity());
            assertEquals(quantity.getUnits(), madeQuantity.getUnits());
            Component madeComponent = madeQuantity.getComponent();
            assertNotNull(madeComponent);
            assertEquals(component.getChemicalName(), madeComponent.getChemicalName());

        }

        finally {

            // Not testing persistence
            this.dataStorage.abort();

        }

    }

}
