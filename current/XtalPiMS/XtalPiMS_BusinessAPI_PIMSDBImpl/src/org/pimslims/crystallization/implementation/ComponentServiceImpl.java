/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.crystallization.implementation;

import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Component;
import org.pimslims.business.crystallization.service.ComponentService;
import org.pimslims.business.crystallization.view.ComponentView;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author ian
 */
@Deprecated
// not implemented
public class ComponentServiceImpl extends BaseServiceImpl implements ComponentService {

    public ComponentServiceImpl(DataStorage baseStorage) {
        super(baseStorage);
    }

    public Integer findViewCount(BusinessCriteria criteria) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<ComponentView> findViews(BusinessCriteria criteria) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String convertPropertyName(String property) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Component> findAll() throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Component> find(BusinessCriteria criteria) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void create(Component component) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
