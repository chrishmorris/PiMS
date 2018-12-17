package org.pimslims.crystallization.implementation;

import java.util.Calendar;

import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.AbstractXtalTest;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;

public class TrialServiceUtilsTest extends AbstractXtalTest {

    private static final Calendar NOW = Calendar.getInstance();

    /**
     * Constructor for TrialServiceUtilsTest
     * 
     * @param methodName
     * @param dataStorage
     */
    public TrialServiceUtilsTest(String methodName) {
        super(methodName, DataStorageFactory.getDataStorageFactory().getDataStorage());
    }

    public void testCreateInputSampleWritableVersionExperimentGroupStringSampleFloat()
        throws ConstraintException, BusinessException, AccessException {
        this.dataStorage.openResources("administrator");
        final WritableVersion version = ((DataStorageImpl) dataStorage).getWritableVersion();
        try {
            ExperimentGroup group = new ExperimentGroup(version, UNIQUE, "test");
            ExperimentType type = new ExperimentType(version, UNIQUE);
            Experiment experiment = new Experiment(version, UNIQUE, NOW, NOW, type);
            experiment.setExperimentGroup(group);
            Sample sample = new Sample(version, UNIQUE);
            TrialServiceUtils.createInputSample(version, group, UNIQUE, sample, 100f / 1000000000f);
            assertEquals(1, experiment.getInputSamples().size());
            TrialServiceUtils.createInputSample(version, group, UNIQUE, sample, 100f / 1000000000f);
            assertEquals(1, experiment.getInputSamples().size());
            InputSample is = experiment.getInputSamples().iterator().next();
            assertEquals(sample, is.getSample());
        } finally {
            version.abort();
        }
    }

}
