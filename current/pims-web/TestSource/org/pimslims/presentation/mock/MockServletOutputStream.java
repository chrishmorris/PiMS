/**
 * V2_0-pims-web org.pimslims.servlet.mock MockServletOutputStream.java
 * 
 * @author cm65
 * @date 11 Dec 2007
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2007 cm65 
 * 
 * 
 */
package org.pimslims.presentation.mock;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

/**
 * MockServletOutputStream
 * 
 */
public class MockServletOutputStream extends ServletOutputStream {

    private final OutputStream outputStream;

    /**
     * @param outputStream
     */
    public MockServletOutputStream(OutputStream outputStream) {
        super();
        this.outputStream = outputStream;
    }

    /**
     * @see java.io.OutputStream#write(int)
     */
    @Override
    public void write(int arg0) throws IOException {
        outputStream.write(arg0);
    }

    /**
     * @throws IOException
     * @see java.io.OutputStream#close()
     */
    @Override
    public void close() throws IOException {
        this.outputStream.close();
    }

    /**
     * @throws IOException
     * @see java.io.OutputStream#flush()
     */
    @Override
    public void flush() throws IOException {
        this.outputStream.flush();
    }

    /**
     * @param b
     * @param off
     * @param len
     * @throws IOException
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.outputStream.write(b, off, len);
    }

    /**
     * @param b
     * @throws IOException
     * @see java.io.OutputStream#write(byte[])
     */
    @Override
    public void write(byte[] b) throws IOException {
        this.outputStream.write(b);
    }

}
