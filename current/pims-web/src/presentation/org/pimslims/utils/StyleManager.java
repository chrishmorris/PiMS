/**
 * V2_2-pims-web org.pimslims.utils StyleManager.java
 * 
 * @author pvt43
 * @date 3 Nov 2008
 * 
 * Protein Information Management System
 * @version: 2.2
 * 
 * Copyright (c) 2008 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;

/**
 * StyleManager
 * 
 * Class support a collection of colors used by HSSFCell to define the color of the font
 */
public class StyleManager implements Iterator<Short> {

    class StyleInUse {
        // Can contain only two values
        Short[] colors;

        Double value;

        StyleInUse(final Double value, final Short color) {
            this.value = value;
            if (this.colors == null) {
                this.colors = new Short[2];
            }
            this.colors[0] = color;
        }

        public Short getNextColor(final Short usedColor) {
            if (usedColor == this.colors[0]) {
                if (this.colors[1] == null && StyleManager.this.iter.hasNext()) {
                    this.colors[1] = StyleManager.this.iter.next();
                }
                return this.colors[1];
            }
            return this.colors[0];
        }

        boolean hasValue(final Double value) {
            return this.value.equals(value);
        }

    }

    final List<StyleInUse> usedStyles;

    final Short[] styles =
        new Short[] { HSSFColor.BLUE.index, HSSFColor.BLACK.index, HSSFColor.BROWN.index,
            HSSFColor.DARK_BLUE.index, HSSFColor.DARK_GREEN.index, HSSFColor.ORANGE.index,
            HSSFColor.AQUA.index, HSSFColor.CORAL.index, HSSFColor.GREEN.index,
            HSSFColor.CORNFLOWER_BLUE.index, HSSFColor.DARK_TEAL.index, HSSFColor.GREY_80_PERCENT.index,
            HSSFColor.GOLD.index, HSSFColor.INDIGO.index, HSSFColor.OLIVE_GREEN.index, HSSFColor.PLUM.index,
            HSSFColor.PALE_BLUE.index, HSSFColor.GREY_50_PERCENT.index, HSSFColor.DARK_RED.index,
            HSSFColor.DARK_YELLOW.index, HSSFColor.BLUE_GREY.index };

    final Iterator<Short> iter;

    public StyleManager() {
        final List<Short> astyle = Arrays.asList(this.styles);
        this.iter = astyle.iterator();
        this.usedStyles = new ArrayList<StyleInUse>();
    }

    /**
     * Iterator<Short>.hasNext
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return this.iter.hasNext();
    }

    /**
     * Iterator<Short>.next
     * 
     * @see java.util.Iterator#next()
     */
    public Short next() {
        return this.iter.next();
    }

    /**
     * Iterator<Short>.remove
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        this.iter.remove();
    }

    public Short getNextColor(final Double value, Short color) {
        // Check whether the color for the same volume has been given
        // if so give different color this time
        if (color != null) {
            for (final StyleInUse style : this.usedStyles) {
                if (style.hasValue(value)) {
                    return style.getNextColor(color);
                }
            }
        }
        // First time request OR No color for this volume was given, give new color 
        if (this.hasNext()) {
            color = this.next();
        }
        final StyleInUse nextStyle = new StyleInUse(value, color);
        this.usedStyles.add(nextStyle);
        return color;
    }

}
