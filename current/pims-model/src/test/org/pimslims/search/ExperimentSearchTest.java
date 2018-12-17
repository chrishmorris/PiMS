package org.pimslims.search;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.search.conditions.Contain;
import org.pimslims.search.conditions.OrCondition;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

public class ExperimentSearchTest extends AbstractTestCase {

    private static final Calendar NOW = Calendar.getInstance();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.wv = this.getWV();

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSearchExperiment() throws ConstraintException {
        try {
            for (int i = 0; i < 2; i++) {
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
            final Paging paging = new Paging(0, 2);
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
            final PIMSCriteria query =
                new PIMSCriteriaImpl(wv, experimentMetaClass.getJavaClass(), paging, null);
            query.setAttributeMap(Contain.convertMap(criteria));

            results = new ArrayList<ModelObject>(query.list());
/*
            for (final ModelObject object : results) {
                System.out.println("result [" + object.get_Name() + "]");
            } */

            Assert.assertEquals(2, results.size());

        } finally {
            this.wv.abort();
        }
    }

    public void testSearchOutputSamplesByProduct() throws ConstraintException {
        try {
            final Paging paging = new Paging(0, 2);
            Collection<ModelObject> results;

            String productName = UNIQUE + "esp";
            ExperimentType type = new ExperimentType(this.wv, UNIQUE);
            Experiment experiment = new Experiment(this.wv, UNIQUE, NOW, NOW, type);
            Sample sample = new Sample(this.wv, UNIQUE);
            sample.addSampleCategory(new SampleCategory(this.wv, productName));
            new OutputSample(this.wv, experiment).setSample(sample);

            //final Map experimentCriteria = new java.util.HashMap();
            Map osCriteria = new java.util.HashMap();
            Map sampleCriteria = new HashMap();
            Map categoryCriteria = new HashMap();
            categoryCriteria.put(SampleCategory.PROP_NAME, productName);
            sampleCriteria.put(AbstractSample.PROP_SAMPLECATEGORIES, categoryCriteria);
            osCriteria.put(OutputSample.PROP_SAMPLE, sampleCriteria);
            //experimentCriteria.put(Experiment.PROP_OUTPUTSAMPLES, osCriteria = new java.util.HashMap());

            final PIMSCriteria query = new PIMSCriteriaImpl(wv, OutputSample.class, paging, null);
            query.setAttributeMap(osCriteria);

            results = new ArrayList<ModelObject>(query.list());
            Assert.assertEquals(1, results.size());

        } finally {
            this.wv.abort();
        }
    }

    public void testSearchExperimentByProducts() throws ConstraintException {
        try {
            String productName = UNIQUE + "esp";
            final Paging paging = new Paging(0, 2);
            Collection<ModelObject> results;

            final MetaClass experimentMetaClass = this.wv.getModel().getMetaClass(Experiment.class.getName());
            Map categoryCriteria =
                Collections.singletonMap(SampleCategory.PROP_NAME, new OrCondition(new String[] {
                    productName, "nonesuch" + UNIQUE }));
            Map sampleCriteria =
                Collections.singletonMap(AbstractSample.PROP_SAMPLECATEGORIES, categoryCriteria);
            Map osCriteria = Collections.singletonMap(OutputSample.PROP_SAMPLE, sampleCriteria);
            final Map criteria = new java.util.HashMap();
            criteria.put(Experiment.PROP_OUTPUTSAMPLES, osCriteria);

            final PIMSCriteria query =
                new PIMSCriteriaImpl(wv, experimentMetaClass.getJavaClass(), paging, null);
            query.setAttributeMap(criteria);

            results = new ArrayList<ModelObject>(query.list());
            Assert.assertEquals(0, results.size());

        } finally {
            this.wv.abort();
        }
    }
}
