/**
 * 
 */
package org.pimslims.lab.primer;


/**
 * @author susy Provides methods for creating YorkPrimerBeans and other primer functions
 * 
 */
public class YorkPrimerUtility {

    /**
     * Utility for creating and using primer beans
     */
    private YorkPrimerUtility() {
        // this class contains static methods only
    }

    /**
     * Method to process name and sequence of a primer tag Allows for user-entered tags which will not be of
     * the form tagname: sequence
     * 
     * @param nameAndTag String representing tag name and sequence
     * @return String tag processed from tag and name string
     */
    public static String processTag(final String nameAndTag) {
        String tagSeq = "";
        if (null != nameAndTag && !"".equals(nameAndTag)) {
            final String[] splitTag = nameAndTag.split(": ");
            if (splitTag.length > 1) {
                tagSeq = splitTag[1];
            }
        }
        return tagSeq;
    }

    /**
     * Method to provide values for expressed and final protein seqs depending on name of user-selected Tag
     * 
     * @param nameAndTag String representing tag name and sequence
     * @return protEndSeqs String[] of protein sequences
     */
    public static String[] createEndSeqs(final String nameAndTag) {
        final String[] protEndSeqs = new String[2];
        if (!"".equals(nameAndTag)) {
            final String[] splitTag = nameAndTag.split(": ");
            // if(nameAndTag!=""){
            if (splitTag[0].equals("LIC-F")) {
                protEndSeqs[0] = "QWRTHHHHHH";
                protEndSeqs[1] = "QWRTHHHHHH";
            }
            if (splitTag[0].equals("LIC-3CF")) {
                protEndSeqs[0] = "QWRTHHHHHHQWRT";
                protEndSeqs[1] = "QWRT";
            }
        }
        return protEndSeqs;
    }

    /**
     * Method to ensure primer sequence contains no non-sequence characters
     * 
     * @param sequence String sequence of primer to clean
     * @return cleanPrimer String sequence of cleaned primer
     */
    public static String cleanSequence(final String sequence) {
        String cleanSeq = "";
        final String seqToClean = sequence;
        cleanSeq = seqToClean.replaceAll("[^A-Z]", "");
        return cleanSeq;
    }
}
