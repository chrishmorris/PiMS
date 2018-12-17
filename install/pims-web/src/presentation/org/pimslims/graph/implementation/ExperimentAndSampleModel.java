package org.pimslims.graph.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.pimslims.graph.IGraph;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;

/**
 * Represents a graph of experiments and samples.
 * 
 * Note that the current constructors follow all the links which they can find in the database. This might be
 * more detail that we really want in the diagram. In the light of experience, we might devise some relevance
 * criteria.
 * 
 * @author cm65
 * 
 */
public class ExperimentAndSampleModel extends AbstractGraphModel {

    public ExperimentAndSampleModel() {
        this(AbstractGraphModel.MAX_NODES);
    }

    /**
     * Constructor for ExperimentAndSampleModel
     * 
     * @param maxNodes2
     */
    public ExperimentAndSampleModel(final int maxNodes2) {
        super(null, maxNodes2);
    }

    /**
     * The experiments Note that this is order preserving, so the image matches the image map
     */
    protected final Set<Experiment> experiments = new LinkedHashSet<Experiment>();

    /**
     * The samples
     */
    private final Set<Sample> samples = new LinkedHashSet();

    @Override
    public IGraph createGraphModel(final ModelObject object) {
        if (null == object) {
            return this;
        }
        if (object instanceof Experiment || object instanceof Sample) {
            this.complete(Collections.singleton(object));
            return this; // was   return new ExperimentAndSampleModel(object, this.maxNodes);
        } else if (object instanceof ResearchObjective) {
            this.complete(new ArrayList<ModelObject>(((ResearchObjective) object).getExperiments()));
            return this;
        } else {
            throw new IllegalArgumentException("graphs are not currently available for: "
                + object.get_MetaClass().getMetaClassName());
        }
    }

    /**
     * Finds the graph the covers the experiments and samples provided. Applications will usually prefer to
     * use one of the convenience constructors below.
     * 
     * @param version the current transactions
     * @param experiments
     * @param samples
     */
    @SuppressWarnings("unchecked")
    ExperimentAndSampleModel(final Collection<ModelObject> objects, final int maxNodes) {
        super(null, maxNodes);
        this.complete(objects);
    }

    /**
     * Finds the experiments that use a sample, plus those that produced it.
     * 
     * (At present this may show more than the user really wants to see.)
     * 
     * @param sample
     */
    ExperimentAndSampleModel(final ModelObject object) {
        this(object, AbstractGraphModel.MAX_NODES);
    }

    /**
     * Constructor for ExperimentAndSampleModel
     * 
     * @param object
     * @param maxNodes2
     */
    public ExperimentAndSampleModel(final ModelObject object, final int maxNodes2) {
        super(object, maxNodes2);
        this.complete(Collections.singleton(object));
    }

    /**
     * @param modelObject the object to start from in making the graph
     * @return a model of the links from that object
     * 
     */
    /*
    public static IGraph getExperimentAndSampleModel(final ModelObject modelObject) {

        if (modelObject instanceof Experiment) {
            return new ExperimentAndSampleModel(modelObject);
        } else if (modelObject instanceof Sample) {
            return new ExperimentAndSampleModel(modelObject);
        } else if (modelObject instanceof ResearchObjective) {
            return new ExperimentAndSampleModel((ResearchObjective) modelObject);
        } else {
            throw new IllegalArgumentException("graphs are not currently available for: "
                + modelObject.get_MetaClass().getMetaClassName());
        }
    }
    */
    /**
     * Find the experiments and samples produced from a target or a complex of target(s)
     * 
     * @param blueprint represents the target(s) if there is an error reading the ExpBlueprint
     */
    ExperimentAndSampleModel(final ResearchObjective blueprint) {
        this(new ArrayList<ModelObject>(blueprint.getExperiments()), AbstractGraphModel.MAX_NODES);
    }

    /**
     * @return Returns the experiments.
     */
    Set getExperiments() {
        return new LinkedHashSet<Experiment>(this.experiments);
    }

    /**
     * @return Returns the samples.
     */
    Set getSamples() {
        return new LinkedHashSet<Sample>(this.samples);
    }

