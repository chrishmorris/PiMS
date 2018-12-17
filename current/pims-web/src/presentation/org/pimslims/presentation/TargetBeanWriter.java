// ----------------------------------------------------------------------------------------------
// SPOTTarget.java SPOT target bean
// Created by Johan van Niekerk SSPF-Dundee
//
// 23 March 2006
// ----------------------------------------------------------------------------------------------

package org.pimslims.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.bioinf.newtarget.PIMSTarget;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.reference.Database;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.Target;

/**
 * Factory class to populate the fields of a Presentation layer TargetBean
 * 
 * @author Johan van Niekerk
 */
public class TargetBeanWriter {

    public static Target createNewTarget(final WritableVersion version, final TargetBean tb)
        throws ConstraintException, AccessException {

        // Get a hash to hold the attributes
        //final HashMap<String, Object> dbNameAttributes = new HashMap<String, Object>();
        // Find DBNAME for genbank
        final Database dbNameGenbank = version.findFirst(Database.class, Database.PROP_NAME, "GenBank");

        // Check we got dbNameGenbank
        if (dbNameGenbank == null) {
            throw new RuntimeException(
                "Cannot find required DbName for \"Genbank\". Please check that it exists. ");
        }

        /* Check we got dbNameHyperlink
        else if (dbNameHyperlink == null) {
            throw new RuntimeException(
                "Cannot find required DbName for \"SPoT Hyperlink\". Please check that it exists. ");
        } */

        ModelObject organism = null;
        if (null != tb.getOrganismHook()) {
            organism = version.get(tb.getOrganismHook());
        }

        // Write Protein Molecule
        // If dnaName is set in targetBean = Target is DNA Target:  Create alias
        Molecule protein = null;
        Molecule dna = null;
        final Collection targAliases = new ArrayList();

        if (null != tb.getDnaName()) {

            //For DNA Target, need to set Protein MolType to DNA and use DNA seq for Protein sequence
            final Map<String, Object> dMoleculeAttributes = new HashMap<String, Object>();
            dMoleculeAttributes.put(Molecule.PROP_SEQUENCE, tb.getDnaSeq());
            dMoleculeAttributes.put(Molecule.PROP_MOLTYPE, "DNA");
            dMoleculeAttributes.put(Substance.PROP_NAME, tb.getDnaName());

            //MolComponent dna = (Molecule) version.create(Molecule.class, dMoleculeAttributes);
            protein = version.create(Molecule.class, dMoleculeAttributes);
            targAliases.add(PIMSTarget.DNA_TARGET);

        } else if (null != tb.getSourceName()) {

            final Map<String, Object> dMoleculeAttributes = new HashMap<String, Object>();
            dMoleculeAttributes.put(Molecule.PROP_SEQUENCE, "");
            dMoleculeAttributes.put(Molecule.PROP_MOLTYPE, "protein");
            dMoleculeAttributes.put(Substance.PROP_NAME, tb.getSourceName());

            //MolComponent dna = (Molecule) version.create(Molecule.class, dMoleculeAttributes);
            protein = version.create(Molecule.class, dMoleculeAttributes);

            targAliases.add("*Natural Source Target*");

        } else {

            //For Protein Target
            final HashMap<String, Object> moleculeAttributes = new HashMap<String, Object>();
            moleculeAttributes.put("seqString", tb.getProtSeq());
            moleculeAttributes.put("molType", "protein");
            moleculeAttributes.put("name", tb.getProtein_name());
            //MolComponent protein = null;
            protein = version.create(Molecule.class, moleculeAttributes);

            // Write DNA Molecule           
            final HashMap<String, Object> dMoleculeAttributes = new HashMap<String, Object>();
            dMoleculeAttributes.put("seqString", tb.getDnaSeq());
            dMoleculeAttributes.put("molType", "DNA");
            dMoleculeAttributes.put("name", tb.getProtein_name() + "DNA");

            //MolComponent dna = (Molecule) version.create(Molecule.class, dMoleculeAttributes);
            dna = version.create(Molecule.class, dMoleculeAttributes);

        }

        final HashMap<String, Object> dbRefAttributes = new HashMap<String, Object>();

        // Write Target:
        //NOTE: DNA Target doesn't need PROP_NUCLEICACIDS
        final HashMap<String, Object> targetAttributes = new HashMap<String, Object>();
        if (null != protein) {
            targetAttributes.put(Target.PROP_PROTEIN, protein);
        }
        //if (null != tb.getDnaName()) {
        if (null != dna) {
            targetAttributes.put(Target.PROP_NUCLEICACIDS, dna);
        }
        targetAttributes.put(Target.PROP_WHYCHOSEN, tb.getComments());
        targetAttributes.put(Target.PROP_FUNCTIONDESCRIPTION, tb.getFunc_desc());
        targetAttributes.put(Target.PROP_NAME, tb.getTarget_id());

        final Target target = version.create(Target.class, targetAttributes);

        target.setSpecies((Organism) organism);

        // DNA Targets don't need a hyperlink or extra DNA molecule but do have an alias
        // Write Genbank DbRef
        //ExternalDbLink dbRef = null;
        if (null != tb.getGi_number() && tb.getGi_number().length() > 0) {
            dbRefAttributes.put(ExternalDbLink.PROP_ACCESSION_NUMBER, tb.getGi_number());
            dbRefAttributes.put(ExternalDbLink.PROP_DATABASE, dbNameGenbank);
            dbRefAttributes.put(Attachment.PROP_PARENTENTRY, target);
            version.create(ExternalDbLink.class, dbRefAttributes);
        }

        // Write SPoT Hyperlink DbRef
        //final ExternalDbLink dbRefHyperlink = null;
        /* if (null != tb.getHyperlink()) {
            dbRefAttributes.clear();
            dbRefAttributes.put(ExternalDbLink.PROP_URL, tb.getHyperlink());
            dbRefAttributes.put(ExternalDbLink.PROP_CODE, "[unknown]");
            dbRefAttributes.put(ExternalDbLink.PROP_DBNAME, dbNameHyperlink);
            dbRefAttributes.put(Attachment.PROP_PARENTENTRY, target);
            dbRefHyperlink = (ExternalDbLink) version.create(ExternalDbLink.class, dbRefAttributes);
        } */

        //For Protein Target add the dna sequence
        if (null != tb.getProtein_name() && null != dna) {
            target.addNucleicAcid(dna);
        }
        if (null != tb.getDnaName() || null != tb.getSourceName()) {
            target.setAliasNames(targAliases);
        }
        // now show the new target
        tb.setHook(target.get_Hook());
        /* if (null != tb.getPersonHook() && !"".equals(tb.getPersonHook())) {
             //final Person scientist = version.get(tb.getPersonHook());
             //final Set<User> users = scientist.getUsers();
             //assert 1 == users.size() : "No or too many users for person: " + scientist.get_Name();
             //target.setCreator(users.iterator().next());
         } */
        return target;

    }

}
