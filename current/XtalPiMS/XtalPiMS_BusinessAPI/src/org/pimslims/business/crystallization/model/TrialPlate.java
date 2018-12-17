/**
 * 
 */
package org.pimslims.business.crystallization.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pimslims.business.Link;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.model.Person;

/**
 * <p>
 * Description a TrialPlate.
 * </p>
 * 
 * TODO implement hashCode and equals
 * 
 * @author Jon Diprose
 * 
 */
public class TrialPlate {

    /**
     * The numeric identifier of this TrialPlate. </p>
     */
    private Long id = null;

    /**
     * The String identifier of this TrialPlate. </p>
     */
    private String barcode = null;

    /**
     * <p>
     * Description of this TrialPlate.
     * </p>
     */
    private String description = null;

    /**
     * <p>
     * Date/time a which the physical plate was created.
     * </p>
     */
    private Calendar createDate = null;

    /**
     * <p>
     * Date/time a which the physical plate was destroyed.
     * </p>
     */
    private Calendar destroyDate = null;

    /**
     * The Wells on this PlateLayout
     */
    private final Map<WellPosition, TrialDrop> trialDrops = new LinkedHashMap<WellPosition, TrialDrop>();

    /**
     * The Wells on this PlateLayout
     */
    private final Map<WellPosition, ConditionQuantity> reservoirs =
        new LinkedHashMap<WellPosition, ConditionQuantity>();

    /**
     * The PlateType for which this PlateLayout is relevant
     */
    private PlateType plateType = null;

    /**
     * The location (imager) which this plate is currently in...
     */
    private Location location = null;

    /**
     * Plate experiment URL - provides a link back to the LIMS system that is used to create the trial plate
     */
    private Link plateExperimentLink = null;

    /**
     * The schedule for this plate of past and future inspections
     */
    private List<Schedule> scheduledInspections = null;

    public Construct getConstruct() {
        return construct;
    }

    public void setConstruct(final Construct construct) {
        this.construct = construct;
    }

    private Person operator = null;

    private Person owner = null;

    private Construct construct = null;

    private Group group = null;

    /**
     * @return Returns the group.
     */
    public Group getGroup() {
        return group;
    }

    /**
     * @param group The group to set.
     */
    public void setGroup(final Group group) {
        this.group = group;
    }

    public Person getOperator() {
        return operator;
    }

