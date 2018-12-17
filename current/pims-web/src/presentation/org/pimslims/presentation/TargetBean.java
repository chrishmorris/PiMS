package org.pimslims.presentation;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pimslims.bioinf.newtarget.PIMSTarget;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.Target;

/**
 * Presentation layer Target Bean
 * 
 * @author Johan van Niekerk
 */
public class TargetBean extends ModelObjectBean implements Comparable<Object>, Serializable {

    /**
     * Constructor for TargetBean
     * 
     * @param modelObject
     */
    public TargetBean(final Target target) {
        super(target);
        TargetBeanReader.readTarget(this, target);
        this.isDnaTarget = PIMSTarget.isDNATarget(target);
        this.values.put("Constructs", this.getNumConstructs());
    }

    /**
     * 
     */
    public TargetBean() {
        super();
        this.isDnaTarget = false;
    }

    public static final String COMMENTS = Target.PROP_WHYCHOSEN;

    public static final String FUNCTION_DESCRIPTION = Target.PROP_FUNCTIONDESCRIPTION;

    /**
     * To satisfy Serializable Interface
     */
    private static final long serialVersionUID = -5304416616813381722L;

    // private Target pimsTarget;
    private String target_id;

    private String protein_name;

    //private String hyperlink;

    @Deprecated
    // there are lots of External DB Links, do't privelege GenBank
    private String gi_number;

    private String func_desc;

    private ModelObjectBean organism;

    private String scientificName;

    private String genbankLink;

    private String labNotebook;

    private Integer numConstructs;

    private String dnaSeq;

    private String protSeq;

    private String comments;

    //For DNA_Target need seqence name to replace proteinName
    private String dnaName;

    //For Natural Source_Target need seqence name to replace proteinName
    private String sourceName;

    @Deprecated
    private String personHook;

    // private String projectHook;

    private String targetGroupHook;

    // Each Bean in the Presentation layer contains a hook to the corresponding
    // PIMS object
    private String pimsTargetHook;

    // Getters and setters
    public String getComments() {
        return this.comments;
    }

    public void setComments(final String comments) {
        this.comments = comments;
    }

    public String getPimsTargetHook() {
        return this.pimsTargetHook;
    }

    public void setPimsTargetHook(final String pimsTargetHook) {
        this.pimsTargetHook = pimsTargetHook;
    }

    public String getDnaSeq() {
        return this.dnaSeq;
    }

    public final Pattern WHITE_SPACE = Pattern.compile("\\s", Pattern.MULTILINE);

    /**
     * the hook of the target in the DB, or null if none or unknown
     */
    private String hook;

    private String proteinHook;

    private String dnaSeqHook;

    private String name;

    private String whyChosen;

    //private String hyperlinkHook;

    private String aliases;

    private final boolean isDnaTarget;

    private String researchObjectiveHook;

    public void setDnaSeq(final String dnaSeq) {
        if (null == dnaSeq) {
            this.dnaSeq = "";
        }
        final Matcher m = this.WHITE_SPACE.matcher(dnaSeq);
        this.dnaSeq = m.replaceAll("");
    }

    @Override
    public int compareTo(final Object obj) {
        if (!(obj instanceof TargetBean)) {
            throw new ClassCastException("obj1 is not a SPOTTarget! ");
        }

        Integer argTargetId = null;
        Integer thisTargetId = null;
        try {
            argTargetId = Integer.parseInt(((TargetBean) obj).getTarget_id());
            thisTargetId = Integer.parseInt(this.getTarget_id());
        } catch (final NumberFormatException e) {
            return 0;
        }

        return thisTargetId.compareTo(argTargetId);
    }

    public static long getSerialVersionUID() {
        return TargetBean.serialVersionUID;
    }

    public String getFunc_desc() {
        return this.func_desc;
    }

    public void setFunc_desc(final String func_desc) {
        this.func_desc = func_desc;
    }

    public String getGenbankLink() {
        return this.genbankLink;
    }

    public void setGenbankLink(final String genbankLink) {
        this.genbankLink = genbankLink;
    }

    public String getGi_number() {
        return this.gi_number;
    }

    public void setGi_number(final String gi_number) {
        this.gi_number = gi_number;
    }

    /* @Deprecated
    public String getHyperlink() {
        return this.hyperlink;
    } 

    @Deprecated
    public void setHyperlink(final String hyperlink) {
        this.hyperlink = hyperlink;
    } */

    public String getOrganismName() {
        if (null == this.organism) {
            return null;
        }
        return this.organism.getName();
    }

    public String getOrganismHook() {
        if (null == this.organism) {
            return null;
        }
        return this.organism.getHook();
    }

    public ModelObjectBean getOrganismBean() {
        return this.organism;
    }

    public void setOrganism(final ModelObjectBean organism) {
        this.organism = organism;
        assert null == organism || Organism.class.getName().equals(organism.getClassName()) : "Bad organism for target: "
            + this.getName()
            + ", type: "
            + organism.getClassName()
            + ", not: "
            + Organism.class.getClass().getName();
    }

