package org.pimslims.crystallization.dao.view;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessCriterion;
import org.pimslims.business.criteria.BusinessOrder;
import org.pimslims.business.criteria.EqualsExpression;
import org.pimslims.business.criteria.LikeExpression;
import org.pimslims.business.criteria.PropertyNameConvertor;
import org.pimslims.business.crystallization.view.InspectionView;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.AbstractModelObject;
import org.pimslims.model.crystallization.DropAnnotation;
import org.pimslims.model.holder.Holder;

public class HumanScoreViewDAO extends AbstractSQLViewDAO<ScoreView> implements
    PropertyNameConvertor<ScoreView> {

    /**
     * HumanScoreViewDAO.findViewCount
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractSQLViewDAO#findViewCount(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Integer findViewCount(final BusinessCriteria criteria) throws BusinessException {
        updateCriteria(criteria);
        return super.findViewCount(criteria);
    }

    /**
     * HumanScoreViewDAO.findViews
     * 
     * @throws BusinessException
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractSQLViewDAO#findViews(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<ScoreView> findViews(final BusinessCriteria criteria) throws BusinessException {
        updateCriteria(criteria);
        return super.findViews(criteria);

    }

    private BusinessCriteria updateCriteria(final BusinessCriteria criteria) throws BusinessException {
        BusinessCriterion barcodeCriterion = null;
        for (final BusinessCriterion businessCriterion : criteria.getCriteria()) {
            if (businessCriterion.getProperty().equals(convertPropertyName(ScoreView.PROP_BARCODE))) {
                barcodeCriterion = businessCriterion;
            }
        }
        if (barcodeCriterion == null) {
            return criteria;
        }
        // replace barcode with id
        final String barcode;
        if (barcodeCriterion instanceof EqualsExpression) {
            barcode = (String) ((EqualsExpression) barcodeCriterion).getValue();
        } else {
            barcode = ((LikeExpression) barcodeCriterion).getValue();
        }
        final AbstractModelObject holder = this.version.findFirst(Holder.class, Holder.PROP_NAME, barcode);
        assert null != holder : "Plate not found: " + barcode;
        criteria.remove(barcodeCriterion);
        criteria.add(new EqualsExpression("plateid", holder.getDbId(), true));

        // add orderbyDate if not added
        boolean orderByDate = false;
        for (final BusinessOrder orderBy : criteria.getOrderBy()) {
            if (orderBy.getProperty().equals(InspectionView.PROP_DATE)) {
                orderByDate = true;
            }

        }
        if (!orderByDate) {
            criteria.addOrder(BusinessOrder.DESC(InspectionView.PROP_DATE));
        }
        return criteria;
    }

    public HumanScoreViewDAO(final ReadableVersion version) {
        super(version);
    }

    @Override
    public String convertPropertyName(final String propertyName) throws BusinessException {
        /*
         * public static String PROP_BARCODE = "barcode" ->plate.name; public
         * static String PROP_WELL = "well"->; public static String PROP_DATE =
         * "date"; public static String PROP_DESCRIPTION = "description"; public
         * static String PROP_COLOUR = "colour"; public static String PROP_NAME
         * = "name"; public static String PROP_VERSION = "version"; public
         * static String PROP_TYPE = "type"; public static String
         * PROP_INSPECTION_NAME = "inspectionName";
         */
        if (propertyName == null) {
            throw new IllegalArgumentException("Null filter name");
        }
        if (propertyName.equals(ScoreView.PROP_BARCODE)) {
            return "plate.name";
        } else if (propertyName.equals("plateid")) {
            return "userScore.HOLDERID";
        } else if (propertyName.equals("row")) {
            return "sample.ROWPOSITION";
        } else if (propertyName.equals("col")) {
            return "sample.COLPOSITION";
        } else if (propertyName.equals("sub")) {
            return "sample.SUBPOSITION";
        } else if (propertyName.equals(ScoreView.PROP_DATE)) {
            return "userScore.scoreDate";
        } else if (propertyName.equals(ScoreView.PROP_DESCRIPTION)) {
            return "score.name";
        } else if (propertyName.equals(ScoreView.PROP_COLOUR)) {
            return "score.color";
        } else if (propertyName.equals(ScoreView.PROP_NAME)) {
            return "user_.name";
        } else if (propertyName.equals(ScoreView.PROP_VERSION)) {
            throw new UnsupportedOperationException(propertyName + " Not supported.");
        } else if (propertyName.equals(ScoreView.PROP_TYPE)) {
            throw new UnsupportedOperationException(propertyName + " Not supported.");
        } else if (propertyName.equals(ScoreView.PROP_INSPECTION_NAME)) {
            throw new UnsupportedOperationException(propertyName + " Not supported.");
        }
        throw new BusinessException("Unable to find matching property in " + this.getClass());

    }

    @Override
    String getCountableName() {
        return "userScore";
    }

    @Override
    Class<DropAnnotation> getRootClass() {
        return DropAnnotation.class;
    }

    @Override
    public String getViewSql(final BusinessCriteria criteria) {
        final String selectHQL =
            "select plate.name as barcode, sample.ROWPOSITION as row, sample.COLPOSITION as col, sample.SUBPOSITION as sub,"
                + "userScore.scoreDate as scoredate, score.color as color, user_.NAME as username, score.NAME as scorename, userScore_.accessid "
                + "from CRYZ_DROPANNOTATION userScore "
                + "join CORE_LABBOOKENTRY userScore_ on userScore.LabBookEntryID=userScore_.DBID "
                + "left join ACCO_USER user_ on userScore_.CREATORID=user_.SYSTEMCLASSID "
                + "join CRYZ_SCORE score on userScore.SCOREID=score.LabBookEntryID "
                + "left join SAM_SAMPLE sample on userScore.SAMPLEID=sample.ABSTRACTSAMPLEID "
                + "left join HOLD_ABSTRACTHOLDER plate on userScore.HOLDERID=plate.LabBookEntryID  ";

        final String subSelect = " userScore.imageid is null ";
        // TODO why is that? Was removed in r18995, reinstated in r18996
        return buildViewQuerySQL(criteria, selectHQL, subSelect, "userScore_", DropAnnotation.class);
    }

    /**
     * HumanScoreViewDAO.buildViewQuery
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractSQLViewDAO#buildViewQuerySQL(org.pimslims.business.criteria.BusinessCriteria,
     *      java.lang.String, java.lang.String, java.lang.String, java.lang.Class)
     */
    @Override
    protected String buildViewQuerySQL(final BusinessCriteria criteria, final String selectHQL,
        final String subSelect, final String accessControledMOName, final Class class1) {
        String sql = super.buildViewQuerySQL(criteria, selectHQL, subSelect, accessControledMOName, class1);
        sql = sql.replace("ORDER BY userScore.scoreDate", "ORDER BY userScore_.dbid");
        return sql;
    }

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
        q.addScalar("username", StandardBasicTypes.STRING);
        q.addScalar("scorename", StandardBasicTypes.STRING); */
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
            view.setInspectionName("");
            view.setDescription((String) result[7]);
            view.setType("human");
            views.add(view);
        }
        return views;
    }

}
