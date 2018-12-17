/*
 * Created on 13-May-2005 @author: Chris Morris
 */
package org.pimslims.lab.protocol;

import java.util.List;
import java.util.Map;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;

/**
 * Factory methods to help work with protocols. (Eventually methods like these will be generated
 * automatically)
 * 
 * Each method creates an instance of a class. The required attributes and associations are parameters of the
 * methods. The optional ones can be provided in a map. If none are needed, pass
 * java.util.Collections.EMPTY_MAP.
 * 
 * @version 0.1
 */
public class ProtocolFactory {

    /**
     * Create a new experiment type This method willbe used rarely - experiment types are reference data
     * 
     * @param version current transaction
     * @param name name of protocol
     * @param attributes map field name => value
     * @return the new experiment type
     * @throws ConstraintException
     * @throws AccessException
     */
    public static ExperimentType createExperimentType(final WritableVersion version, final String name,
        final Map attributes) throws ConstraintException, AccessException {
        final Map<String, Object> a = new java.util.HashMap<String, Object>(attributes);
        a.put(ExperimentType.PROP_NAME, name);
        final ModelObject object = version.create(ExperimentType.class, a);
        return (ExperimentType) object;
    }

    /**
     * Get an experiment type, or create it if it does not exist already
     * 
     * @param version current transaction
     * @param name name of protocol
     * @return the new experiment type
     * @throws ConstraintException
     * @throws AccessException
     */
    public static ExperimentType getExperimentType(final WritableVersion version, final String name)
        throws ConstraintException, AccessException {
        final Map<String, Object> a = new java.util.HashMap<String, Object>();
        a.put("name", name);
        final java.util.Collection types = version.findAll(ExperimentType.class, a);
        if (0 == types.size()) {
            return ProtocolFactory.createExperimentType(version, name, java.util.Collections.EMPTY_MAP);
        } else if (1 == types.size()) {
            final ModelObject type = (ModelObject) new java.util.ArrayList(types).get(0);
            return (ExperimentType) type;
        }
        throw new RuntimeException("Invalid database state: duplicate experiment types");
    }

    /**
     * Create a new sample category. This method will be used rarely, sample categories are reference data.
     * 
     * @param version
     * @param name
     * @param attributes
     * @return
     * @throws ConstraintException
     * @throws AccessException
     */
    public static SampleCategory createSampleCategory(final WritableVersion version, final String name,
        final Map attributes) throws ConstraintException, AccessException {
        final Map<String, Object> a = new java.util.HashMap<String, Object>(attributes);
        a.put("name", name);
        final ModelObject object = version.create(SampleCategory.class, a);
        return (SampleCategory) object;
    }

    /**
     * Get a sample category, or create it if it does not exist yet.
     * 
     * @param version
     * @param name
     * @return
     * @throws ConstraintException
     * @throws AccessException
     */
    public static SampleCategory getSampleCategory(final WritableVersion version, final String name)
        throws ConstraintException, AccessException {
        assert null != name;
        final Map<String, Object> a = new java.util.HashMap<String, Object>();
        a.put("name", name);
        final java.util.Collection types = version.findAll(SampleCategory.class, a);
        if (0 == types.size()) {
            return ProtocolFactory.createSampleCategory(version, name, java.util.Collections.EMPTY_MAP);
        } else if (1 == types.size()) {
            final java.util.Iterator i = types.iterator();
            return (SampleCategory) i.next();
        }
        throw new RuntimeException("Invalid database state: duplicate sample categories " + name);
    }

    /**
     * Find existing experiment types If you want all the current experiment types, use
     * java.util.Collections.EMPTY_MAP as the criteria.
     * 
     * @param version current transaction
     * @param criteria map field name => value
     * @return a collection of the experiment types
     * @throws ConstraintException
     * @throws AccessException
     * 
     * public static java.util.Collection findAllExperimentTypes(final WritableVersion version, final Map
     * criteria) throws ConstraintException, AccessException { return version.findAll(ExperimentType.class,
     * criteria); }
     */

    /**
     * Create a protocol
     * 
     * @param version - current transaction
     * @param name - name of the protocol
     * @param experimentType - experiment type of the protocol
     * @param attributes - map containing name/value for attributes and roles
     * @return the new protocol or the existing one
     * @throws ConstraintException
     * @throws AccessException
     */
    public static org.pimslims.model.protocol.Protocol createProtocol(final WritableVersion version,
        final String name, final ExperimentType experimentType, final Map attributes)
        throws ConstraintException, AccessException {
        final Map<String, Object> a = new java.util.HashMap<String, Object>(attributes);
        a.put(Protocol.PROP_NAME, name);
        a.put(Protocol.PROP_EXPERIMENTTYPE, experimentType);
        return new Protocol(version, a);
    }

    public static ParameterDefinition createParameterDefinition(final WritableVersion version,
        final Protocol protocol, final Map attributes) throws ConstraintException, AccessException {
        final Map<String, Object> a = new java.util.HashMap<String, Object>(attributes);
        a.put(ParameterDefinition.PROP_PROTOCOL, protocol);
        final ModelObject object = version.create(ParameterDefinition.class, a);
        return (ParameterDefinition) object;
    }

/*
    public static ModelObject createParameterDefinition(final WritableVersion version,
        final ModelObject protocol, final Map attributes) throws ConstraintException, AccessException {
        final Map<String, Object> a = new java.util.HashMap<String, Object>(attributes);
        a.put("protocol", protocol);
        final ModelObject object = version.create(ParameterDefinition.class, a);
        return object;
    } */

