package org.pimslims.presentation.leeds;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ConstructUtility;
import org.pimslims.lab.Util;
import org.pimslims.lab.Utils;
import org.pimslims.leeds.FormFieldsNames;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.PrimerBeanWriter;
import org.pimslims.presentation.construct.ConstructBeanWriter;

/**
 * @author Petr Troshin aka pvt43
 * 
 *         PrimerFormBean
 * 
 */

@Deprecated
// Leeds code is no longer supported
public class PrimerFormBean implements FormFieldsNames {
    // Hook to forward primer
    String fprimerHook;

    // Hook to reverse primer
    String rprimerHook;

    // Forward Primer
    PrimerBean forwardPrimer = new PrimerBean(true);

    // Reverse Primer
    PrimerBean reversePrimer = new PrimerBean(false);

    // Comments
    String comments;

    // The plasmid for which the primer is designed
    String plasmideconcerned;

    // Author of the primers
    String designedBy;

    // Target gene clonned from (primers position in relation to the gene
    // sequence)
    String from;

    // Target gene clonned to (primers position in relation to the gene
    // sequence)
    String to;

    // The length of the clonned part of the gene
    String genelength;

    // Introduced by the primers modifications (e.g. point mutations)
    String modif;

    // The length of the PCR product
    String pcrprodlength;

    // Whom to send the form that the primers to be ordered
    String sendformto;

    // Date of creation
    Date date;

    /**
     * Constructor for PrimerFormBean
     */
    public PrimerFormBean() {
        super();
        // this constructor is required for a useBean declaration
    }

    // Full form constructor
    public PrimerFormBean(final PrimerBean forwardPrimer, final PrimerBean reversePrimer,
        final String comments, final String designedBy, final String plasmideconcerned, final String from,
        final String to, final String genelength, final String modif, final String pcrprodlength,
        final String sendformto, final Date date) {
        this.forwardPrimer = forwardPrimer;
        this.reversePrimer = reversePrimer;
        this.comments = comments;
        this.designedBy = designedBy;
        this.plasmideconcerned = plasmideconcerned;
        this.from = from; // Integer.parseInt(from.trim());
        this.to = to; // Integer.parseInt(to.trim());
        this.genelength = genelength;
        this.modif = modif;
        this.pcrprodlength = pcrprodlength;
        this.sendformto = sendformto;
        this.date = date;
    }

    // Bare form constructor
    public PrimerFormBean(final PrimerBean forwardPrimer, final PrimerBean reversePrimer) {
        this.forwardPrimer = forwardPrimer;
        this.reversePrimer = reversePrimer;
    }

    public static final Pattern PRIMER_NAME = Pattern.compile("^(\\w*)( |-|)[RFrf]\\d*$");

    public Experiment create(final WritableVersion version) throws AccessException, ConstraintException {
        assert version != null : "Please provide WritableVersion !";
        assert this.forwardPrimer != null && this.reversePrimer != null;
        this.forwardPrimer.setDirection(ConstructBeanWriter.FPRIMER);
        this.reversePrimer.setDirection(ConstructBeanWriter.RPRIMER);
        final Experiment experiment =
            PrimerBeanWriter.createExperiment(version, version.getCurrentDefaultOwner(),
                this.forwardPrimer.getName() + " / " + this.reversePrimer.getName(),
                FormFieldsNames.leedscloningDesign);
        final PrimerForm pform = new PrimerForm(experiment, version);
        PrimerBeanWriter.writePrimer(version, this.forwardPrimer, experiment);
        PrimerBeanWriter.writePrimer(version, this.reversePrimer, experiment);
        pform.setForwardPrimer(this.forwardPrimer);
        pform.setReversePrimer(this.reversePrimer);
        this.update(pform);

        // create an expression objective
        final ResearchObjective ro = new ResearchObjective(version, experiment.getName(), "primer form");
        final ResearchObjectiveElement element =
            new ResearchObjectiveElement(version, ConstructUtility.SPOTCONSTRUCT, "primer form", ro);
        if (null != this.getFrom()) {
            element.setApproxBeginSeqId(Integer.valueOf(this.getFrom()));
        }
        if (null != this.getTo()) {
            element.setApproxEndSeqId(Integer.valueOf(this.getTo()));
        }
        experiment.setProject(ro);

        // link to target if possible
        Target target = null;
        Matcher m = PrimerFormBean.PRIMER_NAME.matcher(this.forwardPrimer.getName());
        if (m.matches()) {
            final String targetName = m.group(1);
            target = version.findFirst(Target.class, Target.PROP_NAME, targetName);
        }
        if (null == target) {
            m = PrimerFormBean.PRIMER_NAME.matcher(this.reversePrimer.getName());
            if (m.matches()) {
                final String targetName = m.group(1);
                target = version.findFirst(Target.class, Target.PROP_NAME, targetName);
            }
        }
        element.setTarget(target);

        return experiment;
    }

