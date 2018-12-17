/**
 * current-pims-web org.pimslims.bioinf TopHitBeanUtility.java
 * 
 * @author Susy Griffiths YSBL
 * @date 11-Oct-2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 susy
 * 
 * 
 */
package org.pimslims.presentation.bioinf;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.pimslims.bioinf.TopHitBean;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.Target;

/**
 * TopHitBeanUtility Utility class to create TopHitBeans from Blast DbRefs
 * 
 */
public class TopHitBeanUtility {

    /**
     * method to create Blast TopHit Bean
     * 
     * @throws ParseException
     * @throws ConstraintException
     * @throws AccessException
     */
    public static TopHitBean makeTopHitBean(final Target target) throws AccessException, ConstraintException,
        ParseException {
        final TopHitBean thb = new TopHitBean();
        //String tdbStatus ="";

        //set TargetId
        final String hook = target.get_Hook();
        thb.setTargetId(hook);

        //set Target protein sequence length
        if (target.getSeqString() != null) { //shouldn't really be possible
            final String targSeq = target.getSeqString().replaceAll(" ", "").trim();
            //final int seqLen = target.getSeqString().trim().length();
            final int seqLen = targSeq.length();
            thb.setTargetSeqLen(seqLen);
        }
        //set details field: Protein name, Organism & Project
        String protName = "";
        String organism = "";
        protName = target.getProtein().getName();
        if (target.getSpecies() != null) {
            organism = target.getSpecies().getName();
        }
        final String projectName = "";
        /* projects no longer used
        if (target.getProjects() != null) {
            if (target.getProjects().size() > 0) {
                final Project project = target.getProjects().iterator().next();
                projectName = project.getShortName();
            }
        } */
        final String details = TopHitBeanUtility.makeDetails(protName, organism, projectName);
        thb.setDetails(details);

        //Get all the TopHit DbRefs for target
        final Collection<ExternalDbLink> tdbrs = target.getExternalDbLinks();
        final List<ExternalDbLink> dbRefList = new ArrayList(tdbrs);
        final List<ExternalDbLink> topHitList = BlastMultipleUtility.getTopHits(dbRefList);

        //Find PDB topHits
        final List<ExternalDbLink> pdbHits = TopHitBeanUtility.findRefsForDbName(topHitList, "pdb");
        TopHitBeanUtility.populatePDBTHB(target, thb, pdbHits);

        //Find TargetDB hits        
        final List<ExternalDbLink> tdbHits = TopHitBeanUtility.findRefsForDbName(topHitList, "targetdb");
        //System.out.println("There are TDBHits: "+tdbHits.size());
        TopHitBeanUtility.populateTDBTHB(thb, tdbHits);
        return thb;
    }

