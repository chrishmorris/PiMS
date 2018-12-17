/**
 * pims-web org.pimslims.utils.sequence ZipSequence.java
 * 
 * @author Marc Savitsky
 * @date 1 Aug 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.lab.sequence;


/**
 * ZipSequence ">1794230.seq - ID: A02-T7 on 2006/10/17-8:6:39 automatically edited with PhredPhrap, start
 * with base no.: 28 Internal Params: Windowsize: 20, Goodqual: 19, Badqual: 10, Minseqlength: 50, nbadelimit:
 * 1\n" +
 * "aTCCGGGannTTTAATTCAACCCAACACAATATATTATAGTTAAATAAGAATTATTATCAAATCATTTGTATATTAATTAAAATACTATACTGTAAATTACATTTTATTTACAATCAAAGGAGATATACCATGGCACACCATCACCACCATCACAGCAGCGGTCTGGAAGTTCTGTTTCAGGGCCCGGCAAGTCAAGCGTGGCAACCAGGTGTTGCGATGCCTAACTTGTACAAGATGCAAAGAATGCTTCTTGAAAAGCGTGACCTTCAGAATTATGGTGAAAATGCTGTTATACCAAAAGGAATAATGATGAATGTCGCAAAGTATACTCAACTGTGTCAATACTTAAATACACTTACTTTAGCTGTACCCTACAACATGAGAGTTATTCACTTTGGTGCTGGCTCTGATAAAGGAGTTGCACCAGGTACAGCTGTGCTCAGACAATGGTTGCCAACTGGCACACTACTTGTCGATTCAGATCTTAATGACTTCGTCTCCGACGCAGATTCTACTTTAATTGGAGACTGTGCAACAGTACATACGGCTAATAAATGGGACCTTATTATTAGCGATATGTATGACCCTAGGACCAAACATGTGACAAAAGAGAATGACTCTAAAGAAGGGTTTTTCACTTATCTGTGTGGATTTATAAAGCAAAAACTAGCCCTGGGTGGTTCTATAGCTGTAAAGATAACAGAGCATTCTTGGAATGCTGACCTTTACAAGCTTATGGGCCATTTCTCATGGTGGACAGCTTTTGTTACAAATGTAAATGCATCATCATCGGaggCATTTTTAATTGGGGCTAACTATCTTGGCAAGCCgannnaaCaAATTGATGGCTATACCATGCATGCTAACTACATTTTCTggaggAaCACAAATCCTATccAGttGTCTTCCTATTCACTctttGACaTg "
 * );
 * 
 */
public class PearsonDnaSequence extends AmbiguousDnaSequence implements ASequence {

    private String name = "";

    private String id = "";

    private String waffle = "";

    public PearsonDnaSequence(final String fileName, final String sequence) {

        if (sequence.charAt(0) != '>') { // not a fasta format
            this.id = fileName;
            this.name = fileName;
            this.sequence = AmbiguousDnaSequence.cleanSequence(sequence);
            //System.out.println("ZipSequence NOT Fasta [" + fileName + ":" + this.sequence + "]");
            return;
        }

        int i, j;
        i = sequence.indexOf(" ");
        if (i > 0) {
            this.name = sequence.substring(1, i);
        }

        i = sequence.indexOf("ID: ");
        j = sequence.indexOf(" ", i + 4);
        if (i > 0 && j > 0) {
            this.id = sequence.substring(i + 4, j);
        } else {
            this.id = this.name;
        }

        i = sequence.indexOf("\n");
        if (i > 0) {
            this.waffle = sequence.substring(0, i);
            this.sequence = AmbiguousDnaSequence.cleanSequence(sequence.substring(i + 1));
        }

        //System.out.println("ZipSequence Fasta [" + fileName + ":" + this.sequence + "]");
    }

    public byte[] getAlignSequence() {
        final String alignSequence = new String(">" + this.id + "\n" + this.sequence);
        return alignSequence.getBytes();
    }

    public String getName() {
        return this.name;
    }

    public String getWaffle() {
        return this.waffle;
    }

    public String getID() {
        return this.id;
    }
}
