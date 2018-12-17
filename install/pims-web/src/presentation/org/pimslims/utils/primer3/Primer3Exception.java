/**
 * 
 */
package org.pimslims.utils.primer3;

/**
 * Exception thrown when there are problems while generating Primers by PIMS using primer3_core.exe
 * 
 * @author Marc Savitsky
 * @since 27-03-2007
 * 
 */
public class Primer3Exception extends Exception {

    /**
     * @param arg0
     */
    public Primer3Exception(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public Primer3Exception(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public Primer3Exception(Throwable arg0) {
        super(arg0);
    }

}
