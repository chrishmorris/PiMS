package org.pimslims.presentation.plateExperiment;

import java.util.Calendar;

import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.people.Person;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.servlet.utils.ValueFormatter;

public class PlateBean extends ModelObjectShortBean {

    @Deprecated
    private final ExperimentGroup expGroup;

    private String expGroupHook;

    private String expGroupName;

    private String expTypeHook;

    private String expTypeName;

    private String holderTypeHook;

    private String holderTypeName;

    private final String details;

    private final String status;

    private final Calendar startDate;

    private final Calendar endDate;

    @Deprecated
    private final Person creator;

    private Protocol protocol;

    //private Holder holder;

    public PlateBean(final ExperimentGroup group) {
        super(group);
        this.expGroup = group;
        this.setExpGroupHook(group.get_Hook());
        this.setExpGroupName(group.getName());

        this.details = group.getDetails();
        this.endDate = group.getEndDate();
        this.startDate = group.getStartDate();

        if (group.getExperiments().isEmpty()) {
            this.status = null;
            this.creator = null;
        } else {
            // Warning - These values may not apply across the whole plate
            final Experiment experiment = group.getExperiments().iterator().next();
            this.status = experiment.getStatus();
            this.creator = experiment.getCreatorPerson();
            final ExperimentType et = experiment.getExperimentType();
            this.expTypeHook = et.get_Hook();
            this.expTypeName = et.getName();

            this.protocol = experiment.getProtocol();
        }

        final Holder holder = HolderFactory.getPlate(group);
        if (holder != null && null != holder.getHolderType()) {
            final AbstractHolderType ht = holder.getHolderType();
            //this.holder = holder;
            this.holderTypeHook = ht.get_Hook();
            this.holderTypeName = ht.getName();
        }

    }

    /**
     * @return the expGroupHook
     */
    public String getExpGroupHook() {
        return this.expGroupHook;
    }

    /**
     * @param expGroupHook the expGroupHook to set
     */
    void setExpGroupHook(final String expGroupHook) {
        this.expGroupHook = expGroupHook;
    }

    /**
     * @return the expGroupName
     */
    public String getExpGroupName() {
        return this.expGroupName;
    }

    /**
     * @param expGroupName the expGroupName to set
     */
    void setExpGroupName(final String expGroupName) {
        this.expGroupName = expGroupName;
    }

    /**
     * @return the expTypeHook
     */
    public String getExpTypeHook() {
        return this.expTypeHook;
    }

    /**
     * @param expTypeHook the expTypeHook to set
     */
    void setExpTypeHook(final String expTypeHook) {
        this.expTypeHook = expTypeHook;
    }

    /**
     * @return the expTypeName
     */
    public String getExpTypeName() {
        return this.expTypeName;
    }

    /**
     * @param expTypeName the expTypeName to set
     */
    void setExpTypeName(final String expTypeName) {
        this.expTypeName = expTypeName;
    }

    /**
     * @return the holderTypeHook
     */
    public String getHolderTypeHook() {
        return this.holderTypeHook;
    }

    /**
     * @param holderTypeHook the holderTypeHook to set
     */
    void setHolderTypeHook(final String holderTypeHook) {
        this.holderTypeHook = holderTypeHook;
    }

    /**
     * @return the holderTypeName
     */
    public String getHolderTypeName() {
        return this.holderTypeName;
    }

    /**
     * @param holderTypeName the holderTypeName to set
     */
    void setHolderTypeName(final String holderTypeName) {
        this.holderTypeName = holderTypeName;
    }

    /**
     * @return the details
     */
    public String getDetails() {
        return this.details;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * @return the protocol
     */
    public Protocol getProtocol() {
        return this.protocol;
    }

    /**
     * @return the creator
     */
    public String getCreator() {
        if (null != this.creator) {
            return this.creator.getGivenName() + " " + this.creator.getFamilyName();
        }
        return "";
    }

    /**
     * @return
     */
    public Calendar getStartDate() {
        return this.startDate;
    }

    /**
     * @return the start date as a formatted string
     */
    @Deprecated
    // better to return Calendar, so browser can convert to local time
    public String getStartDateString() {
        if (null == this.startDate) {
            return "";
        }
        return ValueFormatter.formatDate(this.startDate);
    }

    /**
     * @return
     */
    public Calendar getEndDate() {
        return this.endDate;
    }

    /**
     * @return the experimentGroup as a modelObject
     */
    public ModelObject getmodelObject() {
        return this.expGroup;
    }

    public int getPCRProductLength() {
        //final Map<String, String> pcrProds = SequenceGetter.getPCRProductsFromPlate(this.expGroup);

        return 0;
        /*
        holder.getColPosition() + holder.
        1) getColumn(experiment) + getRow(Experiment) (HolderFactory)
        2) getExperimentByPosition(row, column)
        3) What object would you like to get numbers from?
        4) What return if there is no numbers?
        */
    }

    /**
     * @return Returns the expGroup.
     */
    public ExperimentGroup getExpGroup() {
        return this.expGroup;
    }

    /**
     * Needed for context menus.
     * 
     * @return the expGroupHook
     */
    @Override
    public String getHook() {
        return this.getExpGroupHook();
    }

    /**
     * Needed for context menus.
     * 
     * @return the name of the plate
     */
    @Override
    public String getName() {
        return this.getExpGroupName();
    }

    /**
     * Needed for context menus.
     * 
     * @return the class name
     */
    @Override
    public String getClassName() {
        return ExperimentGroup.class.getName();
    }

    /**
     * Needed for context menus.
     * 
     * @return the class display name
     */
    @Override
    public String getClassDisplayName() {
        return super.getClassDisplayName();
    }

}
