/*
 * PlateInspectionService.java
 *
 * Created on 08 May 2007, 11:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.pimslims.business.crystallization.service;

import java.util.Collection;

import org.pimslims.business.BaseService;
import org.pimslims.business.ViewService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.PlateInspection;
import org.pimslims.business.crystallization.view.InspectionView;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author IMB
 */
public interface PlateInspectionService extends BaseService, ViewService<InspectionView> {

    /**
     * 
     * @param id
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public PlateInspection find(long id) throws BusinessException;

    /**
     * Finds all inspections. This will only be used by an administrator. Not sure that this will get used
     * much as it will probably crash everything!
     * 
     * @param person - find all the inspections for this person. if it is null, find all inspections for all.
     * @param userOnly - only find inspections for this user (true) or this user and all the groups to which
     *            they belong (false)
     * @param paging
     * @throws org.pimslims.business.exception.BusinessException
     * @return A list of all plate inspections for all time.
     */
    public Collection<PlateInspection> findAll(BusinessCriteria criteria) throws BusinessException;

    /**
     * 
     * @param barcode
     * @throws org.pimslims.business.exception.BusinessException
     * @return
     */
    public Collection<InspectionView> findLatest(BusinessCriteria criteria) throws BusinessException;

    /**
     * 
     * @param plateInspection
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(PlateInspection plateInspection) throws BusinessException;

    /**
     * 
     * @param plateInspection
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void update(PlateInspection plateInspection) throws BusinessException;

    /**
     * 
     * @param inspectionName
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public PlateInspection findByInspectionName(String inspectionName) throws BusinessException;

    /**
     * 
     * @param barcode
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<PlateInspection> findByPlate(String barcode) throws BusinessException;


 }
