package org.pimslims.embl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Parse all the provided files, to check that we understand the format
 * 
 * @author cm65
 * 
 */
public class Validator {

    public static void main(final String[] args) {
        if (0 == args.length) {
            System.out.println("Usage: filename filename ....");
        }
        for (int i = 0; i < args.length; i++) {
            File file = new File(args[i]);
            try {
                InputStream in = new FileInputStream(file);
                HamburgParser.parse(in);
                System.out.println("OK: " + file.getAbsolutePath());
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + file.getAbsolutePath());
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Error while processing: "
                        + file.getAbsolutePath());
                System.out.println(e.getMessage());
            } catch (AssertionError e) {
                System.out.println("Error processing: "
                        + file.getAbsolutePath());
                e.printStackTrace(System.out);
            } catch (IllegalArgumentException e) {
                System.out.println("Error processing: "
                        + file.getAbsolutePath());
                Throwable cause = e;
                while (null != cause.getCause()) {
                    cause = cause.getCause();
                }
                cause.printStackTrace(System.out);
                //CHECKSTYLE:OFF
            } catch (Exception e) { // unusual to catch Exception, but
                // appropriate here
                System.out.println("Error processing: "
                        + file.getAbsolutePath());
                e.printStackTrace(System.out);
            }
        }
    }

}
