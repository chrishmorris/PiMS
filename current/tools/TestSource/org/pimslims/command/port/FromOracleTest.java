/**
 * pims-tools org.pimslims.command.port FromOracleTest.java
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

import java.io.IOException;
import java.io.StringReader;

import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Primer;

/**
 * FromOracleTest
 * 
 * We need a utility to get CLOBS out of Oracle. Amazingly, Oracle's standard dump does not dump them! We only
 * use CLOBS for sequences.
 * 
 */

/* This script will dump the sequences.
 * 
sqlplus pimsadmin/c4pt41n4m3r1c4@db1-vip.ngs.rl.ac.uk:1521/ngs11.esc.rl.ac.uk >sequences.txt <<++END++

SET LONG 32000
SET HEADING OFF
PAGES 0
RECSEP OFF

select substanceid, seqstring from mole_molecule where seqstring is not null;
EXIT
++END++

 * 
 * Here is the foot of the results:
              337419
MSGLNDIFEAQKIEWHEMEEQQPEPKSQRDSALGGAAAATPGGLSLSLSPGASGSSGSGSDGDSVPVSPQPAPPSPPAAP
CLPPLAHHPHLPPHPPPPPPQHLAAPAHQPQPAAQLHRTTNFFIDNILRPDFGCKKEQPPPQLLVAAAARGGAGGGGRVE
RDRGQTAAGRDPVHPLGTRAPGAASLLCAPDANCGPPDGSQPAAAGAGASKAGNPAAAAAAAAAAVAAAAAAAAAKPSDT
GGGGSGGGAGSPGAQGTKYPEHGNPAILLMGSANGGPVVKTDSQQPLVWPAWVYCTRYSDRPSSGPRTRKLKKKKNEKED
KRPRTAFTAEQLQRLKAEFQANRYITEQRRQTLAQELSLNESQIKIWFQNKRAKIKKATGIKNGLALHLMAQGLYNHSTT
TVQDKDESE

             337420
GGGGACAAGTTTGTACAAAAAAGCAGGCTTCGAAGGAGATAGAACCATGTCCGGTCTGAACGACATCTTCGAAGCTCAGA
AAATCGAATGGCACGAAATGGAAGAACAGCAGCCGGAACCTAAAAGTCAGCGCGACTCGGCCCTCGGCGGCGCGGCGGCG
GCGACTCCGGGCGGCCTCAGCCTGAGCCTCAGTCCGGGCGCCAGCGGCAGCAGCGGCAGCGGCAGCGATGGAGACAGCGT
GCCGGTGTCCCCGCAGCCTGCGCCCCCCTCGCCGCCCGCGGCGCCTTGCCTGCCGCCCCTGGCCCACCACCCGCACCTCC

CCCCACACCCCCCGCCCCCGCCGCCTCAGCATCTCGCGGCGCCTGCTCACCAGCCGCAGCCAGCGGCCCAGCTGCACCGC
ACCACCAACTTTTTCATCGACAACATCCTGAGGCCGGACTTCGGCTGCAAAAAGGAGCAGCCGCCACCGCAGCTTCTGGT
GGCTGCGGCGGCCAGAGGAGGCGCAGGAGGAGGAGGCCGGGTCGAGCGTGACAGAGGCCAGACTGCCGCAGGTAGAGACC
CTGTCCACCCGTTGGGCACCCGGGCGCCAGGCGCTGCCTCGCTCCTGTGCGCCCCGGACGCGAACTGTGGCCCACCCGAC
GGCTCCCAGCCAGCCGCCGCCGGCGCGGGCGCGTCTAAAGCTGGGAACCCGGCTGCGGCGGCGGCGGCGGCCGCGGCGGC
AGTGGCGGCGGCGGCGGCGGCCGCAGCAGCCAAGCCCTCGGACACCGGTGGCGGCGGCAGTGGAGGCGGCGCGGGGAGCC
CCGGAGCGCAGGGCACCAAATACCCGGAGCACGGCAACCCGGCTATCCTACTTATGGGCTCAGCCAACGGCGGGCCCGTG
GTCAAAACTGACTCGCAGCAGCCTCTCGTATGGCCCGCCTGGGTGTACTGCACACGTTATTCGGATCGTCCATCCTCCGG
TCCGCGCACCAGGAAGCTGAAGAAGAAGAAGAACGAGAAGGAGGACAAGCGGCCGCGGACCGCGTTCACGGCCGAGCAGC
TGCAGAGACTCAAGGCGGAGTTCCAGGCAAACCGCTACATCACGGAGCAGCGGCGGCAGACCCTGGCCCAGGAACTCAGC
CTCAACGAGTCCCAGATCAAGATCTGGTTCCAGAACAAGCGCGCCAAGATCAAGAAAGCCACAGGCATCAAGAACGGCCT
GGCGCTGCACCTCATGGCCCAGGGACTGTACAACCACTCCACCACCACGGTCCAGGACAAAGACGAGAGCGAGTGAGAAT
TC


             337422
GGGGACAAGTTTGTACAAAAAAGCAGGCTTCGAAGGAGATAGAACCATGTCCGGTCTGAACGACATCTTCGAAGCTCAGA
AAATCGAATGGCACGAAATGGAAGAACAGCAGCCGGAA

             337427
GAATTCTCACTCGCTCTCGTCTTTGTCCT


2750 rows selected.

SQL> Disconnected from Oracle Database 11g Enterprise Edition Release 11.1.0.7.0 - 64bit Production
With the Partitioning, Real Application Clusters, OLAP, Data Mining
and Real Application Testing options


 * 
 * */
