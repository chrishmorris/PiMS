/**
 * pims-web org.pimslims.servlet AjaxValidatorTest.java
 * 
 * @author Marc Savitsky
 * @date 22 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky
 * 
 */
package org.pimslims.servlet;

import java.util.HashMap;

import junit.framework.Assert;

import org.pimslims.metamodel.MetaClass;
import org.pimslims.test.AbstractTestCase;

/**
 * AjaxValidatorTest
 * 
 */
public class AjaxValidatorTest extends AbstractTestCase {

    /**
     * @param name
     */
    public AjaxValidatorTest(final String name) {
        super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.rv = this.getRV();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {

        Assert.assertFalse(this.rv.isCompleted());
        if (this.rv.isCompleted()) {
            // can't tidy up
            return;
        }
        if (null != this.rv) {
            this.rv.abort(); // not testing persistence
        }
    }

    public void testValidateExperimentGroup() {

        final String metaClassName = "org.pimslims.model.experiment.ExperimentGroup";
        final MetaClass metaClass = AbstractTestCase.model.getMetaClass(metaClassName);
        final java.util.Map<String, Object> params = new HashMap<String, Object>();
        String errorString;

        params.put("name", "PCR123");
        errorString = AjaxValidate.validateExperimentGroup(this.rv, metaClass, params, "PCR\\d\\d\\d");
        //System.out.println("AjaxValidate.validateExperimentGroup [" + errorString + "]");
        Assert.assertNull(errorString);

        params.clear();
        params.put("name", "marc");
        errorString = AjaxValidate.validateExperimentGroup(this.rv, metaClass, params, "PCR\\d\\d\\d");
        //System.out.println("AjaxValidate.validateExperimentGroup [" + errorString + "]");
        Assert.assertNotNull(errorString);
    }

}
