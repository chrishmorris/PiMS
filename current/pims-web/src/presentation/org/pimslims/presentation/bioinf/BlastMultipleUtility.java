/**
 * current-pims-web org.pimslims.presentation.bioinf BlastMultipleUtility.java
 * 
 * @author susy
 * @date 05-Dec-2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 susy
 * 
 * 
 */
package org.pimslims.presentation.bioinf;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pimslims.bioinf.BioinfUtility;
import org.pimslims.bioinf.BlastAlignmentBean;
import org.pimslims.bioinf.BlastHeaderBean;
import org.pimslims.bioinf.BlastHitBean;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.reference.Database;

import uk.ac.ebi.webservices.InputParams;

/**
 * BlastMultipleUtility
 * 
 */
public class BlastMultipleUtility {

    // Mapping for database abbreviations
    public static final java.util.Map<String, String> dbCodes = new HashMap<String, String>();
    static {
        BlastMultipleUtility.dbCodes.put("pdb", "PDB");
        BlastMultipleUtility.dbCodes.put(InputParams.TARGETDB, "TargetDB");
        BlastMultipleUtility.dbCodes.put("embl", "EMBL"); // should this be InputParams.EMBL
    }

    public static final String TEXT = " top hit:: ";

    static final int MAX = 20;

    /*
     * Use bDbRefPrms to create DbRef for use without new class
     */
    public static Map createDbRefParams(final ReadableVersion rv, final Map attrMap) throws AccessException,
        ConstraintException {
        final HashMap<String, Object> dbRefPrms = new HashMap<String, Object>();
        if (!attrMap.isEmpty()) {

            final HashMap<String, Object> dbNamePrms = new HashMap<String, Object>();

            // Get the matching DbName
            dbNamePrms.put("name", attrMap.get("name"));
            final List allDbNames = (List) rv.findAll(Database.class, dbNamePrms);
            // System.out.println("We have "+ allDbNames.size()+ " matching
            // DbNames");
            if (1 == allDbNames.size()) {
                final ModelObject modelObject = (ModelObject) allDbNames.get(0);
                dbRefPrms.put(ExternalDbLink.PROP_DATABASE, modelObject);
                //dbRefPrms.put("dbName", modelObject);
                // System.out.println("The DbName is "+
                // allDbNames.get(0).getClass());
                // System.out.println("The DbName is "+modelObject.get_Name());
                final String refCode = (String) attrMap.get("code");
                dbRefPrms.put("code", refCode);
                final String refUrl = (String) attrMap.get("url");
                dbRefPrms.put("url", refUrl);
                final String refDetails = (String) attrMap.get("planB");
                //dbRefPrms.put("details", refDetails);
                dbRefPrms.put(Attachment.PROP_DETAILS, refDetails);
                //?? Do we need the date?

            } else {
                new RuntimeException("No matching DbName for: " + attrMap.get("name"));
            }
            // System.out.println("TESTING 240510: BMU l.90 Details in the DbRefMap are: " + dbRefPrms.get("details"));
        }
        return dbRefPrms;
    }

    /**
     * @param testDates
     * @return
     */
    public static Date getLatestDate(final List<Date> testDates) {

        Date latest = null;
        if (testDates.size() > 0) {
            latest = testDates.get(0);
            for (int i = 0; i < testDates.size(); i++) {
                if (latest.before(testDates.get(i))) {
                    latest = testDates.get(i);
                }
            }
        }
        return latest;
    }

    /**
     * Method to Find the latest matching TopHit DbRef based on the date and remove all earlier ones
     * 
     * @param matching TopHits
     * @param latestDate
     * @return topMatch DbRef
     * @throws ParseException
     * @throws ConstraintException
     * @throws AccessException
     */
    public static ExternalDbLink getLatestMatch(final List<ExternalDbLink> matchingTopHits,
        final Date latestDate) throws ParseException, AccessException, ConstraintException {
        ExternalDbLink topMatch = null;
        final List<ExternalDbLink> equalRefs = new java.util.ArrayList();
        if (matchingTopHits.size() > 0) {
            //System.out.println("Number of Top Hit DbRefs: "+matchingTopHits.size());
            for (final ExternalDbLink dbr : matchingTopHits) {
                final Date refDate = BlastMultipleUtility.makeDate(dbr.getDetails());
                if (refDate.equals(latestDate)) {
                    equalRefs.add(dbr);
                }
            }
            if (equalRefs.size() == 1) {
                topMatch = equalRefs.get(0);
            } else if (equalRefs.size() > 1) {
                topMatch = equalRefs.get(0);
            }

        }
        return topMatch;
    }

