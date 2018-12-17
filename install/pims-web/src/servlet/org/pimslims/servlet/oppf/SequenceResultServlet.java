/**
 * pims-web org.pimslims.servlet.oppf SequenceResultServlet.java
 * 
 * @author Marc Savitsky
 * @date 7 Oct 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.servlet.oppf;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.oppf.sequence.SequencingResult;
import org.pimslims.persistence.JpqlQuery;
import org.pimslims.presentation.PimsQuery;
import org.pimslims.presentation.plateExperiment.PlateBean;
import org.pimslims.presentation.plateExperiment.PlateReader;
import org.pimslims.servlet.ListFiles;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.util.File;

/**
 * SequenceResultServlet
 * 
 */
public class SequenceResultServlet extends PIMSServlet {

    private final String sequenceParameter = "__SEQUENCE";

    public static final String HOLDERTYPENAME = "holderType";

    public static final String EXPTYPENAME = "expType";

    @Override
    public String getServletInfo() {
        return "Import Sequence results";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final long begin = System.currentTimeMillis();
        System.out.println("SequenceResultServlet.doGet A [" + (System.currentTimeMillis() - begin) + "]");

        if (!this.checkStarted(request, response)) {
            return;
        }

        //final ServletContext scontext = this.getServletContext();
        //final PrintWriter writer = response.getWriter();
        ReadableVersion version = null;

        try {
            // get a read transaction
            version = this.getReadableVersion(request, response);

            final Collection<PlateBean> myPlateBeans = this.findExptGroups(version);

            request.setAttribute("sequenceplates", myPlateBeans);

            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/oppf/SequenceResult.jsp");
            dispatcher.forward(request, response);

            version.commit();

        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);

        } finally {
            if (version != null && !version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * SequenceResultServlet.findExptGroups - performance improvement
     * 
     * @param version
     * @return
     */
    private Collection<PlateBean> findExptGroups(final ReadableVersion version) {

        // TODO - not sure a subselect is necessary
        final List<String> subSelectCriteria = new ArrayList<String>();
        subSelectCriteria.add("exp.protocol.id = pd.protocol.id");
        subSelectCriteria.add("pd.name = '" + this.sequenceParameter + "'");
        final String subSelectWhereHQL =
            JpqlQuery.getWhereHQL(version, "exp.experimentGroup", subSelectCriteria, ExperimentGroup.class);
        //TODO needs access control
        final String subSelectHQL =
            " select distinct exp.experimentGroup.id from " + Experiment.class.getName() + "  exp, "
                + ParameterDefinition.class.getName() + " pd " + subSelectWhereHQL;

        final String selectHQL =
            "from " + ExperimentGroup.class.getName() + " eg where eg.id in (" + subSelectHQL + ")";
        // TODO where status is to be run or in process

        final org.pimslims.presentation.PimsQuery query = PimsQuery.getQuery(version, selectHQL);
        //System.out.println("createQuery using " + (System.currentTimeMillis() - start) + "ms");

        // TODO Limit/page? ExperimentGroup status should take care of length

        final long start = System.currentTimeMillis();
        System.out.println("query.list() using [" + query.getQueryString() + "]");
        final List results = query.list();
        System.out.println("query.list() using " + (System.currentTimeMillis() - start) + "ms");

        final Collection<PlateBean> plateBeans = new ArrayList<PlateBean>();
        for (final Object record : results) {
            plateBeans.add(new PlateBean((ExperimentGroup) record));
        }

        return plateBeans;

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

        final long begin = System.currentTimeMillis();
        System.out.println("SequenceResultServlet.doPost A [" + (System.currentTimeMillis() - begin) + "]");

        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new ServletException("no file found in submission");
        }
        final PrintWriter writer = response.getWriter();
        final String pathInfo = request.getPathInfo();
        if (null == pathInfo || 1 > pathInfo.length()) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            writer.print("No object specified");
            return;
        }

        final String hook = pathInfo.substring(1);
        //final String userName = PIMSServlet.getUsername(request);
        final WritableVersion version = this.getWritableVersion(request, response);

        final org.pimslims.metamodel.ModelObject object =
            this.getRequiredObject(version, request, response, hook);
        if (object == null) {
            return; // error message was provided by the
        }

        //final Holder holder = null;
        ExperimentGroup group = null;
        //final PlateReader reader = null;

        if (object instanceof ExperimentGroup) {
            group = (ExperimentGroup) object;
            //JMD            holder = HolderFactory.getPlate(group);
        }

        // TODO Surely we should run away if group is null?
/*JMD        
        // TODO Surely we should run away if holder is null?
        if (null != holder) {
            reader = new PlateReader(version, holder, null);
        }
 */
        try {

            // TODO Should we ask?
            // TODO Log, not System.out.println!
            System.out.println("sequenceresultservlet - object's owner is: \"" + object.get_Owner() + "\"");
            version.setDefaultOwner(object.get_Owner());

            // Create a new file upload handler
            final ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
            // Set upload parameters
            // TODO upload.setSizeThreshold(yourMaxMemorySize);
            upload.setSizeMax(ListFiles.MAX_UPLOAD);
            // TODO upload.setRepositoryPath(yourTempDirectory);
            final java.util.Collection<FileItem> items = upload.parseRequest(request);

            final File file = this.uploadFile(version, (LabBookEntry) object, items);

/*JMD            
            if (null != file) {
                this.alignPlate(version, file, reader, userName);
            }
            //TODO item.delete();
 */

            if (null != file) {
                final SequencingResult sr = new SequencingResult();
                sr.alignPlate(version, file, group);
            }

            version.commit();

        } catch (final org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException e) {
            this.writeErrorHead(request, response, "File is too big", HttpServletResponse.SC_BAD_REQUEST);
            writer.print("The maximum size is: " + ListFiles.MAX_UPLOAD);
            PIMSServlet.writeFoot(writer, request);
            return;
        } catch (final FileUploadException e) {
            throw new ServletException(e);
        } catch (final IOException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        System.out.println("SequenceResultServlet.doPost Z [" + (System.currentTimeMillis() - begin) + ":"
            + "View/" + object.get_Hook() + "]");
        PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + object.get_Hook());
    }

