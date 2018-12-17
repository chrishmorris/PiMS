/**
 * pims-model org.pimslims.model.holder Container.java
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

import java.util.Collection;

import org.pimslims.exception.ConstraintException;
import org.pimslims.model.reference.ContainerType;

/**
 * Container TODO remove this, it is no longer helpful, now that we have deprecated Location.
 */
public interface Container extends Containable {

    /**
     * Container.setContainerType
     * 
     * @param object
     * @throws ConstraintException
     */
    void setContainerType(ContainerType type) throws ConstraintException;

    /**
     * Container.getContainerType
     * 
     * @return
     */
    ContainerType getContainerType();

    /**
     * Container.getContained Note: getContents would be a better name, but it is already in use in Location
     * for one of the methods this will replace
     * 
     * @return
     */
    Collection<Containable> getContained();

    /**
     * Container.getDimension
     * 
     * @return 0 for a loop, up to 3 for a crystallization plate
     */
    int getDimension();

}
