package org.pimslims.crystallization.dao.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessCriterion;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.criteria.EqualsExpression;
import org.pimslims.business.criteria.PropertyNameConvertor;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.view.ConditionView;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.crystallization.view.InspectionView;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.crystallization.view.TrialDropView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.logging.Logger;

public class TrialDropViewDAO implements ViewDAO<TrialDropView>, PropertyNameConvertor<TrialDropView> {
    private static final Logger log = Logger.getLogger(AbstractViewDAO.class);

    private final ReadableVersion version;

    private final ImageViewDAO imageDAO;

    private final ConditionViewDAO conditionDAO;

    private final HumanScoreViewDAO humanScoreDAO;

    private final SoftwareScoreViewDAO softwareScoreDAO;

    public TrialDropViewDAO(final ReadableVersion version) {
        super();
        this.version = version;
        imageDAO = new ImageViewDAO(version);
        conditionDAO = new ConditionViewDAO(version);
        humanScoreDAO = new HumanScoreViewDAO(version);
        softwareScoreDAO = new SoftwareScoreViewDAO(version);
    }

    public Collection<TrialDropView> findViews(final BusinessCriteria criteria) throws BusinessException {
        log.debug("Finding trialDropViews... ");
        final BusinessCriteria trialDropCriteria = processCriteria(criteria);

        // Null returned when latest inspection asked for but no inspections yet
        // TODO We can do better than this - eg conditionViews should still be valid
        if (null == trialDropCriteria) {
            return new ArrayList<TrialDropView>();
        }

        //find related image views
        final BusinessCriteria imageCriteria = getImageCriteria(trialDropCriteria);
        final Collection<ImageView> imageViews = imageDAO.findViews(imageCriteria);
        log.debug("Found " + imageViews.size() + " images");

        String screenName = null;
        if (!imageViews.isEmpty()) {
            screenName = imageViews.iterator().next().getScreen();
        }
        Collection<ConditionView> conditionViews = null;
        if (screenName != null) {
            //find related conditions
            final BusinessCriteria conditionCriteria = new BusinessCriteria(conditionDAO);
            conditionCriteria.add(BusinessExpression.Equals(ConditionView.PROP_LOCAL_NAME, screenName, true));
            conditionViews = conditionDAO.findViews(conditionCriteria);
            log.debug("Found " + conditionViews.size() + " conditions");
        }
        //find human scores
        final BusinessCriteria humanScoreCriteria = getHumanScoreCriteria(trialDropCriteria);
        final Collection<ScoreView> humanScoreViews = humanScoreDAO.findViews(humanScoreCriteria);
        log.debug("Found " + humanScoreViews.size() + " human scores");

        //find software scores
        final BusinessCriteria softwareScoreCriteria = getSoftwareScoreCriteria(trialDropCriteria);
        final Collection<ScoreView> softwareScoreViews = softwareScoreDAO.findViews(softwareScoreCriteria);
        log.debug("Found " + softwareScoreViews.size() + " software scores");

        //TrialDropView is based on imageViews and conditionView
        final Collection<TrialDropView> trialDropViews =
            covertToTrialDropViews(imageViews, conditionViews, humanScoreViews, softwareScoreViews);

        return trialDropViews;
    }

    private BusinessCriteria processCriteria(final BusinessCriteria criteria) throws BusinessException {
        final BusinessCriteria trialDropCriteria = criteria;
        String barcode = null;
        String inspectionName = null;
        EqualsExpression inspectionExpression = null;
        for (final BusinessCriterion businessCriterion : trialDropCriteria.getCriteria()) {
            if (businessCriterion instanceof EqualsExpression) {
                if (ImageView.PROP_BARCODE.equals(businessCriterion.getProperty())) {
                    barcode = (String) ((EqualsExpression) businessCriterion).getValue();
                } else if (ImageView.PROP_INSPECTION_NAME.equals(businessCriterion.getProperty())) {
                    inspectionName = (String) ((EqualsExpression) businessCriterion).getValue();
                    inspectionExpression = (EqualsExpression) businessCriterion;
                }
            }
        }
        if (barcode != null && inspectionName != null && inspectionName.equals("-")) {
            criteria.remove(inspectionExpression);
            final String lastInspectionName = getLastInspectionName(barcode);
            if (null == lastInspectionName) {
                log.info("Failed to find last inspection name for plate " + barcode
                    + " - assuming no inspections yet");
                return null;
            }
            criteria.add(new EqualsExpression(ImageView.PROP_INSPECTION_NAME, lastInspectionName, true));
            log.debug("Found last inspection name=" + lastInspectionName + " for plate " + barcode);

        }

        return trialDropCriteria;

    }

