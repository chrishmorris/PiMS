/**
 * pims-web org.pimslims.bioinf.local BiojavaAlignment.java
 * 
 * @author cm65
 * @date 3 Jun 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65 *
 * 
 */
package org.pimslims.bioinf.local;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.biojava.bio.BioError;
import org.biojava.bio.BioException;
import org.biojava.bio.alignment.AlignmentPair;
import org.biojava.bio.alignment.SmithWaterman;
import org.biojava.bio.alignment.SubstitutionMatrix;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.pimslims.bioinf.util.MatrixLoader;
import org.pimslims.lab.sequence.AmbiguousDnaSequence;
import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.lab.sequence.ProteinSequence;
import org.pimslims.model.molecule.Molecule;

/**
 * BiojavaAlignment
 * 
 */
/**
 * BiojavaAlignment
 * 
 */
public class BiojavaAlignment extends PimsAlignment {

    /**
     * the number of irrelevant lines at the start of the formatted output
     */
    private static final int HEADER_LINES = 8;

    private final SmithWaterman aligner;

    private String[] formattedAlignmentString;

    private final short extensionPenalty;

    private final short openingPenalty;

    //private final String matrixName;

    private final String hitName;

    private final String queryName;

    private String templateSequence;

    private String hitSequence;

    private final int identity;

    private final int score;

    /**
     * @param querySequence
     * @param molecule
     * @param nf
     * @param revComplement
     * @param alignment
     * @param extensionPenalty
     * @param openingPenalty
     * @param matrixName
     * @param hitName
     * @param queryName
     */
    public BiojavaAlignment(final String querySequence, final Molecule molecule, final boolean revComplement,
        final short extend, final short openingPenalty, final String hitName, final String queryName,
        final Sequence query, final Sequence target, final SubstitutionMatrix matrix) throws BioException {
        super(querySequence, molecule, revComplement);
        this.aligner = new SmithWaterman((short) -1, // match
            (short) 3, // replace 
            openingPenalty, // insert
            openingPenalty, // delete
            extend, // gapExtend
            matrix // SubstitutionMatrix
            );
        final AlignmentPair pairwiseAlignment = this.aligner.pairwiseAlignment(query, target);
        this.formatAlignmentString(pairwiseAlignment.formatOutput(80));
        this.score = pairwiseAlignment.getScore();
        this.identity = pairwiseAlignment.getNumIdenticals();
        this.extensionPenalty = extend;
        this.openingPenalty = openingPenalty;
        //this.matrixName = matrixName;
        this.hitName = hitName;
        this.queryName = queryName;
    }

    /**
     * @see org.pimslims.bioinf.local.PimsAlignment#getExtensionPenalty()
     */
    @Override
    public int getExtensionPenalty() {
        return this.extensionPenalty;
    }

    /**
     * @see org.pimslims.bioinf.local.PimsAlignment#getFormatted()
     */
    @Override
    public String[] getFormatted() {
        return this.formattedAlignmentString;
    }

    /**
     * e.g.
     * 
     * Time (ms): 15 Length: 136 Score: 699.1999999284744 Query: Template, Length: 104 Target:
     * protein-609117932661175532, Length: 120
     * 
     * 
     * Query: 1 AAAA----WWWCCRRRRAAAA----WWWCCRRRRAAAA----WWWCCRRRRAAAA----W 44 |||| ||| |||||||| ||| ||||||||
     * ||| |||||||| | Target: 1 AAAAMMMMWWW--RRRRAAAAMMMMWWW--RRRRAAAAMMMMWWW--RRRRAAAAMMMMW 54
     * 
     * Query: 45 WWCCRRRRAAAA----WWWCCRRRRAAAA----WWWCCRRRRAAAA----WWWCCRRRRA 92 || |||||||| ||| |||||||| |||
     * |||||||| ||| ||||| Target: 55 WW--RRRRAAAAMMMMWWW--RRRRAAAAMMMMWWW--RRRRAAAAMMMMWWW--RRRRA 106
     * 
     * Query: 93 AAA----WWWCCRRRR 104 ||| ||| |||| Target: 107 AAAMMMMWWW--RRRR 120
     * 
     * 
     * Sets identity, template, and hit
     */
    private void formatAlignmentString(final String alignmentString) {

        System.out.println("####\n" + alignmentString + "\n#####");
        final String[] split = alignmentString.split("\n|\r\n|\r");

        assert split.length >= 11 : "Lines in Alignment string: " + split.length;

        // discard header lines
        this.formattedAlignmentString = new String[split.length - BiojavaAlignment.HEADER_LINES];
        String pipes = "";
        this.templateSequence = "";
        this.hitSequence = "";
        for (int i = 0; i < this.formattedAlignmentString.length; i++) {
            final String line = split[i + BiojavaAlignment.HEADER_LINES];
            this.formattedAlignmentString[i] = line;
            if (line.startsWith("Target:")) {
                this.hitSequence += this.getFormattedSequence(line);
            } else if (line.startsWith("Query:")) {
                this.templateSequence += this.getFormattedSequence(line);
            } else {
                // pipes
                pipes += line;
            }
        }

    }

    /**
     * e.g. Query: 3 MMMVVVWW 10
     */
    static final Pattern ALIGNMENT_SEQUENCE = Pattern.compile("^\\w+:\\s+\\d+\\s+(.*?)\\s+\\d+\\s*$");

    /**
     * @return the length of the substring of the template that is matched
     * @see org.pimslims.bioinf.local.PimsAlignment#getTemplateLength()
     */
    @Override
    public int getTemplateLength() {
        final String matchedTemplate = this.getTemplateSequence();
        return matchedTemplate.replace("-", "").length();
    }