    /**
     * Indicates if there is a link in the graph. Note that we may decide that not every link in the database
     * is draw. The return from the this method reflects the state of the ExperimentAndSampleModel, not all
     * the information in the database.
     * 
     * @param sample
     * @param experiment
     * @return true if the sample is linked as input for the experiment
     * 
     */
    boolean isInput(final Sample sample, final Experiment experiment) {
        return super.edges.contains(this.newInputLink(sample, experiment));
    }

    /**
     * Indicates if there is a link in the graph. Note that we may decide that not every link in the database
     * is draw. The return from the this method reflects the state of the ExperimentAndSampleModel, not all
     * the information in the database.
     * 
     * @param sample
     * @param experiment
     * @return true if the sample is linked as output of the experiment
     * 
     */
    boolean isOutput(final Sample sample, final Experiment experiment) {
        return super.edges.contains(this.newOutputLink(experiment, sample));
    }

    /**
     * Follows the inputSample and outputSample associations to populate the whole graph.
     * 
     * @param experiments2 the experiments to start from
     * @param samples2 the samples to start from
     * 
     */
    @SuppressWarnings("unchecked")
    private void complete(final Collection<ModelObject> objects) {

        // nodes still to process
        final List<ModelObject> todo_objects = new ArrayList<ModelObject>(objects);

        while (0 < todo_objects.size()) {

            // Quit because the diagram is getting too big
            // TODO look at objects still in todo_objects
            //      if they have links that link to objects that have been removed (now nodes)
            //      flag the nodes as incomplete
            if (this.nodes.size() > this.getMaxNodes()) {
                this.flagIncompleteNodes(todo_objects);
                break;
            }

            final ModelObject object = todo_objects.get(0);
            todo_objects.remove(0);

            if (object instanceof Experiment) {

                final Experiment experiment = (Experiment) object;
                if (this.experiments.contains(experiment)) {
                    continue;
                }

                this.experiments.add(experiment); // show done
                this.getNode(experiment);

                final List<InputSample> input = new ArrayList(experiment.getInputSamples());
                Collections.sort(input); // ensure order is reproducible
                for (final Iterator<InputSample> iter = input.iterator(); iter.hasNext();) {
                    final InputSample is = iter.next();
                    final Sample sample = is.getSample();
                    if (null == sample) {
                        continue;
                    }

                    todo_objects.add(sample); // LATER input_samples
                    super.edges.add(this.newInputLink(sample, experiment));
                }

                final List<OutputSample> outputs = new ArrayList(experiment.getOutputSamples());
                Collections.sort(outputs); // ensure order is reproducible
                for (final OutputSample output : outputs) {
                    final Sample sample = output.getSample();
                    if (null == sample) {
                        continue;
                    }

                    super.edges.add(this.newOutputLink(experiment, sample));
                    todo_objects.add(sample);
                }
            }

            if (object instanceof Sample) {

                final Sample sample = (Sample) object;
                if (this.samples.contains(sample)) {
                    continue;
                }

                this.samples.add(sample); // show done
                this.getNode(sample);

                final OutputSample output = sample.getOutputSample();
                if (null == output) {
                    continue;
                }

                final Experiment producer = output.getExperiment();

                if (null != producer) {
                    todo_objects.add(producer);
                    super.edges.add(this.newOutputLink(producer, sample));
                }

                //TODO if there are many of these, the graph can become wide
                // In that case the failed experiments could be moved down the page
                // as a hint to Graphviz, add invisible links of low weight, 
                // from successful experiments to failed ones.
                final Collection<InputSample> consumers = sample.getInputSamples();
                for (final Iterator<InputSample> iter = consumers.iterator(); iter.hasNext();) {
                    final InputSample is = iter.next();
                    final Experiment consumer = is.getExperiment();

                    if (null != consumer) {
                        todo_objects.add(consumer);
                        super.edges.add(this.newInputLink(sample, consumer));
                    }
                }
            }
        }
    }

    // TODO look at objects still in todo_objects
    //      if they have links that link to objects that have been removed (now nodes)
    //      flag the nodes as incomplete
    private void flagIncompleteNodes(final List<ModelObject> objects) {
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
