/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

import org.pimslims.business.XtalObject;

/**
 * 
 * @author ian
 */
public class SchedulePlanOffset extends XtalObject {

    private SchedulePlan schedulePlan = null;

    private int offsetHoursFromTimeZero = 0;

    private int imagingNumber = 0;

    private int priority = 5;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public SchedulePlan getSchedulePlan() {
        return schedulePlan;
    }

    public void setSchedulePlan(final SchedulePlan schedulePlan) {
        this.schedulePlan = schedulePlan;
    }

    public int getOffsetHoursFromTimeZero() {
        return offsetHoursFromTimeZero;
    }

    public void setOffsetHoursFromTimeZero(final int offsetHoursFromTimeZero) {
        this.offsetHoursFromTimeZero = offsetHoursFromTimeZero;
    }

    public int getImagingNumber() {
        return imagingNumber;
    }

    public void setImagingNumber(final int imagingNumber) {
        this.imagingNumber = imagingNumber;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(final int priority) {
        this.priority = priority;
    }
}
