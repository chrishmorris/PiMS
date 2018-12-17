/*
 * SoftwareScore.java
 *
 * Created on 17 April 2007, 09:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

import java.util.Calendar;

/**
 * <p>After consultation with users, a user annotation or score of an image
 * relates to the Trial Drop and not just the single image whereas an
 * automated software annotation / score relates to a single image</p>
 * @author IMB
 */
public class SoftwareScore {
    private long id = -1L;
    private ScoreValue value = null;
    private Software softwareAnnotator = null;
    private Calendar date = null;
    private Image image = null;

    /**
     * Creates a new instance of a SoftwareScore
     */
    public SoftwareScore() {

    }

    /**
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public ScoreValue getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(ScoreValue value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    public Software getSoftwareAnnotator() {
        return softwareAnnotator;
    }

    /**
     *
     * @param softwareAnnotator
     */
    public void setSoftwareAnnotator(Software softwareAnnotator) {
        this.softwareAnnotator = softwareAnnotator;
    }

    /**
     *
     * @return
     */
    public Calendar getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(Calendar date) {
        this.date = date;
    }

    /**
     *
     * @return
     */
    public Image getImage() {
        return image;
    }

    /**
     *
     * @param image
     */
    public void setImage(Image image) {
        this.image = image;
    }


}
