/**
 * 
 */
package org.pimslims.bioinf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.pimslims.lab.Utils;
import org.pimslims.lab.sequence.AmbiguousDnaSequence;

import uk.ac.ebi.webservices.InputParams;

/**
 * @author Susy Griffiths YSBL 22-06-07 Utility class for PiMS Bioinformatics
 */
public class BioinfUtility {

    private static final String ASPACE = " "; // single whitespace

    private static final String TWOSPACE = "  "; // two whitespaces

    private static final String SPACES14 = "              ";

    private static final String TARGROWSTART = "target:"; // lhs end of target

    // row in alignment
    private static final String HITROWSTART = "hit:";

    private static final String DB = "database";

    private static final String LENGTH = "length";

    private static final String GAP = "-";

    private static final String REG = "[A-Z]";

    private static final String REG2 = "[A-Za-z]";

    private static final String START = "start";

    private final static String END = "end";

    private static final String NUM = "number";

    private static final String ALIS = "alignments";

    private static final int SUBSTART = 5;

    /*
     * To count the 'gaps' in a string represented by "-" These are used in the querySeq and matchSeq element
     * text in the alignment element
     */
    public static int gapCounter(final String seq) {
        int gapNum = 0;
        final String gap = BioinfUtility.GAP;
        gapNum = AmbiguousDnaSequence.countRegexpInString(seq, gap);
        return gapNum;
    }

