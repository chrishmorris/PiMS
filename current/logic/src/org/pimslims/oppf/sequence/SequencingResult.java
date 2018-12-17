/**
 * v4_2-pims-web org.pimslims.oppf.sequence SequencingResult.java
 * 
 * @author jon
 * @date Nov 17, 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 jon The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.oppf.sequence;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.pimslims.bioinf.local.PimsAlignment;
import org.pimslims.bioinf.local.SWSearch;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.lab.sequence.PcrDnaSequence;
import org.pimslims.lab.sequence.PearsonDnaSequence;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.persistence.PimsQuery;
import org.pimslims.util.File;

/**
 * SequencingResult
 * 
 */
public class SequencingResult {

    private final int SHORT = 10;

    private final Float YES = new Float("50.0");

    private final Float MAYBE = new Float("25.0");

    private final String[] values = { "Yes", "Maybe", "No" };

    public static final String MIME_ZIP = "application/zip";

    public static final String MIME_COMPRESS = "application/x-zip-compressed";

    /**
     * SequenceResultServlet.getOPPFNumber - extract OPPF number from a sequencing result file name of the
     * form "1234-T7.seq".
     * 
     * @param name
     * @return
     */
    protected static String getOPPFNumber(final String name) {

        final Pattern REGEXP = Pattern.compile("^(\\d+)(-T7).*$");
        final Matcher matcher = REGEXP.matcher(name);
        if (!matcher.matches()) {
            return null;
        }

        //System.out.println("SequenceResultServlet.getOPPFNumber [OPPF" + oppf + "]");
        return "OPPF" + matcher.group(1);
    }

    /**
     * SequencingResult.isZipFile
     * 
     * @param file
     * @return
     */
    public static boolean isZipFile(final File file) {
        if (file.getMimeType().equals(SequencingResult.MIME_ZIP)) {
            return true;
        }
        if (file.getMimeType().equals(SequencingResult.MIME_COMPRESS)) {
            return true;
        }
        return false;
    }

    /**
     * SequencingResult.readInputStream
     * 
     * @param zipFile
     * @param entry
     * @return
     * @throws IOException
     */
    public static String readInputStream(final ZipFile zipFile, final ZipEntry entry) throws IOException {
        final StringBuffer sb = new StringBuffer();
        final InputStream zis = zipFile.getInputStream(entry);
        int len;
        final byte[] b = new byte[4096];
        while ((len = zis.read(b, 0, 4096)) >= 0) {
            sb.append(new String(b, 0, len));
        }
        zis.close();
        return sb.toString();
    }

