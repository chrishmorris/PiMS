// ----------------------------------------------------------------------------------------------
// SPOTTarget.java SPOT target bean
// Created by Johan van Niekerk SSPF-Dundee
//
// 23 March 2006
// ----------------------------------------------------------------------------------------------

package org.pimslims.presentation;

import java.util.Iterator;
import java.util.Set;

import org.pimslims.lab.ConstructUtility;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.Alias;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;

/**
 * Factory class to populate the fields of a Presentation layer TargetBean
 * 
 * @author Johan van Niekerk
 */
public class TargetBeanReader {

    /**
     * Dummy db name used for target's web page This is a legacy SPOT function, due to be retired
     */
    public static final String HYPERLINK_DBNAME = "unspecified";

    public static void readTarget(final TargetBean tb, final Target t) {

        tb.setHook(t.get_Hook());
        tb.setMetaClass(t.get_MetaClass());

        tb.setTarget_id(t.getName());
        tb.setName(t.getName());
        tb.setWhyChosen(t.getWhyChosen());
        tb.setFunc_desc(t.getFunctionDescription());
        if (null != t.getCreator()) {
            tb.setCreator(new ModelObjectShortBean(t.getCreator()));
        }

        final Organism organism = t.getSpecies();
        if (null == organism) {
            tb.setOrganism(null);
        } else {
            final ModelObjectBean organismBean = BeanFactory.newBean(organism);
            tb.setOrganism(organismBean);
            Organism.class.getClass().equals(organismBean.getClassName());
            tb.setScientificName(organism.getScientificName());
        }
        String aliases = "";
        for (final Alias alias : t.getAliases()) {
            aliases += alias.getName() + "; ";
        }
        tb.setAliases(aliases);

        // LATER There's probably a more efficient way to do this, but is it
        // really necessary?
        final Set<ExternalDbLink> dbRefs = t.getExternalDbLinks();
        // System.out.println("size of dbrefs is:"+dbRefs.size());
        for (final ExternalDbLink dbRef : dbRefs) {
            // System.out.println("dbrefs name=:"+dbRef.getDbName().getName());
            if (dbRef.getDatabaseName().getName().toString().equalsIgnoreCase(TargetBeanReader.HYPERLINK_DBNAME)) {
                // System.out.println("dbrefs is url:"+dbRef.getUrl());
                //tb.setHyperlink(dbRef.getUrl());
                //tb.setHyperlinkHook(dbRef.get_Hook() + ":" + ExternalDbLink.PROP_URL);
            } else if (dbRef.getDatabaseName().getName().toString().equalsIgnoreCase("GenBank")) {
                tb
                    .setGenbankLink("http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&amp;db=Protein&amp;list_uids="
                        + dbRef.getAccessionNumber());
                tb.setGi_number(dbRef.getAccessionNumber());
                // System.out.println("dbrefs is GenBank:"+dbRef.getStatus());
            }
        } // */

        // Set the Project
        /*
        if (t.getProjects() != null) {
            if (t.getProjects().size() > 0) {
                final Project project = t.getProjects().iterator().next();
                tb.setProject(project.getShortName());
            }
        }
        */
        if (t.getAccess() != null) {
            tb.setLabNotebook(t.getAccess().getName());
        }

        // Set the Hook
        tb.setPimsTargetHook(t.get_Hook());

        // Set the comments
        tb.setComments(t.getWhyChosen());

        // Set the Number of constructs
        // Actually counting the number of ExpBlueprints would be more accurate
        Integer cnt = 0;
        final Set<ResearchObjectiveElement> bcs = t.getResearchObjectiveElements();
        for (final ResearchObjectiveElement bc : bcs) {
            if (ConstructUtility.constructTypes.contains(bc.getComponentType())) {
                cnt++;
            } else {
                // it is the one that represents the target as a whole
                tb.setResearchObjectiveHook(bc.getResearchObjective().get_Hook());
            }
        }

        tb.setNumConstructs(cnt);

        // Set the Protein sequence
        final org.pimslims.model.molecule.Molecule protein = t.getProtein();
        tb.setProtein(protein);
        // Set the DNA sequence

        final Molecule dna = TargetBeanReader.getTargetDNA(t);
        if (null != dna) {
            tb.setDnaSeq(dna.getSequence());
            tb.setDnaSeqHook(dna.get_Hook() + ":" + Molecule.PROP_SEQUENCE);
        }

        // Set the Translated DNA sequence
        // String seq = ConstructDesignUtilities.translate(tb.getDnaSeq());
        if (t.getTargetGroups().size() > 0) {
            //TODO the first targetgroup will be used in the tb
            //we may have more than one targetgroup
            tb.setTargetGroupHook(t.getTargetGroups().iterator().next().get_Hook());
        }
    }

    private static Molecule getTargetDNA(final Target t) {
        if (null != t.getProtein() && t.getProtein().getMolType().equals("DNA")) {
            return t.getProtein();
        }
        Molecule ret = null;
        final Set<Molecule> nucleicAcids = t.getNucleicAcids();
        final Iterator iDna = nucleicAcids.iterator();
        while (iDna.hasNext()) {
            final Molecule dna = (Molecule) iDna.next();
            ret = dna;
            final String dnaSeq = dna.getSequence();
            if (null != dnaSeq && !"".equals(dnaSeq)) {
                return dna;
            }
        }
        return ret;
    }

}
