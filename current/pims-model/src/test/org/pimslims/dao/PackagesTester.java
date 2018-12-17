/*
 * Created on 05-Jan-2005 @author: Chris Morris
 */
package org.pimslims.dao;

import org.pimslims.test.AbstractTestCase;

/**
 * Lists the CCPN metaclasses
 * 
 * @version 0.1
 */
public class PackagesTester extends AbstractTestCase {

    /**
     * 
     */
    public PackagesTester() {
        super();
    }

    /**
     * @param prefix
     * @param p
     */
/*    public void listPackage(final String prefix, final MetaPackage p) {
        log(prefix + "." + p.shortName);
        printList(p.containedPackageNames);
        printList(p.classNames);
    }*/

    private void printList(final java.util.List list) {
        for (java.util.Iterator i = list.iterator(); i.hasNext();) {
            String name = (String) i.next();
            log(name);
        }
        log("-------");
    }
}
