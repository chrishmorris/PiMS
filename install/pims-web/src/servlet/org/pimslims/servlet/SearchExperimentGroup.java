/**
 * pims-web org.pimslims.servlet SearchExperimentGroup.java
 * 
 * @author cm65
 * @date 16 Sep 2009
 * 
 * Protein Information Management System
 * @version: 2.2
 * 
 * Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet;

import javax.servlet.http.HttpServletRequest;

import org.pimslims.model.experiment.ExperimentGroup;

/**
 * SearchExperimentGroup
 * 
 */
public class SearchExperimentGroup extends QuickSearch {

    /**
     * Beleived not used - see SearchPlate
     * 
     * SearchExperimentGroup.getMetaClassName
     * 
     * @see org.pimslims.servlet.OldSearch#getMetaClassName(javax.servlet.http.HttpServletRequest,
     *      java.lang.String)
     */
    @Override
    protected String getMetaClassName(final HttpServletRequest request) {
        return ExperimentGroup.class.getName();
    }

}
