package org.pimslims.presentation.leeds;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.Set;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Creator;
import org.pimslims.lab.Util;
import org.pimslims.leeds.FormFieldsNames;
import org.pimslims.leeds.LeedsFormServletUtils;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.PrimerBeanReader;
import org.pimslims.presentation.construct.ConstructBeanWriter;

/**
 * Class represent Leeds primer form recorded in PIMS
 * 
 * @author Petr Troshin
 */
@Deprecated
// Leeds functionality is obsolete
public class PrimerForm implements FormFieldsNames {

    /**
     * 
     */
    private static final String WRITABLE_VERSION_MUST_BE_PASSED_FOR_THIS_UPDATE =
        "Writable version must be passed for this update";

    PrimerBean forwardPrimer;

    PrimerBean reversePrimer;

    /*
     * Resources - names of Entry Clone & Expression Construct forms
     */
    static final PropertyResourceBundle primerformRb;

    static final PropertyResourceBundle fprimerRb;

    static final PropertyResourceBundle rprimerRb;
    //TODO change this, the error reporting for static initializers is not good
    static {
        InputStream in_stream = null;
        InputStream in2_stream = null;
        InputStream in3_stream = null;
        try {
            in_stream = PrimerForm.class.getResourceAsStream("PrimerForm_en.properties");
            primerformRb = new PropertyResourceBundle(in_stream);
            in2_stream = PrimerForm.class.getResourceAsStream("ForwardPrimer_en.properties");
            fprimerRb = new PropertyResourceBundle(in2_stream);
            in3_stream = PrimerForm.class.getResourceAsStream("ReversePrimer_en.properties");
            rprimerRb = new PropertyResourceBundle(in3_stream);
        } catch (final IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (in_stream != null) {
                try {
                    in_stream.close();
                } catch (final IOException isc) {
                    // do nothing
                }
            }
            if (in2_stream != null) {
                try {
                    in2_stream.close();
                } catch (final IOException isc) {
                    // do nothing
                }
            }
            if (in3_stream != null) {
                try {
                    in3_stream.close();
                } catch (final IOException isc) {
                    // do nothing
                }
            }
        }
    }

    // Load categories from properties
    public static final Map<String, String> primerFormProp = Collections
        .unmodifiableMap(LeedsFormServletUtils.resourceToMap(PrimerForm.primerformRb));

    public static final Map<String, String> fprimerProp = Collections.unmodifiableMap(LeedsFormServletUtils
        .resourceToMap(PrimerForm.fprimerRb));

    public static final Map<String, String> rprimerProp = Collections.unmodifiableMap(LeedsFormServletUtils
        .resourceToMap(PrimerForm.rprimerRb));

    // Form experiment
    Experiment experiment;

    WritableVersion rw;

    /*
     * DB mapping Experiment.details - comments Person - designedBy Experiment.Parameter - from
     * Experiment.Parameter - to Experiment.Parameter - genelength Experiment.Parameter - modif
     * Experiment.Parameter - pcrprodlength Experiment.Parameter - sendformto Experiment.date- date
     */

    /*
     *
     */
    private PrimerForm(final Experiment experiment) {
        this.experiment = experiment;

        final String experimentTypeName = experiment.getExperimentType().getName();
        //System.out.println("PrimerForm [" + experimentTypeName + "]");

        if (experimentTypeName.equals(FormFieldsNames.leedscloningDesign)) {
            Sample s;
            s = this.getInSample(ConstructBeanWriter.FPRIMER);
            if (null != s) {
                final PrimerBean bean = PrimerBeanReader.readPrimer(s);
                this.forwardPrimer = bean;
            }
            s = this.getInSample(ConstructBeanWriter.RPRIMER);
            if (null != s) {
                final PrimerBean bean = PrimerBeanReader.readPrimer(s);
                this.reversePrimer = bean;
            }
        }

        if (experimentTypeName.equals(FormFieldsNames.oppfcloningDesign)
            || experimentTypeName.equals(FormFieldsNames.spotcloningDesign)) {

            final Set<OutputSample> outputSamples = experiment.getOutputSamples();
            for (final OutputSample outputSample : outputSamples) {
                final Sample sample = outputSample.getSample();
                if (sample != null) {
                    final Set<SampleComponent> sampleComponents = sample.getSampleComponents();
                    for (final SampleComponent sampleComponent : sampleComponents) {
                        //There has to be a better way than this! might need datamodel tweek
                        if (sampleComponent.getRefComponent().get_MetaClass().getJavaClass().getName()
                            .equals("org.pimslims.model.molecule.Primer")) {
                            final PrimerBean bean = PrimerBeanReader.readPrimer(sample);
                            if (bean.isForward()) {
                                this.forwardPrimer = bean;
                            }
                            if (bean.isReverse()) {
                                this.reversePrimer = bean;
                            }
                        }

                    }
                }
            }
        }
    }

