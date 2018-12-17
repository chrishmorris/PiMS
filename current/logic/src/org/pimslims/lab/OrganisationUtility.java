/**
 * 
 */
package org.pimslims.lab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.people.Group;
import org.pimslims.model.people.Organisation;

/**
 * @author cm65
 * 
 */
public class OrganisationUtility {

    public static ModelObject createOrganisation(final WritableVersion version, final String name,
        final Map attributes) throws ConstraintException, AccessException {
        final Map<String, Object> a = new java.util.HashMap<String, Object>(attributes);
        a.put("name", name);
        final ModelObject object = version.create(Organisation.class, a);
        return object;
    }

    public static ModelObject createGroup(final WritableVersion version, final ModelObject organisation,
        final Map attributes) throws ConstraintException, AccessException {
        final Map<String, Object> a = new java.util.HashMap<String, Object>(attributes);
        a.put("organisation", Collections.singleton(organisation));
        final ModelObject object = version.create(Group.class, a);
        return object;
    }

    public static ModelObject findOrCreateGroup(final WritableVersion version, final String name,
        final ModelObject organisation, final Map attributes) throws ConstraintException, AccessException {
        final Map<String, Object> a = new java.util.HashMap<String, Object>();
        a.put("name", name);
        final Collection<ModelObject> groups = organisation.findAll("groups", a);
        assert groups.size() <= 1 : "Too many groups called: " + name;
        if (1 == groups.size()) {
            final List<ModelObject> groupList = new ArrayList(groups);
            return groupList.get(0);
        }

        // create it
        a.putAll(attributes);
        a.put("organisation", Collections.singleton(organisation));
        final ModelObject object = version.create(Group.class, a);
        return object;
    }

}
