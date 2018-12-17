/**
 * current-pims-web org.pimslims.command.DataUpdate ExperimentParameterFixerTest.java
 * 
 * @author bl67
 * @date 17 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 bl67
 * 
 * 
 */
package org.pimslims.command.DataUpdate;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import junit.framework.Assert;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.test.AbstractTestCase;

/**
 * ExperimentParameterFixerTest
 * 
 */
public class ExperimentParameterFixerTest extends AbstractTestCase {

    private PrintStream stderr;

    private ByteArrayOutputStream quietOut;

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // The code under test prints to stderr, creating the appearance of a problem
        this.stderr = System.err;
        this.quietOut = new ByteArrayOutputStream();
        final PrintStream quietPrint = new PrintStream(this.quietOut);
        System.setErr(quietPrint);
    }

    /*
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        System.setErr(this.stderr);
        // to see output: System.out.println(quietOut.toString());
    }

    public void testFixParamterType() throws ConstraintException, AccessException {
        this.wv = this.getWV();
        try {
            final ExperimentParamterFixer fixer = new ExperimentParamterFixer();
            fixer.fixData(this.wv);

            final ParameterDefinition pd = this.create(ParameterDefinition.class);
            pd.setName("pdname" + System.currentTimeMillis());
            final String pdName = pd.getName();
            pd.setParamType("Int");

            final Parameter p = this.create(Parameter.class);
            p.setName("abc");
            p.setValue("abc");
            p.setParameterDefinition(pd);
            fixer.fixData(this.wv);

            final Parameter fixedP = this.wv.get(p.get_Hook());
            Assert.assertEquals(pdName, fixedP.getName());

        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }

    }

}
