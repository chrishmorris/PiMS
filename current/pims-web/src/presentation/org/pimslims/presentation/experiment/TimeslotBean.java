/**
 * V4_3-web org.pimslims.presentation.experiment TimeslotBean.java
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

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.pimslims.model.experiment.Instrument;

/**
 * TimeslotBean Represents a 20 minute time slot at an instrument
 */
public class TimeslotBean {

    /**
     * InstrumentTimeslotBean
     * 
     */
    public static class InstrumentTimeslotBean {

        private final boolean booked;

        /**
         * rows int duration/20 mins if this is the start of a booking 0 if this is unbooked, or within a
         * booking
         */
        private final int rows;

        /**
         * name String title
         */
        private final String name;

        public InstrumentTimeslotBean(final boolean booked, final int rows, final String name) {
            super();
            this.booked = booked;
            this.rows = rows;
            this.name = name;
        }

        public boolean isBooked() {
            return this.booked;
        }

        public int getRows() {
            return this.rows;
        }

        public String getName() {
            return this.name;
        }

    }

    public static final String[] SLOTS = new String[] { "0900", "", "", "1000", "", "", "1100", "", "",
        "1200", "", "", "1300", "", "", "1400", "", "", "1500", "", "", "1600", "", "", };

    private final String header;

    private final String start, end;

    private final Map<String, InstrumentTimeslotBean> instruments;

    public TimeslotBean(final String header, final Map<String, InstrumentTimeslotBean> instruments,
        final String start, final String end) {
        super();
        this.header = header;
        this.instruments = instruments;
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return this.start;
    }

    public String getEnd() {
        return this.end;
    }

    public String getHeader() {
        return this.header;
    }

    public static TimeslotBean[] getTimeslots(final Calendar date, final Collection<Instrument> instruments) {
        final TimeslotBean[] ret = new TimeslotBean[TimeslotBean.SLOTS.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = new TimeslotBean(TimeslotBean.SLOTS[i], Collections.EMPTY_MAP, "", "");
        }
        return ret;
    }

    /**
     * TimeslotBean.getInstruments
     * 
     * @return
     */
    public Map<String, InstrumentTimeslotBean> getInstruments() {
        return this.instruments;
    }

}
