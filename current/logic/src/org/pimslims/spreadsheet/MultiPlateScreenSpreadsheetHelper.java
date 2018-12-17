/**
 * V4_3-web org.pimslims.spreadsheet MultiPlateScreenSpreadsheet.java
 * 
 * @author cm65
 * @date 1 Dec 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.spreadsheet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Container;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;

/**
 * MultiPlateScreenSpreadsheet
 * 
 */
public class MultiPlateScreenSpreadsheetHelper {

    private final Collection<SpreadsheetError> errors = new ArrayList<SpreadsheetError>();

    /**
     * MultiPlateScreenSpreadsheet.getSheet
     * 
     * @param toSave
     * @return
     */
    public SpreadSheet getSheet(final ExperimentGroup group) {
        final MultiPlateScreen bean = this.makeBean(group);
        try {
            return new SpreadSheet(bean.toStringArray());
        } catch (final InvalidFormatException e) {
            // cant happen
            throw new RuntimeException(e);
        } catch (final IOException e) {
            // cant happen
            throw new RuntimeException(e);
        }
    }

    // exposed for testing only, clients should use getSheet
    MultiPlateScreen makeBean(final ExperimentGroup group) {
        final MultiPlateScreen bean = new MultiPlateScreen();
        for (final Iterator iterator = group.getExperiments().iterator(); iterator.hasNext();) {
            final Experiment experiment = (Experiment) iterator.next();
            final OutputSample os = experiment.getOutputSamples().iterator().next();
            assert null != os.getSample();
            final String barcode = os.getSample().getContainer().getName();
            final String well = HolderFactory.getPositionInHolder(experiment);
            if (null != experiment.getProject()) {
                if (experiment.getProject() instanceof ResearchObjective) {
                    Target target = null;
                    final Set<ResearchObjectiveElement> roes =
                        ((ResearchObjective) experiment.getProject()).getResearchObjectiveElements();
                    for (final Iterator iterator2 = roes.iterator(); iterator2.hasNext();) {
                        final ResearchObjectiveElement roe = (ResearchObjectiveElement) iterator2.next();
                        if (null != roe.getTarget()) {
                            target = roe.getTarget();
                        }
                    }
                    if (null != target) {
                        bean.add(barcode, well, "Target", target.getName());
                        if (null != target.getSpecies()) {
                            bean.add(barcode, well, "Organism", target.getSpecies().getName());
                        }
                    }
                    bean.add(barcode, well, "Project", experiment.getProject().getName());
                }
            }
            // export parameters
            final Set<Parameter> parameters = experiment.getParameters();
            for (final Iterator iterator2 = parameters.iterator(); iterator2.hasNext();) {
                final Parameter parameter = (Parameter) iterator2.next();
                bean.add(barcode, well, parameter.getName(), parameter.getValue());
            }
            // export input samples
            //TODO also amounts
            for (final Iterator iterator2 = experiment.getInputSamples().iterator(); iterator2.hasNext();) {
                final InputSample is = (InputSample) iterator2.next();
                if (null != is.getSample()) {
                    bean.add(barcode, well, is.getName(), is.getSample().get_Name());
                }
            }
        }
        return bean;
    }

