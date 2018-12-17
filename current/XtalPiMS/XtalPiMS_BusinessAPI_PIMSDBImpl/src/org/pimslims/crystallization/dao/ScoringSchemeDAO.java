/**
 * xtalPiMS Business API - PIMSDB Impl org.pimslims.crystallization.dao ScoringSchemeDAO.java
 * 
 * @author bl67
 * @date 18 Mar 2009
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2009 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pimslims.business.crystallization.model.ScoreValue;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.crystallization.Score;
import org.pimslims.model.crystallization.ScoringScheme;

/**
 * ScoringSchemeDAO
 * 
 */
public class ScoringSchemeDAO extends
    GenericDAO<ScoringScheme, org.pimslims.business.crystallization.model.ScoringScheme> {

    /**
     * Constructor for ScoringSchemeDAO
     * 
     * @param version
     */
    public ScoringSchemeDAO(final ReadableVersion version) {
        super(version);
    }

    /**
     * ScoringSchemeDAO.createPORelated
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#createPORelated(org.pimslims.metamodel.AbstractModelObject,
     *      org.pimslims.business.XtalObject)
     */
    @Override
    protected void createPORelated(final ScoringScheme pobject,
        final org.pimslims.business.crystallization.model.ScoringScheme xobject) throws ConstraintException,
        BusinessException, ModelException {
        final ScoreValueDAO scoreValueDAO = new ScoreValueDAO(this.version);
        for (final ScoreValue xScoreValue : xobject.getScores()) {
            xScoreValue.setScoringScheme(xobject);
            scoreValueDAO.createPO(xScoreValue);
        }

    }

    /**
     * ScoringSchemeDAO.getFullAttributes
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#getFullAttributes(org.pimslims.business.XtalObject)
     */
    @Override
    protected Map<String, Object> getFullAttributes(
        final org.pimslims.business.crystallization.model.ScoringScheme xobject) {
        final Map<String, Object> attributes = new HashMap<String, Object>(getKeyAttributes(xobject));
        attributes.put(ScoringScheme.PROP_DETAILS, xobject.getDescription());
        attributes.put(ScoringScheme.PROP_VERSION, xobject.getVersion());
        return attributes;
    }

    /**
     * ScoringSchemeDAO.getKeyAttributes
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#getKeyAttributes(org.pimslims.business.XtalObject)
     */
    @Override
    protected Map<String, Object> getKeyAttributes(
        final org.pimslims.business.crystallization.model.ScoringScheme xobject) {
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(ScoringScheme.PROP_NAME, (xobject).getName());
        return attributes;
    }

    /**
     * ScoringSchemeDAO.getPClass
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#getPClass()
     */
    @Override
    protected Class<ScoringScheme> getPClass() {
        return ScoringScheme.class;
    }

    /**
     * ScoringSchemeDAO.loadXAttribute
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#loadXAttribute(org.pimslims.metamodel.AbstractModelObject)
     */
    @Override
    protected org.pimslims.business.crystallization.model.ScoringScheme loadXAttribute(
        final ScoringScheme pobject) throws BusinessException {
        final org.pimslims.business.crystallization.model.ScoringScheme xScoringScheme =
            new org.pimslims.business.crystallization.model.ScoringScheme();
        xScoringScheme.setDescription(pobject.getDetails());
        xScoringScheme.setName(pobject.getName());
        xScoringScheme.setVersion(pobject.getVersion());
        return xScoringScheme;
    }

    /**
     * ScoringSchemeDAO.loadXRole
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#loadXRole(org.pimslims.business.XtalObject,
     *      org.pimslims.metamodel.AbstractModelObject)
     */
    @Override
    protected org.pimslims.business.crystallization.model.ScoringScheme loadXRole(
        final org.pimslims.business.crystallization.model.ScoringScheme xobject, final ScoringScheme pobject)
        throws BusinessException {
        final ScoreValueDAO scoreValueDAO = new ScoreValueDAO(this.version);
        final List<ScoreValue> xScoreValues = new LinkedList<ScoreValue>();
        for (final Score pScore : pobject.getScores()) {
            final ScoreValue xScore = scoreValueDAO.getSimpleXO(pScore);
            xScore.setScoringScheme(xobject);
            xScoreValues.add(xScore);
        }
        Collections.sort(xScoreValues);
        xobject.setScores(xScoreValues);
        return xobject;
    }

    /**
     * ScoringSchemeDAO.updatePORelated
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#updatePORelated(org.pimslims.metamodel.AbstractModelObject,
     *      org.pimslims.business.XtalObject)
     */
    @Override
    protected void updatePORelated(final ScoringScheme pobject,
        final org.pimslims.business.crystallization.model.ScoringScheme xobject) throws ModelException {
        // TODO Auto-generated method stub

    }

    private static Map<String, org.pimslims.business.crystallization.model.ScoringScheme> ScoringSchemeNameHookMap =
        new HashMap<String, org.pimslims.business.crystallization.model.ScoringScheme>();

    public org.pimslims.business.crystallization.model.ScoringScheme findByName(final String name)
        throws BusinessException {
        ScoringScheme pScoringScheme = null;
        if (!ScoringSchemeNameHookMap.containsKey(name)) {
            pScoringScheme = version.findFirst(ScoringScheme.class, ScoringScheme.PROP_NAME, name);
            ScoringSchemeNameHookMap.put(name, this.getFullXO(pScoringScheme));
        }

        return ScoringSchemeNameHookMap.get(name);
    }
}
