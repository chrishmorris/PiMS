/**
 * V4_3-web org.pimslims.utils SVGTest.java
 * 
 * @author cm65
 * @date 8 Nov 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.w3c.dom.Document;

/**
 * SVGTest
 * 
 */
public class SVGTest extends TestCase {

    /**
     * EXAMPLE String
     */
    private static final String EXAMPLE = "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">\r\n"
        + "  <circle cx=\"100\" cy=\"50\" r=\"40\" stroke=\"black\"\r\n"
        + "  stroke-width=\"2\" fill=\"red\" />\r\n" + "</svg>";

    /**
     * Constructor for SVGTest
     * 
     * @param name
     */
    public SVGTest(final String name) {
        super(name);
    }

    /**
     * SVGTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * SVGTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link org.pimslims.utils.SVG#SVG(java.lang.String)}.
     */
    public void testSVG() {
        new SVG(SVGTest.EXAMPLE);
    }

    /**
     * Test method for {@link org.pimslims.utils.SVG#getDocument()}.
     */
    public void testGetDocument() {
        final Document document = new SVG(SVGTest.EXAMPLE).getDocument();
        final String text = document.getTextContent();
        Assert.assertEquals("", text);
    }

    /**
     * Test method for {@link org.pimslims.utils.SVG#getHeight()}.
     */
    public void testGetHeight() {
        final String height = new SVG(SVGTest.EXAMPLE).getHeight();
        Assert.assertEquals("", height);
    }

    /**
     * Test method for {@link org.pimslims.utils.SVG#getWidth()}.
     */
    public void testGetWidth() {
        final String width = new SVG(SVGTest.EXAMPLE).getWidth();
        Assert.assertEquals("", width);
    }

}
