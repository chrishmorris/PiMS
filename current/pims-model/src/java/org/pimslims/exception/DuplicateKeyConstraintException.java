/**
 * org.pimslims.metamodel DuplicateKeyConstraintException.java
 * 
 * @date 05-Jan-2007 16:43:08
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 * Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 * Copyright (c) 2007
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * 
 * A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.exception;

import org.pimslims.metamodel.ModelObject;


/**
 * Indicates that a data model value violates a duplicate key constraint.
 * 
 */
public class DuplicateKeyConstraintException extends ConstraintException {

    private static final long serialVersionUID = 1L;

    /**
     * 
     * @param msg error message
     */
    public DuplicateKeyConstraintException(String msg) {
        super(msg);
    }

    public DuplicateKeyConstraintException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DuplicateKeyConstraintException(final ModelObject object, final String attributeName,
        final Object value, final String message, final Throwable cause) {
        super(object, attributeName, value, message, cause);
    }
}
