package org.pimslims.crystallization.dao.view;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessOrder;
import org.pimslims.business.criteria.PropertyNameConvertor;
import org.pimslims.business.crystallization.view.InspectionView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.holder.Holder;

public class InspectionViewDAO extends AbstractSQLViewDAO<InspectionView> implements
    PropertyNameConvertor<InspectionView> {

    public InspectionViewDAO(final ReadableVersion version) {
        super(version);
    }

    @Override
    public String convertPropertyName(final String propertyName) throws BusinessException {
        if (propertyName == null) {
            throw new IllegalArgumentException("Null filter name");
        }
        if (propertyName.equals(InspectionView.PROP_BARCODE)) {
            return "plate.name";
        } else if (propertyName.equals(InspectionView.PROP_DATE)) {
            return "inspection.completionTime ";
        } else if (propertyName.equals(InspectionView.PROP_IMAGER)) {
            return "instrument.name";
        } else if (propertyName.equals(InspectionView.PROP_INSPECTION_NAME)) {
            return "inspection.name";
        } else if (propertyName.equals(InspectionView.PROP_TEMPERATURE)) {
            return "instrument.temperature";
            // } else if
            // (propertyName.equals(InspectionView.PROP_INSPECTION_NUMBER)) {
            // return "expParam.value";
        }
        throw new BusinessException("Unable to find matching property in " + this.getClass());

    }

    @Override
    String getCountableName() {
        return "plate.name";
    }

    @Override
    Class<Holder> getRootClass() {
        return Holder.class;
    }

    @Override
    public String getViewSql(final BusinessCriteria criteria) {
        final String selectHQL =
            "select plate.name as barcode, "
                + "inspection.completionTime as completionTime, "
                + "instrument.name as instrumentName, "
                + "inspection.name as inspectionName, "
                + "instrument.temperature as temperature,"
                + "plate0.accessid as accessid, "
                + "inspection0.details as details "
                + "from SCHE_SCHEDULEDTASK inspection "
                + "join HOLD_ABSTRACTHOLDER plate on inspection.HOLDERID=plate.LabBookEntryID "
                + "join core_labbookentry plate0 on plate.LabBookEntryID=plate0.dbid "
                + "join core_labbookentry inspection0 on inspection.LabBookEntryID=inspection0.dbid "
                + "left join EXPE_INSTRUMENT instrument on inspection.INSTRUMENTID=instrument.LabBookEntryID ";

        final String subSelect = " inspection.completionTime is not null";
        return buildViewQuerySQL(criteria, selectHQL, subSelect, "plate0", Holder.class);
    }

    @Override
    Collection<InspectionView> runSearch(final org.hibernate.SQLQuery hqlQuery) {
        final Collection<InspectionView> plateViews = new LinkedList<InspectionView>();
        final SQLQuery q = hqlQuery;
        /* q.addScalar("barcode", StandardBasicTypes.STRING);
        q.addScalar("completionTime", StandardBasicTypes.CALENDAR);
        q.addScalar("instrumentName", StandardBasicTypes.STRING);
        q.addScalar("inspectionName", StandardBasicTypes.STRING);
        q.addScalar("temperature", StandardBasicTypes.FLOAT);
        q.addScalar("details", StandardBasicTypes.STRING); */
        final List<?> results = hqlQuery.list();
        // int j = 0;
        for (final Object object : results) {
            final Object result[] = (Object[]) object;
            /*
             * j++; for (int i = 0; i < result.length; i++) { log.debug("view" +
             * j + ":" + result[i]); }
             */

            final InspectionView view = new InspectionView();
            view.setBarcode((String) result[0]);
            view.setDate(getCalDate(result[1]));
            view.setImager((String) result[2]);
            view.setInspectionName((String) result[3]);
            if (null != result[4]) {
                view.setTemperature(AbstractSQLViewDAO.getFloat(result[4]));
            }
            view.setDetails((String) result[6]);
            plateViews.add(view);
        }
        return plateViews;
    }

    public Collection<InspectionView> findLatest(final BusinessCriteria criteria) throws BusinessException {

        criteria.addOrder(BusinessOrder.DESC(InspectionView.PROP_DATE));
        criteria.setMaxResults(1);
        final Collection<InspectionView> results = findViews(criteria);
        return results;
    }
}
