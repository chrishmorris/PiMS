package org.pimslims.bioinf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.reference.Database;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.bioinf.BlastMultiple;
import org.pimslims.presentation.bioinf.BlastMultipleUtility;
import org.pimslims.properties.PropertyGetter;
import org.pimslims.test.POJOFactory;

/**
 * BlastMultipleTester
 * 
 * @author Susy Griffiths YSBL September 07
 */

public class BlastMultipleTester extends TestCase {

    private final ModelImpl model;

    /**
     * @param name
     */
    public BlastMultipleTester(final String name) {
        super(name);
        this.model = (ModelImpl) ModelImpl.getModel();
        PropertyGetter.setProxySetting();
    }

    private static final String NO_RESULT = "";

    private static final String TESTDETAILS = "PDB top hit:: *date=24/09/2007 *e-value=1.0E-155 "
        + "*%similarity=96.0 *description=mol:protein length:261 Cytochrome c oxidase subunit 3";

    private static final String TESTDETAILS2 = "PDB top hit:: *date=23/09/2007 *e-value=1.0E-155 "
        + "*%similarity=96.0 *description=mol:protein length:261 Cytochrome c oxidase subunit 3";

    private static final String TESTDETAILS3 = "PDB top hit:: *date=22/09/2007 *e-value=1.0E-155 "
        + "*%similarity=96.0 *description=mol:protein length:261 Cytochrome c oxidase subunit 3";

    private static final String BADDETAILS = "yadayadayada";

    private static final String THEDETAILS = "PDB top hit:: *date=25/09/2007 *e-value=1.0E-155 "
        + "*%similarity=96.0 *description=mol:protein length:261 Cytochrome c oxidase subunit 3";

    private static final String PDB = "PDB";

    private static final String TESTCODE = "2REL";

    //private static final String EMAIL = "pims-defects@dl.ac.uk";

