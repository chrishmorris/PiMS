/**
 * IspybRest2 org.pimslims.ispyb NotFoundException.java
 * 
 * @author cm65
 * @date 20 Jun 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.ispyb;

/**
 * NotFoundException TODO move this to jar
 */
public class NotFoundException extends Exception {

    /**
     * Constructor for NotFoundException
     * 
     * @param arg0
     * @param arg1
     */
    NotFoundException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Constructor for NotFoundException
     * 
     * @param arg0
     */
    private NotFoundException(String arg0) {
        super(arg0);
    }

    /**
     * Constructor for NotFoundException
     * 
     * @param arg0
     */
    NotFoundException(Throwable arg0) {
        super(arg0);
    }

}
