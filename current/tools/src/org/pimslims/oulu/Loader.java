/**
 * tools org.pimslims.oulu Loader.java
 * 
 * @author cm65
 * @date 18 Sep 2009
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.oulu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pimslims.access.Access;
import org.pimslims.csv.CsvParser;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Construct;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanWriter;
import org.pimslims.presentation.vector.VectorBean;

/**
 * Loader
 * 
 * ID,Experimental Amino Acid Sequence,Experimental Nucleotide
 * Sequence,Title,Target,Organism,Mutations,Insertion,Truncations,Vector,Comments,Experimental MW,Experimental
 * MW sans HA,Item Type 1,?,NONE,IMPT-1,ADORA3,H.Sapiens,WT,,FL,p2228,A1,18.01524,-1797.28476,Item 60,
 * MEEGGDFDNYYGADNQSECEYTDWKSSGALIPAIYMLVFLLGTTGNGLVLWTVFRSSREKRRSADIFIASLAVADLTFVVTLPLWATYTYRDYDWPFGTFFCKLSSYLIFVNMYASVFCLTGLSFDRYLAIVRPVANARLRLRVSGAVATAVLWVLAALLAMPVMVLRTTGDLENTTKVQCYMDYSMVATVSSEWAWEVGLGVSSTTVGFVVPFTIMLTCYFFIAQTIAGHFRKERIEGLRKRRRLLSIIVVLVVTFALCWMPYHLVKTLYMLGSLLHWPCDFDLFLMNIFPYCTCISYVNSCLNPFLYAFFDPRFRQACTSMLCCGQSRCAGTSHSSSGEKSASYSSGHSQGPGPNMGKGGEQMHEKSIPYSQETLVVD
 * ,
 * ATGGAGGAAGGTGGTGATTTTGACAACTACTATGGGGCAGACAACCAGTCTGAGTGTGAGTACACAGACTGGAAATCCTCGGGGGCCCTCATCCCTGCCATCTACATGTTGGTCTTCCTCCTGGGCACCACGGGCAACGGTCTGGTGCTCTGGACCGTGTTTCGGAGCAGCCGGGAGAAGAGGCGCTCAGCTGATATCTTCATTGCTAGCCTGGCGGTGGCTGACCTGACCTTCGTGGTGACGCTGCCCCTGTGGGCTACCTACACGTACCGGGACTATGACTGGCCCTTTGGGACCTTCTTCTGCAAGCTCAGCAGCTACCTCATCTTCGTCAACATGTACGCCAGCGTCTTCTGCCTCACCGGCCTCAGCTTCGACCGCTACCTGGCCATCGTGAGGCCAGTGGCCAATGCTCGGCTGAGGCTGCGGGTCAGCGGGGCCGTGGCCACGGCAGTTCTTTGGGTGCTGGCCGCCCTCCTGGCCATGCCTGTCATGGTGTTACGCACCACCGGGGACTTGGAGAACACCACTAAGGTGCAGTGCTACATGGACTACTCCATGGTGGCCACTGTGAGCTCAGAGTGGGCCTGGGAGGTGGGCCTTGGGGTCTCGTCCACCACCGTGGGCTTTGTGGTGCCCTTCACCATCATGCTGACCTGTTACTTCTTCATCGCCCAAACCATCGCTGGCCACTTCCGCAAGGAACGCATCGAGGGCCTGCGGAAGCGGCGCCGGCTGCTCAGCATCATCGTGGTGCTGGTGGTGACCTTTGCCCTGTGCTGGATGCCCTACCACCTGGTGAAGACGCTGTACATGCTGGGCAGCCTGCTGCACTGGCCCTGTGACTTTGACCTCTTCCTCATGAACATCTTCCCCTACTGCACCTGCATCAGCTACGTCAACAGCTGCCTCAACCCCTTCCTCTATGCCTTTTTCGACCCCCGCTTCCGCCAGGCCTGCACCTCCATGCTCTGCTGTGGCCAGAGCAGGTGCGCAGGCACCTCCCACAGCAGCAGTGGGGAGAAGTCAGCCAGCTACTCTTCGGGGCACAGCCAGGGGCCCGGCCCCAACATGGGCAAGGGTGGAGAACAGATGCACGAGAAATCCATCCCCTACAGCCAGGAGACCCTTGTGGTTGAC
 * ,IMPT-60,APJ,H.Sapiens,WT,,FL,pSFV-HA-FLAG-10xHis-ER,none,42660.47614,40845.17614,Item 505,
 * MKTIIALSYIFCLVFAGAPGPTSVPLVKAHRSSVSDYVNYDIIVRHYNYTGKLNISADKENSIKLTSVVFILICCFIILENIFVLLTIWKTKKFHRPMYYFIGNLALSDLLAGVAYTANLLLSGATTYKLTPAQWFLREGSMFVALSASVFSLLAIAIERYITMLKMKLHNGSNNFRLFLLISACWVISLILGGLPIMGWNCISALSSCSTVLPLYHKHYILFCTTVFTLLLLSIVILYCRIYSLVRTRSNIFEMLRIDEGLRLKIYKDTEGYYTIGIGHLLTKSPSLNAAKSELDKAIGRNTNGVITKDEAEKLFNQDVDAAVRGILRNAKLKPVYDSLDAVRRAALINMVFQMGETGVAGFTNSLRMLQQKRWDEAAVNLAKSRWYNQTPNRAKRVITTFRTGTWDAYSRSSENVALLKTVIIVLSVFIACWAPLFILLLLDVGCKVKTCDILFRAEYFLVLAVLNSGTNPIIYTLTNKEMRRAFIRIMGRPLEVLFQGPHHHHHHHHHHDYKDDDDK
 * ,
 * ATGAAGACGATCATCGCCCTGAGCTACATCTTCTGCCTGGTGTTCGCCGGCGCGCCGGGACCCACATCGGTGCCGTTGGTGAAAGCCCATAGATCGAGCGTTAGCGACTACGTAAATTATGACATCATCGTCAGACATTACAACTACACCGGAAAGCTGAACATCTCCGCGGATAAGGAGAACTCAATCAAACTCACCAGCGTTGTGTTCATTTTGATCTGCTGCTTCATCATCCTGGAAAACATCTTCGTTCTTCTCACTATCTGGAAGACTAAGAAGTTTCACAGGCCAATGTACTACTTCATTGGCAACTTGGCTCTCAGTGACCTTCTGGCCGGAGTGGCTTATACCGCCAATCTGTTGCTCTCCGGTGCTACGACATATAAGCTCACTCCCGCGCAGTGGTTTCTGCGCGAAGGTTCGATGTTCGTAGCTTTGTCAGCTTCGGTGTTCTCTCTGCTCGCGATCGCCATCGAGAGATACATAACAATGCTGAAAATGAAGTTGCACAACGGCTCTAACAACTTCCGTCTGTTCCTGTTGATTAGTGCTTGCTGGGTTATCAGCCTGATTTTGGGTGGTCTGCCCATTATGGGCTGGAACTGCATCTCTGCGTTGAGTTCTTGCAGCACTGTTCTGCCGCTGTACCACAAGCACTACATTCTGTTTTGTACCACCGTCTTTACCCTGTTGCTGTTGTCGATCGTTATCCTGTACTGTAGAATCTATTCACTTGTGCGCACGCGTTCCAATATCTTCGAGATGCTCCGTATAGACGAGGGCTTGCGTCTGAAGATCTACAAGGATACTGAGGGATATTACACCATCGGTATCGGCCACTTGTTGACAAAGTCCCCCAGCCTCAACGCCGCTAAGTCTGAACTCGACAAGGCCATCGGAAGGAACACAAACGGAGTGATCACGAAGGATGAAGCTGAAAAGCTGTTCAACCAGGATGTGGACGCTGCCGTCAGGGGCATTCTTAGGAACGCTAAGTTGAAGCCTGTTTACGACAGTTTGGACGCCGTTAGGCGTGCAGCCCTGATTAACATGGTCTTCCAAATGGGTGAAACAGGAGTGGCGGGCTTCACTAACTCACTCCGTATGCTCCAACAGAAGAGGTGGGACGAGGCTGCCGTAAACCTTGCTAAGTCAAGGTGGTATAATCAAACCCCAAACCGCGCTAAACGTGTGATCACCACTTTCAGAACCGGAACTTGGGACGCATACTCGAGGAGTTCCGAGAACGTTGCTCTTCTTAAGACAGTCATTATCGTTCTGTCAGTCTTCATCGCGTGCTGGGCACCCCTGTTCATCCTCCTTCTCCTTGACGTCGGCTGCAAGGTTAAGACCTGCGACATCCTTTTCAGGGCAGAATACTTCCTCGTACTGGCAGTGCTGAACAGCGGTACTAACCCAATCATTTACACGCTCACCAACAAAGAAATGAGGAGAGCTTTCATTCGCATCATGGGCCGGCCTCTGGAAGTTCTGTTCCAGGGGCCCCATCATCATCATCATCATCATCATCATCATGACTACAAAGACGATGACGACAAG
 * ,IMPT-494,S1PR1,H.Sapiens,WT,T4L,472-528,pFastBac,none,58914.29014,57098.99014,Item
 * 
 */
