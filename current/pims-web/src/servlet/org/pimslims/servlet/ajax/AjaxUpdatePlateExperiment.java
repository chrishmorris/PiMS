/**
 * current-pims-web org.pimslims.servlet.plateExperiment UpdatePlateExperiment.java
 * 
 * @author cm65
 * @date 4 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.servlet.ajax;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ConstructMileStoneUtil;
import org.pimslims.lab.Measurement;
import org.pimslims.lab.experiment.ExperimentUtility;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.plateExperiment.PlateExperimentBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * UpdatePlateExperiment
 * 
 * @see org.pimslims.servlet.ajax.UpdatePlateExperiment
 */
public class AjaxUpdatePlateExperiment extends PIMSServlet {

    /**
     * @see org.pimslims.servlet.Update#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Writes editted values to database";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     *      
     *      Before that, the query will be a GET. It will have one parameter like: 
     *      "well" => String[] { "A03", "B04", .... }

     *      And it will write a JSON file like
     *      {
     *       "Buffer" => undefined,
     *       "isShaken" => true,
     *       "Induction" => ""
     *      }
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        throw new UnsupportedOperationException(this.getClass().getName() + " does not accept GET");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     
    
     *      The update will be a POST, and the parameters the servlet receives will
     *      be like:
     *          "well" => String[] { "A03", "B04", .... } 
     *          "Buffer" => String[] { "org.pimslims.model.sample.Sample:4567" }
     *          "isShaken" => String[] { "true" }
     *          "Induction" => String[] { "" } 
     *      Note that as usual updating an input sample can change the
     *      ResearchObjective for an experiment.
     */

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final long start = System.currentTimeMillis();

        //final Map m = request.getParameterMap();
        /*for (final Iterator it = m.entrySet().iterator(); it.hasNext();) {
            final Map.Entry e = (Map.Entry) it.next();
            final String[] values = (String[]) e.getValue();
            final StringBuffer s = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                s.append(values[i] + ",");
            }
            s.deleteCharAt(s.length() - 1);
            System.out.println(this.getClass().getName()+" request parameter [" + e.getKey()
                + ":" + s.toString() + "]");
        } */