    /**
     * MultiPlateScreenSpreadsheet.getExperimentGroup
     * 
     */
    public ExperimentGroup getExperimentGroup(final SpreadSheet sheet, final String name,
        final WritableVersion version, final Protocol protocol, final Calendar date,
        final HolderType holderType) throws ConstraintException {
        final ExperimentGroup group = new ExperimentGroup(version, name, "from spreadsheet");
        final Object[][] values = sheet.toArray(0);
        final MultiPlateScreen bean = new MultiPlateScreen((String[][]) values);
        final Collection<String> wells = bean.getWells();
        final Collection<String> keywords = bean.getKeywords();
        final Collection<String> barcodes = bean.getPlates();
        assert 1 == protocol.getRefOutputSamples().size();
        final RefOutputSample refOutputSample = protocol.getRefOutputSamples().iterator().next();
        for (final Iterator iterator = barcodes.iterator(); iterator.hasNext();) {
            final String barcode = (String) iterator.next();
            Container holder = version.findFirst(Holder.class, AbstractHolder.PROP_NAME, barcode);
            if (null == holder) {
                holder = new Holder(version, barcode, holderType);
            }
            for (final Iterator iteratorW = wells.iterator(); iteratorW.hasNext();) {
                final String well = (String) iteratorW.next();
                final Experiment experiment =
                    new Experiment(version, name + ":" + well, date, date, protocol.getExperimentType());
                experiment.setExperimentGroup(group);
                experiment.setProtocol(protocol);
                final OutputSample os = new OutputSample(version, experiment);
                final Sample sample = new Sample(version, barcode + ":" + well);
                os.setSample(sample);
                os.setRefOutputSample(refOutputSample);
                sample.addSampleCategory(refOutputSample.getSampleCategory());
                sample.setContainer(holder);
                sample.setRowPosition(HolderFactory.getRow(well));
                sample.setColPosition(HolderFactory.getColumn(well, holderType));

                for (final Iterator iterator2 = keywords.iterator(); iterator2.hasNext();) {
                    final String keyword = (String) iterator2.next();
                    final String value = bean.getValue(barcode, well, keyword);
                    final ParameterDefinition pdef =
                        protocol.findFirst(Protocol.PROP_PARAMETERDEFINITIONS, ParameterDefinition.PROP_NAME,
                            keyword);
                    final RefInputSample ris =
                        protocol.findFirst(Protocol.PROP_REFINPUTSAMPLES, RefInputSample.PROP_NAME, keyword);
                    String targetName = "";
                    String organism = "";
                    if ("Target".equals(keyword)) {
                        targetName = value; // process this later
                    } else if ("Organism".equals(keyword)) {
                        organism = value; // process this later
                    } else if ("Project".equals(keyword) || "Construct".equals(keyword)) {
                        ResearchObjective project =
                            version.findFirst(ResearchObjective.class, ResearchObjective.PROP_COMMONNAME,
                                value);
                        if (null == project) {
                            project = new ResearchObjective(version, value, "loaded from spreadsheet");
                        }
                        experiment.setProject(project);
                        if (!"".equals(targetName)) {
                            final Set<ResearchObjectiveElement> roes = project.getResearchObjectiveElements();
                            boolean found = false;
                            for (final Iterator iterator3 = roes.iterator(); iterator3.hasNext();) {
                                final ResearchObjectiveElement roe =
                                    (ResearchObjectiveElement) iterator3.next();
                                final Target target = roe.getTarget();
                                if (null != target && targetName.equals(target.getName())) {
                                    found = true;
                                    if (!"".equals(organism)) {
                                        if (null == target.getSpecies()
                                            || !organism.equals(target.getSpecies().getName())) {
                                            this.errors.add(new SpreadsheetError(barcode, well, "Organism",
                                                value));
                                        }
                                    }
                                    break;
                                }
                            }
                            if (!found) {
                                this.errors.add(new SpreadsheetError(barcode, well, "Target", value));
                            }
                        }
                    } else if (null != pdef) {
                        final Parameter parameter = new Parameter(version, experiment);
                        parameter.setName(keyword);
                        parameter.setValue(value);
                        parameter.setParameterDefinition(pdef);
                        parameter.setParamType(pdef.getParamType());
                    } else if (null != ris) {
                        final InputSample is = new InputSample(version, experiment);
                        is.setName(keyword);
                        is.setRefInputSample(ris);
                        final Sample input = version.findFirst(Sample.class, AbstractSample.PROP_NAME, value);
                        if (null == input) {
                            this.errors.add(new SpreadsheetError(barcode, well, keyword, value));
                        } else {
                            is.setSample(input);
                        }
                    } else {
                        this.errors.add(new SpreadsheetError(barcode, well, keyword, value));
                    }
                    // could process target, if project not set

                }
            }
        }
        return group;
    }

    Collection<SpreadsheetError> getErrors() {
        return this.errors;
    }

}
