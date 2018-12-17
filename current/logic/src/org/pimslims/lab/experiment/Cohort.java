/**
 * V3_1-pims-web org.pimslims.lab.experiment Cohort.java
 * 
 * @author cm65
 * @date 13 Jan 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.lab.experiment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.sample.Sample;

/**
 * Cohort
 * 
 * Represents an collection of experiments, and the subsequence experiments in the pipeline. Used for making a
 * report on a "cohort" of constructs, to show where they got to.
 * 
 */
public class Cohort {

    /**
     * histories Map<WellBean,Collection<List<Experiment>>>
     * 
     * For each well, the relevant histories that start in that well
     */
    private final Map<WellBean, Collection<List<Experiment>>> histories = new HashMap();

    /**
     * protocols List<Protocol> The protocols used at stages 0, 1, ...
     */
    private final List<Protocol> protocols = new ArrayList();

    /**
     * Stage The experiments in a pipeline stage, for this cohort
     * 
     * 
     * public class Stage {
     * 
     * private final Protocol protocol;
     * 
     * public Stage(final Protocol protocol) { super(); this.protocol = protocol; }
     * 
     * public Protocol getProtocol() { return this.protocol; } }
     */

    private final List<Holder> plates = new ArrayList<Holder>();

    /**
     * Constructor for Cohort
     * 
     * @param experiments the experiments that started the cohort
     * 
     * @param group
     */
    public Cohort(final Collection<Experiment> experiments) {
        for (final Iterator iterator = experiments.iterator(); iterator.hasNext();) {
            final Experiment experiment = (Experiment) iterator.next();
            final Holder holder = HolderFactory.getPlate(experiment);
            if (null == holder) {
                continue;
            }
            final String well = HolderFactory.getPositionInHolder(experiment);
            if (!this.plates.contains(holder)) {
                this.plates.add(holder);
            }
            final WellBean wellBean = new WellBean(holder.getName(), well);
            this.histories.put(wellBean, Collections.singleton(Collections.singletonList(experiment)));
        }
        this.setStages(experiments.iterator().next().getProtocol(), 0);
    }

    /**
     * Cohort.getPlates
     * 
     * @return all the plates in the cohort
     */
    List<Holder> getPlates() {
        return this.plates;

    }

    /**
     * Cohort.getNumberOfStages
     * 
     * @return the number of experimental pipeline stages performed
     */
    public int getNumberOfStages() {
        return this.protocols.size();
    }

    /**
     * 
     * @param previous the number of stages before this experiment
     * @return
     */
    void setStages(final Protocol protocol, final int previous) {
        this.protocols.add(protocol);
        final Map<Protocol, Integer> protocols = new HashMap(); // protocol => number of times it is used at this level

        // for each well, for each experiment, for each output sample, review next experiment
        boolean finished = true;
        for (final Iterator iterator = this.histories.entrySet().iterator(); iterator.hasNext();) {
            final Map.Entry entry = (Entry) iterator.next();
            final WellBean well = (WellBean) entry.getKey();
            final Collection<List<Experiment>> oldHistories = (Collection) entry.getValue();
            final Collection<List<Experiment>> newHistories = new HashSet();
            this.histories.put(well, newHistories);
            for (final Iterator inWellIt = oldHistories.iterator(); inWellIt.hasNext();) {
                final List<Experiment> history = (List<Experiment>) inWellIt.next();
                newHistories.add(history); // remove it if there is a next experiment
                if (history.size() < previous) {
                    continue;
                }
                final Experiment experiment = history.get(history.size() - 1);
                if ("Failed".equals(experiment.getStatus())) {
                    continue;
                }
                final Set<OutputSample> oss = experiment.getOutputSamples();
                // TODO could also use ExperimentModel.isCharacterisationExperiment
                for (final Iterator it2 = oss.iterator(); it2.hasNext();) {
                    final OutputSample outputSample = (OutputSample) it2.next();
                    final Sample sample = outputSample.getSample();
                    if (null == sample) {
                        if (!newHistories.contains(history)) {
                            newHistories.add(history);
                        }
                        continue;
                    }
                    final Set<InputSample> iss = sample.getInputSamples();
                    for (final Iterator iterator2 = iss.iterator(); iterator2.hasNext();) {
                        final InputSample inputSample = (InputSample) iterator2.next();
                        final Experiment nextExp = inputSample.getExperiment();
                        if (nextExp.getOutputSamples().isEmpty()) {
                            if (!newHistories.contains(history)) {
                                newHistories.add(history);
                            }
                            continue;// ignore QA
                        }
                        finished = false; // will need another iteration
                        //TODO could also call ExperimentModel.isCharacterisationExperiment
                        final Protocol nextProtocol = nextExp.getProtocol();
                        assert null != nextProtocol : "No protocol for experiment: " + nextExp.getName();
                        if (!protocols.containsKey(nextProtocol)) {
                            protocols.put(nextProtocol, 0);
                        }
                        protocols.put(nextProtocol, 1 + protocols.get(nextProtocol));
                        newHistories.remove(history); // can now do better
                        final List<Experiment> newHistory = new ArrayList(history);
                        if (experiment.getProtocol() != this.protocols.get(previous)) {
                            // it had a minority protocol, was probably a recovery experiment
                            newHistory.remove(experiment);
                        }
                        newHistory.add(newHistory.size(), nextExp);
                        newHistories.add(newHistory);
                    }
                }
            }

        }
        // choose the majority protocol
        Protocol majority = null;
        int numExperiments = 0;
        for (final Iterator iterator2 = protocols.keySet().iterator(); iterator2.hasNext();) {
            final Protocol nextProtocol = (Protocol) iterator2.next();
            if (numExperiments < protocols.get(nextProtocol)) {
                majority = nextProtocol;
                numExperiments = protocols.get(nextProtocol);
            }
        }
        if (null == majority) {
            return;
        }
        if (!finished) {
            this.setStages(majority, previous + 1);
        }
    }

    /**
     * Cohort.getProtocol Return the protocol used at the ith stage
     * 
     * @param stage
     * @return the protocol
     */
    public Protocol getProtocol(final int stage) {
        if (this.protocols.size() <= stage) {
            throw new IllegalArgumentException("This cohort does not have stage: " + stage);
        }
        return this.protocols.get(stage);
    }

    /**
     * Cohort.getWells
     * 
     * @return the well beans, sorted e.g. A01 .. H12
     */
    public List<WellBean> getWells() {
        final List<WellBean> ret = new ArrayList(this.histories.keySet());
        Collections.sort(ret, WellBean.COLUMN_ORDER);
        return ret;
    }

    /**
     * getExperiment
     * 
     * @param wellBean the starting well well
     * @param the stage
     * @return the experiment at that stage, that follows on from the starting well
     */
    public Experiment getExperiment(final WellBean wellBean, final int stage) {

        Collection<List<Experiment>> histories = Cohort.this.histories.get(wellBean);
        if (null == histories) {
            return null;
        }
        if (1 < histories.size()) {
            List<Experiment> longest = new ArrayList();
            for (final Iterator iterator = histories.iterator(); iterator.hasNext();) {
                final List<Experiment> history = (List<Experiment>) iterator.next();
                if (longest.size() <= history.size()) {
                    longest = history;
                }
            }
            // cache the result, so we don't have to do that again
            histories = Collections.singleton(longest);
            Cohort.this.histories.put(wellBean, histories);
        }
        final List<Experiment> history = histories.iterator().next();
        if (history.size() <= stage) {
            return null;
        }
        return history.get(stage);

    }

}
