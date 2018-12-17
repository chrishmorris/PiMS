/**
 * V2_3-pims-web org.pimslims.presentation NoteBean.java
 * 
 * @author cm65
 * @date 3 Sep 2008
 * 
 * Protein Information Management System
 * @version: 2.2
 * 
 * Copyright (c) 2008 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation;

import java.util.Calendar;
import java.util.List;

import org.pimslims.model.core.Note;

/**
 * NoteBean
 * 
 */
public class NoteBean extends ModelObjectShortBean {

    private final List<ModelObjectShortBean> seeAlso;

    private final Calendar date;

    private final ModelObjectShortBean author;

    private final String details;

    /**
     * Constructor for NoteBean
     * 
     * @param note
     */
    public NoteBean(final Note note) {
        super(note);
        this.details = note.getDetails();
        if (null == note.getAuthor()) {
            this.author = null;
        } else {
            this.author = new ModelObjectShortBean(note.getAuthor());
        }
        this.seeAlso = ModelObjectShortBean.getBeans(note.getRelatedEntries());
        this.date = note.getDate();
    }

    /**
     * NoteBean.getDescription
     * 
     * @return
     */
    public String getDetails() {
        return this.details;
    }

    /**
     * NoteBean.getPerson
     * 
     * @return
     */
    public ModelObjectShortBean getPerson() {
        return this.author;
    }

    /**
     * NoteBean.getDate
     * 
     * @return
     */
    public Calendar getDate() {
        return this.date;
    }

    /**
     * NoteBean.getSeeAlso
     * 
     * @return
     */
    public List<ModelObjectShortBean> getSeeAlso() {
        return this.seeAlso;
    }

}
