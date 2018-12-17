/**
 * current-pims-web org.pimslims.lab.experiment ExperimentCopier.java
 * 
 * @author Petr Troshin
 * @date 13 Mar 2008
 * 
 *       Protein Information Management System
 * @version: 2.1
 * 
 *           Copyright (c) 2008 Petr
 * 
 * 
 */
package org.pimslims.lab.experiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.lab.Utils;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Method;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.people.Group;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Milestone;

/**
 * ExperimentCopier For deep copying of Experiments
 */
public class ExperimentCopier {

    /**
     * Must be used in synchronized context is in multithreaded environment
     * 
     * @param dupl
     * @param objects
     * @param wv
     * @throws AccessException
     * @throws ConstraintException
     */
    static void duplicateOutputSamples(final Experiment dupl, final Set objects, final WritableVersion wv)
        throws AccessException, ConstraintException {

        final ArrayList<OutputSample> refOuts = new ArrayList<OutputSample>(objects.size());
        for (final Object mobj : objects) {

            final Map<String, Object> attributes = new HashMap<String, Object>();
            attributes.put(OutputSample.PROP_EXPERIMENT, dupl);
            attributes.put(LabBookEntry.PROP_ACCESS, wv.getFirstProject());
            final OutputSample outSample = new OutputSample(wv, attributes);

            final OutputSample oldSample = ((OutputSample) mobj);
            Util.duplicate(outSample, Utils.deleteNullValues(((ModelObject) mobj).get_Values()));
            // Now link sample with RefSample
            final RefOutputSample refSample = ((OutputSample) mobj).getRefOutputSample();
            if (refSample != null) {
                outSample.setRefOutputSample(refSample);
            }

            // Now link OutputSample with Sample
            final Sample sample = oldSample.getSample();
            //final Long time = System.currentTimeMillis();
            if (sample != null) {
                String name = sample.getName();
                if (Util.isEmpty(name)) {
                    name = refSample.getName();
                } else {
                    final int idx = name.indexOf("_");
                    if (idx > 0) {
                        // Assume that the name ends with timestamp
                        try {
                            Long.decode(name.substring(idx + 1));
                            // if above was the case then trim the timestamp
                            name = name.substring(0, idx);
                        } catch (final NumberFormatException e) {
                            // do nothing
                        }
                    }
                }

                // A Sample can be associate only to one OutputSample at a time, thus, needs to be copied here
                final Map<String, Object> attr = new HashMap<String, Object>();
                attr.put(AbstractSample.PROP_NAME, Util.makeName(wv, sample.getName(), Sample.class));
                attr.put(LabBookEntry.PROP_ACCESS, wv.getFirstProject());
                final Sample duplSample = new Sample(wv, attr);

                //final Sample duplSample = new Sample(wv, Util.makeName(wv, sample.getName(), Sample.class));
                final Map<String, Object> prop = Utils.deleteNullValues(((ModelObject) sample).get_Values());
                prop.remove("name"); // name is already set
                Util.duplicate(duplSample, prop);

                duplSample.setSampleCategories(sample.getSampleCategories());
                outSample.setSample(duplSample);
            }

            refOuts.add(outSample);
        }
        dupl.setOutputSamples(new HashSet<OutputSample>(refOuts));
    }

    /**
     * Must be used in synchronized context is in multithreaded environment
     * 
     * @param dupl
     * @param objects
     * @param wv
     * @throws AccessException
     * @throws ConstraintException
     */
    static void duplicateInputSamples(final Experiment dupl, final List objects, final WritableVersion wv)
        throws AccessException, ConstraintException {
        final ArrayList<InputSample> inps = new ArrayList<InputSample>(objects.size());
        for (final Object mobj : objects) {

            final Map<String, Object> attributes = new HashMap<String, Object>();
            attributes.put(InputSample.PROP_EXPERIMENT, dupl);
            attributes.put(LabBookEntry.PROP_ACCESS, wv.getFirstProject());
            final InputSample inSample = new InputSample(wv, attributes);

            final InputSample oldInSample = ((InputSample) mobj);
            Util.duplicate(inSample, Utils.deleteNullValues(((ModelObject) mobj).get_Values()));
            // Now link InputSample with RefSample
            final RefInputSample refSample = oldInSample.getRefInputSample();
            if (refSample != null) {
                inSample.setRefInputSample(refSample);
            }
            // Now link InputSample with Sample
            final Sample sample = oldInSample.getSample();
            if (sample != null) {
                inSample.setSample(sample);
            }
            inps.add(inSample);
        }
        dupl.setInputSamples(inps);
    }

