/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.crystallization.implementation;

import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.ScoringScheme;
import org.pimslims.business.crystallization.service.ScoringSchemeService;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.ScoringSchemeDAO;

/**
 * 
 * @author ian
 */
public class ScoringSchemeServiceImpl extends BaseServiceImpl implements ScoringSchemeService {
    ScoringSchemeDAO scoringSchemeDAO;

    public ScoringSchemeServiceImpl(final DataStorage baseStorage) {
        super(baseStorage);
        scoringSchemeDAO = new ScoringSchemeDAO(version);
    }

    public ScoringScheme find(final long id) throws BusinessException {
        return scoringSchemeDAO.getFullXO((org.pimslims.model.crystallization.ScoringScheme) version.get(id));

    }

    public ScoringScheme find(final String id) throws BusinessException {
        return scoringSchemeDAO.getFullXO((org.pimslims.model.crystallization.ScoringScheme) version.get(id));
    }

    /**
     * REQUIRED
     * 
     * @param name
     * @return
     * @throws org.pimslims.business.exception.BusinessException
     */
    public ScoringScheme findByName(final String name) throws BusinessException {
        return scoringSchemeDAO.findByName(name);
    }

    /**
     * REQUIRED create both scoringScheme and score values
     * 
     * @param scoringScheme
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void create(final ScoringScheme scoringScheme) throws BusinessException {
        scoringSchemeDAO.createPO(scoringScheme);
    }

    /**
     * @TODO REQUIRED
     * @param scoringScheme
     * @throws org.pimslims.business.exception.BusinessException
     */
    public void update(final ScoringScheme scoringScheme) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<ScoringScheme> findAll(final BusinessCriteria criteria) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