    // access methods
    @SuppressWarnings("unchecked")
    public static String getExperimentType(final ModelObject protocol) {
        final List types = new java.util.ArrayList(protocol.get("experimentType"));
        if (0 == types.size()) {
            return null;
        }
        final ModelObject type = (ModelObject) types.get(0);
        return type.get_Name();
    }
/*
    public static boolean hasExperiments(final ModelObject protocol) {
        final Collection experiments = protocol.get("experiments");
        return 0 != experiments.size();
    } */

/*
    public static void deleteProcedure(final WritableVersion wv, final ModelObject procedure)
        throws AccessException, ConstraintException {
        // Collection al=procedure.get("protocol");
        // ModelObject protocol = (ModelObject)al.toArray()[0];
        for (final Iterator iter = procedure.get("steps").iterator(); iter.hasNext();) {
            final ModelObject step = (ModelObject) iter.next();
            wv.delete(step);
        }
        wv.delete(procedure);
        // int count=1;

    } */

/*
    public static void moveProcedureUp(final ModelObject thisProcedure) throws AccessException,
        ConstraintException {
        final Integer thisProcNum = (Integer) thisProcedure.get_Value("procNumber");
        if (1 == thisProcNum.intValue()) {
            throw new RuntimeException("Tried to move first procedure up");
        }

        final Integer otherProcNum = new Integer(thisProcNum.intValue() - 1);
        final Collection al = thisProcedure.get("protocol");
        final ModelObject protocol = (ModelObject) al.toArray()[0];
        final List procedures = new ArrayList(protocol.get("procedures"));
        ModelObject otherProcedure = null;
        for (final Iterator i = procedures.iterator(); i.hasNext();) {
            final ModelObject currentProcedure = (ModelObject) i.next();
            final Integer currentProcNumber = (Integer) currentProcedure.get_Value("procNumber");
            if (currentProcNumber.intValue() == otherProcNum.intValue()) {
                otherProcedure = currentProcedure;
                continue;
            }
        }
        if (null == otherProcedure) {
            throw new RuntimeException("Tried to move procedure up, but no previous procedure found");
        }
        thisProcedure.set_Value("procNumber", otherProcNum);
        otherProcedure.set_Value("procNumber", thisProcNum);
    } */
/*
    public static void moveProcedureDown(final ModelObject thisProcedure) throws AccessException,
        ConstraintException {
        final Integer thisProcNum = (Integer) thisProcedure.get_Value("procNumber");
        final Collection al = thisProcedure.get("protocol");
        final ModelObject protocol = (ModelObject) al.toArray()[0];
        final List procedures = new ArrayList(protocol.get("procedures"));
        if (procedures.size() == thisProcNum.intValue()) {
            throw new RuntimeException("Tried to move last procedure down");
        }

        final Integer otherProcNum = new Integer(thisProcNum.intValue() + 1);
        ModelObject otherProcedure = null;
        for (final Iterator i = procedures.iterator(); i.hasNext();) {
            final ModelObject currentProcedure = (ModelObject) i.next();
            final Integer currentProcNumber = (Integer) currentProcedure.get_Value("procNumber");
            if (currentProcNumber.intValue() == otherProcNum.intValue()) {
                otherProcedure = currentProcedure;
                continue;
            }
        }
        if (null == otherProcedure) {
            throw new RuntimeException("Tried to move procedure down, but no next procedure found");
        }
        thisProcedure.set_Value("procNumber", otherProcNum);
        otherProcedure.set_Value("procNumber", thisProcNum);
    } */
/*
    public static void moveStepUp(final ModelObject thisStep) throws AccessException, ConstraintException {
        final Integer thisStepNum = (Integer) thisStep.get_Value("stepNumber");
        if (1 == thisStepNum.intValue()) {
            throw new RuntimeException("Tried to move first step in procedure up");
        }

        final Integer otherStepNum = new Integer(thisStepNum.intValue() - 1);
        final Collection al = thisStep.get("procedure");
        final ModelObject procedure = (ModelObject) al.toArray()[0];

        final List steps = new ArrayList(procedure.get("steps"));
        ModelObject otherStep = null;
        for (final Iterator i = steps.iterator(); i.hasNext();) {
            final ModelObject currentStep = (ModelObject) i.next();
            final Integer currentStepNumber = (Integer) currentStep.get_Value("stepNumber");
            if (currentStepNumber.intValue() == otherStepNum.intValue()) {
                otherStep = currentStep;
                continue;
            }
        }
        if (null == otherStep) {
            throw new RuntimeException("Tried to move step up, but no previous step found");
        }
        thisStep.set_Value("stepNumber", otherStepNum);
        otherStep.set_Value("stepNumber", thisStepNum);
    } */
/*
    public static void moveStepDown(final ModelObject thisStep) throws AccessException, ConstraintException {
        final Integer thisStepNum = (Integer) thisStep.get_Value("stepNumber");
        final Collection al = thisStep.get("procedure");
        final ModelObject procedure = (ModelObject) al.toArray()[0];
        final List steps = new ArrayList(procedure.get("steps"));
        if (steps.size() == thisStepNum.intValue()) {
            throw new RuntimeException("Tried to move last step in procedure down");
        }

        final Integer otherStepNum = new Integer(thisStepNum.intValue() + 1);
        ModelObject otherStep = null;
        for (final Iterator i = steps.iterator(); i.hasNext();) {
            final ModelObject currentStep = (ModelObject) i.next();
            final Integer currentStepNumber = (Integer) currentStep.get_Value("stepNumber");
            if (currentStepNumber.intValue() == otherStepNum.intValue()) {
                otherStep = currentStep;
                continue;
            }
        }
        if (null == otherStep) {
            throw new RuntimeException("Tried to move step down, but no next step found");
        }
        thisStep.set_Value("stepNumber", otherStepNum);
        otherStep.set_Value("stepNumber", thisStepNum);
    } */

}
