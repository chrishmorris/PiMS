/**
 * org.pimslims.generatedApi.role RefSampleToHazardPhrases.java
 * 
 * @date 20-Oct-2006 08:53:54
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 *           Copyright (c) 2006
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.model.api;

import org.pimslims.exception.ModelException;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.protocol.ParameterDefinition;

/**
 * RefSampleToHazardPhrases Test association between the Hazard phrases and the refSample
 * 
 */
public class ParameterTester extends org.pimslims.test.AbstractTestCase {

    public void testSetParameterDefinition() {
        wv = getWV();
        try {
            ParameterDefinition pd = create(ParameterDefinition.class);
            String pdName = pd.getName();
            String pdType = pd.getParamType();

            Parameter parameter = create(Parameter.class);
            // Parameter's name&type should be set from ParameterDefinition
            parameter.setParameterDefinition(pd);
            assertEquals(pdName, parameter.getName());
            assertEquals(pdType, parameter.getParamType());

            // wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }

        }
    }

    public void testIntParameterTypeCheck() {
        wv = getWV();
        try {
            ParameterDefinition pd = create(ParameterDefinition.class);
            pd.setParamType("Int");

            Parameter parameter = create(Parameter.class);
            parameter.setParameterDefinition(pd);

            // these are acceptable values
            parameter.setValue(null);
            assertEquals(0, parameter.getValue().length());
            parameter.setValue("");
            parameter.setValue("0");
            parameter.setValue("1");
            parameter.setValue("-1");
            assertEquals("-1", parameter.getValue());

            // test invalid value
            try {
                parameter.setValue("not a number!");
                fail("should not accept string which is not a number");
            } catch (ModelException ex) {
                // it is ok
            }

            // wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }

        }
    }

    public void testFloatParameterTypeCheck() {
        wv = getWV();
        try {
            ParameterDefinition pd = create(ParameterDefinition.class);
            pd.setParamType("Float");

            Parameter parameter = create(Parameter.class);
            parameter.setParameterDefinition(pd);

            // these are acceptable values
            parameter.setValue(null);
            parameter.setValue("");
            parameter.setValue("0");
            parameter.setValue("1");
            parameter.setValue("-1");
            parameter.setValue("2.0");
            parameter.setValue("-2.0");

            // test invalid value
            try {
                parameter.setValue("not a number!");
                fail("should not accept string which is not a number");
            } catch (ModelException ex) {
                // it is ok
            }

            // wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }

        }
    }

    public void testBooleanParameterTypeCheck() {
        wv = getWV();
        try {
            ParameterDefinition pd = create(ParameterDefinition.class);
            pd.setParamType("Boolean");

            Parameter parameter = create(Parameter.class);
            parameter.setParameterDefinition(pd);

            // these are acceptable values
            parameter.setValue(null);
            parameter.setValue("");
            parameter.setValue(Boolean.TRUE.toString());
            parameter.setValue("false");
            parameter.setValue("TRUE");
            parameter.setValue("FALSE");

            // test invalid value
            try {
                parameter.setValue("1");
                fail("should not accept string which is not a boolean");
            } catch (ModelException ex) {
                // it is ok
            }

            // wv.commit();
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testResultParameter() {
        wv = getWV();
        try {
            ParameterDefinition pd = create(ParameterDefinition.class);
            pd.setIsResult(Boolean.TRUE);
            Parameter parameter = create(Parameter.class);
            parameter.setParameterDefinition(pd);
            
            Parameter pdNext = pd.getResultParameters().iterator().next();
            assertEquals(1, pd.getResultParameters().size());
            assertEquals(0, pd.getSetupParameters().size());
            assertEquals(parameter.get_Hook(), pdNext.get_Hook());
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            wv.abort(); // not testing persistence here
        }
    }

    public void testSetupParameter() {
        wv = getWV();
        try {
            ParameterDefinition pd = create(ParameterDefinition.class);
            pd.setIsResult(Boolean.FALSE);
            Parameter parameter = create(Parameter.class);
            parameter.setParameterDefinition(pd);
            
            Parameter pdNext = pd.getSetupParameters().iterator().next();
            assertEquals(0, pd.getResultParameters().size());
            assertEquals(1, pd.getSetupParameters().size());
            assertEquals(parameter.get_Hook(), pdNext.get_Hook());
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            wv.abort(); // not testing persistence here
        }
    }
}
