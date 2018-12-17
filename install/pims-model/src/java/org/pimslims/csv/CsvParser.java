/**
 * V2_2-pims-web org.pimslims.csv LabeledCSVParser.java
 * 
 * @author cm65
 * @date 29 May 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65 *
 * 
 */
package org.pimslims.csv;

import java.io.IOException;
import java.io.Reader;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Processes a CSV file with column headers
 * 
 */
public class CsvParser {

    private final CSVReader myParser;

    private String[] currentLine;

    private String[] labels;

    /**
     * @param reader
     * @throws IOException
     */
    public CsvParser(final Reader reader) throws IOException {
        this.myParser = new CSVReader(reader);
        this.getLabels();
    }

    /**
     * @return
     * @throws IOException
     */
    public String[] getLabels() throws IOException {
        if (null != this.labels) {
            return this.labels.clone();
        }
        this.labels = this.myParser.readNext();
        return this.labels;
    }

    /**
     * @return
     * @throws IOException
     */
    public Object getLine() throws IOException {
        if (null == this.labels) {
            throw new IllegalStateException("You must call getLabels() first");
        }
        this.currentLine = this.myParser.readNext();
        return this.currentLine;
    }

    /**
     * @param wellName
     * @return
     */
    public String getValueByLabel(final String label) {
        for (int i = 0; i < this.labels.length; i++) {
            if (this.labels[i].equals(label)) {
                if (i >= this.currentLine.length) {
                    return null;
                }
                return this.currentLine[i];
            }
        }
        throw new IllegalArgumentException("No such column: " + label);
    }

    /**
     * CsvParser.getIntegerByLabel
     * 
     * @param string
     * @return
     */
    public Integer getIntegerByLabel(final String label) {
        final String value = this.getValueByLabel(label);
        if (null == value || "".equals(value)) {
            return null;
        }
        final Integer ret = Integer.decode(value);
        return ret;
    }

}
