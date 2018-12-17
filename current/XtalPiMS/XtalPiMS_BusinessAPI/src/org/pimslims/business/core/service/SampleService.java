/*
 * SampleService.java
 *
 * Created on 17 April 2007, 09:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.core.service;

import java.util.Collection;

import org.pimslims.business.BaseService;
import org.pimslims.business.ViewService;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.view.SampleQuantityView;
import org.pimslims.business.crystallization.view.SampleView;
import org.pimslims.business.exception.BusinessException;

/**
 * For this part I put things the way we use them, but I am sure that Anne Pajon or anyone else in the PIMS
 * team (or an advanced user) who know really better than me the PIMS DB, can better organize the APIs with
 * coherent parameters than me. Also some of these might already be covered by the Factory methods or beans
 * develop within PIMS.
 * 
 * @author IMB
 */
public interface SampleService extends BaseService, ViewService<SampleView> {
    /**
     * Get information on a specific sample.
     * 
     * @param sampleId sample reference
     * @return information on this sample (construct, concentration, pH, molecular weight, nb subunits, batch,
     *         origin, type, sub cellular location)
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Sample find(long sampleId) throws BusinessException;

    /**
     * 
     * @param name
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Sample findByName(String name) throws BusinessException;

    /**
     * 
     * @param paging
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<Sample> findAll(BusinessCriteria criteria) throws BusinessException;

    /**
     * 
     * @param sample
     * @param construct
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void setConstructForSample(Sample sample, Construct construct) throws BusinessException;

    /**
     * 
     * @param sample
     * @param person
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void setOperatorForSample(Sample sample, Person person) throws BusinessException;

    /**
     * 
     * @param sample
     * @throws org.pimslims.business.exception.BusinessException
     * @return
     */
    public Person getOwner(Sample sample) throws BusinessException;

    /**
     * 
     * @param sample
     * @param person
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void setOwnerForSample(Sample sample, Person person) throws BusinessException;

    /**
     * Insert new sample in the DB.
     * 
     * @param sample The sample details - contains all things which are related to a sample (construct,
     *            concentration, pH, molecular weight, nb subunits, batch, origin, type, sub cellular
     *            location)
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(Sample sample) throws BusinessException;

    /**
     * 
     * @param sample
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void update(Sample sample) throws BusinessException;

    /**
     * 
     * @param sample
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void close(Sample sample) throws BusinessException;

    /**
     * 
     * @param criteria
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public Collection<SampleQuantityView> findSampleQuantities(BusinessCriteria criteria)
        throws BusinessException;

}
