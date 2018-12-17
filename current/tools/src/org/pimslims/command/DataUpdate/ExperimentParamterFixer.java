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
import java.util.HashSet;

import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;

/**
 * ExperimentParamterFixer
 * 
 */
public class ExperimentParamterFixer implements IDataFixer {

    /**
     * @throws ConstraintException
     * @throws AccessException
     * @see org.pimslims.command.DataUpdate.IDataFixer#fixData(org.pimslims.dao.WritableVersion)
     */
    public Boolean fixData(final WritableVersion wv) throws ConstraintException, AccessException {

        Boolean everythingIsCorrect = true;
        final Collection<Parameter> expParamters = wv.getAll(Parameter.class, 0, Integer.MAX_VALUE); // doesn't matter if this is slow
        final MetaAttribute pTypeAttribute =
            ModelImpl.getModel().getMetaClass(Parameter.class.getName()).getAttribute(
                Parameter.PROP_PARAMTYPE);
        for (final Parameter p : expParamters) {
            ParameterDefinition pd = p.getParameterDefinition();
            String pType = p.getParamType();
            final String pName = p.getName();
            //try to find out the ParameterDefinition from exp's protocol with same Name
            if (pd == null && pName != null && !pName.equals("")) {
                final Experiment exp = p.getExperiment();
                if (exp != null) {
                    final Protocol protocol = exp.getProtocol();
                    if (protocol != null) {
                        final Collection<ParameterDefinition> pdsFound = this.findPdByName(protocol, pName);

                        if (pdsFound.size() == 1) {
                            pd = pdsFound.iterator().next();
                            p.setParameterDefinition(pd);
                            //System.out.println("Set " + p + "'s ParameterDefinition to " + pd  + " which was null!");
                        }
                    }
                }
            }

            if (pd == null) {
                if (!pTypeAttribute.isValid(pType)) {
                    if (p.getValue() == null || p.getValue().trim().length() == 0) {

                        //System.out.println("Deleted: " + p + " as it has no value and no type");
                        p.delete();
                    } else {
                        if (pType.equalsIgnoreCase("Integer")) {
                            pType = "Int";
                        } else {
                            pType = "String";
                        }
                        p.setParamType(pType);
                        //System.out.println("Set " + p + "'s parmType to String!");

                    }
                }
            } else {
                //check ptype with ParameterDefinition
                if (!pType.equals(pd.getParamType())) {
                    p.setParamType(pd.getParamType());
                    //System.out.println("Update " + p + "'s parameterType from " + pType + " to "+ pd.getParamType());
                    everythingIsCorrect = false;
                }
                //check pName with ParameterDefinition
                final String name = p.getName();
                if (!name.equals(pd.getName())) {
                    p.setName(pd.getName());
                    //System.out.println("Update " + p + "'s name from " + name + " to " + pd.getName());
                    everythingIsCorrect = false;
                }
            }

            if (!this.isValidPvalue(p)) {
                everythingIsCorrect = false;
            }

        }

        return everythingIsCorrect;
    }

    /**
     * @param value
     * @return
     * 
     * private boolean isEmpty(final String value) { if (value == null || value.trim().length() == 0) { return
     * true; } return false; }
     */

    /**
     * @param protocol
     * @param name
     * @return
     */
    private Collection<ParameterDefinition> findPdByName(final Protocol protocol, final String name) {
        final Collection<ParameterDefinition> pds = new HashSet<ParameterDefinition>();
        for (final ParameterDefinition pd : protocol.getParameterDefinitions()) {
            if (pd.getName().equalsIgnoreCase(name)) {
                pds.add(pd);
            }
        }

        return pds;
    }

    /**
     * @param p
     * @return
     * @throws ConstraintException
     */
    private boolean isValidPvalue(final Parameter p) throws ConstraintException {
        final String value = p.getValue();
        final String Type = p.getParamType();
        final String name = p.getName();
        if (value == null || value.length() == 0) {
            return true;
        }

        if (Type.equals("Int")) {
            try {
                new Integer(value);
                return true;
            } catch (final NumberFormatException e) {
                if (name.toLowerCase().contains(" overlap")) {
                    p.setValue((new Integer(value.length())).toString());
                    //System.out.println("As an Overlap, " + p + "'s value is changed to " + p.getValue() + " from " + value);
                    return false;
                }
                System.err.println("Warning: " + p + " 's value is '" + value + "' which is an invalid "
                    + Type + " and it can not be corrected automatically");
                return false;
            }
        } else if (Type.equals("Boolean")) {
            if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                return true;
            } else {
                System.err.println("Warning: " + p + " 's value is '" + value + "' which is an invalid "
                    + Type + " and it can not be corrected automatically");
                return false;
            }
        } else if (Type.equals("Float")) {
            try {
                new Float(value);
                return true;
            } catch (final NumberFormatException e) {
                System.err.println("Warning: " + p + " 's value is '" + value + "' which is an invalid "
                    + Type + " and it can not be corrected automatically");
                return false;
            }
        }
        return true;
    }

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#getDescription()
     */
    public String getDescription() {

        return "1, Fix paramter type&name of experiment's paramter if parameter Definition is defined! \n"
            + "2, Also check the value is legal or not\n";
    }

}
