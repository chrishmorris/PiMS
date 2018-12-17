/**
 * 
 */
package org.pimslims.metamodel;

import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.test.POJOFactory;
import org.pimslims.util.File;

/**
 * @author cm65
 * 
 */
public abstract class TestFile extends TestCase {

    protected static final byte[] DATA = "test".getBytes();

    /**
     * The write transaction to use
     */
    protected WritableVersion wv = null;

    /**
     * The instance of File under test
     */
    protected File file = null;

    /**
     * The model being tested
     */
    protected final AbstractModel model;

    /**
     * Basic sanity checks for an instance of File
     * 
     * @param f
     */
    protected void doTestFile(final File f) {
        assertNotNull("null", f);
        assertNotNull("hook", f.getHook());
        assertNotNull(file.getDate());
        // subclass may contain more checks
    }

    /**
     * Check for path attacks
     */
    public void testPathAttack() {
        try {
            final File file2 = wv.createFile(DATA, "bad../../path", POJOFactory.createTargetGroup(wv));
            assertTrue("path attack", -1 == file2.getName().indexOf("../"));
            file2.delete();
        } catch (final AccessException e) {
            fail(e.getMessage());
        } catch (final ConstraintException e) {
            // that's fine
        } catch (final IOException e) {
            fail(e.getMessage());
        }
    }

    /**
     * LATER Check for poisoned null This test is awkward to write, becuase the exception makes the
     * transaction invalid public void testNullAttack() { try { File file2 = wv.createFile(DATA,
     * "bad\00null"); assertFalse("poisoned null", -1==file2.getName().indexOf("null")); assertTrue("poisoned
     * null", -1==file2.getName().indexOf("\00")); file2.delete(); } catch (AccessException e) {
     * fail(e.getMessage()); } catch (ConstraintException e) { // that's fine } catch (ApiException e) {
     * fail(e.getMessage()); } catch (IOException e) { fail(e.getMessage()); } }
     * 
     * @throws ConstraintException
     */

    public void testSetDescription() throws ConstraintException {

        file.setDescription("just testing");
        assertEquals("description", "just testing", file.getDescription());

    }

    public void testSetTitle() throws ConstraintException {

        file.setTitle("just testing title");
        assertEquals("title", "just testing title", file.getTitle());

    }

    public void testSetLegend() throws ConstraintException {

        file.setLegend("just testing legend");
        assertEquals("legend", "just testing legend", file.getLegend());

    }

    protected void doTestExtension(final String extension) throws ConstraintException {
        file.setExtension(extension);
        assertEquals("extension: " + file.getExtension(), extension, file.getExtension());
        assertFalse("mime type for: " + extension, "application/octet-stream".equals(file.getMimeType()));
    }

    public void testSetExtension() {
        try {
            assertEquals("no extension", "", file.getExtension());
            doTestExtension(".gif");
            assertEquals("extension => mime type", "image/gif", file.getMimeType());
            assertTrue("renamed", file.getFile().exists());

            // test again, when there is already an extension
            doTestExtension(".mpg");
        } catch (final ConstraintException e) {
            fail(e.getMessage());
        }
    }

    public void testCreateExtension() {
        try {
            final File file2 = wv.createFile(DATA, "image.gif", POJOFactory.createTargetGroup(wv));
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

    public void testMpsiExtensions() {
        try {
            doTestExtension(".pdf");
            doTestExtension(".html");
            doTestExtension(".htm");
            doTestExtension(".zip");
            doTestExtension(".jpg");
            doTestExtension(".doc");
            doTestExtension(".txt");

            doTestExtension(".mht"); // message/rfc822 (email)

            doTestExtension(".gb"); // genbank
            doTestExtension(".sw"); // swissprot
        } catch (final ConstraintException e) {
            fail(e.getMessage());
        }
    }

    public void testSetMimeType() {
        try {
            assertEquals("no mime type", "application/octet-stream", file.getMimeType());
            file.setMimeType("application/zip");
            assertEquals("mime type", "application/zip", file.getMimeType());
            assertEquals("mime type => extension: " + file.getName(), ".zip", file.getExtension());
        } catch (final ConstraintException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Subcalss must provide test for File.add()
     */
    public abstract void testAdd();

    public void testGetLength() {

        assertEquals("length", DATA.length, file.getLength());
    }

    public void testRead() {
        try {
            file.open();
            final byte[] in = new byte[DATA.length];
            file.read(in);
            assertTrue("read: " + in, Arrays.equals(DATA, in));
            final int length = file.read(in);
            assertEquals("read: ", -1, length);
            file.close();
        } catch (final IOException e) {
            fail(e.getMessage());

        }
    }

    public void testDelete() {

        final java.io.File diskFile = file.getFile();
        file.delete();
        assertFalse("deleted", diskFile.exists());

    }

    private static final String FILE_NAME = "test" + new java.util.Date().getTime() + "x";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        wv = this.model.getTestVersion();
        /*
         * Test for 'org.pimslims.implementation.File.createFile(WritableVersion, byte[], String, String)'
         * Note that this also tests createFile(WritableVersion, InputStream, String, String)
         */

        file = wv.createFile(DATA, FILE_NAME, POJOFactory.createTargetGroup(wv));
        doTestFile(file);
    }

    @Override
    protected void tearDown() throws Exception {
        try {
            file.delete();
        } finally {
            if (null != wv) {
                wv.abort();
            }
            super.tearDown();
        }
    }

    /**
     * Constructor for TestFile.
     * 
     * @param methodName
     */
    public TestFile(final AbstractModel model, final String methodName) {
        super(methodName);
        this.model = model;
    }

    public void testNameClash() {

        try {
            final File file2 = wv.createFile(DATA, FILE_NAME, POJOFactory.createTargetGroup(wv));
            doTestFile(file2);
            assertFalse("name clash", file.getName().equals(file2.getName()));
            file2.delete();
        } catch (final AccessException e) {
            fail(e.getMessage());
        } catch (final ConstraintException e) {
            fail(e.getMessage());
        } catch (final IOException e) {
            fail(e.getMessage());
        }
    }

    public void testNoExtension() {
        try {
            final File file2 = wv.createFile(DATA, "testNoExtn", POJOFactory.createTargetGroup(wv));
            doTestFile(file2);
            assertEquals("extension", "", file2.getExtension());
            assertEquals("mime type", "application/octet-stream", file.getMimeType());
            file2.setMimeType("application/octet-stream"); // apache
            // commons-uploader 
            // getContentType()
            // will return this
            assertEquals("extension", "", file2.getExtension());
        } catch (final AccessException e) {
            fail(e.getMessage());
        } catch (final ConstraintException e) {
            fail(e.getMessage());
        } catch (final IOException e) {
            fail(e.getMessage());
        }
    }

    public void testKeepExtension() {
        try {
            final File file2 = wv.createFile(DATA, "testKeepExtn.akta", POJOFactory.createTargetGroup(wv));
            doTestFile(file2);
            assertEquals("extension", ".akta", file2.getExtension());
            assertEquals("mime type", "application/octet-stream", file.getMimeType());
            //file2.setMimeType("application/octet-stream"); // apache
            // commons-uploader 
            // getContentType()
            // will return this
            assertEquals("extension", ".akta", file2.getExtension());
        } catch (final AccessException e) {
            fail(e.getMessage());
        } catch (final ConstraintException e) {
            fail(e.getMessage());
        } catch (final IOException e) {
            fail(e.getMessage());
        }
    }
    /**
     * LATER ensure disk file is deleted if transaction is aborted
     */
    // public void testAbort() {}
}
