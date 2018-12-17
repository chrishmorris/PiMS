package org.pimslims.report;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.report.PivotTable.Cell;
import org.pimslims.report.PivotTable.Column;

public class ThroughputReportTest extends TestCase {

    private static final String UNIQUE = "tr" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    /**
     * Constructor for PivotTableTest
     * 
     * @param name
     */
    public ThroughputReportTest(String method) {
        super(method);
        this.model = ModelImpl.getModel();
    }

    private String getUnique() {
        return this.getClass().getSimpleName() + ":" + this.getName() + UNIQUE;
    }

    public void testNoCells() {
        PivotTable<LabBookEntry> table = new ThroughputReport(Collections.EMPTY_SET);
        Iterator<PivotTable<LabBookEntry>.Row> rows = table.getRowInterator();
        assertFalse(rows.hasNext());
        Iterator<Column> columns = table.getColumnInterator();
        assertFalse(columns.hasNext());
        Cell[][] cells = table.getCells();
        assertEquals(0, cells.length);
    }

    public void testOneCell() throws ConstraintException {
        ThroughputReport table = new ThroughputReport();

        WritableVersion version = this.model.getTestVersion();
        try {

            table = new ThroughputReport(Collections.singleton(makeExperiment(version, getUnique())));
        } finally {
            version.abort();
        }

        Iterator<PivotTable<LabBookEntry>.Row> rows = table.getRowInterator();
        assertTrue(rows.hasNext());
        assertEquals(getUnique() + "ro", rows.next().getName());
        assertFalse(rows.hasNext());
        Iterator<Column> columns = table.getColumnInterator();
        assertTrue(columns.hasNext());
        assertEquals(getUnique() + "p", columns.next().getName());
        assertFalse(columns.hasNext());
        Cell[][] cells = table.getCells();
        assertEquals(1, cells.length);
        assertEquals(1, cells[0].length);
    }

    public void testBadExperiment() throws ConstraintException {
        ThroughputReport table;

        WritableVersion version = this.model.getTestVersion();
        try {
            String name = getUnique();
            ExperimentType type = new ExperimentType(version, name);
            Experiment experiment = new Experiment(version, name, NOW, NOW, type);
            table = new ThroughputReport(Collections.singleton(experiment));
        } finally {
            version.abort();
        }
        Cell[][] cells = table.getCells();
        assertEquals(0, cells.length);

    }

    private Experiment makeExperiment(WritableVersion version, String name) throws ConstraintException {
        ExperimentType type = new ExperimentType(version, name);
        Experiment experiment = new Experiment(version, name, NOW, NOW, type);
        experiment.setStatus("OK");
        experiment.setProtocol(new Protocol(version, name + "p", type));
        experiment.setProject(new ResearchObjective(version, name + "ro", name + "ro"));
        return experiment;
    }

    public void testTwoConstructs() throws ConstraintException {
        ThroughputReport table = new ThroughputReport(Collections.EMPTY_SET);
        WritableVersion version = this.model.getTestVersion();
        try {
            Experiment experiment1 = makeExperiment(version, getUnique() + "1");
            table.addExperiment(experiment1);
            Experiment experiment2 = makeExperiment(version, getUnique() + "2");
            experiment2.setProtocol(experiment1.getProtocol()); // same column
            table.addExperiment(experiment2);
        } finally {
            version.abort();
        }

        Iterator<PivotTable<LabBookEntry>.Row> rows = table.getRowInterator();
        assertTrue(rows.hasNext());
        assertEquals(getUnique() + "1ro", rows.next().getName());
        assertTrue(rows.hasNext());
        assertEquals(getUnique() + "2ro", rows.next().getName());
        assertFalse(rows.hasNext());
        Iterator<Column> columns = table.getColumnInterator();
        assertTrue(columns.hasNext());
        assertEquals(getUnique() + "1p", columns.next().getName());
        assertFalse(columns.hasNext());
        Cell[][] cells = table.getCells();
        assertEquals(2, cells.length);
        assertEquals(1, cells[0].length);
        assertEquals(NOW, cells[0][0].getValue());
        assertEquals(1, cells[1].length);
        assertEquals(NOW, cells[1][0].getValue());
    }

    public void testTwoProtocols() throws ConstraintException {
        ThroughputReport table = new ThroughputReport();
        WritableVersion version = this.model.getTestVersion();
        try {
            Experiment experiment1 = makeExperiment(version, getUnique() + "1");
            table.addExperiment(experiment1);
            Experiment experiment2 = makeExperiment(version, getUnique() + "2");
            experiment2.setProject(experiment1.getProject()); // same row
            table.addExperiment(experiment2);
        } finally {
            version.abort();
        }
        Iterator<Column> columns = table.getColumnInterator();
        assertTrue(columns.hasNext());
        assertEquals(getUnique() + "1p", columns.next().getName());
        assertTrue(columns.hasNext());
        assertEquals(getUnique() + "2p", columns.next().getName());
        assertFalse(columns.hasNext());

        Iterator<PivotTable<LabBookEntry>.Row> rows = table.getRowInterator();
        assertTrue(rows.hasNext());
        assertEquals(getUnique() + "1ro", rows.next().getName());
        assertFalse(rows.hasNext());
        Cell[][] cells = table.getCells();
        assertEquals(1, cells.length);
        assertEquals(2, cells[0].length);
        assertEquals(NOW, cells[0][0].getValue());
        assertEquals(NOW, cells[0][1].getValue());
    }

    public void TODOtestDate() {
        PivotTable<LabBookEntry> table = new ThroughputReport();
        // TODO table.add(earlierfailedexperiment1);
        // TODO table.add(latersuccessfulexperiment2);
        Cell[][] cells = table.getCells();
        assertEquals(1, cells.length);
        assertEquals(1, cells[0].length);
        //TODO assertEquals later date, cells[0][0]
    }

    public void testParameter() throws ConstraintException {
        final Map<String, Filtered> keywordFilter = new HashMap();
        keywordFilter.put("in", Filtered.IN);
        ThroughputReport table = null;
        WritableVersion version = this.model.getTestVersion();
        try {
            Experiment experiment1 = makeExperiment(version, getUnique() + "1");
            Parameter parm = new Parameter(version, experiment1);
            parm.setName("in");
            parm.setValue(getUnique() + "v");
            Parameter parm2 = new Parameter(version, experiment1);
            parm2.setName("out");
            parm2.setValue(getUnique());
            table = new ThroughputReport(keywordFilter, Collections.singleton(experiment1));
        } finally {
            version.abort();
        }
        Iterator<Column> columns = table.getColumnInterator();
        assertTrue(columns.hasNext());
        assertEquals(getUnique() + "1p", columns.next().getName());
        assertTrue(columns.hasNext());
        assertEquals("in", columns.next().getName());
        assertFalse(columns.hasNext());

        Iterator<PivotTable<LabBookEntry>.Row> rows = table.getRowInterator();
        assertTrue(rows.hasNext());
        assertEquals(getUnique() + "1ro", rows.next().getName());
        assertFalse(rows.hasNext());
        Cell[][] cells = table.getCells();
        assertEquals(1, cells.length);
        assertEquals(2, cells[0].length);
        assertEquals(NOW, cells[0][0].getValue());
        assertEquals(getUnique() + "v", cells[0][1].getValue());
    }

    /* TODO test duplicate parameter names.
     * Note that if different protocols use the same parameter name, 
     * this could be a propagated parameter. In that case, only one column is wanted.
     * The code currently assumes that this is the case.
     * 
     * */

}
