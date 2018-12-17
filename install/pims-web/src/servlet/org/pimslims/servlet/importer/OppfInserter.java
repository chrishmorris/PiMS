/**
* V4_4-web org.pimslims.servlet.importer OppfInserter.java
 * 
 * @author cm65
 * @date 2 May 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.importer;

import java.util.Collection;

import org.pimslims.dao.WritableVersion;

/**
 * OppfInserter
 * 
 */
public interface OppfInserter {

    /**
     * OppfInserter.insertTargets
     * 
     * @param version
     * @param targetList
     */
    void insertTargets(WritableVersion version, Collection<String> targetList);

}
