/**
 * current-pims-web org.pimslims.data ExtensionsUtilityTester.java
 * 
 * @author susy
 * @date 24-Apr-2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 susy
 * 
 * 
 * 
 */
package org.pimslims.data;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Extension;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ComponentCategory;

/**
 * ExtensionsUtilityTester
 * 
 */
public class ExtensionsUtilityTester extends TestCase {
    AbstractModel model = ModelImpl.getModel();

    private static final String UNIQUE = "eut" + System.currentTimeMillis();

    private static final String EXTENSIONNAME = "name" + ExtensionsUtilityTester.UNIQUE;

    private static final String EXTENSIONSEQ = "GGATTCC";

    private static final String CATEGORY = "Forward Extension";

    private static final String EXTENSIONCAT = "forward";

    private static final String RELATEDTAG = "MSGHH";

    private static final String RESTSITE = "EcoRI";

    private static final String NAME = ExtensionsUtilityTester.UNIQUE + "name";

    private static final String SEQUENCE = "CATG";

    private static final String ENC_TAG = ExtensionsUtilityTester.UNIQUE + "tag";

    private static final String REST_SITE = ExtensionsUtilityTester.UNIQUE + "site";

    /*
     * Test method for 'org.pimslims.data.ExtensionsUtility.loadFile(WritableVersion, String)'
     */
/*    public void testLoadFile() throws FileNotFoundException {
        //TODO CHANGE Later final String filename = "./data/Extensions.csv";
        //final String filename = "./data/test/TestExtensionsPlus290309.csv";
        final String filename = "./data/ExtensionsPlus290309.csv";
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            ExtensionsUtility.loadFile(wv, filename);
            //load some data for testing
            final Map<String, Object> attributes = new HashMap<String, Object>();
            attributes.put(AbstractComponent.PROP_NAME, "Novagen 3C/LIC-R");
            //AbstractComponent name = wv.findFirst(AbstractComponent.class, attributes);
            final Collection matchingComps = wv.findAll(AbstractComponent.class, attributes);
            Assert.assertTrue(1 == matchingComps.size());
            //wv.commit();

            System.out.println("Loaded details from file: " + filename);
        } catch (final AccessException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        } catch (final ConstraintException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        } catch (final IOException e) {
            System.out.println("Unable to read from file: " + filename);
            throw new FileNotFoundException();
            //} catch (final AbortedException e) {
            // TODO Auto-generated catch block
            //throw new RuntimeException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }
*/

    /*
     * Test method for 'org.pimslims.data.ExtensionsUtility.makeCompCatList(WritableVersion, String)'
     */
    public void testMakeCompCatList() {
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);

