/*
 * LocationService.java Created on 27 June 2007, 11:50 To change this template, choose Tools | Template
 * Manager and open the template in the editor.
 */

package org.pimslims.business.core.service;

import java.util.Collection;

import org.pimslims.business.BaseService;
import org.pimslims.business.core.model.Location;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author IMB
 */
public interface LocationService extends BaseService {
    /**
     * Get information on a specific institute.
     * 
     * @return information about the institute.
     * @param id
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Location find(long id) throws BusinessException;

    /**
     * 
     * @param paging
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Location> findAll(BusinessCriteria criteria) throws BusinessException;

    /**
     * Insert new institute in the DB.
     * 
     * @param location
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(Location location) throws BusinessException;

    /**
     * 
     * @param location
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void update(Location location) throws BusinessException;

    /**
     * For when you want the imager to exist, but not allow users to add plates to it (because you have sold
     * it or something!)
     * 
     * @param location
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void close(Location location) throws BusinessException;

    /**
     * 
     * @param name
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Location findByName(String name) throws BusinessException;

    /**
     * Create an image record and link it to this location
     * 
     * @param image
     * @throws BusinessException
     */
    void createImageAndLink(Image image) throws BusinessException;

    /**
     * @see LocationServiceImpl#createImageAndLink(Image image)
     * @param image
     * @throws BusinessException
     */
    void createImagesAndLink(Collection<Image> images) throws BusinessException;
}
