package org.pimslims.lab;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

/**
 * @author Petr Troshin aka pvt43
 * 
 *         Utils
 * 
 */
public class Utils {

    public static class StringFormatter {

        public static final int GENBANK = 2;

        public static final int FASTA = 1;

        public static final int DEFAULT = 0;

        /**
         * Do not add new lines, use spaces only, best option for in html form field display
         */
        public static final int NONE = 2;

        /**
         * Add new line as <br/>
         */
        public static final int HTML = 1;

        /**
         * Add new line as "/n"
         */
        public static final int TEXT = 0;

        public static String getSpaces(final int number, final String space) {
            String spacer = "";
            for (int i = 0; i < number; i++) {
                spacer += space;
            }
            return spacer;
        }

        public static String getSpace(final int escapeStyle) {
            if (escapeStyle == StringFormatter.HTML) {
                return "&nbsp;"; // 1 html space
            }
            return " "; // 1 space
        }

        public static int addNumbers(final int position, int sbPosition, final StringBuffer sb,
            final String strDevider, final String space) {
            final int dsize = new Integer(position).toString().length();
            final String curSpacer = StringFormatter.getSpaces(9 - dsize, space);
            if (position == 1) {
                sb.insert(sbPosition, curSpacer + position + space);
                sbPosition += space.length() + dsize + curSpacer.length();
            } else {
                sb.insert(sbPosition, strDevider + curSpacer + position + space);
                sbPosition += space.length() + strDevider.length() + dsize + curSpacer.length();
            }
            return sbPosition;
        }

        public static String toOneString(final String value) {

            String cleanValue = "";
            for (final StringTokenizer st = new StringTokenizer(value); st.hasMoreTokens();) {
                cleanValue += st.nextToken();
            }
            return cleanValue;
        }

        public static String getFormatedLine(final String sequence) {
            return StringFormatter.getFormatedSequence(10, 6, sequence, StringFormatter.TEXT,
                StringFormatter.DEFAULT);
        }

        /**
         * Format sequence per 80 letter in one string. Without spaces.
         * 
         * @return multiple line formated sequence, one line 80 letters length
         */
        public static String getFormatedSequence(final int gap_distance, final int sets_in_line,
            final String sequence, final int escapeStyle, final int format) {
            if (sequence == null) {
                return "";
            }

            assert gap_distance >= 0 : "Wrong gap_distance parameter ";
            assert sets_in_line >= 0 : "Wrong sets_in_line parameter ";

            final StringBuffer sb = new StringBuffer(StringFormatter.toOneString(sequence));
            for (int i = 0, counter = 0; i < sb.length(); i = i + gap_distance, counter++) {
                if (counter % sets_in_line == 0) {
                    final int position = counter * gap_distance + 1;

                    switch (escapeStyle) {

                        case HTML:
                            if (format == StringFormatter.GENBANK) {
                                i =
                                    StringFormatter.addNumbers(position, i, sb, "<br />",
                                        StringFormatter.getSpace(escapeStyle));
                            } else {
                                sb.insert(i, "<br />");
                                i += "<br />".length();
                            }
                            break;

                        case TEXT:
                            if (format == StringFormatter.GENBANK) {
                                i =
                                    StringFormatter.addNumbers(position, i, sb, "\n",
                                        StringFormatter.getSpace(escapeStyle));
                            } else {
                                sb.insert(i, "\n");
                                i += "\n".length();
                            }
                            break;

                        default: // Let it leak

                        case NONE:
                            if (format == StringFormatter.GENBANK) {
                                i =
                                    StringFormatter.addNumbers(position, i, sb, " ",
                                        StringFormatter.getSpace(escapeStyle));
                            } else {
                                sb.insert(i, " ");
                                i += " ".length();
                            }
                            break;
                    }
                } else {
                    sb.insert(i, StringFormatter.getSpace(escapeStyle));
                    i += StringFormatter.getSpace(escapeStyle).length();
                }
            }
            return sb.toString();
        }

    } // StringFormatter class end

    public static Map deleteNullValues(final Map<String, Object> attributes) {
        final Map<String, Object> cleanedAttr = new HashMap<String, Object>();
        for (final Entry<String, Object> entr : attributes.entrySet()) {
            final Object value = entr.getValue();
            if (value != null) {
                if (value instanceof String[]) {
                    if (((String[]) value).length == 0) {
                        continue;
                    }
                }
                if (value instanceof Collection) {
                    if (((Collection) value).isEmpty()) {
                        continue;
                    }
                }
                if (value instanceof String) {
                    if (((String) value).length() == 0) {
                        continue;
                    }
                }
                cleanedAttr.put(entr.getKey(), value);
            }
        }
        return cleanedAttr;
    }

    public static final String full_date_format = "d MMMMM yyyy";

    public static final String day_date_month_format = "EEEEE d MMMMM";

    /* only the browser can resolve time zone, so this should be used only for reports
     * Excel can interpret this format
     * HTML5 uses yyyy-MM-ddThh:mm:ss, but Excel can't do that.
     * */
    public static final String date_format = "yyyy-MM-dd hh:mm:ss";

    public static final String link_date_format = "yyyy-MM-dd";

    //TODO update this for time+date
    //TODO %Y-%m-%dT%H:%M would match html5 datetime-local
    @Deprecated
    // is this used?
    public static final String calendar_date_format = "%d/%m/%Y";

    /**
     * @return a date format don't pass date as string, only the browser can resolve time zone Note also that
     *         date formats cannot be reused, since they store the time zone
     */
    @Deprecated
    public static SimpleDateFormat getDateFormat() {
        final SimpleDateFormat sdf = new SimpleDateFormat(Utils.date_format);
        return sdf;
    }

    /**
     * Current date in the default PIMS format
     * 
     * @return
     */
    @Deprecated
    //unused
    public static String getDate(Date datevalue) {
        final SimpleDateFormat sdf = new SimpleDateFormat(Utils.date_format);
        if (datevalue == null) {
            datevalue = new Date();
        }
        return sdf.format(datevalue);
    }
}
