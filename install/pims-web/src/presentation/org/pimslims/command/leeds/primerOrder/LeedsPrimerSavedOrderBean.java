package org.pimslims.command.leeds.primerOrder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.pimslims.leeds.FormFieldsNames;
import org.pimslims.model.core.Annotation;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.presentation.order.SavedOrderBean;
import org.pimslims.util.File;
import org.pimslims.util.FileImpl;

public class LeedsPrimerSavedOrderBean extends SavedOrderBean implements ILeedsPrimerOrderBean {
    /**
     * 
     */
    public static final String MOLAR = "M";

    // plate wide value
    String purificationType;

    String startingSynthesisScale;

    String bufferType;

    String normalization;

    // well wide value
    Float MW;

    String restrictionSites;

    Integer lenghtOnGene;

    Float tmSeller;

    Float tmFull;

    Float tmGene;

    Float concertrationInUM;

    Float volumnInNL;

    Float OD;

    Float amountInUg;

    Map<org.pimslims.util.File, String> annotations;// file->name

    public LeedsPrimerSavedOrderBean(final Experiment exp) {
        super(exp);
        // set annotations
        this.annotations = new HashMap<org.pimslims.util.File, String>();
        for (final Annotation annotation : exp.getAnnotations()) {
            this.annotations.put(FileImpl.getFile(annotation), annotation.getDetails().replace("/", " "));
        }
        // set MW
        if (super.getMcHook() != null) {
            final Molecule mc = exp.get_Version().get(super.getMcHook());
            this.MW = mc.getMolecularMass();
        }
        // set µg
        if (super.getSampleHook() != null) {
            final Sample sample = exp.get_Version().get(super.getSampleHook());
            if (null != sample.getCurrentAmount() && "kg".equals(sample.getAmountUnit())) {
                this.amountInUg = 1000000000f * sample.getCurrentAmount();
            }
        }
        // set Conc. µM
        if (super.getScHook() != null) {
            final SampleComponent sc = exp.get_Version().get(super.getScHook());
            if (LeedsPrimerSavedOrderBean.MOLAR.equals(sc.getConcentrationUnit())
                && null != sc.getConcentration()) {
                this.concertrationInUM = 1000000f * sc.getConcentration();
            }
        }
        // set values from parameters
        final Collection<Parameter> parameters = exp.getParameters();
        for (final Parameter p : parameters) {
            if (p.getName().equalsIgnoreCase("Purification type")) {
                this.setPurificationType(p.getValue());
            } else if (p.getName().equalsIgnoreCase("Starting Synthesis Scale")) {
                this.setStartingSynthesisScale(p.getValue());
            } else if (p.getName().equalsIgnoreCase("BufferType")) {
                this.setBufferType(p.getValue());
            } else if (p.getName().equalsIgnoreCase("Normalization type")) {
                this.setNormalization(p.getValue());
            } else if (p.getName().equalsIgnoreCase("Restriction sites")) {
                this.setRestrictionSites(p.getValue());
            } else if (p.getName().equalsIgnoreCase("Tm seller")) {
                this.setTmSeller(this.getFloatValue(p.getValue()));
            } else if (p.getName().equalsIgnoreCase("Tm full")) {
                this.setTmFull(this.getFloatValue(p.getValue()));
            } else if (p.getName().equalsIgnoreCase("Tm gene")) {
                this.setTmGene(this.getFloatValue(p.getValue()));
            } else if (p.getName().equalsIgnoreCase("volumn in nL")) {
                this.setVolumnInNL(this.getFloatValue(p.getValue()));
            } else if (p.getName().equalsIgnoreCase("OD")) {
                this.setOd(this.getFloatValue(p.getValue()));
            } else if (p.getName().equalsIgnoreCase(FormFieldsNames.lenghtOnGene)) {
                this.setLenghtOnGene(this.getIntValue(p.getValue()));
            }

        }
        /*
         * too slow Parameter p=exp.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, "Purification
         * type"); if(p!=null) setPurificationType(p.getValue()); p=exp.findFirst(Experiment.PROP_PARAMETERS,
         * Parameter.PROP_NAME, "Starting Synthesis Scale"); if(p!=null)
         * setStartingSynthesisScale(p.getValue()); p=exp.findFirst(Experiment.PROP_PARAMETERS,
         * Parameter.PROP_NAME, "BufferType"); if(p!=null) setBufferType(p.getValue());
         * p=exp.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, "Normalization type"); if(p!=null)
         * setNormalization(p.getValue()); p=exp.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME,
         * "Restriction sites"); if(p!=null) setRestrictionSites(p.getValue());
         * p=exp.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, "Tm seller"); if(p!=null)
         * setTmSeller(getFloatValue(p.getValue())); p=exp.findFirst(Experiment.PROP_PARAMETERS,
         * Parameter.PROP_NAME, "Tm full"); if(p!=null) setTmFull(getFloatValue(p.getValue()));
         * p=exp.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, "Tm gene"); if(p!=null)
         * setTmGene(getFloatValue(p.getValue())); p=exp.findFirst(Experiment.PROP_PARAMETERS,
         * Parameter.PROP_NAME, "volumn in nL"); if(p!=null) setVolumnInNL(getFloatValue(p.getValue()));
         * p=exp.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, "OD"); if(p!=null)
         * setOD(getFloatValue(p.getValue()));
         */

    }