    /**
     * @param thb
     * @param tdbHits
     * @throws NumberFormatException
     * @throws ParseException
     * @throws AccessException
     * @throws ConstraintException
     */
    public static void populateTDBTHB(final TopHitBean thb, final List<ExternalDbLink> tdbHits)
        throws ParseException, AccessException, ConstraintException {
        String tdbStatus;
        if (tdbHits.size() == 1) {
            assert tdbHits.size() == 1;
            thb.setDateTDBHit(TopHitBeanUtility.getValueFromDetails(tdbHits.get(0).getDetails(), 1));
            thb.setHitIDTDB(tdbHits.get(0).getAccessionNumber());
            thb.setExpectTDB(Double.parseDouble(TopHitBeanUtility.getValueFromDetails(tdbHits.get(0)
                .getDetails(), 2)));
            final Double numIds2 =
                Double.parseDouble(TopHitBeanUtility.getValueFromDetails(tdbHits.get(0).getDetails(), 3));
            thb.setNumIdentitiesTDB(numIds2);
            thb.setSimTDB(numIds2 * 100 / thb.getTargetSeqLen());
            //thb.setSimTDB(Double.parseDouble(getValueFromDetails(tdbHits.get(0).getDetails(),3)));
            if (thb.getSimTDB() > 50.0) {
                thb.setIsGt50(true);
            } else if (thb.getSimTDB() <= 50.0) {
                thb.setIsGt50(false);
            }
            tdbStatus = TopHitBeanUtility.getValueFromDetails(tdbHits.get(0).getDetails(), 4);
            thb.setTargetDBStatus(tdbStatus);
            thb.setIsCrystallizedOrLater(TopHitBeanUtility.crystallizedOrLater(tdbStatus.trim()));
            thb.setIsSameAsPreviousTDB(false);
        }

        else if (tdbHits.size() >= 2) {
            assert tdbHits.size() >= 2;
            final Date tDBhitDate = TopHitBeanUtility.getLatestDate(tdbHits);
            final ExternalDbLink tDBTopHit = BlastMultipleUtility.getLatestMatch(tdbHits, tDBhitDate);
            //System.out.println("Top hit details: "+tDBTopHit.getDetails());
            thb.setDateTDBHit(TopHitBeanUtility.getValueFromDetails(tDBTopHit.getDetails(), 1));
            thb.setHitIDTDB(tDBTopHit.getAccessionNumber());
            thb.setExpectTDB(Double.parseDouble(TopHitBeanUtility.getValueFromDetails(tDBTopHit.getDetails(),
                2)));
            final Double numIds2 =
                Double.parseDouble(TopHitBeanUtility.getValueFromDetails(tDBTopHit.getDetails(), 3));
            thb.setNumIdentitiesTDB(numIds2);
            thb.setSimTDB(numIds2 * 100 / thb.getTargetSeqLen());
            //System.out.println("The identity is: "+thb.getSimTDB());
            //thb.setSimTDB(Double.parseDouble(getValueFromDetails(tDBTopHit.getDetails(),3)));
            if (thb.getSimTDB() > 50.0) {
                thb.setIsGt50(true);
            } else if (thb.getSimTDB() <= 50.0) {
                thb.setIsGt50(false);
            }
            tdbStatus = TopHitBeanUtility.getValueFromDetails(tDBTopHit.getDetails(), 4);
            thb.setTargetDBStatus(tdbStatus);
            thb.setIsCrystallizedOrLater(TopHitBeanUtility.crystallizedOrLater(tdbStatus.trim()));

            //now find if the next latest DbRef is the same as latest hit
            tdbHits.remove(tDBTopHit);
            final Date nextTDBDate = TopHitBeanUtility.getLatestDate(tdbHits);
            for (final ExternalDbLink tdb : tdbHits) {
                final String testTDBCode = tdb.getAccessionNumber();
                final String nextDate = TopHitBeanUtility.getValueFromDetails(tdb.getDetails(), 1);
                final Date tdbrDate = BlastMultipleUtility.makeDate(tdb.getDetails());
                if (testTDBCode.equalsIgnoreCase(tDBTopHit.getAccessionNumber())) {
                    //check this is the next latest top hit
                    if (tdbrDate.equals(nextTDBDate)) {
                        thb.setIsSameAsPreviousTDB(true);
                        thb.setTdbHitSince(nextDate);
                        //break;
                    }
                } else {
                    thb.setIsSameAsPreviousTDB(false);
                }
            }

        }
    }

