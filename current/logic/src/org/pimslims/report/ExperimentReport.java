package org.pimslims.report;

import java.util.Calendar;
import java.util.Map;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.kpi.BarChartBean;
import org.pimslims.kpi.Kpi;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.search.Conditions;

public class ExperimentReport extends Report<Experiment> {

    public ExperimentReport(ReadableVersion version, Map<String, Object> criteria, String like) {
        super(version, Experiment.class, criteria, like);
    }

    /**
     * Constructor for ExperimentReport
     * 
     * @param version
     * @param javaClass
     * @param searchCriteria
     * @param like
     * @param start
     * @param end
     */
    public ExperimentReport(ReadableVersion version, Map<String, Object> searchCriteria, String like,
        Calendar start, Calendar end) {
        super(version, Experiment.class, searchCriteria, like, start, end);
    }

    public BarChartBean getAttrition(String chartUrl) {
        return Kpi.getAttrition(this, chartUrl);
    }

    /**
     * ExperimentReport.addDateCriteria
     * 
     * @see org.pimslims.report.Report#addDateCriteria(java.util.Calendar, java.util.Calendar, java.util.Map)
     */
    @Override
    void addDateCriteria(Calendar start, Calendar end, Map<String, Object> criteria) {
        if (null != start) {
            criteria.put(Experiment.PROP_STARTDATE, Conditions.greaterOrEquals(start));
        }
        if (null != end) {
            criteria.put(Experiment.PROP_ENDDATE, Conditions.lessOrEquals(end));
        }
    }

    //TODO add product criteria

}
