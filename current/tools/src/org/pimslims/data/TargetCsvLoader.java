/**
 * current-pims-web org.pimslims.data TargetCsvLoader.java
 * 
 * @author susy
 * @date 05-Feb-2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 susy The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import org.biojava.bio.BioException;
import org.pimslims.csv.CsvParser;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.lab.sequence.ProteinSequence;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.Database;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.Target;

/**
 * TargetCsvLoader To load Targets from correctly formatted csv file -see example in /data/test/targets.csv
 * Originally developed for EMBL Grenoble
 */
public class TargetCsvLoader {

    /**
     * Constructor for TargetCsvLoader
     */
    public TargetCsvLoader() {
        super();
    }

    /**
     * TargetCsvLoader.load
     * 
     * @param in java.io.InputStream
     * @param version org.pimslims.WritableVersion
     * @return ret java.util.Collection of Targets
     * @throws IOException
     * @throws ConstraintException
     * @throws BioException
     */
    public static Collection<Target> load(final WritableVersion version, final InputStream in)
        throws IOException, ConstraintException, BioException {
        final Collection<Target> ret = new ArrayList();
        final Reader reader = new InputStreamReader(in);
        final CsvParser p = new CsvParser(reader);
        p.getLabels();
        while (null != p.getLine()) {
            final String name = p.getValueByLabel("Name");
            if (null == name || "".equals(name)) {
                System.out.println("Unable to create Target, no value for name");
                continue;
            }
            String proteinName = p.getValueByLabel("Protein Name");
            if (null == proteinName || "".equals(proteinName)) {
                proteinName = name;
            }
            final Molecule protein = new Molecule(version, "protein", proteinName);
            protein.setSeqString(new ProteinSequence(p.getValueByLabel("Protein Sequence")).getSequence());
            final Target target = new Target(version, name, protein);

            // process DNA sequence
            final String dna = new DnaSequence(p.getValueByLabel("DNA Sequence")).getSequence();
            if (!"".equals(dna)) {
                final Molecule dnaMolecule = new Molecule(version, "DNA", name);
                dnaMolecule.setSeqString(dna);
                final Collection<Molecule> dnaMols = new ArrayList();
                dnaMols.add(dnaMolecule);
                target.setNucleicAcids(dnaMols);
            }
            final String funcDesc = p.getValueByLabel("Function Description");
            target.setFunctionDescription(funcDesc);
            final String comments = p.getValueByLabel("Comments");
            target.setWhyChosen(comments);

            final String orgName = p.getValueByLabel("Organism");
            if (!"".equals(orgName)) {
                final Organism org = new Organism(version, orgName);
                target.setSpecies(org);
            }

            //Process DbRefs
            //Process GINumber -needs GenBank
            final Database dbNameGenbank = version.findFirst(Database.class, Database.PROP_NAME, "GenBank");
            // Check we got dbNameGenbank
            if (dbNameGenbank == null) {
                throw new RuntimeException(
                    "Cannot find required DbName for \"Genbank\". Please check that it exists. ");
            } else {
                final String ginum = p.getValueByLabel("Gi Number");
                if (!"".equals(ginum) && ginum.length() > 0) {
                    final ExternalDbLink dblink = new ExternalDbLink(version, dbNameGenbank, target);
                    dblink.setCode(ginum);
                }
            }

            ret.add(target);
        }
        return ret;
    }

    /**
     * @param args arguments
     * @throws org.biojava.BioException
     */
    public static void main(final String[] args) throws BioException {
        if (args.length == 0) {
            System.out.println("Usage: TargetCsvLoader filename.csv");
        }

        final AbstractModel model = ModelImpl.getModel();
        //AbstractLoader.silent = false;
        String filename = "";
        for (int i = 0; i < args.length; i++) {
            if (!args[i].endsWith(".csv")) {
                filename = filename + args[i] + " ";
            } else {
                filename = filename + args[i];
            }
            //File file = new File(args[i]);
        }
        final File file = new File(filename);
        final WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);

        try {
            final InputStream in = new FileInputStream(file);

            TargetCsvLoader.load(wv, in);
            wv.commit();
            System.out.println("Loaded details from file: " + file);
        } catch (final java.io.IOException ex) {
            System.out.println("Unable to read from file: " + file);
            ex.printStackTrace();
        } catch (final ModelException ex) {
            AbstractLoader.print("Unable to add details from file: " + file);
            ex.printStackTrace();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        System.out.println("TargetCsvLoader has finished");
    }
    //System.out.println("TargetCsvLoader has finished");

    //}

}
