/*
 * Created on 28-Apr-2005 @author: Chris Morris
 */
package org.pimslims.lab.create;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;

/**
 * Factory method for org.pimslims.model.target.Target
 * 
 * @see org.pimslims.dao.WritableVersionImpl#create(java.lang.Class, java.util.Map) Ensures there is a status
 */
public class TargetFactory {

    private TargetFactory() {
        // can't contruct this class, only supplies a static method
    }

    private static TargetStatus getDefaultStatus(final WritableVersion version) throws AccessException,
        ConstraintException {
        return TargetFactory.getStatus(version, "Selected");
    }

    /**
     * Find or create a target status TODO no, can create reference data not owned by admin
     * 
     * @param status The code for the status required
     * @return an object representing the status
     * @throws ConstraintException
     * @throws AccessException
     */
    @SuppressWarnings("unchecked")
    public static TargetStatus getStatus(final WritableVersion version, final String status)
        throws AccessException, ConstraintException {
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("name", status);
        final Collection<TargetStatus> statuses = version.findAll(TargetStatus.class, attributes);
        if (1 == statuses.size()) {
            final ModelObject s = new ArrayList<ModelObject>(statuses).get(0);
            return (TargetStatus) s;
        }
        final TargetStatus ts = version.create(TargetStatus.class, attributes);
        return ts;
    }

    /**
     * Convert the dummy researchObjective into a the target it represents
     * 
     * @param expBlueprint
     * @return the target it represents, or null
     */
    public static Target getPimsTarget(final ResearchObjective expBlueprint) {
        final Collection<ResearchObjectiveElement> components = expBlueprint.getResearchObjectiveElements();
        if (1 != components.size()) {
            return null;
        }
        final ResearchObjectiveElement component = components.iterator().next();
        return component.getTarget();
    }
}
