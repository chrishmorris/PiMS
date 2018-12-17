/**
 * pims-web org.pimslims.servlet.recordfetch TargetRecorder.java
 * 
 * @author pvt43
 * @date 4 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 pvt43
 * 
 * 
 */
package org.pimslims.servlet.recordfetch;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.bioinf.BioInfException;
import org.pimslims.bioinf.DBFetch;
import org.pimslims.bioinf.newtarget.PIMSTarget;
import org.pimslims.bioinf.newtarget.RecordParser;
import org.pimslims.bioinf.targets.PIMSTargetWriter;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.servlet.PIMSServlet;

/**
 * TargetRecorder This class is for recording the targets from text files in variety of formats.
 */
public class TargetRecorder extends PIMSServlet {

    /**
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Record target from biological database record";
    }

    /**
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        PIMSServlet.validatePost(request);
        final String accessHook = request.getParameter(PIMSServlet.LAB_NOTEBOOK_ID);
        final String database = request.getParameter("database");
        final String dbid = request.getParameter("dbid");
        final String cds = request.getParameter("cds");
        String targetHook = null;
        final String record = request.getParameter("record");
        if (null == record) {
            this.writeErrorHead(request, response, "No record submitted", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        RecordParser rp = null;

        if (!Util.isEmpty(record)) {
            try {
                rp = DBFetch.getParser(record, cds);
            } catch (final BioInfException e) {
                // the error will be reported by the code below
            }
        }
        if (null == rp) {
            this.writeErrorHead(request, response, "Sorry, unable to recognise format of file",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //RichFeature rf = ParserUtility.getCDSByName(cds, rp.getBioEntry());
        final PIMSTarget ptarget = new PIMSTarget(rp);
        //ptarget.setProject(projectHook); 
        ptarget.setAccess(accessHook);
        ptarget.setDatabaseName(database);

        /* Replace the database name for TIGR database */
        if (database.trim().equalsIgnoreCase("JCVI")) {
            // recover format substitution done earlier             
            ptarget.setDatabaseName(rp.getDatabaseFormat());
            // add proper Db reference for TIGR
            ptarget.addXaccessions(database, dbid);
        }

        final WritableVersion rw = this.getWritableVersion(request, response);
        if (rw == null) {
            return;
        }
        try {

            final PIMSTargetWriter pwriter = new PIMSTargetWriter(rw, ptarget);

            targetHook = pwriter.record().get_Hook();

            rw.commit();
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } finally {
            if (!rw.isCompleted()) {
                rw.abort();
            }
        }
        PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + targetHook);

    }
}