public class Loader {

    public static List<ResearchObjective> load(WritableVersion version, Reader reader) throws IOException,
        ConstraintException, AccessException {

        List<ResearchObjective> ret = new ArrayList();

        CsvParser parser = new CsvParser(reader);
        final ArrayList<String> labels = new ArrayList(Arrays.asList(parser.getLabels()));

        Loader loader = new Loader(version);
        while (parser.getLine() != null) {

            String id = parser.getValueByLabel("ID");

            try {
                assert "Item".equals(parser.getValueByLabel("Item Type")) : "What is the meaning of Item Type: "
                    + parser.getValueByLabel("Item Type");

                // ignore: Experimental MW sans HA
                // ignore: Experimental MW

                Construct vector = loader.getVector(parser.getValueByLabel("Vector"));
                // process target
                Organism species = loader.getSpecies(parser.getValueByLabel("Organism"));
                String geneName = parser.getValueByLabel("Target");
                String proteinSeq = parser.getValueByLabel("Experimental Amino Acid Sequence");
                if ("?".equals(proteinSeq)) {
                    proteinSeq = null;
                }
                String dnaSeq = parser.getValueByLabel("Experimental Nucleotide Sequence");
                if ("NONE".equals(dnaSeq)) {
                    dnaSeq = null;
                }
                Target target = loader.getTarget(geneName, species, dnaSeq);

                // get details of construct
                String constructName = parser.getValueByLabel("Title");
                String mutations = parser.getValueByLabel("Mutations");
                String truncations = parser.getValueByLabel("Truncations");
                String insertion = parser.getValueByLabel("Insertion");

                // save it
                ResearchObjective construct =
                    loader.saveConstruct(proteinSeq, target, constructName, mutations, truncations,
                        insertion, parser.getValueByLabel("Comments"), dnaSeq);
                ret.add(construct);
                version.flush();
                System.out.println("Saved: " + construct.getName());
            } catch (ConstraintException e) {
                System.err.println("Error loading line: " + id);
                throw e;
            }
        }
        return ret;
    }

