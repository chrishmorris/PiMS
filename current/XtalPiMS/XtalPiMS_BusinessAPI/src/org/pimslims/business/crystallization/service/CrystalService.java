/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.pimslims.business.crystallization.service;

import java.util.Collection;

import org.pimslims.business.BaseService;
import org.pimslims.business.ViewService;
import org.pimslims.business.crystallization.model.Crystal;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.view.ComponentView;
import org.pimslims.business.exception.BusinessException;

/**
 * Service to find Crystals recorded on a particular well/plate and create and delete crystals.
 * 
 * @author Jon Diprose
 */
public interface CrystalService extends BaseService {

    public Collection<Crystal> findByBarcodeAndWell(String barcode, WellPosition wellPosition) throws BusinessException;

    public void create(Crystal crystal) throws BusinessException;

    public void delete(Crystal crystal) throws BusinessException;

}
