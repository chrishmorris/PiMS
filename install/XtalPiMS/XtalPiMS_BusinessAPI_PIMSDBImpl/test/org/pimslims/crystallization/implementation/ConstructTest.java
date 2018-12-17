package org.pimslims.crystallization.implementation;

import java.util.Calendar;
import java.util.Collection;

import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.service.ConstructService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.view.ConstructView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.ConstructServiceTest;
import org.pimslims.crystallization.dao.SampleDAO;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.target.ResearchObjective;

public class ConstructTest extends ConstructServiceTest {

    /**
     * unique string for avoiding name clashes in tests
     */
    static final String UNIQUE = "test" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    public ConstructTest(final String methodName) {
        super(methodName, DataStorageFactory.getDataStorageFactory().getDataStorage());
    }

    public void testFindByName() throws BusinessException, ConstraintException {
        this.dataStorage.openResources("administrator");
        try {
            final WritableVersion version =
                (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();
            final ResearchObjective eb = new ResearchObjective(version, UNIQUE, "test");

            final ConstructService service = this.dataStorage.getConstructService();
            Construct result = service.findByName("nonesuch");
            assertNull(result);
            result = service.findByName(eb.getCommonName());
            assertNotNull(result);
            assertEquals(eb.getCommonName(), result.getName());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void testGetSamples() throws BusinessException, ConstraintException {
        this.dataStorage.openResources("administrator");
        try {
            final WritableVersion version =
                (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();
            final ResearchObjective eb = new ResearchObjective(version, UNIQUE, "test");
            final org.pimslims.model.sample.Sample pimsSample =
                new org.pimslims.model.sample.Sample(version, "sample" + UNIQUE);
            final ExperimentType type = new ExperimentType(version, "ETname" + UNIQUE);
            final Experiment experiment = new Experiment(version, UNIQUE, NOW, NOW, type);
            final OutputSample os = new OutputSample(version, experiment);
            os.setSample(pimsSample);
            experiment.setResearchObjective(eb);
            final SampleCategory purified =
                version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, SampleDAO.PURIFIED_PROTEIN);
            assertNotNull(purified);
            pimsSample.addSampleCategory(purified);

            final ConstructService constructService = this.dataStorage.getConstructService();
            final Construct construct = constructService.findByName(eb.getCommonName());
            //List<Sample> samples = constructService.getSamples(construct  );

            //assertEquals(1, samples.size());
            //Sample result = samples.iterator().next();
            //assertNotNull(result.getConstruct());
            //assertEquals(eb.getCommonName(), result.getConstruct().getName());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void testConstructView() throws BusinessException, ConstraintException {
        this.dataStorage.openResources("administrator");
        try {
            final WritableVersion version =
                (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();
            final ResearchObjective eb = new ResearchObjective(version, UNIQUE, "test");

            final ConstructService service = this.dataStorage.getConstructService();
            final BusinessCriteria criteria = new BusinessCriteria(service);
            criteria.add(BusinessExpression.Equals(ConstructView.PROP_NAME, UNIQUE, true));
            final Collection<ConstructView> constructViews = service.findViews(criteria);
            assertEquals(1, constructViews.size());
            final ConstructView constructView = constructViews.iterator().next();
            assertEquals(UNIQUE, constructView.getConstructName());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }
}
