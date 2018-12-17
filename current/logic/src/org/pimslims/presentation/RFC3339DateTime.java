package org.pimslims.presentation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RFC3339DateTime See http://tools.ietf.org/html/rfc3339#section-5.6 Used by HTML5 datetime fields
 */
public class RFC3339DateTime {

    static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    // e.g. 1937-01-01T12:00:27.87+00:20
    private static final Pattern WITH_OFFSET = Pattern.compile("^(.*T.*)(\\+|-)(\\d\\d):(\\d\\d)$");

    private final Calendar calendar;

    public RFC3339DateTime(String rfc3339dateTime) throws ParseException {
        String string = rfc3339dateTime;
        Matcher m = WITH_OFFSET.matcher(rfc3339dateTime);
        if (m.matches()) {
            // remove colon
            string = m.group(1) + m.group(2) + m.group(3) + m.group(4);
        }
        String[] format =
            new String[] { "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ssZ",
                "yyyy-MM-dd'T'HH:mm:ss.S'Z'", "yyyy-MM-dd'T'HH:mm:ss'Z'" };
        this.calendar = Calendar.getInstance(UTC);
        for (int i = 0; i < format.length; i++) {
            DateFormat df = new SimpleDateFormat(format[i]);
            df.setTimeZone(UTC);
            try {
                Date date = df.parse(string);
                this.calendar.setTime(date);
                break;
            } catch (ParseException e) {
                if (i + 1 == format.length) {
                    throw e;
                }
            }
        }
    }

    public RFC3339DateTime(Calendar now) {
        this.calendar = now;
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    /**
     * RFC3339DateTime.toString
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        int offset = this.calendar.getTimeZone().getOffset(this.calendar.getTimeInMillis());
        return this.format(offset / (60 * 1000));
    }

    public String format(int minutes) {
        if (0 == minutes) {
            DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            format1.setTimeZone(UTC);
            return format1.format(this.calendar.getTime());
        }

        String[] ids = TimeZone.getAvailableIDs(minutes * 60 * 1000);
        if (0 == ids.length) {
            // we have to work it out ourselves
            Calendar calendar2 = (Calendar) this.calendar.clone();
            calendar2.add(Calendar.MINUTE, minutes);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            format.setTimeZone(calendar2.getTimeZone());
            String ret = format.format(calendar2.getTime());
            return ret + (minutes > 0 ? '+' : '-')
                + String.format("%1$02d:%2$02d", Math.abs(minutes) / 60, Math.abs(minutes) % 60);
        } else {
            // use java api
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            String id = ids[0];
            for (int i = 0; i < ids.length; i++) {
                //System.out.println(ids[i]);
                if (ids[i].startsWith("Etc")) {
                    id = ids[i];
                }
            }
            format.setTimeZone(TimeZone.getTimeZone(id));
            String ret = format.format(this.calendar.getTime());
            int length = ret.length();
            return ret.substring(0, length - 2) + ":" + ret.substring(length - 2);
        }
    }

}
