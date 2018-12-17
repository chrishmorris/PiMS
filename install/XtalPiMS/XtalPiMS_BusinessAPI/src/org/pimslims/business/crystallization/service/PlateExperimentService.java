/*
 * PlateExperimentService.java
 *
 * Created on 08 May 2007, 11:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pimslims.business.crystallization.service;

import java.util.Collection;

import org.pimslims.business.BaseService;
import org.pimslims.business.ViewService;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author IMB
 */
public interface PlateExperimentService extends BaseService, ViewService<PlateExperimentView> {

    /**
     * 
     * @param barcode
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<PlateExperimentView> findByPlateBarcode(String barcode) throws BusinessException;

  
}
