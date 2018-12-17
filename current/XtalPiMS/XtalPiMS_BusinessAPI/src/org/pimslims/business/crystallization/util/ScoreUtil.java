/**
 * xtalPiMSApi org.pimslims.business.crystallization.util ScoreUtil.java
 * 
 * @author cm65
 * @date 25 Mar 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.business.crystallization.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.service.PersonService;
import org.pimslims.business.crystallization.model.ScoreValue;
import org.pimslims.business.crystallization.model.ScoringScheme;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.UserScore;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.HumanScoreService;
import org.pimslims.business.crystallization.service.ScoringSchemeService;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.exception.BusinessException;

/**
 * ScoreUtil
 * 
 */
public class ScoreUtil {

    public static void setScore(DataStorage dataStorage, final String barcode, final String well,
        final String scheme, final String annotation, String username) throws BusinessException {
        final WellPosition wellPosition = new WellPosition(well);

        final HumanScoreService scoreService = dataStorage.getHumanScoreService();
        final PersonService personService = dataStorage.getPersonService();

        final ScoringSchemeService scoringSchemeService = dataStorage.getScoringSchemeService();
        final TrialService trialService = dataStorage.getTrialService();
        //PlateInspection plateInspection = plateInspectionService.findByInspectionName(name);
        //Image image = imageService.findImage(barcode, wellPosition, plateInspection, null);
        final TrialDrop drop = trialService.findTrialDrop(barcode, wellPosition);
        final ScoringScheme scoringScheme = scoringSchemeService.findByName(scheme);

        final UserScore score = new UserScore();
        //TODO This should store the date of the image that was scored not the current date!
        final Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        score.setDate(cal);
        score.setUser(personService.findByUsername(username));
        score.setDrop(drop);

        final Iterator<ScoreValue> it = scoringScheme.getScores().iterator();
        ScoreValue scoreValue = null;
        while (it.hasNext()) {
            scoreValue = it.next();
            if (scoreValue.getDescription().equals(annotation)) {
                break;
            }
        }

        score.setValue(scoreValue);

        scoreService.create(score);
    }

}
