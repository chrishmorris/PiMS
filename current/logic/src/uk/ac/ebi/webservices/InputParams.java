/**
 * InputParams.java
 * 
 * This file was auto-generated from WSDL by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java
 * emitter.
 */

package uk.ac.ebi.webservices;

import java.util.HashSet;
import java.util.Set;

/**
 * This class was part of the old NCBI Blast SOAP interface. see
 * http://www.ebi.ac.uk/Tools/webservices/services/ncbiblast#inputparams We continue to use it with the new
 * http://www.ebi.ac.uk/Tools/webservices/about/webservices
 */
public class InputParams implements java.io.Serializable {

    // allowed values for scores, from http://www.ebi.ac.uk/Tools/services/rest/ncbiblast/parameterdetails/scores
    private static final Set SCORES = new HashSet();
    static {
        InputParams.SCORES.add("5");
        InputParams.SCORES.add("10");
        InputParams.SCORES.add("20");
        InputParams.SCORES.add("50"); // default
        InputParams.SCORES.add("100");
        InputParams.SCORES.add("150");
        InputParams.SCORES.add("200");
        InputParams.SCORES.add("250");
        InputParams.SCORES.add("500");
        InputParams.SCORES.add("750");
        InputParams.SCORES.add("1000");
    }

    /* Maximum number of scores to output. */
    private String scores = "50";

    /* Maximum number of alignments to output. */
    private String numal = "50";

    /* NCBI BLAST program to use for search. */
    private java.lang.String program;

    /* Sequence database to search. */
    private java.lang.String database;

    /* scoring matrix. */
    private java.lang.String matrix;

    /* E-value threshold. 
     * Allowed values:1e-200
    ,1e-100
    ,1e-50
    ,1e-10
    ,1e-5
    ,1e-4
    ,1e-3
    ,1e-2
    ,1e-1
    ,1.0
    ,10 (default)
    ,100
    ,1000
     * */
    private String exp;

    /* Low complexity sequence filter for input sequence. */
    private java.lang.String filter;

    /* Alignment format for output. */
    private java.lang.Integer align;

    /* Gapped alignment. */
    private java.lang.String gapalign;

    /* Score for a match in nucleotide alignments. */
    private java.lang.Integer match;

    /* Score for a missmatch in nucleotide alignments. */
    private java.lang.Integer mismatch;

    /* Gap extension penalty. */
    private java.lang.Integer extendgap;

    /* Gap creation penalty. */
    private java.lang.Integer opengap;

    /* The amount a score must drop before extension of word hits
     * is halted. */
    private java.lang.Integer dropoff;

    /* Asynchronous submission. */
    @Deprecated
    // obsolete
    private java.lang.Boolean async;

    /* User e-mail address. */
    private java.lang.String email;

    public InputParams() {
        super();
    }

    /**
     * Gets the program value for this InputParams.
     * 
     * @return program * NCBI BLAST program to use for search.
     */
    public java.lang.String getProgram() {
        return this.program;
    }

    /**
     * Sets the program value for this InputParams.
     * 
     * @param program NCBI BLAST program to use for search.
     */
    public void setProgram(final java.lang.String program) {
        this.program = program;
    }

    /**
     * Gets the database value for this InputParams.
     * 
     * @return database * Sequence database to search.
     */
    public java.lang.String getDatabase() {
        return this.database;
    }

    /**
     * Sets the database value for this InputParams.
     * 
     * @param database Sequence database to search.
     */
    public void setDatabase(final java.lang.String database) {
        this.database = database;
    }

    /**
     * Gets the matrix value for this InputParams.
     * 
     * @return matrix * scoring matrix.
     */
    public java.lang.String getMatrix() {
        return this.matrix;
    }

    /**
     * Sets the matrix value for this InputParams.
     * 
     * @param matrix scoring matrix.
     */
    public void setMatrix(final java.lang.String matrix) {
        this.matrix = matrix;
    }

    /**
     * Gets the exp value for this InputParams.
     * 
     * @return exp * E-value threshold.
     */
    public String getExp() {
        return this.exp;
    }

    /**
     * Sets the exp value for this InputParams.
     * 
     * @param exp E-value threshold.
     */
    public void setExp(final String exp) {
        //TODO check value is on allowed list
        this.exp = exp;
    }

    /**
     * Gets the filter value for this InputParams.
     * 
     * @return filter * Low complexity sequence filter for input sequence.
     */
    public java.lang.String getFilter() {
        return this.filter;
    }

    /**
     * Sets the filter value for this InputParams.
     * 
     * @param filter Low complexity sequence filter for input sequence.
     */
    public void setFilter(final java.lang.String filter) {
        this.filter = filter;
    }