    /*
     * method to retrieve topHit DbRefs
     */
    public static List<ExternalDbLink> getTopHits(final List<ExternalDbLink> refList) {
        final List topHits = new java.util.ArrayList();
        //System.out.println("The Dbref list contains " +refList.size()+ " DbRefs");
        if (refList.size() > 0) {
            for (final ExternalDbLink dbr : refList) {
                final String refDetails = dbr.getDetails();
                if (null != refDetails) {
                    //System.out.println("Details: " +refDetails);
                    if (refDetails.contains("top hit::")) {
                        topHits.add(dbr);
                        //System.out.println("Got a topHit!");
                        assert topHits.size() > 0;
                    }
                }
            }
        }
        return topHits;
    }

    /*
     * Make a BlastDbRef using the Hash Map created, then add a Target
     */
    public static ModelObject makeBlastDbRef(final WritableVersion wv, final Map attrMap)
        throws AccessException, ConstraintException, ClassNotFoundException {
        if (!attrMap.isEmpty()) {
            //System.out.println("TESTING 240510: BM l.172 makeBlastDbRef: The BlastDbRefMap contains: " + attrMap.toString());
            final ModelObject bDbref = wv.create(ExternalDbLink.class, attrMap);

            return bDbref;
        }
        return null;

    }

    /*
     * Method to process a Blast result Make beans and create a Map to be used
     * to create a DbRef Need: DbRef.code = HitBean.hitDbId DbRef.url =
     * HitBean.dbBaseURL + HitBean.hitURLid (for PDB) or
     * http://targetdb.pdb.org/#search for TargetDb DbRef.details =
     * HitBean.description DbName.name = HeaderBean.databaseSearched Also for
     * 'new' Similarity class: Sim.date = HeaderBean.searchDate Sim.evalue =
     * HitBean.alignmentBeans[0].expect Sim.%similarity = % identity
     * Sim.description = 'status' from TargetDb hit
     */
    public static Map makeBlastDbRefMap(final String blastResult) {
        final HashMap<String, Object> bDbRefPrms = new HashMap<String, Object>();

        if (blastResult.startsWith("<?xml version=")) {
            List blastBeans = new ArrayList();
            assert null != blastResult && !"".equals(blastResult);
            blastBeans = PimsBlastXmlParser.parseXmlString(blastResult);
            assert blastBeans.size() >= 1;
            final BlastHeaderBean bhb = (BlastHeaderBean) blastBeans.get(0);

            // can only make a blastDbRef if there is a HitBean
            // Top hit is second bean
            if (blastBeans.size() > 1) {
                final BlastHitBean topHitBean = (BlastHitBean) blastBeans.get(1);
                // Make the elements for the DbRef
                bDbRefPrms.put("code", topHitBean.getHitDbId());
                // System.out.println("The base url is:
                // "+topHitBean.getDbBaseURL());
                if ("pdb".equalsIgnoreCase(topHitBean.getDatabase())) {
                    bDbRefPrms.put("url", topHitBean.getDbBaseURL() + topHitBean.getHitURLid());
                } else if (InputParams.TARGETDB.equalsIgnoreCase(topHitBean.getDatabase())) {
                    bDbRefPrms.put("url", topHitBean.getDbBaseURL());
                }
                bDbRefPrms.put("details", topHitBean.getDescription());
                // now the elements for the DbName
                bDbRefPrms.put("name", BlastMultipleUtility.dbCodes.get(bhb.getDatabaseSearched()));
                // Now the elements for the 'new' class
                bDbRefPrms.put("date", bhb.getSearchDate());
                final List<BlastAlignmentBean> aliBeans = topHitBean.getAlignmentBeans();
                bDbRefPrms.put("evalue", aliBeans.get(0).getExpect());
                //301007 change to % identity over targe sequence -request by Lester 301007
                //bDbRefPrms.put("similarity", aliBeans.get(0).getIdentity());
                bDbRefPrms.put("similarity", aliBeans.get(0).getNumIdentities());
                // The description attribute will be used to compare the
                // 'status' of a TargetDb hit
                // Otherwise, use the details value
                if ("TargetDB".equals(bDbRefPrms.get("name"))) {
                    bDbRefPrms
                        .put("description", BioinfUtility.tdbStatus((String) bDbRefPrms.get("details")));
                } else {
                    bDbRefPrms.put("description", bDbRefPrms.get("details"));
                }
                // 140907 Plan B
                // Until new BlastRef class is available, record attributes in
                // DbRef.details
                final String evalueString = bDbRefPrms.get("evalue").toString();
                final String simString = bDbRefPrms.get("similarity").toString();
                final String planB =
                    bDbRefPrms.get("name") + BlastMultipleUtility.TEXT + "*date=" + bDbRefPrms.get("date")
                        + " *e-value=" + evalueString + " *%similarity=" + simString + " *description="
                        + bDbRefPrms.get("description");
                bDbRefPrms.put("planB", planB);
                System.out.println("planB is: " + bDbRefPrms.get("planB"));
                // System.out.println("Blast ref map created with
                // "+bDbRefPrms.size()+ " items");
            }
        }
        return bDbRefPrms;
    }

