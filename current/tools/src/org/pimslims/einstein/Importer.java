/**
 * tools org.pimslims.einstein Importer.java
 * 
 * @author cm65
 * @date 9 Sep 2014
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2014 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.einstein;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Calendar;

import org.pimslims.csv.CsvParser;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.Database;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanWriter;

/**
 * Importer
 * 
 */
public class Importer {

    private final WritableVersion version;

    private final Database database;

    private final SampleCategory vectorCategory;

    private final ExperimentType expType;

    private final Protocol protocol;

    private final RefOutputSample refOutputSample;

    /**
     * Constructor for Importer
     * 
     * @param version
     */
    public Importer(WritableVersion version) {
        this.version = version;
        this.database = version.findFirst(Database.class, Database.PROP_NAME, "NCBI Protein");
        this.vectorCategory = version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Vector");
        this.expType = version.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, "Cloning");
        this.protocol =
            version.findFirst(Protocol.class, Protocol.PROP_NAME, "Albert Einstein College Import");

        this.refOutputSample = (RefOutputSample) this.protocol.findFirst(Protocol.PROP_REFOUTPUTSAMPLES);
    }

    /**
     * Importer.importTab
     * 
     * @param in
     * 
     * @param string
     * @throws IOException
     * @throws ConstraintException
     * @throws AccessException
     */
    public void importTab(InputStream in) throws IOException, ConstraintException, AccessException {
        importTab(null, in);
    }

    /**
     * Importer.importTab
     * 
     * @param name TODO
     * @param in
     * @param string
     * 
     * @throws IOException
     * @throws ConstraintException
     * @throws AccessException
     * 
     *             everything is in a 96 well plate in the freezer and thus far we are using this excel sheet
     *             to keep track of everything. Rack name comes from the excel tab or column AD and well# of
     *             that rack is in Column A
     * 
     *             The following columns do not seem to contain useful information, because they are constant
     *             or blank or have a simple relationship to other columns. However, I might have been unlucky
     *             in the example data you supplied. If they should be uploaded, please supply example values.
     * 
     *             LIMS-ID Seems to have a dependable relationship to Barcode-ID, does it ever vary? Do you
     *             want a clickable link to an existing LIMS? Domain-ID Seems to be the same as LIMS-ID, does
     *             it ever vary?
     * 
     *             >this is our unique code for our systems. The barcode tells us valuable information for
     *             instance 140201750 The vector is coded 14 for pSGC-His or 25 pNYCOMPSC, 020175 is the
     *             LIMS/domain ID and 0 at end tells us if it is full length, if not 0 it was truncated. The
     *             LIMS and domain rarely change from one another, but I was asked to keep them both in.
     * 
     *             The production team gives us the excel sheet from small scale, and we can continue to do
     *             that in excel and then upload it into the database. After that excel sheet the next steps
     *             we can record in PIMS
     * 
     *             The LIMS ID tells us what the construct is but barcode gives us the full details of that
     *             particular construct like vector and all. See answer to next question.
     * 
     *             What is the Domain-ID the name of? The target? In particular, if you made two different
     *             constructs for a target, would they have the same Domain-ID and different Barcode IDs?
     * 
     *             -This is where it gets confusing. 99% of the time the domain ID and LIMS ID are the same
     *             and it is incorporated into the barcode barcode for example 140315040 is 14 is vector
     *             031504 is lims ID and 0 is full length. So the barcode tells us a lot about what we are
     *             looking at but the LIMS ID is our construct.
     */
    public void importTab(String name, InputStream in) throws IOException, ConstraintException,
        AccessException {

        Holder holder = findOrCreateHolder(name);
        ExperimentGroup group = new ExperimentGroup(version, name, "Albert Einstein College import");
        final Reader reader = new InputStreamReader(in);
        final CsvParser p = new CsvParser(reader);
        p.getLabels();
        while (null != p.getLine()) {
            String limsid = p.getValueByLabel("LIMS-ID");
            if (limsid.contains("ppppp") || limsid.contains("nnnnn")) {
                continue; // control
            }
            Experiment experiment = null;
            if (null != p.getValueByLabel("WELL-#")) {
                String well = p.getValueByLabel("WELL-#").trim(); // the well where the plasmid is
                Sample sample = new Sample(version, name + ":" + well);
                if (null != this.refOutputSample) {
                    sample.addSampleCategory(this.refOutputSample.getSampleCategory());
                }
                positionSample(well, holder, sample);
                Calendar now = Calendar.getInstance();
                experiment = new Experiment(version, name + ":" + well, now, now, this.expType);
                experiment.setExperimentGroup(group);
                experiment.setProtocol(protocol);
                Parameter parameter = new Parameter(version, experiment);
                parameter.setName("Solubility");
                parameter.setValue(p.getValueByLabel("Auto Solubility"));
                parameter.setParameterDefinition((ParameterDefinition) this.protocol.findFirst(
                    Protocol.PROP_PARAMETERDEFINITIONS, ParameterDefinition.PROP_NAME, "Solubility"));
                experiment.setStatus("OK");
                OutputSample os = new OutputSample(version, experiment);
                os.setRefOutputSample(refOutputSample);
                os.setSample(sample);
                if (null != p.getValueByLabel("Template")
                    && !"SYNTHETIC".equals(p.getValueByLabel("Template"))
                    && !"".equals(p.getValueByLabel("Template"))) {
                    String template = p.getValueByLabel("Template");
                    String twell = p.getValueByLabel("TWELL-#"); // the well the template was in 
                    Holder tholder = findOrCreateHolder(template);
                    String sampleName = template + ":" + twell;
                    Sample tsample = findOrCreateSample(sampleName);
                    positionSample(twell, tholder, tsample);
                    InputSample is = new InputSample(version, experiment);
                    is.setName("Template");
                    is.setRefInputSample((RefInputSample) this.protocol.findFirst(
                        Protocol.PROP_REFINPUTSAMPLES, RefInputSample.PROP_NAME, "Template"));
                    is.setSample(tsample);
                }
            }

            Target target = importTarget(p);
            if (null != p.getValueByLabel("Domain-ID")) {
                ResearchObjective ro = importConstruct(p, target);
                if (null != experiment) {
                    experiment.setProject(ro);
                }
            }
        }
    }

    private void positionSample(String well, Holder tholder, Sample tsample) throws ConstraintException {
        assert null != well;
        try {
            HolderFactory.positionSample(tholder, tsample, well);
        } catch (NumberFormatException e) {
            System.err.println("Bad well number: " + well);
        }
    }

    private Sample findOrCreateSample(String name) throws ConstraintException {
        Sample sample = version.findFirst(Sample.class, Sample.PROP_NAME, name);
        if (null == sample) {
            sample = new Sample(version, name);
        }
        return sample;
    }

    private Holder findOrCreateHolder(String name) throws ConstraintException {
        HolderType type = version.findFirst(HolderType.class, Holder.PROP_NAME, "96 well rd-bottom");
        Holder holder = version.findFirst(Holder.class, Holder.PROP_NAME, name);
        if (null == holder) {
            holder = new Holder(version, name, type);
        }
        return holder;
    }

    private String getSequence(final CsvParser p, final String column) {
        if ("EMPTY".equals(p.getValueByLabel(column))) {
            return "";
        }
        ;
        return p.getValueByLabel(column);
    }

    private ResearchObjective importConstruct(final CsvParser p, Target target) throws ConstraintException,
        AccessException {
        ConstructBean bean =
            new ConstructBean(new TargetBean(target), new PrimerBean(true), new PrimerBean(false));
        bean.setConstructId("C" + p.getValueByLabel("Domain-ID"));
        bean.setDnaSeq(p.getValueByLabel("Construct NT"));
        bean.setExpressedProt(p.getValueByLabel("Construct AA"));
        bean.setFwdPrimer(p.getValueByLabel("F-Primer"));
        bean.setRevPrimer(p.getValueByLabel("R-Primer"));

        String start = p.getValueByLabel("Start");
        if ("N/A".equals(start) || "".equals(start)) {
            start = "1";
        }
        bean.setTargetProtStart(Integer.valueOf(start));
        String end = p.getValueByLabel("End");
        if ("N/A".equals(end) || "".equals(end)) {
            bean.setTargetProtEnd(target.getSeqString().length());
        } else {
            bean.setTargetProtEnd(Integer.valueOf(end));
        }

        ResearchObjective ro = ConstructBeanWriter.createNewConstruct(version, bean);
        if (null != p.getValueByLabel("Vector")) {
            Sample vector =
                this.version.findFirst(Sample.class, Sample.PROP_NAME, p.getValueByLabel("Vector"));
            if (null == vector) {
                vector = new Sample(version, p.getValueByLabel("Vector"));
                vector.addSampleCategory(this.vectorCategory);
            }
            ((InputSample) version.get(bean.getVectorInputSampleHook())).setSample(vector);
        }

        return ro;
    }

    private Target importTarget(final CsvParser p) throws ConstraintException {
        String accession = p.getValueByLabel("Accession");
        String dnaSeq = p.getValueByLabel("Target NT");
        String proteinSeq = p.getValueByLabel("Target AA");

        Target target = this.version.findFirst(Target.class, Target.PROP_NAME, accession);
        if (null == target) {
            Molecule protein = new Molecule(version, "protein", accession + "Protein");
            target = new Target(version, accession, protein);
        }
        target.getProtein().setSequence(proteinSeq);
        if (target.getNucleicAcids().isEmpty()) {
            target.addNucleicAcid(new Molecule(version, "DNA", accession + "DNA"));
        }
        target.getNucleicAcids().iterator().next().setSequence(dnaSeq);

        ExternalDbLink link = new ExternalDbLink(version, this.database, target);
        link.setAccessionNumber(accession);
        link.setUrl(this.database.getUrl() + accession);
        return target;
    }

    public static void main(String[] args) {
        if (0 == args.length) {
            System.out.println("Usage: userid plate file");
            System.exit(1);
        }
        AbstractModel model = ModelImpl.getModel();
        WritableVersion version = model.getWritableVersion(args[0]);
        if (null == version) {
            System.err.println("Unknown username: " + args[0]);
            System.exit(4);
        }
        try {
            InputStream is = new FileInputStream(args[2]);
            new Importer(version).importTab(args[1], is);
            version.commit();
            System.out.println("OK Loaded: " + args[2]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ConstraintException e) {
            e.printStackTrace();
        } catch (AccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AbortedException e) {
            e.printStackTrace();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