    /*
     * Method to create an array of rows for displaying the alignment rows of query-pattern-match each of 60
     * 'residues' long including gaps Decorated with numbering at each end Based on Jo's method in
     * SpotNewConstructWizardStep1 to display sequence
     */
    public static List processAlignment(final String seq1, final String seq2, final String seq3,
        final int seq1Start, final int seq3Start, final int seq1End, final int seq3End) {
        final List<String> threeSeqArray = new java.util.ArrayList<String>();
        final int resPerRow = 60; // alignments are formatted with 60 residues per row
        final int resPerBlock = 10; // alignments have 6 blocks of 10 residues per row
        final int targetPad = 5; // number of padding characters at lh end for target rows
        final int hitPad = 8; // number of padding characters at lh end for hit rows

        String seq2mod = seq2;
        int cols = 0;
        int cntr = 0;
        String newSeq1 = "";
        String newSeq2 = "";
        String newSeq3 = "";
        final String padding = "   ";
        int gapsInSeq1 = 0;
        int gapsInSeq3 = 0;
        int seq1StartRow = seq1Start;
        int seq3StartRow = seq3Start;
        final String reg = BioinfUtility.REG2;

        // Fix problem with 'short' or incorrect patterns:
        // Sometimes, the xml misses off '+' at start and /or end of pattern
        // Also PiMS-1159 has revealed missing residues and longer pattern

        final String posSymbol = "+";
        if (seq1.length() - seq2mod.length() == 1) {
            seq2mod = posSymbol + seq2mod;
        } else if (seq1.length() - seq2mod.length() == 2) {
            seq2mod = posSymbol + seq2mod + posSymbol;
        } else if ((seq1.length() - seq2mod.length() > 2) || (seq1.length() - seq2mod.length() < 0)
            || (seq1.length() - seq3.length() != 0)) {
            return threeSeqArray;
        }

        for (int i = 0; i < seq1.length(); i++) {
            newSeq1 = newSeq1 + (seq1.substring(i, i + 1));
            newSeq2 = newSeq2 + (seq2mod.substring(i, i + 1));
            newSeq3 = newSeq3 + (seq3.substring(i, i + 1));
            cols++;
            if (cols % resPerBlock == 0) {
                newSeq1 = newSeq1 + BioinfUtility.ASPACE;
                newSeq2 = newSeq2 + BioinfUtility.ASPACE;
                newSeq3 = newSeq3 + BioinfUtility.ASPACE;
            }
            if (cols == resPerRow) {
                cntr++;
                // decorate the lh end of the sequence
                newSeq1 =
                    BioinfUtility.TARGROWSTART
                        + BioinfUtility.padString(String.valueOf(seq1StartRow), targetPad)
                        + BioinfUtility.TWOSPACE + newSeq1;
                newSeq2 = BioinfUtility.SPACES14 + newSeq2;
                newSeq3 =
                    BioinfUtility.HITROWSTART + BioinfUtility.padString(String.valueOf(seq3StartRow), hitPad)
                        + BioinfUtility.TWOSPACE + newSeq3;
                final int gapsInSeq1Row = BioinfUtility.gapCounter(newSeq1);
                final int gapsInSeq3Row = BioinfUtility.gapCounter(newSeq3);
                gapsInSeq1 = gapsInSeq1 + gapsInSeq1Row;
                gapsInSeq3 = gapsInSeq3 + gapsInSeq3Row;

                // decorate the rh end of the sequence
                newSeq1 = newSeq1 + padding + (seq1Start - 1 + (resPerRow * cntr) - gapsInSeq1);
                if (seq3Start > seq3End) { //minus strand
                    newSeq3 = newSeq3 + padding + (seq3Start - (resPerRow * cntr) - gapsInSeq3);
                } else {
                    newSeq3 = newSeq3 + padding + (seq3Start - 1 + (resPerRow * cntr) - gapsInSeq3);

                }

                threeSeqArray.add(newSeq1);
                threeSeqArray.add(newSeq2);
                threeSeqArray.add(newSeq3);
                cols = 0;
                newSeq1 = "";
                newSeq2 = "";
                newSeq3 = "";
                seq1StartRow = seq1Start + (resPerRow * cntr) - gapsInSeq1;
                if (seq3Start > seq3End) { //minus strand
                    seq3StartRow = seq3Start - 1 - (resPerRow * cntr) - gapsInSeq3;
                } else {
                    seq3StartRow = seq3Start + (resPerRow * cntr) - gapsInSeq3;
                }
            }

        }
        //last rows
        if (seq1.length() % resPerRow > 0) {
            newSeq1 = newSeq1 + padding + seq1End;
            if (seq1Start > seq1End) {
                newSeq1 =
                    BioinfUtility.TARGROWSTART
                        + BioinfUtility.padString(
                            String.valueOf(seq1End - 1
                                + AmbiguousDnaSequence.countRegexpInString(newSeq1, reg)), targetPad)
                        + BioinfUtility.TWOSPACE + newSeq1;
            } else {
                newSeq1 =
                    BioinfUtility.TARGROWSTART
                        + BioinfUtility.padString(
                            String.valueOf(seq1End + 1
                                - AmbiguousDnaSequence.countRegexpInString(newSeq1, reg)), targetPad)
                        + BioinfUtility.TWOSPACE + newSeq1;
            }
            newSeq2 = BioinfUtility.SPACES14 + newSeq2;
            newSeq3 = newSeq3 + padding + seq3End;
            if (seq3Start > seq3End) {
                newSeq3 =
                    BioinfUtility.HITROWSTART
                        + BioinfUtility.padString(
                            String.valueOf(seq3End - 1
                                + AmbiguousDnaSequence.countRegexpInString(newSeq3, reg)), hitPad)
                        + BioinfUtility.TWOSPACE + newSeq3;
            } else {
                newSeq3 =
                    BioinfUtility.HITROWSTART
                        + BioinfUtility.padString(
                            String.valueOf(seq3End + 1
                                - AmbiguousDnaSequence.countRegexpInString(newSeq3, reg)), hitPad)
                        + BioinfUtility.TWOSPACE + newSeq3;
            }
            threeSeqArray.add(newSeq1);
            threeSeqArray.add(newSeq2);
            threeSeqArray.add(newSeq3);
        }
        return threeSeqArray;

    }

