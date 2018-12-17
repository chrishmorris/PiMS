/*
 * Created on 28-Apr-2005 @author: Chris Morris
 */
package org.pimslims.lab.create;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;

/**
 * Factory method for org.pimslims.model.experiment
 * 
 * @see org.pimslims.dao.WritableVersionImpl#create(java.lang.Class, java.util.Map) If a protocol is supplied,
 *      this method creates the parameter definitions
 */
public class ExperimentFactory {

    /**
     * Add Experiment.Parameters for each Protocol.ParameterDefinition
     * 
     * @param experiment the experiment to process
     * 
     * @throws ConstraintException
     * @throws AccessException
     */
	@Deprecated
	// beleived not used
    public static void makeParameters(final Experiment experiment) throws ConstraintException,
        AccessException {
        ExperimentFactory.createProtocolParametersForExperiment((WritableVersion) experiment.get_Version(),
            experiment.getProtocol(), experiment);
    }

	/**
     * Is this experiment potentially for a complex?
     * 
     * @param experiment
     * @return boolean could this experiment be for a complex?
     */
    public static boolean isComplexExperiment(final Experiment experiment) {
        //System.out.println("ExperimentFactory.isComplexExperiment");
        final Set<Target> targets = ExperimentFactory.getExperimentTargets(experiment);
        //for (final Target target : targets) {
        //    System.out.println("ExperimentFactory.isComplexExperiment [" + target.getName() + "]");
        //}
        if (targets.size() > 1) {
            return true;
        }
        return false;
    }

    /**
     * 
     * @param experiment
     * @return Set of all targets for inputSamples of this experiment
     */
    private static Set<Target> getExperimentTargets(final Experiment experiment) {
        //System.out.println("ExperimentFactory.getExperimentTargets");
        final Set<Target> targets = new HashSet<Target>();
        final Collection<InputSample> inputSamples = experiment.getInputSamples();
        for (final InputSample inputSample : inputSamples) {
            final Sample sample = inputSample.getSample();
            if (null != sample) {
                final OutputSample outputSample = sample.getOutputSample();
                if (null != outputSample) {
                    final Experiment exp = outputSample.getExperiment();
                    if (null != exp) {
                        final ResearchObjective blueprint = (ResearchObjective) exp.getProject();
                        if (null != blueprint) {
                            final Collection<ResearchObjectiveElement> components =
                                blueprint.getResearchObjectiveElements();
                            for (final ResearchObjectiveElement roe : components) {
                                if (null != roe.getTarget()) {
                                    targets.add(roe.getTarget());
                                }
                            }
                        }
                    }
                }
            }
        }
        return targets;
    }

    /**
     * 
     * @param experiment
     * @return the ResearchObjective for the complex that satisfies this experiment
     */
    public static ResearchObjective getComplex(final Experiment experiment) {
        //System.out.println("ExperimentFactory.getComplex");

        // Get a list of all complexes that could be involved with this experiment
        final Set<ResearchObjective> expBlueprints = new HashSet<ResearchObjective>();
        final Set<Target> targets = ExperimentFactory.getExperimentTargets(experiment);

        for (final Target target : targets) {
            final Collection<ResearchObjectiveElement> blueprintComponents =
                target.getResearchObjectiveElements();
            for (final ResearchObjectiveElement blueprintComponent : blueprintComponents) {
                if (blueprintComponent.getComponentType().equals("complex")) {
                    expBlueprints.add(blueprintComponent.getResearchObjective());
                }
            }
        }

        // exclude complexes that don't involve all of the targets from this experiment
        final Set<ResearchObjective> complexes = new HashSet<ResearchObjective>();
        for (final ResearchObjective expBlueprint : expBlueprints) {
            final Collection<ResearchObjectiveElement> blueprintComponents =
                expBlueprint.getResearchObjectiveElements();

            final Set<Target> componentTargets = new HashSet<Target>();
            for (final ResearchObjectiveElement blueprintComponent : blueprintComponents) {
                componentTargets.add(blueprintComponent.getTarget());
            }
            if (componentTargets.containsAll(targets)) {
                complexes.add(expBlueprint);
            }
        }

        // TODO what to do if there is more than one possible complex? 
        if (complexes.size() == 1) {
            return complexes.iterator().next();
        }
        return null;
    }

