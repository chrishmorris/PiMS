/**
 * org.pimslims.metamodel MetaProperty.java
 * 
 * @date 7 Dec 2007 09:48:03
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 *           Copyright (c) 2007
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.metamodel;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;

/**
 * MetaProperty
 * 
 */
public interface MetaProperty extends MetaElement {

    /**
     * Indicates whether this property is derived. These properties cannot be set even at creation time.
     * 
     * @return true if this property is calculated rather than set
     */
    public boolean isDerived();

    /**
     * Indicates whether this property is required.
     * 
     * @return true if this property is required
     */
    public boolean isRequired();

    /**
     * set the MetaProperty of ModelObject to value
     * 
     * @param mo
     * @param value
     * @throws ConstraintException
     * @throws AccessException
     */
    public abstract void set_prop(ModelObject mo, Object value) throws ConstraintException, AccessException;

    /**
     * get value of the MetaProperty of ModelObject
     * 
     * @param mo
     * @param value
     * @throws ConstraintException
     */
    public abstract Object get_prop(ModelObject mo);

}
