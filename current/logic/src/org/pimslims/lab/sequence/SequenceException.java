/**
 * pims-web org.pimslims.servlet.oppf SequenceException.java
 * 
 * @author Marc Savitsky
 * @date 27 Aug 2008
 * 
 * Protein Information Management System
 * @version: 2.2
 * 
 * Copyright (c) 2008 Marc Savitsky The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.lab.sequence;

/**
 * SequenceException
 * 
 */

public class SequenceException extends Exception {

    /**
     * @param arg0
     */
    public SequenceException(final String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public SequenceException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public SequenceException(final Throwable arg0) {
        super(arg0);
    }

}
