/*
 * Created on 04.03.2005
 */
package org.pimslims.lab;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This is an utility class for generation different formats of data.
 * 
 * @author Petr Troshin
 */
public class StuffProducer {

    private StuffProducer() { /* empty */
    }

    /**
     * Array contains all non command characters of English keyboard
     */
    static char[] characters =
        { 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', '[', ']', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k',
            'l', ';', '#', 'z', 'x', 'c', 'v', 'b', 'n', 'm', ',', '.', '/', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', '0', '-', '=', '!', '$', '\'', '$', '%', '^', '&', '*', '(', ')', '_', '+', '?',
            '~', ':', '@', 'K', '>', '<', '|', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S',
            'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M' };

    /**
     * Array contains type characters and digits of English keyboard
     */
    public static char[] typeAndDigitCharacters =
        { 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z',
            'x', 'c', 'v', 'b', 'n', 'm', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'K', 'Q', 'W',
            'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X',
            'C', 'V', 'B', 'N', 'M', '_' };

    /**
     * Array contains only type characters of English keyboard
     */
    public static char[] typeCharacters =
        { 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z',
            'x', 'c', 'v', 'b', 'n', 'm', 'K', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S',
            'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '_' };

    /**
     * Method returns random positive int
     * 
     * @return int
     */
    public static int getint() {
        final Random r = new Random();
        final long time = new Double(Calendar.getInstance().getTimeInMillis() * Math.random()).longValue();
        String ints = new Long((r.nextInt() * time)).toString();
        final int substrpos = new Double(ints.length() * Math.random()).intValue();
        ints = ints.substring(substrpos);
        final int i = Math.abs(new Long(ints).intValue());
        // System.out.println(i);
        return i;
    }

    /**
     * Method returns random positive Integer
     * 
     * @return Integer
     */
    public static Integer getInt() {
        return new Integer(StuffProducer.getint());
    }

    /**
     * Method returns random positive float
     * 
     * @return float
     */
    public static float getfloat() {
        return StuffProducer.getLong().floatValue();
    }

    /**
     * Method returns random positive Float
     * 
     * @return Float
     */
    public static Float getFloat() {
        return new Float(StuffProducer.getfloat());
    }

    /**
     * Method returns random positive double
     * 
     * @return double
     */
    public static double getdouble() {
        final Random r = new Random();
        return new Double(r.nextDouble() * StuffProducer.getint()).doubleValue();
    }

    /**
     * Method returns random positive Double
     * 
     * @return Double
     */
    public static Double getDouble() {
        return new Double(StuffProducer.getdouble());
    }

    /**
     * Method returns random positive long
     * 
     * @return long
     */
    public static long getlong() {
        final Random r = new Random();
        return (new Long(r.nextLong() * StuffProducer.getint())).longValue();
    }

    /**
     * Method returns random positive Long
     * 
     * @return Long
     */
    public static Long getLong() {
        return new Long(StuffProducer.getlong());
    }

    /**
     * Method returns random booleans
     * 
     * @return boolean
     */
    public static boolean getboolean() {
        final Random r = new Random();
        return r.nextBoolean();
    }

    /**
     * Method returns random Booleans
     * 
     * @return Boolean
     */
    public static Boolean getBoolean() {
        return new Boolean(StuffProducer.getboolean());
    }

    /**
     * Method returns randomly generated string of the same format as network MAC address
     * 
     * @return String
     */
    public static String macaddrProducer() {
        String macaddr = "";
        final Random r = new Random();
        for (int i = 0; i < 5; i++) {
            macaddr += Integer.toHexString(r.nextInt(16));
            macaddr += ":" + r.nextInt(10);
        }
        return r.nextInt(10) + macaddr + r.nextInt(10);
    }

    /**
     * Method returns randomly generated string with length no more than 1000 characters. Generated strings
     * can contain all characters (digits, punctuation marks, brackets etc.)
     * 
     * @return text
     */
    public static String getString() {
        final Random r = new Random();
        String text = "";
        for (int i = 0; i < r.nextInt(1000); i = i + 2) {
            text +=
                Character.toString(StuffProducer.characters[r.nextInt(StuffProducer.characters.length - 1)]);
            text +=
                Character.toString(StuffProducer.characters[(new Double(Math.random()
                    * (StuffProducer.characters.length - 1))).intValue()]);
            // System.out.println("iteration " + i + " text " + text);
        }
        // System.out.println("text lenth " + text.length());
        return text;
    }

    /**
     * Method returns randomly generated string with length no more than 1000 characters. Strings are
     * generated only from character defined in the characters[]
     * 
     * @param characters Array of characters to use to generate string. You can use statically defined ones
     *            from this class.
     * @return text
     */
    public static String getString(final char[] characters) {
        final Random r = new Random();
        String text = "";
        for (int i = 0; i < r.nextInt(1000); i = i + 2) {
            text += Character.toString(characters[r.nextInt(characters.length - 1)]);
            text +=
                Character.toString(characters[(new Double(Math.random() * (characters.length - 1)))
                    .intValue()]);
            // System.out.println("iteration " + i + " text " + text);
        }
        // System.out.println("text lenth " + text.length());
        return text;
    }

    /**
     * Method generates the strings of a random length but the length of generated strings are no more than
     * textLenght. Strings are formed by randomly choosing character from statically defined character array.
     * Generated strings can contain all characters (digits, punctuation marks, brackets etc.)
     * 
     * @param textLength
     * @return text
     * @see StuffProducer.characters
     */
    public static String getString(final int textLength) {
        final Random r = new Random();
        String text = "";
        assert (textLength >= 0);
        for (int i = 1; i < textLength; i = i + 2) {
            text +=
                Character.toString(StuffProducer.characters[r.nextInt(StuffProducer.characters.length - 1)]);
            text +=
                Character.toString(StuffProducer.characters[(new Double(Math.random()
                    * (StuffProducer.characters.length - 1))).intValue()]);
            // System.out.println("iteration " + i + " text " + text);
        }
        // System.out.println("text lenth " + text.length());
        return text;
    }

    /**
     * Method generates the strings of a random length but the length of generated strings are no more than
     * textLenght. Strings are formed by randomly choosing character from statically defined character array.
     * Strings are generated only from character defined in the provided characters array.
     * 
     * @param characters Array of characters to use to generate string. You can use statically defined ones
     *            from this class.
     * @param textLength
     * @return text
     * @see StuffProducer.characters
     */
    public static String getString(final char[] characters, final int textLength) {
        final Random r = new Random();
        String text = "";
        assert (textLength >= 0);
        for (int i = 1; i < textLength; i = i + 2) {
            text += Character.toString(characters[r.nextInt(characters.length - 1)]);
            text +=
                Character.toString(characters[(new Double(Math.random() * (characters.length - 1)))
                    .intValue()]);
            // System.out.println("iteration " + i + " text " + text);
        }
        // System.out.println("text lenth " + text.length());
        return text;
    }

    /**
     * Method returns the List (to be precise ArrayList) The List size between 0 and 10 The List filled by
     * randomly generated strings with size no more than fieldLenght
     * 
     * @param fieldLength
     * @return List
     */
    public static List getList(final int fieldLength) {
        final Random r = new Random();
        final ArrayList l = new ArrayList();
        final int arraylen = r.nextInt(10);
        for (int i = 0; i < arraylen; i++) {
            l.add(StuffProducer.getString(fieldLength));
        }
        return l;
    }

    /**
     * Method returns the String Array The Array size between 0 and 10 The Array filled by randomly generated
     * strings with size no more than fieldLenght
     * 
     * @param fieldLength
     * @return Array
     */
    public static String[] getStringArray(final int fieldLength) {
        final Random r = new Random();
        final int arraylen = r.nextInt(10);
        final String[] sar = new String[arraylen];
        for (int i = 0; i < arraylen; i++) {
            sar[i] = StuffProducer.getString(fieldLength);
        }
        return sar;
    }

    /**
     * Method returns the String Array The Array size between 0 and 10 The Array filled with randomly
     * generated strings
     * 
     * @param fieldLength
     * @return Array
     */
    public static String[] getStringArray() {
        final Random r = new Random();
        final int arraylen = r.nextInt(10);
        final String[] sar = new String[arraylen];
        for (int i = 0; i < arraylen; i++) {
            sar[i] = StuffProducer.getString();
        }
        return sar;
    }

    /**
     * Method returns the Collection (to be precise ArrayList) The Collection size between 0 and 10 The
     * Collection filled by randomly generated strings with size no more than fieldLenght
     * 
     * @param fieldLength
     * @return Collection
     */
    public static Collection getCollection(final int fieldLength) {
        return StuffProducer.getList(fieldLength);
    }

    /**
     * Method returns the Map filled by randomly generated strings as key as well as values. Key string length
     * is random but not more than 10 characters. Values string length is random but not more than 50
     * characters. May return empty keys as well as values.
     * 
     * @return Map
     */
    public static Map getMap() {
        final Random r = new Random();
        final HashMap h = new HashMap();
        final int len = r.nextInt(10);
        for (int i = 0; i < len; i++) {
            h.put(StuffProducer.getString(r.nextInt(10)), StuffProducer.getString(r.nextInt(50)));
        }
        return h;
    }

    /**
     * Method gets the ArrayList as a key values returns Map filled by randomly generated strings. Strings
     * length is random but not more than 50 characters. May generate empty values.
     * 
     * @param keys
     * @return Map
     */
    public static Map getMap(final ArrayList keys) {
        final Random r = new Random();
        final HashMap h = new HashMap();
        for (final Iterator iter = keys.iterator(); iter.hasNext();) {
            final String element = (String) iter.next();
            h.put(element, StuffProducer.getString(r.nextInt(50)));
        }
        return h;
    }

    /**
     * Method gets the String[] as a key values May generate empty values.
     * 
     * @param keys
     * @return Map - HashMap filled by strings of random length more than 50 characters.
     */
    public static Map getMap(final String[] keys) {
        final Random r = new Random();
        final HashMap h = new HashMap();
        for (int i = 0; i < keys.length; i++) {
            h.put(keys[i], StuffProducer.getString(r.nextInt(50)));
        }
        return h;
    }

    /**
     * Method not used saved it is here for testing purposes only
     * 
     * @param args
     */
    public static void main(final String[] args) {
        /* empty */
    }

} // class end