    /*
     * Method to process the Header element from the xml output of WSBlast Creates a BlastHeaderBean for a
     * single sequence Blast search Need databaseSearched, searchDate and targetLength
     */
    public static BlastHeaderBean makeHeaderBean(final Element element) {
        List headerChildren = new java.util.ArrayList();
        List paramList = new java.util.ArrayList();
        List dbList = new java.util.ArrayList();
        List seqsList = new java.util.ArrayList();
        final BlastHeaderBean bhb = new BlastHeaderBean();
        String dbSearched = "";
        int targetLength = 0;
        //String searchDate = "";

        assert "Header".equals(element.getName());
        headerChildren = element.getChildren();
        // System.out.println("The header contains "+ headerChildren.size()+ "
        // elements");

        final Element paramElement = (Element) headerChildren.get(2);
        assert "parameters".equals(paramElement.getName());
        paramList = paramElement.getChildren();

        // get the database name
        final Element dbsElement = (Element) paramList.get(1);
        assert "databases".equals(dbsElement.getName());
        dbList = dbsElement.getChildren();
        final Element dbElement1 = (Element) dbList.get(0);
        assert BioinfUtility.DB.equals(dbElement1.getName());
        dbSearched = dbElement1.getAttributeValue("name").trim();
        bhb.setDatabaseSearched(dbSearched);

        // get the sequence length
        final Element seqsElement = (Element) paramList.get(0);
        assert "sequences".equals(seqsElement.getName());
        seqsList = seqsElement.getChildren();
        final Element seqElement1 = (Element) seqsList.get(0);
        assert "sequence".equals(seqElement1.getName());
        targetLength = Integer.parseInt(seqElement1.getAttributeValue(BioinfUtility.LENGTH));
        bhb.setTargetLen(targetLength);

        // get the date: the timeInfo element does not always exist
        Date dateOfSearch = new Date();
        final SimpleDateFormat sdf = new SimpleDateFormat(Utils.date_format);
        if (dateOfSearch == null) {
            dateOfSearch = new Date();
        }
        final String date = sdf.format(dateOfSearch);
        bhb.setSearchDate(date);

        return bhb;
    }

    /**
     * Current date in the default PIMS format
     * 
     * @return
     */
    @Deprecated
    // use Calendar
    public static String getDate(Date datevalue) {
        final SimpleDateFormat sdf = new SimpleDateFormat(Utils.date_format);
        if (datevalue == null) {
            datevalue = new Date();
        }
        return sdf.format(datevalue);
    }

    /*
     * Method to process the 'hit' element and create a BlastHitBean Need hitNum, hitDbId, database,
     * hitLength, description, no of alignments and alignmentBeans
     */
    public static BlastHitBean makeBlastHitBean(final Element element) {
        final BlastHitBean bHitBean = new BlastHitBean();
        int hitNum = 0;
        String hitDbId = "";
        String database = "";
        int hitLength = 0;
        String description = "";
        int noOfAlignments = 0;
        List allAlis = new java.util.ArrayList();
        List alignments = new java.util.ArrayList();
        final List<BlastAlignmentBean> aliBeans = new java.util.ArrayList<BlastAlignmentBean>();

        hitNum = Integer.parseInt(element.getAttributeValue(BioinfUtility.NUM));
        bHitBean.setHitNum(hitNum);
        hitDbId = element.getAttributeValue("id");
        bHitBean.setHitDbId(hitDbId);
        database = element.getAttributeValue(BioinfUtility.DB).trim();
        bHitBean.setDatabase(database);
        // Need to process the ID to use it for creating link to database record
        // e.g. PDB hits in Blast output indicate the chain e.g.1XMR_H = chain H
        // of PDB record 1xmr
        // For sgt, cannot make link to record so just redirect to TargetDb
        // search page
        // But, need to trim trailing '_' from id
        // TODO processing for other datadases
        if ("pdb".equalsIgnoreCase(bHitBean.getDatabase())) {
            bHitBean.setDbBaseURL(BioinfUtility.dbURLMap.get("pdb"));
            bHitBean.setHitURLid(hitDbId.substring(0, Math.min(4, hitDbId.length())));
        } else if (InputParams.TARGETDB.equalsIgnoreCase(bHitBean.getDatabase())) {
            bHitBean.setDbBaseURL(BioinfUtility.dbURLMap.get(InputParams.TARGETDB));
            hitDbId = hitDbId.replaceAll("_", "");
            bHitBean.setHitDbId(hitDbId);
            // } else if (InputParams.EMBL.equalsIgnoreCase(bHitBean.getDatabase())) {
        } else if (bHitBean.getDatabase().startsWith("em")) {
            bHitBean.setDbBaseURL(BioinfUtility.dbURLMap.get("embl")); // should this be InputParams.EMBL
            bHitBean.setHitURLid(hitDbId.trim());
        }
        hitLength = Integer.parseInt(element.getAttributeValue(BioinfUtility.LENGTH));
        bHitBean.setHitLength(hitLength);
        description = element.getAttributeValue("description");
        bHitBean.setDescription(description);
        allAlis = element.getChildren();
        final Element alisElement = (Element) allAlis.get(0);
        assert BioinfUtility.ALIS.equals(alisElement.getName());
        noOfAlignments = Integer.parseInt(alisElement.getAttributeValue("total"));
        bHitBean.setNoOfAlignments(noOfAlignments);

        // The alignments
        alignments = alisElement.getChildren();
        assert noOfAlignments == alignments.size();
        for (final Iterator i = alignments.iterator(); i.hasNext();) {
            final Element aliElement = (Element) i.next();
            assert "alignment".equals(aliElement.getName());
            final BlastAlignmentBean aliBean = BioinfUtility.makeBlastAlignmentBean(aliElement);
            aliBeans.add(aliBean);
        }
        bHitBean.setAlignmentBeans((ArrayList<BlastAlignmentBean>) aliBeans);
        assert aliBeans.size() == noOfAlignments;

        return bHitBean;
    }