        java.util.Collection<ComponentCategory> componentCategoryList;
        try {
            componentCategoryList = ExtensionsUtility.makeCompCatList(wv, ExtensionsUtilityTester.CATEGORY);
            Assert.assertTrue(1 == componentCategoryList.size());
        } catch (final ConstraintException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    /*
     * Test method for 'org.pimslims.data.ExtensionsUtility.makeExtMap(WritableVersion, String x6)'
     */
    public void testMakeExtMap() {
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        Map<String, Object> attrMap = new java.util.HashMap<String, Object>();
        try {
            attrMap =
                ExtensionsUtility.makeExtMap(wv, ExtensionsUtilityTester.EXTENSIONNAME,
                    ExtensionsUtilityTester.EXTENSIONSEQ, ExtensionsUtilityTester.CATEGORY,
                    ExtensionsUtilityTester.EXTENSIONCAT, ExtensionsUtilityTester.RELATEDTAG,
                    ExtensionsUtilityTester.RESTSITE);
            Assert.assertEquals(attrMap.get(Molecule.PROP_MOLTYPE), "DNA");
            Assert.assertTrue(attrMap.get(Extension.PROP_EXTENSIONTYPE).equals("forward"));
            Assert.assertNotSame("The type is not reverse", attrMap.get(Extension.PROP_EXTENSIONTYPE),
                "reverse");
        } catch (final ConstraintException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    /*
     * Test method for 'org.pimslims.data.ExtensionsUtility.molMapFromExtMap()
     */
    public void testMolMapFromExtMap() {
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        Map<String, Object> extMap = new java.util.HashMap<String, Object>();
        Map<String, Object> molMap = new java.util.HashMap<String, Object>();
        try {
            extMap =
                ExtensionsUtility.makeExtMap(wv, ExtensionsUtilityTester.EXTENSIONNAME,
                    ExtensionsUtilityTester.EXTENSIONSEQ, ExtensionsUtilityTester.CATEGORY,
                    ExtensionsUtilityTester.EXTENSIONCAT, ExtensionsUtilityTester.RELATEDTAG,
                    ExtensionsUtilityTester.RESTSITE);
            molMap = ExtensionsUtility.molMapFromExtMap(wv, extMap);
            Assert.assertNull(molMap.get(Extension.PROP_EXTENSIONTYPE));
            Assert.assertEquals(ExtensionsUtilityTester.EXTENSIONNAME, molMap
                .get(Substance.PROP_NAME));

        } catch (final ConstraintException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    /*
     * Test method for 'org.pimslims.data.ExtensionsUtility.molToExtension()
     */
    public void testCreate() throws AccessException {
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final Extension extension =
                ExtensionsUtility.create(wv, ExtensionsUtilityTester.NAME, ExtensionsUtilityTester.SEQUENCE,
                    ExtensionsUtilityTester.CATEGORY, "forward", ExtensionsUtilityTester.ENC_TAG,
                    ExtensionsUtilityTester.REST_SITE);
            Assert.assertEquals(ExtensionsUtilityTester.NAME, extension.getName());
            Assert.assertEquals("forward", extension.getExtensionType());
            Assert.assertEquals(ExtensionsUtilityTester.SEQUENCE, extension.getSeqString());
            assertEquals(ExtensionsUtilityTester.REST_SITE, extension.getRestrictionEnzyme());
            assertEquals(ExtensionsUtilityTester.ENC_TAG, extension.getRelatedProteinTagSeq());
        } catch (final ConstraintException e) {
            e.getCause().getCause().printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    /*
     * Test method for 'org.pimslims.data.ExtensionsUtility.molToExtension()
     */
    public void testCreateMoleculeAlreadyExists() throws AccessException {
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            new Molecule(wv, "DNA", ExtensionsUtilityTester.NAME);
            final Extension extension =
                ExtensionsUtility.create(wv, ExtensionsUtilityTester.NAME, ExtensionsUtilityTester.SEQUENCE,
                    ExtensionsUtilityTester.CATEGORY, "forward", ExtensionsUtilityTester.ENC_TAG,
                    ExtensionsUtilityTester.REST_SITE);
            Assert.assertEquals(ExtensionsUtilityTester.NAME, extension.getName());
            Assert.assertEquals("forward", extension.getExtensionType());
            Assert.assertEquals(ExtensionsUtilityTester.SEQUENCE, extension.getSeqString());
            assertEquals(ExtensionsUtilityTester.REST_SITE, extension.getRestrictionEnzyme());
            assertEquals(ExtensionsUtilityTester.ENC_TAG, extension.getRelatedProteinTagSeq());
        } catch (final ConstraintException e) {
            Throwable cause = e;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            cause.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    /*
     * Test method for 'org.pimslims.data.ExtensionsUtility.molToExtension()
     */
    public void testMolToExtension() throws ConstraintException, AccessException {
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final ComponentCategory category =
                new ComponentCategory(wv, "cc" + ExtensionsUtilityTester.UNIQUE);
            wv.flush();
            final Set<ComponentCategory> componentCategoryList = new java.util.HashSet<ComponentCategory>();
            componentCategoryList.add(category);
            final Map<String, Object> molMap = new java.util.HashMap<String, Object>();
            molMap.put(Substance.PROP_NAME, ExtensionsUtilityTester.EXTENSIONNAME);
            molMap.put(Substance.PROP_CATEGORIES, componentCategoryList);
            molMap.put(Molecule.PROP_MOLTYPE, "DNA");
            molMap.put(Molecule.PROP_SEQDETAILS, ExtensionsUtilityTester.CATEGORY);
            Molecule molExt = new Molecule(wv, molMap);
            final LabNotebook molOwner = molExt.getAccess();
            Collection<Extension> extensionsFound = wv.findAll(Extension.class, molMap);
            Assert.assertEquals(0, extensionsFound.size());

            ExtensionsUtility.molToExtension(wv, componentCategoryList);
            extensionsFound = wv.findAll(Extension.class, molMap);
            Assert.assertEquals(1, extensionsFound.size());
            final Extension newExt = wv.findFirst(Extension.class, molMap);
            Assert.assertEquals(newExt.getAccess(), molOwner);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    /*
     * Test method for 'org.pimslims.data.ExtensionsUtility.molToExtension()
     */
    public void testUserExtensionToExtension() throws ConstraintException, AccessException {
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final ComponentCategory category =
                wv.findFirst(ComponentCategory.class, ComponentCategory.PROP_NAME, "Forward Extension");
            wv.flush();
            final Set<ComponentCategory> componentCategoryList = new java.util.HashSet<ComponentCategory>();
            componentCategoryList.add(category);
            final Map<String, Object> molMap = new java.util.HashMap<String, Object>();
            molMap.put(Substance.PROP_NAME, ExtensionsUtilityTester.EXTENSIONNAME);
            molMap.put(Substance.PROP_CATEGORIES, componentCategoryList);
            molMap.put(Molecule.PROP_MOLTYPE, "DNA");
            molMap.put(Molecule.PROP_SEQSTRING, ExtensionsUtilityTester.SEQUENCE);
            molMap.put(Molecule.PROP_SEQDETAILS, ExtensionsUtilityTester.CATEGORY);
            final Molecule molExt = new Molecule(wv, molMap);

            Collection<Extension> extensionsFound = wv.findAll(Extension.class, molMap);
            Assert.assertEquals(0, extensionsFound.size());

            ExtensionsUtility.molToExtension(wv, componentCategoryList);
            extensionsFound = wv.findAll(Extension.class, molMap);
            Assert.assertEquals(1, extensionsFound.size());
            Assert.assertEquals(ExtensionsUtilityTester.SEQUENCE, extensionsFound.iterator().next()
                .getSeqString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    /*
     * Test method for 'org.pimslims.data.ExtensionsUtility.updateExtension()
     */
    public void testpdateExtension() {
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        java.util.Collection<ComponentCategory> componentCategoryList;
        try {
            componentCategoryList = ExtensionsUtility.makeCompCatList(wv, ExtensionsUtilityTester.CATEGORY);
            final Map<String, Object> extInDBMap = new java.util.HashMap<String, Object>();
            extInDBMap.put(Substance.PROP_NAME, ExtensionsUtilityTester.EXTENSIONNAME);
            extInDBMap.put(Substance.PROP_CATEGORIES, componentCategoryList);
            extInDBMap.put(Molecule.PROP_MOLTYPE, "DNA");
            extInDBMap.put(Extension.PROP_EXTENSIONTYPE, "reverse");
            extInDBMap.put(Molecule.PROP_SEQSTRING, "GTGTGTCC");

            Extension extension = new Extension(wv, extInDBMap);
            Assert.assertNotSame(ExtensionsUtilityTester.EXTENSIONSEQ, extension.getSeqString());

            final Collection<Extension> foundExtension =
                wv.findAll(Extension.class, Substance.PROP_NAME,
                    ExtensionsUtilityTester.EXTENSIONNAME);
            Assert.assertEquals(1, foundExtension.size());

            final Map<String, Object> extInFileMap = extInDBMap;
            extInFileMap.remove(Molecule.PROP_SEQSTRING);
            extInFileMap.put(Molecule.PROP_SEQSTRING, ExtensionsUtilityTester.EXTENSIONSEQ);
            extInFileMap.put(Extension.PROP_RELATEDPROTEINTAGSEQ, ExtensionsUtilityTester.ENC_TAG);
            extInFileMap.put(Extension.PROP_RESTRICTIONENZYME, ExtensionsUtilityTester.REST_SITE);

            extension = ExtensionsUtility.updateExtension(extInFileMap, foundExtension);
            Assert.assertEquals(ExtensionsUtilityTester.EXTENSIONSEQ, extension.getSeqString());
            assertEquals(ExtensionsUtilityTester.REST_SITE, extension.getRestrictionEnzyme());
            assertEquals(ExtensionsUtilityTester.ENC_TAG, extension.getRelatedProteinTagSeq());
        } catch (final ConstraintException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }
}