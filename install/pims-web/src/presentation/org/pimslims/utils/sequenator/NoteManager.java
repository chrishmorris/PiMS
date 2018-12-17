/**
 * current-pims-web org.pimslims.utils.sequenator NoteManager.java
 * 
 * @author Petr aka pvt43
 * @date 25 Apr 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Petr The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils.sequenator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.model.core.Note;
import org.pimslims.model.experiment.Experiment;

/**
 * NoteManager
 * 
 */
public class NoteManager {

    public static final String reprocessTag = "Reprocess";

    public static final String reinjectTag = "Reinject";

    public static final String privateNoteName = "SequencingAdminNote";

    public static final String publicNoteName = "MessageForUser";

    Experiment experiment;

    /**
     * Constructor for NoteManager
     */
    public NoteManager(final Experiment experiment) {
        assert experiment != null;
        this.experiment = experiment;
    }

    /**
     * @web function NoteManager.getNew
     * @param experiment
     * @return
     */
    public static NoteManager getNew(final Experiment experiment) {
        return new NoteManager(experiment);
    }

    public List<Note> getPrivateNotes() {
        return this.getNotes(NoteManager.privateNoteName);
    }

    public boolean isMarkedforReprocessing() {
        for (final Note tag : this.getAllTagNotes()) {
            if (tag.getName().equalsIgnoreCase(NoteManager.reprocessTag)) {
                return true;
            }
        }
        return false;
    }

    public List<Note> getPublicNotes() {
        return this.getNotes(NoteManager.publicNoteName);
    }

    private List<Note> getNotes(final String noteType) {
        assert !Util.isEmpty(noteType);
        final List<Note> notes = new ArrayList<Note>();
        for (final Note n : this.experiment.getNotes()) {
            if (n.getName().equals(noteType)) {
                notes.add(n);
            }
        }
        return notes;
    }

    public List<Note> getAllNotes() {
        final ArrayList<Note> notes = new ArrayList<Note>();
        notes.addAll(this.getPrivateNotes());
        notes.addAll(this.getPublicNotes());
        Collections.sort(notes, new NoteDateComp());

        return notes;
    }

    /**
     * There may be only one note of "Tag" type for any given Tag and experiment
     * 
     * NoteManager.getTagNote
     * 
     * @param noteType
     * @return
     */
    private Note getTagNote(final String noteType) {
        assert !Util.isEmpty(noteType);
        for (final Note n : this.experiment.getNotes()) {
            if (n.getName().equals(noteType)) {
                return n;
            }
        }
        return null;
    }

    Note getReinjectTag() {
        return this.getTagNote(NoteManager.reinjectTag);
    }

    Note getReprocessTag() {
        return this.getTagNote(NoteManager.reprocessTag);
    }

    //TODO make it not tag specific 
    public List<Note> getAllTagNotes() {
        final ArrayList<Note> notes = new ArrayList<Note>();
        Note tag = this.getReinjectTag();
        if (tag != null) {
            notes.add(tag);
        }
        tag = this.getReprocessTag();
        if (tag != null) {
            notes.add(tag);
        }
        Collections.sort(notes, new NoteDateComp());

        return notes;
    }

    static class NoteDateComp implements Comparator<Note> {
        public int compare(final Note o1, final Note o2) {
            return o1.getDate().compareTo(o2.getDate());
        }
    }

    private void addTag(final WritableVersion rw, final String tagType) throws ConstraintException {
        assert !Util.isEmpty(tagType);
        final Note n = new Note(rw, this.experiment);
        n.setName(tagType);
        n.setDate(Calendar.getInstance());
        n.setDetails("***$#% THIS IS A TAG! DO NOT EDIT! %#$***");
        n.setCreator(rw.getCurrentUser());
    }

    private void addNote(final WritableVersion rw, final String tagType, final String details)
        throws ConstraintException {
        assert !Util.isEmpty(tagType);
        assert !Util.isEmpty(details);
        final Note n = new Note(rw, this.experiment);
        n.setName(tagType);
        n.setDetails(details);
        n.setDate(Calendar.getInstance());
        n.setCreator(rw.getCurrentUser());
    }

    public void addReinjectTag(final WritableVersion rw) throws ConstraintException {
        final Note rtag = this.getReinjectTag();
        // Only add a tag only if it has not been added yet
        if (rtag == null) {
            this.addTag(rw, NoteManager.reinjectTag);
        } else {
            System.out.println("WARNING: Attempt to set Tag " + NoteManager.reinjectTag + " to Experiment "
                + this.experiment + " twice! ");
            System.out.println("Each Tag can only be added once!");
        }
    }

