package org.pimslims.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.ReadableVersionImpl;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.search.Condition;
import org.pimslims.search.Conditions;
import org.pimslims.search.PIMSCriteriaImpl;
import org.pimslims.search.Paging;

public class Report<T extends ModelObject> {

    private static final Calendar PAST = Calendar.getInstance();

    private static final Calendar FUTURE = Calendar.getInstance();
    static {
        PAST.setTimeInMillis(0);
        FUTURE.set(2100, 01, 01);
    }

    protected final PIMSCriteriaImpl search;

    protected final Map<String, Object> criteria;

    protected final ReadableVersion version;

    public Report(ReadableVersion version, Class<T> javaClass, Map<String, Object> criteria, String like) {
        this(version, javaClass, criteria, like, null, null);
    }

    public Report(ReadableVersion version, Class<T> javaClass, Map<String, Object> searchCriteria,
        String like, Calendar start, Calendar end) {
        this.version = version;
        Map<String, Object> criteria = new HashMap(searchCriteria);
        addDateCriteria(start, end, criteria);

        // could criteria = Contain.convertMap(criteria);
        MetaClass metaClass = version.getMetaClass(javaClass);
        this.search =
            (PIMSCriteriaImpl) ((ReadableVersionImpl) version).CreateQuery(javaClass, null, version.isAdmin()
                ? null : version.getReadableLabNotebooks());

        if (null == like || "".equals(like)) {
            this.criteria = criteria;
        } else {
            final Condition condition = Conditions.contains(like);
            this.criteria = PIMSCriteriaImpl.mergeLikeCondition(metaClass, condition, criteria);
        }
        this.search.setAttributeMap(this.criteria);

    }

    void addDateCriteria(Calendar start, Calendar end, Map<String, Object> criteria) {
        if (null == start && null == end) {
            return;
        }
        Collection<Condition> conditions = new ArrayList(2);
        if (null != start) {
            conditions.add(Conditions.greaterOrEquals(start));
        }
        if (null != end) {
            conditions.add(Conditions.lessOrEquals(end));
        }
        criteria.put(LabBookEntry.PROP_CREATIONDATE, Conditions.and(conditions));
    }

    public List<T> getResults(Paging paging) {
        this.search.setPaging(paging);
        return this.search.list();
    }

    public int count() {
        return this.search.count();
    }

    public List wrap(String queryPrefix, String queryPostfix) {
        return this.search.wrap(queryPrefix, queryPostfix);
    }

    /**
     * Report.setLabNotebooks Filter by LabNotebook
     * 
     * @param books the books of interest
     */
    public void setLabNotebooks(Collection<LabNotebook> books) {
        this.search.setLabNotebooks(books);
    };

}