    public PrimerForm(final Experiment experiment, final ReadableVersion version) {
        this(experiment);
    }

    /*
     * If update is required use this constructor!
     */
    public PrimerForm(final Experiment experiment, final WritableVersion rw) {
        this(experiment);
        this.rw = rw;
    }

    public PrimerBean getForwardPrimer() {
        //System.out.println("PrimerForm.getForwardPrimer [" + forwardPrimer + "]");
        return this.forwardPrimer;
    }

    public PrimerBean getReversePrimer() {
        return this.reversePrimer;
    }

    /*
    public Primer getForwardPrimer() {
        Sample sample = getInSample(fprimer);
        if (sample != null) {
            return new Primer(sample);
        }
        return null;
    }

    public Primer getReversePrimer() {
        Sample sample = getInSample(rprimer);
        if (sample != null)
            return new Primer(sample);
        return null;
    }
     */

    /*
     * This could be either hook to the expression or entryclone plasmid but also if these are not found could
     * be a name of a plasmid which is ordinary text The difference must be handled on the presentation layer
     */
    public String getPlasmid() {
        final Parameter param = this.getParameter(FormFieldsNames.RECOMBINANT_PLASMID);
        if (param != null) {
            return param.getValue();
        }

        return "";
    }

    private Sample getInSample(final String sampleCategoryName) {
        final InputSample inSample = this.getInputSample(sampleCategoryName);
        if (inSample != null) {
            return inSample.getSample();
        }
        return null;
    }

    private InputSample getInputSample(final String sampleCategoryName) {
        final Collection<InputSample> inSamples = this.experiment.getInputSamples();
        for (final InputSample inSample : inSamples) {
            final Sample sample = inSample.getSample();
            if (null == sample) {
                continue;
            }
            final Set<SampleCategory> scats = sample.getSampleCategories();
            for (final SampleCategory scat : scats) {
                if (scat.getName().equalsIgnoreCase(sampleCategoryName)) {
                    return inSample;
                }
            }
        }
        return null;
    }

    /**
     * In order to set forward primer as reverse primer for the form primer.sample.sampleCategory must be
     * changed This is not supported at the moment! Thus one Forward Primer can be changed to another forward
     * primer but not the reverse one!
     * 
     * @param forwardPrimer
     * @throws AccessException
     * @throws ConstraintException
     * @throws
     */
    public void setForwardPrimer(final PrimerBean forwardPrimer) throws AccessException, ConstraintException {
        final InputSample inSample = this.getInputSample(ConstructBeanWriter.FPRIMER);
        // TODO make sure that the sampleCategories of a primer contains
        // appropriate category!
        // Ideas to check -
        // assert ! forwardPrimer.sample.getSampleCategories().getName()
        // .contains(FormFieldsNames.rprimer) : "Sample must not have Reverse
        // Primer sample category!";
        // assert
        // forwardPrimer.sample.getSampleCategories().contains(FormFieldsNames.fprimer)
        // : "Sample must have Forward Primer sample category!";

        if (inSample == null) {
            this.createInputSample(forwardPrimer.getSample(), ConstructBeanWriter.FPRIMER);
        } else {
            this.getInputSample(ConstructBeanWriter.FPRIMER).setSample(forwardPrimer.getSample());
        }
    }

    public void setReversePrimer(final PrimerBean reversePrimer) throws AccessException, ConstraintException {
        final InputSample sample = this.getInputSample(ConstructBeanWriter.RPRIMER);
        // TODO See above (serForwardPrimer method)
        if (sample == null) {
            assert reversePrimer.getSample() != null : "Rev pr in Primer is null";
            this.createInputSample(reversePrimer.getSample(), ConstructBeanWriter.RPRIMER);
        } else {
            this.getInputSample(ConstructBeanWriter.RPRIMER).setSample(reversePrimer.getSample());
        }
    }

    private void createInputSample(final Sample sample, final String sampleCat) throws AccessException,
        ConstraintException {
        assert this.rw != null : PrimerForm.WRITABLE_VERSION_MUST_BE_PASSED_FOR_THIS_UPDATE;
        assert sample != null : "Sample is null!";
        Creator.recordInputSamples(this.rw, sample, this.experiment, sample.getName() + " " + sampleCat);
    }

    // Getters

    public String getComments() {
        return this.experiment.getDetails();
    }

    public String getDesignedBy() {
        final User creator = this.experiment.getOperator();
        if (creator == null) {
            return null;
        }
        return creator.get_Hook();
    }

    public String getFrom() {
        final Parameter param = this.getParameter(FormFieldsNames.from);
        if (param != null) {
            return param.getValue();
        }

        return "";
    }