    ResearchObjective saveConstruct(String proteinSeq, Target target, String constructName, String mutations,
        String truncations, String insertion, String comments, String dnaSeq) throws ConstraintException,
        AccessException {
        TargetBean targetBean = new TargetBean(target);
        ConstructBean bean = new ConstructBean(targetBean);
        bean.setName(constructName);
        bean.setExpressedProt(proteinSeq);
        processTruncation(bean, truncations, dnaSeq);
        String description = mutations + "\n" + truncations;
        if (!"".equals(insertion)) {
            description += "\n" + insertion;
        }
        bean.setDescription(description);
        bean.setComments(comments);
        return ConstructBeanWriter.createNewConstruct(version, bean);
    }

    private static final Pattern START_TO_END = Pattern.compile("^\\s*(\\d+)-(\\d+)");

    // e.g. "C-term delta341-380" or "Cdelta376-380"
    private static final Pattern C_DELTA = Pattern.compile(".*(Cdelta|C-term delta)(\\d+)-(\\d+)");

    // e.g. first part of "Ndelta1-10, Cdelta376-380"
    private static final Pattern N_DELTA = Pattern.compile(".*Ndelta1-(\\d+)");

    /**
     * Loader.processTruncation
     * 
     * @param bean
     * @param truncations a string from the Oulu spreadsheet, see examples above
     */
    private void processTruncation(ConstructBean bean, String truncations, String dnaSeq) {
        // default is full length
        bean.setTargetProtStart(1);
        if (null != dnaSeq) {
            bean.setTargetProtEnd(dnaSeq.length() / 3);
        }
        if ("FL".equals(truncations)) {
            return;
        }
        if ("FL no Met".equals(truncations)) {
            if (null == dnaSeq || dnaSeq.startsWith("ATG")) {
                bean.setTargetProtStart(2);
            }
            return;
        }

        Matcher m = N_DELTA.matcher(truncations);
        if (m.lookingAt()) {
            String start = m.group(1);
            bean.setTargetProtStart(Integer.parseInt(start) + 1);
        }

        m = C_DELTA.matcher(truncations);
        if (m.lookingAt()) {
            String end = m.group(2);
            bean.setTargetProtEnd(Integer.parseInt(end) - 1);
        }

        m = START_TO_END.matcher(truncations);
        if (m.lookingAt()) {
            String start = m.group(1);
            bean.setTargetProtStart(Integer.parseInt(start));
            String end = m.group(2);
            bean.setTargetProtEnd(Integer.parseInt(end));
            return;
        }

    }