    /*
     * make dates in TopHit DbRef.details string
     */
    //TODO make this Calendar
    public static Date makeDate(final String detailsField) throws ParseException {
        DateFormat formatter;
        Date aDate = null;
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        final String[] detailBits = detailsField.split("\\*");
        if (detailBits.length > 1) {
            String dateField = detailBits[1];
            dateField = dateField.substring(5);
            //aDate = new SimpleDateFormat("dd/mm/yyyy").parse(dateField);
            aDate = formatter.parse(dateField);
            //System.out.println("The date is: " +aDate);
        }
        return aDate;
    }

    /*
     * Check if there are already DbRefs matching the new TopHit
     * TODO This should be for all targets, will need modification with new DM class
     */
    public static List matchingDbRefs(final ReadableVersion rv, final Map map) {
/*        final HashMap<String, Object> matchingPrms = new HashMap<String, Object>();
        matchingPrms.put("code", map.get("code"));
        matchingPrms.put("dbName", map.get("dbName"));
        matchingPrms.put("targets",  map.get(ExternalDbLink.PROP_PARENTENTRY));*/
        //System.out.println("TESTING 250510 matchingDbRefs contains: " + map.toString());
        final List matchingRefs = (List) rv.findAll(ExternalDbLink.class, map);
        return matchingRefs;
    }

    /*
     * Method to process 20 targets at a time Make a sublist of 20 Create a call
     * to WSNCBIBlast for each
     */
    public static void process20(final List targets) {
        final List targetList = targets;
        // int counter = 0;
        final int remainder = targetList.size() % BlastMultipleUtility.MAX;
        if (targetList.size() - remainder == 0) {
            assert targetList.size() == remainder;
            BlastMultiple.blastSublist(targetList);
        } else {

            for (int i = 0; i < targetList.size() - remainder; i += BlastMultipleUtility.MAX) {
                final List twenty = targetList.subList(i, i + BlastMultipleUtility.MAX);
                // counter +=1;
                assert BlastMultipleUtility.MAX == twenty.size();
                BlastMultiple.blastSublist(twenty);
            }
        }
    }

    /*
     * Method to process 20 targets at a time Make a sublists of <=20 Create a
     * call to WSNCBIBlast for each
     */
    public static void processSublist(final List targets) {
        final List targetList = targets;
        int counter = 0;
        List twenty = null;
        List lessThan20 = null;
        final int remainder = targetList.size() % BlastMultipleUtility.MAX;
        // System.out.println("Remainder size is "+remainder);

        final int listSize = targetList.size();
        // System.out.println("TargetList size is "+listSize);
        int numSubLists = 0;

        // ****process a Target list of size <20
        if (listSize == remainder && remainder > 0) {
            lessThan20 = targetList;
            // System.out.println("Processing the short list, size: "
            // +lessThan20.size());
            BlastMultiple.blastSublist(lessThan20);
        }
        // ****process groups of twenty targets
        else {
            for (int i = 0; i < listSize; i += 1) {
                counter += 1;
                if (counter % BlastMultipleUtility.MAX == 0) {
                    numSubLists += 1;
                    // System.out.println("Processing sublist: "+numSubLists);
                    // System.out.println("Last target in list is number:
                    // "+counter);
                    twenty = targetList.subList(i - 1, BlastMultipleUtility.MAX * numSubLists);
                    BlastMultiple.blastSublist(twenty);
                }
                // ****process the remainder
                if (counter == listSize && remainder > 0) {
                    lessThan20 = targetList.subList(listSize - remainder, listSize);
                    // System.out.println("Processing remainder sublist of:
                    // "+lessThan20.size());
                    BlastMultiple.blastSublist(lessThan20);
                }
            }
        }
    }

