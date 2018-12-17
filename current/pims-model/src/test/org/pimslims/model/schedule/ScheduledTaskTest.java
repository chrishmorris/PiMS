/**
 * pims-dm org.pimslims.model.schedule ScheduledTaskTest.java
 * 
 * @author cm65
 * @date 12 May 2010
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.model.schedule;

import java.util.Calendar;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.crystallization.Image;
import org.pimslims.model.holder.Holder;
import org.pimslims.test.AbstractTestCase;

/**
 * ScheduledTaskTest
 * 
 */
public class ScheduledTaskTest extends AbstractTestCase {

    private static final Calendar NOW = Calendar.getInstance();

    /**
     * Test method for {@link org.pimslims.model.schedule.ScheduledTask#setCompletionTime(java.util.Calendar)}
     * .
     * 
     * @throws ConstraintException
     */
    public void testSetCompletionTime() throws ConstraintException {
        WritableVersion version = this.getWV();
        try {
            ScheduledTask task = new ScheduledTask(version);
            task.setScheduledTime(NOW);
            version.flush();
            assertEquals(NOW, task.getScheduledTime());

        } finally {
            version.abort();
        }
    }

    public void testImages() throws ConstraintException {
        WritableVersion version = this.getWV();
        try {
            Holder holder = new Holder(version, UNIQUE);
            ScheduledTask task = new ScheduledTask(version, UNIQUE, Calendar.getInstance(), holder);
            version.flush();
            assertTrue(task.getImages().isEmpty());
            new Image(version, "/path/", "drop.jpg").setScheduledTask(task);
            version.flush();
            assertFalse(task.getImages().isEmpty());
            assertEquals(1, task.getImages(2).size());

        } finally {
            version.abort();
        }
    }

}
