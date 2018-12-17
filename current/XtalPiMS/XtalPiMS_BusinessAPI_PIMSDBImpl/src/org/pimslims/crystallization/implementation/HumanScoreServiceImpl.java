/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.crystallization.implementation;

import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.UserScore;
import org.pimslims.business.crystallization.service.HumanScoreService;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.ScoreDAO;
import org.pimslims.crystallization.dao.TrialDropDAO;
import org.pimslims.crystallization.dao.view.HumanScoreViewDAO;
import org.pimslims.crystallization.dao.view.ViewDAO;
import org.pimslims.exception.ConstraintException;
import org.pimslims.logging.Logger;
import org.pimslims.model.crystallization.DropAnnotation;
import org.pimslims.model.crystallization.Score;
import org.pimslims.model.holder.Holder;

/**
 * 
 * @author ian
 */
public class HumanScoreServiceImpl extends BaseServiceImpl implements
		HumanScoreService {
	private static final Logger log = Logger
			.getLogger(PlateInspectionServiceImpl.class);

	public HumanScoreServiceImpl(final DataStorage baseStorage) {
		super(baseStorage);
	}

	public UserScore findHumanScore(final long id) throws BusinessException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void create(final UserScore score) throws BusinessException {
		try {
			final Score pScore = ScoreDAO.getpimsScore(getVersion(),
					score.getValue());
			assert pScore != null; // score should be existing

			final org.pimslims.model.sample.Sample sample = TrialDropDAO
					.getpimsSample(getVersion(), score.getDrop());
			getWritableVersion()
					.setDefaultOwner(sample.getHolder().getAccess());

			final DropAnnotation pUserScore = new DropAnnotation(
					getWritableVersion(), pScore);
			/*
			 * if (score.getUser() != null) { final User annotator = (new
			 * PersonDAO(getVersion())).getUser(score.getUser()); // no, can no
			 * longer do this pUserScore.setCreator(annotator); }
			 */
			pUserScore.setSample(sample);
			pUserScore.setScoreDate(score.getDate());
			final Holder holder = sample.getHolder();
			if (holder != null) {
				pUserScore.setHolder(holder);
				// update holder's cystal number
				holder.updateDerivedData();

			}
			score.setId(pUserScore.getDbId());
		} catch (final ConstraintException e) {
			throw new BusinessException(e);
		}
	}

	public Integer findViewCount(final BusinessCriteria criteria)
			throws BusinessException {
		return getViewDAO().findViewCount(criteria);
	}

	public Collection<ScoreView> findViews(final BusinessCriteria criteria)
			throws BusinessException {
		return getViewDAO().findViews(criteria);
	}

	public String convertPropertyName(final String propertyName)
			throws BusinessException {
		return getViewDAO().convertPropertyName(propertyName);
	}

	private ViewDAO<ScoreView> viewDAO;

	private ViewDAO<ScoreView> getViewDAO() {
		if (viewDAO == null) {
			viewDAO = new HumanScoreViewDAO(version);
		}
		return viewDAO;
	}
}