    /**
     * @see org.pimslims.bioinf.local.PimsAlignment#getGaps()
     */
    @Override
    public int getGaps() {
        final int inserts = this.getTemplateSequence().split("-").length - 1;
        final int deletes = this.getHitSequence().split("-").length - 1;
        return inserts + deletes;
    }

    /**
     * @see org.pimslims.bioinf.local.PimsAlignment#getHitName()
     */
    @Override
    public String getHitName() {
        return this.hitName;
    }

    /**
     * @see org.pimslims.bioinf.local.PimsAlignment#getIdentity()
     * @return the number of bases that are identical in the alignment
     */
    @Override
    public int getIdentity() {
        if (0 == this.identity) {
            return 0;
        }
        return this.identity + 1; // why +1 ? Seems to work
    }

    /**
     * @see org.pimslims.bioinf.local.PimsAlignment#getHitSequence()
     */
    @Override
    public String getHitSequence() {
        return this.hitSequence;
    }

    private String getFormattedSequence(final String formattedHit) throws AssertionError {
        final Matcher m = BiojavaAlignment.ALIGNMENT_SEQUENCE.matcher(formattedHit);
        if (!m.matches()) {
            throw new AssertionError("Unexpected alignment: "
                + Arrays.toString(this.formattedAlignmentString));
        }
        return m.group(1);
    }

    /**
     * @return the subsequence of the query that was matched
     * @throws AssertionError
     */
    private String getTemplateSequence() throws AssertionError {
        return this.templateSequence;
    }

    /**
     * @see org.pimslims.bioinf.local.PimsAlignment#getPercentGaps()
     */
    @Override
    public String getPercentGaps() {
        return this.nf.format(((float) this.getGaps() / (float) this.getTemplateSequence().length()) * 100);
    }

    /**
     * @see org.pimslims.bioinf.local.PimsAlignment#getHitSeqLength()
     * @Override public int getHitSeqLength() { return this.getHitSequence().replace("-", "").length(); }
     */

    /**
     * @see org.pimslims.bioinf.local.PimsAlignment#getMatrixId()
     * @Override public String getMatrixId() { return this.matrixName; }
     */

    /**
     * @see org.pimslims.bioinf.local.PimsAlignment#getOpeningPenalty()
     */
    @Override
    public int getOpeningPenalty() {
        return this.openingPenalty;
    }

    /**
     * @see org.pimslims.bioinf.local.PimsAlignment#getQueryName()
     */
    @Override
    public String getQueryName() {
        return this.queryName;
    }

    /**
     * @see org.pimslims.bioinf.local.PimsAlignment#getScore()
     */
    @Override
    public int getScore() {
        return this.score;
    }

    /**
     * @see org.pimslims.bioinf.local.PimsAlignment#getSimilarity()
     */
    @Override
    public int getSimilarity() {
        throw new UnsupportedOperationException("This aligner does not calculate similarity");
    }

    /**
     * @param name
     * @param seq
     * @param revComplement
     * @param rv
     * @param molecule
     * @param extend
     * @param openingPenalty
     * @param matrix
     * @param templateName
     * @param templateSeqString
     * @param open
     * @return
     */
    public static PimsAlignment getSmithWatermanAlignment(final String name, final String seq,
        final boolean revComplement, final Molecule molecule, final short extend, final short openingPenalty,
        String matrix, final String templateName, final String templateSeqString, final boolean isDNA) {

        //FiniteAlphabet alphabet = null;
        try {
            SubstitutionMatrix bjmatrix = null;
            if (isDNA) {
                if (null == matrix || "IDENTITY".equals(matrix)) {
                    bjmatrix = new SubstitutionMatrix(DNATools.getDNA(), (short) 1, (short) -1);
                } else {
					bjmatrix = new SubstitutionMatrix(DNATools.getDNA(),
							MatrixLoader.load(matrix), matrix);

                }
            } else {
                if (null == matrix) {
                    matrix = "BLOSUM62";
                }
				bjmatrix = new SubstitutionMatrix(ProteinTools.getTAlphabet(),
						MatrixLoader.load(matrix), matrix);
            }
            Sequence query, target;
            if (isDNA) {
                target = new AmbiguousDnaSequence(seq).getBiojavaSequence(name);
                query = new DnaSequence(templateSeqString).getBiojavaSequence(templateName);
            } else {
                target = new ProteinSequence(seq).getBiojavaSequence(name);
                query = new ProteinSequence(templateSeqString).getBiojavaSequence(templateName);
            }
            /* final SmithWaterman aligner = new SmithWaterman((short) -1, // match
                (short) 3f, // replace 
                openingPenalty, // insert
                openingPenalty, // delete
                extend, // gapExtend
                bjmatrix // SubstitutionMatrix
                ); */
            final PimsAlignment ret =
                new BiojavaAlignment(templateSeqString, molecule, revComplement, extend, openingPenalty,
                    name, templateName, query, target, bjmatrix);
            return ret;
        } catch (final BioError e) {
            Throwable cause = e;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            cause.printStackTrace(); //TODO remove
            throw new RuntimeException(cause);
        } catch (final IllegalSymbolException e) {
            if (isDNA) {
                throw new RuntimeException("Not a DNA matrix:" + matrix, e);
            }
            throw new RuntimeException("Error for matrix:" + matrix, e);
            //CHECKSTYLE:OFF horribly, pairwiseAlignement() is declared as throwing exception
        } catch (final Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e; // propagate it
            }
            throw new RuntimeException("alignment failure for sequence: " + name, e);
        }
    }
}
