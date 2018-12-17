/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.crystallization.implementation;

import java.util.Collection;
import java.util.Iterator;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.SoftwareScore;
import org.pimslims.business.crystallization.service.SoftwareScoreService;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.ImageDAO;
import org.pimslims.crystallization.dao.ScoreDAO;
import org.pimslims.crystallization.dao.SoftwareDAO;
import org.pimslims.crystallization.dao.view.SoftwareScoreViewDAO;
import org.pimslims.crystallization.dao.view.ViewDAO;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.crystallization.DropAnnotation;
import org.pimslims.model.crystallization.Score;
import org.pimslims.model.experiment.Software;

/**
 * 
 * @author ian
 */
public class SoftwareScoreServiceImpl extends BaseServiceImpl implements SoftwareScoreService {

    public SoftwareScoreServiceImpl(final DataStorage baseStorage) {
        super(baseStorage);
    }

    public void create(final Collection<SoftwareScore> scores) throws BusinessException {
        final org.pimslims.dao.FlushMode oldFlushModel = ((WritableVersionImpl) version).getFlushMode();
        // improve performance
        ((WritableVersionImpl) version).setFlushMode(org.pimslims.dao.FlushMode.batchMode());

        final Iterator<SoftwareScore> it = scores.iterator();
        while (it.hasNext()) {
            create(it.next());
        }
        ((WritableVersionImpl) version).setFlushMode(oldFlushModel);
    }

    public void create(final SoftwareScore score) throws BusinessException {
        try {
            final Score pScore = ScoreDAO.getpimsScore(getVersion(), score.getValue());
            assert pScore != null; //score should be existing
            final Software software = SoftwareDAO.getpimsSoftware(getVersion(), score.getSoftwareAnnotator());
            assert software != null; //software should be existing
            final org.pimslims.model.crystallization.Image image =
                ImageDAO.getpimsImage(getVersion(), score.getImage());
            assert image != null;//image should be existing

            final DropAnnotation pUserScore = new DropAnnotation(getWritableVersion(), pScore);
            pUserScore.setSoftware(software);
            pUserScore.setImage(image);
            pUserScore.setScoreDate(score.getDate());
            pUserScore.setHolder(image.getSample().getHolder());

            score.setId(pUserScore.getDbId());
        } catch (final ConstraintException e) {
            throw new BusinessException(e);
        }
    }

    public SoftwareScore findSoftwareScore(final long id) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Integer findViewCount(final BusinessCriteria criteria) throws BusinessException {
        return getViewDAO().findViewCount(criteria);
    }

    public Collection<ScoreView> findViews(final BusinessCriteria criteria) throws BusinessException {
        return getViewDAO().findViews(criteria);
    }

    public String convertPropertyName(final String property) throws BusinessException {
        return getViewDAO().convertPropertyName(property);
    }

    private ViewDAO<ScoreView> viewDAO;

    private ViewDAO<ScoreView> getViewDAO() {
        if (viewDAO == null) {
            viewDAO = new SoftwareScoreViewDAO(version);
        }
        return viewDAO;
    }

}
