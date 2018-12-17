package org.pimslims.servlet.recordfetch;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;

import javax.naming.ServiceUnavailableException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringEscapeUtils;
import org.pimslims.bioinf.BioInfException;
import org.pimslims.bioinf.CdsList;
import org.pimslims.bioinf.DBFetch;
import org.pimslims.bioinf.FeatureBean;
import org.pimslims.bioinf.newtarget.BioFormatGuesser;
import org.pimslims.bioinf.newtarget.ParserUtility;
import org.pimslims.bioinf.newtarget.RecordParser;
import org.pimslims.bioinf.newtarget.RecordParser.EMBLDbs;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.target.Target;
import org.pimslims.servlet.ListFiles;
import org.pimslims.servlet.PIMSServlet;

/**
 * Retrieve database record
 * 
 * @author Petr Troshin
 * @date May 2006
 * @refactored December 2007
 * 
 */
public class RetrieveDBRecord extends PIMSServlet {

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     * Read database name and id from request and obtain the record from remote database
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // Only runs if not file
        final String dbid = request.getParameter("dbid");
        final String database = request.getParameter("database");

        if (dbid == null || dbid.trim().length() == 0) {
            RetrieveDBRecord.showError(request, response,
                "The database id has not been specified. Cannot proceed", null);
            return;
        }

        final String rec = RetrieveDBRecord.getRecord(database, dbid, request, response);
        if (null == rec) {
            return;
        }

        final ReadableVersion version = super.getReadableVersion(request, response);
        if (version == null) {
            return;
        }

