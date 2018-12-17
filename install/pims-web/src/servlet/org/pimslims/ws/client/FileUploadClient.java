/**
 * current-pims-web org.pimslims.sec EncryptionTester.java
 * 
 * @author Petr Troshin aka pvt43
 * @date 5 Mar 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Petr The copyright holder has licenced the STFC to redistribute this software
 */

package org.pimslims.ws.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.MTOMFeature;

import org.pimslims.sec.EncryptionManager;
import org.pimslims.ws.client.stubs.FileUpload;
import org.pimslims.ws.client.stubs.FileUploadService;

// import com.sun.xml.internal.ws.developer.JAXWSProperties;

@Deprecated
// this work was not finished
public class FileUploadClient {

    final EncryptionManager emanager;

    final FileUpload proxy;

    final FileFinder ffinder;

    /**
     * Constructor for FileUploadClient
     * 
     * @throws FileNotFoundException
     */
    @Deprecated
    public FileUploadClient(final File rootDirectory) throws FileNotFoundException {
        this.ffinder = new FileFinder(rootDirectory);

        final MTOMFeature feature = new MTOMFeature();
        final FileUploadService service = new FileUploadService();
        this.proxy = service.getFileUploadPort(feature);
        final Map<String, Object> ctxt = ((BindingProvider) this.proxy).getRequestContext();
        ctxt.put(
            "com.sun.xml.ws.transport.http.client.streaming.chunk.size" /*JAXWSProperties.HTTP_CLIENT_STREAMING_CHUNK_SIZE*/,
            8192);

        // Get Encryption service
        this.emanager = new EncryptionManager(EncryptionManager.class.getResourceAsStream("wskeystore.ks"));
    }

    @Deprecated
    private void upload(final File file) throws IOException {

        // Calculate file digest
        final byte[] checksum = this.emanager.getDigest(file);
        // Make the file to store encrypted content of file to be sent
        final File encryptedFile =
            File.createTempFile(file.getName() + "_" + System.currentTimeMillis(), ".enc");

        // encrypt 
        this.emanager.encrypt(file, encryptedFile);

        // send encrypted file and hash for not encrypted file
        this.proxy.upload("file.bin", checksum, new DataHandler(new FileDataSource(encryptedFile)));

        encryptedFile.delete();
    }

    @Deprecated
    public static void main(final String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: FileUploadClient <full path to directory with files to upload>");
            System.out.println("Example: FileUploadClient c:\\files ");
            System.exit(0);
        }
        final String dir = args[0];
        final File rootDir = new File(dir);
        if (!rootDir.exists()) {
            System.out.println("The directory '" + dir + "' does not appear to exist! Cannot proceed");
            System.exit(0);
        }
        if (!rootDir.isDirectory()) {
            System.out.println("The directory '" + dir + "' is not a directory! Cannot proceed");
            System.exit(0);
        }
        if (!rootDir.canRead()) {
            System.out.println("The directory '" + dir + "' is not readable! Cannot proceed");
            System.exit(0);
        }

        final FileUploadClient fupload = new FileUploadClient(rootDir);
        // Get list of the files to be sent (that server expected to receive)
        final Set<String> flist = new HashSet(fupload.proxy.getFileList());

        System.out.println("Looking for " + flist);

        if (flist.isEmpty()) {
            System.out.println("Nothing to upload!");
            System.out.println("Quitting");
            System.exit(0);
        }

        final Set<SequencingFileSet> files = fupload.ffinder.findFiles(flist);
        System.out.println("Found:" + files);

        for (final SequencingFileSet sfs : files) {
            for (final File file : sfs.getFiles()) {
                System.out.println("Uploading file " + file.getName());
                fupload.upload(file);
                System.out.println("File " + file.getName() + " is uploaded");
                System.out.println();
            }
        }

        System.out.println("Done!");
    }
}
