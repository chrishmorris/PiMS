package org.pimslims.presentation.leeds;

import java.util.Collection;
import java.util.Map;

import org.pimslims.lab.Util;
import org.pimslims.dao.ReadableVersion;

import org.pimslims.model.target.Target;

/**
 * @author Petr Troshin aka pvt43
 * 
 * FindLeedsTarget
 * 
 */
public class FindLeedsTarget {

    private final static String pattern = "pMPSIL";

    private final static String pattern2 = "pCR-MPSIL";

    private final static String pattern3 = "MPSIL";

    /*
     * Find target corresponding to this experiment
     * expName = pMPSIL0166A / pMPSIL0166 / pMPSIL0166-R
     * @return hook of the corresponding target
     */
    public static Target findCorrespondingTarget(final ReadableVersion rv, final String experimentName) {
        String commonName = FindLeedsTarget.getTargetCommonName(experimentName);
        // Experiment name does not match recognition pattern -> cannot find experiment
        if (commonName == null) {
            return null;
        }
        // Above should return 0116 (see example data in the method descr)
        //System.out.println("Parsed commonName " + commonName);
        commonName = "UL" + commonName;
        return FindLeedsTarget.findTarget(rv, commonName);
    }

    public static Target findTarget(final ReadableVersion rv, final String targetName) {
        final Map<String, Object> tprop = org.pimslims.lab.Util.getNewMap();
        tprop.put(Target.PROP_NAME, targetName);
        final Collection<Target> targets = rv.findAll(Target.class, tprop);
        // No such target
        if (targets == null || targets.size() == 0) {
            return null;
        }
        // Multiple matches found - leave it for the user to decide
        if (targets.size() > 1) {
            return null;
        }
        // if here return value
        return targets.iterator().next();
    }

    public static boolean isMPSITarget(final String targetName) {
        boolean mpsi = false;
        if (targetName.startsWith(FindLeedsTarget.pattern) || targetName.startsWith(FindLeedsTarget.pattern2)
            || targetName.startsWith(FindLeedsTarget.pattern3)) {
            mpsi = true;
        }
        return mpsi;
    }

    public static String getTargetCommonName(String experimentName) {
        if (Util.isEmpty(experimentName)) {
            return null;
        }
        experimentName = experimentName.trim();
        String commonName = experimentName;
        if (FindLeedsTarget.isMPSITarget(experimentName)) {
            if (experimentName.startsWith(FindLeedsTarget.pattern)) {
                commonName =
                    experimentName.substring(experimentName.indexOf(FindLeedsTarget.pattern)
                        + FindLeedsTarget.pattern.length());
            } else if (experimentName.startsWith(FindLeedsTarget.pattern2)) {
                commonName =
                    experimentName.substring(experimentName.indexOf(FindLeedsTarget.pattern2)
                        + FindLeedsTarget.pattern2.length());
            } else if (experimentName.startsWith(FindLeedsTarget.pattern3)) {
                commonName =
                    experimentName.substring(experimentName.indexOf(FindLeedsTarget.pattern3)
                        + FindLeedsTarget.pattern3.length());
            }
            final int dindx = FindLeedsTarget.findLastDigit(commonName);
            if (dindx > 0 && commonName.length() > dindx + 1) {
                commonName = commonName.substring(0, dindx + 1);
            } // else -> nothing to cut
        }

        return commonName;
    }

    /*
     * Examine the string from its end and return the position of firts digit found in the string
     * if nothing found returns -1
     */
    private static int findLastDigit(final String commonName) {
        if (Util.isEmpty(commonName)) {
            return -1;
        }
        assert Character.isDigit(commonName.charAt(0)) : "Check the prefix of the target name it must be either pMPSI or pCR-MPSI";

        if (!Character.isDigit(commonName.charAt(0))) {
            System.out.println("Check the prefix of the target name it must be either pMPSIL or pCR-MPSIL");
        }

        for (int i = 0; i < commonName.length(); i++) {
            if (!Character.isDigit(commonName.charAt(i))) {
                return i - 1;
            }
        }
        return -1;
    }

}
