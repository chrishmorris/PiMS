package org.pimslims.business.crystallization.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author ian
 * 
 */
public class TrialDropView implements java.lang.Comparable<TrialDropView> {
    public static final String PROP_BARCODE = "barcode";

    public static final String PROP_WELL = "well";

    public static final String PROP_SYNCHROTRON = "synchrotron";

    public static final String PROP_IMAGES = "images";

    public static final String PROP_MICROSCOPE_IMAGES = "microscopeImages";

    public static final String PROP_HUMAN_SCORES = "humanScores";

    public static final String PROP_SOFTWARE_SCORES = "softwareScores";

    public static final String PROP_SAMPLES = "samples";

    public static final String PROP_CONDITION = "condition";

    public static final String PROP_OWNER = "owner";

    public static final String PROP_GROUP = "group";

    private String barcode;

    private String well;

    private Boolean synchrotron = false;

    private String owner;

    private String group;

    private ConditionView condition;

    private List<ImageView> images = new ArrayList<ImageView>();

    private List<ImageView> microscopeImages = new ArrayList<ImageView>();

    private List<SampleQuantityView> samples = new ArrayList<SampleQuantityView>();

    private List<ScoreView> humanScores = new ArrayList<ScoreView>();

    private List<ScoreView> softwareScores = new ArrayList<ScoreView>();

