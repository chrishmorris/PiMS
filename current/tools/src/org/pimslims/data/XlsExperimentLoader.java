/**
 * web org.pimslims.data.mpsi XlsExperimentLoader.java
 * 
 * @author bl67
 * @date 23 Apr 2008
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2008 bl67
 * 
 * 
 */
package org.pimslims.data;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.pimslims.lab.create.ExperimentFactory;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.presentation.experiment.ExperimentWriter;
import org.pimslims.utils.XlsReader;

/**
 * XlsExperimentLoader
 * 
 */
public class XlsExperimentLoader extends XlsReader {
    protected final WritableVersion wv;

    protected Protocol protocol;

    /**
     * @param filerName
     * @param fieldNames
     * @param protocol2
     * @throws FileNotFoundException
     */
    public XlsExperimentLoader(final String filerName, final List<String> fieldNames, final Protocol protocol)
        throws FileNotFoundException {
        super(filerName, fieldNames);
        this.protocol = protocol;
        if (protocol != null) {
            this.wv = (WritableVersion) protocol.get_Version();
        } else {
            this.wv = null;
        }
    }

    public Experiment createNextExp() throws ConstraintException, AccessException {
        //create experiment
        final String name = "name" + System.currentTimeMillis();
        final Calendar now = java.util.Calendar.getInstance();
        final ExperimentType et = this.protocol.getExperimentType();
        final Experiment exp = new Experiment(this.wv, name, now, now, et);
        exp.setName(et.getName() + exp.getDbId());

        //create parameters
        ExperimentFactory.createProtocolParametersForExperiment(this.wv, this.protocol, exp);

        //get values from file
        final Map<String, String> values = super.next();

        //set parameter values from file
        for (final Parameter p : exp.getParameters()) {
            final String pname = p.getName();
            if (!values.keySet().contains(pname)) {
                throw new ConstraintException("Can not find value for " + pname + " at row:"
                    + this.currentRow);
            }
            final String value = values.get(pname);
            p.setValue(value);
        }
        //create output sample
        ExperimentWriter.createOutputSamplesForExperiment(this.wv, exp, this.protocol);
        //create input sample
        HolderFactory.createInputSamplesForExperiment(this.wv, exp, this.protocol);

        //output info
        System.out.println("Created " + exp + " for row:" + this.currentRow);

        return exp;
    }
}