    public void addReprocessTag(final WritableVersion rw) throws ConstraintException {
        final Note rtag = this.getReprocessTag();
        // Only add a tag only if it has not been added yet
        if (rtag == null) {
            this.addTag(rw, NoteManager.reprocessTag);
        } else {
            System.out.println("WARNING: Attempt to set Tag " + NoteManager.reprocessTag + " to Experiment "
                + this.experiment + " twice! ");
            System.out.println("Each Tag can only be added once!");
        }
    }

    public void addPrivateNote(final WritableVersion rw, final String details) throws ConstraintException {
        if (!Util.isEmpty(details)) {
            this.addNote(rw, NoteManager.privateNoteName, details);
        }
    }

    public void addPublicNote(final WritableVersion rw, final String details) throws ConstraintException {
        if (!Util.isEmpty(details)) {
            this.addNote(rw, NoteManager.publicNoteName, details);
        }
    }

    public static boolean isTag(final String tagName) {
        assert !Util.isEmpty(tagName);
        return tagName.trim().equalsIgnoreCase(NoteManager.reinjectTag)
            || tagName.trim().equalsIgnoreCase(NoteManager.reprocessTag);
    }

    public static boolean isPublic(final String tagName) {
        assert !Util.isEmpty(tagName);
        return tagName.trim().equalsIgnoreCase(NoteManager.publicNoteName);
    }

    public static String addHyperlinkToOrder(final String actionURL, final String details) {
        assert !Util.isEmpty(actionURL);

        if (Util.isEmpty(details)) {
            return "";
        }

        // Extract a sequnce order number from a text
        // ksjg SO_12 ksjdf SO_23 
        final String regex = "SO_\\d*";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher m = pattern.matcher(details);
        boolean found = false;
        final StringBuffer updatedDetails = new StringBuffer();
        while (m.find()) {
            found = true;
            final String match = m.group();
            final String url = " <a href='" + actionURL + match + "'" + ">" + match + "</a> ";
            m.appendReplacement(updatedDetails, url);
        }
        if (found) {
            return updatedDetails.toString();
        } else {
            return details;
        }
    }

    /*
    //Marker to be able to find awaiting results runs will be recorded as ExperimentGroup.details not a note!
    public static final String awaitingResultsRunTag = "***#AwaitingResultsRun#*** DO NOT EDIT OR REMOVE!!!";

    enum TagType {
        PlannedRunsTag, AwaitingResultsRunTag, CompletedRunTag
    }

    final static String completedTag = "CompletedRunTag";

    public static void addCompletedNote(final WritableVersion rw, final ExperimentGroup eg)
        throws ConstraintException {
        NoteManager.addTag(rw, eg, TagType.CompletedRunTag);
    }

    private static void addTag(final WritableVersion rw, final ExperimentGroup eg, final TagType type)
        throws ConstraintException {
        final Note n = new Note(rw, eg);
        switch (type) {
            case AwaitingResultsRunTag:
                n.setName(NoteManager.TagType.AwaitingResultsRunTag.toString());
                break;
            case CompletedRunTag:
                n.setName(NoteManager.TagType.CompletedRunTag.toString());
                break;
            case PlannedRunsTag:
                n.setName(NoteManager.TagType.PlannedRunsTag.toString());
                break;
            default:
                throw new AssertionError("Ingorrect tag Type: " + type);
        }
        n.setDate(Calendar.getInstance());
        n.setDetails("***$#% THIS IS A TAG! DO NOT EDIT! %#$***");
        n.setCreator(rw.getCurrentUser());
    }

    public static void addPlannedRunTag(final WritableVersion rw, final ExperimentGroup eg)
        throws ConstraintException {
        NoteManager.addTag(rw, eg, TagType.PlannedRunsTag);
    }

    public static void removePlannedRunTag(final WritableVersion rw, final ExperimentGroup eg)
        throws ConstraintException, AccessException {
        NoteManager.removeTag(eg.getNotes(), TagType.PlannedRunsTag);
    }

    public static void addAwaitingResultsRunTag(final WritableVersion rw, final ExperimentGroup eg)
        throws ConstraintException {
        NoteManager.addTag(rw, eg, TagType.AwaitingResultsRunTag);
    }

    public static void removeAwaitingResultsTag(final ExperimentGroup eg) throws AccessException,
        ConstraintException {
        final List<Note> notes = eg.getNotes();
        NoteManager.removeTag(notes, TagType.AwaitingResultsRunTag);
    }

    private static void removeTag(final List<Note> notes, final TagType type) throws AccessException,
        ConstraintException {
        assert type != null;
        for (final Note note : notes) {
            if (note.getName().equalsIgnoreCase(type.toString())) {
                note.delete();
            }
        }
    }
    */
}
