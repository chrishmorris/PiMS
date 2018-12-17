package org.pimslims.leeds;

import org.pimslims.lab.experiment.ExperimentUtility;

/**
 * @author Petr Troshin aka pvt43
 * 
 *         FormFieldsNames
 * 
 */
@Deprecated
public interface FormFieldsNames {

    // Reference Data
    // for Entry clone & Expression construct
    String application = "PIMS";

    // Experiment Type TODO that doesn't sound right
    String entryClone = "Entry clone";

    String ENTRY_CLONE_PROTOCOL = FormFieldsNames.entryClone;

    String ENTRY_CLONE_INPUT_SAMPLE = FormFieldsNames.entryClone;

    // old experiment type name
    String LEGACY_EXPRESSION_CONSTRUCT_TYPE = "Expression";

    String RECOMBINATION = "Recombination";

    String EXPRESSION_CONSTRUCT_PROTOCOL = "Expression construct";

    String dfrozen = "Deep-Frozen culture";

    String SPRIMER = "Sense Primer";

    String APRIMER = "AntiSense Primer";

    String ANTIBIOTIC_RESISTANCE = "Antibiotic resistances"; // was "Marker"

    String rsite = "Restriction sites";

    String tag = "Tag";

    String genotype = "Genotype";

    String organism = "Organism";

    String strain = "Strain"; // sample category TODO change this to "Competent Cells"

    String competentCells = "Competent Cells";

    String STRAIN_INPUT = "Competent Cells"; // input sample

    // sample category, was "Plasmid"
    public final String TRANSFORMED_CELLS = "Transformed cells";

    // sample category, was "Plasmid"
    public final String VECTOR = "Vector";

    // sample category, and parameter name
    public final String RECOMBINANT_PLASMID = "Plasmid";

    // input sample name TODO change to "Expression Construct"
    public final String EXPRESSION_CONSTRUCT = "Plasmid";

    // For Primers
    String tmfull = "Tm full";

    String tmgene = "Tm gene";

    String tmseller = "Tm seller";

    String particularity = "Particularity";

    String lenghtOnGene = "Length on the gene";

    String leedscloningDesign = "Cloning design";

    String oppfcloningDesign = "Optic Construct Design";

    String spotcloningDesign = ExperimentUtility.SPOTCONSTRUCT_DESIGN;

    // was SPOTCONSTRUCT_DESIGN_OLD

    String primersDesign = "Primer design";

    String gcgene = "GC gene";

    String boxHolderType = "Box";

    @Deprecated
    // use a research objective element
    String from = "From";

    @Deprecated
    // use a research objective element
    String to = "To";

    String geneLength = "Gene Length";

    String modifications = "Modifications";

    String pCRProdLength = "PCR Product Length";

    String sendFormTo = "Send Form To";

    String ul = "volumn in nL";

    String od = "OD";

    // was String OPPF_ORDER = "OPPF Primer Order Plate"; 

    String PIMS_ORDER = "Primer Order Plate";

    String PIMS_FORWARD_PRIMERS = "PIMS Plate Layout - Forward Primers";

    String PIMS_REVERSE_PRIMERS = "PIMS Plate Layout - Reverse Primers";

    interface PlasmidPdfForm {
        String location1 = "location1";

        String box1 = "box1";

        String position1 = "position1";

        String location2 = "location2";

        String box2 = "box2";

        String position2 = "position2";

        String name = "Name 1";

        String derived = "derived";

        String tag = "Tag";

        String marker = "marker";

        String primer = "primer";

        String rsite = "restriction-site";

        String clonesaver = "clonesaver";

        String vectormap = "vectormap";

        String sequenced = "sequenced";

        String date = "date";

        String designedBy = "designed by";
    }

    interface DeepFrozenCulturePdfForm {
        String name = "Name 1";

        String date = "date";

        String genotype = "genotype";

        String designed = "designed by";

        String marker = "marker";

        String organism = "organism";

        String location1 = "location1";

        String position1 = "position1";

        String box1 = "box1";

        String location2 = "location2";

        String position2 = "position2";

        String box2 = "box2";
    }

    interface PrimersPdfForm {

        String gcgene2 = "GC gene 2";

        String sequence1 = "sequence1";

        String tmseller1 = "Tm seller 1";

        String position1 = "position 1";

        String gcfull1 = "GC full 1";

        String lengthgene1 = "length gene1";

        String sequence2 = "Sequence 2";

        String restrictionsite1 = "restriction site 1";

        String tmfull1 = "Tm full 1";

        String seller1 = "Seller 1";

        String date = "date";

        String seller2 = "Seller 2";

        String tmfull2 = "Tm full 2";

        String modif = "modif";

        String tmgene2 = "Tm gene 2";

        String genelength = "gene length";

        String lengthgene2 = "length gene 2";

        String tmseller2 = "Tm seller 2";

        String gcfull2 = "GC full 2";

        String designedby = "designed by";

        String plasmideconcerned2 = "plasmide concerned 2";

        String position2 = "position 2";

        String sendPDFform = "send PDF form";

        String length2 = "length 2";

        String box2 = "box 2";

        String storagelocation2 = "storage location 2";

        String plasmideconcerned1 = "plasmide concerned 1";

        String from = "from";

        String box1 = "box 1";

        String gcgene1 = "GC gene 1";

        String name1 = "Name 1";

        String tmgene1 = "Tm gene 1";

        String concentration1 = "concentration 1";

        String restrictionsite2 = "restriction site 2";

        String storagelocation1 = "storage location 1";

        String to = "to";

        String pcrproductlenght = "pcr product lenght";

        String concentration2 = "concentration 2";

        String name2 = "Name 2";

        String length1 = "length1";

        String particularity1 = "particularity 1";

        String particularity2 = "particularity 2";
    }
}
