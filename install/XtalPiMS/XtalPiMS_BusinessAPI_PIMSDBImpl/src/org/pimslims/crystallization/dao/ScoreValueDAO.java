/**
 * xtalPiMS Business API - PIMSDB Impl org.pimslims.crystallization.dao ScoreValueDAO.java
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

import java.util.HashMap;
import java.util.Map;

import org.pimslims.business.crystallization.model.ScoreValue;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.crystallization.Score;

/**
 * ScoreValueDAO
 * 
 */
public class ScoreValueDAO extends GenericDAO<Score, ScoreValue> {

    /**
     * Constructor for ScoreValueDAO
     * 
     * @param version
     */
    public ScoreValueDAO(final ReadableVersion version) {
        super(version);
    }

    /**
     * ScoreValueDAO.createPORelated
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#createPORelated(org.pimslims.metamodel.AbstractModelObject,
     *      org.pimslims.business.XtalObject)
     */
    @Override
    protected void createPORelated(final Score pobject, final ScoreValue xobject) throws ConstraintException,
        BusinessException, ModelException {
        // nothing

    }

    /**
     * ScoreValueDAO.getFullAttributes
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#getFullAttributes(org.pimslims.business.XtalObject)
     */
    @Override
    protected Map<String, Object> getFullAttributes(final ScoreValue xobject) {
        final Map<String, Object> attributes = new HashMap<String, Object>(getKeyAttributes(xobject));
        attributes.put(Score.PROP_COLOR, (xobject).getIntColour() + "");
        attributes.put(Score.PROP_NAME, (xobject).getDescription());
        return attributes;
    }

    /**
     * ScoreValueDAO.getKeyAttributes
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#getKeyAttributes(org.pimslims.business.XtalObject)
     */
    @Override
    protected Map<String, Object> getKeyAttributes(final ScoreValue xobject) {
        final ScoringSchemeDAO scoringSchemeDAO = new ScoringSchemeDAO(version);
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(Score.PROP_VALUE, (xobject).getValue());
        attributes.put(Score.PROP_SCORINGSCHEME, scoringSchemeDAO.getPO((xobject).getScoringScheme()));
        return attributes;
    }

    /**
     * ScoreValueDAO.getPClass
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#getPClass()
     */
    @Override
    protected Class<Score> getPClass() {
        return Score.class;
    }

    /**
     * ScoreValueDAO.loadXAttribute
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#loadXAttribute(org.pimslims.metamodel.AbstractModelObject)
     */
    @Override
    protected ScoreValue loadXAttribute(final Score pobject) throws BusinessException {
        final ScoreValue xScoreValue = new ScoreValue();
        xScoreValue.setIntColour(new Integer(pobject.getColor()));
        xScoreValue.setDescription(pobject.getName());
        xScoreValue.setValue(pobject.getValue());
        return xScoreValue;
    }

    /**
     * ScoreValueDAO.loadXRole
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#loadXRole(org.pimslims.business.XtalObject,
     *      org.pimslims.metamodel.AbstractModelObject)
     */
    @Override
    protected ScoreValue loadXRole(final ScoreValue xobject, final Score pobject) throws BusinessException {
        return xobject;
    }

    /**
     * ScoreValueDAO.updatePORelated
     * 
     * @see org.pimslims.crystallization.dao.GenericDAO#updatePORelated(org.pimslims.metamodel.AbstractModelObject,
     *      org.pimslims.business.XtalObject)
     */
    @Override
    protected void updatePORelated(final Score pobject, final ScoreValue xobject) throws ModelException {
        // TODO Auto-generated method stub

    }

}