    /**
     * Gets the numal value for this InputParams.
     * 
     * @return numal * Maximum number of alignments to output.
     */
    public String getNumal() {
        return this.numal;
    }

    /**
     * Sets the numal value for this InputParams.
     * 
     * @param numal Maximum number of alignments to output.
     */
    public void setNumal(final String numal) {
        assert InputParams.SCORES.contains(numal);
        this.numal = numal;
    }

    /**
     * Gets the scores value for this InputParams.
     * 
     * @return scores * Maximum number of scores to output.
     */
    public String getScores() {
        return this.scores;
    }

    /**
     * Sets the scores value for this InputParams.
     * 
     * @param scores Maximum number of scores to output.
     */
    public void setScores(final String scores) {
        assert InputParams.SCORES.contains(scores);
        this.scores = scores;
    }

    /**
     * Gets the align value for this InputParams.
     * 
     * @return align * Alignment format for output.
     */
    public java.lang.Integer getAlign() {
        return this.align;
    }

    /**
     * Sets the align value for this InputParams.
     * 
     * @param align Alignment format for output.
     */
    public void setAlign(final java.lang.Integer align) {
        this.align = align;
    }

    /**
     * Gets the gapalign value for this InputParams.
     * 
     * @return gapalign * Gapped alignment.
     */
    public java.lang.String getGapalign() {
        return this.gapalign;
    }

    /**
     * Sets the gapalign value for this InputParams.
     * 
     * @param gapalign Gapped alignment.
     */
    public void setGapalign(final java.lang.String gapalign) {
        this.gapalign = gapalign;
    }

    /**
     * Gets the match value for this InputParams.
     * 
     * @return match * Score for a match in nucleotide alignments.
     */
    public java.lang.Integer getMatch() {
        return this.match;
    }

    /**
     * Sets the match value for this InputParams.
     * 
     * @param match Score for a match in nucleotide alignments.
     */
    public void setMatch(final java.lang.Integer match) {
        this.match = match;
    }

    /**
     * Gets the mismatch value for this InputParams.
     * 
     * @return mismatch * Score for a missmatch in nucleotide alignments.
     */
    public java.lang.Integer getMismatch() {
        return this.mismatch;
    }

    /**
     * Sets the mismatch value for this InputParams.
     * 
     * @param mismatch Score for a missmatch in nucleotide alignments.
     */
    public void setMismatch(final java.lang.Integer mismatch) {
        this.mismatch = mismatch;
    }

    /**
     * Gets the extendgap value for this InputParams.
     * 
     * @return extendgap * Gap extension penalty.
     */
    public java.lang.Integer getExtendgap() {
        return this.extendgap;
    }

    /**
     * Sets the extendgap value for this InputParams.
     * 
     * @param extendgap Gap extension penalty.
     */
    public void setExtendgap(final java.lang.Integer extendgap) {
        this.extendgap = extendgap;
    }

    /**
     * Gets the opengap value for this InputParams.
     * 
     * @return opengap * Gap creation penalty.
     */
    public java.lang.Integer getOpengap() {
        return this.opengap;
    }

    /**
     * Sets the opengap value for this InputParams.
     * 
     * @param opengap Gap creation penalty.
     */
    public void setOpengap(final java.lang.Integer opengap) {
        this.opengap = opengap;
    }

    /**
     * Gets the dropoff value for this InputParams.
     * 
     * @return dropoff * The amount a score must drop before extension of word hits is halted.
     */
    public java.lang.Integer getDropoff() {
        return this.dropoff;
    }

    /**
     * Sets the dropoff value for this InputParams.
     * 
     * @param dropoff The amount a score must drop before extension of word hits is halted.
     */
    public void setDropoff(final java.lang.Integer dropoff) {
        this.dropoff = dropoff;
    }

    /**
     * Gets the async value for this InputParams.
     * 
     * @return async * Asynchronous submission.
     */
    private java.lang.Boolean getAsync() {
        return this.async;
    }

    /**
     * Sets the async value for this InputParams.
     * 
     * @param async Asynchronous submission.
     */
    public void setAsync(final java.lang.Boolean async) {
        this.async = async;
    }

    /**
     * Gets the email value for this InputParams.
     * 
     * @return email * User e-mail address.
     */
    public java.lang.String getEmail() {
        return this.email;
    }

    /**
     * Sets the email value for this InputParams.
     * 
     * @param email User e-mail address.
     */
    public void setEmail(final java.lang.String email) {
        this.email = email;
    }

    private java.lang.Object __equalsCalc = null;

