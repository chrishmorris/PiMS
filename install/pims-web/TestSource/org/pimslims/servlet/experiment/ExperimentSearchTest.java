package org.pimslims.servlet.experiment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.search.Conditions;
import org.pimslims.search.Paging;
import org.pimslims.search.Searcher;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

public class ExperimentSearchTest extends AbstractTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.wv = this.getWV();

        for (int i = 0; i < 10; i++) {
            final Experiment experiment = POJOFactory.createExperiment(this.wv);
            experiment.setName("Experiment" + new Integer(i).toString());
            experiment.setStatus("To_be_run");

            final Sample sample = POJOFactory.createSample(this.wv);
            final OutputSample outputSample = POJOFactory.createOutputSample(this.wv, experiment);
            outputSample.setSample(sample);

            final Sample sample2 = POJOFactory.createSample(this.wv);
            final OutputSample outputSample2 = POJOFactory.createOutputSample(this.wv, experiment);
            outputSample2.setSample(sample2);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSearchExperiment() {
        try {
            final Paging paging = new Paging(0, 10);
            final Searcher s = new Searcher(this.wv);
            Collection<ModelObject> results;

            final MetaClass experimentMetaClass = this.wv.getModel().getMetaClass(Experiment.class.getName());
            final Map criteria = new java.util.HashMap();

            final Map<String, Object> sampleCriteria = new HashMap<String, Object>();
            sampleCriteria.put(Sample.PROP_HOLDER, Conditions.isNull());
            final Map<String, Object> osCriteria = new HashMap<String, Object>();
            osCriteria.put(OutputSample.PROP_SAMPLE, sampleCriteria);
            final Map<String, Object> expGroupAndSampleCriteria = new HashMap<String, Object>();
            expGroupAndSampleCriteria.put(Experiment.PROP_OUTPUTSAMPLES, osCriteria);
            expGroupAndSampleCriteria.put(Experiment.PROP_EXPERIMENTGROUP, Conditions.isNull());
            criteria.put("OR", expGroupAndSampleCriteria);

            results = s.search(criteria, experimentMetaClass, paging);

            for (final ModelObject object : results) {
                System.out.println("result [" + object.get_Name() + "]");
            }

            Assert.assertEquals(10, results.size());

        } finally {
            this.wv.abort();
        }
    }
}