        //final String UserName = PIMSServlet.getUsername(request);
        final WritableVersion version = this.getWritableVersion(request, response);
        if (null == version) {
            return;
        }
        try {
            final Collection<PlateExperimentBean> beans =
                AjaxUpdatePlateExperiment.processRequest(version, request.getParameterMap());

            request.setAttribute("plateExperimentBeans", beans);
            version.commit();
            System.out.println(this.getClass().getName() + " [" + (System.currentTimeMillis() - start) + "]");
            final RequestDispatcher rd =
                request.getRequestDispatcher("/JSP/json/PlateExperimentEditExperiments.jsp");
            rd.forward(request, response);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final ParseException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * e.g. is789:sample
     */
    private static final String SAMPLE = "sample";

    /**
     * e.g. is789:amount
     */
    private static final String AMOUNT = "amount";

    private static final String STATUS = "status";

    private static final String MILESTONE = "milestoneAchieved";

    private static final String RESEARCHOBJECTIVE = "researchObjective";

    private static final Pattern EXPERIMENTHOOK = Pattern.compile("^(([a-zA-Z.]+:\\d+):*(.*))$");

    public static Collection<PlateExperimentBean> processRequest(final WritableVersion version,
        final Map<String, String[]> parms) throws ServletException, AccessException, ConstraintException,
        ParseException {

        final Map<Experiment, Collection<ModelObject>> changedExperiments =
            new HashMap<Experiment, Collection<ModelObject>>();
        final Map<String, String> experimentHooks = new HashMap<String, String>();
        //String updateKey = null;
        String updateValue = null;

        for (final Iterator it = parms.entrySet().iterator(); it.hasNext();) {
            final Map.Entry e = (Map.Entry) it.next();
            final String key = (String) e.getKey();
            final String[] value = (String[]) e.getValue();

            //System.out.println("processRequest [" + key + "|" + value[0] + "]");

            final Matcher m = AjaxUpdatePlateExperiment.EXPERIMENTHOOK.matcher(key);
            if (m.matches()) {
                experimentHooks.put(key, value[0]);
            } else {
                //updateKey = key;
                updateValue = value[0];
            }
        }

        //System.out.println("updateKey [" + updateKey + ":" + updateValue + "]");

        ModelObject object = null;
        if (null != updateValue) {
            object = version.get(updateValue);
        }
        //if (null == object) {
        //    System.out.println("Invalid experiment role [" + updateKey + ":" + updateValue + "]");
        //    return Collections.EMPTY_SET;
        //}

        for (final Iterator it = experimentHooks.entrySet().iterator(); it.hasNext();) {

            final Map.Entry entry = (Map.Entry) it.next();
            final String key = (String) entry.getKey();
            final String value = (String) entry.getValue();
            String experimentHook = key;

            // decode keys type of org.pimslims.model.experiment.Experiment:123456:status
            final Matcher m = AjaxUpdatePlateExperiment.EXPERIMENTHOOK.matcher(key);
            if (m.matches()) {
                experimentHook = m.group(2);
            }

            final Experiment experiment = version.get(experimentHook);

            if (null != experiment) {
                final Collection<ModelObject> objects = new HashSet<ModelObject>();
                objects.addAll(AjaxUpdatePlateExperiment.updateExperiment(version, experiment, key, value,
                    object));

                if (!objects.isEmpty()) {
                    if (changedExperiments.containsKey(experiment)) {
                        final Collection<ModelObject> modelObjects = changedExperiments.get(experiment);
                        objects.addAll(modelObjects);
                        changedExperiments.remove(experiment);
                    }
                    changedExperiments.put(experiment, objects);
                }
            }
        }

        return AjaxUpdatePlateExperiment.getPlateBeans(changedExperiments);
    }

    /**
     * 
     * UpdatePlateExperiment.updateExperiment
     * 
     * @param experiment
     * @param key
     * @param value
     * @param object
     * @return
     * @throws ConstraintException
     */
    private static Collection<ModelObject> updateExperiment(final WritableVersion version,
        final Experiment experiment, final String key, final String value, final ModelObject object)
        throws ConstraintException, AccessException {

        /* System.out.println("UpdatePlateExperiment.updateExperiment [" + experiment.get_Name() + ":" + key
            + ":" + value + ":" + object + "]"); */

        if (null == object) {

            if (key.endsWith(AjaxUpdatePlateExperiment.STATUS)) {
                experiment.setStatus(value);
                return Collections.singleton((ModelObject) experiment);
            }

            if (key.endsWith(AjaxUpdatePlateExperiment.MILESTONE)) {
                return AjaxUpdatePlateExperiment.updateMilestone(version, experiment, value);
            }

            if (key.endsWith(AjaxUpdatePlateExperiment.RESEARCHOBJECTIVE)) {
                return AjaxUpdatePlateExperiment.updateResearchObjective(experiment, value);
            }
        }

        if (object instanceof ParameterDefinition) {
            return AjaxUpdatePlateExperiment.updateParameterDefinition(experiment,
                (ParameterDefinition) object, value);
        }

        if (object instanceof RefInputSample) {

            if (key.endsWith(AjaxUpdatePlateExperiment.SAMPLE)) {
                return AjaxUpdatePlateExperiment
                    .updateInputSample(experiment, (RefInputSample) object, value);
            }

            if (key.endsWith(AjaxUpdatePlateExperiment.AMOUNT)) {
                return AjaxUpdatePlateExperiment.updateAmount(experiment, (RefInputSample) object, value);
            }
        }

        return null;
    }

    /**
     * 
     * UpdatePlateExperiment.getPlateBeans
     * 
     * @param changedExperiments
     * @return
     */
    private static Collection<PlateExperimentBean> getPlateBeans(
        final Map<Experiment, Collection<ModelObject>> changedExperiments) {

        final Collection<PlateExperimentBean> plateBeans = new HashSet<PlateExperimentBean>();

        for (final Iterator it = changedExperiments.entrySet().iterator(); it.hasNext();) {
            final Map.Entry e = (Map.Entry) it.next();
            final Experiment experiment = (Experiment) e.getKey();
            final Collection<ModelObject> modelObjects = (Collection<ModelObject>) e.getValue();

            //System.out.println("UpdatePlateExperiment.getPlateBeans [" + experiment.get_Name() + ":"
            //    + modelObject.get_Hook() + "]");

            final PlateExperimentBean bean = new PlateExperimentBean(experiment);
            bean.clearInputSampleBeans();
            bean.clearOutputSampleBeans();
            bean.clearParameterBeans();

            for (final ModelObject modelObject : modelObjects) {
                if (modelObject instanceof InputSample) {
                    bean.addInputSampleBean((InputSample) modelObject);
                }
                if (modelObject instanceof Parameter) {
                    bean.addParameterBean((Parameter) modelObject);
                }
            }

            //System.out.println("UpdatePlateExperiment plateBean [" + bean.getExperimentName() + ":"
            //    + bean.getExperimentStatus() + ":" + bean.getParameterBeans().size() + ":"
            //    + bean.getInputSampleBeans().size() + ":" + bean.getOutputSampleBeans().size() + ":"
            //    + bean.getBluePrintName() + "]");

            plateBeans.add(bean);
        }
        return plateBeans;
    }

    /**
     * @param version
     * @param value
     * @param hook
     * @param risDbId
     * @param ret
     * @throws ConstraintException
     */
    public static Collection<ModelObject> updateInputSample(final Experiment experiment,
        final RefInputSample ris, final String value) throws ConstraintException {

        //System.out.println("UpdatePlateExperiment.updateInputSample [" + experiment.get_Name() + ":"
        //    + ris.get_Name() + ":" + value + "]");

        final Collection<ModelObject> updates = new ArrayList<ModelObject>();
        final InputSample is =
            experiment.findFirst(Experiment.PROP_INPUTSAMPLES, InputSample.PROP_REFINPUTSAMPLE, ris);

        final ReadableVersion version = experiment.get_Version();
        final Sample sample = (Sample) version.get(value);
        assert "".equals(value) || null != sample : "No such sample: " + value;
        is.setSample(sample);
        updates.add(is);

        // Primer? then set the corresponding primer and project
        // is this a Primer?

        if (null != sample) {
            final ResearchObjective expBlueprint =
                (ResearchObjective) AjaxUpdatePlateExperiment.getResearchObjective(sample);
            if (null == experiment.getProject()) {
                experiment.setProject(expBlueprint);
            }
            final Set<SampleComponent> sampleComps = sample.getSampleComponents();
            if (sampleComps.size() == 1) {
                final SampleComponent scomp = sampleComps.iterator().next();
                final Molecule molcomponent =
                    (Molecule) sample.get_Version().get(scomp.getRefComponent().get_Hook());
                if (molcomponent instanceof Primer) {
                    //updates.addAll(UpdatePlateExperiment.updatePrimerValues(experiment, sample));
                    updates.addAll(AjaxUpdatePlateExperiment.updatePrimerDesignSamples(experiment,
                        expBlueprint));
                }
            }
        }
        return updates;
    }

    private static Collection<ModelObject> updateAmount(final Experiment experiment,
        final RefInputSample ris, final String value) throws ConstraintException {

        if (!Measurement.isAmount(value)) {
            return Collections.EMPTY_LIST;
        }
        final InputSample is =
            experiment.findFirst(Experiment.PROP_INPUTSAMPLES, InputSample.PROP_REFINPUTSAMPLE, ris);
        final Measurement amount = Measurement.getMeasurement(value);
        is.setAmount(amount.getFloatValue());
        is.setAmountDisplayUnit(amount.getDisplayUnit());
        is.setAmountUnit(amount.getStorageUnit());
        return Collections.singleton((ModelObject) is);

    }

    /**
     * @param hook
     * @param dbId
     * @param value the value to set
     * @throws ConstraintException
     */
    private static Collection<ModelObject> updateParameterDefinition(final Experiment experiment,
        final ParameterDefinition def, final String value) throws ConstraintException {

        //System.out.println("UpdatePlateExperiment.updateParameterDefinition [" + experiment.get_Name() + ":"
        //    + def.get_Name() + ":" + value + "]");

        final Parameter parameter =
            experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_PARAMETERDEFINITION, def);
        assert null != parameter : "No such parameter: pd" + def.getDbId() + " for: " + experiment.getName();
        parameter.setValue(value);
        return Collections.singleton((ModelObject) parameter);
    }

