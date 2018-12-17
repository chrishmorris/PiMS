package org.pimslims.command.spine2;

/*
 * produce an xml document suitable for input to SPINE2
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jdom.Attribute;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.pimslims.lab.ConstructUtility;
import org.pimslims.model.core.Citation;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;
import org.pimslims.presentation.ComplexBean;
import org.pimslims.presentation.ComplexBeanReader;
import org.pimslims.presentation.ResearchObjectiveElementBeanI;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;

public class Spine2Export extends Document {

    private static SimpleDateFormat formatDate = new SimpleDateFormat("yyyy'-'MM'-'dd");

    // use of Set avoids duplicates
    private final Set<ComplexBean> complexes = new HashSet();

    private final ArrayList<TargetBean> targets = new ArrayList();

    private final ArrayList<MyExperiment> experiments = new ArrayList();

    private final Collection<MySample> expressionVectors = new HashSet<MySample>();

    private final ArrayList<MySample> constructs = new ArrayList();

    private final ArrayList<MySample> scaledupConstructs = new ArrayList();

    private final ArrayList<ConstructBean> virtualTargets = new ArrayList();

    private final ArrayList<Citation> citations = new ArrayList();

    private final ArrayList<ExternalDbLink> dbrefs = new ArrayList();

    // Collection to translate PiMS/SPINE2 experiment types
    private static Map<String, String> experimentTypes = new HashMap<String, String>();
    {
        Spine2Export.experimentTypes.put("Transformation", "expression");
        Spine2Export.experimentTypes.put("Concentration", "scaleUp");
    }

    // Collection to translate PiMS/SPINE2 target groups
    private static Map<String, String> targetGroups = new HashMap<String, String>();
    {
        Spine2Export.targetGroups.put("Immune Targets", "Immune");
        Spine2Export.targetGroups.put("Cancer Targets", "Cancer");
        Spine2Export.targetGroups.put("Virus Targets", "Viral");
    }

    // Collection to translate PiMS/SPINE2 work packages
    private static Map<String, String> workPackages = new HashMap<String, String>();
    {
        Spine2Export.workPackages.put("OPPF", "WP1.2");
    }

    // Collection to translate PiMS/SPINE2 database names
    private static Map<String, String> databaseNames = new HashMap<String, String>();
    {
        Spine2Export.databaseNames.put("Swiss-Prot", "SwissProt");
        Spine2Export.databaseNames.put("GenBank", "GenBank");
        Spine2Export.databaseNames.put("NCBI Taxonomy", "NCBI");
        Spine2Export.databaseNames.put("PDB", "PDB");
        Spine2Export.databaseNames.put("RefSeq", "RefSeq");
        Spine2Export.databaseNames.put("UniProt", "UniProt");
    }

    // Collection to translate PiMS/SPINE2 organism names
    private static Map<String, String> organismNames = new HashMap<String, String>();
    {
        Spine2Export.organismNames.put("Homo sapiens", "Homo_sapiens");
        Spine2Export.organismNames.put("Mus musculus", "Mus_musculus");
        Spine2Export.organismNames.put("Rattus rattus", "Rattus_rattus");
        Spine2Export.organismNames.put("Rattus norvegicus", "Rattus_norvegicus");
        Spine2Export.organismNames.put("Gallus gallus", "Gallus_gallus");
    }

    /**
     * 
     */
    public Spine2Export() {
        super();
        this.setDocType(new DocType("SPINE2TrackerUpload",
            "http://www.spine2.eu/SPINE2TT/schema/targetTracker/SPINE2Uploadv1.0.dtd"));
        this.setRootElement(new Element("SPINE2TrackerUpload"));
    }

    /*
     * <!-- Complexes -->
     * <!ELEMENT complexes (complexExisting*, complex*)>
     * <!ELEMENT complexExisting EMPTY>
     * <!ATTLIST complexExisting
     *  name CDATA #REQUIRED>
     *   <!-- The complexExisting node refers to a complex already in the database and is required for
     *   specifying a complex that references an existing complex. -->
     */
    public void complexesElement() {
        if (this.complexes.isEmpty()) {
            return;
        }
        final Element element = new Element("complexes");
        // element.addContent(complexExistingElement());

        for (final ComplexBean bean : this.complexes) {
            element.addContent(this.complexElement(bean));
        }

        this.getRootElement().addContent(element);
    }

    /*
     * <!ELEMENT complex (description, derivation, explanatoryNotes, inhibitors, ligands, components)>
     * <!ATTLIST complex
     *   name CDATA #REQUIRED
     *   incidental CDATA #REQUIRED>
     * <!ELEMENT derivation (#PCDATA)>
     * <!ELEMENT explanatoryNotes (#PCDATA)>
     * <!ELEMENT components (component+)>
     * <!ELEMENT component EMPTY>
     * <!ATTLIST component
     *   id CDATA #REQUIRED
     *   type (target|complex) #REQUIRED>
     *  <!-- This component reference may refer to either a new (defined in this XML file)
     *  or existing component. To refer to a target or complex that is already in the
     *  SPINE2 database the user must specify the id of that component in either the
     *  complexExisting or targetExisting node. -->
     * <!ELEMENT inhibitors (inhibitor+)>
     * <!ELEMENT inhibitor (#PCDATA)>
     * <!ELEMENT ligands (ligand+)>
     * <!ELEMENT ligand (#PCDATA)>
     */
    private Element complexElement(final ComplexBean bean) {
        final Element complex = new Element("complex");
        complex.setAttribute(new Attribute("name", bean.getName()));
        complex.setAttribute(new Attribute("incidental", ""));

        complex.addContent(this.descriptionElement(bean.getWhyChosen()));
        complex.addContent(this.derivationElement(""));
        complex.addContent(this.explanatoryNotesElement(bean.getDetails()));

        complex.addContent(this.inhibitorsElement(Collections.singleton("")));
        complex.addContent(this.ligandsElement(Collections.singleton("")));
        complex.addContent(this.componentsElement(bean.getComponents()));

        return complex;
    }

    /*
     * <!-- Derivation of a Complex --> <!ELEMENT derivation (#PCDATA)>
     */
    private Element derivationElement(final String data) {
        final Element element = new Element("derivation");
        element.setText(this.noNulls(data));
        return element;
    }

    /*
     * <!-- Explanatory Notes of a Complex --> <!ELEMENT explanatoryNotes (#PCDATA)>
     */
    private Element explanatoryNotesElement(final String data) {
        final Element element = new Element("explanatoryNotes");
        element.setText(this.noNulls(data));
        return element;
    }

    /*
     * <!ELEMENT components (component+)>
     * <!ELEMENT component EMPTY>
     * <!ATTLIST component
     *   id CDATA #REQUIRED
     *   type (target|complex) #REQUIRED>
     *  <!-- This component reference may refer to either a new (defined in this XML file)
     *  or existing component. To refer to a target or complex that is already in the
     *  SPINE2 database the user must specify the id of that component in either the
     *  complexExisting or targetExisting node. -->
     */
    private Element componentsElement(final Collection<ResearchObjectiveElementBeanI> components) {
        final Element element = new Element("components");

        for (final ResearchObjectiveElementBeanI component : components) {
            for (final TargetBean target : this.targets) {
                if (target.getName().equals(component.getProteinName())) {
                    element.addContent(this.componentElement(component));
                }
            }
        }
        return element;
    }

    private Element componentElement(final ResearchObjectiveElementBeanI bean) {
        final Element element = new Element("component");
        element.setAttribute(new Attribute("id", bean.getProteinName()));
        element.setAttribute(new Attribute("type", "target"));
        return element;
    }

    /**
     * <!ELEMENT inhibitors (inhibitor+)> <!ELEMENT inhibitor (#PCDATA)>
     */
    private Element inhibitorsElement(final Collection inhibitors) {
        final Element element = new Element("inhibitors");

        for (final Object inhibitor : inhibitors) {
            element.addContent(this.inhibitorElement(inhibitor));
        }
        return element;
    }

    private Element inhibitorElement(final Object object) {
        final Element element = new Element("inhibitor");
        element.addContent(object.toString());
        return element;
    }

    /**
     * <!ELEMENT ligands (ligand+)> <!ELEMENT ligand (#PCDATA)>
     */
    private Element ligandsElement(final Collection ligands) {
        final Element element = new Element("ligands");

        for (final Object ligand : ligands) {
            element.addContent(this.ligandElement(ligand));
        }
        return element;
    }

    private Element ligandElement(final Object object) {
        final Element element = new Element("ligand");
        element.addContent(object.toString());
        return element;
    }

    /**
     * <!-- Target definitions --> <!ELEMENT targets (targetExisting*, target*)>
     */
    public void targetsElement() {
        if (this.targets.isEmpty()) {
            return;
        }
        final Element element = new Element("targets");
        // element.addContent(targetExistingElement());

        for (final TargetBean bean : this.targets) {
            element.addContent(this.targetElement(bean));
        }

        this.getRootElement().addContent(element);
    }

    /*
     * <!ELEMENT targetExisting EMPTY> <!ATTLIST targetExisting targetID CDATA #REQUIRED> <!-- The
     * targetExisting node refers to a target already in the database and is required for specifying a complex
     * that references an existing target. -->

    private Element targetExistingElement() {
        Element target = new Element("targetExisting");
        target.setAttribute(new Attribute("targetID", ""));
        return target;
    } */

    /*
     * <!ELEMENT target (sequence, targetGroups, description)>
     * <!ATTLIST target targetID CDATA REQUIRED
     * proteinName CDATA #REQUIRED
     * hostSpecies (Homo_sapiens|Mus_musculus|Rattus_rattus|Rattus_norvegicus|Gallus_gallus) #REQUIRED
     * workpackageSection (WP1.1|WP1.2|WP1.3|WP1.4|WP1.5|WP1.6|WP1.7|WP1.8|WP1.9) #REQUIRED>
     */

    private Element targetElement(final TargetBean bean) {
        final Element target = new Element("target");

        target.setAttribute(new Attribute("targetID", bean.getName()));
        target.setAttribute(new Attribute("proteinName", this.noNulls(bean.getProtein_name())));
        target.setAttribute(new Attribute("hostSpecies", this.noNulls(Spine2Export.organismNames.get(bean
            .getScientificName()))));

        String wpackage = null;
        if (bean.getLabNotebook() != null) {
            final String project = bean.getLabNotebook();
            if (Spine2Export.workPackages.containsKey(project)) {
                wpackage = Spine2Export.workPackages.get(project);
            } else {
                wpackage = project;
            }

        }

        target.setAttribute(new Attribute("workpackageSection", this.noNulls(wpackage)));

        target.addContent(this.sequenceElement(bean.getProtSeq()));
        target.addContent(this.targetGroupsElement(Collections.EMPTY_LIST));
        target.addContent(this.descriptionElement(bean.getWhyChosen()));
        return target;
    }

    /*
     * <!ELEMENT targetGroups (targetGroup+)>
     */
    private Element targetGroupsElement(final Collection<TargetGroup> groups) {
        final Element element = new Element("targetGroups");

        for (final TargetGroup group : groups) {
            if (Spine2Export.targetGroups.containsKey(group.get_Name())) {
                element.addContent(this.targetGroupElement(group));
            }
        }
        return element;
    }

    /*
     * <!ELEMENT targetGroup EMPTY>
     * <!ATTLIST targetGroup id
     * (Constituitive_pathway|Ubiquitin|Cell_cycle|Synaptic|Regulatory|Cancer|Immune|Viral) #REQUIRED>
     */
    private Element targetGroupElement(final TargetGroup group) {
        final Element element = new Element("targetGroup");
        element.setAttribute(new Attribute("id", Spine2Export.targetGroups.get(group.get_Name())));
        // element.setAttribute(new Attribute("id", group.get_Name()));
        return element;
    }

    /*
     *  <!-- Virtual Targets -->
     * <!ELEMENT virtualTargets (virtualTargetExisting*,virtualTarget*)>
     * <!ELEMENT virtualTargetExisting EMPTY>
     * <!ATTLIST virtualTargetExisting publicName CDATA #REQUIRED>
     * <!ELEMENT virtualTarget (associatedTarget,sequence,tags)>
     * <!ATTLIST virtualTarget publicName CDATA #REQUIRED
     * privateName CDATA #REQUIRED>
     * <!ELEMENT associatedTarget EMPTY>
     * <!--targetID should be the ID of a Target (either an ExistingTarget or a Target)-->
     * <!ATTLIST associatedTarget targetID CDATA #REQUIRED>
     */
    public void virtualTargetsElement() {
        if (this.virtualTargets.isEmpty()) {
            return;
        }
        final Element element = new Element("virtualTargets");

        // element.addContent(virtualTargetExistingElement());

        for (final ConstructBean bean : this.virtualTargets) {
            element.addContent(this.virtualTargetElement(bean));
        }
        this.getRootElement().addContent(element);
    }

    /*
     * <!ELEMENT virtualTarget (associatedTarget,sequence,tags)>
     * <!ATTLIST virtualTarget publicName CDATA #REQUIRED
     * privateName CDATA #REQUIRED>
     */
    private Element virtualTargetElement(final ConstructBean bean) {
        final Element element = new Element("virtualTarget");
        element.setAttribute(new Attribute("publicName", bean.getName()));
        element.setAttribute(new Attribute("privateName", bean.getLocalName()));

        element.addContent(this.associatedTargetElement(bean.getTargetName()));
        element.addContent(this.sequenceElement(bean.getProtSeq()));
        element.addContent(this.tagsElement(Collections.singleton("6xHis")));

        return element;
    }

    /*
     * <!ELEMENT associatedTarget EMPTY>
     * <!--targetID should be the ID of a Target (either an ExistingTarget
     * or a Target)-->
     * <!ATTLIST associatedTarget targetID CDATA #REQUIRED>
     */
    private Element associatedTargetElement(final String name) {
        final Element element = new Element("associatedTarget");
        element.setAttribute(new Attribute("targetID", name));
        return element;
    }

    /*
     *  <!-- Expression Vectors -->
     * <!ELEMENT expressionVectors (expressionVectorExisting*,
     * expressionVector*)>
     */
    public void expressionVectorsElement() {

        if (this.expressionVectors.isEmpty()) {
            return;
        }
        final Element element = new Element("expressionVectors");

        // element.addContent(expressionVectorExistingElement());

        for (final MySample bean : this.expressionVectors) {
            element.addContent(this.expressionVectorElement(bean));
        }
        this.getRootElement().addContent(element);
    }

    /*
     * <!ELEMENT expressionVectorExisting EMPTY> <!ATTLIST expressionVectorExisting externalKey CDATA
     * #REQUIRED>
     
    private Element expressionVectorExistingElement() {
        final Element element = new Element("expressionVectorExisting");
        element.setAttribute(new Attribute("externalKey", ""));
        return element;
    } */

    /*
     * <!ELEMENT expressionVector (constituents) >
     * <!ATTLIST expressionVector externalKey CDATA #REQUIRED
     * plasmidType
     * (pET-based|pTH-based|pOP-based|pDEST-based|pProEx-based|pGEX-based|pbs-based_-_Yeast_Overexpression|pLEX|pHLsec|Baculovirus_-_polyhedrin_promoter|Yeast_Genome|Insect_Cell_Genome|Mammalian_Cell_Genome)
     * #REQUIRED>
     */
    private Element expressionVectorElement(final MySample bean) {
        final Element element = new Element("expressionVector");
        element.setAttribute(new Attribute("externalKey", Long.toString(bean.getId())));
        element.setAttribute(new Attribute("plasmidType", "pET-based"));

        element.addContent(this.constituentsElement(bean.getConstituents(), bean.getComplexName()));
        return element;
    }

    /*
     * <!ELEMENT constructs (constructExisting*, construct*)>
     */
    public void constructsElement() {
        if (this.constructs.isEmpty()) {
            return;
        }
        final Element element = new Element("constructs");

        // element.addContent(constructExistingElement());

        for (final MySample bean : this.constructs) {
            element.addContent(this.constructElement(bean));
        }
        this.getRootElement().addContent(element);
    }

    /*
     * <!ELEMENT constructExisting EMPTY> <!ATTLIST constructExisting externalKey CDATA #REQUIRED>
     
    private Element constructExistingElement() {
        final Element element = new Element("constructExisting");
        element.setAttribute(new Attribute("externalKey", ""));
        return element;
    } */

    /*
     * <!ELEMENT construct (sourceExperiment, constituents, notesRemarks, detection, expression, folding,
     * optimisation, qa, expectedSequence)>
     * <!ATTLIST construct externalKey CDATA #REQUIRED
     * creationDate CDATA #REQUIRED
     * offTarget (Yes | No) "No" coDerived (Yes | No) "No">
     */
    private Element constructElement(final MySample bean) {
        final Element element = new Element("construct");
        element.setAttribute(new Attribute("externalKey", Long.toString(bean.getId())));
        element.setAttribute(new Attribute("creationDate", Spine2Export.formatDate.format(bean
            .getCreationDate())));
        element.setAttribute(new Attribute("offTarget", "No"));
        element.setAttribute(new Attribute("coDerived", "No"));

        element.addContent(this.sourceExperimentElement(Long.toString(bean.getSourceExperiment())));
        element.addContent(this.constituentsElement(bean.getConstituents(), bean.getComplexName()));
        element.addContent(this.notesRemarksElement(bean.getOutputSample().getExperiment().getDetails()));
        element.addContent(this.detectionElement());
        element.addContent(this.expressionElement());
        element.addContent(this.foldingElement());
        element.addContent(this.optimisationElement());
        element.addContent(this.qaElement());
        element.addContent(this.expectedSequenceElement());
        return element;
    }

    /*
     * <!-- Elements required for Construct -->
     * <!ELEMENT detection (#PCDATA)> <!ATTLIST detection method (Method) #REQUIRED>
     */
    private Element detectionElement() {
        final Element element = new Element("detection");
        element.setAttribute(new Attribute("method", "Method"));
        return element;
    }

    /*
     * <!ELEMENT expression (#PCDATA)> <!ATTLIST expression method
     * (bacterial|yeast|mammalian|insect|Cell_free|annelids) #REQUIRED details
     * (Origami_DE3BL21_DE3BL21_DE3_pLysS|BL21_DE3_pLysE|BL21_DE3_-Gold|BL21-Gold_DE3_pLysS|BL21_DE3_RIL|Rosetta2_DE3Rosetta2_DE3_pLysS|Rosetta-gami_DE3Rosetta_DE3_pLysS|Rosetta_DE3C41_DE3_-pLysSRARE|B834_DE3B834_DE3_pLysS|HEK293T|HEK293S|HEK293EBNA|HEK293GnT1|ArcticExpress_DE3ArcticExpress_DE3_-RIL|ArcticExpress_DE3_-RP|anabaena|sf9|Sf21|Hi_Five|HEK293T|293T_S|CHO|P._pastoris|S._cervisiae|C._elegans|A._pombe)
     * #REQUIRED>
     */
    private Element expressionElement() {
        final Element element = new Element("expression");
        element.setAttribute(new Attribute("method", "other"));
        element.setAttribute(new Attribute("details", "other"));
        return element;
    }

    /*
     * <!ELEMENT folding (#PCDATA)> <!ATTLIST folding method
     * (Dilution_or_dialysis|Co-folding_or_Chaperone|Chromatography|Other_or_kit) #REQUIRED>
     */
    private Element foldingElement() {
        final Element element = new Element("folding");
        element.setAttribute(new Attribute("method", "notDone"));
        return element;
    }

    /*
     * <!ELEMENT optimisation (#PCDATA)> <!ATTLIST optimisation method
     * (codon_optimisation|expression_temperature|additives) #REQUIRED>
     */
    private Element optimisationElement() {
        final Element element = new Element("optimisation");
        element.setAttribute(new Attribute("method", "expression_temperature"));
        return element;
    }

    /*
     * <!ELEMENT qa (#PCDATA)> <!ATTLIST qa method
     * (Mass_spectroscopy|Gel_filtration|SAXS|Thermofluor|SDS-PAGE|Western_blot|Dot_blot) #REQUIRED>
     */
    private Element qaElement() {
        final Element element = new Element("qa");
        element.setAttribute(new Attribute("method", "SDS-PAGE"));
        return element;
    }

    /*
     * <!ELEMENT expectedSequence (#PCDATA)>
     */
    private Element expectedSequenceElement() {
        final Element element = new Element("expectedSequence");
        return element;
    }

    /*
     *  <!-- Scaled-up constructs -->
     * <!ELEMENT scaledUpConstructs (scaledUpConstructExisting*, scaledUpConstruct*)>
     * <!ELEMENT scaledUpConstructExisting EMPTY>
     * <!ATTLIST scaledUpConstructExisting externalKey CDATA #REQUIRED>
     */
    public void scaledUpConstructsElement() {

        if (this.scaledupConstructs.isEmpty()) {
            return;
        }

        final Element element = new Element("scaledUpConstructs");
        for (final MySample bean : this.scaledupConstructs) {
            element.addContent(this.scaledUpConstructElement(bean));
        }
        this.getRootElement().addContent(element);
    }

    /*
     * * <!ELEMENT scaledUpConstruct (sourceExperiment, constituents, notesRemarks) >
     * <!ATTLIST scaledUpConstruct externalKey CDATA #REQUIRED
     * creationDate CDATA #REQUIRED
     * offTarget (Yes | No) "No"
     * coDerived (Yes | No) "No">
     */
    private Element scaledUpConstructElement(final MySample bean) {
        final Element element = new Element("scaledUpConstruct");
        element.setAttribute("externalKey", Long.toString(bean.getId()));
        element.setAttribute("creationDate", Spine2Export.formatDate.format(bean.getCreationDate()));
        element.setAttribute("offTarget", "No");
        element.setAttribute("coDerived", "No");

        element.addContent(this.sourceExperimentElement(Long.toString(bean.getSourceExperiment())));
        element.addContent(this.constituentsElement(bean.getConstituents(), bean.getComplexName()));
        element.addContent(this.notesRemarksElement(""));
        return element;
    }

    /*
     *  <!-- Crystals -->
     * <!ELEMENT crystals (crystalExisting*, crystal*)>
     * <!ELEMENT crystalExisting EMPTY>
     * <!ATTLIST crystalExisting externalKey CDATA #REQUIRED>
     * <!ELEMENT crystal (sourceExperiment, constituents, notesRemarks) >
     * <!ATTLIST crystal externalKey CDATA #REQUIRED
     * creationDate CDATA #REQUIRED
     * offTarget (Yes | No) "No" coDerived (Yes | No) "No">
     */
    public void crystalsElement() {
        final Element element = new Element("crystals");
        this.getRootElement().addContent(element);
    }

    /*
     *  <!-- Crystal Structure -->
     * <!ELEMENT crystalStructures (crystalStructureExisting*, crystalStructure*)>
     * <!ELEMENT crystalStructureExisting EMPTY>
     * <!ATTLIST crystalStructureExisting externalKey CDATA #REQUIRED>
     * <!ELEMENT crystalStructure (sourceExperiment, constituents, notesRemarks, description) >
     * <!ATTLIST crystalStructure externalKey CDATA #REQUIRED creationDate CDATA #REQUIRED
     * offTarget (Yes | No) "No" coDerived (Yes | No) "No" novel (Yes | No) "No" resolution CDATA #REQUIRED>
     */
    public void crystalStructuresElement() {
        final Element element = new Element("crystalStructures");
        this.getRootElement().addContent(element);
    }

    /*
     *  <!-- HSQC Spectra -->
     * <!ELEMENT hsqcSpectra (hsqcSpectrumExisting*, hsqcSpectrum*)>
     * <!ELEMENT hsqcSpectrumExisting EMPTY>
     * <!ATTLIST hsqcSpectrumExisting externalKey CDATA #REQUIRED>
     * <!ELEMENT hsqcSpectrum (sourceExperiment, constituents, notesRemarks) >
     * <!ATTLIST hsqcSpectrum externalKey CDATA #REQUIRED
     * creationDate CDATA #REQUIRED
     * offTarget (Yes | No) "No" coDerived (Yes | No) "No" spectrumType
     * (COSY|TOCSY|NOESY|TROSY) #REQUIRED
     * labellingIsotope (13C|15N) #REQUIRED>
     */
    public void hsqcSpectraElement() {
        final Element element = new Element("hsqcSpectra");
        this.getRootElement().addContent(element);
    }

    /*
     *  <!-- NMR Data -->
     * <!ELEMENT nmrDataEntities (nmrDataExisting*, nmrData*)>
     * <!ELEMENT nmrDataExisting EMPTY>
     * <!ATTLIST nmrDataExisting externalKey CDATA #REQUIRED>
     * <!ELEMENT nmrData (sourceExperiment, constituents, notesRemarks) >
     * <!ATTLIST nmrData externalKey CDATA #REQUIRED
     * creationDate CDATA #REQUIRED
     * offTarget (Yes | No) "No" coDerived (Yes | No) "No">
     */
    public void nmrDataEntitiesElement() {
        final Element element = new Element("nmrDataEntities");
        this.getRootElement().addContent(element);
    }

    /*
     *  <!-- NMR Structure -->
     * <!ELEMENT nmrStructures (nmrStructureExisting*, nmrStructure*)>
     * <!ELEMENT nmrStructureExisting EMPTY>
     * <!ATTLIST nmrStructureExisting externalKey CDATA #REQUIRED>
     * <!ELEMENT nmrStructure (sourceExperiment, constituents, notesRemarks, description) >
     * <!ATTLIST nmrStructure
     * externalKey CDATA #REQUIRED creationDate CDATA #REQUIRED
     * offTarget (Yes | No) "No" coDerived (Yes | No)
     * "No" novel (Yes | No) "No" resolution CDATA #REQUIRED>
     */
    public void nmrStructuresElement() {
        final Element element = new Element("nmrStructures");
        this.getRootElement().addContent(element);
    }

    /*
     *  <!-- Missing entity -->
     * <!ELEMENT missingEntities (missingEntity*)>
     * <!ELEMENT missingEntity (referencedTarget, constituents) >
     * <!ATTLIST missingEntity externalKey CDATA #REQUIRED type
     * (construct|scaledUpConstruct|crystal|crystalStructure|hsqcSpectrum|nmrData|nmrStructure) #REQUIRED
     * supplyingPartner CDATA #REQUIRED name CDATA #REQUIRED>
     * <!ELEMENT referencedTarget EMPTY>
     * <!ATTLIST referencedTarget targetID CDATA #REQUIRED>
     */
    public void missingEntitiesElement() {
        final Element element = new Element("missingEntities");
        this.getRootElement().addContent(element);
    }

    /*
     * <!-- Publications and database references --> <!ELEMENT publications (publication*)>
     */
    public void publicationsElement(final Collection<Citation> beans) {
        if (beans.isEmpty()) {
            return;
        }
        final Element element = new Element("publications");
        for (final Citation bean : beans) {
            element.addContent(this.publicationElement(bean));
        }
        this.getRootElement().addContent(element);
    }

    /*
     * <!ELEMENT publication (structureReference,authors,editors)>
     * <!ATTLIST publication title CDATA #REQUIRED
     * uri CDATA #REQUIRED
     * publicationDate CDATA #REQUIRED>
     */
    private Element publicationElement(final Citation bean) {
        final Element element = new Element("publication");
        element.setAttribute(new Attribute("title", this.noNulls(bean.getTitle())));
        element.setAttribute(new Attribute("uri", this.noNulls("")));
        element.setAttribute(new Attribute("publicationDate", ""));
        element.addContent(this.structureReferenceElement());
        // TODO CHECK MODEL CHANGE: authors and editors are stored as String in Citation now
        //element.addContent(this.personsElement(bean.getAuthors(), "authors", "author"));
        //element.addContent(this.personsElement(bean.getEditors(), "editors", "editor"));
        return element;
    }

    /*
     * <!-- The structure this database entry refers to -->
     * <!ELEMENT structureReference EMPTY> <!--ID
     * reference to the structure (nmrStructure or crystalStructure that is linked to-->
     * <!ATTLIST structureReference externalKey CDATA #REQUIRED>
     */
    private Element structureReferenceElement() {
        final Element element = new Element("structureReference");
        element.setAttribute(new Attribute("externalKey", this.noNulls("")));
        return element;
    }

    /*
     * <!ELEMENT authors (author)>
     * <!ELEMENT author (#PCDATA)>
     * <!ELEMENT editors (editor)>
     * <!ELEMENT editor (#PCDATA)>
     
    private Element personsElement(final Collection<Person> persons, final String plural,
        final String singular) {
        final Element element = new Element(plural);
        for (final Person p : persons) {
            element.addContent(this.personElement(p, singular));
        }
        return element;
    } */
