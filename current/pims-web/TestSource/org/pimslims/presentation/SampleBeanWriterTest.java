package org.pimslims.presentation;

import java.util.Collection;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.presentation.sample.SampleBeanWriter;

public class SampleBeanWriterTest extends TestCase {

    private static final String SAMPLE_NAME = "testSample" + System.currentTimeMillis();

    private static final String UNIQUE = "sbw" + System.currentTimeMillis();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(SampleBeanWriterTest.class);
    }

    private AbstractModel model;

    public SampleBeanWriterTest(final String arg0) {
        super(arg0);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.model = ModelImpl.getModel();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCreate() throws AccessException, ConstraintException {

        final WritableVersion version =
            this.model.getTestVersion();

        try {
            final RefSample rs = new RefSample(version, "recipe" + SampleBeanWriterTest.UNIQUE);

            final ModelObject modelObject =
                SampleBeanWriter.createSample(version, SampleBeanWriterTest.SAMPLE_NAME, rs, rs.getAccess());
            final Sample s = (Sample) modelObject;

            Assert.assertTrue(s.getName().equals(SampleBeanWriterTest.SAMPLE_NAME));
            // System.out.println(s.getIonicStrength()+":"+rs.getIonicStrength());
            if (null != rs.getIonicStrength()) {
                Assert.assertTrue(s.getIonicStrength().equals(rs.getIonicStrength()));
            }
            Assert.assertEquals(rs.getSampleComponents().size(), s.getSampleComponents().size());

            System.out.println("SampleComponents in refSample [" + rs.getSampleComponents().size() + "]");
            final Collection<SampleComponent> sampleComponents = rs.getSampleComponents();
            for (final SampleComponent component : sampleComponents) {
                System.out.println("SampleComponent [" + component.get_Name() + "]");
            }

            System.out.println("SampleComponents in Sample [" + s.getSampleComponents().size() + "]");
            final Collection<SampleComponent> components = s.getSampleComponents();
            for (final SampleComponent component : components) {
                System.out.println("SampleComponent [" + component.get_Name() + "]");
            }

        } finally {
            version.abort();
        }
    }
}
