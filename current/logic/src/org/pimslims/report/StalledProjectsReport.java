package org.pimslims.report;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.ReadableVersionImpl;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.persistence.JpqlQuery;
import org.pimslims.search.PIMSCriteriaImpl;
import org.pimslims.search.Serial;

public class StalledProjectsReport extends Report<ResearchObjective> {

    private final Map<String, Object> experimentCriteria;

    public StalledProjectsReport(ReadableVersion version, Calendar start, Calendar end,
        Map<String, Object> projectCriteria, Map<String, Object> experimentCriteria) {
        super(version, ResearchObjective.class, projectCriteria, null, start, end);
        this.experimentCriteria = experimentCriteria;
    }

    @Override
    public int count() {
        return getExperimentCriteria().count();
    }

    /**
     * StalledProjectsReport.getExperimentQuery
     * 
     * @return
     * 
     *         private JpqlQuery getExperimentQuery() { Collection<LabNotebook> labNotebooks =
     *         version.isAdmin() ? null : version.getReadableLabNotebooks(); PIMSCriteriaImpl expSearch =
     *         getExperimentCriteria(); JpqlQuery experimentQuery = JpqlQuery.createFindAllHQL(expSearch,
     *         version, labNotebooks); experimentQuery.doBindingParameters(expSearch.attributeMap, new
     *         Serial()); return experimentQuery; }
     */

    /**
     * StalledProjectsReport.getExperimentCriteria
     * 
     * @param labNotebooks
     * @return
     */
    private PIMSCriteriaImpl getExperimentCriteria() {
        Collection<LabNotebook> labNotebooks =
            this.version.isAdmin() ? null : this.version.getReadableLabNotebooks();
        PIMSCriteriaImpl expSearch =
            (PIMSCriteriaImpl) ((ReadableVersionImpl) this.version).CreateQuery(Experiment.class, null,
                labNotebooks);
        Map<String, Object> map = new HashMap(this.experimentCriteria);
        map.put(Experiment.PROP_PROJECT, this.criteria);
        map.put(Experiment.PROP_STATUS, "Failed");
        expSearch.setAttributeMap(map);
        return expSearch;
    }

    /**
     * StalledProjectsReport.getStalled
     * 
     * @return failed experiments, where there has been no more action on project
     * 
     *         TODO check project not cancelled TODO could simple find all latest experiments, then filter in
     *         Java for failed ones ;
     */
    public List<Calendar> getStalled() {
        /*Collection<LabNotebook> labNotebooks =
            this.version.isAdmin() ? null : this.version.getReadableLabNotebooks();
        PIMSCriteriaImpl expSearch = this.getExperimentCriteria();
        JpqlQuery query =
            JpqlQuery
                .createFindAllHQL(
                    expSearch,
                    this.version,
                    labNotebooks,
                    "",
                    " and not exists (select resumed from Experiment resumed where researchObjective=resumed.researchObjective and  A.endDate<resumed.startDate )",
                    " distinct A from " + Experiment.class.getName());
        query.doBindingParameters(expSearch.getAttributeMap(), new Serial());
        return query.list(); */

        Collection<LabNotebook> labNotebooks =
            this.version.isAdmin() ? null : this.version.getReadableLabNotebooks();
        PIMSCriteriaImpl experimentCriteria2 = this.getExperimentCriteria();
        JpqlQuery query =
            JpqlQuery.createFindAllHQL(experimentCriteria2, version, labNotebooks,
                " join A.researchObjective.experiments other ", "  ", " A.endDate "
                    + "from org.pimslims.model.experiment.Experiment A ",
                " group by A having A.endDate=max(other.endDate) ");
        query.doBindingParameters(experimentCriteria2.getAttributeMap(), new Serial());
        return query.list();
    }

    /**
     * StalledProjectsReport.getResumed
     * 
     * @return for each failed experiments where there has been another experiment for project the end date,
     *         and the start date of the next one TODO filter by project
     */
    public List<Object[]> getResumed() {
        Collection<LabNotebook> labNotebooks =
            this.version.isAdmin() ? null : this.version.getReadableLabNotebooks();
        PIMSCriteriaImpl experimentCriteria2 = this.getExperimentCriteria();
        JpqlQuery query =
            JpqlQuery.createFindAllHQL(experimentCriteria2, version, labNotebooks,
                " join A.researchObjective.experiments resumed ", " and A.endDate<resumed.startDate ",
                "   A.endDate , min(resumed.startDate)  \r\n"
                    + "from org.pimslims.model.experiment.Experiment A   \r\n", " group by A");
        query.doBindingParameters(experimentCriteria2.getAttributeMap(), new Serial());
        return query.list();
    }

}
