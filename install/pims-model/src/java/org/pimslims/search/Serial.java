/**
 * pims-model org.pimslims.search Serial.java
 * 
 * @author bl67
 * @date 11 Dec 2008
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2008 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.search;

/**
 * Serial
 * 
 */
public class Serial {

    int serial = 0;

    public Serial() {
    }

    public int getNextValue() {
        serial += 1;
        return serial;
    }
}
