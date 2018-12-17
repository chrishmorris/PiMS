/**
 * ColorUtil.java
 *
 *
 * org.pimslims.crystallization.util ColorUtil
 * @author Ian Berry
 * @date 08 October 2007
 *
 * Protein Information Management System
 * @version: 1.3
 *
 * Copyright (c) 2007 Ian Berry, University Of Oxford
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * A copy of the license is in dist/docs/LGPL.txt.
 * It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.business.crystallization.util;

import java.awt.Color;
/**
 * A Set of routines for converting Color values to Hex.
 * @author ian
 */
public class ColorUtil {

    /**
     * constructor.
     */
    public ColorUtil() {
    }
    /**
     * Converts a java.awt.Color color to a Hex value.
     * @param color the color to convert
     * @return A hex version of the color
     */
    public static String convertColorToHex(final Color color) {
        String colourString = "#"
                + toHexString(color.getRed(), 2, '0')
                + toHexString(color.getGreen(), 2, '0')
                + toHexString(color.getBlue(), 2, '0');

        return colourString;
    }

    public static String convertColorToHex(final int colorInfo) {
        Color color = new Color(colorInfo);

        return convertColorToHex(color);
    }
    
    public static String convertColorToHex(final String colorAsInt) {
        Color color = new Color(Integer.parseInt(colorAsInt));
        return convertColorToHex(color);
    }
    public static String convertColorToHex(final Long colorAsLong) {
        Color color = new Color(colorAsLong.intValue());
        return convertColorToHex(color);
    }
    
    
    /**
     * Converts the value to a hex string with a certain length and the given
     * padding character.
     * @param value the value to convert
     * @param len the length of the final string
     * @param pad the character to use for padding
     * @return the Hex string
     */
    public static final String toHexString(final int value, final int len,
            final char pad) {
        StringBuffer sb = new StringBuffer(Integer.toHexString(value));
        int npad = len - sb.length();
        while (npad-- > 0) {
            sb.insert(0, pad);
        }
        return new String(sb);
    }
}
