/**
 * V4_4-web org.pimslims.presentation.pdf TableI.java
 * 
 * @author cm65
 * @date 28 Sep 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.pdf;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPage;

/**
 * TableI
 * 
 */
public interface TableI {

    /**
     * COLUMN_1 int
     */
    public static final float COLUMN_1 = 0.6f * PDPage.PAGE_SIZE_A4.getLowerLeftX() + 0.4f
        * PDPage.PAGE_SIZE_A4.getUpperRightX();

    /**
     * FONT_HEIGHT float
     */
    static final float FONT_HEIGHT = PdfDocument.FONT_SIZE
        * PdfDocument.FONT.getFontDescriptor().getFontBoundingBox().getHeight() / 1000;

    static final float PADDING = 2f;

    static final float CELL_HEIGHT = 2 * DelayedTable.PADDING + DelayedTable.FONT_HEIGHT;

    static final float VALUE_EMS =
        (PDPage.PAGE_SIZE_A4.getUpperRightX() - COLUMN_1 - 2 * DelayedTable.PADDING) / PdfDocument.FONT_SIZE;

    static final float TITLE_EMS = (PDPage.PAGE_SIZE_A4.getUpperRightX()
        - PDPage.PAGE_SIZE_A4.getLowerLeftX() - 2 * DelayedTable.PADDING)
        / PdfDocument.FONT_SIZE;

    /**
     * TableI.setSpacingAfter
     * 
     * @param i
     */

    void addKeyAndValue(String key, String value) throws IOException;

    /**
     * TableI.addImage
     * 
     * @param png
     * @param string
     * @param string2
     */
    void addImage(byte[] png, String string, String string2) throws IOException;

    /**
     * TableI.close
     * 
     * @return
     */
    float close() throws IOException;

    int getRows();

    String[] getTitle();

}
