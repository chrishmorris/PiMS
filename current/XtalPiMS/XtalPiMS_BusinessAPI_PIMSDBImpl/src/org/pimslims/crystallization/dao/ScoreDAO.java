package org.pimslims.crystallization.dao;

import java.util.HashMap;
import java.util.Map;

import org.pimslims.logging.Logger;
import org.pimslims.business.crystallization.model.ScoreValue;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.crystallization.Score;
import org.pimslims.model.crystallization.ScoringScheme;

/**
 * <p>
 * Data Access Object for {@link org.pimslims.business.crystallization.model.ScoreValue}
 * {@link org.pimslims.business.crystallization.model.ScoringScheme}
 * {@link org.pimslims.business.crystallization.model.SoftwareScore}
 * {@link org.pimslims.business.crystallization.model.UserScore}
 * </p>
 * 
 * @author Bill Lin
 */
public class ScoreDAO {
    private static final Logger log = Logger.getLogger(ScoreDAO.class);

    public static Score getpimsScore(final ReadableVersion rv, final ScoreValue scoreValue) {
        if (scoreValue == null) {
            return null;
        }

        Score pScore = rv.get(Score.class, scoreValue.getId());
        if (pScore == null) {
            final Map<String, Object> pMap = new HashMap<String, Object>();
            pMap.put(Score.PROP_DETAILS, scoreValue.getDescription());
            if (scoreValue.getScoringScheme() != null) {
                final Map<String, Object> schemaMap = new HashMap<String, Object>();
                schemaMap.put(ScoringScheme.PROP_NAME, scoreValue.getScoringScheme().getName());
                pMap.put(Score.PROP_SCORINGSCHEME, schemaMap);
            }
            pScore = rv.findFirst(Score.class, pMap);
        }
        return pScore;

    }
}
