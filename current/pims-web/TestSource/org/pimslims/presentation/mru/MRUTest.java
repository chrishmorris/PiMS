package org.pimslims.presentation.mru;

import java.util.Date;

import junit.framework.Assert;

import org.pimslims.access.Access;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

public class MRUTest extends AbstractTestCase {

    public void testMRUConstructorWithModelObject() {
        this.wv = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);

        try {
            final Target target = POJOFactory.createTarget(this.wv);
            final String commonName = "target" + new Date();
            target.setName(commonName);
            final MRU mru = new MRU(Access.ADMINISTRATOR, target);
            Assert.assertEquals(Target.class.getName(), mru.getClassName());
            Assert.assertEquals(commonName, mru.getName());
            Assert.assertEquals(target.get_Hook(), mru.getHook());
            Assert.assertEquals(Access.ADMINISTRATOR, mru.getUserName());
            Assert.assertEquals("Target", mru.getClassDisplayName());
            // wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public void testMRUConstructorWithHook() throws ConstraintException {
        this.wv = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);

        try {
            final Target target = POJOFactory.createTarget(this.wv);
            final String commonName = "target" + new Date();
            target.setName(commonName);
            final ModelObjectShortBean mru = MRUController.addObject(Access.ADMINISTRATOR, target.get_Hook());
            Assert.assertNull(mru);
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }

        this.wv = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);
        String hook = null;
        final String commonName = "target" + new Date();

        try {
            final Target target = POJOFactory.createTarget(this.wv);
            target.setName(commonName);
            hook = target.get_Hook();

            this.wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }

        final MRU mru = MRUController.addObject(Access.ADMINISTRATOR, hook);
        Assert.assertEquals(Target.class.getName(), mru.getClassName());
        Assert.assertEquals(commonName, mru.getName());
        Assert.assertEquals(hook, mru.getHook());
        Assert.assertEquals(Access.ADMINISTRATOR, mru.getUserName());
    }

}
