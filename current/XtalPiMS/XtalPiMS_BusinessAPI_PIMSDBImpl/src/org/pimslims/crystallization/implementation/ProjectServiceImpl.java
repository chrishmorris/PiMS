/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.crystallization.implementation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.model.Project;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.core.model.Target;
import org.pimslims.business.core.service.ProjectService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;

/**
 * 
 * @author ian
 */
public class ProjectServiceImpl extends BaseServiceImpl implements
		ProjectService {
	final PersonServiceImpl personService;

	public ProjectServiceImpl(DataStorage dataStorage) {
		super(dataStorage);
		personService = new PersonServiceImpl(dataStorage);
	}

	public Project find(long id) throws BusinessException {
		return this.find(org.pimslims.model.target.Project.class.getName()
				+ ":" + id);
	}

	public Project find(String id) throws BusinessException {
		return getXtalProject((org.pimslims.model.target.Project) version
				.get(id));
	}

	public Project findByName(String name) throws BusinessException {
		return getXtalProject(this.version, name);
	}

	public Person getOwner(Project project) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Group getGroup(Project project) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Person getLocalContact(Project project) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Collection<Sample> getSamples(Project project,
			BusinessCriteria criteria) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Collection<Project> findByOwner(Person person,
			BusinessCriteria criteria) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Collection<Project> findByOwner(String username,
			BusinessCriteria criteria) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Collection<Project> findByLocalContact(Person person,
			BusinessCriteria criteria) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Collection<Project> findByLocalContact(String username,
			BusinessCriteria criteria) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Project findBySample(Sample sample) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Collection<Project> findByGroup(Group group,
			BusinessCriteria criteria) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Collection<Project> findByPlate(TrialPlate plate,
			BusinessCriteria criteria) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Collection<Project> findByPlate(String barcode,
			BusinessCriteria criteria) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Project findByPlate(PlateExperimentView plateExperiment)
			throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * @TODO REQUIRED
	 * @param project
	 * @throws org.pimslims.business.exception.BusinessException
	 */
	public void create(Project project) throws BusinessException {
		try {
			org.pimslims.model.target.Project pimsTargetProject = new org.pimslims.model.target.Project(
					(WritableVersion) version, project.getName(),
					project.getName());
			project.setId(pimsTargetProject.getDbId());

			// Set targets
			List<org.pimslims.model.target.Target> pimsTargets = new LinkedList<org.pimslims.model.target.Target>();
			for (Target xTarget : project.getTargets()) {
				org.pimslims.model.target.Target pimsTarget = version
						.findFirst(org.pimslims.model.target.Target.class,
								org.pimslims.model.target.Target.PROP_NAME,
								xTarget.getName());
				pimsTargets.add(pimsTarget);
			}
			pimsTargetProject.setTargets(pimsTargets);

			// Set the owner, in the mean time its set the Group
			Person owner = project.getOwner();

			// could
			// Person localContact = project.getLocalContact();

			// where we can store project.getDescription() ????
		} catch (ConstraintException e) {
			// unacceptable values, e.g. user name already exists
			throw new BusinessException(e);
		}
	}

	/**
	 * @TODO REQUIRED
	 * @param project
	 * @throws org.pimslims.business.exception.BusinessException
	 */
	public void update(Project project) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * @param project
	 * @throws org.pimslims.business.exception.BusinessException
	 */
	public void close(Project project) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * @param name
	 * @return
	 * @throws BusinessException
	 */
	protected Project getXtalProject(ReadableVersion version, String name)
			throws BusinessException {
		org.pimslims.model.target.Project pimsTargetProject = version
				.findFirst(org.pimslims.model.target.Project.class,
						org.pimslims.model.target.Project.PROP_SHORTNAME, name);

		if (null == pimsTargetProject) {
			return null;
		}
		return getXtalProject(pimsTargetProject);
	}

	/**
	 * 
	 * @param pimsOrganisation
	 * @return
	 * @throws BusinessException
	 */
	protected Project getXtalProject(
			org.pimslims.model.target.Project pimsTargetProject)
			throws BusinessException {
		final Project project = new Project();
		project.setName(pimsTargetProject.get_Name());
		project.setId(pimsTargetProject.getDbId());
		project.setDescription(pimsTargetProject.getCompleteName());

		// Set the Group
		// AccessObject access=pimsTargetProject.getAccess();
		// Permission permission=access.findFirst(AccessObject.PROP_PERMISSIONS,
		// Permission.PROP_OPTYPE, "create");
		/*
		 * TODO the group does not work in this way, need reimplement!
		 * org.pimslims.model.people.Person pimsPerson =
		 * PersonDAO.getpimsPerson(version, pimsTargetProject.get_Owner()); if
		 * (null != pimsPerson) { org.pimslims.model.people.Group pimsGroup =
		 * pimsPerson.getCurrentGroup(); if (null != pimsGroup) {
		 * project.setGroup(GroupServiceImpl.getXtalGroup(pimsGroup)); } }
		 */

		// project.setLocalContact(localContact);
		// Set the owner
		Person owner = personService.findByUsername(pimsTargetProject
				.get_Owner());
		if (null != owner) {
			project.setOwner(owner);
		}

		// project.setTarget(target);
		return project;
	}

	public Collection<Project> findAll(BusinessCriteria criteria)
			throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
