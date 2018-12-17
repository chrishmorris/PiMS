package org.pimslims.presentation.plateExperiment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.ExperimentParameterSetter;
import org.pimslims.lab.experiment.ExperimentValue;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.lab.experiment.LeftToRightGradient;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.mru.MRUController;

public class PlateExperimentUtility {

    /**
     * delete a expgroup and all other objects created in a plateExperiment
     * 
     * @param wv
     * @param groupHook
     * @throws AccessException
     * @throws ConstraintException
     */
    public static void deleteExpGroup(final WritableVersion wv, final String groupHook)
        throws AccessException, ConstraintException {
        final ExperimentGroup group = wv.get(groupHook);
        if (group == null) {
            return;
        }

        final Collection<Holder> holders = HolderFactory.getPlates(group);
        for (final Holder plate : holders) {
            MRUController.deleteObject(plate.get_Hook());
            plate.delete();
        }

        // delete it's exps and related
        for (final Experiment exp : group.getExperiments()) {
            // delete exp's parameters
            wv.delete(exp.getParameters());
            // delete exp's OutputSample and sample
            for (final OutputSample os : exp.getOutputSamples()) {
                if (os.getSample() != null) {
                    MRUController.deleteObject(os.getSample().get_Hook());
                    os.getSample().delete();
                }
            }
            wv.delete(exp.getOutputSamples());
            // delete exp's InputSample
            wv.delete(exp.getInputSamples());
            // delete exp
            MRUController.deleteObject(exp.get_Hook());
            exp.delete();
        }
        MRUController.deleteObject(group.get_Hook());
        group.delete();

    }

    /*
     * I think it would be very useful to be able to set/get values for Samples in the same way as parameters.
     * This method has not been tested
     */
    public static void setGroupValues(final ExperimentGroup group, final ExperimentValue setter,
        final Object value) throws ConstraintException {
        final Set<Experiment> experiments = group.getExperiments();
        for (final Iterator iter = experiments.iterator(); iter.hasNext();) {
            final Experiment experiment = (Experiment) iter.next();
            setter.set(experiment, value);
        }
    }

    public static Map<String, Object> getGroupValues(final ExperimentGroup group, final ExperimentValue setter) {
        final Map<String, Object> ret = new HashMap<String, Object>();
        final Set<Experiment> experiments = group.getExperiments();
        for (final Iterator iter = experiments.iterator(); iter.hasNext();) {
            final Experiment experiment = (Experiment) iter.next();
            ret.put(PlateExperimentUtility.getWell(experiment), setter.get(experiment));
        }
        return ret;
    }

    private static String getWell(final Experiment experiment) {
        return HolderFactory.getPositionInHolder(experiment);
    }

    public static void setGradient(final ExperimentGroup group, final ExperimentParameterSetter setter,
        final LeftToRightGradient gradient) {
        // TODO Auto-generated method stub

    }

    /**
     * @param group a plate experiment
     * @return the ExperimentGroups that processed the products of this group
     */
    public static Set<ExperimentGroup> getNextPlateExperiments(final ExperimentGroup group) {

        final Set<ExperimentGroup> ret = new HashSet<ExperimentGroup>();
        for (final Iterator iter = group.getExperiments().iterator(); iter.hasNext();) {
            final Experiment experiment = (Experiment) iter.next();
            for (final Iterator iterator = experiment.getOutputSamples().iterator(); iterator.hasNext();) {
                final OutputSample os = (OutputSample) iterator.next();
                final Sample sample = os.getSample();
                if (null == sample) {
                    continue;
                }
                for (final Iterator iterator2 = sample.getInputSamples().iterator(); iterator2.hasNext();) {
                    final InputSample is = (InputSample) iterator2.next();
                    final ExperimentGroup next = is.getExperiment().getExperimentGroup();
                    if (null == next) {
                        continue;
                    }
                    ret.add(next);
                }
            }
        }
        return ret;
    }

    /**
     * TODO make test case
     * 
     * @param group a plate experiment
     * @return the ExperimentGroups that produced the inputs of this group
     */
    public static Set<ExperimentGroup> getPreviousPlateExperiments(final ExperimentGroup group) {

        final Set<ExperimentGroup> ret = new HashSet<ExperimentGroup>();

        for (final Experiment experiment : group.getExperiments()) {
            //for (Iterator iter = group.getExperiments().iterator(); iter.hasNext();) {
            //    Experiment experiment = (Experiment) iter.next();

            for (final InputSample is : experiment.getInputSamples()) {
                //for (Iterator iterator = experiment.getInputSamples().iterator(); iterator.hasNext();) {
                //    InputSample is = (InputSample) iterator.next();

                final Sample sample = is.getSample();
                if (null == sample) {
                    continue;
                }

                final OutputSample os = sample.getOutputSample();
                if (null == os) {
                    continue;
                }

                final ExperimentGroup next = os.getExperiment().getExperimentGroup();
                if (null == next) {
                    continue;
                }

                ret.add(next);
            }
        }
        return ret;
    }