public class FromOracleTest extends TestCase {

    /**
     * SHORT_SEQ String
     */
    private static final String SHORT_SEQ = "GAATTCTCACTCGCTCTCGTCTTTGTCCT";

    private static final String LONG_SEQ =
        "GGGGACAAGTTTGTACAAAAAAGCAGGCTTCGAAGGAGATAGAACCATGTCCGGTCTGAACGACATCTTCGAAGCTCAGA\r\n"
            + "AAATCGAATGGCACGAAATGGAAGAACAGCAGCCGGAACCTAAAAGTCAGCGCGACTCGGCCCTCGGCGGCGCGGCGGCG\r\n"
            + "GCGACTCCGGGCGGCCTCAGCCTGAGCCTCAGTCCGGGCGCCAGCGGCAGCAGCGGCAGCGGCAGCGATGGAGACAGCGT\r\n"
            + "GCCGGTGTCCCCGCAGCCTGCGCCCCCCTCGCCGCCCGCGGCGCCTTGCCTGCCGCCCCTGGCCCACCACCCGCACCTCC\r\n" + "\r\n"
            + "CCCCACACCCCCCGCCCCCGCCGCCTCAGCATCTCGCGGCGCCTGCTCACCAGCCGCAGCCAGCGGCCCAGCTGCACCGC";

    private static final String UNIQUE = "fo" + System.currentTimeMillis();

    private final AbstractModel model;

    public void testNone() {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            doTest(
                version,
                "\r\n"
                    + "\r\n"
                    + "2750 rows selected.\r\n"
                    + "\r\n"
                    + "SQL> Disconnected from Oracle Database 11g Enterprise Edition Release 11.1.0.7.0 - 64bit Production\r\n"
                    + "With the Partitioning, Real Application Clusters, OLAP, Data Mining\r\n"
                    + "and Real Application Testing options\r\n" + "\r\n" + "");
        } finally {
            version.abort(); // not testing persistence
        }
    }

    /**
     * Constructor for FromOracleTest
     * 
     * @param name
     */
    public FromOracleTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testShortSeq() throws ConstraintException {

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Molecule molecule = new Molecule(version, "DNA", UNIQUE);
            doTest(version, "             " + molecule.getDbId() + "\n" + SHORT_SEQ + "\r\n\r\n"
                + "2 rows selected.\r\n");
            assertEquals(SHORT_SEQ, molecule.getSeqString());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testPrimer() throws ConstraintException {

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Molecule molecule = new Primer(version, "forward", true, "DNA", UNIQUE);
            doTest(version, "             " + molecule.getDbId() + "\n" + SHORT_SEQ + "\r\n\r\n"
                + "2 rows selected.\r\n");
            assertEquals(SHORT_SEQ, molecule.getSeqString());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    private static final String WITH_SPACES =
        "GATACCAGATCGGAGCCAGC CATATCGATATATCTAAGAT CGACAGCTCGATATCGCTAT AGCCGATACCAGATCGT\r\n"
            + "CAG CTTACCATATCGATATATCT ATATCGAGCTCGATATCGCT AAGCCGATACCAGATCGCAG CCATATCGATATA\r\n"
            + "TCTCAGA TCGATCAGCTCGATATCGCT AAGCCGATACCAGATCGTCA GCTAAAGCCATATCGATATA TCTACTCGA\r\n"
            + "TCAGCTCGATA TCGCTACAGCCG\r\n" + "\r\n" + "         ";

    public void testWithSpaces() throws ConstraintException {

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Molecule molecule = new Molecule(version, "DNA", UNIQUE);
            doTest(version, "             " + molecule.getDbId() + "\n" + WITH_SPACES + "\r\n\r\n"
                + "2 rows selected.\r\n");
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testLongSeq() throws ConstraintException {

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Molecule molecule = new Molecule(version, "DNA", UNIQUE);
            doTest(version, "             " + molecule.getDbId() + "\n" + LONG_SEQ + "\r\n\r\n"
                + "2 rows selected.\r\n");
            assertEquals(LONG_SEQ.replaceAll("\\s", ""), molecule.getSeqString());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testTwo() throws ConstraintException {

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Molecule molecule1 = new Molecule(version, "DNA", UNIQUE + "one");
            Molecule molecule2 = new Molecule(version, "DNA", UNIQUE + "two");
            doTest(version, "             " + molecule1.getDbId() + "\n" + SHORT_SEQ + "\r\n\r\n"
                + "             " + molecule2.getDbId() + "\n" + LONG_SEQ + "\r\n\r\n"
                + "2 rows selected.\r\n");
            assertEquals(SHORT_SEQ, molecule1.getSeqString());
            assertEquals(LONG_SEQ.replaceAll("\\s", ""), molecule2.getSeqString());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    /**
     * FromOracleTest.doTest
     * 
     * @param string
     */
    private void doTest(WritableVersion version, String string) {
        try {
            FromOracle.loadSequences(version, new StringReader(string));
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (ConstraintException e) {
            fail(e.getMessage());
        }

    }

}
