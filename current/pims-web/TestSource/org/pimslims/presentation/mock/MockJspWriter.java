/** 
 * pims-web org.pimslims.tag MockJspWriter.java
 * @author cm65
 * @date 4 Oct 2007
 *
 * Protein Information Management System
 * @version: 1.3
 *
 * Copyright (c) 2007 cm65 
     * 
     * 
  * 
 */
package org.pimslims.presentation.mock;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.jsp.JspWriter;

/**
 * MockJspWriter
 * 
 * An implementation of JspWriter for testing
 *
 */
public class MockJspWriter extends JspWriter {
    
    private final PrintWriter stringWriter;

    /**
     * @param stringWriter a place to put the output
     */
    public MockJspWriter(StringWriter stringWriter) {
        super(Integer.MAX_VALUE, false);
        this.stringWriter = new PrintWriter(stringWriter);
    }

    /**
     * @see javax.servlet.jsp.JspWriter#clear()
     */
    @Override
    public void clear() throws IOException {
        // TODO Auto-generated method stub

    }

    /**
     * @see javax.servlet.jsp.JspWriter#clearBuffer()
     */
    @Override
    public void clearBuffer() throws IOException {
        // TODO Auto-generated method stub

    }

    /**
     * @see javax.servlet.jsp.JspWriter#close()
     */
    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub

    }

    /**
     * @see javax.servlet.jsp.JspWriter#flush()
     */
    @Override
    public void flush() throws IOException {
        // TODO Auto-generated method stub

    }

    /**
     * @see javax.servlet.jsp.JspWriter#getRemaining()
     */
    @Override
    public int getRemaining() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @see javax.servlet.jsp.JspWriter#newLine()
     */
    @Override
    public void newLine() throws IOException {
        this.print("\n");
    }

    @Override
    public void print(boolean arg0) {
        this.stringWriter.print(arg0);
    }

    @Override
    public void print(char arg0) {
        this.stringWriter.print(arg0);
    }


    @Override
    public void print(char[] arg0) {
        this.stringWriter.print(arg0);
    }

    @Override
    public void print(double arg0) {
        this.stringWriter.print(arg0);
    }

    @Override
    public void print(float arg0) {
        this.stringWriter.print(arg0);
    }

    @Override
    public void print(int arg0) {
        this.stringWriter.print(arg0);
    }

    @Override
    public void print(long arg0) {
        this.stringWriter.print(arg0);
    }

    @Override
    public void print(Object arg0) {
        this.stringWriter.print(arg0);
    }

    @Override
    public void print(String arg0) {
        this.stringWriter.print(arg0);
    }


    @Override
    public void println() {
        this.stringWriter.println();
    }

    @Override
    public void println(boolean arg0) {
        this.stringWriter.println(arg0);
    }

    @Override
    public void println(char arg0) {
        this.stringWriter.println(arg0);
    }

    @Override
    public void println(char[] arg0) {
        this.stringWriter.println(arg0);
    }

    @Override
    public void println(double arg0) {
        this.stringWriter.println(arg0);
    }

    @Override
    public void println(float arg0) {
        this.stringWriter.println(arg0);
    }

    @Override
    public void println(int arg0) {
        this.stringWriter.println(arg0);
    }

    @Override
    public void println(long arg0) {
        this.stringWriter.println(arg0);
    }

    @Override
    public void println(Object arg0) {
        this.stringWriter.println(arg0);
    }


    @Override
    public void println(String arg0) {
        this.stringWriter.println(arg0);
    }

    @Override
    public void write(char[] arg0, int arg1, int arg2) {
        this.stringWriter.write(arg0, arg1, arg2);
    }

    @Override
    public void write(char[] arg0) {
        this.stringWriter.write(arg0);
    }


    @Override
    public void write(int arg0) {
        this.stringWriter.write(arg0);
    }

    @Override
    public void write(String arg0, int arg1, int arg2) {
        this.stringWriter.write(arg0, arg1, arg2);
    }

    @Override
    public void write(String arg0) {
        this.stringWriter.write(arg0);
    }

}
