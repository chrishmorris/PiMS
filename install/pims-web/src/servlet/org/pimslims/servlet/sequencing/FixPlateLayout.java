/**
 * 
 */
package org.pimslims.servlet.sequencing;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.lab.experiment.ExperimentCopier;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.util.File;
import org.pimslims.util.FileImpl;
import org.pimslims.utils.experiment.Utils;
import org.pimslims.utils.sequenator.ManualTemplateHelper;
import org.pimslims.utils.sequenator.PlatePlanner;
import org.pimslims.utils.sequenator.RoboticsTemplateHelper;
import org.pimslims.utils.sequenator.SOrdersManager;
import org.pimslims.utils.sequenator.SequencePrepSheet;

/**
 * Output samples & primers names to the plateSetup excel template. Load completed with volumes plateSetup
 * excel template and create a plate experiment out of it.
 * 
 * @author pvt43
 * 
 */
/**
 * @author Petr Troshin
 * 
 */
public class FixPlateLayout extends PIMSServlet {

    public static final int MAX_UPLOAD = 80 * 1024 * 1024;

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Input/Output Plate setup template, sequencing plate";
    }

    /**
     * PlateSetup.doGet
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse) 1. Get the list of orders
     * 
     *      2. Generate a spreadsheet using plateSetUp.xls
     * 
     *      3. Send the file to the client
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // Default
        //get "orderXX" 
        //"sNum" value will be equal to the order of "orders" on plate

        WritableVersion rw = null;
        try {
            rw = this.getWritableVersion(request, response);

            final boolean isSeqAdmin = AdminHome.isSequecingAdmin(request);
            if (!isSeqAdmin) {
                request.setAttribute("message",
                    "You are not allowed to perform this action. Only sequencing-administartor can do this!");
                final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
                dispatcher.forward(request, response);
                this.commit(request, response, rw);
                return;
            }

            final HttpSession session = request.getSession();
            final PlatePlanner planner = (PlatePlanner) session.getAttribute(PlanSOPlate.plannerKey);
            // Warn if the session is expired!
            if (planner == null) {
                request.setAttribute("message", "Sorry your session has expired. Please start again.");
                final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
                dispatcher.forward(request, response);
                this.commit(request, response, rw);
                return;
            }

            final ArrayList<Experiment> experiments = planner.getLayedSampleList();
            //Change of these experiments will not be permitted as version in them was closed
            // have to retrieved all experiments with a current version
            Utils.reobtainCollection(experiments, rw);

            // Make control based on the reference control
            // Please note that the version with which these experiments were obtained has been closed, 
            // therefore can only get basic properties from these, 
            // so these experiments needs to be retrieved from the DB with fresh version to perform deep copying.  

            final Experiment rControl = planner.getRcontrol();
            final Experiment iControl = planner.getIcontrol();
            for (int i = 0; i < new ArrayList(experiments).size(); i++) {
                final Experiment exp = experiments.get(i);
                if (exp == null) {
                    continue;
                }
                // Replace reference controls with their copies
                if (rControl != null && exp.get_Hook().equals(rControl.get_Hook())) {
                    final Experiment rcontrolCopy = this.makeControl(rw, rControl.get_Hook());
                    experiments.set(i, rcontrolCopy);
                }
                if (iControl != null && exp.get_Hook().equals(iControl.get_Hook())) {
                    final Experiment icontrolCopy = this.makeControl(rw, iControl.get_Hook());
                    experiments.set(i, icontrolCopy);
                }
            }

            // Shrink the array to its actual size
            final SequencePrepSheet spsmanual = new SequencePrepSheet(SequencePrepSheet.TemplateType.manual);
            final SequencePrepSheet spsRobot = new SequencePrepSheet(SequencePrepSheet.TemplateType.manual);

            final SOrdersManager som = new SOrdersManager(experiments, rw);

            if (som.getSize() > 96) {
                request
                    .setAttribute(
                        "message",
                        "The order contains more than 96 samples. Cannot proceed! Please make sure that you did not select any controls if "
                            + " you are running a plate with 96 samples in it! Orders are: "
                            + planner.getLayedOrders());
                final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
                dispatcher.forward(request, response);
                this.commit(request, response, rw);
                return;
            }

            final HolderType holderType =
                rw.findFirst(HolderType.class, AbstractHolderType.PROP_NAME, "96 deep well");
            assert null != holderType : "No '96 deep well' holder type! ";

            // Keep run number the same for both as at the end only one will be used
            final String runNumber = SequencePrepSheet.getNextRunNumber(rw);
            final byte[] sheet =
                spsmanual
                    .writeShortSetupSheet(experiments, holderType, runNumber, new ManualTemplateHelper());
            final byte[] robotSheet =
                spsRobot.writeShortSetupSheet(experiments, holderType, runNumber,
                    new RoboticsTemplateHelper());
            //File file = null;
            ExperimentGroup expGroup = null;

            // Update the processing status of the orders
            // Assign a reference number for the output sample
            expGroup = som.setLayoutCompletedStep(experiments, rw, runNumber);

            //file =
            FixPlateLayout.recordExcelFile(rw, sheet, "ManualSequencingSetup", expGroup,
                "Sequencing Setup Template for Manual Processing");

            //file =
            FixPlateLayout.recordExcelFile(rw, robotSheet, "RobotSequencingSetup", expGroup,
                "Sequencing Setup Template for Robot");

            request.setAttribute("expGroup", expGroup);

            final RequestDispatcher rd =
                request.getRequestDispatcher("/JSP/sequencing/PlateSetupInOutput.jsp");
            rd.forward(request, response);

            this.commit(request, response, rw);
            // Remove PlatePlanner from the session as its no longer needed 
            session.removeAttribute(PlanSOPlate.plannerKey);

        } catch (final ConstraintException e) {
            throw new RuntimeException(e);
        } catch (final AccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (!rw.isCompleted()) {
                rw.abort();
            }
        }

    }

    Experiment makeControl(final WritableVersion rw, final String expHook) throws ConstraintException,
        AccessException {
        Experiment expCopy = null;
        if (Util.isHookValid(expHook)) {
            final Experiment exp = rw.get(expHook);
            expCopy = ExperimentCopier.duplicate(exp, rw);
            expCopy.setStatus(SOrdersManager.ExpStatus.To_be_run.toString());
        }
        return expCopy;
    }

    public static File uploadFile(final HttpServletRequest request, final HttpServletResponse response,
        final WritableVersion version, final LabBookEntry modelObject) throws ServletException, IOException {

        synchronized (request) {

            File file = null;
            if (!ServletFileUpload.isMultipartContent(request)) {
                throw new ServletException("No file found in submission");
            }

            try {
                // Create a new file upload handler
                final DiskFileItemFactory factory = new DiskFileItemFactory();
                // TODO factory.setRepository(repository);

                factory.setSizeThreshold(FixPlateLayout.MAX_UPLOAD * 10);
                final ServletFileUpload sfu = new ServletFileUpload(factory);
                sfu.setSizeMax(FixPlateLayout.MAX_UPLOAD); // Maximum upload size
                /*
                 this.writeHead(request, response, "FILE:");
                 writer.print("The maximum size is: ");
                 */

                // Parse the request
                final List<FileItem> items = sfu.parseRequest(request);

                // Process the uploaded items
                // At present, our form just has one item,
                // but this code is capable of handling many
                final java.util.Iterator<FileItem> iter = items.iterator();
                //String fileDesc = "";
                // Process only form fields to ensure that progressId is set prior to the file downloading
                while (iter.hasNext()) {
                    final FileItem item = iter.next();
                    if (item.isFormField()) {
                        // caller should set an id under which the progressListener will be provided 
                        /*if (item.getFieldName().equals("fileDescription")) {
                            fileDesc = item.getString();
                        } */
                        // This is needed to current tab value recognition
                        if (item.getFieldName().equals("_tab")) {
                            final String curTab = item.getString();
                            if (!Util.isEmpty(curTab)) {
                                request.getSession().setAttribute("_tab", curTab);
                                // System.out.println("Set tab in Multi:" + curTab);
                            }
                        }
                        continue;
                    }

                    if (0 == item.getName().trim().length()) {

                        request.setAttribute("message", "File not uploaded. " + item.getName()
                            + "A file name must be specified");
                        final RequestDispatcher dispatcher =
                            request.getRequestDispatcher("/JSP/OneMessage.jsp");
                        dispatcher.forward(request, response);
                        return null;
                    }
                    if (0 == item.getSize()) {
                        request.setAttribute("message", "File not uploaded. " + item.getName()
                            + " is length 0");
                        final RequestDispatcher dispatcher =
                            request.getRequestDispatcher("/JSP/OneMessage.jsp");
                        dispatcher.forward(request, response);
                        return null;
                    }
                    final java.io.InputStream uploadedStream = item.getInputStream();
                    file =
                        FixPlateLayout.recordFile(version, uploadedStream, modelObject, item.getName(),
                            item.getContentType());
                    uploadedStream.close();
                    //TODO item.delete();
                }

            } catch (final org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException e) {
                request.setAttribute("message", "File is too big" + "The maximum size is: "
                    + FixPlateLayout.MAX_UPLOAD);
                final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
                dispatcher.forward(request, response);
                return null;
            } catch (final FileUploadException e) {
                throw new ServletException(e);
            } catch (final IOException e) {
                throw new ServletException(e);
            } catch (final ConstraintException e) {
                throw new ServletException(e);
            } catch (final AccessException e) {
                throw new ServletException(e);
            }

            return file;
        }
    }

    private static File recordFile(final WritableVersion version, final InputStream uploadedStream,
        final LabBookEntry modelObject, final String fileName, final String contentType)
        throws AccessException, ConstraintException, IOException {

        assert version != null;
        assert uploadedStream != null;
        assert !Util.isEmpty(fileName);
        assert !Util.isEmpty(contentType);

        System.out.println("Writing:" + fileName);

        File file = null;
        if (modelObject != null) {
            file = version.createFile(uploadedStream, fileName, modelObject);
            file.setTitle(fileName); // preserve original item name
            file.setDescription(fileName); // preserve original item name
            //file = FileImpl.createFile(version, uploadedStream, fileName, modelObject);
        } else {
            // This is a temporary object nor title neither extension can be set  
            file = FileImpl.createFile(version, uploadedStream, fileName);
        }

        assert file != null;

        file.setMimeType(contentType);
        // BUt reset mimetype for well known types
        FixPlateLayout.setMimeType(file, fileName);
        return file;
    }

    public static void setMimeType(final File file, final String fileName) throws ConstraintException {
        if (Util.isEmpty(fileName) || file == null) {
            return;
        }
        final String fname = fileName.toLowerCase();
        if (fname.endsWith(".txt")) {
            file.setExtension(".txt");
            file.setMimeType("text/plain");
        }
        // Only this combination works!
        if (fname.endsWith(".seq")) {
            file.setExtension(".seq");
            file.setMimeType("text/plane");
        }
        if (fname.endsWith(".ab1")) {
            file.setExtension(".ab1");
            file.setMimeType("application/x-dna");
        }
        if (fname.endsWith(".scf")) {
            file.setExtension(".scf");
            // This one does not actually have a known mimetype 
            // I have to invent my own to enable users to permanently link 
            // viewing application with these files. 
            file.setMimeType("application/sc-dna");
        }
        if (fname.endsWith(".zip")) {
            file.setExtension(".zip");
            file.setMimeType("application/zip");
        }

    }

    public static File recordExcelFile(final WritableVersion rw, final byte[] content, final String fileName,
        final LabBookEntry mObject, final String description) throws AccessException, ConstraintException,
        IOException {
        assert !Util.isEmpty(fileName);
        assert mObject != null;
        assert rw != null;
        final File file = rw.createFile(content, fileName + ".xls", mObject);
        file.setTitle(fileName + ".xls");
        if (!Util.isEmpty(description)) {
            file.setDescription(description);
        }
        file.setMimeType(SequencePrepSheet.MSExcel_MIMETYPE);
        return file;
    }

    public static File uploadFile(final HttpServletRequest request, final HttpServletResponse response,
        final WritableVersion version, final String fileName) throws ServletException, IOException {

        synchronized (request) {

            File file = null;
            if (!ServletFileUpload.isMultipartContent(request)) {
                throw new ServletException("No file found in submission");
            }

            try {
                // Create a new file upload handler
                final DiskFileItemFactory factory = new DiskFileItemFactory();
                // TODO factory.setRepository(repository);

                factory.setSizeThreshold(FixPlateLayout.MAX_UPLOAD * 10);
                final ServletFileUpload sfu = new ServletFileUpload(factory);
                sfu.setSizeMax(FixPlateLayout.MAX_UPLOAD); // Maximum upload size
                /*
                 this.writeHead(request, response, "FILE:");
                 writer.print("The maximum size is: ");
                 */

                // Parse the request
                final List<FileItem> items = sfu.parseRequest(request);

                // Process the uploaded items
                // At present, our form just has one item,
                // but this code is capable of handling many
                final java.util.Iterator<FileItem> iter = items.iterator();
                //String fileDesc = "";
                // Process only form fields to ensure that progressId is set prior to the file downloading
                while (iter.hasNext()) {
                    final FileItem item = iter.next();
                    if (item.isFormField()) {
                        // caller should set an id under which the progressListener will be provided 
                        if (item.getFieldName().equals("fileDescription")) {
                            //fileDesc = item.getString();
                        }
                        // This is needed to current tab value recognition
                        if (item.getFieldName().equals("_tab")) {
                            final String curTab = item.getString();
                            if (!Util.isEmpty(curTab)) {
                                request.getSession().setAttribute("_tab", curTab);
                                // System.out.println("Set tab in Multi:" + curTab);
                            }
                        }
                        continue;
                    }

                    if (0 == item.getName().trim().length()) {

                        request.setAttribute("message", "File not uploaded. " + item.getName()
                            + "A file name must be specified");
                        final RequestDispatcher dispatcher =
                            request.getRequestDispatcher("/JSP/OneMessage.jsp");
                        dispatcher.forward(request, response);
                        return null;
                    }
                    if (0 == item.getSize()) {
                        request.setAttribute("message", "File not uploaded. " + item.getName()
                            + " is length 0");
                        final RequestDispatcher dispatcher =
                            request.getRequestDispatcher("/JSP/OneMessage.jsp");
                        dispatcher.forward(request, response);
                        return null;
                    }

                    final java.io.InputStream uploadedStream = item.getInputStream();
                    file =
                        FixPlateLayout.recordFile(version, uploadedStream, null, fileName,
                            item.getContentType());

                    uploadedStream.close();
                    //TODO item.delete();
                }

            } catch (final org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException e) {
                request.setAttribute("message", "File is too big" + "The maximum size is: "
                    + FixPlateLayout.MAX_UPLOAD);
                final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
                dispatcher.forward(request, response);
                return null;
            } catch (final FileUploadException e) {
                throw new ServletException(e);
            } catch (final IOException e) {
                throw new ServletException(e);
            } catch (final ConstraintException e) {
                throw new ServletException(e);
            } catch (final AccessException e) {
                throw new ServletException(e);
            }

            return file;
        }
    }

    public static int getOrderNumber(final String orderId) {
        return Integer.parseInt(orderId.substring("orderId".length()).trim());
    }

}