    private String getLastInspectionName(final String barcode) throws BusinessException {
        final InspectionViewDAO inspectionDAO = new InspectionViewDAO(this.version);
        final BusinessCriteria criteria = new BusinessCriteria(inspectionDAO);
        criteria.add(BusinessExpression.Equals(ImageView.PROP_BARCODE, barcode, true));
        final Collection<InspectionView> lastInspection = inspectionDAO.findLatest(criteria);

        // JMD This is not correct - causes problems with plates prior to first imaging
        //assert lastInspection.size() == 1 : "Should find 1 inspection for barcode:" + barcode;
        if (lastInspection.isEmpty()) {
            return null;
        }
        return lastInspection.iterator().next().getInspectionName();
    }

    /**
     * 
     * TrialDropViewDAO.covertToTrialDropViews
     * 
     * @param imageViews
     * @param conditionViews
     * @param softwareScoreViews
     * @param humanScoreViews
     * @return Map<well,trialDropView>
     */
    private Collection<TrialDropView> covertToTrialDropViews(final Collection<ImageView> imageViews,
        final Collection<ConditionView> conditionViews, final Collection<ScoreView> humanScoreViews,
        final Collection<ScoreView> softwareScoreViews) {
        final List<TrialDropView> trialDropViews = new LinkedList<TrialDropView>();
        if (imageViews.size() == 0) {
            return trialDropViews;
        }
        final Map<String, ConditionView> conditionMapViews = convertToConditionMap(conditionViews);
        final Map<String, List<ScoreView>> humanScoreMapViews = convertToScoreViewMap(humanScoreViews);
        final Map<String, List<ScoreView>> softwareScoreMapViews = convertToScoreViewMap(softwareScoreViews);
        Map<String, TrialDropView> wellToDrops = new HashMap<String, TrialDropView>();
        for (final ImageView imageView : imageViews)
            if (imageView.getUrl() != null && imageView.getUrl().length() > 1) {
                final String well = imageView.getWell();
                TrialDropView dropView;
                if (wellToDrops.containsKey(well)) {
                    dropView = wellToDrops.get(well);
                } else {
                    dropView = new TrialDropView();
                    trialDropViews.add(dropView);
                    wellToDrops.put(well, dropView);
                }
                //put information from ImageViews to TrialDropView
                dropView.setBarcode(imageView.getBarcode());
                dropView.getImages().add(imageView);
                dropView.setWell(well);
                //put condition to TrialDropView
                final String wellWithNoSubPosition = (new WellPosition(well)).toStringNoSubPosition();
                if (conditionMapViews != null) {
                    final ConditionView conditionView = conditionMapViews.get(wellWithNoSubPosition);
                    dropView.setCondition(conditionView);
                }
                //put software score to TrialDropView
                if (softwareScoreMapViews.containsKey(well)) {
                    dropView.setSoftwareScores(softwareScoreMapViews.get(well));
                    //  dropView.getImages().iterator().next().setColour(
                    //      dropView.getSoftwareScores().iterator().next().getColour());
                }

                //put human score to TrialDropView
                if (humanScoreMapViews.containsKey(well)) {
                    dropView.setHumanScores(humanScoreMapViews.get(well));
                    //  dropView.getImages().iterator().next().setColour(
                    //      dropView.getHumanScores().iterator().next().getColour());
                }

            }
        Collections.sort(trialDropViews);
        return trialDropViews;
    }

    /**
     * 
     * TrialDropViewDAO.convertToScoreViewMap
     * 
     * @param softwareScoreViews
     * @return Map<well_full,List<ScoreView>>
     */
    private Map<String, List<ScoreView>> convertToScoreViewMap(final Collection<ScoreView> scoreViews) {
        final Map<String, List<ScoreView>> scoreMapViews = new HashMap<String, List<ScoreView>>();
        for (final ScoreView scoreView : scoreViews) {
            final String well = scoreView.getWell();
            if (!scoreMapViews.containsKey(well)) {
                scoreMapViews.put(well, new LinkedList<ScoreView>());
            }
            scoreMapViews.get(well).add(scoreView);
        }
        return scoreMapViews;
    }

    /**
     * 
     * TrialDropViewDAO.convertToConditionMap
     * 
     * @param conditionViews
     * @return Map<Well_NoSubPosition, ConditionView>
     */
    private Map<String, ConditionView> convertToConditionMap(final Collection<ConditionView> conditionViews) {
        if (conditionViews == null) {
            return null;
        }
        final Map<String, ConditionView> conditionMapViews = new HashMap<String, ConditionView>();
        for (final ConditionView conditionView : conditionViews) {
            conditionMapViews.put(conditionView.getWell(), conditionView);
        }

        return conditionMapViews;
    }

