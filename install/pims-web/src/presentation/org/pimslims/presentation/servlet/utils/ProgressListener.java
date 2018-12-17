/**
 * 
 */
package org.pimslims.presentation.servlet.utils;

import java.io.PrintWriter;

/**
 * Provides a progress indication for a slow-running servlet. The method signatures have been chosen so that
 * it can also be called from a JSP.
 * 
 * Note that if there is an exception after the flush, then the error page will not be displayed. TODO supply
 * an error reporter here and wrap the subsequent code in a try block.
 * 
 * @author cm65
 * 
 */
@Deprecated
public class ProgressListener {

    private final java.io.PrintWriter writer;

    private final int total;

    private int count;

    /**
     * Create and display the progress listener. Note that the caller must have already written the page
     * header.
     * 
     * 
     * @param total number of operations to perform
     * @param writer output stream for the page
     * @param title legend, e.g. "Processing..."
     */
    @Deprecated
    // no pages should be so slow they need this
    public ProgressListener(final int total, final PrintWriter writer, final String title) {
        super();
        this.total = 0 == total ? 1 : total; // avoid divide by zero
        // exception
        this.writer = writer;
        writer
            .print("<div id=\"progressContainer\" style=\" width: 100%; padding:2px; border: solid 1px\">\r\n"
                + "<div id=\"progress\" style=\"background-color: blue;  width: 1px; \">&nbsp;</div>\r\n"
                + "</div><script type=\"text/javascript\">\r\n"
                + "var progress = document.getElementById(\"progress\");\r\n"
                + "var progressContainer = document.getElementById(\"progressContainer\");\r\n"
                + "</script>\r\n");
        writer.flush();
    }

    /**
     * The servlet or JSP should call this method periodically, e.g. by if (i%10==0)
     * {progressListener.setProgress(i);}
     * 
     * @param progress number of operations performed so far
     * @throw IOException if e.g. the user has clicked stop
     */
    public void setProgress(int progress) throws java.io.IOException {
        this.count = progress;
        progress = Math.min(progress, this.total); // don't go over 100%
        final int perCentage = (100 * progress) / this.total;
        this.writer.print("<script type=\"text/javascript\">progress.style.width = \"" + perCentage
            + "%\";</script>\n");
        final boolean error = this.writer.checkError(); // flushes and checks is open
        if (error) {
            throw new java.io.IOException("output cancelled");
        }
    }

    public int getCurrentCount() {
        return this.count;
    }

    /**
     * The page or JSP should normally call this method after the loop is complete, to remove the progress bar
     * from view.
     * 
     * @param hidden
     */
    public void setProgressHidden(final boolean hidden) {
        if (hidden) {
            this.writer
                .print("<script type=\"text/javascript\">progressContainer.style.display = \"none\";</script>\n");
        } else {
            this.writer
                .print("<script type=\"text/javascript\">progressContainer.style.display = \"block\";</script>\n");
        }
    }

}