    public String getScientificName() {
        return this.scientificName;
    }

    public void setScientificName(final String name) {
        this.scientificName = name;
    }

    public String getProtein_name() {
        return this.protein_name;
    }

    public void setProtein_name(final String protein_name) {
        this.protein_name = protein_name;
    }

    /**
     * @return Returns the dnaName.
     */
    public String getDnaName() {
        return this.dnaName;
    }

    /**
     * @param dnaName The dnaName to set.
     */
    public void setDnaName(final String dnaName) {
        this.dnaName = dnaName;
    }

    /**
     * @return Returns the dnaName.
     */
    public String getSourceName() {
        return this.sourceName;
    }

    /**
     * @param dnaName The dnaName to set.
     */
    public void setSourceName(final String sourceName) {
        this.sourceName = sourceName;
    }

    /**
     * @return PiMS local name (immutable)
     */
    @Deprecated
    public String getTarget_id() {
        return this.target_id;
    }

    public void setTarget_id(final String target_id) {
        this.target_id = target_id;
    }

    @Deprecated
    // target.project is no longer used
    public String getProject() {
        return this.labNotebook;
    }

    public String getLabNotebook() {
        return this.labNotebook;
    }

    public void setLabNotebook(final String project) {
        this.labNotebook = project;
    }

    public Integer getNumConstructs() {
        return this.numConstructs;
    }

    public void setNumConstructs(final Integer numConstructs) {
        this.numConstructs = numConstructs;
    }

    public String getProtSeq() {
        return this.protSeq;
    }

    public void setProtSeq(String protSeq) {
        if (null == protSeq) {
            protSeq = "";
        }
        final Matcher m = this.WHITE_SPACE.matcher(protSeq);
        this.protSeq = m.replaceAll("");
    }

    @Deprecated
    public String getPersonHook() {
        return this.personHook;
    }

    @Deprecated
    public void setPersonHook(final String personHook) {
        this.personHook = personHook;
    }

/*
    public String getProjectHook() {
        return this.projectHook;
    }

    public void setProjectHook(final String projectHook) {
        this.projectHook = projectHook;
    } */

    @Override
    public void setHook(final String hook) {
        this.hook = hook;
    }

    /**
     * @return Returns the hook.
     */
    @Override
    public String getHook() {
        return this.hook;
    }

    public String getProtSeqHook() {
        return this.proteinHook + ":" + Molecule.PROP_SEQUENCE;
    }

    public String getProteinNameHook() {
        return this.proteinHook + ":" + Substance.PROP_NAME;
    }

    public String getDnaSeqHook() {
        return this.dnaSeqHook;
    }

    /**
     * @param dnaSeqHook The dnaSeqHook to set. Used by jsp for Update servlet.
     */
    public void setDnaSeqHook(final String dnaSeqHook) {
        this.dnaSeqHook = dnaSeqHook;
    }

    /**
     * @return PiMS common name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @param name The name to set.
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

    public String getWhyChosen() {
        return this.whyChosen;
    }

    public void setWhyChosen(final String whyChosen) {
        this.whyChosen = whyChosen;
    }

/*
    public void setHyperlinkHook(final String hook) {
        this.hyperlinkHook = hook;
    }

   
     * @return Returns the hook used to save the hyperlink
     * 
     * public String getHyperlinkHook() { return this.hyperlinkHook; }
     */

    public String getAliases() {
        return this.aliases;
    }

    public void setAliases(final String aliases) {
        this.aliases = aliases;
    }

    /**
     * @return Returns the targetGroupHook.
     */
    public String getTargetGroupHook() {
        return this.targetGroupHook;
    }

    /**
     * @param targetGroupHook The targetGroupHook to set.
     */
    public void setTargetGroupHook(final String targetGroupHook) {
        this.targetGroupHook = targetGroupHook;
    }

    /**
     * TargetBean.setProtein
     * 
     * @param protein
     */
    public void setProtein(final Molecule protein) {
        if (protein != null) {
            this.setProtSeq(protein.getSequence());
            this.proteinHook = protein.get_Hook();
            final String proteinName = protein.getName();
            /* was if (proteinName.endsWith("Protein")) {
                proteinName = proteinName.substring(0, proteinName.length() - "Protein".length());
            } */
            this.setProtein_name(proteinName);
        }

    }

    /**
     * TargetBean.isDNATarget
     * 
     * @return
     */
    public boolean isDNATarget() {
        return this.isDnaTarget;
    }

    /**
     * TargetBean.setCreator
     * 
     * @param modelObjectShortBean
     */
    @Override
    public void setCreator(final ModelObjectShortBean modelObjectShortBean) {
        super.setCreator(modelObjectShortBean);

    }

    /**
     * TargetBean.getResearchObjectiveHook
     * 
     * @return
     */
    public String getResearchObjectiveHook() {
        return this.researchObjectiveHook;
    }

    /**
     * @param researchObjectiveHook The researchObjectiveHook to set.
     */
    public void setResearchObjectiveHook(final String researchObjectiveHook) {
        this.researchObjectiveHook = researchObjectiveHook;
    }

}
