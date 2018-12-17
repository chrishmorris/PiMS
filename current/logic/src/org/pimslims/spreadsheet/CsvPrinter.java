/**
 * V2_2-pims-web org.pimslims.csv CSVPrinter.java
 * 
 * @author cm65
 * @date 29 May 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65 *
 * 
 * 
 */
package org.pimslims.spreadsheet;

import java.io.IOException;
import java.io.Writer;

/**
 * CSVPrinter
 * 
 */
public class CsvPrinter {

    private final Writer writer;

    /**
     * @param responseWriter
     */
    public CsvPrinter(final Writer writer) {
        this.writer = writer;
    }

    /**
     * @param headers
     * @throws IOException
     */
    public void println(final String[] data) throws IOException {
        for (int i = 0; i < data.length; i++) {
            if (0 < i) {
                this.writer.append(',');
            }
            if (null == data[i]) {
                this.writer.append("");
            } else if (data[i].contains(",") || data[i].contains("\"")) {
                this.writer.append('"');
                this.writer.append(this.escape(data[i]));
                this.writer.append('"');
            } else {
                this.writer.append(data[i]);
            }
        }
        this.writer.append('\n');
    }

    /**
     * @param string
     * @return the string, with every " or \ preceded with a \
     */
    private CharSequence escape(final String string) {
        return string.replaceAll("([\"\\\\])", "\\\\$1");
    }

    /**
     * 
     */
    public void close() throws IOException {
        this.writer.close();
    }

}
