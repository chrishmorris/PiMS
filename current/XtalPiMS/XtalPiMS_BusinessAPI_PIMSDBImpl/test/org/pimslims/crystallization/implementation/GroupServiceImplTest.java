/**
 * GroupServiceImplTest.java
 * 
 * Created on 5 Feb 2008
 * 
 * Author seroul
 */
package org.pimslims.crystallization.implementation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Organisation;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.service.ConstructService;
import org.pimslims.business.core.service.GroupService;
import org.pimslims.business.core.service.OrganisationService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.service.ScreenService;
import org.pimslims.business.crystallization.view.ConstructView;
import org.pimslims.business.crystallization.view.ScreenView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.GroupServiceTest;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Software;
import org.pimslims.model.people.PersonInGroup;
import org.pimslims.model.target.ResearchObjective;

/**
 * @author seroul
 * 
 */
public class GroupServiceImplTest extends GroupServiceTest {

	// unique string used to avoid name clashes
	private static final String UNIQUE = "GroupService"
			+ System.currentTimeMillis();

	/**
	 * @param name
	 */
	public GroupServiceImplTest(final String testName) {
		super(testName, DataStorageFactory.getDataStorageFactory()
				.getDataStorage());
	}

	public static Test suite() {
		final TestSuite suite = new TestSuite(GroupServiceImplTest.class);

		return suite;
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();

	}

