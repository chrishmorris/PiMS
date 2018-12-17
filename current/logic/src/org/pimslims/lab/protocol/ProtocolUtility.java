package org.pimslims.lab.protocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.lab.Utils;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;

public class ProtocolUtility {

    /**
     * Deep protocol copy method
     * 
     * @param protocol
     * @param rw
     * @return
     * @throws ConstraintException
     * @throws AccessException
     */
    public static Protocol duplicate(final Protocol protocol, final WritableVersion rw)
        throws ConstraintException, AccessException {

        Protocol dupl = null;

        final Map<String, Object> attributes = new HashMap<String, Object>();
        // This name will be reset later
        attributes.put(Protocol.PROP_NAME, "aa" + System.currentTimeMillis());
        attributes.put(Protocol.PROP_EXPERIMENTTYPE, protocol.getExperimentType());
        dupl = new Protocol(rw, attributes);

        assert dupl != null;
        final Map<String, Object> duplParam = Utils.deleteNullValues(protocol.get_Values());
        // Protocol name mast be unique
        // Name is character varing must be < 80 characters
        duplParam.put(Protocol.PROP_NAME,
            Util.makeName(rw, protocol.get_Name(), org.pimslims.model.protocol.Protocol.class));
        dupl.set_Values(duplParam);
        ProtocolUtility.duplicateRefInputSamples(dupl, protocol.getRefInputSamples(), rw);
        ProtocolUtility.duplicateRefOutputSamples(dupl, protocol.getRefOutputSamples(), rw);
        ProtocolUtility.duplicateParameterDefenitions(dupl, protocol.getParameterDefinitions(), rw);

        return dupl;
    }

    static boolean isDigits(String str) {
        str = str.trim();
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Must be used in sinchronized context is in multithreaded environment
     * 
     * @param dupl
     * @param objects
     * @param wv
     * @throws AccessException
     * @throws ConstraintException
     */
	static void duplicateRefInputSamples(final Protocol dupl, final Collection<RefInputSample> objects,
        final WritableVersion wv) throws AccessException, ConstraintException {
        for (final RefInputSample mobj : objects) {

            final Map<String, Object> attributes = new HashMap<String, Object>();
            // This name will be reset later
            attributes.put(RefInputSample.PROP_SAMPLECATEGORY, mobj.getSampleCategory());
            attributes.put(RefInputSample.PROP_PROTOCOL, dupl);
            if (null!=mobj.getRecipe()) {
				attributes.put(RefInputSample.PROP_RECIPE, mobj.getRecipe());
            }
            final RefInputSample refInSample = new RefInputSample(wv, attributes);

            Util.duplicate(refInSample, Utils.deleteNullValues(((ModelObject) mobj).get_Values()));
        }
    }

    /**
     * Must be used in sinchronized context is in multithreaded environment
     * 
     * @param dupl
     * @param list
     * @param wv
     * @throws AccessException
     * @throws ConstraintException
     */
    static void duplicateParameterDefenitions(final Protocol dupl, final List<ParameterDefinition> list,
        final WritableVersion wv) throws AccessException, ConstraintException {
        final ArrayList<ParameterDefinition> parDefs = new ArrayList<ParameterDefinition>(list.size());
        for (final Object mobj : list) {
            final Map<String, Object> values = new HashMap<String, Object>();
            values.put(ParameterDefinition.PROP_PROTOCOL, dupl);
            values.put(ParameterDefinition.PROP_PARAMTYPE, ((ParameterDefinition) mobj).getParamType());
            values.put(ParameterDefinition.PROP_NAME, ((ParameterDefinition) mobj).getName());


            final ParameterDefinition parDef = wv.create(ParameterDefinition.class, values);

            Util.duplicate(parDef, Utils.deleteNullValues(((ModelObject) mobj).get_Values()));
            parDefs.add(parDef);
        }
        dupl.setParameterDefinitions(new LinkedList<ParameterDefinition>(parDefs));
    }

    /**
     * Must be used in sinchronized context is in multithreaded environment
     * 
     * @param dupl
     * @param objects
     * @param wv
     * @throws AccessException
     * @throws ConstraintException
     */
    static void duplicateRefOutputSamples(final Protocol dupl, final Set objects, final WritableVersion wv)
        throws AccessException, ConstraintException {
        for (final Object mobj : objects) {
            final RefOutputSample oldROS = (RefOutputSample) mobj;
            final RefOutputSample copy = new RefOutputSample(wv, oldROS.getSampleCategory(), dupl);
            copy.setName(oldROS.getName());
            Util.duplicate(copy, Utils.deleteNullValues(((ModelObject) mobj).get_Values()));
        }
    }

} // class end
