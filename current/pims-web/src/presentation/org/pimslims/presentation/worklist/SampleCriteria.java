package org.pimslims.presentation.worklist;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;

public class SampleCriteria {

    /**
     * Represents some conditions to be used for searching for samples
     */
    private static final int WEEK = 7;

    static final int MAXRECORDS = 10;

    public static final SampleCriteria WEEK_OLD_SEARCH = new SampleCriteria();
    static {
        SampleCriteria.WEEK_OLD_SEARCH.setActive(true);
        SampleCriteria.WEEK_OLD_SEARCH.setAlreadyInUse(false);
        SampleCriteria.WEEK_OLD_SEARCH.setDaysNoProgress(SampleCriteria.WEEK);
        SampleCriteria.WEEK_OLD_SEARCH.setExpTypeNameReadyFor(null);
        SampleCriteria.WEEK_OLD_SEARCH.setLimit(SampleCriteria.MAXRECORDS);
        SampleCriteria.WEEK_OLD_SEARCH.setUserHookAssignedTo(null);
        SampleCriteria.WEEK_OLD_SEARCH.setReadyForNext(null);
    }

    public static final SampleCriteria NEW_SAMPLE_SEARCH = new SampleCriteria();
    static {
        SampleCriteria.NEW_SAMPLE_SEARCH.setActive(true);
        SampleCriteria.NEW_SAMPLE_SEARCH.setAlreadyInUse(false);
        SampleCriteria.NEW_SAMPLE_SEARCH.setDaysNoProgress(-1);
        SampleCriteria.NEW_SAMPLE_SEARCH.setExpTypeNameReadyFor(null);
        SampleCriteria.NEW_SAMPLE_SEARCH.setLimit(SampleCriteria.MAXRECORDS);
        SampleCriteria.NEW_SAMPLE_SEARCH.setUserHookAssignedTo(null);
        SampleCriteria.NEW_SAMPLE_SEARCH.setReadyForNext(true);
    }

    public static final SampleCriteria ASSIGNED_SAMPLE_SEARCH = new SampleCriteria();
    static {
        SampleCriteria.ASSIGNED_SAMPLE_SEARCH.setActive(true);
        SampleCriteria.ASSIGNED_SAMPLE_SEARCH.setAlreadyInUse(null);
        SampleCriteria.ASSIGNED_SAMPLE_SEARCH.setDaysNoProgress(-1);
        SampleCriteria.ASSIGNED_SAMPLE_SEARCH.setExpTypeNameReadyFor(null);
        SampleCriteria.ASSIGNED_SAMPLE_SEARCH.setLimit(SampleCriteria.MAXRECORDS);
        SampleCriteria.ASSIGNED_SAMPLE_SEARCH.setUserHookAssignedTo(null);
        SampleCriteria.ASSIGNED_SAMPLE_SEARCH.setReadyForNext(null);
    }

    static final int UNLIMITED = 0;

    String userHookAssignedTo = null;

    int daysNoProgress = SampleCriteria.UNLIMITED;

    Boolean readyForNext = true;

    String expTypeNameReadyFor = null;

    Boolean alreadyInUse = null;

    Boolean active = null;

    int limit = SampleCriteria.UNLIMITED;

    /**
     * @return the alreadyInUse
     */
    public Boolean getAlreadyInUse() {
        return this.alreadyInUse;
    }

    /**
     * @param alreadyInUse the alreadyInUse to set
     */
    public void setAlreadyInUse(final Boolean alreadyInUse) {
        this.alreadyInUse = alreadyInUse;
    }

    /**
     * @return the daysNoProgress
     */
    public int getDaysNoProgress() {
        return this.daysNoProgress;
    }

    /**
     * @param daysNoProgress the daysNoProgress to set
     */
    public void setDaysNoProgress(final int daysNoProgress) {
        this.daysNoProgress = daysNoProgress;
    }

    /**
     * @return the limit
     */
    public int getLimit() {
        return this.limit;
    }

    /**
     * @param limit the limit to set
     */
    public void setLimit(final int limit) {
        this.limit = limit;
    }

    /**
     * @return the personHookAssignedTo
     */
    public String getUserHookAssignedTo() {
        return this.userHookAssignedTo;
    }

    /**
     * @param personHookAssignedTo the personHookAssignedTo to set
     */
    public void setUserHookAssignedTo(final String userHookAssignedTo) {
        this.userHookAssignedTo = userHookAssignedTo;
    }

    /**
     * @return the readyForNext
     */
    public Boolean getReadyForNext() {
        return this.readyForNext;
    }

    /**
     * @param readyForNext the readyForNext to set
     */
    public void setReadyForNext(final Boolean readyForNext) {
        this.readyForNext = readyForNext;
    }

    /**
     * @return the expTypeNameReadyFor
     */
    public String getExpTypeNameReadyFor() {
        return this.expTypeNameReadyFor;
    }

    /**
     * @param expTypeNameReadyFor the expTypeNameReadyFor to set
     */
    public void setExpTypeNameReadyFor(final String expTypeNameReadyFor) {
        this.expTypeNameReadyFor = expTypeNameReadyFor;
    }

    /**
     * @return the active
     */
    public Boolean getActive() {
        return this.active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(final Boolean active) {
        this.active = active;
    }

    public String getUserNameAssignedTo(final ReadableVersion version) {
        if (this.userHookAssignedTo == null) {
            return null;
        }
        final ModelObject user = version.get(this.userHookAssignedTo);
        return user.get_Name();
    }

}
