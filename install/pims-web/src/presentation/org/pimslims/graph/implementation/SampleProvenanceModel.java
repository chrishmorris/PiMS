/**
 * pims-web org.pimslims.utils.graph.implementation ExperimentModel.java
 * 
 * @author Marc Savitsky
 * @date 15 Apr 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky * *
 * 
 */
package org.pimslims.graph.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.pimslims.graph.IGraph;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ComplexBeanReader;
import org.pimslims.presentation.construct.ConstructBeanReader;

/**
 * Represents a graph of experiments, for the sample provenance report.
 * 
 * Note that the current constructors follow all the links which they can find in the database. This might be
 * more detail that we really want in the diagram. In the light of experience, we might devise some relevance
 * criteria. TODO this is cloned from ExperimentAndSampleModel. Used in T2CReport.
 * 
 * @author cm65
 * 
 */

public class SampleProvenanceModel extends AbstractGraphModel {

    private final List<Experiment> experiments = new ArrayList<Experiment>();

    /**
     * Constructor for ExperimentModel
     */
    public SampleProvenanceModel(final ModelObject centre) {
        super(centre);
    }

    /**
     * Constructor for testing
     * 
     * @param sample
     * @param maximum allowed nodes
     */
    public SampleProvenanceModel(final Sample sample, final int maxNodes) {
        super(sample, maxNodes);
        this.complete(sample);
    }

    //TODO @Override
    private ModelObjectNode getNode(final LabBookEntry object) {

        if (!this.nodes.containsKey(object.getDbId())) {
            final String classname = object.get_MetaClass().getShortName();
            if (classname.equals(ModelObjectNode.CLASSNAME_SAMPLE)) {
                this.nodes.put(object.getDbId(), new ModelObjectNode(object));
            } else if (classname.equals(ModelObjectNode.CLASSNAME_EXPERIMENT)) {
                this.nodes.put(object.getDbId(), new ModelObjectNode(object));
            } else
            //TODO do these other cases really happen?
            if (classname.equals(ModelObjectNode.CLASSNAME_TARGET)) {
                this.nodes.put(object.getDbId(), new ModelObjectNode(object));
            } else if (classname.equals(ModelObjectNode.CLASSNAME_RESEARCHOBJECTIVE)) {
                final ResearchObjective researchObjective = (ResearchObjective) object;
                if (ComplexBeanReader.isComplex(researchObjective)) {
                    this.nodes.put(object.getDbId(), new ModelObjectNode(object));
                } else {
                    this.nodes.put(object.getDbId(), new ResearchObjectiveNode(object));
                }

            } else {
                this.nodes.put(object.getDbId(), new ModelObjectNode(object));
            }
        }
        return this.nodes.get(object.getDbId());
    }

    @Override
    public IGraph createGraphModel(final ModelObject object) {

        final LabBookEntry baseClass = (LabBookEntry) object;
        if (baseClass instanceof Sample) {
            this.complete((Sample) baseClass);
        }

        return this;
    }

