package org.pimslims.presentation.construct;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.TargetBean;

/**
 * Presentation layer ConstructBean
 * 
 * @author Johan van Niekerk
 */
public class ConstructBean extends ConstructBeanForList {

    /**
     * FINAL_PROTEIN String
     */
    public static final String FINAL_PROTEIN = "Final Protein";

    /**
     * EXPRESSED_PROTEIN String
     */
    public static final String EXPRESSED_PROTEIN = "Expressed Protein";

    /**
     * REVERSE_OVERLAP String
     */
    static final String REVERSE_OVERLAP = "Reverse Overlap";

    /**
     * FORWARD_OVERLAP String
     */
    static final String FORWARD_OVERLAP = "Forward Overlap";

    /**
     * REVERSE_TAG String
     */
    static final String REVERSE_TAG = "Reverse Tag";

    /**
     * FORWARD_TAG String
     */
    static final String FORWARD_TAG = "Forward Tag";

    /**
     * 
     */
    static final String FALSE = "No";

    /**
     * 
     */
    static final String TRUE = "Yes";

    // primerless construct
    public ConstructBean(final TargetBean tb) {
        this(tb, null, null);
    }

    public ConstructBean(final ResearchObjective ro, final TargetBean tb, final PrimerBean forward,
        final PrimerBean reverse) {
        super(ro, tb, forward, reverse);
        this.dnaTarget = tb.isDNATarget() ? "dnaTarget" : null;
    }

    public ConstructBean(final TargetBean tb, final PrimerBean forward, final PrimerBean reverse) {
        super(tb, forward, reverse);
        if (tb != null) {
            this.setAccess(tb.getAccess());
        }
        if (null != tb) {
            this.dnaTarget = tb.isDNATarget() ? "dnaTarget" : null;
        }
    }

    //TODO no, use field of superclass
    private String name;

    private Calendar created;

    private String localName;

    private String dnaSeq;

    private String protSeq;

    private String pcrProduct;

    private Double pcrProductGC;

    private String description;

    private String comments;

    //private String forwardTag;

    //private String reverseTag;

    private String expressedProt;

    private String finalProt;

    private Integer targetProtStart;

    private Integer targetProtEnd;

    //Elements for DNA Target constructs

    private String dnaTarget;

    private Integer targetDnaStart;

    private Integer targetDnaEnd;

    private Double extinction;

    private Double abs01pc;

    private Double weight;

    private Double protPi;

    private Date dateOfEntry = new Date();

    private String userName;

    private String finalProtN;

    private String finalProtC;

    private Float desiredTm;

    private Double expProtWeight;

    private Double expProtExtinction;

    private Double expProtabs01pc;

    private Long expProtLen;

    private Double expProtPi;

    //For Synthetic gene
    private String sgSampleHook;

    private String sgSampleName;

    private String pcrProductHook;

    private String descriptionHook;

    private String commentsHook;

    private String finalProteinHook;

    private String expressedProteinHook;

    private String userHook;

    private String protSeqHook;

    private String templateName;

    private String wildDNASeq;

    private boolean mayDelete;

    private boolean SDMConstruct = false;

    private String vectorInputSampleHook = null;

    // Getters and Setters
    public String getComments() {
        return this.comments;
    }

    public void setComments(final String comments) {
        this.comments = comments;
    }

    public void setCommentsHook(final String string) {
        this.commentsHook = string;
    }

    /**
     * @return Returns the commentsHook.
     */
    public String getCommentsHook() {
        return this.commentsHook;
    }

    public void setConstructId(final String constructId) {
        this.name = constructId;
    }

    public Calendar getCreationDate() {
        return this.created;
    }

    public void setCreationDate(final Calendar created) {
        this.created = created;
    }

    public String getLocalName() {
        return this.localName;
    }

    public void setLocalName(final String localName) {
        this.localName = localName;
    }

