/**
 * V4_4-web org.pimslims.presentation.pdf PdfDocument.java
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

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpaceFactory;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

/**
 * PdfDocument
 * 
 * Note that coordinates are in points, from the lower left, so the top has large y.
 * 
 */
public class PdfDocument implements DocumentI {

    /**
     * PAGE_SIZE PDRectangle
     */
    static final PDRectangle PAGE_SIZE = PDPage.PAGE_SIZE_A4;

    public static final float PAGE_HEIGHT = PDPage.PAGE_SIZE_A4.getHeight();

    private final PDDocument document;

    private final OutputStream outputStream;

    private PDPage page;

    private float y;

    private final String title;

    static final PDType1Font FONT = PDType1Font.HELVETICA;

    private static final float footerFont = 6f;

    private static final float headerFont = 10f;

    static final float FONT_SIZE = 8f;

    static final float LEFT_MARGIN = 50f;

    static final float RIGHT_MARGIN = 50f;

    private static final float TOP_MARGIN = 50f;

    private static final float PADDING = 2f;

    /**
     * Constructor for PdfDocument
     * 
     * @throws IOException
     */
    public PdfDocument(final OutputStream outputStream, final String title, final String footer)
        throws IOException {
        super();
        this.document = new PDDocument();
        this.outputStream = outputStream;

        this.title = title;
        newPage();
        final PDDocumentInformation metaData = this.document.getDocumentInformation();
        metaData.setTitle(title);
        metaData.setCreationDate(Calendar.getInstance());
        metaData.setProducer("Protein Information Management System");
        this.setFooter(footer);
        // TODO this.document.setMargins(50, 50, 50, 50);
    }

    /**
     * PdfDocument.newPage
     * 
     * @throws IOException
     */
    private void newPage() throws IOException {
        this.page = new PDPage(PdfDocument.PAGE_SIZE);
        this.page.setMediaBox(PdfDocument.PAGE_SIZE);
        this.document.addPage(this.page);
        this.y = PdfDocument.PAGE_SIZE.getUpperRightY();
        addTitle();
    }

    private void addTitle() throws IOException {
        final PDPageContentStream content = this.getContentStream();
        content.setFont(PdfDocument.FONT, PdfDocument.headerFont);
        this.y = this.y - PdfDocument.TOP_MARGIN;
        content.moveTextPositionByAmount(PdfDocument.LEFT_MARGIN, this.y);
        content.drawString(title + "\n");
        content.endText();
        content.close();
        this.y =
            this.y - PdfDocument.headerFont
                * PdfDocument.FONT.getFontDescriptor().getFontBoundingBox().getHeight() / 1000;
    }

    private void setFooter(final String footer) throws IOException {

        final PDPageContentStream content = this.getContentStream();
        content.setFont(PdfDocument.FONT, PdfDocument.footerFont);
        content.moveTextPositionByAmount(PdfDocument.LEFT_MARGIN,
            (PdfDocument.PAGE_SIZE.getLowerLeftY() + 30f));
        content.drawString(footer + "\n");
        content.endText();
        content.close();

    }

    /**
     * @return
     * @throws IOException
     */
    private PDPageContentStream getContentStream() throws IOException {
        final PDPageContentStream content = new PDPageContentStream(this.document, this.page, true, true);
        content.beginText();
        content.setFont(FONT, FONT_SIZE);
        return content;
    }

    @Override
    public TableI createTable(final String title) throws IOException {
        final TableI table = new DelayedTable(title);

        return table;
    }

    @Override
    public void addTable(final TableI table) throws IOException {
        DelayedTable table1 = (DelayedTable) table;
        if (this.y + table1.getY() < 0) {
            // it does not fit on page
            newPage();
        }
        DelayedTable table2 = new DrawnTable(table.getTitle(), this.y, this.document, this.page);
        List<String[]> kvps = table1.getKeyValuePairs();
        for (Iterator iterator = kvps.iterator(); iterator.hasNext();) {
            String[] strings = (String[]) iterator.next();
            table2.addKeyAndValue(strings[0], strings[1]);
        }
        Map<String, byte[]> images = table1.getImages();
        for (Iterator iterator = images.keySet().iterator(); iterator.hasNext();) {
            String string = (String) iterator.next();
            table2.addImage(images.get(string), "", string);
        }
        this.y = table2.close() - 2 * PADDING;
    }