    public void update(final PrimerForm pform) throws ConstraintException, AccessException {
        if (!Util.isEmpty(this.comments)) {
            pform.setComments(this.comments);
        }
        if (!Util.isEmpty(this.designedBy)) {
            pform.setdesignedBy(this.designedBy);
        }
        if (!Util.isEmpty(this.plasmideconcerned)) {
            pform.setPlasmid(this.plasmideconcerned);
        }
        if (!Util.isEmpty(this.from)) {
            pform.setFrom(this.from);
        }
        if (!Util.isEmpty(this.to)) {
            pform.setTo(this.to);
        }
        if (!Util.isEmpty(this.genelength)) {
            pform.setGeneLength(this.genelength);
        }
        if (!Util.isEmpty(this.modif)) {
            pform.setModifications(this.modif);
        }
        if (!Util.isEmpty(this.pcrprodlength)) {
            pform.setPCRProductLength(this.pcrprodlength);
        }
        if (!Util.isEmpty(this.sendformto)) {
            pform.setSendFormTo(this.sendformto);
        }
        if (this.date != null) {
            pform.setDate(this.date);
        }
    }

    /**
     * @return the comments
     */
    public final String getComments() {
        return this.comments;
    }

    /**
     * @param comments the comments to set
     */
    public final void setComments(final String comments) {
        this.comments = comments;
    }

    /**
     * @return the date
     */
    public final String getDate() {
        return Utils.getDate(this.date);
    }

    /**
     * @param date the date to set
     */
    public final void setDate(final String date) {
        this.date = AbstractConstructBean.parseDate(date);
    }

    /**
     * @return the designedBy
     */
    public final String getDesignedBy() {
        return this.designedBy;
    }

    /**
     * @param designedBy the designedBy to set
     */
    public final void setDesignedBy(final String designedBy) {
        this.designedBy = designedBy;
    }

    /**
     * @return the forwardPrimer
     * 
     *         public final String getFprimerHook() { return fprimerHook; } /**
     * @param forwardPrimer the forwardPrimer to set
     * 
     *            public final void setFprimerHook(String fprimerHook) { assert Utils.isHookValid(fprimerHook)
     *            : "Primer hook is invalid"; this.fprimerHook= fprimerHook; }
     */

    /**
     * @return the forwardPrimer
     */
    public final PrimerBean getForwardPrimer() {
        return this.forwardPrimer;
    }

    /**
     * @param forwardPrimer the forwardPrimer to set
     */
    public final void setForwardPrimer(final PrimerBean forwardPrimer) {
        this.forwardPrimer = forwardPrimer;
    }

    /**
     * @return the reversePrimer
     */
    public final PrimerBean getReversePrimer() {
        return this.reversePrimer;
    }

    /**
     * @param reversePrimer the reversePrimer to set
     */
    public final void setReversePrimer(final PrimerBean reversePrimer) {
        this.reversePrimer = reversePrimer;
    }

    /**
     * @return the from
     */
    public final String getFrom() {
        return this.from;
    }

    /**
     * @param from the from to set
     */
    public final void setFrom(final String from) {
        this.from = from;
    }

    /**
     * @return the genelength
     */
    public final String getGenelength() {
        return this.genelength;
    }

    /**
     * @param genelength the genelength to set
     */
    public final void setGenelength(final String genelength) {
        this.genelength = genelength;
    }

    /**
     * @return the modif
     */
    public final String getModif() {
        return this.modif;
    }

    /**
     * @param modif the modif to set
     */
    public final void setModif(final String modif) {
        this.modif = modif;
    }

    /**
     * @return the pcrprodlength
     */
    public final String getPcrprodlength() {
        return this.pcrprodlength;
    }

    /**
     * @param pcrprodlength the pcrprodlength to set
     */
    public final void setPcrprodlength(final String pcrprodlength) {
        this.pcrprodlength = pcrprodlength;
    }

    /**
     * @return the plasmideconcerned
     */
    public final String getPlasmideconcerned() {
        return this.plasmideconcerned;
    }

    /**
     * @param plasmideconcerned the plasmideconcerned to set
     */
    public final void setPlasmideconcerned(final String plasmideconcerned) {
        this.plasmideconcerned = plasmideconcerned;
    }

    /**
     * @return the reversePrimer
     */
    public final String getRprimerHook() {
        return this.rprimerHook;
    }

    /**
     * @param reversePrimer the reversePrimer to set
     */
    public final void setRprimerHook(final String rprimerHook) {
        assert Util.isHookValid(rprimerHook) : "Primer hook is invalid";
        this.rprimerHook = rprimerHook;
    }

    /**
     * @return the sendformto
     */
    public final String getSendformto() {
        return this.sendformto;
    }

    /**
     * @param sendformto the sendformto to set
     */
    public final void setSendformto(final String sendformto) {
        this.sendformto = sendformto;
    }

    /**
     * @return the to
     */
    public final String getTo() {
        return this.to;
    }

    /**
     * @param to the to to set
     */
    public final void setTo(final String to) {
        this.to = to;
    }

}