    /*
     * Method to process the 'hit' element and create a BlastHitBean Need
     */
    public static BlastAlignmentBean makeBlastAlignmentBean(final Element element) {
        final BlastAlignmentBean bAliBean = new BlastAlignmentBean();
        List aliElElements = new java.util.ArrayList();
        List<String> alignmentRows = new java.util.ArrayList<String>();

        int hitNum = 0;
        int alignmentNo = 0;

        // Get the hit number
        final Element parent = element.getParentElement();
        assert BioinfUtility.ALIS.equals(parent.getName());
        final Element grandParent = parent.getParentElement();
        assert "hit".equals(grandParent.getName());
        hitNum = Integer.parseInt(grandParent.getAttributeValue(BioinfUtility.NUM));
        bAliBean.setHitNum(hitNum);

        // Get the alignment number
        alignmentNo = Integer.parseInt(element.getAttributeValue(BioinfUtility.NUM));
        bAliBean.setAlignmentNo(alignmentNo);

        // Alignment element may not contain all of the child elements
        aliElElements = element.getChildren();
        BioinfUtility.processAliElements(aliElElements, bAliBean);

        // process the alignments to create an array of strings of length 60 of query-pattern-match sequences
        alignmentRows =
            BioinfUtility.processAlignment(bAliBean.getQuerySeq(), bAliBean.getPattern(),
                bAliBean.getMatchSeq(), bAliBean.getQuerySeqStart(), bAliBean.getMatchSeqStart(),
                bAliBean.getQuerySeqEnd(), bAliBean.getMatchSeqEnd());
        assert alignmentRows.size() > 1 || alignmentRows.size() == 0;
        bAliBean.setAlignmentRows((ArrayList<String>) alignmentRows);

        return bAliBean;
    }

    /*
     * Pads a string with spaces at the rh end Use for processing the start of rows in an alignment
     */
    public static String padString(final String rhEnd, final int totLength) {
        String paddedString = "";
        String newEnd = rhEnd;
        while (newEnd.length() < totLength) {
            newEnd = BioinfUtility.ASPACE + newEnd;
        }
        paddedString = newEnd;
        return paddedString;
    }

    /*
     * Method to parse the description from a TargetDb Blast hit To get the status
     */
    public static String tdbStatus(final String description) {
        String status = "";
        if (!description.endsWith("#")) {
            final String[] bits = description.split("#");
            status = bits[bits.length - 1].trim();
        }
        return status;
    }

    // Mapping for database URLs
    //TODO check this, EBI has changed the services
    private static java.util.Map<String, String> dbURLMap = new HashMap<String, String>();
    static {
        BioinfUtility.dbURLMap.put("sgt", "http://targetdb.pdb.org/#search");
        //BioinfUtility.dbURLMap.put("pdb", "http://www.ebi.ac.uk/msd-srv/atlas?id="); // at EBI OUT OF DATE 29-01-10
        BioinfUtility.dbURLMap.put("pdb", "http://www.ebi.ac.uk/pdbe-srv/view/entry/");
        BioinfUtility.dbURLMap.put("embl", "http://www.ebi.ac.uk/cgi-bin/dbfetch?db=embl&id=");

    }

