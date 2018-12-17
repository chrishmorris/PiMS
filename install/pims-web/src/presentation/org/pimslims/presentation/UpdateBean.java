/**
 * current-pims-web org.pimslims.presentation UpdateBean.java
 * 
 * @author cm65
 * @date 3 Mar 2008
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2008 cm65 
 * 
 * 
 */
package org.pimslims.presentation;

import java.util.Map;

/**
 * UpdateBean
 * 
 */
public interface UpdateBean {

    /**
     * @return Returns the names of the properties that have been changed e.g. Experiment.PROP_STARTDATE
     */
    public Map<String, Object> getChanged();

}