    public static Protocol getGroupProtocol(final ExperimentGroup group) {

        final Set<Experiment> experiments = group.getExperiments();
        if (0 == experiments.size()) {
            return null;
        }
        final Object[] objects = experiments.toArray();
        final Experiment experiment = (Experiment) objects[0];
        return experiment.getProtocol();
    }

    /**
     * 
     * PlateUtils.getChildGroups
     * 
     * @param group
     * @return
     */
    public static Collection<ExperimentGroup> getChildGroups(final ExperimentGroup group,
        final boolean withScore) {

        final Collection<ExperimentGroup> groups = new HashSet<ExperimentGroup>();
        for (final Experiment experiment : group.getExperiments()) {
            for (final OutputSample outputSample : experiment.getOutputSamples()) {
                final Sample sample = outputSample.getSample();
                if (null != sample) {
                    for (final InputSample inputSample : sample.getInputSamples()) {
                        final Experiment exp = inputSample.getExperiment();
                        if (null != exp && null != exp.getExperimentGroup()) {
                            if (withScore) {
                                if (PlateExperimentUtility.containsScoreParameter(exp)) {
                                    groups.add(exp.getExperimentGroup());
                                }
                            } else {
                                groups.add(exp.getExperimentGroup());
                            }
                        }
                    }
                }
            }
        }
        return groups;
    }

    /**
     * 
     * PlateExperimentUtility.getParentGroups
     * 
     * @param groups
     * @return
     */
    public static Collection<ExperimentGroup> getParentGroups(final Collection<ExperimentGroup> groups) {

        final long start = System.currentTimeMillis();
        final Collection<ExperimentGroup> parentGroupSet = new HashSet<ExperimentGroup>();
        for (final ExperimentGroup group : groups) {
            /*
            for (final Experiment experiment : group.getExperiments()) {
                for (final InputSample inputSample : experiment.getInputSamples()) {
                    final Sample sample = inputSample.getSample();
                    if (null != sample) {
                        final OutputSample outputSample = sample.getOutputSample();
                        if (null != outputSample) {
                            final Experiment expt = outputSample.getExperiment();
                            if (null != expt && null != expt.getExperimentGroup()) {
                                parentGroupSet.add(expt.getExperimentGroup());
                            }
                        }
                    }
                }
            }
            */
            final long startx = System.currentTimeMillis();
            parentGroupSet.addAll(PlateExperimentDAO.getParentGroups(group));
            System.out.println("PlateExperimentUtility.getParentGroups ["
                + (System.currentTimeMillis() - startx) + "]");
        }

        System.out.println("PlateExperimentUtility.getParentGroups total ["
            + (System.currentTimeMillis() - start) + "]");
        final List<ExperimentGroup> parentGroupList = new ArrayList<ExperimentGroup>();
        parentGroupList.addAll(parentGroupSet);
        Collections.sort(parentGroupList);
        return parentGroupList;
    }

    /**
     * 
     * PlateExperimentUtility.getSiblingGroups
     * 
     * @param groups
     * @return
     */
    public static Collection<ExperimentGroup> getSiblingGroups(final Collection<ExperimentGroup> groups) {

        final Set<ExperimentGroup> siblingGroups = new HashSet<ExperimentGroup>();
        for (final ExperimentGroup group : groups) {
            if (!group.getExperiments().isEmpty()) {
                final Experiment experiment = group.getExperiments().iterator().next();
                if (PlateExperimentUtility.containsScoreParameter(experiment)) {
                    siblingGroups.add(group);
                }
            }
        }

        final List<ExperimentGroup> siblingGroupList = new ArrayList<ExperimentGroup>();
        siblingGroupList.addAll(siblingGroups);
        Collections.sort(siblingGroupList);
        return siblingGroupList;
    }

    /**
     * 
     * PlateUtils.getSiblingPlates
     * 
     * @param group
     * @return
     */
    public static Collection<ExperimentGroup> getSiblingGroups(final ExperimentGroup group) {

        final Collection<ExperimentGroup> parentGroups =
            PlateExperimentUtility.getParentGroups(Collections.singletonList(group));

        final Collection<ExperimentGroup> siblingGroups = new HashSet<ExperimentGroup>();
        for (final ExperimentGroup pgroup : parentGroups) {
            siblingGroups.addAll(PlateExperimentUtility.getChildGroups(pgroup, true));
        }
        siblingGroups.add(group);
        return siblingGroups;
    }

    public static boolean containsScoreParameter(final Experiment experiment) {
        final Collection<Parameter> parameters = experiment.getParameters();
        for (final Parameter parameter : parameters) {
            final ParameterDefinition parameterDefinition = parameter.getParameterDefinition();
            if (null != parameterDefinition && parameterDefinition.getName().startsWith("__")) {
                return true;
            }
        }
        return false;
    }

}