    public String getTo() {
        final Parameter param = this.getParameter(FormFieldsNames.to);
        if (param != null) {
            return param.getValue();
        }

        return "";
    }

    public String getGeneLength() {
        final Parameter param = this.getParameter(FormFieldsNames.geneLength);
        if (param != null) {
            return param.getValue();
        }

        return "";
    }

    public String getModifications() {
        final Parameter param = this.getParameter(FormFieldsNames.modifications);
        if (param != null) {
            return param.getValue();
        }

        return "";
    }

    public String getPCRProductLength() {
        final Parameter param = this.getParameter(FormFieldsNames.pCRProdLength);
        if (param != null) {
            return param.getValue();
        }

        return "";
    }

    public String getSendFormTo() {
        final Parameter param = this.getParameter(FormFieldsNames.sendFormTo);
        if (param != null) {
            return param.getValue();
        }

        return "";
    }

    public String getDate() {
        return AbstractSavedPlasmid.getDate(this.experiment.getStartDate());
    }

    public Calendar getDateasTime() {
        return this.experiment.getStartDate();
    }

    // Setters
    public void setComments(final String comments) throws ConstraintException {
        this.experiment.setDetails(comments);
    }

    public void setdesignedBy(final String userHook) throws ConstraintException, AccessException {
        User designer = null;
        if (Util.isEmpty(userHook)) {
            return;
        }

        if (Util.isHookValid(userHook)) {
            designer = this.experiment.get_Version().get(userHook);
            assert designer != null : "Cannot change author of the experiment bacause person hook "
                + userHook + " is not correct or no person found";
        } else {
            assert this.rw != null : PrimerForm.WRITABLE_VERSION_MUST_BE_PASSED_FOR_THIS_UPDATE;
            final Map personProp = new HashMap<String, Object>();
            personProp.put(User.PROP_NAME, userHook);
            designer = Util.getOrCreate(this.rw, User.class, personProp);

        }
        this.experiment.setOperator(designer);
    }

    public void setFrom(final String fromVal) throws AccessException, ConstraintException {
        final Parameter param = this.getParameter(FormFieldsNames.from);
        if (param != null) {
            param.setValue(fromVal);
        } else {
            assert this.rw != null : PrimerForm.WRITABLE_VERSION_MUST_BE_PASSED_FOR_THIS_UPDATE;
            Creator.recordParameters(this.rw, FormFieldsNames.from, "Int", fromVal, this.experiment);
        }
    }

    public void setTo(final String toVal) throws AccessException, ConstraintException {
        final Parameter param = this.getParameter(FormFieldsNames.to);
        if (param != null) {
            param.setValue(toVal);
        } else {
            assert this.rw != null : PrimerForm.WRITABLE_VERSION_MUST_BE_PASSED_FOR_THIS_UPDATE;
            Creator.recordParameters(this.rw, FormFieldsNames.to, "Int", toVal, this.experiment);
        }
    }

    public void setGeneLength(final String geneLengthVal) throws AccessException, ConstraintException {
        final Parameter param = this.getParameter(FormFieldsNames.geneLength);
        if (param != null) {
            param.setValue(geneLengthVal);
        } else {
            assert this.rw != null : PrimerForm.WRITABLE_VERSION_MUST_BE_PASSED_FOR_THIS_UPDATE;
            Creator.recordParameters(this.rw, FormFieldsNames.geneLength, "Int", geneLengthVal,
                this.experiment);
        }
    }

    public void setModifications(final String modificationsVal) throws AccessException, ConstraintException {
        final Parameter param = this.getParameter(FormFieldsNames.modifications);
        if (param != null) {
            param.setValue(modificationsVal);
        } else {
            assert this.rw != null : PrimerForm.WRITABLE_VERSION_MUST_BE_PASSED_FOR_THIS_UPDATE;
            Creator.recordParameters(this.rw, FormFieldsNames.modifications, "String", modificationsVal,
                this.experiment);
        }
    }

    public void setPCRProductLength(final String PCRproductLength) throws AccessException,
        ConstraintException {
        final Parameter param = this.getParameter(FormFieldsNames.pCRProdLength);
        if (param != null) {
            param.setValue(PCRproductLength);
        } else {
            assert this.rw != null : PrimerForm.WRITABLE_VERSION_MUST_BE_PASSED_FOR_THIS_UPDATE;
            Creator.recordParameters(this.rw, FormFieldsNames.pCRProdLength, "Int", PCRproductLength,
                this.experiment);
        }
    }

