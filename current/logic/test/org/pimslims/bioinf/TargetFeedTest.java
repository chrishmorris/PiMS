package org.pimslims.bioinf;

import java.io.IOException;

import javax.naming.ServiceUnavailableException;

import junit.framework.TestCase;

import org.pimslims.bioinf.newtarget.UniProtParser;
import org.pimslims.dao.ModelImpl;
import org.pimslims.properties.PropertyGetter;

public class TargetFeedTest extends TestCase {

    /**
     * TargetFeedTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        ModelImpl.getModel();
        PropertyGetter.setProxySetting();
    }

    public void test() throws ServiceUnavailableException, IOException {
        final String record = DBFetch.getDBrecord("Uniprot", "Q762B6");
        UniProtParser uniprot = new UniProtParser(record);
        assertEquals("", uniprot.getProperty("DE"));
        assertEquals("", uniprot.getProperty("DT"));
        assertEquals("ATP7A", uniprot.getGeneName());
    }

}
