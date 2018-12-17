/**
 * V2_3-pims-web org.pimslims.presentation BeanFactory.java
 * 
 * @author cm65
 * @date 14 Aug 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation;

import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.molecule.Construct;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.presentation.experiment.ExperimentBean;
import org.pimslims.presentation.protocol.ProtocolBean;
import org.pimslims.presentation.sample.SampleBean;
import org.pimslims.presentation.target.ResearchObjectiveElementBean;
import org.pimslims.presentation.vector.RefSampleBean;
import org.pimslims.presentation.vector.SampleComponentBean;
import org.pimslims.presentation.vector.VectorBean;

/**
 * BeanFactory
 * 
 * Provides beans of the appropriate type for model objects.
 * 
 */
public class BeanFactory {

    public static ModelObjectBean newBean(final ModelObject modelObject) {

        if (null == modelObject) {
            return null;
        }

        if (modelObject instanceof Construct) {
            return new VectorBean((Construct) modelObject);
        }

        if (modelObject instanceof Experiment) {
            return new ExperimentBean((Experiment) modelObject);
        }

        if (modelObject instanceof Holder) {
            return new HolderBean((Holder) modelObject);
        }

        if (modelObject instanceof Protocol) {
            return new ProtocolBean((Protocol) modelObject);
        }

        if (modelObject instanceof RefSample) {
            return new RefSampleBean((RefSample) modelObject);
        }

        if (modelObject instanceof ResearchObjective) {
            final ResearchObjective researchObjective = (ResearchObjective) modelObject;
            if (ComplexBeanReader.isComplex(researchObjective)) {
                //ComplexBean is not a ModelObjectBean
            } else if (researchObjective.getResearchObjectiveElements().size() == 0) {
                //not construct without ResearchObjectiveElement
            } else {
                return ConstructBeanReader.readConstruct(researchObjective);
            }
        }

        if (modelObject instanceof ResearchObjectiveElement) {
            return new ResearchObjectiveElementBean((ResearchObjectiveElement) modelObject);
        }

        if (modelObject instanceof Sample) {
            return new SampleBean((Sample) modelObject);
        }

        if (modelObject instanceof Target) {
            return new TargetBean((Target) modelObject);
        }
        if (modelObject instanceof SampleComponent) {
            return new SampleComponentBean((SampleComponent) modelObject);
        }

        return new ModelObjectBean(modelObject);
    }
}
