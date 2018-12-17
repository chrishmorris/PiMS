package org.pimslims.bioinf;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * BlastAlignmentBean, used to store details of a Blast alignment
 * 
 * @author Susy Griffiths YSBL June 2007
 */
public class BlastAlignmentBean implements Comparable, Serializable {
    /**
     * To satisfy Serializable Interface
     */
    private static final long serialVersionUID = 1;

    /**
     * properties
     */

    private Integer hitNum;

    private Integer alignmentNo;

    private Integer score;

    private Double bits;

    private Double expect;

    private Double probability;

    private Double identity; // % of identities

    private Integer numIdentities; // number of identities

    private Double positives; // % of positives

    private Integer numPositives; // number of positives

    private Integer gaps;
    
    private String strand;

    private String querySeq;

    private Integer querySeqStart;

    private Integer querySeqEnd;

    private Integer alignmentLen;

    private String pattern;

    private String matchSeq;

    private Integer matchSeqStart;

    private Integer matchSeqEnd;

    private ArrayList<String> alignmentRows; // processed rows for display in

    // JSP

    /**
     * 
     */
    public BlastAlignmentBean() {
        super();
    }

    public int compareTo(Object obj) {
        if (!(obj instanceof BlastAlignmentBean)) {
            throw new ClassCastException("obj1 is not a Blast alignment ");
        }
        return 0;
    }

    /**
     * @return Returns the bits.
     */
    public Double getBits() {
        return bits;
    }

    /**
     * @param bits The bits to set.
     */
    public void setBits(Double bits) {
        this.bits = bits;
    }

    /**
     * @return Returns the expect.
     */
    public Double getExpect() {
        return expect;
    }

    /**
     * @param expect The expect to set.
     */
    public void setExpect(Double expect) {
        this.expect = expect;
    }

    /**
     * @return Returns the hitNum.
     */
    public int getHitNum() {
        return hitNum;
    }

    /**
     * @param hitNum The hitNum to set.
     */
    public void setHitNum(int hitNum) {
        this.hitNum = hitNum;
    }

    /**
     * @return Returns the identity.
     */
    public Double getIdentity() {
        return identity;
    }

    /**
     * @param identity The identity to set.
     */
    public void setIdentity(Double identity) {
        this.identity = identity;
    }

    /**
     * @return Returns the matchSeq.
     */
    public String getMatchSeq() {
        return matchSeq;
    }

    /**
     * @param matchSeq The matchSeq to set.
     */
    public void setMatchSeq(String matchSeq) {
        this.matchSeq = matchSeq;
    }

    /**
     * @return Returns the matchSeqEnd.
     */
    public int getMatchSeqEnd() {
        return matchSeqEnd;
    }

    /**
     * @param matchSeqEnd The matchSeqEnd to set.
     */
    public void setMatchSeqEnd(int matchSeqEnd) {
        this.matchSeqEnd = matchSeqEnd;
    }

    /**
     * @return Returns the matchSeqStart.
     */
    public int getMatchSeqStart() {
        return matchSeqStart;
    }

    /**
     * @param matchSeqStart The matchSeqStart to set.
     */
    public void setMatchSeqStart(int matchSeqStart) {
        this.matchSeqStart = matchSeqStart;
    }

    /**
     * @return Returns the pattern.
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * @param pattern The pattern to set.
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * @return Returns the positives.
     */
    public Double getPositives() {
        return positives;
    }

    /**
     * @param positives The positives to set.
     */
    public void setPositives(Double positives) {
        this.positives = positives;
    }

    /**
     * @return Returns the probability.
     */
    public Double getProbability() {
        return probability;
    }

    /**
     * @param probability The probability to set.
     */
    public void setProbability(Double probability) {
        this.probability = probability;
    }

    /**
     * @return Returns the querySeq.
     */
    public String getQuerySeq() {
        return querySeq;
    }

    /**
     * @param querySeq The querySeq to set.
     */
    public void setQuerySeq(String querySeq) {
        this.querySeq = querySeq;
    }

    /**
     * @return Returns the querySeqEnd.
     */
    public int getQuerySeqEnd() {
        return querySeqEnd;
    }

    /**
     * @param querySeqEnd The querySeqEnd to set.
     */
    public void setQuerySeqEnd(int querySeqEnd) {
        this.querySeqEnd = querySeqEnd;
    }

    /**
     * @return Returns the querySeqStart.
     */
    public int getQuerySeqStart() {
        return querySeqStart;
    }

    /**
     * @param querySeqStart The querySeqStart to set.
     */
    public void setQuerySeqStart(int querySeqStart) {
        this.querySeqStart = querySeqStart;
    }

    /**
     * @return Returns the score.
     */
    public Integer getScore() {
        return score;
    }

    /**
     * @param score The score to set.
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * @return Returns the alignmentNo.
     */
    public int getAlignmentNo() {
        return alignmentNo;
    }

    /**
     * @param alignmentNo The alignmentNo to set.
     */
    public void setAlignmentNo(int alignmentNo) {
        this.alignmentNo = alignmentNo;
    }

    /**
     * @return Returns the alignmentLen.
     */
    public int getAlignmentLen() {
        return alignmentLen;
    }

    /**
     * @param alignmentLen The alignmentLen to set.
     */
    public void setAlignmentLen(int alignmentLen) {
        this.alignmentLen = alignmentLen;
    }

    /**
     * @return Returns the gaps.
     */
    public int getGaps() {
        return gaps;
    }

    /**
     * @param gaps The gaps to set.
     */
    public void setGaps(int gaps) {
        this.gaps = gaps;
    }

    /**
     * @return Returns the strand.
     */
    public String getStrand() {
        return this.strand;
    }

    /**
     * @param strand The strand to set.
     */
    public void setStrand(String strand) {
        this.strand = strand;
    }

    /**
     * @return Returns the numIdentities.
     */
    public Integer getNumIdentities() {
        return numIdentities;
    }

    /**
     * @param numIdentities The numIdentities to set.
     */
    public void setNumIdentities(Integer numIdentities) {
        this.numIdentities = numIdentities;
    }

    /**
     * @return Returns the numPositives.
     */
    public Integer getNumPositives() {
        return numPositives;
    }

    /**
     * @param numPositives The numPositives to set.
     */
    public void setNumPositives(Integer numPositives) {
        this.numPositives = numPositives;
    }

    /**
     * @return Returns the alignmentRows.
     */
    public ArrayList<String> getAlignmentRows() {
        return alignmentRows;
    }

    /**
     * @param alignmentRows The alignmentRows to set.
     */
    public void setAlignmentRows(ArrayList<String> alignmentRows) {
        this.alignmentRows = alignmentRows;
    }

}
