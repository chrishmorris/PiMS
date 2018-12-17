package org.pimslims.servlet.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.pimslims.csv.CsvParser;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.servlet.ListFiles;
import org.pimslims.servlet.PIMSServlet;

import uk.ac.ox.oppf.pims.inserter.Inserter;

public class OpticImporter extends PIMSServlet {

    @Override
    public String getServletInfo() {
        return "Import Targets and Constructs from Optic";
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

        System.out.println("org.pimslims.servlet.importer.OpticImporter.doGet");

        /*
         * System.out.println("org.pimslims.servlet.importer.OpticImporter.doGet");
         * for (Iterator iter =
         * request.getParameterMap().entrySet().iterator(); iter.hasNext();) { Map.Entry entry =
         * (Map.Entry)iter.next(); String key = (String)entry.getKey(); String[] values =
         * (String[])entry.getValue(); for (int i = 0; i < values.length; i++) System.out.println("Parameter
         * ["+key+","+values[i]+"]"); }
         */

        if (!this.checkStarted(request, response)) {
            return;
        }

        // Get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return; // error message has already been sent
        }

        // always use a transaction in a try/catch block
        try {
            request.setAttribute("accessObjects", PIMSServlet.getPossibleCreateOwners(version));
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/oppf/OpticImport.jsp");
            dispatcher.forward(request, response);
            version.commit();
        } catch (final AbortedException ex) {
            throw new ServletException(ex);
        } catch (final ConstraintException ex) {
            throw new ServletException(ex);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

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

        System.out.println("org.pimslims.servlet.importer.OpticImporter.doPost");
        if (!this.checkStarted(request, response)) {
            return;
        }

        final PrintWriter writer = response.getWriter();
        final Map<String, String> parms = new HashMap<String, String>();

        final Collection<String> targetList = new ArrayList<String>();
        //final Collection<String> constructList = new ArrayList<String>();

        final DiskFileUpload upload = new DiskFileUpload();
        upload.setSizeMax(ListFiles.MAX_UPLOAD);
        try {
            final java.util.Collection<FileItem> items = upload.parseRequest(request);

            // Process the uploaded items
            for (final FileItem item : items) {

                if (item.isFormField()) {
                    System.out.println("parms [" + item.getFieldName() + "," + item.getString() + "]");
                    parms.put(item.getFieldName(), item.getString());
                    continue;
                }
                if (0 == item.getName().trim().length()) {
                    continue;
                }

                if (0 == item.getSize()) {
                    this.writeErrorHead(request, response, "File not uploaded",
                        HttpServletResponse.SC_BAD_REQUEST);
                    writer.print(item.getName() + " is length 0");
                    PIMSServlet.writeFoot(writer, request);
                    return;
                }

                targetList
                    .addAll(this.getTargetsFromSpreadsheet(new InputStreamReader(item.getInputStream())));
                //TODO item.delete()
            }
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final FileUploadException e) {
            throw new ServletException(e);
        }

        final String targets = parms.get("opticElement");
        if (null != targets) {
            final StringTokenizer st = new StringTokenizer(targets, ",");
            while (st.hasMoreTokens()) {
                targetList.add(st.nextToken());
            }
        }

        //for testing
        for (final Iterator<String> iter = targetList.iterator(); iter.hasNext();) {
            System.out.println("targetList [" + iter.next() + "]");
        }
        //targetList.clear();

        final WritableVersion version = this.getWritableVersion(request, response);
        if (version == null) {
            return;
        }

        try {
            final LabNotebook project = version.get(parms.get(PIMSServlet.LAB_NOTEBOOK_ID));
            assert project != null : "project should not be null";
            version.setDefaultOwner(project);

            final Inserter inserter = OpticImporter.getInserter();
            if (!targetList.isEmpty()) {
                inserter.insertTargets(version, targetList);
            }

            //if (!constructList.isEmpty()) {
            //    inserter.insertConstructs(version, constructList);
            //}

            final RequestDispatcher dispatcher = request.getRequestDispatcher("/");
            dispatcher.forward(request, response);
            version.commit();

        } catch (final AbortedException e) {
            this.ReportBadRequest(request, response, "Session aborted", e);
            return;
        } catch (final ConstraintException e) {
            this.ReportBadRequest(request, response, "Invalid value", e);
            return;
        } catch (final ClassNotFoundException e) {
            this.ReportBadRequest(request, response, Inserter.class.getName() + "has Database Problem ", e);
            return;
        } catch (final SQLException e) {
            this.ReportBadRequest(request, response, Inserter.class.getName() + "has Database Problem ", e);
            return;
        } catch (final AccessException e) {
            this.ReportBadRequest(request, response, Inserter.class.getName() + "has Access Problem ", e);
            return;
        } catch (final IllegalAlphabetException e) {
            this.ReportBadRequest(request, response, Inserter.class.getName() + "has Problem ", e);
            return;
        } catch (final IllegalSymbolException e) {
            this.ReportBadRequest(request, response, Inserter.class.getName() + "has Problem ", e);
            return;
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public static Inserter getInserter() {
        try {
            final File jar =
                org.pimslims.properties.PropertyGetter.getFileProperty("log4j", "extra-lib/log4j-1.2.16.jar");
            final URL url = jar.toURI().toURL();
            final URL[] urls = new URL[] { url };
            assert jar.exists();
            final ClassLoader cl = new URLClassLoader(urls, null);
            cl.loadClass("org.apache.log4j.Logger"); //TODO remove
            final Class clazz = cl.loadClass(Inserter.class.getName());
            return (Inserter) clazz.newInstance();
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (final InstantiationException e) {
            throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private Collection<String> getTargetsFromSpreadsheet(final Reader reader) throws IOException,
        ConstraintException {

        final CsvParser parser = new CsvParser(reader);
        final Collection<String> labels = new ArrayList<String>(Arrays.asList(parser.getLabels()));
        final Collection<String> targets = new HashSet<String>();

        // process standard column headers
        String wellName = null;
        if (labels.contains("OPTIC id")) {
            wellName = "OPTIC id";
            labels.remove("OPTIC id");
        } else if (labels.contains("optic id")) {
            wellName = "optic id";
            labels.remove("optic id");
        } else {
            throw new IllegalArgumentException("CSV file must contain 'OPTIC id' column");
            // LATER accept "row" and "column"
        }

        // now iterate through the file
        while (parser.getLine() != null) {
            final String target = parser.getValueByLabel(wellName);
            if (this.validString(target) && this.notDuplicate(targets, target)) {
                targets.add(target);
            }
        }

        return targets;
    }

    private boolean validString(final String s) {
        if (null != s && s.trim().length() > 0) {
            return true;
        }
        return false;
    }

    /**
     * check that the string target is not already in the collection targets
     * 
     * @param targets
     * @param target
     * @return
     */
    private boolean notDuplicate(final Collection<String> targets, final String target) {
        for (final String entry : targets) {
            if (entry.equals(target)) {
                return false;
            }
        }
        return true;
    }
}
