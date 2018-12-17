package org.pimslims.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// import org.apache.commons.logging.Log;
// import org.apache.commons.logging.LogFactory;
/**
 * The class allows to run an external executable program from java. Sets an input stream for the executable
 * program, records an output stream and errors of the program and returns outputstream as a byte array;
 * 
 * @author Ekaterina Pilicheva
 * @since 31.03.2006
 */

public class ExecRunner {

    // TODO remove private Log log = LogFactory.getLog(Class.class);

    // executable command and its arguments
    String[] commands;

    /**
     * public constructor for ExecRunner
     * 
     * @param command - an array of Strings, starts from an absolute path to the *.exe file to be run,
     *            followed by other command line arguments
     * @throws ExecRunnerException
     */
    public ExecRunner(final String[] command) {

        assert !(command == null || command.length < 1);
        this.commands = command;

    }// End of ExecRunner

    /**
     * the method sets input stream for the executable program, runs executable program in a new created
     * proccess, logs errors stream info coming out of the program exe, returns output from the executable
     * program as an array of bytes
     * 
     * @param inputToDot - InputStream for executable program
     * @return output after from executable progra
     * @throws ExecRunnerException
     */
    public byte[] runCommand() throws ExecRunnerException.BadPathException {

        byte[] res;
        ProcessBuilder pb = null;
        Process process = null;
        InputStream errorFromDotStream = null;
        InputStream fromDotStream = null;
        OutputStream toDotStream = null;
        try {
            // create and start process
            pb = new ProcessBuilder(this.commands);
            process = pb.start();

            // get error stream and results stream from process

            errorFromDotStream = process.getErrorStream();
            fromDotStream = process.getInputStream();

            // create and start new threads to deal with error and results from
            // the process

            final HelperThread errorThread = new HelperThread(errorFromDotStream, "ERROR");
            final HelperThread fromDotThread = new HelperThread(fromDotStream, "OUTPUT");
            errorThread.start();
            fromDotThread.start();

            // attach input data to the process

            toDotStream = process.getOutputStream();
            toDotStream.flush();
            toDotStream.close();
            final int exitCode = process.waitFor();
            errorThread.join(1000);
            fromDotThread.join(1000);

            // log all error messages from error stream and exceptions from
            // output stream,
            // but do not throw an exception

            final StringBuffer errorMessage = new StringBuffer();
            final byte[] errorBuf = errorThread.getResult();
            IOException ioexception = null;
            int i = 0;
            if (errorBuf != null && errorBuf.length > 0) {
                while (i < errorBuf.length) {
                    errorMessage.append((char) errorBuf[i]);
                    i++;
                }
                // TODO log.warn(errorMessage.toString());
            }
            if ((ioexception = fromDotThread.getProblem()) != null) {
                errorMessage.append(ioexception.getMessage());
            }
            if (errorMessage.length() > 0) {
                // TODO log.warn(errorMessage, ioexception);
            }

            // Only throw exception if exit code is not 0
            if (exitCode != 0) {

                errorMessage.append(errorThread.getResult());
                throw new RuntimeException(errorMessage.toString());
            }

            // get results from the process output thread

            res = fromDotThread.getResult();

        } catch (final IOException ioe) {
            final String message = ioe.getMessage();
            if (message.startsWith("CreateProcess: ") && message.endsWith("error=3")) {
                throw new ExecRunnerException.BadPathException(message, ioe);
            }
            throw new RuntimeException(ioe.getMessage(), ioe);
        } catch (final InterruptedException ie) {
            throw new RuntimeException(ie.getMessage(), ie);
        }
        return res;
    }// End of runCommand

    public byte[] runCommand(final InputStream inputToDot) throws ExecRunnerException.BadPathException {

        // check input stream is not null
        //System.out.println("ExecRunner.runCommand");
        assert null != inputToDot;

        byte[] res;
        ProcessBuilder pb = null;
        Process process = null;
        InputStream errorFromDotStream = null;
        InputStream fromDotStream = null;
        OutputStream toDotStream = null;
        try {
            // create and start process
            final StringBuffer sb = new StringBuffer();
            for (int i = 0; i < this.commands.length; i++) {
                sb.append(this.commands[i] + " ");
            }
            //System.out.println("ExecRunner.runCommand [" + sb.substring(0, sb.length() - 1) + "]");

            pb = new ProcessBuilder(this.commands);
            process = pb.start();

            // get error stream and results stream from process

            errorFromDotStream = process.getErrorStream();
            fromDotStream = process.getInputStream();

            // create and start new threads to deal with error and results from
            // the process

            final HelperThread errorThread = new HelperThread(errorFromDotStream, "ERROR");
            final HelperThread fromDotThread = new HelperThread(fromDotStream, "OUTPUT");
            errorThread.start();
            fromDotThread.start();

            // attach input data to the process

            toDotStream = process.getOutputStream();
            int c;
            while ((c = inputToDot.read()) != -1) {
                toDotStream.write(c);
            }
            toDotStream.flush();
            toDotStream.close();
            final int exitCode = process.waitFor();
            errorThread.join(1000);
            fromDotThread.join(1000);

            // log all error messages from error stream and exceptions from
            // output stream,
            // but do not throw an exception

            final StringBuffer errorMessage = new StringBuffer();
            final byte[] errorBuf = errorThread.getResult();
            IOException ioexception = null;
            int i = 0;
            if (errorBuf != null && errorBuf.length > 0) {
                while (i < errorBuf.length) {
                    errorMessage.append((char) errorBuf[i]);
                    i++;
                }
                // TODO log.warn(errorMessage.toString());
            }
            if ((ioexception = fromDotThread.getProblem()) != null) {
                errorMessage.append(ioexception.getMessage());
            }
            if (errorMessage.length() > 0) {
                // TODO log.warn(errorMessage, ioexception);
            }

            // Only throw exception if exit code is not 0
            if (exitCode != 0) {
                //System.out.println("ExecRunner.runCommand exitCode [" + exitCode + "]");
                errorMessage.append(errorThread.getResult());
                throw new java.lang.AssertionError("dot failed: " + errorMessage.toString());
            }

            // get results from the process output thread

            res = fromDotThread.getResult();

        } catch (final IOException ioe) {
            // TODO print process.getErrorStream()
            final String message = ioe.getMessage();
            System.out.println("IOException [" + message + "]");
            ioe.printStackTrace();
            if (message.startsWith("CreateProcess: ") && message.endsWith("error=3")) {
                throw new ExecRunnerException.BadPathException(message, ioe);
            }
            throw new RuntimeException("Cant run: " + this.commands[0] + " because: " + ioe.getMessage(), ioe);
        } catch (final InterruptedException ie) {
            throw new RuntimeException(ie.getMessage(), ie);
        } finally {
            try {
                if (inputToDot != null) {
                    inputToDot.close();
                }
            } catch (final IOException e) {
                // do nothing
            }
        }
        return res;
    }// End of runCommand
}
