/**
 * V4_3-web org.pimslims.presentation TimeZoneTest.java
 * 
 * @author cm65
 * @date 13 Jul 2012
 * 
 *       Protein Information Management System
 * @version: 5.0
 * 
 *           Note that:
 * 
 *           Australia/North and Australia/South are both GMT+9, but only North has Daylight Saving Time.
 * 
 *           Two time zones have the same display name if their current rules are the same. They can still
 *           have different rules for some past dates.
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * TimeZoneTest
 * 
 */
public class TimeZoneTest extends TestCase {

    /**
     * GMT TimeZone
     */
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    /**
     * ALIAS_TABLE Map<String,String>
     */
    private static final Map<String, String> ALIAS_TABLE = sun.util.calendar.ZoneInfo.getAliasTable();

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

    /**
     * DATE String
     */
    private static final String DATE = "2012-01-30 12:59:59";

    private static final Comparator<? super TimeZone> COMPARATOR = new Comparator() {
        @Override
        public int compare(final Object arg0, final Object arg1) {
            final TimeZone zone0 = (TimeZone) arg0;
            final TimeZone zone1 = (TimeZone) arg1;
            final int offset0 = zone0.getRawOffset();
            final int offset1 = zone1.getRawOffset();
            if (offset0 == offset1) {
                return zone0.getDisplayName().compareTo(zone1.getDisplayName());
            }
            return new Integer(offset0).compareTo(new Integer(offset1));
        }
    };

    List<TimeZone> getTimeZones() {
        final String[] ids = TimeZone.getAvailableIDs();
        final List<TimeZone> ret = new ArrayList(ids.length);
        id: for (int i = 0; i < ids.length; i++) {
            final TimeZone zone = TimeZone.getTimeZone(ids[i]);
            for (final Iterator iterator = new ArrayList(ret).iterator(); iterator.hasNext();) {
                final TimeZone timeZone = (TimeZone) iterator.next();
                if (zone.hasSameRules(timeZone)) {
                    if (this.isBetter(zone, timeZone)) {
                        ret.remove(timeZone);
                    } else {
                        continue id;
                    }
                }
            }
            ret.add(zone);
        }
        Collections.sort(ret, TimeZoneTest.COMPARATOR);
        return ret;
    }

    /**
     * TimeZoneTest.isBetter
     * 
     * @param zone
     * @param timeZone
     * @return
     */
    private boolean isBetter(final TimeZone zone1, final TimeZone zone2) {
        final int goodness1 = this.goodness(zone1.getID());
        final int goodness2 = this.goodness(zone2.getID());
        if (goodness1 == goodness2) {
            System.out.println(zone1.getID() + " ? " + zone2.getID());
        }
        return goodness1 > goodness2;
    }

    private int goodness(final String id) {

        // dislike "NZ_CHAT"
        if (id.toUpperCase().equals(id)) {
            return 1;
        }
        // dislike "Etc/GMT-7"
        if (id.startsWith("Etc")) {
            return 0;
        }
        // Prefer Chile/Continental to America/Santiago
        if (id.startsWith("America")) {
            return 2;
        }
        // US/Samoa worse than Pacific/Pago_Pago
        if (id.startsWith("US")) {
            return 3;
        }
        // hate SystemV/HST10
        if (id.startsWith("SystemV")) {
            return Integer.MIN_VALUE;
        }

        //it's fine, but we prefer ones with fewer "/" separators
        return Integer.MAX_VALUE - id.split("/").length;
    }

    Map<String, TimeZone> getTimeZonesByDisplayName() {
        final String[] ids = TimeZone.getAvailableIDs();
        final Map<String, TimeZone> ret = new HashMap(ids.length);
        for (int i = 0; i < ids.length; i++) {
            final TimeZone zone = TimeZone.getTimeZone(ids[i]);
            final String name = zone.getDisplayName();
            if (ret.containsKey(name)) {
                if (!zone.hasSameRules(ret.get(name))) {
                    System.out.println(ids[i] + "!=" + ret.get(name).getID());
                }
            }
            ret.put(name, zone);
        }
        return ret;
    }

