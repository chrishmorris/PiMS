/**
 * current-pims-web org.pimslims.ws.client SequencingFileSet.java
 * 
 * @author pvt43
 * @date 10 Mar 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.ws.client;

import java.io.File;
import java.util.Set;

/**
 * SequencingFileSet
 * 
 */
public class SequencingFileSet {

    public static enum FileTypes {
        seq, ab1, scf
    }

    final private Set<File> files;

    public File seq;

    public File ab1;

    public File scf;

    /**
     * Constructor for SequencingFileSet
     */
    public SequencingFileSet(final Set<File> files) {
        assert files != null && !files.isEmpty() && files.size() <= 3 : "The number of files "
            + "are either 0 empty or more than 3. Cannot proceed! Parameter is: " + files;
        this.files = files;
        for (final File f : files) {
            final String fname = f.getName().toLowerCase().trim();
            if (fname.endsWith(FileTypes.seq.toString())) {
                this.seq = f;
            } else if (fname.endsWith(FileTypes.ab1.toString())) {
                this.ab1 = f;
            } else if (fname.endsWith(FileTypes.scf.toString())) {
                this.scf = f;
            } else {
                throw new AssertionError("Type of the file " + f.getAbsolutePath() + " cannot be recognised!");
            }
        }
    }

    /**
     * SequencingFileSet.toString
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String res = "";
        if (this.seq != null) {
            res += this.seq.getAbsolutePath() + "\n";
        }
        if (this.ab1 != null) {
            res += this.ab1.getAbsolutePath() + "\n";
        }
        if (this.scf != null) {
            res += this.scf.getAbsolutePath() + "\n";
        }
        return res;
    }

    String getBaseName() {
        String bname = null;
        if (this.seq != null) {
            bname = this.seq.getName();
        } else if (this.ab1 != null) {
            bname = this.ab1.getName();
        } else {
            bname = this.scf.getName();
        }
        // Cut the extension
        return bname.substring(0, bname.length() - 4).toLowerCase();
    }

    String getFullBaseName() {
        String bname = null;
        if (this.seq != null) {
            bname = this.seq.getAbsolutePath();
        }
        if (this.ab1 != null) {
            bname = this.ab1.getAbsolutePath();
        } else {
            bname = this.scf.getAbsolutePath();
        }
        // Cut the extension
        return bname.substring(0, bname.length() - 4).toLowerCase();
    }

    public boolean match(final String baseName) {

        if (this.getBaseName().equalsIgnoreCase(baseName)) {
            return true;
        }
        return false;
    }

    /**
     * SequencingFileSet.equals
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof SequencingFileSet)) {
            return false;
        }
        final SequencingFileSet sset = (SequencingFileSet) obj;
        if (sset.getFileNumber() != this.getFileNumber()) {
            return false;
        }
        if (sset.ab1.equals(this.ab1) && sset.seq.equals(this.seq) && sset.scf.equals(this.scf)) {
            return true;
        }
        return false;
    }

    int getFileNumber() {
        int count = 0;
        if (this.seq != null) {
            count++;
        }
        if (this.ab1 != null) {
            count++;
        }
        if (this.scf != null) {
            count++;
        }
        return count;
    }

    /**
     * SequencingFileSet.hashCode
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int code = 0;
        if (this.seq != null) {
            code += this.seq.hashCode();
        }
        if (this.ab1 != null) {
            code += this.ab1.hashCode();
        }
        if (this.scf != null) {
            code += this.scf.hashCode();
        }
        return code;
    }

    public Set<File> getFiles() {
        return this.files;
    }

}
