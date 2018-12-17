/**
 * pims-web org.pimslims.bioinf DBFetchTest.java
 * 
 * @author cm65
 * @date 20 Oct 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.bioinf.newtarget;

import java.io.IOException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.biojava.bio.BioException;
import org.pimslims.bioinf.DBFetch;
import org.pimslims.dao.ModelImpl;
import org.pimslims.properties.PropertyGetter;

/**
 * DBFetchTest
 * 
 */
public class DBFetchTest extends TestCase {

    /**
     * @param name
     */
    public DBFetchTest(final String name) {
        super(name);
        // set up the HTTP proxy
        ModelImpl.getModel();
        PropertyGetter.setProxySetting();
    }

    /**
     * Test method for {@link org.pimslims.bioinf.DBFetch#getGenbankNucleotideEntry(java.lang.String)}.
     */
    public void testGetGenbankNucleotideEntry() throws IOException {
        final String entry = DBFetch.getGenbankNucleotideEntry("U49845");
        Assert.assertNotNull(entry);
        Assert.assertFalse(entry.contains("</html>"));
        // could do new EMBLNucleotideParser(entry, null); but we need an example with only one CDS
    }

    /**
     * Test method for {@link org.pimslims.bioinf.DBFetch#getGenbankNucleotideEntry(java.lang.String)}.
     */
    public void testNotFound() throws IOException {
        final String entry = DBFetch.getGenbankNucleotideEntry("nonesuch");
        Assert.assertNull(entry, entry);
    }

    /**
     * Test method for {@link org.pimslims.bioinf.DBFetch#getGenbankProteinEntry(java.lang.String)}.
     
    public void testGetGenbankProteinEntry() throws IOException, BioException {
        final String entry = DBFetch.getGenbankProteinEntry("U49845");
        Assert.assertNotNull(entry);
        BioJavaGenBankProteinParserTester.parseGenbankProteinEntry(entry);
    } */

}
