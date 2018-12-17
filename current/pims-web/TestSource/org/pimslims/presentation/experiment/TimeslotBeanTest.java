/**
 * V4_3-web org.pimslims.presentation.experiment TimeslotBeanTest.java
 * 
 * @author cm65
 * @date 9 Jul 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.experiment;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.presentation.experiment.TimeslotBean.InstrumentTimeslotBean;

/**
 * TimeslotBeanTest
 * 
 */
public class TimeslotBeanTest extends TestCase {

    public void testRows() {
        final Map<String, InstrumentTimeslotBean> instruments = new HashMap();
        instruments.put("Akta1", new InstrumentTimeslotBean(true, 1, "SOD1"));
        final int rows =
            new TimeslotBean("0900", instruments, "0900", "0920").getInstruments().values().iterator().next()
                .getRows();
        Assert.assertEquals(1, rows);
    }

    //TODO create instrument and ScheduledTask, check it is rendered as a bean.
}
