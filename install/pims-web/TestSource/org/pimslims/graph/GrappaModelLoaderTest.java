/**
 * V2_0-pims-web org.pimslims.utils.graph GrappaModelLoaderTest.java
 * 
 * @author cm65
 * @date 11 Jan 2008
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2008 cm65 
 * 
 * 
 */
package org.pimslims.graph;

import junit.framework.TestCase;

/**
 * GrappaModelLoaderTest
 * 
 */
public class GrappaModelLoaderTest extends TestCase {

    /**
     * @param name
     */
    public GrappaModelLoaderTest(String name) {
        super(name);
    }

    /**
     * Test method for {@link org.pimslims.utils.graph.GrappaModelLoader#escape(java.lang.String)}.
     */
    public void testEscapeEmpty() {
        assertEquals("", GrappaModelLoader.escape(""));
    }

    /**
     * Test method for {@link org.pimslims.utils.graph.GrappaModelLoader#escape(java.lang.String)}.
     */
    public void testEscapeNewLine() {
        assertEquals("a b", GrappaModelLoader.escape("a\nb"));
        assertEquals("a  b", GrappaModelLoader.escape("a\n\nb"));
    }

    /**
     * Test method for {@link org.pimslims.utils.graph.GrappaModelLoader#escape(java.lang.String)}.
     */
    public void testEscapeQuote() {
        assertEquals("a&#034;b", GrappaModelLoader.escape("a\"b"));
    }

    /**
     * Test method for {@link org.pimslims.utils.graph.GrappaModelLoader#escape(java.lang.String)}.
     */
    public void testEscapeSlash() {
        assertEquals("a\\\\b", GrappaModelLoader.escape("a\\b"));
    }

}
