/*
 * ScoreValue.java Created on 08 May 2007, 16:20 To change this template, choose Tools | Template Manager and
 * open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

import java.awt.Color;

import org.pimslims.business.XtalObject;

/**
 * <p>
 * A Score Value is a way of storing a score and how it is represented. When a user creates their own scoring
 * scheme, the scores will be stored here and referenced whenever that score is allocated
 * </p>
 * 
 * @author IMB
 */
public class ScoreValue extends XtalObject implements java.lang.Comparable<ScoreValue> {

    private String description = "";

    private int value = 0;

    private Color colour = Color.WHITE;

    private ScoringScheme scoringScheme = null;

    /**
     * @return Returns the scoringScheme.
     */
    public ScoringScheme getScoringScheme() {
        return scoringScheme;
    }

    /**
     * @param scoringScheme The scoringScheme to set.
     */
    public void setScoringScheme(final ScoringScheme scoringScheme) {
        this.scoringScheme = scoringScheme;
    }

    /**
     * Creates a new instance of a ScoreValue
     */
    public ScoreValue() {

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
    public int getValue() {
        return value;
    }

    /**
     * 
     * @param value
     */
    public void setValue(final int value) {
        this.value = value;
    }

    /**
     * 
     * @return
     */
    public Color getColour() {
        return colour;
    }

    public int getIntColour() {
        return colour.getRGB();
    }

    /**
     * 
     * @param colour
     */
    public void setColour(final Color colour) {
        this.colour = colour;
    }

    public int compareTo(final ScoreValue o) {
        return (new Integer(this.value)).compareTo(new Integer(o.value));
    }

    public void setIntColour(final int intColor) {
        this.colour = new Color(intColor);

    }
}
