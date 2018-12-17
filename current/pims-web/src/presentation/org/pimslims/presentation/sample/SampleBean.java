package org.pimslims.presentation.sample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;

/**
 * Represents a sample in a list on a JSP page.
 * 
 * @see org.pimslims.model.sample.Sample
 * @author Marc Savitsky
 * 
 */
public class SampleBean extends ModelObjectBean implements Serializable {

    private String details;

    private Boolean isActive;

    private Boolean isHazard;

    private Float pH;

    private Float ionicStrength;

    private Integer rowPosition;

    private final Integer colPosition;

    private Integer subPosition;

    private Float initialAmount;

    private Float currentAmount;

    private String amountUnit;

    private String amountDisplayUnit;

    private Boolean currentAmountFlag;

    private Integer batchNum;

    private String assignTo;

    /**
     * Images<hook>
     */
    private Collection<String> images = new ArrayList<String>();

    private final ModelObjectShortBean holder;

    private final String positionInPlate; //e.g. A01

    private final String sequence;

    public SampleBean() {
        // empty constructor
        this.holder = null;
        this.rowPosition = null;
        this.colPosition = null;
        this.positionInPlate = "";
        this.sequence = null;
    }

    public SampleBean(final Sample sample) {
        super(sample);
        this.setDetails(sample.getDetails());
        this.positionInPlate = HolderFactory.getPositionInHolder(sample);
        this.colPosition = sample.getColPosition();
        this.rowPosition = sample.getRowPosition();
        this.setSubPosition(sample.getSubPosition());
        if (null == sample.getHolder()) {
            this.holder = null;
        } else {
            this.holder = new ModelObjectShortBean(sample.getHolder());
        }
        this.setCurrentAmount(sample.getCurrentAmount());
        this.setAmountDisplayUnit(sample.getAmountDisplayUnit());

        final User user = sample.getAssignTo();
        if (null != user) {
            this.setAssignTo(user.getName());
        } else {
            this.setAssignTo("Unassigned");
        }
        this.setIsActive(sample.getIsActive());
        this.sequence = ""; // TODO sample.getSequence();
    }

    /**
     * @return Returns the positionInPlate.
     */
    public String getPositionInPlate() {
        return this.positionInPlate;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(final String details) {
        this.details = details;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(final Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsHazard() {
        return this.isHazard;
    }

    public void setIsHazard(final Boolean isHazard) {
        this.isHazard = isHazard;
    }

    public Float getpH() {
        return this.pH;
    }

    public String getpHString() {
        if (null == this.pH) {
            return "";
        }
        return this.pH.toString();
    }

    public void setpH(final Float pH) {
        this.pH = pH;
    }

    public Float getIonicStrength() {
        return this.ionicStrength;
    }

    public String getIonicStrengthString() {
        if (null == this.ionicStrength) {
            return "";
        }
        return this.ionicStrength.toString();
    }

    public void setIonicStrength(final Float ionicStrength) {
        this.ionicStrength = ionicStrength;
    }

    public Integer getRowPosition() {
        return this.rowPosition;
    }

    public String getRowAlphaPosition() {
        if (null == this.rowPosition) {
            return "";
        }
        return HolderFactory.ROWS[this.rowPosition - 1];
    }

    public void setRowAlphaPosition(final String s) {
        int i = 0;
        for (i = 0; i < HolderFactory.ROWS.length; i++) {
            if (HolderFactory.ROWS[i].equals(s)) {
                break;
            }
        }
        this.rowPosition = i + 1;
    }

    public Integer getColPosition() {
        return this.colPosition;
    }

    public Integer getSubPosition() {
        return this.subPosition;
    }

    public void setSubPosition(final Integer subPosition) {
        this.subPosition = subPosition;
    }

    public Float getInitialAmount() {
        return this.initialAmount;
    }

    public void setInitialAmount(final Float initialAmount) {
        this.initialAmount = initialAmount;
    }

    public Float getCurrentAmount() {
        return this.currentAmount;
    }

    public void setCurrentAmount(final Float currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getAssignTo() {
        return this.assignTo;
    }

    public void setAssignTo(final String assignTo) {
        this.assignTo = assignTo;
    }

    public String getAmountUnit() {
        return this.amountUnit;
    }

    public void setAmountUnit(final String amountUnit) {
        this.amountUnit = amountUnit;
    }

    public String getAmountDisplayUnit() {
        return this.amountDisplayUnit;
    }

    public void setAmountDisplayUnit(final String amountDisplayUnit) {
        this.amountDisplayUnit = amountDisplayUnit;
    }

    public Boolean getCurrentAmountFlag() {
        return this.currentAmountFlag;
    }

    public void setCurrentAmountFlag(final Boolean currentAmountFlag) {
        this.currentAmountFlag = currentAmountFlag;
    }

    public Integer getBatchNum() {
        return this.batchNum;
    }

    public void setBatchNum(final Integer batchNum) {
        this.batchNum = batchNum;
    }

    public ModelObjectShortBean getHolder() {
        return this.holder;
    }

    @Deprecated
    // use getHolder
    public String getHolderHook() {
        if (null == this.holder) {
            return null;
        }
        return this.holder.getHook();
    }

    @Deprecated
    // use getHolder
    public String getHolderName() {
        if (null == this.holder) {
            return null;
        }
        return this.holder.getName();
    }

    public Object[] getImages() {
        return this.images.toArray();
    }

    public void setImages(final String[] images) {
        if (null != images) {
            this.images = Arrays.asList(images);
        }
    }

    public static Collection<SampleBean> getSampleBeans(final Collection<Sample> samples) {
        // use a list, to preserve order the input is ordered
        final Collection<SampleBean> ret = new ArrayList<SampleBean>(samples.size());
        for (final Iterator<Sample> iter = samples.iterator(); iter.hasNext();) {
            final Sample sample = iter.next();
            final SampleBean bean = new SampleBean(sample);
            //Person person = sample.getAssignTo();
            ret.add(bean);
        }
        return ret;
    }

    /**
     * SampleBean.getSequence
     * 
     * @return
     */
    public String getSequence() {
        return this.sequence;
    }

}
