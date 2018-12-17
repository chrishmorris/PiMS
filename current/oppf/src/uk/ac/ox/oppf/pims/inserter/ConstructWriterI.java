package uk.ac.ox.oppf.pims.inserter;

/**
 * V5_0-web org.pimslims.servlet.importer ConstructWriterI.java
 * 
 * @author cm65
 * @date 4 Jun 2014
 * 
 *       Protein Information Management System
 * @version: 5.0
 * 
 *           Copyright (c) 2014 cm65 The copyright holder has licenced the STFC to redistribute this software
 */

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.target.ResearchObjective;

/**
 * ConstructWriterI
 */
public interface ConstructWriterI {

    public abstract void createMolComp(WritableVersion version, String category, String seq, String molType,
        String id) throws AccessException, ConstraintException;

    public abstract void createPrimerDesignExperiment(WritableVersion version, Object pimsConstructBean,
        ResearchObjective ro) throws AccessException, ConstraintException;

}