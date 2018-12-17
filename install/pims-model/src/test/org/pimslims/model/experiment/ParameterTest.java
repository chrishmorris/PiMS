/**
 * pims-model org.pimslims.model.experiment ParameterTest.java
 * 
 * @author cm65
 * @date 1 Feb 2011
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.model.experiment;

import java.util.Calendar;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.test.AbstractTestCase;

/**
 * ParameterTest
 * 
 */
public class ParameterTest extends AbstractTestCase {

    /**
     * ten String
     */
    private static final String TEN = "0123456789";

    /**
     * FIFTY String
     */
    private static final String FIFTY = TEN + TEN + TEN + TEN + TEN;

    private static final Calendar NOW = Calendar.getInstance();

    private static final String VALUE = FIFTY + FIFTY + FIFTY + FIFTY + FIFTY + FIFTY;

    /**
     * Test method for {@link org.pimslims.model.experiment.Parameter#setValue(java.lang.String)}.
     * 
     * @throws ConstraintException
     */
    public void testSetLongValue() throws ConstraintException {
        WritableVersion wv = this.getWV();
        try {

            ExperimentType type = new ExperimentType(wv, UNIQUE);
            Experiment experiment = new Experiment(wv, UNIQUE, NOW, NOW, type);
            Parameter parm = new Parameter(wv, experiment);
            parm.setValue(VALUE);
            wv.flush();
            Parameter found = wv.get(parm.get_Hook());
            assertEquals(VALUE, found.getValue());
        } finally {
            wv.abort();
        }
    }

}
