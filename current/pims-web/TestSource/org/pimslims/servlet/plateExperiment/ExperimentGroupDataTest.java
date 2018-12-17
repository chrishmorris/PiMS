package org.pimslims.servlet.plateExperiment;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;

import junit.framework.Assert;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.AbstractCsvData;
import org.pimslims.lab.PlateCsv;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.experiment.ExperimentGroupWriter;
import org.pimslims.servlet.plateExperiment.PlateExperimentCsv.PlateExperimentData;
import org.pimslims.test.AbstractTestCase;

public class ExperimentGroupDataTest extends AbstractTestCase {

    private static final String UNIQUE = "egd" + System.currentTimeMillis();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ExperimentGroupDataTest.class);
    }

    public final void testEmptyExpGroupDataHeaders() throws AccessException, IOException {
        this.wv = this.getWV();
        try {
            final ExperimentGroup group = this.create(ExperimentGroup.class);
            final PlateCsv data = new PlateExperimentCsv.PlateExperimentData(group);
            final String[] attributeNames = data.getHeaders();
            Assert.assertTrue(Arrays.asList(attributeNames).contains("Construct")); //TODO change to "Project"
            Assert.assertFalse(data.hasNext());

            final String csv = this.getCsvAsString(group);
            final ExperimentGroupWriter egw = new ExperimentGroupWriter(this.wv, group);
            final Reader reader = new StringReader(csv);
            egw.setValuesFromSpreadSheetForCreate(reader);

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            this.wv.abort(); // not testing persistence here
        }
    }

    private String getCsvAsString(final ExperimentGroup group) throws AccessException, IOException {
        String csv;
        final AbstractCsvData csvData = new PlateExperimentData(group);
        csv = PlateCsv.toString(csvData);
        return csv;
    }

    public final void testPlateDataHeaders() throws AccessException {
        this.wv = this.getWV();
        final String pdName = ExperimentGroupDataTest.UNIQUE;
        try {
            final ExperimentGroup group = this.getTestingExpGroup(pdName);

            final PlateCsv data = new PlateExperimentCsv.PlateExperimentData(group);
            final String[] attributeNames = data.getHeaders();
            Assert.assertTrue(Arrays.asList(attributeNames).contains("Well"));
            Assert.assertTrue(Arrays.asList(attributeNames).contains("Construct"));
            Assert.assertTrue(Arrays.asList(attributeNames).contains(pdName));
            Assert.assertTrue(data.hasNext());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            this.wv.abort(); // not testing persistence here
        }
    }

    private ExperimentGroup getTestingExpGroup(final String pdName) throws AccessException,
        ConstraintException {
        final ExperimentGroup group = this.create(ExperimentGroup.class);
        final Protocol protocol = this.create(Protocol.class);
        final ParameterDefinition pd = this.create(ParameterDefinition.class);
        pd.setName(pdName);
        protocol.setParameterDefinitions(Collections.singletonList(pd));
        final SampleCategory category = new SampleCategory(this.wv, ExperimentGroupDataTest.UNIQUE);
        final RefInputSample ris = new RefInputSample(this.wv, category, protocol);
        ris.setName("is1");
        ris.setDisplayUnit("mg");
        final Experiment exp = this.create(Experiment.class, Experiment.PROP_PROTOCOL, protocol);
        final OutputSample os = this.create(OutputSample.class, OutputSample.PROP_EXPERIMENT, exp);
        final Holder holder = this.create(Holder.class);
        final AbstractHolderType type =
            this.create(HolderType.class, AbstractHolderType.PROP_NAME, "test plate "
                + ExperimentGroupDataTest.UNIQUE);
        holder.setHolderType(type);
        final Sample sample = this.create(Sample.class);
        os.setSample(sample);
        sample.setColPosition(1);
        sample.setRowPosition(1);
        sample.setHolder(holder);

        exp.setExperimentGroup(group);
        return group;
    }

    public final void testNext() throws AccessException {
        this.wv = this.getWV();
        final String pdName = ExperimentGroupDataTest.UNIQUE;
        final String pdValue = ExperimentGroupDataTest.UNIQUE + "v";
        try {
            final ExperimentGroup group = this.getTestingExpGroup(pdName);
            // set parameter value
            final Parameter parameter =
                this.create(Parameter.class, Parameter.PROP_EXPERIMENT, group.getExperiments().iterator()
                    .next());
            parameter.setName(pdName);
            parameter.setValue(pdValue);

            final PlateCsv data = new PlateExperimentCsv.PlateExperimentData(group);
            Assert.assertTrue(data.hasNext());
            final String[] headers = data.getHeaders();
            final String[] values = data.next();
            Assert.assertEquals(Arrays.toString(headers) + Arrays.toString(values), headers.length,
                values.length);
            Assert.assertTrue(Arrays.asList(values).contains(pdValue));
            Assert.assertTrue(Arrays.asList(values).contains("A1"));
            Assert.assertFalse(data.hasNext());
        } catch (final ConstraintException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            this.wv.abort(); // not testing persistence here
        }

    }
}