    public void setSendFormTo(final String sendFormToVal) throws AccessException, ConstraintException {
        final Parameter param = this.getParameter(FormFieldsNames.sendFormTo);
        if (param != null) {
            param.setValue(sendFormToVal);
        } else {
            assert this.rw != null : PrimerForm.WRITABLE_VERSION_MUST_BE_PASSED_FOR_THIS_UPDATE;
            Creator.recordParameters(this.rw, FormFieldsNames.sendFormTo, "String", sendFormToVal,
                this.experiment);
        }
    }

    public void setDate(final Date date) throws ConstraintException {
        this.experiment.setStartDate(PrimerForm.getCalendar(date));
    }

    // TODO Think whether it worth to unify various getParameter
    // to have one static synchronized method?
    private Parameter getParameter(final String paramName) {
        final Set<Parameter> paramaters = this.experiment.getParameters();
        for (final Parameter par : paramaters) {
            final String mname = par.getName();
            if (mname != null && mname.equals(paramName)) {
                return par;
            }
        }
        return null;
    }

    public static Collection<PrimerForm> getPrimerForms(final ReadableVersion rv) {

        final Map expProp = org.pimslims.lab.Util.getNewMap();
        //TODO I suggest using a single experiment type for all these protocols
        expProp.put(ExperimentType.PROP_NAME, FormFieldsNames.leedscloningDesign);
        final Collection<ExperimentType> expTypes =
            rv.findAll(org.pimslims.model.reference.ExperimentType.class, expProp);

        expProp.put(ExperimentType.PROP_NAME, FormFieldsNames.oppfcloningDesign);
        expTypes.addAll(rv.findAll(org.pimslims.model.reference.ExperimentType.class, expProp));

        expProp.put(ExperimentType.PROP_NAME, FormFieldsNames.spotcloningDesign);
        expTypes.addAll(rv.findAll(org.pimslims.model.reference.ExperimentType.class, expProp));

        if (expTypes == null || expTypes.size() == 0) {
            return null;
            //System.out.println("PrimerForm.getPrimerForms Exp size [" + expTypes.size() + "]");
            //assert expTypes.size() == 1 : "There must be only one experiment type 'Cloning design'";
        }

        final ArrayList<PrimerForm> primerForms = new ArrayList<PrimerForm>();
        for (final java.util.Iterator iter = expTypes.iterator(); iter.hasNext();) {
            final ExperimentType cldesign = (ExperimentType) iter.next();

            final Collection<Experiment> exps = cldesign.getExperiments();
            //System.out.println("PrimerForm.getPrimerForms [" + cldesign.get_Name() + ":" + exps.size() + "]");
            for (final Experiment exp : exps) {
                final PrimerForm pform = new PrimerForm(exp, rv);
                primerForms.add(pform);
            }
        }
        return primerForms;
    }

    public void setPlasmid(String plasmideconcerned) throws AccessException, ConstraintException {
        final Parameter param = this.getParameter(FormFieldsNames.RECOMBINANT_PLASMID);
        // If the name of the plasmid concerned match the name of the plasmid
        // recorded than replace it with the hook.
        final Experiment exp = this.getPlasmidExperiment(plasmideconcerned, this.rw);
        if (exp != null) {
            plasmideconcerned = exp.get_Hook();
        }
        if (param != null) {
            param.setValue(plasmideconcerned);
        } else {
            assert this.rw != null : "Please provide WritableVersion in the constructor";
            Creator.recordParameters(this.rw, FormFieldsNames.RECOMBINANT_PLASMID, "String",
                plasmideconcerned, this.experiment);
        }
    }

    private Experiment getPlasmidExperiment(String name, final ReadableVersion rv) {
        name = name.trim();
        final Collection<Experiment> plasmids = SavedPlasmid.getEntryCloneExperiments(rv);
        final Collection<Experiment> expPlasmid = SavedDeepFrozenCulture.getExpConstructExperiments(rv);
        if (expPlasmid != null) {
            plasmids.addAll(expPlasmid);
        }
        for (final Experiment exp : plasmids) {
            if (name.equals(exp.getName())) {
                return exp;
            }
        }
        return null;
    }

    public String getHook() {
        return this.experiment.get_Hook();
    }

    public String getName() {
        return this.experiment.getName();
    }

    public ReadableVersion getVersion() {
        return this.experiment.get_Version();
    }

    public Experiment getExperiment() {
        return this.experiment;
    }

    public static String cleanLocation(String location) {
        // Some MPSI location has : prior to the name e.g. ":Freezer MPSI 1"
        final int fbracket = location.indexOf("Freezer MPSI");
        if (fbracket >= 0) {
            location = location.substring(fbracket).trim();
        }
        return location;
    }

    @Deprecated
    // used only in unsupported code
    public static Calendar getCalendar(final Date datevalue) {
        final Calendar ret = Calendar.getInstance();
        if (datevalue != null) {
            ret.setTimeInMillis(datevalue.getTime());
        }
        return ret;
    }
} // end of class
