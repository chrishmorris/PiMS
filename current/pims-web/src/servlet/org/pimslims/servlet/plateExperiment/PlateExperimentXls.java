/**
 * V4_3-web org.pimslims.servlet.plateExperiment PlateExperimentXls.java
 * 
 * @author cm65
 * @date 5 Dec 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.plateExperiment;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.spreadsheet.MultiPlateScreenSpreadsheetHelper;
import org.pimslims.spreadsheet.SpreadSheet;

/**
 * PlateExperimentXls
 * 
 */
public class PlateExperimentXls extends PIMSServlet {

    /**
     * PlateExperimentXls.getServletInfo
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Export as .xlsx";
    }

    /**
     * PlateExperimentXls.doGet
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new ServletException(this.getClass().getName() + " does not support HTTP GET");
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final String pathInfo = request.getPathInfo();
        if (null == pathInfo || 1 > pathInfo.length()) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("No object specified");
            return;
        }
        int last = pathInfo.lastIndexOf("/");
        if (0 == last) {
            // only one /
            last = pathInfo.length();
        }
        final String hook = pathInfo.substring(1, last);

        final ReadableVersion version = this.getReadableVersion(request, response);
        try {
            //TODO use template if specified

            final ExperimentGroup group =
                (ExperimentGroup) this.getRequiredObject(version, request, response, hook);
            final SpreadSheet sheet = new MultiPlateScreenSpreadsheetHelper().getSheet(group);
            // send the file to the browser
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            final OutputStream responseWriter = response.getOutputStream();
            sheet.write(responseWriter);
            version.commit();
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

}
