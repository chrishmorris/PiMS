/**
 * xtalPiMSImpl org.pimslims.crystallization.dao SampleDAO.java
 * 
 * @author bl67
 * @date 11 Mar 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 bl67 This library is free software; you can redistribute it and/or modify it
 *           under the terms of the GNU Lesser General Public License as published by the Free Software
 *           Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.crystallization.dao;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.business.core.model.Construct;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;

/**
 * SampleDAO
 * 
 */
public class SampleDAO extends GenericDAO<Sample, org.pimslims.business.core.model.Sample> {
    /**
     * Experiment type used to record summary of production process. Usually there will be a more detailed
     * record, ending with purification. In that case no experiment of this type will be created.
     */
    public static final String EXPERIMENT_TYPE = "Protein production summary";

    /**
     * Sample category name for protein available for crustallogenesis
     */
    public static final String PURIFIED_PROTEIN = TrialDropDAO.PURIFIED_PROTEIN;

    /**
     * @param version
     */
    public SampleDAO(final ReadableVersion version) {
        super(version);
    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#createPORelated(org.pimslims.metamodel.AbstractModelObject,
     *      org.pimslims.business.XtalObject)
     */
    @Override
    protected void createPORelated(final Sample pobject, final org.pimslims.business.core.model.Sample xobject)
        throws ConstraintException, BusinessException, ModelException {
        //set SampleCategory
        final SampleCategory purifiedProtein = SampleDAO.getSampleCategory(this.version);
        pobject.addSampleCategory(purifiedProtein);
        //set construct
        if (xobject.getConstruct() != null) {
            final ConstructDAO constructDAO = new ConstructDAO(version);
            final ResearchObjective expblueprint = constructDAO.getPO(xobject.getConstruct());
            Experiment exp;
            if (pobject.getOutputSample() == null) {
                exp = createPreExpForSample(pobject);
            } else {
                exp = pobject.getOutputSample().getExperiment();
            }
            exp.setResearchObjective(expblueprint);
            if (xobject.getCreateDate() != null) {
                exp.setStartDate(xobject.getCreateDate());
            }

        }

    }

    private Experiment createPreExpForSample(final Sample pobject) throws ConstraintException {

        final Calendar today = Calendar.getInstance();
        final Experiment exp =
            new Experiment(getWritableVersion(), "experiment" + pobject.getDbId(), today, today,
                getSampleDefaultExpType(version));
        final OutputSample outsample = new OutputSample(getWritableVersion(), exp);
        outsample.setSample(pobject);
        return exp;
    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#getFullAttributes(org.pimslims.business.XtalObject)
     */
    @Override
    protected Map<String, Object> getFullAttributes(final org.pimslims.business.core.model.Sample xobject) {
        final Map<String, Object> attributes = new HashMap<String, Object>(getKeyAttributes(xobject));

        attributes.put(Sample.PROP_DETAILS, xobject.getDescription());
        attributes.put(Sample.PROP_ISACTIVE, true);
        attributes.put(Sample.PROP_PH, new Float(xobject.getPH()));
        attributes.put(Sample.PROP_BATCHNUM, xobject.getBatchReference());
        return attributes;
    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#getKeyAttributes(org.pimslims.business.XtalObject)
     */
    @Override
    protected Map<String, Object> getKeyAttributes(final org.pimslims.business.core.model.Sample xobject) {
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(Sample.PROP_NAME, xobject.getName());
        return attributes;
    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#getPClass()
     */
    @Override
    protected Class<Sample> getPClass() {
        return Sample.class;
    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#loadXAttribute(org.pimslims.metamodel.AbstractModelObject)
     */
    @Override
    protected org.pimslims.business.core.model.Sample loadXAttribute(final Sample pobject)
        throws BusinessException {
        final org.pimslims.business.core.model.Sample sample =
            new org.pimslims.business.core.model.Sample(pobject.getName());
        sample.setDescription(pobject.getDetails());
        if (pobject.getPh() != null) {
            sample.setPH(pobject.getPh());
        }
        return sample;
    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#loadXRole(org.pimslims.business.XtalObject,
     *      org.pimslims.metamodel.AbstractModelObject)
     */
    @Override
    protected org.pimslims.business.core.model.Sample loadXRole(
        final org.pimslims.business.core.model.Sample xobject, final Sample pobject) throws BusinessException {
        final ConstructDAO constructDAO = new ConstructDAO(version);
        Construct construct = null;
        final OutputSample os = pobject.getOutputSample();
        if (null != os) {
            final ResearchObjective eb = os.getExperiment().getResearchObjective();
            if (os.getExperiment().getEndDate() != null) {
                final Calendar createDate = Calendar.getInstance();
                createDate.setTimeInMillis(os.getExperiment().getEndDate().getTime().getTime());
                xobject.setCreateDate(createDate);
            }
            if (null != eb) {
                construct = constructDAO.getSimpleXO(eb);
            }
        }
        xobject.setConstruct(construct);

        return xobject;
    }

    /**
     * @see org.pimslims.crystallization.dao.GenericDAO#updatePORelated(org.pimslims.metamodel.AbstractModelObject,
     *      org.pimslims.business.XtalObject)
     */
    @Override
    protected void updatePORelated(final Sample pobject, final org.pimslims.business.core.model.Sample xobject)
        throws ModelException {
        // TODO Auto-generated method stub

    }

    public static ExperimentType getSampleDefaultExpType(final ReadableVersion version) {
        final ExperimentType et =
            version.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, EXPERIMENT_TYPE);
        assert null != et : "You have no ExperimentType called: " + EXPERIMENT_TYPE
            + "\nPlease load the PiMS reference data";
        return et;
    }

    public static SampleCategory getSampleCategory(final ReadableVersion version) {
        final SampleCategory sc =
            version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, PURIFIED_PROTEIN);
        assert null != sc : "You have no sample category called: " + PURIFIED_PROTEIN
            + "\nPlease load the PiMS reference data";
        return sc;
    }

    public org.pimslims.business.core.model.Sample findByName(final String name) throws BusinessException {
        final org.pimslims.model.sample.Sample pimsSample =
            version.findFirst(org.pimslims.model.sample.Sample.class,
                org.pimslims.model.sample.Sample.PROP_NAME, name);
        if (null == pimsSample) {
            return null;
        }
        org.pimslims.business.core.model.Sample xSample = getFullXO(pimsSample);
        //TODO set concentration
        return xSample;
    }

}