    private static Collection<ModelObject> updateMilestone(final WritableVersion version,
        final Experiment experiment, final String value) throws ConstraintException, AccessException {

        System.out.println("UpdatePlateExperiment.updateMilestone [" + experiment.get_Name() + ":" + value
            + "]");

        final ResearchObjective expb = (ResearchObjective) experiment.getProject();
        if (null == expb) {
            return Collections.EMPTY_LIST;
        }

        String targetHook = null;
        if (expb.getResearchObjectiveElements() != null && expb.getResearchObjectiveElements().size() > 0) {
            targetHook = expb.getResearchObjectiveElements().iterator().next().getTarget().get_Hook();
        }

        // check existed milestone
        final Set<Milestone> milestones = experiment.getMilestones(); //  is it
        // possible to get multi-milestone in one experiment?
        boolean milestoneFound = false;
        for (final Milestone milestone : milestones) {
            if (expb.equals(milestone.getResearchObjective())) {
                milestoneFound = true;
            }
        }
        if (!milestoneFound && "true".equals(value)) {
            ConstructMileStoneUtil.createMilestone(version, expb, targetHook, experiment.getExperimentType(),
                experiment, new java.util.Date(), "");
        }

        if (milestoneFound && "false".equals(value)) {
            for (final Milestone milestone : milestones) {
                if (expb.equals(milestone.getResearchObjective())) {
                    milestone.delete();
                }
            }
        }

        return Collections.singleton((ModelObject) experiment);
    }

