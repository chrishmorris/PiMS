package org.pimslims.presentation.pdf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDPage;

public class DelayedTable implements TableI {

    final String[] title;

    /**
     * y Minus the height of the table
     */
    protected float y;

    protected int rows = 0;

    private final List<String[]> keyValuePairs = new ArrayList();

    private final Map<String, byte[]> images = new HashMap();

    /**
     * Constructor for DelayedTable
     * 
     * @throws IOException
     */
    public DelayedTable(String title) throws IOException {
        this(DrawnTable.insertNewlines(title, VALUE_EMS).split("\n"));
    }

    /**
     * Constructor for DelayedTable
     * 
     * @throws IOException
     */
    public DelayedTable(String[] title) throws IOException {
        super();
        this.y = -TableI.FONT_HEIGHT - TableI.PADDING;
        this.title = title;
    }

    /**
     * @return Returns the title.
     */
    @Override
    public String[] getTitle() {
        return title;
    }

    /**
     * DelayedTable.close
     * 
     * @see org.pimslims.presentation.pdf.TableI#close()
     */
    @Override
    public float close() throws IOException {
        this.y = this.y - 2 * TableI.PADDING;
        return this.y;
        // now it is ready for rendering
    }

    /**
     * DelayedTable.getRows
     * 
     * @see org.pimslims.presentation.pdf.TableI#getRows()
     */
    @Override
    public int getRows() {
        return this.rows;
    }

    void addValueRow(String value) throws IOException { //TODO need similar addTitleRow
        this.y = this.y - TableI.FONT_HEIGHT - TableI.PADDING;
        this.rows = this.rows + 1;
    }

    @Override
    public void addKeyAndValue(String key, String value) throws IOException {
        this.addKeyCell(key);
        this.addValueCell(value);
        this.keyValuePairs.add(new String[] { key, value });
        // was this.hr();
    }

    void addValueCell(String value) throws IOException {
        if (null == value) {
            value = "";
        }
        String[] rows = DrawnTable.insertNewlines(value, VALUE_EMS).split("\n");
        for (int i = 0; i < rows.length; i++) {
            addValueRow(rows[i]);
        }
    }

    void addKeyCell(String key) throws IOException {
    }

    void drawImage(BufferedImage image, float width, float height, float imageTop) throws IOException {
        // nothing to do

    }

    void vr(float leftMargin, float f) throws IOException {
        // nothing to do
    }

    /**
     * TableImpl.doAddImage
     * 
     * @param bytes
     * @param title2
     * @throws IOException
     */
    @Override
    public void addImage(final byte[] bytes, final String mimeType, final String title2) throws IOException {
        final java.awt.image.BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
        if (null == bufferedImage) {
            throw new IllegalArgumentException("Sorry, cannot show this image: " + title2);
        }
        if (bufferedImage.getColorModel().hasAlpha()) {
            // if we try to show this, all images get damaged
            throw new IllegalArgumentException("Sorry, cannot show images with transparency");
        }

        final float width =
            PDPage.PAGE_SIZE_A4.getUpperRightX() - TableI.COLUMN_1 - PdfDocument.RIGHT_MARGIN - 2
                * TableI.PADDING;
        final float height = width * bufferedImage.getHeight() / bufferedImage.getWidth();
        this.vr(PdfDocument.LEFT_MARGIN, height + 2 * TableI.PADDING);
        this.vr(TableI.COLUMN_1, height + 2 * TableI.PADDING);
        this.vr(PDPage.PAGE_SIZE_A4.getUpperRightX() - PdfDocument.RIGHT_MARGIN, height + 2 * TableI.PADDING);

        this.y = this.y - height - TableI.PADDING;
        // was this.hr(); // this.y = this.y - TableI.PADDING;
        float imageTop = this.y;

        drawImage(bufferedImage, width, height, imageTop);
        // TODO this isn't really good enough, titles may not be unique and
        // order should be preserved
        this.images.put(title2, bytes);
        this.rows = this.rows + 1;
    }

    public List<String[]> getKeyValuePairs() {
        return this.keyValuePairs;
    }

    public float getY() {
        return this.y;
    }

    public Map<String, byte[]> getImages() {
        return this.images;
    }

    // calculate space for a line of text
    float drawString(String string, float x, float y) throws IOException {
        return this.y - TableI.FONT_HEIGHT - TableI.PADDING;
    }

    protected void drawStrings(String[] line, float x) throws IOException {
        for (int i = 0; i < line.length; i++) {
            this.y = this.drawString(line[i], x, this.y);
        }
    }
}
