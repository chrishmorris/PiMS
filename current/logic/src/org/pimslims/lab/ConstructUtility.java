package org.pimslims.lab;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.experiment.ExperimentUtility;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;

/**
 * provides methods to indicate whether a record should be handled by the SPOT package or by the default logic
 * 
 */
public class ConstructUtility {

    /**
     * ResearchObjectiveElement type for constructs
     */
    public static final String SPOTCONSTRUCT = "SPOTConstruct";

    public static final String OPTICCONSTRUCT = "OpticConstruct";

    public static final Collection<String> constructTypes = new HashSet<String>();
    static {
        ConstructUtility.constructTypes.add(ConstructUtility.SPOTCONSTRUCT);
        ConstructUtility.constructTypes.add(ConstructUtility.OPTICCONSTRUCT);
    }

    /**
     * Naming system used for target statuses etc
     */
    public static final String SPOT_NAMING_SYSTEM = "SPOT";

    public static boolean isSpotConstruct(final Project project) {
        return ConstructUtility.isSpotConstruct((ResearchObjective) project);
    }

    public static boolean isSpotConstruct(final ResearchObjective project) {

		ReadableVersion version = project.get_Version();
		ExperimentType type = version.findFirst(ExperimentType.class,
				ExperimentType.PROP_NAME,
				ExperimentUtility.SPOTCONSTRUCT_DESIGN);
		Map<String, Object> criteria = new HashMap();
		criteria.put(Experiment.PROP_PROJECT, project);
		criteria.put(Experiment.PROP_EXPERIMENTTYPE, type);
		Experiment experiment = version.findFirst(Experiment.class, criteria);

		return null != experiment;
    }

    public static boolean isSpotConstruct(final ResearchObjectiveElement component) {
        if (ConstructUtility.constructTypes.contains(component.getComponentType())) {
            return true;
        }
        return false;
    }

    public static boolean isSpotConstruct(final ModelObject object) {
        final Object specificObject = object;
        if (specificObject instanceof ResearchObjective) {
            return ConstructUtility.isSpotConstruct((Project) specificObject);
        } else if (specificObject instanceof ResearchObjectiveElement) {
            return ConstructUtility.isSpotConstruct((ResearchObjectiveElement) specificObject);
        } else {
            return false;
        }
    }

    private ConstructUtility() {
        // this class contains static methods only
    }

}
