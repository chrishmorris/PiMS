package org.pimslims.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

// import org.apache.commons.logging.Log;
// import org.apache.commons.logging.LogFactory;

public class HelperThread extends Thread {

    private final InputStream is;

    // private String type;
    private byte[] result;

    private IOException exception;

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    HelperThread(final InputStream in_stream, final String in_type) {
        this.is = in_stream;
        // this.type = in_type;
    }

    @Override
    public void run() {
        try {
            // ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int c;
            while ((c = this.is.read()) != -1) {
                this.baos.write(c);
            }
            this.baos.flush();
            this.result = this.baos.toByteArray();
        } catch (final IOException ioe) {
            this.exception = ioe;
            throw new RuntimeException(ioe);
        } finally {
            try {
                this.is.close();
                this.baos.close();
            } catch (final IOException ioe2) {
                if (this.exception == null) {
                    this.exception = ioe2;
                }
            }
        }

    }

    public byte[] getResult() {
        return this.result;
    }

    public ByteArrayOutputStream getResults() {
        return this.baos;
    }

    public IOException getProblem() {
        return this.exception;
    }

}
