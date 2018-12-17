/**
 * current-pims-web org.pimslims.utils.sequenator SOrderFileList.java
 * 
 * @author Petr
 * @date 8 Mar 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Petr The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils.sequenator;

import java.util.Set;

import org.pimslims.lab.sequence.FileNames;

/**
 * SOrderFileList
 * 
 */
@Deprecated
// obsolete, unused
public class SOrderFileList implements FileNames {

    /**
     * FileNames.getFileNames
     * 
     * @see org.pimslims.ws.server.FileNames#getFileNames()
     */
    public Set<String> getFileNames() {
        /*
         * 1. get list of seqsetupDone orders
         * 2. get a list of   names from orders
         * 3. Pack them into the set and return 
         */
        return null;
    }

}
