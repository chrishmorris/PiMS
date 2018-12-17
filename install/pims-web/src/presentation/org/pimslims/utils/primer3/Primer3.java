package org.pimslims.utils.primer3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.pimslims.lab.primer.DesignPrimers;
import org.pimslims.utils.ExecRunner;
import org.pimslims.utils.ExecRunnerException;

public class Primer3 implements DesignPrimers {

    private static String primer3_path = "C:\\temp\\primer3-1.1.0-beta\\src\\primer3_core";

    private static String oligo_path = "C:\\temp\\primer3-1.1.0-beta\\src\\oligotm";

    public ArrayList<String> makePrimers(final String[] sequence, final float desiredTm) {

        final ArrayList<String> primers = new ArrayList<String>();

        final int iStart = 46;
        final int iEnd = 705;

        final int iLength = iEnd - iStart + 1;
        final String sLength = new String(new Integer(iLength).toString());
        final Primer3Model model = new Primer3Model();

        model.setPrimerSequenceID("AB004788_46_705");
        model.setSequence(sequence[0]);
        model.setIncludedRegion(iStart + "," + sLength);
        model.setPrimerProductSizeRange(sLength + "-" + sLength);

        try {

            final Primer3Results results = Primer3.runPrimer3(Primer3.primer3_path, model.toBytes());

            primers.add(results.getForwardPrimer().getPrimerSeq());
            primers.add(results.getReversePrimer().getPrimerSeq());

        } catch (final ExecRunnerException e) {
            System.out.println("Exception caught [" + e.getLocalizedMessage() + "]");
            e.printStackTrace();

        } catch (final Primer3Exception e) {
            System.out.println("Exception caught [" + e.getLocalizedMessage() + "]");
            e.printStackTrace();
        }

        return primers;
    }

    /**
     * runs primer3_core.exe in a new process, keeps track of error stream and returns results as a byte array
     * 
     * @param primer3_path
     * @param format - desired output format for primer3_core.exe
     * @param inputdata - byte array of input data for Primer3; see Primer3 documentation for details
     * @return - a Primer3Results object
     * @throws Primer3Exception
     * @throws ExecRunnerException
     */
    public static Primer3Results runPrimer3(final String primer3_path, final byte[] inputdata)
        throws Primer3Exception, ExecRunnerException.BadPathException {

        System.out.println("Primer3.runPrimer3 [" + primer3_path + "," + new String(inputdata) + "]");
        byte[] results;

        // checking input parameters

        if (primer3_path == null || primer3_path.length() < 1) {
            throw new Primer3Exception("path to primer3_core.exe is wrong; path = " + primer3_path);
        }

        if (inputdata.length == 0) {
            throw new Primer3Exception("no input data for runPrimer3()");
        }

        // running primer3.exe in a new process

        final String[] commands = { primer3_path, "-format_output" };
        ByteArrayInputStream input = null;
        try {
            final ExecRunner runner = new ExecRunner(commands);
            input = new ByteArrayInputStream(inputdata);
            results = runner.runCommand(input);

        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (final IOException ioe) {
                    // nothing useful to do here
                }
            }
        }

        return new Primer3Results(results);

    }// End of runPrimer3

    public static float calcTm(final String primer) throws Primer3Exception,
        ExecRunnerException.BadPathException {

        System.out.println("Primer3.calcTm [" + Primer3.oligo_path + " " + primer + "]");
        byte[] results;

        // checking input parameters

        if (Primer3.oligo_path == null || Primer3.oligo_path.length() < 1) {
            throw new Primer3Exception("path to oligotm.exe is wrong; path = " + Primer3.oligo_path);
        }

        if (primer.length() == 0) {
            throw new Primer3Exception("no input data for calcTm()");
        }

        // running primer3.exe in a new process

        final String[] commands = { Primer3.oligo_path, primer };

        final ExecRunner runner = new ExecRunner(commands);
        results = runner.runCommand();

        final Double d = new Double(new String(results));

        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

}
