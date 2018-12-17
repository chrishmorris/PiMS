/**
 * 
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.AbstractCsvData;
import org.pimslims.spreadsheet.CsvPrinter;

/**
 * AbstractCsvServlet
 * 
 * Provides a facility to output data from PiMS as a CSV file. Please extend it and implement getCsvData. The
 * URL should end in ".csv", so that the browser can save the file with an appropriate name. It should be
 * something like:
 * http://localhost:8080/.../TargetGroupCsv/org.pimslims.model.target.TargetGroup:4239/targetGroup.csv
 * 
 * @author cm65
 * 
 */
public abstract class AbstractCsvServlet extends PIMSServlet {

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
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
            final AbstractCsvData csv = this.getCsvData(version, hook, PIMSServlet.getParameterMap(request));

            // send the file to the browser
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/csv"); // see RFC4180
            final String browser = request.getHeader("User-Agent");
            if (null != browser && browser.toLowerCase().contains("safari")) {
                // safari misbehaves for all text/ mime types
                response.setContentType("application/csv");
            }
            final PrintWriter responseWriter = response.getWriter();
            final CsvPrinter csvPrinter = new CsvPrinter(responseWriter);
            csvPrinter.println(csv.getHeaders());
            while (csv.hasNext()) {
                csvPrinter.println(csv.next());
            }
            try {
                csvPrinter.close();
            } catch (final IOException e) {
                throw new ServletException(e);
            }
            version.commit();
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    /**
     * @param version a read transaction to use
     * @param hook part of the path info, e.g. xxxx in pims/ServletName/xxxx/filename.csv
     * @return
     * @throws ServletException
     * @throws AccessException
     */
    protected abstract AbstractCsvData getCsvData(ReadableVersion version, String hook,
        Map<String, String> parms) throws ServletException, AccessException;

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Display data from PiMS in CSV format";
    }

}
