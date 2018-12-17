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
import java.util.ArrayList;
import java.util.List;

import org.pimslims.access.Access;
import org.pimslims.dao.WritableVersion;
import org.pimslims.lab.file.CaliperPCRFile;
import org.pimslims.lab.file.CaliperVerificationFile;
import org.pimslims.lab.file.FileFactory;
import org.pimslims.lab.file.IFileType;
import org.pimslims.test.AbstractTestCase;

/**
 * CaliperImport
 * 
 * Note This will be replaced by CaliperFileImport in PiMS 4.2
 * 
 */
public class FileImporter {

    /**
     * 
     * CaliperImporter.processFolder
     * 
     * @param wv
     * @param folder
     * @throws Exception
     */
    public static void processFolder(final String folder) throws Exception {

        System.out.println("FileImporter.processFolder [" + folder + "]");

        File[] files = getFiles(folder);
        for (int j = 0; j < files.length; j++) {
            final WritableVersion version = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);
            try {
                //System.out.println("FileImporter.processFolder file [" + files[j].getName() + "]");
                File file = files[j];
                final IFileType iFile =
                    FileFactory.getIFile(version, file.getName(), new FileInputStream(file));
                if (null == iFile) {
                    System.out.println("FileImporter.processFolder IGNORED - not a valid IFile ["
                        + file.getName() + "]");
                    continue;
                }

                if (null != iFile.process(version)) {

                    // Destination directory
                    File dir = new File(folder + "\\processed");
                    if (!dir.exists()) {
                        if (!dir.mkdir()) {
                            throw new Exception("can't create folder [" + dir.getPath() + "]");
                        }
                    }

                    System.out.println("FileImporter.processFolder move [" + file.getPath() + " to "
                        + dir.getPath() + "]");

                    // Move file to new directory
                    if (!file.renameTo(new File(dir, file.getName()))) {
                        throw new Exception("can't move file  [" + file.getName() + "]");
                    }
                    version.commit();
                }
            } catch (final Exception e) {
                e.printStackTrace();

            } finally {
                if (!version.isCompleted()) {
                    version.abort();
                }
            }
        }

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

                    default:
                        break;

                }
            }
        }

        if (folders.isEmpty()) {
            System.err.println("use is FileImporter [-r] -f folder");
            System.exit(1);
        }

        FileFactory.register(new CaliperPCRFile());
        FileFactory.register(new CaliperVerificationFile());

        try {
            for (String folder : folders) {
                FileImporter.processFolder(folder);

                if (recursive) {
                    for (File file : getFolders(folder)) {
                        FileImporter.processFolder(file.getAbsolutePath());
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Exception caught [" + e.getLocalizedMessage() + "]");
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("CaliperImporter completed");
    }

}