    /**
     * @param target
     * @param thb
     * @param pdbHits
     * @throws NumberFormatException
     * @throws ParseException
     * @throws AccessException
     * @throws ConstraintException
     */
    public static void populatePDBTHB(final Target target, final TopHitBean thb,
        final List<ExternalDbLink> pdbHits) throws ParseException, AccessException, ConstraintException {
        if (pdbHits.size() == 1) {
            assert pdbHits.size() == 1;
            thb.setDatePDBHit(TopHitBeanUtility.getValueFromDetails(pdbHits.get(0).getDetails(), 1));
            thb.setHitIDPDB(pdbHits.get(0).getAccessionNumber());
            thb.setExpectPDB(Double.parseDouble(TopHitBeanUtility.getValueFromDetails(pdbHits.get(0)
                .getDetails(), 2)));
            final Double numIds =
                Double.parseDouble(TopHitBeanUtility.getValueFromDetails(pdbHits.get(0).getDetails(), 3));
            thb.setNumIdentitiesPDB(numIds);
            //System.out.println("The number of identities is: "+numIds);
            thb.setSimPDB(numIds * 100 / thb.getTargetSeqLen());
            //System.out.println("The similarity is:"+  thb.getSimPDB());
            //thb.setSimPDB(Double.parseDouble(getValueFromDetails(pdbHits.get(0).getDetails(),3)));
            thb.setIsSolvedLocally(TopHitBeanUtility.milestoneInPDB(target));
            thb.setIsSameAsPrevious(false);
        }

        else if (pdbHits.size() >= 2) {
            assert pdbHits.size() >= 2;

            final Date hitDate = TopHitBeanUtility.getLatestDate(pdbHits);
            final ExternalDbLink pDBTopHit = BlastMultipleUtility.getLatestMatch(pdbHits, hitDate);
            //System.out.println("Top hit details: "+pDBTopHit.getDetails());
            final String topDate = TopHitBeanUtility.getValueFromDetails(pDBTopHit.getDetails(), 1);
            final String topCode = pDBTopHit.getAccessionNumber();
            thb.setDatePDBHit(topDate);
            thb.setHitIDPDB(topCode);
            thb.setExpectPDB(Double.parseDouble(TopHitBeanUtility.getValueFromDetails(pDBTopHit.getDetails(),
                2)));
            final Double numIds =
                Double.parseDouble(TopHitBeanUtility.getValueFromDetails(pDBTopHit.getDetails(), 3));
            thb.setNumIdentitiesPDB(numIds);
            thb.setSimPDB(numIds * 100 / thb.getTargetSeqLen());
            //thb.setSimPDB(Double.parseDouble(getValueFromDetails(pDBTopHit.getDetails(),3)));
            thb.setIsSolvedLocally(TopHitBeanUtility.milestoneInPDB(target));

            //now find if the next latest DbRef is the same as latest hit
            pdbHits.remove(pDBTopHit);
            final Date nextLatestDate = TopHitBeanUtility.getLatestDate(pdbHits);
            for (final ExternalDbLink db : pdbHits) {
                final String testCode = db.getAccessionNumber();
                final String nextDate = TopHitBeanUtility.getValueFromDetails(db.getDetails(), 1);
                final Date dbrDate = BlastMultipleUtility.makeDate(db.getDetails());
                if (testCode.equalsIgnoreCase(topCode)) {
                    //check this is the next latest top hit
                    if (dbrDate.equals(nextLatestDate)) {
                        thb.setIsSameAsPrevious(true);
                        thb.setPdbHitSince(nextDate);
                        //break;
                    }
                } else {
                    thb.setIsSameAsPrevious(false);
                }
            }

        }
    }

    /**
     * method to find the two latest tophitDbRefs in a list
     * 
     * @param pdbHits
     * @return
     */
    public static ArrayList<ExternalDbLink> getTopTwo(final List<ExternalDbLink> hitList) {
        final ArrayList<ExternalDbLink> twoLatest = new java.util.ArrayList<ExternalDbLink>();
        //        DbRef[] twoOnly = new DbRef[2];

        final List<Date> dates = new java.util.ArrayList();
        if (hitList.size() >= 2) {
            final ExternalDbLink[] twoOnly = new ExternalDbLink[2];
            //Find the latest tophit date
            try {
                for (final ExternalDbLink dbref : hitList) {
                    final Date theDate = BlastMultipleUtility.makeDate(dbref.getDetails());
                    dates.add(theDate);
                }
                final List<Date> twoLatestDates = TopHitBeanUtility.getTwoLatestDates(dates);
                final Date latest = twoLatestDates.get(0);

                //use latest date to find matching TopHitDbRef
                for (final ExternalDbLink dbr : hitList) {
                    final Date refDate = BlastMultipleUtility.makeDate(dbr.getDetails());
                    if (twoLatestDates.size() == 2) {
                        final Date nextLatest = twoLatestDates.get(1);
                        if (refDate.equals(nextLatest)) {
                            twoOnly[1] = dbr;
                        }
                    }

                    if (refDate.equals(latest)) {
                        //twoLatest.add(dbr);
                        twoOnly[0] = dbr;
                        break;
                    }
                }

            } catch (final ParseException e) {
                throw new RuntimeException(e);
            }
            for (final ExternalDbLink hits : twoOnly) {
                if (hits != null) {
                    twoLatest.add(hits);
                }
            }
        }
        return twoLatest;
    }

