/**
 * org.pimslims.upgrader DateFixerTest.java
 * 
 * @date 21-Dec-2006 10:59:11
 * 
 * @author Bill
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 *           Copyright (c) 2006 STFC
 * 
 */
package org.pimslims.command;

import java.util.Collection;

import junit.framework.Assert;

import org.pimslims.command.DataUpdate.DataFixer;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.sample.Sample;
import org.pimslims.test.AbstractTestCase;

public class DataFixerSSPFTest extends AbstractTestCase {

    public void testDeleteExpParameters() throws AccessException, ConstraintException, AbortedException {
        this.wv = this.getWV();
        try {
            DataFixer.DeleteDuplicatedExpParameters(this.wv);

            this.wv.commit();
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }

        }
    }

    public void testReplaceInvalidChar() throws AbortedException, ConstraintException, AccessException {
        this.wv = this.getWV();
        String hook;
        try {
            final Sample sample = this.create(Sample.class);
            sample.setAmountUnit("Âµg");
            hook = sample.get_Hook();
            this.wv.commit();
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }

        this.wv = this.getWV();
        try {
            DataFixer.replaceInvalidChar(this.wv);
            final Sample sample = this.wv.get(hook);
            Assert.assertEquals("µg", sample.getAmountUnit());
            this.wv.commit();
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public void testDetectEmptyRequiredAttribute() throws AbortedException, ConstraintException,
        AccessException {

        System.out
            .println("\nThis test will scan the data to find potential problem like: required attribute with null value");

        for (final String className : AbstractTestCase.model.getClassNames()) {
            if (!className.endsWith("LabBookEntry") && !className.endsWith("ContentStored")
                && !className.endsWith("ExperimentalData")) {
                System.out.println("Checking " + className);
                final WritableVersion wv = AbstractTestCase.model.getWritableVersion(AbstractModel.SUPERUSER);
                try {
                    final MetaClass mc = AbstractTestCase.model.getMetaClass(className);
                    final Collection<ModelObject> mos = wv.getAll(mc.getJavaClass(), 0, Integer.MAX_VALUE); // doesnn't matter if this is slow
                    for (final MetaAttribute ma : mc.getAttributes().values()) {
                        if (ma.isRequired() && !ma.isDerived() && !ma.isHidden()) {
                            for (final ModelObject mo : mos) {
                                final Object attributeValue = mo.get_Value(ma.getName());
                                if (!this.ValueisValid(attributeValue)) {
                                    this.reportInvalid(ma, mo);
                                    if (className.endsWith("AppDataString")) {
                                        mo.delete();
                                    } else if (className.endsWith("MolComponent")
                                        && ma.getName().endsWith("molType")) {
                                        mo.set_Value(ma.getName(), "other");
                                    } else {
                                        mo.set_Value(ma.getName(), ma.getName());
                                    }
                                }
                            }
                        }
                    }
                    /*
                     * wv.commit(); } catch (ModelException ex) { fail(ex.toString());
                     */
                    wv.commit();
                } finally {
                    if (!wv.isCompleted()) {
                        wv.abort();
                    }
                }
            }
        }

    }

    /**
     * @param ma
     * @param mo
     */
    private MetaAttribute lastMA = null;

    private String errorInfo = null;

    private void reportInvalid(final MetaAttribute ma, final ModelObject mo) {
        if (ma != this.lastMA) {
            this.lastMA = ma;
            System.out.println("Invalid value found in " + ma.getMetaClass().getName() + "." + ma.getName()
                + ":");
        }
        System.out.println(mo.get_Hook() + this.errorInfo);

    }

    /**
     * @param attributeValue
     * @return
     */
    private boolean ValueisValid(final Object attributeValue) {
        if (attributeValue == null) {
            this.errorInfo = " is null";
            return false;
        } else if (attributeValue instanceof String) {
            final String av = (String) attributeValue;
            if (av.length() == 0) {
                this.errorInfo = " is not null but a empty string";
                return false;
            }
            if (av.trim().length() == 0) {
                this.errorInfo = " is Empty after trim";
                return false;
            }
        }
        return true;
    }
}
