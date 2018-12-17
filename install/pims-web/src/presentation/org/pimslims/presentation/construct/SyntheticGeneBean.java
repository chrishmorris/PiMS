/**
 * pims-web org.pimslims.presentation.construct SyntheticGeneBean.java
 * 
 * @author susy (Susy Griffiths YSBL)
 * @date 19 Nov 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 susy The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.construct;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.pimslims.exception.AccessException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.experiment.ExperimentBean;
import org.pimslims.presentation.experiment.ExperimentReader;
import org.pimslims.presentation.experiment.InputSampleBean;

/**
 * SyntheticGeneBean
 * 
 */
public class SyntheticGeneBean extends ModelObjectBean {

    private String sgeneHook; //hook for the Sample

    private String sgeneName;

    private String labNotebook;

    private Experiment experiment;

    private ExperimentBean experimentBean;

    private List<Parameter> parameters;

    private Collection<Sample> outputs;

    private List<InputSampleBean> inputsamples;

    private TargetBean targetBean;

    private String dnaSeq;

    private String sgDnaSeqHook;

    private String proteinSeq;

    private String sgProteinSeqHook;

    private String fivePrimeSite;

    private String threePrimeSite;

    private String expressionHost;

    private String vectorRes;

    private String vector;

    private String vectorHook; //hook for Vector Input Sample

    private String targetHook; //hook for Target

    private String targetName;

    private String UserName;

    /**
     * Constructor for SyntheticGeneBean When Bean is for existing Sample
     */
    public SyntheticGeneBean(final Sample modelObject) {
        super(modelObject);
        this.labNotebook = modelObject.getAccess().getName();
        this.sgeneHook = modelObject.get_Hook();
        this.experiment = modelObject.getOutputSample().getExperiment();
        this.experimentBean = new ExperimentBean(this.experiment);
        this.parameters = ExperimentReader.getParameters(this.experiment);
        this.outputs = ExperimentReader.getOutputsFromGroup(this.experiment);
        for (final Sample output : this.outputs) {
            if (SyntheticGeneManager.isSynthGene(output)) {
                final Set<SampleComponent> sampleComps = output.getSampleComponents();
                for (final SampleComponent sampleComp : sampleComps) {
                    final Molecule mol = (Molecule) sampleComp.getRefComponent();
                    if (mol.getMolType().equals("DNA")) {
                        this.dnaSeq = mol.getSequence();
                    }
                    if (mol.getMolType().equals("protein")) {
                        this.proteinSeq = mol.getSequence();
                    }
                }
            }
        }
        try {
            this.inputsamples =
                ExperimentReader.getInputSamples(this.experiment, this.UserName, this.outputs);
            if (null != this.experiment.getProject()) {
                final Set<ResearchObjectiveElement> resoes =
                    ((ResearchObjective) this.experiment.getProject()).getResearchObjectiveElements();
                if (resoes.size() > 0) {
                    for (final ResearchObjectiveElement roe : resoes) {
                        if (roe.getComponentType().equals("target")) {
                            final Target target = roe.getTarget();
                            this.targetBean = new TargetBean(target);
                        }
                    }
                }
            } else {
                this.targetBean = new TargetBean();
            }
        } catch (final AccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructor for SyntheticGeneBean When Bean is for creating a Sample
     */
    public SyntheticGeneBean() {
        super();
    }

    public String getSgeneHook() {
        return this.sgeneHook;
    }

    public void setSgeneHook(final String sgeneHook) {
        this.sgeneHook = sgeneHook;
    }

    public String getSgeneName() {
        return this.sgeneName;
    }

    public void setSgeneName(final String sgeneName) {
        this.sgeneName = sgeneName;
    }

    public String getLabNotebook() {
        return this.labNotebook;
    }

    public void setLabNotebook(final String labNotebook) {
        this.labNotebook = labNotebook;
    }

    public Experiment getExperiment() {
        return this.experiment;
    }

    public void setExperiment(final Experiment experiment) {
        this.experiment = experiment;
    }

    public ExperimentBean getExperimentBean() {
        return this.experimentBean;
    }

    public void setExperimentBean(final ExperimentBean experimentBean) {
        this.experimentBean = experimentBean;
    }

    public List<Parameter> getParameters() {
        return this.parameters;
    }

    public void setParameters(final List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Collection<Sample> getOutputs() {
        return this.outputs;
    }

    public void setOutputs(final Collection<Sample> outputs) {
        this.outputs = outputs;
    }

    public List<InputSampleBean> getInputsamples() {
        return this.inputsamples;
    }

    public void setInputsamples(final List<InputSampleBean> inputsamples) {
        this.inputsamples = inputsamples;
    }

    public TargetBean getTargetBean() {
        return this.targetBean;
    }

    public void setTargetBean(final TargetBean targetBean) {
        this.targetBean = targetBean;
    }

    public String getDnaSeq() {
        return this.dnaSeq;
    }

    public void setDnaSeq(final String dnaSeq) {
        this.dnaSeq = dnaSeq;
    }

    public String getSgDnaSeqHook() {
        return this.sgDnaSeqHook;
    }

    public void setSgDnaSeqHook(final String sgDnaSeqHook) {
        this.sgDnaSeqHook = sgDnaSeqHook;
    }

    public String getProteinSeq() {
        return this.proteinSeq;
    }

    public void setProteinSeq(final String proteinSeq) {
        this.proteinSeq = proteinSeq;
    }

    public String getSgProteinSeqHook() {
        return this.sgProteinSeqHook;
    }

    public void setSgProteinSeqHook(final String sgProteinSeqHook) {
        this.sgProteinSeqHook = sgProteinSeqHook;
    }

    public String getFivePrimeSite() {
        return this.fivePrimeSite;
    }

    public void setFivePrimeSite(final String fivePrimeSite) {
        this.fivePrimeSite = fivePrimeSite;
    }

    public String getThreePrimeSite() {
        return this.threePrimeSite;
    }

    public void setThreePrimeSite(final String threePrimeSite) {
        this.threePrimeSite = threePrimeSite;
    }

    public String getExpressionHost() {
        return this.expressionHost;
    }

    public void setExpressionHost(final String expressionHost) {
        this.expressionHost = expressionHost;
    }

    public String getVectorRes() {
        return this.vectorRes;
    }

    public void setVectorRes(final String vectorRes) {
        this.vectorRes = vectorRes;
    }

    public String getVector() {
        return this.vector;
    }

    public void setVector(final String vector) {
        this.vector = vector;
    }

    public String getVectorHook() {
        return this.vectorHook;
    }

    public void setVectorHook(final String vectorHook) {
        this.vectorHook = vectorHook;
    }

    public String getTargetHook() {
        return this.targetHook;
    }

    public void setTargetHook(final String targetHook) {
        this.targetHook = targetHook;
    }

    public String getTargetName() {
        return this.targetName;
    }

    public void setTargetName(final String targetName) {
        this.targetName = targetName;
    }

    public String getUserName() {
        return this.UserName;
    }

    public void setUserName(final String userName) {
        this.UserName = userName;
    }

}
