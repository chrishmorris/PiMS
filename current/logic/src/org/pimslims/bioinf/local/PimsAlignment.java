/**
 * pims-web org.pimslims.bioinf.local PimsAlignment.java
 * 
 * @author cm65
 * @date 3 Jun 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65 *
 * 
 * 
 */
package org.pimslims.bioinf.local;

import java.text.NumberFormat;
import java.util.Collection;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;

/**
 * PimsAlignment
 * 
 */
public abstract class PimsAlignment implements Comparable<PimsAlignment> {

    public enum ComponentTypes {
        Target_DNA, Target_Protein, Construct, Primer
    }

    protected final String querySequence;

    protected final NumberFormat nf;

    protected final boolean revComplement;

    final Molecule molecule;

    /**
     * @param querySequence
     * @param molecule
     * @param nf
     * @param revComplement
     */
    public PimsAlignment(final String querySequence, final Molecule molecule, final boolean revComplement) {
        super();
        this.querySequence = querySequence;
        this.nf = NumberFormat.getInstance();
        this.nf.setMaximumFractionDigits(2);
        this.revComplement = revComplement;
        this.molecule = molecule;
    }

    public abstract String getHitName();

    /**
     */
    public int compareTo(final PimsAlignment other) {
        return new Integer(other.getScore()).compareTo(this.getScore());
    }

    /**
     * @return the penalty for opening a gap
     */
    public abstract int getOpeningPenalty();

    public abstract String[] getFormatted();

    /**
     * Due to bug in implementation the length of template is the only known
     * 
     * @return
     */
    public abstract int getTemplateLength();

    /**
     * return the percentage of identical bases in the aligned subsequence
     */
    public String getPercentIdentity() {
        final float percent = ((float) this.getIdentity() / (float) this.getTemplateLength()) * 100;
        return this.nf.format(percent);
    }

    /**
     */
    public String getPercentSimilarity() {
        return this.nf.format(((float) this.getSimilarity() / (float) this.getTemplateLength()) * 100);
    }

    /**
     */
    public String getPercentGaps() {
        return this.nf.format(((float) this.getGaps() / (float) this.getTemplateLength()) * 100);
    }

    /**
     * @return the panalty for extending a gap
     */
    public abstract int getExtensionPenalty();

    /**
     * @return the number of indels in the alignment
     */
    public abstract int getGaps();

    /**
     * @return the number of identical bases in the alignment
     */
    public abstract int getIdentity();

    /**
     * @return the score of the alignment
     */
    public abstract int getScore();

    /**
     * @return
     * @see jaligner.Alignment#getSimilarity()
     */
    public abstract int getSimilarity();

    /**
     * @return the subsequence of the target which was aligned
     */
    public abstract String getHitSequence();

    /**
     * @return the name of the matrix used
     * 
     *         public abstract String getMatrixId();
     */

    public int getQuerySeqLength() {
        return this.querySequence.length();
    }

    public String getIdentityOverQuerySeq() {
        return this.nf.format((float) this.getIdentity() / this.getQuerySeqLength() * 100);
    }

    public boolean getIsReverseComplement() {
        return this.revComplement;
    }

    public static ComponentTypes getMolComponentType(final Molecule mcomp) {

        if (mcomp.getDnaForTargets().isEmpty() && !mcomp.getResearchObjectiveElements().isEmpty()) {
            return ComponentTypes.Construct;
        }
        if (!mcomp.getDnaForTargets().isEmpty()) {
            return ComponentTypes.Target_DNA;
        }
        if (!mcomp.getProteinForTargets().isEmpty()) {
            return ComponentTypes.Target_Protein;
        }
        if (mcomp.getDnaForTargets().isEmpty() && !mcomp.getRelatedResearchObjectiveElements().isEmpty()) {
            //TODO handle this case specifically
            return ComponentTypes.Construct;
        }
        final Collection<SampleComponent> scs =
            mcomp.get_Version().findAll(SampleComponent.class, SampleComponent.PROP_REFCOMPONENT, mcomp);
        if (!scs.isEmpty()) {
            return ComponentTypes.Primer;
        }

        return null;
    }

    /**
     * @return the molecule which was found
     */
    public Molecule getHit(final ReadableVersion rv) {
        return this.molecule;
    }

    public String getHook() {
        if (null == this.molecule) {
            return "";
        }
        return this.molecule.get_Hook();
    }

    public String getType() {
        return this.molecule.getMolType();
    }

    protected ComponentTypes getCompType() {
        final ComponentTypes ct = PimsAlignment.getMolComponentType(this.molecule);
        return ct;
    }

    public String getDetails() {
        return this.molecule.getDetails();
    }

    public String getCompTypeDecorated() {
        final ComponentTypes ct = this.getCompType();
        if (ct == ComponentTypes.Target_DNA || ct == ComponentTypes.Target_Protein) {
            return "Target";
        }
        if (ct == null) {
            return "Unknown";
        }
        return ct.toString();
    }

    /**
     * 
     * @return Hook to ResearchObjective if the sequence was part of Construct sequence hook to a target if
     *         sequence was linked to one (the first target will be returned) otherwise return hook to the
     *         Molecule itself
     */
    public String getLinkedHook() {
        if (this.getCompType() == ComponentTypes.Construct) {
            ResearchObjective exb = null;
            for (final ResearchObjectiveElement bc : this.molecule.getResearchObjectiveElements()) {
                exb = bc.getResearchObjective();
                if (exb != null) {
                    break;
                }
            }
            if (exb == null) {
                for (final ResearchObjectiveElement bc : this.molecule.getRelatedResearchObjectiveElements()) {
                    exb = bc.getResearchObjective();
                    if (exb != null) {
                        break;
                    }
                }
            }
            return exb.get_Hook();
        }

        if (this.getCompType() == ComponentTypes.Target_DNA) {
            for (final Target t : this.molecule.getDnaForTargets()) {
                if (t != null) {
                    return t.get_Hook();
                }
            }
        }
        if (this.getCompType() == ComponentTypes.Target_Protein) {
            for (final Target t : this.molecule.getProteinForTargets()) {
                if (t != null) {
                    return t.get_Hook();
                }
            }
        }

        if (this.getCompType() == ComponentTypes.Primer) {
            final SampleComponent scs =
                this.molecule.get_Version().findFirst(SampleComponent.class,
                    SampleComponent.PROP_REFCOMPONENT, this.molecule);
            if (scs != null) {
                final AbstractSample as = scs.getAbstractSample();
                if (as != null) {
                    return as.get_Hook();
                } else {
                    return scs.get_Hook();
                }
            }
        }
        // back up case
        return this.getHook();
    }

    public String getLinkedName() {
        final ModelObject modelObject = this.molecule.get_Version().get(this.getLinkedHook());
        return modelObject.get_Name();
    }

/*
    public String getIdentityOverHit() {
        return this.nf.format((float) this.getIdentity() / this.getHitSeqLength() * 100);
    } */

    public String getIdentityOverAlignment() {
        return this.nf.format((float) this.getIdentity() / this.getTemplateLength() * 100);
    }

    /**
     * @return
     */
    public abstract String getQueryName();
}
