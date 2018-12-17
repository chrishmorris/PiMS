package org.pimslims.lab.primer;

import java.io.Serializable;

/**
 * YorkPrimerBean, used to make list of Primers based on user-entered Tm TODO rename Not just for York!
 * 
 * @author Susy Griffiths YSBL
 */
public class YorkPrimerBean implements Comparable, Serializable {
    /**
     * To satisfy Serializable Interface
     */
    private static final long serialVersionUID = 1;

    /**
     * properties
     */

    private String constructId;

    private String primerType;

    private String primerSeq;

    private Float primerTm;

    private Integer primerLength;

    public int compareTo(final Object obj) {
        if (!(obj instanceof YorkPrimerBean)) {
            throw new ClassCastException("obj1 is not a YSBL Primer ");
        }
        return 0;
    }

    /**
     * @return Returns the constructId.
     */
    public String getConstructId() {
        return this.constructId;
    }

    /**
     * @param constructId The constructId to set.
     */
    public void setConstructId(final String constructId) {
        this.constructId = constructId;
    }

    /**
     * @return Returns the primerLength.
     */
    public Integer getPrimerLength() {
        return this.primerLength;
    }

    /**
     * @param primerLength The primerLength to set.
     */
    public void setPrimerLength(final Integer primerLength) {
        this.primerLength = primerLength;
    }

    /**
     * @return Returns the primerSeq.
     */
    public String getPrimerSeq() {
        return this.primerSeq;
    }

    /**
     * @param primerSeq The primerSeq to set.
     */
    public void setPrimerSeq(final String primerSeq) {
        this.primerSeq = primerSeq;
    }

    /**
     * @return Returns the primerTm.
     */
    public Float getPrimerTm() {
        return this.primerTm;
    }

    /**
     * @param primerTm The primerTm to set.
     */
    public void setPrimerTm(final Float primerTm) {
        this.primerTm = primerTm;
    }

    /**
     * @return Returns the primerType.
     */
    public String getPrimerType() {
        return this.primerType;
    }

    /**
     * @param primerType The primerType to set.
     */
    public void setPrimerType(final String primerType) {
        this.primerType = primerType;
    }

}
