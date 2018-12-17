/**
 * pims-web org.pimslims.lab.primer PrimerProxy.java
 * 
 * @author bl67
 * @date 8 Jul 2008
 * 
 * Protein Information Management System
 * @version: 2.2
 * 
 * Copyright (c) 2008 bl67 *
 * 
 * 
 */
package org.pimslims.lab.primer;

import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.model.molecule.Primer;

/**
 * PrimerProxy Used only in PrimerBeanReader
 */
@Deprecated
// retire this, it give little value
public class PrimerProxy {

    private final org.pimslims.model.molecule.Primer primer;

    /**
     * @param primer
     */
    public PrimerProxy(final Primer primer) {
        super();
        this.primer = primer;
        assert primer != null;
    }

    public float getTmFullFloat() {
        if (null != this.primer.getMeltingTemperature()) {
            return this.primer.getMeltingTemperature().floatValue();
        } else if (this.primer.getSequence() != null) {//if tm is not stored, we should calculate it
            return (new DnaSequence(this.primer.getSequence())).getTm();
        }
        return 0.0f;
    }

/*
    public String getGcfull() {
        if (null != this.primer.getSeqString()) {
            DnaSequence dnaSequence;
            try {
                dnaSequence = new DnaSequence(this.primer.getSeqString());
            } catch (final IllegalArgumentException e) {
                return "0"; // bad sequence
            }
            return new Double(dnaSequence.getGCContent()).toString();
        }
        return "0";
    } */

    public String getDirection() {
        return this.primer.getDirection();
    }

    public String getSeqString() {
        return this.primer.getSequence();
    }

}
