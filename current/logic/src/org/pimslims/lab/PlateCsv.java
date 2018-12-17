/**
 * V3_3-web org.pimslims.lab CreatePlateCsv.java
 * 
 * @author cm65
 * @date 19 Mar 2010
 * 
 * Protein Information Management System
 * @version: 3.2
 * 
 * Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.lab;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.spreadsheet.CsvPrinter;

/**
 * CreatePlateCsv
 * 
 */
public abstract class PlateCsv implements AbstractCsvData {

    protected final List<String> parameterNames;

    protected final List<String> inputSampleNames = new ArrayList<String>();

    protected final Map<String, String> inputSampleUnits = new HashMap<String, String>();

    protected Iterator iterator;

    protected final List<String> parameterDefaults;

    public PlateCsv(final Protocol protocol) {
        this.parameterNames = new LinkedList<String>();
        this.parameterDefaults = new LinkedList<String>();
        if (protocol != null && protocol.getParameterDefinitions() != null) {
            final Collection<ParameterDefinition> pds = protocol.getParameterDefinitions();
            for (final ParameterDefinition pd : pds) {
                this.parameterNames.add(pd.getName());
                String defaultValue = pd.getDefaultValue();
                if (null == defaultValue) {
                    defaultValue = "";
                }
                this.parameterDefaults.add(defaultValue);
            }
        }
        if (protocol != null && protocol.getRefInputSamples() != null) {
            final Collection<RefInputSample> ris = protocol.getRefInputSamples();
            for (final RefInputSample ri : ris) {
                this.inputSampleNames.add(ri.getName());
                final String unit = ri.getDisplayUnit();
                if (null != unit && !"".equals(unit)) {
                    this.inputSampleUnits.put(ri.getName(), unit);
                }
            }
        }

    }

    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    /**
     * PlateCsv.isPlateExperiment
     * 
     * @return
     */
    protected abstract boolean isPlateExperiment();

    protected String experimentNumber(final String name) {
        final int i = name.lastIndexOf(':');
        if (i > 0 && i + 1 < name.length()) {
            return name.substring(i + 1);
        }
        return name;
    }

    public String[] getHeaders() {

        final List<String> headers = new LinkedList<String>();

        if (this.isPlateExperiment()) {
            headers.add("PlateId");
        } else {
            headers.add("GroupId");
        }

        if (this.isPlateExperiment()) {
            headers.add("Well");
        } else {
            headers.add("Experiment");
        }

        headers.add("Target");
        headers.add("Construct");

        headers.addAll(this.parameterNames);

        for (final String inputSampleName : this.inputSampleNames) {
            headers.add(inputSampleName);
            if (this.inputSampleUnits.containsKey(inputSampleName)) {
                headers.add(inputSampleName + " " + this.inputSampleUnits.get(inputSampleName));
            }
        }

        final String[] arrayHeaders = new String[headers.size()];
        headers.toArray(arrayHeaders);
        return arrayHeaders;
    }

    public static String toString(final AbstractCsvData csvData) {
        try {
            final StringWriter sw = new StringWriter();
            final CsvPrinter printer = new CsvPrinter(sw);
            printer.println(csvData.getHeaders());
            while (csvData.hasNext()) {
                printer.println(csvData.next());
            }
            return sw.toString();
        } catch (final IOException e) {
            // cant really happen
            throw new RuntimeException(e);
        }
    }

}
