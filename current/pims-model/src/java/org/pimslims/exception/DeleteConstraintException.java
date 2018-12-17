/**
 * org.pimslims.metamodel DuplicateKeyConstraintException.java
 * 
 * @date 05-Jan-2007 16:43:08
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
package org.pimslims.exception;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.MetaRoleImpl;
import org.pimslims.metamodel.ModelObject;

/**
 * Indicates that a data model value violates a duplicate key constraint.
 * 
 */
public class DeleteConstraintException extends ConstraintException {

    private static final long serialVersionUID = 1L;

    private Collection<ModelObject> requiredBy = new HashSet<ModelObject>();

    public DeleteConstraintException(Throwable cause) {
        super("Can not delete " + getModelObjectHook(cause.getMessage())
            + " as it is required by other objects.", cause);
        getRequiredByObjects(cause.getMessage());
    }

    public Collection<ModelObject> getRequiredByObjects() {
        return requiredBy;
    }

    /**
     * DeleteConstraintException.getRequiredByObjects
     * 
     * @param modelObjectHook
     * @return
     */
    private Collection<ModelObject> getRequiredByObjects(String message) {
        String modelObjectHook = getModelObjectHook(message);
        Collection<ModelObject> results = new LinkedList<ModelObject>();
        AbstractModel model = ModelImpl.getModel();
        ReadableVersion rv = model.getReadableVersion(Access.ADMINISTRATOR);
        super.object = rv.get(modelObjectHook);
        try {
            ModelObject objectToDelete = rv.get(modelObjectHook);
            MetaClass ma = objectToDelete.get_MetaClass();
            Collection<MetaRole> roles = getRequiredNotParentRoles(model, ma);
            for (MetaRole role : roles) {
                Collection<ModelObject> otherObjects =
                    rv.findAll(role.getMetaClass().getJavaClass(), role.getName(), objectToDelete);
                for (ModelObject otherObject : otherObjects) {
                    results.add(otherObject);
                }
            }
        } finally {
            rv.abort();
        }
        requiredBy = results;
        return results;
    }

    /**
     * DeleteConstraintException.getRequiredNotParentRoles
     * 
     * @param model
     * @param ma
     * @return
     */
    private static Collection<MetaRole> getRequiredNotParentRoles(AbstractModel model, MetaClass ma) {
        Collection<MetaRole> roles = new HashSet<MetaRole>();

        //TODO use visit(ModelVisitor)
        for (String className : model.getClassNames()) {
            MetaClass otherMa = model.getMetaClass(className);
            for (MetaRole role : otherMa.getMetaRoles().values()) {
                if (role.isRequired() && role.getOtherMetaClass().getName().equals(ma.getName())) {
                    if (!((MetaRoleImpl) role).isParentRole()) {
                        roles.add(role);
                    }
                }
            }
        }
        return roles;
    }

    /**
     * DeleteConstraintException.getModelObjectHook
     * 
     * @param message
     * @return
     */
    private static String getModelObjectHook(String message) {
        String hook = message.substring(message.indexOf('[') + 1, message.indexOf(']'));
        hook = hook.replace("#", ":");

        return hook;
    }
}