    /**
     * SequencingResult.alignPlate
     * 
     * NB This routine applies no access control its db query. The query is based off experimentGroup so make
     * sure that access control was applied when obtaining that parameter.
     * 
     * TODO Move to logic? TODO Refactor to support single entry alignments based on position in plate.
     * 
     * @param version
     * @param file
     * @param experimentGroup
     * @throws AccessException
     * @throws ConstraintException
     * @throws IOException
     */
    @Deprecated
    // not used
    public PimsAlignment alignExperiment(final ReadableVersion version, final File file,
        final Experiment experiment) throws AccessException, ConstraintException, IOException {

        if (null == experiment) {
            return null;
        }

        if (!SequencingResult.isZipFile(file)) {
            return null;
        }
        //final long begin = System.currentTimeMillis();
        //System.out.println("SequenceResultServlet.alignPlate A [" + (System.currentTimeMillis() - begin)
        //    + "]");

        // Return value
        PimsAlignment alignment = null;

        file.open();
        final ZipFile zipFile = new ZipFile(file.getFile());

        final Collection<ZipEntry> entries = Collections.list((Enumeration<ZipEntry>) zipFile.entries());

        // Array of long to hold loop timings
        final long timings[] = new long[8];

        // Do db read here, though I suspect the alignment performance is just too poor.
        // I think I actually only need construct name and pcr sequence for each experiment
        // - ah, no, I need position in plate also, and the [id of the] parameter would help.
        // I've got the ExperimentGroup to search from.
        // NB NO ACCESS CONTROL IS APPLIED - THIS IS INTENTIONAL
        // TODO Extract 'PCR Product' and '__SEQUENCE' as constants, to somewhere meaningful.
        // TODO Named query?
        final String selectHQL =
            "select p, s.rowPosition, s.colPosition, ro.commonName, m.seqString from Sample s join s.outputSample os join os.experiment e join e.parameters p join e.researchObjective ro join ro.researchObjectiveElements roe join roe.trialMolecules m join m.categories c where c.name = 'PCR Product' and p.name = '__SEQUENCE' and e.id = :eid order by s.rowPosition, s.colPosition";
        final PimsQuery query = PimsQuery.getQuery(version, selectHQL);
        query.setLong("eid", experiment.getDbId());

        final List results = query.list();
        // Debugging:
        //System.out.println("test query row count: " + results.size());
        //for (final Object record : results) {
        //    final Object[] rec = (Object[]) record;
        //    System.out.println(rec[0] + ", " + rec[1] + ", " + rec[2] + ", " + rec[3]);
        //}

        timings[0] = System.currentTimeMillis();

        for (final ZipEntry entry : entries) {

            timings[1] = System.currentTimeMillis();

            if (entry.getName().endsWith(".seq")) {

                //System.out.println("SequenceResultServlet.alignPlate loop [" + entry.getName() + ":"
                //    + (System.currentTimeMillis() - begin) + "]");

                final Object[] record = this.getRecordforEntry(results, entry);

                // Jump to next time round loop if no record, no parameter or no sequence
                if ((null == record) || (null == record[0]) || (null == record[4]) || "".equals(record[4])) {
                    // TODO Log, not System.out.println!
                    System.out.println("Failed to get db record for " + entry.getName());
                    continue;
                }

                // Cast some elements for convenience
                final Parameter scoreParameter = (Parameter) record[0];
                final String constructName = (String) record[3];
                final String constructPcrSequence = (String) record[4];

                timings[2] = System.currentTimeMillis();

                // Get sequence as determined from sequencing results
                final PearsonDnaSequence zipSequence =
                    new PearsonDnaSequence(entry.getName(), SequencingResult.readInputStream(zipFile, entry));

                timings[3] = System.currentTimeMillis();

                // Wrap expected sequence
                final PcrDnaSequence pcrSequence = new PcrDnaSequence(constructPcrSequence);
                pcrSequence.setName(constructName);

                timings[4] = System.currentTimeMillis();

                alignment = SequencingResult.getAlignment(pcrSequence, zipSequence);

                timings[5] = System.currentTimeMillis();

                final String score = this.values[this.scoreAlignment(alignment, zipSequence)];

                // TODO Log not System.out.println!
                System.out.println("Experiment for " + constructName + " at position [" + record[1] + ","
                    + record[2] + "]");
                System.out.println(" zip [" + zipSequence.getID() + ":" + zipSequence.getLength() + "]");
                System.out.println(" pcr [" + pcrSequence.getID() + ":" + pcrSequence.getLength() + "]");
                System.out.println(" score [" + score + "]");

                timings[6] = System.currentTimeMillis();

                scoreParameter.setValue(score);
                scoreParameter.flush();

                timings[7] = System.currentTimeMillis();

                // Report timings
                for (int i = 1; i < timings.length; i++) {
                    // TODO Log not System.out.println!
                    System.out.println("timings[" + i + "]=" + timings[i] + ", delta="
                        + (timings[i] - timings[Math.max(i - 1, 0)]));
                }

                // Have processed only relevant entry so can break here
                break;

            } // End of if file suffix is .seq

        } // End of loop over entries

        file.close();

        //System.out.println("SequenceResultServlet.alignPlate Z [" + (System.currentTimeMillis() - begin)
        //    + "]");

        return alignment;

    }

