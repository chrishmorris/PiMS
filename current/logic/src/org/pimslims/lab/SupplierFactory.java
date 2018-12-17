/*
 * Created on 13-May-2005 @author Chris Morris, c.morris@dl.ac.uk PIMS project, www.pims-lims.org Copyright
 * (C) 2006 Daresbury Lab Daresbury, Warrington WA4 4AD, UK
 */
package org.pimslims.lab;

import java.util.Map;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.people.Organisation;

/**
 * Factory methods to help work with suppliers.
 * 
 * Each method creates an instance of a class. The required attributes and associations are parameters of the
 * methods. The optional ones can be provided in a map. If none are needed, pass
 * java.util.Collections.EMPTY_MAP.
 * 
 */
public class SupplierFactory {

    /**
     * Create a new supplier organisation
     * 
     * @param version - current transaction
     * @param name - the name of the supplier
     * @param attributes - map field name/value
     * @return the new supplier organisation
     * @throws ConstraintException
     * @throws AccessException
     */
    public static Organisation createOrganisation(final WritableVersion version, final String name,
        final Map attributes) throws ConstraintException, AccessException {
        final Map<String, Object> a = new java.util.HashMap<String, Object>(attributes);
        a.put(Organisation.PROP_NAME, name);
        return new Organisation(version, a);
    }

    /**
     * Get or create a supplier organisation
     * 
     * @param version - current transaction
     * @param name - the name of the supplier
     * @return the new supplier organisation
     * @throws ConstraintException
     * @throws AccessException
     */
    public static Organisation getOrganisation(final WritableVersion version, final String name)
        throws ConstraintException, AccessException {
        if (null == name || "".equals(name)) {
            return null;
        }
        final Map<String, Object> a = new java.util.HashMap<String, Object>();
        a.put(Organisation.PROP_NAME, name);
        final java.util.Collection organisationsFound = version.findAll(Organisation.class, a);
        if (organisationsFound.isEmpty()) {
            if (version.isAdmin()) {
                return SupplierFactory.createOrganisation(version, name, java.util.Collections.EMPTY_MAP);
            } else {
                throw new RuntimeException("Can not create organisation, " + name
                    + ", as current user is not Administrator!");
            }
        }
        return (Organisation) organisationsFound.iterator().next();
    }

}
