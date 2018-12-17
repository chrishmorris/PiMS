/**
 * current-pims-web org.pimslims.lab.sequence PositionAwareSequenceFactory.java
 * 
 * @author Petr Troshin
 * @date 25 Nov 2007
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2007 Petr 
 * 
 * 
 */
package org.pimslims.lab.sequence;

/**
 * PositionAwareSequenceFactory
 * 
 */
public class PositionAwareSequenceFactory {

    private PositionAwareSequenceFactory() {
        // No constructor
    }

    public static final PositionAwareSequenceFactory getFactory() {
        return new PositionAwareSequenceFactory();
    }
}