    //private static final String PROTEINSEQ = "QWERTYQWERTYQWERTYQWERTY";

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(BlastMultipleTester.class);
    }

    public void testGetDate() {
        final Date datevalue = new Date(0);
        Assert.assertEquals("1970-01-01 01:00:00", BioinfUtility.getDate(datevalue));
    }

    /*
     * Test method for
     * 'org.pimslims.bioinf.BlastMultiple.allTargets(ReadableVersion)'
     */
    public void testAllTargets() {
        final ReadableVersion rv = this.model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final List ats = BlastMultiple.allTargets(rv);
            Assert.assertNotNull(ats);

            /* final List twentyTargets = ats.subList(0, 20);
             int k = 0;
             for (final Iterator j = twentyTargets.iterator(); j.hasNext();) {
                 final Target target = (Target) j.next();
                 k += 1;
                 // System.out.println(k +" Target: " + target.get_Hook());
             } */
        } finally {
            rv.abort();
        }
    }

    /*
     * Test method for
     * 'org.pimslims.bioinf.BlastMultiple.makeBlastDbRefMap(String blastResult)'
     */
    public void testMakeBlastDbRefMap() {
        // System.out.println("******testMakeBlastDbRefMap");
        final Map refMap = BlastMultipleUtility.makeBlastDbRefMap(BlastMultipleTester.xmlString);
        Assert.assertEquals((String) refMap.get("code"), "2REL_");
        Assert.assertEquals("http://www.ebi.ac.uk/pdbe-srv/view/entry/2REL", refMap.get("url"));
        Assert.assertEquals((String) refMap.get("details"), "mol:protein length:57  R-ELAFIN");
        Assert.assertEquals((String) refMap.get("name"), BlastMultipleTester.PDB);
        Assert.assertNotNull(refMap.get("date"));
        final Double expect = (Double) refMap.get("evalue");
        Assert.assertEquals(refMap.get("evalue"), expect);
        Assert.assertEquals(refMap.get("similarity"), 24);
        Assert.assertEquals(refMap.get("description"), "mol:protein length:57  R-ELAFIN");

        // test for BioinfUtility.tdbStatus(String)
        final String testDesc = "JCSG####### Work Stopped";
        final String newDesc = BioinfUtility.tdbStatus(testDesc);
        Assert.assertEquals(newDesc, "Work Stopped");
        Assert.assertEquals(refMap.get("planB"), "PDB top hit:: *date=" + refMap.get("date") + " *e-value="
            + refMap.get("evalue") + " *%similarity=24 *description=" + refMap.get("description"));
        // System.out.println("The new status is : "+newDesc);
        //test details without a status
        final String badDesc = "RSGI#######";
        final String newBadDesc = BioinfUtility.tdbStatus(badDesc);
        //System.out.println("The bad description: " + badDesc + " is now: " + newBadDesc);
        Assert.assertEquals(newBadDesc, BlastMultipleTester.NO_RESULT);

    }

    /*
     * Test method for
     * 'org.pimslims.bioinf.BlastMultiple.makeBlastDbRefMap(String blastResult)'
     * Test with sgt blast output
     */
    public void testMakeSgtBlastDbRefMap() {
        final Map sgtRefMap = BlastMultipleUtility.makeBlastDbRefMap(BlastMultipleTester.testSgtXml);
        Assert.assertEquals((String) sgtRefMap.get("name"), "TargetDB");
        Assert.assertEquals(sgtRefMap.get("description"), "Expressed");
    }

    /*
     * Test method for
     * 'org.pimslims.bioinf.BlastMultiple.makeBlastDbRefMap(String blastResult)'
     * test no Blast result
     */
    public void testCantMakeBlastDbRefMap() {
        final Map emptyRefMap = BlastMultipleUtility.makeBlastDbRefMap(BlastMultipleTester.NO_RESULT);
        assert (emptyRefMap.isEmpty());
    }

    /*
     * Test method for
     * 'org.pimslims.bioinf.BlastMultiple.createDbRefParams(WritableVersion wv,
     * Map attrMap)' createDbRefParams
     */
    public void TODOtestCreateDbRefParams() throws AccessException, ConstraintException {
        final WritableVersion rv4 = this.model.getTestVersion();

        try {
            final Map parms = BlastMultipleUtility.makeBlastDbRefMap(BlastMultipleTester.testSgtXml);
            final Map refPrms = BlastMultipleUtility.createDbRefParams(rv4, parms);
            Assert.assertNotNull(refPrms);
            assert (refPrms.size() > 0);
            Assert.assertEquals(refPrms.get("code"), "Tbru022802AAA");
            assert (refPrms.size() == 4);
            final Database dbName = (Database) refPrms.get(ExternalDbLink.PROP_DATABASE);
            Assert.assertNotNull(dbName);
            Assert.assertEquals("TargetDB", dbName.getName());
            Assert.assertTrue("The Map to update with contains the details", refPrms.get("details")
                .toString().contains("*e-value=0.041 *%similarity=22 *description=Expressed"));
        } finally {
            rv4.abort();
        }
    }

    /*
     * Test method for
     * 'org.pimslims.bioinf.BlastMultiple.createDbRefParams(WritableVersion wv,
     * Map attrMap)' createDbRefParams
     */
    public void testCantCreateDbRefParams() throws AccessException, ConstraintException {
        final ReadableVersion rv5 = this.model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final Map emptyRefPrms =
                BlastMultipleUtility.createDbRefParams(rv5,
                    BlastMultipleUtility.makeBlastDbRefMap(BlastMultipleTester.NO_RESULT));
            assert (emptyRefPrms.isEmpty());
            final Database noDbName = (Database) emptyRefPrms.get("dbName");
            Assert.assertNull(noDbName);
        } finally {
            rv5.abort();
        }
    }

    /*
     * Test method for
     * org.pimslims.bioinf.BlastMultiple.matchingDbrefs
     * TODO 040308 Test case fails
     */
    public void TODOtestFindMatchingDbRefs() throws ConstraintException {
        final Map<String, Object> dbRefPrms = new HashMap<String, Object>();
        final Map<String, Object> dbNamePrms = new HashMap<String, Object>();
        final WritableVersion rv6 = this.model.getTestVersion();
        try {
            //final Target target = getOneTarget(rv6);
            final Target target = BlastMultipleTester.makeTarget(rv6);

            final String aCode = "2EIN_P";
            final String aName = BlastMultipleTester.PDB;

            // Get the matching DbName for PDB and add to the search map
            dbNamePrms.put(Database.PROP_NAME, aName);
            final List allDbNames = (List) rv6.findAll(Database.class, dbNamePrms);
            if (1 == allDbNames.size()) {
                final ModelObject modelObject = (ModelObject) allDbNames.get(0);
                dbRefPrms.put(ExternalDbLink.PROP_DATABASE, modelObject);
            }
            dbRefPrms.put(ExternalDbLink.PROP_ACCESSION_NUMBER, aCode);
            dbRefPrms.put(Attachment.PROP_PARENTENTRY, target);
            //Search for matching DbRefs for the target
            final List refMatches = BlastMultipleUtility.matchingDbRefs(rv6, dbRefPrms);
            Assert.assertTrue("no matching DbRefs", refMatches.size() == 0);

            //Now make a DbRef and add to Target and retry
            final ExternalDbLink dbRef = new ExternalDbLink(rv6, (Database) allDbNames.get(0), target);
            dbRef.setAccessionNumber(aCode);
            final List refMatches2 = BlastMultipleUtility.matchingDbRefs(rv6, dbRefPrms);
            Assert.assertTrue("Matching DbRefs", refMatches2.size() == 1);
        } finally {
            rv6.abort();
        }
    }

    /*
     * Test method for
     */
    public void testUpdateBlastDbRefs() throws ConstraintException, AccessException, ClassNotFoundException {

        final WritableVersion WV = this.model.getTestVersion();

        //List to add DbRefs to
        //      final List<DbRef> refList = new java.util.ArrayList();
        final List<ExternalDbLink> noDbRefs = new java.util.ArrayList();
        try {
            //DbName to use
            final Database dbName = BlastMultipleTester.makeDbName(WV);

            //make DbRefs to match Blast xml top hit, one which doesn't and add to List
            final ExternalDbLink notTopHit = POJOFactory.create(WV, ExternalDbLink.class);
            notTopHit.setDetails(BlastMultipleTester.BADDETAILS);
            notTopHit.setAccessionNumber(BlastMultipleTester.TESTCODE);
            notTopHit.setDatabaseName(dbName);
            //refList.add(notTopHit);

            final ExternalDbLink ref1 = POJOFactory.create(WV, ExternalDbLink.class);
            ref1.setDetails(BlastMultipleTester.TESTDETAILS);
            ref1.setAccessionNumber(BlastMultipleTester.TESTCODE);
            ref1.setDatabaseName(dbName);
            //refList.add(ref1);

            final ExternalDbLink ref2 = POJOFactory.create(WV, ExternalDbLink.class);
            ref2.setDetails(BlastMultipleTester.TESTDETAILS2);
            ref2.setAccessionNumber(BlastMultipleTester.TESTCODE);
            ref2.setDatabaseName(dbName);
            //refList.add(ref2);

            final ExternalDbLink ref3 = POJOFactory.create(WV, ExternalDbLink.class);
            //ref3.setDetails(TESTDETAILS3);
            ref3.setDetails(BlastMultipleTester.THEDETAILS);
            ref3.setAccessionNumber(BlastMultipleTester.TESTCODE);
            ref3.setDatabaseName(dbName);
            //refList.add(ref3);

            //try{
            final Target targToUse = BlastMultipleTester.makeTarget(WV);

            //first test no matching topHits -DbRef matches but NOT a topHit
            noDbRefs.add(notTopHit);
            noDbRefs.add(ref1);
            noDbRefs.add(ref2);
            noDbRefs.add(ref3);
            noDbRefs.add(ref2);
            noDbRefs.add(ref3);
            //Map from Blast result to create DbRef
            final HashMap<String, Object> blastMap = new HashMap<String, Object>();
            blastMap.put(ExternalDbLink.PROP_DATABASE, dbName);
            blastMap.put(ExternalDbLink.PROP_ACCESSION_NUMBER, BlastMultipleTester.TESTCODE);
            blastMap.put(LabBookEntry.PROP_DETAILS, BlastMultipleTester.THEDETAILS);
            blastMap.put(Attachment.PROP_PARENTENTRY, targToUse);
            BlastMultipleUtility.updateBlastRefs(WV, noDbRefs, blastMap);

        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } catch (final ClassNotFoundException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            WV.abort();
        }

    }

    /*
     * Test method for BlastMultiple.updateBlastDbrefs
     * Test processing of target with one DbRefs, not a TopHit
     * Expect new DbRef
     */
    public void testUpdateBlastDbRefsNoMatch() throws ConstraintException, AccessException,
        ClassNotFoundException {

        final WritableVersion WV = this.model.getTestVersion();
        List<ExternalDbLink> noTopHits = new java.util.ArrayList();
        final Target targToUse = BlastMultipleTester.makeTarget(WV);

        try {
            noTopHits = BlastMultipleTester.makeDbRefList(WV).subList(0, 1);
            assert (noTopHits.size() == 1);
            for (final ExternalDbLink th : noTopHits) {
                th.setParentEntry(targToUse);
            }
            //DbName to use for Blast result
            final Database dbName = BlastMultipleTester.makeDbName(WV);

            //Map from Blast result to create DbRef
            final HashMap<String, Object> blastMap = new HashMap<String, Object>();
            blastMap.put(ExternalDbLink.PROP_DATABASE, dbName);
            blastMap.put(ExternalDbLink.PROP_ACCESSION_NUMBER, BlastMultipleTester.TESTCODE);
            blastMap.put(LabBookEntry.PROP_DETAILS, BlastMultipleTester.THEDETAILS);
            blastMap.put(Attachment.PROP_PARENTENTRY, targToUse);
            BlastMultipleUtility.updateBlastRefs(WV, noTopHits, blastMap);
            Assert.assertEquals("Target should have 2 DbRefs now", 2, targToUse.getExternalDbLinks().size());
            final List<ExternalDbLink> refs = new java.util.ArrayList();
            for (final ExternalDbLink el : targToUse.getExternalDbLinks()) {
                refs.add(el);
            }
            Assert.assertEquals("Target should only have 1 tophit", 1, BlastMultipleUtility.getTopHits(refs)
                .size());

        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } catch (final ConstraintException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } catch (final ClassNotFoundException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            WV.abort();
        }
    }

    /*
     * Test method for BlastMultiple.updateBlastDbrefs
     * Test processing of target with two DbRefs, 1 is TopHit
     */
    public void testUpdateBlastDbRefsOneMatch() throws ConstraintException, AccessException,
        ClassNotFoundException {

        final WritableVersion WV = this.model.getTestVersion();
        List<ExternalDbLink> oneTopHit = new java.util.ArrayList();
        final Target targ2ToUse = BlastMultipleTester.makeTarget(WV);
        final Database dbName2 = BlastMultipleTester.makeDbName(WV);
        try {
            oneTopHit = BlastMultipleTester.makeDbRefList(WV).subList(0, 2);
            assert (oneTopHit.size() == 2); //Target should have 2 ExternalDbLinks
            for (final ExternalDbLink th : oneTopHit) {
                th.setParentEntry(targ2ToUse);
                th.setDatabaseName(dbName2);
            }

            //Map from Blast result to create DbRef
            final HashMap<String, Object> blastMap2 = new HashMap<String, Object>();
            blastMap2.put(ExternalDbLink.PROP_DATABASE, dbName2);
            blastMap2.put(ExternalDbLink.PROP_ACCESSION_NUMBER, BlastMultipleTester.TESTCODE);
            blastMap2.put(LabBookEntry.PROP_DETAILS, BlastMultipleTester.THEDETAILS);
            blastMap2.put(Attachment.PROP_PARENTENTRY, targ2ToUse);
            BlastMultipleUtility.updateBlastRefs(WV, oneTopHit, blastMap2);
            Assert.assertEquals("Target should now have 3 ExternalDbLinks", 3, targ2ToUse
                .getExternalDbLinks().size());
            //System.out.println("DbRefs num is " + targ2ToUse.getExternalDbLinks().size());
            final List<ExternalDbLink> refs = new java.util.ArrayList();
            for (final ExternalDbLink el : targ2ToUse.getExternalDbLinks()) {
                refs.add(el);
            }
            Assert.assertEquals("Target should have 2 tophits", 2, BlastMultipleUtility.getTopHits(refs)
                .size());

        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } catch (final ClassNotFoundException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            WV.abort();
        }
    }

    /*
     * Test method for BlastMultiple.updateBlastDbrefs
     * Test processing of target with two matching DbRefs, 2 are TopHits from same Db
     * expect latest to be updated NOT a new DbRef
     */
    public void testUpdateBlastDbRefsTwoMatch() throws ConstraintException, AccessException,
        ClassNotFoundException, ParseException {

        final WritableVersion WV = this.model.getTestVersion();
        List<ExternalDbLink> twoTopHits = new java.util.ArrayList();
        //DbName to use
        final Database dbName3 = BlastMultipleTester.makeDbName(WV);
        final Target targ3ToUse = BlastMultipleTester.makeTarget(WV);
        try {
            twoTopHits = BlastMultipleTester.makeDbRefList(WV).subList(0, 3);
            assert (twoTopHits.size() == 3); //Target starts with 3 ExternalDbLinks
            for (final ExternalDbLink th : twoTopHits) {
                th.setParentEntry(targ3ToUse);
                th.setDatabaseName(dbName3);
            }

            //Map from Blast result to create DbRef
            final HashMap<String, Object> blastMap3 = new HashMap<String, Object>();
            blastMap3.put(ExternalDbLink.PROP_DATABASE, dbName3);
            blastMap3.put(ExternalDbLink.PROP_ACCESSION_NUMBER, BlastMultipleTester.TESTCODE);
            blastMap3.put(LabBookEntry.PROP_DETAILS, BlastMultipleTester.THEDETAILS);
            blastMap3.put(Attachment.PROP_PARENTENTRY, targ3ToUse);
            BlastMultipleUtility.updateBlastRefs(WV, twoTopHits, blastMap3);
            Assert.assertEquals("Target should still have 3 ExternalDbLinks", 3, targ3ToUse
                .getExternalDbLinks().size());
            final List<ExternalDbLink> refs = new java.util.ArrayList();
            for (final ExternalDbLink el : targ3ToUse.getExternalDbLinks()) {
                refs.add(el);
            }

            final List<ExternalDbLink> targTopHits = BlastMultipleUtility.getTopHits(refs);
            Assert.assertEquals("Target should have 2 tophits", 2, targTopHits.size());
            final Date latestDate = BlastMultipleUtility.makeDate(BlastMultipleTester.THEDETAILS);
            final ExternalDbLink updated = BlastMultipleUtility.getLatestMatch(targTopHits, latestDate);
            //System.out.println("Details for updated is: " + updated.getDetails());
            Assert.assertEquals("Latest DbRef should have been updated with THEDETAILS",
                BlastMultipleTester.THEDETAILS, updated.getDetails());

        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } catch (final ClassNotFoundException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            WV.abort();
        }
    }

    /*
     * Test method for
     * 'org.pimslims.bioinf.BlastMultiple.makeBlastDbRef(WritableVersion wv, Map
     * attrMap, Target target)' Use DbRefParams to create Db REf then add Target
     */
    public void testMakeBlastDbRef() throws AccessException, ConstraintException, ClassNotFoundException {
        //final ReadableVersion rv5 = MODEL.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR); // final
        final HashMap<String, Object> refPrms2 = new HashMap<String, Object>();
        final WritableVersion wv2 = this.model.getTestVersion();
        try {
            final Target target = BlastMultipleTester.makeTarget(wv2);
            // a DbName for testing
            final Database dbName = POJOFactory.createDbName(wv2);
            final String dbn = "name" + new Date();
            dbName.setName(dbn);
            Assert.assertEquals("Check the dbName name", dbName.get_Name(), dbn);
            refPrms2.put(ExternalDbLink.PROP_DATABASE, dbName);
            refPrms2.put(Attachment.PROP_PARENTENTRY, target);
            //250510 -processing details
            refPrms2.put(Attachment.PROP_DETAILS, BlastMultipleTester.TESTDETAILS);
            //refPrms2.put("details", BlastMultipleTester.TESTDETAILS);
            // make the Dbref
            final ExternalDbLink dbRef = (ExternalDbLink) BlastMultipleUtility.makeBlastDbRef(wv2, refPrms2);
            //BlastMultiple.makeBlastDbRef(wv2, rv5, refPrms2, target);
            Assert.assertNotNull("Check DbRef is made", dbRef);
            Assert.assertEquals("dbRef name is set", dbRef.getDatabaseName().getName(), dbName.getName());
            Assert.assertEquals(dbName, dbRef.getDatabaseName());
            Assert.assertTrue(dbName.getDbRefs().contains(dbRef));
            Assert.assertEquals("Target is set", dbRef.getParentEntry(), target);
            Assert.assertEquals("The details have been set", dbRef.getDetails(),
                BlastMultipleTester.TESTDETAILS);

        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            wv2.abort();
        }
    }

    /*
     * Test method for
     * 'org.pimslims.bioinf.BlastMultiple.makeBlastDbRef(WritableVersion wv, Map
     * attrMap, Target target)' Use DbRefParams to create Db REf then add Target
     */
    public void testCantMakeBlastDbRef() throws AccessException, ConstraintException, ClassNotFoundException {
        final HashMap<String, Object> refPrms3 = new HashMap<String, Object>();
        final WritableVersion wv3 = this.model.getTestVersion();
        try {
            BlastMultipleTester.makeTarget(wv3);
            // make the Dbref
            final ExternalDbLink bdbr2 = (ExternalDbLink) BlastMultipleUtility.makeBlastDbRef(wv3, refPrms3);
            // BlastMultiple.makeBlastDbRef(wv3, rv6, refPrms3, target );
            Assert.assertNull("Check DbRef is not made", bdbr2);

        } catch (final AccessException e) {
            Assert.fail(e.getMessage());

        } catch (final ConstraintException e) {
            // thats OK
        } finally {
            wv3.abort();
        }

    }

    /*
     * Test method for
     * 'org.pimslims.bioinf.BlastMultiple.makeDate(String)'
     */
    public void testMakeDate() throws ParseException {
        //      final String testDetails = "PDB top hit:: *date=24/09/2007 *e-value=1.0E-155 " +
        //      "*%similarity=96.0 *description=mol:protein length:261 Cytochrome c oxidase subunit 3";
        final Date compDate = new SimpleDateFormat("dd/MM/yyyy").parse("24/09/2007");
        try {
            final Date testDate = BlastMultipleUtility.makeDate(BlastMultipleTester.TESTDETAILS);
            Assert.assertEquals(testDate.toString(), compDate.toString());
            final Date badDate = BlastMultipleUtility.makeDate(BlastMultipleTester.BADDETAILS);
            Assert.assertNull("can't make a date from wrong string", badDate);
        } catch (final ParseException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    /*
     * Test method for
     * 'org.pimslims.bioinf.BlastMultiple.getTopHits(List<DbRef>)'
     */
    public void testGetTopHits() {
        final WritableVersion version = this.model.getTestVersion();
        final Map<String, Object> noPrms = new HashMap<String, Object>();
        final String noCode = "No matching code";
        noPrms.put("code", noCode);

        try {
            //final List noHits = (List) version.findAll(DbRef.class, noPrms); -needs a db with DbRefs in
            //make an empty List
            final List<ExternalDbLink> noHits = new java.util.ArrayList<ExternalDbLink>();
            Assert.assertTrue("There are no matching DbRefs", noHits.size() == 0);
            final List noTopHits = BlastMultipleUtility.getTopHits(noHits);
            Assert.assertTrue("There are no TopHits", noTopHits.size() == 0);
            //final List allDbRefs = (List) version.getAll(DbRef.class);
            final List someDbRefs = BlastMultipleTester.makeDbRefList(version);

            if (someDbRefs.size() > 0) {
                final List topHitList = BlastMultipleUtility.getTopHits(someDbRefs);
                if (topHitList.size() > 0) {
                    final ExternalDbLink ref1 = (ExternalDbLink) topHitList.get(0);
                    Assert.assertTrue("DB contains some tophits", ref1.getDetails().contains("top hit::"));
                } else {
                    Assert.assertNull("No tophits in DB", topHitList);
                }
            }
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            version.abort();
        }

    }

    /*
     * Test method for
     * 'org.pimslims.bioinf.BlastMultiple.getLatestDate(List<Date>)'
     */
    public void testGetLatestDate() {
        final List<Date> testDates = new java.util.ArrayList();
        final Date noDate = BlastMultipleUtility.getLatestDate(testDates);
        Assert.assertNull("Can't get latest from empty list", noDate);
        try {
            final Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse("24/09/2007");
            final Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse("25/09/2007");
            final Date date3 = new SimpleDateFormat("dd/MM/yyyy").parse("26/09/2007");
            final Date date4 = new SimpleDateFormat("dd/MM/yyyy").parse("22/09/2007");
            testDates.add(date1);
            testDates.add(date2);
            testDates.add(date3);
            testDates.add(date4);
            final Date latest = BlastMultipleUtility.getLatestDate(testDates);
            Assert.assertEquals("The latest date is 26/09/2007", latest, date3);

        } catch (final ParseException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /*
     * Test method for
     * 'org.pimslims.bioinf.BlastMultiple.getLatestMatch(List<DbRef>)'
     */
    public void testGetLatestMatch() {
        final WritableVersion wv8 = this.model.getTestVersion();

        try {
            final List someDbRefs2 = new java.util.ArrayList();
            final Database dbname = BlastMultipleTester.makeDbName(wv8);
            final ExternalDbLink dbr1 = POJOFactory.create(wv8, ExternalDbLink.class);
            dbr1.setDetails(BlastMultipleTester.TESTDETAILS);
            dbr1.setAccessionNumber("code1");
            dbr1.setDatabaseName(dbname);
            someDbRefs2.add(dbr1);
            final ExternalDbLink dbr2 = POJOFactory.create(wv8, ExternalDbLink.class);
            dbr2.setDetails(BlastMultipleTester.TESTDETAILS2);
            dbr2.setAccessionNumber("code2");
            dbr2.setDatabaseName(dbname);
            someDbRefs2.add(dbr2);
            final ExternalDbLink dbr3 = POJOFactory.create(wv8, ExternalDbLink.class);
            dbr3.setDetails(BlastMultipleTester.TESTDETAILS3);
            dbr3.setAccessionNumber("code3");
            dbr3.setDatabaseName(dbname);
            someDbRefs2.add(dbr3);
            final ExternalDbLink dbr4 = POJOFactory.create(wv8, ExternalDbLink.class);
            dbr4.setDetails(BlastMultipleTester.TESTDETAILS);
            dbr4.setAccessionNumber("code4");
            dbr4.setDatabaseName(dbname);
            someDbRefs2.add(dbr4);
            final Date latestDate = new SimpleDateFormat("dd/MM/yyyy").parse("24/09/2007");
            //no PiMS records with this date
            final Date impossibleDate = new SimpleDateFormat("dd/MM/yyyy").parse("24/09/1985");

            final ExternalDbLink noRef = BlastMultipleUtility.getLatestMatch(someDbRefs2, impossibleDate);
            Assert.assertNull("The latest matching DbRef", noRef);
            //System.out.println("The dbref is: "+noRef.getStatus());

            final ExternalDbLink aRef = BlastMultipleUtility.getLatestMatch(someDbRefs2, latestDate);
            Assert.assertNotNull("The latest matching DbRef", aRef);
            //System.out.println("The dbref is: "+aRef.getStatus());

        } catch (final ParseException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            wv8.abort();
        }

    }

    /*
     * method to test BlastMultiple.updateLatestMatch
     */
    public void testUpdateLatestMatch() {
        final WritableVersion WV = this.model.getTestVersion();
        List<ExternalDbLink> matchingHits = new java.util.ArrayList();
        final List<ExternalDbLink> noHits = new java.util.ArrayList();

        try {
            //List of Dbrefs to test
            matchingHits = BlastMultipleTester.makeDbRefList(WV).subList(1, 4);
            //matchingHits = makeDbRefList().subList(1,4);

            /*TESTING May 2010
            List<ExternalDbLink> testHits = new java.util.ArrayList();
            testHits = BlastMultipleTester.makeDbRefList(WV);
            int j = 1;
            System.out.println("***TESTING All the hits:");
            for (final ExternalDbLink dbr : testHits) {
                System.out.println(j + dbr.getDetails());
                j++;
            }

            //END
            */
            assert matchingHits.size() == 3;
            /*TESTING May 2010
            int i = 1;
            System.out.println("Testing Update latest match:");
            for (final ExternalDbLink dbr : matchingHits) {
                System.out.println(i + dbr.getDetails());
                i++;
            }
            END */
            //DbName for the Blast Map
            final Database dbNAME = BlastMultipleTester.makeDbName(WV);

            //Map from Blast result to create DbRef
            final HashMap<String, Object> blastMAP = new HashMap<String, Object>();
            blastMAP.put(ExternalDbLink.PROP_DATABASE, dbNAME);
            blastMAP.put(ExternalDbLink.PROP_ACCESSION_NUMBER, BlastMultipleTester.TESTCODE);
            blastMAP.put(LabBookEntry.PROP_DETAILS, BlastMultipleTester.THEDETAILS);

            //Date for latest date
            final Date theLatestDate = new SimpleDateFormat("dd/MM/yyyy").parse("24/09/2007");

            final ExternalDbLink updated =
                BlastMultipleUtility.updateLatestMatch(matchingHits, blastMAP, theLatestDate);
            Assert.assertEquals("The DbRef has been updated", updated.getDetails(),
                BlastMultipleTester.THEDETAILS);
            //System.out.println("The details are: " + updated.getDetails());

            final ExternalDbLink notUpdated =
                BlastMultipleUtility.updateLatestMatch(noHits, blastMAP, theLatestDate);
            Assert.assertNull("No matching DbRefs", notUpdated);

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } catch (final ParseException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            WV.abort();
        }

    }

    /*
     * method to make a target for use in testing
     */
    static int targetCount = 0;

    public static Target makeTarget(final WritableVersion version) throws ConstraintException {
        final Target target = POJOFactory.createTarget(version);
        final String commonName = "target" + BlastMultipleTester.targetCount + new Date();
        BlastMultipleTester.targetCount++;
        target.setName(commonName);
        Assert.assertEquals("Check the target name", target.get_Name(), commonName);
        return target;

    }

    private static int dbn = 0;

    /*
     * method to make a DbName for use in testing
     */
    public static synchronized Database makeDbName(final WritableVersion WV) throws ConstraintException {
        final Database dbName = POJOFactory.createDbName(WV);
        final String name = "dbname" + (BlastMultipleTester.dbn++);
        dbName.setName(name + System.currentTimeMillis());
        return dbName;
    }

    /*
     * method for getting one target from Db
     
    private static Target getOneTarget(final ReadableVersion version) {
        final List targetList = BlastMultiple.allTargets(version);
        final List someTargets = targetList.subList(0, 1);
        final Target target = (Target) someTargets.get(0);
        return target;
    } */

    /*
     * method to make a list of DbRefs to process
     */
    private static List<ExternalDbLink> makeDbRefList(final WritableVersion version)
        throws ConstraintException, AccessException {

        final List<ExternalDbLink> listOfRefs = new java.util.ArrayList();
        final Target target = BlastMultipleTester.makeTarget(version);
        final Database dbName = BlastMultipleTester.makeDbName(version);
        final ExternalDbLink notTopHit = new ExternalDbLink(version, dbName, target);
        notTopHit.setDetails(BlastMultipleTester.BADDETAILS);
        notTopHit.setAccessionNumber(BlastMultipleTester.TESTCODE);
        listOfRefs.add(notTopHit);

        final ExternalDbLink ref1 = new ExternalDbLink(version, dbName, target);
        ref1.setDetails(BlastMultipleTester.TESTDETAILS);
        ref1.setAccessionNumber(BlastMultipleTester.TESTCODE);
        listOfRefs.add(ref1);

        final ExternalDbLink ref2 = new ExternalDbLink(version, dbName, target);
        ref2.setDetails(BlastMultipleTester.TESTDETAILS2);
        ref2.setAccessionNumber(BlastMultipleTester.TESTCODE);
        listOfRefs.add(ref2);

        final ExternalDbLink ref3 = new ExternalDbLink(version, dbName, target);
        ref3.setDetails(BlastMultipleTester.TESTDETAILS3);
        ref3.setAccessionNumber(BlastMultipleTester.TESTCODE);
        listOfRefs.add(ref3);

        final ExternalDbLink equalRef = new ExternalDbLink(version, dbName, target);
        equalRef.setDetails(BlastMultipleTester.THEDETAILS);
        equalRef.setAccessionNumber(BlastMultipleTester.TESTCODE);
        listOfRefs.add(equalRef);

        return listOfRefs;
    }

    private static String xmlString =
        "<?xml version=\"1.0\"?>\r\n"
            + "<EBIApplicationResult xmlns=\"http://www.ebi.ac.uk/schema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://www.ebi.ac.uk/schema/ApplicationResult.xsd\">\r\n"
            + "<Header>\r\n"
            + "	<program name=\"NCBI-blastp\" version=\"2.2.15 [Oct-15-2006]\" citation=\"PMID:9254694\"/>\r\n"
            + "	<commandLine command=\"/ebi/extserv/bin/ncbi-blast/blastall -p blastp -d $IDATA_CURRENT/blastdb/pdb -i /ebi/extserv/blast-work/interactive/blast-20070524-14405797.input -M BLOSUM62 -b 5 -v 5 -e 1.0 -X 0 -G 11 -E 1 -a 8 -L 1,132 -m 0 -gt -F F \"/>\r\n"
            + "	<parameters>\r\n"
            + "		<sequences total=\"1\">\r\n"
            + "			<sequence number=\"1\" name=\"Sequence\" type=\"p\" length=\"132\"/>\r\n"
            + "		</sequences>\r\n"
            + "		<databases total=\"1\" sequences=\"95624\" letters=\"22913533\">\r\n"
            + "			<database number=\"1\" name=\"pdb\" type=\"p\" created=\"2007-05-23T23:00:00+01:00\"/>\r\n"
            + "		</databases>\r\n"
            + "		<gapOpen>11</gapOpen>\r\n"
            + "		<gapExtension>1</gapExtension>\r\n"
            + "	</parameters>\r\n"
            + "</Header>\r\n"
            + "<SequenceSimilaritySearchResult>\r\n"
            + "	<hits total=\"1\">\r\n"
            + "		<hit number=\"1\" database=\"pdb\" id=\"2REL_\" length=\"57\" description=\"mol:protein length:57  R-ELAFIN\">\r\n"
            + "			<alignments total=\"2\">\r\n"
            + "				<alignment number=\"1\">\r\n"
            + "					<score>141</score>\r\n"
            + "					<bits>58.9</bits>\r\n"
            + "					<expectation>1e-09</expectation>\r\n"
            + "					<identity>48</identity>\r\n"
            + "					<positives>55</positives>\r\n"
            + "					<querySeq start=\"81\" end=\"129\">PTRRKPGKCPVTYGQCLMLNPPNFCEMDGQCKRDLKCCMGMCGKSCVSP</querySeq>\r\n"
            + "					<pattern>P   KPG CP+   +C MLNPPN C  D  C    KCC G CG +C  P</pattern>\r\n"
            + "					<matchSeq start=\"8\" end=\"56\">PVSTKPGSCPIILIRCAMLNPPNRCLKDTDCPGIKKCCEGSCGMACFVP</matchSeq>\r\n"
            + "				</alignment>\r\n"
            + "				<alignment number=\"2\">\r\n"
            + "					<score>88</score>\r\n"
            + "					<bits>38.5</bits>\r\n"
            + "					<expectation>0.002</expectation>\r\n"
            + "					<identity>36</identity>\r\n"
            + "					<positives>44</positives>\r\n"
            + "					<querySeq start=\"29\" end=\"75\">SFKAGVCPPKKSAQCLRYKKPECQSDWQCPGKKRCCPDTCGIKCLDP</querySeq>\r\n"
            + "					<pattern> K G CP       +      C  D  CPG K+CC  +CG+ C  P</pattern>\r\n"
            + "					<matchSeq start=\"10\" end=\"56\">STKPGSCPIILIRCAMLNPPNRCLKDTDCPGIKKCCEGSCGMACFVP</matchSeq>\r\n"
            + "				</alignment>\r\n" + "			</alignments>\r\n" + "		</hit>" + "	</hits>\r\n"
            + "</SequenceSimilaritySearchResult>\r\n" + "</EBIApplicationResult>";

    private static String testSgtXml =
        "<?xml version=\"1.0\"?>\r\n"
            + "<EBIApplicationResult xmlns=\"http://www.ebi.ac.uk/schema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://www.ebi.ac.uk/schema/ApplicationResult.xsd\">\r\n"
            + "<Header>\r\n"
            + "	<program name=\"NCBI-blastp\" version=\"2.2.15 [Oct-15-2006]\" citation=\"PMID:9254694\"/>\r\n"
            + "	<commandLine command=\"/ebi/extserv/bin/ncbi-blast/blastall -p blastp -d $IDATA_CURRENT/blastdb/sgt -i /ebi/extserv/blast-work/interactive/blast-20070524-14405797.input -M BLOSUM62 -b 5 -v 5 -e 1.0 -X 0 -G 11 -E 1 -a 8 -L 1,132 -m 0 -gt -F F \"/>\r\n"
            + "	<parameters>\r\n"
            + "		<sequences total=\"1\">\r\n"
            + "			<sequence number=\"1\" name=\"Sequence\" type=\"p\" length=\"72\"/>\r\n"
            + "		</sequences>\r\n"
            + "		<databases total=\"1\" sequences=\"140301\" letters=\"43929337\">\r\n"
            + "			<database number=\"1\" name=\"sgt\" type=\"p\" created=\"2007-08-22T23:00:00+01:00\"/>\r\n"
            + "		</databases>\r\n"
            + "		<gapOpen>11</gapOpen>\r\n"
            + "		<gapExtension>1</gapExtension>\r\n"
            + "	</parameters>\r\n"
            + "</Header>\r\n"
            + "<SequenceSimilaritySearchResult>\r\n"
            + "	<hits total=\"1\">\r\n"
            + "		<hit number=\"1\" database=\"sgt\" id=\"Tbru022802AAA___\" length=\"145\" description=\"########### Expressed\">\r\n"
            + "			<alignments total=\"1\">\r\n"
            + "				<alignment number=\"1\">\r\n"
            + "					<score>78</score>\r\n"
            + "					<bits>34.7</bits>\r\n"
            + "					<expectation>0.041</expectation>\r\n"
            + "					<identity>31</identity>\r\n"
            + "					<positives>43</positives>\r\n"
            + "					<querySeq start=\"4\" end=\"72\">ESGRRARPKDVEEELSKLPVDVSREDDEIVVKVGRGRRLPEDEFRETIAKLKRMGFKFDPDTKTWRKRS</querySeq>\r\n"
            + "					<pattern>E G R R +DVEE  S L   V+   D I   V         E  E + +L +M   F     TW+K++</pattern>\r\n"
            + "					<matchSeq start=\"17\" end=\"85\">EEGSRKRKRDVEEGCSALVSHVNSTLDVITTDVRNTLTEQHCEMEERVNRLMKMAGDFATAVDTWQKKA</matchSeq>\r\n"
            + "				</alignment>\r\n" + "			</alignments>\r\n" + "		</hit>" + "	</hits>\r\n"
            + "</SequenceSimilaritySearchResult>\r\n" + "</EBIApplicationResult>";
}
