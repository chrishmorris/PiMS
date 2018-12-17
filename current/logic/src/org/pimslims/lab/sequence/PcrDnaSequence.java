/**
 * pims-web org.pimslims.utils.sequence PCRSequence.java
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
 * PCRSequence
 * 
 */
public class PcrDnaSequence extends AmbiguousDnaSequence implements ASequence {

    public String name = "";

    public String targetName = "";

    public final String waffle = "";

    public PcrDnaSequence(final String sequence) {
        super(sequence);
    }

    public byte[] getAlignSequence() {
        final String alignSequence = new String(">" + this.name + "\n" + this.sequence);
        return alignSequence.getBytes();
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String s) {
        this.name = s;
    }

    public String getID() {
        return this.name;
    }

    public String getWaffle() {
        return this.waffle;
    }

    public String getTargetName() {
        return this.targetName;
    }

    public void setTargetName(final String s) {
        this.targetName = s;
    }

}
