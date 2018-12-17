/**
 * 
 */
package org.pimslims.graph;

/**
 * Exception thrown when there are problems while generating Graph by PIMS using dot.exe
 * 
 * @author Ekaterina Pilicheva
 * @since 31.03.2006
 * 
 */
public class GraphGenerationException extends Exception {

    /**
     * @param arg0
     */
    public GraphGenerationException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public GraphGenerationException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public GraphGenerationException(Throwable arg0) {
        super(arg0);
    }

}