    public void setOperator(final Person operator) {
        this.operator = operator;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(final Person owner) {
        this.owner = owner;
    }

    /**
     * the screen used in this trialPlate
     */
    private Screen screen;

    /**
     * isAdditiveScreen boolean screen is only added to drop
     */
    private boolean isAdditiveScreen;

    /**
     * motherLiquor Condition Reservoir additive - often conditions from hit in initial screen
     */
    private Condition motherLiquor;

    /**
     * @return Returns the reservoir additive - often the conditions from a hit in the initial screen
     */
    public Condition getMotherLiquor() {
        return this.motherLiquor;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(final Screen screen) {
        this.screen = screen;
    }

    /**
     * Construct a TrialPlate for the specified PlateType
     * 
     * @param plateType
     */
    public TrialPlate(final PlateType plateType) {
        setPlateType(plateType);
    }

    /**
     * @return the plateType
     */
    public PlateType getPlateType() {
        return plateType;
    }

    /**
     * @param plateType the plateType to set
     */
    public void setPlateType(final PlateType plateType) {
        this.plateType = plateType;
    }

    /**
     * Get all the TrialDrops in list which order is the order of insert/add
     * 
     * @return
     */
    public List<TrialDrop> getTrialDrops() {
        final List<TrialDrop> trialDropList = new LinkedList<TrialDrop>();
        for (final WellPosition wp : trialDrops.keySet()) {
            trialDropList.add(trialDrops.get(wp));
        }
        return trialDropList;
    }

    /**
     * Get a TrialDrop by WellPosition
     * 
     * @param wellPosition
     * @return
     */
    public TrialDrop getTrialDrop(final WellPosition wellPosition) {
    	return trialDrops.get(wellPosition);
    }

    /**
     * Get a TrialDrop by row, column and subPosition
     * 
     * @param row
     * @param column
     * @param subPosition
     * @return
     */
    public TrialDrop getTrialDrop(final int row, final int column, final int subPosition) {
        return trialDrops.get(new WellPosition(row, column, subPosition));
    }

    /**
     * Get a TrialDrop by name
     * 
     * @param name
     * @return
     */
    public TrialDrop getTrialDrop(final String name) {
        return trialDrops.get(new WellPosition(name));
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return the barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * @param barcode the barcode to set
     */
    public void setBarcode(final String barcode) {
        this.barcode = barcode;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @return the createDate
     */
    public Calendar getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(final Calendar createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the destroyDate
     */
    public Calendar getDestroyDate() {
        return destroyDate;
    }

    /**
     * @param destroyDate the destroyDate to set
     */
    public void setDestroyDate(final Calendar destroyDate) {
        this.destroyDate = destroyDate;
    }

    /**
     * <p>
     * Populate all non-reservoir positions with new, empty TrialDrops. Any existing TrialDrops are discarded.
     * </p>
     */

    public void buildAllTrialDrops() {
        final PlateType type = getPlateType();
        assert null != type : "Plate type not known";

        trialDrops.clear();

        for (int row = 1; row <= type.getRows(); row++) {
            for (int col = 1; col <= type.getColumns(); col++) {
                for (int sub = 1; sub <= type.getSubPositions(); sub++) {
                    if (sub != type.getReservoir()) {
                        final WellPosition wp = new WellPosition(row, col, sub);
                        final TrialDrop td = new TrialDrop();
                        td.setWellPosition(wp);
                        td.setPlate(this);
                        trialDrops.put(wp, td);
                    }
                }
            }
        }

    }

    /**
     * <p>
     * Add the specified TrialDrop to this TrialPlate.
     * </p>
     * 
     * @param trialDrop - the TrialDrop to add to this TrialPlate
     * @return The TrialDrop previously at the new TrialDrop's WellPosition, if any
     */
    public TrialDrop addTrialDrop(final TrialDrop trialDrop) {
        return this.trialDrops.put(trialDrop.getWellPosition(), trialDrop);
    }

    /**
     * <p>
     * Remove and return the TrialDrop at the specified WellPosition in this TrialPlate.
     * </p>
     * 
     * @param wellPosition - the WellPosition from which to remove the TrialDrop
     * @return The TrialDrop at the specified WellPosition in this TrialPlate
     */
    public TrialDrop removeTrialDrop(final WellPosition wellPosition) {
        return this.trialDrops.remove(wellPosition);
    }

    /**
     * <p>
     * Get the specified ConditionQuantity in this TrialPlate at the specified WellPosition. Note that the
     * WellPosition's subposition is ignored and the reservoir subposition from the PlateType is used instead.
     * </p>
     * TODO get this under test
     * 
     * @param wellPosition - the WellPosition at which to add to this ConditionQuantity
     * @param reservoir - the ConditionQuantity to add to this TrialPlate
     * @return The ConditionQuantity previously at the WellPosition, if any
     */
    public ConditionQuantity getReservoir(final WellPosition wellPosition) {
        final WellPosition wp =
            new WellPosition(wellPosition.getRow(), wellPosition.getColumn(), getPlateType().getReservoir());
        return this.reservoirs.get(wp);
    }

    /**
     * <p>
     * Add the specified ConditionQuantity to this TrialPlate at the specified WellPosition. Note that the
     * WellPosition's subposition is ignored and the reservoir subposition from the PlateType is used instead.
     * </p>
     * 
     * @param wellPosition - the WellPosition at which to add to this ConditionQuantity
     * @param reservoir - the ConditionQuantity to add to this TrialPlate
     * @return The ConditionQuantity previously at the WellPosition, if any
     */
    public ConditionQuantity addReservoir(final WellPosition wellPosition, final ConditionQuantity reservoir) {
        final WellPosition wp =
            new WellPosition(wellPosition.getRow(), wellPosition.getColumn(), getPlateType().getReservoir());
        return this.reservoirs.put(wp, reservoir);
    }

    /**
     * <p>
     * Remove and return the ConditionQuantity in this TrialPlate from the specified WellPosition. Note that
     * the WellPosition's subposition is ignored and the reservoir subposition from the PlateType is used
     * instead.
     * </p>
     * 
     * @param wellPosition - the WellPosition from which to remove the TrialDrop
     * @return The TrialDrop at the specified WellPosition in this TrialPlate
     */
    public ConditionQuantity removeReservoir(final WellPosition wellPosition) {
        final WellPosition wp =
            new WellPosition(wellPosition.getRow(), wellPosition.getColumn(), getPlateType().getReservoir());
        return this.reservoirs.remove(wp);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    public Link getPlateExperimentLink() {
        return plateExperimentLink;
    }

    public void setPlateExperimentLink(final Link plateExperimentLink) {
        this.plateExperimentLink = plateExperimentLink;
    }

    public List<Schedule> getScheduledInspections() {
        if (this.scheduledInspections == null) {
            this.scheduledInspections = new ArrayList<Schedule>();
        }
        return scheduledInspections;
    }

    public void setScheduledInspections(final List<Schedule> scheduledInspections) {
        this.scheduledInspections = scheduledInspections;
    }

    public void addScheduledInspection(final Schedule inspection) {
        if (this.scheduledInspections == null) {
            this.scheduledInspections = new ArrayList<Schedule>();
        }
        this.scheduledInspections.add(inspection);
    }

    /**
     * TrialPlate.setAdditiveScreen
     * 
     * @param isAdditiveScreen true if screen is added only to drop, false if screen is also added to wells
     */
    public void setAdditiveScreen(boolean isAdditiveScreen) {
        this.isAdditiveScreen = isAdditiveScreen;
    }

    /**
     * TrialPlate.isAdditiveScreen
     * 
     * @return true if screen is added only to drop, false if screen is also added to wells
     * 
     *         public boolean isAdditiveScreen() { return isAdditiveScreen; }
     */

    public void setMotherLiquor(Condition additive) {
        this.motherLiquor = additive;
    }

    /**
     * TrialPlate.isAdditiveScreen
     * 
     * @return
     */
    public boolean isAdditiveScreen() {
        return this.isAdditiveScreen;
    }
}
