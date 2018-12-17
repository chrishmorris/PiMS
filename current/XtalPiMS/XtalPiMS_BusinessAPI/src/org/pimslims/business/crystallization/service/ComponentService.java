/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.pimslims.business.crystallization.service;

import java.util.Collection;

import org.pimslims.business.BaseService;
import org.pimslims.business.ViewService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Component;
import org.pimslims.business.crystallization.view.ComponentView;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author ian
 */
@Deprecated
// not implemented
public interface ComponentService extends BaseService, ViewService<ComponentView> {

    public Collection<Component> findAll() throws BusinessException;

    public Collection<Component> find(BusinessCriteria criteria) throws BusinessException;

    public void create(Component component) throws BusinessException;

}