    private ExperimentFactory() {
        // can't contruct this class, only supplies static methods
    }

    /**
     * 
     * @param writeVersion
     * @param experimentMO
     * @param protocolMO
     * 
     * @throws ConstraintException
     * @throws AccessException
     */
    public static void createProtocolParametersForExperiment(final WritableVersion wv,
        final Protocol protocol, final Experiment experiment) throws AccessException, ConstraintException {

        // find all the existing ones
        final Collection<Parameter> parms = experiment.getParameters();
        final Collection<String> names = new HashSet(parms.size());
        for (final Iterator iter = parms.iterator(); iter.hasNext();) {
            final Parameter parm = (Parameter) iter.next();
            names.add(parm.getName());
        }

        final Collection<ParameterDefinition> paramDefCol = protocol.getParameterDefinitions();
        for (final ParameterDefinition paramDef : paramDefCol) {
            if (names.contains(paramDef.getName())) {
                continue;
            } // don't make duplicate
            final Map attrMap = new HashMap();
            attrMap.put(Parameter.PROP_EXPERIMENT, experiment);
            attrMap.put(Parameter.PROP_PARAMETERDEFINITION, paramDef);
            attrMap.put(Parameter.PROP_NAME, paramDef.getName());
            attrMap.put(Parameter.PROP_PARAMTYPE, paramDef.getParamType());
            attrMap.put(Parameter.PROP_VALUE, paramDef.getDefaultValue());
			Parameter created = wv.create(Parameter.class, attrMap);
			if (paramDef.getIsResult()) {
				continue;
			}

			List<InputSample> iss = experiment.getInputSamples();
			for (Iterator iterator = iss.iterator(); iterator.hasNext();) {
				InputSample is = (InputSample) iterator.next();
				if (null != is.getSample()
						&& null != is.getSample().getOutputSample()) {
					Experiment previous = is.getSample().getOutputSample()
							.getExperiment();
					Parameter previousParameter = previous.findFirst(
							Experiment.PROP_PARAMETERS, Parameter.PROP_NAME,
							paramDef.getName());
					if (null != previousParameter) {
						created.setValue(previousParameter.getValue());
					}
				}
			}
        }
    }// EndOf createParametersForExperiment

	/**
	 * HolderFactory.propagateExperimentProperties
	 * @param input
	 * @param experiment
	 * @throws ConstraintException
	 */
	public static void propagateExperimentProperties(final Sample input,
			final Experiment experiment) throws ConstraintException {
		if (null == input) {
			return;
		}
		final OutputSample os = input.getOutputSample();

		if (null == os) {
			return;
		}
		final Experiment before = os.getExperiment();
		experiment.setProject(before.getProject()); // TODO what about
													// complexation, bicistronic
													// infusion?
		// propagate parameters, e.g. sequence
		Set<Parameter> parameters = experiment.getParameters();
		for (Iterator iterator = parameters.iterator(); iterator.hasNext();) {
			Parameter parameter = (Parameter) iterator.next();
			if (null != parameter.getValue()
					&& !"".equals(parameter.getValue())) {
				continue;
			}
			ParameterDefinition pdef = parameter.getParameterDefinition();
			if (null == pdef || false != pdef.getIsResult()) {
				continue;
			} // this is a setup parameter, and not yet populated
			Parameter previous = before.findFirst(Experiment.PROP_PARAMETERS,
					Parameter.PROP_NAME, pdef.getName());
			if (null != previous) {
				parameter.setValue(previous.getValue());
			}
			;
		}
	}
}
