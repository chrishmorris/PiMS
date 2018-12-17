package org.pimslims.lab;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.reference.WorkflowItem;

public class TestWorkflowItem extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(TestWorkflowItem.class);
    }

    private WritableVersion version;

    private final AbstractModel model;

    public TestWorkflowItem(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    @Override
    protected void setUp() throws Exception {
        this.version = this.model.getTestVersion();
    }

    @Override
    protected void tearDown() throws Exception {
        this.version.abort();
    }

    public void test() {
        try {
            final TargetStatus status = new TargetStatus(this.version, "test" + System.currentTimeMillis());
            final ExperimentType type = new ExperimentType(this.version, "test" + System.currentTimeMillis());
            final WorkflowItem item = new WorkflowItem(this.version, type);
            item.setStatus(status);
            Assert.assertEquals(type, item.getExperimentType());
            Assert.assertEquals(status, item.getStatus());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        }
    }

}
