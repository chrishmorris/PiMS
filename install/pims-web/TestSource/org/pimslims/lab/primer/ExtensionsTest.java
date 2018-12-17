/**
 * current-pims-web org.pimslims.lab.primer TagsTest.java
 * 
 * @author cm65
 * @date 17 Mar 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.lab.primer;

import java.util.Collection;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.molecule.Extension;
import org.pimslims.presentation.construct.ExtensionBean;
import org.pimslims.presentation.construct.Extensions;

/**
 * TagsTest
 * 
 */
public class ExtensionsTest extends TestCase {
    private final ModelImpl model;

    private static final String FORWARD = "Forward";

    private static final String FDIR = ExtensionsTest.FORWARD + " Extension";

    private static final String RDIR = "Reverse Extension";

    private static final String EXNAME = "Fred";

    private static final String EXSEQ = "GGTTAACC";

    /**
     * @param name
     */
    public ExtensionsTest(final String name) {
        super(name);
        // initialise properties
        this.model = (ModelImpl) ModelImpl.getModel();
    }

    /**
     * TODO Test method for {@link org.pimslims.lab.primer.Tags#getReverseTags()}.
     */

    /**
     * Test method for org.pimslims.lab.primer.Extensions#getExtensions()
     */
    public final void testGetExtensions() {
        final WritableVersion wv = this.model.getTestVersion();
        final String noDirection = "noCategory";
        final Map<String, String> fwdExtensions = Extensions.getExtensions(wv, ExtensionsTest.FDIR);
        Assert.assertTrue(0 != fwdExtensions.size());
        System.out.println("There are " + fwdExtensions.size() + " Forward Extensions");
        Assert.assertTrue(fwdExtensions.containsKey("Novagen 3C/LIC-F"));

        final Map<String, String> revExtensions = Extensions.getExtensions(wv, ExtensionsTest.RDIR);
        Assert.assertTrue(0 != revExtensions.size());
        System.out.println("There are " + revExtensions.size() + " Reverse Extensions");
        Assert.assertTrue(revExtensions.containsKey("Novagen Ek/LIC-R"));

        final Map<String, String> noExtensions = Extensions.getExtensions(wv, noDirection);
        Assert.assertTrue("Non existent component category", 0 == noExtensions.size());

    }

    /**
     * Test method for org.pimslims.lab.primer.Extensions#getExtensions()
     * 
     * @throws ConstraintException org.pimslims.exception.ConstraintException
     * @throws AccessException org.pimslims.exception.AccessException
     */
    public final void testMakeNewExtension() throws AccessException, ConstraintException {
        final WritableVersion wv = this.model.getTestVersion();
        try {
            final String newName = ExtensionsTest.EXNAME;
            final String newSeq = ExtensionsTest.EXSEQ;
            final String fdir = ExtensionsTest.FORWARD;
            final String newTag = "HHHHHH";
            final String newSite = "GAATTC";
            final Extension mobj =
                Extensions.makeNewExtension(wv, null, newName, newSeq, fdir, newTag, newSite);
            Assert.assertNotNull(mobj);
            Assert.assertNotNull(mobj.get_Hook());
            //final String yes = Extensions.makeNewExtension(wv, newName, newSeq, fdir, newTag, newSite);
            //Assert.assertTrue("New Forward Extension saved in PiMS".equals(yes));
            //Test to process when no values for Tag and Restriction enz
            final Extension mobj2 =
                Extensions.makeNewExtension(wv, null, newName + "2", newSeq, fdir, "", "");
            Assert.assertNotNull(mobj2);
            Assert.assertNotNull(mobj2.get_Hook());

            final ExtensionBean bean = new ExtensionBean(mobj);
            Assert.assertTrue(bean.getMayDelete());
        } finally {
            wv.abort(); // not testing persistence
        }
    }