    private final WritableVersion version;

    /**
     * Constructor for Loader
     * 
     * @param version
     */
    public Loader(WritableVersion version) {
        super();
        this.version = version;
    }

    /**
     * Loader.getVector
     * 
     * @param valueByLabel
     * @return
     * @throws ConstraintException
     */
    Construct getVector(String name) throws ConstraintException {
        if ("".equals(name)) {
            return null;
        }
        name += " Vector";
        Construct ret = this.version.findFirst(Construct.class, Construct.PROP_NAME, name);
        if (null != ret) {
            return ret;
        }
        Construct construct = new Construct(version, name);
        VectorBean.makeRecipe(construct);
        return construct;
    }

    /**
     * Loader.getSpecies
     * 
     * @param valueByLabel
     * @return
     * @throws ConstraintException
     */
    Organism getSpecies(String name) throws ConstraintException {
        if ("".equals(name)) {
            return null;
        }
        Organism ret = this.version.findFirst(Organism.class, Organism.PROP_NAME, name);
        if (null != ret) {
            return ret;
        }
        return new Organism(version, name);
    }

    /**
     * Loader.getTarget
     * 
     * @param geneName
     * @param species
     * @param proteinSeq
     * @param dnaSeq
     * @return
     * @throws ConstraintException
     */
    Target getTarget(String geneName, Organism species, String dnaSeq) throws ConstraintException {
        Target ret = this.version.findFirst(Target.class, Target.PROP_NAME, geneName);
        if (null != ret) {
            return ret;
        }
        //can we get details from Uniprot? The name is a non-species-specific gene name.
        // Veli-Pekka Jaakola says this is the entry code minus "_HUMAN" (ENTRYCODE_HUMAN)
        Molecule protein = new Molecule(version, "protein", geneName + " protein");
        ret = new Target(version, geneName, protein);
        Molecule dna = new Molecule(version, "DNA", geneName + " dna");
        dna.setSeqString(dnaSeq);
        ret.addNucleicAcid(dna);
        ret.setSpecies(species);
        return ret;
    }

    public static void main(String[] args) {

        if (2 != args.length) {
            System.err.println("Usage: project filename");
            System.exit(1);
        }

        AbstractModel model = ModelImpl.getModel();
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        version.setDefaultOwner(args[0]);
        File file = new File(args[1]);
        try {
            Reader reader = new FileReader(file);
            load(version, reader);
            version.commit();
            System.out.println("Loaded: " + file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + file.getAbsolutePath());
            e.printStackTrace();
            System.exit(2);
        } catch (ConstraintException e) {
            e.printStackTrace();
            System.exit(2);
        } catch (AccessException e) {
            e.printStackTrace();
            System.exit(2);
        } catch (IOException e) {
            System.err.println("Error reading file: " + file.getAbsolutePath());
            e.printStackTrace();
            System.exit(2);
        } catch (AbortedException e) {
            e.printStackTrace();
            System.exit(2);
        } finally {
            if (!version.isCompleted())
                version.abort();
        }

    }

}
