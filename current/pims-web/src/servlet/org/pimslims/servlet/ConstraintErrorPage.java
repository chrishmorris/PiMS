/**
 * 
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.DeleteConstraintException;
import org.pimslims.exception.DuplicateKeyConstraintException;
import org.pimslims.lab.CustomConstraintException;
import org.pimslims.metamodel.ModelObject;

/**
 * @author cm65
 * 
 */
@javax.servlet.annotation.WebServlet("/update/ConstraintErrorPage")
public class ConstraintErrorPage extends PIMSServlet {

    // e.g. "sam_abstractsample_name_projectid_must_be_unique"
    static final Pattern CONSTRAINT = Pattern
        .compile(".*?\"[a-z]*?_([a-z]*?)_([a-z]*?)_projectid_must_be_unique\"$");

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final java.io.PrintWriter writer = response.getWriter();
        this.writeErrorHead(request, response, "Sorry, there has been an error",
            HttpServletResponse.SC_BAD_REQUEST);

        boolean printStacktrace = false;
        // friendly error box
        final Throwable ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
        writer.print("<div style=\"border:1px solid blue;padding:15px;background-color:#ccf\">");
        writer
            .print("<p>There was a problem inside PIMS, and it was unable to save the values you entered.</p>"
                + "<p>You may be able to fix this by going back and correcting the values in your form.</p>");

        // if we know the object cause problem, let user know
        if (ex instanceof DeleteConstraintException) {
            final ConstraintException exception = (ConstraintException) ex;
            if (exception.object != null) {
                writer.print("<p><span class='error'> <a href=\"View/" + exception.object.get_Hook() + "\">"
                    + StringEscapeUtils.escapeXml(exception.object.get_Name())
                    + "</a> can not be deleted. </span></p><p>");
            }
        } else if (ex instanceof ConstraintException) {
            final ConstraintException exception = (ConstraintException) ex;
            if (exception.object != null) {
                writer.print("<p><span class='error'>"
                    + StringEscapeUtils.escapeXml(exception.object.get_Name())
                    + " has a problem. </span></p><p>");
            }
        }
        // detailed error message based on different exception
        if (ex instanceof DeleteConstraintException) {
            final DeleteConstraintException cexc = (DeleteConstraintException) ex;
            writer.print("It is required by other records</p><p>");
            if (0 < cexc.getRequiredByObjects().size()) {
                writer.print("including these ones:</p><p>");
                for (final ModelObject mo : cexc.getRequiredByObjects()) {
                    writer.print("<a href=\"View/" + mo.get_Hook() + "\">" + mo.get_Name() + "</a>; ");
                }
            }
        } else if (ex instanceof org.pimslims.exception.DuplicateKeyConstraintException) {
            final org.pimslims.exception.DuplicateKeyConstraintException dex =
                (DuplicateKeyConstraintException) ex;
            // TODO use dex.attributeName and dex.value, when they are supplied
            final Matcher m = ConstraintErrorPage.CONSTRAINT.matcher(dex.getMessage());
            if (m.matches()) {
                writer.print("<div class='error'>The " + m.group(2) + " you have chosen is already in use"
                    + " for another " + m.group(1) + ".</div>");
            } else {
                writer.print("<div class='error'>The name you have chosen is already in use.</div>");
            }
        } else if (ex instanceof CustomConstraintException) {
            final CustomConstraintException cexc = (CustomConstraintException) ex;
            writer.print("You can not set: <span class='error'>" + cexc.attributeName
                + "</span> to: <span class='error'>" + cexc.value + "</span>");
        } else if (ex instanceof ConstraintException) {
            final ConstraintException exception = (ConstraintException) ex;
            if (exception.attributeName != null) {
                writer.print("You can not set: " + exception.attributeName + " to: " + exception.value);
            } else {
                writer.print(exception.getMessage());
                printStacktrace = true; // in this case, we should
                                        // printStacktrace for more info
            }
        }

        writer.print("</p><a href='javascript:history.back()'>Back</a></div><br/>");

        writer.print(ErrorPage.getDeveloperInformationBox(request, ex, printStacktrace));

        PIMSServlet.writeFoot(writer, request);
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        this.doGet(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "A constraint exception has occurred - show an error message";
    }

}