    private File uploadFile(final WritableVersion version, final LabBookEntry object,
        final Collection<FileItem> items) throws AccessException, ConstraintException, FileUploadException,
        IOException {

        //final long begin = System.currentTimeMillis();
        //System.out.println("SequenceResultServlet.uploadFile A [" + (System.currentTimeMillis() - begin)
        //    + "]");
        String fileDesc = "";
        File file = null;

        for (final FileItem item : items) {

            if (item.isFormField()) {
                if (item.getFieldName().equals("fileDescription")) {
                    fileDesc = item.getString();
                }
                continue;
            }
            if (0 == item.getName().trim().length()) {
                throw new FileUploadException("A file name must be specified");
            }
            if (0 == item.getSize()) {
                throw new FileUploadException(item.getName() + " is length 0");
            }

            final java.io.InputStream uploadedStream = item.getInputStream();

            file = version.createFile(uploadedStream, item.getName(), object);
            file.setMimeType(item.getContentType());
            uploadedStream.close();
        }

        if (null != file) {
            file.setDescription(fileDesc.length() > 253 ? fileDesc.substring(253) : fileDesc);
        }
        //System.out.println("SequenceResultServlet.uploadFile Z [" + (System.currentTimeMillis() - begin)
        //    + "]");
        return file;
    }

    protected static Experiment getExperimentforEntry(final PlateReader reader, final ZipEntry entry) {

        Experiment experiment = null;
        experiment =
            reader.getExperimentByPosition(SequenceResultServlet.getRow(entry.getName()),
                SequenceResultServlet.getColumn(entry.getName()));
        if (null == experiment) {
            experiment =
                reader.getExperimentByResearchObjective(SequenceResultServlet.getOPPFNumber(entry.getName()));
        }
        return experiment;
    }

    /**
     * SequenceResultServlet.getRow
     * 
     * @param name
     * @return
     * @deprecated Use {@link #getRecordforEntry(List, ZipEntry)} directly
     */
    @Deprecated
    protected static int getRow(final String name) {

        final Pattern REGEXP = Pattern.compile("^.*([A-P])(\\d+).*$");
        final Matcher matcher = REGEXP.matcher(name);
        if (!matcher.matches()) {
            return -1;
        }

        final String row = matcher.group(1);
        //System.out.println("SequenceResultServlet.getRow [" + row + "]");
        return HolderFactory.getRow(row);
    }

    /**
     * SequenceResultServlet.getColumn
     * 
     * @param name
     * @return
     * @deprecated Use {@link #getRecordforEntry(List, ZipEntry)} directly
     */
    @Deprecated
    protected static int getColumn(final String name) {

        final Pattern REGEXP = Pattern.compile("^.*([A-P])(\\d+).*$");
        final Matcher matcher = REGEXP.matcher(name);
        if (!matcher.matches()) {
            return -1;
        }

        final String column = matcher.group(2);
        //System.out.println("SequenceResultServlet.getColumn [" + column + "]");
        return HolderFactory.getColumn("X" + column); // getColumn(position) expects postition eg A12
    }

    /**
     * SequenceResultServlet.getOPPFNumber
     * 
     * TODO Move to logic
     * 
     * @param name
     * @return
     */
    protected static String getOPPFNumber(final String name) {

        final Pattern REGEXP = Pattern.compile("^(\\d+)(-T7).*$");
        final Matcher matcher = REGEXP.matcher(name);
        if (!matcher.matches()) {
            return null;
        }

        final String oppf = "OPPF" + matcher.group(1);
        //System.out.println("SequenceResultServlet.getOPPFNumber [" + oppf + "]");
        return oppf;
    }

}
