/**
 * pims-web org.pimslims.utils.sequence ASequence.java
 * 
 * @author Marc Savitsky
 * @date 1 Aug 2008
 * 
 * Protein Information Management System
 * @version: 2.2
 * 
 * Copyright (c) 2008 Marc Savitsky The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.lab.sequence;

/**
 * ASequence
 * 
 */
public interface ASequence {

    public abstract String getSequence();

    public abstract int getLength();

    public abstract String getName();

    public abstract String getWaffle();

    public abstract String getID();

    public abstract byte[] getAlignSequence();
}