    /**
     * Update BlastDbRefs Checks for existing DbRefs which match Target TopHit 'code' and 'dbName' If none or
     * 1 match, create a new DbRef If 2, need to delete oldest and create new one
     * 
     * @param vw
     * @param list -a List of matching DbRefs
     * @param attrMap -blastDbRefParams just created from Blast output
     * @param target Target used for Blast
     * 
     */
    public static void updateBlastRefs(final WritableVersion wv, final List<ExternalDbLink> list,
        final Map attrMap) throws AccessException, ConstraintException, ClassNotFoundException {
        //System.out.println("TESTING 250510 updateBlastRefs with: " + attrMap.toString());
        if (list.size() == 0 || list.size() == 1) {
            BlastMultipleUtility.makeBlastDbRef(wv, attrMap);
        } else if (list.size() >= 2) {
            final String TargetID = list.get(0).getParentEntry().getName();
            //count how many are TopHits: if 0 or 1 make a new BlastDbref
            //if >=2 are, remove oldest and make new BlastDbRef
            final List<ExternalDbLink> tophits = BlastMultipleUtility.getTopHits(list);
            final int numHits = tophits.size();
            //System.out.println("TESTING 250510: Target top hits = " + numHits);
            if (numHits == 0) {
                System.out.println("No top hits for target, " + TargetID + " adding first TopHit");
                BlastMultipleUtility.makeBlastDbRef(wv, attrMap);
            } else if (numHits == 1) {
                System.out.println("One top hits for target, " + TargetID + " adding a second TopHit");
                BlastMultipleUtility.makeBlastDbRef(wv, attrMap);
            } else {
                final List<Date> dates = new java.util.ArrayList();
                try {
                    for (final ExternalDbLink dbref : tophits) {
                        final String theDetails = dbref.getDetails();
                        //TESTING 250510
                        //System.out.println("TESTING 250510: " + theDetails);
                        final Date theDate = BlastMultipleUtility.makeDate(theDetails);
                        dates.add(theDate);
                    }
                    final Date latest = BlastMultipleUtility.getLatestDate(dates);
                    //final ExternalDbLink lastTopHit =
                    BlastMultipleUtility.updateLatestMatch(tophits, attrMap, latest);
                } catch (final ParseException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    /**
     * Method to Find the latest matching TopHit DbRef based on the date and update the details field
     * 
     * @param matchingHits
     * @return upadted DbRef
     * @throws ParseException
     * @throws ConstraintException
     */
    public static ExternalDbLink updateLatestMatch(final List<ExternalDbLink> matchingHits, final Map refMap,
        final Date latestDate) throws ParseException, ConstraintException {
        final String newDetails = (String) refMap.get("details");
        ExternalDbLink topMatch = null;
        final List<ExternalDbLink> equalRefs = new java.util.ArrayList();
        if (matchingHits.size() > 0) {
            //System.out.println("Number of Top Hit DbRefs: "+matchingTopHits.size());
            for (final ExternalDbLink dbr : matchingHits) {
                final Date refDate = BlastMultipleUtility.makeDate(dbr.getDetails());
                if (refDate.equals(latestDate)) {
                    //System.out.println("equal");
                    equalRefs.add(dbr);
                }
                // else System.out.println("Impossible date for latest date: "+latestDate);
            }
            topMatch = equalRefs.get(0);
            //System.out.println("TESTING 240510 Old details: " + topMatch.getDetails());
            //System.out.println("TESTING 240510 Details to add: " + newDetails);
            //System.out.println("Updating details for: " + topMatch.getCode());

            topMatch.setDetails(newDetails);
            //System.out.println("TESTING 240510 Details added: " + topMatch.getDetails());

        }
        return topMatch;
    }

}