    /**
     * Must be used in synchronized context is in multithreaded environment
     * 
     * @param dupl
     * @param objects
     * @param wv
     * @throws AccessException
     * @throws ConstraintException
     */
    static void duplicateParameters(final Experiment dupl, final Set objects, final WritableVersion wv)
        throws AccessException, ConstraintException {
        final ArrayList<Parameter> pars = new ArrayList<Parameter>(objects.size());
        for (final Object mobj : objects) {

            final Map<String, Object> attributes = new HashMap<String, Object>();
            attributes.put(Parameter.PROP_EXPERIMENT, dupl);
            attributes.put(LabBookEntry.PROP_ACCESS, wv.getFirstProject());
            final Parameter par = new Parameter(wv, attributes);

            final Parameter oldPar = (Parameter) mobj;
            Util.duplicate(par, Utils.deleteNullValues(((ModelObject) mobj).get_Values()));
            // Now link parameter with ParameterDefinitions
            final ParameterDefinition pd = oldPar.getParameterDefinition();
            if (pd != null) {
                par.setParameterDefinition(pd);
            }
            pars.add(par);
        }
        dupl.setParameters(new HashSet<Parameter>(pars));
    }

    /**
     * Must be used in synchronized context is in multithreaded environment
     * 
     * @param dupl
     * @param objects
     * @param wv
     * @throws AccessException
     * @throws ConstraintException
     */
    static void duplicateMilestones(final Experiment dupl, final Set objects, final WritableVersion wv)
        throws AccessException, ConstraintException {
        final ArrayList<Milestone> miles = new ArrayList<Milestone>(objects.size());
        for (final Object mobj : objects) {

            final Map<String, Object> attributes = new HashMap<String, Object>();
            attributes.put(Milestone.PROP_EXPERIMENT, dupl);
            attributes.put(LabBookEntry.PROP_ACCESS, wv.getFirstProject());
            final Milestone oldMile = new Milestone(wv, attributes);

            final Milestone mile =
                new Milestone(wv, oldMile.getDate(), oldMile.getStatus(), oldMile.getTarget());
            Util.duplicate(mile, Utils.deleteNullValues(((ModelObject) mobj).get_Values()));
            miles.add(mile);
            // ResearchObjective exp = oldMile.getResearchObjective();
        }
        dupl.setMilestones(miles);
    }

    /**
     * Deep experiment copy method. This method deliberately does not copy files and Annotations!
     * 
     * @param experiment
     * @param rw
     * @return
     * @throws ConstraintException
     * @throws AccessException
     * 
     */
    public static Experiment duplicate(final Experiment experiment, final WritableVersion rw)
        throws ConstraintException, AccessException {
        Experiment dupl = null;

        final Map<String, Object> attributes = new HashMap<String, Object>();
        // This name will be reset later
        attributes.put(Experiment.PROP_NAME, "aa" + System.currentTimeMillis());
        attributes.put(Experiment.PROP_EXPERIMENTTYPE, experiment.getExperimentType());
        attributes.put(Experiment.PROP_ENDDATE, java.util.Calendar.getInstance());
        attributes.put(Experiment.PROP_STARTDATE, java.util.Calendar.getInstance());
        attributes.put(LabBookEntry.PROP_ACCESS, rw.getFirstProject());
        dupl = new Experiment(rw, attributes);

        // This name will be reset later
        //final Calendar tm = java.util.Calendar.getInstance();
        //dupl = new Experiment(rw, "aa" + System.currentTimeMillis(), tm, tm, experiment.getExperimentType());
        assert dupl != null;
        final Map<String, Object> duplParam = Utils.deleteNullValues(experiment.get_Values());
        // Experiment name mast be unique
        // Name is character varing must be < 80 characters
        duplParam.put(Experiment.PROP_NAME, ExperimentCopier.makeName(rw, experiment.get_Name()));
        dupl.set_Values(duplParam);
        ExperimentCopier.duplicateInputSamples(dupl, experiment.getInputSamples(), rw);
        ExperimentCopier.duplicateOutputSamples(dupl, experiment.getOutputSamples(), rw);
        ExperimentCopier.duplicateParameters(dupl, experiment.getParameters(), rw);
        ExperimentCopier.duplicateMilestones(dupl, experiment.getMilestones(), rw);

        final Project tExpBluep = experiment.getProject();
        if (tExpBluep != null) {
            dupl.setProject(tExpBluep);
        }
        final Protocol protocol = experiment.getProtocol();
        if (protocol != null) {
            dupl.setProtocol(protocol);
        }

        final ExperimentGroup expGroup = experiment.getExperimentGroup();
        if (expGroup != null) {
            dupl.setExperimentGroup(expGroup);
        }
        final Group group = experiment.getGroup();
        if (group != null) {
            dupl.setGroup(group);
        }

        final Method method = experiment.getMethod();
        if (method != null) {
            dupl.setMethod(method);
        }
        final User lastEditor = experiment.getLastEditor();
        if (lastEditor != null) {
            dupl.setLastEditor(lastEditor);
        }
        return dupl;
    }

    /**
     * Make sure experiment name is unique
     * 
     * @param experiments
     * @param pname
     * @return
     */

    private static String makeName(final WritableVersion rw, final String pname) {

        return rw.getUniqueName(Experiment.class, pname);
    }

}
