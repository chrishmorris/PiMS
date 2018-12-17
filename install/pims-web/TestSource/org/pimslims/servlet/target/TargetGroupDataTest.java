package org.pimslims.servlet.target;

import java.util.Arrays;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;

public class TargetGroupDataTest extends TestCase {

    private static final String UNIQUE = "tgd" + System.currentTimeMillis();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(TargetGroupDataTest.class);
    }

    private final AbstractModel model;

    public TargetGroupDataTest(final String arg0) {
        super(arg0);
        this.model = ModelImpl.getModel();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
     * Test method for
     * 'org.pimslims.servlet.target.TargetGroupCsv.TargetGroupData.TargetGroupData(TargetGroup,
     * AbstractModel)'
     */
    public final void testTargetGroupData() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final TargetGroup group = new TargetGroup(version, TargetGroupDataTest.UNIQUE);
            final TargetGroupCsv.TargetGroupData data = new TargetGroupCsv.TargetGroupData(group, this.model);
            final String[] attributeNames = data.getHeaders();
            Assert.assertTrue(Arrays.asList(attributeNames).contains(Target.PROP_NAME));
            Assert.assertFalse(data.hasNext());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    /*
     * Test method for 'org.pimslims.servlet.target.TargetGroupCsv.TargetGroupData.next()'
     */
    public final void testNext() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final TargetGroup group = new TargetGroup(version, "test" + System.currentTimeMillis());
            final Molecule protein =
                new Molecule(version, "protein", "testProtein" + System.currentTimeMillis());
            final Target target = new Target(version, "testName" + System.currentTimeMillis(), protein);
            group.addTarget(target);

            final TargetGroupCsv.TargetGroupData data = new TargetGroupCsv.TargetGroupData(group, this.model);

            Assert.assertTrue(data.hasNext());
            final String[] values = data.next();
            Assert.assertTrue(Arrays.asList(values).contains(target.getName()));
            Assert.assertFalse(data.hasNext());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence here
        }
    }
}
