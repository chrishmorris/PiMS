/*
 * ScoringScheme.java Created on 03 May 2007, 18:10 To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

import java.util.ArrayList;
import java.util.List;

import org.pimslims.business.XtalObject;
import org.pimslims.business.exception.BusinessException;

/**
 * <p>
 * A Scoring scheme is a predefined set of scores that can be allocated.
 * </p>
 * <p>
 * When a user wants to score an image, they will have to choose a scoring scheme and then the score value
 * from that scoring scheme.
 * </p>
 * 
 * @author IMB
 */
public class ScoringScheme extends XtalObject {

    private String name = "";

    private String version = "";

    private String description = "";

    private List<ScoreValue> scores = new ArrayList<ScoreValue>();

    /**
     * Creates a new instance of a Scoring Scheme
     */
    public ScoringScheme() {

    }

    /**
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     */
    public String getVersion() {
        return version;
    }

    /**
     * 
     * @param version
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     */
    public List<ScoreValue> getScores() {
        return scores;
    }

    /**
     * 
     * @param scores
     */
    public void setScores(final List<ScoreValue> scores) {
        this.scores = scores;
    }

    /**
     * 
     * @param score
     */
    public void addScore(final ScoreValue score) {
        if (this.scores == null) {
            this.scores = new ArrayList<ScoreValue>();
        }
        this.scores.add(score);
    }

    public void removeScore(final ScoreValue score) throws BusinessException {
        if (this.scores == null) {
            throw new BusinessException("Unable to remove score from an empty list");
        }
        if (this.scores.contains(score)) {
            this.scores.remove(score);
        } else {
            throw new BusinessException("This score is not contained within this scoring scheme!");
        }
    }
}
