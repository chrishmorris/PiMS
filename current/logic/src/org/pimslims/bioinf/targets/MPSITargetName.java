package org.pimslims.bioinf.targets;

import java.util.HashMap;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.ReadableVersionImpl;

/**
 * Suggest a name for a new target
 * 
 * @author Petr Troshin date May 2006
 */
public class MPSITargetName {
    static int NUMBERSTRINGLENGTH = 6;

    /**
     * Collection if user name -> data owner code Filled in based on the information from user.xml file
     */
    private static HashMap<String, String> userDataowner = new HashMap();

    // Want to make only one instance
    private MPSITargetName() {
        //this.setup(this.getClass().getResourceAsStream("Users.xml"));
    }

    private static MPSITargetName instance;

    static MPSITargetName getInstance() {
        if (MPSITargetName.instance == null) {
            MPSITargetName.instance = new MPSITargetName();
        }
        return MPSITargetName.instance;
    }

    /*
     * Return the String data owner code e.g. UL
     */
    @Deprecated
    // obsolete
    private static String getUserOwnDataOwnerCode(final String loginName) {
        return MPSITargetName.userDataowner.get(loginName);
    }

    /*
     * Parse & put users in the Map
      
    @Deprecated
    // obsolete
    private static void parseUsers(final Element usersElem, final String orgNameCode) {
        final List users = usersElem.getChildren("user");
        for (final Iterator iterator = users.iterator(); iterator.hasNext();) {
            final Element user = (Element) iterator.next();
            //final String login = user.getChild("loginName").getValue();
            // was MPSITargetName.userDataowner.put(login, orgNameCode);
        }
    } */

    /**
     * Call DB sequence for a next number. Method assumed that there is a sequence in the database with the
     * following name "mpsi_" + dataOwner + "_targetNumber" TODO remove these sequences
     * 
     * @param rv - Readeable version
     * @param loginName - String
     * @return next target id for the dataowner to which user belongs
     */
    @Deprecated
    // obsolete
    private synchronized static long getTargetNextNumber(final ReadableVersion rv, final String loginName,
        final String dataOwnerCode) {
        final ReadableVersionImpl rvim = (ReadableVersionImpl) rv;
        final String seqName = "mpsi_" + dataOwnerCode + "_target";
        return rvim.getNextVal(seqName);
    }

    /**
     * Call DB sequence "generic_target" for the next number
     * 
     * @param rv
     * @param loginName
     * @return next target id for the dataowner to which user belongs
     */
    private synchronized static long getTargetNextNumber(final ReadableVersion rv) {
        final ReadableVersionImpl rvim = (ReadableVersionImpl) rv;
        final String seqName = "generic_target";
        return rvim.getNextVal(seqName);

    }

    /**
     * Make commonName for the target. If dataOwner is recognised (was discribed in the user.xml) generate the
     * target name which stick to MPSi naming convention otherwise return next number from the sequence
     * 
     * @param rv - Readable version
     * @param loginName - String user login name
     * @return String target common name
     */
    public static String getTargetCommonName(final ReadableVersion rv, final String loginName) {
        //was final String dataOwnerCode = MPSITargetName.getUserOwnDataOwnerCode(loginName);
        String commonName = "";
        /* if (dataOwnerCode != null) {
            // like MPSI
            commonName =
                dataOwnerCode
                    + MPSITargetName.unify(MPSITargetName.getTargetNextNumber(rv, loginName, dataOwnerCode));
        } else { */
        // like SSPF
        commonName =
            MPSITargetName.getFullLengthNumberString(new Long(MPSITargetName.getTargetNextNumber(rv))
                .toString());
        //}
        return "T" + commonName;
    }

    static public String getFullLengthNumberString(String commonName) {
        while (commonName.length() < MPSITargetName.NUMBERSTRINGLENGTH) {
            commonName = "0" + commonName;
        }
        return commonName;
    }

    /**
     * Assuming that the number of targets < 10000 for each site (dataowner)
     * 
     * @param targetNumber
     * @return String completed with leading zeros
     */
    @Deprecated
    // obsolete
    private static String unify(final long targetNumber) {
        assert targetNumber > 0 && targetNumber < 10000 : "Target number is incorrect ";
        String num = (new Long(targetNumber)).toString();
        final int length = num.length();
        for (int i = 0; i < 4 - length; i++) {
            num = "0" + num;
        }
        return num;
    }

}
