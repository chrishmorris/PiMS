/**
 * pims-tools org.pimslims.tools CaliperImport.java
 * 
 * @author Marc Savitsky
 * @date 17 May 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.pimslims.lab.file.CaliperFile;

/**
 * CaliperImport
 * 
 */
public class CaliperFileImport {

    static String targetURL;// = "http://localhost:8080/pims/ImportFiles";

    /**
     * 
     * CaliperImporter.processFolder
     * 
     * @param wv
     * @param folder
     * @throws Exception
     */
    public static void processFolder(final String folder) throws Exception {

        System.out.println("CaliperFileImport.processFolder [" + folder + "]");

        File[] files = getFiles(folder);
        for (int j = 0; j < files.length; j++) {

            System.out.println("CaliperFileImport.processFolder file [" + files[j].getName() + "]");

            if (!CaliperFile.isCaliperFile(files[j].getName())) {
                System.out.println("CaliperFileImport.processFolder IGNORED - not a valid IFile ["
                    + files[j].getName() + "]");
                continue;
            }

            int status = executePost(targetURL + "/ImportFiles", files[j]);
            System.out.println("CaliperFileImport.excutePost response[" + status + "]");

            if (200 == status) {

                // Destination directory
                File dir = new File(folder + "\\processed");
                if (!dir.exists()) {
                    if (!dir.mkdir()) {
                        throw new Exception("can't create folder [" + dir.getPath() + "]");
                    }
                }

                System.out.println("FileImporter.processFolder move [" + files[j].getPath() + " to "
                    + dir.getPath() + "]");

                // Move file to new directory
                if (!files[j].renameTo(new File(dir, files[j].getName()))) {
                    throw new Exception("can't move file  [" + files[j].getName() + "]");
                }
            }

        }

    }

    /**
     * CSVImporter.executePost
     * 
     * @param targetURL
     * @param string
     */
    private static int executePost(String targetURL, File file) {

        System.out.println("CaliperFileImport.excutePost [" + targetURL + "]");
        int status = -1;
        String charset = "UTF-8";
        URL url;
        HttpURLConnection connection = null;
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.

        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            OutputStream output = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true); // true = autoFlush, important!

            writer.println("--" + boundary);
            writer.println("Content-Disposition: form-data; name=\"binaryFile\"; filename=\""
                + file.getName() + "\"");
            writer.println("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName()));
            writer.println("Content-Transfer-Encoding: binary");
            writer.println();

            final InputStream input = new FileInputStream(file);

            byte[] buffer = new byte[1024];
            for (int length = 0; (length = input.read(buffer)) > 0;) {
                output.write(buffer, 0, length);
            }
            output.flush(); // Important! Output cannot be closed. Close of writer will close output as well.
            input.close();

            writer.println(); // Important! Indicates end of binary boundary.

            // End of multipart/form-data.
            writer.println("--" + boundary + "--");

            writer.flush();
            writer.close();

            //Get Response   
            status = (connection).getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return status;
    }

    /**
     * 
     * CaliperImporter.getFiles
     * 
     * @param dir
     * @return
     */
    public static File[] getFiles(String dir) {
        File fl = new File(dir);
        File[] files = fl.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });

        return files;
    }

    /**
     * 
     * FileImporter.getFolders
     * 
     * @param dir
     * @return
     */
    public static File[] getFolders(String dir) {
        File fl = new File(dir);
        File[] files = fl.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });

        return files;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        List<String> folders = new ArrayList<String>();
        boolean recursive = false;

        for (int i = 0; i < args.length; i++) {
            System.out.println("arg[" + i + "]: " + args[i] + " " + args[i + 1]);

            if (args[i].charAt(0) == '-') {
                char c = args[i].charAt(1);

                switch (c) {

                    case 'f':
                        i++;
                        folders.add(args[i]);
                        break;

                    case 'r':
                        recursive = true;
                        break;

                    case 'u':
                        i++;
                        targetURL = args[i];
                        break;

                    default:
                        break;

                }
            }
        }

        if (folders.isEmpty()) {
            System.err.println("use is CaliperFileImport [-r] -f folder -u targetURL");
            System.exit(1);
        }

        try {
            for (String folder : folders) {
                CaliperFileImport.processFolder(folder);

                if (recursive) {
                    for (File file : getFolders(folder)) {
                        CaliperFileImport.processFolder(file.getAbsolutePath());
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Exception caught [" + e.getLocalizedMessage() + "]");
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("CaliperFileImport completed");
    }

}
