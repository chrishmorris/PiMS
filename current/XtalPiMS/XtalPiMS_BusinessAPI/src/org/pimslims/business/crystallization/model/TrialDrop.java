/**
 * 
 */
package org.pimslims.business.crystallization.model;

import java.util.ArrayList;
import java.util.List;

import org.pimslims.business.Link;

/**
 * @author Jon Diprose
 * 
 */
public class TrialDrop {

    public TrialDrop() {
        super();
    }

    /**
     * The plate on which this TrialDrop is located
     */
    private TrialPlate plate = null;

    /**
     * The wellPosition of this TrialDrop within plate
     */
    private WellPosition wellPosition = null;

    /**
     * The list of SampleQuantity in this TrialDrop
     */
    private final List<SampleQuantity> samples = new ArrayList<SampleQuantity>();

    /**
     * The well additive in this TrialDrop
     */
    private ConditionQuantity motherLiquor = null;

    /**
     * The reservoir in this TrialDrop
     */
    private ConditionQuantity reservoir = null;

    /**
     * drop experiment - links back to the core LIMS view of the experiment that is being carried out in this
     * drop
     */
    private Link dropExperimentLink = null;

    /**
     * a description of this experiment
     */
    private String description = null;

    /**
     * the id of sample
     */
    private Long id = null;

    /**
     * the name of sample
     */
    private String name = null;

    /**
     * additiveScreen boolean Screen is added only to drop, not to reservoir
     */
    private boolean additiveScreen;

    /**
     * @return the plate
     */
    public TrialPlate getPlate() {
        return plate;
    }

    /**
     * @param plate the plate to set
     */
    public void setPlate(TrialPlate plate) {
        this.plate = plate;
    }

    /**
     * @return the wellPosition
     */
    public WellPosition getWellPosition() {
        return wellPosition;
    }

    /**
     * @param position the wellPosition to set
     */
    public void setWellPosition(WellPosition position) {
        this.wellPosition = position;
    }

    /**
     * @return the well additive TODO get this under test
     */
    public ConditionQuantity getMotherLiquor() {
        return motherLiquor;
    }

    /**
     * @param condition the well additive to set
     */
    public void setMotherLiquor(ConditionQuantity condition) {
        this.motherLiquor = condition;
    }

    /**
     * @return the protein samples
     */
    public List<SampleQuantity> getSamples() {
        return samples;
    }

    /**
     * Add a protein sample
     * 
     * @param sample - the sample to add
     */
    public void addSample(SampleQuantity sample) {
        samples.add(sample);
    }

    public void clearSamples() {
        samples.clear();
    }

    /**
     * @return the reservoir
     */
    //TODO get this under test
    public ConditionQuantity getReservoir() {
        return reservoir;
    }

    /**
     * @param reservoir the reservoir to set 
     */
    public void setReservoir(ConditionQuantity reservoir) {
        this.reservoir = reservoir;
    }

    public Link getDropExperimentLink() {
        return dropExperimentLink;
    }

    public void setDropExperimentLink(Link dropExperimentLink) {
        this.dropExperimentLink = dropExperimentLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @return Returns the name In PiMS, this is used as the sample name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the additiveScreen.
     */
    public boolean isAdditiveScreen() {
        return this.additiveScreen;
    }

    /**
     * @param additiveScreen The additiveScreen to set.
     */
    public void setAdditiveScreen(boolean additiveScreen) {
        this.additiveScreen = additiveScreen;
    }

}
