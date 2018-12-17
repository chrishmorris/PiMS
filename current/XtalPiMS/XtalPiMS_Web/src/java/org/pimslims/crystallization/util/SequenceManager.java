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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.business.DataStorage;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.search.Paging;

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
		ExperimentGroup group = version.findFirst(ExperimentGroup.class,
				ExperimentGroup.PROP_NAME, barcode);
		Project project;
		if (null == group) {
			project = null;
		} else {
			org.pimslims.model.experiment.Experiment experiment = group
					.findFirst(ExperimentGroup.PROP_EXPERIMENTS,
							java.util.Collections.EMPTY_MAP);
			project = experiment.getProject();
		}

		return getSequence(project);
	}

	private String getSequence(Project project) {
		if (null == project) {
			return null;
		}
		ConstructBean cb = org.pimslims.presentation.construct.ConstructBeanReader
				.readConstruct((ResearchObjective) project);
		return cb.getFinalProt();
	}

	private ConstructBean getConstructBean(String barcode) {
		ExperimentGroup group = version.findFirst(ExperimentGroup.class,
				ExperimentGroup.PROP_NAME, barcode);
		Project project;
		if (null == group) {
			project = null;
		} else {
			org.pimslims.model.experiment.Experiment experiment = group
					.findFirst(ExperimentGroup.PROP_EXPERIMENTS,
							java.util.Collections.EMPTY_MAP);
			project = experiment.getProject();
		}
		ConstructBean cb;
		if (null == project) {
			cb = null;
		} else {
			cb = org.pimslims.presentation.construct.ConstructBeanReader
					.readConstruct((ResearchObjective) project);
		}
		return cb;
	}

	/**
	 * SequenceManager.setSequence
	 * 
	 * @param barcode
	 * @param string
	 * @return
	 * @throws AccessException
	 * @throws ConstraintException
	 */
	private ResearchObjective setSequence(final String barcode,
			final String sequence) throws ConstraintException, AccessException {
		ExperimentGroup group = version.findFirst(ExperimentGroup.class,
				ExperimentGroup.PROP_NAME, barcode);
		org.pimslims.model.experiment.Experiment experiment = group.findFirst(
				ExperimentGroup.PROP_EXPERIMENTS,
				java.util.Collections.EMPTY_MAP);
		ResearchObjective ro = (ResearchObjective) experiment.getProject();
		if (null != ro) {
			// TODO if used in other plate, probably shouldn't change the
			// sequence
		} else {
			if (null != sequence) {
				ro = this.getResearchObjective(sequence);
			}
			if (null == ro) {
				ConstructBean bean = new ConstructBean(new TargetBean());
				// was bean.setFinalProt(sequence);
				// TODO bean.getTargetBean().setName(acronym);
				bean.setConstructId(barcode);
				ro = org.pimslims.presentation.construct.ConstructBeanWriter
						.createNewConstruct((WritableVersion) version, bean);
			}
			Set<Experiment> experiments = group.getExperiments();
			for (Iterator iterator1 = experiments.iterator(); iterator1
					.hasNext();) {
				Experiment experiment2 = (Experiment) iterator1.next();
				experiment2.setProject(ro);
			}
		}
		if (sequence.equals(this.getSequence(ro))) {
			// nothing to do
		} else {
			final Collection<ResearchObjectiveElement> elements = ro
					.getResearchObjectiveElements();
			roe: for (final Iterator iterator = elements.iterator(); iterator
					.hasNext();) {
				final ResearchObjectiveElement element = (ResearchObjectiveElement) iterator
						.next();
				for (final Molecule bc : element.getTrialMolComponents()) {
					for (final ComponentCategory category : bc.getCategories()) {
						if (category.getName().equals(
								ConstructBean.FINAL_PROTEIN)) {
							bc.setSequence(sequence);
							break roe;
						}
					}
				}
			}
			/*
			 * was throw new AssertionError(
			 * "Please load reference data, missing Component Category: " +
			 * ConstructBean.FINAL_PROTEIN);
			 */
		}
		return ro;
	}

	private ResearchObjective getResearchObjective(String finalProteinSequence) {
		assert null != finalProteinSequence;
		Map criteria = new HashMap(2);
		criteria.put(Molecule.PROP_SEQUENCE, finalProteinSequence);
		criteria.put(Molecule.PROP_CATEGORIES, Collections.singletonMap(
				ComponentCategory.PROP_NAME, ConstructBean.FINAL_PROTEIN));
		Molecule protein = version.findFirst(Molecule.class, criteria);
		if (null == protein) {
			return null;
		}
		Collection<ResearchObjectiveElement> roes = protein
				.getRelatedResearchObjectiveElements();
		if (1 != roes.size()) {
			return null; // not a construct, or cant know which construct is
							// right
		}
		return roes.iterator().next().getResearchObjective();
	}

	private void setAcronym(ResearchObjective ro, String acronym)
			throws ConstraintException, AccessException {
		final Collection<ResearchObjectiveElement> elements = ro
				.getResearchObjectiveElements();
		Target target = null;
		for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
			ResearchObjectiveElement roe = (ResearchObjectiveElement) iterator
					.next();
			if (null != roe.getTarget()) {
				if (null != target) {
					throw new AssertionError(
							"Bicistronic constructs are not yet supported");
				}
				target = roe.getTarget();
			}
		}
		if (null == target) {
			// no target is assigned to this construct
			WritableVersion wv = (WritableVersion) this.version;
			target = wv.findFirst(Target.class, Target.PROP_NAME, acronym);
			if (null == target) {
				Molecule protein = new Molecule(wv, "protein", acronym);
				target = new Target(wv, acronym, protein);
			}
			new ResearchObjectiveElement(wv, "target", acronym, ro)
					.setTarget(target);
			wv.flush();
		} else {
			if (!target.getName().equals(acronym)) {
				throw new AssertionError("That construct is for target: "
						+ target.getName()); // was target.setName(acronym);
			}
		}
	}

	public String getAcronym(String barcode) throws ConstraintException,
			AccessException {
		ExperimentGroup group = version.findFirst(ExperimentGroup.class,
				ExperimentGroup.PROP_NAME, barcode);
		org.pimslims.model.experiment.Experiment experiment = group.findFirst(
				ExperimentGroup.PROP_EXPERIMENTS,
				java.util.Collections.EMPTY_MAP);
		ResearchObjective ro1 = (ResearchObjective) experiment.getProject();
		if (null == ro1) {
			return null;
		}
		ResearchObjective ro = ro1;
		Target target = null;
		final Collection<ResearchObjectiveElement> elements = ro
				.getResearchObjectiveElements();
		for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
			ResearchObjectiveElement roe = (ResearchObjectiveElement) iterator
					.next();
			if (null != roe.getTarget()) {
				if (null != target) {
					throw new AssertionError(
							"Bicistronic constructs are not yet supported");
				}
				target = roe.getTarget();
			}
		}
		if (null == target) {
			return null;
		}
		return target.getName();
	}

	public Collection<ConstructBean> getConstructs(String acronym) {
		Target target = this.version.findFirst(Target.class, Target.PROP_NAME,
				acronym);
		if (null == target) {
			return Collections.EMPTY_LIST;
		}
		Collection<ResearchObjectiveElement> elements = target
				.getResearchObjectiveElements();
		Collection<ConstructBean> ret = new LinkedHashSet(elements.size());
		for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
			ResearchObjectiveElement roe = (ResearchObjectiveElement) iterator
					.next();
			ret.add(ConstructBeanReader.readConstruct(roe
					.getResearchObjective()));
		}
		return ret;
	}

	public ResearchObjective setSequenceAndAcronym(String barcode,
			String sequence, String acronym, String labNotebook)
			throws ConstraintException, AccessException {
		if (null != labNotebook) {
			((WritableVersion) this.version).setDefaultOwner(labNotebook);
		}
		ExperimentGroup group = this.version.findFirst(ExperimentGroup.class,
				ExperimentGroup.PROP_NAME, barcode);
		org.pimslims.model.experiment.Experiment experiment = group.findFirst(
				ExperimentGroup.PROP_EXPERIMENTS,
				java.util.Collections.EMPTY_MAP);
		ResearchObjective ro1 = (ResearchObjective) experiment.getProject();
		if (null != ro1) {
			// TODO if used in other plate, probably shouldn't change the
			// sequence
		} else {
			if (null != sequence) {
				ro1 = this.getResearchObjective(sequence);
			}
			if (null == ro1) {
				ConstructBean bean = new ConstructBean(new TargetBean());
				// was bean.setFinalProt(sequence);
				// TODO bean.getTargetBean().setName(acronym);
				bean.setConstructId(barcode);
				ro1 = org.pimslims.presentation.construct.ConstructBeanWriter
						.createNewConstruct((WritableVersion) this.version, bean);
			}
			Set<Experiment> experiments = group.getExperiments();
			for (Iterator iterator1 = experiments.iterator(); iterator1
					.hasNext();) {
				Experiment experiment2 = (Experiment) iterator1.next();
				experiment2.setProject(ro1);
			}
		}
		if (sequence.equals(this.getSequence(ro1))) {
			// nothing to do
		} else {
			final Collection<ResearchObjectiveElement> elements = ro1
					.getResearchObjectiveElements();
			roe: for (final Iterator iterator = elements.iterator(); iterator
					.hasNext();) {
				final ResearchObjectiveElement element = (ResearchObjectiveElement) iterator
						.next();
				for (final Molecule bc : element.getTrialMolComponents()) {
					for (final ComponentCategory category : bc.getCategories()) {
						if (category.getName().equals(
								ConstructBean.FINAL_PROTEIN)) {
							bc.setSequence(sequence);
							break roe;
						}
					}
				}
			}
			/*
			 * was throw new AssertionError(
			 * "Please load reference data, missing Component Category: " +
			 * ConstructBean.FINAL_PROTEIN);
			 */
		}
		ResearchObjective ro = ro1;
		if (null != acronym) {
			this.setAcronym(ro, acronym);
		}
		return ro;
	}

	public Collection<String> getBarcodes(String sequence) {
		Map criteria = new HashMap();
		ResearchObjective ro = this.getResearchObjective(sequence);
		criteria.put(ExperimentGroup.PROP_EXPERIMENTS,
				Collections.singletonMap(Experiment.PROP_PROJECT, ro));
		Collection groups = version.findAll(ExperimentGroup.class, criteria,
				new Paging(0, 100));

		List<String> ret = new ArrayList();
		for (Iterator iterator = groups.iterator(); iterator.hasNext();) {
			ExperimentGroup group = (ExperimentGroup) iterator.next();
			if (HolderFactory.isPlateExperiment(group)) {
				ret.add(group.getName());
			}
		}
		Collections.sort(ret);
		return ret;
	}

}
