/**
 * web org.pimslims.data XlsReaderTest.java
 * 
 * @author bl67
 * @date 22 Apr 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 bl67
 * 
 * 
 */
package org.pimslims.data;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.AllToolsTests;
import org.pimslims.utils.XlsReader;

/**
 * XlsReaderTest
 * 
 */
public class XlsReaderTest extends TestCase {
    String Filename = AllToolsTests.TestingDataPath + "/simple.xls";

    public void testLoadSimpleData() throws IOException {
        XlsReader xlsReader = null;
        try {
            final List<String> nameList = new LinkedList<String>();
            nameList.add("name");
            nameList.add("date");
            nameList.add("value");
            xlsReader = new XlsReader(this.Filename, nameList);
            Assert.assertTrue(xlsReader.hasNext());
            final Map<String, String> row1 = xlsReader.next();
            Assert.assertTrue(xlsReader.hasNext());
            final Map<String, String> row2 = xlsReader.next();
            Assert.assertFalse(xlsReader.hasNext());

            Assert.assertNotNull(row1);
            Assert.assertEquals("name1", row1.get("name"));
            Assert.assertEquals("1", row1.get("value"));
            Assert.assertEquals("name2", row2.get("name"));
            Assert.assertEquals("2", row2.get("value"));
        } finally {
            if (xlsReader != null) {
                xlsReader.close();
            }
        }
    }
}