    /**
     * TimeZoneTest.testDisplayNameNotUnique Here are two different time zones with the same "display name"
     */
    public void testDisplayNameNotUnique() {
        final TimeZone urumqi = TimeZone.getTimeZone("Asia/Urumqi");
        final TimeZone taipei = TimeZone.getTimeZone("Asia/Taipei");
        Assert.assertEquals(urumqi.getDisplayName(), taipei.getDisplayName());
        Assert.assertFalse(urumqi.hasSameRules(taipei));
    }

    /**
     * TimeZoneTest.testNoSuchTimeZone If you use a bad ID, you get GMT
     */
    public void testNoSuchTimeZone() {
        Assert.assertEquals("Greenwich Mean Time", TimeZone.getTimeZone("nonesuch").getDisplayName());
    }

    public void TODOtestDistinct() {
        final TimeZone[] zones = this.getTimeZones().toArray(new TimeZone[] {});
        for (int i = 0; i < zones.length; i++) {
            for (int j = i + 1; j < zones.length; j++) {
                if (zones[i].hasSameRules(zones[j])) {
                    //System.out.println(zones[i].getID() + " same as " + zones[j].getID());
                    //TODO Assert.fail(zones[i].getID() + " same as " + zones[j].getID());
                }
            }
        }
    }

    /**
     * TimeZoneTest.testCantParseDate Not every timezone id is acceptable to SimpleDateFormat
     */
    public void testCantParseID() {
        final TimeZone zone = TimeZone.getTimeZone("Etc/GMT-14");
        try {
            TimeZoneTest.FORMAT.parse(TimeZoneTest.DATE + zone.getID());
            Assert.fail("");
        } catch (final ParseException e) {
            // that's what happens
        }

    }

    final private static Date NOW = new Date();

    public void testSimpleDateFormatOffset() throws ParseException {

        final SimpleDateFormat utc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        utc.setTimeZone(TimeZoneTest.GMT);
        final String nowUTC = utc.format(TimeZoneTest.NOW);
        Assert.assertEquals(TimeZoneTest.NOW.getTime(), utc.parse(nowUTC).getTime());
        final String[] availableIDs = TimeZone.getAvailableIDs();
        for (int i = 0; i < availableIDs.length; i++) {
            final TimeZone zone = TimeZone.getTimeZone(availableIDs[i]);
            this.doTestOffset(zone);
        }

    }

    /**
     * TimeZoneTest.doTestOffset
     * 
     * @param zone
     * @throws ParseException
     */
    private void doTestOffset(final TimeZone zone) throws ParseException {
        final DateFormat there = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        there.setTimeZone(zone);
        final String nowThere = there.format(TimeZoneTest.NOW);
        // could Assert.assertEquals(TimeZoneTest.NOW.getTime(), utc.parse(nowNST).getTime());
        if (!"GMT+03:07".equals(zone.getDisplayName())) { // there is a 4s error in these
            Assert.assertEquals(zone.getID(), TimeZoneTest.NOW.getTime(), there.parse(nowThere).getTime());
        }

        final DateFormat clockTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        clockTime.setTimeZone(zone);
        final String clockTimeThere = clockTime.format(TimeZoneTest.NOW);
        clockTime.setTimeZone(TimeZoneTest.GMT);
        // parse the clock time there, as if it was at meridian
        final Date parsed = clockTime.parse(clockTimeThere);
        Assert.assertEquals(zone.getOffset(TimeZoneTest.NOW.getTime()),
            parsed.getTime() - TimeZoneTest.NOW.getTime());
    }

    /**
     * TimeZoneTest.testAliasCanBeDifferent
     * 
     * @throws ParseException "Alias" table does not show rules are the same
     */
    public void testAliasCanBeDifferent() throws ParseException {
        Assert.assertEquals("Pacific/Honolulu", TimeZoneTest.ALIAS_TABLE.get("HST"));
        Assert
            .assertFalse(TimeZone.getTimeZone("HST").hasSameRules(TimeZone.getTimeZone("Pacific/Honolulu")));
    }

    /**
     * TimeZoneTest.testAliasTable The values in the alias table are all valid ids
     */
    public void testAliasesAreIDs() {
        final Collection<String> ids = new HashSet(TimeZoneTest.ALIAS_TABLE.values());

        for (final Iterator iterator = ids.iterator(); iterator.hasNext();) {
            final String id = (String) iterator.next();
            final TimeZone zone = java.util.TimeZone.getTimeZone(id);
            Assert.assertEquals(id, zone.getID());
        }
    }

}