/*
    private Element personElement(final Person person, final String singular) {
        final Element element = new Element(singular);
        element.setText(person.getGivenName());
        return element;
    } */

    /*
     * <!ELEMENT databaseReferences (databaseReference)>
     */
    public void databaseReferencesElement(final Collection<ExternalDbLink> beans) {
        if (beans.isEmpty()) {
            return;
        }
        final Element element = new Element("databaseReferences");
        for (final ExternalDbLink bean : beans) {
            if (Spine2Export.databaseNames.containsKey(bean.getDatabaseName().getName())) {
                element.addContent(this.databaseReferenceElement(bean));
            }
        }
        this.getRootElement().addContent(element);
    }

    /*
     * <!ELEMENT databaseReference (structureReference)>
     * <!ATTLIST databaseReference uri CDATA #REQUIRED
     * accessionDate CDATA #REQUIRED
     * database (SwissProt|GenBank|NCBI|PDB|PIR-PSD|RefSeq|TrEMBL|UniProt) #REQUIRED
     * key CDATA #REQUIRED>
     */
    private Element databaseReferenceElement(final ExternalDbLink bean) {
        final Element element = new Element("databaseReference");
        element.setAttribute(new Attribute("uri", this.noNulls(bean.getUrl())));
        element.setAttribute(new Attribute("accessionDate", Spine2Export.formatDate.format(new Date())));
        element.setAttribute(new Attribute("database", this.noNulls(Spine2Export.databaseNames.get(bean
            .getDatabaseName().getName()))));
        element.setAttribute(new Attribute("key", Long.toString(bean.getDbId())));
        element.addContent(this.structureReferenceElement());
        return element;
    }

    /*
     * <!-- Experiments -->
     * <!ELEMENT experiments (experimentExisting*, experiment*)>
     */
    public void experimentsElement() {
        if (this.experiments.isEmpty()) {
            return;
        }
        final Element element = new Element("experiments");
        for (final MyExperiment bean : this.experiments) {
            element.addContent(this.experimentElement(bean));
        }
        this.getRootElement().addContent(element);
    }

    /*
     * <!ELEMENT experimentExisting EMPTY>
     * <!ATTLIST experimentExisting key CDATA #REQUIRED>
    
    private Element experimentExistingElement() {
        final Element element = new Element("experimentExisting");
        element.setAttribute(new Attribute("key", ""));
        return element;
    } */

    /*
     * <!ELEMENT experiment (inputs)>
     * <!ATTLIST experiment key CDATA #REQUIRED
     * type
     * (expression|scaleUp|crystallisation|HQSC|NMR|nmrStructureDetermination|crystalStructureDetermination)
     * #REQUIRED
     * numberOfPositives CDATA #REQUIRED
     * numberOfTrials CDATA #REQUIRED
     * startDate CDATA #REQUIRED >
     */
    private Element experimentElement(final MyExperiment bean) {
        final Element element = new Element("experiment");
        element.setAttribute(new Attribute("key", Long.toString(bean.getExperiment().getDbId())));
        element.setAttribute(new Attribute("type", this.noNulls(Spine2Export.experimentTypes.get(bean
            .getExperiment().getExperimentType().getName()))));
        element.setAttribute(new Attribute("numberOfPositives", "1"));
        element.setAttribute(new Attribute("numberOfTrials", "1"));
        element.setAttribute(new Attribute("startDate", Spine2Export.formatDate.format(bean.getExperiment()
            .getStartDate().getTime())));
        element.addContent(this.inputsElement(bean.getMyInputSamples()));
        return element;
    }

    /*
     * <!ELEMENT inputs (entityReference*)>
     */
    private Element inputsElement(final Collection<Sample> inputs) {
        final Element element = new Element("inputs");
        for (final Sample sample : inputs) {
            element.addContent(this.entityReferenceElement(sample.getDbId()));
        }
        return element;
    }

    /*
     * <!ELEMENT entityReference EMPTY>
     * <!-- externalKey is the externalKey of the entity that forms this
     * input. -->
     * <!ATTLIST entityReference externalKey CDATA #REQUIRED>
     */
    private Element entityReferenceElement(final Long input) {
        final Element element = new Element("entityReference");
        element.setAttribute(new Attribute("externalKey", Long.toString(input)));
        return element;
    }

    /*
     * <!-- Common Entity elements -->
     * <!-- sourceExperiment key is the key for the source experiment, as defined in this entry. -->
     * <!ELEMENT sourceExperiment EMPTY>
     * <!ATTLIST sourceExperiment key CDATA #REQUIRED>
     */
    private Element sourceExperimentElement(final String key) {
        final Element element = new Element("sourceExperiment");
        element.setAttribute(new Attribute("key", key));
        return element;
    }

    /*
     * <!-- General remarks about this Entity -->
     * <!ELEMENT notesRemarks (#PCDATA)>
     */
    private Element notesRemarksElement(final String data) {
        final Element element = new Element("notesRemarks");
        element.setText(this.noNulls(data));
        return element;
    }

    /*
     * <!-- Description of a Structure -->
     * <!ELEMENT description (#PCDATA)>
     */
    private Element descriptionElement(final String data) {
        final Element element = new Element("description");
        element.setText(this.noNulls(data));
        return element;
    }

    /*
     * <!-- The constituents of this element, if known -->
     * <!-- complexName is the name of the complex this is associated with -->
     * <!ELEMENT constituents (virtualTargetReference*) >
     * <!ATTLIST constituents complexName CDATA #REQUIRED>
     */
    private Element constituentsElement(final Collection objects, final String complexName) {
        final Element element = new Element("constituents");

        element.setAttribute(new Attribute("complexName", complexName));

        for (final Object object : objects) {
            element.addContent(this.virtualTargetReferenceElement(object));
        }
        return element;
    }

    /*
     * <!-- Reference to a VirtualTarget -->
     * <!ELEMENT virtualTargetReference EMPTY>
     * <!ATTLIST virtualTargetReference publicName CDATA #REQUIRED>
     */
    private Element virtualTargetReferenceElement(final Object o) {
        final Element element = new Element("virtualTargetReference");
        element.setAttribute(new Attribute("publicName", o.toString()));
        return element;
    }

    /*
     * <!ELEMENT tags (tag+)>
     */
    private Element tagsElement(final Collection objects) {
        final Element element = new Element("tags");
        for (final Object object : objects) {
            element.addContent(this.tagElement(object));
        }
        return element;
    }

    /*
     * <!ELEMENT tag EMPTY> <!ATTLIST tag id CDATA #REQUIRED>
     * <!ATTLIST tag
    id (6xHis|7xHis|8xHis|10xHis|GST|MBP|CBP|Myc|Flag|Protein_A|Biotin|T7|Nus|HA|Strep|Myoisin) #REQUIRED>
     */
    private Element tagElement(final Object o) {
        final Element element = new Element("tag");
        element.setAttribute(new Attribute("id", o.toString()));
        return element;
    }

    /*
     * <!ELEMENT sequence (#PCDATA)>
     * <!ATTLIST sequence type (aa|na|other) #REQUIRED>
     * <!-- Protein sequences
     * (assume IUPAC 1-letter codes). This should be the actual sequence relevant for context, e.g. domain if
     * appropriate, not necessarily the whole gene; for a virtual target this will in general be a shorter
     * sequence than for the whole target. -->
     */
    private Element sequenceElement(final String sequence) {
        final Element element = new Element("sequence");
        element.setAttribute(new Attribute("type", "other"));
        element.setText(this.noNulls(sequence));
        return element;
    }

    private String noNulls(final String s) {
        if (null == s) {
            return "";
        }
        return s;
    }

    private static Collection<ExternalDbLink> getTargetDbRefs(final Target target) {
        return target.getExternalDbLinks();
    }

    private static Collection<Citation> getTargetCitations(final Target target) {

        final ArrayList<Citation> beans = new ArrayList();
        beans.addAll(target.getCitations());
        return beans;
    }

    private static Collection<ConstructBean> getTargetVirtualTargets(final Target target) {

        final ArrayList<ConstructBean> beans = new ArrayList();
        for (final ResearchObjectiveElement bluePrintComponent : target.getResearchObjectiveElements()) {
            final ResearchObjective bluePrint = bluePrintComponent.getResearchObjective();
            if (Spine2Export.isSpotConstruct(bluePrint)) {
                beans.add(ConstructBeanReader.readConstruct(bluePrint));
            }
        }
        return beans;
    }

    /**
     * 
     * @param bluePrint
     * @return
     */
    public static boolean isSpotConstruct(final ResearchObjective bluePrint) {
        return ConstructUtility.isSpotConstruct(bluePrint);
    }

    /**
     * 
     * @param collection
     * @param mySample
     * @return
     */
    private boolean containsSample(final Collection<MySample> collection, final MySample mySample) {
        for (final MySample sample : collection) {
            if (sample.getId() == mySample.getId()) {
                return true;
            }
        }
        return false;
    }

    private boolean containsExperiment(final Collection<MyExperiment> collection,
        final MyExperiment myExperiment) {
        for (final MyExperiment experiment : collection) {
            if (experiment.getExperiment().getDbId() == myExperiment.getExperiment().getDbId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param target
     */
    public void process(final Target target) {

        //System.out.println("process [" + target.getName() + "]");
        //this.targets.add(target);
        this.targets.add(new TargetBean(target));
        this.virtualTargets.addAll(Spine2Export.getTargetVirtualTargets(target));
        this.citations.addAll(Spine2Export.getTargetCitations(target));
        this.dbrefs.addAll(Spine2Export.getTargetDbRefs(target));

        for (final ResearchObjectiveElement blueprintComponent : target.getResearchObjectiveElements()) {
            // check if it is SPOTConstruct
            final String compType = blueprintComponent.getComponentType();
            boolean isSPOTConstruct = false;
            if (compType != null) {
                isSPOTConstruct = compType.startsWith(ConstructUtility.SPOTCONSTRUCT);
            }

            if (compType.equals("complex")) {
                final ComplexBean complex =
                    ComplexBeanReader.readComplexBean(blueprintComponent.getResearchObjective());
                this.complexes.add(complex);
            }

            // create experiments list
            if (!isSPOTConstruct || compType.equals("complex")) {

                final ResearchObjective expBlueprint = blueprintComponent.getResearchObjective();
                final Set<Experiment> expLst = expBlueprint.getExperiments();

                for (final Experiment exp : expLst) {
                    this.processExperiment(exp);
                }
            }
        }
    }

    private void processExperiment(final Experiment experiment) {

        // An expression vector is the output of the Cloning (Infusion) experiment
        if (experiment.getExperimentType().getName().equals("Cloning")) {
            final MySample expressionVector = new MyExpressionVector(experiment);
            if (!this.containsSample(this.expressionVectors, expressionVector)) {
                this.expressionVectors.add(expressionVector);
            }
        }

        // An expression experiment
        if (experiment.getExperimentType().getName().equals("Transformation")) {
            final MyExperiment expressionExperiment = new MyExpressionExperiment(experiment);
            if (!this.containsExperiment(this.experiments, expressionExperiment)) {
                this.experiments.add(expressionExperiment);
            }
        }

        // A construct is the output of the Expression experiment
        if (experiment.getExperimentType().getName().equals("Expression")) {
            final MySample construct = new MyConstruct(experiment);
            if (!this.containsSample(this.constructs, construct)) {
                this.constructs.add(construct);
            }
        }

        // A scaledupconstruct is the output of the Concentration experiment
        if (experiment.getExperimentType().getName().equals("Concentration")) {
            final MyExperiment scaleupExperiment = new MyScaleupExperiment(experiment);
            if (!this.containsExperiment(this.experiments, scaleupExperiment)) {
                this.experiments.add(scaleupExperiment);
            }
            final MySample scaledupConstruct = new MyScaledupConstruct(experiment);
            if (!this.containsSample(this.scaledupConstructs, scaledupConstruct)) {
                this.scaledupConstructs.add(scaledupConstruct);
            }
        }
    }

    public void process(final Collection<Target> targets) {

        for (final Target target : targets) {
            this.process(target);
        }

        this.complexesElement();
        this.targetsElement();
        this.virtualTargetsElement();
        this.expressionVectorsElement();
        this.constructsElement();
        this.scaledUpConstructsElement();
        // tse.publicationsElement(citations);
        // tse.databaseReferencesElement(dbrefs);
        this.experimentsElement();
    }

    public void addComplex(final ComplexBean bean) {
        this.complexes.add(bean);
    }

    public void addTarget(final TargetBean bean) {
        this.targets.add(bean);
    }

    public void addVirtualTarget(final ConstructBean bean) {
        this.virtualTargets.add(bean);
    }

    public void addExperiment(final Experiment bean) {
        this.processExperiment(bean);
    }

    public void process() {
        this.complexesElement();
        this.targetsElement();
        this.virtualTargetsElement();
        this.expressionVectorsElement();
        this.constructsElement();
        this.scaledUpConstructsElement();
        // tse.publicationsElement(citations);
        // tse.databaseReferencesElement(dbrefs);
        this.experimentsElement();
    }
}

abstract class MyExperiment {

    protected Set<Sample> inputs = new HashSet<Sample>();

    private final Experiment experiment;

    protected MyExperiment(final Experiment experiment) {
        this.experiment = experiment;
    }

    protected Experiment getExperiment() {
        return this.experiment;
    }

    protected void setMyInputSample(final Sample sample) {
        this.inputs.add(sample);
    }

    protected Collection<Sample> getMyInputSamples() {
        return this.inputs;
    }

    protected Collection<Sample> getInputs(final Experiment experiment, final String type) {

        final Set<Sample> components = new HashSet<Sample>();
        for (final InputSample inputSample : experiment.getInputSamples()) {
            final Sample sample = inputSample.getSample();
            if (null != sample) {
                final OutputSample outputSample = sample.getOutputSample();
                if (null != outputSample) {
                    final Experiment exp = outputSample.getExperiment();
                    if (null != exp) {
                        if (exp.getExperimentType().getName().equals(type)
                            && this.isWanted((ResearchObjective) experiment.getProject(),
                                (ResearchObjective) exp.getProject())) {
                            components.add(sample);
                            //System.out.println("components.add(" + sample.getDbId() + ":" + sample.getName()
                            //    + ")");
                        }
                        components.addAll(this.getInputs(exp, type));
                    }
                }
            }
        }
        return components;
    }

    /**
     * This is to cover the case where the expression vector selected belongs to a target that we are not
     * interested in.
     * 
     * @param experiment
     * @param sample
     * @return
     */
    @Deprecated
    protected boolean isWanted(final ResearchObjective expBlueprint, final ResearchObjective myExpBlueprint) {

        final Set<Target> targets = new HashSet<Target>();
        if (ComplexBeanReader.isComplex(expBlueprint)) {
            for (final ResearchObjectiveElement blueprintComponent : expBlueprint
                .getResearchObjectiveElements()) {
                if (blueprintComponent.getComponentType().equals("complex")) {
                    targets.add(blueprintComponent.getTarget());
                }
            }
        } else {
            for (final ResearchObjectiveElement blueprintComponent : expBlueprint
                .getResearchObjectiveElements()) {
                targets.add(blueprintComponent.getTarget());
            }
        }

        //for (final Target target : targets) {
        //    System.out.println("target [" + target.getName() + "]");
        //}

        final Target target = myExpBlueprint.getResearchObjectiveElements().iterator().next().getTarget();
        //System.out.println("contains [" + target.getName() + "]");
        if (targets.contains(target)) {
            return true;
        }

        return false;
    }
}

class MyExpressionExperiment extends MyExperiment {

    protected MyExpressionExperiment(final Experiment experiment) {
        super(experiment);

        // look back for cloning experiment for inputs
        //  nb. this is necessary because in the case of multi-trans transformations
        //  the expression experiment may have a different target than cloning experiment

        //System.out.println("ExpressionExperiment selected [" + experiment.getDbId() + ":"
        //    + experiment.getProject().getCommonName() + ":" + experiment.getExperimentType().getName()
        //    + ":" + experiment.getName() + "]");
        this.inputs.addAll(this.getInputs(experiment, "Cloning"));
    }
}

class MyScaleupExperiment extends MyExperiment {

    protected MyScaleupExperiment(final Experiment experiment) {
        super(experiment);
        //System.out.println("ScaleupExperiment selected [" + experiment.getProject().getCommonName()
        //    + ":" + experiment.getExperimentType().getName() + ":" + experiment.getName() + "]");
        this.inputs.addAll(this.getInputs(experiment, "Expression"));
    }
}

abstract class MySample {

    private final Long id;

    private Long sourceExperiment;

    private final Set<String> constituents = new HashSet<String>();

    private final OutputSample outputSample;

    private final Date creationDate;

    private String complexName = new String();

    @Deprecated
    protected MySample(final Experiment experiment) {

        //System.out.println("New MySample selected ["
        //    + experiment.getOutputSamples().iterator().next().getSample().getDbId() + ":"
        //    + experiment.getProject().getCommonName() + ":" + experiment.getExperimentType().getName()
        //    + "]");

        this.outputSample = experiment.getOutputSamples().iterator().next();
        this.id = this.outputSample.getSample().getDbId();
        this.creationDate = this.outputSample.getSample().getCreationDate().getTime();

        if (ComplexBeanReader.isComplex((ResearchObjective) experiment.getProject())) {
            this.constituents.addAll(this.getComponents(experiment));
            this.complexName = experiment.getProject().getName();
        } else {
            this.constituents.add(experiment.getProject().getName());
        }
    }

    protected OutputSample getOutputSample() {
        return this.outputSample;
    }

    protected String getComplexName() {
        return this.complexName;
    }

    protected Long getId() {
        return this.id;
    }

    protected Date getCreationDate() {
        return this.creationDate;
    }

    protected void setMySourceExperiment(final Long s) {
        this.sourceExperiment = s;
    }

    protected Long getSourceExperiment() {
        return this.sourceExperiment;
    }

    protected Set<String> getConstituents() {
        return this.constituents;
    }

    /**
     * warning - recursive method to search down the experiment tree and list SpotConstructs
     * 
     * @param experiment
     * @return
     */
    private Collection<String> getComponents(Experiment experiment) {
        //System.out.println("getComponents(" + experiment.get_Name() + ")");
        final Set<String> components = new HashSet<String>();

        for (final InputSample inputSample : experiment.getInputSamples()) {
            final Sample sample = inputSample.getSample();
            if (null != sample) {
                final OutputSample outputSample = sample.getOutputSample();
                if (null != outputSample) {
                    experiment = outputSample.getExperiment();
                    if (null != experiment) {
                        final Project blueprint = experiment.getProject();
                        if (Spine2Export.isSpotConstruct((ResearchObjective) blueprint)) {
                            components.add(blueprint.getName());
                            //System.out.println("components.add(" + blueprint.getCommonName() + ")");
                        }
                        components.addAll(this.getComponents(experiment));
                    }
                }
            }
        }

        return components;
    }

    protected Experiment getSourceExperiment(final Experiment experiment, final String type) {

        if (experiment.getExperimentType().getName().equals(type)) {
            return experiment;
        } else {
            for (final InputSample inputSample : experiment.getInputSamples()) {
                final Sample sample = inputSample.getSample();
                if (null != sample) {
                    final OutputSample outputSample = sample.getOutputSample();
                    if (null != outputSample) {
                        final Experiment exp = outputSample.getExperiment();
                        if (null != exp) {
                            return this.getSourceExperiment(exp, type);
                        }
                    }
                }
            }
        }
        return null;
    }
}

class MyExpressionVector extends MySample {

    protected MyExpressionVector(final Experiment experiment) {
        super(experiment);
    }
}

class MyConstruct extends MySample {

    protected MyConstruct(final Experiment experiment) {
        super(experiment);
        this.setMySourceExperiment(this.getSourceExperiment(experiment, "Transformation").getDbId());
    }
}

class MyScaledupConstruct extends MySample {

    protected MyScaledupConstruct(final Experiment experiment) {
        super(experiment);
        this.setMySourceExperiment(this.getSourceExperiment(experiment, "Concentration").getDbId());
    }
}
