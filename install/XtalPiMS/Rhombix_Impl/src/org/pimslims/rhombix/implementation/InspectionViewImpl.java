/**
 * Rhombix_Impl org.pimslims.rhombix InspectionViewImpl.java
 * 
 * @author cm65
 * @date 13 Apr 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

import org.pimslims.business.crystallization.view.InspectionView;

/**
 * InspectionViewImpl
 * 
 */
public class InspectionViewImpl extends InspectionView {

    private long id;

    /**
     * @return Returns the id.
     */
    public long getId() {
        return this.id;
    }

    /**
     * InspectionViewImpl.setId
     * 
     * @param id
     */
    public void setId(long id) {
        this.id = id;

    }

}