    /**
     * SequencingResult.alignPlate
     * 
     * NB This routine applies no access control its db query. The query is based off experimentGroup so make
     * sure that access control was applied when obtaining that parameter.
     * 
     * TODO Move to logic? TODO Refactor to support single entry alignments based on position in plate.
     * 
     * @param version
     * @param file
     * @param experimentGroup
     * @throws AccessException
     * @throws ConstraintException
     * @throws IOException
     */
    public void alignPlate(final WritableVersion version, final File file,
        final ExperimentGroup experimentGroup) throws AccessException, ConstraintException, IOException {

        if (null == experimentGroup) {
            return;
        }

        if (!SequencingResult.isZipFile(file)) {
            return;
        }
        //final long begin = System.currentTimeMillis();
        //System.out.println("SequenceResultServlet.alignPlate A [" + (System.currentTimeMillis() - begin)
        //    + "]");

        file.open();
        final ZipFile zipFile = new ZipFile(file.getFile());

        final Collection<ZipEntry> entries = Collections.list((Enumeration<ZipEntry>) zipFile.entries());

        // Array of long to hold loop timings
        final long timings[] = new long[8];

        // Do db read here, though I suspect the alignment performance is just too poor.
        // I think I actually only need construct name and pcr sequence for each experiment
        // - ah, no, I need position in plate also, and the [id of the] parameter would help.
        // I've got the ExperimentGroup to search from.
        // NB NO ACCESS CONTROL IS APPLIED - THIS IS INTENTIONAL
        // the user already has access to the experiment group
        // TODO Extract 'PCR Product' and '__SEQUENCE' as constants, to somewhere meaningful.
        // TODO Named query?
        final String selectHQL =
            "select p, s.rowPosition, s.colPosition, ro.commonName, m.seqString from Sample s join s.outputSample os join os.experiment e join e.parameters p join e.researchObjective ro join ro.researchObjectiveElements roe join roe.trialMolecules m join m.categories c where c.name = 'PCR Product' and p.name = '__SEQUENCE' and e.experimentGroup.id = :egid order by s.rowPosition, s.colPosition";
        final PimsQuery query = PimsQuery.getQuery(version, selectHQL);
        query.setLong("egid", experimentGroup.getDbId());

        final List results = query.list();
        // Debugging:
        //System.out.println("test query row count: " + results.size());
        //for (final Object record : results) {
        //    final Object[] rec = (Object[]) record;
        //    System.out.println(rec[0] + ", " + rec[1] + ", " + rec[2] + ", " + rec[3]);
        //}

        timings[0] = System.currentTimeMillis();

        for (final ZipEntry entry : entries) {

            timings[1] = System.currentTimeMillis();

            if (entry.getName().endsWith(".seq")) {

                //System.out.println("SequenceResultServlet.alignPlate loop [" + entry.getName() + ":"
                //    + (System.currentTimeMillis() - begin) + "]");

                final Object[] record = this.getRecordforEntry(results, entry);

                // Jump to next time round loop if no record, no parameter or no sequence
                if ((null == record) || (null == record[0]) || (null == record[4]) || "".equals(record[4])) {
                    // TODO Log, not System.out.println!
                    System.out.println("Failed to get db record for " + entry.getName());
                    continue;
                }

                // Cast some elements for convenience
                final Parameter scoreParameter = (Parameter) record[0];
                final String constructName = (String) record[3];
                final String constructPcrSequence = (String) record[4];

                timings[2] = System.currentTimeMillis();

                // Get sequence as determined from sequencing results
                final PearsonDnaSequence zipSequence =
                    new PearsonDnaSequence(entry.getName(), SequencingResult.readInputStream(zipFile, entry));

                timings[3] = System.currentTimeMillis();

                // Wrap expected sequence
                final PcrDnaSequence pcrSequence = new PcrDnaSequence(constructPcrSequence);
                pcrSequence.setName(constructName);

                timings[4] = System.currentTimeMillis();

                final PimsAlignment alignment = SequencingResult.getAlignment(pcrSequence, zipSequence);

                timings[5] = System.currentTimeMillis();

                final String score = this.values[this.scoreAlignment(alignment, zipSequence)];

                // TODO Log not System.out.println!
                System.out.println("Experiment for " + constructName + " at position [" + record[1] + ","
                    + record[2] + "]");
                System.out.println(" zip [" + zipSequence.getID() + ":" + zipSequence.getLength() + "]");
                System.out.println(" pcr [" + pcrSequence.getID() + ":" + pcrSequence.getLength() + "]");
                System.out.println(" score [" + score + "]");

                timings[6] = System.currentTimeMillis();

                scoreParameter.setValue(score);
                scoreParameter.flush();

                timings[7] = System.currentTimeMillis();

                // Report timings
                for (int i = 1; i < timings.length; i++) {
                    // TODO Log not System.out.println!
                    System.out.println("timings[" + i + "]=" + timings[i] + ", delta="
                        + (timings[i] - timings[Math.max(i - 1, 0)]));
                }

            } // End of if file suffix is .seq

        } // End of loop over entries

        file.close();

        //System.out.println("SequenceResultServlet.alignPlate Z [" + (System.currentTimeMillis() - begin)
        //    + "]");

    }

