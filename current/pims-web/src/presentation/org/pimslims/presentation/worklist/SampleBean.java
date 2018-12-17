package org.pimslims.presentation.worklist;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ModelObjectShortBean;

/**
 * the bean used to display sample information in customized sample list
 * 
 * @author bl67
 * 
 */
public class SampleBean extends ModelObjectShortBean {
    String sampleHook;

    String sampleName;

    String lastExpType;

    String lastExpHook;

    String lastExpResult;

    Integer noProgressDays;

    Map<String, String> nextExpTypes; // exp type hook->exp type name

    String userHookAssignedTo;

    String personNameAssignedTo;

    Map<String, String> expsUsedIn;// exp hook->exp name

    Boolean active;

    public SampleBean(final Sample sample) {
        super(sample);
        assert sample != null;
        this.setSampleHook(sample.get_Hook());
        this.setSampleName(sample.getName());

        // last exp's information
        Experiment exp = null;
        if (sample.getOutputSample() != null) {
            exp = sample.getOutputSample().getExperiment();
        }
        if (exp != null) {
            this.setLastExpHook(exp.get_Hook());
            this.setLastExpResult(exp.getStatus());
            if (exp.getExperimentType() != null) {
                this.setLastExpType(exp.getExperimentType().getName());
            }
        }

        // set no progress days
        this.setNoProgressDays(this.getNoProgressDaysFromExp(exp));
        // set exps this sample used as inputsample
        this.setExpsUsedIn(this.getExpsUsedFromSample(sample));

        // set exp types ready for
        this.setNextExpTypes(this.getNextExpTypesFromSample(sample));

        // set active
        this.setActive(sample.getIsActive());

        // set assignedTo
        if (sample.getAssignTo() != null) {
            this.setPersonHookAssignedTo(sample.getAssignTo().get_Hook());
            this.setPersonNameAssignedTo(sample.getAssignTo().get_Name());
        }

    }

    private Map<String, String> getNextExpTypesFromSample(final Sample sample) {
        if (sample == null) {
            return null;
        }
        final Map<String, String> ets = new HashMap<String, String>();
        final Set<SampleCategory> categories = sample.getSampleCategories();
        //final Collection<RefInputSample> ret = new HashSet<RefInputSample>();
        for (final Iterator iter = categories.iterator(); iter.hasNext();) {
            final SampleCategory category = (SampleCategory) iter.next();
            for (final Iterator iterator = category.getRefInputSamples().iterator(); iterator.hasNext();) {
                final RefInputSample input = (RefInputSample) iterator.next();
                final ExperimentType et = input.getProtocol().getExperimentType();
                ets.put(et.get_Hook(), et.getName());
            }
        }

        return ets;
    }

    private Map<String, String> getExpsUsedFromSample(final Sample sample) {
        if (sample == null || sample.getInputSamples() == null) {
            return null;
        }
        final Map<String, String> exps = new HashMap<String, String>();
        for (final InputSample is : sample.getInputSamples()) {
            final Experiment exp = is.getExperiment();
            exps.put(exp.get_Hook(), exp.getName());
        }
        return exps;
    }

    private Integer getNoProgressDaysFromExp(final Experiment exp) {
        if (exp == null || exp.getEndDate() == null) {
            return null;
        }
        final Calendar expDate = exp.getEndDate();
        final Long timeDifference = System.currentTimeMillis() - expDate.getTimeInMillis();
        final Long days = (timeDifference / (1000 * 60 * 60 * 24));
        return days.intValue();

    }

    /**
     * @return the expsUsedIn
     */
    public Map<String, String> getExpsUsedIn() {
        return this.expsUsedIn;
    }

    /**
     * @param expsUsedIn the expsUsedIn to set
     */
    public void setExpsUsedIn(final Map<String, String> expsUsedIn) {
        this.expsUsedIn = expsUsedIn;
    }

    /**
     * @return the lastExpHook
     */
    public String getLastExpHook() {
        return this.lastExpHook;
    }

    /**
     * @param lastExpHook the lastExpHook to set
     */
    public void setLastExpHook(final String lastExpHook) {
        this.lastExpHook = lastExpHook;
    }

    /**
     * @return the lastExpResult
     */
    public String getLastExpResult() {
        return this.lastExpResult;
    }

    /**
     * @param lastExpResult the lastExpResult to set
     */
    public void setLastExpResult(final String lastExpResult) {
        this.lastExpResult = lastExpResult;
    }

    /**
     * @return the lastExpType
     */
    public String getLastExpType() {
        return this.lastExpType;
    }

    /**
     * @param lastExpType the lastExpType to set
     */
    public void setLastExpType(final String lastExpType) {
        this.lastExpType = lastExpType;
    }

    /**
     * @return the nextExpTypes
     */
    public Map<String, String> getNextExpTypes() {
        return this.nextExpTypes;
    }

    /**
     * @param nextExpTypes the nextExpTypes to set
     */
    public void setNextExpTypes(final Map<String, String> nextExpTypes) {
        this.nextExpTypes = nextExpTypes;
    }

    /**
     * @return the noProgressDays
     */
    public Integer getNoProgressDays() {
        return this.noProgressDays;
    }

    /**
     * @param noProgressDays the noProgressDays to set
     */
    public void setNoProgressDays(final Integer noProgressDays) {
        this.noProgressDays = noProgressDays;
    }

    /**
     * @return the personHookAssignedTo
     */
    public String getPersonHookAssignedTo() {
        return this.userHookAssignedTo;
    }

    /**
     * @param personHookAssignedTo the hook of the person the sample is assigned to
     */
    private void setPersonHookAssignedTo(final String personHookAssignedTo) {
        this.userHookAssignedTo = personHookAssignedTo;
    }

    /**
     * @return the personNameAssignedTo
     */
    public String getPersonNameAssignedTo() {
        return this.personNameAssignedTo;
    }

    /**
     * @param personNameAssignedTo the personNameAssignedTo to set
     */
    public void setPersonNameAssignedTo(final String personNameAssignedTo) {
        this.personNameAssignedTo = personNameAssignedTo;
    }

    /**
     * @return the sampleHook
     */
    public String getSampleHook() {
        return this.sampleHook;
    }

    /**
     * @param sampleHook the sampleHook to set
     */
    public void setSampleHook(final String sampleHook) {
        this.sampleHook = sampleHook;
    }

    /**
     * @return the sampleName
     */
    public String getSampleName() {
        return this.sampleName;
    }

    /**
     * @param sampleName the sampleName to set
     */
    public void setSampleName(final String sampleName) {
        this.sampleName = sampleName;
    }

    /**
     * @return true if the sample is marked as active
     */
    public Boolean getActive() {
        return this.active;
    }

    /**
     * @param active an indication whether the sample is to be treated as active
     */
    public void setActive(final Boolean active) {
        this.active = active;
    }

}
