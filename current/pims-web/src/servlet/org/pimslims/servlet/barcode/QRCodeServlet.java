/**
 * pims-web org.pimslims.servlet.barcode Barcode.java
 * 
 * @author edaniel
 * @date 10.9.2013
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2013 edaniel The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.servlet.barcode;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.servlet.PIMSServlet;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Barcode
 * 
 */
@WebServlet("/public/qr")
public class QRCodeServlet extends PIMSServlet {

    /**
     * PIMSServlet.getServletInfo
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Generates a barcode image";
    }

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws IOException {

        final String content = request.getParameter("content");

        int size = 100;
        if (null != request.getParameter("size")) {
            size = Integer.parseInt(request.getParameter("size"));
        }

        response.setContentType("image/png");
        final OutputStream out = response.getOutputStream();
        try {
            this.outputQR(content, size, out);
        } catch (final WriterException e) {
            throw new RuntimeException(e);
        }

    }

    private void outputQR(final String content, final int size, final OutputStream stream)
        throws WriterException, IOException {
        final Charset charset = Charset.forName("UTF-8");
        final CharsetEncoder encoder = charset.newEncoder();
        BitMatrix bitMatrix = null;
        final QRCodeWriter writer = new QRCodeWriter();
        bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size);
        MatrixToImageWriter.writeToStream(bitMatrix, "png", stream);
    }

}
