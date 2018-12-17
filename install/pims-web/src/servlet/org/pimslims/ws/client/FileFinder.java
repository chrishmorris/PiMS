/**
 * current-pims-web org.pimslims.ws.client FileFinder.java
 * 
 * @author Petr
 * @date 9 Mar 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Petr The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.ws.client;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.pimslims.lab.Util;

/**
 * FileFinder
 * 
 */
public class FileFinder {

    String rootDirPath;

    /*
     * Keys are the complete path to the file but in LowCase! 
     * This can be an issue on the case sensetive file systems
     * Therefore these keys are not a substitute for a real file path!!!
     */
    HashMap<String, File> files;

    Set<SequencingFileSet> fileSets;

    /**
     * Constructor for FileFinder
     * 
     * @throws FileNotFoundException
     */
    public FileFinder(final String rootDirPath) throws FileNotFoundException {
        this(new File(rootDirPath));
        this.rootDirPath = rootDirPath;
    }

    /**
     * Constructor for FileFinder
     * 
     * @throws FileNotFoundException
     */
    public FileFinder(final File rootDirectory) throws FileNotFoundException {
        this.files = new HashMap<String, File>();
        if (!rootDirectory.isDirectory()) {
            throw new RuntimeException(this.rootDirPath + " must be a directory!");
        }
        if (!rootDirectory.canRead()) {
            throw new RuntimeException("Cannot read from :" + this.rootDirPath);
        }

        this.files.putAll(this.traverse(rootDirectory));
        this.fileSets = this.getFileSets();
        if (this.fileSets.isEmpty()) {
            throw new FileNotFoundException("No files matching criteria are found in the directory "
                + this.rootDirPath + " and its subdirectories");
        }
    }

    static final FileFilter seqFilefilter = new FileFilter() {
        /**
         * Accepts seq ab1 scf zip (instrument standard)
         * 
         * .accept
         * 
         * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
         */
        public boolean accept(final File file) {
            if (!file.isFile()) {
                return false;
            }
            final String name = file.getPath();
            if (Util.isEmpty(name)) {
                return false;
            }
            final String standName = name.toLowerCase().trim();
            if (standName.endsWith(".ab1") || standName.endsWith(".seq") || standName.endsWith(".scf")
                || standName.endsWith(".zip")) {
                return true;
            }
            return false;
        }
    };

    /**
     * 
     * FileFinder.traverse
     * 
     * @param directory
     * @return
     */
    private HashMap<String, File> traverse(final File directory) {
        assert directory.isDirectory() : directory + " must be a directory!";
        final HashMap<String, File> fileList = new HashMap<String, File>();
        for (final File file : directory.listFiles()) {
            if (file.isDirectory()) {
                this.files.putAll(this.traverse(file));
            }
            if (FileFinder.seqFilefilter.accept(file)) {
                fileList.put(file.getAbsolutePath().toLowerCase(), file);
            }
        }
        return fileList;
    }

    public int getFileNumber() {
        return this.files.size();
    }

    /**
     * File with the same name but with different extension e.g. temp1.seq, temp1.ab1
     * 
     * FileFinder.getFileSet
     * 
     * @return
     */
    SequencingFileSet getFileSet(final File file) {
        //final SequencingFileSet sfs = new SequencingFileSet();
        final Set<File> fset = new HashSet<File>();
        String name = file.getAbsolutePath();
        // Chop extension
        name = name.substring(0, name.length() - 4).toLowerCase();

        for (String fname : this.files.keySet()) {
            fname = fname.toLowerCase();
            if (fname.startsWith(name)) {
                fset.add(file);
            }
        }
        return new SequencingFileSet(fset); //this.files.keySet().size();
    }

    /*TODO refactor*/
    Set<SequencingFileSet> getFileSets() {
        final Set<SequencingFileSet> sfs = new HashSet<SequencingFileSet>();
        final Set<String> fnames = this.files.keySet();
        final HashMap<String, File> addedFiles = new HashMap<String, File>();
        for (String fname : fnames) {
            fname = fname.toLowerCase();
            if (addedFiles.get(fname) != null) {
                // Skip the files grouped already
                continue;
            }
            fname = fname.substring(0, fname.length() - 4).toLowerCase();
            final Map<String, File> groupedFiles = this.getFileSet(fname);
            addedFiles.putAll(groupedFiles);
            if (!groupedFiles.isEmpty()) {
                sfs.add(new SequencingFileSet(new HashSet(groupedFiles.values())));
            }
        }

        return sfs;
    }

    /**
     * 
     * FileFinder.getFileSet
     * 
     * @param baseFileName - full file name (with path) but with no extension
     */
    HashMap<String, File> getFileSet(final String baseFileName) {
        final HashMap<String, File> fs = new HashMap<String, File>();

        for (final SequencingFileSet.FileTypes type : SequencingFileSet.FileTypes.values()) {
            final File f = this.files.get(baseFileName + "." + type.toString());
            if (f != null) {
                fs.put(f.getAbsolutePath().toLowerCase(), f);
            }
        }

        return fs;
    }

    public Set<File> getFiles() {
        return new HashSet(this.files.values());
    }

    public SequencingFileSet findFileSet(final String baseName) {
        for (final SequencingFileSet sfs : this.fileSets) {
            if (sfs.match(baseName)) {
                return sfs;
            }
        }
        return null;
    }

    public HashSet<SequencingFileSet> findFiles(final Set<String> baseNames) {
        final HashSet<SequencingFileSet> foundFiles = new HashSet<SequencingFileSet>();
        for (final String name : baseNames) {
            if (Util.isEmpty(name)) {
                continue;
            }
            final SequencingFileSet sfs = this.findFileSet(name);
            if (sfs != null) {
                foundFiles.add(sfs);
            }
        }
        return foundFiles;
    }

}