    private static Collection<ModelObject> updateResearchObjective(final Experiment experiment,
        final String value) throws ConstraintException {

        //System.out.println("UpdatePlateExperiment.updateResearchObjective [" + experiment.get_Name() + ":"
        //    + value + "]");

        final Collection<ModelObject> updates = new ArrayList<ModelObject>();
        final ReadableVersion version = experiment.get_Version();
        final ResearchObjective researchObjective = version.get(value);
        experiment.setProject(researchObjective);
        updates.add(experiment);

        if (null != researchObjective) {
            updates
                .addAll(AjaxUpdatePlateExperiment.updatePrimerDesignSamples(experiment, researchObjective));
        }
        return updates;
    }

    /**
     * 
     * UpdatePlateExperiment.getResearchObjective
     * 
     * @param sample
     * @return
     */
    private static Project getResearchObjective(final Sample sample) {

        final OutputSample output = sample.getOutputSample();
        if (null == output) {
            return null;
        }

        final Experiment exp = output.getExperiment();
        if (null == exp) {
            return null;
        }

        return exp.getProject();
    }

    /**
     * 
     * UpdatePlateExperiment.updatePrimerDesignSamples
     * 
     * @param experiment
     * @param expBlueprint
     * @return
     * @throws ConstraintException
     */
    private static Collection<ModelObject> updatePrimerDesignSamples(final Experiment experiment,
        final ResearchObjective expBlueprint) throws ConstraintException {

        final Collection<ModelObject> updates = new ArrayList<ModelObject>();
        final Experiment primerdesignExperiment = ExperimentUtility.getPrimerDesignExperiment(expBlueprint);
        if (null == primerdesignExperiment) {
            return Collections.EMPTY_LIST;
        }

        final Sample forwardPrimerSample = ExperimentUtility.getForwardPrimerSample(primerdesignExperiment);
        final Sample reversePrimerSample = ExperimentUtility.getReversePrimerSample(primerdesignExperiment);
        final Sample templateSample = ExperimentUtility.getTemplateSample(primerdesignExperiment);

        for (final InputSample input : experiment.getInputSamples()) {

            if (null == input.getSample()) {
                SampleCategory inputCategory = null;
                if (null != input.getRefInputSample()) {
                    inputCategory = input.getRefInputSample().getSampleCategory();
                }
                if (null != forwardPrimerSample) {
                    for (final SampleCategory sampleCategory : forwardPrimerSample.getSampleCategories()) {
                        if (sampleCategory == inputCategory) {
                            input.setSample(forwardPrimerSample);
                            updates.add(input);
                        }
                    }
                }

                if (null != reversePrimerSample) {
                    for (final SampleCategory sampleCategory : reversePrimerSample.getSampleCategories()) {
                        if (sampleCategory == inputCategory) {
                            input.setSample(reversePrimerSample);
                            updates.add(input);
                        }
                    }
                }

                if (null != templateSample) {
                    for (final SampleCategory sampleCategory : templateSample.getSampleCategories()) {
                        if (sampleCategory == inputCategory) {
                            input.setSample(templateSample);
                            updates.add(input);
                        }
                    }
                }
            }
        }
        return updates;
    }

}