    /**
     * 
     */
    public TrialDropView() {
        super();
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(final String barcode) {
        this.barcode = barcode;
    }

    public String getWell() {
        return well;
    }

    public void setWell(final String well) {
        this.well = well;
    }

    public Boolean getSynchrotron() {
        return synchrotron;
    }

    public void setSynchrotron(final Boolean synchrotron) {
        this.synchrotron = synchrotron;
    }

    /**
     * @return a list of images from the inspection, e.g. visible + UV
     */
    public List<ImageView> getImages() {
        return images;
    }

    public void setImages(final List<ImageView> images) {
        this.images = images;
    }

    public void addImage(final ImageView image) {
        if (image != null) {
            this.images.add(image);
        }
    }

    public List<ImageView> getMicroscopeImages() {
        return microscopeImages;
    }

    public void setMicroscopeImages(final List<ImageView> microscopeImages) {
        this.microscopeImages = microscopeImages;
    }

    public void addMicroscopeImage(final ImageView image) {
        if (image != null) {
            this.microscopeImages.add(image);
        }
    }

    public List<SampleQuantityView> getSamples() {
        return samples;
    }

    public void setSamples(final List<SampleQuantityView> samples) {
        this.samples = samples;
    }

    public void addSample(final SampleQuantityView sample) {
        if (sample != null) {
            this.samples.add(sample);
        }
    }

    public ConditionView getCondition() {
        return condition;
    }

    public void setCondition(final ConditionView condition) {
        this.condition = condition;
    }

    public List<ScoreView> getHumanScores() {
        return humanScores;
    }

    public void setHumanScores(final List<ScoreView> humanScores) {
        this.humanScores = humanScores;
    }

    public void addHumanScore(final ScoreView score) {
        if (score != null) {
            this.humanScores.add(score);
        }
    }

    public List<ScoreView> getSoftwareScores() {
        return softwareScores;
    }

    public void setSoftwareScores(final List<ScoreView> softwareScores) {
        this.softwareScores = softwareScores;
    }

    public void addSoftwareScore(final ScoreView score) {
        if (score != null) {
            this.softwareScores.add(score);
        }
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(final String group) {
        this.group = group;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
        result = prime * result + ((condition == null) ? 0 : condition.hashCode());
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + ((humanScores == null) ? 0 : humanScores.hashCode());
        result = prime * result + ((images == null) ? 0 : images.hashCode());
        result = prime * result + ((microscopeImages == null) ? 0 : microscopeImages.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result + ((samples == null) ? 0 : samples.hashCode());
        result = prime * result + ((softwareScores == null) ? 0 : softwareScores.hashCode());
        result = prime * result + ((synchrotron == null) ? 0 : synchrotron.hashCode());
        result = prime * result + ((well == null) ? 0 : well.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TrialDropView other = (TrialDropView) obj;
        if (this.barcode != other.barcode && (this.barcode == null || !this.barcode.equals(other.barcode))) {
            return false;
        }
        if (this.well != other.well && (this.well == null || !this.well.equals(other.well))) {
            return false;
        }
        if (this.synchrotron != other.synchrotron
            && (this.synchrotron == null || !this.synchrotron.equals(other.synchrotron))) {
            return false;
        }
        if (this.owner != other.owner && (this.owner == null || !this.owner.equals(other.owner))) {
            return false;
        }
        if (this.group != other.group && (this.group == null || !this.group.equals(other.group))) {
            return false;
        }
        return true;
    }

    //    @Override
    //    public boolean equals(Object obj) {
    //        if (obj == null) {
    //            return false;
    //        }
    //        if (getClass() != obj.getClass()) {
    //            return false;
    //        }
    //        final TrialDropView other = (TrialDropView) obj;
    //        if (!this.barcode.equals(other.barcode) && (this.barcode == null || !this.barcode.equals(other.barcode))) {
    //            return false;
    //        }
    //        if (!this.well.equals(other.well) && (this.well == null || !this.well.equals(other.well))) {
    //            return false;
    //        }
    //        if (this.synchrotron != other.synchrotron && (this.synchrotron == null || !this.synchrotron.equals(other.synchrotron))) {
    //            return false;
    //        }
    //        if (!this.owner.equals(other.owner) && (this.owner == null || !this.owner.equals(other.owner))) {
    //            return false;
    //        }
    //        if (!this.group.equals(other.group) && (this.group == null || !this.group.equals(other.group))) {
    //            return false;
    //        }
    //        return true;
    //    }

    public JSONObject toJSON() {
        final JSONObject obj = new JSONObject();

        obj.put(PROP_BARCODE, this.getBarcode());
        obj.put(PROP_WELL, this.getWell());
        obj.put(PROP_SYNCHROTRON, this.getSynchrotron());
        obj.put(PROP_OWNER, this.getOwner());
        obj.put(PROP_GROUP, this.getGroup());
        if (this.getImages() != null) {
            final JSONArray imagesArray = new JSONArray();
            final Iterator<ImageView> imageIt = this.getImages().iterator();
            while (imageIt.hasNext()) {
                final ImageView image = imageIt.next();
                imagesArray.add(image.toJSON());
            }

            obj.put(PROP_IMAGES, imagesArray);
        }
        if (this.getMicroscopeImages() != null) {
            final JSONArray microscopeImagesArray = new JSONArray();
            final Iterator<ImageView> imageIt = this.getMicroscopeImages().iterator();
            while (imageIt.hasNext()) {
                final ImageView image = imageIt.next();
                microscopeImagesArray.add(image.toJSON());
            }

            obj.put(PROP_MICROSCOPE_IMAGES, microscopeImagesArray);
        }
        if (this.getHumanScores() != null) {
            final JSONArray humanScoresArray = new JSONArray();
            final Iterator<ScoreView> scoreIt = this.getHumanScores().iterator();
            while (scoreIt.hasNext()) {
                final ScoreView score = scoreIt.next();
                humanScoresArray.add(score.toJSON());
            }
            obj.put(PROP_HUMAN_SCORES, humanScoresArray);
        }
        if (this.getSoftwareScores() != null) {
            final JSONArray softwareScoresArray = new JSONArray();
            final Iterator<ScoreView> scoreIt = this.getSoftwareScores().iterator();
            while (scoreIt.hasNext()) {
                final ScoreView score = scoreIt.next();
                softwareScoresArray.add(score.toJSON());
            }
            obj.put(PROP_SOFTWARE_SCORES, softwareScoresArray);
        }
        if (this.getSamples() != null) {
            final JSONArray samplesArray = new JSONArray();
            final Iterator<SampleQuantityView> sampleIt = this.getSamples().iterator();
            while (sampleIt.hasNext()) {
                final SampleQuantityView sample = sampleIt.next();
                samplesArray.add(sample.toJSON());
            }
            obj.put(PROP_SAMPLES, samplesArray);
        }
        if (this.getCondition() != null) {
            obj.put(PROP_CONDITION, this.getCondition().toJSON());
        }
        return obj;
    }

    public int compareTo(final TrialDropView o) {
        return this.well.compareTo(o.well);
    }
}
