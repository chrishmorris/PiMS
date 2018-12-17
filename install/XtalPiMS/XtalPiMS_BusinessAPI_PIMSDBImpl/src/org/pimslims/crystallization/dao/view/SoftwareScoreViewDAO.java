/**
 * xtalPiMSImpl org.pimslims.crystallization.dao.view SoftwareScoreViewDAO.java
 * 
 * @author bl67
 * @date 2 May 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.dao.view;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.PropertyNameConvertor;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.crystallization.DropAnnotation;

/**
 * SoftwareScoreViewDAO
 * 
 */
public class SoftwareScoreViewDAO extends AbstractSQLViewDAO<ScoreView> implements
    PropertyNameConvertor<ScoreView> {

    /**
     * Constructor for SoftwareScoreViewDAO
     * 
     * @param version
     */
    public SoftwareScoreViewDAO(final ReadableVersion version) {
        super(version);
    }

    /**
     * SoftwareScoreViewDAO.convertPropertyName
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#convertPropertyName(java.lang.String)
     */
    @Override
    public String convertPropertyName(final String propertyName) throws BusinessException {
        if (propertyName == null) {
            throw new IllegalArgumentException("Null filter name");
        }
        if (propertyName.equals(ScoreView.PROP_BARCODE)) {
            return "plate.name";
        } else if (propertyName.equals(ScoreView.PROP_INSPECTION_NAME)) {
            return "inspection.name";
        } else if (propertyName.equals("row")) {
            return "sample.ROWPOSITION";
        } else if (propertyName.equals("col")) {
            return "sample.COLPOSITION";
        } else if (propertyName.equals("sub")) {
            return "sample.SUBPOSITION";
        } else if (propertyName.equals(ScoreView.PROP_DATE)) {
            return "softwareScore.scoreDate";
        } else if (propertyName.equals(ScoreView.PROP_DESCRIPTION)) {
            return "score.details";
        } else if (propertyName.equals(ScoreView.PROP_COLOUR)) {
            return "score.color";
        } else if (propertyName.equals(ScoreView.PROP_NAME)) {
            return "software.name";
        } else if (propertyName.equals(ScoreView.PROP_VERSION)) {
            return "software.version";
        } else if (propertyName.equals(ScoreView.PROP_TYPE)) {
            throw new UnsupportedOperationException(propertyName + " Not supported.");

        }
        throw new BusinessException("Unable to find matching property in " + this.getClass());
    }

    /**
     * SoftwareScoreViewDAO.getCountableName
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#getCountableName()
     */
    @Override
    String getCountableName() {
        return "dropannotaion";
    }

    /**
     * SoftwareScoreViewDAO.getRootClass
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#getRootClass()
     */
    @Override
    Class<DropAnnotation> getRootClass() {
        return DropAnnotation.class;
    }

    /**
     * SoftwareScoreViewDAO.getViewHQL
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#getViewHQL(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public String getViewSql(final BusinessCriteria criteria) {
        final String selectHQL =
            "select plate.name as barcode,"
                + "sample.ROWPOSITION as row,"
                + "sample.COLPOSITION as col,"
                + "sample.SUBPOSITION as sub,"
                + "dropannotaion.scoreDate as scoredate,"
                + "score.color as color, "
                + "software.name as softwarename,"
                + "score.name as scorename,"
                + "software.version as softwareversion, "
                + "inspection.name as inspectionname,"
                + "plate_.accessid as accessid "
                + "from CRYZ_DROPANNOTATION dropannotaion "
                + "join EXPE_SOFTWARE software on dropannotaion.SOFTWAREID=software.LabBookEntryID "
                + "join CRYZ_IMAGE image on dropannotaion.IMAGEID=image.LabBookEntryID "
                + "join SAM_SAMPLE sample on  image.SAMPLEID=sample.ABSTRACTSAMPLEID "
                + "join HOLD_ABSTRACTHOLDER plate on sample.HOLDERID=plate.LabBookEntryID "
                + "join CORE_LABBOOKENTRY plate_ on plate.LabBookEntryID=plate_.DBID "
                + "left join SCHE_SCHEDULEDTASK inspection on image.SCHEDULEDTASKID=inspection.LabBookEntryID "
                + "join CRYZ_SCORE score on dropannotaion.SCOREID=score.LabBookEntryID ";
        final String subSelect = " dropannotaion.SAMPLEID is null ";
        return buildViewQuerySQL(criteria, selectHQL, subSelect, "plate_", DropAnnotation.class);
    }

    /**
     * SoftwareScoreViewDAO.runSearch
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#runSearch(org.hibernate.Query)
     */
    @Override
    Collection<ScoreView> runSearch(final org.hibernate.SQLQuery hqlQuery) {
        final Collection<ScoreView> views = new LinkedList<ScoreView>();
        final SQLQuery q = hqlQuery;
        /* q.addScalar("barcode", StandardBasicTypes.STRING);
        q.addScalar("row", StandardBasicTypes.INTEGER);
        q.addScalar("col", StandardBasicTypes.INTEGER);
        q.addScalar("sub", StandardBasicTypes.INTEGER);
        q.addScalar("scoredate", StandardBasicTypes.CALENDAR);
        q.addScalar("color", StandardBasicTypes.STRING);
        q.addScalar("softwarename", StandardBasicTypes.STRING);
        q.addScalar("scorename", StandardBasicTypes.STRING);
        q.addScalar("softwareversion", StandardBasicTypes.STRING);
        q.addScalar("inspectionname", StandardBasicTypes.STRING); */

        final List<?> results = hqlQuery.list();
        // int j = 0;
        for (final Object object : results) {
            final Object result[] = (Object[]) object;
            /*
             * j++; for (int i = 0; i < result.length; i++) { log.debug("view" +
             * j + ":" + result[i]); }
             */

            final ScoreView view = new ScoreView();
            view.setBarcode((String) result[0]);
            final Integer row = (Integer) result[1];
            final Integer col = (Integer) result[2];
            final Integer sub = (Integer) result[3];
            view.setWell(getWell(row, col, sub));
            view.setDate(getCalDate(result[4]));
            view.setColourLong(new Long((String) result[5]));
            view.setName((String) result[6]);
            view.setDescription((String) result[7]);
            view.setVersion((String) result[8]);
            view.setInspectionName((String) result[9]);
            view.setType("software");
            views.add(view);
        }
        return views;
    }
}
