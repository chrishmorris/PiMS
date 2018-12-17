/**
 * 
 */
package org.pimslims.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.TestFile;
import org.pimslims.model.core.Annotation;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.target.Project;
import org.pimslims.test.POJOFactory;
import org.pimslims.util.File;
import org.pimslims.util.FileImpl;

/**
 * Test cases for File, PIMS API class representing uploaded files
 * 
 * @author cm65
 * 
 */
public class FileImplTester extends TestFile {

    private static final String UNIQUE = "fi" + System.currentTimeMillis();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(FileImplTester.class);
    }

    /**
     * Constructor for TestFile.
     * 
     * @param methodName
     */
    public FileImplTester(final String methodName) {
        super(ModelImpl.getModel(), methodName);
    }

    /**
     * Basic sanity checks for an instance of File
     * 
     * @param file
     */
    @Override
    protected void doTestFile(final File file) {
        super.doTestFile(file);
        assertNotNull("annotation", ((FileImpl) file).getAnnotation());
    }

    /*
     * protected org.pimslims.model.annotation.Annotation createAnnotation() { }
     */

    /**
     * Test getFile(Annnotation)
     */
    public void testGetFileAnnotation() {
        try {
            final Annotation annotation =
                FileImpl.createAnnotation(wv, "testAnnotation", POJOFactory.createTargetGroup(wv), null);
            final File file2 = FileImpl.getFile(annotation);
            doTestFile(file2);
            file2.delete();
        } catch (final AccessException e) {
            fail(e.getMessage());
        } catch (final ConstraintException e) {
            fail(e.getMessage());

        }
    }

    public void testCreateFileParentLater() throws AccessException, ConstraintException, IOException {
        final InputStream in = new java.io.ByteArrayInputStream(UNIQUE.getBytes());
        final File fi = FileImpl.createFile(this.wv, in, UNIQUE);
        final LabBookEntry parent = new Organisation(this.wv, UNIQUE);
        fi.add(parent);

        // check that worked
        assertNotNull(fi.getHook());
        final Set<Attachment> attachments = parent.getAttachments();
        assertEquals(1, attachments.size());
        final FileImpl file2 = FileImpl.getFile((Annotation) attachments.iterator().next());
        assertEquals(fi.getName(), file2.getName());

        // also check contents were saved
        file2.open();
        final byte[] contents = new byte[UNIQUE.getBytes().length];
        file2.read(contents);
        file2.close();
        assertEquals(UNIQUE, new String(contents));

    }

    public void testCreateFileNoParent() throws AccessException, ConstraintException, IOException {
        final InputStream in = new java.io.ByteArrayInputStream(UNIQUE.getBytes());
        final File fi = FileImpl.createFile(this.wv, in, UNIQUE);
        // check consequences of forgetting fi.add(parent);
        try {
            fi.getHook();
            fail("Can get hook but no parent assigned");
        } catch (final IllegalStateException e) {
            // that's as it should be
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.TestFile#testAdd()
     */
    @Override
    public void testAdd() {
        final MetaRole attachments =
            model.getMetaClass(Project.class.getName()).getMetaRole(LabBookEntry.PROP_ATTACHMENTS);
        assertNotNull(attachments);
        try {
            final LabBookEntry object =
                wv.create(org.pimslims.model.target.Project.class,
                    org.pimslims.test.POJOFactory.getAttrProject());
            file.add(object);
            assertEquals("added", 1, object.get(LabBookEntry.PROP_ATTACHMENTS).size());

            // now test getting files
            final java.util.Collection files = object.get_Files();
            assertEquals("one file", 1, files.size());
            final File file2 = (File) new java.util.ArrayList(files).get(0);
            assertEquals("same file", file.getName(), file2.getName());

        } catch (final AccessException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (final ConstraintException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * add an extra test to generic testDelete() method to ensure that the Annotation record is deleted
     * 
     * @see org.pimslims.metamodel.TestFile#testDelete()
     */
    @Override
    public void testDelete() {
        final Annotation annotation = ((FileImpl) file).getAnnotation();
        final String hook = annotation.get_Hook();

        super.testDelete();

        assertNull("annotation deleted", wv.get(hook));
    }

    public void testLongName() {
        try {
            final File file2 =
                wv.createFile(DATA, "123456789012345678901234567890xx.gif", POJOFactory.createTargetGroup(wv));
            doTestFile(file2);
            assertEquals("extension", ".gif", file2.getExtension());
            file2.delete();
        } catch (final AccessException e) {
            fail(e.getMessage());
        } catch (final ConstraintException e) {
            fail(e.getMessage());
        } catch (final IOException e) {
            fail(e.getMessage());
        }
    }

    public void testNameIgnorePath() {
        try {
            final String fileShortName = "acnir_pnnir_rpnir_acpaz_220807.doc";
            final FileImpl file =
                (FileImpl) wv.createFile(DATA, "C:\\Documents And Settings\\mje73\\My Documents\\Mol.Bio\\"
                    + fileShortName, POJOFactory.createTargetGroup(wv));
            assertEquals(fileShortName, file.getDetails());
            assertEquals(".doc", file.getExtension());
            file.delete();
        } catch (final AccessException e) {
            fail(e.getMessage());
        } catch (final ConstraintException e) {
            fail(e.getMessage());
        } catch (final IOException e) {
            fail(e.getMessage());
        }
    }

    @Override
    public void testNoExtension() {
        try {
            final String fileShortName = "acnir_pnnir_rpnir_acpaz_220807";
            final FileImpl file =
                (FileImpl) wv.createFile(DATA, "C:\\Documents And Settings\\mje73\\My Documents\\Mol.Bio\\"
                    + fileShortName, POJOFactory.createTargetGroup(wv));
            file.setExtension("");
            assertEquals(fileShortName, file.getDetails());
            assertEquals("", file.getExtension());
            file.delete();
        } catch (final AccessException e) {
            fail(e.getMessage());
        } catch (final ConstraintException e) {
            fail(e.getMessage());
        } catch (final IOException e) {
            fail(e.getMessage());
        }
    }

    public void testAutoMimeType() {
        try {
            final String fileShortName = "acnir_pnnir_rpnir_acpaz_220807.doc";
            final FileImpl file =
                (FileImpl) wv.createFile(DATA, "C:\\Documents And Settings\\mje73\\My Documents\\Mol.Bio\\"
                    + fileShortName, POJOFactory.createTargetGroup(wv));
            assertEquals("application/msword", file.getMimeType());
            file.delete();
        } catch (final AccessException e) {
            fail(e.getMessage());
        } catch (final ConstraintException e) {
            fail(e.getMessage());
        } catch (final IOException e) {
            fail(e.getMessage());
        }
    }
}
