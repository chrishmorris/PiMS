/**
 * current-pims-web org.pimslims.command.DataUpdate ExperimentParamterFixer.java
 * 
 * @author bl67
 * @date 17 Dec 2007
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2007 bl67
 * 
 * 
 */
package org.pimslims.command.DataUpdate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;

/*
 * ExperimentParamterFixer
 *
 */
public class ExperimentParamterDeleter implements IDataFixer {

    /**
     * @throws ConstraintException
     * @throws AccessException
     * @see org.pimslims.command.DataUpdate.IDataFixer#fixData(org.pimslims.dao.WritableVersion)
     */
    public Boolean fixData(final WritableVersion wv) throws ConstraintException, AccessException {

        Boolean everythingIsCorrect = true;
        final Collection<Experiment> exps = wv.getAll(Experiment.class, 0, Integer.MAX_VALUE); // doesn't matter if this is slow
        for (final Experiment exp : exps) {
            final Set<Parameter> pds = exp.getParameters();
            //cacluate parameter to be kept
            final Map<String, Parameter> pdsWillBeKept = new HashMap<String, Parameter>();
            for (final Parameter p : pds) {
                final String name = p.getName();
                if (!pdsWillBeKept.keySet().contains(name)) {
                    pdsWillBeKept.put(name, p);
                } else if (!this.isEmpty(pdsWillBeKept.get(name).getValue())) {
                    //keep the Parameter which has value
                    pdsWillBeKept.put(name, p);
                }

            }
            //delete redundent parameter
            for (final Parameter p : pds) {
                if (!this.isEmpty(p.getName())) {
                    if (!pdsWillBeKept.values().contains(p)) {
                        System.out
                            .println("Deleted: "
                                + p
                                + " as it has no value and its name is duplicated with other parameter within same experiment");
                        p.delete();
                        everythingIsCorrect = false;
                    }
                }

            }

        }

        return everythingIsCorrect;
    }

    /**
     * @param value
     * @return
     */
    private boolean isEmpty(final String value) {
        if (value == null || value.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * @param protocol
     * @param name
     * @return
     * 
     * private Collection<ParameterDefinition> findPdByName(Protocol protocol, String name) { Collection<ParameterDefinition>
     * pds = new HashSet<ParameterDefinition>(); for (ParameterDefinition pd :
     * protocol.getParameterDefinitions()) { if (pd.getName().equalsIgnoreCase(name)) pds.add(pd); }
     * 
     * return pds; }
     */

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#getDescription()
     */
    public String getDescription() {

        return "Delete parameter which has empty value and duplicated with in same experiment";
    }

}
