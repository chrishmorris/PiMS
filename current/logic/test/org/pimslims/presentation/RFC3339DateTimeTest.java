package org.pimslims.presentation;

import java.text.ParseException;
import java.util.Calendar;
import java.util.TimeZone;

import junit.framework.TestCase;

public class RFC3339DateTimeTest extends TestCase {

    private static final TimeZone CET = TimeZone.getTimeZone("Europe/Berlin");

    public void testCalendar() throws ParseException {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(RFC3339DateTime.UTC);
        String string = new RFC3339DateTime(now).toString();
        assertEquals(now, new RFC3339DateTime(string).getCalendar());
    }

    public void testZ() throws ParseException {
        Calendar calendar = new RFC3339DateTime("1985-04-12T23:20:50Z").getCalendar();
        assertEquals("1985-04-12T23:20:50.000Z", new RFC3339DateTime(calendar).toString());
    }

    public void testOffset() throws ParseException {
        Calendar calendar1 = new RFC3339DateTime("1996-12-19T16:39:57-08:00").getCalendar();
        Calendar calendar2 = new RFC3339DateTime("1996-12-20T00:39:57Z").getCalendar();
        assertEquals(calendar2.getTimeInMillis(), calendar1.getTimeInMillis());
    }

    public void testSSSOffset() throws ParseException {
        Calendar calendar1 = new RFC3339DateTime("1996-12-19T16:39:57.123-08:00").getCalendar();
        Calendar calendar2 = new RFC3339DateTime("1996-12-20T00:39:57.123Z").getCalendar();
        assertEquals(calendar2.getTimeInMillis(), calendar1.getTimeInMillis());
    }

    public void testFormatOffset() throws ParseException {
        RFC3339DateTime date = new RFC3339DateTime("1996-12-20T00:39:57.123Z");
        assertEquals("1996-12-19T16:39:57.123-08:00", date.format(-8 * 60));
    }

    public void testUknownTimeZone() throws ParseException {
        RFC3339DateTime date = new RFC3339DateTime("1937-01-01T12:00:27.870+00:20");
        assertEquals("1937-01-01T12:00:27.870+00:20", date.format(+20));
    }

    public void testTimeZone() throws ParseException {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(CET);
        String string = new RFC3339DateTime(now).toString();
        String offset = "+01:00";
        if (CET.inDaylightTime(now.getTime())) {
            offset = "+02:00";
        }
        assertTrue(string, string.endsWith(offset));
        assertEquals(now.getTime(), new RFC3339DateTime(string).getCalendar().getTime());
    }

    public void testSSSZ() throws ParseException {
        Calendar calendar = new RFC3339DateTime("1985-04-12T23:20:50.527Z").getCalendar();
        assertEquals("1985-04-12T23:20:50.527Z", new RFC3339DateTime(calendar).toString());
    }

    /* This is a legal format, see http://tools.ietf.org/html/rfc3339#section-5.6
     * but we do not actually encounter it
    public void testSSZ() throws ParseException {
        Calendar calendar = new RFC3339DateTime("1985-04-12T23:20:50.52Z").getCalendar();
        assertEquals("1985-04-12T23:20:50.520Z", new RFC3339DateTime(calendar).toString());
    } */

}
