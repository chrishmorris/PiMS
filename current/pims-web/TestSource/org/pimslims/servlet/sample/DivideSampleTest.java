/**
 * V2_1-pims-web org.pimslims.servlet.sample DivideSample.java
 * 
 * @author Marc Savitsky
 * @date 9 Apr 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky
 * 
 */
package org.pimslims.servlet.sample;

import java.util.Collection;

import junit.framework.Assert;

import org.pimslims.access.Access;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * DivideSample
 * 
 */
public class DivideSampleTest extends AbstractTestCase {

    Sample sample;

    Sample sampleNoOutputSample;

    /**
     * @param name
     */
    public DivideSampleTest(final String name) {
        super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.wv = this.getWV();

        final Experiment experiment = POJOFactory.createExperiment(this.wv);
        final OutputSample outputSample = POJOFactory.createOutputSample(this.wv, experiment);
        this.sample = POJOFactory.createSample(this.wv);

        this.sample.setOutputSample(outputSample);

        this.sample.setAmountUnit("L");
        this.sample.setAmountDisplayUnit("uL");
        this.sample.setCurrentAmount(0.0009f);

        this.sampleNoOutputSample = POJOFactory.createSample(this.wv);
        this.sampleNoOutputSample.setAmountUnit("L");
        this.sampleNoOutputSample.setAmountDisplayUnit("uL");
        this.sampleNoOutputSample.setCurrentAmount(0.0009f);

    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        if (this.wv.isCompleted()) {
            // can't tidy up
            return;
        }
        if (null != this.wv) {
            this.wv.abort(); // not testing persistence
        }
    }

    public void testcreateOutputSampleForExperiment() {
        try {
            DivideSample.createOutputSampleForExperiment(this.wv, this.sample.getOutputSample(), this.sample);
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } finally {
            this.wv.abort();
        }
    }

    public void testmakeName() {
        try {
            //final long t = System.currentTimeMillis();
            Assert.assertEquals("testmakeName failed", this.sample.getName() + " aliquot 1",
                DivideSample.makeName(this.wv, this.sample.getName()));
            //System.out.println("DivideSample.testmakeName [" + (System.currentTimeMillis() - t) + "]");
        } finally {
            this.wv.abort();
        }
    }

    public void testDivide() {
        try {
            final int i = 3;
            final Collection<Sample> samples =
                DivideSample.divide(this.wv, this.sample, i, Access.ADMINISTRATOR);
            Assert.assertEquals(i, samples.size());
            float f = 0;
            for (final Sample mySample : samples) {
                f = +mySample.getCurrentAmount();
            }
            Assert.assertEquals(f, this.sample.getCurrentAmount().floatValue());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } finally {
            this.wv.abort();
        }
    }

    public void testDivideNoOutputSample() {
        try {
            final int i = 3;
            final Collection<Sample> samples =
                DivideSample.divide(this.wv, this.sampleNoOutputSample, i, Access.ADMINISTRATOR);
            Assert.assertEquals(i, samples.size());
            float f = 0;
            for (final Sample mySample : samples) {
                f = +mySample.getCurrentAmount();
            }
            Assert.assertEquals(f, this.sampleNoOutputSample.getCurrentAmount().floatValue());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } finally {
            this.wv.abort();
        }
    }

}
