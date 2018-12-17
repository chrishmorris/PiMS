/**
 * pims-model org.pimslims.model.holder Containable.java
 * 
 * @author cm65
 * @date 27 Sep 2011
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.model.holder;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.ConstraintException;

/**
 * Containable
 * 
 */
public interface Containable {

    public String getName();

    public String get_Hook();

    public ReadableVersion get_Version();

    /**
     * Containable.getContainer
     * 
     * @return
     */
    Container getContainer();

    /**
     * Containable.setContainer
     * 
     * @param loop
     * @throws ConstraintException
     */
    void setContainer(Container container) throws ConstraintException;

    public void setColPosition(Integer column) throws ConstraintException;

    public void setRowPosition(Integer row) throws ConstraintException;

    public void setSubPosition(Integer subposition) throws ConstraintException;

    public Integer getColPosition();

    public Integer getRowPosition();

    public Integer getSubPosition();

}
