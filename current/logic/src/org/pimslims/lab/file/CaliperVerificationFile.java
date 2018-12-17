/**
 * pims-web org.pimslims.lab.file CaliperResultFile.java
 * 
 * @author Marc Savitsky
 * @date 12 May 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.lab.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import org.jdom.JDOMException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;

/**
 * Caliper Verification ResultFile This class gives the opportunity for custom scoring for verification
 * experiments
 * 
 */
public class CaliperVerificationFile extends CaliperFile {

    /**
     * BASES_FROM_VECTOR String
     */
    static final String BASES_FROM_VECTOR = "Bases from vector";

    protected static String OPPF_VERIFICATION = "OPPF Verification";

    protected static String VECTOR = "Vector";

    /**
     * org.pimslims.lab.file.IFileTypeListener Class.forName(CaliperVerificationFile.class.getName());
     */
    static {
        FileFactory.register(new CaliperVerificationFile());
    }

    public CaliperVerificationFile() {
        // Empty constructor
    }

    @Override
    public String getTypeName() {
        return "CaliperVerification";
    }

    @Override
    public boolean isOfThisType(final ReadableVersion version, final String fileName) throws IFileException {
        final Matcher m = CaliperFile.FILE_CALIPER.matcher(fileName);
        if (!m.matches()) {
            return false;
        }

        final ExperimentGroup group =
            CaliperFile.findFromExperimentGroup(version, CaliperFile.getBarcode(fileName));
        if (!group.getExperiments().isEmpty()) {
            final Experiment experiment = group.getExperiments().iterator().next();
            final Protocol protocol = experiment.getProtocol();

            if (CaliperVerificationFile.OPPF_VERIFICATION.equals(protocol.getName())) {
                return true;
            }
        }

        return false;
    }

    public CaliperVerificationFile(final String name, final InputStream inputStream) throws JDOMException,
        IOException {

        this.fileName = name;

        final BufferedReader d = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = d.readLine()) != null) {
            sb.append(line + "\n");
        }
        this.inputString = sb.toString();
    }

    @Override
    public CaliperVerificationFile getInstance(final String name, final InputStream inputStream)
        throws JDOMException, IOException {
        return new CaliperVerificationFile(name, inputStream);
    }

    /**
     * 
     * CaliperVerificationFile.getPCRProductSize
     * 
     * @param experiment
     * @return
     */
    @Override
    public int getPCRProductSize(final Experiment experiment) {

        //System.out.println("CaliperVerificationFile.getPCRProductSize [" + experiment.get_Name() + "]");
        final ResearchObjective researchObjective = (ResearchObjective) experiment.getProject();
        final ResearchObjectiveElement element = CaliperFile.getResearchObjectiveElement(researchObjective);
        int size = -1;

        if (null != element) {
            for (final Molecule bc : element.getTrialMolComponents()) {
                for (final ComponentCategory category : bc.getCategories()) {
                    if (category.getName().equals(CaliperFile.PCR_PRODUCT)) {
                        size = bc.getSequence().length();
                    }
                }
            }
        }
        final Integer basesFromVector = this.getBasesFromVector(experiment);
        return size + basesFromVector;
    }

    //TODO make this an attribute of a vector
    private static final Map<String, Integer> vectorSize = new HashMap<String, Integer>();
    static {
        CaliperVerificationFile.vectorSize.put("pOPINF", 175);
    }

    private Integer getBasesFromVector(final Experiment experiment) {
        Integer basesFromVector = 0;
        final Parameter parm =
            experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME,
                CaliperVerificationFile.BASES_FROM_VECTOR);
        if (null == parm) {
            final RefSample vector = CaliperVerificationFile.getVector(experiment);
            if (null != vector) {
                final String vectorName = vector.getName();

                if (CaliperVerificationFile.vectorSize.containsKey(vectorName)) {
                    basesFromVector = CaliperVerificationFile.vectorSize.get(vectorName);
                }
            }
        } else {
            basesFromVector = Integer.valueOf(parm.getValue());
        }
        return basesFromVector;
    }

    /**
     * 
     * CaliperVerificationFile.getVector
     * 
     * @param experiment
     * @return
     */
    public static RefSample getVector(Experiment experiment) {

        while (null != experiment) {
            final RefSample vector = CaliperVerificationFile.findVectorSample(experiment);
            if (null != vector) {
                return vector;
            }
            experiment = CaliperVerificationFile.previousExperiment(experiment);
        }
        return null;

    }

    /**
     * 
     * CaliperVerificationFile.findVectorSample
     * 
     * @param experiment
     * @return
     */
    private static RefSample findVectorSample(final Experiment experiment) {

        for (final InputSample input : experiment.getInputSamples()) {
            final Sample sample = input.getSample();
            if (null != sample) {
                for (final SampleCategory category : sample.getSampleCategories()) {
                    if (CaliperVerificationFile.VECTOR.equals(category.getName())) {
                        return sample.getRefSample();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 
     * CaliperVerificationFile.previousExperiment
     * 
     * @param experiment
     * @return
     */
    private static Experiment previousExperiment(final Experiment experiment) {

        for (final InputSample input : experiment.getInputSamples()) {
            final Sample sample = input.getSample();
            if (null != sample) {
                final OutputSample output = sample.getOutputSample();
                if (null != output) {
                    final Experiment exp = output.getExperiment();
                    if (null != exp) {
                        return exp;
                    }
                }
            }
        }
        return null;
    }
}
