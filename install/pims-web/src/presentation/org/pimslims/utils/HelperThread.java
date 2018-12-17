package org.pimslims.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

// import org.apache.commons.logging.Log;
// import org.apache.commons.logging.LogFactory;

public class HelperThread extends Thread {

    // TODO remove private Log log = LogFactory.getLog(Class.class);

    private InputStream is;

    // private String type;
    private byte[] result;

    private IOException exception;

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    HelperThread(InputStream in_stream, String in_type) {
        this.is = in_stream;
        // this.type = in_type;
    }

    @Override
    public void run() {
        try {
            // ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int c;
            while ((c = is.read()) != -1) {
                baos.write(c);
            }
            baos.flush();
            result = baos.toByteArray();
        } catch (IOException ioe) {
            exception = ioe;
            throw new RuntimeException(ioe);
        } finally {
            try {
                is.close();
                baos.close();
            } catch (IOException ioe2) {
                if (exception == null) {
                    exception = ioe2;
                }
            }
        }

    }

    public byte[] getResult() {
        return result;
    }

    public ByteArrayOutputStream getResults() {
        return baos;
    }

    public IOException getProblem() {
        return exception;
    }

}
