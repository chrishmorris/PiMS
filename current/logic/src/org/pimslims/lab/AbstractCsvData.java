/**
 * V3_3-web org.pimslims.lab AbstractCsvData.java
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

/**
 * A description of the data to go in the CSV file. You should make an class that implements this, and return
 * an instance of it from your getCsvData() method.
 * 
 * @author cm65
 * 
 */
public interface AbstractCsvData {

    /**
     * @return the column headers
     */
    String[] getHeaders();

    /**
     * @return the values for the next line
     * @throws NoSuchElementException if there is no more data
     */
    String[] next();

    /**
     * @return true if there is more data
     */
    boolean hasNext();

}
