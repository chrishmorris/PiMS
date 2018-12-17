/**
 * pims-dm org.pimslims.other TimeoutTest.java
 * 
 * @author bl67
 * @date 26 Jun 2009
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2009 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.other;

import org.pimslims.dao.ReadableVersionImpl;
import org.pimslims.test.AbstractTestCase;

/**
 * TimeoutTest
 * 
 */
public class TimeoutTest extends AbstractTestCase {

    public void testTimeout() throws InterruptedException {
        wv = getWV();
        try {
            ((ReadableVersionImpl) wv).setTimeout(1);
            Thread.sleep(1000);
            rv = getRV();
            // create(Target.class);
            assertTrue(wv.isCompleted());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }
}
