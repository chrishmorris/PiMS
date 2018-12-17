/**
 * current-pims-web org.pimslims.bioinf BlastMultipleExtraTester.java
 * 
 * @author susy
 * @date 01-Mar-2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 susy
 * 
 * 
 * 
 */
package org.pimslims.bioinf;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.bioinf.newtarget.PIMSTarget;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.bioinf.BlastMultiple;
import org.pimslims.properties.PropertyGetter;

/**
 * BlastMultipleExtraTester Tests methods which are dependent on use of web services
 * 
 */
public class BlastMultipleExtraTester extends TestCase {

    private final ModelImpl model;

    private static final String EMAIL = "pims-defects@dl.ac.uk";

    private static final String PROTEINSEQ = "QWERTYQWERTYQWERTYQWERTY";

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(BlastMultipleExtraTester.class);
    }

    /**
     * Constructor for BlastMultipleExtraTester.
     * 
     * @param arg0
     */
    public BlastMultipleExtraTester(final String arg0) {
        super(arg0);
        this.model = (ModelImpl) ModelImpl.getModel();
        PropertyGetter.setProxySetting();

    }

    /*
     * Test method for 'org.pimslims.presentation.bioinf.BlastMultiple.blastTargetAsynch(Target, String, String)'
     */
    /*
     * Test method for 'org.pimslims.bioinf.BlastMultiple.blastTargetAsynch(Target target, String dbToUse)'
      
    public void testBlastTargetAsynch() throws ConstraintException, UnknownHostException {
        final WritableVersion wv1 = this.model.getTestVersion();
        try {
            final String db = "TargetDB";
            final Target targ = BlastMultipleTester.makeTarget(wv1);
            final String blastResult =
                BlastMultiple.blastTargetAsynch(targ, db, BlastMultipleExtraTester.EMAIL);
            Assert.assertNull(blastResult);

            //need a protein sequence
            final Molecule protein = POJOFactory.createProtein(wv1);
            protein.setSequence(BlastMultipleExtraTester.PROTEINSEQ);
            protein.setName("proteinName");
            targ.setProtein(protein);

            final String blastResult2 =
                BlastMultiple.blastTargetAsynch(targ, db, BlastMultipleExtraTester.EMAIL);
            Assert.assertNotNull(blastResult2);
            //System.out.println(blastResult2);
        } catch (final UnknownHostException e) {
            Assert.fail(e.getMessage());
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        } catch (final InterruptedException e) {
            Assert.fail(e.getMessage());
        } finally {
            wv1.abort();
        }
    } */

    /*
     * Test method for 'org.pimslims.presentation.bioinf.BlastMultiple.runAndProcessBlast(Target, String, WritableVersion, String)'
     */
    public void testRunAndProcessBlast() throws AbortedException {
        final WritableVersion wv = this.model.getTestVersion();
        //final String db2 = "TargetDB";
        final String db2 = "PDB";
        try {
            //final Target tar = getOneTarget();
            final List targs = BlastMultiple.allTargets(wv);
            Assert.assertNotNull(targs);
            final List someTargets = targs.subList(0, 1);
            for (final Iterator i = someTargets.iterator(); i.hasNext();) {
                final Target target = (Target) i.next();
                if (target.getSeqString().length() > 0 && !PIMSTarget.isDNATarget(target)) {

                    //final Set<DbRef> tdbrs = tar.getDbRefs();
                    //final Set<DbRef> tdbrs = target.getDbRefs();
                    //System.out.println("Target has "+ tdbrs.size()+ " DbRefs");
                    //org.pimslims.bioinf.BlastMultiple.runAndProcessBlast(tar, db2,  wv);
                    BlastMultiple.runAndProcessBlast(target, db2, wv, BlastMultipleExtraTester.EMAIL);
                } else {
                    System.out.println("Can't process Target " + target.get_Name());
                }
            }
            // if you wanted to save them wv.commit() here
            //wv.commit();
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (final ClassNotFoundException e) {
            Assert.fail(e.getMessage());
        } catch (final IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (final InterruptedException e) {
            Assert.fail(e.getMessage());
        } finally {

            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    /*
     * Test method for 'org.pimslims.bioinf.BlastMultiple.process20(List)'
     */
    public void testProcess20() {
        final ReadableVersion rv2 = this.model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final List alltargs = BlastMultiple.allTargets(rv2);
            Assert.assertNotNull(alltargs);

            final List someTargets = alltargs.subList(0, 2);
            // System.out.println("******testProcess20");

            org.pimslims.presentation.bioinf.BlastMultipleUtility.process20(someTargets);
        } finally {
            rv2.abort();
        }
    }

    /*
     * Test method for 'org.pimslims.bioinf.BlastMultiple.processSublist(List)'
     */
    public void testProcessSublist() {
        final ReadableVersion rv3 = this.model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final List alltargs = BlastMultiple.allTargets(rv3);
            Assert.assertNotNull(alltargs);

            final List someTargets = alltargs.subList(0, 2);
            // System.out.println("******testProcessSublist");
            org.pimslims.presentation.bioinf.BlastMultipleUtility.processSublist(someTargets);
        } finally {
            rv3.abort();
        }
    }

}
