/**
 * current-pims-web org.pimslims.utils.sequenator SequencingFile.java
 * 
 * @author pvt43
 * @date 16 Feb 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils.sequenator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.pimslims.lab.Util;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.sample.Sample;
import org.pimslims.util.File;
import org.pimslims.utils.experiment.Utils;

/**
 * Class which represents sequencing results which are 3 files.
 * 
 * One is to contain sequence as a text. The anothers are machine readable files in AB1 and SCF formats.
 * Please note that this class is different from ws.client.SequencingFileSet This is created to avoid
 * exporting excessive amount of classes from PIMS DM to the client
 */
public class SequencingFileSet {

    static final int BUFFER = 1024;

    Sample sample;

    File seq;

    File ab1;

    File scf;

    /**
     * Constructor for SequencingFile
     */
    public SequencingFileSet(final Experiment exp) {
        assert exp != null : "Sorry, cannot proceed. Experiment is null!";
        assert exp.getProtocol() != null
            && exp.getProtocol().getName().equalsIgnoreCase(SequencingOrder.soProtocolName) : "It appears that the experiment is either does not have a protocol or the protocol is not a sequening order protocol.";
        assert exp.getOutputSamples() != null && exp.getOutputSamples().size() == 1 : "Only one sample for output is expected!";
        this.sample = exp.getOutputSamples().iterator().next().getSample();

        final Collection<org.pimslims.util.File> files = exp.get_Files();
        for (final org.pimslims.util.File pimsf : files) {
            if (pimsf.getExtension().equalsIgnoreCase(".seq")) {
                this.seq = pimsf;
            }
            if (pimsf.getExtension().equalsIgnoreCase(".ab1")) {
                this.ab1 = pimsf;
            }
            if (pimsf.getExtension().equalsIgnoreCase(".scf")) {
                this.scf = pimsf;
            }
        }

    }

    public List<File> getFiles() {
        final ArrayList<File> files = new ArrayList<File>();
        if (this.seq != null) {
            files.add(this.seq);
        }
        if (this.ab1 != null) {
            files.add(this.ab1);
        }
        if (this.scf != null) {
            files.add(this.scf);
        }

        return files;
    }

    /**
     * SequencingFileSet.toString
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String str = "";
        if (this.seq != null) {
            str += this.seq.getName() + "\n";
        }
        if (this.ab1 != null) {
            str += this.ab1.getName() + "\n";
        }
        if (this.scf != null) {
            str += this.scf.getName() + "\n";
        }

        return str;
    }

    public static void archive(final List<SequencingFileSet> fileSets, final java.io.File archiveTo)
        throws IOException {

        final FileOutputStream dest = new FileOutputStream(archiveTo);
        final ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        //out.setMethod(ZipOutputStream.DEFLATED);
        final byte data[] = new byte[SequencingFileSet.BUFFER];
        BufferedInputStream origin = null;
        final HashSet<String> names = new HashSet<String>();
        for (final SequencingFileSet sfs : fileSets) {
            for (final File file : sfs.getFiles()) {
                final FileInputStream fi = new FileInputStream(file.getFile());
                origin = new BufferedInputStream(fi, SequencingFileSet.BUFFER);
                // Makes sure all names in archive are unique
                String fname = file.getDescription();
                if (names.contains(fname)) {
                    fname = fname + "_" + System.currentTimeMillis();
                }
                final ZipEntry entry = new ZipEntry(fname);
                out.putNextEntry(entry);
                names.add(fname);
                int count;
                while ((count = origin.read(data, 0, SequencingFileSet.BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
        }
        out.close();
    }

    public static void buildFasta(final List<SequencingFileSet> fileSets, final java.io.File archiveTo)
        throws IOException {

        final FileWriter dest = new FileWriter(archiveTo);
        final BufferedWriter out = new BufferedWriter(dest);

        for (final SequencingFileSet sfs : fileSets) {
            if (sfs.seq == null) {
                continue;
            }
            final String seq = Util.readFileToString(sfs.seq.getFile());

            out.write(seq);
            out.newLine();
        }
        out.close();
        dest.close();
    }

    public static void buildTrimmedFasta(final List<SequencingFileSet> fileSets, final java.io.File archiveTo)
        throws IOException {

        final FileWriter dest = new FileWriter(archiveTo);
        final BufferedWriter out = new BufferedWriter(dest);

        for (final SequencingFileSet sfs : fileSets) {
            if (sfs.seq == null) {
                continue;
            }
            String fastaSequence = Util.readFileToString(sfs.seq.getFile());
            if (Util.isEmpty(fastaSequence)) {
                continue;
            }
            final String name = Utils.getFastaName(fastaSequence);
            String sequence = SequencingOrder.getSequenceFromFasta(fastaSequence);
            sequence = DnaSequenceAnalyser.getWellReadFragment(sequence);
            sequence = Util.getFormatedSequence(sequence);
            fastaSequence = new StringBuffer(name).append(sequence).toString();
            out.write(fastaSequence);
            out.newLine();
            out.newLine();
        }
        out.close();
        dest.close();
    }

} // class end
