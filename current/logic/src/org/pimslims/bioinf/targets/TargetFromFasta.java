/**
 * V3_2-web org.pimslims.bioinf.targets TargetFromFasta.java
 * 
 * @author cm65
 * @date 12 Jun 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.bioinf.targets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pimslims.bioinf.newtarget.PIMSTarget;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.Target;

/**
 * TargetFromFasta
 * 
 */
public class TargetFromFasta {

    private static final Pattern FASTA_HEADER = Pattern.compile(">(\\S+) ?(.*)");

    // target types
    public static final boolean PROTEIN = true;

    public static final boolean DNA = false;

    private final WritableVersion version;

    private final LabNotebook owner;

    /**
     * Constructor for TargetFromFasta
     * 
     * @param version
     */
    public TargetFromFasta(final WritableVersion version, final String labNotebook) {
        this.version = version;
        if (null == labNotebook) {
            this.owner = null;
        } else {
            this.owner = version.findFirst(LabNotebook.class, LabNotebook.PROP_NAME, labNotebook);
        }
    }

    /**
     * TargetFromFasta.load
     * 
     * @param in
     * @return
     */
    public Collection<Target> save(final InputStream in, final boolean isProtein) throws IOException,
        ConstraintException, AccessException {
        if (null != this.owner) {
            this.version.setDefaultOwner(this.owner.getName()); // TODO could restore at end
        }
        final Collection<Target> ret = new ArrayList();
        final BufferedReader r = new BufferedReader(new InputStreamReader(in));
        String line = r.readLine();
        if (null == line) {
            return ret;
        }
        if (!line.startsWith(">")) {
            throw new IllegalArgumentException("Unexpected first line of FASTA file: " + line);
        }
        final java.util.Iterator<String> wells = HolderFactory.POSITIONS_BY_COLUMN_96.iterator();
        while (null != line) {
            final String well = wells.next();
            // at this point, line starts with ">"
            final Matcher m = TargetFromFasta.FASTA_HEADER.matcher(line);
            if (!m.matches()) {
                throw new AssertionError("Unexpected FASTA header: " + line);
            }
            final String name = well + "_" + m.group(1);
            final String description = m.group(2);
            String sequence = "";
            while (null != (line = r.readLine()) && !line.startsWith(">")) {
                sequence = sequence + line;
            }
            final Target target =
                this.makeTarget(name, new DnaSequence(sequence).getSequence(), description, isProtein);
            ret.add(target);

            // at this point, line is null or starts with ">"
        }
        return ret;
    }

    /**
     * TargetFromFasta.makeTarget
     * 
     * @param name
     * @param sequence
     * @return
     */
    private Target makeTarget(final String name, final String sequence, final String description,
        final boolean isProtein) throws ConstraintException, AccessException {
        final Map<String, Object> prop = new HashMap<String, Object>();
        final Molecule protein = new Molecule(this.version, "protein", name + "p");
        prop.put(Target.PROP_NAME, name);
        prop.put(Target.PROP_PROTEIN, protein);
        prop.put(LabBookEntry.PROP_DETAILS, description);

        final Target target = this.version.create(Target.class, prop);

        if (isProtein) {
            final Molecule dna = new Molecule(this.version, "DNA", name + "dna");
            dna.setSequence(sequence);
            target.addNucleicAcid(dna);
        } else {
            // DNA target, e.g. promoter
            protein.setMolType("DNA");
            protein.setSequence(sequence);
            target.setAliasNames(Collections.singletonList(PIMSTarget.DNA_TARGET));
        }
        return target;
    }

}