    @Override
    public void addAuthor(final String arg0) {
        this.document.getDocumentInformation().setAuthor(arg0);
    }

    @Override
    public void addTitle(final String arg0) {
        this.document.getDocumentInformation().setTitle(arg0);
    }

    @Override
    public void close() throws IOException {
        try {
            this.document.save(this.outputStream);
            this.document.close();
        } catch (final COSVisitorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addImage(final byte[] graph) throws MalformedURLException, IOException {

        ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(graph));
        java.awt.image.BufferedImage bufferedImage = ImageIO.read(iis);
        /* if (null == bufferedImage) {            
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("png");
            assert readers.hasNext();
            ImageReader reader = readers.next();
            reader.setInput(iis, true, true);
            bufferedImage = reader.read(reader.getMinIndex(), null);
        } */
        if (null == bufferedImage) {
            return; // sometimes fails for PNGs made by GraphViz.
        }
        final PDXObjectImage image = PdfDocument.getPdfImage(this.document, bufferedImage);
        if (null == image) {
            return; // only succeeds with some images
        }
        final float width =
            PdfDocument.PAGE_SIZE.getUpperRightX() - PdfDocument.PAGE_SIZE.getLowerLeftX()
                - PdfDocument.LEFT_MARGIN - PdfDocument.RIGHT_MARGIN;
        final float height = width * image.getHeight() / image.getWidth();
        // System.out.println("" + width + "*" + height);
        final PDPageContentStream content = new PDPageContentStream(this.document, this.page, true, true);
        content.setNonStrokingColorSpace(image.getColorSpace());
        content.setStrokingColorSpace(image.getColorSpace());
        // float x = (PAGE_SIZE.getLowerLeftX() + PAGE_SIZE.getUpperRightX()) /
        // 2 - width / 2;
        content.drawXObject(image, PdfDocument.LEFT_MARGIN, this.y - height, width, height);
        content.close();
        this.y = this.y - height - PdfDocument.PADDING;
    }

    static PDXObjectImage getPdfImage(final PDDocument document2,
        final java.awt.image.BufferedImage bufferedImage) throws IOException {
        assert null != bufferedImage;
        final ColorModel fromModel = bufferedImage.getColorModel();

        // System.out.println(fromModel + "\n" + fromModel.getColorSpace());

        PDXObjectImage ret;
        try {
            ret = new PDPixelMap(document2, bufferedImage);
        } catch (final IllegalArgumentException e) {
            if (e.getMessage().contains("is incompatible with ColorModel")) {
                if (null == fromModel) {
                    e.printStackTrace();
                    return null; // don't know how to recover
                }
                final PDColorSpace space =
                    PDColorSpaceFactory.createColorSpace(document2, fromModel.getColorSpace());
                ret =
                    new PDPixelMap(document2, PdfDocument.convert(bufferedImage, space.createColorModel(8)));
            } else {
                throw e;
            }
        }
        return ret;
    }

    private static BufferedImage convert(final BufferedImage from, final ColorModel to) {
        final RenderingHints hints =
            new RenderingHints(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
        final ColorConvertOp converter = new ColorConvertOp(hints);
        return converter.createCompatibleDestImage(from, to);

    }

    @Override
    public void addCreationDate() {
        this.document.getDocumentInformation().setCreationDate(Calendar.getInstance());
    }

    /**
     * @return Returns the y.
     */
    public float getY() {
        return y;
    }

    /**
     * PdfDocument.width
     * 
     * @param string
     * @return the width of the string when rendered, in 0.001 ems Value in points is this times FONT_SIZE
     *         /1000
     * @throws IOException
     */
    public static float width(String string) throws IOException {
        return (FONT.getStringWidth(string));
    }

    public int getNumberOfPages() {
        return this.document.getNumberOfPages();
    }

}
