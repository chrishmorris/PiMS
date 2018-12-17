package org.pimslims.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.bioinf.NcbiBlastImpl;

/**
 * 
 * 
 * @author Peter Troshin
 * @date November 2007
 * 
 */

public class AutomatedBlastStatus extends PIMSServlet {

    public AutomatedBlastStatus() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse) requires classname=org.pimslims.model.holder.Holder&barcode=123456 in
     *      request parameters
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        if (!this.checkStarted(request, response)) {
            return;
        }
        final PrintWriter writer = response.getWriter();
        this.writeHead(request, response, "Automatic BLAST Search Status");
        final NumberFormat nformatter = new DecimalFormat();
        nformatter.setMaximumFractionDigits(2);
        final Thread[] th = new Thread[Thread.activeCount() + 5]; // to avoid being other array size
        Thread.enumerate(th);
        boolean noTaskfound = true;
        for (int i = 0; i < th.length; i++) {
            if (th[i] == null) {
                continue;
            }
            final String tname = th[i].getName();
            if (tname == null) {
                continue;
            }
            //TODO note this will not work in Tomcat7
            if (tname.equals("periodicBlastTargetDB" + this.getServletContext().getRealPath("/"))) {
                noTaskfound = false;
                if (th[i].isAlive()) {
                    writer.print("Periodic Blast against TargetDb has been ENABLED " + "<br/>");
                    final AutomatedBlastListener.PeriodicBlast pb =
                        (AutomatedBlastListener.PeriodicBlast) th[i];
                    writer.print("Periodic Blast against TargetDb is "
                        + (pb.isRunning() ? NcbiBlastImpl.RUNNING : "NOT RUNNING") + "<br/>");
                    writer.print("Time of last run: "
                        + (pb.getLastTimeRun() == null ? "Never run" : pb.getLastTimeRun()) + "<br/>");
                    writer.print("Next run is at: " + pb.getNextTimeRun() + "<br/>");
                    writer.print("Time elapsed from the start: "
                        + (pb.getTimeFromLastRun() == -1 ? "Never run" : pb.getTimeFromLastRun()
                            + " minutes ") + "<br/>");
                    writer.print("Automated run interval: " + pb.getRunInterval() + " minutes / "
                        + pb.getRunInterval() / 60 + " hours / "
                        + nformatter.format(pb.getRunInterval() / (60.0 * 24)) + " days " + "<br/>");
                } else {
                    writer
                        .print("There has been a problem with automated BLAST search please send log to PIMS developers"
                            + "<br/>");
                }
            }
            if (tname.equals("periodicBlastPDB" + this.getServletContext().getRealPath("/"))) {
                noTaskfound = false;
                if (th[i].isAlive()) {
                    writer.print("<hr/>");
                    writer.print("Periodic Blast against PDB has been ENABLED " + "<br/>");
                    final AutomatedBlastListener.PeriodicBlast pb =
                        (AutomatedBlastListener.PeriodicBlast) th[i];
                    writer.print("Periodic Blast against PDB is "
                        + (pb.isRunning() ? NcbiBlastImpl.RUNNING : "NOT RUNNING") + "<br/>");
                    writer.print("Time of last run: "
                        + (pb.getLastTimeRun() == null ? "Never run" : pb.getLastTimeRun()) + "<br/>");
                    writer.print("Next run is at: " + pb.getNextTimeRun() + "<br/>");
                    writer.print("Time elapsed from the start: "
                        + (pb.getTimeFromLastRun() == -1 ? "Never run" : pb.getTimeFromLastRun()
                            + " minutes ") + "<br/>");
                    writer.print("Automated run interval: " + pb.getRunInterval() + " minutes / "
                        + pb.getRunInterval() / 60 + " hours / "
                        + nformatter.format(pb.getRunInterval() / (60.0 * 24)) + " days " + "<br/>");
                } else {
                    writer
                        .print("There has been a problem with automated BLAST search please send log to PIMS developers"
                            + "<br/>");
                }
            }
        }
        if (noTaskfound) {
            writer.print("Automatic BLAST searches are disabled or not configured");
        }
        PIMSServlet.writeFoot(writer, request);
    }

    /**
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Automated BLAST search status";
    }

}