	public void testAccessControl() throws ConstraintException,
			BusinessException {
		// create 3 person
		final Person xPerson1_1 = getxPerson("1_1");
		final Person xPerson1_2 = getxPerson("1_2");
		final Person xPerson2_1 = getxPerson("2_1");
		String organisationName = "organisation" + UNIQUE;
		Group xGroup1 = null;
		Group xGroup2 = null;
		// set up 2 usergroup
		try {
			this.dataStorage.openResources("administrator");
			// create an organisation
			final Organisation organisation = new Organisation();
			organisation.setName(organisationName);
			final OrganisationService organisationService = this.dataStorage
					.getOrganisationService();
			assertNotNull(organisationService);
			organisationService.create(organisation);

			// create 2 Group
			xGroup1 = new Group("group1" + UNIQUE, organisation);
			xGroup2 = new Group("group2" + UNIQUE, organisation);
			final GroupService groupService = this.dataStorage
					.getGroupService();
			// add 2 person to group1
			xGroup1.addUser(xPerson1_1);
			xGroup1.addUser(xPerson1_2);
			// add 1 person to group2
			xGroup2.addUser(xPerson2_1);
			groupService.create(xGroup1);
			groupService.create(xGroup2);
			this.dataStorage.commit();
		} catch (final Exception e) {

			e.printStackTrace();
			try {
				this.dataStorage.abort();
			} catch (final BusinessException e1) {
				e1.printStackTrace();
				// fail anyway
			}
			fail();
		}

		// user1 create something
		Long ID = null;
		try {
			this.dataStorage.openResources(xPerson1_1.getUsername());
			// Get a WritableVersion
			final WritableVersion version = ((DataStorageImpl) this.dataStorage)
					.getWritableVersion();
			final Software pimsObject = new Software(version, "software"
					+ UNIQUE, "version" + UNIQUE);
			ID = pimsObject.getDbId();
			this.dataStorage.commit();
		} catch (final BusinessException e) {
			e.printStackTrace();
			try {
				this.dataStorage.abort();
			} catch (final BusinessException e1) {
				e1.printStackTrace();
				// fail anyway
			}
			fail();
		}

		// user1 can see it
		assertNotNull(loadPiMSObject(xPerson1_1.getUsername(), ID));
		// user2 can see it as in same group
		assertNotNull(loadPiMSObject(xPerson1_2.getUsername(), ID));
		// user3 can not see it as in different group
		assertNull(loadPiMSObject(xPerson2_1.getUsername(), ID));

		/*
		 * was test user1 can not see user2's group
		 * this.dataStorage.openResources(xPerson1_1.getUsername()); try {
		 * 
		 * final GroupService service = this.dataStorage.getGroupService();
		 * final BusinessCriteria criteria = new BusinessCriteria(service);
		 * criteria.add(BusinessExpression.Equals(GroupView.PROP_NAME,
		 * xGroup2.getName(), true)); final Collection<GroupView> groupViews =
		 * service.findViews(criteria); assertEquals(0, groupViews.size()); }
		 * finally { this.dataStorage.abort(); // not testing persistence }
		 */

		// try construct view of this user
		this.dataStorage.openResources(xPerson1_1.getUsername());
		try {
			final WritableVersion version = (WritableVersion) ((DataStorageImpl) this.dataStorage)
					.getVersion();
			final ResearchObjective eb = new ResearchObjective(version, UNIQUE,
					"test");

			final ConstructService service = this.dataStorage
					.getConstructService();
			final BusinessCriteria criteria = new BusinessCriteria(service);
			criteria.add(BusinessExpression.Equals(ConstructView.PROP_NAME,
					UNIQUE, true));
			final Collection<ConstructView> constructViews = service
					.findViews(criteria);
			assertEquals(1, constructViews.size());
			final ConstructView constructView = constructViews.iterator()
					.next();
			assertEquals(UNIQUE, constructView.getConstructName());

		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
		// try screen view of this user
		this.dataStorage.openResources(xPerson1_1.getUsername());
		try {
			// find Views
			final ScreenService service = this.dataStorage.getScreenService();
			final BusinessCriteria criteria = new BusinessCriteria(service);
			criteria.setMaxResults(10);
			final Collection<ScreenView> views = service.findViews(criteria);
			service.findViewCount(criteria);

		} finally {
			this.dataStorage.abort(); // not testing persistence
		}

		// now tidy up
		this.dataStorage.openResources("administrator");
		WritableVersion version = ((DataStorageImpl) this.dataStorage)
				.getWritableVersion();
		try {
			org.pimslims.model.people.Organisation organisation = version
					.findFirst(org.pimslims.model.people.Organisation.class,
							"name", organisationName);
			Set<org.pimslims.model.people.Group> groups = organisation
					.getGroups();
			Set<LabNotebook> notebooks = new HashSet();
			for (Iterator iterator = groups.iterator(); iterator.hasNext();) {
				org.pimslims.model.people.Group group = (org.pimslims.model.people.Group) iterator
						.next();
				Set<PersonInGroup> pigs = group.getPersonInGroups();
				for (Iterator iterator2 = pigs.iterator(); iterator2.hasNext();) {
					PersonInGroup pig = (PersonInGroup) iterator2.next();
					Set<User> users = pig.getPerson().getUsers();
					for (Iterator iterator3 = users.iterator(); iterator3
							.hasNext();) {
						User user = (User) iterator3.next();
						Set<UserGroup> userGroups = user.getUserGroups();
						for (Iterator iterator4 = userGroups.iterator(); iterator4
								.hasNext();) {
							UserGroup userGroup = (UserGroup) iterator4.next();
							Set<Permission> permissions = userGroup
									.getPermissions();
							for (Iterator iterator5 = permissions.iterator(); iterator5
									.hasNext();) {
								Permission permission = (Permission) iterator5
										.next();
								notebooks.add(permission.getAccessObject());
							}
							version.delete(permissions);

						}
						version.delete(userGroups);
					}
					version.delete(users);
					pig.getPerson().delete();
				}
				version.delete(pigs);
			}
			version.delete(groups);
			organisation.delete();
			for (Iterator iterator2 = notebooks.iterator(); iterator2.hasNext();) {
				LabNotebook notebook = (LabNotebook) iterator2.next();
				version.delete(notebook.getLabBookEntries());
				notebook.delete();
			}
			version.commit();
		} catch (AccessException e) {
			fail(e.getMessage());
		} catch (AbortedException e) {
			fail(e.getMessage());
		} finally {
			if (!version.isCompleted()) {
				version.abort();
			}
		}
	}

	private Object loadPiMSObject(final String username, final Long id)
			throws BusinessException {
		try {
			this.dataStorage.openResources(username);
			final ReadableVersion rv = ((DataStorageImpl) this.dataStorage)
					.getVersion();
			return rv.get(id);
		} finally {
			this.dataStorage.abort();
		}
	}

	private Person getxPerson(final String prefix) {
		final Person xPerson = new Person();
		xPerson.setFamilyName("person" + prefix + UNIQUE);
		xPerson.setUsername("user" + prefix + UNIQUE);
		return xPerson;
	}

}
