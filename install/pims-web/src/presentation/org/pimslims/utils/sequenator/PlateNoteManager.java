/**
 * current-pims-web org.pimslims.utils.sequenator NoteManager.java
 * 
 * @author Petr aka pvt43
 * @date June 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Petr The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils.sequenator;

import java.util.Calendar;
import java.util.List;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.Note;
import org.pimslims.model.experiment.ExperimentGroup;

/**
 * PlateNoteManager
 * 
 */
public class PlateNoteManager {

    ExperimentGroup eg;

    /**
     * Constructor for NoteManager
     */
    public PlateNoteManager(final ExperimentGroup eg) {
        assert eg != null;
        this.eg = eg;
    }

    List<Note> getNotes() {
        return this.eg.getNotes();
    }

    public boolean isPlateCompletionConfirmed() {
        return this.hasTag(TagType.CompletedRunTag);
    }

    boolean hasTag(final TagType type) {
        for (final Note note : this.getNotes()) {
            if (note.getName().equals(type.toString())) {
                return true;
            }
        }
        return false;
    }

    enum TagType {
        PlannedRunsTag, AwaitingResultsRunTag, CompletedRunTag
    }

    public void addCompletedNote(final WritableVersion rw) throws ConstraintException, AccessException {
        this.addTag(rw, TagType.CompletedRunTag);
    }

    private void addTag(final WritableVersion rw, final TagType type) throws ConstraintException,
        AccessException {
        assert type != null;
        assert !this.hasTag(type);
        this.removeOtherTags(type);
        final Note n = new Note(rw, this.eg);
        switch (type) {
            case AwaitingResultsRunTag:
                n.setName(PlateNoteManager.TagType.AwaitingResultsRunTag.toString());
                break;
            case CompletedRunTag:
                n.setName(PlateNoteManager.TagType.CompletedRunTag.toString());
                break;
            case PlannedRunsTag:
                n.setName(PlateNoteManager.TagType.PlannedRunsTag.toString());
                break;
            default:
                throw new AssertionError("Ingorrect tag Type: " + type);
        }
        n.setDate(Calendar.getInstance());
        n.setDetails("***$#% THIS IS A TAG! DO NOT EDIT! %#$***");
        n.setCreator(rw.getCurrentUser());
    }

    void removeOtherTags(final TagType type) throws AccessException, ConstraintException {
        final String tag = type.toString();
        for (final Note note : this.getNotes()) {
            final String nname = note.getName();
            if (nname.equals(tag)) {
                continue;
            }
            if (nname.equals(TagType.AwaitingResultsRunTag.toString())) {
                note.delete();
            }
            if (nname.equals(TagType.PlannedRunsTag.toString())) {
                note.delete();
            }
            if (nname.equals(TagType.CompletedRunTag.toString())) {
                note.delete();
            }
        }
    }

    public void addPlannedRunTag(final WritableVersion rw) throws ConstraintException, AccessException {
        this.addTag(rw, TagType.PlannedRunsTag);
    }

    public void addAwaitingResultsRunTag(final WritableVersion rw) throws ConstraintException,
        AccessException {
        this.addTag(rw, TagType.AwaitingResultsRunTag);
    }

    /*
    public void removePlannedRunTag(final WritableVersion rw, final ExperimentGroup eg)
        throws ConstraintException, AccessException {
        this.removeTag(TagType.PlannedRunsTag);
    }

    public void removeAwaitingResultsTag() throws AccessException, ConstraintException {
        this.removeTag(TagType.AwaitingResultsRunTag);
    }

    private void removeTag(final TagType type) throws AccessException, ConstraintException {
        assert type != null;
        for (final Note note : this.getNotes()) {
            if (note.getName().equalsIgnoreCase(type.toString())) {
                note.delete();
            }
        }
    }
    */

}