    //get the only 3 useful criteria for Image search are barcode and inspection name , well
    //TODO or instrument?
    private BusinessCriteria getImageCriteria(final BusinessCriteria criteria) throws BusinessException {
        final BusinessCriteria imageCriteria = new BusinessCriteria(imageDAO);
        final List<BusinessCriterion> trialDropCrierias = criteria.getCriteria();
        for (final BusinessCriterion businessCriterion : trialDropCrierias) {
            if (businessCriterion.getProperty().equals(ImageView.PROP_BARCODE)) {
                imageCriteria.add(BusinessExpression.Equals(ImageView.PROP_BARCODE,
                    ((EqualsExpression) businessCriterion).getValue(), true));
                log.debug(businessCriterion);
            } else if (businessCriterion.getProperty().equals(ImageView.PROP_INSPECTION_NAME)) {
                imageCriteria.add(BusinessExpression.Equals(ImageView.PROP_INSPECTION_NAME,
                    ((EqualsExpression) businessCriterion).getValue(), true));
                log.debug(businessCriterion);
            } else if (businessCriterion.getProperty().equals(ImageView.PROP_WELL)) {
                addWellCriterion(imageCriteria, businessCriterion);
                log.debug(businessCriterion);
            } else if (businessCriterion.getProperty().equals(ImageView.PROP_TYPE)) {
                imageCriteria.add(BusinessExpression.Equals(ImageView.PROP_TYPE,
                    ((EqualsExpression) businessCriterion).getValue(), true));
                log.debug(businessCriterion);
            }
        }
        return imageCriteria;
    }

    private BusinessCriteria getHumanScoreCriteria(final BusinessCriteria criteria) throws BusinessException {
        final BusinessCriteria hsCriteria = new BusinessCriteria(humanScoreDAO);
        final List<BusinessCriterion> trialDropCrierias = criteria.getCriteria();
        for (final BusinessCriterion businessCriterion : trialDropCrierias) {
            if (businessCriterion.getProperty().equals(ImageView.PROP_BARCODE)) {
                hsCriteria.add(BusinessExpression.Equals(ImageView.PROP_BARCODE,
                    ((EqualsExpression) businessCriterion).getValue(), true));
                log.debug(businessCriterion);
            } else if (businessCriterion.getProperty().equals(ImageView.PROP_WELL)) {
                addWellCriterion(hsCriteria, businessCriterion);
                log.debug(businessCriterion);
            }
        }
        return hsCriteria;
    }

    private BusinessCriteria getSoftwareScoreCriteria(final BusinessCriteria criteria)
        throws BusinessException {
        final BusinessCriteria imageCriteria = new BusinessCriteria(softwareScoreDAO);
        final List<BusinessCriterion> trialDropCrierias = criteria.getCriteria();
        for (final BusinessCriterion businessCriterion : trialDropCrierias) {
            if (businessCriterion.getProperty().equals(ImageView.PROP_BARCODE)) {
                imageCriteria.add(BusinessExpression.Equals(ImageView.PROP_BARCODE,
                    ((EqualsExpression) businessCriterion).getValue(), true));
                log.debug(businessCriterion);
            } else if (businessCriterion.getProperty().equals(ImageView.PROP_INSPECTION_NAME)) {
                imageCriteria.add(BusinessExpression.Equals(ImageView.PROP_INSPECTION_NAME,
                    ((EqualsExpression) businessCriterion).getValue(), true));
                log.debug(businessCriterion);
            } else if (businessCriterion.getProperty().equals(ImageView.PROP_WELL)) {
                addWellCriterion(imageCriteria, businessCriterion);
                log.debug(businessCriterion);
            }
        }
        return imageCriteria;
    }

    /**
     * translate the well criteria to row,col,sub position criteria TrialDropViewDAO.addWellCriterion
     * 
     * @param criteria
     * @param wellCriterion
     * @throws BusinessException
     */
    public static void addWellCriterion(final BusinessCriteria criteria, final BusinessCriterion wellCriterion)
        throws BusinessException {
        assert wellCriterion.getProperty().equals("well");
        final EqualsExpression wellExpression = (EqualsExpression) wellCriterion;
        final String well = (String) wellExpression.getValue();
        final WellPosition wellPostion = new WellPosition(well);
        criteria.add(BusinessExpression.Equals("row", wellPostion.getRow(), true));
        criteria.add(BusinessExpression.Equals("col", wellPostion.getColumn(), true));
        criteria.add(BusinessExpression.Equals("sub", wellPostion.getSubPosition(), true));
    }

    public Integer getViewCount(final BusinessCriteria criteria) throws BusinessException {
        final BusinessCriteria imageCriteria = getImageCriteria(criteria);
        return imageDAO.findViewCount(imageCriteria);
    }

    public String convertPropertyName(final String property) throws BusinessException {
        if (property == null) {
            throw new IllegalArgumentException("Null filter name");
        }
        return property;
    }

    public Integer findViewCount(final BusinessCriteria criteria) throws BusinessException {
        return getViewCount(criteria);
    }

}
