/**
 * pims-tools org.pimslims.command.port FromOracle.java
 * 
 * @author cm65
 * @date 9 Mar 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.command.port;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Construct;
import org.pimslims.model.molecule.Extension;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Primer;

/**
 * FromOracle This program needs some input, which you can get like this:
 * 
 * sqlplus pimsadmin/c4pt41n4m3r1c4@db1-vip.ngs.rl.ac.uk:1521/ngs11.esc.rl.ac.uk >sequences.txt <<++END++
 * 
 * SET LONG 32000 SET HEADING OFF PAGES 0 RECSEP OFF
 * 
 * select substanceid, seqstring from mole_molecule where seqstring is not null; EXIT ++END++
 */
public class FromOracle {

    // Patterns for the various lines in the input file. See FromOracleTest.

    static final Pattern END = Pattern.compile("\\d+ rows selected\\.");

    static final Pattern BLANK_LINE = Pattern.compile("^\\s*$");

    static final Pattern DBID = Pattern.compile("^\\s*(\\d+)\\s*$");

    // usually CATG or protein single letter code, but many also contain ambiguous residues
    static final Pattern SEQUENCE = Pattern.compile("^\\s*([^\\d]*)\\s*$");

    /**
     * FromOracle.loadSequences
     * 
     * @param version
     * @param stringReader
     * @throws IOException
     * @throws ConstraintException
     */
    public static void loadSequences(WritableVersion version, Reader reader) throws IOException,
        ConstraintException {
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        int count = 0;
        while (true) {
            if (END.matcher(line).matches()) {
                break;
            }
            if (BLANK_LINE.matcher(line).matches()) {
                line = br.readLine();
            } else {
                Matcher dbidMatcher = DBID.matcher(line);
                if (dbidMatcher.matches()) {
                    String dbid = dbidMatcher.group(1);
                    Molecule molecule = getMolecule(version, dbid);
                    String sequence = "";
                    seq: while (true) {
                        line = br.readLine();
                        Matcher seqMatcher = SEQUENCE.matcher(line);
                        if (!seqMatcher.matches()) {
                            break seq;
                        }
                        sequence += seqMatcher.group(1);
                    }
                    if (null == molecule) {
                        System.err.println("no such molecule: <" + dbid + ">");
                    } else {
                        molecule.setSeqString(sequence);
                        count++;
                    }
                } else {
                    throw new IllegalArgumentException("Unexpected line: " + line);
                }

            }
        }
        System.out.println("Sequences loaded: " + count);

    }

    private static Molecule getMolecule(WritableVersion version, String dbid) {
        String hook = Molecule.class.getName() + ":" + dbid;
        Molecule molecule = version.get(hook);
        if (null == molecule) {
            molecule = version.get(Primer.class.getName() + ":" + dbid);
        }
        if (null == molecule) {
            molecule = version.get(Extension.class.getName() + ":" + dbid);
        }
        if (null == molecule) {
            molecule = version.get(Construct.class.getName() + ":" + dbid);
        }

        return molecule;
    }

    public static void main(String[] args) {
        AbstractModel model = ModelImpl.getModel();
        WritableVersion version = null;
        try {
            version = model.getWritableVersion(Access.ADMINISTRATOR);
            InputStream in = new FileInputStream(new File(args[0]));
            loadSequences(version, new java.io.InputStreamReader(in));
            version.commit();
            System.out.println("OK: Sequences loaded");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (null != version && !version.isCompleted())
                version.abort();
        }
    }

}
