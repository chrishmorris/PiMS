/**
 * pims-web org.pimslims.presentation ComplexBeanWriter.java
 * 
 * @author Marc Savitsky
 * @date 15 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky * *
 * 
 */
package org.pimslims.presentation;


import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.target.ResearchObjective;

/**
 * ComplexBeanWriter
 * 
 */
public class ComplexBeanWriter {

    public static final String COMPLEXTYPE = "complex";

    public static void setName(final WritableVersion version, final ComplexBean complex)
        throws ConstraintException {
        final ResearchObjective blueprint = (ResearchObjective) version.get(complex.getBlueprintHook());
        blueprint.setCommonName(complex.getName());
        blueprint.setLocalName(complex.getName());
    }

    public static void setWhyChosen(final WritableVersion version, final ComplexBean complex)
        throws ConstraintException {
        final ResearchObjective blueprint = (ResearchObjective) version.get(complex.getBlueprintHook());
        blueprint.setWhyChosen(complex.getWhyChosen());
    }

    public static void setDetails(final WritableVersion version, final ComplexBean complex)
        throws ConstraintException {
        final ResearchObjective blueprint = (ResearchObjective) version.get(complex.getBlueprintHook());
        blueprint.setDetails(complex.getDetails());
    }
}
