/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.business.crystallization.model;

import java.util.List;

import org.pimslims.business.XtalObject;

/**
 * 
 * @author ian
 */
public class SchedulePlan extends XtalObject {

    private String name = "";

    private String description = "";

    private List<SchedulePlanOffset> offsets = null;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<SchedulePlanOffset> getOffsets() {
        return offsets;
    }

    public void setOffsets(final List<SchedulePlanOffset> offsets) {
        this.offsets = offsets;
    }

}
