package org.pimslims.presentation.target;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ResearchObjectiveElementBeanI;
import org.pimslims.presentation.ComplexBean;
import org.pimslims.presentation.ComplexBeanWriter;
import org.pimslims.presentation.ModelObjectBean;

/**
 * Represents a sample in a list on a JSP page. TODO merge with BlueprintComponentBean
 * 
 * @see org.pimslims.model.sample.Sample
 * @author Marc Savitsky
 * 
 */
public class ResearchObjectiveElementBean extends ModelObjectBean implements Serializable,
    ResearchObjectiveElementBeanI {

    private final String proteinName;

    private final String shortWhyChosen;

    private final String targetName;

    private final String targetHook;

    public static final Comparator NAME_ORDER = new Comparator() {
        public int compare(final Object o1, final Object o2) {
            final ResearchObjectiveElementBeanI c1 = (ResearchObjectiveElementBeanI) o1;
            final ResearchObjectiveElementBeanI c2 = (ResearchObjectiveElementBeanI) o2;
            return c1.getTargetName().compareTo(c2.getTargetName());
        }
    };

    public ResearchObjectiveElementBean(final ResearchObjectiveElement researchObjectiveElement) {

        super(researchObjectiveElement);
        final String protein_name = "";
        final Target target = researchObjectiveElement.getTarget();
        if (null == target) {
            this.proteinName = "";
            this.targetName = "";
            this.targetHook = null;
        } else {
            this.targetName = target.get_Name();
            this.targetHook = target.get_Hook();
            final Molecule protein = target.getProtein();
            if (protein != null) {
                this.proteinName = protein.getName();
            } else {
                this.proteinName = "";
            }

        }
        this.shortWhyChosen = this.setShortWhyChosen(researchObjectiveElement.getWhyChosen());
    }

    public String getTargetHook() {
        return this.targetHook;
    }

    /**
     * 
     * ResearchObjectiveElementBean.getProteinName
     * 
     * @return
     */
    public String getProteinName() {
        return this.proteinName;
    }

    /**
     * 
     * ResearchObjectiveElementBean.getShortWhyChosen
     * 
     * @return
     */
    public String getShortWhyChosen() {
        return this.shortWhyChosen;
    }

    /**
     * 
     * ResearchObjectiveElementBean.setShortWhyChosen
     * 
     * @param whyChosen
     * @return
     */
    public String setShortWhyChosen(final String whyChosen) {
        final int max = 120;

        if (whyChosen.length() < max) {
            return whyChosen;
        }
        final StringBuffer sb = new StringBuffer(whyChosen.substring(0, max));
        final int i = sb.lastIndexOf(" ");
        return sb.substring(0, i) + "....";
    }

    public String getStart() {
        return (String) this.values.get(ResearchObjectiveElement.PROP_APPROXBEGINSEQID);
    }

    public String getEnd() {
        return (String) this.values.get(ResearchObjectiveElement.PROP_APPROXENDSEQID);
    }

    /**
     * BlueprintComponentBeanI.getTargetName
     * 
     * @see org.pimslims.presentation.BlueprintComponentBeanI#getTargetName()
     */
    @Override
    public String getTargetName() {
        return this.targetName;
    }

    /**
     * BlueprintComponentBeanI.getWhyChosen
     * 
     * @see org.pimslims.presentation.BlueprintComponentBeanI#getWhyChosen()
     */
    @Override
    public String getWhyChosen() {
        // TODO Auto-generated method stub
        return null;
    }

    public static ComplexBean readComplexHook(final ReadableVersion version, final String hook) {
    
        final ComplexBean bean = new ComplexBean();
        final ResearchObjective blueprint = version.get(hook);
        bean.setBlueprintHook(blueprint.get_Hook());
        bean.setName(blueprint.getCommonName());
        bean.setWhyChosen(blueprint.getWhyChosen());
        bean.setDetails(blueprint.getDetails());
    
        for (final ResearchObjectiveElement component : blueprint.getResearchObjectiveElements()) {
            final ResearchObjectiveElementBeanI componentBean = new ResearchObjectiveElementBean(component);
            //if (null != componentBean.getTarget()) {
            bean.addComponent(componentBean);
            //}
        }
    
        return bean;
    }

    public static ComplexBean createNewComplex(final WritableVersion version, final ComplexBean complex)
        throws AccessException, ConstraintException {
    
        final HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(ResearchObjective.PROP_LOCALNAME, complex.getName());
        attributes.put(ResearchObjective.PROP_COMMONNAME, complex.getName());
        attributes.put(ResearchObjective.PROP_WHYCHOSEN, complex.getWhyChosen());
        attributes.put(LabBookEntry.PROP_DETAILS, complex.getDetails());
    
        if (complex.getLabNotebookHook() != null) {
            final LabNotebook access = version.get(complex.getLabNotebookHook());
            attributes.put(LabBookEntry.PROP_ACCESS, access);
        }
        final ResearchObjective blueprint = version.create(ResearchObjective.class, attributes);
    
        //if (!complex.getComponents().isEmpty()) {
        for (final ResearchObjectiveElementBeanI componentBean : complex.getComponents()) {
            blueprint.addResearchObjectiveElement(createResearchObjectiveElement(
                version, blueprint, componentBean));
        }
        //}
    
        complex.setBlueprintHook(blueprint.get_Hook());
        return complex;
    }

    public static ResearchObjectiveElement createResearchObjectiveElement(final WritableVersion version,
        final ResearchObjective expBlueprint, final ResearchObjectiveElementBeanI componentBean)
        throws AccessException, ConstraintException {
        final Map<String, Object> m = new HashMap<String, Object>();
        m.put(ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, expBlueprint);
        m.put(ResearchObjectiveElement.PROP_COMPONENTTYPE, ComplexBeanWriter.COMPLEXTYPE);
        m.put(ResearchObjectiveElement.PROP_WHYCHOSEN, componentBean.getWhyChosen());
        m.put(ResearchObjectiveElement.PROP_APPROXBEGINSEQID, componentBean.getStart());
        m.put(ResearchObjectiveElement.PROP_APPROXENDSEQID, componentBean.getEnd());
        if (componentBean.getTargetHook() != null) {
            m.put(ResearchObjectiveElement.PROP_TARGET, version.get(componentBean.getTargetHook()));
        }
        if (expBlueprint.getAccess() != null) {
            m.put(LabBookEntry.PROP_ACCESS, expBlueprint.getAccess());
        }
        //m.put(ResearchObjectiveElement.PROP_MOLCOMPONENT, createMolComp(version, OpticPIMSMapping.template,
        //    construct.getDnaSeq(), "DNA"));
        return version.create(ResearchObjectiveElement.class, m);
    }
}
