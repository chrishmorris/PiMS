package org.pimslims.report;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.protocol.Protocol;

public class ThroughputReport extends PivotTable<LabBookEntry> {

    /**
     * EarliestCell Reports the date of the successful experiment, or null if there were no experiments, or a
     * failure indication.
     */
    public static class EarliestCell implements Cell {

        private Calendar value = null;

        @Override
        public void add(Object object) {
            Experiment experiment = (Experiment) object;
            if ("OK".equals(experiment.getStatus())) {
                Calendar finished = experiment.getEndDate();
                if (null == this.value || this.value.after(finished)) {
                    this.value = finished;
                }
            }
        }

        @Override
        public Object getValue() {
            return this.value;
        }

    }

    /**
     * ConsensusCell Reports the common value, if all entries are the same, otherwise an indication that they
     * vary If there are successful experiments, limit the report to them. Effective for an inherited
     * parameter, e.g. sequence. Note that we may also need a running total, e.g. passage number.
     */
    public static class ConsensusCell implements Cell {

        String value = null;

        String okValue = null;

        @Override
        public void add(Object object) {
            Parameter parm = (Parameter) object;
            if (null == this.value) {
                this.value = parm.getValue();
            } else if (this.value == parm.getValue()) {
                // that's fine				
            } else {
                this.value = "[various]";
            }
            ;
            if ("OK".equals(parm.getExperiment().getStatus())) {
                if (null == this.okValue) {
                    this.okValue = parm.getValue();
                } else if (this.okValue == parm.getValue()) {
                    // that's fine				
                } else {
                    this.okValue = "[various]";
                }
                ;
            }
        }

        @Override
        public Object getValue() {
            if (null != okValue) {
                return okValue;
            }
            return this.value;
        }

    }

    private final Map<String, Filtered> parameterFilter;

    /**
     * Constructor for ThroughputReport
     */
    public ThroughputReport() {
        this(Collections.EMPTY_SET);
    }

    /**
     * Constructor for ThroughputReport
     */
    public ThroughputReport(Collection<Experiment> experiments) {
        this(Collections.EMPTY_MAP, experiments);
    }

    public ThroughputReport(Map<String, Filtered> keywordFilter, Collection<Experiment> experiments) {
        super();
        this.parameterFilter = keywordFilter;
        for (Iterator iterator = experiments.iterator(); iterator.hasNext();) {
            Experiment experiment = (Experiment) iterator.next();
            this.addExperiment(experiment);
        }
    }

    //TODO pull up to PivotTable
    private final Map<String, Column> columnMap = new HashMap();

    private final Set<String> protocols = new HashSet();

    @Override
    protected org.pimslims.report.PivotTable.Column getColumn(LabBookEntry page) {
        String key = page.getName();
        if (page instanceof Experiment) {
            Experiment experiment = (Experiment) page;
            Protocol protocol = experiment.getProtocol();
            if (null == protocol) {
                return null;
            }
            key = protocol.getName();
            this.protocols.add(key);
        }
        if (!this.columnMap.containsKey(key)) {
            this.columnMap.put(key, new Column(key));
        }
        return this.columnMap.get(key);
    }

    private final Map<Project, Row> rowMap = new HashMap();

    @Override
    protected org.pimslims.report.PivotTable.Row getRow(LabBookEntry page) {
        Experiment experiment;
        if (page instanceof Parameter) {
            experiment = ((Parameter) page).getExperiment();
        } else {
            experiment = (Experiment) page;
        }
        Project project = experiment.getProject();
        if (null == project) {
            return null;
        }
        if (!this.rowMap.containsKey(project)) {
            this.rowMap.put(project, new Row(project.getName()));
        }
        return this.rowMap.get(project);
    }

    @Override
    protected Class<? extends Cell> getCellClass(String column) {
        if (protocols.contains(column)) {
            return EarliestCell.class;
        }
        return ConsensusCell.class;
    }

    public void addExperiment(Experiment experiment) {
        super.add(experiment);
        Set<Parameter> parameters = experiment.getParameters();
        for (Iterator iterator = parameters.iterator(); iterator.hasNext();) {
            Parameter parameter = (Parameter) iterator.next();
            if (Filtered.IN == this.parameterFilter.get(parameter.getName())) {
                this.add(parameter);
            }
        }
    }

}