    public final void testDuplicateExtension() throws AccessException, ConstraintException {
        final WritableVersion wv = this.model.getTestVersion();
        try {
            final String newName = ExtensionsTest.EXNAME;
            final String newSeq = ExtensionsTest.EXSEQ;
            final String fdir = ExtensionsTest.FORWARD;
            ModelObject mobj2 = null;
            mobj2 = Extensions.makeNewExtension(wv, null, newName, newSeq, fdir, "", "");
            Assert.assertNotNull(mobj2);
            mobj2 = Extensions.makeNewExtension(wv, null, newName, newSeq, fdir, "", "");
            Assert.fail("Made duplicate extension");
        } catch (final ConstraintException e) {
            // that's as it should be
        } finally {
            wv.abort(); // not testing persistence
        }
    }

    public final void testMakeExtensionBeans() {
        final ReadableVersion rv = this.model.getReadableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        final String direction = ExtensionsTest.FORWARD;
        final String noDirection = "nowhere";

        try {
            final Collection<ExtensionBean> exBeans = Extensions.makeExtensionBeans(rv, direction);
            Assert.assertTrue("Got some beans", exBeans.size() > 0);
            final Collection<ExtensionBean> noExBeans = Extensions.makeExtensionBeans(rv, noDirection);
            Assert.assertTrue("No beans", noExBeans.size() == 0);
        } finally {
            rv.abort(); // not testing persistence
        }
    }

    /**
     * Test method for org.pimslims.lab.primer.Extensions#save()
     * 
     * @throws ConstraintException org.pimslims.exception.ConstraintException
     * @throws AccessException org.pimslims.exception.AccessException
     */
    public final void testSaveExtensionFromBean() throws AccessException, ConstraintException {
        final WritableVersion wv = this.model.getTestVersion();
        try {
            final ExtensionBean extbean = new ExtensionBean();
            final String exhook = Extensions.save(wv, extbean);
            //Assert.assertTrue("Extension not created", exhook.isEmpty());
            Assert.assertFalse("Extension not created",
                exhook.startsWith("org.pimslims.model.molecule.Extension:"));
            extbean.setExName(ExtensionsTest.EXNAME);
            extbean.setExSeq(ExtensionsTest.EXSEQ);
            extbean.setDirection(ExtensionsTest.FORWARD);
            final String exhook2 = Extensions.save(wv, extbean);
            Assert.assertTrue("Extension created",
                exhook2.startsWith("org.pimslims.model.molecule.Extension:"));
        } finally {
            wv.abort(); // not testing persistence
        }
    }

    /**
     * Test method for org.pimslims.lab.primer.Extensions#duplicate()
     * 
     * @throws ConstraintException org.pimslims.exception.ConstraintException
     * @throws AccessException org.pimslims.exception.AccessException
     */
    public final void testDuplicate() throws AccessException, ConstraintException {
        final WritableVersion wv = this.model.getTestVersion();
        try {
            final String newName = ExtensionsTest.EXNAME;
            final String newSeq = ExtensionsTest.EXSEQ;
            final String fdir = ExtensionsTest.FORWARD;
            final String newTag = "HHHHHH";
            final String newSite = "GAATTC";
            final Extension mobj =
                Extensions.makeNewExtension(wv, null, newName, newSeq, fdir, newTag, newSite);
            Assert.assertNotNull(mobj);
            Assert.assertNotNull(mobj.get_Hook());
            final Extension duplicate = Extensions.duplicate(mobj, wv);
            Assert.assertTrue("Names are not the same", duplicate.get_Name().contains(mobj.get_Name()));
            Assert.assertFalse("The names are different", duplicate.get_Name() == mobj.getName());
            Assert.assertEquals(duplicate.getSequence(), newSeq);
            Assert.assertEquals(duplicate.getSequenceDetails(), fdir + " Extension");
            Assert.assertEquals(duplicate.getCategories(), mobj.getCategories());
            Assert.assertEquals(duplicate.getRelatedProteinTagSeq(), newTag);
        } finally {
            wv.abort(); // not testing persistence
        }
    }

}