    @Override
    public synchronized boolean equals(final java.lang.Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof InputParams)) {
            return false;
        }
        final InputParams other = (InputParams) obj;
        if (this == obj) {
            return true;
        }
        if (this.__equalsCalc != null) {
            return (this.__equalsCalc == obj);
        }
        this.__equalsCalc = obj;
        boolean _equals;
        _equals =
            true
                && ((this.program == null && other.getProgram() == null) || (this.program != null && this.program
                    .equals(other.getProgram())))
                && ((this.database == null && other.getDatabase() == null) || (this.database != null && this.database
                    .equals(other.getDatabase())))
                && ((this.matrix == null && other.getMatrix() == null) || (this.matrix != null && this.matrix
                    .equals(other.getMatrix())))
                && ((this.exp == null && other.getExp() == null) || (this.exp != null && this.exp
                    .equals(other.getExp())))
                && ((this.filter == null && other.getFilter() == null) || (this.filter != null && this.filter
                    .equals(other.getFilter())))
                && ((this.numal == null && other.getNumal() == null) || (this.numal != null && this.numal
                    .equals(other.getNumal())))
                && ((this.scores == null && other.getScores() == null) || (this.scores != null && this.scores
                    .equals(other.getScores())))
                && ((this.align == null && other.getAlign() == null) || (this.align != null && this.align
                    .equals(other.getAlign())))
                && ((this.gapalign == null && other.getGapalign() == null) || (this.gapalign != null && this.gapalign
                    .equals(other.getGapalign())))
                && ((this.match == null && other.getMatch() == null) || (this.match != null && this.match
                    .equals(other.getMatch())))
                && ((this.mismatch == null && other.getMismatch() == null) || (this.mismatch != null && this.mismatch
                    .equals(other.getMismatch())))
                && ((this.extendgap == null && other.getExtendgap() == null) || (this.extendgap != null && this.extendgap
                    .equals(other.getExtendgap())))
                && ((this.opengap == null && other.getOpengap() == null) || (this.opengap != null && this.opengap
                    .equals(other.getOpengap())))
                && ((this.dropoff == null && other.getDropoff() == null) || (this.dropoff != null && this.dropoff
                    .equals(other.getDropoff())))
                && ((this.async == null && other.getAsync() == null) || (this.async != null && this.async
                    .equals(other.getAsync())))
                && ((this.email == null && other.getEmail() == null) || (this.email != null && this.email
                    .equals(other.getEmail())));
        this.__equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    private String sequenceType = "protein";

    public static final String MIN_HITS = "5";

    /**
     * EMBL String
     */
    public static final String EMBL = "em_rel"; // or "uniprotkb_trembl";

    //TODO EBI say use "em_rel", but this does not work

    /**
     * TARGETDB String
     */
    public static final String TARGETDB = "sgt";

    @Override
    public synchronized int hashCode() {
        if (this.__hashCodeCalc) {
            return 0;
        }
        this.__hashCodeCalc = true;
        int _hashCode = 1;
        if (this.getProgram() != null) {
            _hashCode += this.getProgram().hashCode();
        }
        if (this.getDatabase() != null) {
            _hashCode += this.getDatabase().hashCode();
        }
        if (this.getMatrix() != null) {
            _hashCode += this.getMatrix().hashCode();
        }
        if (this.getExp() != null) {
            _hashCode += this.getExp().hashCode();
        }
        if (this.getFilter() != null) {
            _hashCode += this.getFilter().hashCode();
        }
        if (this.getNumal() != null) {
            _hashCode += this.getNumal().hashCode();
        }
        if (this.getScores() != null) {
            _hashCode += this.getScores().hashCode();
        }
        if (this.getAlign() != null) {
            _hashCode += this.getAlign().hashCode();
        }
        if (this.getGapalign() != null) {
            _hashCode += this.getGapalign().hashCode();
        }
        if (this.getMatch() != null) {
            _hashCode += this.getMatch().hashCode();
        }
        if (this.getMismatch() != null) {
            _hashCode += this.getMismatch().hashCode();
        }
        if (this.getExtendgap() != null) {
            _hashCode += this.getExtendgap().hashCode();
        }
        if (this.getOpengap() != null) {
            _hashCode += this.getOpengap().hashCode();
        }
        if (this.getDropoff() != null) {
            _hashCode += this.getDropoff().hashCode();
        }
        if (this.getAsync() != null) {
            _hashCode += this.getAsync().hashCode();
        }
        if (this.getEmail() != null) {
            _hashCode += this.getEmail().hashCode();
        }
        this.__hashCodeCalc = false;
        return _hashCode;
    }

    /**
     * InputParams.getSequenceType
     * 
     * @return
     */
    public String getSequenceType() {
        return this.sequenceType;
    }

    /**
     * InputParams.setSequenceType
     * 
     * @param string
     */
    public void setSequenceType(final String string) {
        assert "dna".equals(string) || "protein".equals(string);
        this.sequenceType = string;
    }

}