    public Date getDateOfEntry() {
        return this.dateOfEntry;
    }

    public void setDateOfEntry(final Date dateOfEntry) {
        this.dateOfEntry = dateOfEntry;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setDescriptionHook(final String string) {
        this.descriptionHook = string;
    }

    /**
     * @return Returns the descriptionHook.
     */
    public String getDescriptionHook() {
        return this.descriptionHook;
    }

    public Float getDesiredTm() {
        return this.desiredTm;
    }

    public void setDesiredTm(final Float desiredTm) {
        this.desiredTm = desiredTm;
    }

    public String getDnaSeq() {
        return this.dnaSeq;
    }

    public void setDnaSeq(final String dnaSeq) {
        this.dnaSeq = dnaSeq;
    }

    public int getDnaLen() {
        if (this.dnaSeq == null) {
            return 0;
        }
        return this.dnaSeq.length();
    }

    public int getDnaLenBy3() {
        return this.getDnaLen() / 3;
    }

    public String getDnaTarget() {
        return this.dnaTarget;
    }

    public void setDnaTarget(final String dnaTarget) {
        this.dnaTarget = dnaTarget;
    }

    public String getExpressedProt() {
        return this.expressedProt;
    }

    public void setExpressedProt(final String expressedProt) {
        this.expressedProt = expressedProt;
    }

    public void setExpressedProteinHook(final String string) {
        this.expressedProteinHook = string;
    }

    /**
     * @return Returns the expressedProteinHook.
     */
    public String getExpressedProteinHook() {
        return this.expressedProteinHook;
    }

    /**
     * @return Returns the Expressed Protein Extinction.
     */
    public Double getExpProtExtinction() {
        return this.expProtExtinction;
    }

    /**
     * @param expProtExtinction The Expressed Protein Extinction to set.
     */
    public void setExpProtExtinction(final Double expProtExtinction) {
        this.expProtExtinction = expProtExtinction;
    }

    public Double getExpProtabs01pc() {
        return this.expProtabs01pc;
    }

    public void setExpProtabs01pc(final Double expProtabs01pc) {
        this.expProtabs01pc = expProtabs01pc;
    }

    /**
     * @return Returns the Expressed Protein Len.
     */
    public Long getExpProtLen() {
        return this.expProtLen;
    }

    /**
     * @param expProtLen The Expressed Protein Len to set.
     */
    public void setExpProtLen(final Long expProtLen) {
        this.expProtLen = expProtLen;
    }

    /**
     * @return Returns the Expressed Protein Pi.
     */
    public Double getExpProtPi() {
        return this.expProtPi;
    }

    /**
     * @param expProtPi The Expressed Protein Pi to set.
     */
    public void setExpProtPi(final Double expProtPi) {
        this.expProtPi = expProtPi;
    }

    /**
     * @return Returns the Expressed Protein Weight.
     */
    public Double getExpProtWeight() {
        return this.expProtWeight;
    }

    /**
     * @param expProtWeight The Expressed Protein Weight to set.
     */
    public void setExpProtWeight(final Double expProtWeight) {
        this.expProtWeight = expProtWeight;
    }

    public Double getExtinction() {
        return this.extinction;
    }

    public void setExtinction(final Double extinction) {
        this.extinction = extinction;
    }

    public Double getAbs01pc() {
        return this.abs01pc;
    }

    public void setAbs01pc(final Double abs01pc) {
        this.abs01pc = abs01pc;
    }

    public String getFinalProt() {
        return this.finalProt;
    }

    public void setFinalProt(final String finalProt) {
        this.finalProt = finalProt;
    }

    public String getFinalProtC() {
        return this.finalProtC;
    }

    public void setFinalProtC(final String finalProtC) {
        this.finalProtC = finalProtC;
    }

    public String getFinalProtN() {
        return this.finalProtN;
    }

    public void setFinalProtN(final String finalProtN) {
        this.finalProtN = finalProtN;
    }

    public Integer getFinalProteinLength() {
        if (null == this.getFinalProt()) {
            return null;
        }
        return this.getFinalProt().length();
    }

    public void setFinalProteinHook(final String string) {
        this.finalProteinHook = string;
    }

    /**
     * @return Returns the finalProteinHook.
     */
    public String getFinalProteinHook() {
        return this.finalProteinHook;
    }

    public String getForwardTag() {
        return this.forwardPrimerBean.getTag();
    }

    public void setForwardTag(final String forwardTag) {
        this.forwardPrimerBean.setTag(forwardTag);
    }

    /**
     * ConstructBean.getForwardExtension
     * 
     * @return
     */
    public String getForwardExtension() {
        return this.forwardPrimerBean.getExtension();
    }

    public void setForwardExtension(final String extension) {
        this.forwardPrimerBean.setExtension(extension);
    }

    public String getFwdOverlap() {
        return this.forwardPrimerBean.getOverlap();
    }

    public Integer getFwdOverlapLen() {
        if (null == this.forwardPrimerBean.getLengthOnGeneString()
            || "".equals(this.forwardPrimerBean.getLengthOnGeneString())) {
            return new Integer(0);
        }
        return Integer.valueOf(this.forwardPrimerBean.getLengthOnGeneString());
    }

    public void setFwdOverlapLen(final Integer overlapLen) {
        this.forwardPrimerBean.setLengthOnGeneString(Integer.toString(overlapLen));
    }

    public String getFwdPrimer() {
        return this.forwardPrimerBean.getSequence();
    }

    public void setFwdPrimer(final String fwdPrimer) {
        this.forwardPrimerBean.setSequence(fwdPrimer);
    }

    public String getFwdPrimerName() {
        return this.forwardPrimerBean.getName();
    }

    public float getGcContent() {
        if (this.dnaSeq != null) {
            return (new DnaSequence(this.dnaSeq)).getGCContent();
        }
        // else
        return 0;
    }

    public String getIsStopCodon() {
        if (this.dnaSeq != null) {
            if ((new DnaSequence(this.dnaSeq)).getIsStopCodon()) {
                return ConstructBean.TRUE;
            }
            return ConstructBean.FALSE;
        }
        // else
        return ConstructBean.FALSE;
    }

    /**
     * 
     * @return does target DNA contains Construct DNA but NOT equal
     */
    public String getIsDoMainConstruct() {
        if (this.dnaSeq == null || this.getTargetDnaSeq() == null) {
            return ConstructBean.FALSE;
        }

        if (this.getTargetDnaSeq().equalsIgnoreCase(this.dnaSeq)) {
            return ConstructBean.FALSE;
        } else if (this.getTargetDnaSeq().toUpperCase().contains(this.dnaSeq.toUpperCase())) {
            return ConstructBean.TRUE;
        } else {
            return ConstructBean.FALSE;
        }
    }

    /*
     * PiMS 3164 make latest milestone details available for search constructs
     */
    public void setLatestMilestone(final Milestone milestone) {
        this.latestMilestone = milestone;
    }

    public Milestone getLatestMilestone() {
        return this.latestMilestone;
    }

    public ModelObjectBean getLatestMilestoneBean() {
        if (null == this.latestMilestone) {
            return null;
        }
        return BeanFactory.newBean(this.latestMilestone);
    }

    public Calendar getLatestMilestoneStatusDate() {
        if (null == this.latestMilestone || null == this.latestMilestone.getExperiment()) {
            return null;
        }
        return this.latestMilestone.getDate();
    }

    /**
     * ConstructBean.getMayDelete
     * 
     * @return
     */
    @Override
    public boolean getMayDelete() {
        return this.mayDelete;
    }

    /**
     * ConstructBean.setMayDelete
     * 
     * @param get_MayDelete
     */
    public void setMayDelete(final boolean mayDelete) {
        this.mayDelete = mayDelete;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    /* 
     * @return if name is T0017.full, returns "full"
     * */
    public String getNameSuffix() {
        final int i = this.name.indexOf('.');
        if (-1 == i) {
            return this.name;
        }
        return this.name.substring(i + 1);
    }

    /**
     * @return the organism
     */

    public ModelObjectBean getOrganismBean() {
        return this.targetBean.getOrganismBean();
    }

    public String getOrganismName() {
        if (null == this.targetBean.getOrganismBean()) {
            return null;
        }
        return this.targetBean.getOrganismBean().getName();
    }

    @Deprecated
    public String getPimsExpBlueprintHook() {
        return this.getHook();
    }

    public void setPimsExpBlueprintHook(final String hook) {
        this.setHook(hook);
    }

    public String getPcrProductSeqHook() {
        return this.pcrProductHook;
    }

    public void setPcrProductSeqHook(final String pimsPCRProductHook) {
        this.pcrProductHook = pimsPCRProductHook;
    }

    //TODO rename as setInsert
    public void setPcrProductSeq(final String pcrProduct) {
        this.pcrProduct = pcrProduct;
    }

    //TODO rename as getInsert
    public String getPcrProductSeq() {
        return this.pcrProduct;
    }

    public int getPcrProductSize() {
        if (null != this.pcrProduct) {
            return this.pcrProduct.replaceAll(" ", "").length();
        }
        return 0;
    }

    /**
     * @return Returns the pcrProductGC.
     */
    public Double getPcrProductGC() {
        return this.pcrProductGC;
    }

    /**
     * @param pcrProductGC The pcrProductGC to set.
     */
    public void setPcrProductGC(final Double pcrProductGC) {
        this.pcrProductGC = pcrProductGC;
    }

    public List<PrimerBean> getPrimers() {
        final List<PrimerBean> ret = new ArrayList(2);
        if (null != this.forwardPrimerBean) {
            ret.add(this.forwardPrimerBean);
        }
        if (null != this.reversePrimerBean) {
            ret.add(this.reversePrimerBean);
        }
        return ret;
    }

    public Double getProtPi() {
        return this.protPi;
    }

    public void setProtPi(final Double protPi) {
        this.protPi = protPi;
    }

    public String getProtSeq() {
        return this.protSeq;
    }

    public void setProtSeq(final String protSeq) {
        this.protSeq = protSeq;
    }

    /**
     * @return Returns the protSeqHook.
     */
    public String getProtSeqHook() {
        return this.protSeqHook;
    }

    /**
     * @param protSeqHook The protSeqHook to set.
     */
    public void setProtSeqHook(final String protSeqHook) {
        this.protSeqHook = protSeqHook;
    }

    public String getRevOverlap() {
        final Integer revOverlapLen = this.getRevOverlapLen();
        if (null != revOverlapLen && null != this.getRevPrimer()) {
            if (revOverlapLen.intValue() > this.getRevPrimer().length()) {
                return this.getRevPrimer();
            } else {
                return this.getRevPrimer().substring(this.getRevPrimer().length() - revOverlapLen.intValue());
            }
        } else {
            return "";
        }
    }

    /**
     * ConstructBean.getReversePrimer
     * 
     * @return
     */
    public PrimerBean getReversePrimer() {
        return this.reversePrimerBean;
    }

    /**
     * ConstructBean.getReverseExtension
     * 
     * @return
     */
    public String getReverseExtension() {
        return this.reversePrimerBean.getExtension();
    }

    public void setReverseExtension(final String extension) {
        this.reversePrimerBean.setExtension(extension);
    }

    /**
     * ConstructBean.getReverseSeq
     * 
     * @return the reverse complement of the DNA sequence
     */
    public String getReverseSeq() {
        final DnaSequence dna = new DnaSequence(this.getDnaSeq());
        return dna.getReverseComplement().getSequence();
    }

    public String getReverseTag() {
        return this.reversePrimerBean.getTag();
    }

    public void setReverseTag(final String reverseTag) {
        this.reversePrimerBean.setTag(reverseTag);
    }

    public Integer getRevOverlapLen() {
        if (null == this.reversePrimerBean.getLengthOnGeneString()
            || "".equals(this.reversePrimerBean.getLengthOnGeneString())) {
            return new Integer(0);
        }
        return Integer.valueOf(this.reversePrimerBean.getLengthOnGeneString());
    }

    public void setRevOverlapLen(final Integer overlapLen) {
        this.reversePrimerBean.setLengthOnGeneString(Integer.toString(overlapLen));
    }

    public String getRevPrimer() {
        return this.reversePrimerBean.getSequence();
    }

    public void setRevPrimer(final String revPrimer) {
        this.reversePrimerBean.setSequence(revPrimer);
    }

    public String getRevPrimerName() {
        return this.reversePrimerBean.getName();
    }

    /**
     * ConstructBean.SDMConstruct
     * 
     * @param parameter
     */
    public void setSDMConstruct(final boolean sdm) {
        this.SDMConstruct = sdm;
    }

    public boolean getSdmConstruct() {
        return this.SDMConstruct;
    }

    /**
     * ConstructBean.getTargetBean
     * 
     * @return the target bean
     */
    public Collection<TargetBean> getTargetBeans() {
        if (null == this.targetBean) {
            return Collections.EMPTY_LIST;
        }
        return Collections.singleton(this.targetBean);
    }

    public String getTargetName() {
        if (null == this.targetBean) {
            return null;
        }
        return this.targetBean.getName();
    }

    @Deprecated
    // set in TargetBean
    public void setTargetName(final String name) {
        this.targetBean.setName(name);
    }

    public String getTargetHook() {
        return this.targetBean.getHook();
    }

    @Deprecated
    // set in TargetBean
    public void setTargetHook(final String pimsTargetHook) {
        this.targetBean.setHook(pimsTargetHook);
    }

    public Integer getTargetDnaEnd() {
        return this.targetDnaEnd;
    }

    public void setTargetDnaEnd(final Integer targetDnaEnd) {
        this.targetDnaEnd = targetDnaEnd;
    }

    public Integer getTargetDnaStart() {
        return this.targetDnaStart;
    }

    public void setTargetDnaStart(final Integer targetDnaStart) {
        this.targetDnaStart = targetDnaStart;
    }

    public String getTargetDnaSeq() {
        return this.targetBean.getDnaSeq();
    }

    @Deprecated
    // set in TargetBean
    public void setTargetDnaSeq(final String targetDnaSeq) {
        this.targetBean.setDnaSeq(targetDnaSeq);
    }

    public Integer getTargetProtEnd() {
        return this.targetProtEnd;
    }

    public void setTargetProtEnd(final Integer targetProtEnd) {
        this.targetProtEnd = targetProtEnd;
    }

    public Integer getTargetProtStart() {
        return this.targetProtStart; //TODO targetDNAStart / 3
    }

    public void setTargetProtStart(final Integer targetProtStart) {
        this.targetProtStart = targetProtStart; //TODO targetDnaStart = 3 *
    }

    public String getTargetProtSeq() {
        return this.targetBean.getProtSeq();
    }

    public void setTargetProtSeq(final String targetProtSeq) {
        if (null == targetProtSeq) {
            this.targetBean.setProtSeq(null);
        } else {
            final String protseq = targetProtSeq.replaceAll(" ", "");
            this.targetBean.setProtSeq(protseq);
        }
    }

    /**
     * @return Returns the templateName.
     */
    public String getTemplateName() {
        return this.templateName;
    }

    /**
     * @param templateName The templateName to set.
     */
    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }

    /**
     * @param get_Hook
     */
    public void setUserHook(final String hook) {
        this.userHook = hook;
    }

    /**
     * @return Returns the personHook.
     */
    public String getUserHook() {
        return this.userHook;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(final String name) {
        this.userName = name;
    }

    public final String getVectorInputSampleHook() {
        return this.vectorInputSampleHook;
    }

    /**
     * ConstructBean.setVectorInputSampleHook
     * 
     * @param get_Hook
     */
    public void setVectorInputSampleHook(final String hook) {
        this.vectorInputSampleHook = hook;
    }

    public Double getWeight() {
        return this.weight;
    }

    public void setWeight(final Double weight) {
        this.weight = weight;
    }

    /**
     * ConstructBean.setWildDnaSeq
     * 
     * @param parameter
     */
    public void setWildDnaSeq(final String parameter) {
        this.wildDNASeq = parameter;
    }

    public String getWildDnaSeq() {
        return this.wildDNASeq;
    }

    /**
     * @return Returns the whyChosen.
     */
    public String getWhyChosen() {
        return this.targetBean.getWhyChosen();
    }

    public final boolean isPrimerless() {
        return (null == this.forwardPrimerBean || null == this.forwardPrimerBean.getSequence())
            && (null == this.reversePrimerBean || null == this.reversePrimerBean.getSequence());
    }

    public String getSgSampleHook() {
        return this.sgSampleHook;
    }

    public void setSgSampleHook(final String sgSampleHook) {
        this.sgSampleHook = sgSampleHook;
    }

    public String getSgSampleName() {
        return this.sgSampleName;
    }

    public void setSgSampleName(final String sgSampleName) {
        this.sgSampleName = sgSampleName;
    }

    /**
     * ConstructBean.suggestPrimerNames
     */
    public void suggestPrimerNames() {
        //TODO what about clashes? 
        this.setForwardPrimerBeanName();
        this.setReversePrimerBeanName();
    }

    /**
     * ConstructBean.setForwardPrimerBeanName
     */
    private void setForwardPrimerBeanName() {
        if (null == this.forwardPrimerBean
            || (null != this.forwardPrimerBean.getName() && !"".equals(this.forwardPrimerBean.getName()))) {
            return;
        }
        String start = "1";
        if (null != this.getTargetProtStart()) {
            start = this.getTargetProtStart().toString();
        }
        String targetName = this.getTargetName();
        if (null == targetName) {
            targetName = this.name;
        }
        this.forwardPrimerBean.setName(targetName + "F" + start);
    }

    /**
     * ConstructBean.setReversePrimerBeanName
     */
    private void setReversePrimerBeanName() {
        if (null == this.reversePrimerBean
            || (null != this.reversePrimerBean.getName() && !"".equals(this.reversePrimerBean.getName()))) {
            return;
        }
        String end = "full";
        if (null != this.getTargetProtEnd()) {
            end = this.getTargetProtEnd().toString();
        }
        String targetName = this.getTargetName();
        if (null == targetName) {
            targetName = this.name;
        }
        this.reversePrimerBean.setName(targetName + "R" + end);
    }

    /**
     * @param cb org.pimslims.presentation.construct.ConstructBean
     */
    public static void annotate(final ConstructBean cb) {
    
        for (final Iterator iterator = cb.getPrimers().iterator(); iterator.hasNext();) {
            final PrimerBean bean = (PrimerBean) iterator.next();
            PrimerBean.annotatePrimer(bean);
        }
        //Not for DNA Target constructs
        if (null == cb.getDnaTarget() || "".equals(cb.getDnaTarget())) {
            ConstructAnnotator.calcFinalProt(cb);
            ConstructAnnotator.calcExpressedProt(cb);
            if (null != cb.getFinalProt()) {
                ConstructAnnotator.annotateFinalProt(cb);
            }
        }
    
        cb.setPcrProductSeq(ConstructAnnotator.getPCRProductSequence(cb));
    }

}
