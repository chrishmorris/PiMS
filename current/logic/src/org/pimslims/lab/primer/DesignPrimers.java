/**
 * 
 */
package org.pimslims.lab.primer;

import java.util.List;

/**
 * @author Susy Griffiths YSBL 03-02-07
 * 
 */
public interface DesignPrimers {

    public static final int MAX_LENGTH = 50;

    List<String> makePrimers(String[] sequence, float desiredTm);

}
