/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.crystallization.implementation;

import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Project;
import org.pimslims.business.core.model.Target;
import org.pimslims.business.core.service.TargetService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;

/**
 * 
 * @author ian
 */
public class TargetServiceImpl extends BaseServiceImpl implements TargetService {

	public TargetServiceImpl(DataStorage dataStorage) {
		super(dataStorage);
	}

	public Target find(long id) throws BusinessException {
		return this.find(org.pimslims.model.target.Target.class.getName() + ":"
				+ id);
	}

	public Target find(String id) throws BusinessException {
		return getXtalTarget((org.pimslims.model.target.Target) version.get(id));
	}

	/**
	 * @TODO REQUIRED
	 * @param name
	 * @return
	 * @throws org.pimslims.business.exception.BusinessException
	 */
	public Target findByName(String name) throws BusinessException {
		return getXtalTarget(this.version, name);
	}

	public Project getProject(Target target) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Collection<Construct> getConstructs(Target target,
			BusinessCriteria criteria) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * @TODO REQUIRED
	 * @param target
	 * @throws org.pimslims.business.exception.BusinessException
	 */
	public void create(Target target) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * @TODO REQUIRED
	 * @param target
	 * @throws org.pimslims.business.exception.BusinessException
	 */
	public void update(Target target) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * 
	 * @param target
	 * @throws org.pimslims.business.exception.BusinessException
	 */
	public void close(Target target) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * @param name
	 * @return
	 */
	static Target getXtalTarget(ReadableVersion version, String name) {
		org.pimslims.model.target.Target pimsTarget = version.findFirst(
				org.pimslims.model.target.Target.class,
				org.pimslims.model.target.Target.PROP_NAME, name);

		if (null == pimsTarget) {
			return null;
		}
		return getXtalTarget(pimsTarget);
	}

	/**
	 * 
	 * @param pimsOrganisation
	 * @return
	 */
	static Target getXtalTarget(org.pimslims.model.target.Target pimsTarget) {
		final Target target = new Target();
		target.setName(pimsTarget.get_Name());
		target.setId(pimsTarget.getDbId());

		// target.setGroup(group);
		// target.setDescription(pimsTarget.getCompleteName());
		return target;
	}

	public Collection<Target> findAll(BusinessCriteria criteria)
			throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
