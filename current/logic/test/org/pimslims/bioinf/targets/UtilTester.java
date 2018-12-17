package org.pimslims.bioinf.targets;

import org.pimslims.lab.Util;

import junit.framework.TestCase;

public class UtilTester extends TestCase {

    public void testisEmpty() {
        assertTrue(Util.isEmpty(""));
        assertTrue(Util.isEmpty(" "));
        assertTrue(Util.isEmpty(null));
        assertTrue(Util.isEmpty("\n\n"));
        assertFalse(Util.isEmpty(" s "));
        assertFalse(Util.isEmpty(" 1"));
    }
}
