/**
 * pims-model org.pimslims.persistence HqlQuery.java
 * 
 * @author cm65
 * @date Sep 14, 2012
 * 
 *       Protein Information Management System
 * @version: 4.4
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.persistence;

import org.pimslims.dao.ReadableVersion;

/**
 * HqlQuery
 * 
 */
@Deprecated
// obsolete name
public class HqlQuery extends JpqlQuery {

    /**
     * Constructor for HqlQuery
     * 
     * @param string select statement, without initial "select "
     * @param class1
     */
    public HqlQuery(String string, ReadableVersion version, Class class1) {
        super(string, version, class1);
    }

}
