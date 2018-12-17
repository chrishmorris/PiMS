/**
 * V5_0-web org.pimslims.mhtml MhtmlTest.java
 * 
 * @author cm65
 * @date 15 Jul 2013
 * 
 *       Protein Information Management System
 * @version: 5.0
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.mhtml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * MhtmlTest Note that mixine file and http URLs seems to trigger security rules in IE
 */
public class MhtmlTest extends TestCase {

    public void testPage() throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        new Mhtml(os, "<html></html>".getBytes(), "http://example.org/pims/").getMessage();
        os.close();
        final String message = new String(os.toByteArray(), "UTF8");
        Assert.assertTrue(message, message.contains("MIME-Version: 1.0"));
        Assert.assertTrue(message, message.contains("Content-Type: multipart/related"));
        Assert.assertTrue(message, message.contains("Content-Type: text/html"));
    }

    public void testImage() throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final URL url = this.getClass().getResource("test.png");
        final Mhtml mhtml =
            new Mhtml(os, "<html><head></head><body><img src=\"" + url.toExternalForm()
                + "\" /></body></html>", "file://example.org/pims/test.html"); //note must be file to allow image
        mhtml.addImage(url, "image/png");
        mhtml.getMessage();
        os.close();
        final String message = new String(os.toByteArray(), "UTF8");
        Assert.assertTrue(message, message.contains("Content-Type: image/png"));
        Assert.assertTrue(message, message.contains("Content-Transfer-Encoding: base64"));
        Assert.assertTrue(message, message.contains("Content-Location: " + url.toExternalForm()));
    }

    public void couldtestPrint() throws IOException {
        final File file = new File("test.mhtml");
        final FileOutputStream os = new FileOutputStream(file);
        final URL url = this.getClass().getResource("test.png");
        final Mhtml mhtml =
            new Mhtml(
                os,
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r\n"
                    + "<HTML><HEAD>\r\n"
                    + "<META content=\"text/html; charset=windows-1252\" http-equiv=Content-Type>\r\n"
                    + "<META name=GENERATOR content=\"MSHTML 9.00.8112.16496\"></HEAD>\r\n"
                    + "<BODY><IMG \r\n"
                    + "src=\"file:///C:/working-copy/pims-repo/pims-web/WebContent/WEB-INF/classes/org/pimslims/mhtml/test.png\"></BODY></HTML>",
                "file://example.org/pims/test.html");
        mhtml.addImage(url, "image/png");
        mhtml.getMessage();
        os.close();
        System.out.println("Saved file: " + file.getAbsolutePath());
    }

    public void testRelativeURL() throws IOException {
        final File file = new File("test.mhtml");
        final FileOutputStream os = new FileOutputStream(file);
        final URL url = this.getClass().getResource("test.png");
        final Mhtml mhtml =
            new Mhtml(os, "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r\n"
                + "<HTML><HEAD>\r\n"
                + "<META content=\"text/html; charset=windows-1252\" http-equiv=Content-Type>\r\n"
                + "<META name=GENERATOR content=\"MSHTML 9.00.8112.16496\"></HEAD>\r\n" + "<BODY><IMG \r\n"
                + "src=\"/skins/commercial/header_logo.png\"></BODY></HTML>",
                "http://pims.structuralbiology.eu:8080/pims/Search/org.pimslims.model.experiment.Experiment");
        mhtml.addPublicPart(url, "image/png", "/skins/commercial/header_logo.png");
        mhtml.getMessage();
        os.close();
        System.out.println("Saved file: " + file.getAbsolutePath());
    }

}
