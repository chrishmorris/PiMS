package org.pimslims.presentation;

import java.util.Collection;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.SampleComponent;

public class SampleComponentBeanTest extends TestCase {

    private static final String UNIQUE = "test" + System.currentTimeMillis();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(SampleComponentBeanTest.class);
    }

    private final AbstractModel model;

    public SampleComponentBeanTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    public void testNoComponents() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final RefSample refSample = new RefSample(version, "recipe" + SampleComponentBeanTest.UNIQUE);
            final Collection<ModelObjectBean> beans =
                ModelObjectBean.getModelObjectBeans(refSample.getSampleComponents());
            assert null != beans;
            Assert.assertEquals(0, beans.size());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void test() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final RefSample refSample = new RefSample(version, "recipe" + SampleComponentBeanTest.UNIQUE);
            final Substance chemical =
                new Molecule(version, "other", "chemical" + SampleComponentBeanTest.UNIQUE);
            final SampleComponent sc = new SampleComponent(version, chemical, refSample);
            sc.setDetails("details" + SampleComponentBeanTest.UNIQUE);
            final Collection<ModelObjectBean> beans =
                ModelObjectBean.getModelObjectBeans(refSample.getSampleComponents());
            assert null != beans;
            Assert.assertEquals(1, beans.size());
            final ModelObjectBean bean = beans.iterator().next();
            Assert.assertEquals(sc.getDetails(), bean.getValues().get(LabBookEntry.PROP_DETAILS));
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

}