    /**
     * @param dates
     * @return
     */
    private static ArrayList<Date> getTwoLatestDates(final List<Date> dates) {
        final ArrayList<Date> latestDates = new java.util.ArrayList<Date>();
        final List<Date> nextDates = new java.util.ArrayList();

        //get the latest date
        final Date latest = BlastMultipleUtility.getLatestDate(dates);
        latestDates.add(0, latest);

        //remove all dates which are latest, and find next latest
        for (final Date d : dates) {
            if (!d.equals(latest)) {
                nextDates.add(d);
            }
        }
        if (nextDates.size() > 0) {
            final Date nextLatest = BlastMultipleUtility.getLatestDate(nextDates);
            latestDates.add(nextLatest);
        }
        return latestDates;
    }

    /**
     * @param tdbStatus
     * @return
     */
    public static Boolean crystallizedOrLater(final String tdbStatus) {
        boolean crystOrLater = false;

        final String[] tdbStatusArray =
            { "Crystallized", "Diffraction-quality Crystals", "Diffraction", "NMR Assigned", "HSQC",
                "Crystal Structure", "NMR Structure", "In PDB" };
        final List<String> tbdStatusList = new java.util.ArrayList();
        for (final String status : tdbStatusArray) {
            tbdStatusList.add(status.trim());
        }
        assert tbdStatusList.size() == tdbStatusArray.length;

        if (tbdStatusList.contains(tdbStatus)) {
            crystOrLater = true;
        }
        return crystOrLater;
    }

    /**
     * method to check if Target has milestone 'inPDB' returns true if it does
     * 
     * @param target
     * @return
     */
    public static Boolean milestoneInPDB(final Target target) {
        final Collection<Milestone> milestones = target.getMilestones();
        boolean inPDB = false;
        for (final Milestone ms : milestones) {
            final String msName = ms.getStatus().getName();
            if (msName.contains("In PDB")) {
                inPDB = true;
            }
        }
        return inPDB;
    }

    /**
     * method to retrieve values for date, expect, similarity and details from DbRef details field
     * 
     * @param details = the details field from the topHit DbRef
     * @param field = the element required
     * @return value = value of the field
     */
    public static String getValueFromDetails(final String details, final int field) {
        String value = "";
        if (!"".equals(details)) {
            final String[] detailBits = details.split("\\*");
            if (field == 1) {
                //this is the date
                value = detailBits[1].substring(5).trim();
            } else if (field == 2) {
                //this is the Expect
                value = detailBits[2].substring(8).trim();
            } else if (field == 3) {
                //this is the similarity %
                value = detailBits[3].substring(12).trim();
            } else if (field == 4) {
                //this is the details
                value = detailBits[4].substring(12).trim();
            }
        }
        return value;
    }

    /**
     * method to make details string for the bean
     * 
     * @param pName = protein name
     * @param org = organism name
     * @param proj = project short name
     * @return concatenated string TODO project is no longer used
     */
    public static String makeDetails(final String pName, final String org, final String proj) {
        String details = "";
        if (!"".equals(pName) && (!"".equals(org) || !"".equals(proj))) {
            details = pName + " - ";
        } else {
            details = pName;
        }

        if (!"".equals(org) && !"".equals(proj)) {
            details = details + org + " - " + proj;
        } else {
            details = details + org + proj;
        }

        return details;
    }

    /**
     * method to find TopHits for particualr DBName
     */
    public static List<ExternalDbLink> findRefsForDbName(final List<ExternalDbLink> refList, final String name) {
        final List<ExternalDbLink> matchingRefs = new java.util.ArrayList();
        for (final ExternalDbLink dbr : refList) {
            if (dbr.getDatabaseName().getName().equalsIgnoreCase(name)) {
                matchingRefs.add(dbr);
            }
        }
        return matchingRefs;
    }

    /**
     * @param listOfRefs
     * @return
     */
    //TODO make this Calendar
    public static Date getLatestDate(final List<ExternalDbLink> listOfRefs) {
        Date latestDate = null;
        final List<Date> dates = new java.util.ArrayList();
        if (listOfRefs.size() > 0) {
            for (final ExternalDbLink dbref : listOfRefs) {
                final String theDetails = dbref.getDetails();
                Date theDate;
                try {
                    theDate = BlastMultipleUtility.makeDate(theDetails);
                } catch (final ParseException e) {
                    throw new RuntimeException(e);
                }
                dates.add(theDate);
            }
            latestDate = BlastMultipleUtility.getLatestDate(dates);
        }
        return latestDate;
    }
}
