/**
 * pims-model org.pimslims.access ChildAccessTest.java
 * 
 * @author bl67
 * @date 3 Dec 2009
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2009 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.access;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.test.AbstractTestCase;

/**
 * ChildAccessTest
 * 
 */
public class ChildAccessTest extends AbstractTestCase {

    public void testSetProtocolChildAccessThoughParent() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            LabNotebook owner = create(LabNotebook.class);
            wv.flush();
            ExperimentType type = new ExperimentType(wv, UNIQUE);
            wv.flush();
            Protocol protocol = new Protocol(wv, UNIQUE, type);
            RefInputSample refInput = create(RefInputSample.class, RefInputSample.PROP_PROTOCOL, protocol);
            RefOutputSample refOutput =
                create(RefOutputSample.class, RefOutputSample.PROP_PROTOCOL, protocol);
            ParameterDefinition pd =
                create(ParameterDefinition.class, ParameterDefinition.PROP_PROTOCOL, protocol);
            //set access on parent
            protocol.setAccess(owner);
            assertEquals(owner, protocol.getAccess());
            assertEquals(owner, refInput.getAccess());
            assertEquals(owner, refOutput.getAccess());
            assertEquals(owner, pd.getAccess());
        } finally {
            wv.abort();
        }
    }

    public void testSetSampleChildAccessThoughParent() throws AccessException, ConstraintException {
        wv = getWV();
        try {
            LabNotebook owner = create(LabNotebook.class);
            Sample sample = create(Sample.class);
            SampleComponent sc = create(SampleComponent.class, SampleComponent.PROP_ABSTRACTSAMPLE, sample);
            //set access on parent
            sample.setAccess(owner);
            assertEquals(owner, sample.getAccess());
            assertEquals(owner, sc.getAccess());
        } finally {
            wv.abort();
        }
    }

}
