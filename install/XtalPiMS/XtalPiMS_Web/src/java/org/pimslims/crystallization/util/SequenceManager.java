/**
 * xtalPiMS_Web org.pimslims.crystallization.util SequenceManager.java
 * 
 * @author cm65
 * @date 21 Nov 2012
 * 
 *       Protein Information Management System
 * @version: 4.3
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.pimslims.business.DataStorage;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.construct.ConstructBean;

/**
 * SequenceManager
 * 
 */
public class SequenceManager {

    private final ReadableVersion version;

    /**
     * Constructor for SequenceManager
     * 
     * @param dataStorage
     */
    public SequenceManager(DataStorage dataStorage) {
        this.version = ((DataStorageImpl) dataStorage).getVersion();
    }

    /**
     * SequenceManager.getSequence
     * 
     * @param barcode
     * @return
     */
    public String getSequence(String barcode) {
        ExperimentGroup group = version.findFirst(ExperimentGroup.class, ExperimentGroup.PROP_NAME, barcode);
        if (null == group) {
            return null;
        }
        org.pimslims.model.experiment.Experiment experiment =
            group.findFirst(ExperimentGroup.PROP_EXPERIMENTS, java.util.Collections.EMPTY_MAP);
        Project project = experiment.getProject();
        if (null == project) {
            return null;
        }
        ConstructBean cb =
            org.pimslims.presentation.construct.ConstructBeanReader
                .readConstruct((ResearchObjective) project);
        return cb.getFinalProt();
    }

    /**
     * SequenceManager.setSequence
     * 
     * @param barcode
     * @param string
     * @throws AccessException
     * @throws ConstraintException
     */
    public void setSequence(String barcode, String sequence) throws ConstraintException, AccessException {
        ExperimentGroup group = version.findFirst(ExperimentGroup.class, ExperimentGroup.PROP_NAME, barcode);
        org.pimslims.model.experiment.Experiment experiment =
            group.findFirst(ExperimentGroup.PROP_EXPERIMENTS, java.util.Collections.EMPTY_MAP);
        if (null == experiment.getProject()) {
            ConstructBean bean = new ConstructBean(new TargetBean());
            bean.setFinalProt(sequence);
            bean.setConstructId(barcode);
            ResearchObjective ro =
                org.pimslims.presentation.construct.ConstructBeanWriter.createNewConstruct(
                    (WritableVersion) version, bean);
            Set<Experiment> experiments = group.getExperiments();
            for (Iterator iterator = experiments.iterator(); iterator.hasNext();) {
                Experiment experiment2 = (Experiment) iterator.next();
                experiment2.setProject(ro);
            }
        } else {
            ResearchObjective ro = (ResearchObjective) experiment.getProject();
            final Collection<ResearchObjectiveElement> elements = ro.getResearchObjectiveElements();
            for (final Iterator iterator = elements.iterator(); iterator.hasNext();) {
                final ResearchObjectiveElement element = (ResearchObjectiveElement) iterator.next();
                for (final Molecule bc : element.getTrialMolComponents()) {
                    for (final ComponentCategory category : bc.getCategories()) {
                        if (category.getName().equals(ConstructBean.FINAL_PROTEIN)) {
                            bc.setSequence(sequence);
                        }
                    }
                }
            }
        }
    }

}
