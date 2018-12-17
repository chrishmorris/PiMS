/*
 * PlateExperimentServiceImplTest.java JUnit based test Created on 09 August 2007, 16:56
 */

package org.pimslims.crystallization.util;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.pimslims.access.Access;
import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.target.ResearchObjective;

public class SequenceManagerTest extends TestCase {

	private static final Calendar NOW = Calendar.getInstance();

	private static final String BARCODE = "sm" + System.currentTimeMillis();

	private static final String UNIQUE = "sm" + System.currentTimeMillis();

	private final DataStorage dataStorage;

	public SequenceManagerTest(final String testName) {
		super(testName);
		this.dataStorage = DataStorageFactory.getDataStorageFactory()
				.getDataStorage();
	}

	public static Test suite() {
		final TestSuite suite = new TestSuite(SequenceManagerTest.class);

		return suite;
	}

	/**
     *  
     */
	public void testSequence() throws Exception {
		this.dataStorage.openResources("administrator");
		try {
			final TrialService trialservice = this.dataStorage
					.getTrialService();
			final TrialPlate plate = createFilledTrialPlate(BARCODE);
			trialservice.saveTrialPlate(plate);
			this.dataStorage.flush();

			SequenceManager manager = new SequenceManager(this.dataStorage);
			assertNull(manager.getSequence(BARCODE));
			// set for the first time
			manager.setSequenceAndAcronym(BARCODE, "QWERTY", null, null);
			assertEquals("QWERTY", manager.getSequence(BARCODE));
			// change an existing sequence
			manager.setSequenceAndAcronym(BARCODE, "WERTY", "Buzz", null);
			assertEquals("Buzz", manager.getAcronym(BARCODE));
			assertEquals("WERTY", manager.getSequence(BARCODE));

			assertEquals(1, manager.getConstructs("Buzz").size());
			assertEquals("WERTY", manager.getConstructs("Buzz").iterator()
					.next().getFinalProt());

		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	/**
     *  
     */
	public void testSequenceTwice() throws Exception {
		this.dataStorage.openResources("administrator");
		try {
			final TrialService trialservice = this.dataStorage
					.getTrialService();
			TrialPlate plate = createFilledTrialPlate(BARCODE);
			trialservice.saveTrialPlate(plate);
			plate = createFilledTrialPlate(BARCODE + "two");
			trialservice.saveTrialPlate(plate);
			this.dataStorage.flush();

			SequenceManager manager = new SequenceManager(this.dataStorage);
			// set for the first time
			ResearchObjective ro = manager.setSequenceAndAcronym(BARCODE,
					"QWERTY", UNIQUE, null);
			// set for the first time
			ResearchObjective ro2 = manager.setSequenceAndAcronym(BARCODE
					+ "two", "QWERTY", null, null);
			assertEquals("QWERTY", manager.getSequence(BARCODE));
			assertEquals("QWERTY", manager.getSequence(BARCODE + "two"));
			assertEquals(ro, ro2);

			Collection<String> barcodes = manager.getBarcodes("QWERTY");
			assertEquals(2, barcodes.size());
			/*
			 * TODO change an existing sequence manager.setSequence(BARCODE,
			 * "WERTY"); assertEquals("WERTY", manager.getSequence(BARCODE));
			 * assertEquals("QWERTY", manager.getSequence(BARCODE + "two"));
			 */
		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	public void testChangeSequence() throws Exception {
		this.dataStorage.openResources("administrator");
		try {
			final TrialService trialservice = this.dataStorage
					.getTrialService();
			TrialPlate plate = createFilledTrialPlate(BARCODE);
			trialservice.saveTrialPlate(plate);
			plate = createFilledTrialPlate(BARCODE + "two");
			trialservice.saveTrialPlate(plate);
			plate = createFilledTrialPlate(BARCODE + "three");
			trialservice.saveTrialPlate(plate);
			this.dataStorage.flush();

			SequenceManager manager = new SequenceManager(this.dataStorage);
			manager.setSequenceAndAcronym(BARCODE, "QWERTY", UNIQUE, null);
			manager.setSequenceAndAcronym(BARCODE + "two", "QWERTYQWERTY",
					UNIQUE, null);
			manager.setSequenceAndAcronym(BARCODE + "three", "QWERTYQW",
					UNIQUE, null);

			assertEquals("QWERTY", manager.getSequence(BARCODE));
			assertEquals("QWERTYQWERTY", manager.getSequence(BARCODE + "two"));
			assertEquals("QWERTYQW", manager.getSequence(BARCODE + "three"));

			/*
			 * TODO change an existing sequence manager.setSequence(BARCODE,
			 * "WERTY"); assertEquals("WERTY", manager.getSequence(BARCODE));
			 * assertEquals("QWERTY", manager.getSequence(BARCODE + "two"));
			 */
		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	public void testAcronym() throws Exception {
		this.dataStorage.openResources("administrator");
		try {
			final TrialService trialservice = this.dataStorage
					.getTrialService();
			final TrialPlate plate = createFilledTrialPlate(BARCODE);
			trialservice.saveTrialPlate(plate);
			this.dataStorage.flush();

			SequenceManager manager = new SequenceManager(this.dataStorage);
			assertNull(manager.getAcronym(BARCODE));
			// set for the first time
			manager.setSequenceAndAcronym(BARCODE, "YTREWQ", "Buzz", null);
			assertEquals("Buzz", manager.getAcronym(BARCODE));

			/*
			 * TODO change an existing acronym
			 * manager.setSequenceAndAcronym(BARCODE, "YTREWQ", "Buzz2");
			 * assertEquals("Buzz2", manager.getAcronym(BARCODE));
			 */

			assertTrue(manager.getConstructs("WERTY").isEmpty());

			manager.setSequenceAndAcronym(BARCODE, "QWERTY", null, null);
			assertEquals("QWERTY", manager.getSequence(BARCODE));
			assertEquals(1, manager.getConstructs("Buzz").size());
			assertEquals("QWERTY", manager.getConstructs("Buzz").iterator()
					.next().getFinalProt());

			// set acronym again
			final TrialPlate plate2 = createFilledTrialPlate(BARCODE + "2");
			trialservice.saveTrialPlate(plate2);
			this.dataStorage.flush();

			manager.setSequenceAndAcronym(BARCODE + "2", "YTREWQ", "Buzz", null);

		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	/*
	 * hard to implement, order of calls changed in servlet instead public void
	 * testAcronymTwice() throws Exception {
	 * this.dataStorage.openResources("administrator"); try { final TrialService
	 * trialservice = this.dataStorage.getTrialService(); final TrialPlate plate
	 * = createFilledTrialPlate(BARCODE); trialservice.saveTrialPlate(plate);
	 * this.dataStorage.flush();
	 * 
	 * SequenceManager manager = new SequenceManager(this.dataStorage);
	 * assertNull(manager.getAcronym(BARCODE)); // set for the first time
	 * manager.setAcronym(BARCODE, "Buzz"); manager.setSequence(BARCODE,
	 * "QWERTY"); assertEquals(1, manager.getConstructs("Buzz").size());
	 * 
	 * // set acronym again final TrialPlate plate2 =
	 * createFilledTrialPlate(BARCODE + "2");
	 * trialservice.saveTrialPlate(plate2); this.dataStorage.flush();
	 * manager.setAcronym(BARCODE + "2", "Buzz"); manager.setSequence(BARCODE +
	 * "2", "QWERTY"); assertEquals(1, manager.getConstructs("Buzz").size());
	 * 
	 * } finally { this.dataStorage.abort(); // not testing persistence } }
	 */

	public void testAnotherAcronym() throws Exception {
		this.dataStorage.openResources("administrator");
		try {
			final TrialService trialservice = this.dataStorage
					.getTrialService();
			TrialPlate plate = createFilledTrialPlate(BARCODE);
			trialservice.saveTrialPlate(plate);
			plate = createFilledTrialPlate(BARCODE + "two");
			trialservice.saveTrialPlate(plate);
			this.dataStorage.flush();

			SequenceManager manager = new SequenceManager(this.dataStorage);
			manager.setSequenceAndAcronym(BARCODE + "two", "YTREWQ", "Buzz2",
					null);
			assertEquals("Buzz2", manager.getAcronym(BARCODE + "two"));
			manager.setSequenceAndAcronym(BARCODE + "two", "WERTY", null, null);

			// new plate, new construct
			manager.setSequenceAndAcronym(BARCODE, "QWERTY", "Buzz", null);
			assertEquals("Buzz", manager.getAcronym(BARCODE));
			assertEquals("QWERTY", manager.getSequence(BARCODE));
			assertEquals(1, manager.getConstructs("Buzz").size());
			assertEquals("QWERTY", manager.getConstructs("Buzz").iterator()
					.next().getFinalProt());

		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	public TrialPlate createFilledTrialPlate(final String barcode)
			throws BusinessException, ConstraintException {

		// final Screen screen = createScreen(wv, barcode);

		// Create a PlateType
		final PlateType plateType = createDummyPlateType("PlateType" + barcode);
		plateType.setSubPositions(1);
		// create user
		// final Person user1 = super.createXPerson();
		// final Person user2 =
		// super.createXGroup().getUsers().iterator().next();
		// Create xtal TrialPlate
		final TrialService service = this.dataStorage.getTrialService();
		final TrialPlate plate = new TrialPlate(plateType);
		plate.setBarcode(barcode);
		plate.setCreateDate(Calendar.getInstance());
		plate.buildAllTrialDrops();
		/*
		 * could set sample //final Sample sample = new Sample(sampleName);
		 * final SampleQuantity sampleQuantity = new SampleQuantity(sample,
		 * 0.0000001, "L");
		 * 
		 * for (final TrialDrop trialDrop : plate.getTrialDrops()) {
		 * 
		 * trialDrop.addSample(sampleQuantity); }
		 */
		plate.setDescription("description for " + barcode);
		plate.setDestroyDate(Calendar.getInstance());

		// plate.setScreen(screen);

		// plate.setOperator(user1);
		// plate.setOwner(user2);
		// plate.setGroup(user2.getGroups().iterator().next());

		return plate;

	}

	public void testUser() throws Exception {
		String username = UNIQUE + "user";
		String bookname = UNIQUE + "book";
		this.dataStorage.openResources(Access.ADMINISTRATOR);
		WritableVersion version = ((DataStorageImpl) this.dataStorage)
				.getWritableVersion();
		try {
			LabNotebook book = new LabNotebook(version, UNIQUE);
			LabNotebook book2 = new LabNotebook(version, bookname);
			User user = new User(version, username);
			UserGroup group = new UserGroup(version, UNIQUE);
			group.addMemberUser(user);
			new Permission(version, "create", book, group);
			new Permission(version, "create", book2, group);
			new Permission(version, "read", book2, group);
			new Permission(version, "update", book2, group);
			this.dataStorage.commit();
		} finally {
			if (!version.isCompleted()) {
				version.close();
				Assert.fail();
				return;
			}
		}

		this.dataStorage.openResources(username);
		try {
			((DataStorageImpl) this.dataStorage).getWritableVersion()
					.setDefaultOwner(bookname);
			final TrialService trialservice = this.dataStorage
					.getTrialService();
			final TrialPlate plate = createFilledTrialPlate(BARCODE);
			trialservice.saveTrialPlate(plate);
			this.dataStorage.flush();
			((DataStorageImpl) this.dataStorage).getWritableVersion()
					.setDefaultOwner((LabNotebook) null);

			SequenceManager manager = new SequenceManager(this.dataStorage);
			manager.setSequenceAndAcronym(BARCODE, "QWERTY", null, bookname);
			assertEquals("QWERTY", manager.getSequence(BARCODE));
			this.dataStorage.flush();

		} finally {
			this.dataStorage.abort(); // not testing persistence
		}

		this.dataStorage.openResources(Access.ADMINISTRATOR);
		version = ((DataStorageImpl) this.dataStorage).getWritableVersion();
		try {
			User user = version.findFirst(User.class, User.PROP_NAME, username);
			if (null != user) {
				Collection<LabNotebook> books = new HashSet(2);
				Set<UserGroup> groups = user.getUserGroups();
				for (Iterator iterator = groups.iterator(); iterator.hasNext();) {
					UserGroup group = (UserGroup) iterator.next();
					Set<Permission> permissions = group.getPermissions();
					for (Iterator iterator2 = permissions.iterator(); iterator2
							.hasNext();) {
						Permission permission = (Permission) iterator2.next();
						books.add(permission.getLabNotebook());
					}
					group.delete();
				}
				version.delete(books);
				user.delete();
			}
			this.dataStorage.commit();
		} finally {
			if (!version.isCompleted()) {
				version.close();
			}
		}
	}

	private PlateType createDummyPlateType(final String name) {

		final PlateType plateType = new PlateType();
		plateType.setName(name);
		plateType.setRows(8);
		plateType.setColumns(12);
		plateType.setSubPositions(2);
		plateType.setReservoir(2);

		return plateType;

	}
}