    /**
     * Follows the inputSample and outputSample associations to populate the whole graph.
     * 
     * @param experiments2 the experiments to start from
     * @param samples2 the samples to start from
     * 
     */
    void complete(final Sample thisSample) {

        // nodes still to process
        //System.out.println("ExperimentModel.complete [" + thisSample.getName() + "]");
        final List<Experiment> todo_objects = new ArrayList<Experiment>();

        final OutputSample outputSample = thisSample.getOutputSample();
        if (null != outputSample) {
            this.getNode(thisSample);
            final Experiment experiment = outputSample.getExperiment();
            super.edges.add(this.newOutputLink(experiment, thisSample));
            todo_objects.add(experiment);
        }

        while (0 < todo_objects.size()) {

            // Quit because the diagram is getting too big
            // TODO look at objects still in todo_objects
            //      if they have links that link to objects that have been removed (now nodes)
            //      flag the nodes as incomplete
            if (this.nodes.size() > this.getMaxNodes()) {
                this.flagIncompleteNodes(todo_objects);
                break;
            }

            final Experiment experiment = todo_objects.get(0);
            todo_objects.remove(0);

            if (this.experiments.contains(experiment)) {
                continue;
            }

            this.experiments.add(experiment); // show done
            this.getNode(experiment);

            if (ConstructBeanReader.isConstructDesignExperiment(experiment)) {

                final ResearchObjective construct = experiment.getResearchObjective();
                if (null != construct) {
                    super.edges.add(this.newInputLink(construct, experiment));
                    this.getNode(construct);

                    //System.out.println("ExperimentModel.complete [" + construct.getCommonName() + "]");

                    final Set<ResearchObjectiveElement> roes = construct.getResearchObjectiveElements();
                    roe: for (final Iterator iterator = roes.iterator(); iterator.hasNext();) {
                        final ResearchObjectiveElement roe = (ResearchObjectiveElement) iterator.next();
                        final Target target = roe.getTarget();
                        if (null == target) {
                            continue roe;
                        }
                        super.edges.add(this.newInputLink(target, construct));
                        this.getNode(target);

                        for (final ResearchObjectiveElement component : target.getResearchObjectiveElements()) {

                            //System.out.println("ExperimentModel.complete [" + component.get_Name() + ":"
                            //    + component.getComponentType() + "]");

                            if (component.getComponentType().equals("complex")) {
                                final ResearchObjective complex = component.getResearchObjective();
                                super.edges.add(this.newInputLink(complex, target));
                                this.getNode(complex);
                            }
                        }
                    }
                }
            }

            for (final InputSample is : experiment.getInputSamples()) {
                final Sample sample = is.getSample();
                if (null == sample) {
                    continue;
                }

                final OutputSample output = sample.getOutputSample();
                if (null == output) {
                    continue;
                }

                final Experiment producer = output.getExperiment();

                todo_objects.add(producer); // LATER input_samples
                super.edges.add(this.newInputLink(producer, experiment));
            }

            // /* hmm, seems to add side branches 
            final Collection<OutputSample> outputs = experiment.getOutputSamples();
            for (final OutputSample output : outputs) {
                final Sample sample = output.getSample();
                if (null == sample) {
                    continue;
                }

                final Collection<InputSample> consumers = sample.getInputSamples();
                for (final Iterator<InputSample> iter = consumers.iterator(); iter.hasNext();) {
                    final InputSample is = iter.next();
                    final Experiment consumer = is.getExperiment();

                    if (null != consumer && ConstructBeanReader.isCharacterisationExperiment(consumer)) {
                        //todo_objects.add(consumer);
                        super.edges.add(this.newInputLink(experiment, consumer));
                    }
                }
            } // */
        }
    }

    //TODO can we inherit this?
    // TODO look at objects still in todo_objects
    //      if they have links that link to objects that have been removed (now nodes)
    //      flag the nodes as incomplete
    void flagIncompleteNodes(final List<Experiment> objects) {
        /*
        System.out.println("ExperimentandSamplesModel.flagIncompleteNodes");
        System.out.println("todo objects");
        for (ModelObject object : objects) {
            System.out.println("      [" + object.get_Hook() + "]");
        }

        System.out.println("links");
        for (ModelObjectLink link : links) {
            System.out.println("      [" + link.getHead().getId() + ":" + link.getTail().getId() + "]");
        }

        System.out.println("nodes");
        for (INode node : getNodes()) {
            System.out.println("      [" + node.getId() + "]");
        }
        */
        for (final ModelObject object : objects) {
            //System.out.println("process object [" + object.get_Hook() + ":" + object.get_Name() + "]");

            if (object instanceof Experiment) {

                final Experiment experiment = (Experiment) object;
                final Collection<InputSample> input = experiment.getInputSamples();
                for (final Iterator<InputSample> iter = input.iterator(); iter.hasNext();) {
                    final InputSample is = iter.next();
                    final Sample sample = is.getSample();
                    if (null != sample && !this.nodes.containsKey(sample.getDbId())) {
                        this.flagNode(object);
                    }
                }

                final Collection<OutputSample> outputs = experiment.getOutputSamples();
                for (final OutputSample output : outputs) {
                    final Sample sample = output.getSample();
                    if (null != sample && !this.nodes.containsKey(sample)) {
                        this.flagNode(object);
                    }
                }
            }

            if (object instanceof Sample) {

                final Sample sample = (Sample) object;
                final OutputSample output = sample.getOutputSample();
                if (null == output) {
                    continue;
                }

                final Experiment producer = output.getExperiment();

                if (null != producer && !this.nodes.containsKey(producer)) {
                    this.flagNode(object);
                }

                final Collection<InputSample> consumers = sample.getInputSamples();
                for (final Iterator<InputSample> iter = consumers.iterator(); iter.hasNext();) {
                    final InputSample is = iter.next();
                    final Experiment consumer = is.getExperiment();

                    if (null != consumer && !this.nodes.containsKey(consumer)) {
                        this.flagNode(object);
                    }
                }
            }
        }
    }

}
