/**
 * tools org.pimslims.command.DataUpdate ExperimentConnector.java
 * 
 * @author cm65
 * @date 14 Jun 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.command.DataUpdate;

import java.util.Collection;
import java.util.Iterator;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;

/**
 * ExperimentConnector
 * 
 */
public class ExperimentConnector {

    /**
     * ExperimentConnector.fixParameters
     * 
     * @param version
     * @throws ConstraintException
     */
    public static void fixParameters(WritableVersion version) throws ConstraintException {
        Collection<Parameter> toFix =
            version.findAll(Parameter.class, Parameter.PROP_PARAMETERDEFINITION, null);
        for (Iterator iterator = toFix.iterator(); iterator.hasNext();) {
            Parameter parameter = (Parameter) iterator.next();
            Protocol protocol = parameter.getExperiment().getProtocol();
            if (null == protocol) {
                continue;
            }
            ParameterDefinition pd =
                (ParameterDefinition) protocol.findFirst(Protocol.PROP_PARAMETERDEFINITIONS,
                    ParameterDefinition.PROP_NAME, parameter.getName());
            parameter.setParameterDefinition(pd);
        }

    }

    /**
     * ExperimentConnector.fixInputs
     * 
     * @param version
     * @throws ConstraintException
     */
    public static void fixInputs(WritableVersion version) throws ConstraintException {
        Collection<InputSample> toFix =
            version.findAll(InputSample.class, InputSample.PROP_REFINPUTSAMPLE, null);
        for (Iterator iterator = toFix.iterator(); iterator.hasNext();) {
            InputSample is = (InputSample) iterator.next();
            Protocol protocol = is.getExperiment().getProtocol();
            if (null == protocol) {
                continue;
            }
            RefInputSample ris =
                (RefInputSample) protocol.findFirst(Protocol.PROP_REFINPUTSAMPLES, RefInputSample.PROP_NAME,
                    is.getName());
            is.setRefInputSample(ris);
        }

    }

    /**
     * ExperimentConnector.fixOutputs
     * 
     * @param version
     */
    public static void fixOutputs(WritableVersion version) throws ConstraintException {
        Collection<OutputSample> toFix =
            version.findAll(OutputSample.class, OutputSample.PROP_REFOUTPUTSAMPLE, null);
        for (Iterator iterator = toFix.iterator(); iterator.hasNext();) {
            OutputSample os = (OutputSample) iterator.next();
            Protocol protocol = os.getExperiment().getProtocol();
            if (null == protocol) {
                continue;
            }
            RefOutputSample ros =
                (RefOutputSample) protocol.findFirst(Protocol.PROP_REFOUTPUTSAMPLES,
                    RefOutputSample.PROP_NAME, os.getName());
            os.setRefOutputSample(ros);
        }

    }

}
