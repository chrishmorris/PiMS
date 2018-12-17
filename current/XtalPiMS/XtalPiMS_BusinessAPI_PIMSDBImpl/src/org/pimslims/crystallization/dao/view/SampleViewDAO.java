package org.pimslims.crystallization.dao.view;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessCriterion;
import org.pimslims.business.crystallization.view.SampleView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.SampleDAO;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;

public class SampleViewDAO extends AbstractSQLViewDAO<SampleView> {

    public SampleViewDAO(final ReadableVersion version) {
        super(version);
    }

    @Override
    public String convertPropertyName(final String propertyName) throws BusinessException {
        if (propertyName == null) {
            throw new IllegalArgumentException("Null filter name");
        }
        if (propertyName.equals(SampleView.PROP_NAME)) {
            return "abstactSample.name";
        } else if (propertyName.equals(SampleView.PROP_ID)) {
            return "sample.abstractsampleid";
        } else if (propertyName.equals(SampleView.PROP_DESCRIPTION)) {
            return "baseSample.details";
        } else if (propertyName.equals(SampleView.PROP_CONSTRUCT_ID)) {
            return "construct.labbookentryid";
        } else if (propertyName.equals(SampleView.PROP_PH)) {
            return "abstactSample.ph";
        } else if (propertyName.equals(SampleView.PROP_CREATE_DATE)) {
            return "exp.startdate";
        } else if (propertyName.equals(SampleView.PROP_CONSTRUCT_NAME)) {
            return "construct.commonname";
        } else if (propertyName.equals(SampleView.PROP_OWNER)) {
            return "creator.name";
        } else if (propertyName.equals(SampleView.PROP_BATCH_REFERENCE)) {
            return "sample.batchnum";
        } else if (propertyName.equals(SampleView.PROP_GROUP)) {
            return "accessobject.name";
        }
        return "IGNORED";
    }

    private void processSampleCriteria(final BusinessCriteria criteria) {
        for (final BusinessCriterion c : criteria.getCriteria()) {
            if (c.getProperty().equals("IGNORED")) {
                criteria.remove(c);
            }
        }

    }

    @Override
    String getCountableName() {
        return "sample";
    }

    @Override
    Class<? extends ModelObject> getRootClass() {
        return Sample.class;
    }

    @Override
    public String getViewSql(final BusinessCriteria criteria) {
        processSampleCriteria(criteria);
        final String selectHQL =
            "select sample.abstractsampleid as sampleid, abstactSample.name, baseSample.details, abstactSample.ph, "
                + "construct.labbookentryid as constructid,construct.commonname, exp.startdate, accessobject.name as groupname, "
                + "sample.batchnum,creator.name as creatorName, baseSample.accessid "
                + "from sam_sample sample "
                + "join sam_sampca2abstsa sc on sc.abstractsampleid=sample.abstractsampleid "
                + "join sam_abstractsample abstactSample on abstactSample.labbookentryid=sample.abstractsampleid "
                + "join (select min(experimentid) as experimentid, sampleid from expe_outputsample group by sampleid) outputsample on outputsample.sampleid=sample.abstractsampleid "
                + "join expe_experiment exp on exp.labbookentryid=outputsample.experimentid "
                + "join targ_researchobjective construct on construct.labbookentryid=exp.researchobjectiveid "
                + "join core_labbookentry baseSample on baseSample.dbid=sample.abstractsampleid "
                + "left join core_accessobject accessobject on accessobject.systemclassid=baseSample.accessid "
                + "left join acco_user creator on creator.systemclassid=baseSample.creatorid ";

        return buildViewQuerySQL(criteria, selectHQL, "sc.samplecategoryid=" + getSCID(), "baseSample",
            Sample.class);
    }

    static String samplecategoryHook = null;

    private Long getSCID() {
        SampleCategory sc = null;
        if (samplecategoryHook != null) {
            sc = this.version.get(samplecategoryHook);
        }
        if (sc == null) {
            sc =
                this.version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME,
                    SampleDAO.PURIFIED_PROTEIN);
            assert sc != null : "reference data missing for SampleCategory: " + SampleDAO.PURIFIED_PROTEIN;
            samplecategoryHook = sc.get_Hook();
        }
        return sc.getDbId();
    }

    @Override
    Collection<SampleView> runSearch(final org.hibernate.SQLQuery hqlQuery) {
        final Collection<SampleView> views = new LinkedList<SampleView>();
        final SQLQuery q = hqlQuery;
        /* q.addScalar("sampleid", StandardBasicTypes.LONG);
        q.addScalar("name", StandardBasicTypes.STRING);
        q.addScalar("details", StandardBasicTypes.STRING);
        q.addScalar("ph", StandardBasicTypes.FLOAT);
        q.addScalar("constructid", StandardBasicTypes.LONG);
        q.addScalar("commonname", StandardBasicTypes.STRING);
        q.addScalar("startdate", StandardBasicTypes.CALENDAR);
        q.addScalar("groupname", StandardBasicTypes.STRING);
        q.addScalar("batchnum", StandardBasicTypes.STRING);
        q.addScalar("creatorName", StandardBasicTypes.STRING); */

        final List<?> results = hqlQuery.list();
        // int j = 0;
        for (final Object object : results) {
            final Object result[] = (Object[]) object;

            final SampleView view = new SampleView();
            view.setSampleId(AbstractSQLViewDAO.getLong(result[0]));
            view.setSampleName((String) result[1]);
            view.setDescription((String) result[2]);
            view.setPH(AbstractSQLViewDAO.getFloat(result[3]));
            view.setConstructId(AbstractSQLViewDAO.getLong(result[4]));
            view.setConstructName((String) result[5]);
            view.setCreateDate(getCalDate(result[6]));
            view.setGroup((String) result[7]);
            view.setBatchReference((String) result[8]);
            view.setOwner((String) result[9]);
            views.add(view);
        }
        return views;
    }

}
