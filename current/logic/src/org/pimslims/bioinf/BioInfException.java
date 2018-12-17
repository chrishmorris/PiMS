/**
 * pims-web org.pimslims.bioinf BioInfException.java
 * 
 * @author cm65
 * @date 18 Oct 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.bioinf;

/**
 * BioInfException
 * 
 * Indicates a failure to interrogate a public database, or to interpret the response. The public databases
 * change often, so we sometimes have to work around errors.
 * 
 */
public class BioInfException extends Exception {

    /**
     * Constructor for BioInfException
     * 
     * @param arg0
     * @param arg1
     */
    public BioInfException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Constructor for BioInfException
     * 
     * @param arg0
     */
    public BioInfException(final String arg0) {
        super(arg0);
    }

}