    /*
     * Method to process alignment elements
     */
    public static void processAliElements(final List elements, final BlastAlignmentBean bean) {

        Integer score = 0;
        Double bits = 0.0;
        Double expect = 0.0;
        Double probability = 0.0;
        double identity = 0;
        Double positives = 0.0;
        String strand = "";

        for (final Iterator j = elements.iterator(); j.hasNext();) {
            final Element el = (Element) j.next();

            if ("score".equals(el.getName())) {
                score = Integer.parseInt(el.getTextTrim());
                bean.setScore(score);
            } else if ("bits".equals(el.getName())) {
                bits = Double.parseDouble(el.getTextTrim());
                bean.setBits(bits);
            } else if ("expectation".equals(el.getName())) {
                expect = Double.parseDouble(el.getTextTrim());
                bean.setExpect(expect);
            } else if ("probability".equals(el.getName())) {
                probability = Double.parseDouble(el.getTextTrim());
                bean.setProbability(probability);
            } else if ("identity".equals(el.getName())) {
                identity = Double.parseDouble(el.getTextTrim());
                bean.setIdentity(identity);
            } else if ("positives".equals(el.getName())) {
                positives = Double.parseDouble(el.getTextTrim());
                bean.setPositives(positives);
            } else if ("strand".equals(el.getName())) {
                strand = el.getTextTrim().substring(BioinfUtility.SUBSTART);
                bean.setStrand(strand);
            }
        }
        BioinfUtility.processSequenceElements(elements, bean);
    }

    /*
     * Method to process the Query, pattern and match elements
     */
    public static void processSequenceElements(final List elements, final BlastAlignmentBean theBean) {
        final List<String> threeStrings = new java.util.ArrayList();
        String qSeq = "";
        String mSeq = "";
        final String gap = BioinfUtility.GAP;
        int gaps = 0;
        String querySeq = "";
        int querySeqStart = 0;
        int querySeqEnd = 0;
        int alignmentLen = 0;
        String pattern = "";
        int numIdentities = 0;
        int numPositives = 0;
        String matchSeq = "";
        int matchSeqStart = 0;
        int matchSeqEnd = 0;

        for (final Iterator k = elements.iterator(); k.hasNext();) {
            final Element seqEl = (Element) k.next();
            if ("querySeq".equals(seqEl.getName())) {
                querySeq = seqEl.getText();
                querySeqStart = Integer.parseInt(seqEl.getAttributeValue(BioinfUtility.START));
                querySeqEnd = Integer.parseInt(seqEl.getAttributeValue(BioinfUtility.END));
                qSeq = querySeq;
                threeStrings.add(querySeq);
                alignmentLen = querySeqEnd - querySeqStart + 1;
                theBean.setQuerySeq(querySeq);
                theBean.setQuerySeqStart(querySeqStart);
                theBean.setQuerySeqEnd(querySeqEnd);
                theBean.setAlignmentLen(alignmentLen);
            } else if ("pattern".equals(seqEl.getName())) {
                pattern = seqEl.getText();
                threeStrings.add(pattern);
                theBean.setPattern(pattern);
                if (pattern.contains("|")) { //for DNA equence alignment
                    numIdentities = AmbiguousDnaSequence.countRegexpInString(pattern, "\\|");
                } else {
                    numIdentities = AmbiguousDnaSequence.countRegexpInString(pattern, BioinfUtility.REG);
                }
                theBean.setNumIdentities(numIdentities);
                numPositives = AmbiguousDnaSequence.countRegexpInString(pattern, "[A-Z+]");
                theBean.setNumPositives(numPositives);
            } else if ("matchSeq".equals(seqEl.getName())) {
                matchSeq = seqEl.getText();
                matchSeqStart = Integer.parseInt(seqEl.getAttributeValue(BioinfUtility.START));
                matchSeqEnd = Integer.parseInt(seqEl.getAttributeValue(BioinfUtility.END));
                mSeq = matchSeq;
                threeStrings.add(matchSeq);
                theBean.setMatchSeq(matchSeq);
                theBean.setMatchSeqStart(matchSeqStart);
                theBean.setMatchSeqEnd(matchSeqEnd);
            }
            // count the gaps
            final int gapsInQuery = AmbiguousDnaSequence.countRegexpInString(qSeq, gap);
            final int gapsInMatch = AmbiguousDnaSequence.countRegexpInString(mSeq, gap);
            gaps = gapsInQuery + gapsInMatch;
            theBean.setGaps(gaps);
        }
    }
}
