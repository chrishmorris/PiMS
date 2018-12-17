/**
 * V4_4-web org.pimslims.presentation.pdf TableImpl.java
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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

public class DrawnTable extends DelayedTable {

    private final PDPageContentStream content;

    private final PDDocument document;

    private final float left = PdfDocument.LEFT_MARGIN;

    private final float right = PdfDocument.PAGE_SIZE.getWidth() - PdfDocument.RIGHT_MARGIN;

    private final PDPage page;

    /**
     * Constructor for TableImpl Actually renders a table, after its start is known
     * 
     * @param top
     * 
     * @param document
     * @param page
     * 
     * 
     * @throws IOException
     */
    public DrawnTable(final String[] title, final float top, final PDDocument document, final PDPage page)
        throws IOException {
        super(title);
        this.document = document;
        this.page = page;

        this.content = new PDPageContentStream(this.document, page, true, true);
        this.content.setFont(PdfDocument.FONT, PdfDocument.FONT_SIZE);
        this.y = top;
        this.vr(PdfDocument.LEFT_MARGIN, TableI.CELL_HEIGHT * title.length);
        this.vr(PDPage.PAGE_SIZE_A4.getUpperRightX() - PdfDocument.RIGHT_MARGIN, TableI.CELL_HEIGHT
            * title.length);
        this.hr();
        this.drawStrings(title, this.left + TableI.PADDING);

        this.hr();
    }

    void hr() throws IOException {
        this.content.drawLine(this.left, this.y, this.right, this.y);
        // some callers should this.y = this.y - TableI.PADDING;
    }

    @Override
    void vr(final float x, final float height) throws IOException {
        this.content.drawLine(x, this.y, x, this.y - height);
    }

    /**
     * @param title
     * @param x
     * @param y
     * @return
     * @throws IOException
     */
    @Override
    float drawString(final String string, final float x, final float y) throws IOException {
        this.content.beginText();
        this.content.moveTextPositionByAmount(x, y - TableI.FONT_HEIGHT);
        this.content.drawString(string);
        this.content.endText();
        return super.drawString(string, x, y);
    }

    /**
     * TableImpl.addValueRow
     * 
     * @param value
     * @throws IOException
     */
    @Override
    void addValueRow(final String value) throws IOException {
        this.drawString(value, TableI.COLUMN_1 + TableI.PADDING, this.y);
        this.vr(PdfDocument.LEFT_MARGIN, TableI.CELL_HEIGHT);
        this.vr(TableI.COLUMN_1, TableI.CELL_HEIGHT);
        this.vr(PDPage.PAGE_SIZE_A4.getUpperRightX() - PdfDocument.RIGHT_MARGIN, TableI.CELL_HEIGHT);
        super.addValueRow(value);
    }

    @Override
    @Deprecated
    public void addKeyCell(final String key) throws IOException {
        this.drawString(key, PdfDocument.LEFT_MARGIN + TableI.PADDING, this.y);
    }

    @Override
    public void addImage(final byte[] bytes, final String mimeType, final String title2) throws IOException {
        this.drawString(title2, PdfDocument.LEFT_MARGIN + TableI.PADDING, this.y);
        try {
            super.addImage(bytes, mimeType, title2);
        } catch (IllegalArgumentException ex) {
            this.drawString(ex.getMessage(), TableI.COLUMN_1 + TableI.PADDING, this.y);
        }
        this.y -= PADDING;
        this.hr();

    }

    /**
     * DrawnTable.addKeyAndValue
     * 
     * @see org.pimslims.presentation.pdf.DelayedTable#addKeyAndValue(java.lang.String, java.lang.String)
     */
    @Override
    public void addKeyAndValue(String key, String value) throws IOException {
        super.addKeyAndValue(key, value);
        this.y = this.y - TableI.PADDING;
        this.hr();
    }

    /**
     * TableImpl.drawImage
     * 
     * @param bufferedImage
     * @param width
     * @param height
     * @param imageTop
     * @throws IOException
     */
    @Override
    void drawImage(final BufferedImage bufferedImage, final float width, final float height, float imageTop)
        throws IOException {

        final PDXObjectImage image = PdfDocument.getPdfImage(this.document, bufferedImage);

        final PDPageContentStream content =
            new PDPageContentStream(this.document, this.page, true, true, false);
        content.setNonStrokingColorSpace(image.getColorSpace());
        content.setStrokingColorSpace(image.getColorSpace());
        // or scale and content.drawImage(image, TableI.COLUMN_1 + TableI.PADDING, imageTop);
        content.drawXObject(image, TableI.COLUMN_1 + TableI.PADDING, imageTop, width, height);
        content.close();
    }

    /**
     * TableI.close
     * 
     * @throws IOException
     * @see org.pimslims.presentation.pdf.TableI#close()
     */
    @Override
    public float close() throws IOException {
        this.content.close();
        return this.y - TableI.PADDING;
    }

    /**
     * @return Returns the y.
     */
    @Override
    public float getY() {
        return y;
    }

    @Override
    public int getRows() {
        return this.rows;
    }

    /**
     * TableImpl.insertNewlines
     * 
     * @param tooWide a string to print
     * @param width the desired width, in ems
     * @return
     * @throws IOException
     */
    public static String insertNewlines(String tooWide, final float width) throws IOException {

        Matcher m = Pattern.compile("(\\s+|-)").matcher(tooWide);
        String ret = "";
        int from = 0;
        while (m.find()) {
            String next = tooWide.substring(from, m.start()) /* + m.group() */;
            ret = append(ret, next, width);
            from = m.start();
        }
        // no more breaks to end of string
        ret = append(ret, tooWide.substring(from), width);
        return ret + "\n";
    }

    /**
     * TableImpl.append
     * 
     * @param buffer the string so far
     * @param next the part to add
     * @param width the maximum width
     * @return
     * @throws IOException
     */
    private static String append(String buffer, String next, final float width) throws IOException {

        String tail = buffer.substring(1 + buffer.lastIndexOf('\n'));
        float widthSoFar = PdfDocument.width(tail);

        if (widthSoFar + PdfDocument.width(next) >= 1000 * width) {
            if (!"".equals(buffer)) {
                buffer += "\n"; // it won't fit on the current line
            }
            if (PdfDocument.width(next) >= 1000 * width) {
                // it won't fit on a line at all
                int last = next.length();
                while (PdfDocument.width(next.substring(0, last)) >= 1000 * width) {
                    last = last - 1;
                }
                // now 0..last will fit
                buffer = buffer + next.substring(0, last);
                return append(buffer, next.substring(last), width);
            }
        }
        // it fits
        buffer += next;
        return buffer;
    }

}