    /**
     * SequenceResultServlet.scoreAlignment
     * 
     * TODO I don't understand what this is actually doing!
     * 
     * @param alignment
     * @param zipSequence
     * @return
     */
    private int scoreAlignment(final PimsAlignment alignment, final PearsonDnaSequence zipSequence) {

        final int i = (zipSequence.getLength() * 100) / alignment.getQuerySeqLength();
        if (i < this.SHORT) {
            System.out.println("scoreAlignment NO sequence too short [" + zipSequence.getLength() + "/"
                + alignment.getQuerySeqLength() + "=" + i + "]");
            return 2;
        }

        int shortest = alignment.getQuerySeqLength();
        if (alignment.getQuerySeqLength() > zipSequence.getLength()) {
            shortest = zipSequence.getLength();
        }
        final Float calculate = new Float((alignment.getScore() / shortest) * 100);
        //System.out
        //    .println("scoreAlignment [" + alignment.getScore() + "/" + shortest + "=" + calculate + "]");

        if (calculate > this.YES) {
            return 0;
        } else if (calculate > this.MAYBE) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * SequenceResultServlet.getRecordforEntry
     * 
     * New version that searches through the results of the custom HQL.
     * 
     * Tries to get a match on db object to zipfile object first on the basis of position in plate and failing
     * down to OPPF number, both parsed from zipped filename.
     * 
     * @param results List returned from custom HQL (a list of Object that are really Object[])
     * @param entry Metadata of the zipped file
     * @return The relevant Object[] from results, or null if not found
     */
    protected Object[] getRecordforEntry(final List results, final ZipEntry entry) {

        Object[] ret = null;

        // TODO Migrate the row/col extraction to HolderFactory?
        final Pattern REGEXP = Pattern.compile("^.*([A-P])(\\d+).*$");
        final Matcher matcher = REGEXP.matcher(entry.getName());
        if (matcher.matches()) {

            final int row = HolderFactory.getRow(matcher.group(1));
            final int col = HolderFactory.getColumn(matcher.group(2));

            for (final Object o : results) {
                final Object[] oa = (Object[]) o;
                if ((row == ((Integer) oa[1]).intValue()) && (col == ((Integer) oa[2]).intValue())) {
                    ret = oa;
                    break;
                }
            }

        } else {

            final String constructName = SequencingResult.getOPPFNumber(entry.getName());
            if (null != constructName) {

                for (final Object o : results) {
                    final Object[] oa = (Object[]) o;
                    if (constructName.equals(oa[3])) {
                        ret = oa;
                        break;
                    }
                }

            }
        }

        return ret;

    }

    /**
     * AlignSequenceServlet.getAlignment
     * 
     * @param pcrSequence
     * @param zipSequence
     * @param version
     * @return
     * @throws AccessException
     * @throws ConstraintException
     */
    protected static PimsAlignment getAlignment(final PcrDnaSequence pcrSequence,
        final PearsonDnaSequence zipSequence) throws AccessException, ConstraintException {
        return SequencingResult.getAlignment(pcrSequence.getName(), pcrSequence.getSequence(),
            zipSequence.getName(), zipSequence.getSequence());
    }

    /**
     * AlignSequenceServlet.getAlignment
     * 
     * @param pcrSequence
     * @param zipSequence
     * @return
     * @throws AccessException
     * @throws ConstraintException
     */
    protected static PimsAlignment getAlignment(final String expectedName, final String expectedSequence,
        final String measuredName, final String measuredSequence) throws AccessException, ConstraintException {

        SWSearch search = null;
        search = new SWSearch(expectedName, expectedSequence);
        //final Collection<PimsAlignment> alignments =
        //    search.align(Collections.singleton(zipSequence.getMolComponent(version)), true, version);
        final Map<String, String> namedSequences = new HashMap<String, String>();
        namedSequences.put(measuredName, SWSearch.cleanSequence(measuredSequence));
        final Collection<PimsAlignment> alignments = search.align(namedSequences, true);

        PimsAlignment alignment = null;
        if (alignments.iterator().hasNext()) {
            alignment = alignments.iterator().next();
        }

        return alignment;
    }

}
