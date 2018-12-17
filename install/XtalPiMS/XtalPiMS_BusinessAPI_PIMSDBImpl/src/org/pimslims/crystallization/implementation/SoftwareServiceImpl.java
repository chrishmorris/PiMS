/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.crystallization.implementation;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Collection;
import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.Software;
import org.pimslims.business.crystallization.service.SoftwareService;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.exception.ConstraintException;

/**
 * 
 * @author ian
 */
public class SoftwareServiceImpl extends BaseServiceImpl implements SoftwareService {

    /**
     * Creates a new instance of ScreenServiceImpl
     * 
     * @param dataStorage
     */
    public SoftwareServiceImpl(DataStorage dataStorage) {
        super(dataStorage);
    }

    /**
     * @param name
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Software findByName(String name) throws BusinessException {
        org.pimslims.model.experiment.Software pSoftware =
            getVersion().findFirst(org.pimslims.model.experiment.Software.class,
                org.pimslims.model.experiment.Software.PROP_NAME, name);
        Software xSoftware = new Software();
        xSoftware.setId(pSoftware.getDbId());
        xSoftware.setName(pSoftware.getName());
        xSoftware.setVersion(pSoftware.getVersion());
        xSoftware.setVendorName(pSoftware.getVendorName());
        xSoftware.setDescription(pSoftware.getDetails());
        xSoftware.setVendorAddress(pSoftware.getVendorAddress());
        if (pSoftware.getVendorWebAddress() != null && pSoftware.getVendorWebAddress().length() > 0)
            try {
                xSoftware.setVendorURL(new URL(pSoftware.getVendorWebAddress()));
            } catch (MalformedURLException e) {
                // invalid url
                throw new BusinessException(e);
            }
        return xSoftware;
    }

    /**
     * @param software
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(Software software) throws BusinessException {
        //create pims software
        try {
            org.pimslims.model.experiment.Software pSoftware =
                new org.pimslims.model.experiment.Software(getWritableVersion(), software.getName(), software
                    .getVersion());
            updatePSofware(software, pSoftware);
        } catch (ConstraintException e) {
            // invalid values, e.g. name already exists
            throw new BusinessException(e);
        }
    }

    private void updatePSofware(Software software, org.pimslims.model.experiment.Software pSoftware)
        throws ConstraintException {
        pSoftware.setVersion(software.getVersion());
        pSoftware.setVendorName(software.getVendorName());
        pSoftware.setDetails(software.getDescription());
        pSoftware.setVendorAddress(software.getVendorAddress());
        if (software.getVendorURL() != null)
            pSoftware.setVendorWebAddress(software.getVendorURL().toString());

        software.setId(pSoftware.getDbId());
    }

    /**
     * update pims' software which find by name
     * 
     * @param software
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void update(Software software) throws BusinessException {
        try {
            org.pimslims.model.experiment.Software pSoftware =
                getVersion().findFirst(org.pimslims.model.experiment.Software.class,
                    org.pimslims.model.experiment.Software.PROP_NAME, software.getName());
            updatePSofware(software, pSoftware);
        } catch (ConstraintException e) {
            // invalid values, e.g. name already exists
            throw new BusinessException(e);
        }

    }

    public Collection<Software> findAll(BusinessCriteria criteria) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