        try {
            request.setAttribute("database", database);
            request.setAttribute("dbid", dbid);
            request.setAttribute("accessObjects", PIMSServlet.getPossibleCreateOwners(version));

            this.prepareParameters(request, response, rec);
            version.commit();

        } catch (final AbortedException e) {
            throw new RuntimeException(e);
        } catch (final ConstraintException e) {
            throw new RuntimeException(e);

        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    /**
     * @param request
     * @param response
     * @param dbf
     * @param rec
     * @throws ServletException
     * @throws IOException
     */
    private void prepareParameters(final HttpServletRequest request, final HttpServletResponse response,
        final String rec) throws ServletException, IOException {

        RecordParser rp = null;
        try {
            rp = DBFetch.getParser(rec, null);
        } catch (final BioInfException e) {
            // error message will be displayed by the code below
        }
        if (rp == null) {
            RetrieveDBRecord.showError(request, response,
                "Cannot recognise format of the record, please refer to the help pages", rec);
            return;
        }

        final CdsList bio = rp.getBioEntry();
        if (!ParserUtility.hasCDS(rp.getBioEntry()) && rp.getDatabaseFormat().equals(EMBLDbs.EMBL.toString())) {
            RetrieveDBRecord
                .showError(
                    request,
                    response,
                    "Sorry, the sequence appear to not contain CDS region. EMBL nucleotide sequence must contain CDS for the target to be defined!",
                    rec);
            return;
        }

        //final FeatureHolder fh = null;

        //TODO request.setAttribute("accessObjects", PIMSServlet.getPossibleCreateOwners(rv));
        request.setAttribute("record", rec);

        if (!ParserUtility.isSingleCDS(bio)) {
            // Ask which CDS to make a target from
            request.setAttribute("cdslist", FeatureBean.getFeatureBeans(bio));
            request.setAttribute("type", rp.getSourceType().toString());
            request.setAttribute("otherType", rp.getSourceType() == RecordParser.Type.DNA
                ? RecordParser.Type.protein.toString() : RecordParser.Type.DNA.toString());

            final RequestDispatcher rd = request.getRequestDispatcher("/JSP/ChooseCDS.jsp");
            rd.forward(request, response);
            return;
        }

        // Continue processing for single CDS record
        String srsUrl = rp.getLookedUpSourceURL();
        if (srsUrl != null && !srsUrl.contains("http")) {
            srsUrl = request.getContextPath() + "/read/DisplayBioEntry/" + srsUrl;
        }

        request.setAttribute("type",
            rp.getSourceType() == RecordParser.Type.complete ? "both" : rp.getSourceType());
        request.setAttribute("otherType", rp.getSourceType() == RecordParser.Type.DNA
            ? RecordParser.Type.protein.toString() : RecordParser.Type.DNA.toString());
        request.setAttribute("lookedUpSourceURL", srsUrl);

        // no longer check for uniqueness, must be able to download target twice into different projects

        // succeeded
        final RequestDispatcher rd = request.getRequestDispatcher("/JSP/BioEntryPreview.jsp");
        rd.forward(request, response);

    }

    private static String getLinkToTargetView(final Target target, final HttpServletRequest request) {
        return "<a href='" + request.getContextPath() + "/View/" + target.get_Hook() + "'>"
            + StringEscapeUtils.escapeXml(target.getName()) + "</a>";

    }

    public static String getRecord(final String database, String dbid, final HttpServletRequest request,
        final HttpServletResponse response) throws IOException, ServletException {
        String rec = null;
        try {
            dbid = dbid.trim();
            rec = DBFetch.getDBrecord(database, dbid);
        } catch (final ConnectException cex) {
            cex.printStackTrace();
            RetrieveDBRecord
                .showError(
                    request,
                    response,
                    "Cannot connect to the server. This might be because of your proxy settings. Please contact your system administrator.",
                    null);
            return null;
        } catch (final IOException ioe) {
            ioe.printStackTrace();
            RetrieveDBRecord.showError(request, response,
                "Sorry cannot find record " + StringEscapeUtils.escapeXml(dbid) + " in database " + database,
                null);
            return null;
        } catch (final ServiceUnavailableException e) {
            e.printStackTrace();
            final String message = "Sorry the service requested is unavailable.";
            RetrieveDBRecord.showError(request, response, message, null);
            return null;
        }

        if (rec == null || rec.equals("") || rec.equals("")) {
            RetrieveDBRecord.showError(request, response,
                "Sorry cannot find record " + StringEscapeUtils.escapeXml(dbid) + " in database " + database,
                null);
            return null;
        }
        return rec;
    }

    /**
     * 
     * @param request
     * @param response
     * @param message
     * @throws IOException
     * @throws ServletException
     * @throws IOException
     * @throws ServletException
     */
    private static void showError(final HttpServletRequest request, final HttpServletResponse response,
        final String message, final String record) throws IOException, ServletException {
        System.out.println("RetrieveDBRecord ##########" + message);
        request.setAttribute("message", message);
        request.setAttribute("record", record);
        final RequestDispatcher rd = request.getRequestDispatcher("/JSP/Upload.jsp");
        try {
            rd.forward(request, response);
        } catch (final IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Called from doPost to load file provided by the user
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void uploadFile(final HttpServletRequest request, final HttpServletResponse response,
        final ReadableVersion version) throws ServletException, IOException {
        String result = null;
        String database = null;
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new ServletException("no file found in submission");
        }
        try {
            // Create a new file upload handler
            final DiskFileUpload upload = new DiskFileUpload();
            // Set upload parameters
            // TODO upload.setSizeThreshold(yourMaxMemorySize);
            upload.setSizeMax(ListFiles.MAX_UPLOAD);
            // TODO upload.setRepositoryPath(yourTempDirectory);

            // Parse the request
            final java.util.Collection items = upload.parseRequest(request);

            // Process the uploaded items
            // At present, our form just has one item,
            // but this code is capable of handling many

            final java.util.Iterator iter = items.iterator();

            while (iter.hasNext()) {
                final FileItem item = (FileItem) iter.next();

                if (item.isFormField()) {
                    // Extract database format
                    if (item.getFieldName().equals("database")) {
                        database = item.getString();
                    }
                    continue;
                }
                if (0 == item.getName().trim().length()) {
                    this.writeErrorHead(request, response, "File not uploaded",
                        HttpServletResponse.SC_BAD_REQUEST);
                    final PrintWriter writer = response.getWriter();
                    writer.print("A file name must be specified");
                    PIMSServlet.writeFoot(writer, request);
                    return;
                }
                if (0 == item.getSize()) {
                    this.writeErrorHead(request, response, "File not uploaded",
                        HttpServletResponse.SC_BAD_REQUEST);
                    final PrintWriter writer = response.getWriter();
                    writer.print(item.getName() + " is length 0");
                    PIMSServlet.writeFoot(writer, request);
                    return;
                }
                result = item.getString();
                //TODO item.delete();
            }

        } catch (final SizeLimitExceededException e) {
            this.writeErrorHead(request, response, "File is too big", HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (final FileUploadException e) {
            throw new ServletException(e);
        } catch (final IOException e) {
            throw new ServletException(e);
        }
        // that's fine, show the results

        if (!BioFormatGuesser.canParse(result)) {
            RetrieveDBRecord
                .showError(
                    request,
                    response,
                    "Sorry, cannot recognise the record type. Supported record formats are UniProt/SwissProt, GenBank and EMBL.",
                    result);
            return;
        }

        // cant write temp file at this stage, nothing to attach it to

        request.setAttribute("database", database);
        //request.setAttribute("fromFile", new Boolean(true));
        //request.setAttribute("fileHook", fHook);

        // succeeded

        this.prepareParameters(request, response, result);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     *
     * Load file provided by the user
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        // If the the file was sent
        PIMSServlet.validatePost(request);
        if (!this.checkStarted(request, response)) {
            return;
        }

        final ReadableVersion version = super.getReadableVersion(request, response);
        try {
            request.setAttribute("accessObjects", PIMSServlet.getPossibleCreateOwners(version));

            if (ServletFileUpload.isMultipartContent(request)) {
                this.uploadFile(request, response, version);
                return;
            }

        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        throw new ServletException("No file supplied");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Retrieve database record and record it as PIMS file";
    }

}
