/** 
 * V4_3-web org.pimslims.presentation.experiment SpreadsheetError.java
 * @author cm65
 * @date 20 Dec 2011
 *
 * Protein Information Management System
 * @version: 3.2
 *
 * Copyright (c) 2011 cm65 
 * The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.spreadsheet;

/**
 * @author cm65
 * 
 */
public class SpreadsheetError {

    private final String well;

    private final String label;

    private final String value;

    private final String barcode;

    public SpreadsheetError(final String well, final String label, final String value) {
        this("", well, label, value);
    }

    /**
     * Constructor for SpreadsheetError
     * 
     * @param barcode
     * @param well2
     * @param keyword
     * @param value2
     */
    public SpreadsheetError(final String barcode, final String well, final String keyword,
        final String value) {
        this.barcode = barcode;
        this.well = well;
        this.label = keyword;
        this.value = value;
    }

    String getWell() {
        return this.well;
    }

    public String getKeyword() {
        return this.label;
    }

    String getValue() {
        return this.value;
    }

    String getBarcode() {
        return this.barcode;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String ret =
            "Well: " + this.well + " unable to use column: " + this.label + ". Value was: " + this.value;
        if (null != this.barcode && !"".equals(this.barcode)) {
            ret = "Plate: " + this.barcode + " " + ret;
        }
        return ret;
    }

}