    Integer getIntValue(final String value) {
        if (value == null || value.equals("")) {
            return null;
        }

        return new Integer(value);
    }

    Float getFloatValue(final String value) {
        if (value == null || value.equals("")) {
            return null;
        }

        return new Float(value);
    }

    /**
     * @return the annotationHooks
     */
    public Set<org.pimslims.util.File> getFiles() {
        return this.annotations.keySet();
    }

    /**
     * @return the amountInUg
     */
    public Float getAmountInUg() {
        return this.amountInUg;
    }

    /**
     * @param amountInUg the amountInUg to set
     */
    public void setAmountInUg(final Float amountInUg) {
        this.amountInUg = amountInUg;
    }

    /**
     * @return the bufferType
     */
    public String getBufferType() {
        return this.bufferType;
    }

    /**
     * @param bufferType the bufferType to set
     */
    public void setBufferType(final String bufferType) {
        this.bufferType = bufferType;
    }

    /**
     * @return the concertrationInUM
     */
    public Float getConcertrationInUM() {
        return this.concertrationInUM;
    }

    /**
     * @param concertrationInUM the concertrationInUM to set
     */
    public void setConcertrationInUM(final Float concertrationInUM) {
        this.concertrationInUM = concertrationInUM;
    }

    /**
     * @return the mW
     */
    public Float getMw() {
        return this.MW;
    }

    /**
     * @param mw the mW to set
     */
    public void setMw(final Float mw) {
        this.MW = mw;
    }

    /**
     * @return the normalization
     */
    public String getNormalization() {
        return this.normalization;
    }

    /**
     * @param normalization the normalization to set
     */
    public void setNormalization(final String normalization) {
        this.normalization = normalization;
    }

    /**
     * @return the oD
     */
    public Float getOd() {
        return this.OD;
    }

    /**
     * @param od the oD to set
     */
    public void setOd(final Float od) {
        this.OD = od;
    }

    /**
     * @return the purificationType
     */
    public String getPurificationType() {
        return this.purificationType;
    }

    /**
     * @param purificationType the purificationType to set
     */
    public void setPurificationType(final String purificationType) {
        this.purificationType = purificationType;
    }

    /**
     * @return the restrictionSites
     */
    public String getRestrictionSites() {
        return this.restrictionSites;
    }

    /**
     * @param restrictionSites the restrictionSites to set
     */
    public void setRestrictionSites(final String restrictionSites) {
        this.restrictionSites = restrictionSites;
    }

    /**
     * @return the startingSynthesisScale
     */
    public String getStartingSynthesisScale() {
        return this.startingSynthesisScale;
    }

    /**
     * @param startingSynthesisScale the startingSynthesisScale to set
     */
    public void setStartingSynthesisScale(final String startingSynthesisScale) {
        this.startingSynthesisScale = startingSynthesisScale;
    }

    /**
     * @return the tmFull
     */
    public Float getTmFull() {
        return this.tmFull;
    }

    /**
     * @param tmFull the tmFull to set
     */
    public void setTmFull(final Float tmFull) {
        this.tmFull = tmFull;
    }

    /**
     * @return the tmGene
     */
    public Float getTmGene() {
        return this.tmGene;
    }

    /**
     * @param tmGene the tmGene to set
     */
    public void setTmGene(final Float tmGene) {
        this.tmGene = tmGene;
    }

    /**
     * @return the tmSeller
     */
    public Float getTmSeller() {
        return this.tmSeller;
    }

    /**
     * @param tmSeller the tmSeller to set
     */
    public void setTmSeller(final Float tmSeller) {
        this.tmSeller = tmSeller;
    }

    /**
     * @return the volumnInNL
     */
    public Float getVolumnInNL() {
        return this.volumnInNL;
    }

    /**
     * @param volumnInNL the volumnInNL to set
     */
    public void setVolumnInNL(final Float volumnInNL) {
        this.volumnInNL = volumnInNL;
    }

    /**
     * @return the annotations
     */
    public Map<File, String> getAnnotations() {
        return this.annotations;
    }

    /**
     * @param annotations the annotations to set
     */
    public void setAnnotations(final Map<File, String> annotations) {
        this.annotations = annotations;
    }

    /**
     * @return the lenghtOnGene
     */
    public Integer getLenghtOnGene() {
        return this.lenghtOnGene;
    }

    /**
     * @param lenghtOnGene the lenghtOnGene to set
     */
    public void setLenghtOnGene(final Integer lenghtOnGene) {
        this.lenghtOnGene = lenghtOnGene;
    }

}
